package org.apache.poi.hssf.record;

import org.apache.poi.util.HexDump;
import org.apache.poi.util.LittleEndianOutput;

public final class LabelSSTRecord extends CellRecord implements Cloneable {
    public static final short sid = (short) 253;
    private int field_4_sst_index;

    public LabelSSTRecord(RecordInputStream in) {
        super(in);
        this.field_4_sst_index = in.readInt();
    }

    public void setSSTIndex(int index) {
        this.field_4_sst_index = index;
    }

    public int getSSTIndex() {
        return this.field_4_sst_index;
    }

    protected String getRecordName() {
        return "LABELSST";
    }

    protected void appendValueText(StringBuilder sb) {
        sb.append("  .sstIndex = ");
        sb.append(HexDump.shortToHex(getSSTIndex()));
    }

    protected void serializeValue(LittleEndianOutput out) {
        out.writeInt(getSSTIndex());
    }

    protected int getValueDataSize() {
        return 4;
    }

    public short getSid() {
        return (short) 253;
    }

    public LabelSSTRecord clone() {
        LabelSSTRecord rec = new LabelSSTRecord();
        copyBaseFields(rec);
        rec.field_4_sst_index = this.field_4_sst_index;
        return rec;
    }
}
