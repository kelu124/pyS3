package com.itextpdf.text;

public class DocumentException extends Exception {
    private static final long serialVersionUID = -2191131489390840739L;

    public DocumentException(Exception ex) {
        super(ex);
    }

    public DocumentException(String message) {
        super(message);
    }
}
