package com.itextpdf.text.pdf.security;

import com.itextpdf.text.log.Logger;
import com.itextpdf.text.log.LoggerFactory;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PRStream;
import com.itextpdf.text.pdf.PdfArray;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.security.LtvVerification.CertificateOption;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509CRL;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.spongycastle.cert.ocsp.BasicOCSPResp;
import org.spongycastle.cert.ocsp.OCSPException;
import org.spongycastle.cert.ocsp.OCSPResp;

public class LtvVerifier extends RootStoreVerifier {
    protected static final Logger LOGGER = LoggerFactory.getLogger(LtvVerifier.class);
    protected PdfDictionary dss;
    protected AcroFields fields;
    protected boolean latestRevision = true;
    protected CertificateOption option = CertificateOption.SIGNING_CERTIFICATE;
    protected PdfPKCS7 pkcs7;
    protected PdfReader reader;
    protected Date signDate;
    protected String signatureName;
    protected boolean verifyRootCertificate = true;

    public LtvVerifier(PdfReader reader) throws GeneralSecurityException {
        super(null);
        this.reader = reader;
        this.fields = reader.getAcroFields();
        List<String> names = this.fields.getSignatureNames();
        this.signatureName = (String) names.get(names.size() - 1);
        this.signDate = new Date();
        this.pkcs7 = coversWholeDocument();
        Logger logger = LOGGER;
        String str = "Checking %ssignature %s";
        Object[] objArr = new Object[2];
        objArr[0] = this.pkcs7.isTsp() ? "document-level timestamp " : "";
        objArr[1] = this.signatureName;
        logger.info(String.format(str, objArr));
    }

    public void setVerifier(CertificateVerifier verifier) {
        this.verifier = verifier;
    }

    public void setCertificateOption(CertificateOption option) {
        this.option = option;
    }

    public void setVerifyRootCertificate(boolean verifyRootCertificate) {
        this.verifyRootCertificate = verifyRootCertificate;
    }

    protected PdfPKCS7 coversWholeDocument() throws GeneralSecurityException {
        PdfPKCS7 pkcs7 = this.fields.verifySignature(this.signatureName);
        if (this.fields.signatureCoversWholeDocument(this.signatureName)) {
            LOGGER.info("The timestamp covers whole document.");
            if (pkcs7.verify()) {
                LOGGER.info("The signed document has not been modified.");
                return pkcs7;
            }
            throw new VerificationException(null, "The document was altered after the final signature was applied.");
        }
        throw new VerificationException(null, "Signature doesn't cover whole document.");
    }

    public List<VerificationOK> verify(List<VerificationOK> result) throws IOException, GeneralSecurityException {
        if (result == null) {
            result = new ArrayList();
        }
        while (this.pkcs7 != null) {
            result.addAll(verifySignature());
        }
        return result;
    }

    public List<VerificationOK> verifySignature() throws GeneralSecurityException, IOException {
        LOGGER.info("Verifying signature.");
        List<VerificationOK> result = new ArrayList();
        Certificate[] chain = this.pkcs7.getSignCertificateChain();
        verifyChain(chain);
        int total = 1;
        if (CertificateOption.WHOLE_CHAIN.equals(this.option)) {
            total = chain.length;
        }
        int i = 0;
        while (i < total) {
            int i2 = i + 1;
            X509Certificate signCert = chain[i];
            X509Certificate issuerCert = null;
            if (i2 < chain.length) {
                issuerCert = chain[i2];
            }
            LOGGER.info(signCert.getSubjectDN().getName());
            List<VerificationOK> list = verify(signCert, issuerCert, this.signDate);
            if (list.size() == 0) {
                signCert.verify(signCert.getPublicKey());
                if (this.latestRevision && chain.length > 1) {
                    list.add(new VerificationOK(signCert, getClass(), "Root certificate in final revision"));
                }
                if (list.size() == 0 && this.verifyRootCertificate) {
                    throw new GeneralSecurityException();
                }
                try {
                    if (chain.length > 1) {
                        list.add(new VerificationOK(signCert, getClass(), "Root certificate passed without checking"));
                    }
                } catch (GeneralSecurityException e) {
                    throw new VerificationException(signCert, "Couldn't verify with CRL or OCSP or trusted anchor");
                }
            }
            result.addAll(list);
            i = i2;
        }
        switchToPreviousRevision();
        return result;
    }

