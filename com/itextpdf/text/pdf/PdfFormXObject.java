package com.itextpdf.text.pdf;

public class PdfFormXObject extends PdfStream {
    public static final PdfLiteral MATRIX = new PdfLiteral("[1 0 0 1 0 0]");
    public static final PdfNumber ONE = new PdfNumber(1);
    public static final PdfNumber ZERO = new PdfNumber(0);

    PdfFormXObject(PdfTemplate template, int compressionLevel) {
        put(PdfName.TYPE, PdfName.XOBJECT);
        put(PdfName.SUBTYPE, PdfName.FORM);
        put(PdfName.RESOURCES, template.getResources());
        put(PdfName.BBOX, new PdfRectangle(template.getBoundingBox()));
        put(PdfName.FORMTYPE, ONE);
        if (template.getLayer() != null) {
            put(PdfName.OC, template.getLayer().getRef());
        }
        if (template.getGroup() != null) {
            put(PdfName.GROUP, template.getGroup());
        }
        PdfArray matrix = template.getMatrix();
        if (matrix == null) {
            put(PdfName.MATRIX, MATRIX);
        } else {
            put(PdfName.MATRIX, matrix);
        }
        this.bytes = template.toPdf(null);
        put(PdfName.LENGTH, new PdfNumber(this.bytes.length));
        if (template.getAdditional() != null) {
            putAll(template.getAdditional());
        }
        flateCompress(compressionLevel);
    }
}
