package com.itextpdf.text.pdf.parser;

import com.itextpdf.text.pdf.BaseField;

public class SimpleTextExtractionStrategy implements TextExtractionStrategy {
    private Vector lastEnd;
    private Vector lastStart;
    private final StringBuffer result = new StringBuffer();

    public void beginTextBlock() {
    }

    public void endTextBlock() {
    }

    public String getResultantText() {
        return this.result.toString();
    }

    protected final void appendTextChunk(CharSequence text) {
        this.result.append(text);
    }

    public void renderText(TextRenderInfo renderInfo) {
        boolean firstRender = this.result.length() == 0;
        boolean hardReturn = false;
        LineSegment segment = renderInfo.getBaseline();
        Vector start = segment.getStartPoint();
        Vector end = segment.getEndPoint();
        if (!firstRender) {
            Vector x0 = start;
            Vector x1 = this.lastStart;
            Vector x2 = this.lastEnd;
            if (x2.subtract(x1).cross(x1.subtract(x0)).lengthSquared() / x2.subtract(x1).lengthSquared() > BaseField.BORDER_WIDTH_THIN) {
                hardReturn = true;
            }
        }
        if (hardReturn) {
            appendTextChunk("\n");
        } else if (!(firstRender || this.result.charAt(this.result.length() - 1) == ' ' || renderInfo.getText().length() <= 0 || renderInfo.getText().charAt(0) == ' ' || this.lastEnd.subtract(start).length() <= renderInfo.getSingleSpaceWidth() / BaseField.BORDER_WIDTH_MEDIUM)) {
            appendTextChunk(" ");
        }
        appendTextChunk(renderInfo.getText());
        this.lastStart = start;
        this.lastEnd = end;
    }

    public void renderImage(ImageRenderInfo renderInfo) {
    }
}
