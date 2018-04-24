package com.itextpdf.text.pdf.codec;

import java.io.PrintStream;

public class LZWStringTable {
    private static final short HASHSIZE = (short) 9973;
    private static final short HASHSTEP = (short) 2039;
    private static final short HASH_FREE = (short) -1;
    private static final int MAXBITS = 12;
    private static final int MAXSTR = 4096;
    private static final short NEXT_FIRST = (short) -1;
    private static final int RES_CODES = 2;
    short numStrings_;
    byte[] strChr_ = new byte[4096];
    short[] strHsh_ = new short[9973];
    int[] strLen_ = new int[4096];
    short[] strNxt_ = new short[4096];

    public int AddCharString(short index, byte b) {
        if (this.numStrings_ >= (short) 4096) {
            return 65535;
        }
        int hshidx = Hash(index, b);
        while (this.strHsh_[hshidx] != (short) -1) {
            hshidx = (hshidx + 2039) % 9973;
        }
        this.strHsh_[hshidx] = this.numStrings_;
        this.strChr_[this.numStrings_] = b;
        if (index == (short) -1) {
            this.strNxt_[this.numStrings_] = (short) -1;
            this.strLen_[this.numStrings_] = 1;
        } else {
            this.strNxt_[this.numStrings_] = index;
            this.strLen_[this.numStrings_] = this.strLen_[index] + 1;
        }
        int i = this.numStrings_;
        this.numStrings_ = (short) (i + 1);
        return i;
    }

    public short FindCharString(short index, byte b) {
        if (index == (short) -1) {
            return (short) (b & 255);
        }
        int hshidx = Hash(index, b);
        while (true) {
            int nxtidx = this.strHsh_[hshidx];
            if (nxtidx == -1) {
                return (short) -1;
            }
            if (this.strNxt_[nxtidx] == index && this.strChr_[nxtidx] == b) {
                return (short) nxtidx;
            }
            hshidx = (hshidx + 2039) % 9973;
        }
    }

    public void ClearTable(int codesize) {
        int q;
        this.numStrings_ = (short) 0;
        for (q = 0; q < 9973; q++) {
            this.strHsh_[q] = (short) -1;
        }
        int w = (1 << codesize) + 2;
        for (q = 0; q < w; q++) {
            AddCharString((short) -1, (byte) q);
        }
    }

    public static int Hash(short index, byte lastbyte) {
        return ((((short) (lastbyte << 8)) ^ index) & 65535) % 9973;
    }

    public int expandCode(byte[] buf, int offset, short code, int skipHead) {
        if (offset == -2 && skipHead == 1) {
            skipHead = 0;
        }
        if (code == (short) -1 || skipHead == this.strLen_[code]) {
            return 0;
        }
        int expandLen;
        int codeLen = this.strLen_[code] - skipHead;
        int bufSpace = buf.length - offset;
        if (bufSpace > codeLen) {
            expandLen = codeLen;
        } else {
            expandLen = bufSpace;
        }
        int skipTail = codeLen - expandLen;
        int idx = offset + expandLen;
        while (idx > offset && code != (short) -1) {
            skipTail--;
            if (skipTail < 0) {
                idx--;
                buf[idx] = this.strChr_[code];
            }
            code = this.strNxt_[code];
        }
        if (codeLen > expandLen) {
            return -expandLen;
        }
        return expandLen;
    }

    public void dump(PrintStream out) {
        for (short i = (short) 258; i < this.numStrings_; i++) {
            out.println(" strNxt_[" + i + "] = " + this.strNxt_[i] + " strChr_ " + Integer.toHexString(this.strChr_[i] & 255) + " strLen_ " + Integer.toHexString(this.strLen_[i]));
        }
    }
}
