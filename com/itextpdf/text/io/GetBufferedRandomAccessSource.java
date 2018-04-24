package com.itextpdf.text.io;

import android.support.v4.media.session.PlaybackStateCompat;
import java.io.IOException;

public class GetBufferedRandomAccessSource implements RandomAccessSource {
    private final byte[] getBuffer;
    private long getBufferEnd = -1;
    private long getBufferStart = -1;
    private final RandomAccessSource source;

    public GetBufferedRandomAccessSource(RandomAccessSource source) {
        this.source = source;
        this.getBuffer = new byte[((int) Math.min(Math.max(source.length() / 4, 1), PlaybackStateCompat.ACTION_SKIP_TO_QUEUE_ITEM))];
        this.getBufferStart = -1;
        this.getBufferEnd = -1;
    }

    public int get(long position) throws IOException {
        if (position < this.getBufferStart || position > this.getBufferEnd) {
            int count = this.source.get(position, this.getBuffer, 0, this.getBuffer.length);
            if (count == -1) {
                return -1;
            }
            this.getBufferStart = position;
            this.getBufferEnd = (((long) count) + position) - 1;
        }
        return this.getBuffer[(int) (position - this.getBufferStart)] & 255;
    }

    public int get(long position, byte[] bytes, int off, int len) throws IOException {
        return this.source.get(position, bytes, off, len);
    }

    public long length() {
        return this.source.length();
    }

    public void close() throws IOException {
        this.source.close();
        this.getBufferStart = -1;
        this.getBufferEnd = -1;
    }
}
