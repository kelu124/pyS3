package org.apache.poi.hssf.record;

import java.io.ByteArrayInputStream;

public abstract class Record extends RecordBase {
    public abstract short getSid();

    protected Record() {
    }

    public final byte[] serialize() {
        byte[] retval = new byte[getRecordSize()];
        serialize(0, retval);
        return retval;
    }

    public String toString() {
        return super.toString();
    }

    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException("The class " + getClass().getName() + " needs to define a clone method");
    }

    public Record cloneViaReserialise() {
        RecordInputStream rinp = new RecordInputStream(new ByteArrayInputStream(serialize()));
        rinp.nextRecord();
        Record[] r = RecordFactory.createRecord(rinp);
        if (r.length == 1) {
            return r[0];
        }
        throw new IllegalStateException("Re-serialised a record to clone it, but got " + r.length + " records back!");
    }
}
