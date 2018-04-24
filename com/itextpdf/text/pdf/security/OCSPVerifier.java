package com.itextpdf.text.pdf.security;

import com.itextpdf.text.log.Logger;
import com.itextpdf.text.log.LoggerFactory;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import org.spongycastle.cert.X509CertificateHolder;
import org.spongycastle.cert.jcajce.JcaX509CertificateConverter;
import org.spongycastle.cert.ocsp.BasicOCSPResp;
import org.spongycastle.cert.ocsp.CertificateStatus;
import org.spongycastle.cert.ocsp.OCSPException;
import org.spongycastle.cert.ocsp.SingleResp;
import org.spongycastle.operator.OperatorCreationException;
import org.spongycastle.operator.bc.BcDigestCalculatorProvider;
import org.spongycastle.operator.jcajce.JcaContentVerifierProviderBuilder;

public class OCSPVerifier extends RootStoreVerifier {
    protected static final Logger LOGGER = LoggerFactory.getLogger(OCSPVerifier.class);
    protected List<BasicOCSPResp> ocsps;

    public OCSPVerifier(CertificateVerifier verifier, List<BasicOCSPResp> ocsps) {
        super(verifier);
        this.ocsps = ocsps;
    }

    public List<VerificationOK> verify(X509Certificate signCert, X509Certificate issuerCert, Date signDate) throws GeneralSecurityException, IOException {
        List<VerificationOK> result = new ArrayList();
        int validOCSPsFound = 0;
        if (this.ocsps != null) {
            for (BasicOCSPResp ocspResp : this.ocsps) {
                if (verify(ocspResp, signCert, issuerCert, signDate)) {
                    validOCSPsFound++;
                }
            }
        }
        boolean online = false;
        if (this.onlineCheckingAllowed && validOCSPsFound == 0 && verify(getOcspResponse(signCert, issuerCert), signCert, issuerCert, signDate)) {
            validOCSPsFound++;
            online = true;
        }
        LOGGER.info("Valid OCSPs found: " + validOCSPsFound);
        if (validOCSPsFound > 0) {
            result.add(new VerificationOK(signCert, getClass(), "Valid OCSPs Found: " + validOCSPsFound + (online ? " (online)" : "")));
        }
        if (this.verifier != null) {
            result.addAll(this.verifier.verify(signCert, issuerCert, signDate));
        }
        return result;
    }

    public boolean verify(BasicOCSPResp ocspResp, X509Certificate signCert, X509Certificate issuerCert, Date signDate) throws GeneralSecurityException, IOException {
        if (ocspResp == null) {
            return false;
        }
        SingleResp[] resp = ocspResp.getResponses();
        for (int i = 0; i < resp.length; i++) {
            if (signCert.getSerialNumber().equals(resp[i].getCertID().getSerialNumber())) {
                if (issuerCert == null) {
                    issuerCert = signCert;
                }
                try {
                    if (resp[i].getCertID().matchesIssuer(new X509CertificateHolder(issuerCert.getEncoded()), new BcDigestCalculatorProvider())) {
                        Date nextUpdate = resp[i].getNextUpdate();
                        if (nextUpdate == null) {
                            nextUpdate = new Date(resp[i].getThisUpdate().getTime() + 180000);
                            LOGGER.info(String.format("No 'next update' for OCSP Response; assuming %s", new Object[]{nextUpdate}));
                        }
                        if (signDate.after(nextUpdate)) {
                            LOGGER.info(String.format("OCSP no longer valid: %s after %s", new Object[]{signDate, nextUpdate}));
                        } else if (resp[i].getCertStatus() == CertificateStatus.GOOD) {
                            isValidResponse(ocspResp, issuerCert);
                            return true;
                        }
                    } else {
                        LOGGER.info("OCSP: Issuers doesn't match.");
                    }
                } catch (OCSPException e) {
                }
            }
        }
        return false;
    }

    public void isValidResponse(BasicOCSPResp ocspResp, X509Certificate issuerCert) throws GeneralSecurityException, IOException {
        X509Certificate responderCert = issuerCert;
        X509CertificateHolder[] certHolders = ocspResp.getCerts();
        if (certHolders.length > 0) {
            responderCert = new JcaX509CertificateConverter().setProvider("BC").getCertificate(certHolders[0]);
            try {
                responderCert.verify(issuerCert.getPublicKey());
            } catch (GeneralSecurityException e) {
                if (super.verify(responderCert, issuerCert, null).size() == 0) {
                    throw new VerificationException(responderCert, "Responder certificate couldn't be verified");
                }
            }
        }
        if (!verifyResponse(ocspResp, responderCert)) {
            throw new VerificationException(responderCert, "OCSP response could not be verified");
        }
    }

    public boolean verifyResponse(BasicOCSPResp ocspResp, X509Certificate responderCert) {
        if (isSignatureValid(ocspResp, responderCert)) {
            return true;
        }
        if (this.rootStore == null) {
            return false;
        }
        try {
            Enumeration<String> aliases = this.rootStore.aliases();
            while (aliases.hasMoreElements()) {
                String alias = (String) aliases.nextElement();
                try {
                    if (this.rootStore.isCertificateEntry(alias) && isSignatureValid(ocspResp, (X509Certificate) this.rootStore.getCertificate(alias))) {
                        return true;
                    }
                } catch (GeneralSecurityException e) {
                }
            }
            return false;
        } catch (GeneralSecurityException e2) {
            return false;
        }
    }

    public boolean isSignatureValid(BasicOCSPResp ocspResp, Certificate responderCert) {
        boolean z = false;
        try {
            z = ocspResp.isSignatureValid(new JcaContentVerifierProviderBuilder().setProvider("BC").build(responderCert.getPublicKey()));
        } catch (OperatorCreationException e) {
        } catch (OCSPException e2) {
        }
        return z;
    }

    public BasicOCSPResp getOcspResponse(X509Certificate signCert, X509Certificate issuerCert) {
        if (signCert == null && issuerCert == null) {
            return null;
        }
        BasicOCSPResp ocspResp = new OcspClientBouncyCastle().getBasicOCSPResp(signCert, issuerCert, null);
        if (ocspResp == null) {
            return null;
        }
        SingleResp[] resp = ocspResp.getResponses();
        for (SingleResp certStatus : resp) {
            if (certStatus.getCertStatus() == CertificateStatus.GOOD) {
                return ocspResp;
            }
        }
        return null;
    }
}
