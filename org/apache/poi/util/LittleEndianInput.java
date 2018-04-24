package org.apache.poi.util;

public interface LittleEndianInput {
    int available();

    byte readByte();

    double readDouble();

    void readFully(byte[] bArr);

    void readFully(byte[] bArr, int i, int i2);

    int readInt();

    long readLong();

    short readShort();

    int readUByte();

    int readUShort();
}
