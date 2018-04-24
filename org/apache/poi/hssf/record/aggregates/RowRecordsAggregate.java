package org.apache.poi.hssf.record.aggregates;

import com.itextpdf.text.pdf.codec.TIFFConstants;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.apache.poi.hssf.model.RecordStream;
import org.apache.poi.hssf.record.CellValueRecordInterface;
import org.apache.poi.hssf.record.DBCellRecord.Builder;
import org.apache.poi.hssf.record.DimensionsRecord;
import org.apache.poi.hssf.record.FormulaRecord;
import org.apache.poi.hssf.record.IndexRecord;
import org.apache.poi.hssf.record.MulBlankRecord;
import org.apache.poi.hssf.record.Record;
import org.apache.poi.hssf.record.RowRecord;
import org.apache.poi.hssf.record.UnknownRecord;
import org.apache.poi.hssf.record.aggregates.RecordAggregate.PositionTrackingVisitor;
import org.apache.poi.hssf.record.aggregates.RecordAggregate.RecordVisitor;
import org.apache.poi.ss.SpreadsheetVersion;
import org.apache.poi.ss.formula.FormulaShifter;

public final class RowRecordsAggregate extends RecordAggregate {
    private int _firstrow;
    private int _lastrow;
    private RowRecord[] _rowRecordValues;
    private final Map<Integer, RowRecord> _rowRecords;
    private final SharedValueManager _sharedValueManager;
    private final List<Record> _unknownRecords;
    private final ValueRecordsAggregate _valuesAgg;

    public RowRecordsAggregate() {
        this(SharedValueManager.createEmpty());
    }

    private RowRecordsAggregate(SharedValueManager svm) {
        this._firstrow = -1;
        this._lastrow = -1;
        this._rowRecordValues = null;
        if (svm == null) {
            throw new IllegalArgumentException("SharedValueManager must be provided.");
        }
        this._rowRecords = new TreeMap();
        this._valuesAgg = new ValueRecordsAggregate();
        this._unknownRecords = new ArrayList();
        this._sharedValueManager = svm;
    }

    public RowRecordsAggregate(RecordStream rs, SharedValueManager svm) {
        this(svm);
        while (rs.hasNext()) {
            Record rec = rs.getNext();
            switch (rec.getSid()) {
                case (short) 81:
                    addUnknownRecord(rec);
                    break;
                case (short) 215:
                    break;
                case TIFFConstants.TIFFTAG_JPEGDCTABLES /*520*/:
                    insertRow((RowRecord) rec);
                    break;
                default:
                    if (!(rec instanceof UnknownRecord)) {
                        if (!(rec instanceof MulBlankRecord)) {
                            if (rec instanceof CellValueRecordInterface) {
                                this._valuesAgg.construct((CellValueRecordInterface) rec, rs, svm);
                                break;
                            }
                            throw new RuntimeException("Unexpected record type (" + rec.getClass().getName() + ")");
                        }
                        this._valuesAgg.addMultipleBlanks((MulBlankRecord) rec);
                        break;
                    }
                    addUnknownRecord(rec);
                    while (rs.peekNextSid() == 60) {
                        addUnknownRecord(rs.getNext());
                    }
                    break;
            }
        }
    }

    private void addUnknownRecord(Record rec) {
        this._unknownRecords.add(rec);
    }

    public void insertRow(RowRecord row) {
        this._rowRecords.put(Integer.valueOf(row.getRowNumber()), row);
        this._rowRecordValues = null;
        if (row.getRowNumber() < this._firstrow || this._firstrow == -1) {
            this._firstrow = row.getRowNumber();
        }
        if (row.getRowNumber() > this._lastrow || this._lastrow == -1) {
            this._lastrow = row.getRowNumber();
        }
    }

    public void removeRow(RowRecord row) {
        int rowIndex = row.getRowNumber();
        this._valuesAgg.removeAllCellsValuesForRow(rowIndex);
        Integer key = Integer.valueOf(rowIndex);
        RowRecord rr = (RowRecord) this._rowRecords.remove(key);
        if (rr == null) {
            throw new RuntimeException("Invalid row index (" + key.intValue() + ")");
        } else if (row != rr) {
            this._rowRecords.put(key, rr);
            throw new RuntimeException("Attempt to remove row that does not belong to this sheet");
        } else {
            this._rowRecordValues = null;
        }
    }

    public RowRecord getRow(int rowIndex) {
        int maxrow = SpreadsheetVersion.EXCEL97.getLastRowIndex();
        if (rowIndex >= 0 && rowIndex <= maxrow) {
            return (RowRecord) this._rowRecords.get(Integer.valueOf(rowIndex));
        }
        throw new IllegalArgumentException("The row number must be between 0 and " + maxrow + ", but had: " + rowIndex);
    }

    public int getPhysicalNumberOfRows() {
        return this._rowRecords.size();
    }

