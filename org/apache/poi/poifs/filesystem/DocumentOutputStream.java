package org.apache.poi.poifs.filesystem;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;

public final class DocumentOutputStream extends OutputStream {
    private final int _limit;
    private final OutputStream _stream;
    private int _written = 0;

    DocumentOutputStream(OutputStream stream, int limit) {
        this._stream = stream;
        this._limit = limit;
    }

    public void write(int b) throws IOException {
        limitCheck(1);
        this._stream.write(b);
    }

    public void write(byte[] b) throws IOException {
        write(b, 0, b.length);
    }

    public void write(byte[] b, int off, int len) throws IOException {
        limitCheck(len);
        this._stream.write(b, off, len);
    }

    public void flush() throws IOException {
        this._stream.flush();
    }

    public void close() {
    }

    void writeFiller(int totalLimit, byte fill) throws IOException {
        if (totalLimit > this._written) {
            byte[] filler = new byte[(totalLimit - this._written)];
            Arrays.fill(filler, fill);
            this._stream.write(filler);
        }
    }

    private void limitCheck(int toBeWritten) throws IOException {
        if (this._written + toBeWritten > this._limit) {
            throw new IOException("tried to write too much data");
        }
        this._written += toBeWritten;
    }
}
