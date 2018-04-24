package org.apache.poi.hssf.record;

import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.hssf.record.chart.BeginRecord;
import org.apache.poi.hssf.record.chart.CatLabRecord;
import org.apache.poi.hssf.record.chart.ChartEndBlockRecord;
import org.apache.poi.hssf.record.chart.ChartEndObjectRecord;
import org.apache.poi.hssf.record.chart.ChartFRTInfoRecord;
import org.apache.poi.hssf.record.chart.ChartRecord;
import org.apache.poi.hssf.record.chart.ChartStartBlockRecord;
import org.apache.poi.hssf.record.chart.ChartStartObjectRecord;
import org.apache.poi.hssf.record.chart.ChartTitleFormatRecord;
import org.apache.poi.hssf.record.chart.DataFormatRecord;
import org.apache.poi.hssf.record.chart.EndRecord;
import org.apache.poi.hssf.record.chart.LegendRecord;
import org.apache.poi.hssf.record.chart.LinkedDataRecord;
import org.apache.poi.hssf.record.chart.SeriesRecord;
import org.apache.poi.hssf.record.chart.SeriesTextRecord;
import org.apache.poi.hssf.record.chart.SeriesToChartGroupRecord;
import org.apache.poi.hssf.record.chart.ValueRangeRecord;
import org.apache.poi.hssf.record.pivottable.DataItemRecord;
import org.apache.poi.hssf.record.pivottable.ExtendedPivotTableViewFieldsRecord;
import org.apache.poi.hssf.record.pivottable.PageItemRecord;
import org.apache.poi.hssf.record.pivottable.StreamIDRecord;
import org.apache.poi.hssf.record.pivottable.ViewDefinitionRecord;
import org.apache.poi.hssf.record.pivottable.ViewFieldsRecord;
import org.apache.poi.hssf.record.pivottable.ViewSourceRecord;
import org.apache.poi.util.RecordFormatException;

public final class RecordFactory {
    private static final Class<?>[] CONSTRUCTOR_ARGS = new Class[]{RecordInputStream.class};
    private static final int NUM_RECORDS = 512;
    private static short[] _allKnownRecordSIDs;
    private static final Map<Integer, I_RecordCreator> _recordCreatorsById = recordsToMap(recordClasses);
    private static final Class<? extends Record>[] recordClasses = new Class[]{ArrayRecord.class, AutoFilterInfoRecord.class, BackupRecord.class, BlankRecord.class, BOFRecord.class, BookBoolRecord.class, BoolErrRecord.class, BottomMarginRecord.class, BoundSheetRecord.class, CalcCountRecord.class, CalcModeRecord.class, CFHeaderRecord.class, CFHeader12Record.class, CFRuleRecord.class, CFRule12Record.class, ChartRecord.class, ChartTitleFormatRecord.class, CodepageRecord.class, ColumnInfoRecord.class, ContinueRecord.class, CountryRecord.class, CRNCountRecord.class, CRNRecord.class, DateWindow1904Record.class, DBCellRecord.class, DConRefRecord.class, DefaultColWidthRecord.class, DefaultRowHeightRecord.class, DeltaRecord.class, DimensionsRecord.class, DrawingGroupRecord.class, DrawingRecord.class, DrawingSelectionRecord.class, DSFRecord.class, DVALRecord.class, DVRecord.class, EOFRecord.class, ExtendedFormatRecord.class, ExternalNameRecord.class, ExternSheetRecord.class, ExtSSTRecord.class, FeatRecord.class, FeatHdrRecord.class, FilePassRecord.class, FileSharingRecord.class, FnGroupCountRecord.class, FontRecord.class, FooterRecord.class, FormatRecord.class, FormulaRecord.class, GridsetRecord.class, GutsRecord.class, HCenterRecord.class, HeaderRecord.class, HeaderFooterRecord.class, HideObjRecord.class, HorizontalPageBreakRecord.class, HyperlinkRecord.class, IndexRecord.class, InterfaceEndRecord.class, InterfaceHdrRecord.class, IterationRecord.class, LabelRecord.class, LabelSSTRecord.class, LeftMarginRecord.class, LegendRecord.class, MergeCellsRecord.class, MMSRecord.class, MulBlankRecord.class, MulRKRecord.class, NameRecord.class, NameCommentRecord.class, NoteRecord.class, NumberRecord.class, ObjectProtectRecord.class, ObjRecord.class, PaletteRecord.class, PaneRecord.class, PasswordRecord.class, PasswordRev4Record.class, PrecisionRecord.class, PrintGridlinesRecord.class, PrintHeadersRecord.class, PrintSetupRecord.class, ProtectionRev4Record.class, ProtectRecord.class, RecalcIdRecord.class, RefModeRecord.class, RefreshAllRecord.class, RightMarginRecord.class, RKRecord.class, RowRecord.class, SaveRecalcRecord.class, ScenarioProtectRecord.class, SelectionRecord.class, SeriesRecord.class, SeriesTextRecord.class, SharedFormulaRecord.class, SSTRecord.class, StringRecord.class, StyleRecord.class, SupBookRecord.class, TabIdRecord.class, TableRecord.class, TableStylesRecord.class, TextObjectRecord.class, TopMarginRecord.class, UncalcedRecord.class, UseSelFSRecord.class, UserSViewBegin.class, UserSViewEnd.class, ValueRangeRecord.class, VCenterRecord.class, VerticalPageBreakRecord.class, WindowOneRecord.class, WindowProtectRecord.class, WindowTwoRecord.class, WriteAccessRecord.class, WriteProtectRecord.class, WSBoolRecord.class, BeginRecord.class, ChartFRTInfoRecord.class, ChartStartBlockRecord.class, ChartEndBlockRecord.class, ChartStartObjectRecord.class, ChartEndObjectRecord.class, CatLabRecord.class, DataFormatRecord.class, EndRecord.class, LinkedDataRecord.class, SeriesToChartGroupRecord.class, DataItemRecord.class, ExtendedPivotTableViewFieldsRecord.class, PageItemRecord.class, StreamIDRecord.class, ViewDefinitionRecord.class, ViewFieldsRecord.class, ViewSourceRecord.class};

