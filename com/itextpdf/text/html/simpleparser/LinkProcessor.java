package com.itextpdf.text.html.simpleparser;

import com.itextpdf.text.Paragraph;

@Deprecated
public interface LinkProcessor {
    boolean process(Paragraph paragraph, ChainedProperties chainedProperties);
}
