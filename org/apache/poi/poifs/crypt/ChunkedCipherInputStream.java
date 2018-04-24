package org.apache.poi.poifs.crypt;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import javax.crypto.Cipher;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.util.Internal;
import org.apache.poi.util.LittleEndianInput;
import org.apache.poi.util.LittleEndianInputStream;

@Internal
public abstract class ChunkedCipherInputStream extends LittleEndianInputStream {
    private byte[] _chunk;
    private Cipher _cipher;
    private int _lastIndex = 0;
    private long _pos = 0;
    private long _size;
    private final int chunkBits;
    private final int chunkMask;
    private final int chunkSize;

    protected abstract Cipher initCipherForBlock(Cipher cipher, int i) throws GeneralSecurityException;

    public ChunkedCipherInputStream(LittleEndianInput stream, long size, int chunkSize) throws GeneralSecurityException {
        super((InputStream) stream);
        this._size = size;
        this.chunkSize = chunkSize;
        this.chunkMask = chunkSize - 1;
        this.chunkBits = Integer.bitCount(this.chunkMask);
        this._cipher = initCipherForBlock(null, 0);
    }

    public int read() throws IOException {
        byte[] b = new byte[1];
        if (read(b) == 1) {
            return b[0];
        }
        return -1;
    }

    public int read(byte[] b, int off, int len) throws IOException {
        int total = 0;
        if (available() <= 0) {
            return -1;
        }
        while (len > 0) {
            if (this._chunk == null) {
                try {
                    this._chunk = nextChunk();
                } catch (GeneralSecurityException e) {
                    throw new EncryptedDocumentException(e.getMessage(), e);
                }
            }
            int i = (int) (((long) this.chunkSize) - (this._pos & ((long) this.chunkMask)));
            int avail = available();
            if (avail == 0) {
                return total;
            }
            i = Math.min(avail, Math.min(i, len));
            System.arraycopy(this._chunk, (int) (this._pos & ((long) this.chunkMask)), b, off, i);
            off += i;
            len -= i;
            this._pos += (long) i;
            if ((this._pos & ((long) this.chunkMask)) == 0) {
                this._chunk = null;
            }
            total += i;
        }
        return total;
    }

    public long skip(long n) throws IOException {
        long start = this._pos;
        long skip = Math.min((long) available(), n);
        if ((((this._pos + skip) ^ start) & ((long) (this.chunkMask ^ -1))) != 0) {
            this._chunk = null;
        }
        this._pos += skip;
        return skip;
    }

    public int available() {
        return (int) (this._size - this._pos);
    }

    public boolean markSupported() {
        return false;
    }

    public synchronized void mark(int readlimit) {
        throw new UnsupportedOperationException();
    }

    public synchronized void reset() throws IOException {
        throw new UnsupportedOperationException();
    }

    private byte[] nextChunk() throws GeneralSecurityException, IOException {
        int index = (int) (this._pos >> this.chunkBits);
        initCipherForBlock(this._cipher, index);
        if (this._lastIndex != index) {
            super.skip((long) ((index - this._lastIndex) << this.chunkBits));
        }
        byte[] block = new byte[Math.min(super.available(), this.chunkSize)];
        super.read(block, 0, block.length);
        this._lastIndex = index + 1;
        return this._cipher.doFinal(block);
    }
}
