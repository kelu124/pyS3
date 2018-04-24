package com.itextpdf.text.pdf.security;

import java.security.KeyStore;
import java.security.cert.CRL;
import java.security.cert.Certificate;
import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.List;
import org.spongycastle.cert.ocsp.BasicOCSPResp;
import org.spongycastle.cms.jcajce.JcaSimpleSignerInfoVerifierBuilder;
import org.spongycastle.operator.jcajce.JcaContentVerifierProviderBuilder;
import org.spongycastle.tsp.TimeStampToken;

public class CertificateVerification {
    public static String verifyCertificate(X509Certificate cert, Collection<CRL> crls, Calendar calendar) {
        if (calendar == null) {
            calendar = new GregorianCalendar();
        }
        if (cert.hasUnsupportedCriticalExtension()) {
            for (String oid : cert.getCriticalExtensionOIDs()) {
                if (!"2.5.29.15".equals(oid) || !cert.getKeyUsage()[0]) {
                    try {
                        if ("2.5.29.37".equals(oid) && cert.getExtendedKeyUsage().contains("1.3.6.1.5.5.7.3.8")) {
                        }
                    } catch (CertificateParsingException e) {
                    }
                    return "Has unsupported critical extension";
                }
            }
        }
        try {
            cert.checkValidity(calendar.getTime());
            if (crls != null) {
                for (CRL crl : crls) {
                    if (crl.isRevoked(cert)) {
                        return "Certificate revoked";
                    }
                }
            }
            return null;
        } catch (Exception e2) {
            return e2.getMessage();
        }
    }

    public static List<VerificationException> verifyCertificates(Certificate[] certs, KeyStore keystore, Collection<CRL> crls, Calendar calendar) {
        List<VerificationException> result = new ArrayList();
        if (calendar == null) {
            calendar = new GregorianCalendar();
        }
        loop0:
        for (int k = 0; k < certs.length; k++) {
            X509Certificate cert = certs[k];
            String err = verifyCertificate(cert, crls, calendar);
            if (err != null) {
                result.add(new VerificationException(cert, err));
            }
            try {
                Enumeration<String> aliases = keystore.aliases();
                while (aliases.hasMoreElements()) {
                    try {
                        String alias = (String) aliases.nextElement();
                        if (keystore.isCertificateEntry(alias)) {
                            X509Certificate certStoreX509 = (X509Certificate) keystore.getCertificate(alias);
                            if (verifyCertificate(certStoreX509, crls, calendar) == null) {
                                try {
                                    cert.verify(certStoreX509.getPublicKey());
                                    break loop0;
                                } catch (Exception e) {
                                }
                            } else {
                                continue;
                            }
                        } else {
                            continue;
                        }
                    } catch (Exception e2) {
                    }
                }
            } catch (Exception e3) {
            }
            int j = 0;
            while (j < certs.length) {
                if (j != k) {
                    try {
                        cert.verify(certs[j].getPublicKey());
                        break;
                    } catch (Exception e4) {
                    }
                }
                j++;
            }
            if (j == certs.length) {
                result.add(new VerificationException(cert, "Cannot be verified against the KeyStore or the certificate chain"));
            }
        }
        if (result.size() == 0) {
            result.add(new VerificationException(null, "Invalid state. Possible circular certificate chain"));
        }
        return result;
    }

    public static List<VerificationException> verifyCertificates(Certificate[] certs, KeyStore keystore, Calendar calendar) {
        return verifyCertificates(certs, keystore, null, calendar);
    }

    public static boolean verifyOcspCertificates(BasicOCSPResp ocsp, KeyStore keystore, String provider) {
        if (provider == null) {
            provider = "BC";
        }
        try {
            Enumeration<String> aliases = keystore.aliases();
            while (aliases.hasMoreElements()) {
                try {
                    String alias = (String) aliases.nextElement();
                    if (keystore.isCertificateEntry(alias)) {
                        if (ocsp.isSignatureValid(new JcaContentVerifierProviderBuilder().setProvider(provider).build(((X509Certificate) keystore.getCertificate(alias)).getPublicKey()))) {
                            return true;
                        }
                    } else {
                        continue;
                    }
                } catch (Exception e) {
                }
            }
        } catch (Exception e2) {
        }
        return false;
    }

    public static boolean verifyTimestampCertificates(TimeStampToken ts, KeyStore keystore, String provider) {
        if (provider == null) {
            provider = "BC";
        }
        try {
            Enumeration<String> aliases = keystore.aliases();
            while (aliases.hasMoreElements()) {
                try {
                    String alias = (String) aliases.nextElement();
                    if (keystore.isCertificateEntry(alias)) {
                        ts.isSignatureValid(new JcaSimpleSignerInfoVerifierBuilder().setProvider(provider).build((X509Certificate) keystore.getCertificate(alias)));
                        return true;
                    }
                } catch (Exception e) {
                }
            }
        } catch (Exception e2) {
        }
        return false;
    }
}
