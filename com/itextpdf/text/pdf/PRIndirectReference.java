package com.itextpdf.text.pdf;

import java.io.IOException;
import java.io.OutputStream;

public class PRIndirectReference extends PdfIndirectReference {
    protected PdfReader reader;

    public PRIndirectReference(PdfReader reader, int number, int generation) {
        this.type = 10;
        this.number = number;
        this.generation = generation;
        this.reader = reader;
    }

    public PRIndirectReference(PdfReader reader, int number) {
        this(reader, number, 0);
    }

    public void toPdf(PdfWriter writer, OutputStream os) throws IOException {
        os.write(PdfEncodings.convertToBytes(new StringBuffer().append(writer.getNewObjectNumber(this.reader, this.number, this.generation)).append(" ").append(this.reader.isAppendable() ? this.generation : 0).append(" R").toString(), null));
    }

    public PdfReader getReader() {
        return this.reader;
    }

    public void setNumber(int number, int generation) {
        this.number = number;
        this.generation = generation;
    }
}
