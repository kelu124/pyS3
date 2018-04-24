package com.itextpdf.text.pdf.languages;

public interface LanguageProcessor {
    boolean isRTL();

    String process(String str);
}
