package com.itextpdf.text.pdf.security;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfDeveloperExtension;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfSignature;
import com.itextpdf.text.pdf.PdfSignatureAppearance;
import com.itextpdf.text.pdf.PdfString;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.util.HashMap;

public class LtvTimestamp {
    public static void timestamp(PdfSignatureAppearance sap, TSAClient tsa, String signatureName) throws IOException, DocumentException, GeneralSecurityException {
        int contentEstimated = tsa.getTokenSizeEstimate();
        sap.addDeveloperExtension(PdfDeveloperExtension.ESIC_1_7_EXTENSIONLEVEL5);
        sap.setVisibleSignature(new Rectangle(0.0f, 0.0f, 0.0f, 0.0f), 1, signatureName);
        PdfSignature dic = new PdfSignature(PdfName.ADOBE_PPKLITE, PdfName.ETSI_RFC3161);
        dic.put(PdfName.TYPE, PdfName.DOCTIMESTAMP);
        sap.setCryptoDictionary(dic);
        HashMap<PdfName, Integer> exc = new HashMap();
        exc.put(PdfName.CONTENTS, new Integer((contentEstimated * 2) + 2));
        sap.preClose(exc);
        InputStream data = sap.getRangeStream();
        MessageDigest messageDigest = tsa.getMessageDigest();
        byte[] buf = new byte[4096];
        while (true) {
            int n = data.read(buf);
            if (n <= 0) {
                break;
            }
            messageDigest.update(buf, 0, n);
        }
        try {
            byte[] tsToken = tsa.getTimeStampToken(messageDigest.digest());
            if (contentEstimated + 2 < tsToken.length) {
                throw new IOException("Not enough space");
            }
            byte[] paddedSig = new byte[contentEstimated];
            System.arraycopy(tsToken, 0, paddedSig, 0, tsToken.length);
            PdfDictionary dic2 = new PdfDictionary();
            dic2.put(PdfName.CONTENTS, new PdfString(paddedSig).setHexWriting(true));
            sap.close(dic2);
        } catch (Exception e) {
            throw new GeneralSecurityException(e);
        }
    }
}
