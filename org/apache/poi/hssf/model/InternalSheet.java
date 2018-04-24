package org.apache.poi.hssf.model;

import android.support.v4.view.MotionEventCompat;
import com.itextpdf.text.pdf.codec.wmf.MetaDo;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.poi.hssf.record.BOFRecord;
import org.apache.poi.hssf.record.CalcCountRecord;
import org.apache.poi.hssf.record.CalcModeRecord;
import org.apache.poi.hssf.record.CellValueRecordInterface;
import org.apache.poi.hssf.record.ColumnInfoRecord;
import org.apache.poi.hssf.record.DefaultColWidthRecord;
import org.apache.poi.hssf.record.DefaultRowHeightRecord;
import org.apache.poi.hssf.record.DeltaRecord;
import org.apache.poi.hssf.record.DimensionsRecord;
import org.apache.poi.hssf.record.DrawingRecord;
import org.apache.poi.hssf.record.EOFRecord;
import org.apache.poi.hssf.record.EscherAggregate;
import org.apache.poi.hssf.record.GridsetRecord;
import org.apache.poi.hssf.record.GutsRecord;
import org.apache.poi.hssf.record.IterationRecord;
import org.apache.poi.hssf.record.NoteRecord;
import org.apache.poi.hssf.record.PaneRecord;
import org.apache.poi.hssf.record.PrintGridlinesRecord;
import org.apache.poi.hssf.record.PrintHeadersRecord;
import org.apache.poi.hssf.record.Record;
import org.apache.poi.hssf.record.RecordBase;
import org.apache.poi.hssf.record.RecordFormatException;
import org.apache.poi.hssf.record.RefModeRecord;
import org.apache.poi.hssf.record.RowRecord;
import org.apache.poi.hssf.record.SCLRecord;
import org.apache.poi.hssf.record.SaveRecalcRecord;
import org.apache.poi.hssf.record.SelectionRecord;
import org.apache.poi.hssf.record.UncalcedRecord;
import org.apache.poi.hssf.record.UnknownRecord;
import org.apache.poi.hssf.record.WSBoolRecord;
import org.apache.poi.hssf.record.WindowTwoRecord;
import org.apache.poi.hssf.record.aggregates.ChartSubstreamRecordAggregate;
import org.apache.poi.hssf.record.aggregates.ColumnInfoRecordsAggregate;
import org.apache.poi.hssf.record.aggregates.ConditionalFormattingTable;
import org.apache.poi.hssf.record.aggregates.CustomViewSettingsRecordAggregate;
import org.apache.poi.hssf.record.aggregates.DataValidityTable;
import org.apache.poi.hssf.record.aggregates.MergedCellsTable;
import org.apache.poi.hssf.record.aggregates.PageSettingsBlock;
import org.apache.poi.hssf.record.aggregates.RecordAggregate;
import org.apache.poi.hssf.record.aggregates.RecordAggregate.PositionTrackingVisitor;
import org.apache.poi.hssf.record.aggregates.RecordAggregate.RecordVisitor;
import org.apache.poi.hssf.record.aggregates.RowRecordsAggregate;
import org.apache.poi.hssf.record.aggregates.WorksheetProtectionBlock;
import org.apache.poi.hssf.usermodel.HSSFShapeTypes;
import org.apache.poi.ss.formula.FormulaShifter;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.PaneInformation;
import org.apache.poi.util.Internal;
import org.apache.poi.util.POILogFactory;
import org.apache.poi.util.POILogger;

@Internal
public final class InternalSheet {
    public static final short BottomMargin = (short) 3;
    public static final short LeftMargin = (short) 0;
    public static final byte PANE_LOWER_LEFT = (byte) 2;
    public static final byte PANE_LOWER_RIGHT = (byte) 0;
    public static final byte PANE_UPPER_LEFT = (byte) 3;
    public static final byte PANE_UPPER_RIGHT = (byte) 1;
    public static final short RightMargin = (short) 1;
    public static final short TopMargin = (short) 2;
    private static POILogger log = POILogFactory.getLogger(InternalSheet.class);
    ColumnInfoRecordsAggregate _columnInfos;
    private DataValidityTable _dataValidityTable = null;
    private DimensionsRecord _dimensions;
    private GutsRecord _gutsRecord;
    protected boolean _isUncalced = false;
    private final MergedCellsTable _mergedCellsTable = new MergedCellsTable();
    private final WorksheetProtectionBlock _protectionBlock = new WorksheetProtectionBlock();
    private PageSettingsBlock _psBlock;
    private List<RecordBase> _records;
    protected final RowRecordsAggregate _rowsAggregate;
    protected SelectionRecord _selection = null;
    private ConditionalFormattingTable condFormatting;
    protected DefaultColWidthRecord defaultcolwidth = new DefaultColWidthRecord();
    protected DefaultRowHeightRecord defaultrowheight = new DefaultRowHeightRecord();
    protected GridsetRecord gridset = null;
    protected PrintGridlinesRecord printGridlines = null;
    protected PrintHeadersRecord printHeaders = null;
    private Iterator<RowRecord> rowRecIterator = null;
    protected WindowTwoRecord windowTwo = null;

