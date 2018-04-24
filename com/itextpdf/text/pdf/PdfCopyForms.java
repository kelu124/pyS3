package com.itextpdf.text.pdf;

import com.itextpdf.text.DocWriter;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.interfaces.PdfEncryptionSettings;
import com.itextpdf.text.pdf.interfaces.PdfViewerPreferences;
import java.io.IOException;
import java.io.OutputStream;
import java.security.cert.Certificate;
import java.util.HashMap;
import java.util.List;

public class PdfCopyForms implements PdfViewerPreferences, PdfEncryptionSettings {
    private PdfCopyFormsImp fc;

    public PdfCopyForms(OutputStream os) throws DocumentException {
        this.fc = new PdfCopyFormsImp(os);
    }

    public void addDocument(PdfReader reader) throws DocumentException, IOException {
        this.fc.addDocument(reader);
    }

    public void addDocument(PdfReader reader, List<Integer> pagesToKeep) throws DocumentException, IOException {
        this.fc.addDocument(reader, pagesToKeep);
    }

    public void addDocument(PdfReader reader, String ranges) throws DocumentException, IOException {
        this.fc.addDocument(reader, SequenceList.expand(ranges, reader.getNumberOfPages()));
    }

    public void copyDocumentFields(PdfReader reader) throws DocumentException {
        this.fc.copyDocumentFields(reader);
    }

    public void setEncryption(byte[] userPassword, byte[] ownerPassword, int permissions, boolean strength128Bits) throws DocumentException {
        this.fc.setEncryption(userPassword, ownerPassword, permissions, strength128Bits ? 1 : 0);
    }

    public void setEncryption(boolean strength, String userPassword, String ownerPassword, int permissions) throws DocumentException {
        setEncryption(DocWriter.getISOBytes(userPassword), DocWriter.getISOBytes(ownerPassword), permissions, strength);
    }

    public void close() {
        this.fc.close();
    }

    public void open() {
        this.fc.openDoc();
    }

    public void addJavaScript(String js) {
        this.fc.addJavaScript(js, !PdfEncodings.isPdfDocEncoding(js));
    }

    public void setOutlines(List<HashMap<String, Object>> outlines) {
        this.fc.setOutlines(outlines);
    }

    public PdfWriter getWriter() {
        return this.fc;
    }

    public boolean isFullCompression() {
        return this.fc.isFullCompression();
    }

    public void setFullCompression() throws DocumentException {
        this.fc.setFullCompression();
    }

    public void setEncryption(byte[] userPassword, byte[] ownerPassword, int permissions, int encryptionType) throws DocumentException {
        this.fc.setEncryption(userPassword, ownerPassword, permissions, encryptionType);
    }

    public void addViewerPreference(PdfName key, PdfObject value) {
        this.fc.addViewerPreference(key, value);
    }

    public void setViewerPreferences(int preferences) {
        this.fc.setViewerPreferences(preferences);
    }

    public void setEncryption(Certificate[] certs, int[] permissions, int encryptionType) throws DocumentException {
        this.fc.setEncryption(certs, permissions, encryptionType);
    }
}
