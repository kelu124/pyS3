package com.itextpdf.text.html;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.pdf.BaseField;
import java.util.HashMap;
import java.util.Properties;
import java.util.StringTokenizer;

@Deprecated
public class HtmlUtilities {
    public static final float DEFAULT_FONT_SIZE = 12.0f;
    public static final int[] FONTSIZES = new int[]{8, 10, 12, 14, 18, 24, 36};
    private static HashMap<String, Float> sizes = new HashMap();

    static {
        sizes.put("xx-small", new Float(4.0f));
        sizes.put("x-small", new Float(6.0f));
        sizes.put("small", new Float(8.0f));
        sizes.put("medium", new Float(10.0f));
        sizes.put("large", new Float(13.0f));
        sizes.put("x-large", new Float(18.0f));
        sizes.put("xx-large", new Float(26.0f));
    }

    public static float parseLength(String string) {
        return parseLength(string, DEFAULT_FONT_SIZE);
    }

    public static float parseLength(String string, float actualFontSize) {
        if (string == null) {
            return 0.0f;
        }
        Float fl = (Float) sizes.get(string.toLowerCase());
        if (fl != null) {
            return fl.floatValue();
        }
        int pos = 0;
        int length = string.length();
        boolean ok = true;
        while (ok && pos < length) {
            switch (string.charAt(pos)) {
                case '+':
                case '-':
                case '.':
                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9':
                    pos++;
                    break;
                default:
                    ok = false;
                    break;
            }
        }
        if (pos == 0) {
            return 0.0f;
        }
        if (pos == length) {
            return Float.parseFloat(string + "f");
        }
        float f = Float.parseFloat(string.substring(0, pos) + "f");
        string = string.substring(pos);
        if (string.startsWith("in")) {
            return f * 72.0f;
        }
        if (string.startsWith("cm")) {
            return (f / 2.54f) * 72.0f;
        }
        if (string.startsWith("mm")) {
            return (f / 25.4f) * 72.0f;
        }
        if (string.startsWith("pc")) {
            return f * DEFAULT_FONT_SIZE;
        }
        if (string.startsWith(HtmlTags.EM)) {
            return f * actualFontSize;
        }
        if (string.startsWith("ex")) {
            return (f * actualFontSize) / BaseField.BORDER_WIDTH_MEDIUM;
        }
        return f;
    }

    public static BaseColor decodeColor(String s) {
        BaseColor baseColor = null;
        if (s != null) {
            try {
                baseColor = WebColors.getRGBColor(s.toLowerCase().trim());
            } catch (IllegalArgumentException e) {
            }
        }
        return baseColor;
    }

    public static Properties parseAttributes(String string) {
        Properties result = new Properties();
        if (string != null) {
            StringTokenizer keyValuePairs = new StringTokenizer(string, ";");
            while (keyValuePairs.hasMoreTokens()) {
                StringTokenizer keyValuePair = new StringTokenizer(keyValuePairs.nextToken(), ":");
                if (keyValuePair.hasMoreTokens()) {
                    String key = keyValuePair.nextToken().trim();
                    if (keyValuePair.hasMoreTokens()) {
                        String value = keyValuePair.nextToken().trim();
                        if (value.startsWith("\"")) {
                            value = value.substring(1);
                        }
                        if (value.endsWith("\"")) {
                            value = value.substring(0, value.length() - 1);
                        }
                        result.setProperty(key.toLowerCase(), value);
                    }
                }
            }
        }
        return result;
    }

    public static String removeComment(String string, String startComment, String endComment) {
        StringBuffer result = new StringBuffer();
        int pos = 0;
        int end = endComment.length();
        int start = string.indexOf(startComment, 0);
        while (start > -1) {
            result.append(string.substring(pos, start));
            pos = string.indexOf(endComment, start) + end;
            start = string.indexOf(startComment, pos);
        }
        result.append(string.substring(pos));
        return result.toString();
    }

    public static String eliminateWhiteSpace(String content) {
        StringBuffer buf = new StringBuffer();
        int len = content.length();
        boolean newline = false;
        for (int i = 0; i < len; i++) {
            char character = content.charAt(i);
            switch (character) {
                case '\t':
                case '\r':
                    break;
                case '\n':
                    if (i <= 0) {
                        break;
                    }
                    newline = true;
                    buf.append(' ');
                    break;
                case ' ':
                    if (!newline) {
                        buf.append(character);
                        break;
                    }
                    break;
                default:
                    newline = false;
                    buf.append(character);
                    break;
            }
        }
        return buf.toString();
    }

    public static int getIndexedFontSize(String value, String previous) {
        int sIndex = 0;
        if (value.startsWith("+") || value.startsWith("-")) {
            if (previous == null) {
                previous = "12";
            }
            int c = (int) Float.parseFloat(previous);
            for (int k = FONTSIZES.length - 1; k >= 0; k--) {
                if (c >= FONTSIZES[k]) {
                    sIndex = k;
                    break;
                }
            }
            if (value.startsWith("+")) {
                value = value.substring(1);
            }
            sIndex += Integer.parseInt(value);
        } else {
            try {
                sIndex = Integer.parseInt(value) - 1;
            } catch (NumberFormatException e) {
                sIndex = 0;
            }
        }
        if (sIndex < 0) {
            sIndex = 0;
        } else if (sIndex >= FONTSIZES.length) {
            sIndex = FONTSIZES.length - 1;
        }
        return FONTSIZES[sIndex];
    }

    public static int alignmentValue(String alignment) {
        if (alignment == null) {
            return -1;
        }
        if (HtmlTags.ALIGN_CENTER.equalsIgnoreCase(alignment)) {
            return 1;
        }
        if (HtmlTags.ALIGN_LEFT.equalsIgnoreCase(alignment)) {
            return 0;
        }
        if (HtmlTags.ALIGN_RIGHT.equalsIgnoreCase(alignment)) {
            return 2;
        }
        if (HtmlTags.ALIGN_JUSTIFY.equalsIgnoreCase(alignment)) {
            return 3;
        }
        if (HtmlTags.ALIGN_JUSTIFIED_ALL.equalsIgnoreCase(alignment)) {
            return 8;
        }
        if (HtmlTags.ALIGN_TOP.equalsIgnoreCase(alignment)) {
            return 4;
        }
        if (HtmlTags.ALIGN_MIDDLE.equalsIgnoreCase(alignment)) {
            return 5;
        }
        if (HtmlTags.ALIGN_BOTTOM.equalsIgnoreCase(alignment)) {
            return 6;
        }
        if (HtmlTags.ALIGN_BASELINE.equalsIgnoreCase(alignment)) {
            return 7;
        }
        return -1;
    }
}
