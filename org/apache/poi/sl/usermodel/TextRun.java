package org.apache.poi.sl.usermodel;

import java.awt.Color;

public interface TextRun {

    public enum TextCap {
        NONE,
        SMALL,
        ALL
    }

    Hyperlink<?, ?> createHyperlink();

    PaintStyle getFontColor();

    String getFontFamily();

    Double getFontSize();

    Hyperlink<?, ?> getHyperlink();

    byte getPitchAndFamily();

    String getRawText();

    TextCap getTextCap();

    boolean isBold();

    boolean isItalic();

    boolean isStrikethrough();

    boolean isSubscript();

    boolean isSuperscript();

    boolean isUnderlined();

    void setBold(boolean z);

    void setFontColor(Color color);

    void setFontColor(PaintStyle paintStyle);

    void setFontFamily(String str);

    void setFontSize(Double d);

    void setItalic(boolean z);

    void setStrikethrough(boolean z);

    void setText(String str);

    void setUnderlined(boolean z);
}
