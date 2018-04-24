package com.itextpdf.text.html.simpleparser;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.html.HtmlTags;
import java.util.Map;

class HTMLTagProcessors$13 implements HTMLTagProcessor {
    HTMLTagProcessors$13() {
    }

    public void startElement(HTMLWorker worker, String tag, Map<String, String> attrs) throws DocumentException {
        worker.carriageReturn();
        if (worker.isPendingTD()) {
            worker.endElement(tag);
        }
        worker.setSkipText(false);
        worker.setPendingTD(true);
        worker.updateChain(HtmlTags.TD, attrs);
        worker.pushToStack(worker.createCell(tag));
    }

    public void endElement(HTMLWorker worker, String tag) throws DocumentException {
        worker.carriageReturn();
        worker.setPendingTD(false);
        worker.updateChain(HtmlTags.TD);
        worker.setSkipText(true);
    }
}
