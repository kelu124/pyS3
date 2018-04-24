package com.itextpdf.text.io;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.Iterator;
import java.util.LinkedList;

class PagedChannelRandomAccessSource extends GroupedRandomAccessSource implements RandomAccessSource {
    public static final int DEFAULT_MAX_OPEN_BUFFERS = 16;
    public static final int DEFAULT_TOTAL_BUFSIZE = 67108864;
    private final int bufferSize;
    private final FileChannel channel;
    private final MRU<RandomAccessSource> mru;

    private static class MRU<E> {
        private final int limit;
        private LinkedList<E> queue = new LinkedList();

        public MRU(int limit) {
            this.limit = limit;
        }

        public E enqueue(E newElement) {
            if (this.queue.size() > 0 && this.queue.getFirst() == newElement) {
                return null;
            }
            Iterator<E> it = this.queue.iterator();
            while (it.hasNext()) {
                if (newElement == it.next()) {
                    it.remove();
                    this.queue.addFirst(newElement);
                    return null;
                }
            }
            this.queue.addFirst(newElement);
            if (this.queue.size() > this.limit) {
                return this.queue.removeLast();
            }
            return null;
        }
    }

    public PagedChannelRandomAccessSource(FileChannel channel) throws IOException {
        this(channel, 67108864, 16);
    }

    public PagedChannelRandomAccessSource(FileChannel channel, int totalBufferSize, int maxOpenBuffers) throws IOException {
        super(buildSources(channel, totalBufferSize / maxOpenBuffers));
        this.channel = channel;
        this.bufferSize = totalBufferSize / maxOpenBuffers;
        this.mru = new MRU(maxOpenBuffers);
    }

    private static RandomAccessSource[] buildSources(FileChannel channel, int bufferSize) throws IOException {
        long size = channel.size();
        if (size <= 0) {
            throw new IOException("File size must be greater than zero");
        }
        int bufferCount = ((int) (size / ((long) bufferSize))) + (size % ((long) bufferSize) == 0 ? 0 : 1);
        MappedChannelRandomAccessSource[] sources = new MappedChannelRandomAccessSource[bufferCount];
        for (int i = 0; i < bufferCount; i++) {
            long pageOffset = ((long) i) * ((long) bufferSize);
            sources[i] = new MappedChannelRandomAccessSource(channel, pageOffset, Math.min(size - pageOffset, (long) bufferSize));
        }
        return sources;
    }

    protected int getStartingSourceIndex(long offset) {
        return (int) (offset / ((long) this.bufferSize));
    }

    protected void sourceReleased(RandomAccessSource source) throws IOException {
        RandomAccessSource old = (RandomAccessSource) this.mru.enqueue(source);
        if (old != null) {
            old.close();
        }
    }

    protected void sourceInUse(RandomAccessSource source) throws IOException {
        ((MappedChannelRandomAccessSource) source).open();
    }

    public void close() throws IOException {
        super.close();
        this.channel.close();
    }
}
