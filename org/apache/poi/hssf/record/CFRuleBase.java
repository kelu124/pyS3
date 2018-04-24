package org.apache.poi.hssf.record;

import org.apache.poi.hssf.model.HSSFFormulaParser;
import org.apache.poi.hssf.record.cf.BorderFormatting;
import org.apache.poi.hssf.record.cf.FontFormatting;
import org.apache.poi.hssf.record.cf.PatternFormatting;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.formula.Formula;
import org.apache.poi.ss.formula.FormulaType;
import org.apache.poi.ss.formula.ptg.Ptg;
import org.apache.poi.util.BitField;
import org.apache.poi.util.BitFieldFactory;
import org.apache.poi.util.LittleEndianOutput;
import org.apache.poi.util.POILogFactory;
import org.apache.poi.util.POILogger;

public abstract class CFRuleBase extends StandardRecord implements Cloneable {
    public static final byte CONDITION_TYPE_CELL_VALUE_IS = (byte) 1;
    public static final byte CONDITION_TYPE_COLOR_SCALE = (byte) 3;
    public static final byte CONDITION_TYPE_DATA_BAR = (byte) 4;
    public static final byte CONDITION_TYPE_FILTER = (byte) 5;
    public static final byte CONDITION_TYPE_FORMULA = (byte) 2;
    public static final byte CONDITION_TYPE_ICON_SET = (byte) 6;
    public static final int TEMPLATE_ABOVE_AVERAGE = 25;
    public static final int TEMPLATE_ABOVE_OR_EQUAL_TO_AVERAGE = 29;
    public static final int TEMPLATE_BELOW_AVERAGE = 26;
    public static final int TEMPLATE_BELOW_OR_EQUAL_TO_AVERAGE = 30;
    public static final int TEMPLATE_CELL_VALUE = 0;
    public static final int TEMPLATE_COLOR_SCALE_FORMATTING = 2;
    public static final int TEMPLATE_CONTAINS_BLANKS = 9;
    public static final int TEMPLATE_CONTAINS_ERRORS = 11;
    public static final int TEMPLATE_CONTAINS_NO_BLANKS = 10;
    public static final int TEMPLATE_CONTAINS_NO_ERRORS = 12;
    public static final int TEMPLATE_CONTAINS_TEXT = 8;
    public static final int TEMPLATE_DATA_BAR_FORMATTING = 3;
    public static final int TEMPLATE_DUPLICATE_VALUES = 27;
    public static final int TEMPLATE_FILTER = 5;
    public static final int TEMPLATE_FORMULA = 1;
    public static final int TEMPLATE_ICON_SET_FORMATTING = 4;
    public static final int TEMPLATE_LAST_7_DAYS = 18;
    public static final int TEMPLATE_LAST_MONTH = 19;
    public static final int TEMPLATE_LAST_WEEK = 23;
    public static final int TEMPLATE_NEXT_MONTH = 20;
    public static final int TEMPLATE_NEXT_WEEK = 22;
    public static final int TEMPLATE_THIS_MONTH = 24;
    public static final int TEMPLATE_THIS_WEEK = 21;
    public static final int TEMPLATE_TODAY = 15;
    public static final int TEMPLATE_TOMORROW = 16;
    public static final int TEMPLATE_UNIQUE_VALUES = 7;
    public static final int TEMPLATE_YESTERDAY = 17;
    static final BitField align = bf(134217728);
    static final BitField alignHor = bf(1);
    static final BitField alignIndent = bf(32);
    static final BitField alignJustLast = bf(16);
    static final BitField alignRot = bf(8);
    static final BitField alignShrin = bf(64);
    static final BitField alignTextDir = bf(Integer.MIN_VALUE);
    static final BitField alignVer = bf(2);
    static final BitField alignWrap = bf(4);
    static final BitField bord = bf(268435456);
    static final BitField bordBlTr = bf(32768);
    static final BitField bordBot = bf(8192);
    static final BitField bordLeft = bf(1024);
    static final BitField bordRight = bf(2048);
    static final BitField bordTlBr = bf(16384);
    static final BitField bordTop = bf(4096);
    static final BitField fmtBlockBits = bf(2080374784);
    static final BitField font = bf(67108864);
    protected static final POILogger logger = POILogFactory.getLogger(CFRuleBase.class);
    static final BitField mergeCell = bf(128);
    static final BitField modificationBits = bf(4194303);
    static final BitField notUsed2 = bf(3670016);
    static final BitField patt = bf(536870912);
    static final BitField pattBgCol = bf(262144);
    static final BitField pattCol = bf(131072);
    static final BitField pattStyle = bf(65536);
    static final BitField prot = bf(1073741824);
    static final BitField protHidden = bf(512);
    static final BitField protLocked = bf(256);
    static final BitField undocumented = bf(62914560);
    protected BorderFormatting _borderFormatting;
    protected FontFormatting _fontFormatting;
    protected PatternFormatting _patternFormatting;
    private byte comparison_operator;
    private byte condition_type;
    protected short formatting_not_used;
    protected int formatting_options;
    private Formula formula1;
    private Formula formula2;

