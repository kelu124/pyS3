package org.apache.poi.hssf.usermodel;

import org.apache.poi.hssf.model.HSSFFormulaParser;
import org.apache.poi.hssf.record.CFRule12Record;
import org.apache.poi.hssf.record.CFRuleBase;
import org.apache.poi.hssf.record.cf.BorderFormatting;
import org.apache.poi.hssf.record.cf.FontFormatting;
import org.apache.poi.hssf.record.cf.PatternFormatting;
import org.apache.poi.ss.formula.ptg.Ptg;
import org.apache.poi.ss.usermodel.ConditionType;
import org.apache.poi.ss.usermodel.ConditionalFormattingRule;

public final class HSSFConditionalFormattingRule implements ConditionalFormattingRule {
    private static final byte CELL_COMPARISON = (byte) 1;
    private final CFRuleBase cfRuleRecord;
    private final HSSFSheet sheet;
    private final HSSFWorkbook workbook;

    HSSFConditionalFormattingRule(HSSFSheet pSheet, CFRuleBase pRuleRecord) {
        if (pSheet == null) {
            throw new IllegalArgumentException("pSheet must not be null");
        } else if (pRuleRecord == null) {
            throw new IllegalArgumentException("pRuleRecord must not be null");
        } else {
            this.sheet = pSheet;
            this.workbook = pSheet.getWorkbook();
            this.cfRuleRecord = pRuleRecord;
        }
    }

    CFRuleBase getCfRuleRecord() {
        return this.cfRuleRecord;
    }

    private CFRule12Record getCFRule12Record(boolean create) {
        if (this.cfRuleRecord instanceof CFRule12Record) {
            return (CFRule12Record) this.cfRuleRecord;
        }
        if (!create) {
            return null;
        }
        throw new IllegalArgumentException("Can't convert a CF into a CF12 record");
    }

    private HSSFFontFormatting getFontFormatting(boolean create) {
        if (this.cfRuleRecord.getFontFormatting() == null) {
            if (!create) {
                return null;
            }
            this.cfRuleRecord.setFontFormatting(new FontFormatting());
        }
        return new HSSFFontFormatting(this.cfRuleRecord, this.workbook);
    }

    public HSSFFontFormatting getFontFormatting() {
        return getFontFormatting(false);
    }

    public HSSFFontFormatting createFontFormatting() {
        return getFontFormatting(true);
    }

    private HSSFBorderFormatting getBorderFormatting(boolean create) {
        if (this.cfRuleRecord.getBorderFormatting() == null) {
            if (!create) {
                return null;
            }
            this.cfRuleRecord.setBorderFormatting(new BorderFormatting());
        }
        return new HSSFBorderFormatting(this.cfRuleRecord, this.workbook);
    }

    public HSSFBorderFormatting getBorderFormatting() {
        return getBorderFormatting(false);
    }

    public HSSFBorderFormatting createBorderFormatting() {
        return getBorderFormatting(true);
    }

    private HSSFPatternFormatting getPatternFormatting(boolean create) {
        if (this.cfRuleRecord.getPatternFormatting() == null) {
            if (!create) {
                return null;
            }
            this.cfRuleRecord.setPatternFormatting(new PatternFormatting());
        }
        return new HSSFPatternFormatting(this.cfRuleRecord, this.workbook);
    }

    public HSSFPatternFormatting getPatternFormatting() {
        return getPatternFormatting(false);
    }

    public HSSFPatternFormatting createPatternFormatting() {
        return getPatternFormatting(true);
    }

    private HSSFDataBarFormatting getDataBarFormatting(boolean create) {
        CFRule12Record cfRule12Record = getCFRule12Record(create);
        if (cfRule12Record == null) {
            return null;
        }
        if (cfRule12Record.getDataBarFormatting() == null) {
            if (!create) {
                return null;
            }
            cfRule12Record.createDataBarFormatting();
        }
        return new HSSFDataBarFormatting(cfRule12Record, this.sheet);
    }

    public HSSFDataBarFormatting getDataBarFormatting() {
        return getDataBarFormatting(false);
    }

    public HSSFDataBarFormatting createDataBarFormatting() {
        return getDataBarFormatting(true);
    }

    private HSSFIconMultiStateFormatting getMultiStateFormatting(boolean create) {
        CFRule12Record cfRule12Record = getCFRule12Record(create);
        if (cfRule12Record == null) {
            return null;
        }
        if (cfRule12Record.getMultiStateFormatting() == null) {
            if (!create) {
                return null;
            }
            cfRule12Record.createMultiStateFormatting();
        }
        return new HSSFIconMultiStateFormatting(cfRule12Record, this.sheet);
    }

    public HSSFIconMultiStateFormatting getMultiStateFormatting() {
        return getMultiStateFormatting(false);
    }

    public HSSFIconMultiStateFormatting createMultiStateFormatting() {
        return getMultiStateFormatting(true);
    }

    private HSSFColorScaleFormatting getColorScaleFormatting(boolean create) {
        CFRule12Record cfRule12Record = getCFRule12Record(create);
        if (cfRule12Record == null) {
            return null;
        }
        if (cfRule12Record.getColorGradientFormatting() == null) {
            if (!create) {
                return null;
            }
            cfRule12Record.createColorGradientFormatting();
        }
        return new HSSFColorScaleFormatting(cfRule12Record, this.sheet);
    }

    public HSSFColorScaleFormatting getColorScaleFormatting() {
        return getColorScaleFormatting(false);
    }

    public HSSFColorScaleFormatting createColorScaleFormatting() {
        return getColorScaleFormatting(true);
    }

    public ConditionType getConditionType() {
        return ConditionType.forId(this.cfRuleRecord.getConditionType());
    }

    public byte getComparisonOperation() {
        return this.cfRuleRecord.getComparisonOperation();
    }

    public String getFormula1() {
        return toFormulaString(this.cfRuleRecord.getParsedExpression1());
    }

    public String getFormula2() {
        if (this.cfRuleRecord.getConditionType() == (byte) 1) {
            switch (this.cfRuleRecord.getComparisonOperation()) {
                case (byte) 1:
                case (byte) 2:
                    return toFormulaString(this.cfRuleRecord.getParsedExpression2());
            }
        }
        return null;
    }

    protected String toFormulaString(Ptg[] parsedExpression) {
        return toFormulaString(parsedExpression, this.workbook);
    }

    protected static String toFormulaString(Ptg[] parsedExpression, HSSFWorkbook workbook) {
        if (parsedExpression == null || parsedExpression.length == 0) {
            return null;
        }
        return HSSFFormulaParser.toFormulaString(workbook, parsedExpression);
    }
}
