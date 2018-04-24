package com.itextpdf.text.pdf.security;

import java.security.cert.X509Certificate;

public interface OcspClient {
    byte[] getEncoded(X509Certificate x509Certificate, X509Certificate x509Certificate2, String str);
}
