package com.itextpdf.text.pdf;

import com.itextpdf.text.DocWriter;
import java.io.IOException;
import java.io.OutputStream;

public class PdfWriter$PdfTrailer extends PdfDictionary {
    long offset;

    public PdfWriter$PdfTrailer(int size, long offset, PdfIndirectReference root, PdfIndirectReference info, PdfIndirectReference encryption, PdfObject fileID, long prevxref) {
        this.offset = offset;
        put(PdfName.SIZE, new PdfNumber(size));
        put(PdfName.ROOT, root);
        if (info != null) {
            put(PdfName.INFO, info);
        }
        if (encryption != null) {
            put(PdfName.ENCRYPT, encryption);
        }
        if (fileID != null) {
            put(PdfName.ID, fileID);
        }
        if (prevxref > 0) {
            put(PdfName.PREV, new PdfNumber(prevxref));
        }
    }

    public void toPdf(PdfWriter writer, OutputStream os) throws IOException {
        PdfWriter.checkPdfIsoConformance(writer, 8, this);
        os.write(DocWriter.getISOBytes("trailer\n"));
        super.toPdf(null, os);
        os.write(10);
        PdfWriter.writeKeyInfo(os);
        os.write(DocWriter.getISOBytes("startxref\n"));
        os.write(DocWriter.getISOBytes(String.valueOf(this.offset)));
        os.write(DocWriter.getISOBytes("\n%%EOF\n"));
    }
}
