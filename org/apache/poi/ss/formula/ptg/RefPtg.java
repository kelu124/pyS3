package org.apache.poi.ss.formula.ptg;

import org.apache.poi.ss.util.CellReference;
import org.apache.poi.util.LittleEndianInput;
import org.apache.poi.util.LittleEndianOutput;

public final class RefPtg extends Ref2DPtgBase {
    public static final byte sid = (byte) 36;

    public /* bridge */ /* synthetic */ void write(LittleEndianOutput littleEndianOutput) {
        super.write(littleEndianOutput);
    }

    public RefPtg(String cellref) {
        super(new CellReference(cellref));
    }

    public RefPtg(int row, int column, boolean isRowRelative, boolean isColumnRelative) {
        super(row, column, isRowRelative, isColumnRelative);
    }

    public RefPtg(LittleEndianInput in) {
        super(in);
    }

    public RefPtg(CellReference cr) {
        super(cr);
    }

    protected byte getSid() {
        return sid;
    }
}
