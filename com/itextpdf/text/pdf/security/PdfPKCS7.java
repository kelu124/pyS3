package com.itextpdf.text.pdf.security;

import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.error_messages.MessageLocalization;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.security.MakeSignature.CryptoStandard;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.CRL;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509CRL;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Set;
import org.spongycastle.asn1.ASN1EncodableVector;
import org.spongycastle.asn1.ASN1Enumerated;
import org.spongycastle.asn1.ASN1InputStream;
import org.spongycastle.asn1.ASN1Integer;
import org.spongycastle.asn1.ASN1ObjectIdentifier;
import org.spongycastle.asn1.ASN1OctetString;
import org.spongycastle.asn1.ASN1OutputStream;
import org.spongycastle.asn1.ASN1Primitive;
import org.spongycastle.asn1.ASN1Sequence;
import org.spongycastle.asn1.ASN1Set;
import org.spongycastle.asn1.ASN1TaggedObject;
import org.spongycastle.asn1.DERNull;
import org.spongycastle.asn1.DEROctetString;
import org.spongycastle.asn1.DERSequence;
import org.spongycastle.asn1.DERSet;
import org.spongycastle.asn1.DERTaggedObject;
import org.spongycastle.asn1.DERUTCTime;
import org.spongycastle.asn1.cms.Attribute;
import org.spongycastle.asn1.cms.AttributeTable;
import org.spongycastle.asn1.cms.ContentInfo;
import org.spongycastle.asn1.ess.ESSCertID;
import org.spongycastle.asn1.ess.ESSCertIDv2;
import org.spongycastle.asn1.ess.SigningCertificate;
import org.spongycastle.asn1.ess.SigningCertificateV2;
import org.spongycastle.asn1.ocsp.BasicOCSPResponse;
import org.spongycastle.asn1.ocsp.OCSPObjectIdentifiers;
import org.spongycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.spongycastle.asn1.tsp.MessageImprint;
import org.spongycastle.asn1.x509.AlgorithmIdentifier;
import org.spongycastle.cert.jcajce.JcaX509CertificateHolder;
import org.spongycastle.cert.ocsp.BasicOCSPResp;
import org.spongycastle.cert.ocsp.CertificateID;
import org.spongycastle.jce.X509Principal;
import org.spongycastle.jce.provider.X509CertParser;
import org.spongycastle.operator.jcajce.JcaDigestCalculatorProviderBuilder;
import org.spongycastle.tsp.TimeStampToken;
import org.spongycastle.tsp.TimeStampTokenInfo;

public class PdfPKCS7 {
    private byte[] RSAdata;
    private BasicOCSPResp basicResp;
    private Collection<Certificate> certs;
    private Collection<CRL> crls;
    private byte[] digest;
    private String digestAlgorithmOid;
    private byte[] digestAttr;
    private String digestEncryptionAlgorithmOid;
    private Set<String> digestalgos;
    private MessageDigest encContDigest;
    private byte[] externalDigest;
    private byte[] externalRSAdata;
    private PdfName filterSubtype;
    private ExternalDigest interfaceDigest;
    private boolean isCades;
    private boolean isTsp;
    private String location;
    private MessageDigest messageDigest;
    private String provider;
    private String reason;
    private Signature sig;
    private byte[] sigAttr;
    private byte[] sigAttrDer;
    private X509Certificate signCert;
    private Collection<Certificate> signCerts;
    private Calendar signDate;
    private String signName;
    private int signerversion = 1;
    private TimeStampToken timeStampToken;
    private boolean verified;
    private boolean verifyResult;
    private int version = 1;

    public PdfPKCS7(PrivateKey privKey, Certificate[] certChain, String hashAlgorithm, String provider, ExternalDigest interfaceDigest, boolean hasRSAdata) throws InvalidKeyException, NoSuchProviderException, NoSuchAlgorithmException {
        this.provider = provider;
        this.interfaceDigest = interfaceDigest;
        this.digestAlgorithmOid = DigestAlgorithms.getAllowedDigests(hashAlgorithm);
        if (this.digestAlgorithmOid == null) {
            throw new NoSuchAlgorithmException(MessageLocalization.getComposedMessage("unknown.hash.algorithm.1", hashAlgorithm));
        }
        this.signCert = (X509Certificate) certChain[0];
        this.certs = new ArrayList();
        for (Certificate element : certChain) {
            this.certs.add(element);
        }
        this.digestalgos = new HashSet();
        this.digestalgos.add(this.digestAlgorithmOid);
        if (privKey != null) {
            this.digestEncryptionAlgorithmOid = privKey.getAlgorithm();
            if (this.digestEncryptionAlgorithmOid.equals(SecurityConstants.RSA)) {
                this.digestEncryptionAlgorithmOid = SecurityIDs.ID_RSA;
            } else if (this.digestEncryptionAlgorithmOid.equals(SecurityConstants.DSA)) {
                this.digestEncryptionAlgorithmOid = SecurityIDs.ID_DSA;
            } else {
                throw new NoSuchAlgorithmException(MessageLocalization.getComposedMessage("unknown.key.algorithm.1", this.digestEncryptionAlgorithmOid));
            }
        }
        if (hasRSAdata) {
            this.RSAdata = new byte[0];
            this.messageDigest = DigestAlgorithms.getMessageDigest(getHashAlgorithm(), provider);
        }
        if (privKey != null) {
            this.sig = initSignature(privKey);
        }
    }