    public static final class ComparisonOperator {
        public static final byte BETWEEN = (byte) 1;
        public static final byte EQUAL = (byte) 3;
        public static final byte GE = (byte) 7;
        public static final byte GT = (byte) 5;
        public static final byte LE = (byte) 8;
        public static final byte LT = (byte) 6;
        public static final byte NOT_BETWEEN = (byte) 2;
        public static final byte NOT_EQUAL = (byte) 4;
        public static final byte NO_COMPARISON = (byte) 0;
        private static final byte max_operator = (byte) 8;
    }

    public abstract CFRuleBase clone();

    private static BitField bf(int i) {
        return BitFieldFactory.getInstance(i);
    }

    protected CFRuleBase(byte conditionType, byte comparisonOperation) {
        setConditionType(conditionType);
        setComparisonOperation(comparisonOperation);
        this.formula1 = Formula.create(Ptg.EMPTY_PTG_ARRAY);
        this.formula2 = Formula.create(Ptg.EMPTY_PTG_ARRAY);
    }

    protected CFRuleBase(byte conditionType, byte comparisonOperation, Ptg[] formula1, Ptg[] formula2) {
        this(conditionType, comparisonOperation);
        this.formula1 = Formula.create(formula1);
        this.formula2 = Formula.create(formula2);
    }

    protected CFRuleBase() {
    }

    protected int readFormatOptions(RecordInputStream in) {
        this.formatting_options = in.readInt();
        this.formatting_not_used = in.readShort();
        int len = 6;
        if (containsFontFormattingBlock()) {
            this._fontFormatting = new FontFormatting(in);
            len = 6 + this._fontFormatting.getDataLength();
        }
        if (containsBorderFormattingBlock()) {
            this._borderFormatting = new BorderFormatting(in);
            len += this._borderFormatting.getDataLength();
        }
        if (!containsPatternFormattingBlock()) {
            return len;
        }
        this._patternFormatting = new PatternFormatting(in);
        return len + this._patternFormatting.getDataLength();
    }

    public byte getConditionType() {
        return this.condition_type;
    }

    protected void setConditionType(byte condition_type) {
        if (!(this instanceof CFRuleRecord) || condition_type == (byte) 1 || condition_type == (byte) 2) {
            this.condition_type = condition_type;
            return;
        }
        throw new IllegalArgumentException("CFRuleRecord only accepts Value-Is and Formula types");
    }

    public void setComparisonOperation(byte operation) {
        if (operation < (byte) 0 || operation > (byte) 8) {
            throw new IllegalArgumentException("Valid operators are only in the range 0 to 8");
        }
        this.comparison_operator = operation;
    }

    public byte getComparisonOperation() {
        return this.comparison_operator;
    }

    public boolean containsFontFormattingBlock() {
        return getOptionFlag(font);
    }

    public void setFontFormatting(FontFormatting fontFormatting) {
        this._fontFormatting = fontFormatting;
        setOptionFlag(fontFormatting != null, font);
    }

    public FontFormatting getFontFormatting() {
        if (containsFontFormattingBlock()) {
            return this._fontFormatting;
        }
        return null;
    }

    public boolean containsAlignFormattingBlock() {
        return getOptionFlag(align);
    }

    public void setAlignFormattingUnchanged() {
        setOptionFlag(false, align);
    }

    public boolean containsBorderFormattingBlock() {
        return getOptionFlag(bord);
    }

    public void setBorderFormatting(BorderFormatting borderFormatting) {
        this._borderFormatting = borderFormatting;
        setOptionFlag(borderFormatting != null, bord);
    }

    public BorderFormatting getBorderFormatting() {
        if (containsBorderFormattingBlock()) {
            return this._borderFormatting;
        }
        return null;
    }

    public boolean containsPatternFormattingBlock() {
        return getOptionFlag(patt);
    }

    public void setPatternFormatting(PatternFormatting patternFormatting) {
        this._patternFormatting = patternFormatting;
        setOptionFlag(patternFormatting != null, patt);
    }

    public PatternFormatting getPatternFormatting() {
        if (containsPatternFormattingBlock()) {
            return this._patternFormatting;
        }
        return null;
    }

    public boolean containsProtectionFormattingBlock() {
        return getOptionFlag(prot);
    }

    public void setProtectionFormattingUnchanged() {
        setOptionFlag(false, prot);
    }

    public int getOptions() {
        return this.formatting_options;
    }

    private boolean isModified(BitField field) {
        return !field.isSet(this.formatting_options);
    }

