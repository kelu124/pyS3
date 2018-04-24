package com.itextpdf.text.pdf.security;

import com.itextpdf.text.ExceptionConverter;
import java.security.cert.CRL;
import java.security.cert.X509CRL;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;

public class CrlClientOffline implements CrlClient {
    private ArrayList<byte[]> crls = new ArrayList();

    public CrlClientOffline(byte[] crlEncoded) {
        this.crls.add(crlEncoded);
    }

    public CrlClientOffline(CRL crl) {
        try {
            this.crls.add(((X509CRL) crl).getEncoded());
        } catch (Exception ex) {
            throw new ExceptionConverter(ex);
        }
    }

    public Collection<byte[]> getEncoded(X509Certificate checkCert, String url) {
        return this.crls;
    }
}
