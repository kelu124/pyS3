package com.itextpdf.text.pdf;

class ColorDetails {
    ICachedColorSpace colorSpace;
    PdfName colorSpaceName;
    PdfIndirectReference indirectReference;

    ColorDetails(PdfName colorName, PdfIndirectReference indirectReference, ICachedColorSpace scolor) {
        this.colorSpaceName = colorName;
        this.indirectReference = indirectReference;
        this.colorSpace = scolor;
    }

    public PdfIndirectReference getIndirectReference() {
        return this.indirectReference;
    }

    PdfName getColorSpaceName() {
        return this.colorSpaceName;
    }

    public PdfObject getPdfObject(PdfWriter writer) {
        return this.colorSpace.getPdfObject(writer);
    }
}
