package org.apache.poi.ss.usermodel;

import org.apache.poi.ss.util.CellRangeAddress;

public interface ConditionalFormatting {
    void addRule(ConditionalFormattingRule conditionalFormattingRule);

    CellRangeAddress[] getFormattingRanges();

    int getNumberOfRules();

    ConditionalFormattingRule getRule(int i);

    void setRule(int i, ConditionalFormattingRule conditionalFormattingRule);
}
