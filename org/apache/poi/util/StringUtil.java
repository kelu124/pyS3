package org.apache.poi.util;

import com.google.zxing.pdf417.PDF417Common;
import com.itextpdf.text.pdf.codec.wmf.MetaDo;
import com.itextpdf.text.xml.xmp.XmpWriter;
import com.lee.ultrascan.model.TgcCurve;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

@Internal
public class StringUtil {
    protected static final Charset ISO_8859_1 = Charset.forName("ISO-8859-1");
    protected static final Charset UTF16LE = Charset.forName(XmpWriter.UTF16LE);
    public static final Charset UTF8 = Charset.forName(XmpWriter.UTF8);
    private static Map<Integer, Integer> msCodepointToUnicode;
    private static final int[] symbolMap_f020 = new int[]{32, 33, 8704, 35, 8707, 37, 38, 8717, 40, 41, 8727, 43, 44, 8722, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 8773, 913, 914, TgcCurve.TGC_CURVE_SIZE, 916, 917, 934, 915, 919, 921, 977, 922, 923, 924, 925, 927, PDF417Common.MAX_CODEWORDS_IN_BARCODE, 920, PDF417Common.NUMBER_OF_CODEWORDS, 931, CodePageUtil.CP_SJIS, 933, 962, 937, 926, CodePageUtil.CP_GBK, 918, 91, 8765, 93, 8869, 95, 32, 945, 946, 967, 948, CodePageUtil.CP_MS949, 966, 947, 951, 953, 981, 954, 955, 956, 957, 959, 960, 952, 961, 963, 964, 965, 982, 969, 958, 968, 950, 123, 124, 125, 8764, 32};
    private static final int[] symbolMap_f0a0 = new int[]{8364, 978, 8242, 8804, 8260, 8734, 402, 9827, 9830, 9829, 9824, 8596, 8591, 8593, 8594, 8595, 176, 177, 8243, 8805, 215, 181, 8706, 8729, MetaDo.META_CREATEPALETTE, 8800, 8801, 8776, 8230, 9168, 9135, 8629, 8501, 8475, 8476, 8472, 8855, 8853, 8709, 8745, 8746, 8835, 8839, 8836, 8834, 8838, 8712, 8713, 8736, 8711, 174, 169, 8482, 8719, 8730, 8901, 172, 8743, 8744, 8660, 8656, 8657, 8658, 8659, 9674, 9001, 174, 169, 8482, 8721, 9115, 9116, 9117, 9121, 9122, 9123, 9127, 9128, 9129, 9130, 32, 9002, 8747, 8992, 9134, 8993, 9118, 9119, 9120, 9124, 9125, 9126, 9131, 9132, 9133, 32};

    private StringUtil() {
    }

    public static String getFromUnicodeLE(byte[] string, int offset, int len) throws ArrayIndexOutOfBoundsException, IllegalArgumentException {
        if (offset < 0 || offset >= string.length) {
            throw new ArrayIndexOutOfBoundsException("Illegal offset " + offset + " (String data is of length " + string.length + ")");
        } else if (len >= 0 && (string.length - offset) / 2 >= len) {
            return new String(string, offset, len * 2, UTF16LE);
        } else {
            throw new IllegalArgumentException("Illegal length " + len);
        }
    }

    public static String getFromUnicodeLE(byte[] string) {
        if (string.length == 0) {
            return "";
        }
        return getFromUnicodeLE(string, 0, string.length / 2);
    }

    public static byte[] getToUnicodeLE(String string) {
        return string.getBytes(UTF16LE);
    }

    public static String getFromCompressedUnicode(byte[] string, int offset, int len) {
        return new String(string, offset, Math.min(len, string.length - offset), ISO_8859_1);
    }

    public static String readCompressedUnicode(LittleEndianInput in, int nChars) {
        byte[] buf = new byte[nChars];
        in.readFully(buf);
        return new String(buf, ISO_8859_1);
    }

    public static String readUnicodeString(LittleEndianInput in) {
        int nChars = in.readUShort();
        if ((in.readByte() & 1) == 0) {
            return readCompressedUnicode(in, nChars);
        }
        return readUnicodeLE(in, nChars);
    }

    public static String readUnicodeString(LittleEndianInput in, int nChars) {
        if ((in.readByte() & 1) == 0) {
            return readCompressedUnicode(in, nChars);
        }
        return readUnicodeLE(in, nChars);
    }

    public static void writeUnicodeString(LittleEndianOutput out, String value) {
        out.writeShort(value.length());
        boolean is16Bit = hasMultibyte(value);
        out.writeByte(is16Bit ? 1 : 0);
        if (is16Bit) {
            putUnicodeLE(value, out);
        } else {
            putCompressedUnicode(value, out);
        }
    }

