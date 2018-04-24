package com.itextpdf.text.pdf;

public class PdfIndirectReference extends PdfObject {
    protected int generation;
    protected int number;

    protected PdfIndirectReference() {
        super(0);
        this.generation = 0;
    }

    PdfIndirectReference(int type, int number, int generation) {
        super(0, new StringBuffer().append(number).append(" ").append(generation).append(" R").toString());
        this.generation = 0;
        this.number = number;
        this.generation = generation;
    }

    protected PdfIndirectReference(int type, int number) {
        this(type, number, 0);
    }

    public int getNumber() {
        return this.number;
    }

    public int getGeneration() {
        return this.generation;
    }

    public String toString() {
        return new StringBuffer().append(this.number).append(" ").append(this.generation).append(" R").toString();
    }
}
