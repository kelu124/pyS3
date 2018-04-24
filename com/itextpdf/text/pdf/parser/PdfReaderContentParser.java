package com.itextpdf.text.pdf.parser;

import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfReader;
import java.io.IOException;

public class PdfReaderContentParser {
    private final PdfReader reader;

    public PdfReaderContentParser(PdfReader reader) {
        this.reader = reader;
    }

    public <E extends RenderListener> E processContent(int pageNumber, E renderListener) throws IOException {
        new PdfContentStreamProcessor(renderListener).processContent(ContentByteUtils.getContentBytesForPage(this.reader, pageNumber), this.reader.getPageN(pageNumber).getAsDict(PdfName.RESOURCES));
        return renderListener;
    }
}
