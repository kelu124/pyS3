package com.itextpdf.text.pdf.security;

import com.itextpdf.text.Utilities;
import com.itextpdf.text.error_messages.MessageLocalization;
import com.itextpdf.text.log.Logger;
import com.itextpdf.text.log.LoggerFactory;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PRIndirectReference;
import com.itextpdf.text.pdf.PdfArray;
import com.itextpdf.text.pdf.PdfDeveloperExtension;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfIndirectReference;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfObject;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfStream;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.spongycastle.asn1.ASN1EncodableVector;
import org.spongycastle.asn1.ASN1Enumerated;
import org.spongycastle.asn1.ASN1InputStream;
import org.spongycastle.asn1.DEROctetString;
import org.spongycastle.asn1.DERSequence;
import org.spongycastle.asn1.DERTaggedObject;
import org.spongycastle.asn1.ocsp.OCSPObjectIdentifiers;

public class LtvVerification {
    private Logger LOGGER = LoggerFactory.getLogger(LtvVerification.class);
    private AcroFields acroFields;
    private PdfReader reader;
    private PdfStamper stp;
    private boolean used = false;
    private Map<PdfName, ValidationData> validated = new HashMap();
    private PdfWriter writer;

    public enum CertificateInclusion {
        YES,
        NO
    }

    public enum CertificateOption {
        SIGNING_CERTIFICATE,
        WHOLE_CHAIN
    }

    public enum Level {
        OCSP,
        CRL,
        OCSP_CRL,
        OCSP_OPTIONAL_CRL
    }

    private static class ValidationData {
        public List<byte[]> certs;
        public List<byte[]> crls;
        public List<byte[]> ocsps;

        private ValidationData() {
            this.crls = new ArrayList();
            this.ocsps = new ArrayList();
            this.certs = new ArrayList();
        }
    }

    public LtvVerification(PdfStamper stp) {
        this.stp = stp;
        this.writer = stp.getWriter();
        this.reader = stp.getReader();
        this.acroFields = stp.getAcroFields();
    }

    public boolean addVerification(String signatureName, OcspClient ocsp, CrlClient crl, CertificateOption certOption, Level level, CertificateInclusion certInclude) throws IOException, GeneralSecurityException {
        if (this.used) {
            throw new IllegalStateException(MessageLocalization.getComposedMessage("verification.already.output", new Object[0]));
        }
        PdfPKCS7 pk = this.acroFields.verifySignature(signatureName);
        this.LOGGER.info("Adding verification for " + signatureName);
        Certificate[] xc = pk.getCertificates();
        X509Certificate signingCert = pk.getSigningCertificate();
        ValidationData vd = new ValidationData();
        for (X509Certificate cert : xc) {
            this.LOGGER.info("Certificate: " + cert.getSubjectDN());
            if (certOption != CertificateOption.SIGNING_CERTIFICATE || cert.equals(signingCert)) {
                byte[] ocspEnc = null;
                if (!(ocsp == null || level == Level.CRL)) {
                    ocspEnc = ocsp.getEncoded(cert, getParent(cert, xc), null);
                    if (ocspEnc != null) {
                        vd.ocsps.add(buildOCSPResponse(ocspEnc));
                        this.LOGGER.info("OCSP added");
                    }
                }
                if (crl != null && (level == Level.CRL || level == Level.OCSP_CRL || (level == Level.OCSP_OPTIONAL_CRL && ocspEnc == null))) {
                    Collection<byte[]> cims = crl.getEncoded(cert, null);
                    if (cims != null) {
                        for (byte[] cim : cims) {
                            boolean dup = false;
                            for (byte[] b : vd.crls) {
                                if (Arrays.equals(b, cim)) {
                                    dup = true;
                                    break;
                                }
                            }
                            if (!dup) {
                                vd.crls.add(cim);
                                this.LOGGER.info("CRL added");
                            }
                        }
                    }
                }
                if (certInclude == CertificateInclusion.YES) {
                    vd.certs.add(cert.getEncoded());
                }
            }
        }
        if (vd.crls.isEmpty() && vd.ocsps.isEmpty()) {
            return false;
        }
        this.validated.put(getSignatureHashKey(signatureName), vd);
        return true;
    }

