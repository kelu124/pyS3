package org.apache.poi.hssf.record;

import android.support.v4.view.MotionEventCompat;
import org.apache.poi.util.BitField;
import org.apache.poi.util.BitFieldFactory;
import org.apache.poi.util.LittleEndianOutput;
import org.bytedeco.javacpp.avcodec;

public final class ExtendedFormatRecord extends StandardRecord {
    public static final short ALT_BARS = (short) 3;
    public static final short BIG_SPOTS = (short) 9;
    public static final short BRICKS = (short) 10;
    public static final short CENTER = (short) 2;
    public static final short CENTER_SELECTION = (short) 6;
    public static final short DASHED = (short) 3;
    public static final short DASH_DOT = (short) 9;
    public static final short DASH_DOT_DOT = (short) 11;
    public static final short DIAMONDS = (short) 16;
    public static final short DOTTED = (short) 4;
    public static final short DOUBLE = (short) 6;
    public static final short FILL = (short) 4;
    public static final short FINE_DOTS = (short) 2;
    public static final short GENERAL = (short) 0;
    public static final short HAIR = (short) 7;
    public static final short JUSTIFY = (short) 5;
    public static final short LEFT = (short) 1;
    public static final short MEDIUM = (short) 2;
    public static final short MEDIUM_DASHED = (short) 8;
    public static final short MEDIUM_DASH_DOT = (short) 10;
    public static final short MEDIUM_DASH_DOT_DOT = (short) 12;
    public static final short NONE = (short) 0;
    public static final short NO_FILL = (short) 0;
    public static final short NULL = (short) -16;
    public static final short RIGHT = (short) 3;
    public static final short SLANTED_DASH_DOT = (short) 13;
    public static final short SOLID_FILL = (short) 1;
    public static final short SPARSE_DOTS = (short) 4;
    public static final short SQUARES = (short) 15;
    public static final short THICK = (short) 5;
    public static final short THICK_BACKWARD_DIAG = (short) 7;
    public static final short THICK_FORWARD_DIAG = (short) 8;
    public static final short THICK_HORZ_BANDS = (short) 5;
    public static final short THICK_VERT_BANDS = (short) 6;
    public static final short THIN = (short) 1;
    public static final short THIN_BACKWARD_DIAG = (short) 13;
    public static final short THIN_FORWARD_DIAG = (short) 14;
    public static final short THIN_HORZ_BANDS = (short) 11;
    public static final short THIN_VERT_BANDS = (short) 12;
    public static final short VERTICAL_BOTTOM = (short) 2;
    public static final short VERTICAL_CENTER = (short) 1;
    public static final short VERTICAL_JUSTIFY = (short) 3;
    public static final short VERTICAL_TOP = (short) 0;
    public static final short XF_CELL = (short) 0;
    public static final short XF_STYLE = (short) 1;
    private static final BitField _123_prefix = BitFieldFactory.getInstance(8);
    private static final BitField _adtl_diag = BitFieldFactory.getInstance(2080768);
    private static final BitField _adtl_diag_line_style = BitFieldFactory.getInstance(31457280);
    private static final BitField _adtl_fill_pattern = BitFieldFactory.getInstance(-67108864);
    private static final BitField _alignment = BitFieldFactory.getInstance(7);
    private static final BitField _border_bottom = BitFieldFactory.getInstance(avcodec.MB_TYPE_L0L1);
    private static final BitField _border_left = BitFieldFactory.getInstance(15);
    private static final BitField _border_right = BitFieldFactory.getInstance(240);
    private static final BitField _border_top = BitFieldFactory.getInstance(3840);
    private static final BitField _bottom_border_palette_idx = BitFieldFactory.getInstance(16256);
    private static final BitField _diag = BitFieldFactory.getInstance(avcodec.MB_TYPE_L1);
    private static final BitField _fill_background = BitFieldFactory.getInstance(16256);
    private static final BitField _fill_foreground = BitFieldFactory.getInstance(127);
    private static final BitField _hidden = BitFieldFactory.getInstance(2);
    private static final BitField _indent = BitFieldFactory.getInstance(15);
    private static final BitField _indent_not_parent_alignment = BitFieldFactory.getInstance(4096);
    private static final BitField _indent_not_parent_border = BitFieldFactory.getInstance(8192);
    private static final BitField _indent_not_parent_cell_options = BitFieldFactory.getInstance(32768);
    private static final BitField _indent_not_parent_font = BitFieldFactory.getInstance(2048);
    private static final BitField _indent_not_parent_format = BitFieldFactory.getInstance(1024);
    private static final BitField _indent_not_parent_pattern = BitFieldFactory.getInstance(16384);
    private static final BitField _justify_last = BitFieldFactory.getInstance(128);
    private static final BitField _left_border_palette_idx = BitFieldFactory.getInstance(127);
    private static final BitField _locked = BitFieldFactory.getInstance(1);
    private static final BitField _merge_cells = BitFieldFactory.getInstance(32);
    private static final BitField _parent_index = BitFieldFactory.getInstance(65520);
    private static final BitField _reading_order = BitFieldFactory.getInstance(192);
    private static final BitField _right_border_palette_idx = BitFieldFactory.getInstance(16256);
    private static final BitField _rotation = BitFieldFactory.getInstance(MotionEventCompat.ACTION_POINTER_INDEX_MASK);
    private static final BitField _shrink_to_fit = BitFieldFactory.getInstance(16);
    private static final BitField _top_border_palette_idx = BitFieldFactory.getInstance(127);
    private static final BitField _vertical_alignment = BitFieldFactory.getInstance(112);
    private static final BitField _wrap_text = BitFieldFactory.getInstance(8);
    private static final BitField _xf_type = BitFieldFactory.getInstance(4);
    public static final short sid = (short) 224;
    private short field_1_font_index;
    private short field_2_format_index;
    private short field_3_cell_options;
    private short field_4_alignment_options;
    private short field_5_indention_options;
    private short field_6_border_options;
    private short field_7_palette_options;
    private int field_8_adtl_palette_options;
    private short field_9_fill_palette_options;

