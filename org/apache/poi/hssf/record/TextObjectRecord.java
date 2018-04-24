package org.apache.poi.hssf.record;

import org.apache.poi.hssf.record.cont.ContinuableRecord;
import org.apache.poi.hssf.record.cont.ContinuableRecordOutput;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.ss.formula.ptg.OperandPtg;
import org.apache.poi.ss.formula.ptg.Ptg;
import org.apache.poi.util.BitField;
import org.apache.poi.util.BitFieldFactory;
import org.apache.poi.util.HexDump;

public final class TextObjectRecord extends ContinuableRecord {
    private static final int FORMAT_RUN_ENCODED_SIZE = 8;
    public static final short HORIZONTAL_TEXT_ALIGNMENT_CENTERED = (short) 2;
    public static final short HORIZONTAL_TEXT_ALIGNMENT_JUSTIFIED = (short) 4;
    public static final short HORIZONTAL_TEXT_ALIGNMENT_LEFT_ALIGNED = (short) 1;
    public static final short HORIZONTAL_TEXT_ALIGNMENT_RIGHT_ALIGNED = (short) 3;
    private static final BitField HorizontalTextAlignment = BitFieldFactory.getInstance(14);
    public static final short TEXT_ORIENTATION_NONE = (short) 0;
    public static final short TEXT_ORIENTATION_ROT_LEFT = (short) 3;
    public static final short TEXT_ORIENTATION_ROT_RIGHT = (short) 2;
    public static final short TEXT_ORIENTATION_TOP_TO_BOTTOM = (short) 1;
    public static final short VERTICAL_TEXT_ALIGNMENT_BOTTOM = (short) 3;
    public static final short VERTICAL_TEXT_ALIGNMENT_CENTER = (short) 2;
    public static final short VERTICAL_TEXT_ALIGNMENT_JUSTIFY = (short) 4;
    public static final short VERTICAL_TEXT_ALIGNMENT_TOP = (short) 1;
    private static final BitField VerticalTextAlignment = BitFieldFactory.getInstance(112);
    public static final short sid = (short) 438;
    private static final BitField textLocked = BitFieldFactory.getInstance(512);
    private OperandPtg _linkRefPtg;
    private HSSFRichTextString _text;
    private Byte _unknownPostFormulaByte;
    private int _unknownPreFormulaInt;
    private int field_1_options;
    private int field_2_textOrientation;
    private int field_3_reserved4;
    private int field_4_reserved5;
    private int field_5_reserved6;
    private int field_8_reserved7;

    public TextObjectRecord(RecordInputStream in) {
        this.field_1_options = in.readUShort();
        this.field_2_textOrientation = in.readUShort();
        this.field_3_reserved4 = in.readUShort();
        this.field_4_reserved5 = in.readUShort();
        this.field_5_reserved6 = in.readUShort();
        int field_6_textLength = in.readUShort();
        int field_7_formattingDataLength = in.readUShort();
        this.field_8_reserved7 = in.readInt();
        if (in.remaining() <= 0) {
            this._linkRefPtg = null;
        } else if (in.remaining() < 11) {
            throw new RecordFormatException("Not enough remaining data for a link formula");
        } else {
            int formulaSize = in.readUShort();
            this._unknownPreFormulaInt = in.readInt();
            Ptg[] ptgs = Ptg.readTokens(formulaSize, in);
            if (ptgs.length != 1) {
                throw new RecordFormatException("Read " + ptgs.length + " tokens but expected exactly 1");
            }
            this._linkRefPtg = (OperandPtg) ptgs[0];
            if (in.remaining() > 0) {
                this._unknownPostFormulaByte = Byte.valueOf(in.readByte());
            } else {
                this._unknownPostFormulaByte = null;
            }
        }
        if (in.remaining() > 0) {
            throw new RecordFormatException("Unused " + in.remaining() + " bytes at end of record");
        }
        String text;
        if (field_6_textLength > 0) {
            text = readRawString(in, field_6_textLength);
        } else {
            text = "";
        }
        this._text = new HSSFRichTextString(text);
        if (field_7_formattingDataLength > 0) {
            processFontRuns(in, this._text, field_7_formattingDataLength);
        }
    }

    private static String readRawString(RecordInputStream in, int textLength) {
        if ((in.readByte() & 1) == 0) {
            return in.readCompressedUnicode(textLength);
        }
        return in.readUnicodeLEString(textLength);
    }

    private static void processFontRuns(RecordInputStream in, HSSFRichTextString str, int formattingRunDataLength) {
        if (formattingRunDataLength % 8 != 0) {
            throw new RecordFormatException("Bad format run data length " + formattingRunDataLength + ")");
        }
        int nRuns = formattingRunDataLength / 8;
        for (int i = 0; i < nRuns; i++) {
            int index = in.readShort();
            short iFont = in.readShort();
            in.readInt();
            str.applyFont(index, str.length(), iFont);
        }
    }

    public short getSid() {
        return sid;
    }

