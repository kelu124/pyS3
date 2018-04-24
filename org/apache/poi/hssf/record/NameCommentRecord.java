package org.apache.poi.hssf.record;

import org.apache.poi.util.HexDump;
import org.apache.poi.util.LittleEndianOutput;
import org.apache.poi.util.StringUtil;

public final class NameCommentRecord extends StandardRecord {
    public static final short sid = (short) 2196;
    private final short field_1_record_type;
    private final short field_2_frt_cell_ref_flag;
    private final long field_3_reserved;
    private String field_6_name_text;
    private String field_7_comment_text;

    public NameCommentRecord(String name, String comment) {
        this.field_1_record_type = (short) 0;
        this.field_2_frt_cell_ref_flag = (short) 0;
        this.field_3_reserved = 0;
        this.field_6_name_text = name;
        this.field_7_comment_text = comment;
    }

    public void serialize(LittleEndianOutput out) {
        int i;
        int i2 = 1;
        int field_4_name_length = this.field_6_name_text.length();
        int field_5_comment_length = this.field_7_comment_text.length();
        out.writeShort(this.field_1_record_type);
        out.writeShort(this.field_2_frt_cell_ref_flag);
        out.writeLong(this.field_3_reserved);
        out.writeShort(field_4_name_length);
        out.writeShort(field_5_comment_length);
        boolean isNameMultiByte = StringUtil.hasMultibyte(this.field_6_name_text);
        if (isNameMultiByte) {
            i = 1;
        } else {
            i = 0;
        }
        out.writeByte(i);
        if (isNameMultiByte) {
            StringUtil.putUnicodeLE(this.field_6_name_text, out);
        } else {
            StringUtil.putCompressedUnicode(this.field_6_name_text, out);
        }
        boolean isCommentMultiByte = StringUtil.hasMultibyte(this.field_7_comment_text);
        if (!isCommentMultiByte) {
            i2 = 0;
        }
        out.writeByte(i2);
        if (isCommentMultiByte) {
            StringUtil.putUnicodeLE(this.field_7_comment_text, out);
        } else {
            StringUtil.putCompressedUnicode(this.field_7_comment_text, out);
        }
    }

    protected int getDataSize() {
        return (StringUtil.hasMultibyte(this.field_7_comment_text) ? this.field_7_comment_text.length() * 2 : this.field_7_comment_text.length()) + ((StringUtil.hasMultibyte(this.field_6_name_text) ? this.field_6_name_text.length() * 2 : this.field_6_name_text.length()) + 18);
    }

    public NameCommentRecord(RecordInputStream ris) {
        RecordInputStream in = ris;
        this.field_1_record_type = in.readShort();
        this.field_2_frt_cell_ref_flag = in.readShort();
        this.field_3_reserved = in.readLong();
        int field_4_name_length = in.readShort();
        int field_5_comment_length = in.readShort();
        if (in.readByte() == (byte) 0) {
            this.field_6_name_text = StringUtil.readCompressedUnicode(in, field_4_name_length);
        } else {
            this.field_6_name_text = StringUtil.readUnicodeLE(in, field_4_name_length);
        }
        if (in.readByte() == (byte) 0) {
            this.field_7_comment_text = StringUtil.readCompressedUnicode(in, field_5_comment_length);
        } else {
            this.field_7_comment_text = StringUtil.readUnicodeLE(in, field_5_comment_length);
        }
    }

    public short getSid() {
        return sid;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("[NAMECMT]\n");
        sb.append("    .record type            = ").append(HexDump.shortToHex(this.field_1_record_type)).append("\n");
        sb.append("    .frt cell ref flag      = ").append(HexDump.byteToHex(this.field_2_frt_cell_ref_flag)).append("\n");
        sb.append("    .reserved               = ").append(this.field_3_reserved).append("\n");
        sb.append("    .name length            = ").append(this.field_6_name_text.length()).append("\n");
        sb.append("    .comment length         = ").append(this.field_7_comment_text.length()).append("\n");
        sb.append("    .name                   = ").append(this.field_6_name_text).append("\n");
        sb.append("    .comment                = ").append(this.field_7_comment_text).append("\n");
        sb.append("[/NAMECMT]\n");
        return sb.toString();
    }

    public String getNameText() {
        return this.field_6_name_text;
    }

    public void setNameText(String newName) {
        this.field_6_name_text = newName;
    }

    public String getCommentText() {
        return this.field_7_comment_text;
    }

    public void setCommentText(String comment) {
        this.field_7_comment_text = comment;
    }

    public short getRecordType() {
        return this.field_1_record_type;
    }
}