    public void verifyChain(Certificate[] chain) throws GeneralSecurityException {
        for (int i = 0; i < chain.length; i++) {
            chain[i].checkValidity(this.signDate);
            if (i > 0) {
                chain[i - 1].verify(chain[i].getPublicKey());
            }
        }
        LOGGER.info("All certificates are valid on " + this.signDate.toString());
    }

    public List<VerificationOK> verify(X509Certificate signCert, X509Certificate issuerCert, Date signDate) throws GeneralSecurityException, IOException {
        boolean z;
        boolean z2 = false;
        RootStoreVerifier rootStoreVerifier = new RootStoreVerifier(this.verifier);
        rootStoreVerifier.setRootStore(this.rootStore);
        CRLVerifier crlVerifier = new CRLVerifier(rootStoreVerifier, getCRLsFromDSS());
        crlVerifier.setRootStore(this.rootStore);
        if (this.latestRevision || this.onlineCheckingAllowed) {
            z = true;
        } else {
            z = false;
        }
        crlVerifier.setOnlineCheckingAllowed(z);
        OCSPVerifier ocspVerifier = new OCSPVerifier(crlVerifier, getOCSPResponsesFromDSS());
        ocspVerifier.setRootStore(this.rootStore);
        if (this.latestRevision || this.onlineCheckingAllowed) {
            z2 = true;
        }
        ocspVerifier.setOnlineCheckingAllowed(z2);
        return ocspVerifier.verify(signCert, issuerCert, signDate);
    }

    public void switchToPreviousRevision() throws IOException, GeneralSecurityException {
        LOGGER.info("Switching to previous revision.");
        this.latestRevision = false;
        this.dss = this.reader.getCatalog().getAsDict(PdfName.DSS);
        Calendar cal = this.pkcs7.getTimeStampDate();
        if (cal == null) {
            cal = this.pkcs7.getSignDate();
        }
        this.signDate = cal.getTime();
        List<String> names = this.fields.getSignatureNames();
        if (names.size() > 1) {
            this.signatureName = (String) names.get(names.size() - 2);
            this.reader = new PdfReader(this.fields.extractRevision(this.signatureName));
            this.fields = this.reader.getAcroFields();
            names = this.fields.getSignatureNames();
            this.signatureName = (String) names.get(names.size() - 1);
            this.pkcs7 = coversWholeDocument();
            Logger logger = LOGGER;
            String str = "Checking %ssignature %s";
            Object[] objArr = new Object[2];
            objArr[0] = this.pkcs7.isTsp() ? "document-level timestamp " : "";
            objArr[1] = this.signatureName;
            logger.info(String.format(str, objArr));
            return;
        }
        LOGGER.info("No signatures in revision");
        this.pkcs7 = null;
    }

    public List<X509CRL> getCRLsFromDSS() throws GeneralSecurityException, IOException {
        List<X509CRL> crls = new ArrayList();
        if (this.dss != null) {
            PdfArray crlarray = this.dss.getAsArray(PdfName.CRLS);
            if (crlarray != null) {
                CertificateFactory cf = CertificateFactory.getInstance("X.509");
                for (int i = 0; i < crlarray.size(); i++) {
                    crls.add((X509CRL) cf.generateCRL(new ByteArrayInputStream(PdfReader.getStreamBytes((PRStream) crlarray.getAsStream(i)))));
                }
            }
        }
        return crls;
    }

    public List<BasicOCSPResp> getOCSPResponsesFromDSS() throws IOException, GeneralSecurityException {
        List<BasicOCSPResp> ocsps = new ArrayList();
        if (this.dss != null) {
            PdfArray ocsparray = this.dss.getAsArray(PdfName.OCSPS);
            if (ocsparray != null) {
                for (int i = 0; i < ocsparray.size(); i++) {
                    OCSPResp ocspResponse = new OCSPResp(PdfReader.getStreamBytes((PRStream) ocsparray.getAsStream(i)));
                    if (ocspResponse.getStatus() == 0) {
                        try {
                            ocsps.add((BasicOCSPResp) ocspResponse.getResponseObject());
                        } catch (OCSPException e) {
                            throw new GeneralSecurityException(e);
                        }
                    }
                }
            }
        }
        return ocsps;
    }
}
