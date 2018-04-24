package org.apache.poi.hssf.record;

import org.apache.poi.util.BitField;
import org.apache.poi.util.BitFieldFactory;
import org.apache.poi.util.LittleEndianOutput;

public final class WindowTwoRecord extends StandardRecord {
    private static final BitField active = BitFieldFactory.getInstance(1024);
    private static final BitField arabic = BitFieldFactory.getInstance(64);
    private static final BitField defaultHeader = BitFieldFactory.getInstance(32);
    private static final BitField displayFormulas = BitFieldFactory.getInstance(1);
    private static final BitField displayGridlines = BitFieldFactory.getInstance(2);
    private static final BitField displayGuts = BitFieldFactory.getInstance(128);
    private static final BitField displayRowColHeadings = BitFieldFactory.getInstance(4);
    private static final BitField displayZeros = BitFieldFactory.getInstance(16);
    private static final BitField freezePanes = BitFieldFactory.getInstance(8);
    private static final BitField freezePanesNoSplit = BitFieldFactory.getInstance(256);
    private static final BitField savedInPageBreakPreview = BitFieldFactory.getInstance(2048);
    private static final BitField selected = BitFieldFactory.getInstance(512);
    public static final short sid = (short) 574;
    private short field_1_options;
    private short field_2_top_row;
    private short field_3_left_col;
    private int field_4_header_color;
    private short field_5_page_break_zoom;
    private short field_6_normal_zoom;
    private int field_7_reserved;

    public WindowTwoRecord(RecordInputStream in) {
        int size = in.remaining();
        this.field_1_options = in.readShort();
        this.field_2_top_row = in.readShort();
        this.field_3_left_col = in.readShort();
        this.field_4_header_color = in.readInt();
        if (size > 10) {
            this.field_5_page_break_zoom = in.readShort();
            this.field_6_normal_zoom = in.readShort();
        }
        if (size > 14) {
            this.field_7_reserved = in.readInt();
        }
    }

    public void setOptions(short options) {
        this.field_1_options = options;
    }

    public void setDisplayFormulas(boolean formulas) {
        this.field_1_options = displayFormulas.setShortBoolean(this.field_1_options, formulas);
    }

    public void setDisplayGridlines(boolean gridlines) {
        this.field_1_options = displayGridlines.setShortBoolean(this.field_1_options, gridlines);
    }

    public void setDisplayRowColHeadings(boolean headings) {
        this.field_1_options = displayRowColHeadings.setShortBoolean(this.field_1_options, headings);
    }

    public void setFreezePanes(boolean freezepanes) {
        this.field_1_options = freezePanes.setShortBoolean(this.field_1_options, freezepanes);
    }

    public void setDisplayZeros(boolean zeros) {
        this.field_1_options = displayZeros.setShortBoolean(this.field_1_options, zeros);
    }

    public void setDefaultHeader(boolean header) {
        this.field_1_options = defaultHeader.setShortBoolean(this.field_1_options, header);
    }

    public void setArabic(boolean isarabic) {
        this.field_1_options = arabic.setShortBoolean(this.field_1_options, isarabic);
    }

    public void setDisplayGuts(boolean guts) {
        this.field_1_options = displayGuts.setShortBoolean(this.field_1_options, guts);
    }

    public void setFreezePanesNoSplit(boolean freeze) {
        this.field_1_options = freezePanesNoSplit.setShortBoolean(this.field_1_options, freeze);
    }

    public void setSelected(boolean sel) {
        this.field_1_options = selected.setShortBoolean(this.field_1_options, sel);
    }

    public void setActive(boolean p) {
        this.field_1_options = active.setShortBoolean(this.field_1_options, p);
    }

    public void setSavedInPageBreakPreview(boolean p) {
        this.field_1_options = savedInPageBreakPreview.setShortBoolean(this.field_1_options, p);
    }

    public void setTopRow(short topRow) {
        this.field_2_top_row = topRow;
    }

    public void setLeftCol(short leftCol) {
        this.field_3_left_col = leftCol;
    }

    public void setHeaderColor(int color) {
        this.field_4_header_color = color;
    }

    public void setPageBreakZoom(short zoom) {
        this.field_5_page_break_zoom = zoom;
    }

    public void setNormalZoom(short zoom) {
        this.field_6_normal_zoom = zoom;
    }

    public void setReserved(int reserved) {
        this.field_7_reserved = reserved;
    }

    public short getOptions() {
        return this.field_1_options;
    }

    public boolean getDisplayFormulas() {
        return displayFormulas.isSet(this.field_1_options);
    }

