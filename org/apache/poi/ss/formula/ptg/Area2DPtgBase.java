package org.apache.poi.ss.formula.ptg;

import org.apache.poi.ss.util.AreaReference;
import org.apache.poi.util.LittleEndianInput;
import org.apache.poi.util.LittleEndianOutput;

public abstract class Area2DPtgBase extends AreaPtgBase {
    private static final int SIZE = 9;

    protected abstract byte getSid();

    protected Area2DPtgBase(int firstRow, int lastRow, int firstColumn, int lastColumn, boolean firstRowRelative, boolean lastRowRelative, boolean firstColRelative, boolean lastColRelative) {
        super(firstRow, lastRow, firstColumn, lastColumn, firstRowRelative, lastRowRelative, firstColRelative, lastColRelative);
    }

    protected Area2DPtgBase(AreaReference ar) {
        super(ar);
    }

    protected Area2DPtgBase(LittleEndianInput in) {
        readCoordinates(in);
    }

    public final void write(LittleEndianOutput out) {
        out.writeByte(getSid() + getPtgClass());
        writeCoordinates(out);
    }

    public final int getSize() {
        return 9;
    }

    public final String toFormulaString() {
        return formatReferenceAsString();
    }

    public final String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(getClass().getName());
        sb.append(" [");
        sb.append(formatReferenceAsString());
        sb.append("]");
        return sb.toString();
    }
}
