package com.itextpdf.text.pdf;

import com.itextpdf.text.DocWriter;

public class StringUtils {
    private static final byte[] f142b = DocWriter.getISOBytes("\\b");
    private static final byte[] f143f = DocWriter.getISOBytes("\\f");
    private static final byte[] f144n = DocWriter.getISOBytes("\\n");
    private static final byte[] f145r = DocWriter.getISOBytes("\\r");
    private static final byte[] f146t = DocWriter.getISOBytes("\\t");

    private StringUtils() {
    }

    public static byte[] escapeString(byte[] bytes) {
        ByteBuffer content = new ByteBuffer();
        escapeString(bytes, content);
        return content.toByteArray();
    }

    public static void escapeString(byte[] bytes, ByteBuffer content) {
        content.append_i(40);
        for (byte c : bytes) {
            switch (c) {
                case (byte) 8:
                    content.append(f142b);
                    break;
                case (byte) 9:
                    content.append(f146t);
                    break;
                case (byte) 10:
                    content.append(f144n);
                    break;
                case (byte) 12:
                    content.append(f143f);
                    break;
                case (byte) 13:
                    content.append(f145r);
                    break;
                case (byte) 40:
                case (byte) 41:
                case (byte) 92:
                    content.append_i(92).append_i(c);
                    break;
                default:
                    content.append_i(c);
                    break;
            }
        }
        content.append_i(41);
    }
}
