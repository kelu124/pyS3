package com.itextpdf.text.io;

import java.io.IOException;

public interface RandomAccessSource {
    void close() throws IOException;

    int get(long j) throws IOException;

    int get(long j, byte[] bArr, int i, int i2) throws IOException;

    long length();
}
