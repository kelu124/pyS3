package com.itextpdf.text.pdf.crypto;

import org.spongycastle.crypto.engines.AESFastEngine;
import org.spongycastle.crypto.modes.CBCBlockCipher;
import org.spongycastle.crypto.paddings.PaddedBufferedBlockCipher;
import org.spongycastle.crypto.params.KeyParameter;
import org.spongycastle.crypto.params.ParametersWithIV;

public class AESCipher {
    private PaddedBufferedBlockCipher bp = new PaddedBufferedBlockCipher(new CBCBlockCipher(new AESFastEngine()));

    public AESCipher(boolean forEncryption, byte[] key, byte[] iv) {
        this.bp.init(forEncryption, new ParametersWithIV(new KeyParameter(key), iv));
    }

    public byte[] update(byte[] inp, int inpOff, int inpLen) {
        int neededLen = this.bp.getUpdateOutputSize(inpLen);
        byte[] outp = null;
        if (neededLen > 0) {
            outp = new byte[neededLen];
        }
        this.bp.processBytes(inp, inpOff, inpLen, outp, 0);
        return outp;
    }

    public byte[] doFinal() {
        byte[] outp = new byte[this.bp.getOutputSize(0)];
        try {
            int n = this.bp.doFinal(outp, 0);
            if (n == outp.length) {
                return outp;
            }
            byte[] outp2 = new byte[n];
            System.arraycopy(outp, 0, outp2, 0, n);
            return outp2;
        } catch (Exception e) {
            return outp;
        }
    }
}