    public static InternalSheet createSheet(RecordStream rs) {
        return new InternalSheet(rs);
    }

    private InternalSheet(RecordStream rs) {
        RowRecordsAggregate rra = null;
        List<RecordBase> records = new ArrayList(128);
        this._records = records;
        int dimsloc = -1;
        if (rs.peekNextSid() != 2057) {
            throw new RecordFormatException("BOF record expected");
        }
        BOFRecord bof = (BOFRecord) rs.getNext();
        if (bof.getType() == 16 || bof.getType() == 32 || bof.getType() == 64) {
            records.add(bof);
            while (rs.hasNext()) {
                int recSid = rs.peekNextSid();
                if (recSid == 432 || recSid == 2169) {
                    this.condFormatting = new ConditionalFormattingTable(rs);
                    records.add(this.condFormatting);
                } else if (recSid == 125) {
                    this._columnInfos = new ColumnInfoRecordsAggregate(rs);
                    records.add(this._columnInfos);
                } else if (recSid == 434) {
                    this._dataValidityTable = new DataValidityTable(rs);
                    records.add(this._dataValidityTable);
                } else if (RecordOrderer.isRowBlockRecord(recSid)) {
                    if (rra != null) {
                        throw new RecordFormatException("row/cell records found in the wrong place");
                    }
                    RowBlocksReader rbr = new RowBlocksReader(rs);
                    this._mergedCellsTable.addRecords(rbr.getLooseMergedCells());
                    rra = new RowRecordsAggregate(rbr.getPlainRecordStream(), rbr.getSharedFormulaManager());
                    records.add(rra);
                } else if (CustomViewSettingsRecordAggregate.isBeginRecord(recSid)) {
                    records.add(new CustomViewSettingsRecordAggregate(rs));
                } else if (PageSettingsBlock.isComponentRecord(recSid)) {
                    if (this._psBlock == null) {
                        this._psBlock = new PageSettingsBlock(rs);
                        records.add(this._psBlock);
                    } else {
                        this._psBlock.addLateRecords(rs);
                    }
                    this._psBlock.positionRecords(records);
                } else if (WorksheetProtectionBlock.isComponentRecord(recSid)) {
                    this._protectionBlock.addRecords(rs);
                } else if (recSid == 229) {
                    this._mergedCellsTable.read(rs);
                } else if (recSid == 2057) {
                    spillAggregate(new ChartSubstreamRecordAggregate(rs), records);
                } else {
                    Record rec = rs.getNext();
                    if (recSid == MetaDo.META_SETWINDOWORG) {
                        continue;
                    } else if (recSid == 94) {
                        this._isUncalced = true;
                    } else if (recSid == 2152 || recSid == UnknownRecord.SHEETPROTECTION_0867) {
                        records.add(rec);
                    } else if (recSid == 10) {
                        records.add(rec);
                        break;
                    } else {
                        if (recSid == 512) {
                            if (this._columnInfos == null) {
                                this._columnInfos = new ColumnInfoRecordsAggregate();
                                records.add(this._columnInfos);
                            }
                            this._dimensions = (DimensionsRecord) rec;
                            dimsloc = records.size();
                        } else if (recSid == 85) {
                            this.defaultcolwidth = (DefaultColWidthRecord) rec;
                        } else if (recSid == 549) {
                            this.defaultrowheight = (DefaultRowHeightRecord) rec;
                        } else if (recSid == 43) {
                            this.printGridlines = (PrintGridlinesRecord) rec;
                        } else if (recSid == 42) {
                            this.printHeaders = (PrintHeadersRecord) rec;
                        } else if (recSid == 130) {
                            this.gridset = (GridsetRecord) rec;
                        } else if (recSid == 29) {
                            this._selection = (SelectionRecord) rec;
                        } else if (recSid == 574) {
                            this.windowTwo = (WindowTwoRecord) rec;
                        } else if (recSid == 128) {
                            this._gutsRecord = (GutsRecord) rec;
                        }
                        records.add(rec);
                    }
                }
            }
            if (this.windowTwo == null) {
                throw new RecordFormatException("WINDOW2 was not found");
            }
            if (this._dimensions == null) {
                if (rra == null) {
                    rra = new RowRecordsAggregate();
                } else if (log.check(5)) {
                    log.log(5, "DIMENSION record not found even though row/cells present");
                }
                dimsloc = findFirstRecordLocBySid((short) 574);
                this._dimensions = rra.createDimensions();
                records.add(dimsloc, this._dimensions);
            }
            if (rra == null) {
                rra = new RowRecordsAggregate();
                records.add(dimsloc + 1, rra);
            }
            this._rowsAggregate = rra;
            RecordOrderer.addNewSheetRecord(records, this._mergedCellsTable);
            RecordOrderer.addNewSheetRecord(records, this._protectionBlock);
            if (log.check(1)) {
                log.log(1, "sheet createSheet (existing file) exited");
                return;
            }
            return;
        }
        while (rs.hasNext()) {
            if (rs.getNext() instanceof EOFRecord) {
                break;
            }
        }
        throw new UnsupportedBOFType(bof.getType());
    }

