package com.itextpdf.text.pdf;

import com.itextpdf.text.error_messages.MessageLocalization;
import java.io.IOException;

public class PdfShadingPattern extends PdfDictionary {
    protected float[] matrix = new float[]{BaseField.BORDER_WIDTH_THIN, 0.0f, 0.0f, BaseField.BORDER_WIDTH_THIN, 0.0f, 0.0f};
    protected PdfName patternName;
    protected PdfIndirectReference patternReference;
    protected PdfShading shading;
    protected PdfWriter writer;

    public PdfShadingPattern(PdfShading shading) {
        this.writer = shading.getWriter();
        put(PdfName.PATTERNTYPE, new PdfNumber(2));
        this.shading = shading;
    }

    PdfName getPatternName() {
        return this.patternName;
    }

    PdfName getShadingName() {
        return this.shading.getShadingName();
    }

    PdfIndirectReference getPatternReference() {
        if (this.patternReference == null) {
            this.patternReference = this.writer.getPdfIndirectReference();
        }
        return this.patternReference;
    }

    PdfIndirectReference getShadingReference() {
        return this.shading.getShadingReference();
    }

    void setName(int number) {
        this.patternName = new PdfName("P" + number);
    }

    public void addToBody() throws IOException {
        put(PdfName.SHADING, getShadingReference());
        put(PdfName.MATRIX, new PdfArray(this.matrix));
        this.writer.addToBody(this, getPatternReference());
    }

    public void setMatrix(float[] matrix) {
        if (matrix.length != 6) {
            throw new RuntimeException(MessageLocalization.getComposedMessage("the.matrix.size.must.be.6", new Object[0]));
        }
        this.matrix = matrix;
    }

    public float[] getMatrix() {
        return this.matrix;
    }

    public PdfShading getShading() {
        return this.shading;
    }

    ColorDetails getColorDetails() {
        return this.shading.getColorDetails();
    }
}
