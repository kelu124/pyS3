package com.itextpdf.text;

import com.itextpdf.text.pdf.ByteBuffer;
import com.itextpdf.text.pdf.PRTokeniser;
import com.itextpdf.text.pdf.PdfBoolean;
import com.itextpdf.text.pdf.PdfEncodings;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Set;

public class Utilities {
    @Deprecated
    public static <K, V> Set<K> getKeySet(Hashtable<K, V> table) {
        return table == null ? Collections.emptySet() : table.keySet();
    }

    public static Object[][] addToArray(Object[][] original, Object[] item) {
        if (original == null) {
            return new Object[][]{item};
        }
        Object[][] original2 = new Object[(original.length + 1)][];
        System.arraycopy(original, 0, original2, 0, original.length);
        original2[original.length] = item;
        return original2;
    }

    public static boolean checkTrueOrFalse(Properties attributes, String key) {
        return PdfBoolean.TRUE.equalsIgnoreCase(attributes.getProperty(key));
    }

    public static String unEscapeURL(String src) {
        StringBuffer bf = new StringBuffer();
        char[] s = src.toCharArray();
        int k = 0;
        while (k < s.length) {
            char c = s[k];
            if (c != '%') {
                bf.append(c);
            } else if (k + 2 >= s.length) {
                bf.append(c);
            } else {
                int a0 = PRTokeniser.getHex(s[k + 1]);
                int a1 = PRTokeniser.getHex(s[k + 2]);
                if (a0 < 0 || a1 < 0) {
                    bf.append(c);
                } else {
                    bf.append((char) ((a0 * 16) + a1));
                    k += 2;
                }
            }
            k++;
        }
        return bf.toString();
    }

    public static URL toURL(String filename) throws MalformedURLException {
        try {
            return new URL(filename);
        } catch (Exception e) {
            return new File(filename).toURI().toURL();
        }
    }

    public static void skip(InputStream is, int size) throws IOException {
        while (size > 0) {
            long n = is.skip((long) size);
            if (n > 0) {
                size = (int) (((long) size) - n);
            } else {
                return;
            }
        }
    }

    public static final float millimetersToPoints(float value) {
        return inchesToPoints(millimetersToInches(value));
    }

    public static final float millimetersToInches(float value) {
        return value / 25.4f;
    }

    public static final float pointsToMillimeters(float value) {
        return inchesToMillimeters(pointsToInches(value));
    }

    public static final float pointsToInches(float value) {
        return value / 72.0f;
    }

    public static final float inchesToMillimeters(float value) {
        return 25.4f * value;
    }

    public static final float inchesToPoints(float value) {
        return 72.0f * value;
    }

    public static boolean isSurrogateHigh(char c) {
        return c >= '?' && c <= '?';
    }

    public static boolean isSurrogateLow(char c) {
        return c >= '?' && c <= '?';
    }

    public static boolean isSurrogatePair(String text, int idx) {
        if (idx < 0 || idx > text.length() - 2 || !isSurrogateHigh(text.charAt(idx)) || !isSurrogateLow(text.charAt(idx + 1))) {
            return false;
        }
        return true;
    }

    public static boolean isSurrogatePair(char[] text, int idx) {
        if (idx < 0 || idx > text.length - 2 || !isSurrogateHigh(text[idx]) || !isSurrogateLow(text[idx + 1])) {
            return false;
        }
        return true;
    }

    public static int convertToUtf32(char highSurrogate, char lowSurrogate) {
        return ((((highSurrogate - 55296) * 1024) + lowSurrogate) - 56320) + 65536;
    }

    public static int convertToUtf32(char[] text, int idx) {
        return ((((text[idx] - 55296) * 1024) + text[idx + 1]) - 56320) + 65536;
    }

    public static int convertToUtf32(String text, int idx) {
        return ((((text.charAt(idx) - 55296) * 1024) + text.charAt(idx + 1)) - 56320) + 65536;
    }

    public static String convertFromUtf32(int codePoint) {
        if (codePoint < 65536) {
            return Character.toString((char) codePoint);
        }
        codePoint -= 65536;
        return new String(new char[]{(char) ((codePoint / 1024) + 55296), (char) ((codePoint % 1024) + 56320)});
    }

    public static String readFileToString(String path) throws IOException {
        return readFileToString(new File(path));
    }

    public static String readFileToString(File file) throws IOException {
        byte[] jsBytes = new byte[((int) file.length())];
        new FileInputStream(file).read(jsBytes);
        return new String(jsBytes);
    }

    public static String convertToHex(byte[] bytes) {
        ByteBuffer buf = new ByteBuffer();
        for (byte b : bytes) {
            buf.appendHex(b);
        }
        return PdfEncodings.convertToString(buf.toByteArray(), null).toUpperCase();
    }
}
