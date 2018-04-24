package org.apache.poi.hssf.record.aggregates;

import java.util.ArrayList;
import java.util.List;
import org.apache.poi.hssf.model.RecordStream;
import org.apache.poi.hssf.record.MergeCellsRecord;
import org.apache.poi.hssf.record.aggregates.RecordAggregate.RecordVisitor;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;

public final class MergedCellsTable extends RecordAggregate {
    private static int MAX_MERGED_REGIONS = 1027;
    private final List<CellRangeAddress> _mergedRegions = new ArrayList();

    public void read(RecordStream rs) {
        List<CellRangeAddress> temp = this._mergedRegions;
        while (rs.peekNextClass() == MergeCellsRecord.class) {
            MergeCellsRecord mcr = (MergeCellsRecord) rs.getNext();
            int nRegions = mcr.getNumAreas();
            for (int i = 0; i < nRegions; i++) {
                temp.add(mcr.getAreaAt(i));
            }
        }
    }

    public int getRecordSize() {
        int nRegions = this._mergedRegions.size();
        if (nRegions < 1) {
            return 0;
        }
        return (((CellRangeAddressList.getEncodedSize(MAX_MERGED_REGIONS) + 4) * (nRegions / MAX_MERGED_REGIONS)) + 4) + CellRangeAddressList.getEncodedSize(nRegions % MAX_MERGED_REGIONS);
    }

    public void visitContainedRecords(RecordVisitor rv) {
        int nRegions = this._mergedRegions.size();
        if (nRegions >= 1) {
            int nFullMergedCellsRecords = nRegions / MAX_MERGED_REGIONS;
            int nLeftoverMergedRegions = nRegions % MAX_MERGED_REGIONS;
            CellRangeAddress[] cras = new CellRangeAddress[nRegions];
            this._mergedRegions.toArray(cras);
            for (int i = 0; i < nFullMergedCellsRecords; i++) {
                rv.visitRecord(new MergeCellsRecord(cras, i * MAX_MERGED_REGIONS, MAX_MERGED_REGIONS));
            }
            if (nLeftoverMergedRegions > 0) {
                rv.visitRecord(new MergeCellsRecord(cras, nFullMergedCellsRecords * MAX_MERGED_REGIONS, nLeftoverMergedRegions));
            }
        }
    }

    public void addRecords(MergeCellsRecord[] mcrs) {
        for (MergeCellsRecord addMergeCellsRecord : mcrs) {
            addMergeCellsRecord(addMergeCellsRecord);
        }
    }

    private void addMergeCellsRecord(MergeCellsRecord mcr) {
        int nRegions = mcr.getNumAreas();
        for (int i = 0; i < nRegions; i++) {
            this._mergedRegions.add(mcr.getAreaAt(i));
        }
    }

    public CellRangeAddress get(int index) {
        checkIndex(index);
        return (CellRangeAddress) this._mergedRegions.get(index);
    }

    public void remove(int index) {
        checkIndex(index);
        this._mergedRegions.remove(index);
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= this._mergedRegions.size()) {
            throw new IllegalArgumentException("Specified CF index " + index + " is outside the allowable range (0.." + (this._mergedRegions.size() - 1) + ")");
        }
    }

    public void addArea(int rowFrom, int colFrom, int rowTo, int colTo) {
        this._mergedRegions.add(new CellRangeAddress(rowFrom, rowTo, colFrom, colTo));
    }

    public int getNumberOfMergedRegions() {
        return this._mergedRegions.size();
    }
}
