package com.itextpdf.text.pdf.security;

import com.google.common.net.HttpHeaders;
import com.itextpdf.text.error_messages.MessageLocalization;
import com.itextpdf.text.io.StreamUtil;
import com.itextpdf.text.log.Level;
import com.itextpdf.text.log.Logger;
import com.itextpdf.text.log.LoggerFactory;
import com.itextpdf.text.pdf.PdfEncryption;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.security.Security;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import org.spongycastle.asn1.DEROctetString;
import org.spongycastle.asn1.ocsp.OCSPObjectIdentifiers;
import org.spongycastle.asn1.x509.Extension;
import org.spongycastle.asn1.x509.Extensions;
import org.spongycastle.cert.jcajce.JcaX509CertificateHolder;
import org.spongycastle.cert.ocsp.BasicOCSPResp;
import org.spongycastle.cert.ocsp.CertificateID;
import org.spongycastle.cert.ocsp.CertificateStatus;
import org.spongycastle.cert.ocsp.OCSPException;
import org.spongycastle.cert.ocsp.OCSPReq;
import org.spongycastle.cert.ocsp.OCSPReqBuilder;
import org.spongycastle.cert.ocsp.OCSPResp;
import org.spongycastle.cert.ocsp.SingleResp;
import org.spongycastle.jce.provider.BouncyCastleProvider;
import org.spongycastle.ocsp.RevokedStatus;
import org.spongycastle.operator.OperatorException;
import org.spongycastle.operator.jcajce.JcaDigestCalculatorProviderBuilder;

public class OcspClientBouncyCastle implements OcspClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(OcspClientBouncyCastle.class);

    private static OCSPReq generateOCSPRequest(X509Certificate issuerCert, BigInteger serialNumber) throws OCSPException, IOException, OperatorException, CertificateEncodingException {
        Security.addProvider(new BouncyCastleProvider());
        CertificateID id = new CertificateID(new JcaDigestCalculatorProviderBuilder().build().get(CertificateID.HASH_SHA1), new JcaX509CertificateHolder(issuerCert), serialNumber);
        OCSPReqBuilder gen = new OCSPReqBuilder();
        gen.addRequest(id);
        Extension ext = new Extension(OCSPObjectIdentifiers.id_pkix_ocsp_nonce, false, new DEROctetString(new DEROctetString(PdfEncryption.createDocumentId()).getEncoded()));
        gen.setRequestExtensions(new Extensions(new Extension[]{ext}));
        return gen.build();
    }

    private OCSPResp getOcspResponse(X509Certificate checkCert, X509Certificate rootCert, String url) throws GeneralSecurityException, OCSPException, IOException, OperatorException {
        if (checkCert == null || rootCert == null) {
            return null;
        }
        if (url == null) {
            url = CertificateUtil.getOCSPURL(checkCert);
        }
        if (url == null) {
            return null;
        }
        LOGGER.info("Getting OCSP from " + url);
        byte[] array = generateOCSPRequest(rootCert, checkCert.getSerialNumber()).getEncoded();
        HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
        con.setRequestProperty(HttpHeaders.CONTENT_TYPE, "application/ocsp-request");
        con.setRequestProperty(HttpHeaders.ACCEPT, "application/ocsp-response");
        con.setDoOutput(true);
        DataOutputStream dataOut = new DataOutputStream(new BufferedOutputStream(con.getOutputStream()));
        dataOut.write(array);
        dataOut.flush();
        dataOut.close();
        if (con.getResponseCode() / 100 == 2) {
            return new OCSPResp(StreamUtil.inputStreamToArray((InputStream) con.getContent()));
        }
        throw new IOException(MessageLocalization.getComposedMessage("invalid.http.response.1", con.getResponseCode()));
    }

    public BasicOCSPResp getBasicOCSPResp(X509Certificate checkCert, X509Certificate rootCert, String url) {
        try {
            OCSPResp ocspResponse = getOcspResponse(checkCert, rootCert, url);
            if (ocspResponse == null) {
                return null;
            }
            if (ocspResponse.getStatus() != 0) {
                return null;
            }
            return (BasicOCSPResp) ocspResponse.getResponseObject();
        } catch (Exception ex) {
            if (LOGGER.isLogging(Level.ERROR)) {
                LOGGER.error(ex.getMessage());
            }
            return null;
        }
    }

    public byte[] getEncoded(X509Certificate checkCert, X509Certificate rootCert, String url) {
        try {
            BasicOCSPResp basicResponse = getBasicOCSPResp(checkCert, rootCert, url);
            if (basicResponse != null) {
                SingleResp[] responses = basicResponse.getResponses();
                if (responses.length == 1) {
                    CertificateStatus status = responses[0].getCertStatus();
                    if (status == CertificateStatus.GOOD) {
                        return basicResponse.getEncoded();
                    }
                    if (status instanceof RevokedStatus) {
                        throw new IOException(MessageLocalization.getComposedMessage("ocsp.status.is.revoked", new Object[0]));
                    }
                    throw new IOException(MessageLocalization.getComposedMessage("ocsp.status.is.unknown", new Object[0]));
                }
            }
        } catch (Exception ex) {
            if (LOGGER.isLogging(Level.ERROR)) {
                LOGGER.error(ex.getMessage());
            }
        }
        return null;
    }
}
