package com.itextpdf.text.pdf;

import com.itextpdf.text.pdf.crypto.AESCipher;
import com.itextpdf.text.pdf.crypto.ARCFOUREncryption;

public class StandardDecryption {
    private static final int AES_128 = 4;
    private static final int AES_256 = 5;
    private boolean aes;
    protected ARCFOUREncryption arcfour;
    protected AESCipher cipher;
    private boolean initiated;
    private byte[] iv = new byte[16];
    private int ivptr;
    private byte[] key;

    public StandardDecryption(byte[] key, int off, int len, int revision) {
        boolean z = revision == 4 || revision == 5;
        this.aes = z;
        if (this.aes) {
            this.key = new byte[len];
            System.arraycopy(key, off, this.key, 0, len);
            return;
        }
        this.arcfour = new ARCFOUREncryption();
        this.arcfour.prepareARCFOURKey(key, off, len);
    }

    public byte[] update(byte[] b, int off, int len) {
        if (!this.aes) {
            byte[] b2 = new byte[len];
            this.arcfour.encryptARCFOUR(b, off, len, b2, 0);
            return b2;
        } else if (this.initiated) {
            return this.cipher.update(b, off, len);
        } else {
            int left = Math.min(this.iv.length - this.ivptr, len);
            System.arraycopy(b, off, this.iv, this.ivptr, left);
            off += left;
            len -= left;
            this.ivptr += left;
            if (this.ivptr == this.iv.length) {
                this.cipher = new AESCipher(false, this.key, this.iv);
                this.initiated = true;
                if (len > 0) {
                    return this.cipher.update(b, off, len);
                }
            }
            return null;
        }
    }

    public byte[] finish() {
        if (this.aes) {
            return this.cipher.doFinal();
        }
        return null;
    }
}
