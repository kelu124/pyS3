package org.apache.poi.util;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public final class LittleEndianOutputStream extends FilterOutputStream implements LittleEndianOutput {
    public LittleEndianOutputStream(OutputStream out) {
        super(out);
    }

    public void writeByte(int v) {
        try {
            this.out.write(v);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void writeDouble(double v) {
        writeLong(Double.doubleToLongBits(v));
    }

    public void writeInt(int v) {
        int b3 = (v >>> 24) & 255;
        int b2 = (v >>> 16) & 255;
        int b1 = (v >>> 8) & 255;
        try {
            this.out.write((v >>> 0) & 255);
            this.out.write(b1);
            this.out.write(b2);
            this.out.write(b3);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void writeLong(long v) {
        writeInt((int) (v >> null));
        writeInt((int) (v >> 32));
    }

    public void writeShort(int v) {
        int b1 = (v >>> 8) & 255;
        try {
            this.out.write((v >>> 0) & 255);
            this.out.write(b1);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void write(byte[] b) {
        try {
            super.write(b);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void write(byte[] b, int off, int len) {
        try {
            super.write(b, off, len);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
