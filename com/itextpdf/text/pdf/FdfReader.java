package com.itextpdf.text.pdf;

import com.google.zxing.common.StringUtils;
import com.itextpdf.text.log.Counter;
import com.itextpdf.text.log.CounterFactory;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;

public class FdfReader extends PdfReader {
    protected static Counter COUNTER = CounterFactory.getCounter(FdfReader.class);
    PdfName encoding;
    HashMap<String, PdfDictionary> fields;
    String fileSpec;

    public FdfReader(String filename) throws IOException {
        super(filename);
    }

    public FdfReader(byte[] pdfIn) throws IOException {
        super(pdfIn);
    }

    public FdfReader(URL url) throws IOException {
        super(url);
    }

    public FdfReader(InputStream is) throws IOException {
        super(is);
    }

    protected Counter getCounter() {
        return COUNTER;
    }

    protected void readPdf() throws IOException {
        this.fields = new HashMap();
        this.tokens.checkFdfHeader();
        rebuildXref();
        readDocObj();
        readFields();
    }

    protected void kidNode(PdfDictionary merged, String name) {
        PdfArray kids = merged.getAsArray(PdfName.KIDS);
        if (kids == null || kids.isEmpty()) {
            if (name.length() > 0) {
                name = name.substring(1);
            }
            this.fields.put(name, merged);
            return;
        }
        merged.remove(PdfName.KIDS);
        for (int k = 0; k < kids.size(); k++) {
            PdfDictionary dic = new PdfDictionary();
            dic.merge(merged);
            PdfDictionary newDic = kids.getAsDict(k);
            PdfString t = newDic.getAsString(PdfName.f134T);
            String newName = name;
            if (t != null) {
                newName = newName + "." + t.toUnicodeString();
            }
            dic.merge(newDic);
            dic.remove(PdfName.f134T);
            kidNode(dic, newName);
        }
    }

    protected void readFields() {
        this.catalog = this.trailer.getAsDict(PdfName.ROOT);
        PdfDictionary fdf = this.catalog.getAsDict(PdfName.FDF);
        if (fdf != null) {
            PdfString fs = fdf.getAsString(PdfName.f122F);
            if (fs != null) {
                this.fileSpec = fs.toUnicodeString();
            }
            PdfArray fld = fdf.getAsArray(PdfName.FIELDS);
            if (fld != null) {
                this.encoding = fdf.getAsName(PdfName.ENCODING);
                PdfDictionary merged = new PdfDictionary();
                merged.put(PdfName.KIDS, fld);
                kidNode(merged, "");
            }
        }
    }

    public HashMap<String, PdfDictionary> getFields() {
        return this.fields;
    }

    public PdfDictionary getField(String name) {
        return (PdfDictionary) this.fields.get(name);
    }

    public byte[] getAttachedFile(String name) throws IOException {
        PdfDictionary field = (PdfDictionary) this.fields.get(name);
        if (field != null) {
            return PdfReader.getStreamBytes((PRStream) getPdfObject(((PRIndirectReference) ((PdfDictionary) getPdfObject(((PRIndirectReference) field.get(PdfName.f136V)).getNumber())).getAsDict(PdfName.EF).get(PdfName.f122F)).getNumber()));
        }
        return new byte[0];
    }

    public String getFieldValue(String name) {
        PdfDictionary field = (PdfDictionary) this.fields.get(name);
        if (field == null) {
            return null;
        }
        PdfObject v = PdfReader.getPdfObject(field.get(PdfName.f136V));
        if (v == null) {
            return null;
        }
        if (v.isName()) {
            return PdfName.decodeName(((PdfName) v).toString());
        }
        if (!v.isString()) {
            return null;
        }
        PdfString vs = (PdfString) v;
        if (this.encoding == null || vs.getEncoding() != null) {
            return vs.toUnicodeString();
        }
        byte[] b = vs.getBytes();
        if (b.length >= 2 && b[0] == (byte) -2 && b[1] == (byte) -1) {
            return vs.toUnicodeString();
        }
        try {
            if (this.encoding.equals(PdfName.SHIFT_JIS)) {
                return new String(b, StringUtils.SHIFT_JIS);
            }
            if (this.encoding.equals(PdfName.UHC)) {
                return new String(b, "MS949");
            }
            if (this.encoding.equals(PdfName.GBK)) {
                return new String(b, "GBK");
            }
            if (this.encoding.equals(PdfName.BIGFIVE)) {
                return new String(b, "Big5");
            }
            if (this.encoding.equals(PdfName.UTF_8)) {
                return new String(b, "UTF8");
            }
            return vs.toUnicodeString();
        } catch (Exception e) {
        }
    }

    public String getFileSpec() {
        return this.fileSpec;
    }
}
