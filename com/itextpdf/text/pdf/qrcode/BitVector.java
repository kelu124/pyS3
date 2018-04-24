package com.itextpdf.text.pdf.qrcode;

public final class BitVector {
    private static final int DEFAULT_SIZE_IN_BYTES = 32;
    private byte[] array = new byte[32];
    private int sizeInBits = 0;

    public int at(int index) {
        if (index >= 0 && index < this.sizeInBits) {
            return ((this.array[index >> 3] & 255) >> (7 - (index & 7))) & 1;
        }
        throw new IllegalArgumentException("Bad index: " + index);
    }

    public int size() {
        return this.sizeInBits;
    }

    public int sizeInBytes() {
        return (this.sizeInBits + 7) >> 3;
    }

    public void appendBit(int bit) {
        if (bit == 0 || bit == 1) {
            int numBitsInLastByte = this.sizeInBits & 7;
            if (numBitsInLastByte == 0) {
                appendByte(0);
                this.sizeInBits -= 8;
            }
            byte[] bArr = this.array;
            int i = this.sizeInBits >> 3;
            bArr[i] = (byte) (bArr[i] | (bit << (7 - numBitsInLastByte)));
            this.sizeInBits++;
            return;
        }
        throw new IllegalArgumentException("Bad bit");
    }

    public void appendBits(int value, int numBits) {
        if (numBits < 0 || numBits > 32) {
            throw new IllegalArgumentException("Num bits must be between 0 and 32");
        }
        int numBitsLeft = numBits;
        while (numBitsLeft > 0) {
            if ((this.sizeInBits & 7) != 0 || numBitsLeft < 8) {
                appendBit((value >> (numBitsLeft - 1)) & 1);
                numBitsLeft--;
            } else {
                appendByte((value >> (numBitsLeft - 8)) & 255);
                numBitsLeft -= 8;
            }
        }
    }

    public void appendBitVector(BitVector bits) {
        int size = bits.size();
        for (int i = 0; i < size; i++) {
            appendBit(bits.at(i));
        }
    }

    public void xor(BitVector other) {
        if (this.sizeInBits != other.size()) {
            throw new IllegalArgumentException("BitVector sizes don't match");
        }
        int sizeInBytes = (this.sizeInBits + 7) >> 3;
        for (int i = 0; i < sizeInBytes; i++) {
            byte[] bArr = this.array;
            bArr[i] = (byte) (bArr[i] ^ other.array[i]);
        }
    }

    public String toString() {
        StringBuffer result = new StringBuffer(this.sizeInBits);
        for (int i = 0; i < this.sizeInBits; i++) {
            if (at(i) == 0) {
                result.append('0');
            } else if (at(i) == 1) {
                result.append('1');
            } else {
                throw new IllegalArgumentException("Byte isn't 0 or 1");
            }
        }
        return result.toString();
    }

    public byte[] getArray() {
        return this.array;
    }

    private void appendByte(int value) {
        if ((this.sizeInBits >> 3) == this.array.length) {
            byte[] newArray = new byte[(this.array.length << 1)];
            System.arraycopy(this.array, 0, newArray, 0, this.array.length);
            this.array = newArray;
        }
        this.array[this.sizeInBits >> 3] = (byte) value;
        this.sizeInBits += 8;
    }
}
