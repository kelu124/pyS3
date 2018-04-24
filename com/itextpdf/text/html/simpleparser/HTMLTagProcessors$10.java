package com.itextpdf.text.html.simpleparser;

import com.itextpdf.text.DocumentException;
import java.util.Map;

class HTMLTagProcessors$10 implements HTMLTagProcessor {
    HTMLTagProcessors$10() {
    }

    public void startElement(HTMLWorker worker, String tag, Map<String, String> attrs) throws DocumentException {
        worker.carriageReturn();
        worker.updateChain(tag, attrs);
    }

    public void endElement(HTMLWorker worker, String tag) throws DocumentException {
        worker.carriageReturn();
        worker.updateChain(tag);
    }
}
