package org.apache.poi.poifs.nio;

import android.support.v4.media.session.PlaybackStateCompat;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

public class ByteArrayBackedDataSource extends DataSource {
    private byte[] buffer;
    private long size;

    public ByteArrayBackedDataSource(byte[] data, int size) {
        this.buffer = data;
        this.size = (long) size;
    }

    public ByteArrayBackedDataSource(byte[] data) {
        this(data, data.length);
    }

    public ByteBuffer read(int length, long position) {
        if (position >= this.size) {
            throw new IndexOutOfBoundsException("Unable to read " + length + " bytes from " + position + " in stream of length " + this.size);
        }
        return ByteBuffer.wrap(this.buffer, (int) position, (int) Math.min((long) length, this.size - position));
    }

    public void write(ByteBuffer src, long position) {
        long endPosition = position + ((long) src.capacity());
        if (endPosition > ((long) this.buffer.length)) {
            extend(endPosition);
        }
        src.get(this.buffer, (int) position, src.capacity());
        if (endPosition > this.size) {
            this.size = endPosition;
        }
    }

    private void extend(long length) {
        long difference = length - ((long) this.buffer.length);
        if (((double) difference) < ((double) this.buffer.length) * 0.25d) {
            difference = (long) (((double) this.buffer.length) * 0.25d);
        }
        if (difference < PlaybackStateCompat.ACTION_SKIP_TO_QUEUE_ITEM) {
            difference = PlaybackStateCompat.ACTION_SKIP_TO_QUEUE_ITEM;
        }
        byte[] nb = new byte[((int) (((long) this.buffer.length) + difference))];
        System.arraycopy(this.buffer, 0, nb, 0, (int) this.size);
        this.buffer = nb;
    }

    public void copyTo(OutputStream stream) throws IOException {
        stream.write(this.buffer, 0, (int) this.size);
    }

    public long size() {
        return this.size;
    }

    public void close() {
        this.buffer = null;
        this.size = -1;
    }
}
