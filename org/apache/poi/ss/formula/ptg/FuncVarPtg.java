package org.apache.poi.ss.formula.ptg;

import org.apache.poi.ss.formula.function.FunctionMetadata;
import org.apache.poi.ss.formula.function.FunctionMetadataRegistry;
import org.apache.poi.util.LittleEndianInput;
import org.apache.poi.util.LittleEndianOutput;

public final class FuncVarPtg extends AbstractFunctionPtg {
    private static final int SIZE = 4;
    public static final OperationPtg SUM = create("SUM", 1);
    public static final byte sid = (byte) 34;

    private FuncVarPtg(int functionIndex, int returnClass, byte[] paramClasses, int numArgs) {
        super(functionIndex, returnClass, paramClasses, numArgs);
    }

    public static FuncVarPtg create(LittleEndianInput in) {
        return create(in.readByte(), in.readShort());
    }

    public static FuncVarPtg create(String pName, int numArgs) {
        return create(numArgs, AbstractFunctionPtg.lookupIndex(pName));
    }

    private static FuncVarPtg create(int numArgs, int functionIndex) {
        FunctionMetadata fm = FunctionMetadataRegistry.getFunctionByIndex(functionIndex);
        if (fm != null) {
            return new FuncVarPtg(functionIndex, fm.getReturnClassCode(), fm.getParameterClassCodes(), numArgs);
        }
        return new FuncVarPtg(functionIndex, 32, new byte[]{(byte) 32}, numArgs);
    }

    public void write(LittleEndianOutput out) {
        out.writeByte(getPtgClass() + 34);
        out.writeByte(getNumberOfOperands());
        out.writeShort(getFunctionIndex());
    }

    public int getSize() {
        return 4;
    }
}
