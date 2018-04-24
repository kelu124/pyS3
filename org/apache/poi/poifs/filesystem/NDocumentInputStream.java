package org.apache.poi.poifs.filesystem;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Iterator;
import org.apache.poi.poifs.property.DocumentProperty;
import org.apache.poi.util.LittleEndian;

public final class NDocumentInputStream extends DocumentInputStream {
    private ByteBuffer _buffer;
    private boolean _closed;
    private int _current_block_count;
    private int _current_offset;
    private Iterator<ByteBuffer> _data;
    private NPOIFSDocument _document;
    private int _document_size;
    private int _marked_offset;
    private int _marked_offset_count;

    public NDocumentInputStream(DocumentEntry document) throws IOException {
        if (document instanceof DocumentNode) {
            this._current_offset = 0;
            this._current_block_count = 0;
            this._marked_offset = 0;
            this._marked_offset_count = 0;
            this._document_size = document.getSize();
            this._closed = false;
            DocumentNode doc = (DocumentNode) document;
            this._document = new NPOIFSDocument((DocumentProperty) doc.getProperty(), ((DirectoryNode) doc.getParent()).getNFileSystem());
            this._data = this._document.getBlockIterator();
            return;
        }
        throw new IOException("Cannot open internal document storage, " + document + " not a Document Node");
    }

    public NDocumentInputStream(NPOIFSDocument document) {
        this._current_offset = 0;
        this._current_block_count = 0;
        this._marked_offset = 0;
        this._marked_offset_count = 0;
        this._document_size = document.getSize();
        this._closed = false;
        this._document = document;
        this._data = this._document.getBlockIterator();
    }

    public int available() {
        if (!this._closed) {
            return this._document_size - this._current_offset;
        }
        throw new IllegalStateException("cannot perform requested operation on a closed stream");
    }

    public void close() {
        this._closed = true;
    }

    public void mark(int ignoredReadlimit) {
        this._marked_offset = this._current_offset;
        this._marked_offset_count = Math.max(0, this._current_block_count - 1);
    }

    public int read() throws IOException {
        dieIfClosed();
        if (atEOD()) {
            return -1;
        }
        byte[] b = new byte[1];
        int result = read(b, 0, 1);
        if (result < 0) {
            return result;
        }
        if (b[0] < (byte) 0) {
            return b[0] + 256;
        }
        return b[0];
    }

    public int read(byte[] b, int off, int len) throws IOException {
        dieIfClosed();
        if (b == null) {
            throw new IllegalArgumentException("buffer must not be null");
        } else if (off < 0 || len < 0 || b.length < off + len) {
            throw new IndexOutOfBoundsException("can't read past buffer boundaries");
        } else if (len == 0) {
            return 0;
        } else {
            if (atEOD()) {
                return -1;
            }
            int limit = Math.min(available(), len);
            readFully(b, off, limit);
            return limit;
        }
    }

    public void reset() {
        if (this._marked_offset == 0 && this._marked_offset_count == 0) {
            this._current_block_count = this._marked_offset_count;
            this._current_offset = this._marked_offset;
            this._data = this._document.getBlockIterator();
            this._buffer = null;
            return;
        }
        this._data = this._document.getBlockIterator();
        this._current_offset = 0;
        for (int i = 0; i < this._marked_offset_count; i++) {
            this._buffer = (ByteBuffer) this._data.next();
            this._current_offset += this._buffer.remaining();
        }
        this._current_block_count = this._marked_offset_count;
        if (this._current_offset != this._marked_offset) {
            this._buffer = (ByteBuffer) this._data.next();
            this._current_block_count++;
            this._buffer.position(this._buffer.position() + (this._marked_offset - this._current_offset));
        }
        this._current_offset = this._marked_offset;
    }

    public long skip(long n) throws IOException {
        dieIfClosed();
        if (n < 0) {
            return 0;
        }
        int new_offset = this._current_offset + ((int) n);
        if (new_offset < this._current_offset) {
            new_offset = this._document_size;
        } else if (new_offset > this._document_size) {
            new_offset = this._document_size;
        }
        long rval = (long) (new_offset - this._current_offset);
        readFully(new byte[((int) rval)]);
        return rval;
    }

    private void dieIfClosed() throws IOException {
        if (this._closed) {
            throw new IOException("cannot perform requested operation on a closed stream");
        }
    }

    private boolean atEOD() {
        return this._current_offset == this._document_size;
    }

    private void checkAvaliable(int requestedSize) {
        if (this._closed) {
            throw new IllegalStateException("cannot perform requested operation on a closed stream");
        } else if (requestedSize > this._document_size - this._current_offset) {
            throw new RuntimeException("Buffer underrun - requested " + requestedSize + " bytes but " + (this._document_size - this._current_offset) + " was available");
        }
    }

    public void readFully(byte[] buf, int off, int len) {
        checkAvaliable(len);
        int read = 0;
        while (read < len) {
            if (this._buffer == null || this._buffer.remaining() == 0) {
                this._current_block_count++;
                this._buffer = (ByteBuffer) this._data.next();
            }
            int limit = Math.min(len - read, this._buffer.remaining());
            this._buffer.get(buf, off + read, limit);
            this._current_offset += limit;
            read += limit;
        }
    }

    public byte readByte() {
        return (byte) readUByte();
    }

    public double readDouble() {
        return Double.longBitsToDouble(readLong());
    }

    public long readLong() {
        checkAvaliable(8);
        byte[] data = new byte[8];
        readFully(data, 0, 8);
        return LittleEndian.getLong(data, 0);
    }

    public short readShort() {
        checkAvaliable(2);
        byte[] data = new byte[2];
        readFully(data, 0, 2);
        return LittleEndian.getShort(data);
    }

    public int readInt() {
        checkAvaliable(4);
        byte[] data = new byte[4];
        readFully(data, 0, 4);
        return LittleEndian.getInt(data);
    }

    public int readUShort() {
        checkAvaliable(2);
        byte[] data = new byte[2];
        readFully(data, 0, 2);
        return LittleEndian.getUShort(data);
    }

    public int readUByte() {
        checkAvaliable(1);
        byte[] data = new byte[1];
        readFully(data, 0, 1);
        if (data[0] >= (byte) 0) {
            return data[0];
        }
        return data[0] + 256;
    }
}
