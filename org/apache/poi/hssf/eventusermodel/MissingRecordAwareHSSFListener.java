package org.apache.poi.hssf.eventusermodel;

import com.itextpdf.text.pdf.codec.TIFFConstants;
import org.apache.poi.hssf.eventusermodel.dummyrecord.LastCellOfRowDummyRecord;
import org.apache.poi.hssf.eventusermodel.dummyrecord.MissingCellDummyRecord;
import org.apache.poi.hssf.eventusermodel.dummyrecord.MissingRowDummyRecord;
import org.apache.poi.hssf.record.BOFRecord;
import org.apache.poi.hssf.record.CellValueRecordInterface;
import org.apache.poi.hssf.record.MulBlankRecord;
import org.apache.poi.hssf.record.MulRKRecord;
import org.apache.poi.hssf.record.NoteRecord;
import org.apache.poi.hssf.record.Record;
import org.apache.poi.hssf.record.RecordFactory;
import org.apache.poi.hssf.record.RowRecord;
import org.apache.poi.hssf.record.StringRecord;

public final class MissingRecordAwareHSSFListener implements HSSFListener {
    private HSSFListener childListener;
    private int lastCellColumn;
    private int lastCellRow;
    private int lastRowRow;

    public MissingRecordAwareHSSFListener(HSSFListener listener) {
        resetCounts();
        this.childListener = listener;
    }

    public void processRecord(Record record) {
        int thisRow;
        int thisColumn;
        CellValueRecordInterface[] expandedRecords = null;
        int i;
        if (!(record instanceof CellValueRecordInterface)) {
            if (!(record instanceof StringRecord)) {
                thisRow = -1;
                thisColumn = -1;
                switch (record.getSid()) {
                    case (short) 28:
                        NoteRecord nrec = (NoteRecord) record;
                        thisRow = nrec.getRow();
                        thisColumn = nrec.getColumn();
                        break;
                    case (short) 189:
                        expandedRecords = RecordFactory.convertRKRecords((MulRKRecord) record);
                        break;
                    case (short) 190:
                        expandedRecords = RecordFactory.convertBlankRecords((MulBlankRecord) record);
                        break;
                    case TIFFConstants.TIFFTAG_JPEGDCTABLES /*520*/:
                        RowRecord rowrec = (RowRecord) record;
                        if (this.lastRowRow + 1 < rowrec.getRowNumber()) {
                            for (i = this.lastRowRow + 1; i < rowrec.getRowNumber(); i++) {
                                this.childListener.processRecord(new MissingRowDummyRecord(i));
                            }
                        }
                        this.lastRowRow = rowrec.getRowNumber();
                        this.lastCellColumn = -1;
                        break;
                    case (short) 1212:
                        this.childListener.processRecord(record);
                        return;
                    case (short) 2057:
                        BOFRecord bof = (BOFRecord) record;
                        if (bof.getType() == 5 || bof.getType() == 16) {
                            resetCounts();
                            break;
                        }
                    default:
                        break;
                }
            }
            this.childListener.processRecord(record);
            return;
        }
        CellValueRecordInterface valueRec = (CellValueRecordInterface) record;
        thisRow = valueRec.getRow();
        thisColumn = valueRec.getColumn();
        if (expandedRecords != null && expandedRecords.length > 0) {
            thisRow = expandedRecords[0].getRow();
            thisColumn = expandedRecords[0].getColumn();
        }
        if (thisRow != this.lastCellRow && thisRow > 0) {
            if (this.lastCellRow == -1) {
                this.lastCellRow = 0;
            }
            for (i = this.lastCellRow; i < thisRow; i++) {
                int cols = -1;
                if (i == this.lastCellRow) {
                    cols = this.lastCellColumn;
                }
                this.childListener.processRecord(new LastCellOfRowDummyRecord(i, cols));
            }
        }
        if (!(this.lastCellRow == -1 || this.lastCellColumn == -1 || thisRow != -1)) {
            this.childListener.processRecord(new LastCellOfRowDummyRecord(this.lastCellRow, this.lastCellColumn));
            this.lastCellRow = -1;
            this.lastCellColumn = -1;
        }
        if (thisRow != this.lastCellRow) {
            this.lastCellColumn = -1;
        }
        if (this.lastCellColumn != thisColumn - 1) {
            for (i = this.lastCellColumn + 1; i < thisColumn; i++) {
                this.childListener.processRecord(new MissingCellDummyRecord(thisRow, i));
            }
        }
        if (expandedRecords != null && expandedRecords.length > 0) {
            thisColumn = expandedRecords[expandedRecords.length - 1].getColumn();
        }
        if (thisColumn != -1) {
            this.lastCellColumn = thisColumn;
            this.lastCellRow = thisRow;
        }
        if (expandedRecords == null || expandedRecords.length <= 0) {
            this.childListener.processRecord(record);
            return;
        }
        for (CellValueRecordInterface r : expandedRecords) {
            this.childListener.processRecord((Record) r);
        }
    }

    private void resetCounts() {
        this.lastRowRow = -1;
        this.lastCellRow = -1;
        this.lastCellColumn = -1;
    }
}
