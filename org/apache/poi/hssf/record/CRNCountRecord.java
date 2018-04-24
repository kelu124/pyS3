package org.apache.poi.hssf.record;

import org.apache.poi.util.LittleEndianOutput;

public final class CRNCountRecord extends StandardRecord {
    private static final short DATA_SIZE = (short) 4;
    public static final short sid = (short) 89;
    private int field_1_number_crn_records;
    private int field_2_sheet_table_index;

    public CRNCountRecord() {
        throw new RuntimeException("incomplete code");
    }

    public int getNumberOfCRNs() {
        return this.field_1_number_crn_records;
    }

    public CRNCountRecord(RecordInputStream in) {
        this.field_1_number_crn_records = in.readShort();
        if (this.field_1_number_crn_records < 0) {
            this.field_1_number_crn_records = (short) (-this.field_1_number_crn_records);
        }
        this.field_2_sheet_table_index = in.readShort();
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(getClass().getName()).append(" [XCT");
        sb.append(" nCRNs=").append(this.field_1_number_crn_records);
        sb.append(" sheetIx=").append(this.field_2_sheet_table_index);
        sb.append("]");
        return sb.toString();
    }

    public void serialize(LittleEndianOutput out) {
        out.writeShort((short) this.field_1_number_crn_records);
        out.writeShort((short) this.field_2_sheet_table_index);
    }

    protected int getDataSize() {
        return 4;
    }

    public short getSid() {
        return (short) 89;
    }
}
