package com.itextpdf.text.pdf.security;

import com.itextpdf.text.log.Logger;
import com.itextpdf.text.log.LoggerFactory;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

public class RootStoreVerifier extends CertificateVerifier {
    protected static final Logger LOGGER = LoggerFactory.getLogger(RootStoreVerifier.class);
    protected KeyStore rootStore = null;

    public RootStoreVerifier(CertificateVerifier verifier) {
        super(verifier);
    }

    public void setRootStore(KeyStore keyStore) {
        this.rootStore = keyStore;
    }

    public List<VerificationOK> verify(X509Certificate signCert, X509Certificate issuerCert, Date signDate) throws GeneralSecurityException, IOException {
        LOGGER.info("Root store verification: " + signCert.getSubjectDN().getName());
        if (this.rootStore == null) {
            return super.verify(signCert, issuerCert, signDate);
        }
        try {
            List<VerificationOK> result = new ArrayList();
            Enumeration<String> aliases = this.rootStore.aliases();
            while (aliases.hasMoreElements()) {
                String alias = (String) aliases.nextElement();
                try {
                    if (this.rootStore.isCertificateEntry(alias)) {
                        signCert.verify(((X509Certificate) this.rootStore.getCertificate(alias)).getPublicKey());
                        LOGGER.info("Certificate verified against root store");
                        result.add(new VerificationOK(signCert, getClass(), "Certificate verified against root store."));
                        result.addAll(super.verify(signCert, issuerCert, signDate));
                        return result;
                    }
                } catch (GeneralSecurityException e) {
                }
            }
            result.addAll(super.verify(signCert, issuerCert, signDate));
            return result;
        } catch (GeneralSecurityException e2) {
            return super.verify(signCert, issuerCert, signDate);
        }
    }
}
