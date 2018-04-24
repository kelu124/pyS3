package org.apache.poi.hssf.record;

import java.util.Arrays;
import org.apache.poi.hssf.record.cf.ColorGradientFormatting;
import org.apache.poi.hssf.record.cf.ColorGradientThreshold;
import org.apache.poi.hssf.record.cf.DataBarFormatting;
import org.apache.poi.hssf.record.cf.DataBarThreshold;
import org.apache.poi.hssf.record.cf.IconMultiStateFormatting;
import org.apache.poi.hssf.record.cf.IconMultiStateThreshold;
import org.apache.poi.hssf.record.cf.Threshold;
import org.apache.poi.hssf.record.common.ExtendedColor;
import org.apache.poi.hssf.record.common.FtrHeader;
import org.apache.poi.hssf.record.common.FutureRecord;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.formula.Formula;
import org.apache.poi.ss.formula.ptg.Ptg;
import org.apache.poi.ss.usermodel.ConditionalFormattingThreshold.RangeType;
import org.apache.poi.ss.usermodel.IconMultiStateFormatting.IconSet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.util.HexDump;
import org.apache.poi.util.LittleEndianOutput;

public final class CFRule12Record extends CFRuleBase implements FutureRecord, Cloneable {
    public static final short sid = (short) 2170;
    private ColorGradientFormatting color_gradient;
    private DataBarFormatting data_bar;
    private byte[] ext_formatting_data;
    private int ext_formatting_length;
    private byte ext_opts;
    private byte[] filter_data;
    private Formula formula_scale;
    private FtrHeader futureHeader;
    private IconMultiStateFormatting multistate;
    private int priority;
    private byte template_param_length;
    private byte[] template_params;
    private int template_type;

    private CFRule12Record(byte conditionType, byte comparisonOperation) {
        super(conditionType, comparisonOperation);
        setDefaults();
    }

    private CFRule12Record(byte conditionType, byte comparisonOperation, Ptg[] formula1, Ptg[] formula2, Ptg[] formulaScale) {
        super(conditionType, comparisonOperation, formula1, formula2);
        setDefaults();
        this.formula_scale = Formula.create(formulaScale);
    }

    private void setDefaults() {
        this.futureHeader = new FtrHeader();
        this.futureHeader.setRecordType(sid);
        this.ext_formatting_length = 0;
        this.ext_formatting_data = new byte[4];
        this.formula_scale = Formula.create(Ptg.EMPTY_PTG_ARRAY);
        this.ext_opts = (byte) 0;
        this.priority = 0;
        this.template_type = getConditionType();
        this.template_param_length = (byte) 16;
        this.template_params = new byte[this.template_param_length];
    }

    public static CFRule12Record create(HSSFSheet sheet, String formulaText) {
        return new CFRule12Record((byte) 2, (byte) 0, CFRuleBase.parseFormula(formulaText, sheet), null, null);
    }

    public static CFRule12Record create(HSSFSheet sheet, byte comparisonOperation, String formulaText1, String formulaText2) {
        return new CFRule12Record((byte) 1, comparisonOperation, CFRuleBase.parseFormula(formulaText1, sheet), CFRuleBase.parseFormula(formulaText2, sheet), null);
    }

    public static CFRule12Record create(HSSFSheet sheet, byte comparisonOperation, String formulaText1, String formulaText2, String formulaTextScale) {
        return new CFRule12Record((byte) 1, comparisonOperation, CFRuleBase.parseFormula(formulaText1, sheet), CFRuleBase.parseFormula(formulaText2, sheet), CFRuleBase.parseFormula(formulaTextScale, sheet));
    }

    public static CFRule12Record create(HSSFSheet sheet, ExtendedColor color) {
        CFRule12Record r = new CFRule12Record((byte) 4, (byte) 0);
        DataBarFormatting dbf = r.createDataBarFormatting();
        dbf.setColor(color);
        dbf.setPercentMin((byte) 0);
        dbf.setPercentMax((byte) 100);
        DataBarThreshold min = new DataBarThreshold();
        min.setType(RangeType.MIN.id);
        dbf.setThresholdMin(min);
        DataBarThreshold max = new DataBarThreshold();
        max.setType(RangeType.MAX.id);
        dbf.setThresholdMax(max);
        return r;
    }

    public static CFRule12Record create(HSSFSheet sheet, IconSet iconSet) {
        Threshold[] ts = new Threshold[iconSet.num];
        for (int i = 0; i < ts.length; i++) {
            ts[i] = new IconMultiStateThreshold();
        }
        CFRule12Record r = new CFRule12Record((byte) 6, (byte) 0);
        IconMultiStateFormatting imf = r.createMultiStateFormatting();
        imf.setIconSet(iconSet);
        imf.setThresholds(ts);
        return r;
    }

