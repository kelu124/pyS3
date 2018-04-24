package org.apache.poi.poifs.nio;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.util.IOUtils;
import org.apache.poi.util.POILogFactory;
import org.apache.poi.util.POILogger;
import org.apache.poi.util.SuppressForbidden;

public class FileBackedDataSource extends DataSource {
    private static final POILogger logger = POILogFactory.getLogger(FileBackedDataSource.class);
    private List<ByteBuffer> buffersToClean;
    private FileChannel channel;
    private RandomAccessFile srcFile;
    private boolean writable;

    public FileBackedDataSource(File file) throws FileNotFoundException {
        this(newSrcFile(file, "r"), true);
    }

    public FileBackedDataSource(File file, boolean readOnly) throws FileNotFoundException {
        this(newSrcFile(file, readOnly ? "r" : "rw"), readOnly);
    }

    public FileBackedDataSource(RandomAccessFile srcFile, boolean readOnly) {
        this(srcFile.getChannel(), readOnly);
        this.srcFile = srcFile;
    }

    public FileBackedDataSource(FileChannel channel, boolean readOnly) {
        this.buffersToClean = new ArrayList();
        this.channel = channel;
        this.writable = !readOnly;
    }

    public boolean isWriteable() {
        return this.writable;
    }

    public FileChannel getChannel() {
        return this.channel;
    }

    public ByteBuffer read(int length, long position) throws IOException {
        if (position >= size()) {
            throw new IndexOutOfBoundsException("Position " + position + " past the end of the file");
        }
        ByteBuffer dst;
        int worked;
        if (this.writable) {
            dst = this.channel.map(MapMode.READ_WRITE, position, (long) length);
            worked = 0;
            this.buffersToClean.add(dst);
        } else {
            this.channel.position(position);
            dst = ByteBuffer.allocate(length);
            worked = IOUtils.readFully(this.channel, dst);
        }
        if (worked == -1) {
            throw new IndexOutOfBoundsException("Position " + position + " past the end of the file");
        }
        dst.position(0);
        return dst;
    }

    public void write(ByteBuffer src, long position) throws IOException {
        this.channel.write(src, position);
    }

    public void copyTo(OutputStream stream) throws IOException {
        this.channel.transferTo(0, this.channel.size(), Channels.newChannel(stream));
    }

    public long size() throws IOException {
        return this.channel.size();
    }

    public void close() throws IOException {
        for (ByteBuffer buffer : this.buffersToClean) {
            unmap(buffer);
        }
        this.buffersToClean.clear();
        if (this.srcFile != null) {
            this.srcFile.close();
        } else {
            this.channel.close();
        }
    }

    private static RandomAccessFile newSrcFile(File file, String mode) throws FileNotFoundException {
        if (file.exists()) {
            return new RandomAccessFile(file, mode);
        }
        throw new FileNotFoundException(file.toString());
    }

    private static void unmap(final ByteBuffer buffer) {
        if (!buffer.getClass().getName().endsWith("HeapByteBuffer")) {
            AccessController.doPrivileged(new PrivilegedAction<Void>() {
                @SuppressForbidden("Java 9 Jigsaw whitelists access to sun.misc.Cleaner, so setAccessible works")
                public Void run() {
                    try {
                        Method getCleanerMethod = buffer.getClass().getMethod("cleaner", new Class[0]);
                        getCleanerMethod.setAccessible(true);
                        Object cleaner = getCleanerMethod.invoke(buffer, new Object[0]);
                        if (cleaner != null) {
                            cleaner.getClass().getMethod("clean", new Class[0]).invoke(cleaner, new Object[0]);
                        }
                    } catch (Exception e) {
                        FileBackedDataSource.logger.log(5, new Object[]{"Unable to unmap memory mapped ByteBuffer.", e});
                    }
                    return null;
                }
            });
        }
    }
}
