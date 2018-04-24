package com.itextpdf.text.html.simpleparser;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.html.HtmlTags;
import java.util.Map;

class HTMLTagProcessors$4 implements HTMLTagProcessor {
    HTMLTagProcessors$4() {
    }

    public void startElement(HTMLWorker worker, String tag, Map<String, String> attrs) throws DocumentException {
        worker.carriageReturn();
        if (worker.isPendingLI()) {
            worker.endElement(HtmlTags.LI);
        }
        worker.setSkipText(true);
        worker.updateChain(tag, attrs);
        worker.pushToStack(worker.createList(tag));
    }

    public void endElement(HTMLWorker worker, String tag) throws DocumentException {
        worker.carriageReturn();
        if (worker.isPendingLI()) {
            worker.endElement(HtmlTags.LI);
        }
        worker.setSkipText(false);
        worker.updateChain(tag);
        worker.processList();
    }
}