    public ExtendedFormatRecord(RecordInputStream in) {
        this.field_1_font_index = in.readShort();
        this.field_2_format_index = in.readShort();
        this.field_3_cell_options = in.readShort();
        this.field_4_alignment_options = in.readShort();
        this.field_5_indention_options = in.readShort();
        this.field_6_border_options = in.readShort();
        this.field_7_palette_options = in.readShort();
        this.field_8_adtl_palette_options = in.readInt();
        this.field_9_fill_palette_options = in.readShort();
    }

    public void setFontIndex(short index) {
        this.field_1_font_index = index;
    }

    public void setFormatIndex(short index) {
        this.field_2_format_index = index;
    }

    public void setCellOptions(short options) {
        this.field_3_cell_options = options;
    }

    public void setLocked(boolean locked) {
        this.field_3_cell_options = _locked.setShortBoolean(this.field_3_cell_options, locked);
    }

    public void setHidden(boolean hidden) {
        this.field_3_cell_options = _hidden.setShortBoolean(this.field_3_cell_options, hidden);
    }

    public void setXFType(short type) {
        this.field_3_cell_options = _xf_type.setShortValue(this.field_3_cell_options, type);
    }

    public void set123Prefix(boolean prefix) {
        this.field_3_cell_options = _123_prefix.setShortBoolean(this.field_3_cell_options, prefix);
    }

    public void setParentIndex(short parent) {
        this.field_3_cell_options = _parent_index.setShortValue(this.field_3_cell_options, parent);
    }

    public void setAlignmentOptions(short options) {
        this.field_4_alignment_options = options;
    }

    public void setAlignment(short align) {
        this.field_4_alignment_options = _alignment.setShortValue(this.field_4_alignment_options, align);
    }

    public void setWrapText(boolean wrapped) {
        this.field_4_alignment_options = _wrap_text.setShortBoolean(this.field_4_alignment_options, wrapped);
    }

    public void setVerticalAlignment(short align) {
        this.field_4_alignment_options = _vertical_alignment.setShortValue(this.field_4_alignment_options, align);
    }

    public void setJustifyLast(short justify) {
        this.field_4_alignment_options = _justify_last.setShortValue(this.field_4_alignment_options, justify);
    }

    public void setRotation(short rotation) {
        this.field_4_alignment_options = _rotation.setShortValue(this.field_4_alignment_options, rotation);
    }

