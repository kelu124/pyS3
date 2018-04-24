package com.itextpdf.text.pdf;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.error_messages.MessageLocalization;
import com.itextpdf.text.html.HtmlTags;
import java.io.IOException;
import java.util.HashMap;

public class Type3Font extends BaseFont {
    private HashMap<Integer, Type3Glyph> char2glyph;
    private boolean colorized;
    private float llx;
    private float lly;
    private PageResources pageResources;
    private float urx;
    private float ury;
    private boolean[] usedSlot;
    private IntHashtable widths3;
    private PdfWriter writer;

    public Type3Font(PdfWriter writer, char[] chars, boolean colorized) {
        this(writer, colorized);
    }

    public Type3Font(PdfWriter writer, boolean colorized) {
        this.widths3 = new IntHashtable();
        this.char2glyph = new HashMap();
        this.llx = Float.NaN;
        this.pageResources = new PageResources();
        this.writer = writer;
        this.colorized = colorized;
        this.fontType = 5;
        this.usedSlot = new boolean[256];
    }

    public PdfContentByte defineGlyph(char c, float wx, float llx, float lly, float urx, float ury) {
        if (c == '\u0000' || c > 'ÿ') {
            throw new IllegalArgumentException(MessageLocalization.getComposedMessage("the.char.1.doesn.t.belong.in.this.type3.font", (int) c));
        }
        this.usedSlot[c] = true;
        Integer ck = Integer.valueOf(c);
        Type3Glyph glyph = (Type3Glyph) this.char2glyph.get(ck);
        if (glyph != null) {
            return glyph;
        }
        this.widths3.put(c, (int) wx);
        if (!this.colorized) {
            if (Float.isNaN(this.llx)) {
                this.llx = llx;
                this.lly = lly;
                this.urx = urx;
                this.ury = ury;
            } else {
                this.llx = Math.min(this.llx, llx);
                this.lly = Math.min(this.lly, lly);
                this.urx = Math.max(this.urx, urx);
                this.ury = Math.max(this.ury, ury);
            }
        }
        glyph = new Type3Glyph(this.writer, this.pageResources, wx, llx, lly, urx, ury, this.colorized);
        this.char2glyph.put(ck, glyph);
        return glyph;
    }

    public String[][] getFamilyFontName() {
        return getFullFontName();
    }

    public float getFontDescriptor(int key, float fontSize) {
        return 0.0f;
    }

    public String[][] getFullFontName() {
        String[][] strArr = new String[1][];
        strArr[0] = new String[]{"", "", "", ""};
        return strArr;
    }

    public String[][] getAllNameEntries() {
        String[][] strArr = new String[1][];
        strArr[0] = new String[]{"4", "", "", "", ""};
        return strArr;
    }

    public int getKerning(int char1, int char2) {
        return 0;
    }

    public String getPostscriptFontName() {
        return "";
    }

    protected int[] getRawCharBBox(int c, String name) {
        return null;
    }

    int getRawWidth(int c, String name) {
        return 0;
    }

    public boolean hasKernPairs() {
        return false;
    }

    public boolean setKerning(int char1, int char2, int kern) {
        return false;
    }

    public void setPostscriptFontName(String name) {
    }

