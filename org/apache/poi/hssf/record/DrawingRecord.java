package org.apache.poi.hssf.record;

import org.apache.poi.util.LittleEndianOutput;

public final class DrawingRecord extends StandardRecord implements Cloneable {
    private static final byte[] EMPTY_BYTE_ARRAY = new byte[0];
    public static final short sid = (short) 236;
    private byte[] contd;
    private byte[] recordData;

    public DrawingRecord() {
        this.recordData = EMPTY_BYTE_ARRAY;
    }

    public DrawingRecord(RecordInputStream in) {
        this.recordData = in.readRemainder();
    }

    @Deprecated
    public void processContinueRecord(byte[] record) {
        this.contd = record;
    }

    public void serialize(LittleEndianOutput out) {
        out.write(this.recordData);
    }

    protected int getDataSize() {
        return this.recordData.length;
    }

    public short getSid() {
        return (short) 236;
    }

    public byte[] getRecordData() {
        return this.recordData;
    }

    public void setData(byte[] thedata) {
        if (thedata == null) {
            throw new IllegalArgumentException("data must not be null");
        }
        this.recordData = thedata;
    }

    public DrawingRecord clone() {
        DrawingRecord rec = new DrawingRecord();
        rec.recordData = (byte[]) this.recordData.clone();
        if (this.contd != null) {
            rec.contd = (byte[]) this.contd.clone();
        }
        return rec;
    }

    public String toString() {
        return "DrawingRecord[" + this.recordData.length + "]";
    }
}
