package org.apache.poi.hssf.record;

import org.apache.poi.util.BitField;
import org.apache.poi.util.BitFieldFactory;
import org.apache.poi.util.HexDump;
import org.apache.poi.util.LittleEndianInput;
import org.apache.poi.util.LittleEndianOutput;

public final class CommonObjectDataSubRecord extends SubRecord implements Cloneable {
    public static final short OBJECT_TYPE_ARC = (short) 4;
    public static final short OBJECT_TYPE_BUTTON = (short) 7;
    public static final short OBJECT_TYPE_CHART = (short) 5;
    public static final short OBJECT_TYPE_CHECKBOX = (short) 11;
    public static final short OBJECT_TYPE_COMBO_BOX = (short) 20;
    public static final short OBJECT_TYPE_COMMENT = (short) 25;
    public static final short OBJECT_TYPE_DIALOG_BOX = (short) 15;
    public static final short OBJECT_TYPE_EDIT_BOX = (short) 13;
    public static final short OBJECT_TYPE_GROUP = (short) 0;
    public static final short OBJECT_TYPE_GROUP_BOX = (short) 19;
    public static final short OBJECT_TYPE_LABEL = (short) 14;
    public static final short OBJECT_TYPE_LINE = (short) 1;
    public static final short OBJECT_TYPE_LIST_BOX = (short) 18;
    public static final short OBJECT_TYPE_MICROSOFT_OFFICE_DRAWING = (short) 30;
    public static final short OBJECT_TYPE_OPTION_BUTTON = (short) 12;
    public static final short OBJECT_TYPE_OVAL = (short) 3;
    public static final short OBJECT_TYPE_PICTURE = (short) 8;
    public static final short OBJECT_TYPE_POLYGON = (short) 9;
    public static final short OBJECT_TYPE_RECTANGLE = (short) 2;
    public static final short OBJECT_TYPE_RESERVED1 = (short) 10;
    public static final short OBJECT_TYPE_RESERVED2 = (short) 21;
    public static final short OBJECT_TYPE_RESERVED3 = (short) 22;
    public static final short OBJECT_TYPE_RESERVED4 = (short) 23;
    public static final short OBJECT_TYPE_RESERVED5 = (short) 24;
    public static final short OBJECT_TYPE_RESERVED6 = (short) 26;
    public static final short OBJECT_TYPE_RESERVED7 = (short) 27;
    public static final short OBJECT_TYPE_RESERVED8 = (short) 28;
    public static final short OBJECT_TYPE_RESERVED9 = (short) 29;
    public static final short OBJECT_TYPE_SCROLL_BAR = (short) 17;
    public static final short OBJECT_TYPE_SPINNER = (short) 16;
    public static final short OBJECT_TYPE_TEXT = (short) 6;
    private static final BitField autofill = BitFieldFactory.getInstance(8192);
    private static final BitField autoline = BitFieldFactory.getInstance(16384);
    private static final BitField locked = BitFieldFactory.getInstance(1);
    private static final BitField printable = BitFieldFactory.getInstance(16);
    public static final short sid = (short) 21;
    private short field_1_objectType;
    private int field_2_objectId;
    private short field_3_option;
    private int field_4_reserved1;
    private int field_5_reserved2;
    private int field_6_reserved3;

