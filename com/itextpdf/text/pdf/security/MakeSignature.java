package com.itextpdf.text.pdf.security;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.io.RASInputStream;
import com.itextpdf.text.io.RandomAccessSource;
import com.itextpdf.text.io.RandomAccessSourceFactory;
import com.itextpdf.text.io.StreamUtil;
import com.itextpdf.text.log.Logger;
import com.itextpdf.text.log.LoggerFactory;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.ByteBuffer;
import com.itextpdf.text.pdf.PdfArray;
import com.itextpdf.text.pdf.PdfDate;
import com.itextpdf.text.pdf.PdfDeveloperExtension;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfSignature;
import com.itextpdf.text.pdf.PdfSignatureAppearance;
import com.itextpdf.text.pdf.PdfString;
import java.io.IOException;
import java.io.OutputStream;
import java.security.GeneralSecurityException;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;

public class MakeSignature {
    private static final Logger LOGGER = LoggerFactory.getLogger(MakeSignature.class);

    public enum CryptoStandard {
        CMS,
        CADES
    }

    public static void signDetached(PdfSignatureAppearance sap, ExternalDigest externalDigest, ExternalSignature externalSignature, Certificate[] chain, Collection<CrlClient> crlList, OcspClient ocspClient, TSAClient tsaClient, int estimatedSize, CryptoStandard sigtype) throws IOException, DocumentException, GeneralSecurityException {
        Collection<byte[]> crlBytes = null;
        int i = 0;
        while (crlBytes == null && i < chain.length) {
            int i2 = i + 1;
            crlBytes = processCrl(chain[i], crlList);
            i = i2;
        }
        if (estimatedSize == 0) {
            estimatedSize = 8192;
            if (crlBytes != null) {
                for (byte[] element : crlBytes) {
                    estimatedSize += element.length + 10;
                }
            }
            if (ocspClient != null) {
                estimatedSize += 4192;
            }
            if (tsaClient != null) {
                estimatedSize += 4192;
            }
        }
        sap.setCertificate(chain[0]);
        if (sigtype == CryptoStandard.CADES) {
            sap.addDeveloperExtension(PdfDeveloperExtension.ESIC_1_7_EXTENSIONLEVEL2);
        }
        PdfDictionary pdfSignature = new PdfSignature(PdfName.ADOBE_PPKLITE, sigtype == CryptoStandard.CADES ? PdfName.ETSI_CADES_DETACHED : PdfName.ADBE_PKCS7_DETACHED);
        pdfSignature.setReason(sap.getReason());
        pdfSignature.setLocation(sap.getLocation());
        pdfSignature.setSignatureCreator(sap.getSignatureCreator());
        pdfSignature.setContact(sap.getContact());
        pdfSignature.setDate(new PdfDate(sap.getSignDate()));
        sap.setCryptoDictionary(pdfSignature);
        HashMap<PdfName, Integer> exc = new HashMap();
        exc.put(PdfName.CONTENTS, new Integer((estimatedSize * 2) + 2));
        sap.preClose(exc);
        String hashAlgorithm = externalSignature.getHashAlgorithm();
        PdfPKCS7 sgn = new PdfPKCS7(null, chain, hashAlgorithm, null, externalDigest, false);
        byte[] hash = DigestAlgorithms.digest(sap.getRangeStream(), externalDigest.getMessageDigest(hashAlgorithm));
        Calendar cal = Calendar.getInstance();
        byte[] ocsp = null;
        if (chain.length >= 2 && ocspClient != null) {
            ocsp = ocspClient.getEncoded((X509Certificate) chain[0], (X509Certificate) chain[1], null);
        }
        sgn.setExternalDigest(externalSignature.sign(sgn.getAuthenticatedAttributeBytes(hash, cal, ocsp, crlBytes, sigtype)), null, externalSignature.getEncryptionAlgorithm());
        Object encodedSig = sgn.getEncodedPKCS7(hash, cal, tsaClient, ocsp, crlBytes, sigtype);
        if (estimatedSize < encodedSig.length) {
            throw new IOException("Not enough space");
        }
        Object paddedSig = new byte[estimatedSize];
        System.arraycopy(encodedSig, 0, paddedSig, 0, encodedSig.length);
        PdfDictionary dic2 = new PdfDictionary();
        dic2.put(PdfName.CONTENTS, new PdfString((byte[]) paddedSig).setHexWriting(true));
        sap.close(dic2);
    }