    public int getFirstRowNum() {
        return this._firstrow;
    }

    public int getLastRowNum() {
        return this._lastrow;
    }

    public int getRowBlockCount() {
        int size = this._rowRecords.size() / 32;
        if (this._rowRecords.size() % 32 != 0) {
            return size + 1;
        }
        return size;
    }

    private int getRowBlockSize(int block) {
        return getRowCountForBlock(block) * 20;
    }

    public int getRowCountForBlock(int block) {
        int startIndex = block * 32;
        int endIndex = (startIndex + 32) - 1;
        if (endIndex >= this._rowRecords.size()) {
            endIndex = this._rowRecords.size() - 1;
        }
        return (endIndex - startIndex) + 1;
    }

    private int getStartRowNumberForBlock(int block) {
        int startIndex = block * 32;
        if (this._rowRecordValues == null) {
            this._rowRecordValues = (RowRecord[]) this._rowRecords.values().toArray(new RowRecord[this._rowRecords.size()]);
        }
        try {
            return this._rowRecordValues[startIndex].getRowNumber();
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new RuntimeException("Did not find start row for block " + block);
        }
    }

    private int getEndRowNumberForBlock(int block) {
        int endIndex = ((block + 1) * 32) - 1;
        if (endIndex >= this._rowRecords.size()) {
            endIndex = this._rowRecords.size() - 1;
        }
        if (this._rowRecordValues == null) {
            this._rowRecordValues = (RowRecord[]) this._rowRecords.values().toArray(new RowRecord[this._rowRecords.size()]);
        }
        try {
            return this._rowRecordValues[endIndex].getRowNumber();
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new RuntimeException("Did not find end row for block " + block);
        }
    }

    private int visitRowRecordsForBlock(int blockIndex, RecordVisitor rv) {
        int startIndex = blockIndex * 32;
        int endIndex = startIndex + 32;
        Iterator<RowRecord> rowIterator = this._rowRecords.values().iterator();
        int i = 0;
        while (i < startIndex) {
            rowIterator.next();
            i++;
        }
        int result = 0;
        while (rowIterator.hasNext()) {
            int i2 = i + 1;
            if (i >= endIndex) {
                break;
            }
            Record rec = (Record) rowIterator.next();
            result += rec.getRecordSize();
            rv.visitRecord(rec);
            i = i2;
        }
        return result;
    }

    public void visitContainedRecords(RecordVisitor rv) {
        PositionTrackingVisitor stv = new PositionTrackingVisitor(rv, 0);
        int blockCount = getRowBlockCount();
        for (int blockIndex = 0; blockIndex < blockCount; blockIndex++) {
            int rowBlockSize = visitRowRecordsForBlock(blockIndex, rv);
            int pos = 0 + rowBlockSize;
            int startRowNumber = getStartRowNumberForBlock(blockIndex);
            int endRowNumber = getEndRowNumberForBlock(blockIndex);
            Builder dbcrBuilder = new Builder();
            int cellRefOffset = rowBlockSize - 20;
            for (int row = startRowNumber; row <= endRowNumber; row++) {
                if (this._valuesAgg.rowHasCells(row)) {
                    stv.setPosition(0);
                    this._valuesAgg.visitCellsForRow(row, stv);
                    int rowCellSize = stv.getPosition();
                    pos += rowCellSize;
                    dbcrBuilder.addCellOffset(cellRefOffset);
                    cellRefOffset = rowCellSize;
                }
            }
            rv.visitRecord(dbcrBuilder.build(pos));
        }
        for (Record _unknownRecord : this._unknownRecords) {
            rv.visitRecord(_unknownRecord);
        }
    }

    public Iterator<RowRecord> getIterator() {
        return this._rowRecords.values().iterator();
    }

    public int findStartOfRowOutlineGroup(int row) {
        short level = getRow(row).getOutlineLevel();
        int currentRow = row;
        while (currentRow >= 0 && getRow(currentRow) != null) {
            if (getRow(currentRow).getOutlineLevel() < level) {
                return currentRow + 1;
            }
            currentRow--;
        }
        return currentRow + 1;
    }

    public int findEndOfRowOutlineGroup(int row) {
        short level = getRow(row).getOutlineLevel();
        int currentRow = row;
        while (currentRow < getLastRowNum() && getRow(currentRow) != null && getRow(currentRow).getOutlineLevel() >= level) {
            currentRow++;
        }
        return currentRow - 1;
    }

    private int writeHidden(RowRecord pRowRecord, int row) {
        int rowIx = row;
        RowRecord rowRecord = pRowRecord;
        short level = rowRecord.getOutlineLevel();
        while (rowRecord != null && getRow(rowIx).getOutlineLevel() >= level) {
            rowRecord.setZeroHeight(true);
            rowIx++;
            rowRecord = getRow(rowIx);
        }
        return rowIx;
    }

