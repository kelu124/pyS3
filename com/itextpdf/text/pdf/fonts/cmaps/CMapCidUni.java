package com.itextpdf.text.pdf.fonts.cmaps;

import com.itextpdf.text.Utilities;
import com.itextpdf.text.pdf.IntHashtable;
import com.itextpdf.text.pdf.PdfNumber;
import com.itextpdf.text.pdf.PdfObject;
import com.itextpdf.text.pdf.PdfString;
import org.bytedeco.javacpp.avcodec;

public class CMapCidUni extends AbstractCMap {
    private IntHashtable map = new IntHashtable(avcodec.AV_CODEC_ID_PCM_S16BE);

    void addChar(PdfString mark, PdfObject code) {
        if (code instanceof PdfNumber) {
            int codepoint;
            String s = decodeStringToUnicode(mark);
            if (Utilities.isSurrogatePair(s, 0)) {
                codepoint = Utilities.convertToUtf32(s, 0);
            } else {
                codepoint = s.charAt(0);
            }
            this.map.put(((PdfNumber) code).intValue(), codepoint);
        }
    }

    public int lookup(int character) {
        return this.map.get(character);
    }
}
