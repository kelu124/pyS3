package com.itextpdf.text.xml.simpleparser.handler;

import com.itextpdf.text.html.HtmlTags;
import com.itextpdf.text.xml.simpleparser.NewLineHandler;
import java.util.HashSet;
import java.util.Set;

public class HTMLNewLineHandler implements NewLineHandler {
    private final Set<String> newLineTags = new HashSet();

    public HTMLNewLineHandler() {
        this.newLineTags.add(HtmlTags.f35P);
        this.newLineTags.add(HtmlTags.BLOCKQUOTE);
        this.newLineTags.add(HtmlTags.BR);
    }

    public boolean isNewLineTag(String tag) {
        return this.newLineTags.contains(tag);
    }
}
