package org.apache.poi.ss.formula.ptg;

import org.apache.poi.ss.formula.function.FunctionMetadata;
import org.apache.poi.ss.formula.function.FunctionMetadataRegistry;
import org.apache.poi.util.LittleEndianInput;
import org.apache.poi.util.LittleEndianOutput;

public final class FuncPtg extends AbstractFunctionPtg {
    public static final int SIZE = 3;
    public static final byte sid = (byte) 33;

    public static FuncPtg create(LittleEndianInput in) {
        return create(in.readUShort());
    }

    private FuncPtg(int funcIndex, FunctionMetadata fm) {
        super(funcIndex, fm.getReturnClassCode(), fm.getParameterClassCodes(), fm.getMinParams());
    }

    public static FuncPtg create(int functionIndex) {
        FunctionMetadata fm = FunctionMetadataRegistry.getFunctionByIndex(functionIndex);
        if (fm != null) {
            return new FuncPtg(functionIndex, fm);
        }
        throw new RuntimeException("Invalid built-in function index (" + functionIndex + ")");
    }

    public void write(LittleEndianOutput out) {
        out.writeByte(getPtgClass() + 33);
        out.writeShort(getFunctionIndex());
    }

    public int getSize() {
        return 3;
    }
}