    public CommonObjectDataSubRecord(LittleEndianInput in, int size) {
        if (size != 18) {
            throw new RecordFormatException("Expected size 18 but got (" + size + ")");
        }
        this.field_1_objectType = in.readShort();
        this.field_2_objectId = in.readUShort();
        this.field_3_option = in.readShort();
        this.field_4_reserved1 = in.readInt();
        this.field_5_reserved2 = in.readInt();
        this.field_6_reserved3 = in.readInt();
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[ftCmo]\n");
        buffer.append("    .objectType           = ").append("0x").append(HexDump.toHex(getObjectType())).append(" (").append(getObjectType()).append(" )");
        buffer.append(System.getProperty("line.separator"));
        buffer.append("    .objectId             = ").append("0x").append(HexDump.toHex(getObjectId())).append(" (").append(getObjectId()).append(" )");
        buffer.append(System.getProperty("line.separator"));
        buffer.append("    .option               = ").append("0x").append(HexDump.toHex(getOption())).append(" (").append(getOption()).append(" )");
        buffer.append(System.getProperty("line.separator"));
        buffer.append("         .locked                   = ").append(isLocked()).append('\n');
        buffer.append("         .printable                = ").append(isPrintable()).append('\n');
        buffer.append("         .autofill                 = ").append(isAutofill()).append('\n');
        buffer.append("         .autoline                 = ").append(isAutoline()).append('\n');
        buffer.append("    .reserved1            = ").append("0x").append(HexDump.toHex(getReserved1())).append(" (").append(getReserved1()).append(" )");
        buffer.append(System.getProperty("line.separator"));
        buffer.append("    .reserved2            = ").append("0x").append(HexDump.toHex(getReserved2())).append(" (").append(getReserved2()).append(" )");
        buffer.append(System.getProperty("line.separator"));
        buffer.append("    .reserved3            = ").append("0x").append(HexDump.toHex(getReserved3())).append(" (").append(getReserved3()).append(" )");
        buffer.append(System.getProperty("line.separator"));
        buffer.append("[/ftCmo]\n");
        return buffer.toString();
    }

    public void serialize(LittleEndianOutput out) {
        out.writeShort(21);
        out.writeShort(getDataSize());
        out.writeShort(this.field_1_objectType);
        out.writeShort(this.field_2_objectId);
        out.writeShort(this.field_3_option);
        out.writeInt(this.field_4_reserved1);
        out.writeInt(this.field_5_reserved2);
        out.writeInt(this.field_6_reserved3);
    }

    protected int getDataSize() {
        return 18;
    }

    public short getSid() {
        return (short) 21;
    }

    public CommonObjectDataSubRecord clone() {
        CommonObjectDataSubRecord rec = new CommonObjectDataSubRecord();
        rec.field_1_objectType = this.field_1_objectType;
        rec.field_2_objectId = this.field_2_objectId;
        rec.field_3_option = this.field_3_option;
        rec.field_4_reserved1 = this.field_4_reserved1;
        rec.field_5_reserved2 = this.field_5_reserved2;
        rec.field_6_reserved3 = this.field_6_reserved3;
        return rec;
    }

    public short getObjectType() {
        return this.field_1_objectType;
    }

    public void setObjectType(short field_1_objectType) {
        this.field_1_objectType = field_1_objectType;
    }

    public int getObjectId() {
        return this.field_2_objectId;
    }

    public void setObjectId(int field_2_objectId) {
        this.field_2_objectId = field_2_objectId;
    }

    public short getOption() {
        return this.field_3_option;
    }

    public void setOption(short field_3_option) {
        this.field_3_option = field_3_option;
    }

    public int getReserved1() {
        return this.field_4_reserved1;
    }

    public void setReserved1(int field_4_reserved1) {
        this.field_4_reserved1 = field_4_reserved1;
    }

    public int getReserved2() {
        return this.field_5_reserved2;
    }

    public void setReserved2(int field_5_reserved2) {
        this.field_5_reserved2 = field_5_reserved2;
    }

    public int getReserved3() {
        return this.field_6_reserved3;
    }

    public void setReserved3(int field_6_reserved3) {
        this.field_6_reserved3 = field_6_reserved3;
    }

    public void setLocked(boolean value) {
        this.field_3_option = locked.setShortBoolean(this.field_3_option, value);
    }

    public boolean isLocked() {
        return locked.isSet(this.field_3_option);
    }

    public void setPrintable(boolean value) {
        this.field_3_option = printable.setShortBoolean(this.field_3_option, value);
    }

    public boolean isPrintable() {
        return printable.isSet(this.field_3_option);
    }

    public void setAutofill(boolean value) {
        this.field_3_option = autofill.setShortBoolean(this.field_3_option, value);
    }

    public boolean isAutofill() {
        return autofill.isSet(this.field_3_option);
    }

    public void setAutoline(boolean value) {
        this.field_3_option = autoline.setShortBoolean(this.field_3_option, value);
    }

    public boolean isAutoline() {
        return autoline.isSet(this.field_3_option);
    }
}
