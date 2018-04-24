package org.apache.poi.hssf.eventusermodel;

import java.util.ArrayList;
import java.util.List;
import org.apache.poi.hssf.model.InternalWorkbook;
import org.apache.poi.hssf.record.BoundSheetRecord;
import org.apache.poi.hssf.record.EOFRecord;
import org.apache.poi.hssf.record.ExternSheetRecord;
import org.apache.poi.hssf.record.Record;
import org.apache.poi.hssf.record.SSTRecord;
import org.apache.poi.hssf.record.SupBookRecord;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class EventWorkbookBuilder {

    public static class SheetRecordCollectingListener implements HSSFListener {
        private final List<BoundSheetRecord> boundSheetRecords = new ArrayList();
        private final HSSFListener childListener;
        private final List<ExternSheetRecord> externSheetRecords = new ArrayList();
        private SSTRecord sstRecord = null;

        public SheetRecordCollectingListener(HSSFListener childListener) {
            this.childListener = childListener;
        }

        public BoundSheetRecord[] getBoundSheetRecords() {
            return (BoundSheetRecord[]) this.boundSheetRecords.toArray(new BoundSheetRecord[this.boundSheetRecords.size()]);
        }

        public ExternSheetRecord[] getExternSheetRecords() {
            return (ExternSheetRecord[]) this.externSheetRecords.toArray(new ExternSheetRecord[this.externSheetRecords.size()]);
        }

        public SSTRecord getSSTRecord() {
            return this.sstRecord;
        }

        public HSSFWorkbook getStubHSSFWorkbook() {
            HSSFWorkbook wb = HSSFWorkbook.create(getStubWorkbook());
            for (BoundSheetRecord bsr : this.boundSheetRecords) {
                wb.createSheet(bsr.getSheetname());
            }
            return wb;
        }

        public InternalWorkbook getStubWorkbook() {
            return EventWorkbookBuilder.createStubWorkbook(getExternSheetRecords(), getBoundSheetRecords(), getSSTRecord());
        }

        public void processRecord(Record record) {
            processRecordInternally(record);
            this.childListener.processRecord(record);
        }

        public void processRecordInternally(Record record) {
            if (record instanceof BoundSheetRecord) {
                this.boundSheetRecords.add((BoundSheetRecord) record);
            } else if (record instanceof ExternSheetRecord) {
                this.externSheetRecords.add((ExternSheetRecord) record);
            } else if (record instanceof SSTRecord) {
                this.sstRecord = (SSTRecord) record;
            }
        }
    }

    public static InternalWorkbook createStubWorkbook(ExternSheetRecord[] externs, BoundSheetRecord[] bounds, SSTRecord sst) {
        List<Record> wbRecords = new ArrayList();
        if (bounds != null) {
            for (BoundSheetRecord bound : bounds) {
                wbRecords.add(bound);
            }
        }
        if (sst != null) {
            wbRecords.add(sst);
        }
        if (externs != null) {
            wbRecords.add(SupBookRecord.createInternalReferences((short) externs.length));
            for (ExternSheetRecord extern : externs) {
                wbRecords.add(extern);
            }
        }
        wbRecords.add(EOFRecord.instance);
        return InternalWorkbook.createWorkbook(wbRecords);
    }

    public static InternalWorkbook createStubWorkbook(ExternSheetRecord[] externs, BoundSheetRecord[] bounds) {
        return createStubWorkbook(externs, bounds, null);
    }
}
