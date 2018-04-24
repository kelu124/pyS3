package org.apache.poi.hssf.usermodel;

import org.apache.poi.hssf.record.CFRule12Record;
import org.apache.poi.hssf.record.cf.IconMultiStateThreshold;
import org.apache.poi.hssf.record.cf.Threshold;
import org.apache.poi.ss.usermodel.ConditionalFormattingThreshold;
import org.apache.poi.ss.usermodel.IconMultiStateFormatting;
import org.apache.poi.ss.usermodel.IconMultiStateFormatting.IconSet;

public final class HSSFIconMultiStateFormatting implements IconMultiStateFormatting {
    private final CFRule12Record cfRule12Record;
    private final org.apache.poi.hssf.record.cf.IconMultiStateFormatting iconFormatting = this.cfRule12Record.getMultiStateFormatting();
    private final HSSFSheet sheet;

    protected HSSFIconMultiStateFormatting(CFRule12Record cfRule12Record, HSSFSheet sheet) {
        this.sheet = sheet;
        this.cfRule12Record = cfRule12Record;
    }

    public IconSet getIconSet() {
        return this.iconFormatting.getIconSet();
    }

    public void setIconSet(IconSet set) {
        this.iconFormatting.setIconSet(set);
    }

    public boolean isIconOnly() {
        return this.iconFormatting.isIconOnly();
    }

    public void setIconOnly(boolean only) {
        this.iconFormatting.setIconOnly(only);
    }

    public boolean isReversed() {
        return this.iconFormatting.isReversed();
    }

    public void setReversed(boolean reversed) {
        this.iconFormatting.setReversed(reversed);
    }

    public HSSFConditionalFormattingThreshold[] getThresholds() {
        Threshold[] t = this.iconFormatting.getThresholds();
        HSSFConditionalFormattingThreshold[] ht = new HSSFConditionalFormattingThreshold[t.length];
        for (int i = 0; i < t.length; i++) {
            ht[i] = new HSSFConditionalFormattingThreshold(t[i], this.sheet);
        }
        return ht;
    }

    public void setThresholds(ConditionalFormattingThreshold[] thresholds) {
        Threshold[] t = new Threshold[thresholds.length];
        for (int i = 0; i < t.length; i++) {
            t[i] = ((HSSFConditionalFormattingThreshold) thresholds[i]).getThreshold();
        }
        this.iconFormatting.setThresholds(t);
    }

    public HSSFConditionalFormattingThreshold createThreshold() {
        return new HSSFConditionalFormattingThreshold(new IconMultiStateThreshold(), this.sheet);
    }
}