    private X509Certificate getParent(X509Certificate cert, Certificate[] certs) {
        for (X509Certificate parent : certs) {
            if (cert.getIssuerDN().equals(parent.getSubjectDN())) {
                try {
                    cert.verify(parent.getPublicKey());
                    return parent;
                } catch (Exception e) {
                }
            }
        }
        return null;
    }

    public boolean addVerification(String signatureName, Collection<byte[]> ocsps, Collection<byte[]> crls, Collection<byte[]> certs) throws IOException, GeneralSecurityException {
        if (this.used) {
            throw new IllegalStateException(MessageLocalization.getComposedMessage("verification.already.output", new Object[0]));
        }
        ValidationData vd = new ValidationData();
        if (ocsps != null) {
            for (byte[] ocsp : ocsps) {
                vd.ocsps.add(buildOCSPResponse(ocsp));
            }
        }
        if (crls != null) {
            for (byte[] crl : crls) {
                vd.crls.add(crl);
            }
        }
        if (certs != null) {
            for (byte[] cert : certs) {
                vd.certs.add(cert);
            }
        }
        this.validated.put(getSignatureHashKey(signatureName), vd);
        return true;
    }

    private static byte[] buildOCSPResponse(byte[] BasicOCSPResponse) throws IOException {
        DEROctetString doctet = new DEROctetString(BasicOCSPResponse);
        ASN1EncodableVector v2 = new ASN1EncodableVector();
        v2.add(OCSPObjectIdentifiers.id_pkix_ocsp_basic);
        v2.add(doctet);
        ASN1Enumerated den = new ASN1Enumerated(0);
        ASN1EncodableVector v3 = new ASN1EncodableVector();
        v3.add(den);
        v3.add(new DERTaggedObject(true, 0, new DERSequence(v2)));
        return new DERSequence(v3).getEncoded();
    }

    private PdfName getSignatureHashKey(String signatureName) throws NoSuchAlgorithmException, IOException {
        PdfDictionary dic = this.acroFields.getSignatureDictionary(signatureName);
        byte[] bc = dic.getAsString(PdfName.CONTENTS).getOriginalBytes();
        if (PdfName.ETSI_RFC3161.equals(PdfReader.getPdfObject(dic.get(PdfName.SUBFILTER)))) {
            bc = new ASN1InputStream(new ByteArrayInputStream(bc)).readObject().getEncoded();
        }
        return new PdfName(Utilities.convertToHex(hashBytesSha1(bc)));
    }

    private static byte[] hashBytesSha1(byte[] b) throws NoSuchAlgorithmException {
        return MessageDigest.getInstance(SecurityConstants.SHA1).digest(b);
    }

    public void merge() throws IOException {
        if (!this.used && !this.validated.isEmpty()) {
            this.used = true;
            if (this.reader.getCatalog().get(PdfName.DSS) == null) {
                createDss();
            } else {
                updateDss();
            }
        }
    }

    private void updateDss() throws IOException {
        PdfDictionary catalog = this.reader.getCatalog();
        this.stp.markUsed(catalog);
        PdfDictionary dss = catalog.getAsDict(PdfName.DSS);
        PdfArray ocsps = dss.getAsArray(PdfName.OCSPS);
        PdfArray crls = dss.getAsArray(PdfName.CRLS);
        PdfArray certs = dss.getAsArray(PdfName.CERTS);
        dss.remove(PdfName.OCSPS);
        dss.remove(PdfName.CRLS);
        dss.remove(PdfName.CERTS);
        PdfDictionary vrim = dss.getAsDict(PdfName.VRI);
        if (vrim != null) {
            for (PdfName n : vrim.getKeys()) {
                if (this.validated.containsKey(n)) {
                    PdfDictionary vri = vrim.getAsDict(n);
                    if (vri != null) {
                        deleteOldReferences(ocsps, vri.getAsArray(PdfName.OCSP));
                        deleteOldReferences(crls, vri.getAsArray(PdfName.CRL));
                        deleteOldReferences(certs, vri.getAsArray(PdfName.CERT));
                    }
                }
            }
        }
        if (ocsps == null) {
            ocsps = new PdfArray();
        }
        if (crls == null) {
            crls = new PdfArray();
        }
        if (certs == null) {
            certs = new PdfArray();
        }
        outputDss(dss, vrim, ocsps, crls, certs);
    }

