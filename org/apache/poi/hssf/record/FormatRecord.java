package org.apache.poi.hssf.record;

import org.apache.poi.util.HexDump;
import org.apache.poi.util.LittleEndianOutput;
import org.apache.poi.util.StringUtil;

public final class FormatRecord extends StandardRecord implements Cloneable {
    public static final short sid = (short) 1054;
    private final int field_1_index_code;
    private final boolean field_3_hasMultibyte;
    private final String field_4_formatstring;

    private FormatRecord(FormatRecord other) {
        this.field_1_index_code = other.field_1_index_code;
        this.field_3_hasMultibyte = other.field_3_hasMultibyte;
        this.field_4_formatstring = other.field_4_formatstring;
    }

    public FormatRecord(int indexCode, String fs) {
        this.field_1_index_code = indexCode;
        this.field_4_formatstring = fs;
        this.field_3_hasMultibyte = StringUtil.hasMultibyte(fs);
    }

    public FormatRecord(RecordInputStream in) {
        this.field_1_index_code = in.readShort();
        int field_3_unicode_len = in.readUShort();
        this.field_3_hasMultibyte = (in.readByte() & 1) != 0;
        if (this.field_3_hasMultibyte) {
            this.field_4_formatstring = in.readUnicodeLEString(field_3_unicode_len);
        } else {
            this.field_4_formatstring = in.readCompressedUnicode(field_3_unicode_len);
        }
    }

    public int getIndexCode() {
        return this.field_1_index_code;
    }

    public String getFormatString() {
        return this.field_4_formatstring;
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[FORMAT]\n");
        buffer.append("    .indexcode       = ").append(HexDump.shortToHex(getIndexCode())).append("\n");
        buffer.append("    .isUnicode       = ").append(this.field_3_hasMultibyte).append("\n");
        buffer.append("    .formatstring    = ").append(getFormatString()).append("\n");
        buffer.append("[/FORMAT]\n");
        return buffer.toString();
    }

    public void serialize(LittleEndianOutput out) {
        String formatString = getFormatString();
        out.writeShort(getIndexCode());
        out.writeShort(formatString.length());
        out.writeByte(this.field_3_hasMultibyte ? 1 : 0);
        if (this.field_3_hasMultibyte) {
            StringUtil.putUnicodeLE(formatString, out);
        } else {
            StringUtil.putCompressedUnicode(formatString, out);
        }
    }

    protected int getDataSize() {
        return ((this.field_3_hasMultibyte ? 2 : 1) * getFormatString().length()) + 5;
    }

    public short getSid() {
        return sid;
    }

    public FormatRecord clone() {
        return new FormatRecord(this);
    }
}
