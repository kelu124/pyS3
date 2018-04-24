package org.apache.poi.hssf.usermodel;

import org.apache.poi.hssf.record.aggregates.CFRecordsAggregate;
import org.apache.poi.ss.usermodel.ConditionalFormatting;
import org.apache.poi.ss.usermodel.ConditionalFormattingRule;
import org.apache.poi.ss.util.CellRangeAddress;

public final class HSSFConditionalFormatting implements ConditionalFormatting {
    private final CFRecordsAggregate cfAggregate;
    private final HSSFSheet sheet;

    HSSFConditionalFormatting(HSSFSheet sheet, CFRecordsAggregate cfAggregate) {
        if (sheet == null) {
            throw new IllegalArgumentException("sheet must not be null");
        } else if (cfAggregate == null) {
            throw new IllegalArgumentException("cfAggregate must not be null");
        } else {
            this.sheet = sheet;
            this.cfAggregate = cfAggregate;
        }
    }

    CFRecordsAggregate getCFRecordsAggregate() {
        return this.cfAggregate;
    }

    public CellRangeAddress[] getFormattingRanges() {
        return this.cfAggregate.getHeader().getCellRanges();
    }

    public void setRule(int idx, HSSFConditionalFormattingRule cfRule) {
        this.cfAggregate.setRule(idx, cfRule.getCfRuleRecord());
    }

    public void setRule(int idx, ConditionalFormattingRule cfRule) {
        setRule(idx, (HSSFConditionalFormattingRule) cfRule);
    }

    public void addRule(HSSFConditionalFormattingRule cfRule) {
        this.cfAggregate.addRule(cfRule.getCfRuleRecord());
    }

    public void addRule(ConditionalFormattingRule cfRule) {
        addRule((HSSFConditionalFormattingRule) cfRule);
    }

    public HSSFConditionalFormattingRule getRule(int idx) {
        return new HSSFConditionalFormattingRule(this.sheet, this.cfAggregate.getRule(idx));
    }

    public int getNumberOfRules() {
        return this.cfAggregate.getNumberOfRules();
    }

    public String toString() {
        return this.cfAggregate.toString();
    }
}
