package com.itextpdf.text.pdf;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.error_messages.MessageLocalization;
import java.util.HashMap;

public class PdfPage extends PdfDictionary {
    public static final PdfNumber INVERTEDPORTRAIT = new PdfNumber(180);
    public static final PdfNumber LANDSCAPE = new PdfNumber(90);
    public static final PdfNumber PORTRAIT = new PdfNumber(0);
    public static final PdfNumber SEASCAPE = new PdfNumber(270);
    private static final PdfName[] boxNames = new PdfName[]{PdfName.CROPBOX, PdfName.TRIMBOX, PdfName.ARTBOX, PdfName.BLEEDBOX};
    private static final String[] boxStrings = new String[]{"crop", "trim", "art", "bleed"};
    PdfRectangle mediaBox;

    PdfPage(PdfRectangle mediaBox, HashMap<String, PdfRectangle> boxSize, PdfDictionary resources, int rotate) throws DocumentException {
        super(PAGE);
        this.mediaBox = mediaBox;
        if (mediaBox == null || (mediaBox.width() <= 14400.0f && mediaBox.height() <= 14400.0f)) {
            put(PdfName.MEDIABOX, mediaBox);
            put(PdfName.RESOURCES, resources);
            if (rotate != 0) {
                put(PdfName.ROTATE, new PdfNumber(rotate));
            }
            for (int k = 0; k < boxStrings.length; k++) {
                PdfObject rect = (PdfObject) boxSize.get(boxStrings[k]);
                if (rect != null) {
                    put(boxNames[k], rect);
                }
            }
            return;
        }
        throw new DocumentException(MessageLocalization.getComposedMessage("the.page.size.must.be.smaller.than.14400.by.14400.its.1.by.2", Float.valueOf(mediaBox.width()), Float.valueOf(mediaBox.height())));
    }

    PdfPage(PdfRectangle mediaBox, HashMap<String, PdfRectangle> boxSize, PdfDictionary resources) throws DocumentException {
        this(mediaBox, boxSize, resources, 0);
    }

    public boolean isParent() {
        return false;
    }

    void add(PdfIndirectReference contents) {
        put(PdfName.CONTENTS, contents);
    }

    PdfRectangle rotateMediaBox() {
        this.mediaBox = this.mediaBox.rotate();
        put(PdfName.MEDIABOX, this.mediaBox);
        return this.mediaBox;
    }

    PdfRectangle getMediaBox() {
        return this.mediaBox;
    }
}
