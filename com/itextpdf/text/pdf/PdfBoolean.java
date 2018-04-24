package com.itextpdf.text.pdf;

import com.itextpdf.text.error_messages.MessageLocalization;

public class PdfBoolean extends PdfObject {
    public static final String FALSE = "false";
    public static final PdfBoolean PDFFALSE = new PdfBoolean(false);
    public static final PdfBoolean PDFTRUE = new PdfBoolean(true);
    public static final String TRUE = "true";
    private boolean value;

    public PdfBoolean(boolean value) {
        super(1);
        if (value) {
            setContent(TRUE);
        } else {
            setContent(FALSE);
        }
        this.value = value;
    }

    public PdfBoolean(String value) throws BadPdfFormatException {
        super(1, value);
        if (value.equals(TRUE)) {
            this.value = true;
        } else if (value.equals(FALSE)) {
            this.value = false;
        } else {
            throw new BadPdfFormatException(MessageLocalization.getComposedMessage("the.value.has.to.be.true.of.false.instead.of.1", value));
        }
    }

    public boolean booleanValue() {
        return this.value;
    }

    public String toString() {
        return this.value ? TRUE : FALSE;
    }
}
