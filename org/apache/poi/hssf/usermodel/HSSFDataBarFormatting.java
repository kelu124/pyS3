package org.apache.poi.hssf.usermodel;

import org.apache.poi.hssf.record.CFRule12Record;
import org.apache.poi.hssf.record.cf.DataBarThreshold;
import org.apache.poi.ss.usermodel.Color;
import org.apache.poi.ss.usermodel.DataBarFormatting;

public final class HSSFDataBarFormatting implements DataBarFormatting {
    private final CFRule12Record cfRule12Record;
    private final org.apache.poi.hssf.record.cf.DataBarFormatting databarFormatting = this.cfRule12Record.getDataBarFormatting();
    private final HSSFSheet sheet;

    protected HSSFDataBarFormatting(CFRule12Record cfRule12Record, HSSFSheet sheet) {
        this.sheet = sheet;
        this.cfRule12Record = cfRule12Record;
    }

    public boolean isLeftToRight() {
        return !this.databarFormatting.isReversed();
    }

    public void setLeftToRight(boolean ltr) {
        this.databarFormatting.setReversed(!ltr);
    }

    public int getWidthMin() {
        return this.databarFormatting.getPercentMin();
    }

    public void setWidthMin(int width) {
        this.databarFormatting.setPercentMin((byte) width);
    }

    public int getWidthMax() {
        return this.databarFormatting.getPercentMax();
    }

    public void setWidthMax(int width) {
        this.databarFormatting.setPercentMax((byte) width);
    }

    public HSSFExtendedColor getColor() {
        return new HSSFExtendedColor(this.databarFormatting.getColor());
    }

    public void setColor(Color color) {
        this.databarFormatting.setColor(((HSSFExtendedColor) color).getExtendedColor());
    }

    public HSSFConditionalFormattingThreshold getMinThreshold() {
        return new HSSFConditionalFormattingThreshold(this.databarFormatting.getThresholdMin(), this.sheet);
    }

    public HSSFConditionalFormattingThreshold getMaxThreshold() {
        return new HSSFConditionalFormattingThreshold(this.databarFormatting.getThresholdMax(), this.sheet);
    }

    public boolean isIconOnly() {
        return this.databarFormatting.isIconOnly();
    }

    public void setIconOnly(boolean only) {
        this.databarFormatting.setIconOnly(only);
    }

    public HSSFConditionalFormattingThreshold createThreshold() {
        return new HSSFConditionalFormattingThreshold(new DataBarThreshold(), this.sheet);
    }
}
