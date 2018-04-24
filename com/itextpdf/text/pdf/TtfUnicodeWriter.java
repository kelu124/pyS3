package com.itextpdf.text.pdf;

import com.itextpdf.text.DocumentException;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

public class TtfUnicodeWriter {
    protected PdfWriter writer = null;

    public TtfUnicodeWriter(PdfWriter writer) {
        this.writer = writer;
    }

    public void writeFont(TrueTypeFontUnicode font, PdfIndirectReference ref, Object[] params, byte[] rotbits) throws DocumentException, IOException {
        PdfIndirectReference ind_font;
        HashMap<Integer, int[]> longTag = params[0];
        font.addRangeUni(longTag, true, font.subset);
        int[][] metrics = (int[][]) longTag.values().toArray(new int[0][]);
        Arrays.sort(metrics, font);
        byte[] b;
        if (font.cff) {
            b = font.readCffFont();
            if (font.subset || font.subsetRanges != null) {
                CFFFontSubset cff = new CFFFontSubset(new RandomAccessFileOrArray(b), longTag);
                b = cff.Process(cff.getNames()[0]);
            }
            ind_font = this.writer.addToBody(new StreamFont(b, "CIDFontType0C", font.compressionLevel)).getIndirectReference();
        } else {
            if (font.subset || font.directoryOffset != 0) {
                b = font.getSubSet(new HashSet(longTag.keySet()), true);
            } else {
                b = font.getFullFont();
            }
            ind_font = this.writer.addToBody(new StreamFont(b, new int[]{b.length}, font.compressionLevel)).getIndirectReference();
        }
        String subsetPrefix = "";
        if (font.subset) {
            subsetPrefix = BaseFont.createSubsetPrefix();
        }
        ind_font = this.writer.addToBody(font.getCIDFontType2(this.writer.addToBody(font.getFontDescriptor(ind_font, subsetPrefix, null)).getIndirectReference(), subsetPrefix, metrics)).getIndirectReference();
        PdfObject pobj = font.getToUnicode(metrics);
        PdfIndirectReference toUnicodeRef = null;
        if (pobj != null) {
            toUnicodeRef = this.writer.addToBody(pobj).getIndirectReference();
        }
        this.writer.addToBody(font.getFontBaseType(ind_font, subsetPrefix, toUnicodeRef), ref);
    }
}
