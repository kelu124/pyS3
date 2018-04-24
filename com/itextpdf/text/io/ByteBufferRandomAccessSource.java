package com.itextpdf.text.io;

import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.security.AccessController;
import java.security.PrivilegedAction;

class ByteBufferRandomAccessSource implements RandomAccessSource {
    private final ByteBuffer byteBuffer;

    public ByteBufferRandomAccessSource(ByteBuffer byteBuffer) {
        this.byteBuffer = byteBuffer;
    }

    public int get(long position) throws IOException {
        if (position > 2147483647L) {
            throw new IllegalArgumentException("Position must be less than Integer.MAX_VALUE");
        }
        try {
            if (position >= ((long) this.byteBuffer.limit())) {
                return -1;
            }
            return this.byteBuffer.get((int) position) & 255;
        } catch (BufferUnderflowException e) {
            return -1;
        }
    }

    public int get(long position, byte[] bytes, int off, int len) throws IOException {
        if (position > 2147483647L) {
            throw new IllegalArgumentException("Position must be less than Integer.MAX_VALUE");
        } else if (position >= ((long) this.byteBuffer.limit())) {
            return -1;
        } else {
            this.byteBuffer.position((int) position);
            int bytesFromThisBuffer = Math.min(len, this.byteBuffer.remaining());
            this.byteBuffer.get(bytes, off, bytesFromThisBuffer);
            return bytesFromThisBuffer;
        }
    }

    public long length() {
        return (long) this.byteBuffer.limit();
    }

    public void close() throws IOException {
        clean(this.byteBuffer);
    }

    private static boolean clean(final ByteBuffer buffer) {
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