    void writeFont(PdfWriter writer, PdfIndirectReference ref, Object[] params) throws DocumentException, IOException {
        if (this.writer != writer) {
            throw new IllegalArgumentException(MessageLocalization.getComposedMessage("type3.font.used.with.the.wrong.pdfwriter", new Object[0]));
        }
        int firstChar = 0;
        while (firstChar < this.usedSlot.length && !this.usedSlot[firstChar]) {
            firstChar++;
        }
        if (firstChar == this.usedSlot.length) {
            throw new DocumentException(MessageLocalization.getComposedMessage("no.glyphs.defined.for.type3.font", new Object[0]));
        }
        int lastChar = this.usedSlot.length - 1;
        while (lastChar >= firstChar && !this.usedSlot[lastChar]) {
            lastChar--;
        }
        int[] widths = new int[((lastChar - firstChar) + 1)];
        int[] invOrd = new int[((lastChar - firstChar) + 1)];
        int w = 0;
        int u = firstChar;
        int invOrdIndx = 0;
        while (u <= lastChar) {
            int invOrdIndx2;
            if (this.usedSlot[u]) {
                invOrdIndx2 = invOrdIndx + 1;
                invOrd[invOrdIndx] = u;
                widths[w] = this.widths3.get(u);
            } else {
                invOrdIndx2 = invOrdIndx;
            }
            u++;
            w++;
            invOrdIndx = invOrdIndx2;
        }
        PdfArray diffs = new PdfArray();
        PdfDictionary charprocs = new PdfDictionary();
        int last = -1;
        for (int k = 0; k < invOrdIndx; k++) {
            int c = invOrd[k];
            if (c > last) {
                last = c;
                diffs.add(new PdfNumber(last));
            }
            last++;
            int c2 = invOrd[k];
            String s = GlyphList.unicodeToName(c2);
            if (s == null) {
                s = HtmlTags.f32A + c2;
            }
            PdfObject pdfName = new PdfName(s);
            diffs.add(pdfName);
            pdfName = new PdfStream(((Type3Glyph) this.char2glyph.get(Integer.valueOf(c2))).toPdf(null));
            pdfName.flateCompress(this.compressionLevel);
            charprocs.put(pdfName, writer.addToBody(pdfName).getIndirectReference());
        }
        PdfDictionary font = new PdfDictionary(PdfName.FONT);
        font.put(PdfName.SUBTYPE, PdfName.TYPE3);
        if (this.colorized) {
            font.put(PdfName.FONTBBOX, new PdfRectangle(0.0f, 0.0f, 0.0f, 0.0f));
        } else {
            font.put(PdfName.FONTBBOX, new PdfRectangle(this.llx, this.lly, this.urx, this.ury));
        }
        float[] fArr = new float[6];
        font.put(PdfName.FONTMATRIX, new PdfArray(new float[]{0.001f, 0.0f, 0.0f, 0.001f, 0.0f, 0.0f}));
        font.put(PdfName.CHARPROCS, writer.addToBody(charprocs).getIndirectReference());
        PdfDictionary encoding = new PdfDictionary();
        encoding.put(PdfName.DIFFERENCES, diffs);
        font.put(PdfName.ENCODING, writer.addToBody(encoding).getIndirectReference());
        font.put(PdfName.FIRSTCHAR, new PdfNumber(firstChar));
        font.put(PdfName.LASTCHAR, new PdfNumber(lastChar));
        font.put(PdfName.WIDTHS, writer.addToBody(new PdfArray(widths)).getIndirectReference());
        if (this.pageResources.hasResources()) {
            font.put(PdfName.RESOURCES, writer.addToBody(this.pageResources.getResources()).getIndirectReference());
        }
        writer.addToBody(font, ref);
    }

    public PdfStream getFullFontStream() {
        return null;
    }

    public byte[] convertToBytes(String text) {
        char[] cc = text.toCharArray();
        byte[] b = new byte[cc.length];
        int p = 0;
        for (char c : cc) {
            if (charExists(c)) {
                int p2 = p + 1;
                b[p] = (byte) c;
                p = p2;
            }
        }
        if (b.length == p) {
            return b;
        }
        byte[] b2 = new byte[p];
        System.arraycopy(b, 0, b2, 0, p);
        return b2;
    }

    byte[] convertToBytes(int char1) {
        if (!charExists(char1)) {
            return new byte[0];
        }
        return new byte[]{(byte) char1};
    }

    public int getWidth(int char1) {
        if (this.widths3.containsKey(char1)) {
            return this.widths3.get(char1);
        }
        throw new IllegalArgumentException(MessageLocalization.getComposedMessage("the.char.1.is.not.defined.in.a.type3.font", char1));
    }

    public int getWidth(String text) {
        int total = 0;
        for (int width : text.toCharArray()) {
            total += getWidth(width);
        }
        return total;
    }

    public int[] getCharBBox(int c) {
        return null;
    }

    public boolean charExists(int c) {
        if (c <= 0 || c >= 256) {
            return false;
        }
        return this.usedSlot[c];
    }

    public boolean setCharAdvance(int c, int advance) {
        return false;
    }
}