    public void setIndentionOptions(short options) {
        this.field_5_indention_options = options;
    }

    public void setIndent(short indent) {
        this.field_5_indention_options = _indent.setShortValue(this.field_5_indention_options, indent);
    }

    public void setShrinkToFit(boolean shrink) {
        this.field_5_indention_options = _shrink_to_fit.setShortBoolean(this.field_5_indention_options, shrink);
    }

    public void setMergeCells(boolean merge) {
        this.field_5_indention_options = _merge_cells.setShortBoolean(this.field_5_indention_options, merge);
    }

    public void setReadingOrder(short order) {
        this.field_5_indention_options = _reading_order.setShortValue(this.field_5_indention_options, order);
    }

    public void setIndentNotParentFormat(boolean parent) {
        this.field_5_indention_options = _indent_not_parent_format.setShortBoolean(this.field_5_indention_options, parent);
    }

    public void setIndentNotParentFont(boolean font) {
        this.field_5_indention_options = _indent_not_parent_font.setShortBoolean(this.field_5_indention_options, font);
    }

    public void setIndentNotParentAlignment(boolean alignment) {
        this.field_5_indention_options = _indent_not_parent_alignment.setShortBoolean(this.field_5_indention_options, alignment);
    }

    public void setIndentNotParentBorder(boolean border) {
        this.field_5_indention_options = _indent_not_parent_border.setShortBoolean(this.field_5_indention_options, border);
    }

    public void setIndentNotParentPattern(boolean pattern) {
        this.field_5_indention_options = _indent_not_parent_pattern.setShortBoolean(this.field_5_indention_options, pattern);
    }

    public void setIndentNotParentCellOptions(boolean options) {
        this.field_5_indention_options = _indent_not_parent_cell_options.setShortBoolean(this.field_5_indention_options, options);
    }

    public void setBorderOptions(short options) {
        this.field_6_border_options = options;
    }

    public void setBorderLeft(short border) {
        this.field_6_border_options = _border_left.setShortValue(this.field_6_border_options, border);
    }

    public void setBorderRight(short border) {
        this.field_6_border_options = _border_right.setShortValue(this.field_6_border_options, border);
    }

    public void setBorderTop(short border) {
        this.field_6_border_options = _border_top.setShortValue(this.field_6_border_options, border);
    }

    public void setBorderBottom(short border) {
        this.field_6_border_options = _border_bottom.setShortValue(this.field_6_border_options, border);
    }

    public void setPaletteOptions(short options) {
        this.field_7_palette_options = options;
    }

    public void setLeftBorderPaletteIdx(short border) {
        this.field_7_palette_options = _left_border_palette_idx.setShortValue(this.field_7_palette_options, border);
    }

    public void setRightBorderPaletteIdx(short border) {
        this.field_7_palette_options = _right_border_palette_idx.setShortValue(this.field_7_palette_options, border);
    }

    public void setDiag(short diag) {
        this.field_7_palette_options = _diag.setShortValue(this.field_7_palette_options, diag);
    }

    public void setAdtlPaletteOptions(short options) {
        this.field_8_adtl_palette_options = options;
    }

    public void setTopBorderPaletteIdx(short border) {
        this.field_8_adtl_palette_options = _top_border_palette_idx.setValue(this.field_8_adtl_palette_options, border);
    }

    public void setBottomBorderPaletteIdx(short border) {
        this.field_8_adtl_palette_options = _bottom_border_palette_idx.setValue(this.field_8_adtl_palette_options, border);
    }

    public void setAdtlDiag(short diag) {
        this.field_8_adtl_palette_options = _adtl_diag.setValue(this.field_8_adtl_palette_options, diag);
    }

    public void setAdtlDiagLineStyle(short diag) {
        this.field_8_adtl_palette_options = _adtl_diag_line_style.setValue(this.field_8_adtl_palette_options, diag);
    }

    public void setAdtlFillPattern(short fill) {
        this.field_8_adtl_palette_options = _adtl_fill_pattern.setValue(this.field_8_adtl_palette_options, fill);
    }

    public void setFillPaletteOptions(short options) {
        this.field_9_fill_palette_options = options;
    }

