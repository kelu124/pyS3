package org.apache.poi.hssf.record.chart;

import org.apache.poi.hssf.record.RecordInputStream;
import org.apache.poi.hssf.record.StandardRecord;
import org.apache.poi.util.BitField;
import org.apache.poi.util.BitFieldFactory;
import org.apache.poi.util.HexDump;
import org.apache.poi.util.LittleEndianOutput;

public final class ValueRangeRecord extends StandardRecord {
    private static final BitField automaticCategoryCrossing = BitFieldFactory.getInstance(16);
    private static final BitField automaticMajor = BitFieldFactory.getInstance(4);
    private static final BitField automaticMaximum = BitFieldFactory.getInstance(2);
    private static final BitField automaticMinimum = BitFieldFactory.getInstance(1);
    private static final BitField automaticMinor = BitFieldFactory.getInstance(8);
    private static final BitField crossCategoryAxisAtMaximum = BitFieldFactory.getInstance(128);
    private static final BitField logarithmicScale = BitFieldFactory.getInstance(32);
    private static final BitField reserved = BitFieldFactory.getInstance(256);
    public static final short sid = (short) 4127;
    private static final BitField valuesInReverse = BitFieldFactory.getInstance(64);
    private double field_1_minimumAxisValue;
    private double field_2_maximumAxisValue;
    private double field_3_majorIncrement;
    private double field_4_minorIncrement;
    private double field_5_categoryAxisCross;
    private short field_6_options;

    public ValueRangeRecord(RecordInputStream in) {
        this.field_1_minimumAxisValue = in.readDouble();
        this.field_2_maximumAxisValue = in.readDouble();
        this.field_3_majorIncrement = in.readDouble();
        this.field_4_minorIncrement = in.readDouble();
        this.field_5_categoryAxisCross = in.readDouble();
        this.field_6_options = in.readShort();
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[VALUERANGE]\n");
        buffer.append("    .minimumAxisValue     = ").append(" (").append(getMinimumAxisValue()).append(" )");
        buffer.append(System.getProperty("line.separator"));
        buffer.append("    .maximumAxisValue     = ").append(" (").append(getMaximumAxisValue()).append(" )");
        buffer.append(System.getProperty("line.separator"));
        buffer.append("    .majorIncrement       = ").append(" (").append(getMajorIncrement()).append(" )");
        buffer.append(System.getProperty("line.separator"));
        buffer.append("    .minorIncrement       = ").append(" (").append(getMinorIncrement()).append(" )");
        buffer.append(System.getProperty("line.separator"));
        buffer.append("    .categoryAxisCross    = ").append(" (").append(getCategoryAxisCross()).append(" )");
        buffer.append(System.getProperty("line.separator"));
        buffer.append("    .options              = ").append("0x").append(HexDump.toHex(getOptions())).append(" (").append(getOptions()).append(" )");
        buffer.append(System.getProperty("line.separator"));
        buffer.append("         .automaticMinimum         = ").append(isAutomaticMinimum()).append('\n');
        buffer.append("         .automaticMaximum         = ").append(isAutomaticMaximum()).append('\n');
        buffer.append("         .automaticMajor           = ").append(isAutomaticMajor()).append('\n');
        buffer.append("         .automaticMinor           = ").append(isAutomaticMinor()).append('\n');
        buffer.append("         .automaticCategoryCrossing     = ").append(isAutomaticCategoryCrossing()).append('\n');
        buffer.append("         .logarithmicScale         = ").append(isLogarithmicScale()).append('\n');
        buffer.append("         .valuesInReverse          = ").append(isValuesInReverse()).append('\n');
        buffer.append("         .crossCategoryAxisAtMaximum     = ").append(isCrossCategoryAxisAtMaximum()).append('\n');
        buffer.append("         .reserved                 = ").append(isReserved()).append('\n');
        buffer.append("[/VALUERANGE]\n");
        return buffer.toString();
    }

    public void serialize(LittleEndianOutput out) {
        out.writeDouble(this.field_1_minimumAxisValue);
        out.writeDouble(this.field_2_maximumAxisValue);
        out.writeDouble(this.field_3_majorIncrement);
        out.writeDouble(this.field_4_minorIncrement);
        out.writeDouble(this.field_5_categoryAxisCross);
        out.writeShort(this.field_6_options);
    }

    protected int getDataSize() {
        return 42;
    }

