package org.apache.poi.ss.formula.ptg;

import org.apache.poi.util.LittleEndianInput;
import org.apache.poi.util.LittleEndianOutput;

public final class RefNPtg extends Ref2DPtgBase {
    public static final byte sid = (byte) 44;

    public /* bridge */ /* synthetic */ void write(LittleEndianOutput littleEndianOutput) {
        super.write(littleEndianOutput);
    }

    public RefNPtg(LittleEndianInput in) {
        super(in);
    }

    protected byte getSid() {
        return sid;
    }
}
