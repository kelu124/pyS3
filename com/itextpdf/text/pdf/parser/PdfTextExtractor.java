package com.itextpdf.text.pdf.parser;

import com.itextpdf.text.pdf.PdfReader;
import java.io.IOException;

public final class PdfTextExtractor {
    private PdfTextExtractor() {
    }

    public static String getTextFromPage(PdfReader reader, int pageNumber, TextExtractionStrategy strategy) throws IOException {
        return ((TextExtractionStrategy) new PdfReaderContentParser(reader).processContent(pageNumber, strategy)).getResultantText();
    }

    public static String getTextFromPage(PdfReader reader, int pageNumber) throws IOException {
        return getTextFromPage(reader, pageNumber, new LocationTextExtractionStrategy());
    }
}