    public static void writeUnicodeStringFlagAndData(LittleEndianOutput out, String value) {
        boolean is16Bit = hasMultibyte(value);
        out.writeByte(is16Bit ? 1 : 0);
        if (is16Bit) {
            putUnicodeLE(value, out);
        } else {
            putCompressedUnicode(value, out);
        }
    }

    public static int getEncodedSize(String value) {
        return 3 + ((hasMultibyte(value) ? 2 : 1) * value.length());
    }

    public static void putCompressedUnicode(String input, byte[] output, int offset) {
        byte[] bytes = input.getBytes(ISO_8859_1);
        System.arraycopy(bytes, 0, output, offset, bytes.length);
    }

    public static void putCompressedUnicode(String input, LittleEndianOutput out) {
        out.write(input.getBytes(ISO_8859_1));
    }

    public static void putUnicodeLE(String input, byte[] output, int offset) {
        byte[] bytes = input.getBytes(UTF16LE);
        System.arraycopy(bytes, 0, output, offset, bytes.length);
    }

    public static void putUnicodeLE(String input, LittleEndianOutput out) {
        out.write(input.getBytes(UTF16LE));
    }

    public static String readUnicodeLE(LittleEndianInput in, int nChars) {
        byte[] bytes = new byte[(nChars * 2)];
        in.readFully(bytes);
        return new String(bytes, UTF16LE);
    }

    public static String getPreferredEncoding() {
        return ISO_8859_1.name();
    }

    public static boolean hasMultibyte(String value) {
        if (value == null) {
            return false;
        }
        for (char c : value.toCharArray()) {
            if (c > 'ÿ') {
                return true;
            }
        }
        return false;
    }

    public static boolean isUnicodeString(String value) {
        return !value.equals(new String(value.getBytes(ISO_8859_1), ISO_8859_1));
    }

    public static boolean startsWithIgnoreCase(String haystack, String prefix) {
        return haystack.regionMatches(true, 0, prefix, 0, prefix.length());
    }

    public static boolean endsWithIgnoreCase(String haystack, String suffix) {
        int length = suffix.length();
        return haystack.regionMatches(true, haystack.length() - length, suffix, 0, length);
    }

    public static String mapMsCodepointString(String string) {
        if (string == null || "".equals(string)) {
            return string;
        }
        initMsCodepointMap();
        StringBuilder sb = new StringBuilder();
        int length = string.length();
        int offset = 0;
        while (offset < length) {
            Integer msCodepoint = Integer.valueOf(string.codePointAt(offset));
            Integer uniCodepoint = (Integer) msCodepointToUnicode.get(msCodepoint);
            if (uniCodepoint == null) {
                uniCodepoint = msCodepoint;
            }
            sb.appendCodePoint(uniCodepoint.intValue());
            offset += Character.charCount(msCodepoint.intValue());
        }
        return sb.toString();
    }

    public static synchronized void mapMsCodepoint(int msCodepoint, int unicodeCodepoint) {
        synchronized (StringUtil.class) {
            initMsCodepointMap();
            msCodepointToUnicode.put(Integer.valueOf(msCodepoint), Integer.valueOf(unicodeCodepoint));
        }
    }

    private static synchronized void initMsCodepointMap() {
        synchronized (StringUtil.class) {
            if (msCodepointToUnicode == null) {
                int i;
                msCodepointToUnicode = new HashMap();
                int[] arr$ = symbolMap_f020;
                int len$ = arr$.length;
                int i$ = 0;
                int i2 = 61472;
                while (i$ < len$) {
                    i = i2 + 1;
                    msCodepointToUnicode.put(Integer.valueOf(i2), Integer.valueOf(arr$[i$]));
                    i$++;
                    i2 = i;
                }
                arr$ = symbolMap_f0a0;
                len$ = arr$.length;
                i$ = 0;
                i2 = 61600;
                while (i$ < len$) {
                    i = i2 + 1;
                    msCodepointToUnicode.put(Integer.valueOf(i2), Integer.valueOf(arr$[i$]));
                    i$++;
                    i2 = i;
                }
            }
        }
    }

    @Internal
    public static String join(Object[] array, String separator) {
        if (array.length == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(array[0]);
        for (int i = 1; i < array.length; i++) {
            sb.append(separator).append(array[i]);
        }
        return sb.toString();
    }

    @Internal
    public static String join(String separator, Object... array) {
        return join(array, separator);
    }
}
