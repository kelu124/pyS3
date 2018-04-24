package com.itextpdf.text.io;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;

class MappedChannelRandomAccessSource implements RandomAccessSource {
    private final FileChannel channel;
    private final long length;
    private final long offset;
    private ByteBufferRandomAccessSource source;

    public MappedChannelRandomAccessSource(FileChannel channel, long offset, long length) {
        if (offset < 0) {
            throw new IllegalArgumentException(offset + " is negative");
        } else if (length <= 0) {
            throw new IllegalArgumentException(length + " is zero or negative");
        } else {
            this.channel = channel;
            this.offset = offset;
            this.length = length;
            this.source = null;
        }
    }

    void open() throws IOException {
        if (this.source == null) {
            if (this.channel.isOpen()) {
                try {
                    this.source = new ByteBufferRandomAccessSource(this.channel.map(MapMode.READ_ONLY, this.offset, this.length));
                    return;
                } catch (IOException e) {
                    if (exceptionIsMapFailureException(e)) {
                        throw new MapFailedException(e);
                    }
                    throw e;
                }
            }
            throw new IllegalStateException("Channel is closed");
        }
    }

    private static boolean exceptionIsMapFailureException(IOException e) {
        if (e.getMessage() == null || e.getMessage().indexOf("Map failed") < 0) {
            return false;
        }
        return true;
    }

    public int get(long position) throws IOException {
        if (this.source != null) {
            return this.source.get(position);
        }
        throw new IOException("RandomAccessSource not opened");
    }

    public int get(long position, byte[] bytes, int off, int len) throws IOException {
        if (this.source != null) {
            return this.source.get(position, bytes, off, len);
        }
        throw new IOException("RandomAccessSource not opened");
    }

    public long length() {
        return this.length;
    }

    public void close() throws IOException {
        if (this.source != null) {
            this.source.close();
            this.source = null;
        }
    }

    public String toString() {
        return getClass().getName() + " (" + this.offset + ", " + this.length + ")";
    }
}
