package org.apache.poi.ss.formula.ptg;

import org.apache.poi.ss.formula.ExternSheetReferenceToken;
import org.apache.poi.ss.formula.FormulaRenderingWorkbook;
import org.apache.poi.ss.formula.WorkbookDependentFormula;
import org.apache.poi.ss.util.AreaReference;
import org.apache.poi.util.LittleEndianInput;
import org.apache.poi.util.LittleEndianOutput;

public final class Area3DPtg extends AreaPtgBase implements WorkbookDependentFormula, ExternSheetReferenceToken {
    private static final int SIZE = 11;
    public static final byte sid = (byte) 59;
    private int field_1_index_extern_sheet;

    public Area3DPtg(String arearef, int externIdx) {
        super(new AreaReference(arearef));
        setExternSheetIndex(externIdx);
    }

    public Area3DPtg(LittleEndianInput in) {
        this.field_1_index_extern_sheet = in.readShort();
        readCoordinates(in);
    }

    public Area3DPtg(int firstRow, int lastRow, int firstColumn, int lastColumn, boolean firstRowRelative, boolean lastRowRelative, boolean firstColRelative, boolean lastColRelative, int externalSheetIndex) {
        super(firstRow, lastRow, firstColumn, lastColumn, firstRowRelative, lastRowRelative, firstColRelative, lastColRelative);
        setExternSheetIndex(externalSheetIndex);
    }

    public Area3DPtg(AreaReference arearef, int externIdx) {
        super(arearef);
        setExternSheetIndex(externIdx);
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(getClass().getName());
        sb.append(" [");
        sb.append("sheetIx=").append(getExternSheetIndex());
        sb.append(" ! ");
        sb.append(formatReferenceAsString());
        sb.append("]");
        return sb.toString();
    }

    public void write(LittleEndianOutput out) {
        out.writeByte(getPtgClass() + 59);
        out.writeShort(this.field_1_index_extern_sheet);
        writeCoordinates(out);
    }

    public int getSize() {
        return 11;
    }

    public int getExternSheetIndex() {
        return this.field_1_index_extern_sheet;
    }

    public void setExternSheetIndex(int index) {
        this.field_1_index_extern_sheet = index;
    }

    public String format2DRefAsString() {
        return formatReferenceAsString();
    }

    public String toFormulaString(FormulaRenderingWorkbook book) {
        return ExternSheetNameResolver.prependSheetName(book, this.field_1_index_extern_sheet, formatReferenceAsString());
    }

    public String toFormulaString() {
        throw new RuntimeException("3D references need a workbook to determine formula text");
    }
}
