package org.apache.poi.hssf.record.cf;

import java.util.Arrays;
import org.apache.poi.ss.formula.Formula;
import org.apache.poi.ss.formula.ptg.Ptg;
import org.apache.poi.ss.usermodel.ConditionalFormattingThreshold.RangeType;
import org.apache.poi.util.LittleEndianInput;
import org.apache.poi.util.LittleEndianOutput;

public abstract class Threshold {
    private Formula formula;
    private byte type;
    private Double value;

    protected Threshold() {
        this.type = (byte) RangeType.NUMBER.id;
        this.formula = Formula.create(null);
        this.value = Double.valueOf(0.0d);
    }

    protected Threshold(LittleEndianInput in) {
        this.type = in.readByte();
        short formulaLen = in.readShort();
        if (formulaLen > (short) 0) {
            this.formula = Formula.read(formulaLen, in);
        } else {
            this.formula = Formula.create(null);
        }
        if (formulaLen == (short) 0 && this.type != RangeType.MIN.id && this.type != RangeType.MAX.id) {
            this.value = Double.valueOf(in.readDouble());
        }
    }

    public byte getType() {
        return this.type;
    }

    public void setType(byte type) {
        this.type = type;
        if (type == RangeType.MIN.id || type == RangeType.MAX.id || type == RangeType.FORMULA.id) {
            this.value = null;
        } else if (this.value == null) {
            this.value = Double.valueOf(0.0d);
        }
    }

    public void setType(int type) {
        this.type = (byte) type;
    }

    protected Formula getFormula() {
        return this.formula;
    }

    public Ptg[] getParsedExpression() {
        return this.formula.getTokens();
    }

    public void setParsedExpression(Ptg[] ptgs) {
        this.formula = Formula.create(ptgs);
        if (ptgs.length > 0) {
            this.value = null;
        }
    }

    public Double getValue() {
        return this.value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public int getDataLength() {
        int len = this.formula.getEncodedSize() + 1;
        if (this.value != null) {
            return len + 8;
        }
        return len;
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("    [CF Threshold]\n");
        buffer.append("          .type    = ").append(Integer.toHexString(this.type)).append("\n");
        buffer.append("          .formula = ").append(Arrays.toString(this.formula.getTokens())).append("\n");
        buffer.append("          .value   = ").append(this.value).append("\n");
        buffer.append("    [/CF Threshold]\n");
        return buffer.toString();
    }

    public void copyTo(Threshold rec) {
        rec.type = this.type;
        rec.formula = this.formula;
        rec.value = this.value;
    }

    public void serialize(LittleEndianOutput out) {
        out.writeByte(this.type);
        if (this.formula.getTokens().length == 0) {
            out.writeShort(0);
        } else {
            this.formula.serialize(out);
        }
        if (this.value != null) {
            out.writeDouble(this.value.doubleValue());
        }
    }
}
