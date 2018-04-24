package com.itextpdf.text.pdf.parser;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.pdf.BaseField;
import com.itextpdf.text.pdf.CMapAwareDocumentFont;
import com.itextpdf.text.pdf.PdfName;

public class GraphicsState {
    float characterSpacing;
    PdfName colorSpaceFill;
    PdfName colorSpaceStroke;
    Matrix ctm;
    BaseColor fillColor;
    CMapAwareDocumentFont font;
    float fontSize;
    float horizontalScaling;
    boolean knockout;
    float leading;
    int renderMode;
    float rise;
    BaseColor strokeColor;
    float wordSpacing;

    public GraphicsState() {
        this.ctm = new Matrix();
        this.characterSpacing = 0.0f;
        this.wordSpacing = 0.0f;
        this.horizontalScaling = BaseField.BORDER_WIDTH_THIN;
        this.leading = 0.0f;
        this.font = null;
        this.fontSize = 0.0f;
        this.renderMode = 0;
        this.rise = 0.0f;
        this.knockout = true;
        this.colorSpaceFill = null;
        this.colorSpaceStroke = null;
        this.fillColor = null;
        this.strokeColor = null;
    }

    public GraphicsState(GraphicsState source) {
        this.ctm = source.ctm;
        this.characterSpacing = source.characterSpacing;
        this.wordSpacing = source.wordSpacing;
        this.horizontalScaling = source.horizontalScaling;
        this.leading = source.leading;
        this.font = source.font;
        this.fontSize = source.fontSize;
        this.renderMode = source.renderMode;
        this.rise = source.rise;
        this.knockout = source.knockout;
        this.colorSpaceFill = source.colorSpaceFill;
        this.colorSpaceStroke = source.colorSpaceStroke;
        this.fillColor = source.fillColor;
        this.strokeColor = source.strokeColor;
    }

    public Matrix getCtm() {
        return this.ctm;
    }

    public float getCharacterSpacing() {
        return this.characterSpacing;
    }

    public float getWordSpacing() {
        return this.wordSpacing;
    }

    public float getHorizontalScaling() {
        return this.horizontalScaling;
    }

    public float getLeading() {
        return this.leading;
    }

    public CMapAwareDocumentFont getFont() {
        return this.font;
    }

    public float getFontSize() {
        return this.fontSize;
    }

    public int getRenderMode() {
        return this.renderMode;
    }

    public float getRise() {
        return this.rise;
    }

    public boolean isKnockout() {
        return this.knockout;
    }

    public PdfName getColorSpaceFill() {
        return this.colorSpaceFill;
    }

    public PdfName getColorSpaceStroke() {
        return this.colorSpaceStroke;
    }

    public BaseColor getFillColor() {
        return this.fillColor;
    }

    public BaseColor getStrokeColor() {
        return this.strokeColor;
    }
}
