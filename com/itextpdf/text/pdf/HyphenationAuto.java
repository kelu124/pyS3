package com.itextpdf.text.pdf;

import com.itextpdf.text.pdf.hyphenation.Hyphenation;
import com.itextpdf.text.pdf.hyphenation.Hyphenator;

public class HyphenationAuto implements HyphenationEvent {
    protected Hyphenator hyphenator;
    protected String post;

    public HyphenationAuto(String lang, String country, int leftMin, int rightMin) {
        this.hyphenator = new Hyphenator(lang, country, leftMin, rightMin);
    }

    public String getHyphenSymbol() {
        return "-";
    }

    public String getHyphenatedWordPre(String word, BaseFont font, float fontSize, float remainingWidth) {
        this.post = word;
        String hyphen = getHyphenSymbol();
        float hyphenWidth = font.getWidthPoint(hyphen, fontSize);
        if (hyphenWidth > remainingWidth) {
            return "";
        }
        Hyphenation hyphenation = this.hyphenator.hyphenate(word);
        if (hyphenation == null) {
            return "";
        }
        int len = hyphenation.length();
        int k = 0;
        while (k < len && font.getWidthPoint(hyphenation.getPreHyphenText(k), fontSize) + hyphenWidth <= remainingWidth) {
            k++;
        }
        k--;
        if (k < 0) {
            return "";
        }
        this.post = hyphenation.getPostHyphenText(k);
        return hyphenation.getPreHyphenText(k) + hyphen;
    }

    public String getHyphenatedWordPost() {
        return this.post;
    }
}
