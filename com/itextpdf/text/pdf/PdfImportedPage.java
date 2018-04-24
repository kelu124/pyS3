package com.itextpdf.text.pdf;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.error_messages.MessageLocalization;
import java.io.IOException;

public class PdfImportedPage extends PdfTemplate {
    int pageNumber;
    PdfReaderInstance readerInstance;
    int rotation;
    protected boolean toCopy = true;

    PdfImportedPage(PdfReaderInstance readerInstance, PdfWriter writer, int pageNumber) {
        this.readerInstance = readerInstance;
        this.pageNumber = pageNumber;
        this.writer = writer;
        this.rotation = readerInstance.getReader().getPageRotation(pageNumber);
        this.bBox = readerInstance.getReader().getPageSize(pageNumber);
        setMatrix(BaseField.BORDER_WIDTH_THIN, 0.0f, 0.0f, BaseField.BORDER_WIDTH_THIN, -this.bBox.getLeft(), -this.bBox.getBottom());
        this.type = 2;
    }

    public PdfImportedPage getFromReader() {
        return this;
    }

    public int getPageNumber() {
        return this.pageNumber;
    }

    public int getRotation() {
        return this.rotation;
    }

    public void addImage(Image image, float a, float b, float c, float d, float e, float f) throws DocumentException {
        throwError();
    }

    public void addTemplate(PdfTemplate template, float a, float b, float c, float d, float e, float f) {
        throwError();
    }

    public PdfContentByte getDuplicate() {
        throwError();
        return null;
    }

    public PdfStream getFormXObject(int compressionLevel) throws IOException {
        return this.readerInstance.getFormXObject(this.pageNumber, compressionLevel);
    }

    public void setColorFill(PdfSpotColor sp, float tint) {
        throwError();
    }

    public void setColorStroke(PdfSpotColor sp, float tint) {
        throwError();
    }

    PdfObject getResources() {
        return this.readerInstance.getResources(this.pageNumber);
    }

    public void setFontAndSize(BaseFont bf, float size) {
        throwError();
    }

    public void setGroup(PdfTransparencyGroup group) {
        throwError();
    }

    void throwError() {
        throw new RuntimeException(MessageLocalization.getComposedMessage("content.can.not.be.added.to.a.pdfimportedpage", new Object[0]));
    }

    PdfReaderInstance getPdfReaderInstance() {
        return this.readerInstance;
    }

    public boolean isToCopy() {
        return this.toCopy;
    }

    public void setCopied() {
        this.toCopy = false;
    }
}
