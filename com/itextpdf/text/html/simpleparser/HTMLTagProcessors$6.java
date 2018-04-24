package com.itextpdf.text.html.simpleparser;

import java.util.Map;

class HTMLTagProcessors$6 implements HTMLTagProcessor {
    HTMLTagProcessors$6() {
    }

    public void startElement(HTMLWorker worker, String tag, Map<String, String> attrs) {
        worker.updateChain(tag, attrs);
    }

    public void endElement(HTMLWorker worker, String tag) {
        worker.updateChain(tag);
    }
}
