package com.itextpdf.text.html.simpleparser;

import java.util.Map;

class HTMLTagProcessors$3 implements HTMLTagProcessor {
    HTMLTagProcessors$3() {
    }

    public void startElement(HTMLWorker worker, String tag, Map<String, String> map) {
        worker.newLine();
    }

    public void endElement(HTMLWorker worker, String tag) {
    }
}
