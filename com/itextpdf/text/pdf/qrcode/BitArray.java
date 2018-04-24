package com.itextpdf.text.pdf.qrcode;

public final class BitArray {
    public int[] bits;
    public final int size;

    public BitArray(int size) {
        if (size < 1) {
            throw new IllegalArgumentException("size must be at least 1");
        }
        this.size = size;
        this.bits = makeArray(size);
    }

    public int getSize() {
        return this.size;
    }

    public boolean get(int i) {
        return (this.bits[i >> 5] & (1 << (i & 31))) != 0;
    }

    public void set(int i) {
        int[] iArr = this.bits;
        int i2 = i >> 5;
        iArr[i2] = iArr[i2] | (1 << (i & 31));
    }

    public void flip(int i) {
        int[] iArr = this.bits;
        int i2 = i >> 5;
        iArr[i2] = iArr[i2] ^ (1 << (i & 31));
    }

    public void setBulk(int i, int newBits) {
        this.bits[i >> 5] = newBits;
    }

    public void clear() {
        int max = this.bits.length;
        for (int i = 0; i < max; i++) {
            this.bits[i] = 0;
        }
    }

    public boolean isRange(int start, int end, boolean value) {
        if (end < start) {
            throw new IllegalArgumentException();
        } else if (end == start) {
            return true;
        } else {
            end--;
            int firstInt = start >> 5;
            int lastInt = end >> 5;
            int i = firstInt;
            while (i <= lastInt) {
                int mask;
                int firstBit = i > firstInt ? 0 : start & 31;
                int lastBit = i < lastInt ? 31 : end & 31;
                if (firstBit == 0 && lastBit == 31) {
                    mask = -1;
                } else {
                    mask = 0;
                    for (int j = firstBit; j <= lastBit; j++) {
                        mask |= 1 << j;
                    }
                }
                int i2 = this.bits[i] & mask;
                if (!value) {
                    mask = 0;
                }
                if (i2 != mask) {
                    return false;
                }
                i++;
            }
            return true;
        }
    }

    public int[] getBitArray() {
        return this.bits;
    }

    public void reverse() {
        int[] newBits = new int[this.bits.length];
        int size = this.size;
        for (int i = 0; i < size; i++) {
            if (get((size - i) - 1)) {
                int i2 = i >> 5;
                newBits[i2] = newBits[i2] | (1 << (i & 31));
            }
        }
        this.bits = newBits;
    }

    private static int[] makeArray(int size) {
        int arraySize = size >> 5;
        if ((size & 31) != 0) {
            arraySize++;
        }
        return new int[arraySize];
    }

    public String toString() {
        StringBuffer result = new StringBuffer(this.size);
        for (int i = 0; i < this.size; i++) {
            if ((i & 7) == 0) {
                result.append(' ');
            }
            result.append(get(i) ? 'X' : '.');
        }
        return result.toString();
    }
}
