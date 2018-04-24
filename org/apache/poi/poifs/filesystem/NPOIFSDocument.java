package org.apache.poi.poifs.filesystem;

import android.support.v4.media.session.PlaybackStateCompat;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import org.apache.poi.poifs.dev.POIFSViewable;
import org.apache.poi.poifs.property.DocumentProperty;
import org.apache.poi.util.HexDump;

public final class NPOIFSDocument implements POIFSViewable {
    private int _block_size;
    private NPOIFSFileSystem _filesystem;
    private DocumentProperty _property;
    private NPOIFSStream _stream;

    public NPOIFSDocument(DocumentNode document) throws IOException {
        this((DocumentProperty) document.getProperty(), ((DirectoryNode) document.getParent()).getNFileSystem());
    }

    public NPOIFSDocument(DocumentProperty property, NPOIFSFileSystem filesystem) throws IOException {
        this._property = property;
        this._filesystem = filesystem;
        if (property.getSize() < 4096) {
            this._stream = new NPOIFSStream(this._filesystem.getMiniStore(), property.getStartBlock());
            this._block_size = this._filesystem.getMiniStore().getBlockStoreBlockSize();
            return;
        }
        this._stream = new NPOIFSStream(this._filesystem, property.getStartBlock());
        this._block_size = this._filesystem.getBlockStoreBlockSize();
    }

    public NPOIFSDocument(String name, NPOIFSFileSystem filesystem, InputStream stream) throws IOException {
        this._filesystem = filesystem;
        this._property = new DocumentProperty(name, store(stream));
        this._property.setStartBlock(this._stream.getStartBlock());
    }

    public NPOIFSDocument(String name, int size, NPOIFSFileSystem filesystem, POIFSWriterListener writer) throws IOException {
        this._filesystem = filesystem;
        if (size < 4096) {
            this._stream = new NPOIFSStream(filesystem.getMiniStore());
            this._block_size = this._filesystem.getMiniStore().getBlockStoreBlockSize();
        } else {
            this._stream = new NPOIFSStream(filesystem);
            this._block_size = this._filesystem.getBlockStoreBlockSize();
        }
        OutputStream innerOs = this._stream.getOutputStream();
        DocumentOutputStream os = new DocumentOutputStream(innerOs, size);
        POIFSDocumentPath path = new POIFSDocumentPath(name.split("\\\\"));
        writer.processPOIFSWriterEvent(new POIFSWriterEvent(os, path, path.getComponent(path.length() - 1), size));
        innerOs.close();
        this._property = new DocumentProperty(name, size);
        this._property.setStartBlock(this._stream.getStartBlock());
    }

    private int store(InputStream stream) throws IOException {
        BufferedInputStream bis = new BufferedInputStream(stream, 4097);
        bis.mark(4096);
        if (bis.skip(PlaybackStateCompat.ACTION_SKIP_TO_QUEUE_ITEM) < PlaybackStateCompat.ACTION_SKIP_TO_QUEUE_ITEM) {
            this._stream = new NPOIFSStream(this._filesystem.getMiniStore());
            this._block_size = this._filesystem.getMiniStore().getBlockStoreBlockSize();
        } else {
            this._stream = new NPOIFSStream(this._filesystem);
            this._block_size = this._filesystem.getBlockStoreBlockSize();
        }
        bis.reset();
        OutputStream os = this._stream.getOutputStream();
        byte[] buf = new byte[1024];
        int length = 0;
        while (true) {
            int readBytes = bis.read(buf);
            if (readBytes == -1) {
                break;
            }
            os.write(buf, 0, readBytes);
            length += readBytes;
        }
        int usedInBlock = length % this._block_size;
        if (!(usedInBlock == 0 || usedInBlock == this._block_size)) {
            byte[] padding = new byte[(this._block_size - usedInBlock)];
            Arrays.fill(padding, (byte) -1);
            os.write(padding);
        }
        os.close();
        return length;
    }

    void free() throws IOException {
        this._stream.free();
        this._property.setStartBlock(-2);
    }

    NPOIFSFileSystem getFileSystem() {
        return this._filesystem;
    }

    int getDocumentBlockSize() {
        return this._block_size;
    }

    Iterator<ByteBuffer> getBlockIterator() {
        if (getSize() > 0) {
            return this._stream.getBlockIterator();
        }
        return Collections.emptyList().iterator();
    }

    public int getSize() {
        return this._property.getSize();
    }

    public void replaceContents(InputStream stream) throws IOException {
        free();
        int size = store(stream);
        this._property.setStartBlock(this._stream.getStartBlock());
        this._property.updateSize(size);
    }

    DocumentProperty getDocumentProperty() {
        return this._property;
    }

    public Object[] getViewableArray() {
        String result = "<NO DATA>";
        if (getSize() > 0) {
            byte[] data = new byte[getSize()];
            int offset = 0;
            Iterator i$ = this._stream.iterator();
            while (i$.hasNext()) {
                ByteBuffer buffer = (ByteBuffer) i$.next();
                int length = Math.min(this._block_size, data.length - offset);
                buffer.get(data, offset, length);
                offset += length;
            }
            result = HexDump.dump(data, 0, 0);
        }
        return new String[]{result};
    }

    public Iterator<Object> getViewableIterator() {
        return Collections.emptyList().iterator();
    }

    public boolean preferArray() {
        return true;
    }

    public String getShortDescription() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("Document: \"").append(this._property.getName()).append("\"");
        buffer.append(" size = ").append(getSize());
        return buffer.toString();
    }
}
