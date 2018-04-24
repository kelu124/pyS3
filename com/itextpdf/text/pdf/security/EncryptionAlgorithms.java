package com.itextpdf.text.pdf.security;

import java.util.HashMap;

public class EncryptionAlgorithms {
    static final HashMap<String, String> algorithmNames = new HashMap();

    static {
        algorithmNames.put(SecurityIDs.ID_RSA, SecurityConstants.RSA);
        algorithmNames.put(SecurityIDs.ID_DSA, SecurityConstants.DSA);
        algorithmNames.put("1.2.840.113549.1.1.2", SecurityConstants.RSA);
        algorithmNames.put("1.2.840.113549.1.1.4", SecurityConstants.RSA);
        algorithmNames.put("1.2.840.113549.1.1.5", SecurityConstants.RSA);
        algorithmNames.put("1.2.840.113549.1.1.14", SecurityConstants.RSA);
        algorithmNames.put("1.2.840.113549.1.1.11", SecurityConstants.RSA);
        algorithmNames.put("1.2.840.113549.1.1.12", SecurityConstants.RSA);
        algorithmNames.put("1.2.840.113549.1.1.13", SecurityConstants.RSA);
        algorithmNames.put("1.2.840.10040.4.3", SecurityConstants.DSA);
        algorithmNames.put("2.16.840.1.101.3.4.3.1", SecurityConstants.DSA);
        algorithmNames.put("2.16.840.1.101.3.4.3.2", SecurityConstants.DSA);
        algorithmNames.put("1.3.14.3.2.29", SecurityConstants.RSA);
        algorithmNames.put("1.3.36.3.3.1.2", SecurityConstants.RSA);
        algorithmNames.put("1.3.36.3.3.1.3", SecurityConstants.RSA);
        algorithmNames.put("1.3.36.3.3.1.4", SecurityConstants.RSA);
        algorithmNames.put("1.2.643.2.2.19", "ECGOST3410");
    }

    public static String getAlgorithm(String oid) {
        String ret = (String) algorithmNames.get(oid);
        return ret == null ? oid : ret;
    }
}
