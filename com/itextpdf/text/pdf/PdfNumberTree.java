package com.itextpdf.text.pdf;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

public class PdfNumberTree {
    private static final int leafSize = 64;

    public static <O extends PdfObject> PdfDictionary writeTree(HashMap<Integer, O> items, PdfWriter writer) throws IOException {
        if (items.isEmpty()) {
            return null;
        }
        Integer[] numbers = (Integer[]) items.keySet().toArray(new Integer[items.size()]);
        Arrays.sort(numbers);
        int k;
        if (numbers.length <= 64) {
            PdfDictionary dic = new PdfDictionary();
            PdfArray ar = new PdfArray();
            for (k = 0; k < numbers.length; k++) {
                ar.add(new PdfNumber(numbers[k].intValue()));
                ar.add((PdfObject) items.get(numbers[k]));
            }
            dic.put(PdfName.NUMS, ar);
            return dic;
        }
        PdfArray arr;
        int skip = 64;
        PdfIndirectReference[] kids = new PdfIndirectReference[(((numbers.length + 64) - 1) / 64)];
        for (k = 0; k < kids.length; k++) {
            int offset = k * 64;
            int end = Math.min(offset + 64, numbers.length);
            dic = new PdfDictionary();
            arr = new PdfArray();
            arr.add(new PdfNumber(numbers[offset].intValue()));
            arr.add(new PdfNumber(numbers[end - 1].intValue()));
            dic.put(PdfName.LIMITS, arr);
            arr = new PdfArray();
            while (offset < end) {
                arr.add(new PdfNumber(numbers[offset].intValue()));
                arr.add((PdfObject) items.get(numbers[offset]));
                offset++;
            }
            dic.put(PdfName.NUMS, arr);
            kids[k] = writer.addToBody(dic).getIndirectReference();
        }
        int top = kids.length;
        while (top > 64) {
            skip *= 64;
            int tt = ((numbers.length + skip) - 1) / skip;
            for (k = 0; k < tt; k++) {
                offset = k * 64;
                end = Math.min(offset + 64, top);
                dic = new PdfDictionary();
                arr = new PdfArray();
                arr.add(new PdfNumber(numbers[k * skip].intValue()));
                arr.add(new PdfNumber(numbers[Math.min((k + 1) * skip, numbers.length) - 1].intValue()));
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

    private static void iterateItems(PdfDictionary dic, HashMap<Integer, PdfObject> items) {
        PdfArray nn = (PdfArray) PdfReader.getPdfObjectRelease(dic.get(PdfName.NUMS));
        int k;
        if (nn != null) {
            k = 0;
            while (k < nn.size()) {
                int k2 = k + 1;
                items.put(Integer.valueOf(((PdfNumber) PdfReader.getPdfObjectRelease(nn.getPdfObject(k))).intValue()), nn.getPdfObject(k2));
                k = k2 + 1;
            }
            return;
        }
        nn = (PdfArray) PdfReader.getPdfObjectRelease(dic.get(PdfName.KIDS));
        if (nn != null) {
            for (k = 0; k < nn.size(); k++) {
                iterateItems((PdfDictionary) PdfReader.getPdfObjectRelease(nn.getPdfObject(k)), items);
            }
        }
    }

    public static HashMap<Integer, PdfObject> readTree(PdfDictionary dic) {
        HashMap<Integer, PdfObject> items = new HashMap();
        if (dic != null) {
            iterateItems(dic, items);
        }
        return items;
    }
}
