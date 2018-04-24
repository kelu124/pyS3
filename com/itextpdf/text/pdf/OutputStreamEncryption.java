package com.itextpdf.text.pdf;

import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.pdf.crypto.AESCipher;
import com.itextpdf.text.pdf.crypto.ARCFOUREncryption;
import com.itextpdf.text.pdf.crypto.IVGenerator;
import java.io.IOException;
import java.io.OutputStream;

public class OutputStreamEncryption extends OutputStream {
    private static final int AES_128 = 4;
    private static final int AES_256 = 5;
    private boolean aes;
    protected ARCFOUREncryption arcfour;
    protected AESCipher cipher;
    private boolean finished;
    protected OutputStream out;
    private byte[] sb;

    public OutputStreamEncryption(OutputStream out, byte[] key, int off, int len, int revision) {
        boolean z = false;
        this.sb = new byte[1];
        try {
            this.out = out;
            if (revision == 4 || revision == 5) {
                z = true;
            }
            this.aes = z;
            if (this.aes) {
                byte[] iv = IVGenerator.getIV();
                byte[] nkey = new byte[len];
                System.arraycopy(key, off, nkey, 0, len);
                this.cipher = new AESCipher(true, nkey, iv);
                write(iv);
                return;
            }
            this.arcfour = new ARCFOUREncryption();
            this.arcfour.prepareARCFOURKey(key, off, len);
        } catch (Exception ex) {
            throw new ExceptionConverter(ex);
        }
    }

    public OutputStreamEncryption(OutputStream out, byte[] key, int revision) {
        this(out, key, 0, key.length, revision);
    }

    public void close() throws IOException {
        finish();
        this.out.close();
    }

    public void flush() throws IOException {
        this.out.flush();
    }

    public void write(byte[] b) throws IOException {
        write(b, 0, b.length);
    }

    public void write(int b) throws IOException {
        this.sb[0] = (byte) b;
        write(this.sb, 0, 1);
    }

    public void write(byte[] b, int off, int len) throws IOException {
        byte[] b2;
        if (this.aes) {
            b2 = this.cipher.update(b, off, len);
            if (b2 != null && b2.length != 0) {
                this.out.write(b2, 0, b2.length);
                return;
            }
            return;
        }
        b2 = new byte[Math.min(len, 4192)];
        while (len > 0) {
            int sz = Math.min(len, b2.length);
            this.arcfour.encryptARCFOUR(b, off, sz, b2, 0);
            this.out.write(b2, 0, sz);
            len -= sz;
            off += sz;
        }
    }

    public void finish() throws IOException {
        if (!this.finished) {
            this.finished = true;
            if (this.aes) {
                try {
                    byte[] b = this.cipher.doFinal();
                    this.out.write(b, 0, b.length);
                } catch (Exception ex) {
                    throw new ExceptionConverter(ex);
                }
            }
        }
    }
}
