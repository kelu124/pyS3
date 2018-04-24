package org.apache.poi.hssf.record;

import java.util.Iterator;
import org.apache.poi.hssf.record.PageBreakRecord.Break;

public final class VerticalPageBreakRecord extends PageBreakRecord {
    public static final short sid = (short) 26;

    public VerticalPageBreakRecord(RecordInputStream in) {
        super(in);
    }

    public short getSid() {
        return (short) 26;
    }

    public Object clone() {
        PageBreakRecord result = new VerticalPageBreakRecord();
        Iterator<Break> iterator = getBreaksIterator();
        while (iterator.hasNext()) {
            Break original = (Break) iterator.next();
            result.addBreak(original.main, original.subFrom, original.subTo);
        }
        return result;
    }
}