    public void collapseRow(int rowNumber) {
        int startRow = findStartOfRowOutlineGroup(rowNumber);
        int nextRowIx = writeHidden(getRow(startRow), startRow);
        RowRecord row = getRow(nextRowIx);
        if (row == null) {
            row = createRow(nextRowIx);
            insertRow(row);
        }
        row.setColapsed(true);
    }

    public static RowRecord createRow(int rowNumber) {
        return new RowRecord(rowNumber);
    }

    public boolean isRowGroupCollapsed(int row) {
        int collapseRow = findEndOfRowOutlineGroup(row) + 1;
        return getRow(collapseRow) != null && getRow(collapseRow).getColapsed();
    }

    public void expandRow(int rowNumber) {
        if (rowNumber != -1 && isRowGroupCollapsed(rowNumber)) {
            int startIdx = findStartOfRowOutlineGroup(rowNumber);
            RowRecord row = getRow(startIdx);
            int endIdx = findEndOfRowOutlineGroup(rowNumber);
            if (!isRowGroupHiddenByParent(rowNumber)) {
                int i = startIdx;
                while (i <= endIdx) {
                    RowRecord otherRow = getRow(i);
                    if (row.getOutlineLevel() == otherRow.getOutlineLevel() || !isRowGroupCollapsed(i)) {
                        otherRow.setZeroHeight(false);
                    }
                    i++;
                }
            }
            getRow(endIdx + 1).setColapsed(false);
        }
    }

    public boolean isRowGroupHiddenByParent(int row) {
        int endLevel;
        boolean endHidden;
        int startLevel;
        boolean startHidden;
        int endOfOutlineGroupIdx = findEndOfRowOutlineGroup(row);
        if (getRow(endOfOutlineGroupIdx + 1) == null) {
            endLevel = 0;
            endHidden = false;
        } else {
            endLevel = getRow(endOfOutlineGroupIdx + 1).getOutlineLevel();
            endHidden = getRow(endOfOutlineGroupIdx + 1).getZeroHeight();
        }
        int startOfOutlineGroupIdx = findStartOfRowOutlineGroup(row);
        if (startOfOutlineGroupIdx - 1 < 0 || getRow(startOfOutlineGroupIdx - 1) == null) {
            startLevel = 0;
            startHidden = false;
        } else {
            startLevel = getRow(startOfOutlineGroupIdx - 1).getOutlineLevel();
            startHidden = getRow(startOfOutlineGroupIdx - 1).getZeroHeight();
        }
        if (endLevel > startLevel) {
            return endHidden;
        }
        return startHidden;
    }

    public Iterator<CellValueRecordInterface> getCellValueIterator() {
        return this._valuesAgg.iterator();
    }

    public IndexRecord createIndexRecord(int indexRecordOffset, int sizeOfInitialSheetRecords) {
        IndexRecord result = new IndexRecord();
        result.setFirstRow(this._firstrow);
        result.setLastRowAdd1(this._lastrow + 1);
        int blockCount = getRowBlockCount();
        int currentOffset = (indexRecordOffset + IndexRecord.getRecordSizeForBlockCount(blockCount)) + sizeOfInitialSheetRecords;
        for (int block = 0; block < blockCount; block++) {
            currentOffset = (currentOffset + getRowBlockSize(block)) + this._valuesAgg.getRowCellBlockSize(getStartRowNumberForBlock(block), getEndRowNumberForBlock(block));
            result.addDbcell(currentOffset);
            currentOffset += (getRowCountForBlock(block) * 2) + 8;
        }
        return result;
    }

    public void insertCell(CellValueRecordInterface cvRec) {
        this._valuesAgg.insertCell(cvRec);
    }

    public void removeCell(CellValueRecordInterface cvRec) {
        if (cvRec instanceof FormulaRecordAggregate) {
            ((FormulaRecordAggregate) cvRec).notifyFormulaChanging();
        }
        this._valuesAgg.removeCell(cvRec);
    }

    public FormulaRecordAggregate createFormula(int row, int col) {
        FormulaRecord fr = new FormulaRecord();
        fr.setRow(row);
        fr.setColumn((short) col);
        return new FormulaRecordAggregate(fr, null, this._sharedValueManager);
    }

    public void updateFormulasAfterRowShift(FormulaShifter formulaShifter, int currentExternSheetIndex) {
        this._valuesAgg.updateFormulasAfterRowShift(formulaShifter, currentExternSheetIndex);
    }

    public DimensionsRecord createDimensions() {
        DimensionsRecord result = new DimensionsRecord();
        result.setFirstRow(this._firstrow);
        result.setLastRow(this._lastrow);
        result.setFirstCol((short) this._valuesAgg.getFirstCellNum());
        result.setLastCol((short) this._valuesAgg.getLastCellNum());
        return result;
    }
}
