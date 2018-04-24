package com.itextpdf.text.xml;

import com.itextpdf.text.xml.xmp.XmpWriter;
import org.apache.poi.hssf.record.UnknownRecord;

public class XMLUtil {
    public static String escapeXML(String s, boolean onlyASCII) {
        StringBuffer sb = new StringBuffer();
        for (int c : s.toCharArray()) {
            switch (c) {
                case 34:
                    sb.append("&quot;");
                    break;
                case 38:
                    sb.append("&amp;");
                    break;
                case 39:
                    sb.append("&apos;");
                    break;
                case 60:
                    sb.append("&lt;");
                    break;
                case 62:
                    sb.append("&gt;");
                    break;
                default:
                    if (isValidCharacterValue(c)) {
                        if (onlyASCII && c > 127) {
                            sb.append("&#").append(c).append(';');
                            break;
                        }
                        sb.append((char) c);
                        break;
                    }
                    break;
                    break;
            }
        }
        return sb.toString();
    }

    public static String unescapeXML(String s) {
        char[] cc = s.toCharArray();
        int len = cc.length;
        StringBuffer sb = new StringBuffer();
        int i = 0;
        while (i < len) {
            int c = cc[i];
            if (c == 38) {
                int pos = findInArray(';', cc, i + 3);
                if (pos > -1) {
                    String esc = new String(cc, i + 1, (pos - i) - 1);
                    if (esc.startsWith("#")) {
                        esc = esc.substring(1);
                        if (isValidCharacterValue(esc)) {
                            c = (char) Integer.parseInt(esc);
                            i = pos;
                        } else {
                            i = pos;
                            i++;
                        }
                    } else {
                        int tmp = unescape(esc);
                        if (tmp > 0) {
                            c = tmp;
                            i = pos;
                        }
                    }
                }
            }
            sb.append((char) c);
            i++;
        }
        return sb.toString();
    }

    public static int unescape(String s) {
        if ("apos".equals(s)) {
            return 39;
        }
        if ("quot".equals(s)) {
            return 34;
        }
        if ("lt".equals(s)) {
            return 60;
        }
        if ("gt".equals(s)) {
            return 62;
        }
        if ("amp".equals(s)) {
            return 38;
        }
        return -1;
    }

    public static boolean isValidCharacterValue(String s) {
        try {
            return isValidCharacterValue(Integer.parseInt(s));
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isValidCharacterValue(int c) {
        return c == 9 || c == 10 || c == 13 || ((c >= 32 && c <= 55295) || ((c >= 57344 && c <= 65533) || (c >= 65536 && c <= 1114111)));
    }

    public static int findInArray(char needle, char[] haystack, int start) {
        for (int i = start; i < haystack.length; i++) {
            if (haystack[i] == ';') {
                return i;
            }
        }
        return -1;
    }

    public static String getEncodingName(byte[] b4) {
        int b0 = b4[0] & 255;
        int b1 = b4[1] & 255;
        if (b0 == 254 && b1 == 255) {
            return XmpWriter.UTF16BE;
        }
        if (b0 == 255 && b1 == 254) {
            return XmpWriter.UTF16LE;
        }
        int b2 = b4[2] & 255;
        if (b0 == UnknownRecord.PHONETICPR_00EF && b1 == 187 && b2 == 191) {
            return XmpWriter.UTF8;
        }
        int b3 = b4[3] & 255;
        if (b0 == 0 && b1 == 0 && b2 == 0 && b3 == 60) {
            return "ISO-10646-UCS-4";
        }
        if (b0 == 60 && b1 == 0 && b2 == 0 && b3 == 0) {
            return "ISO-10646-UCS-4";
        }
        if (b0 == 0 && b1 == 0 && b2 == 60 && b3 == 0) {
            return "ISO-10646-UCS-4";
        }
        if (b0 == 0 && b1 == 60 && b2 == 0 && b3 == 0) {
            return "ISO-10646-UCS-4";
        }
        if (b0 == 0 && b1 == 60 && b2 == 0 && b3 == 63) {
            return XmpWriter.UTF16BE;
        }
        if (b0 == 60 && b1 == 0 && b2 == 63 && b3 == 0) {
            return XmpWriter.UTF16LE;
        }
        if (b0 == 76 && b1 == 111 && b2 == 167 && b3 == 148) {
            return "CP037";
        }
        return XmpWriter.UTF8;
    }
}
