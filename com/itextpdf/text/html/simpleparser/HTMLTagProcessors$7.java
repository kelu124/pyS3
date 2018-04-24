package com.itextpdf.text.html.simpleparser;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.html.HtmlTags;
import java.util.Map;

class HTMLTagProcessors$7 implements HTMLTagProcessor {
    HTMLTagProcessors$7() {
    }

    public void startElement(HTMLWorker worker, String tag, Map<String, String> attrs) throws DocumentException {
        worker.carriageReturn();
        if (!attrs.containsKey(HtmlTags.SIZE)) {
            attrs.put(HtmlTags.SIZE, Integer.toString(7 - Integer.parseInt(tag.substring(1))));
        }
        worker.updateChain(tag, attrs);
    }

    public void endElement(HTMLWorker worker, String tag) throws DocumentException {
        worker.carriageReturn();
        worker.updateChain(tag);
    }
}
