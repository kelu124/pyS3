package org.apache.poi.ss.formula.ptg;

import org.apache.poi.ss.formula.SheetNameFormatter;
import org.apache.poi.util.LittleEndianOutput;

public final class NameXPxg extends OperandPtg implements Pxg {
    private int externalWorkbookNumber;
    private String nameName;
    private String sheetName;

    public NameXPxg(int externalWorkbookNumber, String sheetName, String nameName) {
        this.externalWorkbookNumber = -1;
        this.externalWorkbookNumber = externalWorkbookNumber;
        this.sheetName = sheetName;
        this.nameName = nameName;
    }

    public NameXPxg(String sheetName, String nameName) {
        this(-1, sheetName, nameName);
    }

    public NameXPxg(String nameName) {
        this(-1, null, nameName);
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(getClass().getName());
        sb.append(" [");
        if (this.externalWorkbookNumber >= 0) {
            sb.append(" [");
            sb.append("workbook=").append(getExternalWorkbookNumber());
            sb.append("] ");
        }
        sb.append("sheet=").append(getSheetName());
        sb.append(" ! ");
        sb.append("name=");
        sb.append(this.nameName);
        sb.append("]");
        return sb.toString();
    }

    public int getExternalWorkbookNumber() {
        return this.externalWorkbookNumber;
    }

    public String getSheetName() {
        return this.sheetName;
    }

    public String getNameName() {
        return this.nameName;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    public String toFormulaString() {
        StringBuffer sb = new StringBuffer();
        boolean needsExclamation = false;
        if (this.externalWorkbookNumber >= 0) {
            sb.append('[');
            sb.append(this.externalWorkbookNumber);
            sb.append(']');
            needsExclamation = true;
        }
        if (this.sheetName != null) {
            SheetNameFormatter.appendFormat(sb, this.sheetName);
            needsExclamation = true;
        }
        if (needsExclamation) {
            sb.append('!');
        }
        sb.append(this.nameName);
        return sb.toString();
    }

    public byte getDefaultOperandClass() {
        return (byte) 32;
    }

    public int getSize() {
        return 1;
    }

    public void write(LittleEndianOutput out) {
        throw new IllegalStateException("XSSF-only Ptg, should not be serialised");
    }
}
