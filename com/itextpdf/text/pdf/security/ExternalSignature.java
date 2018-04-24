package com.itextpdf.text.pdf.security;

import java.security.GeneralSecurityException;

public interface ExternalSignature {
    String getEncryptionAlgorithm();

    String getHashAlgorithm();

    byte[] sign(byte[] bArr) throws GeneralSecurityException;
}
