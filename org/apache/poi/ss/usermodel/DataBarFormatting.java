package org.apache.poi.ss.usermodel;

public interface DataBarFormatting {
    Color getColor();

    ConditionalFormattingThreshold getMaxThreshold();

    ConditionalFormattingThreshold getMinThreshold();

    int getWidthMax();

    int getWidthMin();

    boolean isIconOnly();

    boolean isLeftToRight();

    void setColor(Color color);

    void setIconOnly(boolean z);

    void setLeftToRight(boolean z);

    void setWidthMax(int i);

    void setWidthMin(int i);
}
