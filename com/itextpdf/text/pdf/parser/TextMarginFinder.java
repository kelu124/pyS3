package com.itextpdf.text.pdf.parser;

import com.itextpdf.awt.geom.Rectangle2D.Float;

public class TextMarginFinder implements RenderListener {
    private Float textRectangle = null;

    public void renderText(TextRenderInfo renderInfo) {
        if (this.textRectangle == null) {
            this.textRectangle = renderInfo.getDescentLine().getBoundingRectange();
        } else {
            this.textRectangle.add(renderInfo.getDescentLine().getBoundingRectange());
        }
        this.textRectangle.add(renderInfo.getAscentLine().getBoundingRectange());
    }

    public float getLlx() {
        return this.textRectangle.f96x;
    }

    public float getLly() {
        return this.textRectangle.f97y;
    }

    public float getUrx() {
        return this.textRectangle.f96x + this.textRectangle.width;
    }

    public float getUry() {
        return this.textRectangle.f97y + this.textRectangle.height;
    }

    public float getWidth() {
        return this.textRectangle.width;
    }

    public float getHeight() {
        return this.textRectangle.height;
    }

    public void beginTextBlock() {
    }

    public void endTextBlock() {
    }

    public void renderImage(ImageRenderInfo renderInfo) {
    }
}
