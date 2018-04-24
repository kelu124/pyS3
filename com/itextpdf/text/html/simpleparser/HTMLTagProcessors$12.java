package com.itextpdf.text.html.simpleparser;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.html.HtmlTags;
import java.util.Map;

class HTMLTagProcessors$12 implements HTMLTagProcessor {
    HTMLTagProcessors$12() {
    }

    public void startElement(HTMLWorker worker, String tag, Map<String, String> attrs) throws DocumentException {
        worker.carriageReturn();
        if (worker.isPendingTR()) {
            worker.endElement(tag);
        }
        worker.setSkipText(true);
        worker.setPendingTR(true);
        worker.updateChain(tag, attrs);
    }

    public void endElement(HTMLWorker worker, String tag) throws DocumentException {
        worker.carriageReturn();
        if (worker.isPendingTD()) {
            worker.endElement(HtmlTags.TD);
        }
        worker.setPendingTR(false);
        worker.updateChain(tag);
        worker.processRow();
        worker.setSkipText(true);
    }
}
