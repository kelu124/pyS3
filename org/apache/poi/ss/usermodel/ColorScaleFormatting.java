package org.apache.poi.ss.usermodel;

public interface ColorScaleFormatting {
    ConditionalFormattingThreshold createThreshold();

    Color[] getColors();

    int getNumControlPoints();

    ConditionalFormattingThreshold[] getThresholds();

    void setColors(Color[] colorArr);

    void setNumControlPoints(int i);

    void setThresholds(ConditionalFormattingThreshold[] conditionalFormattingThresholdArr);
}
