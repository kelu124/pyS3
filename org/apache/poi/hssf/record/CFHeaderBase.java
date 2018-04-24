package org.apache.poi.hssf.record;

import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.ss.util.CellRangeUtil;
import org.apache.poi.util.LittleEndianOutput;

public abstract class CFHeaderBase extends StandardRecord implements Cloneable {
    private int field_1_numcf;
    private int field_2_need_recalculation_and_id;
    private CellRangeAddress field_3_enclosing_cell_range;
    private CellRangeAddressList field_4_cell_ranges;

    public abstract CFHeaderBase clone();

    protected abstract String getRecordName();

    protected CFHeaderBase() {
    }

    protected CFHeaderBase(CellRangeAddress[] regions, int nRules) {
        setCellRanges(CellRangeUtil.mergeCellRanges(regions));
        this.field_1_numcf = nRules;
    }

    protected void createEmpty() {
        this.field_3_enclosing_cell_range = new CellRangeAddress(0, 0, 0, 0);
        this.field_4_cell_ranges = new CellRangeAddressList();
    }

    protected void read(RecordInputStream in) {
        this.field_1_numcf = in.readShort();
        this.field_2_need_recalculation_and_id = in.readShort();
        this.field_3_enclosing_cell_range = new CellRangeAddress(in);
        this.field_4_cell_ranges = new CellRangeAddressList(in);
    }

    public int getNumberOfConditionalFormats() {
        return this.field_1_numcf;
    }

    public void setNumberOfConditionalFormats(int n) {
        this.field_1_numcf = n;
    }

    public boolean getNeedRecalculation() {
        return (this.field_2_need_recalculation_and_id & 1) == 1;
    }

    public void setNeedRecalculation(boolean b) {
        if (b != getNeedRecalculation()) {
            if (b) {
                this.field_2_need_recalculation_and_id++;
            } else {
                this.field_2_need_recalculation_and_id--;
            }
        }
    }

    public int getID() {
        return this.field_2_need_recalculation_and_id >> 1;
    }

    public void setID(int id) {
        boolean needsRecalc = getNeedRecalculation();
        this.field_2_need_recalculation_and_id = id << 1;
        if (needsRecalc) {
            this.field_2_need_recalculation_and_id++;
        }
    }

    public CellRangeAddress getEnclosingCellRange() {
        return this.field_3_enclosing_cell_range;
    }

    public void setEnclosingCellRange(CellRangeAddress cr) {
        this.field_3_enclosing_cell_range = cr;
    }

    public void setCellRanges(CellRangeAddress[] cellRanges) {
        if (cellRanges == null) {
            throw new IllegalArgumentException("cellRanges must not be null");
        }
        CellRangeAddressList cral = new CellRangeAddressList();
        CellRangeAddress enclosingRange = null;
        for (CellRangeAddress cr : cellRanges) {
            enclosingRange = CellRangeUtil.createEnclosingCellRange(cr, enclosingRange);
            cral.addCellRangeAddress(cr);
        }
        this.field_3_enclosing_cell_range = enclosingRange;
        this.field_4_cell_ranges = cral;
    }

    public CellRangeAddress[] getCellRanges() {
        return this.field_4_cell_ranges.getCellRangeAddresses();
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[").append(getRecordName()).append("]\n");
        buffer.append("\t.numCF             = ").append(getNumberOfConditionalFormats()).append("\n");
        buffer.append("\t.needRecalc        = ").append(getNeedRecalculation()).append("\n");
        buffer.append("\t.id                = ").append(getID()).append("\n");
        buffer.append("\t.enclosingCellRange= ").append(getEnclosingCellRange()).append("\n");
        buffer.append("\t.cfranges=[");
        int i = 0;
        while (i < this.field_4_cell_ranges.countRanges()) {
            buffer.append(i == 0 ? "" : ",").append(this.field_4_cell_ranges.getCellRangeAddress(i).toString());
            i++;
        }
        buffer.append("]\n");
        buffer.append("[/").append(getRecordName()).append("]\n");
        return buffer.toString();
    }

    protected int getDataSize() {
        return this.field_4_cell_ranges.getSize() + 12;
    }

    public void serialize(LittleEndianOutput out) {
        out.writeShort(this.field_1_numcf);
        out.writeShort(this.field_2_need_recalculation_and_id);
        this.field_3_enclosing_cell_range.serialize(out);
        this.field_4_cell_ranges.serialize(out);
    }

    protected void copyTo(CFHeaderBase result) {
        result.field_1_numcf = this.field_1_numcf;
        result.field_2_need_recalculation_and_id = this.field_2_need_recalculation_and_id;
        result.field_3_enclosing_cell_range = this.field_3_enclosing_cell_range.copy();
        result.field_4_cell_ranges = this.field_4_cell_ranges.copy();
    }
}
