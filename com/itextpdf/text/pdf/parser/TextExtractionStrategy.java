package com.itextpdf.text.pdf.parser;

public interface TextExtractionStrategy extends RenderListener {
    String getResultantText();
}