    public static CFRule12Record createColorScale(HSSFSheet sheet) {
        ExtendedColor[] colors = new ExtendedColor[3];
        ColorGradientThreshold[] ts = new ColorGradientThreshold[3];
        for (int i = 0; i < ts.length; i++) {
            ts[i] = new ColorGradientThreshold();
            colors[i] = new ExtendedColor();
        }
        CFRule12Record r = new CFRule12Record((byte) 3, (byte) 0);
        ColorGradientFormatting cgf = r.createColorGradientFormatting();
        cgf.setNumControlPoints(3);
        cgf.setThresholds(ts);
        cgf.setColors(colors);
        return r;
    }

    public CFRule12Record(RecordInputStream in) {
        this.futureHeader = new FtrHeader(in);
        setConditionType(in.readByte());
        setComparisonOperation(in.readByte());
        int field_3_formula1_len = in.readUShort();
        int field_4_formula2_len = in.readUShort();
        this.ext_formatting_length = in.readInt();
        this.ext_formatting_data = new byte[0];
        if (this.ext_formatting_length == 0) {
            in.readUShort();
        } else {
            int len = readFormatOptions(in);
            if (len < this.ext_formatting_length) {
                this.ext_formatting_data = new byte[(this.ext_formatting_length - len)];
                in.readFully(this.ext_formatting_data);
            }
        }
        setFormula1(Formula.read(field_3_formula1_len, in));
        setFormula2(Formula.read(field_4_formula2_len, in));
        this.formula_scale = Formula.read(in.readUShort(), in);
        this.ext_opts = in.readByte();
        this.priority = in.readUShort();
        this.template_type = in.readUShort();
        this.template_param_length = in.readByte();
        if (this.template_param_length == (byte) 0 || this.template_param_length == (byte) 16) {
            this.template_params = new byte[this.template_param_length];
            in.readFully(this.template_params);
        } else {
            logger.log(5, new Object[]{"CF Rule v12 template params length should be 0 or 16, found " + this.template_param_length});
            in.readRemainder();
        }
        byte type = getConditionType();
        if (type == (byte) 3) {
            this.color_gradient = new ColorGradientFormatting(in);
        } else if (type == (byte) 4) {
            this.data_bar = new DataBarFormatting(in);
        } else if (type == (byte) 5) {
            this.filter_data = in.readRemainder();
        } else if (type == (byte) 6) {
            this.multistate = new IconMultiStateFormatting(in);
        }
    }

    public boolean containsDataBarBlock() {
        return this.data_bar != null;
    }

    public DataBarFormatting getDataBarFormatting() {
        return this.data_bar;
    }

    public DataBarFormatting createDataBarFormatting() {
        if (this.data_bar != null) {
            return this.data_bar;
        }
        setConditionType((byte) 4);
        this.data_bar = new DataBarFormatting();
        return this.data_bar;
    }

    public boolean containsMultiStateBlock() {
        return this.multistate != null;
    }

    public IconMultiStateFormatting getMultiStateFormatting() {
        return this.multistate;
    }

    public IconMultiStateFormatting createMultiStateFormatting() {
        if (this.multistate != null) {
            return this.multistate;
        }
        setConditionType((byte) 6);
        this.multistate = new IconMultiStateFormatting();
        return this.multistate;
    }

    public boolean containsColorGradientBlock() {
        return this.color_gradient != null;
    }

    public ColorGradientFormatting getColorGradientFormatting() {
        return this.color_gradient;
    }

    public ColorGradientFormatting createColorGradientFormatting() {
        if (this.color_gradient != null) {
            return this.color_gradient;
        }
        setConditionType((byte) 3);
        this.color_gradient = new ColorGradientFormatting();
        return this.color_gradient;
    }

    public Ptg[] getParsedExpressionScale() {
        return this.formula_scale.getTokens();
    }

    public void setParsedExpressionScale(Ptg[] ptgs) {
        this.formula_scale = Formula.create(ptgs);
    }

    public short getSid() {
        return sid;
    }

    public void serialize(LittleEndianOutput out) {
        this.futureHeader.serialize(out);
        int formula1Len = CFRuleBase.getFormulaSize(getFormula1());
        int formula2Len = CFRuleBase.getFormulaSize(getFormula2());
        out.writeByte(getConditionType());
        out.writeByte(getComparisonOperation());
        out.writeShort(formula1Len);
        out.writeShort(formula2Len);
        if (this.ext_formatting_length == 0) {
            out.writeInt(0);
            out.writeShort(0);
        } else {
            out.writeInt(this.ext_formatting_length);
            serializeFormattingBlock(out);
            out.write(this.ext_formatting_data);
        }
        getFormula1().serializeTokens(out);
        getFormula2().serializeTokens(out);
        out.writeShort(CFRuleBase.getFormulaSize(this.formula_scale));
        this.formula_scale.serializeTokens(out);
        out.writeByte(this.ext_opts);
        out.writeShort(this.priority);
        out.writeShort(this.template_type);
        out.writeByte(this.template_param_length);
        out.write(this.template_params);
        byte type = getConditionType();
        if (type == (byte) 3) {
            this.color_gradient.serialize(out);
        } else if (type == (byte) 4) {
            this.data_bar.serialize(out);
        } else if (type == (byte) 5) {
            out.write(this.filter_data);
        } else if (type == (byte) 6) {
            this.multistate.serialize(out);
        }
    }