    private void serializeTXORecord(ContinuableRecordOutput out) {
        out.writeShort(this.field_1_options);
        out.writeShort(this.field_2_textOrientation);
        out.writeShort(this.field_3_reserved4);
        out.writeShort(this.field_4_reserved5);
        out.writeShort(this.field_5_reserved6);
        out.writeShort(this._text.length());
        out.writeShort(getFormattingDataLength());
        out.writeInt(this.field_8_reserved7);
        if (this._linkRefPtg != null) {
            out.writeShort(this._linkRefPtg.getSize());
            out.writeInt(this._unknownPreFormulaInt);
            this._linkRefPtg.write(out);
            if (this._unknownPostFormulaByte != null) {
                out.writeByte(this._unknownPostFormulaByte.byteValue());
            }
        }
    }

    private void serializeTrailingRecords(ContinuableRecordOutput out) {
        out.writeContinue();
        out.writeStringData(this._text.getString());
        out.writeContinue();
        writeFormatData(out, this._text);
    }

    protected void serialize(ContinuableRecordOutput out) {
        serializeTXORecord(out);
        if (this._text.getString().length() > 0) {
            serializeTrailingRecords(out);
        }
    }

    private int getFormattingDataLength() {
        if (this._text.length() < 1) {
            return 0;
        }
        return (this._text.numFormattingRuns() + 1) * 8;
    }

    private static void writeFormatData(ContinuableRecordOutput out, HSSFRichTextString str) {
        int nRuns = str.numFormattingRuns();
        for (int i = 0; i < nRuns; i++) {
            out.writeShort(str.getIndexOfFormattingRun(i));
            int fontIndex = str.getFontOfFormattingRun(i);
            if (fontIndex == 0) {
                fontIndex = 0;
            }
            out.writeShort(fontIndex);
            out.writeInt(0);
        }
        out.writeShort(str.length());
        out.writeShort(0);
        out.writeInt(0);
    }

    public void setHorizontalTextAlignment(int value) {
        this.field_1_options = HorizontalTextAlignment.setValue(this.field_1_options, value);
    }

    public int getHorizontalTextAlignment() {
        return HorizontalTextAlignment.getValue(this.field_1_options);
    }

    public void setVerticalTextAlignment(int value) {
        this.field_1_options = VerticalTextAlignment.setValue(this.field_1_options, value);
    }

    public int getVerticalTextAlignment() {
        return VerticalTextAlignment.getValue(this.field_1_options);
    }

    public void setTextLocked(boolean value) {
        this.field_1_options = textLocked.setBoolean(this.field_1_options, value);
    }

    public boolean isTextLocked() {
        return textLocked.isSet(this.field_1_options);
    }

    public int getTextOrientation() {
        return this.field_2_textOrientation;
    }

    public void setTextOrientation(int textOrientation) {
        this.field_2_textOrientation = textOrientation;
    }

    public HSSFRichTextString getStr() {
        return this._text;
    }

    public void setStr(HSSFRichTextString str) {
        this._text = str;
    }

    public Ptg getLinkRefPtg() {
        return this._linkRefPtg;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("[TXO]\n");
        sb.append("    .options        = ").append(HexDump.shortToHex(this.field_1_options)).append("\n");
        sb.append("         .isHorizontal = ").append(getHorizontalTextAlignment()).append('\n');
        sb.append("         .isVertical   = ").append(getVerticalTextAlignment()).append('\n');
        sb.append("         .textLocked   = ").append(isTextLocked()).append('\n');
        sb.append("    .textOrientation= ").append(HexDump.shortToHex(getTextOrientation())).append("\n");
        sb.append("    .reserved4      = ").append(HexDump.shortToHex(this.field_3_reserved4)).append("\n");
        sb.append("    .reserved5      = ").append(HexDump.shortToHex(this.field_4_reserved5)).append("\n");
        sb.append("    .reserved6      = ").append(HexDump.shortToHex(this.field_5_reserved6)).append("\n");
        sb.append("    .textLength     = ").append(HexDump.shortToHex(this._text.length())).append("\n");
        sb.append("    .reserved7      = ").append(HexDump.intToHex(this.field_8_reserved7)).append("\n");
        sb.append("    .string = ").append(this._text).append('\n');
        for (int i = 0; i < this._text.numFormattingRuns(); i++) {
            sb.append("    .textrun = ").append(this._text.getFontOfFormattingRun(i)).append('\n');
        }
        sb.append("[/TXO]\n");
        return sb.toString();
    }

    public Object clone() {
        TextObjectRecord rec = new TextObjectRecord();
        rec._text = this._text;
        rec.field_1_options = this.field_1_options;
        rec.field_2_textOrientation = this.field_2_textOrientation;
        rec.field_3_reserved4 = this.field_3_reserved4;
        rec.field_4_reserved5 = this.field_4_reserved5;
        rec.field_5_reserved6 = this.field_5_reserved6;
        rec.field_8_reserved7 = this.field_8_reserved7;
        rec._text = this._text;
        if (this._linkRefPtg != null) {
            rec._unknownPreFormulaInt = this._unknownPreFormulaInt;
            rec._linkRefPtg = this._linkRefPtg.copy();
            rec._unknownPostFormulaByte = this._unknownPostFormulaByte;
        }
        return rec;
    }
}
