package com.itextpdf.text.io;

import java.io.IOException;

public class WindowRandomAccessSource implements RandomAccessSource {
    private final long length;
    private final long offset;
    private final RandomAccessSource source;

    public WindowRandomAccessSource(RandomAccessSource source, long offset) {
        this(source, offset, source.length() - offset);
    }

    public WindowRandomAccessSource(RandomAccessSource source, long offset, long length) {
        this.source = source;
        this.offset = offset;
        this.length = length;
    }

    public int get(long position) throws IOException {
        if (position >= this.length) {
            return -1;
        }
        return this.source.get(this.offset + position);
    }

    public int get(long position, byte[] bytes, int off, int len) throws IOException {
        if (position >= this.length) {
            return -1;
        }
        return this.source.get(this.offset + position, bytes, off, (int) Math.min((long) len, this.length - position));
    }

    public long length() {
        return this.length;
    }

    public void close() throws IOException {
        this.source.close();
    }
}
