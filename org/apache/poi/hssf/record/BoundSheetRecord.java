package org.apache.poi.hssf.record;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import org.apache.poi.ss.util.WorkbookUtil;
import org.apache.poi.util.BitField;
import org.apache.poi.util.BitFieldFactory;
import org.apache.poi.util.HexDump;
import org.apache.poi.util.LittleEndianOutput;
import org.apache.poi.util.StringUtil;

public final class BoundSheetRecord extends StandardRecord {
    private static final Comparator<BoundSheetRecord> BOFComparator = new C10551();
    private static final BitField hiddenFlag = BitFieldFactory.getInstance(1);
    public static final short sid = (short) 133;
    private static final BitField veryHiddenFlag = BitFieldFactory.getInstance(2);
    private int field_1_position_of_BOF;
    private int field_2_option_flags;
    private int field_4_isMultibyteUnicode;
    private String field_5_sheetname;

    static class C10551 implements Comparator<BoundSheetRecord> {
        C10551() {
        }

        public int compare(BoundSheetRecord bsr1, BoundSheetRecord bsr2) {
            return bsr1.getPositionOfBof() - bsr2.getPositionOfBof();
        }
    }

    public BoundSheetRecord(String sheetname) {
        this.field_2_option_flags = 0;
        setSheetname(sheetname);
    }

    public BoundSheetRecord(RecordInputStream in) {
        this.field_1_position_of_BOF = in.readInt();
        this.field_2_option_flags = in.readUShort();
        int field_3_sheetname_length = in.readUByte();
        this.field_4_isMultibyteUnicode = in.readByte();
        if (isMultibyte()) {
            this.field_5_sheetname = in.readUnicodeLEString(field_3_sheetname_length);
        } else {
            this.field_5_sheetname = in.readCompressedUnicode(field_3_sheetname_length);
        }
    }

    public void setPositionOfBof(int pos) {
        this.field_1_position_of_BOF = pos;
    }

    public void setSheetname(String sheetName) {
        WorkbookUtil.validateSheetName(sheetName);
        this.field_5_sheetname = sheetName;
        this.field_4_isMultibyteUnicode = StringUtil.hasMultibyte(sheetName) ? 1 : 0;
    }

    public int getPositionOfBof() {
        return this.field_1_position_of_BOF;
    }

    private boolean isMultibyte() {
        return (this.field_4_isMultibyteUnicode & 1) != 0;
    }

    public String getSheetname() {
        return this.field_5_sheetname;
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[BOUNDSHEET]\n");
        buffer.append("    .bof        = ").append(HexDump.intToHex(getPositionOfBof())).append("\n");
        buffer.append("    .options    = ").append(HexDump.shortToHex(this.field_2_option_flags)).append("\n");
        buffer.append("    .unicodeflag= ").append(HexDump.byteToHex(this.field_4_isMultibyteUnicode)).append("\n");
        buffer.append("    .sheetname  = ").append(this.field_5_sheetname).append("\n");
        buffer.append("[/BOUNDSHEET]\n");
        return buffer.toString();
    }

    protected int getDataSize() {
        return ((isMultibyte() ? 2 : 1) * this.field_5_sheetname.length()) + 8;
    }

    public void serialize(LittleEndianOutput out) {
        out.writeInt(getPositionOfBof());
        out.writeShort(this.field_2_option_flags);
        String name = this.field_5_sheetname;
        out.writeByte(name.length());
        out.writeByte(this.field_4_isMultibyteUnicode);
        if (isMultibyte()) {
            StringUtil.putUnicodeLE(name, out);
        } else {
            StringUtil.putCompressedUnicode(name, out);
        }
    }

    public short getSid() {
        return (short) 133;
    }

    public boolean isHidden() {
        return hiddenFlag.isSet(this.field_2_option_flags);
    }

    public void setHidden(boolean hidden) {
        this.field_2_option_flags = hiddenFlag.setBoolean(this.field_2_option_flags, hidden);
    }

    public boolean isVeryHidden() {
        return veryHiddenFlag.isSet(this.field_2_option_flags);
    }

    public void setVeryHidden(boolean veryHidden) {
        this.field_2_option_flags = veryHiddenFlag.setBoolean(this.field_2_option_flags, veryHidden);
    }

    public static BoundSheetRecord[] orderByBofPosition(List<BoundSheetRecord> boundSheetRecords) {
        BoundSheetRecord[] bsrs = new BoundSheetRecord[boundSheetRecords.size()];
        boundSheetRecords.toArray(bsrs);
        Arrays.sort(bsrs, BOFComparator);
        return bsrs;
    }
}
