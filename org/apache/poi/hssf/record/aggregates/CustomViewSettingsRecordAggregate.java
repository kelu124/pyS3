package org.apache.poi.hssf.record.aggregates;

import java.util.ArrayList;
import java.util.List;
import org.apache.poi.hssf.model.RecordStream;
import org.apache.poi.hssf.record.HeaderFooterRecord;
import org.apache.poi.hssf.record.Record;
import org.apache.poi.hssf.record.RecordBase;
import org.apache.poi.hssf.record.UnknownRecord;
import org.apache.poi.hssf.record.UserSViewBegin;
import org.apache.poi.hssf.record.UserSViewEnd;
import org.apache.poi.hssf.record.aggregates.RecordAggregate.RecordVisitor;

public final class CustomViewSettingsRecordAggregate extends RecordAggregate {
    private final Record _begin;
    private final Record _end;
    private PageSettingsBlock _psBlock;
    private final List<RecordBase> _recs;

    public CustomViewSettingsRecordAggregate(RecordStream rs) {
        this._begin = rs.getNext();
        if (this._begin.getSid() != UserSViewBegin.sid) {
            throw new IllegalStateException("Bad begin record");
        }
        List<RecordBase> temp = new ArrayList();
        while (rs.peekNextSid() != 427) {
            if (!PageSettingsBlock.isComponentRecord(rs.peekNextSid())) {
                temp.add(rs.getNext());
            } else if (this._psBlock == null) {
                this._psBlock = new PageSettingsBlock(rs);
                temp.add(this._psBlock);
            } else if (rs.peekNextSid() == UnknownRecord.HEADER_FOOTER_089C) {
                this._psBlock.addLateHeaderFooter((HeaderFooterRecord) rs.getNext());
            } else {
                throw new IllegalStateException("Found more than one PageSettingsBlock in chart sub-stream, had sid: " + rs.peekNextSid());
            }
        }
        this._recs = temp;
        this._end = rs.getNext();
        if (this._end.getSid() != UserSViewEnd.sid) {
            throw new IllegalStateException("Bad custom view settings end record");
        }
    }

    public void visitContainedRecords(RecordVisitor rv) {
        if (!this._recs.isEmpty()) {
            rv.visitRecord(this._begin);
            for (int i = 0; i < this._recs.size(); i++) {
                RecordBase rb = (RecordBase) this._recs.get(i);
                if (rb instanceof RecordAggregate) {
                    ((RecordAggregate) rb).visitContainedRecords(rv);
                } else {
                    rv.visitRecord((Record) rb);
                }
            }
            rv.visitRecord(this._end);
        }
    }

    public static boolean isBeginRecord(int sid) {
        return sid == 426;
    }

    public void append(RecordBase r) {
        this._recs.add(r);
    }
}
