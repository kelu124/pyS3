package com.itextpdf.text.pdf.fonts.cmaps;

import com.itextpdf.text.error_messages.MessageLocalization;
import com.itextpdf.text.pdf.PRTokeniser;
import com.itextpdf.text.pdf.PdfContentParser;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfNumber;
import com.itextpdf.text.pdf.PdfObject;
import com.itextpdf.text.pdf.PdfString;
import java.io.IOException;
import java.util.ArrayList;
import org.bytedeco.javacpp.avutil;

public class CMapParserEx {
    private static final PdfName CMAPNAME = new PdfName("CMapName");
    private static final String DEF = "def";
    private static final String ENDBFCHAR = "endbfchar";
    private static final String ENDBFRANGE = "endbfrange";
    private static final String ENDCIDCHAR = "endcidchar";
    private static final String ENDCIDRANGE = "endcidrange";
    private static final int MAXLEVEL = 10;
    private static final String USECMAP = "usecmap";

    public static void parseCid(String cmapName, AbstractCMap cmap, CidLocation location) throws IOException {
        parseCid(cmapName, cmap, location, 0);
    }

    private static void parseCid(String cmapName, AbstractCMap cmap, CidLocation location, int level) throws IOException {
        if (level < 10) {
            PRTokeniser inp = location.getLocation(cmapName);
            ArrayList<PdfObject> list = new ArrayList();
            PdfContentParser cp = new PdfContentParser(inp);
            int maxExc = 50;
            while (true) {
                try {
                    cp.parse(list);
                    if (list.isEmpty()) {
                        break;
                    }
                    String last = ((PdfObject) list.get(list.size() - 1)).toString();
                    if (level == 0 && list.size() == 3 && last.equals(DEF)) {
                        PdfObject key = (PdfObject) list.get(0);
                        if (PdfName.REGISTRY.equals(key)) {
                            cmap.setRegistry(((PdfObject) list.get(1)).toString());
                        } else if (PdfName.ORDERING.equals(key)) {
                            cmap.setOrdering(((PdfObject) list.get(1)).toString());
                        } else if (CMAPNAME.equals(key)) {
                            cmap.setName(((PdfObject) list.get(1)).toString());
                        } else if (PdfName.SUPPLEMENT.equals(key)) {
                            try {
                                cmap.setSupplement(((PdfNumber) list.get(1)).intValue());
                            } catch (Exception e) {
                            }
                        }
                    } else {
                        try {
                            int lmax;
                            int k;
                            if ((last.equals(ENDCIDCHAR) || last.equals(ENDBFCHAR)) && list.size() >= 3) {
                                lmax = list.size() - 2;
                                for (k = 0; k < lmax; k += 2) {
                                    if (list.get(k) instanceof PdfString) {
                                        cmap.addChar((PdfString) list.get(k), (PdfObject) list.get(k + 1));
                                    }
                                }
                            } else if ((last.equals(ENDCIDRANGE) || last.equals(ENDBFRANGE)) && list.size() >= 4) {
                                lmax = list.size() - 3;
                                k = 0;
                                while (k < lmax) {
                                    if ((list.get(k) instanceof PdfString) && (list.get(k + 1) instanceof PdfString)) {
                                        cmap.addRange((PdfString) list.get(k), (PdfString) list.get(k + 1), (PdfObject) list.get(k + 2));
                                    }
                                    k += 3;
                                }
                            } else if (last.equals(USECMAP) && list.size() == 2 && (list.get(0) instanceof PdfName)) {
                                parseCid(PdfName.decodeName(((PdfObject) list.get(0)).toString()), cmap, location, level + 1);
                            }
                        } catch (Throwable th) {
                            inp.close();
                        }
                    }
                } catch (Exception e2) {
                    maxExc--;
                    if (maxExc < 0) {
                    }
                }
            }
            inp.close();
        }
    }

    private static void encodeSequence(int size, byte[] seqs, char cid, ArrayList<char[]> planes) {
        char[] plane;
        int one;
        size--;
        int nextPlane = 0;
        int idx = 0;
        while (idx < size) {
            plane = (char[]) planes.get(nextPlane);
            one = seqs[idx] & 255;
            char c = plane[one];
            if (c == '\u0000' || (c & 32768) != 0) {
                if (c == '\u0000') {
                    planes.add(new char[256]);
                    c = (char) ((planes.size() - 1) | 32768);
                    plane[one] = c;
                }
                nextPlane = c & avutil.FF_LAMBDA_MAX;
                idx++;
            } else {
                throw new RuntimeException(MessageLocalization.getComposedMessage("inconsistent.mapping", new Object[0]));
            }
        }
        plane = (char[]) planes.get(nextPlane);
        one = seqs[size] & 255;
        if ((plane[one] & 32768) != 0) {
            throw new RuntimeException(MessageLocalization.getComposedMessage("inconsistent.mapping", new Object[0]));
        }
        plane[one] = cid;
    }
}
