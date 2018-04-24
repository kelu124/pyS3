package com.itextpdf.text.pdf;

import com.itextpdf.text.DocWriter;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.Image;
import com.itextpdf.text.log.Counter;
import com.itextpdf.text.log.CounterFactory;
import com.itextpdf.text.pdf.AcroFields.Item;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.StringTokenizer;

public class FdfWriter {
    private static final byte[] HEADER_FDF = DocWriter.getISOBytes("%FDF-1.4\n%âãÏÓ\n");
    protected Counter COUNTER = CounterFactory.getCounter(FdfWriter.class);
    HashMap<String, Object> fields = new HashMap();
    private String file;
    private String statusMessage;
    Wrt wrt = null;

    static class Wrt extends PdfWriter {
        private FdfWriter fdf;

        Wrt(OutputStream os, FdfWriter fdf) throws IOException {
            super(new PdfDocument(), os);
            this.fdf = fdf;
            this.os.write(FdfWriter.HEADER_FDF);
            this.body = new PdfWriter$PdfBody(this);
        }

        void write() throws IOException {
            for (PdfReaderInstance element : this.readerInstances.values()) {
                this.currentPdfReaderInstance = element;
                this.currentPdfReaderInstance.writeAllPages();
            }
            PdfDictionary dic = new PdfDictionary();
            dic.put(PdfName.FIELDS, calculate(this.fdf.fields));
            if (this.fdf.file != null) {
                dic.put(PdfName.f122F, new PdfString(this.fdf.file, PdfObject.TEXT_UNICODE));
            }
            if (!(this.fdf.statusMessage == null || this.fdf.statusMessage.trim().length() == 0)) {
                dic.put(PdfName.STATUS, new PdfString(this.fdf.statusMessage));
            }
            PdfDictionary fd = new PdfDictionary();
            fd.put(PdfName.FDF, dic);
            PdfIndirectReference ref = addToBody(fd).getIndirectReference();
            this.os.write(getISOBytes("trailer\n"));
            PdfDictionary trailer = new PdfDictionary();
            trailer.put(PdfName.ROOT, ref);
            trailer.toPdf(null, this.os);
            this.os.write(getISOBytes("\n%%EOF\n"));
            this.os.close();
        }

        PdfArray calculate(HashMap<String, Object> map) throws IOException {
            PdfArray ar = new PdfArray();
            for (Entry<String, Object> entry : map.entrySet()) {
                String key = (String) entry.getKey();
                Object v = entry.getValue();
                PdfDictionary dic = new PdfDictionary();
                dic.put(PdfName.f134T, new PdfString(key, PdfObject.TEXT_UNICODE));
                if (v instanceof HashMap) {
                    dic.put(PdfName.KIDS, calculate((HashMap) v));
                } else if (v instanceof PdfAction) {
                    dic.put(PdfName.f117A, (PdfAction) v);
                } else if (v instanceof PdfAnnotation) {
                    dic.put(PdfName.AA, (PdfAnnotation) v);
                } else if ((v instanceof PdfDictionary) && ((PdfDictionary) v).size() == 1 && ((PdfDictionary) v).contains(PdfName.f128N)) {
                    dic.put(PdfName.AP, (PdfDictionary) v);
                } else {
                    dic.put(PdfName.f136V, (PdfObject) v);
                }
                ar.add(dic);
            }
            return ar;
        }
    }

    public FdfWriter(OutputStream os) throws IOException {
        this.wrt = new Wrt(os, this);
    }

    public void writeTo(OutputStream os) throws IOException {
        if (this.wrt == null) {
            this.wrt = new Wrt(os, this);
        }
        this.wrt.write();
    }

    public void write() throws IOException {
        this.wrt.write();
    }