    public void setFillForeground(short color) {
        this.field_9_fill_palette_options = _fill_foreground.setShortValue(this.field_9_fill_palette_options, color);
    }

    public void setFillBackground(short color) {
        this.field_9_fill_palette_options = _fill_background.setShortValue(this.field_9_fill_palette_options, color);
    }

    public short getFontIndex() {
        return this.field_1_font_index;
    }

    public short getFormatIndex() {
        return this.field_2_format_index;
    }

    public short getCellOptions() {
        return this.field_3_cell_options;
    }

    public boolean isLocked() {
        return _locked.isSet(this.field_3_cell_options);
    }

    public boolean isHidden() {
        return _hidden.isSet(this.field_3_cell_options);
    }

    public short getXFType() {
        return _xf_type.getShortValue(this.field_3_cell_options);
    }

    public boolean get123Prefix() {
        return _123_prefix.isSet(this.field_3_cell_options);
    }

    public short getParentIndex() {
        return _parent_index.getShortValue(this.field_3_cell_options);
    }

    public short getAlignmentOptions() {
        return this.field_4_alignment_options;
    }

    public short getAlignment() {
        return _alignment.getShortValue(this.field_4_alignment_options);
    }

    public boolean getWrapText() {
        return _wrap_text.isSet(this.field_4_alignment_options);
    }

    public short getVerticalAlignment() {
        return _vertical_alignment.getShortValue(this.field_4_alignment_options);
    }

    public short getJustifyLast() {
        return _justify_last.getShortValue(this.field_4_alignment_options);
    }

    public short getRotation() {
        return _rotation.getShortValue(this.field_4_alignment_options);
    }

    public short getIndentionOptions() {
        return this.field_5_indention_options;
    }

    public short getIndent() {
        return _indent.getShortValue(this.field_5_indention_options);
    }

    public boolean getShrinkToFit() {
        return _shrink_to_fit.isSet(this.field_5_indention_options);
    }

    public boolean getMergeCells() {
        return _merge_cells.isSet(this.field_5_indention_options);
    }

    public short getReadingOrder() {
        return _reading_order.getShortValue(this.field_5_indention_options);
    }

    public boolean isIndentNotParentFormat() {
        return _indent_not_parent_format.isSet(this.field_5_indention_options);
    }

    public boolean isIndentNotParentFont() {
        return _indent_not_parent_font.isSet(this.field_5_indention_options);
    }

    public boolean isIndentNotParentAlignment() {
        return _indent_not_parent_alignment.isSet(this.field_5_indention_options);
    }

    public boolean isIndentNotParentBorder() {
        return _indent_not_parent_border.isSet(this.field_5_indention_options);
    }

    public boolean isIndentNotParentPattern() {
        return _indent_not_parent_pattern.isSet(this.field_5_indention_options);
    }

    public boolean isIndentNotParentCellOptions() {
        return _indent_not_parent_cell_options.isSet(this.field_5_indention_options);
    }

    public short getBorderOptions() {
        return this.field_6_border_options;
    }

    public short getBorderLeft() {
        return _border_left.getShortValue(this.field_6_border_options);
    }

    public short getBorderRight() {
        return _border_right.getShortValue(this.field_6_border_options);
    }

    public short getBorderTop() {
        return _border_top.getShortValue(this.field_6_border_options);
    }

    public short getBorderBottom() {
        return _border_bottom.getShortValue(this.field_6_border_options);
    }

    public short getPaletteOptions() {
        return this.field_7_palette_options;
    }

    public short getLeftBorderPaletteIdx() {
        return _left_border_palette_idx.getShortValue(this.field_7_palette_options);
    }

    public short getRightBorderPaletteIdx() {
        return _right_border_palette_idx.getShortValue(this.field_7_palette_options);
    }

    public short getDiag() {
        return _diag.getShortValue(this.field_7_palette_options);
    }

    public int getAdtlPaletteOptions() {
        return this.field_8_adtl_palette_options;
    }

    public short getTopBorderPaletteIdx() {
        return (short) _top_border_palette_idx.getValue(this.field_8_adtl_palette_options);
    }

    public short getBottomBorderPaletteIdx() {
        return (short) _bottom_border_palette_idx.getValue(this.field_8_adtl_palette_options);
    }

