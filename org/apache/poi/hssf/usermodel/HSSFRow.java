package org.apache.poi.hssf.usermodel;

import java.util.Iterator;
import java.util.NoSuchElementException;
import org.apache.poi.hssf.record.CellValueRecordInterface;
import org.apache.poi.hssf.record.RowRecord;
import org.apache.poi.ss.SpreadsheetVersion;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
import org.apache.poi.util.Configurator;
import org.bytedeco.javacpp.avutil;

public final class HSSFRow implements Row, Comparable<HSSFRow> {
    public static final int INITIAL_CAPACITY = Configurator.getIntValue("HSSFRow.ColInitialCapacity", 5);
    private final HSSFWorkbook book;
    private HSSFCell[] cells;
    private final RowRecord row;
    private int rowNum;
    private final HSSFSheet sheet;

    static /* synthetic */ class C10641 {
        static final /* synthetic */ int[] $SwitchMap$org$apache$poi$ss$usermodel$Row$MissingCellPolicy = new int[MissingCellPolicy.values().length];

        static {
            try {
                $SwitchMap$org$apache$poi$ss$usermodel$Row$MissingCellPolicy[MissingCellPolicy.RETURN_NULL_AND_BLANK.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$org$apache$poi$ss$usermodel$Row$MissingCellPolicy[MissingCellPolicy.RETURN_BLANK_AS_NULL.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$org$apache$poi$ss$usermodel$Row$MissingCellPolicy[MissingCellPolicy.CREATE_NULL_AS_BLANK.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
        }
    }

    private class CellIterator implements Iterator<Cell> {
        int nextId = -1;
        int thisId = -1;

        public CellIterator() {
            findNext();
        }

        public boolean hasNext() {
            return this.nextId < HSSFRow.this.cells.length;
        }

        public Cell next() {
            if (hasNext()) {
                HSSFCell cell = HSSFRow.this.cells[this.nextId];
                this.thisId = this.nextId;
                findNext();
                return cell;
            }
            throw new NoSuchElementException("At last element");
        }

        public void remove() {
            if (this.thisId == -1) {
                throw new IllegalStateException("remove() called before next()");
            }
            HSSFRow.this.cells[this.thisId] = null;
        }

        private void findNext() {
            int i = this.nextId + 1;
            while (i < HSSFRow.this.cells.length && HSSFRow.this.cells[i] == null) {
                i++;
            }
            this.nextId = i;
        }
    }

    HSSFRow(HSSFWorkbook book, HSSFSheet sheet, int rowNum) {
        this(book, sheet, new RowRecord(rowNum));
    }

    HSSFRow(HSSFWorkbook book, HSSFSheet sheet, RowRecord record) {
        this.book = book;
        this.sheet = sheet;
        this.row = record;
        setRowNum(record.getRowNumber());
        this.cells = new HSSFCell[(record.getLastCol() + INITIAL_CAPACITY)];
        record.setEmpty();
    }

    public HSSFCell createCell(int column) {
        return createCell(column, CellType.BLANK);
    }

    public HSSFCell createCell(int columnIndex, int type) {
        return createCell(columnIndex, CellType.forInt(type));
    }

    public HSSFCell createCell(int columnIndex, CellType type) {
        short shortCellNum = (short) columnIndex;
        if (columnIndex > avutil.FF_LAMBDA_MAX) {
            shortCellNum = (short) (65535 - columnIndex);
        }
        HSSFCell cell = new HSSFCell(this.book, this.sheet, getRowNum(), shortCellNum, type);
        addCell(cell);
        this.sheet.getSheet().addValueRecord(getRowNum(), cell.getCellValueRecord());
        return cell;
    }

    public void removeCell(Cell cell) {
        if (cell == null) {
            throw new IllegalArgumentException("cell must not be null");
        }
        removeCell((HSSFCell) cell, true);
    }

    private void removeCell(HSSFCell cell, boolean alsoRemoveRecords) {
        int column = cell.getColumnIndex();
        if (column < 0) {
            throw new RuntimeException("Negative cell indexes not allowed");
        } else if (column >= this.cells.length || cell != this.cells[column]) {
            throw new RuntimeException("Specified cell is not from this row");
        } else {
            if (cell.isPartOfArrayFormulaGroup()) {
                cell.notifyArrayFormulaChanging();
            }
            this.cells[column] = null;
            if (alsoRemoveRecords) {
                this.sheet.getSheet().removeValueRecord(getRowNum(), cell.getCellValueRecord());
            }
            if (cell.getColumnIndex() + 1 == this.row.getLastCol()) {
                this.row.setLastCol(calculateNewLastCellPlusOne(this.row.getLastCol()));
            }
            if (cell.getColumnIndex() == this.row.getFirstCol()) {
                this.row.setFirstCol(calculateNewFirstCell(this.row.getFirstCol()));
            }
        }
    }

    protected void removeAllCells() {
        for (int i = 0; i < this.cells.length; i++) {
            if (this.cells[i] != null) {
                removeCell(this.cells[i], true);
            }
        }
        this.cells = new HSSFCell[INITIAL_CAPACITY];
    }

    HSSFCell createCellFromRecord(CellValueRecordInterface cell) {
        HSSFCell hcell = new HSSFCell(this.book, this.sheet, cell);
        addCell(hcell);
        int colIx = cell.getColumn();
        if (this.row.isEmpty()) {
            this.row.setFirstCol(colIx);
            this.row.setLastCol(colIx + 1);
        } else if (colIx < this.row.getFirstCol()) {
            this.row.setFirstCol(colIx);
        } else if (colIx > this.row.getLastCol()) {
            this.row.setLastCol(colIx + 1);
        }
        return hcell;
    }

    public void setRowNum(int rowIndex) {
        int maxrow = SpreadsheetVersion.EXCEL97.getLastRowIndex();
        if (rowIndex < 0 || rowIndex > maxrow) {
            throw new IllegalArgumentException("Invalid row number (" + rowIndex + ") outside allowable range (0.." + maxrow + ")");
        }
        this.rowNum = rowIndex;
        if (this.row != null) {
            this.row.setRowNumber(rowIndex);
        }
    }

    public int getRowNum() {
        return this.rowNum;
    }

    public HSSFSheet getSheet() {
        return this.sheet;
    }

    public int getOutlineLevel() {
        return this.row.getOutlineLevel();
    }

    public void moveCell(HSSFCell cell, short newColumn) {
        if (this.cells.length > newColumn && this.cells[newColumn] != null) {
            throw new IllegalArgumentException("Asked to move cell to column " + newColumn + " but there's already a cell there");
        } else if (this.cells[cell.getColumnIndex()].equals(cell)) {
            removeCell(cell, false);
            cell.updateCellNum(newColumn);
            addCell(cell);
        } else {
            throw new IllegalArgumentException("Asked to move a cell, but it didn't belong to our row");
        }
    }

    private void addCell(HSSFCell cell) {
        int column = cell.getColumnIndex();
        if (column >= this.cells.length) {
            HSSFCell[] oldCells = this.cells;
            int newSize = ((oldCells.length * 3) / 2) + 1;
            if (newSize < column + 1) {
                newSize = column + INITIAL_CAPACITY;
            }
            this.cells = new HSSFCell[newSize];
            System.arraycopy(oldCells, 0, this.cells, 0, oldCells.length);
        }
        this.cells[column] = cell;
        if (this.row.isEmpty() || column < this.row.getFirstCol()) {
            this.row.setFirstCol((short) column);
        }
        if (this.row.isEmpty() || column >= this.row.getLastCol()) {
            this.row.setLastCol((short) (column + 1));
        }
    }

    private HSSFCell retrieveCell(int cellIndex) {
        if (cellIndex < 0 || cellIndex >= this.cells.length) {
            return null;
        }
        return this.cells[cellIndex];
    }

    public HSSFCell getCell(int cellnum) {
        return getCell(cellnum, this.book.getMissingCellPolicy());
    }

    public HSSFCell getCell(int cellnum, MissingCellPolicy policy) {
        HSSFCell cell = retrieveCell(cellnum);
        switch (C10641.$SwitchMap$org$apache$poi$ss$usermodel$Row$MissingCellPolicy[policy.ordinal()]) {
            case 1:
                return cell;
            case 2:
                boolean isBlank = cell != null && cell.getCellTypeEnum() == CellType.BLANK;
                if (isBlank) {
                    return null;
                }
                return cell;
            case 3:
                if (cell == null) {
                    return createCell(cellnum, CellType.BLANK);
                }
                return cell;
            default:
                throw new IllegalArgumentException("Illegal policy " + policy + " (" + policy.id + ")");
        }
    }

    public short getFirstCellNum() {
        if (this.row.isEmpty()) {
            return (short) -1;
        }
        return (short) this.row.getFirstCol();
    }

    public short getLastCellNum() {
        if (this.row.isEmpty()) {
            return (short) -1;
        }
        return (short) this.row.getLastCol();
    }

    public int getPhysicalNumberOfCells() {
        int count = 0;
        for (HSSFCell hSSFCell : this.cells) {
            if (hSSFCell != null) {
                count++;
            }
        }
        return count;
    }

    public void setHeight(short height) {
        if (height == (short) -1) {
            this.row.setHeight((short) -32513);
            this.row.setBadFontHeight(false);
            return;
        }
        this.row.setBadFontHeight(true);
        this.row.setHeight(height);
    }

    public void setZeroHeight(boolean zHeight) {
        this.row.setZeroHeight(zHeight);
    }

    public boolean getZeroHeight() {
        return this.row.getZeroHeight();
    }

    public void setHeightInPoints(float height) {
        if (height == -1.0f) {
            this.row.setHeight((short) -32513);
            return;
        }
        this.row.setBadFontHeight(true);
        this.row.setHeight((short) ((int) (20.0f * height)));
    }

    public short getHeight() {
        short height = this.row.getHeight();
        if ((32768 & height) != 0) {
            return this.sheet.getSheet().getDefaultRowHeight();
        }
        return (short) (height & avutil.FF_LAMBDA_MAX);
    }

    public float getHeightInPoints() {
        return ((float) getHeight()) / 20.0f;
    }

    protected RowRecord getRowRecord() {
        return this.row;
    }

    private int calculateNewLastCellPlusOne(int lastcell) {
        int cellIx = lastcell - 1;
        HSSFCell r = retrieveCell(cellIx);
        while (r == null) {
            if (cellIx < 0) {
                return 0;
            }
            cellIx--;
            r = retrieveCell(cellIx);
        }
        return cellIx + 1;
    }

    private int calculateNewFirstCell(int firstcell) {
        int cellIx = firstcell + 1;
        HSSFCell r = retrieveCell(cellIx);
        while (r == null) {
            if (cellIx <= this.cells.length) {
                return 0;
            }
            cellIx++;
            r = retrieveCell(cellIx);
        }
        return cellIx;
    }

    public boolean isFormatted() {
        return this.row.getFormatted();
    }

    public HSSFCellStyle getRowStyle() {
        if (!isFormatted()) {
            return null;
        }
        short styleIndex = this.row.getXFIndex();
        return new HSSFCellStyle(styleIndex, this.book.getWorkbook().getExFormatAt(styleIndex), this.book);
    }

    public void setRowStyle(HSSFCellStyle style) {
        this.row.setFormatted(true);
        this.row.setXFIndex(style.getIndex());
    }

    public void setRowStyle(CellStyle style) {
        setRowStyle((HSSFCellStyle) style);
    }

    public Iterator<Cell> cellIterator() {
        return new CellIterator();
    }

    public Iterator<Cell> iterator() {
        return cellIterator();
    }

    public int compareTo(HSSFRow other) {
        if (getSheet() == other.getSheet()) {
            return Integer.valueOf(getRowNum()).compareTo(Integer.valueOf(other.getRowNum()));
        }
        throw new IllegalArgumentException("The compared rows must belong to the same sheet");
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof HSSFRow)) {
            return false;
        }
        HSSFRow other = (HSSFRow) obj;
        if (getRowNum() == other.getRowNum() && getSheet() == other.getSheet()) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return this.row.hashCode();
    }
}