    private static void spillAggregate(RecordAggregate ra, List<RecordBase> recs) {
        ra.visitContainedRecords(new 1(recs));
    }

    public InternalSheet cloneSheet() {
        List<Record> clonedRecords = new ArrayList(this._records.size());
        for (int i = 0; i < this._records.size(); i++) {
            RecordBase rb = (RecordBase) this._records.get(i);
            if (rb instanceof RecordAggregate) {
                ((RecordAggregate) rb).visitContainedRecords(new RecordCloner(clonedRecords));
            } else {
                if (rb instanceof EscherAggregate) {
                    rb = new DrawingRecord();
                }
                try {
                    clonedRecords.add((Record) ((Record) rb).clone());
                } catch (CloneNotSupportedException e) {
                    throw new RecordFormatException(e);
                }
            }
        }
        return createSheet(new RecordStream(clonedRecords, 0));
    }

    public static InternalSheet createSheet() {
        return new InternalSheet();
    }

    private InternalSheet() {
        List<RecordBase> records = new ArrayList(32);
        if (log.check(1)) {
            log.log(1, "Sheet createsheet from scratch called");
        }
        records.add(createBOF());
        records.add(createCalcMode());
        records.add(createCalcCount());
        records.add(createRefMode());
        records.add(createIteration());
        records.add(createDelta());
        records.add(createSaveRecalc());
        this.printHeaders = createPrintHeaders();
        records.add(this.printHeaders);
        this.printGridlines = createPrintGridlines();
        records.add(this.printGridlines);
        this.gridset = createGridset();
        records.add(this.gridset);
        this._gutsRecord = createGuts();
        records.add(this._gutsRecord);
        this.defaultrowheight = createDefaultRowHeight();
        records.add(this.defaultrowheight);
        records.add(createWSBool());
        this._psBlock = new PageSettingsBlock();
        records.add(this._psBlock);
        records.add(this._protectionBlock);
        this.defaultcolwidth = createDefaultColWidth();
        records.add(this.defaultcolwidth);
        ColumnInfoRecordsAggregate columns = new ColumnInfoRecordsAggregate();
        records.add(columns);
        this._columnInfos = columns;
        this._dimensions = createDimensions();
        records.add(this._dimensions);
        this._rowsAggregate = new RowRecordsAggregate();
        records.add(this._rowsAggregate);
        WindowTwoRecord createWindowTwo = createWindowTwo();
        this.windowTwo = createWindowTwo;
        records.add(createWindowTwo);
        this._selection = createSelection();
        records.add(this._selection);
        records.add(this._mergedCellsTable);
        records.add(EOFRecord.instance);
        this._records = records;
        if (log.check(1)) {
            log.log(1, "Sheet createsheet from scratch exit");
        }
    }

    public RowRecordsAggregate getRowsAggregate() {
        return this._rowsAggregate;
    }

    private MergedCellsTable getMergedRecords() {
        return this._mergedCellsTable;
    }

    public void updateFormulasAfterCellShift(FormulaShifter shifter, int externSheetIndex) {
        getRowsAggregate().updateFormulasAfterRowShift(shifter, externSheetIndex);
        if (this.condFormatting != null) {
            getConditionalFormattingTable().updateFormulasAfterCellShift(shifter, externSheetIndex);
        }
    }

