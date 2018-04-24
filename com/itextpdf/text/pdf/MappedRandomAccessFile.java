package com.itextpdf.text.pdf;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.Method;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.security.AccessController;
import java.security.PrivilegedAction;

public class MappedRandomAccessFile {
    private static final int BUFSIZE = 1073741824;
    private FileChannel channel = null;
    private MappedByteBuffer[] mappedBuffers;
    private long pos;
    private long size;

    public MappedRandomAccessFile(String filename, String mode) throws FileNotFoundException, IOException {
        if (mode.equals("rw")) {
            init(new RandomAccessFile(filename, mode).getChannel(), MapMode.READ_WRITE);
        } else {
            init(new FileInputStream(filename).getChannel(), MapMode.READ_ONLY);
        }
    }

    private void init(FileChannel channel, MapMode mapMode) throws IOException {
        this.channel = channel;
        this.size = channel.size();
        this.pos = 0;
        int requiredBuffers = ((int) (this.size / 1073741824)) + (this.size % 1073741824 == 0 ? 0 : 1);
        this.mappedBuffers = new MappedByteBuffer[requiredBuffers];
        int index = 0;
        long offset = 0;
        while (offset < this.size) {
            try {
                this.mappedBuffers[index] = channel.map(mapMode, offset, Math.min(this.size - offset, 1073741824));
                this.mappedBuffers[index].load();
                index++;
                offset += 1073741824;
            } catch (IOException e) {
                close();
                throw e;
            } catch (RuntimeException e2) {
                close();
                throw e2;
            }
        }
        if (index != requiredBuffers) {
            throw new Error("Should never happen - " + index + " != " + requiredBuffers);
        }
    }

    public FileChannel getChannel() {
        return this.channel;
    }

    public int read() {
        try {
            int mapN = (int) (this.pos / 1073741824);
            int offN = (int) (this.pos % 1073741824);
            if (mapN >= this.mappedBuffers.length || offN >= this.mappedBuffers[mapN].limit()) {
                return -1;
            }
            byte b = this.mappedBuffers[mapN].get(offN);
            this.pos++;
            return b & 255;
        } catch (BufferUnderflowException e) {
            return -1;
        }
    }

    public int read(byte[] bytes, int off, int len) {
        int mapN = (int) (this.pos / 1073741824);
        int offN = (int) (this.pos % 1073741824);
        int totalRead = 0;
        while (totalRead < len && mapN < this.mappedBuffers.length) {
            MappedByteBuffer currentBuffer = this.mappedBuffers[mapN];
            if (offN > currentBuffer.limit()) {
                break;
            }
            currentBuffer.position(offN);
            int bytesFromThisBuffer = Math.min(len - totalRead, currentBuffer.remaining());
            currentBuffer.get(bytes, off, bytesFromThisBuffer);
            off += bytesFromThisBuffer;
            this.pos += (long) bytesFromThisBuffer;
            totalRead += bytesFromThisBuffer;
            mapN++;
            offN = 0;
        }
        return totalRead == 0 ? -1 : totalRead;
    }

    public long getFilePointer() {
        return this.pos;
    }

    public void seek(long pos) {
        this.pos = pos;
    }

    public long length() {
        return this.size;
    }

    public void close() throws IOException {
        for (int i = 0; i < this.mappedBuffers.length; i++) {
            if (this.mappedBuffers[i] != null) {
                clean(this.mappedBuffers[i]);
                this.mappedBuffers[i] = null;
            }
        }
        if (this.channel != null) {
            this.channel.close();
        }
        this.channel = null;
    }

    protected void finalize() throws Throwable {
        close();
        super.finalize();
    }

    public static boolean clean(final ByteBuffer buffer) {
        if (buffer == null || !buffer.isDirect()) {
            return false;
        }
        return ((Boolean) AccessController.doPrivileged(new PrivilegedAction<Boolean>() {
            public Boolean run() {
                Boolean success = Boolean.FALSE;
                try {
                    Method getCleanerMethod = buffer.getClass().getMethod("cleaner", (Class[]) null);
                    getCleanerMethod.setAccessible(true);
                    Object cleaner = getCleanerMethod.invoke(buffer, (Object[]) null);
                    cleaner.getClass().getMethod("clean", (Class[]) null).invoke(cleaner, (Object[]) null);
                    return Boolean.TRUE;
                } catch (Exception e) {
                    return success;
                }
            }
        })).booleanValue();
    }
}
