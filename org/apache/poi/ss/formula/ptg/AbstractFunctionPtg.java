package org.apache.poi.ss.formula.ptg;

import java.util.Locale;
import org.apache.poi.ss.formula.function.FunctionMetadata;
import org.apache.poi.ss.formula.function.FunctionMetadataRegistry;

public abstract class AbstractFunctionPtg extends OperationPtg {
    private static final short FUNCTION_INDEX_EXTERNAL = (short) 255;
    public static final String FUNCTION_NAME_IF = "IF";
    private final short _functionIndex;
    private final byte _numberOfArgs;
    private final byte[] paramClass;
    private final byte returnClass;

    public abstract int getSize();

    protected AbstractFunctionPtg(int functionIndex, int pReturnClass, byte[] paramTypes, int nParams) {
        this._numberOfArgs = (byte) nParams;
        this._functionIndex = (short) functionIndex;
        this.returnClass = (byte) pReturnClass;
        this.paramClass = paramTypes;
    }

    public final boolean isBaseToken() {
        return false;
    }

    public final String toString() {
        StringBuilder sb = new StringBuilder(64);
        sb.append(getClass().getName()).append(" [");
        sb.append(lookupName(this._functionIndex));
        sb.append(" nArgs=").append(this._numberOfArgs);
        sb.append("]");
        return sb.toString();
    }

    public final short getFunctionIndex() {
        return this._functionIndex;
    }

    public final int getNumberOfOperands() {
        return this._numberOfArgs;
    }

    public final String getName() {
        return lookupName(this._functionIndex);
    }

    public final boolean isExternalFunction() {
        return this._functionIndex == (short) 255;
    }

    public final String toFormulaString() {
        return getName();
    }

    public String toFormulaString(String[] operands) {
        StringBuilder buf = new StringBuilder();
        if (isExternalFunction()) {
            buf.append(operands[0]);
            appendArgs(buf, 1, operands);
        } else {
            buf.append(getName());
            appendArgs(buf, 0, operands);
        }
        return buf.toString();
    }

    private static void appendArgs(StringBuilder buf, int firstArgIx, String[] operands) {
        buf.append('(');
        for (int i = firstArgIx; i < operands.length; i++) {
            if (i > firstArgIx) {
                buf.append(',');
            }
            buf.append(operands[i]);
        }
        buf.append(")");
    }

    public static final boolean isBuiltInFunctionName(String name) {
        return FunctionMetadataRegistry.lookupIndexByName(name.toUpperCase(Locale.ROOT)) >= (short) 0;
    }

    protected final String lookupName(short index) {
        if (index == (short) 255) {
            return "#external#";
        }
        FunctionMetadata fm = FunctionMetadataRegistry.getFunctionByIndex(index);
        if (fm != null) {
            return fm.getName();
        }
        throw new RuntimeException("bad function index (" + index + ")");
    }

    protected static short lookupIndex(String name) {
        short ix = FunctionMetadataRegistry.lookupIndexByName(name.toUpperCase(Locale.ROOT));
        if (ix < (short) 0) {
            return (short) 255;
        }
        return ix;
    }

    public byte getDefaultOperandClass() {
        return this.returnClass;
    }

    public final byte getParameterClass(int index) {
        if (index >= this.paramClass.length) {
            return this.paramClass[this.paramClass.length - 1];
        }
        return this.paramClass[index];
    }
}
