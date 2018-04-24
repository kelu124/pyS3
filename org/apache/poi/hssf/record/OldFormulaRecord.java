package org.apache.poi.hssf.record;

import org.apache.poi.ss.formula.Formula;
import org.apache.poi.ss.formula.ptg.Ptg;
import org.apache.poi.ss.usermodel.CellType;

public final class OldFormulaRecord extends OldCellRecord {
    public static final short biff2_sid = (short) 6;
    public static final short biff3_sid = (short) 518;
    public static final short biff4_sid = (short) 1030;
    public static final short biff5_sid = (short) 6;
    private double field_4_value;
    private short field_5_options;
    private Formula field_6_parsed_expr;
    private SpecialCachedValue specialCachedValue;

    public OldFormulaRecord(RecordInputStream ris) {
        super(ris, ris.getSid() == (short) 6);
        if (isBiff2()) {
            this.field_4_value = ris.readDouble();
        } else {
            long valueLongBits = ris.readLong();
            this.specialCachedValue = SpecialCachedValue.create(valueLongBits);
            if (this.specialCachedValue == null) {
                this.field_4_value = Double.longBitsToDouble(valueLongBits);
            }
        }
        if (isBiff2()) {
            this.field_5_options = (short) ris.readUByte();
        } else {
            this.field_5_options = ris.readShort();
        }
        this.field_6_parsed_expr = Formula.read(ris.readShort(), ris, ris.available());
    }

    public int getCachedResultType() {
        if (this.specialCachedValue == null) {
            return CellType.NUMERIC.getCode();
        }
        return this.specialCachedValue.getValueType();
    }

    public boolean getCachedBooleanValue() {
        return this.specialCachedValue.getBooleanValue();
    }

    public int getCachedErrorValue() {
        return this.specialCachedValue.getErrorValue();
    }

    public double getValue() {
        return this.field_4_value;
    }

    public short getOptions() {
        return this.field_5_options;
    }

    public Ptg[] getParsedExpression() {
        return this.field_6_parsed_expr.getTokens();
    }

    public Formula getFormula() {
        return this.field_6_parsed_expr;
    }

    protected void appendValueText(StringBuilder sb) {
        sb.append("    .value       = ").append(getValue()).append("\n");
    }

    protected String getRecordName() {
        return "Old Formula";
    }
}
