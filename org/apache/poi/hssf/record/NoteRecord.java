package org.apache.poi.hssf.record;

import org.apache.poi.util.LittleEndianOutput;
import org.apache.poi.util.StringUtil;

public final class NoteRecord extends StandardRecord implements Cloneable {
    private static final Byte DEFAULT_PADDING = Byte.valueOf((byte) 0);
    public static final NoteRecord[] EMPTY_ARRAY = new NoteRecord[0];
    public static final short NOTE_HIDDEN = (short) 0;
    public static final short NOTE_VISIBLE = (short) 2;
    public static final short sid = (short) 28;
    private int field_1_row;
    private int field_2_col;
    private short field_3_flags;
    private int field_4_shapeid;
    private boolean field_5_hasMultibyte;
    private String field_6_author;
    private Byte field_7_padding;

    public NoteRecord() {
        this.field_6_author = "";
        this.field_3_flags = (short) 0;
        this.field_7_padding = DEFAULT_PADDING;
    }

    public short getSid() {
        return (short) 28;
    }

    public NoteRecord(RecordInputStream in) {
        this.field_1_row = in.readUShort();
        this.field_2_col = in.readShort();
        this.field_3_flags = in.readShort();
        this.field_4_shapeid = in.readUShort();
        int length = in.readShort();
        this.field_5_hasMultibyte = in.readByte() != (byte) 0;
        if (this.field_5_hasMultibyte) {
            this.field_6_author = StringUtil.readUnicodeLE(in, length);
        } else {
            this.field_6_author = StringUtil.readCompressedUnicode(in, length);
        }
        if (in.available() == 1) {
            this.field_7_padding = Byte.valueOf(in.readByte());
        } else if (in.available() == 2 && length == 0) {
            this.field_7_padding = Byte.valueOf(in.readByte());
            in.readByte();
        }
    }

    public void serialize(LittleEndianOutput out) {
        out.writeShort(this.field_1_row);
        out.writeShort(this.field_2_col);
        out.writeShort(this.field_3_flags);
        out.writeShort(this.field_4_shapeid);
        out.writeShort(this.field_6_author.length());
        out.writeByte(this.field_5_hasMultibyte ? 1 : 0);
        if (this.field_5_hasMultibyte) {
            StringUtil.putUnicodeLE(this.field_6_author, out);
        } else {
            StringUtil.putCompressedUnicode(this.field_6_author, out);
        }
        if (this.field_7_padding != null) {
            out.writeByte(this.field_7_padding.intValue());
        }
    }

    protected int getDataSize() {
        int i = 1;
        int length = ((this.field_5_hasMultibyte ? 2 : 1) * this.field_6_author.length()) + 11;
        if (this.field_7_padding == null) {
            i = 0;
        }
        return length + i;
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[NOTE]\n");
        buffer.append("    .row    = ").append(this.field_1_row).append("\n");
        buffer.append("    .col    = ").append(this.field_2_col).append("\n");
        buffer.append("    .flags  = ").append(this.field_3_flags).append("\n");
        buffer.append("    .shapeid= ").append(this.field_4_shapeid).append("\n");
        buffer.append("    .author = ").append(this.field_6_author).append("\n");
        buffer.append("[/NOTE]\n");
        return buffer.toString();
    }

    public int getRow() {
        return this.field_1_row;
    }

    public void setRow(int row) {
        this.field_1_row = row;
    }

    public int getColumn() {
        return this.field_2_col;
    }

    public void setColumn(int col) {
        this.field_2_col = col;
    }

    public short getFlags() {
        return this.field_3_flags;
    }

    public void setFlags(short flags) {
        this.field_3_flags = flags;
    }

    protected boolean authorIsMultibyte() {
        return this.field_5_hasMultibyte;
    }

    public int getShapeId() {
        return this.field_4_shapeid;
    }

    public void setShapeId(int id) {
        this.field_4_shapeid = id;
    }

    public String getAuthor() {
        return this.field_6_author;
    }

    public void setAuthor(String author) {
        this.field_6_author = author;
        this.field_5_hasMultibyte = StringUtil.hasMultibyte(author);
    }

    public NoteRecord clone() {
        NoteRecord rec = new NoteRecord();
        rec.field_1_row = this.field_1_row;
        rec.field_2_col = this.field_2_col;
        rec.field_3_flags = this.field_3_flags;
        rec.field_4_shapeid = this.field_4_shapeid;
        rec.field_6_author = this.field_6_author;
        return rec;
    }
}