    public PdfPKCS7(byte[] contentsKey, byte[] certsKey, String provider) {
        try {
            this.provider = provider;
            X509CertParser cr = new X509CertParser();
            cr.engineInit(new ByteArrayInputStream(certsKey));
            this.certs = cr.engineReadAll();
            this.signCerts = this.certs;
            this.signCert = (X509Certificate) this.certs.iterator().next();
            this.crls = new ArrayList();
            this.digest = ((ASN1OctetString) new ASN1InputStream(new ByteArrayInputStream(contentsKey)).readObject()).getOctets();
            if (provider == null) {
                this.sig = Signature.getInstance("SHA1withRSA");
            } else {
                this.sig = Signature.getInstance("SHA1withRSA", provider);
            }
            this.sig.initVerify(this.signCert.getPublicKey());
            this.digestAlgorithmOid = "1.2.840.10040.4.3";
            this.digestEncryptionAlgorithmOid = "1.3.36.3.3.1.2";
        } catch (Exception e) {
            throw new ExceptionConverter(e);
        }
    }

    public PdfPKCS7(byte[] contentsKey, PdfName filterSubtype, String provider) {
        this.filterSubtype = filterSubtype;
        this.isTsp = PdfName.ETSI_RFC3161.equals(filterSubtype);
        this.isCades = PdfName.ETSI_CADES_DETACHED.equals(filterSubtype);
        try {
            this.provider = provider;
            ASN1Primitive pkcs = new ASN1InputStream(new ByteArrayInputStream(contentsKey)).readObject();
            if (pkcs instanceof ASN1Sequence) {
                ASN1Sequence signedData = (ASN1Sequence) pkcs;
                if (((ASN1ObjectIdentifier) signedData.getObjectAt(0)).getId().equals(SecurityIDs.ID_PKCS7_SIGNED_DATA)) {
                    ASN1Sequence content = (ASN1Sequence) ((ASN1TaggedObject) signedData.getObjectAt(1)).getObject();
                    this.version = ((ASN1Integer) content.getObjectAt(0)).getValue().intValue();
                    this.digestalgos = new HashSet();
                    Enumeration<ASN1Sequence> e = ((ASN1Set) content.getObjectAt(1)).getObjects();
                    while (e.hasMoreElements()) {
                        this.digestalgos.add(((ASN1ObjectIdentifier) ((ASN1Sequence) e.nextElement()).getObjectAt(0)).getId());
                    }
                    ASN1Sequence rsaData = (ASN1Sequence) content.getObjectAt(2);
                    if (rsaData.size() > 1) {
                        this.RSAdata = ((ASN1OctetString) ((ASN1TaggedObject) rsaData.getObjectAt(1)).getObject()).getOctets();
                    }
                    int next = 3;
                    while (content.getObjectAt(next) instanceof ASN1TaggedObject) {
                        next++;
                    }
                    X509CertParser cr = new X509CertParser();
                    cr.engineInit(new ByteArrayInputStream(contentsKey));
                    this.certs = cr.engineReadAll();
                    ASN1Set signerInfos = (ASN1Set) content.getObjectAt(next);
                    if (signerInfos.size() != 1) {
                        throw new IllegalArgumentException(MessageLocalization.getComposedMessage("this.pkcs.7.object.has.multiple.signerinfos.only.one.is.supported.at.this.time", new Object[0]));
                    }
                    ASN1Sequence signerInfo = (ASN1Sequence) signerInfos.getObjectAt(0);
                    this.signerversion = ((ASN1Integer) signerInfo.getObjectAt(0)).getValue().intValue();
                    ASN1Sequence issuerAndSerialNumber = (ASN1Sequence) signerInfo.getObjectAt(1);
                    X509Principal x509Principal = new X509Principal(issuerAndSerialNumber.getObjectAt(0).toASN1Primitive().getEncoded());
                    BigInteger serialNumber = ((ASN1Integer) issuerAndSerialNumber.getObjectAt(1)).getValue();
                    for (X509Certificate cert : this.certs) {
                        if (cert.getIssuerDN().equals(x509Principal) && serialNumber.equals(cert.getSerialNumber())) {
                            this.signCert = cert;
                            break;
                        }
                    }
                    if (this.signCert == null) {
                        throw new IllegalArgumentException(MessageLocalization.getComposedMessage("can.t.find.signing.certificate.with.serial.1", x509Principal.getName() + " / " + serialNumber.toString(16)));
                    }
                    int next2;
                    signCertificateChain();
                    this.digestAlgorithmOid = ((ASN1ObjectIdentifier) ((ASN1Sequence) signerInfo.getObjectAt(2)).getObjectAt(0)).getId();
                    boolean foundCades = false;
                    if (signerInfo.getObjectAt(3) instanceof ASN1TaggedObject) {
                        ASN1Set sseq = ASN1Set.getInstance((ASN1TaggedObject) signerInfo.getObjectAt(3), false);
                        this.sigAttr = sseq.getEncoded();
                        this.sigAttrDer = sseq.getEncoded("DER");
                        for (int k = 0; k < sseq.size(); k++) {
                            ASN1Sequence seq2 = (ASN1Sequence) sseq.getObjectAt(k);
                            String idSeq2 = ((ASN1ObjectIdentifier) seq2.getObjectAt(0)).getId();
                            if (idSeq2.equals(SecurityIDs.ID_MESSAGE_DIGEST)) {
                                this.digestAttr = ((ASN1OctetString) ((ASN1Set) seq2.getObjectAt(1)).getObjectAt(0)).getOctets();
                            } else if (idSeq2.equals(SecurityIDs.ID_ADBE_REVOCATION)) {
                                ASN1Sequence seqout = (ASN1Sequence) ((ASN1Set) seq2.getObjectAt(1)).getObjectAt(0);
                                for (int j = 0; j < seqout.size(); j++) {
                                    ASN1TaggedObject tg = (ASN1TaggedObject) seqout.getObjectAt(j);
                                    if (tg.getTagNo() == 0) {
                                        findCRL((ASN1Sequence) tg.getObject());
                                    }
                                    if (tg.getTagNo() == 1) {
                                        findOcsp((ASN1Sequence) tg.getObject());
                                    }
                                }
                            } else if (this.isCades && idSeq2.equals(SecurityIDs.ID_AA_SIGNING_CERTIFICATE_V1)) {
                                ESSCertID cerv2 = SigningCertificate.getInstance((ASN1Sequence) ((ASN1Set) seq2.getObjectAt(1)).getObjectAt(0)).getCerts()[0];
                                if (Arrays.equals(new BouncyCastleDigest().getMessageDigest(DigestAlgorithms.SHA1).digest(this.signCert.getEncoded()), cerv2.getCertHash())) {
                                    foundCades = true;
                                } else {
                                    throw new IllegalArgumentException("Signing certificate doesn't match the ESS information.");
                                }
                            } else if (this.isCades && idSeq2.equals(SecurityIDs.ID_AA_SIGNING_CERTIFICATE_V2)) {
                                ESSCertIDv2 cerv22 = SigningCertificateV2.getInstance((ASN1Sequence) ((ASN1Set) seq2.getObjectAt(1)).getObjectAt(0)).getCerts()[0];
                                AlgorithmIdentifier ai2 = cerv22.getHashAlgorithm();
                                if (Arrays.equals(new BouncyCastleDigest().getMessageDigest(DigestAlgorithms.getDigest(ai2.getAlgorithm().getId())).digest(this.signCert.getEncoded()), cerv22.getCertHash())) {
                                    foundCades = true;
                                } else {
                                    throw new IllegalArgumentException("Signing certificate doesn't match the ESS information.");
                                }
                            }
                        }
                        if (this.digestAttr == null) {
                            throw new IllegalArgumentException(MessageLocalization.getComposedMessage("authenticated.attribute.is.missing.the.digest", new Object[0]));
                        }
                        next2 = 3 + 1;
                    } else {
                        next2 = 3;
                    }
                    if (!this.isCades || foundCades) {
                        next = next2 + 1;
                        this.digestEncryptionAlgorithmOid = ((ASN1ObjectIdentifier) ((ASN1Sequence) signerInfo.getObjectAt(next2)).getObjectAt(0)).getId();
                        next2 = next + 1;
                        this.digest = ((ASN1OctetString) signerInfo.getObjectAt(next)).getOctets();
                        if (next2 < signerInfo.size() && (signerInfo.getObjectAt(next2) instanceof ASN1TaggedObject)) {
                            Attribute ts = new AttributeTable(ASN1Set.getInstance((ASN1TaggedObject) signerInfo.getObjectAt(next2), false)).get(PKCSObjectIdentifiers.id_aa_signatureTimeStampToken);
                            if (ts != null && ts.getAttrValues().size() > 0) {
                                this.timeStampToken = new TimeStampToken(new ContentInfo(ASN1Sequence.getInstance(ts.getAttrValues().getObjectAt(0))));
                            }
                        }
                        if (this.isTsp) {
                            this.timeStampToken = new TimeStampToken(new ContentInfo(signedData));
                            this.messageDigest = DigestAlgorithms.getMessageDigestFromOid(this.timeStampToken.getTimeStampInfo().getMessageImprintAlgOID().getId(), null);
                            return;
                        }
                        if (!(this.RSAdata == null && this.digestAttr == null)) {
                            if (PdfName.ADBE_PKCS7_SHA1.equals(getFilterSubtype())) {
                                this.messageDigest = DigestAlgorithms.getMessageDigest(SecurityConstants.SHA1, provider);
                            } else {
                                this.messageDigest = DigestAlgorithms.getMessageDigest(getHashAlgorithm(), provider);
                            }
                            this.encContDigest = DigestAlgorithms.getMessageDigest(getHashAlgorithm(), provider);
                        }
                        this.sig = initSignature(this.signCert.getPublicKey());
                        return;
                    }
                    throw new IllegalArgumentException("CAdES ESS information missing.");
                }
                throw new IllegalArgumentException(MessageLocalization.getComposedMessage("not.a.valid.pkcs.7.object.not.signed.data", new Object[0]));
            }
            throw new IllegalArgumentException(MessageLocalization.getComposedMessage("not.a.valid.pkcs.7.object.not.a.sequence", new Object[0]));
        } catch (IOException e2) {
            throw new IllegalArgumentException(MessageLocalization.getComposedMessage("can.t.decode.pkcs7signeddata.object", new Object[0]));
        } catch (Exception e3) {
            throw new ExceptionConverter(e3);
        }
    }

