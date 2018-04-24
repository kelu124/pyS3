package com.itextpdf.text.pdf.security;

import java.security.GeneralSecurityException;
import java.security.MessageDigest;

public interface ExternalDigest {
    MessageDigest getMessageDigest(String str) throws GeneralSecurityException;
}
