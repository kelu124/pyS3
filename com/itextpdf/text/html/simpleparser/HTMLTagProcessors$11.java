package com.itextpdf.text.html.simpleparser;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.html.HtmlTags;
import java.util.Map;

class HTMLTagProcessors$11 implements HTMLTagProcessor {
    HTMLTagProcessors$11() {
    }

    public void startElement(HTMLWorker worker, String tag, Map<String, String> attrs) throws DocumentException {
        worker.carriageReturn();
        worker.pushToStack(new TableWrapper(attrs));
        worker.pushTableState();
        worker.setPendingTD(false);
        worker.setPendingTR(false);
        worker.setSkipText(true);
        attrs.remove(HtmlTags.ALIGN);
        attrs.put(HtmlTags.COLSPAN, "1");
        attrs.put(HtmlTags.ROWSPAN, "1");
        worker.updateChain(tag, attrs);
    }

    public void endElement(HTMLWorker worker, String tag) throws DocumentException {
        worker.carriageReturn();
        if (worker.isPendingTR()) {
            worker.endElement(HtmlTags.TR);
        }
        worker.updateChain(tag);
        worker.processTable();
        worker.popTableState();
        worker.setSkipText(false);
    }
}
