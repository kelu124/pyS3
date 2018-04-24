package com.itextpdf.text.pdf.parser;

public interface RenderListener {
    void beginTextBlock();

    void endTextBlock();

    void renderImage(ImageRenderInfo imageRenderInfo);

    void renderText(TextRenderInfo textRenderInfo);
}