    private void setModified(boolean modified, BitField field) {
        this.formatting_options = field.setBoolean(this.formatting_options, !modified);
    }

    public boolean isLeftBorderModified() {
        return isModified(bordLeft);
    }

    public void setLeftBorderModified(boolean modified) {
        setModified(modified, bordLeft);
    }

    public boolean isRightBorderModified() {
        return isModified(bordRight);
    }

    public void setRightBorderModified(boolean modified) {
        setModified(modified, bordRight);
    }

    public boolean isTopBorderModified() {
        return isModified(bordTop);
    }

    public void setTopBorderModified(boolean modified) {
        setModified(modified, bordTop);
    }

    public boolean isBottomBorderModified() {
        return isModified(bordBot);
    }

    public void setBottomBorderModified(boolean modified) {
        setModified(modified, bordBot);
    }

    public boolean isTopLeftBottomRightBorderModified() {
        return isModified(bordTlBr);
    }

    public void setTopLeftBottomRightBorderModified(boolean modified) {
        setModified(modified, bordTlBr);
    }

    public boolean isBottomLeftTopRightBorderModified() {
        return isModified(bordBlTr);
    }

    public void setBottomLeftTopRightBorderModified(boolean modified) {
        setModified(modified, bordBlTr);
    }

    public boolean isPatternStyleModified() {
        return isModified(pattStyle);
    }

    public void setPatternStyleModified(boolean modified) {
        setModified(modified, pattStyle);
    }

    public boolean isPatternColorModified() {
        return isModified(pattCol);
    }

    public void setPatternColorModified(boolean modified) {
        setModified(modified, pattCol);
    }

    public boolean isPatternBackgroundColorModified() {
        return isModified(pattBgCol);
    }

    public void setPatternBackgroundColorModified(boolean modified) {
        setModified(modified, pattBgCol);
    }

    private boolean getOptionFlag(BitField field) {
        return field.isSet(this.formatting_options);
    }

    private void setOptionFlag(boolean flag, BitField field) {
        this.formatting_options = field.setBoolean(this.formatting_options, flag);
    }

    protected int getFormattingBlockSize() {
        int i = 0;
        int length = (containsBorderFormattingBlock() ? 8 : 0) + ((containsFontFormattingBlock() ? this._fontFormatting.getRawRecord().length : 0) + 6);
        if (containsPatternFormattingBlock()) {
            i = 4;
        }
        return length + i;
    }

    protected void serializeFormattingBlock(LittleEndianOutput out) {
        out.writeInt(this.formatting_options);
        out.writeShort(this.formatting_not_used);
        if (containsFontFormattingBlock()) {
            out.write(this._fontFormatting.getRawRecord());
        }
        if (containsBorderFormattingBlock()) {
            this._borderFormatting.serialize(out);
        }
        if (containsPatternFormattingBlock()) {
            this._patternFormatting.serialize(out);
        }
    }

    public Ptg[] getParsedExpression1() {
        return this.formula1.getTokens();
    }

    public void setParsedExpression1(Ptg[] ptgs) {
        this.formula1 = Formula.create(ptgs);
    }

    protected Formula getFormula1() {
        return this.formula1;
    }

    protected void setFormula1(Formula formula1) {
        this.formula1 = formula1;
    }

    public Ptg[] getParsedExpression2() {
        return Formula.getTokens(this.formula2);
    }

    public void setParsedExpression2(Ptg[] ptgs) {
        this.formula2 = Formula.create(ptgs);
    }

    protected Formula getFormula2() {
        return this.formula2;
    }

    protected void setFormula2(Formula formula2) {
        this.formula2 = formula2;
    }

    protected static int getFormulaSize(Formula formula) {
        return formula.getEncodedTokenSize();
    }

    public static Ptg[] parseFormula(String formula, HSSFSheet sheet) {
        if (formula == null) {
            return null;
        }
        return HSSFFormulaParser.parse(formula, sheet.getWorkbook(), FormulaType.CELL, sheet.getWorkbook().getSheetIndex(sheet));
    }

    protected void copyTo(CFRuleBase rec) {
        rec.condition_type = this.condition_type;
        rec.comparison_operator = this.comparison_operator;
        rec.formatting_options = this.formatting_options;
        rec.formatting_not_used = this.formatting_not_used;
        if (containsFontFormattingBlock()) {
            rec._fontFormatting = this._fontFormatting.clone();
        }
        if (containsBorderFormattingBlock()) {
            rec._borderFormatting = this._borderFormatting.clone();
        }
        if (containsPatternFormattingBlock()) {
            rec._patternFormatting = (PatternFormatting) this._patternFormatting.clone();
        }
        rec.setFormula1(getFormula1().copy());
        rec.setFormula2(getFormula2().copy());
    }
}
