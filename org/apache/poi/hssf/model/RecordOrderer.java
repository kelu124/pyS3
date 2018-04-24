package org.apache.poi.hssf.model;

import com.itextpdf.text.Jpeg;
import com.itextpdf.text.pdf.codec.TIFFConstants;
import com.itextpdf.text.pdf.codec.wmf.MetaDo;
import java.util.List;
import org.apache.poi.hssf.record.DimensionsRecord;
import org.apache.poi.hssf.record.EOFRecord;
import org.apache.poi.hssf.record.GutsRecord;
import org.apache.poi.hssf.record.Record;
import org.apache.poi.hssf.record.RecordBase;
import org.apache.poi.hssf.record.UnknownRecord;
import org.apache.poi.hssf.record.aggregates.ColumnInfoRecordsAggregate;
import org.apache.poi.hssf.record.aggregates.ConditionalFormattingTable;
import org.apache.poi.hssf.record.aggregates.DataValidityTable;
import org.apache.poi.hssf.record.aggregates.MergedCellsTable;
import org.apache.poi.hssf.record.aggregates.PageSettingsBlock;
import org.apache.poi.hssf.record.aggregates.WorksheetProtectionBlock;
import org.bytedeco.javacpp.opencv_videoio;

final class RecordOrderer {
    private RecordOrderer() {
    }

    public static void addNewSheetRecord(List<RecordBase> sheetRecords, RecordBase newRecord) {
        sheetRecords.add(findSheetInsertPos(sheetRecords, newRecord.getClass()), newRecord);
    }

    private static int findSheetInsertPos(List<RecordBase> records, Class<? extends RecordBase> recClass) {
        if (recClass == DataValidityTable.class) {
            return findDataValidationTableInsertPos(records);
        }
        if (recClass == MergedCellsTable.class) {
            return findInsertPosForNewMergedRecordTable(records);
        }
        if (recClass == ConditionalFormattingTable.class) {
            return findInsertPosForNewCondFormatTable(records);
        }
        if (recClass == GutsRecord.class) {
            return getGutsRecordInsertPos(records);
        }
        if (recClass == PageSettingsBlock.class) {
            return getPageBreakRecordInsertPos(records);
        }
        if (recClass == WorksheetProtectionBlock.class) {
            return getWorksheetProtectionBlockInsertPos(records);
        }
        throw new RuntimeException("Unexpected record class (" + recClass.getName() + ")");
    }

    private static int getWorksheetProtectionBlockInsertPos(List<RecordBase> records) {
        int i = getDimensionsIndex(records);
        while (i > 0) {
            i--;
            if (!isProtectionSubsequentRecord(records.get(i))) {
                return i + 1;
            }
        }
        throw new IllegalStateException("did not find insert pos for protection block");
    }

    private static boolean isProtectionSubsequentRecord(Object rb) {
        if (rb instanceof ColumnInfoRecordsAggregate) {
            return true;
        }
        if (rb instanceof Record) {
            switch (((Record) rb).getSid()) {
                case (short) 85:
                case (short) 144:
                    return true;
            }
        }
        return false;
    }

    private static int getPageBreakRecordInsertPos(List<RecordBase> records) {
        int i = getDimensionsIndex(records) - 1;
        while (i > 0) {
            i--;
            if (isPageBreakPriorRecord(records.get(i))) {
                return i + 1;
            }
        }
        throw new RuntimeException("Did not find insert point for GUTS");
    }

    private static boolean isPageBreakPriorRecord(Object rb) {
        if (rb instanceof Record) {
            switch (((Record) rb).getSid()) {
                case (short) 12:
                case (short) 13:
                case (short) 14:
                case (short) 15:
                case (short) 16:
                case (short) 17:
                case (short) 34:
                case (short) 42:
                case (short) 43:
                case (short) 94:
                case (short) 95:
                case (short) 129:
                case (short) 130:
                case MetaDo.META_SETWINDOWORG /*523*/:
                case (short) 549:
                case (short) 2057:
                    return true;
            }
        }
        return false;
    }

    private static int findInsertPosForNewCondFormatTable(List<RecordBase> records) {
        for (int i = records.size() - 2; i >= 0; i--) {
            Record rb = records.get(i);
            if (rb instanceof MergedCellsTable) {
                return i + 1;
            }
            if (!(rb instanceof DataValidityTable)) {
                switch (rb.getSid()) {
                    case (short) 29:
                    case (short) 65:
                    case (short) 153:
                    case (short) 160:
                    case UnknownRecord.PHONETICPR_00EF /*239*/:
                    case UnknownRecord.LABELRANGES_015F /*351*/:
                    case (short) 574:
                        return i + 1;
                    default:
                        break;
                }
            }
        }
        throw new RuntimeException("Did not find Window2 record");
    }

