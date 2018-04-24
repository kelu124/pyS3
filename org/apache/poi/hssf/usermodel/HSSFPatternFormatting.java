package org.apache.poi.hssf.usermodel;

import org.apache.poi.hssf.record.CFRuleBase;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Color;
import org.apache.poi.ss.usermodel.PatternFormatting;

public class HSSFPatternFormatting implements PatternFormatting {
    private final CFRuleBase cfRuleRecord;
    private final org.apache.poi.hssf.record.cf.PatternFormatting patternFormatting;
    private final HSSFWorkbook workbook;

    protected HSSFPatternFormatting(CFRuleBase cfRuleRecord, HSSFWorkbook workbook) {
        this.workbook = workbook;
        this.cfRuleRecord = cfRuleRecord;
        this.patternFormatting = cfRuleRecord.getPatternFormatting();
    }

    protected org.apache.poi.hssf.record.cf.PatternFormatting getPatternFormattingBlock() {
        return this.patternFormatting;
    }

    public HSSFColor getFillBackgroundColorColor() {
        return this.workbook.getCustomPalette().getColor(getFillBackgroundColor());
    }

    public HSSFColor getFillForegroundColorColor() {
        return this.workbook.getCustomPalette().getColor(getFillForegroundColor());
    }

    public short getFillBackgroundColor() {
        return (short) this.patternFormatting.getFillBackgroundColor();
    }

    public short getFillForegroundColor() {
        return (short) this.patternFormatting.getFillForegroundColor();
    }

    public short getFillPattern() {
        return (short) this.patternFormatting.getFillPattern();
    }

    public void setFillBackgroundColor(Color bg) {
        HSSFColor hcolor = HSSFColor.toHSSFColor(bg);
        if (hcolor == null) {
            setFillBackgroundColor((short) 0);
        } else {
            setFillBackgroundColor(hcolor.getIndex());
        }
    }

    public void setFillForegroundColor(Color fg) {
        HSSFColor hcolor = HSSFColor.toHSSFColor(fg);
        if (hcolor == null) {
            setFillForegroundColor((short) 0);
        } else {
            setFillForegroundColor(hcolor.getIndex());
        }
    }

    public void setFillBackgroundColor(short bg) {
        this.patternFormatting.setFillBackgroundColor(bg);
        if (bg != (short) 0) {
            this.cfRuleRecord.setPatternBackgroundColorModified(true);
        }
    }

    public void setFillForegroundColor(short fg) {
        this.patternFormatting.setFillForegroundColor(fg);
        if (fg != (short) 0) {
            this.cfRuleRecord.setPatternColorModified(true);
        }
    }

    public void setFillPattern(short fp) {
        this.patternFormatting.setFillPattern(fp);
        if (fp != (short) 0) {
            this.cfRuleRecord.setPatternStyleModified(true);
        }
    }
}