    private static void deleteOldReferences(PdfArray all, PdfArray toDelete) {
        if (all != null && toDelete != null) {
            Iterator i$ = toDelete.iterator();
            while (i$.hasNext()) {
                PdfObject pi = (PdfObject) i$.next();
                if (pi.isIndirect()) {
                    PRIndirectReference pir = (PRIndirectReference) pi;
                    int k = 0;
                    while (k < all.size()) {
                        PdfObject po = all.getPdfObject(k);
                        if (po.isIndirect()) {
                            if (pir.getNumber() == ((PRIndirectReference) po).getNumber()) {
                                all.remove(k);
                                k--;
                            }
                        }
                        k++;
                    }
                }
            }
        }
    }

    private void createDss() throws IOException {
        outputDss(new PdfDictionary(), new PdfDictionary(), new PdfArray(), new PdfArray(), new PdfArray());
    }

    private void outputDss(PdfDictionary dss, PdfDictionary vrim, PdfArray ocsps, PdfArray crls, PdfArray certs) throws IOException {
        this.writer.addDeveloperExtension(PdfDeveloperExtension.ESIC_1_7_EXTENSIONLEVEL5);
        PdfDictionary catalog = this.reader.getCatalog();
        this.stp.markUsed(catalog);
        for (PdfName vkey : this.validated.keySet()) {
            PdfArray ocsp = new PdfArray();
            PdfArray crl = new PdfArray();
            PdfArray cert = new PdfArray();
            PdfDictionary vri = new PdfDictionary();
            for (byte[] b : ((ValidationData) this.validated.get(vkey)).crls) {
                PdfStream ps = new PdfStream(b);
                ps.flateCompress();
                PdfIndirectReference iref = this.writer.addToBody(ps, false).getIndirectReference();
                crl.add(iref);
                crls.add(iref);
            }
            for (byte[] b2 : ((ValidationData) this.validated.get(vkey)).ocsps) {
                ps = new PdfStream(b2);
                ps.flateCompress();
                iref = this.writer.addToBody(ps, false).getIndirectReference();
                ocsp.add(iref);
                ocsps.add(iref);
            }
            for (byte[] b22 : ((ValidationData) this.validated.get(vkey)).certs) {
                ps = new PdfStream(b22);
                ps.flateCompress();
                iref = this.writer.addToBody(ps, false).getIndirectReference();
                cert.add(iref);
                certs.add(iref);
            }
            if (ocsp.size() > 0) {
                vri.put(PdfName.OCSP, this.writer.addToBody(ocsp, false).getIndirectReference());
            }
            if (crl.size() > 0) {
                vri.put(PdfName.CRL, this.writer.addToBody(crl, false).getIndirectReference());
            }
            if (cert.size() > 0) {
                vri.put(PdfName.CERT, this.writer.addToBody(cert, false).getIndirectReference());
            }
            vrim.put(vkey, this.writer.addToBody(vri, false).getIndirectReference());
        }
        dss.put(PdfName.VRI, this.writer.addToBody(vrim, false).getIndirectReference());
        if (ocsps.size() > 0) {
            dss.put(PdfName.OCSPS, this.writer.addToBody(ocsps, false).getIndirectReference());
        }
        if (crls.size() > 0) {
            dss.put(PdfName.CRLS, this.writer.addToBody(crls, false).getIndirectReference());
        }
        if (certs.size() > 0) {
            dss.put(PdfName.CERTS, this.writer.addToBody(certs, false).getIndirectReference());
        }
        catalog.put(PdfName.DSS, this.writer.addToBody(dss, false).getIndirectReference());
    }
}
