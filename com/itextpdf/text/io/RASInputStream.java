package com.itextpdf.text.io;

import java.io.IOException;
import java.io.InputStream;

public class RASInputStream extends InputStream {
    private long position = 0;
    private final RandomAccessSource source;

    public RASInputStream(RandomAccessSource source) {
        this.source = source;
    }

    public int read(byte[] b, int off, int len) throws IOException {
        int count = this.source.get(this.position, b, off, len);
        this.position += (long) count;
        return count;
    }

    public int read() throws IOException {
        RandomAccessSource randomAccessSource = this.source;
        long j = this.position;
        this.position = 1 + j;
        return randomAccessSource.get(j);
    }
}