    public String getSignName() {
        return this.signName;
    }

    public void setSignName(String signName) {
        this.signName = signName;
    }

    public String getReason() {
        return this.reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getLocation() {
        return this.location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Calendar getSignDate() {
        Calendar dt = getTimeStampDate();
        if (dt == null) {
            return this.signDate;
        }
        return dt;
    }

    public void setSignDate(Calendar signDate) {
        this.signDate = signDate;
    }

    public int getVersion() {
        return this.version;
    }

    public int getSigningInfoVersion() {
        return this.signerversion;
    }

    public String getDigestAlgorithmOid() {
        return this.digestAlgorithmOid;
    }

    public String getHashAlgorithm() {
        return DigestAlgorithms.getDigest(this.digestAlgorithmOid);
    }

    public String getDigestEncryptionAlgorithmOid() {
        return this.digestEncryptionAlgorithmOid;
    }

    public String getDigestAlgorithm() {
        return getHashAlgorithm() + "with" + getEncryptionAlgorithm();
    }

    public void setExternalDigest(byte[] digest, byte[] RSAdata, String digestEncryptionAlgorithm) {
        this.externalDigest = digest;
        this.externalRSAdata = RSAdata;
        if (digestEncryptionAlgorithm == null) {
            return;
        }
        if (digestEncryptionAlgorithm.equals(SecurityConstants.RSA)) {
            this.digestEncryptionAlgorithmOid = SecurityIDs.ID_RSA;
        } else if (digestEncryptionAlgorithm.equals(SecurityConstants.DSA)) {
            this.digestEncryptionAlgorithmOid = SecurityIDs.ID_DSA;
        } else if (digestEncryptionAlgorithm.equals("ECDSA")) {
            this.digestEncryptionAlgorithmOid = SecurityIDs.ID_ECDSA;
        } else {
            throw new ExceptionConverter(new NoSuchAlgorithmException(MessageLocalization.getComposedMessage("unknown.key.algorithm.1", digestEncryptionAlgorithm)));
        }
    }

    private Signature initSignature(PrivateKey key) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException {
        Signature signature;
        if (this.provider == null) {
            signature = Signature.getInstance(getDigestAlgorithm());
        } else {
            signature = Signature.getInstance(getDigestAlgorithm(), this.provider);
        }
        signature.initSign(key);
        return signature;
    }

    private Signature initSignature(PublicKey key) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException {
        Signature signature;
        String digestAlgorithm = getDigestAlgorithm();
        if (PdfName.ADBE_X509_RSA_SHA1.equals(getFilterSubtype())) {
            digestAlgorithm = "SHA1withRSA";
        }
        if (this.provider == null) {
            signature = Signature.getInstance(digestAlgorithm);
        } else {
            signature = Signature.getInstance(digestAlgorithm, this.provider);
        }
        signature.initVerify(key);
        return signature;
    }

    public void update(byte[] buf, int off, int len) throws SignatureException {
        if (this.RSAdata == null && this.digestAttr == null && !this.isTsp) {
            this.sig.update(buf, off, len);
        } else {
            this.messageDigest.update(buf, off, len);
        }
    }

    public byte[] getEncodedPKCS1() {
        try {
            if (this.externalDigest != null) {
                this.digest = this.externalDigest;
            } else {
                this.digest = this.sig.sign();
            }
            ByteArrayOutputStream bOut = new ByteArrayOutputStream();
            ASN1OutputStream dout = new ASN1OutputStream(bOut);
            dout.writeObject(new DEROctetString(this.digest));
            dout.close();
            return bOut.toByteArray();
        } catch (Exception e) {
            throw new ExceptionConverter(e);
        }
    }

    public byte[] getEncodedPKCS7() {
        return getEncodedPKCS7(null, null, null, null, null, CryptoStandard.CMS);
    }

    public byte[] getEncodedPKCS7(byte[] secondDigest, Calendar signingTime) {
        return getEncodedPKCS7(secondDigest, signingTime, null, null, null, CryptoStandard.CMS);
    }

    public byte[] getEncodedPKCS7(byte[] secondDigest, Calendar signingTime, TSAClient tsaClient, byte[] ocsp, Collection<byte[]> crlBytes, CryptoStandard sigtype) {
        try {
            if (this.externalDigest != null) {
                this.digest = this.externalDigest;
                if (this.RSAdata != null) {
                    this.RSAdata = this.externalRSAdata;
                }
            } else if (this.externalRSAdata == null || this.RSAdata == null) {
                if (this.RSAdata != null) {
                    this.RSAdata = this.messageDigest.digest();
                    this.sig.update(this.RSAdata);
                }
                this.digest = this.sig.sign();
            } else {
                this.RSAdata = this.externalRSAdata;
                this.sig.update(this.RSAdata);
                this.digest = this.sig.sign();
            }
            ASN1EncodableVector digestAlgorithms = new ASN1EncodableVector();
            for (Object element : this.digestalgos) {
                ASN1EncodableVector algos = new ASN1EncodableVector();
                algos.add(new ASN1ObjectIdentifier((String) element));
                algos.add(DERNull.INSTANCE);
                digestAlgorithms.add(new DERSequence(algos));
            }
            ASN1EncodableVector v = new ASN1EncodableVector();
            v.add(new ASN1ObjectIdentifier(SecurityIDs.ID_PKCS7_DATA));
            if (this.RSAdata != null) {
                v.add(new DERTaggedObject(0, new DEROctetString(this.RSAdata)));
            }
            DERSequence contentinfo = new DERSequence(v);
            v = new ASN1EncodableVector();
            for (X509Certificate element2 : this.certs) {
                v.add(new ASN1InputStream(new ByteArrayInputStream(element2.getEncoded())).readObject());
            }
            DERSet dercertificates = new DERSet(v);
            ASN1EncodableVector signerinfo = new ASN1EncodableVector();
            signerinfo.add(new ASN1Integer(this.signerversion));
            v = new ASN1EncodableVector();
            v.add(CertificateInfo.getIssuer(this.signCert.getTBSCertificate()));
            v.add(new ASN1Integer(this.signCert.getSerialNumber()));
            signerinfo.add(new DERSequence(v));
            v = new ASN1EncodableVector();
            v.add(new ASN1ObjectIdentifier(this.digestAlgorithmOid));
            v.add(new DERNull());
            signerinfo.add(new DERSequence(v));
            if (!(secondDigest == null || signingTime == null)) {
                signerinfo.add(new DERTaggedObject(false, 0, getAuthenticatedAttributeSet(secondDigest, signingTime, ocsp, crlBytes, sigtype)));
            }
            v = new ASN1EncodableVector();
            v.add(new ASN1ObjectIdentifier(this.digestEncryptionAlgorithmOid));
            v.add(new DERNull());
            signerinfo.add(new DERSequence(v));
            signerinfo.add(new DEROctetString(this.digest));
            if (tsaClient != null) {
                byte[] tsToken = tsaClient.getTimeStampToken(tsaClient.getMessageDigest().digest(this.digest));
                if (tsToken != null) {
                    ASN1EncodableVector unauthAttributes = buildUnauthenticatedAttributes(tsToken);
                    if (unauthAttributes != null) {
                        signerinfo.add(new DERTaggedObject(false, 1, new DERSet(unauthAttributes)));
                    }
                }
            }
            ASN1EncodableVector body = new ASN1EncodableVector();
            body.add(new ASN1Integer(this.version));
            body.add(new DERSet(digestAlgorithms));
            body.add(contentinfo);
            body.add(new DERTaggedObject(false, 0, dercertificates));
            body.add(new DERSet(new DERSequence(signerinfo)));
            ASN1EncodableVector whole = new ASN1EncodableVector();
            whole.add(new ASN1ObjectIdentifier(SecurityIDs.ID_PKCS7_SIGNED_DATA));
            whole.add(new DERTaggedObject(0, new DERSequence(body)));
            ByteArrayOutputStream bOut = new ByteArrayOutputStream();
            ASN1OutputStream dout = new ASN1OutputStream(bOut);
            dout.writeObject(new DERSequence(whole));
            dout.close();
            return bOut.toByteArray();
        } catch (Exception e) {
            throw new ExceptionConverter(e);
        }
    }

    private ASN1EncodableVector buildUnauthenticatedAttributes(byte[] timeStampToken) throws IOException {
        if (timeStampToken == null) {
            return null;
        }
        ASN1InputStream tempstream = new ASN1InputStream(new ByteArrayInputStream(timeStampToken));
        ASN1EncodableVector unauthAttributes = new ASN1EncodableVector();
        ASN1EncodableVector v = new ASN1EncodableVector();
        v.add(new ASN1ObjectIdentifier("1.2.840.113549.1.9.16.2.14"));
        v.add(new DERSet((ASN1Sequence) tempstream.readObject()));
        unauthAttributes.add(new DERSequence(v));
        return unauthAttributes;
    }

    public byte[] getAuthenticatedAttributeBytes(byte[] secondDigest, Calendar signingTime, byte[] ocsp, Collection<byte[]> crlBytes, CryptoStandard sigtype) {
        try {
            return getAuthenticatedAttributeSet(secondDigest, signingTime, ocsp, crlBytes, sigtype).getEncoded("DER");
        } catch (Exception e) {
            throw new ExceptionConverter(e);
        }
    }

    private DERSet getAuthenticatedAttributeSet(byte[] secondDigest, Calendar signingTime, byte[] ocsp, Collection<byte[]> crlBytes, CryptoStandard sigtype) {
        try {
            ASN1EncodableVector attribute = new ASN1EncodableVector();
            ASN1EncodableVector v = new ASN1EncodableVector();
            v.add(new ASN1ObjectIdentifier(SecurityIDs.ID_CONTENT_TYPE));
            v.add(new DERSet(new ASN1ObjectIdentifier(SecurityIDs.ID_PKCS7_DATA)));
            attribute.add(new DERSequence(v));
            v = new ASN1EncodableVector();
            v.add(new ASN1ObjectIdentifier(SecurityIDs.ID_SIGNING_TIME));
            v.add(new DERSet(new DERUTCTime(signingTime.getTime())));
            attribute.add(new DERSequence(v));
            v = new ASN1EncodableVector();
            v.add(new ASN1ObjectIdentifier(SecurityIDs.ID_MESSAGE_DIGEST));
            v.add(new DERSet(new DEROctetString(secondDigest)));
            attribute.add(new DERSequence(v));
            boolean haveCrl = false;
            if (crlBytes != null) {
                for (byte[] bCrl : crlBytes) {
                    if (bCrl != null) {
                        haveCrl = true;
                        break;
                    }
                }
            }
            if (ocsp != null || haveCrl) {
                ASN1EncodableVector v2;
                v = new ASN1EncodableVector();
                v.add(new ASN1ObjectIdentifier(SecurityIDs.ID_ADBE_REVOCATION));
                ASN1EncodableVector revocationV = new ASN1EncodableVector();
                if (haveCrl) {
                    v2 = new ASN1EncodableVector();
                    for (byte[] bCrl2 : crlBytes) {
                        if (bCrl2 != null) {
                            v2.add(new ASN1InputStream(new ByteArrayInputStream(bCrl2)).readObject());
                        }
                    }
                    revocationV.add(new DERTaggedObject(true, 0, new DERSequence(v2)));
                }
                if (ocsp != null) {
                    DEROctetString doctet = new DEROctetString(ocsp);
                    ASN1EncodableVector vo1 = new ASN1EncodableVector();
                    v2 = new ASN1EncodableVector();
                    v2.add(OCSPObjectIdentifiers.id_pkix_ocsp_basic);
                    v2.add(doctet);
                    ASN1Enumerated den = new ASN1Enumerated(0);
                    ASN1EncodableVector v3 = new ASN1EncodableVector();
                    v3.add(den);
                    v3.add(new DERTaggedObject(true, 0, new DERSequence(v2)));
                    vo1.add(new DERSequence(v3));
                    revocationV.add(new DERTaggedObject(true, 1, new DERSequence(vo1)));
                }
                v.add(new DERSet(new DERSequence(revocationV)));
                attribute.add(new DERSequence(v));
            }
            if (sigtype == CryptoStandard.CADES) {
                v = new ASN1EncodableVector();
                v.add(new ASN1ObjectIdentifier(SecurityIDs.ID_AA_SIGNING_CERTIFICATE_V2));
                ASN1EncodableVector aaV2 = new ASN1EncodableVector();
                aaV2.add(new AlgorithmIdentifier(new ASN1ObjectIdentifier(this.digestAlgorithmOid), null));
                aaV2.add(new DEROctetString(this.interfaceDigest.getMessageDigest(getHashAlgorithm()).digest(this.signCert.getEncoded())));
                v.add(new DERSet(new DERSequence(new DERSequence(new DERSequence(aaV2)))));
                attribute.add(new DERSequence(v));
            }
            return new DERSet(attribute);
        } catch (Exception e) {
            throw new ExceptionConverter(e);
        }
    }

    public boolean verify() throws GeneralSecurityException {
        boolean z = false;
        if (this.verified) {
            return this.verifyResult;
        }
        if (this.isTsp) {
            this.verifyResult = Arrays.equals(this.messageDigest.digest(), this.timeStampToken.getTimeStampInfo().toASN1Structure().getMessageImprint().getHashedMessage());
        } else if (this.sigAttr == null && this.sigAttrDer == null) {
            if (this.RSAdata != null) {
                this.sig.update(this.messageDigest.digest());
            }
            this.verifyResult = this.sig.verify(this.digest);
        } else {
            boolean concludingDigestCompare;
            byte[] msgDigestBytes = this.messageDigest.digest();
            boolean verifyRSAdata = true;
            boolean encContDigestCompare = false;
            if (this.RSAdata != null) {
                verifyRSAdata = Arrays.equals(msgDigestBytes, this.RSAdata);
                this.encContDigest.update(this.RSAdata);
                encContDigestCompare = Arrays.equals(this.encContDigest.digest(), this.digestAttr);
            }
            if (Arrays.equals(msgDigestBytes, this.digestAttr) || encContDigestCompare) {
                concludingDigestCompare = true;
            } else {
                concludingDigestCompare = false;
            }
            boolean sigVerify;
            if (verifySigAttributes(this.sigAttr) || verifySigAttributes(this.sigAttrDer)) {
                sigVerify = true;
            } else {
                sigVerify = false;
            }
            if (concludingDigestCompare && sigVerify && verifyRSAdata) {
                z = true;
            }
            this.verifyResult = z;
        }
        this.verified = true;
        return this.verifyResult;
    }

    private boolean verifySigAttributes(byte[] attr) throws GeneralSecurityException {
        Signature signature = initSignature(this.signCert.getPublicKey());
        signature.update(attr);
        return signature.verify(this.digest);
    }

    public boolean verifyTimestampImprint() throws GeneralSecurityException {
        if (this.timeStampToken == null) {
            return false;
        }
        TimeStampTokenInfo info = this.timeStampToken.getTimeStampInfo();
        MessageImprint imprint = info.toASN1Structure().getMessageImprint();
        return Arrays.equals(new BouncyCastleDigest().getMessageDigest(DigestAlgorithms.getDigest(info.getMessageImprintAlgOID().getId())).digest(this.digest), imprint.getHashedMessage());
    }

    public Certificate[] getCertificates() {
        return (Certificate[]) this.certs.toArray(new X509Certificate[this.certs.size()]);
    }

    public Certificate[] getSignCertificateChain() {
        return (Certificate[]) this.signCerts.toArray(new X509Certificate[this.signCerts.size()]);
    }

    public X509Certificate getSigningCertificate() {
        return this.signCert;
    }

    private void signCertificateChain() {
        ArrayList<Certificate> cc = new ArrayList();
        cc.add(this.signCert);
        ArrayList<Certificate> oc = new ArrayList(this.certs);
        int k = 0;
        while (k < oc.size()) {
            if (this.signCert.equals(oc.get(k))) {
                oc.remove(k);
                k--;
            }
            k++;
        }
        boolean found = true;
        while (found) {
            X509Certificate v = (X509Certificate) cc.get(cc.size() - 1);
            found = false;
            k = 0;
            while (k < oc.size()) {
                X509Certificate issuer = (X509Certificate) oc.get(k);
                try {
                    if (this.provider == null) {
                        v.verify(issuer.getPublicKey());
                    } else {
                        v.verify(issuer.getPublicKey(), this.provider);
                    }
                    found = true;
                    cc.add(oc.get(k));
                    oc.remove(k);
                } catch (Exception e) {
                    k++;
                }
            }
        }
        this.signCerts = cc;
    }

    public Collection<CRL> getCRLs() {
        return this.crls;
    }

    private void findCRL(ASN1Sequence seq) {
        try {
            this.crls = new ArrayList();
            for (int k = 0; k < seq.size(); k++) {
                this.crls.add((X509CRL) CertificateFactory.getInstance("X.509").generateCRL(new ByteArrayInputStream(seq.getObjectAt(k).toASN1Primitive().getEncoded("DER"))));
            }
        } catch (Exception e) {
        }
    }

    public BasicOCSPResp getOcsp() {
        return this.basicResp;
    }

    public boolean isRevocationValid() {
        if (this.basicResp == null) {
            return false;
        }
        if (this.signCerts.size() < 2) {
            return false;
        }
        try {
            X509Certificate[] cs = (X509Certificate[]) getSignCertificateChain();
            CertificateID cid = this.basicResp.getResponses()[0].getCertID();
            return new CertificateID(new JcaDigestCalculatorProviderBuilder().build().get(new AlgorithmIdentifier(cid.getHashAlgOID(), DERNull.INSTANCE)), new JcaX509CertificateHolder(cs[1]), getSigningCertificate().getSerialNumber()).equals(cid);
        } catch (Exception e) {
            return false;
        }
    }

    private void findOcsp(ASN1Sequence seq) throws IOException {
        this.basicResp = null;
        boolean ret;
        do {
            if ((seq.getObjectAt(0) instanceof ASN1ObjectIdentifier) && ((ASN1ObjectIdentifier) seq.getObjectAt(0)).getId().equals(OCSPObjectIdentifiers.id_pkix_ocsp_basic.getId())) {
                this.basicResp = new BasicOCSPResp(BasicOCSPResponse.getInstance(new ASN1InputStream(((ASN1OctetString) seq.getObjectAt(1)).getOctets()).readObject()));
                return;
            }
            ret = true;
            int k = 0;
            while (k < seq.size()) {
                if (seq.getObjectAt(k) instanceof ASN1Sequence) {
                    seq = (ASN1Sequence) seq.getObjectAt(0);
                    ret = false;
                    continue;
                    break;
                } else if (seq.getObjectAt(k) instanceof ASN1TaggedObject) {
                    ASN1TaggedObject tag = (ASN1TaggedObject) seq.getObjectAt(k);
                    if (tag.getObject() instanceof ASN1Sequence) {
                        seq = (ASN1Sequence) tag.getObject();
                        ret = false;
                        continue;
                    } else {
                        return;
                    }
                } else {
                    k++;
                }
            }
        } while (!ret);
    }

    public boolean isTsp() {
        return this.isTsp;
    }

    public TimeStampToken getTimeStampToken() {
        return this.timeStampToken;
    }

    public Calendar getTimeStampDate() {
        if (this.timeStampToken == null) {
            return null;
        }
        Calendar cal = new GregorianCalendar();
        cal.setTime(this.timeStampToken.getTimeStampInfo().getGenTime());
        return cal;
    }

    public PdfName getFilterSubtype() {
        return this.filterSubtype;
    }

    public String getEncryptionAlgorithm() {
        String encryptAlgo = EncryptionAlgorithms.getAlgorithm(this.digestEncryptionAlgorithmOid);
        if (encryptAlgo == null) {
            return this.digestEncryptionAlgorithmOid;
        }
        return encryptAlgo;
    }
}
