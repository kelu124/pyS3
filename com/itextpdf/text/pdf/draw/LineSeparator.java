package com.itextpdf.text.pdf.draw;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.BaseField;
import com.itextpdf.text.pdf.PdfChunk;
import com.itextpdf.text.pdf.PdfContentByte;

public class LineSeparator extends VerticalPositionMark {
    protected int alignment = 6;
    protected BaseColor lineColor;
    protected float lineWidth = BaseField.BORDER_WIDTH_THIN;
    protected float percentage = 100.0f;

    public LineSeparator(float lineWidth, float percentage, BaseColor lineColor, int align, float offset) {
        this.lineWidth = lineWidth;
        this.percentage = percentage;
        this.lineColor = lineColor;
        this.alignment = align;
        this.offset = offset;
    }

    public LineSeparator(Font font) {
        this.lineWidth = PdfChunk.UNDERLINE_THICKNESS * font.getSize();
        this.offset = PdfChunk.UNDERLINE_OFFSET * font.getSize();
        this.percentage = 100.0f;
        this.lineColor = font.getColor();
    }

    public void draw(PdfContentByte canvas, float llx, float lly, float urx, float ury, float y) {
        canvas.saveState();
        drawLine(canvas, llx, urx, y);
        canvas.restoreState();
    }

    public void drawLine(PdfContentByte canvas, float leftX, float rightX, float y) {
        float w;
        float s;
        if (getPercentage() < 0.0f) {
            w = -getPercentage();
        } else {
            w = ((rightX - leftX) * getPercentage()) / 100.0f;
        }
        switch (getAlignment()) {
            case 0:
                s = 0.0f;
                break;
            case 2:
                s = (rightX - leftX) - w;
                break;
            default:
                s = ((rightX - leftX) - w) / BaseField.BORDER_WIDTH_MEDIUM;
                break;
        }
        canvas.setLineWidth(getLineWidth());
        if (getLineColor() != null) {
            canvas.setColorStroke(getLineColor());
        }
        canvas.moveTo(s + leftX, this.offset + y);
        canvas.lineTo((s + w) + leftX, this.offset + y);
        canvas.stroke();
    }

    public float getLineWidth() {
        return this.lineWidth;
    }

    public void setLineWidth(float lineWidth) {
        this.lineWidth = lineWidth;
    }

    public float getPercentage() {
        return this.percentage;
    }

    public void setPercentage(float percentage) {
        this.percentage = percentage;
    }

    public BaseColor getLineColor() {
        return this.lineColor;
    }

    public void setLineColor(BaseColor color) {
        this.lineColor = color;
    }

    public int getAlignment() {
        return this.alignment;
    }

    public void setAlignment(int align) {
        this.alignment = align;
    }
}
