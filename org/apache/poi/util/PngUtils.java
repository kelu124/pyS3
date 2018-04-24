package org.apache.poi.util;

import com.google.common.base.Ascii;

public final class PngUtils {
    private static final byte[] PNG_FILE_HEADER = new byte[]{(byte) -119, (byte) 80, (byte) 78, (byte) 71, (byte) 13, (byte) 10, Ascii.SUB, (byte) 10};

    private PngUtils() {
    }

    public static boolean matchesPngHeader(byte[] data, int offset) {
        if (data == null || data.length - offset < PNG_FILE_HEADER.length) {
            return false;
        }
        for (int i = 0; i < PNG_FILE_HEADER.length; i++) {
            if (PNG_FILE_HEADER[i] != data[i + offset]) {
                return false;
            }
        }
        return true;
    }
}
