package org.apache.poi.hssf.usermodel;

import org.apache.poi.hssf.record.CFRule12Record;
import org.apache.poi.hssf.record.CFRuleBase;
import org.apache.poi.hssf.record.CFRuleRecord;
import org.apache.poi.hssf.record.aggregates.CFRecordsAggregate;
import org.apache.poi.hssf.record.aggregates.ConditionalFormattingTable;
import org.apache.poi.ss.SpreadsheetVersion;
import org.apache.poi.ss.usermodel.ConditionalFormatting;
import org.apache.poi.ss.usermodel.ConditionalFormattingRule;
import org.apache.poi.ss.usermodel.ExtendedColor;
import org.apache.poi.ss.usermodel.IconMultiStateFormatting.IconSet;
import org.apache.poi.ss.usermodel.SheetConditionalFormatting;
import org.apache.poi.ss.util.CellRangeAddress;

public final class HSSFSheetConditionalFormatting implements SheetConditionalFormatting {
    private final ConditionalFormattingTable _conditionalFormattingTable;
    private final HSSFSheet _sheet;

    HSSFSheetConditionalFormatting(HSSFSheet sheet) {
        this._sheet = sheet;
        this._conditionalFormattingTable = sheet.getSheet().getConditionalFormattingTable();
    }

    public HSSFConditionalFormattingRule createConditionalFormattingRule(byte comparisonOperation, String formula1, String formula2) {
        return new HSSFConditionalFormattingRule(this._sheet, CFRuleRecord.create(this._sheet, comparisonOperation, formula1, formula2));
    }

    public HSSFConditionalFormattingRule createConditionalFormattingRule(byte comparisonOperation, String formula1) {
        return new HSSFConditionalFormattingRule(this._sheet, CFRuleRecord.create(this._sheet, comparisonOperation, formula1, null));
    }

    public HSSFConditionalFormattingRule createConditionalFormattingRule(String formula) {
        return new HSSFConditionalFormattingRule(this._sheet, CFRuleRecord.create(this._sheet, formula));
    }

    public HSSFConditionalFormattingRule createConditionalFormattingRule(IconSet iconSet) {
        return new HSSFConditionalFormattingRule(this._sheet, CFRule12Record.create(this._sheet, iconSet));
    }

    public HSSFConditionalFormattingRule createConditionalFormattingRule(HSSFExtendedColor color) {
        return new HSSFConditionalFormattingRule(this._sheet, CFRule12Record.create(this._sheet, color.getExtendedColor()));
    }

    public HSSFConditionalFormattingRule createConditionalFormattingRule(ExtendedColor color) {
        return createConditionalFormattingRule((HSSFExtendedColor) color);
    }

    public HSSFConditionalFormattingRule createConditionalFormattingColorScaleRule() {
        return new HSSFConditionalFormattingRule(this._sheet, CFRule12Record.createColorScale(this._sheet));
    }

    public int addConditionalFormatting(HSSFConditionalFormatting cf) {
        return this._conditionalFormattingTable.add(cf.getCFRecordsAggregate().cloneCFAggregate());
    }

    public int addConditionalFormatting(ConditionalFormatting cf) {
        return addConditionalFormatting((HSSFConditionalFormatting) cf);
    }

    public int addConditionalFormatting(CellRangeAddress[] regions, HSSFConditionalFormattingRule[] cfRules) {
        if (regions == null) {
            throw new IllegalArgumentException("regions must not be null");
        }
        for (CellRangeAddress range : regions) {
            range.validate(SpreadsheetVersion.EXCEL97);
        }
        if (cfRules == null) {
            throw new IllegalArgumentException("cfRules must not be null");
        } else if (cfRules.length == 0) {
            throw new IllegalArgumentException("cfRules must not be empty");
        } else if (cfRules.length > 3) {
            throw new IllegalArgumentException("Number of rules must not exceed 3");
        } else {
            CFRuleBase[] rules = new CFRuleBase[cfRules.length];
            for (int i = 0; i != cfRules.length; i++) {
                rules[i] = cfRules[i].getCfRuleRecord();
            }
            return this._conditionalFormattingTable.add(new CFRecordsAggregate(regions, rules));
        }
    }

    public int addConditionalFormatting(CellRangeAddress[] regions, ConditionalFormattingRule[] cfRules) {
        HSSFConditionalFormattingRule[] hfRules;
        if (cfRules instanceof HSSFConditionalFormattingRule[]) {
            hfRules = (HSSFConditionalFormattingRule[]) cfRules;
        } else {
            hfRules = new HSSFConditionalFormattingRule[cfRules.length];
            System.arraycopy(cfRules, 0, hfRules, 0, hfRules.length);
        }
        return addConditionalFormatting(regions, hfRules);
    }

    public int addConditionalFormatting(CellRangeAddress[] regions, HSSFConditionalFormattingRule rule1) {
        return addConditionalFormatting(regions, rule1 == null ? null : new HSSFConditionalFormattingRule[]{rule1});
    }

    public int addConditionalFormatting(CellRangeAddress[] regions, ConditionalFormattingRule rule1) {
        return addConditionalFormatting(regions, (HSSFConditionalFormattingRule) rule1);
    }

    public int addConditionalFormatting(CellRangeAddress[] regions, HSSFConditionalFormattingRule rule1, HSSFConditionalFormattingRule rule2) {
        return addConditionalFormatting(regions, new HSSFConditionalFormattingRule[]{rule1, rule2});
    }

    public int addConditionalFormatting(CellRangeAddress[] regions, ConditionalFormattingRule rule1, ConditionalFormattingRule rule2) {
        return addConditionalFormatting(regions, (HSSFConditionalFormattingRule) rule1, (HSSFConditionalFormattingRule) rule2);
    }

    public HSSFConditionalFormatting getConditionalFormattingAt(int index) {
        CFRecordsAggregate cf = this._conditionalFormattingTable.get(index);
        if (cf == null) {
            return null;
        }
        return new HSSFConditionalFormatting(this._sheet, cf);
    }

    public int getNumConditionalFormattings() {
        return this._conditionalFormattingTable.size();
    }

    public void removeConditionalFormatting(int index) {
        this._conditionalFormattingTable.remove(index);
    }
}
