package org.apache.poi.hssf.record;

import java.util.Iterator;
import org.apache.poi.hssf.record.PageBreakRecord.Break;

public final class HorizontalPageBreakRecord extends PageBreakRecord implements Cloneable {
    public static final short sid = (short) 27;

    public HorizontalPageBreakRecord(RecordInputStream in) {
        super(in);
    }

    public short getSid() {
        return (short) 27;
    }

    public PageBreakRecord clone() {
        PageBreakRecord result = new HorizontalPageBreakRecord();
        Iterator<Break> iterator = getBreaksIterator();
        while (iterator.hasNext()) {
            Break original = (Break) iterator.next();
            result.addBreak(original.main, original.subFrom, original.subTo);
        }
        return result;
    }
}
