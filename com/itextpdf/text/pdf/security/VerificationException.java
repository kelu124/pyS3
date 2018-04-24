package com.itextpdf.text.pdf.security;

import java.security.GeneralSecurityException;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;

public class VerificationException extends GeneralSecurityException {
    private static final long serialVersionUID = 2978604513926438256L;

    public VerificationException(Certificate cert, String message) {
        String str = "Certificate %s failed: %s";
        Object[] objArr = new Object[2];
        objArr[0] = cert == null ? "Unknown" : ((X509Certificate) cert).getSubjectDN().getName();
        objArr[1] = message;
        super(String.format(str, objArr));
    }
}
