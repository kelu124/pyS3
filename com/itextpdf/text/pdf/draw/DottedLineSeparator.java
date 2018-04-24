package com.itextpdf.text.pdf.draw;

import com.itextpdf.text.pdf.BaseField;
import com.itextpdf.text.pdf.PdfContentByte;

public class DottedLineSeparator extends LineSeparator {
    protected float gap = 5.0f;

    public void draw(PdfContentByte canvas, float llx, float lly, float urx, float ury, float y) {
        canvas.saveState();
        canvas.setLineWidth(this.lineWidth);
        canvas.setLineCap(1);
        canvas.setLineDash(0.0f, this.gap, this.gap / BaseField.BORDER_WIDTH_MEDIUM);
        drawLine(canvas, llx, urx, y);
        canvas.restoreState();
    }

    public float getGap() {
        return this.gap;
    }

    public void setGap(float gap) {
        this.gap = gap;
    }
}
