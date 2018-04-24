package com.itextpdf.text.pdf.fonts.cmaps;

public class CMapSequence {
    public int len;
    public int off;
    public byte[] seq;

    public CMapSequence(byte[] seq, int off, int len) {
        this.seq = seq;
        this.off = off;
        this.len = len;
    }
}