    public int addMergedRegion(int rowFrom, int colFrom, int rowTo, int colTo) {
        if (rowTo < rowFrom) {
            throw new IllegalArgumentException("The 'to' row (" + rowTo + ") must not be less than the 'from' row (" + rowFrom + ")");
        } else if (colTo < colFrom) {
            throw new IllegalArgumentException("The 'to' col (" + colTo + ") must not be less than the 'from' col (" + colFrom + ")");
        } else {
            MergedCellsTable mrt = getMergedRecords();
            mrt.addArea(rowFrom, colFrom, rowTo, colTo);
            return mrt.getNumberOfMergedRegions() - 1;
        }
    }

    public void removeMergedRegion(int index) {
        MergedCellsTable mrt = getMergedRecords();
        if (index < mrt.getNumberOfMergedRegions()) {
            mrt.remove(index);
        }
    }

    public CellRangeAddress getMergedRegionAt(int index) {
        MergedCellsTable mrt = getMergedRecords();
        if (index >= mrt.getNumberOfMergedRegions()) {
            return null;
        }
        return mrt.get(index);
    }

    public int getNumMergedRegions() {
        return getMergedRecords().getNumberOfMergedRegions();
    }

    public ConditionalFormattingTable getConditionalFormattingTable() {
        if (this.condFormatting == null) {
            this.condFormatting = new ConditionalFormattingTable();
            RecordOrderer.addNewSheetRecord(this._records, this.condFormatting);
        }
        return this.condFormatting;
    }

    public void setDimensions(int firstrow, short firstcol, int lastrow, short lastcol) {
        if (log.check(1)) {
            log.log(1, "Sheet.setDimensions");
            log.log(1, new StringBuffer("firstrow").append(firstrow).append("firstcol").append(firstcol).append("lastrow").append(lastrow).append("lastcol").append(lastcol).toString());
        }
        this._dimensions.setFirstCol(firstcol);
        this._dimensions.setFirstRow(firstrow);
        this._dimensions.setLastCol(lastcol);
        this._dimensions.setLastRow(lastrow);
        if (log.check(1)) {
            log.log(1, "Sheet.setDimensions exiting");
        }
    }

    public void visitContainedRecords(RecordVisitor rv, int offset) {
        PositionTrackingVisitor ptv = new PositionTrackingVisitor(rv, offset);
        boolean haveSerializedIndex = false;
        for (int k = 0; k < this._records.size(); k++) {
            RecordBase record = (RecordBase) this._records.get(k);
            if (record instanceof RecordAggregate) {
                ((RecordAggregate) record).visitContainedRecords(ptv);
            } else {
                ptv.visitRecord((Record) record);
            }
            if ((record instanceof BOFRecord) && !haveSerializedIndex) {
                haveSerializedIndex = true;
                if (this._isUncalced) {
                    ptv.visitRecord(new UncalcedRecord());
                }
                if (this._rowsAggregate != null) {
                    int initRecsSize = getSizeOfInitialSheetRecords(k);
                    ptv.visitRecord(this._rowsAggregate.createIndexRecord(ptv.getPosition(), initRecsSize));
                }
            }
        }
    }

    private int getSizeOfInitialSheetRecords(int bofRecordIndex) {
        int result = 0;
        for (int j = bofRecordIndex + 1; j < this._records.size(); j++) {
            RecordBase tmpRec = (RecordBase) this._records.get(j);
            if (tmpRec instanceof RowRecordsAggregate) {
                break;
            }
            result += tmpRec.getRecordSize();
        }
        if (this._isUncalced) {
            return result + UncalcedRecord.getStaticRecordSize();
        }
        return result;
    }

    public void addValueRecord(int row, CellValueRecordInterface col) {
        if (log.check(1)) {
            log.log(1, "add value record  row" + row);
        }
        DimensionsRecord d = this._dimensions;
        if (col.getColumn() >= d.getLastCol()) {
            d.setLastCol((short) (col.getColumn() + 1));
        }
        if (col.getColumn() < d.getFirstCol()) {
            d.setFirstCol(col.getColumn());
        }
        this._rowsAggregate.insertCell(col);
    }

    public void removeValueRecord(int row, CellValueRecordInterface col) {
        log.log(1, "remove value record row " + row);
        this._rowsAggregate.removeCell(col);
    }

    public void replaceValueRecord(CellValueRecordInterface newval) {
        if (log.check(1)) {
            log.log(1, "replaceValueRecord ");
        }
        this._rowsAggregate.removeCell(newval);
        this._rowsAggregate.insertCell(newval);
    }

