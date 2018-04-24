package org.apache.poi.wp.usermodel;

public interface CharacterRun {
    int getCharacterSpacing();

    String getFontName();

    int getFontSize();

    int getKerning();

    boolean isBold();

    boolean isCapitalized();

    boolean isDoubleStrikeThrough();

    boolean isEmbossed();

    boolean isHighlighted();

    boolean isImprinted();

    boolean isItalic();

    boolean isShadowed();

    boolean isSmallCaps();

    boolean isStrikeThrough();

    void setBold(boolean z);

    void setCapitalized(boolean z);

    void setCharacterSpacing(int i);

    void setDoubleStrikethrough(boolean z);

    void setEmbossed(boolean z);

    void setFontSize(int i);

    void setImprinted(boolean z);

    void setItalic(boolean z);

    void setKerning(int i);

    void setShadow(boolean z);

    void setSmallCaps(boolean z);

    void setStrikeThrough(boolean z);

    String text();
}