    public static Collection<byte[]> processCrl(Certificate cert, Collection<CrlClient> crlList) {
        if (crlList == null) {
            return null;
        }
        Collection<byte[]> crlBytes = new ArrayList();
        for (CrlClient cc : crlList) {
            if (cc != null) {
                LOGGER.info("Processing " + cc.getClass().getName());
                Collection<byte[]> b = cc.getEncoded((X509Certificate) cert, null);
                if (b != null) {
                    crlBytes.addAll(b);
                }
            }
        }
        if (crlBytes.isEmpty()) {
            return null;
        }
        return crlBytes;
    }

    public static void signExternalContainer(PdfSignatureAppearance sap, ExternalSignatureContainer externalSignatureContainer, int estimatedSize) throws GeneralSecurityException, IOException, DocumentException {
        PdfSignature dic = new PdfSignature(null, null);
        dic.setReason(sap.getReason());
        dic.setLocation(sap.getLocation());
        dic.setSignatureCreator(sap.getSignatureCreator());
        dic.setContact(sap.getContact());
        dic.setDate(new PdfDate(sap.getSignDate()));
        externalSignatureContainer.modifySigningDictionary(dic);
        sap.setCryptoDictionary(dic);
        HashMap<PdfName, Integer> exc = new HashMap();
        exc.put(PdfName.CONTENTS, new Integer((estimatedSize * 2) + 2));
        sap.preClose(exc);
        byte[] encodedSig = externalSignatureContainer.sign(sap.getRangeStream());
        if (estimatedSize < encodedSig.length) {
            throw new IOException("Not enough space");
        }
        byte[] paddedSig = new byte[estimatedSize];
        System.arraycopy(encodedSig, 0, paddedSig, 0, encodedSig.length);
        PdfDictionary dic2 = new PdfDictionary();
        dic2.put(PdfName.CONTENTS, new PdfString(paddedSig).setHexWriting(true));
        sap.close(dic2);
    }

    public static void signDeferred(PdfReader reader, String fieldName, OutputStream outs, ExternalSignatureContainer externalSignatureContainer) throws DocumentException, IOException, GeneralSecurityException {
        AcroFields af = reader.getAcroFields();
        PdfDictionary v = af.getSignatureDictionary(fieldName);
        if (v == null) {
            throw new DocumentException("No field");
        } else if (af.signatureCoversWholeDocument(fieldName)) {
            PdfArray b = v.getAsArray(PdfName.BYTERANGE);
            long[] gaps = b.asLongArray();
            if (b.size() == 4 && gaps[0] == 0) {
                RandomAccessSource readerSource = reader.getSafeFile().createSourceView();
                byte[] signedContent = externalSignatureContainer.sign(new RASInputStream(new RandomAccessSourceFactory().createRanged(readerSource, gaps)));
                int spaceAvailable = ((int) (gaps[2] - gaps[1])) - 2;
                if ((spaceAvailable & 1) != 0) {
                    throw new DocumentException("Gap is not a multiple of 2");
                }
                spaceAvailable /= 2;
                if (spaceAvailable < signedContent.length) {
                    throw new DocumentException("Not enough space");
                }
                StreamUtil.CopyBytes(readerSource, 0, gaps[1] + 1, outs);
                ByteBuffer bb = new ByteBuffer(spaceAvailable * 2);
                for (byte bi : signedContent) {
                    bb.appendHex(bi);
                }
                int remain = (spaceAvailable - signedContent.length) * 2;
                for (int k = 0; k < remain; k++) {
                    bb.append((byte) ByteBuffer.ZERO);
                }
                bb.writeTo(outs);
                StreamUtil.CopyBytes(readerSource, gaps[2] - 1, gaps[3] + 1, outs);
                return;
            }
            throw new DocumentException("Single exclusion space supported");
        } else {
            throw new DocumentException("Not the last signature");
        }
    }
}
