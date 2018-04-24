package com.itextpdf.text.pdf;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import java.io.IOException;
import java.io.OutputStream;

public class PdfConcatenate {
    protected PdfCopy copy;
    protected Document document;

    public PdfConcatenate(OutputStream os) throws DocumentException {
        this(os, false);
    }

    public PdfConcatenate(OutputStream os, boolean smart) throws DocumentException {
        this.document = new Document();
        if (smart) {
            this.copy = new PdfSmartCopy(this.document, os);
        } else {
            this.copy = new PdfCopy(this.document, os);
        }
    }

    public int addPages(PdfReader reader) throws DocumentException, IOException {
        open();
        int n = reader.getNumberOfPages();
        for (int i = 1; i <= n; i++) {
            this.copy.addPage(this.copy.getImportedPage(reader, i));
        }
        this.copy.freeReader(reader);
        reader.close();
        return n;
    }

    public PdfCopy getWriter() {
        return this.copy;
    }

    public void open() {
        if (!this.document.isOpen()) {
            this.document.open();
        }
    }

    public void close() {
        this.document.close();
    }
}
