package org.apache.poi.ss.formula.ptg;

import org.apache.poi.util.LittleEndianInput;
import org.apache.poi.util.LittleEndianOutput;
import org.apache.poi.util.StringUtil;

public final class StringPtg extends ScalarConstantPtg {
    private static final char FORMULA_DELIMITER = '\"';
    public static final byte sid = (byte) 23;
    private final boolean _is16bitUnicode;
    private final String field_3_string;

    public StringPtg(LittleEndianInput in) {
        int nChars = in.readUByte();
        this._is16bitUnicode = (in.readByte() & 1) != 0;
        if (this._is16bitUnicode) {
            this.field_3_string = StringUtil.readUnicodeLE(in, nChars);
        } else {
            this.field_3_string = StringUtil.readCompressedUnicode(in, nChars);
        }
    }

    public StringPtg(String value) {
        if (value.length() > 255) {
            throw new IllegalArgumentException("String literals in formulas can't be bigger than 255 characters ASCII");
        }
        this._is16bitUnicode = StringUtil.hasMultibyte(value);
        this.field_3_string = value;
    }

    public String getValue() {
        return this.field_3_string;
    }

    public void write(LittleEndianOutput out) {
        out.writeByte(getPtgClass() + 23);
        out.writeByte(this.field_3_string.length());
        out.writeByte(this._is16bitUnicode ? 1 : 0);
        if (this._is16bitUnicode) {
            StringUtil.putUnicodeLE(this.field_3_string, out);
        } else {
            StringUtil.putCompressedUnicode(this.field_3_string, out);
        }
    }

    public int getSize() {
        return ((this._is16bitUnicode ? 2 : 1) * this.field_3_string.length()) + 3;
    }

    public String toFormulaString() {
        String value = this.field_3_string;
        int len = value.length();
        StringBuffer sb = new StringBuffer(len + 4);
        sb.append(FORMULA_DELIMITER);
        for (int i = 0; i < len; i++) {
            char c = value.charAt(i);
            if (c == FORMULA_DELIMITER) {
                sb.append(FORMULA_DELIMITER);
            }
            sb.append(c);
        }
        sb.append(FORMULA_DELIMITER);
        return sb.toString();
    }
}
