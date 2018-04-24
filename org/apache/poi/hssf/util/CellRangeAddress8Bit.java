package org.apache.poi.hssf.util;

import org.apache.poi.ss.util.CellRangeAddressBase;
import org.apache.poi.util.LittleEndianInput;
import org.apache.poi.util.LittleEndianOutput;

public final class CellRangeAddress8Bit extends CellRangeAddressBase {
    public static final int ENCODED_SIZE = 6;

    public CellRangeAddress8Bit(int firstRow, int lastRow, int firstCol, int lastCol) {
        super(firstRow, lastRow, firstCol, lastCol);
    }

    public CellRangeAddress8Bit(LittleEndianInput in) {
        super(readUShortAndCheck(in), in.readUShort(), in.readUByte(), in.readUByte());
    }

    private static int readUShortAndCheck(LittleEndianInput in) {
        if (in.available() >= 6) {
            return in.readUShort();
        }
        throw new RuntimeException("Ran out of data reading CellRangeAddress");
    }

    public void serialize(LittleEndianOutput out) {
        out.writeShort(getFirstRow());
        out.writeShort(getLastRow());
        out.writeByte(getFirstColumn());
        out.writeByte(getLastColumn());
    }

    public CellRangeAddress8Bit copy() {
        return new CellRangeAddress8Bit(getFirstRow(), getLastRow(), getFirstColumn(), getLastColumn());
    }

    public static int getEncodedSize(int numberOfItems) {
        return numberOfItems * 6;
    }
}