    public void addRow(RowRecord row) {
        if (log.check(1)) {
            log.log(1, "addRow ");
        }
        DimensionsRecord d = this._dimensions;
        if (row.getRowNumber() >= d.getLastRow()) {
            d.setLastRow(row.getRowNumber() + 1);
        }
        if (row.getRowNumber() < d.getFirstRow()) {
            d.setFirstRow(row.getRowNumber());
        }
        RowRecord existingRow = this._rowsAggregate.getRow(row.getRowNumber());
        if (existingRow != null) {
            this._rowsAggregate.removeRow(existingRow);
        }
        this._rowsAggregate.insertRow(row);
        if (log.check(1)) {
            log.log(1, "exit addRow");
        }
    }

    public void removeRow(RowRecord row) {
        this._rowsAggregate.removeRow(row);
    }

    public Iterator<CellValueRecordInterface> getCellValueIterator() {
        return this._rowsAggregate.getCellValueIterator();
    }

    public RowRecord getNextRow() {
        if (this.rowRecIterator == null) {
            this.rowRecIterator = this._rowsAggregate.getIterator();
        }
        if (this.rowRecIterator.hasNext()) {
            return (RowRecord) this.rowRecIterator.next();
        }
        return null;
    }

    public RowRecord getRow(int rownum) {
        return this._rowsAggregate.getRow(rownum);
    }

    static BOFRecord createBOF() {
        BOFRecord retval = new BOFRecord();
        retval.setVersion(BOFRecord.VERSION);
        retval.setType(16);
        retval.setBuild(3515);
        retval.setBuildYear(BOFRecord.BUILD_YEAR);
        retval.setHistoryBitMask(HSSFShapeTypes.ActionButtonForwardNext);
        retval.setRequiredVersion(6);
        return retval;
    }

    private static CalcModeRecord createCalcMode() {
        CalcModeRecord retval = new CalcModeRecord();
        retval.setCalcMode((short) 1);
        return retval;
    }

    private static CalcCountRecord createCalcCount() {
        CalcCountRecord retval = new CalcCountRecord();
        retval.setIterations((short) 100);
        return retval;
    }

    private static RefModeRecord createRefMode() {
        RefModeRecord retval = new RefModeRecord();
        retval.setMode((short) 1);
        return retval;
    }

    private static IterationRecord createIteration() {
        return new IterationRecord(false);
    }

    private static DeltaRecord createDelta() {
        return new DeltaRecord(DeltaRecord.DEFAULT_VALUE);
    }

    private static SaveRecalcRecord createSaveRecalc() {
        SaveRecalcRecord retval = new SaveRecalcRecord();
        retval.setRecalc(true);
        return retval;
    }

    private static PrintHeadersRecord createPrintHeaders() {
        PrintHeadersRecord retval = new PrintHeadersRecord();
        retval.setPrintHeaders(false);
        return retval;
    }

    private static PrintGridlinesRecord createPrintGridlines() {
        PrintGridlinesRecord retval = new PrintGridlinesRecord();
        retval.setPrintGridlines(false);
        return retval;
    }

    private static GridsetRecord createGridset() {
        GridsetRecord retval = new GridsetRecord();
        retval.setGridset(true);
        return retval;
    }

    private static GutsRecord createGuts() {
        GutsRecord retval = new GutsRecord();
        retval.setLeftRowGutter((short) 0);
        retval.setTopColGutter((short) 0);
        retval.setRowLevelMax((short) 0);
        retval.setColLevelMax((short) 0);
        return retval;
    }

    private GutsRecord getGutsRecord() {
        if (this._gutsRecord == null) {
            GutsRecord result = createGuts();
            RecordOrderer.addNewSheetRecord(this._records, result);
            this._gutsRecord = result;
        }
        return this._gutsRecord;
    }

    private static DefaultRowHeightRecord createDefaultRowHeight() {
        DefaultRowHeightRecord retval = new DefaultRowHeightRecord();
        retval.setOptionFlags((short) 0);
        retval.setRowHeight((short) 255);
        return retval;
    }

    private static WSBoolRecord createWSBool() {
        WSBoolRecord retval = new WSBoolRecord();
        retval.setWSBool1((byte) 4);
        retval.setWSBool2((byte) -63);
        return retval;
    }

    private static DefaultColWidthRecord createDefaultColWidth() {
        DefaultColWidthRecord retval = new DefaultColWidthRecord();
        retval.setColWidth(8);
        return retval;
    }

    public int getDefaultColumnWidth() {
        return this.defaultcolwidth.getColWidth();
    }

