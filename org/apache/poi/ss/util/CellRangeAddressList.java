package org.apache.poi.ss.util;

import java.util.ArrayList;
import java.util.List;
import org.apache.poi.hssf.record.RecordInputStream;
import org.apache.poi.util.LittleEndianByteArrayOutputStream;
import org.apache.poi.util.LittleEndianOutput;

public class CellRangeAddressList {
    protected final List<CellRangeAddress> _list;

    public CellRangeAddressList() {
        this._list = new ArrayList();
    }

    public CellRangeAddressList(int firstRow, int lastRow, int firstCol, int lastCol) {
        this();
        addCellRangeAddress(firstRow, firstCol, lastRow, lastCol);
    }

    public CellRangeAddressList(RecordInputStream in) {
        this();
        int nItems = in.readUShort();
        for (int k = 0; k < nItems; k++) {
            this._list.add(new CellRangeAddress(in));
        }
    }

    public int countRanges() {
        return this._list.size();
    }

    public void addCellRangeAddress(int firstRow, int firstCol, int lastRow, int lastCol) {
        addCellRangeAddress(new CellRangeAddress(firstRow, lastRow, firstCol, lastCol));
    }

    public void addCellRangeAddress(CellRangeAddress cra) {
        this._list.add(cra);
    }

    public CellRangeAddress remove(int rangeIndex) {
        if (this._list.isEmpty()) {
            throw new RuntimeException("List is empty");
        } else if (rangeIndex >= 0 && rangeIndex < this._list.size()) {
            return (CellRangeAddress) this._list.remove(rangeIndex);
        } else {
            throw new RuntimeException("Range index (" + rangeIndex + ") is outside allowable range (0.." + (this._list.size() - 1) + ")");
        }
    }

    public CellRangeAddress getCellRangeAddress(int index) {
        return (CellRangeAddress) this._list.get(index);
    }

    public int getSize() {
        return getEncodedSize(this._list.size());
    }

    public static int getEncodedSize(int numberOfRanges) {
        return CellRangeAddress.getEncodedSize(numberOfRanges) + 2;
    }

    public int serialize(int offset, byte[] data) {
        int totalSize = getSize();
        serialize(new LittleEndianByteArrayOutputStream(data, offset, totalSize));
        return totalSize;
    }

    public void serialize(LittleEndianOutput out) {
        int nItems = this._list.size();
        out.writeShort(nItems);
        for (int k = 0; k < nItems; k++) {
            ((CellRangeAddress) this._list.get(k)).serialize(out);
        }
    }

    public CellRangeAddressList copy() {
        CellRangeAddressList result = new CellRangeAddressList();
        int nItems = this._list.size();
        for (int k = 0; k < nItems; k++) {
            result.addCellRangeAddress(((CellRangeAddress) this._list.get(k)).copy());
        }
        return result;
    }

    public CellRangeAddress[] getCellRangeAddresses() {
        CellRangeAddress[] result = new CellRangeAddress[this._list.size()];
        this._list.toArray(result);
        return result;
    }
}
