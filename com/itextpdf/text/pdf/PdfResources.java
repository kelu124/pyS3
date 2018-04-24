package com.itextpdf.text.pdf;

class PdfResources extends PdfDictionary {
    PdfResources() {
    }

    void add(PdfName key, PdfDictionary resource) {
        if (resource.size() != 0) {
            PdfDictionary dic = getAsDict(key);
            if (dic == null) {
                put(key, resource);
            } else {
                dic.putAll(resource);
            }
        }
    }
}