    public short getSid() {
        return sid;
    }

    public Object clone() {
        ValueRangeRecord rec = new ValueRangeRecord();
        rec.field_1_minimumAxisValue = this.field_1_minimumAxisValue;
        rec.field_2_maximumAxisValue = this.field_2_maximumAxisValue;
        rec.field_3_majorIncrement = this.field_3_majorIncrement;
        rec.field_4_minorIncrement = this.field_4_minorIncrement;
        rec.field_5_categoryAxisCross = this.field_5_categoryAxisCross;
        rec.field_6_options = this.field_6_options;
        return rec;
    }

    public double getMinimumAxisValue() {
        return this.field_1_minimumAxisValue;
    }

    public void setMinimumAxisValue(double field_1_minimumAxisValue) {
        this.field_1_minimumAxisValue = field_1_minimumAxisValue;
    }

    public double getMaximumAxisValue() {
        return this.field_2_maximumAxisValue;
    }

    public void setMaximumAxisValue(double field_2_maximumAxisValue) {
        this.field_2_maximumAxisValue = field_2_maximumAxisValue;
    }

    public double getMajorIncrement() {
        return this.field_3_majorIncrement;
    }

    public void setMajorIncrement(double field_3_majorIncrement) {
        this.field_3_majorIncrement = field_3_majorIncrement;
    }

    public double getMinorIncrement() {
        return this.field_4_minorIncrement;
    }

    public void setMinorIncrement(double field_4_minorIncrement) {
        this.field_4_minorIncrement = field_4_minorIncrement;
    }

    public double getCategoryAxisCross() {
        return this.field_5_categoryAxisCross;
    }

    public void setCategoryAxisCross(double field_5_categoryAxisCross) {
        this.field_5_categoryAxisCross = field_5_categoryAxisCross;
    }

    public short getOptions() {
        return this.field_6_options;
    }

    public void setOptions(short field_6_options) {
        this.field_6_options = field_6_options;
    }

    public void setAutomaticMinimum(boolean value) {
        this.field_6_options = automaticMinimum.setShortBoolean(this.field_6_options, value);
    }

    public boolean isAutomaticMinimum() {
        return automaticMinimum.isSet(this.field_6_options);
    }

    public void setAutomaticMaximum(boolean value) {
        this.field_6_options = automaticMaximum.setShortBoolean(this.field_6_options, value);
    }

    public boolean isAutomaticMaximum() {
        return automaticMaximum.isSet(this.field_6_options);
    }

    public void setAutomaticMajor(boolean value) {
        this.field_6_options = automaticMajor.setShortBoolean(this.field_6_options, value);
    }

    public boolean isAutomaticMajor() {
        return automaticMajor.isSet(this.field_6_options);
    }

    public void setAutomaticMinor(boolean value) {
        this.field_6_options = automaticMinor.setShortBoolean(this.field_6_options, value);
    }

    public boolean isAutomaticMinor() {
        return automaticMinor.isSet(this.field_6_options);
    }

    public void setAutomaticCategoryCrossing(boolean value) {
        this.field_6_options = automaticCategoryCrossing.setShortBoolean(this.field_6_options, value);
    }

    public boolean isAutomaticCategoryCrossing() {
        return automaticCategoryCrossing.isSet(this.field_6_options);
    }

    public void setLogarithmicScale(boolean value) {
        this.field_6_options = logarithmicScale.setShortBoolean(this.field_6_options, value);
    }

    public boolean isLogarithmicScale() {
        return logarithmicScale.isSet(this.field_6_options);
    }

    public void setValuesInReverse(boolean value) {
        this.field_6_options = valuesInReverse.setShortBoolean(this.field_6_options, value);
    }

    public boolean isValuesInReverse() {
        return valuesInReverse.isSet(this.field_6_options);
    }

    public void setCrossCategoryAxisAtMaximum(boolean value) {
        this.field_6_options = crossCategoryAxisAtMaximum.setShortBoolean(this.field_6_options, value);
    }

    public boolean isCrossCategoryAxisAtMaximum() {
        return crossCategoryAxisAtMaximum.isSet(this.field_6_options);
    }

    public void setReserved(boolean value) {
        this.field_6_options = reserved.setShortBoolean(this.field_6_options, value);
    }

    public boolean isReserved() {
        return reserved.isSet(this.field_6_options);
    }
}
