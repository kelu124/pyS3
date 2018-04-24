package org.apache.poi.hssf.usermodel;

import org.apache.poi.hssf.record.CFRule12Record;
import org.apache.poi.hssf.record.cf.ColorGradientFormatting;
import org.apache.poi.hssf.record.cf.ColorGradientThreshold;
import org.apache.poi.hssf.record.cf.Threshold;
import org.apache.poi.hssf.record.common.ExtendedColor;
import org.apache.poi.ss.usermodel.Color;
import org.apache.poi.ss.usermodel.ColorScaleFormatting;
import org.apache.poi.ss.usermodel.ConditionalFormattingThreshold;

public final class HSSFColorScaleFormatting implements ColorScaleFormatting {
    private final CFRule12Record cfRule12Record;
    private final ColorGradientFormatting colorFormatting = this.cfRule12Record.getColorGradientFormatting();
    private final HSSFSheet sheet;

    protected HSSFColorScaleFormatting(CFRule12Record cfRule12Record, HSSFSheet sheet) {
        this.sheet = sheet;
        this.cfRule12Record = cfRule12Record;
    }

    public int getNumControlPoints() {
        return this.colorFormatting.getNumControlPoints();
    }

    public void setNumControlPoints(int num) {
        this.colorFormatting.setNumControlPoints(num);
    }

    public HSSFExtendedColor[] getColors() {
        ExtendedColor[] colors = this.colorFormatting.getColors();
        HSSFExtendedColor[] hcolors = new HSSFExtendedColor[colors.length];
        for (int i = 0; i < colors.length; i++) {
            hcolors[i] = new HSSFExtendedColor(colors[i]);
        }
        return hcolors;
    }

    public void setColors(Color[] colors) {
        ExtendedColor[] cr = new ExtendedColor[colors.length];
        for (int i = 0; i < colors.length; i++) {
            cr[i] = ((HSSFExtendedColor) colors[i]).getExtendedColor();
        }
        this.colorFormatting.setColors(cr);
    }

    public HSSFConditionalFormattingThreshold[] getThresholds() {
        Threshold[] t = this.colorFormatting.getThresholds();
        HSSFConditionalFormattingThreshold[] ht = new HSSFConditionalFormattingThreshold[t.length];
        for (int i = 0; i < t.length; i++) {
            ht[i] = new HSSFConditionalFormattingThreshold(t[i], this.sheet);
        }
        return ht;
    }

    public void setThresholds(ConditionalFormattingThreshold[] thresholds) {
        ColorGradientThreshold[] t = new ColorGradientThreshold[thresholds.length];
        for (int i = 0; i < t.length; i++) {
            t[i] = (ColorGradientThreshold) thresholds[i].getThreshold();
        }
        this.colorFormatting.setThresholds(t);
    }

    public HSSFConditionalFormattingThreshold createThreshold() {
        return new HSSFConditionalFormattingThreshold(new ColorGradientThreshold(), this.sheet);
    }
}
