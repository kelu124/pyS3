package org.apache.poi.hpsf;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collection;
import java.util.Date;
import org.apache.poi.util.SuppressForbidden;

public class Util {
    public static final long EPOCH_DIFF = 11644473600000L;

    public static boolean equal(byte[] a, byte[] b) {
        if (a.length != b.length) {
            return false;
        }
        for (int i = 0; i < a.length; i++) {
            if (a[i] != b[i]) {
                return false;
            }
        }
        return true;
    }

    public static void copy(byte[] src, int srcOffset, int length, byte[] dst, int dstOffset) {
        for (int i = 0; i < length; i++) {
            dst[dstOffset + i] = src[srcOffset + i];
        }
    }

    public static byte[] cat(byte[][] byteArrays) {
        int i;
        int capacity = 0;
        for (byte[] length : byteArrays) {
            capacity += length.length;
        }
        byte[] result = new byte[capacity];
        int r = 0;
        for (i = 0; i < byteArrays.length; i++) {
            int j = 0;
            while (j < byteArrays[i].length) {
                int r2 = r + 1;
                result[r] = byteArrays[i][j];
                j++;
                r = r2;
            }
        }
        return result;
    }

    public static byte[] copy(byte[] src, int offset, int length) {
        byte[] result = new byte[length];
        copy(src, offset, length, result, 0);
        return result;
    }

    public static Date filetimeToDate(int high, int low) {
        return filetimeToDate((((long) high) << 32) | (((long) low) & 4294967295L));
    }

    public static Date filetimeToDate(long filetime) {
        return new Date((filetime / 10000) - EPOCH_DIFF);
    }

    public static long dateToFileTime(Date date) {
        return 10000 * (date.getTime() + EPOCH_DIFF);
    }

    public static boolean equals(Collection<?> c1, Collection<?> c2) {
        return internalEquals(c1.toArray(), c2.toArray());
    }

    public static boolean equals(Object[] c1, Object[] c2) {
        return internalEquals((Object[]) c1.clone(), (Object[]) c2.clone());
    }

    private static boolean internalEquals(Object[] o1, Object[] o2) {
        for (Object obj1 : o1) {
            boolean matchFound = false;
            int i2 = 0;
            while (!matchFound && i2 < o1.length) {
                if (obj1.equals(o2[i2])) {
                    matchFound = true;
                    o2[i2] = null;
                }
                i2++;
            }
            if (!matchFound) {
                return false;
            }
        }
        return true;
    }

    public static byte[] pad4(byte[] ba) {
        int l = ba.length % 4;
        if (l == 0) {
            return ba;
        }
        byte[] result = new byte[(ba.length + (4 - l))];
        System.arraycopy(ba, 0, result, 0, ba.length);
        return result;
    }

    public static char[] pad4(char[] ca) {
        int l = ca.length % 4;
        if (l == 0) {
            return ca;
        }
        char[] result = new char[(ca.length + (4 - l))];
        System.arraycopy(ca, 0, result, 0, ca.length);
        return result;
    }

    public static char[] pad4(String s) {
        return pad4(s.toCharArray());
    }

    @SuppressForbidden("uses printStackTrace")
    public static String toString(Throwable t) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        t.printStackTrace(pw);
        pw.close();
        try {
            sw.close();
            return sw.toString();
        } catch (IOException e) {
            StringBuffer b = new StringBuffer(t.getMessage());
            b.append("\n");
            b.append("Could not create a stacktrace. Reason: ");
            b.append(e.getMessage());
            return b.toString();
        }
    }
}
