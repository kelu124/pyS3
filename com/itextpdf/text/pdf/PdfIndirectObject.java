package com.itextpdf.text.pdf;

import com.itextpdf.text.DocWriter;
import java.io.IOException;
import java.io.OutputStream;

public class PdfIndirectObject {
    static final byte[] ENDOBJ = DocWriter.getISOBytes("\nendobj\n");
    static final int SIZEOBJ = (STARTOBJ.length + ENDOBJ.length);
    static final byte[] STARTOBJ = DocWriter.getISOBytes(" obj\n");
    protected int generation;
    protected int number;
    protected PdfObject object;
    protected PdfWriter writer;

    protected PdfIndirectObject(int number, PdfObject object, PdfWriter writer) {
        this(number, 0, object, writer);
    }

    PdfIndirectObject(PdfIndirectReference ref, PdfObject object, PdfWriter writer) {
        this(ref.getNumber(), ref.getGeneration(), object, writer);
    }

    PdfIndirectObject(int number, int generation, PdfObject object, PdfWriter writer) {
        this.generation = 0;
        this.writer = writer;
        this.number = number;
        this.generation = generation;
        this.object = object;
        PdfEncryption crypto = null;
        if (writer != null) {
            crypto = writer.getEncryption();
        }
        if (crypto != null) {
            crypto.setHashKey(number, generation);
        }
    }

    public PdfIndirectReference getIndirectReference() {
        return new PdfIndirectReference(this.object.type(), this.number, this.generation);
    }

    protected void writeTo(OutputStream os) throws IOException {
        os.write(DocWriter.getISOBytes(String.valueOf(this.number)));
        os.write(32);
        os.write(DocWriter.getISOBytes(String.valueOf(this.generation)));
        os.write(STARTOBJ);
        this.object.toPdf(this.writer, os);
        os.write(ENDOBJ);
    }

    public String toString() {
        return new StringBuffer().append(this.number).append(' ').append(this.generation).append(" R: ").append(this.object != null ? this.object.toString() : "null").toString();
    }
}
