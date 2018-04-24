package com.itextpdf.text.xml.simpleparser.handler;

import com.itextpdf.text.xml.simpleparser.NewLineHandler;

public class NeverNewLineHandler implements NewLineHandler {
    public boolean isNewLineTag(String tag) {
        return false;
    }
}
