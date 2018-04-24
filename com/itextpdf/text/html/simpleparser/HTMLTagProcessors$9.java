package com.itextpdf.text.html.simpleparser;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.html.HtmlTags;
import java.util.Map;

class HTMLTagProcessors$9 implements HTMLTagProcessor {
    HTMLTagProcessors$9() {
    }

    public void startElement(HTMLWorker worker, String tag, Map<String, String> attrs) throws DocumentException {
        worker.carriageReturn();
        if (!attrs.containsKey(HtmlTags.FACE)) {
            attrs.put(HtmlTags.FACE, "Courier");
        }
        worker.updateChain(tag, attrs);
        worker.setInsidePRE(true);
    }

    public void endElement(HTMLWorker worker, String tag) throws DocumentException {
        worker.carriageReturn();
        worker.updateChain(tag);
        worker.setInsidePRE(false);
    }
}
