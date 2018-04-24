package org.apache.poi.hssf.eventusermodel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.poi.hssf.record.Record;
import org.apache.poi.hssf.record.RecordFactory;

public class HSSFRequest {
    private final Map<Short, List<HSSFListener>> _records = new HashMap(50);

    public void addListener(HSSFListener lsnr, short sid) {
        List<HSSFListener> list = (List) this._records.get(Short.valueOf(sid));
        if (list == null) {
            list = new ArrayList(1);
            this._records.put(Short.valueOf(sid), list);
        }
        list.add(lsnr);
    }

    public void addListenerForAllRecords(HSSFListener lsnr) {
        for (short rectype : RecordFactory.getAllKnownRecordSIDs()) {
            addListener(lsnr, rectype);
        }
    }

    protected short processRecord(Record rec) throws HSSFUserException {
        List<HSSFListener> listeners = (List) this._records.get(Short.valueOf(rec.getSid()));
        short userCode = (short) 0;
        if (listeners != null) {
            for (int k = 0; k < listeners.size(); k++) {
                AbortableHSSFListener listenObj = listeners.get(k);
                if (listenObj instanceof AbortableHSSFListener) {
                    userCode = listenObj.abortableProcessRecord(rec);
                    if (userCode != (short) 0) {
                        break;
                    }
                } else {
                    listenObj.processRecord(rec);
                }
            }
        }
        return userCode;
    }
}
