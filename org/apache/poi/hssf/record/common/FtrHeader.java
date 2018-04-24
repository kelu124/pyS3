package org.apache.poi.hssf.record.common;

import org.apache.poi.hssf.record.RecordInputStream;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.util.LittleEndianOutput;

public final class FtrHeader implements Cloneable {
    private CellRangeAddress associatedRange;
    private short grbitFrt;
    private short recordType;

    public FtrHeader() {
        this.associatedRange = new CellRangeAddress(0, 0, 0, 0);
    }

    public FtrHeader(RecordInputStream in) {
        this.recordType = in.readShort();
        this.grbitFrt = in.readShort();
        this.associatedRange = new CellRangeAddress(in);
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(" [FUTURE HEADER]\n");
        buffer.append("   Type " + this.recordType);
        buffer.append("   Flags " + this.grbitFrt);
        buffer.append(" [/FUTURE HEADER]\n");
        return buffer.toString();
    }

    public void serialize(LittleEndianOutput out) {
        out.writeShort(this.recordType);
        out.writeShort(this.grbitFrt);
        this.associatedRange.serialize(out);
    }

    public static int getDataSize() {
        return 12;
    }

    public short getRecordType() {
        return this.recordType;
    }

    public void setRecordType(short recordType) {
        this.recordType = recordType;
    }

    public short getGrbitFrt() {
        return this.grbitFrt;
    }

    public void setGrbitFrt(short grbitFrt) {
        this.grbitFrt = grbitFrt;
    }

    public CellRangeAddress getAssociatedRange() {
        return this.associatedRange;
    }

    public void setAssociatedRange(CellRangeAddress associatedRange) {
        this.associatedRange = associatedRange;
    }

    public Object clone() {
        FtrHeader result = new FtrHeader();
        result.recordType = this.recordType;
        result.grbitFrt = this.grbitFrt;
        result.associatedRange = this.associatedRange.copy();
        return result;
    }
}
