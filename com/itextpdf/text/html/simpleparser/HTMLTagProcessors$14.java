package com.itextpdf.text.html.simpleparser;

import com.itextpdf.text.DocumentException;
import java.io.IOException;
import java.util.Map;

class HTMLTagProcessors$14 implements HTMLTagProcessor {
    HTMLTagProcessors$14() {
    }

    public void startElement(HTMLWorker worker, String tag, Map<String, String> attrs) throws DocumentException, IOException {
        worker.updateChain(tag, attrs);
        worker.processImage(worker.createImage(attrs), attrs);
        worker.updateChain(tag);
    }

    public void endElement(HTMLWorker worker, String tag) {
    }
}
