package org.apache.poi.ss.util;

import org.apache.poi.hssf.record.RecordInputStream;
import org.apache.poi.ss.formula.SheetNameFormatter;
import org.apache.poi.util.LittleEndianOutput;

public class CellRangeAddress extends CellRangeAddressBase {
    public static final int ENCODED_SIZE = 8;

    public CellRangeAddress(int firstRow, int lastRow, int firstCol, int lastCol) {
        super(firstRow, lastRow, firstCol, lastCol);
        if (lastRow < firstRow || lastCol < firstCol) {
            throw new IllegalArgumentException("lastRow < firstRow || lastCol < firstCol");
        }
    }

    public void serialize(LittleEndianOutput out) {
        out.writeShort(getFirstRow());
        out.writeShort(getLastRow());
        out.writeShort(getFirstColumn());
        out.writeShort(getLastColumn());
    }

    public CellRangeAddress(RecordInputStream in) {
        super(readUShortAndCheck(in), in.readUShort(), in.readUShort(), in.readUShort());
    }

    private static int readUShortAndCheck(RecordInputStream in) {
        if (in.remaining() >= 8) {
            return in.readUShort();
        }
        throw new RuntimeException("Ran out of data reading CellRangeAddress");
    }

    public CellRangeAddress copy() {
        return new CellRangeAddress(getFirstRow(), getLastRow(), getFirstColumn(), getLastColumn());
    }

    public static int getEncodedSize(int numberOfItems) {
        return numberOfItems * 8;
    }

    public String formatAsString() {
        return formatAsString(null, false);
    }

    public String formatAsString(String sheetName, boolean useAbsoluteAddress) {
        StringBuffer sb = new StringBuffer();
        if (sheetName != null) {
            sb.append(SheetNameFormatter.format(sheetName));
            sb.append("!");
        }
        CellReference cellRefFrom = new CellReference(getFirstRow(), getFirstColumn(), useAbsoluteAddress, useAbsoluteAddress);
        CellReference cellRefTo = new CellReference(getLastRow(), getLastColumn(), useAbsoluteAddress, useAbsoluteAddress);
        sb.append(cellRefFrom.formatAsString());
        if (!cellRefFrom.equals(cellRefTo) || isFullColumnRange() || isFullRowRange()) {
            sb.append(':');
            sb.append(cellRefTo.formatAsString());
        }
        return sb.toString();
    }

    public static CellRangeAddress valueOf(String ref) {
        CellReference a;
        CellReference b;
        int sep = ref.indexOf(":");
        if (sep == -1) {
            a = new CellReference(ref);
            b = a;
        } else {
            a = new CellReference(ref.substring(0, sep));
            b = new CellReference(ref.substring(sep + 1));
        }
        return new CellRangeAddress(a.getRow(), b.getRow(), a.getCol(), b.getCol());
    }
}
