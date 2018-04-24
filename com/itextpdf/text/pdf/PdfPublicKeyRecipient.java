package com.itextpdf.text.pdf;

import java.security.cert.Certificate;

public class PdfPublicKeyRecipient {
    private Certificate certificate = null;
    protected byte[] cms = null;
    private int permission = 0;

    public PdfPublicKeyRecipient(Certificate certificate, int permission) {
        this.certificate = certificate;
        this.permission = permission;
    }

    public Certificate getCertificate() {
        return this.certificate;
    }

    public int getPermission() {
        return this.permission;
    }

    protected void setCms(byte[] cms) {
        this.cms = cms;
    }

    protected byte[] getCms() {
        return this.cms;
    }
}
