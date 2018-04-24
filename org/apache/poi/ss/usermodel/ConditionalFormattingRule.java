package org.apache.poi.ss.usermodel;

public interface ConditionalFormattingRule {
    BorderFormatting createBorderFormatting();

    FontFormatting createFontFormatting();

    PatternFormatting createPatternFormatting();

    BorderFormatting getBorderFormatting();

    ColorScaleFormatting getColorScaleFormatting();

    byte getComparisonOperation();

    ConditionType getConditionType();

    DataBarFormatting getDataBarFormatting();

    FontFormatting getFontFormatting();

    String getFormula1();

    String getFormula2();

    IconMultiStateFormatting getMultiStateFormatting();

    PatternFormatting getPatternFormatting();
}