    private interface I_RecordCreator {
        Record create(RecordInputStream recordInputStream);

        Class<? extends Record> getRecordClass();
    }

    private static final class ReflectionConstructorRecordCreator implements I_RecordCreator {
        private final Constructor<? extends Record> _c;

        public ReflectionConstructorRecordCreator(Constructor<? extends Record> c) {
            this._c = c;
        }

        public Record create(RecordInputStream in) {
            try {
                return (Record) this._c.newInstance(new Object[]{in});
            } catch (IllegalArgumentException e) {
                throw new RuntimeException(e);
            } catch (InstantiationException e2) {
                throw new RuntimeException(e2);
            } catch (IllegalAccessException e3) {
                throw new RuntimeException(e3);
            } catch (InvocationTargetException e4) {
                Throwable t = e4.getTargetException();
                if (t instanceof RecordFormatException) {
                    throw ((RecordFormatException) t);
                } else if (t instanceof EncryptedDocumentException) {
                    throw ((EncryptedDocumentException) t);
                } else {
                    throw new RecordFormatException("Unable to construct record instance", t);
                }
            }
        }

        public Class<? extends Record> getRecordClass() {
            return this._c.getDeclaringClass();
        }
    }

    private static final class ReflectionMethodRecordCreator implements I_RecordCreator {
        private final Method _m;

        public ReflectionMethodRecordCreator(Method m) {
            this._m = m;
        }

        public Record create(RecordInputStream in) {
            try {
                return (Record) this._m.invoke(null, new Object[]{in});
            } catch (IllegalArgumentException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e2) {
                throw new RuntimeException(e2);
            } catch (InvocationTargetException e3) {
                throw new RecordFormatException("Unable to construct record instance", e3.getTargetException());
            }
        }

        public Class<? extends Record> getRecordClass() {
            return this._m.getDeclaringClass();
        }
    }

    public static Class<? extends Record> getRecordClass(int sid) {
        I_RecordCreator rc = (I_RecordCreator) _recordCreatorsById.get(Integer.valueOf(sid));
        if (rc == null) {
            return null;
        }
        return rc.getRecordClass();
    }

    public static Record[] createRecord(RecordInputStream in) {
        Record record = createSingleRecord(in);
        if (record instanceof DBCellRecord) {
            return new Record[]{null};
        } else if (record instanceof RKRecord) {
            return new Record[]{convertToNumberRecord((RKRecord) record)};
        } else if (record instanceof MulRKRecord) {
            return convertRKRecords((MulRKRecord) record);
        } else {
            return new Record[]{record};
        }
    }

    public static Record createSingleRecord(RecordInputStream in) {
        I_RecordCreator constructor = (I_RecordCreator) _recordCreatorsById.get(Integer.valueOf(in.getSid()));
        if (constructor == null) {
            return new UnknownRecord(in);
        }
        return constructor.create(in);
    }

