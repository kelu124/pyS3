package org.apache.poi.hssf.record;

import org.apache.poi.ss.usermodel.FormulaError;
import org.apache.poi.util.HexDump;
import org.apache.poi.util.LittleEndianOutput;

public final class BoolErrRecord extends CellRecord implements Cloneable {
    public static final short sid = (short) 517;
    private boolean _isError;
    private int _value;

    static /* synthetic */ class C10541 {
        static final /* synthetic */ int[] $SwitchMap$org$apache$poi$ss$usermodel$FormulaError = new int[FormulaError.values().length];

        static {
            try {
                $SwitchMap$org$apache$poi$ss$usermodel$FormulaError[FormulaError.NULL.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$org$apache$poi$ss$usermodel$FormulaError[FormulaError.DIV0.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$org$apache$poi$ss$usermodel$FormulaError[FormulaError.VALUE.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$org$apache$poi$ss$usermodel$FormulaError[FormulaError.REF.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$org$apache$poi$ss$usermodel$FormulaError[FormulaError.NAME.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$org$apache$poi$ss$usermodel$FormulaError[FormulaError.NUM.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
            try {
                $SwitchMap$org$apache$poi$ss$usermodel$FormulaError[FormulaError.NA.ordinal()] = 7;
            } catch (NoSuchFieldError e7) {
            }
        }
    }

    public BoolErrRecord(RecordInputStream in) {
        super(in);
        switch (in.remaining()) {
            case 2:
                this._value = in.readByte();
                break;
            case 3:
                this._value = in.readUShort();
                break;
            default:
                throw new RecordFormatException("Unexpected size (" + in.remaining() + ") for BOOLERR record.");
        }
        int flag = in.readUByte();
        switch (flag) {
            case 0:
                this._isError = false;
                return;
            case 1:
                this._isError = true;
                return;
            default:
                throw new RecordFormatException("Unexpected isError flag (" + flag + ") for BOOLERR record.");
        }
    }

    public void setValue(boolean value) {
        int i;
        if (value) {
            i = 1;
        } else {
            i = 0;
        }
        this._value = i;
        this._isError = false;
    }

    public void setValue(byte value) {
        setValue(FormulaError.forInt(value));
    }

    public void setValue(FormulaError value) {
        switch (C10541.$SwitchMap$org$apache$poi$ss$usermodel$FormulaError[value.ordinal()]) {
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
                this._value = value.getCode();
                this._isError = true;
                return;
            default:
                throw new IllegalArgumentException("Error Value can only be 0,7,15,23,29,36 or 42. It cannot be " + value.getCode() + " (" + value + ")");
        }
    }

    public boolean getBooleanValue() {
        return this._value != 0;
    }

    public byte getErrorValue() {
        return (byte) this._value;
    }

    public boolean isBoolean() {
        return !this._isError;
    }

    public boolean isError() {
        return this._isError;
    }

    protected String getRecordName() {
        return "BOOLERR";
    }

    protected void appendValueText(StringBuilder sb) {
        if (isBoolean()) {
            sb.append("  .boolVal = ");
            sb.append(getBooleanValue());
            return;
        }
        sb.append("  .errCode = ");
        sb.append(FormulaError.forInt(getErrorValue()).getString());
        sb.append(" (").append(HexDump.byteToHex(getErrorValue())).append(")");
    }

    protected void serializeValue(LittleEndianOutput out) {
        out.writeByte(this._value);
        out.writeByte(this._isError ? 1 : 0);
    }

    protected int getValueDataSize() {
        return 2;
    }

    public short getSid() {
        return (short) 517;
    }

    public BoolErrRecord clone() {
        BoolErrRecord rec = new BoolErrRecord();
        copyBaseFields(rec);
        rec._value = this._value;
        rec._isError = this._isError;
        return rec;
    }
}
