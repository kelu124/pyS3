package org.apache.poi.poifs.nio;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

public abstract class DataSource {
    public abstract void close() throws IOException;

    public abstract void copyTo(OutputStream outputStream) throws IOException;

    public abstract ByteBuffer read(int i, long j) throws IOException;

    public abstract long size() throws IOException;

    public abstract void write(ByteBuffer byteBuffer, long j) throws IOException;
}
