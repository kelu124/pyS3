package com.itextpdf.text.html.simpleparser;

import com.itextpdf.text.DocumentException;
import java.util.Map;

class HTMLTagProcessors$8 implements HTMLTagProcessor {
    HTMLTagProcessors$8() {
    }

    public void startElement(HTMLWorker worker, String tag, Map<String, String> attrs) throws DocumentException {
        worker.carriageReturn();
        if (worker.isPendingLI()) {
            worker.endElement(tag);
        }
        worker.setSkipText(false);
        worker.setPendingLI(true);
        worker.updateChain(tag, attrs);
        worker.pushToStack(worker.createListItem());
    }

    public void endElement(HTMLWorker worker, String tag) throws DocumentException {
        worker.carriageReturn();
        worker.setPendingLI(false);
        worker.setSkipText(true);
        worker.updateChain(tag);
        worker.processListItem();
    }
}
