package org.apache.poi.util;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PushbackInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.util.zip.CRC32;
import java.util.zip.Checksum;
import org.apache.poi.EmptyFileException;

public final class IOUtils {
    private static final POILogger logger = POILogFactory.getLogger(IOUtils.class);

    private IOUtils() {
    }

    public static byte[] peekFirst8Bytes(InputStream stream) throws IOException, EmptyFileException {
        stream.mark(8);
        byte[] header = new byte[8];
        int read = readFully(stream, header);
        if (read < 1) {
            throw new EmptyFileException();
        }
        if (stream instanceof PushbackInputStream) {
            ((PushbackInputStream) stream).unread(header, 0, read);
        } else {
            stream.reset();
        }
        return header;
    }

    public static byte[] toByteArray(InputStream stream) throws IOException {
        return toByteArray(stream, Integer.MAX_VALUE);
    }

    public static byte[] toByteArray(InputStream stream, int length) throws IOException {
        int i;
        if (length == Integer.MAX_VALUE) {
            i = 4096;
        } else {
            i = length;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream(i);
        byte[] buffer = new byte[4096];
        int totalBytes = 0;
        int readBytes;
        do {
            readBytes = stream.read(buffer, 0, Math.min(buffer.length, length - totalBytes));
            totalBytes += Math.max(readBytes, 0);
            if (readBytes > 0) {
                baos.write(buffer, 0, readBytes);
            }
            if (totalBytes >= length) {
                break;
            }
        } while (readBytes > -1);
        if (length == Integer.MAX_VALUE || totalBytes >= length) {
            return baos.toByteArray();
        }
        throw new IOException("unexpected EOF");
    }

    public static byte[] toByteArray(ByteBuffer buffer, int length) {
        if (buffer.hasArray() && buffer.arrayOffset() == 0) {
            return buffer.array();
        }
        byte[] data = new byte[length];
        buffer.get(data);
        return data;
    }

    public static int readFully(InputStream in, byte[] b) throws IOException {
        return readFully(in, b, 0, b.length);
    }

    public static int readFully(InputStream in, byte[] b, int off, int len) throws IOException {
        int total = 0;
        do {
            int got = in.read(b, off + total, len - total);
            if (got >= 0) {
                total += got;
            } else if (total == 0) {
                return -1;
            } else {
                return total;
            }
        } while (total != len);
        return total;
    }

    public static int readFully(ReadableByteChannel channel, ByteBuffer b) throws IOException {
        int total = 0;
        do {
            int got = channel.read(b);
            if (got >= 0) {
                total += got;
                if (total == b.capacity()) {
                    break;
                }
            } else if (total == 0) {
                return -1;
            } else {
                return total;
            }
        } while (b.position() != b.capacity());
        return total;
    }

    public static void copy(InputStream inp, OutputStream out) throws IOException {
        byte[] buff = new byte[4096];
        while (true) {
            int count = inp.read(buff);
            if (count == -1) {
                return;
            }
            if (count > 0) {
                out.write(buff, 0, count);
            }
        }
    }

    public static long calculateChecksum(byte[] data) {
        Checksum sum = new CRC32();
        sum.update(data, 0, data.length);
        return sum.getValue();
    }

    public static long calculateChecksum(InputStream stream) throws IOException {
        Checksum sum = new CRC32();
        byte[] buf = new byte[4096];
        while (true) {
            int count = stream.read(buf);
            if (count == -1) {
                return sum.getValue();
            }
            if (count > 0) {
                sum.update(buf, 0, count);
            }
        }
    }

    public static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Exception exc) {
                logger.log(7, "Unable to close resource: " + exc, exc);
            }
        }
    }
}
