package com.itextpdf.text.pdf.crypto;

public class ARCFOUREncryption {
    private byte[] state = new byte[256];
    private int f155x;
    private int f156y;

    public void prepareARCFOURKey(byte[] key) {
        prepareARCFOURKey(key, 0, key.length);
    }

    public void prepareARCFOURKey(byte[] key, int off, int len) {
        int k;
        int index1 = 0;
        int index2 = 0;
        for (k = 0; k < 256; k++) {
            this.state[k] = (byte) k;
        }
        this.f155x = 0;
        this.f156y = 0;
        for (k = 0; k < 256; k++) {
            index2 = ((key[index1 + off] + this.state[k]) + index2) & 255;
            byte tmp = this.state[k];
            this.state[k] = this.state[index2];
            this.state[index2] = tmp;
            index1 = (index1 + 1) % len;
        }
    }

    public void encryptARCFOUR(byte[] dataIn, int off, int len, byte[] dataOut, int offOut) {
        int length = len + off;
        for (int k = off; k < length; k++) {
            this.f155x = (this.f155x + 1) & 255;
            this.f156y = (this.state[this.f155x] + this.f156y) & 255;
            byte tmp = this.state[this.f155x];
            this.state[this.f155x] = this.state[this.f156y];
            this.state[this.f156y] = tmp;
            dataOut[(k - off) + offOut] = (byte) (dataIn[k] ^ this.state[(this.state[this.f155x] + this.state[this.f156y]) & 255]);
        }
    }

    public void encryptARCFOUR(byte[] data, int off, int len) {
        encryptARCFOUR(data, off, len, data, off);
    }

    public void encryptARCFOUR(byte[] dataIn, byte[] dataOut) {
        encryptARCFOUR(dataIn, 0, dataIn.length, dataOut, 0);
    }

    public void encryptARCFOUR(byte[] data) {
        encryptARCFOUR(data, 0, data.length, data, 0);
    }
}