    protected int getDataSize() {
        int len = FtrHeader.getDataSize() + 6;
        if (this.ext_formatting_length == 0) {
            len += 6;
        } else {
            len += (getFormattingBlockSize() + 4) + this.ext_formatting_data.length;
        }
        len = (((len + CFRuleBase.getFormulaSize(getFormula1())) + CFRuleBase.getFormulaSize(getFormula2())) + (CFRuleBase.getFormulaSize(this.formula_scale) + 2)) + (this.template_params.length + 6);
        byte type = getConditionType();
        if (type == (byte) 3) {
            return len + this.color_gradient.getDataLength();
        }
        if (type == (byte) 4) {
            return len + this.data_bar.getDataLength();
        }
        if (type == (byte) 5) {
            return len + this.filter_data.length;
        }
        if (type == (byte) 6) {
            return len + this.multistate.getDataLength();
        }
        return len;
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[CFRULE12]\n");
        buffer.append("    .condition_type=").append(getConditionType()).append("\n");
        buffer.append("    .dxfn12_length =0x").append(Integer.toHexString(this.ext_formatting_length)).append("\n");
        buffer.append("    .option_flags  =0x").append(Integer.toHexString(getOptions())).append("\n");
        if (containsFontFormattingBlock()) {
            buffer.append(this._fontFormatting.toString()).append("\n");
        }
        if (containsBorderFormattingBlock()) {
            buffer.append(this._borderFormatting.toString()).append("\n");
        }
        if (containsPatternFormattingBlock()) {
            buffer.append(this._patternFormatting.toString()).append("\n");
        }
        buffer.append("    .dxfn12_ext=").append(HexDump.toHex(this.ext_formatting_data)).append("\n");
        buffer.append("    .formula_1 =").append(Arrays.toString(getFormula1().getTokens())).append("\n");
        buffer.append("    .formula_2 =").append(Arrays.toString(getFormula2().getTokens())).append("\n");
        buffer.append("    .formula_S =").append(Arrays.toString(this.formula_scale.getTokens())).append("\n");
        buffer.append("    .ext_opts  =").append(this.ext_opts).append("\n");
        buffer.append("    .priority  =").append(this.priority).append("\n");
        buffer.append("    .template_type  =").append(this.template_type).append("\n");
        buffer.append("    .template_params=").append(HexDump.toHex(this.template_params)).append("\n");
        buffer.append("    .filter_data    =").append(HexDump.toHex(this.filter_data)).append("\n");
        if (this.color_gradient != null) {
            buffer.append(this.color_gradient);
        }
        if (this.multistate != null) {
            buffer.append(this.multistate);
        }
        if (this.data_bar != null) {
            buffer.append(this.data_bar);
        }
        buffer.append("[/CFRULE12]\n");
        return buffer.toString();
    }

    public CFRule12Record clone() {
        CFRule12Record rec = new CFRule12Record(getConditionType(), getComparisonOperation());
        rec.futureHeader.setAssociatedRange(this.futureHeader.getAssociatedRange().copy());
        super.copyTo(rec);
        rec.ext_formatting_length = this.ext_formatting_length;
        rec.ext_formatting_data = new byte[this.ext_formatting_length];
        System.arraycopy(this.ext_formatting_data, 0, rec.ext_formatting_data, 0, this.ext_formatting_length);
        rec.formula_scale = this.formula_scale.copy();
        rec.ext_opts = this.ext_opts;
        rec.priority = this.priority;
        rec.template_type = this.template_type;
        rec.template_param_length = this.template_param_length;
        rec.template_params = new byte[this.template_param_length];
        System.arraycopy(this.template_params, 0, rec.template_params, 0, this.template_param_length);
        if (this.color_gradient != null) {
            rec.color_gradient = (ColorGradientFormatting) this.color_gradient.clone();
        }
        if (this.multistate != null) {
            rec.multistate = (IconMultiStateFormatting) this.multistate.clone();
        }
        if (this.data_bar != null) {
            rec.data_bar = (DataBarFormatting) this.data_bar.clone();
        }
        if (this.filter_data != null) {
            rec.filter_data = new byte[this.filter_data.length];
            System.arraycopy(this.filter_data, 0, rec.filter_data, 0, this.filter_data.length);
        }
        return rec;
    }

    public short getFutureRecordType() {
        return this.futureHeader.getRecordType();
    }

    public FtrHeader getFutureHeader() {
        return this.futureHeader;
    }

    public CellRangeAddress getAssociatedRange() {
        return this.futureHeader.getAssociatedRange();
    }
}
