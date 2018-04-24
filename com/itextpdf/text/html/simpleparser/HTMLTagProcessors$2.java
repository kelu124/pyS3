package com.itextpdf.text.html.simpleparser;

import java.util.Map;

class HTMLTagProcessors$2 implements HTMLTagProcessor {
    HTMLTagProcessors$2() {
    }

    public void startElement(HTMLWorker worker, String tag, Map<String, String> attrs) {
        worker.updateChain(tag, attrs);
        worker.flushContent();
    }

    public void endElement(HTMLWorker worker, String tag) {
        worker.processLink();
        worker.updateChain(tag);
    }
}
