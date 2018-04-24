package com.itextpdf.text.pdf;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.AlgorithmParameterGenerator;
import java.security.AlgorithmParameters;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import org.spongycastle.asn1.ASN1InputStream;
import org.spongycastle.asn1.ASN1ObjectIdentifier;
import org.spongycastle.asn1.ASN1Primitive;
import org.spongycastle.asn1.DEROctetString;
import org.spongycastle.asn1.DEROutputStream;
import org.spongycastle.asn1.DERSet;
import org.spongycastle.asn1.cms.ContentInfo;
import org.spongycastle.asn1.cms.EncryptedContentInfo;
import org.spongycastle.asn1.cms.EnvelopedData;
import org.spongycastle.asn1.cms.IssuerAndSerialNumber;
import org.spongycastle.asn1.cms.KeyTransRecipientInfo;
import org.spongycastle.asn1.cms.RecipientIdentifier;
import org.spongycastle.asn1.cms.RecipientInfo;
import org.spongycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.spongycastle.asn1.x509.AlgorithmIdentifier;
import org.spongycastle.asn1.x509.TBSCertificateStructure;

public class PdfPublicKeySecurityHandler {
    static final int SEED_LENGTH = 20;
    private ArrayList<PdfPublicKeyRecipient> recipients = null;
    private byte[] seed = new byte[20];

    public PdfPublicKeySecurityHandler() {
        try {
            KeyGenerator key = KeyGenerator.getInstance("AES");
            key.init(192, new SecureRandom());
            System.arraycopy(key.generateKey().getEncoded(), 0, this.seed, 0, 20);
        } catch (NoSuchAlgorithmException e) {
            this.seed = SecureRandom.getSeed(20);
        }
        this.recipients = new ArrayList();
    }

    public void addRecipient(PdfPublicKeyRecipient recipient) {
        this.recipients.add(recipient);
    }

    protected byte[] getSeed() {
        return (byte[]) this.seed.clone();
    }

    public int getRecipientsSize() {
        return this.recipients.size();
    }

    public byte[] getEncodedRecipient(int index) throws IOException, GeneralSecurityException {
        PdfPublicKeyRecipient recipient = (PdfPublicKeyRecipient) this.recipients.get(index);
        byte[] cms = recipient.getCms();
        if (cms != null) {
            return cms;
        }
        Certificate certificate = recipient.getCertificate();
        int permission = ((recipient.getPermission() | (3 == 3 ? -3904 : -64)) & -4) + 1;
        byte[] pkcs7input = new byte[24];
        byte one = (byte) permission;
        byte two = (byte) (permission >> 8);
        byte three = (byte) (permission >> 16);
        byte four = (byte) (permission >> 24);
        System.arraycopy(this.seed, 0, pkcs7input, 0, 20);
        pkcs7input[20] = four;
        pkcs7input[21] = three;
        pkcs7input[22] = two;
        pkcs7input[23] = one;
        ASN1Primitive obj = createDERForRecipient(pkcs7input, (X509Certificate) certificate);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        new DEROutputStream(baos).writeObject(obj);
        cms = baos.toByteArray();
        recipient.setCms(cms);
        return cms;
    }

    public PdfArray getEncodedRecipients() throws IOException, GeneralSecurityException {
        PdfArray EncodedRecipients = new PdfArray();
        for (int i = 0; i < this.recipients.size(); i++) {
            try {
                EncodedRecipients.add(new PdfLiteral(StringUtils.escapeString(getEncodedRecipient(i))));
            } catch (GeneralSecurityException e) {
                EncodedRecipients = null;
            } catch (IOException e2) {
                EncodedRecipients = null;
            }
        }
        return EncodedRecipients;
    }

    private ASN1Primitive createDERForRecipient(byte[] in, X509Certificate cert) throws IOException, GeneralSecurityException {
        String s = "1.2.840.113549.3.2";
        AlgorithmParameters algorithmparameters = AlgorithmParameterGenerator.getInstance(s).generateParameters();
        ASN1Primitive derobject = new ASN1InputStream(new ByteArrayInputStream(algorithmparameters.getEncoded("ASN.1"))).readObject();
        KeyGenerator keygenerator = KeyGenerator.getInstance(s);
        keygenerator.init(128);
        SecretKey secretkey = keygenerator.generateKey();
        Cipher cipher = Cipher.getInstance(s);
        cipher.init(1, secretkey, algorithmparameters);
        DEROctetString deroctetstring = new DEROctetString(cipher.doFinal(in));
        return new ContentInfo(PKCSObjectIdentifiers.envelopedData, new EnvelopedData(null, new DERSet(new RecipientInfo(computeRecipientInfo(cert, secretkey.getEncoded()))), new EncryptedContentInfo(PKCSObjectIdentifiers.data, new AlgorithmIdentifier(new ASN1ObjectIdentifier(s), derobject), deroctetstring), null)).toASN1Primitive();
    }

    private KeyTransRecipientInfo computeRecipientInfo(X509Certificate x509certificate, byte[] abyte0) throws GeneralSecurityException, IOException {
        TBSCertificateStructure tbscertificatestructure = TBSCertificateStructure.getInstance(new ASN1InputStream(new ByteArrayInputStream(x509certificate.getTBSCertificate())).readObject());
        AlgorithmIdentifier algorithmidentifier = tbscertificatestructure.getSubjectPublicKeyInfo().getAlgorithm();
        IssuerAndSerialNumber issuerandserialnumber = new IssuerAndSerialNumber(tbscertificatestructure.getIssuer(), tbscertificatestructure.getSerialNumber().getValue());
        Cipher cipher = Cipher.getInstance(algorithmidentifier.getAlgorithm().getId());
        try {
            cipher.init(1, x509certificate);
        } catch (InvalidKeyException e) {
            cipher.init(1, x509certificate.getPublicKey());
        }
        return new KeyTransRecipientInfo(new RecipientIdentifier(issuerandserialnumber), algorithmidentifier, new DEROctetString(cipher.doFinal(abyte0)));
    }
}
