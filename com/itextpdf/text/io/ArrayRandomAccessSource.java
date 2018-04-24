package com.itextpdf.text.io;

import java.io.IOException;

class ArrayRandomAccessSource implements RandomAccessSource {
    private byte[] array;

    public ArrayRandomAccessSource(byte[] array) {
        if (array == null) {
            throw new NullPointerException();
        }
        this.array = array;
    }

    public int get(long offset) {
        if (offset >= ((long) this.array.length)) {
            return -1;
        }
        return this.array[(int) offset] & 255;
    }

    public int get(long offset, byte[] bytes, int off, int len) {
        if (this.array == null) {
            throw new IllegalStateException("Already closed");
        } else if (offset >= ((long) this.array.length)) {
            return -1;
        } else {
            if (((long) len) + offset > ((long) this.array.length)) {
                len = (int) (((long) this.array.length) - offset);
            }
            System.arraycopy(this.array, (int) offset, bytes, off, len);
            return len;
        }
    }

    public long length() {
        return (long) this.array.length;
    }

    public void close() throws IOException {
        this.array = null;
    }
}