    public short getAdtlDiag() {
        return (short) _adtl_diag.getValue(this.field_8_adtl_palette_options);
    }

    public short getAdtlDiagLineStyle() {
        return (short) _adtl_diag_line_style.getValue(this.field_8_adtl_palette_options);
    }

    public short getAdtlFillPattern() {
        return (short) _adtl_fill_pattern.getValue(this.field_8_adtl_palette_options);
    }

    public short getFillPaletteOptions() {
        return this.field_9_fill_palette_options;
    }

    public short getFillForeground() {
        return _fill_foreground.getShortValue(this.field_9_fill_palette_options);
    }

    public short getFillBackground() {
        return _fill_background.getShortValue(this.field_9_fill_palette_options);
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[EXTENDEDFORMAT]\n");
        if (getXFType() == (short) 1) {
            buffer.append(" STYLE_RECORD_TYPE\n");
        } else if (getXFType() == (short) 0) {
            buffer.append(" CELL_RECORD_TYPE\n");
        }
        buffer.append("    .fontindex       = ").append(Integer.toHexString(getFontIndex())).append("\n");
        buffer.append("    .formatindex     = ").append(Integer.toHexString(getFormatIndex())).append("\n");
        buffer.append("    .celloptions     = ").append(Integer.toHexString(getCellOptions())).append("\n");
        buffer.append("          .islocked  = ").append(isLocked()).append("\n");
        buffer.append("          .ishidden  = ").append(isHidden()).append("\n");
        buffer.append("          .recordtype= ").append(Integer.toHexString(getXFType())).append("\n");
        buffer.append("          .parentidx = ").append(Integer.toHexString(getParentIndex())).append("\n");
        buffer.append("    .alignmentoptions= ").append(Integer.toHexString(getAlignmentOptions())).append("\n");
        buffer.append("          .alignment = ").append(getAlignment()).append("\n");
        buffer.append("          .wraptext  = ").append(getWrapText()).append("\n");
        buffer.append("          .valignment= ").append(Integer.toHexString(getVerticalAlignment())).append("\n");
        buffer.append("          .justlast  = ").append(Integer.toHexString(getJustifyLast())).append("\n");
        buffer.append("          .rotation  = ").append(Integer.toHexString(getRotation())).append("\n");
        buffer.append("    .indentionoptions= ").append(Integer.toHexString(getIndentionOptions())).append("\n");
        buffer.append("          .indent    = ").append(Integer.toHexString(getIndent())).append("\n");
        buffer.append("          .shrinktoft= ").append(getShrinkToFit()).append("\n");
        buffer.append("          .mergecells= ").append(getMergeCells()).append("\n");
        buffer.append("          .readngordr= ").append(Integer.toHexString(getReadingOrder())).append("\n");
        buffer.append("          .formatflag= ").append(isIndentNotParentFormat()).append("\n");
        buffer.append("          .fontflag  = ").append(isIndentNotParentFont()).append("\n");
        buffer.append("          .prntalgnmt= ").append(isIndentNotParentAlignment()).append("\n");
        buffer.append("          .borderflag= ").append(isIndentNotParentBorder()).append("\n");
        buffer.append("          .paternflag= ").append(isIndentNotParentPattern()).append("\n");
        buffer.append("          .celloption= ").append(isIndentNotParentCellOptions()).append("\n");
        buffer.append("    .borderoptns     = ").append(Integer.toHexString(getBorderOptions())).append("\n");
        buffer.append("          .lftln     = ").append(Integer.toHexString(getBorderLeft())).append("\n");
        buffer.append("          .rgtln     = ").append(Integer.toHexString(getBorderRight())).append("\n");
        buffer.append("          .topln     = ").append(Integer.toHexString(getBorderTop())).append("\n");
        buffer.append("          .btmln     = ").append(Integer.toHexString(getBorderBottom())).append("\n");
        buffer.append("    .paleteoptns     = ").append(Integer.toHexString(getPaletteOptions())).append("\n");
        buffer.append("          .leftborder= ").append(Integer.toHexString(getLeftBorderPaletteIdx())).append("\n");
        buffer.append("          .rghtborder= ").append(Integer.toHexString(getRightBorderPaletteIdx())).append("\n");
        buffer.append("          .diag      = ").append(Integer.toHexString(getDiag())).append("\n");
        buffer.append("    .paleteoptn2     = ").append(Integer.toHexString(getAdtlPaletteOptions())).append("\n");
        buffer.append("          .topborder = ").append(Integer.toHexString(getTopBorderPaletteIdx())).append("\n");
        buffer.append("          .botmborder= ").append(Integer.toHexString(getBottomBorderPaletteIdx())).append("\n");
        buffer.append("          .adtldiag  = ").append(Integer.toHexString(getAdtlDiag())).append("\n");
        buffer.append("          .diaglnstyl= ").append(Integer.toHexString(getAdtlDiagLineStyle())).append("\n");
        buffer.append("          .fillpattrn= ").append(Integer.toHexString(getAdtlFillPattern())).append("\n");
        buffer.append("    .fillpaloptn     = ").append(Integer.toHexString(getFillPaletteOptions())).append("\n");
        buffer.append("          .foreground= ").append(Integer.toHexString(getFillForeground())).append("\n");
        buffer.append("          .background= ").append(Integer.toHexString(getFillBackground())).append("\n");
        buffer.append("[/EXTENDEDFORMAT]\n");
        return buffer.toString();
    }

