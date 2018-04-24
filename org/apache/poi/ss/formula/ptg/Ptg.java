package org.apache.poi.ss.formula.ptg;

import java.util.ArrayList;
import java.util.List;
import org.apache.poi.util.LittleEndianByteArrayOutputStream;
import org.apache.poi.util.LittleEndianInput;
import org.apache.poi.util.LittleEndianOutput;

public abstract class Ptg {
    public static final byte CLASS_ARRAY = (byte) 64;
    public static final byte CLASS_REF = (byte) 0;
    public static final byte CLASS_VALUE = (byte) 32;
    public static final Ptg[] EMPTY_PTG_ARRAY = new Ptg[0];
    private byte ptgClass = (byte) 0;

    public abstract byte getDefaultOperandClass();

    public abstract int getSize();

    public abstract boolean isBaseToken();

    public abstract String toFormulaString();

    public abstract void write(LittleEndianOutput littleEndianOutput);

    public static Ptg[] readTokens(int size, LittleEndianInput in) {
        List<Ptg> temp = new ArrayList((size / 2) + 4);
        int pos = 0;
        boolean hasArrayPtgs = false;
        while (pos < size) {
            Ptg ptg = createPtg(in);
            if (ptg instanceof Initial) {
                hasArrayPtgs = true;
            }
            pos += ptg.getSize();
            temp.add(ptg);
        }
        if (pos != size) {
            throw new RuntimeException("Ptg array size mismatch");
        } else if (!hasArrayPtgs) {
            return toPtgArray(temp);
        } else {
            Ptg[] toPtgArray = toPtgArray(temp);
            for (int i = 0; i < toPtgArray.length; i++) {
                if (toPtgArray[i] instanceof Initial) {
                    toPtgArray[i] = ((Initial) toPtgArray[i]).finishReading(in);
                }
            }
            return toPtgArray;
        }
    }

    public static Ptg createPtg(LittleEndianInput in) {
        byte id = in.readByte();
        if (id < (byte) 32) {
            return createBasePtg(id, in);
        }
        Ptg retval = createClassifiedPtg(id, in);
        if (id >= (byte) 96) {
            retval.setClass((byte) 64);
            return retval;
        } else if (id >= (byte) 64) {
            retval.setClass((byte) 32);
            return retval;
        } else {
            retval.setClass((byte) 0);
            return retval;
        }
    }

    private static Ptg createClassifiedPtg(byte id, LittleEndianInput in) {
        switch ((id & 31) | 32) {
            case 32:
                return new Initial(in);
            case 33:
                return FuncPtg.create(in);
            case 34:
                return FuncVarPtg.create(in);
            case 35:
                return new NamePtg(in);
            case 36:
                return new RefPtg(in);
            case 37:
                return new AreaPtg(in);
            case 38:
                return new MemAreaPtg(in);
            case 39:
                return new MemErrPtg(in);
            case 41:
                return new MemFuncPtg(in);
            case 42:
                return new RefErrorPtg(in);
            case 43:
                return new AreaErrPtg(in);
            case 44:
                return new RefNPtg(in);
            case 45:
                return new AreaNPtg(in);
            case 57:
                return new NameXPtg(in);
            case 58:
                return new Ref3DPtg(in);
            case 59:
                return new Area3DPtg(in);
            case 60:
                return new DeletedRef3DPtg(in);
            case 61:
                return new DeletedArea3DPtg(in);
            default:
                throw new UnsupportedOperationException(" Unknown Ptg in Formula: 0x" + Integer.toHexString(id) + " (" + id + ")");
        }
    }

