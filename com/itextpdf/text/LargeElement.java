package com.itextpdf.text;

public interface LargeElement extends Element {
    void flushContent();

    boolean isComplete();

    void setComplete(boolean z);
}
