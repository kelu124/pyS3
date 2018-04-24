package org.apache.poi.poifs.filesystem;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Iterator;

public class NPOIFSStream implements Iterable<ByteBuffer> {
    private BlockStore blockStore;
    private OutputStream outStream;
    private int startBlock;

    protected class StreamBlockByteBuffer extends OutputStream {
        ByteBuffer buffer;
        ChainLoopDetector loopDetector;
        int nextBlock;
        byte[] oneByte = new byte[1];
        int prevBlock;

        protected StreamBlockByteBuffer() throws IOException {
            this.loopDetector = NPOIFSStream.this.blockStore.getChainLoopDetector();
            this.prevBlock = -2;
            this.nextBlock = NPOIFSStream.this.startBlock;
        }

        protected void createBlockIfNeeded() throws IOException {
            if (this.buffer == null || !this.buffer.hasRemaining()) {
                int thisBlock = this.nextBlock;
                if (thisBlock == -2) {
                    thisBlock = NPOIFSStream.this.blockStore.getFreeBlock();
                    this.loopDetector.claim(thisBlock);
                    this.nextBlock = -2;
                    if (this.prevBlock != -2) {
                        NPOIFSStream.this.blockStore.setNextBlock(this.prevBlock, thisBlock);
                    }
                    NPOIFSStream.this.blockStore.setNextBlock(thisBlock, -2);
                    if (NPOIFSStream.this.startBlock == -2) {
                        NPOIFSStream.this.startBlock = thisBlock;
                    }
                } else {
                    this.loopDetector.claim(thisBlock);
                    this.nextBlock = NPOIFSStream.this.blockStore.getNextBlock(thisBlock);
                }
                this.buffer = NPOIFSStream.this.blockStore.createBlockIfNeeded(thisBlock);
                this.prevBlock = thisBlock;
            }
        }

        public void write(int b) throws IOException {
            this.oneByte[0] = (byte) (b & 255);
            write(this.oneByte);
        }

        public void write(byte[] b, int off, int len) throws IOException {
            if (off < 0 || off > b.length || len < 0 || off + len > b.length || off + len < 0) {
                throw new IndexOutOfBoundsException();
            } else if (len != 0) {
                do {
                    createBlockIfNeeded();
                    int writeBytes = Math.min(this.buffer.remaining(), len);
                    this.buffer.put(b, off, writeBytes);
                    off += writeBytes;
                    len -= writeBytes;
                } while (len > 0);
            }
        }

        public void close() throws IOException {
            new NPOIFSStream(NPOIFSStream.this.blockStore, this.nextBlock).free(this.loopDetector);
            if (this.prevBlock != -2) {
                NPOIFSStream.this.blockStore.setNextBlock(this.prevBlock, -2);
            }
        }
    }

    protected class StreamBlockByteBufferIterator implements Iterator<ByteBuffer> {
        private ChainLoopDetector loopDetector;
        private int nextBlock;

        protected StreamBlockByteBufferIterator(int firstBlock) {
            this.nextBlock = firstBlock;
            try {
                this.loopDetector = NPOIFSStream.this.blockStore.getChainLoopDetector();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        public boolean hasNext() {
            if (this.nextBlock == -2) {
                return false;
            }
            return true;
        }

        public ByteBuffer next() {
            if (this.nextBlock == -2) {
                throw new IndexOutOfBoundsException("Can't read past the end of the stream");
            }
            try {
                this.loopDetector.claim(this.nextBlock);
                ByteBuffer data = NPOIFSStream.this.blockStore.getBlockAt(this.nextBlock);
                this.nextBlock = NPOIFSStream.this.blockStore.getNextBlock(this.nextBlock);
                return data;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    public NPOIFSStream(BlockStore blockStore, int startBlock) {
        this.blockStore = blockStore;
        this.startBlock = startBlock;
    }

    public NPOIFSStream(BlockStore blockStore) {
        this.blockStore = blockStore;
        this.startBlock = -2;
    }

    public int getStartBlock() {
        return this.startBlock;
    }

    public Iterator<ByteBuffer> iterator() {
        return getBlockIterator();
    }

    public Iterator<ByteBuffer> getBlockIterator() {
        if (this.startBlock != -2) {
            return new StreamBlockByteBufferIterator(this.startBlock);
        }
        throw new IllegalStateException("Can't read from a new stream before it has been written to");
    }

    public void updateContents(byte[] contents) throws IOException {
        OutputStream os = getOutputStream();
        os.write(contents);
        os.close();
    }

    public OutputStream getOutputStream() throws IOException {
        if (this.outStream == null) {
            this.outStream = new StreamBlockByteBuffer();
        }
        return this.outStream;
    }

    public void free() throws IOException {
        free(this.blockStore.getChainLoopDetector());
    }

    private void free(ChainLoopDetector loopDetector) {
        int nextBlock = this.startBlock;
        while (nextBlock != -2) {
            int thisBlock = nextBlock;
            loopDetector.claim(thisBlock);
            nextBlock = this.blockStore.getNextBlock(thisBlock);
            this.blockStore.setNextBlock(thisBlock, -1);
        }
        this.startBlock = -2;
    }
}
