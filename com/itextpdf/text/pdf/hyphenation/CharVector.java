package com.itextpdf.text.pdf.hyphenation;

import java.io.Serializable;

public class CharVector implements Cloneable, Serializable {
    private static final int DEFAULT_BLOCK_SIZE = 2048;
    private static final long serialVersionUID = -4875768298308363544L;
    private char[] array;
    private int blockSize;
    private int f158n;

    public CharVector() {
        this(2048);
    }

    public CharVector(int capacity) {
        if (capacity > 0) {
            this.blockSize = capacity;
        } else {
            this.blockSize = 2048;
        }
        this.array = new char[this.blockSize];
        this.f158n = 0;
    }

    public CharVector(char[] a) {
        this.blockSize = 2048;
        this.array = a;
        this.f158n = a.length;
    }

    public CharVector(char[] a, int capacity) {
        if (capacity > 0) {
            this.blockSize = capacity;
        } else {
            this.blockSize = 2048;
        }
        this.array = a;
        this.f158n = a.length;
    }

    public void clear() {
        this.f158n = 0;
    }

    public Object clone() {
        CharVector cv = new CharVector((char[]) this.array.clone(), this.blockSize);
        cv.f158n = this.f158n;
        return cv;
    }

    public char[] getArray() {
        return this.array;
    }

    public int length() {
        return this.f158n;
    }

    public int capacity() {
        return this.array.length;
    }

    public void put(int index, char val) {
        this.array[index] = val;
    }

    public char get(int index) {
        return this.array[index];
    }

    public int alloc(int size) {
        int index = this.f158n;
        int len = this.array.length;
        if (this.f158n + size >= len) {
            char[] aux = new char[(this.blockSize + len)];
            System.arraycopy(this.array, 0, aux, 0, len);
            this.array = aux;
        }
        this.f158n += size;
        return index;
    }

    public void trimToSize() {
        if (this.f158n < this.array.length) {
            char[] aux = new char[this.f158n];
            System.arraycopy(this.array, 0, aux, 0, this.f158n);
            this.array = aux;
        }
    }
}