    public static NumberRecord convertToNumberRecord(RKRecord rk) {
        NumberRecord num = new NumberRecord();
        num.setColumn(rk.getColumn());
        num.setRow(rk.getRow());
        num.setXFIndex(rk.getXFIndex());
        num.setValue(rk.getRKNumber());
        return num;
    }

    public static NumberRecord[] convertRKRecords(MulRKRecord mrk) {
        NumberRecord[] mulRecs = new NumberRecord[mrk.getNumColumns()];
        for (int k = 0; k < mrk.getNumColumns(); k++) {
            NumberRecord nr = new NumberRecord();
            nr.setColumn((short) (mrk.getFirstColumn() + k));
            nr.setRow(mrk.getRow());
            nr.setXFIndex(mrk.getXFAt(k));
            nr.setValue(mrk.getRKNumberAt(k));
            mulRecs[k] = nr;
        }
        return mulRecs;
    }

    public static BlankRecord[] convertBlankRecords(MulBlankRecord mbk) {
        BlankRecord[] mulRecs = new BlankRecord[mbk.getNumColumns()];
        for (int k = 0; k < mbk.getNumColumns(); k++) {
            BlankRecord br = new BlankRecord();
            br.setColumn((short) (mbk.getFirstColumn() + k));
            br.setRow(mbk.getRow());
            br.setXFIndex(mbk.getXFAt(k));
            mulRecs[k] = br;
        }
        return mulRecs;
    }

    public static short[] getAllKnownRecordSIDs() {
        if (_allKnownRecordSIDs == null) {
            short[] results = new short[_recordCreatorsById.size()];
            int i = 0;
            for (Integer sid : _recordCreatorsById.keySet()) {
                int i2 = i + 1;
                results[i] = sid.shortValue();
                i = i2;
            }
            Arrays.sort(results);
            _allKnownRecordSIDs = results;
        }
        return (short[]) _allKnownRecordSIDs.clone();
    }

    private static Map<Integer, I_RecordCreator> recordsToMap(Class<? extends Record>[] records) {
        Map<Integer, I_RecordCreator> result = new HashMap();
        Set<Class<?>> uniqueRecClasses = new HashSet((records.length * 3) / 2);
        Class<? extends Record>[] arr$ = records;
        int len$ = arr$.length;
        int i$ = 0;
        while (i$ < len$) {
            Class<? extends Record> recClass = arr$[i$];
            if (!Record.class.isAssignableFrom(recClass)) {
                throw new RuntimeException("Invalid record sub-class (" + recClass.getName() + ")");
            } else if (Modifier.isAbstract(recClass.getModifiers())) {
                throw new RuntimeException("Invalid record class (" + recClass.getName() + ") - must not be abstract");
            } else if (uniqueRecClasses.add(recClass)) {
                try {
                    int sid = recClass.getField("sid").getShort(null);
                    Integer key = Integer.valueOf(sid);
                    if (result.containsKey(key)) {
                        throw new RuntimeException("duplicate record sid 0x" + Integer.toHexString(sid).toUpperCase(Locale.ROOT) + " for classes (" + recClass.getName() + ") and (" + ((I_RecordCreator) result.get(key)).getRecordClass().getName() + ")");
                    }
                    result.put(key, getRecordCreator(recClass));
                    i$++;
                } catch (Exception e) {
                    throw new RecordFormatException("Unable to determine record types");
                }
            } else {
                throw new RuntimeException("duplicate record class (" + recClass.getName() + ")");
            }
        }
        return result;
    }

    private static I_RecordCreator getRecordCreator(Class<? extends Record> recClass) {
        try {
            return new ReflectionConstructorRecordCreator(recClass.getConstructor(CONSTRUCTOR_ARGS));
        } catch (NoSuchMethodException e) {
            try {
                return new ReflectionMethodRecordCreator(recClass.getDeclaredMethod("create", CONSTRUCTOR_ARGS));
            } catch (NoSuchMethodException e2) {
                throw new RuntimeException("Failed to find constructor or create method for (" + recClass.getName() + ").");
            }
        }
    }

    public static List<Record> createRecords(InputStream in) throws RecordFormatException {
        List<Record> records = new ArrayList(512);
        RecordFactoryInputStream recStream = new RecordFactoryInputStream(in, true);
        while (true) {
            Record record = recStream.nextRecord();
            if (record == null) {
                return records;
            }
            records.add(record);
        }
    }
}
