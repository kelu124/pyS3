package com.itextpdf.text.pdf.parser;

import com.itextpdf.text.pdf.BaseField;
import com.itextpdf.text.pdf.PRStream;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfIndirectReference;
import com.itextpdf.text.pdf.PdfReader;
import java.io.IOException;

public class ImageRenderInfo {
    private final PdfDictionary colorSpaceDictionary;
    private final Matrix ctm;
    private PdfImageObject imageObject = null;
    private final InlineImageInfo inlineImageInfo;
    private final PdfIndirectReference ref;

    private ImageRenderInfo(Matrix ctm, PdfIndirectReference ref, PdfDictionary colorSpaceDictionary) {
        this.ctm = ctm;
        this.ref = ref;
        this.inlineImageInfo = null;
        this.colorSpaceDictionary = colorSpaceDictionary;
    }

    private ImageRenderInfo(Matrix ctm, InlineImageInfo inlineImageInfo, PdfDictionary colorSpaceDictionary) {
        this.ctm = ctm;
        this.ref = null;
        this.inlineImageInfo = inlineImageInfo;
        this.colorSpaceDictionary = colorSpaceDictionary;
    }

    public static ImageRenderInfo createForXObject(Matrix ctm, PdfIndirectReference ref, PdfDictionary colorSpaceDictionary) {
        return new ImageRenderInfo(ctm, ref, colorSpaceDictionary);
    }

    protected static ImageRenderInfo createForEmbeddedImage(Matrix ctm, InlineImageInfo inlineImageInfo, PdfDictionary colorSpaceDictionary) {
        return new ImageRenderInfo(ctm, inlineImageInfo, colorSpaceDictionary);
    }

    public PdfImageObject getImage() throws IOException {
        prepareImageObject();
        return this.imageObject;
    }

    private void prepareImageObject() throws IOException {
        if (this.imageObject == null) {
            if (this.ref != null) {
                this.imageObject = new PdfImageObject((PRStream) PdfReader.getPdfObject(this.ref), this.colorSpaceDictionary);
            } else if (this.inlineImageInfo != null) {
                this.imageObject = new PdfImageObject(this.inlineImageInfo.getImageDictionary(), this.inlineImageInfo.getSamples(), this.colorSpaceDictionary);
            }
        }
    }

    public Vector getStartPoint() {
        return new Vector(0.0f, 0.0f, BaseField.BORDER_WIDTH_THIN).cross(this.ctm);
    }

    public Matrix getImageCTM() {
        return this.ctm;
    }

    public float getArea() {
        return this.ctm.getDeterminant();
    }

    public PdfIndirectReference getRef() {
        return this.ref;
    }
}
