package org.apache.poi.ss.usermodel;

public interface RichTextString {
    void applyFont(int i, int i2, Font font);

    void applyFont(int i, int i2, short s);

    void applyFont(Font font);

    void applyFont(short s);

    void clearFormatting();

    int getIndexOfFormattingRun(int i);

    String getString();

    int length();

    int numFormattingRuns();
}
