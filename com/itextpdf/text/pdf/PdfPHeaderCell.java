package com.itextpdf.text.pdf;

public class PdfPHeaderCell extends PdfPCell {
    public static final int BOTH = 3;
    public static final int COLUMN = 2;
    public static final int NONE = 0;
    public static final int ROW = 1;
    protected String name;
    protected int scope;

    public PdfPHeaderCell() {
        this.scope = 0;
        this.name = null;
        this.role = PdfName.TH;
    }

    public PdfPHeaderCell(PdfPHeaderCell headerCell) {
        super((PdfPCell) headerCell);
        this.scope = 0;
        this.name = null;
        this.role = headerCell.role;
        this.scope = headerCell.scope;
        this.name = headerCell.getName();
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public PdfName getRole() {
        return this.role;
    }

    public void setRole(PdfName role) {
        this.role = role;
    }

    public void setScope(int scope) {
        this.scope = scope;
    }

    public int getScope() {
        return this.scope;
    }
}
