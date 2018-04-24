package com.itextpdf.text.html.simpleparser;

import com.itextpdf.text.html.HtmlTags;
import java.util.Map;

class HTMLTagProcessors$1 implements HTMLTagProcessor {
    HTMLTagProcessors$1() {
    }

    public void startElement(HTMLWorker worker, String tag, Map<String, String> attrs) {
        tag = mapTag(tag);
        attrs.put(tag, null);
        worker.updateChain(tag, attrs);
    }

    public void endElement(HTMLWorker worker, String tag) {
        worker.updateChain(mapTag(tag));
    }

    private String mapTag(String tag) {
        if (HtmlTags.EM.equalsIgnoreCase(tag)) {
            return "i";
        }
        if (HtmlTags.STRONG.equalsIgnoreCase(tag)) {
            return HtmlTags.f33B;
        }
        if (HtmlTags.STRIKE.equalsIgnoreCase(tag)) {
            return HtmlTags.f36S;
        }
        return tag;
    }
}