    public boolean isGridsPrinted() {
        if (this.gridset == null) {
            this.gridset = createGridset();
            this._records.add(findFirstRecordLocBySid((short) 10), this.gridset);
        }
        return !this.gridset.getGridset();
    }

    public void setGridsPrinted(boolean value) {
        this.gridset.setGridset(!value);
    }

    public void setDefaultColumnWidth(int dcw) {
        this.defaultcolwidth.setColWidth(dcw);
    }

    public void setDefaultRowHeight(short dch) {
        this.defaultrowheight.setRowHeight(dch);
        this.defaultrowheight.setOptionFlags((short) 1);
    }

    public short getDefaultRowHeight() {
        return this.defaultrowheight.getRowHeight();
    }

    public int getColumnWidth(int columnIndex) {
        ColumnInfoRecord ci = this._columnInfos.findColumnInfo(columnIndex);
        if (ci != null) {
            return ci.getColumnWidth();
        }
        return this.defaultcolwidth.getColWidth() * 256;
    }

    public short getXFIndexForColAt(short columnIndex) {
        ColumnInfoRecord ci = this._columnInfos.findColumnInfo(columnIndex);
        if (ci != null) {
            return (short) ci.getXFIndex();
        }
        return (short) 15;
    }

    public void setColumnWidth(int column, int width) {
        if (width > MotionEventCompat.ACTION_POINTER_INDEX_MASK) {
            throw new IllegalArgumentException("The maximum column width for an individual cell is 255 characters.");
        }
        setColumn(column, null, Integer.valueOf(width), null, null, null);
    }

    public boolean isColumnHidden(int columnIndex) {
        ColumnInfoRecord cir = this._columnInfos.findColumnInfo(columnIndex);
        if (cir == null) {
            return false;
        }
        return cir.getHidden();
    }

    public void setColumnHidden(int column, boolean hidden) {
        setColumn(column, null, null, null, Boolean.valueOf(hidden), null);
    }

    public void setDefaultColumnStyle(int column, int styleIndex) {
        setColumn(column, Short.valueOf((short) styleIndex), null, null, null, null);
    }

    private void setColumn(int column, Short xfStyle, Integer width, Integer level, Boolean hidden, Boolean collapsed) {
        this._columnInfos.setColumn(column, xfStyle, width, level, hidden, collapsed);
    }

    public void groupColumnRange(int fromColumn, int toColumn, boolean indent) {
        this._columnInfos.groupColumnRange(fromColumn, toColumn, indent);
        int maxLevel = this._columnInfos.getMaxOutlineLevel();
        GutsRecord guts = getGutsRecord();
        guts.setColLevelMax((short) (maxLevel + 1));
        if (maxLevel == 0) {
            guts.setTopColGutter((short) 0);
        } else {
            guts.setTopColGutter((short) (((maxLevel - 1) * 12) + 29));
        }
    }

    private static DimensionsRecord createDimensions() {
        DimensionsRecord retval = new DimensionsRecord();
        retval.setFirstCol((short) 0);
        retval.setLastRow(1);
        retval.setFirstRow(0);
        retval.setLastCol((short) 1);
        return retval;
    }

    private static WindowTwoRecord createWindowTwo() {
        WindowTwoRecord retval = new WindowTwoRecord();
        retval.setOptions((short) 1718);
        retval.setTopRow((short) 0);
        retval.setLeftCol((short) 0);
        retval.setHeaderColor(64);
        retval.setPageBreakZoom((short) 0);
        retval.setNormalZoom((short) 0);
        return retval;
    }

    private static SelectionRecord createSelection() {
        return new SelectionRecord(0, 0);
    }

    public short getTopRow() {
        return this.windowTwo == null ? (short) 0 : this.windowTwo.getTopRow();
    }

    public void setTopRow(short topRow) {
        if (this.windowTwo != null) {
            this.windowTwo.setTopRow(topRow);
        }
    }

    public void setLeftCol(short leftCol) {
        if (this.windowTwo != null) {
            this.windowTwo.setLeftCol(leftCol);
        }
    }

    public short getLeftCol() {
        return this.windowTwo == null ? (short) 0 : this.windowTwo.getLeftCol();
    }

    public int getActiveCellRow() {
        if (this._selection == null) {
            return 0;
        }
        return this._selection.getActiveCellRow();
    }

    public void setActiveCellRow(int row) {
        if (this._selection != null) {
            this._selection.setActiveCellRow(row);
        }
    }

    public short getActiveCellCol() {
        if (this._selection == null) {
            return (short) 0;
        }
        return (short) this._selection.getActiveCellCol();
    }

