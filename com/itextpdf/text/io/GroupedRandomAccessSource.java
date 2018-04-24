package com.itextpdf.text.io;

import java.io.IOException;

class GroupedRandomAccessSource implements RandomAccessSource {
    private SourceEntry currentSourceEntry;
    private final long size;
    private final SourceEntry[] sources;

    private static class SourceEntry {
        final long firstByte;
        final int index;
        final long lastByte;
        final RandomAccessSource source;

        public SourceEntry(int index, RandomAccessSource source, long offset) {
            this.index = index;
            this.source = source;
            this.firstByte = offset;
            this.lastByte = (source.length() + offset) - 1;
        }

        public long offsetN(long absoluteOffset) {
            return absoluteOffset - this.firstByte;
        }
    }

    public GroupedRandomAccessSource(RandomAccessSource[] sources) throws IOException {
        this.sources = new SourceEntry[sources.length];
        long totalSize = 0;
        for (int i = 0; i < sources.length; i++) {
            this.sources[i] = new SourceEntry(i, sources[i], totalSize);
            totalSize += sources[i].length();
        }
        this.size = totalSize;
        this.currentSourceEntry = this.sources[sources.length - 1];
        sourceInUse(this.currentSourceEntry.source);
    }

    protected int getStartingSourceIndex(long offset) {
        if (offset >= this.currentSourceEntry.firstByte) {
            return this.currentSourceEntry.index;
        }
        return 0;
    }

    private SourceEntry getSourceEntryForOffset(long offset) throws IOException {
        if (offset >= this.size) {
            return null;
        }
        if (offset >= this.currentSourceEntry.firstByte && offset <= this.currentSourceEntry.lastByte) {
            return this.currentSourceEntry;
        }
        sourceReleased(this.currentSourceEntry.source);
        int i = getStartingSourceIndex(offset);
        while (i < this.sources.length) {
            if (offset < this.sources[i].firstByte || offset > this.sources[i].lastByte) {
                i++;
            } else {
                this.currentSourceEntry = this.sources[i];
                sourceInUse(this.currentSourceEntry.source);
                return this.currentSourceEntry;
            }
        }
        return null;
    }

    protected void sourceReleased(RandomAccessSource source) throws IOException {
    }

    protected void sourceInUse(RandomAccessSource source) throws IOException {
    }

    public int get(long position) throws IOException {
        SourceEntry entry = getSourceEntryForOffset(position);
        if (entry == null) {
            return -1;
        }
        return entry.source.get(entry.offsetN(position));
    }

    public int get(long position, byte[] bytes, int off, int len) throws IOException {
        SourceEntry entry = getSourceEntryForOffset(position);
        if (entry == null) {
            return -1;
        }
        int i;
        long offN = entry.offsetN(position);
        int remaining = len;
        while (remaining > 0 && entry != null && offN <= entry.source.length()) {
            int count = entry.source.get(offN, bytes, off, remaining);
            if (count == -1) {
                break;
            }
            off += count;
            position += (long) count;
            remaining -= count;
            offN = 0;
            entry = getSourceEntryForOffset(position);
        }
        if (remaining == len) {
            i = -1;
        } else {
            i = len - remaining;
        }
        return i;
    }

    public long length() {
        return this.size;
    }

    public void close() throws IOException {
        for (SourceEntry entry : this.sources) {
            entry.source.close();
        }
    }
}