    public boolean getDisplayGridlines() {
        return displayGridlines.isSet(this.field_1_options);
    }

    public boolean getDisplayRowColHeadings() {
        return displayRowColHeadings.isSet(this.field_1_options);
    }

    public boolean getFreezePanes() {
        return freezePanes.isSet(this.field_1_options);
    }

    public boolean getDisplayZeros() {
        return displayZeros.isSet(this.field_1_options);
    }

    public boolean getDefaultHeader() {
        return defaultHeader.isSet(this.field_1_options);
    }

    public boolean getArabic() {
        return arabic.isSet(this.field_1_options);
    }

    public boolean getDisplayGuts() {
        return displayGuts.isSet(this.field_1_options);
    }

    public boolean getFreezePanesNoSplit() {
        return freezePanesNoSplit.isSet(this.field_1_options);
    }

    public boolean getSelected() {
        return selected.isSet(this.field_1_options);
    }

    public boolean isActive() {
        return active.isSet(this.field_1_options);
    }

    public boolean getSavedInPageBreakPreview() {
        return savedInPageBreakPreview.isSet(this.field_1_options);
    }

    public short getTopRow() {
        return this.field_2_top_row;
    }

    public short getLeftCol() {
        return this.field_3_left_col;
    }

    public int getHeaderColor() {
        return this.field_4_header_color;
    }

    public short getPageBreakZoom() {
        return this.field_5_page_break_zoom;
    }

    public short getNormalZoom() {
        return this.field_6_normal_zoom;
    }

    public int getReserved() {
        return this.field_7_reserved;
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[WINDOW2]\n");
        buffer.append("    .options        = ").append(Integer.toHexString(getOptions())).append("\n");
        buffer.append("       .dispformulas= ").append(getDisplayFormulas()).append("\n");
        buffer.append("       .dispgridlins= ").append(getDisplayGridlines()).append("\n");
        buffer.append("       .disprcheadin= ").append(getDisplayRowColHeadings()).append("\n");
        buffer.append("       .freezepanes = ").append(getFreezePanes()).append("\n");
        buffer.append("       .displayzeros= ").append(getDisplayZeros()).append("\n");
        buffer.append("       .defaultheadr= ").append(getDefaultHeader()).append("\n");
        buffer.append("       .arabic      = ").append(getArabic()).append("\n");
        buffer.append("       .displayguts = ").append(getDisplayGuts()).append("\n");
        buffer.append("       .frzpnsnosplt= ").append(getFreezePanesNoSplit()).append("\n");
        buffer.append("       .selected    = ").append(getSelected()).append("\n");
        buffer.append("       .active       = ").append(isActive()).append("\n");
        buffer.append("       .svdinpgbrkpv= ").append(getSavedInPageBreakPreview()).append("\n");
        buffer.append("    .toprow         = ").append(Integer.toHexString(getTopRow())).append("\n");
        buffer.append("    .leftcol        = ").append(Integer.toHexString(getLeftCol())).append("\n");
        buffer.append("    .headercolor    = ").append(Integer.toHexString(getHeaderColor())).append("\n");
        buffer.append("    .pagebreakzoom  = ").append(Integer.toHexString(getPageBreakZoom())).append("\n");
        buffer.append("    .normalzoom     = ").append(Integer.toHexString(getNormalZoom())).append("\n");
        buffer.append("    .reserved       = ").append(Integer.toHexString(getReserved())).append("\n");
        buffer.append("[/WINDOW2]\n");
        return buffer.toString();
    }

    public void serialize(LittleEndianOutput out) {
        out.writeShort(getOptions());
        out.writeShort(getTopRow());
        out.writeShort(getLeftCol());
        out.writeInt(getHeaderColor());
        out.writeShort(getPageBreakZoom());
        out.writeShort(getNormalZoom());
        out.writeInt(getReserved());
    }

    protected int getDataSize() {
        return 18;
    }

    public short getSid() {
        return (short) 574;
    }

    public Object clone() {
        WindowTwoRecord rec = new WindowTwoRecord();
        rec.field_1_options = this.field_1_options;
        rec.field_2_top_row = this.field_2_top_row;
        rec.field_3_left_col = this.field_3_left_col;
        rec.field_4_header_color = this.field_4_header_color;
        rec.field_5_page_break_zoom = this.field_5_page_break_zoom;
        rec.field_6_normal_zoom = this.field_6_normal_zoom;
        rec.field_7_reserved = this.field_7_reserved;
        return rec;
    }
}
