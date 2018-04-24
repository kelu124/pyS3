package org.apache.poi.ss.usermodel;

import org.apache.poi.ss.usermodel.IconMultiStateFormatting.IconSet;
import org.apache.poi.ss.util.CellRangeAddress;

public interface SheetConditionalFormatting {
    int addConditionalFormatting(ConditionalFormatting conditionalFormatting);

    int addConditionalFormatting(CellRangeAddress[] cellRangeAddressArr, ConditionalFormattingRule conditionalFormattingRule);

    int addConditionalFormatting(CellRangeAddress[] cellRangeAddressArr, ConditionalFormattingRule conditionalFormattingRule, ConditionalFormattingRule conditionalFormattingRule2);

    int addConditionalFormatting(CellRangeAddress[] cellRangeAddressArr, ConditionalFormattingRule[] conditionalFormattingRuleArr);

    ConditionalFormattingRule createConditionalFormattingColorScaleRule();

    ConditionalFormattingRule createConditionalFormattingRule(byte b, String str);

    ConditionalFormattingRule createConditionalFormattingRule(byte b, String str, String str2);

    ConditionalFormattingRule createConditionalFormattingRule(String str);

    ConditionalFormattingRule createConditionalFormattingRule(ExtendedColor extendedColor);

    ConditionalFormattingRule createConditionalFormattingRule(IconSet iconSet);

    ConditionalFormatting getConditionalFormattingAt(int i);

    int getNumConditionalFormattings();

    void removeConditionalFormatting(int i);
}
