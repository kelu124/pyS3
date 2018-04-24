package com.itextpdf.text.pdf;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

public class PdfNameTree {
    private static final int leafSize = 64;

    public static PdfDictionary writeTree(HashMap<String, ? extends PdfObject> items, PdfWriter writer) throws IOException {
        if (items.isEmpty()) {
            return null;
        }
        String[] names = (String[]) items.keySet().toArray(new String[items.size()]);
        Arrays.sort(names);
        int k;
        if (names.length <= 64) {
            PdfDictionary dic = new PdfDictionary();
            PdfArray ar = new PdfArray();
            for (k = 0; k < names.length; k++) {
                ar.add(new PdfString(names[k], null));
                ar.add((PdfObject) items.get(names[k]));
            }
            dic.put(PdfName.NAMES, ar);
            return dic;
        }
        PdfArray arr;
        int skip = 64;
        PdfIndirectReference[] kids = new PdfIndirectReference[(((names.length + 64) - 1) / 64)];
        for (k = 0; k < kids.length; k++) {
            int offset = k * 64;
            int end = Math.min(offset + 64, names.length);
            dic = new PdfDictionary();
            arr = new PdfArray();
            arr.add(new PdfString(names[offset], null));
            arr.add(new PdfString(names[end - 1], null));
            dic.put(PdfName.LIMITS, arr);
            arr = new PdfArray();
            while (offset < end) {
                arr.add(new PdfString(names[offset], null));
                arr.add((PdfObject) items.get(names[offset]));
                offset++;
            }
            dic.put(PdfName.NAMES, arr);
            kids[k] = writer.addToBody(dic).getIndirectReference();
        }
        int top = kids.length;
        while (top > 64) {
            skip *= 64;
            int tt = ((names.length + skip) - 1) / skip;
            for (k = 0; k < tt; k++) {
                offset = k * 64;
                end = Math.min(offset + 64, top);
                dic = new PdfDictionary();
                arr = new PdfArray();
                arr.add(new PdfString(names[k * skip], null));
                arr.add(new PdfString(names[Math.min((k + 1) * skip, names.length) - 1], null));
                dic.put(PdfName.LIMITS, arr);
                arr = new PdfArray();
                while (offset < end) {
                    arr.add(kids[offset]);
                    offset++;
                }
                dic.put(PdfName.KIDS, arr);
                kids[k] = writer.addToBody(dic).getIndirectReference();
            }
            top = tt;
        }
        arr = new PdfArray();
        for (k = 0; k < top; k++) {
            arr.add(kids[k]);
        }
        dic = new PdfDictionary();
        dic.put(PdfName.KIDS, arr);
        return dic;
    }

    private static PdfString iterateItems(PdfDictionary dic, HashMap<String, PdfObject> items, PdfString leftOverString) {
        PdfArray nn = (PdfArray) PdfReader.getPdfObjectRelease(dic.get(PdfName.NAMES));
        int k;
        if (nn != null) {
            k = 0;
            while (k < nn.size()) {
                PdfString s;
                if (leftOverString == null) {
                    s = (PdfString) PdfReader.getPdfObjectRelease(nn.getPdfObject(k));
                    k++;
                } else {
                    s = leftOverString;
                    leftOverString = null;
                }
                if (k >= nn.size()) {
                    return s;
                }
                items.put(PdfEncodings.convertToString(s.getBytes(), null), nn.getPdfObject(k));
                k++;
            }
        } else {
            nn = (PdfArray) PdfReader.getPdfObjectRelease(dic.get(PdfName.KIDS));
            if (nn != null) {
                for (k = 0; k < nn.size(); k++) {
                    leftOverString = iterateItems((PdfDictionary) PdfReader.getPdfObjectRelease(nn.getPdfObject(k)), items, leftOverString);
                }
            }
        }
        return null;
    }

    public static HashMap<String, PdfObject> readTree(PdfDictionary dic) {
        HashMap<String, PdfObject> items = new HashMap();
        if (dic != null) {
            iterateItems(dic, items, null);
        }
        return items;
    }
}