    public void serialize(LittleEndianOutput out) {
        out.writeShort(getFontIndex());
        out.writeShort(getFormatIndex());
        out.writeShort(getCellOptions());
        out.writeShort(getAlignmentOptions());
        out.writeShort(getIndentionOptions());
        out.writeShort(getBorderOptions());
        out.writeShort(getPaletteOptions());
        out.writeInt(getAdtlPaletteOptions());
        out.writeShort(getFillPaletteOptions());
    }

    protected int getDataSize() {
        return 20;
    }

    public short getSid() {
        return sid;
    }

    public void cloneStyleFrom(ExtendedFormatRecord source) {
        this.field_1_font_index = source.field_1_font_index;
        this.field_2_format_index = source.field_2_format_index;
        this.field_3_cell_options = source.field_3_cell_options;
        this.field_4_alignment_options = source.field_4_alignment_options;
        this.field_5_indention_options = source.field_5_indention_options;
        this.field_6_border_options = source.field_6_border_options;
        this.field_7_palette_options = source.field_7_palette_options;
        this.field_8_adtl_palette_options = source.field_8_adtl_palette_options;
        this.field_9_fill_palette_options = source.field_9_fill_palette_options;
    }

    public int hashCode() {
        return ((((((((((((((((this.field_1_font_index + 31) * 31) + this.field_2_format_index) * 31) + this.field_3_cell_options) * 31) + this.field_4_alignment_options) * 31) + this.field_5_indention_options) * 31) + this.field_6_border_options) * 31) + this.field_7_palette_options) * 31) + this.field_8_adtl_palette_options) * 31) + this.field_9_fill_palette_options;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof ExtendedFormatRecord)) {
            return false;
        }
        ExtendedFormatRecord other = (ExtendedFormatRecord) obj;
        if (this.field_1_font_index != other.field_1_font_index) {
            return false;
        }
        if (this.field_2_format_index != other.field_2_format_index) {
            return false;
        }
        if (this.field_3_cell_options != other.field_3_cell_options) {
            return false;
        }
        if (this.field_4_alignment_options != other.field_4_alignment_options) {
            return false;
        }
        if (this.field_5_indention_options != other.field_5_indention_options) {
            return false;
        }
        if (this.field_6_border_options != other.field_6_border_options) {
            return false;
        }
        if (this.field_7_palette_options != other.field_7_palette_options) {
            return false;
        }
        if (this.field_8_adtl_palette_options != other.field_8_adtl_palette_options) {
            return false;
        }
        if (this.field_9_fill_palette_options != other.field_9_fill_palette_options) {
            return false;
        }
        return true;
    }

    public int[] stateSummary() {
        return new int[]{this.field_1_font_index, this.field_2_format_index, this.field_3_cell_options, this.field_4_alignment_options, this.field_5_indention_options, this.field_6_border_options, this.field_7_palette_options, this.field_8_adtl_palette_options, this.field_9_fill_palette_options};
    }
}