    public String getStatusMessage() {
        return this.statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    boolean setField(String field, PdfObject value) {
        HashMap<String, Object> map = this.fields;
        StringTokenizer tk = new StringTokenizer(field, ".");
        if (!tk.hasMoreTokens()) {
            return false;
        }
        while (true) {
            String s = tk.nextToken();
            HashMap<String, Object> obj = map.get(s);
            if (!tk.hasMoreTokens()) {
                break;
            } else if (obj == null) {
                obj = new HashMap();
                map.put(s, obj);
                map = obj;
            } else if (!(obj instanceof HashMap)) {
                return false;
            } else {
                map = obj;
            }
        }
        if (obj instanceof HashMap) {
            return false;
        }
        map.put(s, value);
        return true;
    }

    void iterateFields(HashMap<String, Object> values, HashMap<String, Object> map, String name) {
        for (Entry<String, Object> entry : map.entrySet()) {
            String s = (String) entry.getKey();
            Object obj = entry.getValue();
            if (obj instanceof HashMap) {
                iterateFields(values, (HashMap) obj, name + "." + s);
            } else {
                values.put((name + "." + s).substring(1), obj);
            }
        }
    }

    public boolean removeField(String field) {
        HashMap<String, Object> map = this.fields;
        StringTokenizer tk = new StringTokenizer(field, ".");
        if (!tk.hasMoreTokens()) {
            return false;
        }
        ArrayList<Object> hist = new ArrayList();
        while (true) {
            String s = tk.nextToken();
            HashMap<String, Object> obj = map.get(s);
            if (obj != null) {
                hist.add(map);
                hist.add(s);
                if (!tk.hasMoreTokens()) {
                    break;
                } else if (!(obj instanceof HashMap)) {
                    return false;
                } else {
                    map = obj;
                }
            } else {
                return false;
            }
        }
        if (obj instanceof HashMap) {
            return false;
        }
        for (int k = hist.size() - 2; k >= 0; k -= 2) {
            map = (HashMap) hist.get(k);
            map.remove((String) hist.get(k + 1));
            if (!map.isEmpty()) {
                break;
            }
        }
        return true;
    }

    public HashMap<String, Object> getFields() {
        HashMap<String, Object> values = new HashMap();
        iterateFields(values, this.fields, "");
        return values;
    }

    public String getField(String field) {
        HashMap<String, Object> map = this.fields;
        StringTokenizer tk = new StringTokenizer(field, ".");
        if (!tk.hasMoreTokens()) {
            return null;
        }
        while (true) {
            HashMap<String, Object> obj = map.get(tk.nextToken());
            if (obj != null) {
                if (!tk.hasMoreTokens()) {
                    break;
                } else if (!(obj instanceof HashMap)) {
                    return null;
                } else {
                    map = obj;
                }
            } else {
                return null;
            }
        }
        if (obj instanceof HashMap) {
            return null;
        }
        if (((PdfObject) obj).isString()) {
            return ((PdfString) obj).toUnicodeString();
        }
        return PdfName.decodeName(obj.toString());
    }

    public boolean setFieldAsName(String field, String value) {
        return setField(field, new PdfName(value));
    }

    public boolean setFieldAsString(String field, String value) {
        return setField(field, new PdfString(value, PdfObject.TEXT_UNICODE));
    }

    public boolean setFieldAsAction(String field, PdfAction action) {
        return setField(field, action);
    }

    public boolean setFieldAsTemplate(String field, PdfTemplate template) {
        try {
            PdfDictionary d = new PdfDictionary();
            if (template instanceof PdfImportedPage) {
                d.put(PdfName.f128N, template.getIndirectReference());
            } else {
                d.put(PdfName.f128N, this.wrt.addToBody(template.getFormXObject(0)).getIndirectReference());
            }
            return setField(field, d);
        } catch (Exception e) {
            throw new ExceptionConverter(e);
        }
    }

    public boolean setFieldAsImage(String field, Image image) {
        try {
            if (Float.isNaN(image.getAbsoluteX())) {
                image.setAbsolutePosition(0.0f, image.getAbsoluteY());
            }
            if (Float.isNaN(image.getAbsoluteY())) {
                image.setAbsolutePosition(image.getAbsoluteY(), 0.0f);
            }
            PdfTemplate tmpl = PdfTemplate.createTemplate(this.wrt, image.getWidth(), image.getHeight());
            tmpl.addImage(image);
            PdfIndirectReference ref = this.wrt.addToBody(tmpl.getFormXObject(0)).getIndirectReference();
            PdfDictionary d = new PdfDictionary();
            d.put(PdfName.f128N, ref);
            return setField(field, d);
        } catch (Exception de) {
            throw new ExceptionConverter(de);
        }
    }

    public boolean setFieldAsJavascript(String field, PdfName jsTrigName, String js) {
        PdfAnnotation dict = this.wrt.createAnnotation(null, null);
        dict.put(jsTrigName, PdfAction.javaScript(js, this.wrt));
        return setField(field, dict);
    }

    public PdfImportedPage getImportedPage(PdfReader reader, int pageNumber) {
        return this.wrt.getImportedPage(reader, pageNumber);
    }

    public PdfTemplate createTemplate(float width, float height) {
        return PdfTemplate.createTemplate(this.wrt, width, height);
    }

    public void setFields(FdfReader fdf) {
        for (Entry<String, PdfDictionary> entry : fdf.getFields().entrySet()) {
            String key = (String) entry.getKey();
            PdfDictionary dic = (PdfDictionary) entry.getValue();
            PdfObject v = dic.get(PdfName.f136V);
            if (v != null) {
                setField(key, v);
            }
            v = dic.get(PdfName.f117A);
            if (v != null) {
                setField(key, v);
            }
        }
    }

    public void setFields(PdfReader pdf) {
        setFields(pdf.getAcroFields());
    }

    public void setFields(AcroFields af) {
        for (Entry<String, Item> entry : af.getFields().entrySet()) {
            String fn = (String) entry.getKey();
            PdfDictionary dic = ((Item) entry.getValue()).getMerged(0);
            PdfObject v = PdfReader.getPdfObjectRelease(dic.get(PdfName.f136V));
            if (v != null) {
                PdfObject ft = PdfReader.getPdfObjectRelease(dic.get(PdfName.FT));
                if (!(ft == null || PdfName.SIG.equals(ft))) {
                    setField(fn, v);
                }
            }
        }
    }

    public String getFile() {
        return this.file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    protected Counter getCounter() {
        return this.COUNTER;
    }
}