    public void setActiveCellCol(short col) {
        if (this._selection != null) {
            this._selection.setActiveCellCol(col);
        }
    }

    public List<RecordBase> getRecords() {
        return this._records;
    }

    public GridsetRecord getGridsetRecord() {
        return this.gridset;
    }

    public Record findFirstRecordBySid(short sid) {
        int ix = findFirstRecordLocBySid(sid);
        if (ix < 0) {
            return null;
        }
        return (Record) this._records.get(ix);
    }

    public void setSCLRecord(SCLRecord sclRecord) {
        int oldRecordLoc = findFirstRecordLocBySid((short) 160);
        if (oldRecordLoc == -1) {
            this._records.add(findFirstRecordLocBySid((short) 574) + 1, sclRecord);
            return;
        }
        this._records.set(oldRecordLoc, sclRecord);
    }

    public int findFirstRecordLocBySid(short sid) {
        int max = this._records.size();
        for (int i = 0; i < max; i++) {
            Record rb = this._records.get(i);
            if ((rb instanceof Record) && rb.getSid() == sid) {
                return i;
            }
        }
        return -1;
    }

    public WindowTwoRecord getWindowTwo() {
        return this.windowTwo;
    }

    public PrintGridlinesRecord getPrintGridlines() {
        return this.printGridlines;
    }

    public void setPrintGridlines(PrintGridlinesRecord newPrintGridlines) {
        this.printGridlines = newPrintGridlines;
    }

    public PrintHeadersRecord getPrintHeaders() {
        return this.printHeaders;
    }

    public void setPrintHeaders(PrintHeadersRecord newPrintHeaders) {
        this.printHeaders = newPrintHeaders;
    }

    public void setSelected(boolean sel) {
        this.windowTwo.setSelected(sel);
    }

    public void createFreezePane(int colSplit, int rowSplit, int topRow, int leftmostColumn) {
        int paneLoc = findFirstRecordLocBySid((short) 65);
        if (paneLoc != -1) {
            this._records.remove(paneLoc);
        }
        if (colSplit == 0 && rowSplit == 0) {
            this.windowTwo.setFreezePanes(false);
            this.windowTwo.setFreezePanesNoSplit(false);
            ((SelectionRecord) findFirstRecordBySid((short) 29)).setPane((byte) 3);
            return;
        }
        int loc = findFirstRecordLocBySid((short) 574);
        PaneRecord pane = new PaneRecord();
        pane.setX((short) colSplit);
        pane.setY((short) rowSplit);
        pane.setTopRow((short) topRow);
        pane.setLeftColumn((short) leftmostColumn);
        if (rowSplit == 0) {
            pane.setTopRow((short) 0);
            pane.setActivePane((short) 1);
        } else if (colSplit == 0) {
            pane.setLeftColumn((short) 0);
            pane.setActivePane((short) 2);
        } else {
            pane.setActivePane((short) 0);
        }
        this._records.add(loc + 1, pane);
        this.windowTwo.setFreezePanes(true);
        this.windowTwo.setFreezePanesNoSplit(true);
        ((SelectionRecord) findFirstRecordBySid((short) 29)).setPane((byte) pane.getActivePane());
    }

    public void createSplitPane(int xSplitPos, int ySplitPos, int topRow, int leftmostColumn, int activePane) {
        int paneLoc = findFirstRecordLocBySid((short) 65);
        if (paneLoc != -1) {
            this._records.remove(paneLoc);
        }
        int loc = findFirstRecordLocBySid((short) 574);
        PaneRecord r = new PaneRecord();
        r.setX((short) xSplitPos);
        r.setY((short) ySplitPos);
        r.setTopRow((short) topRow);
        r.setLeftColumn((short) leftmostColumn);
        r.setActivePane((short) activePane);
        this._records.add(loc + 1, r);
        this.windowTwo.setFreezePanes(false);
        this.windowTwo.setFreezePanesNoSplit(false);
        ((SelectionRecord) findFirstRecordBySid((short) 29)).setPane((byte) 0);
    }

    public PaneInformation getPaneInformation() {
        PaneRecord rec = (PaneRecord) findFirstRecordBySid((short) 65);
        if (rec == null) {
            return null;
        }
        return new PaneInformation(rec.getX(), rec.getY(), rec.getTopRow(), rec.getLeftColumn(), (byte) rec.getActivePane(), this.windowTwo.getFreezePanes());
    }

    public SelectionRecord getSelection() {
        return this._selection;
    }

    public void setSelection(SelectionRecord selection) {
        this._selection = selection;
    }

