package com.itextpdf.text.pdf;

import com.itextpdf.text.DocumentException;

public class PdfException extends DocumentException {
    private static final long serialVersionUID = 6767433960955483999L;

    public PdfException(Exception ex) {
        super(ex);
    }

    PdfException() {
    }

    PdfException(String message) {
        super(message);
    }
}
