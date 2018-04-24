package com.itextpdf.text.pdf.fonts.cmaps;

import com.itextpdf.text.pdf.PdfNumber;
import com.itextpdf.text.pdf.PdfObject;
import com.itextpdf.text.pdf.PdfString;
import java.util.HashMap;

public class CMapCidByte extends AbstractCMap {
    private final byte[] EMPTY = new byte[0];
    private HashMap<Integer, byte[]> map = new HashMap();

    void addChar(PdfString mark, PdfObject code) {
        if (code instanceof PdfNumber) {
            this.map.put(Integer.valueOf(((PdfNumber) code).intValue()), AbstractCMap.decodeStringToByte(mark));
        }
    }

    public byte[] lookup(int cid) {
        byte[] ser = (byte[]) this.map.get(Integer.valueOf(cid));
        if (ser == null) {
            return this.EMPTY;
        }
        return ser;
    }
}
