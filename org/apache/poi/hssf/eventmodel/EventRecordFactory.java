package org.apache.poi.hssf.eventmodel;

import java.io.InputStream;
import java.util.Arrays;
import org.apache.poi.hssf.record.Record;
import org.apache.poi.hssf.record.RecordFactory;
import org.apache.poi.hssf.record.RecordFormatException;
import org.apache.poi.hssf.record.RecordInputStream;

public final class EventRecordFactory {
    private final ERFListener _listener;
    private final short[] _sids;

    public EventRecordFactory(ERFListener listener, short[] sids) {
        this._listener = listener;
        if (sids == null) {
            this._sids = null;
            return;
        }
        this._sids = (short[]) sids.clone();
        Arrays.sort(this._sids);
    }

    private boolean isSidIncluded(short sid) {
        if (this._sids != null && Arrays.binarySearch(this._sids, sid) < 0) {
            return false;
        }
        return true;
    }

    private boolean processRecord(Record record) {
        if (isSidIncluded(record.getSid())) {
            return this._listener.processRecord(record);
        }
        return true;
    }

    public void processRecords(InputStream in) throws RecordFormatException {
        Record last_record = null;
        RecordInputStream recStream = new RecordInputStream(in);
        while (recStream.hasNextRecord()) {
            recStream.nextRecord();
            Record[] recs = RecordFactory.createRecord(recStream);
            if (recs.length > 1) {
                Record[] arr$ = recs;
                int len$ = arr$.length;
                int i$ = 0;
                while (i$ < len$) {
                    Record rec = arr$[i$];
                    if (last_record == null || processRecord(last_record)) {
                        last_record = rec;
                        i$++;
                    } else {
                        return;
                    }
                }
                continue;
            } else {
                Record record = recs[0];
                if (record == null) {
                    continue;
                } else if (last_record == null || processRecord(last_record)) {
                    last_record = record;
                } else {
                    return;
                }
            }
        }
        if (last_record != null) {
            processRecord(last_record);
        }
    }
}
