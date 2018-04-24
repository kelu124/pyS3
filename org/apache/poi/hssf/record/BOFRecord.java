package org.apache.poi.hssf.record;

import org.apache.poi.util.HexDump;
import org.apache.poi.util.LittleEndianOutput;

public final class BOFRecord extends StandardRecord implements Cloneable {
    public static final int BUILD = 4307;
    public static final int BUILD_YEAR = 1996;
    public static final int HISTORY_MASK = 65;
    public static final int TYPE_CHART = 32;
    public static final int TYPE_EXCEL_4_MACRO = 64;
    public static final int TYPE_VB_MODULE = 6;
    public static final int TYPE_WORKBOOK = 5;
    public static final int TYPE_WORKSHEET = 16;
    public static final int TYPE_WORKSPACE_FILE = 256;
    public static final int VERSION = 1536;
    public static final short biff2_sid = (short) 9;
    public static final short biff3_sid = (short) 521;
    public static final short biff4_sid = (short) 1033;
    public static final short biff5_sid = (short) 2057;
    public static final short sid = (short) 2057;
    private int field_1_version;
    private int field_2_type;
    private int field_3_build;
    private int field_4_year;
    private int field_5_history;
    private int field_6_rversion;

    private BOFRecord(int type) {
        this.field_1_version = VERSION;
        this.field_2_type = type;
        this.field_3_build = BUILD;
        this.field_4_year = BUILD_YEAR;
        this.field_5_history = 1;
        this.field_6_rversion = VERSION;
    }

    public static BOFRecord createSheetBOF() {
        return new BOFRecord(16);
    }

    public BOFRecord(RecordInputStream in) {
        this.field_1_version = in.readShort();
        this.field_2_type = in.readShort();
        if (in.remaining() >= 2) {
            this.field_3_build = in.readShort();
        }
        if (in.remaining() >= 2) {
            this.field_4_year = in.readShort();
        }
        if (in.remaining() >= 4) {
            this.field_5_history = in.readInt();
        }
        if (in.remaining() >= 4) {
            this.field_6_rversion = in.readInt();
        }
    }

    public void setVersion(int version) {
        this.field_1_version = version;
    }

    public void setType(int type) {
        this.field_2_type = type;
    }

    public void setBuild(int build) {
        this.field_3_build = build;
    }

    public void setBuildYear(int year) {
        this.field_4_year = year;
    }

    public void setHistoryBitMask(int bitmask) {
        this.field_5_history = bitmask;
    }

    public void setRequiredVersion(int version) {
        this.field_6_rversion = version;
    }

    public int getVersion() {
        return this.field_1_version;
    }

    public int getType() {
        return this.field_2_type;
    }

    public int getBuild() {
        return this.field_3_build;
    }

    public int getBuildYear() {
        return this.field_4_year;
    }

    public int getHistoryBitMask() {
        return this.field_5_history;
    }

    public int getRequiredVersion() {
        return this.field_6_rversion;
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[BOF RECORD]\n");
        buffer.append("    .version  = ").append(HexDump.shortToHex(getVersion())).append("\n");
        buffer.append("    .type     = ").append(HexDump.shortToHex(getType()));
        buffer.append(" (").append(getTypeName()).append(")").append("\n");
        buffer.append("    .build    = ").append(HexDump.shortToHex(getBuild())).append("\n");
        buffer.append("    .buildyear= ").append(getBuildYear()).append("\n");
        buffer.append("    .history  = ").append(HexDump.intToHex(getHistoryBitMask())).append("\n");
        buffer.append("    .reqver   = ").append(HexDump.intToHex(getRequiredVersion())).append("\n");
        buffer.append("[/BOF RECORD]\n");
        return buffer.toString();
    }

    private String getTypeName() {
        switch (this.field_2_type) {
            case 5:
                return "workbook";
            case 6:
                return "vb module";
            case 16:
                return "worksheet";
            case 32:
                return "chart";
            case 64:
                return "excel 4 macro";
            case 256:
                return "workspace file";
            default:
                return "#error unknown type#";
        }
    }

    public void serialize(LittleEndianOutput out) {
        out.writeShort(getVersion());
        out.writeShort(getType());
        out.writeShort(getBuild());
        out.writeShort(getBuildYear());
        out.writeInt(getHistoryBitMask());
        out.writeInt(getRequiredVersion());
    }

    protected int getDataSize() {
        return 16;
    }

    public short getSid() {
        return (short) 2057;
    }

    public BOFRecord clone() {
        BOFRecord rec = new BOFRecord();
        rec.field_1_version = this.field_1_version;
        rec.field_2_type = this.field_2_type;
        rec.field_3_build = this.field_3_build;
        rec.field_4_year = this.field_4_year;
        rec.field_5_history = this.field_5_history;
        rec.field_6_rversion = this.field_6_rversion;
        return rec;
    }
}