    public WorksheetProtectionBlock getProtectionBlock() {
        return this._protectionBlock;
    }

    public void setDisplayGridlines(boolean show) {
        this.windowTwo.setDisplayGridlines(show);
    }

    public boolean isDisplayGridlines() {
        return this.windowTwo.getDisplayGridlines();
    }

    public void setDisplayFormulas(boolean show) {
        this.windowTwo.setDisplayFormulas(show);
    }

    public boolean isDisplayFormulas() {
        return this.windowTwo.getDisplayFormulas();
    }

    public void setDisplayRowColHeadings(boolean show) {
        this.windowTwo.setDisplayRowColHeadings(show);
    }

    public boolean isDisplayRowColHeadings() {
        return this.windowTwo.getDisplayRowColHeadings();
    }

    public void setPrintRowColHeadings(boolean show) {
        this.windowTwo.setDisplayRowColHeadings(show);
    }

    public boolean isPrintRowColHeadings() {
        return this.windowTwo.getDisplayRowColHeadings();
    }

    public boolean getUncalced() {
        return this._isUncalced;
    }

    public void setUncalced(boolean uncalced) {
        this._isUncalced = uncalced;
    }

    public int aggregateDrawingRecords(DrawingManager2 drawingManager, boolean createIfMissing) {
        int loc = findFirstRecordLocBySid((short) 236);
        if (!(loc == -1)) {
            EscherAggregate.createAggregate(getRecords(), loc);
            return loc;
        } else if (!createIfMissing) {
            return -1;
        } else {
            EscherAggregate aggregate = new EscherAggregate(true);
            loc = findFirstRecordLocBySid(EscherAggregate.sid);
            if (loc == -1) {
                loc = findFirstRecordLocBySid((short) 574);
            } else {
                getRecords().remove(loc);
            }
            getRecords().add(loc, aggregate);
            return loc;
        }
    }

    public void preSerialize() {
        for (RecordBase r : getRecords()) {
            if (r instanceof EscherAggregate) {
                r.getRecordSize();
            }
        }
    }

    public PageSettingsBlock getPageSettings() {
        if (this._psBlock == null) {
            this._psBlock = new PageSettingsBlock();
            RecordOrderer.addNewSheetRecord(this._records, this._psBlock);
        }
        return this._psBlock;
    }

    public void setColumnGroupCollapsed(int columnNumber, boolean collapsed) {
        if (collapsed) {
            this._columnInfos.collapseColumn(columnNumber);
        } else {
            this._columnInfos.expandColumn(columnNumber);
        }
    }

    public void groupRowRange(int fromRow, int toRow, boolean indent) {
        for (int rowNum = fromRow; rowNum <= toRow; rowNum++) {
            RowRecord row = getRow(rowNum);
            if (row == null) {
                row = RowRecordsAggregate.createRow(rowNum);
                addRow(row);
            }
            int level = row.getOutlineLevel();
            row.setOutlineLevel((short) Math.min(7, Math.max(0, indent ? level + 1 : level - 1)));
        }
        recalcRowGutter();
    }

    private void recalcRowGutter() {
        int maxLevel = 0;
        Iterator<RowRecord> iterator = this._rowsAggregate.getIterator();
        while (iterator.hasNext()) {
            maxLevel = Math.max(((RowRecord) iterator.next()).getOutlineLevel(), maxLevel);
        }
        GutsRecord guts = getGutsRecord();
        guts.setRowLevelMax((short) (maxLevel + 1));
        guts.setLeftRowGutter((short) ((maxLevel * 12) + 29));
    }

    public DataValidityTable getOrCreateDataValidityTable() {
        if (this._dataValidityTable == null) {
            DataValidityTable result = new DataValidityTable();
            RecordOrderer.addNewSheetRecord(this._records, result);
            this._dataValidityTable = result;
        }
        return this._dataValidityTable;
    }

    public NoteRecord[] getNoteRecords() {
        List<NoteRecord> temp = new ArrayList();
        for (int i = this._records.size() - 1; i >= 0; i--) {
            RecordBase rec = (RecordBase) this._records.get(i);
            if (rec instanceof NoteRecord) {
                temp.add((NoteRecord) rec);
            }
        }
        if (temp.size() < 1) {
            return NoteRecord.EMPTY_ARRAY;
        }
        NoteRecord[] result = new NoteRecord[temp.size()];
        temp.toArray(result);
        return result;
    }

    public int getColumnOutlineLevel(int columnIndex) {
        return this._columnInfos.getOutlineLevel(columnIndex);
    }
}
