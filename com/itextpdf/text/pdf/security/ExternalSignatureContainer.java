package com.itextpdf.text.pdf.security;

import com.itextpdf.text.pdf.PdfDictionary;
import java.io.InputStream;
import java.security.GeneralSecurityException;

public interface ExternalSignatureContainer {
    void modifySigningDictionary(PdfDictionary pdfDictionary);

    byte[] sign(InputStream inputStream) throws GeneralSecurityException;
}
