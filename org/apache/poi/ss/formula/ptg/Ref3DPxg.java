package org.apache.poi.ss.formula.ptg;

import org.apache.poi.ss.formula.SheetIdentifier;
import org.apache.poi.ss.formula.SheetNameFormatter;
import org.apache.poi.ss.formula.SheetRangeIdentifier;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.util.LittleEndianOutput;

public final class Ref3DPxg extends RefPtgBase implements Pxg3D {
    private int externalWorkbookNumber;
    private String firstSheetName;
    private String lastSheetName;

    public Ref3DPxg(int externalWorkbookNumber, SheetIdentifier sheetName, String cellref) {
        this(externalWorkbookNumber, sheetName, new CellReference(cellref));
    }

    public Ref3DPxg(int externalWorkbookNumber, SheetIdentifier sheetName, CellReference c) {
        super(c);
        this.externalWorkbookNumber = -1;
        this.externalWorkbookNumber = externalWorkbookNumber;
        this.firstSheetName = sheetName.getSheetIdentifier().getName();
        if (sheetName instanceof SheetRangeIdentifier) {
            this.lastSheetName = ((SheetRangeIdentifier) sheetName).getLastSheetIdentifier().getName();
        } else {
            this.lastSheetName = null;
        }
    }

    public Ref3DPxg(SheetIdentifier sheetName, String cellref) {
        this(sheetName, new CellReference(cellref));
    }

    public Ref3DPxg(SheetIdentifier sheetName, CellReference c) {
        this(-1, sheetName, c);
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
        sb.append("sheet=").append(this.firstSheetName);
        if (this.lastSheetName != null) {
            sb.append(" : ");
            sb.append("sheet=").append(this.lastSheetName);
        }
        sb.append(" ! ");
        sb.append(formatReferenceAsString());
        sb.append("]");
        return sb.toString();
    }

    public int getExternalWorkbookNumber() {
        return this.externalWorkbookNumber;
    }

    public String getSheetName() {
        return this.firstSheetName;
    }

    public String getLastSheetName() {
        return this.lastSheetName;
    }

    public void setSheetName(String sheetName) {
        this.firstSheetName = sheetName;
    }

    public void setLastSheetName(String sheetName) {
        this.lastSheetName = sheetName;
    }

    public String format2DRefAsString() {
        return formatReferenceAsString();
    }

    public String toFormulaString() {
        StringBuffer sb = new StringBuffer();
        if (this.externalWorkbookNumber >= 0) {
            sb.append('[');
            sb.append(this.externalWorkbookNumber);
            sb.append(']');
        }
        if (this.firstSheetName != null) {
            SheetNameFormatter.appendFormat(sb, this.firstSheetName);
        }
        if (this.lastSheetName != null) {
            sb.append(':');
            SheetNameFormatter.appendFormat(sb, this.lastSheetName);
        }
        sb.append('!');
        sb.append(formatReferenceAsString());
        return sb.toString();
    }

    public int getSize() {
        return 1;
    }

    public void write(LittleEndianOutput out) {
        throw new IllegalStateException("XSSF-only Ptg, should not be serialised");
    }
}
