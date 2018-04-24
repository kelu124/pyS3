package com.itextpdf.text.pdf.parser;

import com.itextpdf.text.pdf.PRIndirectReference;
import com.itextpdf.text.pdf.PRStream;
import com.itextpdf.text.pdf.PdfArray;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfObject;
import com.itextpdf.text.pdf.PdfReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ListIterator;

public class ContentByteUtils {
    private ContentByteUtils() {
    }

    public static byte[] getContentBytesFromContentObject(PdfObject contentObject) throws IOException {
        switch (contentObject.type()) {
            case 5:
                ByteArrayOutputStream allBytes = new ByteArrayOutputStream();
                ListIterator<PdfObject> iter = ((PdfArray) contentObject).listIterator();
                while (iter.hasNext()) {
                    allBytes.write(getContentBytesFromContentObject((PdfObject) iter.next()));
                    allBytes.write(32);
                }
                return allBytes.toByteArray();
            case 7:
                return PdfReader.getStreamBytes((PRStream) PdfReader.getPdfObjectRelease(contentObject));
            case 10:
                return getContentBytesFromContentObject(PdfReader.getPdfObjectRelease((PRIndirectReference) contentObject));
            default:
                throw new IllegalStateException("Unable to handle Content of type " + contentObject.getClass());
        }
    }

    public static byte[] getContentBytesForPage(PdfReader reader, int pageNum) throws IOException {
        PdfObject contentObject = reader.getPageN(pageNum).get(PdfName.CONTENTS);
        if (contentObject == null) {
            return new byte[0];
        }
        return getContentBytesFromContentObject(contentObject);
    }
}
