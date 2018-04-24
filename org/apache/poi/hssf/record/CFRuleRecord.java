package org.apache.poi.hssf.record;

import java.util.Arrays;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.formula.Formula;
import org.apache.poi.ss.formula.ptg.Ptg;
import org.apache.poi.util.LittleEndianOutput;

public final class CFRuleRecord extends CFRuleBase implements Cloneable {
    public static final short sid = (short) 433;

    private CFRuleRecord(byte conditionType, byte comparisonOperation) {
        super(conditionType, comparisonOperation);
        setDefaults();
    }

    private CFRuleRecord(byte conditionType, byte comparisonOperation, Ptg[] formula1, Ptg[] formula2) {
        super(conditionType, comparisonOperation, formula1, formula2);
        setDefaults();
    }

    private void setDefaults() {
        this.formatting_options = modificationBits.setValue(this.formatting_options, -1);
        this.formatting_options = fmtBlockBits.setValue(this.formatting_options, 0);
        this.formatting_options = undocumented.clear(this.formatting_options);
        this.formatting_not_used = (short) -32766;
        this._fontFormatting = null;
        this._borderFormatting = null;
        this._patternFormatting = null;
    }

    public static CFRuleRecord create(HSSFSheet sheet, String formulaText) {
        return new CFRuleRecord((byte) 2, (byte) 0, CFRuleBase.parseFormula(formulaText, sheet), null);
    }

    public static CFRuleRecord create(HSSFSheet sheet, byte comparisonOperation, String formulaText1, String formulaText2) {
        return new CFRuleRecord((byte) 1, comparisonOperation, CFRuleBase.parseFormula(formulaText1, sheet), CFRuleBase.parseFormula(formulaText2, sheet));
    }

    public CFRuleRecord(RecordInputStream in) {
        setConditionType(in.readByte());
        setComparisonOperation(in.readByte());
        int field_3_formula1_len = in.readUShort();
        int field_4_formula2_len = in.readUShort();
        readFormatOptions(in);
        setFormula1(Formula.read(field_3_formula1_len, in));
        setFormula2(Formula.read(field_4_formula2_len, in));
    }

    public short getSid() {
        return sid;
    }

    public void serialize(LittleEndianOutput out) {
        int formula1Len = CFRuleBase.getFormulaSize(getFormula1());
        int formula2Len = CFRuleBase.getFormulaSize(getFormula2());
        out.writeByte(getConditionType());
        out.writeByte(getComparisonOperation());
        out.writeShort(formula1Len);
        out.writeShort(formula2Len);
        serializeFormattingBlock(out);
        getFormula1().serializeTokens(out);
        getFormula2().serializeTokens(out);
    }

    protected int getDataSize() {
        return ((getFormattingBlockSize() + 6) + CFRuleBase.getFormulaSize(getFormula1())) + CFRuleBase.getFormulaSize(getFormula2());
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[CFRULE]\n");
        buffer.append("    .condition_type   =").append(getConditionType()).append("\n");
        buffer.append("    OPTION FLAGS=0x").append(Integer.toHexString(getOptions())).append("\n");
        if (containsFontFormattingBlock()) {
            buffer.append(this._fontFormatting.toString()).append("\n");
        }
        if (containsBorderFormattingBlock()) {
            buffer.append(this._borderFormatting.toString()).append("\n");
        }
        if (containsPatternFormattingBlock()) {
            buffer.append(this._patternFormatting.toString()).append("\n");
        }
        buffer.append("    Formula 1 =").append(Arrays.toString(getFormula1().getTokens())).append("\n");
        buffer.append("    Formula 2 =").append(Arrays.toString(getFormula2().getTokens())).append("\n");
        buffer.append("[/CFRULE]\n");
        return buffer.toString();
    }

    public CFRuleRecord clone() {
        CFRuleRecord rec = new CFRuleRecord(getConditionType(), getComparisonOperation());
        super.copyTo(rec);
        return rec;
    }
}
