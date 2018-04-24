package org.apache.poi.hssf.record;

import org.apache.poi.ss.formula.constant.ConstantValueParser;
import org.apache.poi.util.LittleEndianOutput;

public final class CRNRecord extends StandardRecord {
    public static final short sid = (short) 90;
    private int field_1_last_column_index;
    private int field_2_first_column_index;
    private int field_3_row_index;
    private Object[] field_4_constant_values;

    public CRNRecord() {
        throw new RuntimeException("incomplete code");
    }

    public int getNumberOfCRNs() {
        return this.field_1_last_column_index;
    }

    public CRNRecord(RecordInputStream in) {
        this.field_1_last_column_index = in.readUByte();
        this.field_2_first_column_index = in.readUByte();
        this.field_3_row_index = in.readShort();
        this.field_4_constant_values = ConstantValueParser.parse(in, (this.field_1_last_column_index - this.field_2_first_column_index) + 1);
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(getClass().getName()).append(" [CRN");
        sb.append(" rowIx=").append(this.field_3_row_index);
        sb.append(" firstColIx=").append(this.field_2_first_column_index);
        sb.append(" lastColIx=").append(this.field_1_last_column_index);
        sb.append("]");
        return sb.toString();
    }

    protected int getDataSize() {
        return ConstantValueParser.getEncodedSize(this.field_4_constant_values) + 4;
    }

    public void serialize(LittleEndianOutput out) {
        out.writeByte(this.field_1_last_column_index);
        out.writeByte(this.field_2_first_column_index);
        out.writeShort(this.field_3_row_index);
        ConstantValueParser.encode(out, this.field_4_constant_values);
    }

    public short getSid() {
        return (short) 90;
    }
}
