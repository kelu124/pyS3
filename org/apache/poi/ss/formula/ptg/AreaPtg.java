package org.apache.poi.ss.formula.ptg;

import org.apache.poi.ss.util.AreaReference;
import org.apache.poi.util.LittleEndianInput;

public final class AreaPtg extends Area2DPtgBase {
    public static final short sid = (short) 37;

    public AreaPtg(int firstRow, int lastRow, int firstColumn, int lastColumn, boolean firstRowRelative, boolean lastRowRelative, boolean firstColRelative, boolean lastColRelative) {
        super(firstRow, lastRow, firstColumn, lastColumn, firstRowRelative, lastRowRelative, firstColRelative, lastColRelative);
    }

    public AreaPtg(LittleEndianInput in) {
        super(in);
    }

    public AreaPtg(String arearef) {
        super(new AreaReference(arearef));
    }

    public AreaPtg(AreaReference areaRef) {
        super(areaRef);
    }

    protected byte getSid() {
        return (byte) 37;
    }
}
