package com.itextpdf.text.pdf;

public interface HyphenationEvent {
    String getHyphenSymbol();

    String getHyphenatedWordPost();

    String getHyphenatedWordPre(String str, BaseFont baseFont, float f, float f2);
}