    private static int findInsertPosForNewMergedRecordTable(List<RecordBase> records) {
        for (int i = records.size() - 2; i >= 0; i--) {
            Record rb = records.get(i);
            if (rb instanceof Record) {
                switch (rb.getSid()) {
                    case (short) 29:
                    case (short) 65:
                    case (short) 153:
                    case (short) 160:
                    case (short) 574:
                        return i + 1;
                    default:
                        break;
                }
            }
        }
        throw new RuntimeException("Did not find Window2 record");
    }

    private static int findDataValidationTableInsertPos(List<RecordBase> records) {
        int i = records.size() - 1;
        if (records.get(i) instanceof EOFRecord) {
            while (i > 0) {
                i--;
                RecordBase rb = (RecordBase) records.get(i);
                if (isDVTPriorRecord(rb)) {
                    Record nextRec = (Record) records.get(i + 1);
                    if (isDVTSubsequentRecord(nextRec.getSid())) {
                        return i + 1;
                    }
                    throw new IllegalStateException("Unexpected (" + nextRec.getClass().getName() + ") found after (" + rb.getClass().getName() + ")");
                }
                Record rec = (Record) rb;
                if (!isDVTSubsequentRecord(rec.getSid())) {
                    throw new IllegalStateException("Unexpected (" + rec.getClass().getName() + ") while looking for DV Table insert pos");
                }
            }
            return 0;
        }
        throw new IllegalStateException("Last sheet record should be EOFRecord");
    }

    private static boolean isDVTPriorRecord(RecordBase rb) {
        if ((rb instanceof MergedCellsTable) || (rb instanceof ConditionalFormattingTable)) {
            return true;
        }
        switch (((Record) rb).getSid()) {
            case (short) 29:
            case (short) 65:
            case (short) 153:
            case (short) 160:
            case UnknownRecord.PHONETICPR_00EF /*239*/:
            case UnknownRecord.LABELRANGES_015F /*351*/:
            case opencv_videoio.CV_CAP_PROP_XI_AEAG_ROI_OFFSET_Y /*440*/:
            case (short) 442:
            case (short) 574:
            case (short) 2048:
                return true;
            default:
                return false;
        }
    }

    private static boolean isDVTSubsequentRecord(short sid) {
        switch (sid) {
            case (short) 10:
            case UnknownRecord.SHEETEXT_0862 /*2146*/:
            case UnknownRecord.SHEETPROTECTION_0867 /*2151*/:
            case (short) 2152:
            case UnknownRecord.PLV_MAC /*2248*/:
                return true;
            default:
                return false;
        }
    }

    private static int getDimensionsIndex(List<RecordBase> records) {
        int nRecs = records.size();
        for (int i = 0; i < nRecs; i++) {
            if (records.get(i) instanceof DimensionsRecord) {
                return i;
            }
        }
        throw new RuntimeException("DimensionsRecord not found");
    }

    private static int getGutsRecordInsertPos(List<RecordBase> records) {
        int i = getDimensionsIndex(records) - 1;
        while (i > 0) {
            i--;
            if (isGutsPriorRecord((RecordBase) records.get(i))) {
                return i + 1;
            }
        }
        throw new RuntimeException("Did not find insert point for GUTS");
    }

    private static boolean isGutsPriorRecord(RecordBase rb) {
        if (rb instanceof Record) {
            switch (((Record) rb).getSid()) {
                case (short) 12:
                case (short) 13:
                case (short) 14:
                case (short) 15:
                case (short) 16:
                case (short) 17:
                case (short) 34:
                case (short) 42:
                case (short) 43:
                case (short) 94:
                case (short) 95:
                case (short) 130:
                case MetaDo.META_SETWINDOWORG /*523*/:
                case (short) 2057:
                    return true;
            }
        }
        return false;
    }

    public static boolean isEndOfRowBlock(int sid) {
        switch (sid) {
            case 10:
                throw new RuntimeException("Found EOFRecord before WindowTwoRecord was encountered");
            case 61:
            case 93:
            case 125:
            case 128:
            case 176:
            case 236:
            case Jpeg.M_APPD /*237*/:
            case 434:
            case 438:
            case 574:
                return true;
            default:
                return PageSettingsBlock.isComponentRecord(sid);
        }
    }

    public static boolean isRowBlockRecord(int sid) {
        switch (sid) {
            case 6:
            case 253:
            case 513:
            case 515:
            case opencv_videoio.CV_CAP_PROP_XI_LENS_FOCAL_LENGTH /*516*/:
            case 517:
            case TIFFConstants.TIFFTAG_JPEGDCTABLES /*520*/:
            case 545:
            case 566:
            case 638:
            case 1212:
                return true;
            default:
                return false;
        }
    }
}