    private static Ptg createBasePtg(byte id, LittleEndianInput in) {
        switch (id) {
            case (byte) 0:
                return new UnknownPtg(id);
            case (byte) 1:
                return new ExpPtg(in);
            case (byte) 2:
                return new TblPtg(in);
            case (byte) 3:
                return AddPtg.instance;
            case (byte) 4:
                return SubtractPtg.instance;
            case (byte) 5:
                return MultiplyPtg.instance;
            case (byte) 6:
                return DividePtg.instance;
            case (byte) 7:
                return PowerPtg.instance;
            case (byte) 8:
                return ConcatPtg.instance;
            case (byte) 9:
                return LessThanPtg.instance;
            case (byte) 10:
                return LessEqualPtg.instance;
            case (byte) 11:
                return EqualPtg.instance;
            case (byte) 12:
                return GreaterEqualPtg.instance;
            case (byte) 13:
                return GreaterThanPtg.instance;
            case (byte) 14:
                return NotEqualPtg.instance;
            case (byte) 15:
                return IntersectionPtg.instance;
            case (byte) 16:
                return UnionPtg.instance;
            case (byte) 17:
                return RangePtg.instance;
            case (byte) 18:
                return UnaryPlusPtg.instance;
            case (byte) 19:
                return UnaryMinusPtg.instance;
            case (byte) 20:
                return PercentPtg.instance;
            case (byte) 21:
                return ParenthesisPtg.instance;
            case (byte) 22:
                return MissingArgPtg.instance;
            case (byte) 23:
                return new StringPtg(in);
            case (byte) 25:
                return new AttrPtg(in);
            case (byte) 28:
                return ErrPtg.read(in);
            case (byte) 29:
                return BoolPtg.read(in);
            case (byte) 30:
                return new IntPtg(in);
            case (byte) 31:
                return new NumberPtg(in);
            default:
                throw new RuntimeException("Unexpected base token id (" + id + ")");
        }
    }

    private static Ptg[] toPtgArray(List<Ptg> l) {
        if (l.isEmpty()) {
            return EMPTY_PTG_ARRAY;
        }
        Ptg[] result = new Ptg[l.size()];
        l.toArray(result);
        return result;
    }

    public static int getEncodedSize(Ptg[] ptgs) {
        int result = 0;
        for (Ptg ptg : ptgs) {
            result += ptg.getSize();
        }
        return result;
    }

    public static int getEncodedSizeWithoutArrayData(Ptg[] ptgs) {
        int result = 0;
        for (Ptg ptg : ptgs) {
            if (ptg instanceof ArrayPtg) {
                result += 8;
            } else {
                result += ptg.getSize();
            }
        }
        return result;
    }

    public static int serializePtgs(Ptg[] ptgs, byte[] array, int offset) {
        LittleEndianByteArrayOutputStream out = new LittleEndianByteArrayOutputStream(array, offset);
        List<Ptg> arrayPtgs = null;
        for (Ptg ptg : ptgs) {
            ptg.write(out);
            if (ptg instanceof ArrayPtg) {
                if (arrayPtgs == null) {
                    arrayPtgs = new ArrayList(5);
                }
                arrayPtgs.add(ptg);
            }
        }
        if (arrayPtgs != null) {
            for (Ptg arrayPtg : arrayPtgs) {
                ((ArrayPtg) arrayPtg).writeTokenValueBytes(out);
            }
        }
        return out.getWriteIndex() - offset;
    }

    public String toString() {
        return getClass().toString();
    }

    public final void setClass(byte thePtgClass) {
        if (isBaseToken()) {
            throw new RuntimeException("setClass should not be called on a base token");
        }
        this.ptgClass = thePtgClass;
    }

    public final byte getPtgClass() {
        return this.ptgClass;
    }

    public final char getRVAType() {
        if (isBaseToken()) {
            return '.';
        }
        switch (this.ptgClass) {
            case (byte) 0:
                return 'R';
            case (byte) 32:
                return 'V';
            case (byte) 64:
                return 'A';
            default:
                throw new RuntimeException("Unknown operand class (" + this.ptgClass + ")");
        }
    }

    public static boolean doesFormulaReferToDeletedCell(Ptg[] ptgs) {
        for (Ptg ptg : ptgs) {
            if (isDeletedCellRef(ptg)) {
                return true;
            }
        }
        return false;
    }

    private static boolean isDeletedCellRef(Ptg ptg) {
        if (ptg == ErrPtg.REF_INVALID || (ptg instanceof DeletedArea3DPtg) || (ptg instanceof DeletedRef3DPtg) || (ptg instanceof AreaErrPtg) || (ptg instanceof RefErrorPtg)) {
            return true;
        }
        return false;
    }
}
