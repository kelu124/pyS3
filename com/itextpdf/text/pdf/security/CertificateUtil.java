package com.itextpdf.text.pdf.security;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.security.cert.CRL;
import java.security.cert.CRLException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;
import org.spongycastle.asn1.ASN1InputStream;
import org.spongycastle.asn1.ASN1ObjectIdentifier;
import org.spongycastle.asn1.ASN1OctetString;
import org.spongycastle.asn1.ASN1Primitive;
import org.spongycastle.asn1.ASN1Sequence;
import org.spongycastle.asn1.ASN1TaggedObject;
import org.spongycastle.asn1.DERIA5String;
import org.spongycastle.asn1.DEROctetString;
import org.spongycastle.asn1.x509.CRLDistPoint;
import org.spongycastle.asn1.x509.DistributionPoint;
import org.spongycastle.asn1.x509.DistributionPointName;
import org.spongycastle.asn1.x509.Extension;
import org.spongycastle.asn1.x509.GeneralName;
import org.spongycastle.asn1.x509.GeneralNames;

public class CertificateUtil {
    public static CRL getCRL(X509Certificate certificate) throws CertificateException, CRLException, IOException {
        return getCRL(getCRLURL(certificate));
    }

    public static String getCRLURL(X509Certificate certificate) throws CertificateParsingException {
        ASN1Primitive obj;
        try {
            obj = getExtensionValue(certificate, Extension.cRLDistributionPoints.getId());
        } catch (IOException e) {
            obj = null;
        }
        if (obj == null) {
            return null;
        }
        for (DistributionPoint p : CRLDistPoint.getInstance(obj).getDistributionPoints()) {
            DistributionPointName distributionPointName = p.getDistributionPoint();
            if (distributionPointName.getType() == 0) {
                for (GeneralName name : ((GeneralNames) distributionPointName.getName()).getNames()) {
                    if (name.getTagNo() == 6) {
                        return DERIA5String.getInstance((ASN1TaggedObject) name.toASN1Primitive(), false).getString();
                    }
                }
                continue;
            }
        }
        return null;
    }

    public static CRL getCRL(String url) throws IOException, CertificateException, CRLException {
        if (url == null) {
            return null;
        }
        return CertificateFactory.getInstance("X.509").generateCRL(new URL(url).openStream());
    }

    public static String getOCSPURL(X509Certificate certificate) {
        try {
            ASN1Primitive obj = getExtensionValue(certificate, Extension.authorityInfoAccess.getId());
            if (obj == null) {
                return null;
            }
            ASN1Sequence AccessDescriptions = (ASN1Sequence) obj;
            for (int i = 0; i < AccessDescriptions.size(); i++) {
                ASN1Sequence AccessDescription = (ASN1Sequence) AccessDescriptions.getObjectAt(i);
                if (AccessDescription.size() == 2 && (AccessDescription.getObjectAt(0) instanceof ASN1ObjectIdentifier)) {
                    if (SecurityIDs.ID_OCSP.equals(((ASN1ObjectIdentifier) AccessDescription.getObjectAt(0)).getId())) {
                        String AccessLocation = getStringFromGeneralName((ASN1Primitive) AccessDescription.getObjectAt(1));
                        if (AccessLocation == null) {
                            return "";
                        }
                        return AccessLocation;
                    }
                }
            }
            return null;
        } catch (IOException e) {
            return null;
        }
    }

    public static String getTSAURL(X509Certificate certificate) {
        String str = null;
        byte[] der = certificate.getExtensionValue(SecurityIDs.ID_TSA);
        if (der != null) {
            try {
                str = getStringFromGeneralName(ASN1Sequence.getInstance(ASN1Primitive.fromByteArray(((DEROctetString) ASN1Primitive.fromByteArray(der)).getOctets())).getObjectAt(1).toASN1Primitive());
            } catch (IOException e) {
            }
        }
        return str;
    }

    private static ASN1Primitive getExtensionValue(X509Certificate certificate, String oid) throws IOException {
        byte[] bytes = certificate.getExtensionValue(oid);
        if (bytes == null) {
            return null;
        }
        return new ASN1InputStream(new ByteArrayInputStream(((ASN1OctetString) new ASN1InputStream(new ByteArrayInputStream(bytes)).readObject()).getOctets())).readObject();
    }

    private static String getStringFromGeneralName(ASN1Primitive names) throws IOException {
        return new String(ASN1OctetString.getInstance((ASN1TaggedObject) names, false).getOctets(), "ISO-8859-1");
    }
}
