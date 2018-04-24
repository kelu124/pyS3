package com.itextpdf.text.pdf.fonts.cmaps;

import com.itextpdf.text.pdf.PdfArray;
import com.itextpdf.text.pdf.PdfEncodings;
import com.itextpdf.text.pdf.PdfNumber;
import com.itextpdf.text.pdf.PdfObject;
import com.itextpdf.text.pdf.PdfString;

public abstract class AbstractCMap {
    private String cmapName;
    private String ordering;
    private String registry;
    private int supplement;

    abstract void addChar(PdfString pdfString, PdfObject pdfObject);

    public String getName() {
        return this.cmapName;
    }

    void setName(String cmapName) {
        this.cmapName = cmapName;
    }

    public String getOrdering() {
        return this.ordering;
    }

    void setOrdering(String ordering) {
        this.ordering = ordering;
    }

    public String getRegistry() {
        return this.registry;
    }

    void setRegistry(String registry) {
        this.registry = registry;
    }

    public int getSupplement() {
        return this.supplement;
    }

    void setSupplement(int supplement) {
        this.supplement = supplement;
    }

    void addRange(PdfString from, PdfString to, PdfObject code) {
        byte[] a1 = decodeStringToByte(from);
        byte[] a2 = decodeStringToByte(to);
        if (a1.length != a2.length || a1.length == 0) {
            throw new IllegalArgumentException("Invalid map.");
        }
        byte[] sout = null;
        if (code instanceof PdfString) {
            sout = decodeStringToByte((PdfString) code);
        }
        int start = a1[a1.length - 1] & 255;
        int end = a2[a2.length - 1] & 255;
        for (int k = start; k <= end; k++) {
            a1[a1.length - 1] = (byte) k;
            PdfString s = new PdfString(a1);
            s.setHexWriting(true);
            if (code instanceof PdfArray) {
                addChar(s, ((PdfArray) code).getPdfObject(k - start));
            } else if (code instanceof PdfNumber) {
                addChar(s, new PdfNumber((((PdfNumber) code).intValue() + k) - start));
            } else if (code instanceof PdfString) {
                PdfString s1 = new PdfString(sout);
                s1.setHexWriting(true);
                int length = sout.length - 1;
                sout[length] = (byte) (sout[length] + 1);
                addChar(s, s1);
            }
        }
    }

    public static byte[] decodeStringToByte(PdfString s) {
        byte[] b = s.getBytes();
        byte[] br = new byte[b.length];
        System.arraycopy(b, 0, br, 0, b.length);
        return br;
    }

    public String decodeStringToUnicode(PdfString ps) {
        if (ps.isHexWriting()) {
            return PdfEncodings.convertToString(ps.getBytes(), "UnicodeBigUnmarked");
        }
        return ps.toUnicodeString();
    }
}
