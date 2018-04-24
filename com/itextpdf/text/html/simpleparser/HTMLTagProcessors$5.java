package com.itextpdf.text.html.simpleparser;

import com.itextpdf.text.DocumentException;
import java.util.Map;

class HTMLTagProcessors$5 implements HTMLTagProcessor {
    HTMLTagProcessors$5() {
    }

    public void startElement(HTMLWorker worker, String tag, Map<String, String> attrs) throws DocumentException {
        worker.carriageReturn();
        worker.pushToStack(worker.createLineSeparator(attrs));
    }

    public void endElement(HTMLWorker worker, String tag) {
    }
}
