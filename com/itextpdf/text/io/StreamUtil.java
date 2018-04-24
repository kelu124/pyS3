package com.itextpdf.text.io;

import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public final class StreamUtil {
    private StreamUtil() {
    }

    public static byte[] inputStreamToArray(InputStream is) throws IOException {
        byte[] b = new byte[8192];
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        while (true) {
            int read = is.read(b);
            if (read < 1) {
                out.close();
                return out.toByteArray();
            }
            out.write(b, 0, read);
        }
    }

    public static void CopyBytes(RandomAccessSource source, long start, long length, OutputStream outs) throws IOException {
        if (length > 0) {
            long idx = start;
            byte[] buf = new byte[8192];
            while (length > 0) {
                long n = (long) source.get(idx, buf, 0, (int) Math.min((long) buf.length, length));
                if (n <= 0) {
                    throw new EOFException();
                }
                outs.write(buf, 0, (int) n);
                idx += n;
                length -= n;
            }
        }
    }

    public static InputStream getResourceStream(String key) {
        return getResourceStream(key, null);
    }

    public static InputStream getResourceStream(String key, ClassLoader loader) {
        if (key.startsWith("/")) {
            key = key.substring(1);
        }
        InputStream inputStream = null;
        if (loader != null) {
            inputStream = loader.getResourceAsStream(key);
            if (inputStream != null) {
                return inputStream;
            }
        }
        try {
            ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
            if (contextClassLoader != null) {
                inputStream = contextClassLoader.getResourceAsStream(key);
            }
        } catch (Throwable th) {
        }
        if (inputStream == null) {
            inputStream = StreamUtil.class.getResourceAsStream("/" + key);
        }
        if (inputStream == null) {
            inputStream = ClassLoader.getSystemResourceAsStream(key);
        }
        return inputStream;
    }
}
