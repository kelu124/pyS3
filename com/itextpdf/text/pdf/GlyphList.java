package com.itextpdf.text.pdf;

import com.itextpdf.text.io.StreamUtil;
import com.itextpdf.text.pdf.fonts.FontsResourceAnchor;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.StringTokenizer;

public class GlyphList {
    private static HashMap<String, int[]> names2unicode = new HashMap();
    private static HashMap<Integer, String> unicode2names = new HashMap();

    static {
        InputStream is = null;
        try {
            is = StreamUtil.getResourceStream("com/itextpdf/text/pdf/fonts/glyphlist.txt", new FontsResourceAnchor().getClass().getClassLoader());
            if (is == null) {
                throw new Exception("glyphlist.txt not found as resource. (It must exist as resource in the package com.itextpdf.text.pdf.fonts)");
            }
            byte[] buf = new byte[1024];
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            while (true) {
                int size = is.read(buf);
                if (size < 0) {
                    break;
                }
                out.write(buf, 0, size);
            }
            is.close();
            is = null;
            StringTokenizer tk = new StringTokenizer(PdfEncodings.convertToString(out.toByteArray(), null), "\r\n");
            while (tk.hasMoreTokens()) {
                String line = tk.nextToken();
                if (!line.startsWith("#")) {
                    StringTokenizer t2 = new StringTokenizer(line, " ;\r\n\t\f");
                    if (t2.hasMoreTokens()) {
                        String name = t2.nextToken();
                        if (t2.hasMoreTokens()) {
                            unicode2names.put(Integer.valueOf(t2.nextToken(), 16), name);
                            names2unicode.put(name, new int[]{num.intValue()});
                        }
                    }
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (Exception e) {
                }
            }
        } catch (Exception e2) {
            System.err.println("glyphlist.txt loading error: " + e2.getMessage());
            if (is != null) {
                try {
                    is.close();
                } catch (Exception e3) {
                }
            }
        } catch (Throwable th) {
            if (is != null) {
                try {
                    is.close();
                } catch (Exception e4) {
                }
            }
        }
    }

    public static int[] nameToUnicode(String name) {
        int[] v = (int[]) names2unicode.get(name);
        if (v != null || name.length() != 7 || !name.toLowerCase().startsWith("uni")) {
            return v;
        }
        try {
            return new int[]{Integer.parseInt(name.substring(3), 16)};
        } catch (Exception e) {
            return v;
        }
    }

    public static String unicodeToName(int num) {
        return (String) unicode2names.get(Integer.valueOf(num));
    }
}
