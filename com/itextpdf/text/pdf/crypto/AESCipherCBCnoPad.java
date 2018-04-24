package com.itextpdf.text.pdf.crypto;

import org.spongycastle.crypto.BlockCipher;
import org.spongycastle.crypto.engines.AESFastEngine;
import org.spongycastle.crypto.modes.CBCBlockCipher;
import org.spongycastle.crypto.params.KeyParameter;

public class AESCipherCBCnoPad {
    private BlockCipher cbc = new CBCBlockCipher(new AESFastEngine());

    public AESCipherCBCnoPad(boolean forEncryption, byte[] key) {
        this.cbc.init(forEncryption, new KeyParameter(key));
    }

    public byte[] processBlock(byte[] inp, int inpOff, int inpLen) {
        if (inpLen % this.cbc.getBlockSize() != 0) {
            throw new IllegalArgumentException("Not multiple of block: " + inpLen);
        }
        byte[] outp = new byte[inpLen];
        int baseOffset = 0;
        while (inpLen > 0) {
            this.cbc.processBlock(inp, inpOff, outp, baseOffset);
            inpLen -= this.cbc.getBlockSize();
            baseOffset += this.cbc.getBlockSize();
            inpOff += this.cbc.getBlockSize();
        }
        return outp;
    }
}
