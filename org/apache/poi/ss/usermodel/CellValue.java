package org.apache.poi.ss.usermodel;

import org.apache.poi.ss.formula.eval.ErrorEval;

public final class CellValue {
    public static final CellValue FALSE = new CellValue(CellType.BOOLEAN, 0.0d, false, null, 0);
    public static final CellValue TRUE = new CellValue(CellType.BOOLEAN, 0.0d, true, null, 0);
    private final boolean _booleanValue;
    private final CellType _cellType;
    private final int _errorCode;
    private final double _numberValue;
    private final String _textValue;

    private CellValue(CellType cellType, double numberValue, boolean booleanValue, String textValue, int errorCode) {
        this._cellType = cellType;
        this._numberValue = numberValue;
        this._booleanValue = booleanValue;
        this._textValue = textValue;
        this._errorCode = errorCode;
    }

    public CellValue(double numberValue) {
        this(CellType.NUMERIC, numberValue, false, null, 0);
    }

    public static CellValue valueOf(boolean booleanValue) {
        return booleanValue ? TRUE : FALSE;
    }

    public CellValue(String stringValue) {
        this(CellType.STRING, 0.0d, false, stringValue, 0);
    }

    public static CellValue getError(int errorCode) {
        return new CellValue(CellType.ERROR, 0.0d, false, null, errorCode);
    }

    public boolean getBooleanValue() {
        return this._booleanValue;
    }

    public double getNumberValue() {
        return this._numberValue;
    }

    public String getStringValue() {
        return this._textValue;
    }

    public CellType getCellTypeEnum() {
        return this._cellType;
    }

    @Deprecated
    public int getCellType() {
        return this._cellType.getCode();
    }

    public byte getErrorValue() {
        return (byte) this._errorCode;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer(64);
        sb.append(getClass().getName()).append(" [");
        sb.append(formatAsString());
        sb.append("]");
        return sb.toString();
    }

    public String formatAsString() {
        switch (1.$SwitchMap$org$apache$poi$ss$usermodel$CellType[this._cellType.ordinal()]) {
            case 1:
                return String.valueOf(this._numberValue);
            case 2:
                return '\"' + this._textValue + '\"';
            case 3:
                return this._booleanValue ? "TRUE" : "FALSE";
            case 4:
                return ErrorEval.getText(this._errorCode);
            default:
                return "<error unexpected cell type " + this._cellType + ">";
        }
    }
}
