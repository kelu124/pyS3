package org.apache.poi.hssf.record;

import org.apache.poi.util.HexDump;

public abstract class OldCellRecord {
    private final int field_1_row;
    private final short field_2_column;
    private int field_3_cell_attrs;
    private short field_3_xf_index;
    private final boolean isBiff2;
    private final short sid;

    protected abstract void appendValueText(StringBuilder stringBuilder);

    protected abstract String getRecordName();

    protected OldCellRecord(RecordInputStream in, boolean isBiff2) {
        this.sid = in.getSid();
        this.isBiff2 = isBiff2;
        this.field_1_row = in.readUShort();
        this.field_2_column = in.readShort();
        if (isBiff2) {
            this.field_3_cell_attrs = in.readUShort() << 8;
            this.field_3_cell_attrs += in.readUByte();
            return;
        }
        this.field_3_xf_index = in.readShort();
    }

    public final int getRow() {
        return this.field_1_row;
    }

    public final short getColumn() {
        return this.field_2_column;
    }

    public final short getXFIndex() {
        return this.field_3_xf_index;
    }

    public int getCellAttrs() {
        return this.field_3_cell_attrs;
    }

    public boolean isBiff2() {
        return this.isBiff2;
    }

    public short getSid() {
        return this.sid;
    }

    public final String toString() {
        StringBuilder sb = new StringBuilder();
        String recordName = getRecordName();
        sb.append("[").append(recordName).append("]\n");
        sb.append("    .row    = ").append(HexDump.shortToHex(getRow())).append("\n");
        sb.append("    .col    = ").append(HexDump.shortToHex(getColumn())).append("\n");
        if (isBiff2()) {
            sb.append("    .cellattrs = ").append(HexDump.shortToHex(getCellAttrs())).append("\n");
        } else {
            sb.append("    .xfindex   = ").append(HexDump.shortToHex(getXFIndex())).append("\n");
        }
        appendValueText(sb);
        sb.append("\n");
        sb.append("[/").append(recordName).append("]\n");
        return sb.toString();
    }
}
