package org.apache.poi.poifs.crypt;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import javax.crypto.Cipher;
import org.apache.poi.poifs.filesystem.DirectoryNode;
import org.apache.poi.util.Internal;
import org.apache.poi.util.POILogFactory;
import org.apache.poi.util.POILogger;
import org.apache.poi.util.TempFile;

@Internal
public abstract class ChunkedCipherOutputStream extends FilterOutputStream {
    private static final POILogger logger = POILogFactory.getLogger(ChunkedCipherOutputStream.class);
    private final byte[] _chunk;
    private Cipher _cipher;
    private long _pos = 0;
    protected final int chunkBits;
    protected final int chunkMask;
    protected final int chunkSize;
    private final DirectoryNode dir;
    private final File fileOut;

    protected abstract void calculateChecksum(File file, int i) throws GeneralSecurityException, IOException;

    protected abstract void createEncryptionInfoEntry(DirectoryNode directoryNode, File file) throws IOException, GeneralSecurityException;

    protected abstract Cipher initCipherForBlock(Cipher cipher, int i, boolean z) throws GeneralSecurityException;

    public ChunkedCipherOutputStream(DirectoryNode dir, int chunkSize) throws IOException, GeneralSecurityException {
        super(null);
        this.chunkSize = chunkSize;
        this.chunkMask = chunkSize - 1;
        this.chunkBits = Integer.bitCount(this.chunkMask);
        this._chunk = new byte[chunkSize];
        this.fileOut = TempFile.createTempFile("encrypted_package", "crypt");
        this.fileOut.deleteOnExit();
        this.out = new FileOutputStream(this.fileOut);
        this.dir = dir;
        this._cipher = initCipherForBlock(null, 0, false);
    }

    public void write(int b) throws IOException {
        write(new byte[]{(byte) b});
    }

    public void write(byte[] b) throws IOException {
        write(b, 0, b.length);
    }

    public void write(byte[] b, int off, int len) throws IOException {
        if (len != 0) {
            if (len < 0 || b.length < off + len) {
                throw new IOException("not enough bytes in your input buffer");
            }
            while (len > 0) {
                int posInChunk = (int) (this._pos & ((long) this.chunkMask));
                int nextLen = Math.min(this.chunkSize - posInChunk, len);
                System.arraycopy(b, off, this._chunk, posInChunk, nextLen);
                this._pos += (long) nextLen;
                off += nextLen;
                len -= nextLen;
                if ((this._pos & ((long) this.chunkMask)) == 0) {
                    try {
                        writeChunk();
                    } catch (GeneralSecurityException e) {
                        throw new IOException(e);
                    }
                }
            }
        }
    }

    protected void writeChunk() throws IOException, GeneralSecurityException {
        boolean lastChunk;
        int posInChunk = (int) (this._pos & ((long) this.chunkMask));
        int index = (int) (this._pos >> this.chunkBits);
        if (posInChunk == 0) {
            index--;
            posInChunk = this.chunkSize;
            lastChunk = false;
        } else {
            lastChunk = true;
        }
        this._cipher = initCipherForBlock(this._cipher, index, lastChunk);
        this.out.write(this._chunk, 0, this._cipher.doFinal(this._chunk, 0, posInChunk, this._chunk));
    }

    public void close() throws IOException {
        try {
            writeChunk();
            super.close();
            int oleStreamSize = (int) (this.fileOut.length() + 8);
            calculateChecksum(this.fileOut, (int) this._pos);
            this.dir.createDocument(Decryptor.DEFAULT_POIFS_ENTRY, oleStreamSize, new EncryptedPackageWriter(this, null));
            createEncryptionInfoEntry(this.dir, this.fileOut);
        } catch (GeneralSecurityException e) {
            throw new IOException(e);
        }
    }
}
