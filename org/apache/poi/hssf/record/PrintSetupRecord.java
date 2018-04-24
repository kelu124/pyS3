package org.apache.poi.hssf.record;

import org.apache.poi.util.BitField;
import org.apache.poi.util.BitFieldFactory;
import org.apache.poi.util.LittleEndianOutput;

public final class PrintSetupRecord extends StandardRecord {
    private static final BitField draft = BitFieldFactory.getInstance(16);
    private static final BitField landscape = BitFieldFactory.getInstance(2);
    private static final BitField lefttoright = BitFieldFactory.getInstance(1);
    private static final BitField noOrientation = BitFieldFactory.getInstance(64);
    private static final BitField nocolor = BitFieldFactory.getInstance(8);
    private static final BitField notes = BitFieldFactory.getInstance(32);
    public static final short sid = (short) 161;
    private static final BitField usepage = BitFieldFactory.getInstance(128);
    private static final BitField validsettings = BitFieldFactory.getInstance(4);
    private double field_10_footermargin;
    private short field_11_copies;
    private short field_1_paper_size;
    private short field_2_scale;
    private short field_3_page_start;
    private short field_4_fit_width;
    private short field_5_fit_height;
    private short field_6_options;
    private short field_7_hresolution;
    private short field_8_vresolution;
    private double field_9_headermargin;

    public PrintSetupRecord(RecordInputStream in) {
        this.field_1_paper_size = in.readShort();
        this.field_2_scale = in.readShort();
        this.field_3_page_start = in.readShort();
        this.field_4_fit_width = in.readShort();
        this.field_5_fit_height = in.readShort();
        this.field_6_options = in.readShort();
        this.field_7_hresolution = in.readShort();
        this.field_8_vresolution = in.readShort();
        this.field_9_headermargin = in.readDouble();
        this.field_10_footermargin = in.readDouble();
        this.field_11_copies = in.readShort();
    }

    public void setPaperSize(short size) {
        this.field_1_paper_size = size;
    }

    public void setScale(short scale) {
        this.field_2_scale = scale;
    }

    public void setPageStart(short start) {
        this.field_3_page_start = start;
    }

    public void setFitWidth(short width) {
        this.field_4_fit_width = width;
    }

    public void setFitHeight(short height) {
        this.field_5_fit_height = height;
    }

    public void setOptions(short options) {
        this.field_6_options = options;
    }

    public void setLeftToRight(boolean ltor) {
        this.field_6_options = lefttoright.setShortBoolean(this.field_6_options, ltor);
    }

    public void setLandscape(boolean ls) {
        this.field_6_options = landscape.setShortBoolean(this.field_6_options, ls);
    }

    public void setValidSettings(boolean valid) {
        this.field_6_options = validsettings.setShortBoolean(this.field_6_options, valid);
    }

    public void setNoColor(boolean mono) {
        this.field_6_options = nocolor.setShortBoolean(this.field_6_options, mono);
    }

    public void setDraft(boolean d) {
        this.field_6_options = draft.setShortBoolean(this.field_6_options, d);
    }

    public void setNotes(boolean printnotes) {
        this.field_6_options = notes.setShortBoolean(this.field_6_options, printnotes);
    }

    public void setNoOrientation(boolean orientation) {
        this.field_6_options = noOrientation.setShortBoolean(this.field_6_options, orientation);
    }

    public void setUsePage(boolean page) {
        this.field_6_options = usepage.setShortBoolean(this.field_6_options, page);
    }

    public void setHResolution(short resolution) {
        this.field_7_hresolution = resolution;
    }

    public void setVResolution(short resolution) {
        this.field_8_vresolution = resolution;
    }

    public void setHeaderMargin(double headermargin) {
        this.field_9_headermargin = headermargin;
    }

    public void setFooterMargin(double footermargin) {
        this.field_10_footermargin = footermargin;
    }

    public void setCopies(short copies) {
        this.field_11_copies = copies;
    }

    public short getPaperSize() {
        return this.field_1_paper_size;
    }

    public short getScale() {
        return this.field_2_scale;
    }

    public short getPageStart() {
        return this.field_3_page_start;
    }

    public short getFitWidth() {
        return this.field_4_fit_width;
    }

