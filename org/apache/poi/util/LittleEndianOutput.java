package org.apache.poi.util;

public interface LittleEndianOutput {
    void write(byte[] bArr);

    void write(byte[] bArr, int i, int i2);

    void writeByte(int i);

    void writeDouble(double d);

    void writeInt(int i);

    void writeLong(long j);

    void writeShort(int i);
}
