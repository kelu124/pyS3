package com.itextpdf.text.pdf;

import com.itextpdf.text.DocumentException;
import java.io.IOException;
import java.io.OutputStream;
import java.security.PrivateKey;
import java.util.HashMap;
import org.spongycastle.cms.CMSException;
import org.spongycastle.cms.RecipientInformation;
import org.spongycastle.cms.jcajce.JceKeyTransEnvelopedRecipient;

public final class PdfEncryptor {
    private PdfEncryptor() {
    }

    public static void encrypt(PdfReader reader, OutputStream os, byte[] userPassword, byte[] ownerPassword, int permissions, boolean strength128Bits) throws DocumentException, IOException {
        PdfStamper stamper = new PdfStamper(reader, os);
        stamper.setEncryption(userPassword, ownerPassword, permissions, strength128Bits);
        stamper.close();
    }

    public static void encrypt(PdfReader reader, OutputStream os, byte[] userPassword, byte[] ownerPassword, int permissions, boolean strength128Bits, HashMap<String, String> newInfo) throws DocumentException, IOException {
        PdfStamper stamper = new PdfStamper(reader, os);
        stamper.setEncryption(userPassword, ownerPassword, permissions, strength128Bits);
        stamper.setMoreInfo(newInfo);
        stamper.close();
    }

    public static void encrypt(PdfReader reader, OutputStream os, boolean strength, String userPassword, String ownerPassword, int permissions) throws DocumentException, IOException {
        PdfStamper stamper = new PdfStamper(reader, os);
        stamper.setEncryption(strength, userPassword, ownerPassword, permissions);
        stamper.close();
    }

    public static void encrypt(PdfReader reader, OutputStream os, boolean strength, String userPassword, String ownerPassword, int permissions, HashMap<String, String> newInfo) throws DocumentException, IOException {
        PdfStamper stamper = new PdfStamper(reader, os);
        stamper.setEncryption(strength, userPassword, ownerPassword, permissions);
        stamper.setMoreInfo(newInfo);
        stamper.close();
    }

    public static void encrypt(PdfReader reader, OutputStream os, int type, String userPassword, String ownerPassword, int permissions, HashMap<String, String> newInfo) throws DocumentException, IOException {
        PdfStamper stamper = new PdfStamper(reader, os);
        stamper.setEncryption(type, userPassword, ownerPassword, permissions);
        stamper.setMoreInfo(newInfo);
        stamper.close();
    }

    public static void encrypt(PdfReader reader, OutputStream os, int type, String userPassword, String ownerPassword, int permissions) throws DocumentException, IOException {
        PdfStamper stamper = new PdfStamper(reader, os);
        stamper.setEncryption(type, userPassword, ownerPassword, permissions);
        stamper.close();
    }

    public static String getPermissionsVerbose(int permissions) {
        StringBuffer buf = new StringBuffer("Allowed:");
        if ((permissions & 2052) == 2052) {
            buf.append(" Printing");
        }
        if ((permissions & 8) == 8) {
            buf.append(" Modify contents");
        }
        if ((permissions & 16) == 16) {
            buf.append(" Copy");
        }
        if ((permissions & 32) == 32) {
            buf.append(" Modify annotations");
        }
        if ((permissions & 256) == 256) {
            buf.append(" Fill in");
        }
        if ((permissions & 512) == 512) {
            buf.append(" Screen readers");
        }
        if ((permissions & 1024) == 1024) {
            buf.append(" Assembly");
        }
        if ((permissions & 4) == 4) {
            buf.append(" Degraded printing");
        }
        return buf.toString();
    }

    public static boolean isPrintingAllowed(int permissions) {
        return (permissions & 2052) == 2052;
    }

    public static boolean isModifyContentsAllowed(int permissions) {
        return (permissions & 8) == 8;
    }

    public static boolean isCopyAllowed(int permissions) {
        return (permissions & 16) == 16;
    }

    public static boolean isModifyAnnotationsAllowed(int permissions) {
        return (permissions & 32) == 32;
    }

    public static boolean isFillInAllowed(int permissions) {
        return (permissions & 256) == 256;
    }

    public static boolean isScreenReadersAllowed(int permissions) {
        return (permissions & 512) == 512;
    }

    public static boolean isAssemblyAllowed(int permissions) {
        return (permissions & 1024) == 1024;
    }

    public static boolean isDegradedPrintingAllowed(int permissions) {
        return (permissions & 4) == 4;
    }

    public static byte[] getContent(RecipientInformation recipientInfo, PrivateKey certificateKey, String certificateKeyProvider) throws CMSException {
        return recipientInfo.getContent(new JceKeyTransEnvelopedRecipient(certificateKey).setProvider(certificateKeyProvider));
    }
}