    public short getFitHeight() {
        return this.field_5_fit_height;
    }

    public short getOptions() {
        return this.field_6_options;
    }

    public boolean getLeftToRight() {
        return lefttoright.isSet(this.field_6_options);
    }

    public boolean getLandscape() {
        return landscape.isSet(this.field_6_options);
    }

    public boolean getValidSettings() {
        return validsettings.isSet(this.field_6_options);
    }

    public boolean getNoColor() {
        return nocolor.isSet(this.field_6_options);
    }

    public boolean getDraft() {
        return draft.isSet(this.field_6_options);
    }

    public boolean getNotes() {
        return notes.isSet(this.field_6_options);
    }

    public boolean getNoOrientation() {
        return noOrientation.isSet(this.field_6_options);
    }

    public boolean getUsePage() {
        return usepage.isSet(this.field_6_options);
    }

    public short getHResolution() {
        return this.field_7_hresolution;
    }

    public short getVResolution() {
        return this.field_8_vresolution;
    }

    public double getHeaderMargin() {
        return this.field_9_headermargin;
    }

    public double getFooterMargin() {
        return this.field_10_footermargin;
    }

    public short getCopies() {
        return this.field_11_copies;
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[PRINTSETUP]\n");
        buffer.append("    .papersize      = ").append(getPaperSize()).append("\n");
        buffer.append("    .scale          = ").append(getScale()).append("\n");
        buffer.append("    .pagestart      = ").append(getPageStart()).append("\n");
        buffer.append("    .fitwidth       = ").append(getFitWidth()).append("\n");
        buffer.append("    .fitheight      = ").append(getFitHeight()).append("\n");
        buffer.append("    .options        = ").append(getOptions()).append("\n");
        buffer.append("        .ltor       = ").append(getLeftToRight()).append("\n");
        buffer.append("        .landscape  = ").append(getLandscape()).append("\n");
        buffer.append("        .valid      = ").append(getValidSettings()).append("\n");
        buffer.append("        .mono       = ").append(getNoColor()).append("\n");
        buffer.append("        .draft      = ").append(getDraft()).append("\n");
        buffer.append("        .notes      = ").append(getNotes()).append("\n");
        buffer.append("        .noOrientat = ").append(getNoOrientation()).append("\n");
        buffer.append("        .usepage    = ").append(getUsePage()).append("\n");
        buffer.append("    .hresolution    = ").append(getHResolution()).append("\n");
        buffer.append("    .vresolution    = ").append(getVResolution()).append("\n");
        buffer.append("    .headermargin   = ").append(getHeaderMargin()).append("\n");
        buffer.append("    .footermargin   = ").append(getFooterMargin()).append("\n");
        buffer.append("    .copies         = ").append(getCopies()).append("\n");
        buffer.append("[/PRINTSETUP]\n");
        return buffer.toString();
    }

    public void serialize(LittleEndianOutput out) {
        out.writeShort(getPaperSize());
        out.writeShort(getScale());
        out.writeShort(getPageStart());
        out.writeShort(getFitWidth());
        out.writeShort(getFitHeight());
        out.writeShort(getOptions());
        out.writeShort(getHResolution());
        out.writeShort(getVResolution());
        out.writeDouble(getHeaderMargin());
        out.writeDouble(getFooterMargin());
        out.writeShort(getCopies());
    }

    protected int getDataSize() {
        return 34;
    }

    public short getSid() {
        return (short) 161;
    }

    public Object clone() {
        PrintSetupRecord rec = new PrintSetupRecord();
        rec.field_1_paper_size = this.field_1_paper_size;
        rec.field_2_scale = this.field_2_scale;
        rec.field_3_page_start = this.field_3_page_start;
        rec.field_4_fit_width = this.field_4_fit_width;
        rec.field_5_fit_height = this.field_5_fit_height;
        rec.field_6_options = this.field_6_options;
        rec.field_7_hresolution = this.field_7_hresolution;
        rec.field_8_vresolution = this.field_8_vresolution;
        rec.field_9_headermargin = this.field_9_headermargin;
        rec.field_10_footermargin = this.field_10_footermargin;
        rec.field_11_copies = this.field_11_copies;
        return rec;
    }
}
