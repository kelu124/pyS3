package com.itextpdf.text.pdf.hyphenation;

import java.io.Serializable;

public class ByteVector implements Serializable {
    private static final int DEFAULT_BLOCK_SIZE = 2048;
    private static final long serialVersionUID = -1096301185375029343L;
    private byte[] array;
    private int blockSize;
    private int f157n;

    public ByteVector() {
        this(2048);
    }

    public ByteVector(int capacity) {
        if (capacity > 0) {
            this.blockSize = capacity;
        } else {
            this.blockSize = 2048;
        }
        this.array = new byte[this.blockSize];
        this.f157n = 0;
    }

    public ByteVector(byte[] a) {
        this.blockSize = 2048;
        this.array = a;
        this.f157n = 0;
    }

    public ByteVector(byte[] a, int capacity) {
        if (capacity > 0) {
            this.blockSize = capacity;
        } else {
            this.blockSize = 2048;
        }
        this.array = a;
        this.f157n = 0;
    }

    public byte[] getArray() {
        return this.array;
    }

    public int length() {
        return this.f157n;
    }

    public int capacity() {
        return this.array.length;
    }

    public void put(int index, byte val) {
        this.array[index] = val;
    }

    public byte get(int index) {
        return this.array[index];
    }

    public int alloc(int size) {
        int index = this.f157n;
        int len = this.array.length;
        if (this.f157n + size >= len) {
            byte[] aux = new byte[(this.blockSize + len)];
            System.arraycopy(this.array, 0, aux, 0, len);
            this.array = aux;
        }
        this.f157n += size;
        return index;
    }

    public void trimToSize() {
        if (this.f157n < this.array.length) {
            byte[] aux = new byte[this.f157n];
            System.arraycopy(this.array, 0, aux, 0, this.f157n);
            this.array = aux;
        }
    }
}
