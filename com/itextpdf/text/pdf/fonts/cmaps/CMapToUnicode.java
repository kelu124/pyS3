package com.itextpdf.text.pdf.fonts.cmaps;

import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.error_messages.MessageLocalization;
import com.itextpdf.text.pdf.PdfObject;
import com.itextpdf.text.pdf.PdfString;
import com.itextpdf.text.xml.xmp.XmpWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class CMapToUnicode extends AbstractCMap {
    private Map<Integer, String> doubleByteMappings = new HashMap();
    private Map<Integer, String> singleByteMappings = new HashMap();

    public boolean hasOneByteMappings() {
        return !this.singleByteMappings.isEmpty();
    }

    public boolean hasTwoByteMappings() {
        return !this.doubleByteMappings.isEmpty();
    }

    public String lookup(byte[] code, int offset, int length) {
        if (length == 1) {
            return (String) this.singleByteMappings.get(Integer.valueOf(code[offset] & 255));
        } else if (length != 2) {
            return null;
        } else {
            return (String) this.doubleByteMappings.get(Integer.valueOf(((code[offset] & 255) << 8) + (code[offset + 1] & 255)));
        }
    }

    public Map<Integer, Integer> createReverseMapping() throws IOException {
        Map<Integer, Integer> result = new HashMap();
        for (Entry<Integer, String> entry : this.singleByteMappings.entrySet()) {
            result.put(Integer.valueOf(convertToInt((String) entry.getValue())), entry.getKey());
        }
        for (Entry<Integer, String> entry2 : this.doubleByteMappings.entrySet()) {
            result.put(Integer.valueOf(convertToInt((String) entry2.getValue())), entry2.getKey());
        }
        return result;
    }

    public Map<Integer, Integer> createDirectMapping() throws IOException {
        Map<Integer, Integer> result = new HashMap();
        for (Entry<Integer, String> entry : this.singleByteMappings.entrySet()) {
            result.put(entry.getKey(), Integer.valueOf(convertToInt((String) entry.getValue())));
        }
        for (Entry<Integer, String> entry2 : this.doubleByteMappings.entrySet()) {
            result.put(entry2.getKey(), Integer.valueOf(convertToInt((String) entry2.getValue())));
        }
        return result;
    }

    private int convertToInt(String s) throws IOException {
        byte[] b = s.getBytes(XmpWriter.UTF16BE);
        int value = 0;
        for (int i = 0; i < b.length - 1; i++) {
            value = (value + (b[i] & 255)) << 8;
        }
        return value + (b[b.length - 1] & 255);
    }

    void addChar(int cid, String uni) {
        this.doubleByteMappings.put(Integer.valueOf(cid), uni);
    }

    void addChar(PdfString mark, PdfObject code) {
        try {
            byte[] src = mark.getBytes();
            String dest = createStringFromBytes(code.getBytes());
            if (src.length == 1) {
                this.singleByteMappings.put(Integer.valueOf(src[0] & 255), dest);
            } else if (src.length == 2) {
                this.doubleByteMappings.put(Integer.valueOf(((src[0] & 255) << 8) | (src[1] & 255)), dest);
            } else {
                throw new IOException(MessageLocalization.getComposedMessage("mapping.code.should.be.1.or.two.bytes.and.not.1", src.length));
            }
        } catch (Exception ex) {
            throw new ExceptionConverter(ex);
        }
    }

    private String createStringFromBytes(byte[] bytes) throws IOException {
        if (bytes.length == 1) {
            return new String(bytes);
        }
        return new String(bytes, XmpWriter.UTF16BE);
    }
}
