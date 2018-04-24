package com.itextpdf.text.pdf;

import com.itextpdf.text.AccessibleElementId;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.ElementListener;
import com.itextpdf.text.Image;
import com.itextpdf.text.LargeElement;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.api.Spaceable;
import com.itextpdf.text.error_messages.MessageLocalization;
import com.itextpdf.text.log.Logger;
import com.itextpdf.text.log.LoggerFactory;
import com.itextpdf.text.pdf.events.PdfPTableEventForwarder;
import com.itextpdf.text.pdf.interfaces.IAccessibleElement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PdfPTable implements LargeElement, Spaceable, IAccessibleElement {
    static final /* synthetic */ boolean $assertionsDisabled = (!PdfPTable.class.desiredAssertionStatus());
    public static final int BACKGROUNDCANVAS = 1;
    public static final int BASECANVAS = 0;
    public static final int LINECANVAS = 2;
    public static final int TEXTCANVAS = 3;
    private final Logger LOGGER = LoggerFactory.getLogger(PdfPTable.class);
    protected float[] absoluteWidths;
    protected HashMap<PdfName, PdfObject> accessibleAttributes = null;
    private PdfPTableBody body = null;
    protected boolean complete = true;
    protected int currentColIdx = 0;
    protected PdfPCell[] currentRow;
    protected PdfPCell defaultCell = new PdfPCell((Phrase) null);
    private boolean[] extendLastRow = new boolean[]{false, false};
    private PdfPTableFooter footer = null;
    private int footerRows;
    private PdfPTableHeader header = null;
    protected int headerRows;
    private boolean headersInEvent;
    private int horizontalAlignment = 1;
    protected AccessibleElementId id = new AccessibleElementId();
    protected boolean isColspan = false;
    private boolean keepTogether;
    private boolean lockedWidth = false;
    protected boolean loopCheck = true;
    private int numberOfWrittenRows;
    protected float[] relativeWidths;
    protected PdfName role = PdfName.TABLE;
    protected boolean rowCompleted = true;
    protected ArrayList<PdfPRow> rows = new ArrayList();
    protected boolean rowsNotChecked = true;
    protected int runDirection = 0;
    private boolean skipFirstHeader = false;
    private boolean skipLastFooter = false;
    protected float spacingAfter;
    protected float spacingBefore;
    private boolean splitLate = true;
    private boolean splitRows = true;
    private String summary;
    protected PdfPTableEvent tableEvent;
    protected float totalHeight = 0.0f;
    protected float totalWidth = 0.0f;
    protected float widthPercentage = 80.0f;

    public static class ColumnMeasurementState {
        public int colspan = 1;
        public float height = 0.0f;
        public int rowspan = 1;

        public void beginCell(PdfPCell cell, float completedRowsHeight, float rowHeight) {
            this.rowspan = cell.getRowspan();
            this.colspan = cell.getColspan();
            this.height = Math.max(cell.getMaxHeight(), rowHeight) + completedRowsHeight;
        }

        public void consumeRowspan(float completedRowsHeight, float rowHeight) {
            this.rowspan--;
        }

        public boolean cellEnds() {
            return this.rowspan == 1;
        }
    }

    public static class FittingRows {
        public final float completedRowsHeight;
        private final Map<Integer, Float> correctedHeightsForLastRow;
        public final int firstRow;
        public final float height;
        public final int lastRow;

        public FittingRows(int firstRow, int lastRow, float height, float completedRowsHeight, Map<Integer, Float> correctedHeightsForLastRow) {
            this.firstRow = firstRow;
            this.lastRow = lastRow;
            this.height = height;
            this.completedRowsHeight = completedRowsHeight;
            this.correctedHeightsForLastRow = correctedHeightsForLastRow;
        }

        public void correctLastRowChosen(PdfPTable table, int k) {
            PdfPRow row = table.getRow(k);
            Float value = (Float) this.correctedHeightsForLastRow.get(Integer.valueOf(k));
            if (value != null) {
                row.setFinalMaxHeights(value.floatValue());
            }
        }
    }

    protected PdfPTable() {
    }

    public PdfPTable(float[] relativeWidths) {
        if (relativeWidths == null) {
            throw new NullPointerException(MessageLocalization.getComposedMessage("the.widths.array.in.pdfptable.constructor.can.not.be.null", new Object[0]));
        } else if (relativeWidths.length == 0) {
            throw new IllegalArgumentException(MessageLocalization.getComposedMessage("the.widths.array.in.pdfptable.constructor.can.not.have.zero.length", new Object[0]));
        } else {
            this.relativeWidths = new float[relativeWidths.length];
            System.arraycopy(relativeWidths, 0, this.relativeWidths, 0, relativeWidths.length);
            this.absoluteWidths = new float[relativeWidths.length];
            calculateWidths();
            this.currentRow = new PdfPCell[this.absoluteWidths.length];
            this.keepTogether = false;
        }
    }

    public PdfPTable(int numColumns) {
        if (numColumns <= 0) {
            throw new IllegalArgumentException(MessageLocalization.getComposedMessage("the.number.of.columns.in.pdfptable.constructor.must.be.greater.than.zero", new Object[0]));
        }
        this.relativeWidths = new float[numColumns];
        for (int k = 0; k < numColumns; k++) {
            this.relativeWidths[k] = BaseField.BORDER_WIDTH_THIN;
        }
        this.absoluteWidths = new float[this.relativeWidths.length];
        calculateWidths();
        this.currentRow = new PdfPCell[this.absoluteWidths.length];
        this.keepTogether = false;
    }

    public PdfPTable(PdfPTable table) {
        copyFormat(table);
        int k = 0;
        while (k < this.currentRow.length && table.currentRow[k] != null) {
            this.currentRow[k] = new PdfPCell(table.currentRow[k]);
            k++;
        }
        for (k = 0; k < table.rows.size(); k++) {
            PdfPRow row = (PdfPRow) table.rows.get(k);
            if (row != null) {
                row = new PdfPRow(row);
            }
            this.rows.add(row);
        }
    }

    public static PdfPTable shallowCopy(PdfPTable table) {
        PdfPTable nt = new PdfPTable();
        nt.copyFormat(table);
        return nt;
    }

    protected void copyFormat(PdfPTable sourceTable) {
        this.rowsNotChecked = sourceTable.rowsNotChecked;
        this.relativeWidths = new float[sourceTable.getNumberOfColumns()];
        this.absoluteWidths = new float[sourceTable.getNumberOfColumns()];
        System.arraycopy(sourceTable.relativeWidths, 0, this.relativeWidths, 0, getNumberOfColumns());
        System.arraycopy(sourceTable.absoluteWidths, 0, this.absoluteWidths, 0, getNumberOfColumns());
        this.totalWidth = sourceTable.totalWidth;
        this.totalHeight = sourceTable.totalHeight;
        this.currentColIdx = 0;
        this.tableEvent = sourceTable.tableEvent;
        this.runDirection = sourceTable.runDirection;
        if (sourceTable.defaultCell instanceof PdfPHeaderCell) {
            this.defaultCell = new PdfPHeaderCell((PdfPHeaderCell) sourceTable.defaultCell);
        } else {
            this.defaultCell = new PdfPCell(sourceTable.defaultCell);
        }
        this.currentRow = new PdfPCell[sourceTable.currentRow.length];
        this.isColspan = sourceTable.isColspan;
        this.splitRows = sourceTable.splitRows;
        this.spacingAfter = sourceTable.spacingAfter;
        this.spacingBefore = sourceTable.spacingBefore;
        this.headerRows = sourceTable.headerRows;
        this.footerRows = sourceTable.footerRows;
        this.lockedWidth = sourceTable.lockedWidth;
        this.extendLastRow = sourceTable.extendLastRow;
        this.headersInEvent = sourceTable.headersInEvent;
        this.widthPercentage = sourceTable.widthPercentage;
        this.splitLate = sourceTable.splitLate;
        this.skipFirstHeader = sourceTable.skipFirstHeader;
        this.skipLastFooter = sourceTable.skipLastFooter;
        this.horizontalAlignment = sourceTable.horizontalAlignment;
        this.keepTogether = sourceTable.keepTogether;
        this.complete = sourceTable.complete;
        this.loopCheck = sourceTable.loopCheck;
        this.id = sourceTable.id;
        this.role = sourceTable.role;
        if (sourceTable.accessibleAttributes != null) {
            this.accessibleAttributes = new HashMap(sourceTable.accessibleAttributes);
        }
        this.header = sourceTable.getHeader();
        this.body = sourceTable.getBody();
        this.footer = sourceTable.getFooter();
    }

    public void setWidths(float[] relativeWidths) throws DocumentException {
        if (relativeWidths.length != getNumberOfColumns()) {
            throw new DocumentException(MessageLocalization.getComposedMessage("wrong.number.of.columns", new Object[0]));
        }
        this.relativeWidths = new float[relativeWidths.length];
        System.arraycopy(relativeWidths, 0, this.relativeWidths, 0, relativeWidths.length);
        this.absoluteWidths = new float[relativeWidths.length];
        this.totalHeight = 0.0f;
        calculateWidths();
        calculateHeights();
    }

    public void setWidths(int[] relativeWidths) throws DocumentException {
        float[] tb = new float[relativeWidths.length];
        for (int k = 0; k < relativeWidths.length; k++) {
            tb[k] = (float) relativeWidths[k];
        }
        setWidths(tb);
    }

    protected void calculateWidths() {
        if (this.totalWidth > 0.0f) {
            int k;
            float total = 0.0f;
            int numCols = getNumberOfColumns();
            for (k = 0; k < numCols; k++) {
                total += this.relativeWidths[k];
            }
            for (k = 0; k < numCols; k++) {
                this.absoluteWidths[k] = (this.totalWidth * this.relativeWidths[k]) / total;
            }
        }
    }

    public void setTotalWidth(float totalWidth) {
        if (this.totalWidth != totalWidth) {
            this.totalWidth = totalWidth;
            this.totalHeight = 0.0f;
            calculateWidths();
            calculateHeights();
        }
    }

    public void setTotalWidth(float[] columnWidth) throws DocumentException {
        if (columnWidth.length != getNumberOfColumns()) {
            throw new DocumentException(MessageLocalization.getComposedMessage("wrong.number.of.columns", new Object[0]));
        }
        this.totalWidth = 0.0f;
        for (float f : columnWidth) {
            this.totalWidth += f;
        }
        setWidths(columnWidth);
    }

    public void setWidthPercentage(float[] columnWidth, Rectangle pageSize) throws DocumentException {
        if (columnWidth.length != getNumberOfColumns()) {
            throw new IllegalArgumentException(MessageLocalization.getComposedMessage("wrong.number.of.columns", new Object[0]));
        }
        float totalWidth = 0.0f;
        for (float f : columnWidth) {
            totalWidth += f;
        }
        this.widthPercentage = (totalWidth / (pageSize.getRight() - pageSize.getLeft())) * 100.0f;
        setWidths(columnWidth);
    }

    public float getTotalWidth() {
        return this.totalWidth;
    }

    public float calculateHeights() {
        if (this.totalWidth <= 0.0f) {
            return 0.0f;
        }
        this.totalHeight = 0.0f;
        for (int k = 0; k < this.rows.size(); k++) {
            this.totalHeight += getRowHeight(k, true);
        }
        return this.totalHeight;
    }

    public void resetColumnCount(int newColCount) {
        if (newColCount <= 0) {
            throw new IllegalArgumentException(MessageLocalization.getComposedMessage("the.number.of.columns.in.pdfptable.constructor.must.be.greater.than.zero", new Object[0]));
        }
        this.relativeWidths = new float[newColCount];
        for (int k = 0; k < newColCount; k++) {
            this.relativeWidths[k] = BaseField.BORDER_WIDTH_THIN;
        }
        this.absoluteWidths = new float[this.relativeWidths.length];
        calculateWidths();
        this.currentRow = new PdfPCell[this.absoluteWidths.length];
        this.totalHeight = 0.0f;
    }

    public PdfPCell getDefaultCell() {
        return this.defaultCell;
    }

    public PdfPCell addCell(PdfPCell cell) {
        PdfPCell ncell;
        this.rowCompleted = false;
        if (cell instanceof PdfPHeaderCell) {
            ncell = new PdfPHeaderCell((PdfPHeaderCell) cell);
        } else {
            ncell = new PdfPCell(cell);
        }
        int colspan = Math.min(Math.max(ncell.getColspan(), 1), this.currentRow.length - this.currentColIdx);
        ncell.setColspan(colspan);
        if (colspan != 1) {
            this.isColspan = true;
        }
        if (ncell.getRunDirection() == 0) {
            ncell.setRunDirection(this.runDirection);
        }
        skipColsWithRowspanAbove();
        boolean cellAdded = false;
        if (this.currentColIdx < this.currentRow.length) {
            this.currentRow[this.currentColIdx] = ncell;
            this.currentColIdx += colspan;
            cellAdded = true;
        }
        skipColsWithRowspanAbove();
        while (this.currentColIdx >= this.currentRow.length) {
            int numCols = getNumberOfColumns();
            if (this.runDirection == 3) {
                PdfPCell[] rtlRow = new PdfPCell[numCols];
                int rev = this.currentRow.length;
                int k = 0;
                while (k < this.currentRow.length) {
                    PdfPCell rcell = this.currentRow[k];
                    int cspan = rcell.getColspan();
                    rev -= cspan;
                    rtlRow[rev] = rcell;
                    k = (k + (cspan - 1)) + 1;
                }
                this.currentRow = rtlRow;
            }
            PdfPRow row = new PdfPRow(this.currentRow);
            if (this.totalWidth > 0.0f) {
                row.setWidths(this.absoluteWidths);
                this.totalHeight += row.getMaxHeights();
            }
            this.rows.add(row);
            this.currentRow = new PdfPCell[numCols];
            this.currentColIdx = 0;
            skipColsWithRowspanAbove();
            this.rowCompleted = true;
        }
        if (!cellAdded) {
            this.currentRow[this.currentColIdx] = ncell;
            this.currentColIdx += colspan;
        }
        return ncell;
    }

    private void skipColsWithRowspanAbove() {
        int direction = 1;
        if (this.runDirection == 3) {
            direction = -1;
        }
        while (rowSpanAbove(this.rows.size(), this.currentColIdx)) {
            this.currentColIdx += direction;
        }
    }

    PdfPCell cellAt(int row, int col) {
        PdfPCell[] cells = ((PdfPRow) this.rows.get(row)).getCells();
        int i = 0;
        while (i < cells.length) {
            if (cells[i] != null && col >= i && col < cells[i].getColspan() + i) {
                return cells[i];
            }
            i++;
        }
        return null;
    }

    boolean rowSpanAbove(int currRow, int currCol) {
        boolean z = true;
        if (currCol >= getNumberOfColumns() || currCol < 0 || currRow < 1) {
            return false;
        }
        int row = currRow - 1;
        if (((PdfPRow) this.rows.get(row)) == null) {
            return false;
        }
        PdfPCell aboveCell = cellAt(row, currCol);
        while (aboveCell == null && row > 0) {
            row--;
            if (((PdfPRow) this.rows.get(row)) == null) {
                return false;
            }
            aboveCell = cellAt(row, currCol);
        }
        int distance = currRow - row;
        if (aboveCell.getRowspan() == 1 && distance > 1) {
            int col = currCol - 1;
            PdfPRow aboveRow = (PdfPRow) this.rows.get(row + 1);
            distance--;
            aboveCell = aboveRow.getCells()[col];
            while (aboveCell == null && col > 0) {
                col--;
                aboveCell = aboveRow.getCells()[col];
            }
        }
        if (aboveCell == null || aboveCell.getRowspan() <= distance) {
            z = false;
        }
        return z;
    }

    public void addCell(String text) {
        addCell(new Phrase(text));
    }

    public void addCell(PdfPTable table) {
        this.defaultCell.setTable(table);
        addCell(this.defaultCell).id = new AccessibleElementId();
        this.defaultCell.setTable(null);
    }

    public void addCell(Image image) {
        this.defaultCell.setImage(image);
        addCell(this.defaultCell).id = new AccessibleElementId();
        this.defaultCell.setImage(null);
    }

    public void addCell(Phrase phrase) {
        this.defaultCell.setPhrase(phrase);
        addCell(this.defaultCell).id = new AccessibleElementId();
        this.defaultCell.setPhrase(null);
    }

    public float writeSelectedRows(int rowStart, int rowEnd, float xPos, float yPos, PdfContentByte[] canvases) {
        return writeSelectedRows(0, -1, rowStart, rowEnd, xPos, yPos, canvases);
    }

    public float writeSelectedRows(int colStart, int colEnd, int rowStart, int rowEnd, float xPos, float yPos, PdfContentByte[] canvases) {
        return writeSelectedRows(colStart, colEnd, rowStart, rowEnd, xPos, yPos, canvases, true);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public float writeSelectedRows(int r22, int r23, int r24, int r25, float r26, float r27, com.itextpdf.text.pdf.PdfContentByte[] r28, boolean r29) {
        /*
        r21 = this;
        r0 = r21;
        r5 = r0.totalWidth;
        r6 = 0;
        r5 = (r5 > r6 ? 1 : (r5 == r6 ? 0 : -1));
        if (r5 > 0) goto L_0x0018;
    L_0x0009:
        r5 = new java.lang.RuntimeException;
        r6 = "the.table.width.must.be.greater.than.zero";
        r7 = 0;
        r7 = new java.lang.Object[r7];
        r6 = com.itextpdf.text.error_messages.MessageLocalization.getComposedMessage(r6, r7);
        r5.<init>(r6);
        throw r5;
    L_0x0018:
        r0 = r21;
        r5 = r0.rows;
        r18 = r5.size();
        if (r24 >= 0) goto L_0x0024;
    L_0x0022:
        r24 = 0;
    L_0x0024:
        if (r25 >= 0) goto L_0x0031;
    L_0x0026:
        r25 = r18;
    L_0x0028:
        r0 = r24;
        r1 = r25;
        if (r0 < r1) goto L_0x003a;
    L_0x002e:
        r19 = r27;
    L_0x0030:
        return r19;
    L_0x0031:
        r0 = r25;
        r1 = r18;
        r25 = java.lang.Math.min(r0, r1);
        goto L_0x0028;
    L_0x003a:
        r17 = r21.getNumberOfColumns();
        if (r22 >= 0) goto L_0x0118;
    L_0x0040:
        r22 = 0;
    L_0x0042:
        if (r23 >= 0) goto L_0x0122;
    L_0x0044:
        r23 = r17;
    L_0x0046:
        r0 = r21;
        r5 = r0.LOGGER;
        r6 = "Writing row %s to %s; column %s to %s";
        r7 = 4;
        r7 = new java.lang.Object[r7];
        r9 = 0;
        r10 = java.lang.Integer.valueOf(r24);
        r7[r9] = r10;
        r9 = 1;
        r10 = java.lang.Integer.valueOf(r25);
        r7[r9] = r10;
        r9 = 2;
        r10 = java.lang.Integer.valueOf(r22);
        r7[r9] = r10;
        r9 = 3;
        r10 = java.lang.Integer.valueOf(r23);
        r7[r9] = r10;
        r6 = java.lang.String.format(r6, r7);
        r5.info(r6);
        r20 = r27;
        r12 = 0;
        r0 = r21;
        r5 = r0.rowsNotChecked;
        if (r5 == 0) goto L_0x0085;
    L_0x007b:
        r5 = 2139095039; // 0x7f7fffff float:3.4028235E38 double:1.056853372E-314;
        r0 = r21;
        r1 = r24;
        r0.getFittingRows(r5, r1);
    L_0x0085:
        r0 = r21;
        r1 = r24;
        r2 = r25;
        r16 = r0.getRows(r1, r2);
        r15 = r24;
        r14 = r16.iterator();
    L_0x0095:
        r5 = r14.hasNext();
        if (r5 == 0) goto L_0x01ec;
    L_0x009b:
        r4 = r14.next();
        r4 = (com.itextpdf.text.pdf.PdfPRow) r4;
        r5 = r21.getHeader();
        r5 = r5.rows;
        if (r5 == 0) goto L_0x012c;
    L_0x00a9:
        r5 = r21.getHeader();
        r5 = r5.rows;
        r5 = r5.contains(r4);
        if (r5 == 0) goto L_0x012c;
    L_0x00b5:
        if (r12 != 0) goto L_0x012c;
    L_0x00b7:
        r5 = r21.getHeader();
        r6 = 3;
        r6 = r28[r6];
        r0 = r21;
        r12 = r0.openTableBlock(r5, r6);
    L_0x00c4:
        if (r4 == 0) goto L_0x00db;
    L_0x00c6:
        r5 = r22;
        r6 = r23;
        r7 = r26;
        r8 = r27;
        r9 = r28;
        r10 = r29;
        r4.writeCells(r5, r6, r7, r8, r9, r10);
        r5 = r4.getMaxHeights();
        r27 = r27 - r5;
    L_0x00db:
        r5 = r21.getHeader();
        r5 = r5.rows;
        if (r5 == 0) goto L_0x0176;
    L_0x00e3:
        r5 = r21.getHeader();
        r5 = r5.rows;
        r5 = r5.contains(r4);
        if (r5 == 0) goto L_0x0176;
    L_0x00ef:
        r5 = r25 + -1;
        if (r15 == r5) goto L_0x0107;
    L_0x00f3:
        r5 = r21.getHeader();
        r5 = r5.rows;
        r6 = r15 + 1;
        r0 = r16;
        r6 = r0.get(r6);
        r5 = r5.contains(r6);
        if (r5 != 0) goto L_0x0176;
    L_0x0107:
        r5 = r21.getHeader();
        r6 = 3;
        r6 = r28[r6];
        r0 = r21;
        r12 = r0.closeTableBlock(r5, r6);
    L_0x0114:
        r15 = r15 + 1;
        goto L_0x0095;
    L_0x0118:
        r0 = r22;
        r1 = r17;
        r22 = java.lang.Math.min(r0, r1);
        goto L_0x0042;
    L_0x0122:
        r0 = r23;
        r1 = r17;
        r23 = java.lang.Math.min(r0, r1);
        goto L_0x0046;
    L_0x012c:
        r5 = r21.getBody();
        r5 = r5.rows;
        if (r5 == 0) goto L_0x0151;
    L_0x0134:
        r5 = r21.getBody();
        r5 = r5.rows;
        r5 = r5.contains(r4);
        if (r5 == 0) goto L_0x0151;
    L_0x0140:
        if (r12 != 0) goto L_0x0151;
    L_0x0142:
        r5 = r21.getBody();
        r6 = 3;
        r6 = r28[r6];
        r0 = r21;
        r12 = r0.openTableBlock(r5, r6);
        goto L_0x00c4;
    L_0x0151:
        r5 = r21.getFooter();
        r5 = r5.rows;
        if (r5 == 0) goto L_0x00c4;
    L_0x0159:
        r5 = r21.getFooter();
        r5 = r5.rows;
        r5 = r5.contains(r4);
        if (r5 == 0) goto L_0x00c4;
    L_0x0165:
        if (r12 != 0) goto L_0x00c4;
    L_0x0167:
        r5 = r21.getFooter();
        r6 = 3;
        r6 = r28[r6];
        r0 = r21;
        r12 = r0.openTableBlock(r5, r6);
        goto L_0x00c4;
    L_0x0176:
        r5 = r21.getBody();
        r5 = r5.rows;
        if (r5 == 0) goto L_0x01b1;
    L_0x017e:
        r5 = r21.getBody();
        r5 = r5.rows;
        r5 = r5.contains(r4);
        if (r5 == 0) goto L_0x01b1;
    L_0x018a:
        r5 = r25 + -1;
        if (r15 == r5) goto L_0x01a2;
    L_0x018e:
        r5 = r21.getBody();
        r5 = r5.rows;
        r6 = r15 + 1;
        r0 = r16;
        r6 = r0.get(r6);
        r5 = r5.contains(r6);
        if (r5 != 0) goto L_0x01b1;
    L_0x01a2:
        r5 = r21.getBody();
        r6 = 3;
        r6 = r28[r6];
        r0 = r21;
        r12 = r0.closeTableBlock(r5, r6);
        goto L_0x0114;
    L_0x01b1:
        r5 = r21.getFooter();
        r5 = r5.rows;
        if (r5 == 0) goto L_0x0114;
    L_0x01b9:
        r5 = r21.getFooter();
        r5 = r5.rows;
        r5 = r5.contains(r4);
        if (r5 == 0) goto L_0x0114;
    L_0x01c5:
        r5 = r25 + -1;
        if (r15 == r5) goto L_0x01dd;
    L_0x01c9:
        r5 = r21.getFooter();
        r5 = r5.rows;
        r6 = r15 + 1;
        r0 = r16;
        r6 = r0.get(r6);
        r5 = r5.contains(r6);
        if (r5 != 0) goto L_0x0114;
    L_0x01dd:
        r5 = r21.getFooter();
        r6 = 3;
        r6 = r28[r6];
        r0 = r21;
        r12 = r0.closeTableBlock(r5, r6);
        goto L_0x0114;
    L_0x01ec:
        r0 = r21;
        r5 = r0.tableEvent;
        if (r5 == 0) goto L_0x024d;
    L_0x01f2:
        if (r22 != 0) goto L_0x024d;
    L_0x01f4:
        r0 = r23;
        r1 = r17;
        if (r0 != r1) goto L_0x024d;
    L_0x01fa:
        r5 = r25 - r24;
        r5 = r5 + 1;
        r8 = new float[r5];
        r5 = 0;
        r8[r5] = r20;
        r15 = r24;
    L_0x0205:
        r0 = r25;
        if (r15 >= r0) goto L_0x0226;
    L_0x0209:
        r0 = r16;
        r4 = r0.get(r15);
        r4 = (com.itextpdf.text.pdf.PdfPRow) r4;
        r13 = 0;
        if (r4 == 0) goto L_0x0218;
    L_0x0214:
        r13 = r4.getMaxHeights();
    L_0x0218:
        r5 = r15 - r24;
        r5 = r5 + 1;
        r6 = r15 - r24;
        r6 = r8[r6];
        r6 = r6 - r13;
        r8[r5] = r6;
        r15 = r15 + 1;
        goto L_0x0205;
    L_0x0226:
        r0 = r21;
        r5 = r0.tableEvent;
        r0 = r21;
        r6 = r0.headersInEvent;
        r0 = r21;
        r1 = r26;
        r2 = r24;
        r3 = r25;
        r7 = r0.getEventWidths(r1, r2, r3, r6);
        r0 = r21;
        r6 = r0.headersInEvent;
        if (r6 == 0) goto L_0x0251;
    L_0x0240:
        r0 = r21;
        r9 = r0.headerRows;
    L_0x0244:
        r6 = r21;
        r10 = r24;
        r11 = r28;
        r5.tableLayout(r6, r7, r8, r9, r10, r11);
    L_0x024d:
        r19 = r27;
        goto L_0x0030;
    L_0x0251:
        r9 = 0;
        goto L_0x0244;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.itextpdf.text.pdf.PdfPTable.writeSelectedRows(int, int, int, int, float, float, com.itextpdf.text.pdf.PdfContentByte[], boolean):float");
    }

    private PdfPTableBody openTableBlock(PdfPTableBody block, PdfContentByte canvas) {
        if (!canvas.writer.getStandardStructElems().contains(block.getRole())) {
            return null;
        }
        canvas.openMCBlock(block);
        return block;
    }

    private PdfPTableBody closeTableBlock(PdfPTableBody block, PdfContentByte canvas) {
        if (canvas.writer.getStandardStructElems().contains(block.getRole())) {
            canvas.closeMCBlock(block);
        }
        return null;
    }

    public float writeSelectedRows(int rowStart, int rowEnd, float xPos, float yPos, PdfContentByte canvas) {
        return writeSelectedRows(0, -1, rowStart, rowEnd, xPos, yPos, canvas);
    }

    public float writeSelectedRows(int colStart, int colEnd, int rowStart, int rowEnd, float xPos, float yPos, PdfContentByte canvas) {
        return writeSelectedRows(colStart, colEnd, rowStart, rowEnd, xPos, yPos, canvas, true);
    }

    public float writeSelectedRows(int colStart, int colEnd, int rowStart, int rowEnd, float xPos, float yPos, PdfContentByte canvas, boolean reusable) {
        int totalCols = getNumberOfColumns();
        if (colStart < 0) {
            colStart = 0;
        } else {
            colStart = Math.min(colStart, totalCols);
        }
        if (colEnd < 0) {
            colEnd = totalCols;
        } else {
            colEnd = Math.min(colEnd, totalCols);
        }
        boolean clip = (colStart == 0 && colEnd == totalCols) ? false : true;
        if (clip) {
            float w = 0.0f;
            for (int k = colStart; k < colEnd; k++) {
                w += this.absoluteWidths[k];
            }
            canvas.saveState();
            float lx = colStart == 0 ? 10000.0f : 0.0f;
            canvas.rectangle(xPos - lx, -10000.0f, (w + lx) + (colEnd == totalCols ? 10000.0f : 0.0f), PdfPRow.RIGHT_LIMIT);
            canvas.clip();
            canvas.newPath();
        }
        PdfContentByte[] canvases = beginWritingRows(canvas);
        float y = writeSelectedRows(colStart, colEnd, rowStart, rowEnd, xPos, yPos, canvases, reusable);
        endWritingRows(canvases);
        if (clip) {
            canvas.restoreState();
        }
        return y;
    }

    public static PdfContentByte[] beginWritingRows(PdfContentByte canvas) {
        return new PdfContentByte[]{canvas, canvas.getDuplicate(), canvas.getDuplicate(), canvas.getDuplicate()};
    }

    public static void endWritingRows(PdfContentByte[] canvases) {
        PdfContentByte canvas = canvases[0];
        PdfArtifact artifact = new PdfArtifact();
        canvas.openMCBlock(artifact);
        canvas.saveState();
        canvas.add(canvases[1]);
        canvas.restoreState();
        canvas.saveState();
        canvas.setLineCap(2);
        canvas.resetRGBColorStroke();
        canvas.add(canvases[2]);
        canvas.restoreState();
        canvas.closeMCBlock(artifact);
        canvas.add(canvases[3]);
    }

    public int size() {
        return this.rows.size();
    }

    public float getTotalHeight() {
        return this.totalHeight;
    }

    public float getRowHeight(int idx) {
        return getRowHeight(idx, false);
    }

    protected float getRowHeight(int idx, boolean firsttime) {
        float f = 0.0f;
        if (this.totalWidth > 0.0f && idx >= 0 && idx < this.rows.size()) {
            PdfPRow row = (PdfPRow) this.rows.get(idx);
            if (row != null) {
                if (firsttime) {
                    row.setWidths(this.absoluteWidths);
                }
                f = row.getMaxHeights();
                for (int i = 0; i < this.relativeWidths.length; i++) {
                    if (rowSpanAbove(idx, i)) {
                        int rs = 1;
                        while (rowSpanAbove(idx - rs, i)) {
                            rs++;
                        }
                        PdfPCell cell = ((PdfPRow) this.rows.get(idx - rs)).getCells()[i];
                        float tmp = 0.0f;
                        if (cell != null && cell.getRowspan() == rs + 1) {
                            tmp = cell.getMaxHeight();
                            while (rs > 0) {
                                tmp -= getRowHeight(idx - rs);
                                rs--;
                            }
                        }
                        if (tmp > f) {
                            f = tmp;
                        }
                    }
                }
                row.setMaxHeights(f);
            }
        }
        return f;
    }

    public float getRowspanHeight(int rowIndex, int cellIndex) {
        float f = 0.0f;
        if (this.totalWidth > 0.0f && rowIndex >= 0 && rowIndex < this.rows.size()) {
            PdfPRow row = (PdfPRow) this.rows.get(rowIndex);
            if (row != null && cellIndex < row.getCells().length) {
                PdfPCell cell = row.getCells()[cellIndex];
                if (cell != null) {
                    f = 0.0f;
                    for (int j = 0; j < cell.getRowspan(); j++) {
                        f += getRowHeight(rowIndex + j);
                    }
                }
            }
        }
        return f;
    }

    public boolean hasRowspan(int rowIdx) {
        if (rowIdx < this.rows.size() && getRow(rowIdx).hasRowspan()) {
            return true;
        }
        PdfPRow previousRow = rowIdx > 0 ? getRow(rowIdx - 1) : null;
        if (previousRow != null && previousRow.hasRowspan()) {
            return true;
        }
        for (int i = 0; i < getNumberOfColumns(); i++) {
            if (rowSpanAbove(rowIdx - 1, i)) {
                return true;
            }
        }
        return false;
    }

    public void normalizeHeadersFooters() {
        if (this.footerRows > this.headerRows) {
            this.footerRows = this.headerRows;
        }
    }

    public float getHeaderHeight() {
        float total = 0.0f;
        int size = Math.min(this.rows.size(), this.headerRows);
        for (int k = 0; k < size; k++) {
            PdfPRow row = (PdfPRow) this.rows.get(k);
            if (row != null) {
                total += row.getMaxHeights();
            }
        }
        return total;
    }

    public float getFooterHeight() {
        float total = 0.0f;
        int start = Math.max(0, this.headerRows - this.footerRows);
        int size = Math.min(this.rows.size(), this.headerRows);
        for (int k = start; k < size; k++) {
            PdfPRow row = (PdfPRow) this.rows.get(k);
            if (row != null) {
                total += row.getMaxHeights();
            }
        }
        return total;
    }

    public boolean deleteRow(int rowNumber) {
        if (rowNumber < 0 || rowNumber >= this.rows.size()) {
            return false;
        }
        if (this.totalWidth > 0.0f) {
            PdfPRow row = (PdfPRow) this.rows.get(rowNumber);
            if (row != null) {
                this.totalHeight -= row.getMaxHeights();
            }
        }
        this.rows.remove(rowNumber);
        if (rowNumber < this.headerRows) {
            this.headerRows--;
            if (rowNumber >= this.headerRows - this.footerRows) {
                this.footerRows--;
            }
        }
        return true;
    }

    public boolean deleteLastRow() {
        return deleteRow(this.rows.size() - 1);
    }

    public void deleteBodyRows() {
        ArrayList<PdfPRow> rows2 = new ArrayList();
        for (int k = 0; k < this.headerRows; k++) {
            rows2.add(this.rows.get(k));
        }
        this.rows = rows2;
        this.totalHeight = 0.0f;
        if (this.totalWidth > 0.0f) {
            this.totalHeight = getHeaderHeight();
        }
    }

    public int getNumberOfColumns() {
        return this.relativeWidths.length;
    }

    public int getHeaderRows() {
        return this.headerRows;
    }

    public void setHeaderRows(int headerRows) {
        if (headerRows < 0) {
            headerRows = 0;
        }
        this.headerRows = headerRows;
    }

    public List<Chunk> getChunks() {
        return new ArrayList();
    }

    public int type() {
        return 23;
    }

    public boolean isContent() {
        return true;
    }

    public boolean isNestable() {
        return true;
    }

    public boolean process(ElementListener listener) {
        try {
            return listener.add(this);
        } catch (DocumentException e) {
            return false;
        }
    }

    public String getSummary() {
        return this.summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public float getWidthPercentage() {
        return this.widthPercentage;
    }

    public void setWidthPercentage(float widthPercentage) {
        this.widthPercentage = widthPercentage;
    }

    public int getHorizontalAlignment() {
        return this.horizontalAlignment;
    }

    public void setHorizontalAlignment(int horizontalAlignment) {
        this.horizontalAlignment = horizontalAlignment;
    }

    public PdfPRow getRow(int idx) {
        return (PdfPRow) this.rows.get(idx);
    }

    public ArrayList<PdfPRow> getRows() {
        return this.rows;
    }

    public int getLastCompletedRowIndex() {
        return this.rows.size() - 1;
    }

    public void setBreakPoints(int... breakPoints) {
        keepRowsTogether(0, this.rows.size());
        for (int row : breakPoints) {
            getRow(row).setMayNotBreak(false);
        }
    }

    public void keepRowsTogether(int[] rows) {
        for (int row : rows) {
            getRow(row).setMayNotBreak(true);
        }
    }

    public void keepRowsTogether(int start, int end) {
        if (start < end) {
            while (start < end) {
                getRow(start).setMayNotBreak(true);
                start++;
            }
        }
    }

    public void keepRowsTogether(int start) {
        keepRowsTogether(start, this.rows.size());
    }

    public ArrayList<PdfPRow> getRows(int start, int end) {
        ArrayList<PdfPRow> list = new ArrayList();
        if (start >= 0 && end <= size()) {
            for (int i = start; i < end; i++) {
                list.add(adjustCellsInRow(i, end));
            }
        }
        return list;
    }

    protected PdfPRow adjustCellsInRow(int start, int end) {
        PdfPRow row = getRow(start);
        if (row.isAdjusted()) {
            return row;
        }
        PdfPRow row2 = new PdfPRow(row);
        PdfPCell[] cells = row2.getCells();
        for (int i = 0; i < cells.length; i++) {
            PdfPCell cell = cells[i];
            if (!(cell == null || cell.getRowspan() == 1)) {
                float extra = 0.0f;
                for (int k = start + 1; k < Math.min(end, cell.getRowspan() + start); k++) {
                    extra += getRow(k).getMaxHeights();
                }
                row2.setExtraHeight(i, extra);
            }
        }
        row2.setAdjusted(true);
        row = row2;
        return row2;
    }

    public void setTableEvent(PdfPTableEvent event) {
        if (event == null) {
            this.tableEvent = null;
        } else if (this.tableEvent == null) {
            this.tableEvent = event;
        } else if (this.tableEvent instanceof PdfPTableEventForwarder) {
            ((PdfPTableEventForwarder) this.tableEvent).addTableEvent(event);
        } else {
            PdfPTableEventForwarder forward = new PdfPTableEventForwarder();
            forward.addTableEvent(this.tableEvent);
            forward.addTableEvent(event);
            this.tableEvent = forward;
        }
    }

    public PdfPTableEvent getTableEvent() {
        return this.tableEvent;
    }

    public float[] getAbsoluteWidths() {
        return this.absoluteWidths;
    }

    float[][] getEventWidths(float xPos, int firstRow, int lastRow, boolean includeHeaders) {
        int i;
        if (includeHeaders) {
            firstRow = Math.max(firstRow, this.headerRows);
            lastRow = Math.max(lastRow, this.headerRows);
        }
        if (includeHeaders) {
            i = this.headerRows;
        } else {
            i = 0;
        }
        float[][] widths = new float[((i + lastRow) - firstRow)][];
        int k;
        if (this.isColspan) {
            PdfPRow row;
            int n;
            int n2 = 0;
            if (includeHeaders) {
                for (k = 0; k < this.headerRows; k++) {
                    row = (PdfPRow) this.rows.get(k);
                    if (row == null) {
                        n2++;
                    } else {
                        n = n2 + 1;
                        widths[n2] = row.getEventWidth(xPos, this.absoluteWidths);
                        n2 = n;
                    }
                }
            }
            n = n2;
            while (firstRow < lastRow) {
                row = (PdfPRow) this.rows.get(firstRow);
                if (row == null) {
                    n2 = n + 1;
                } else {
                    n2 = n + 1;
                    widths[n] = row.getEventWidth(xPos, this.absoluteWidths);
                }
                firstRow++;
                n = n2;
            }
        } else {
            int numCols = getNumberOfColumns();
            float[] width = new float[(numCols + 1)];
            width[0] = xPos;
            for (k = 0; k < numCols; k++) {
                width[k + 1] = width[k] + this.absoluteWidths[k];
            }
            for (k = 0; k < widths.length; k++) {
                widths[k] = width;
            }
        }
        return widths;
    }

    public boolean isSkipFirstHeader() {
        return this.skipFirstHeader;
    }

    public boolean isSkipLastFooter() {
        return this.skipLastFooter;
    }

    public void setSkipFirstHeader(boolean skipFirstHeader) {
        this.skipFirstHeader = skipFirstHeader;
    }

    public void setSkipLastFooter(boolean skipLastFooter) {
        this.skipLastFooter = skipLastFooter;
    }

    public void setRunDirection(int runDirection) {
        switch (runDirection) {
            case 0:
            case 1:
            case 2:
            case 3:
                this.runDirection = runDirection;
                return;
            default:
                throw new RuntimeException(MessageLocalization.getComposedMessage("invalid.run.direction.1", runDirection));
        }
    }

    public int getRunDirection() {
        return this.runDirection;
    }

    public boolean isLockedWidth() {
        return this.lockedWidth;
    }

    public void setLockedWidth(boolean lockedWidth) {
        this.lockedWidth = lockedWidth;
    }

    public boolean isSplitRows() {
        return this.splitRows;
    }

    public void setSplitRows(boolean splitRows) {
        this.splitRows = splitRows;
    }

    public void setSpacingBefore(float spacing) {
        this.spacingBefore = spacing;
    }

    public void setSpacingAfter(float spacing) {
        this.spacingAfter = spacing;
    }

    public float spacingBefore() {
        return this.spacingBefore;
    }

    public float spacingAfter() {
        return this.spacingAfter;
    }

    public boolean isExtendLastRow() {
        return this.extendLastRow[0];
    }

    public void setExtendLastRow(boolean extendLastRows) {
        this.extendLastRow[0] = extendLastRows;
        this.extendLastRow[1] = extendLastRows;
    }

    public void setExtendLastRow(boolean extendLastRows, boolean extendFinalRow) {
        this.extendLastRow[0] = extendLastRows;
        this.extendLastRow[1] = extendFinalRow;
    }

    public boolean isExtendLastRow(boolean newPageFollows) {
        if (newPageFollows) {
            return this.extendLastRow[0];
        }
        return this.extendLastRow[1];
    }

    public boolean isHeadersInEvent() {
        return this.headersInEvent;
    }

    public void setHeadersInEvent(boolean headersInEvent) {
        this.headersInEvent = headersInEvent;
    }

    public boolean isSplitLate() {
        return this.splitLate;
    }

    public void setSplitLate(boolean splitLate) {
        this.splitLate = splitLate;
    }

    public void setKeepTogether(boolean keepTogether) {
        this.keepTogether = keepTogether;
    }

    public boolean getKeepTogether() {
        return this.keepTogether;
    }

    public int getFooterRows() {
        return this.footerRows;
    }

    public void setFooterRows(int footerRows) {
        if (footerRows < 0) {
            footerRows = 0;
        }
        this.footerRows = footerRows;
    }

    public void completeRow() {
        while (!this.rowCompleted) {
            addCell(this.defaultCell);
        }
    }

    public void flushContent() {
        deleteBodyRows();
        if (this.numberOfWrittenRows > 0) {
            setSkipFirstHeader(true);
        }
    }

    void addNumberOfRowsWritten(int numberOfWrittenRows) {
        this.numberOfWrittenRows += numberOfWrittenRows;
    }

    public boolean isComplete() {
        return this.complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    public float getSpacingBefore() {
        return this.spacingBefore;
    }

    public float getSpacingAfter() {
        return this.spacingAfter;
    }

    public boolean isLoopCheck() {
        return this.loopCheck;
    }

    public void setLoopCheck(boolean loopCheck) {
        this.loopCheck = loopCheck;
    }

    public PdfObject getAccessibleAttribute(PdfName key) {
        if (this.accessibleAttributes != null) {
            return (PdfObject) this.accessibleAttributes.get(key);
        }
        return null;
    }

    public void setAccessibleAttribute(PdfName key, PdfObject value) {
        if (this.accessibleAttributes == null) {
            this.accessibleAttributes = new HashMap();
        }
        this.accessibleAttributes.put(key, value);
    }

    public HashMap<PdfName, PdfObject> getAccessibleAttributes() {
        return this.accessibleAttributes;
    }

    public PdfName getRole() {
        return this.role;
    }

    public void setRole(PdfName role) {
        this.role = role;
    }

    public AccessibleElementId getId() {
        return this.id;
    }

    public void setId(AccessibleElementId id) {
        this.id = id;
    }

    public boolean isInline() {
        return false;
    }

    public PdfPTableHeader getHeader() {
        if (this.header == null) {
            this.header = new PdfPTableHeader();
        }
        return this.header;
    }

    public PdfPTableBody getBody() {
        if (this.body == null) {
            this.body = new PdfPTableBody();
        }
        return this.body;
    }

    public PdfPTableFooter getFooter() {
        if (this.footer == null) {
            this.footer = new PdfPTableFooter();
        }
        return this.footer;
    }

    public int getCellStartRowIndex(int rowIdx, int colIdx) {
        int lastRow = rowIdx;
        while (getRow(lastRow).getCells()[colIdx] == null && lastRow > 0) {
            lastRow--;
        }
        return lastRow;
    }

    public FittingRows getFittingRows(float availableHeight, int startIdx) {
        if ($assertionsDisabled || getRow(startIdx).getCells()[0] != null) {
            int i;
            int cols = getNumberOfColumns();
            ColumnMeasurementState[] states = new ColumnMeasurementState[cols];
            for (i = 0; i < cols; i++) {
                states[i] = new ColumnMeasurementState();
            }
            float completedRowsHeight = 0.0f;
            float totalHeight = 0.0f;
            Map<Integer, Float> correctedHeightsForLastRow = new HashMap();
            int k = startIdx;
            while (k < size()) {
                ColumnMeasurementState state;
                float f;
                PdfPRow row = getRow(k);
                float rowHeight = row.getMaxRowHeightsWithoutCalculating();
                float maxCompletedRowsHeight = 0.0f;
                i = 0;
                while (i < cols) {
                    PdfPCell cell = row.getCells()[i];
                    state = states[i];
                    if (cell == null) {
                        state.consumeRowspan(completedRowsHeight, rowHeight);
                    } else {
                        state.beginCell(cell, completedRowsHeight, rowHeight);
                    }
                    if (state.cellEnds() && state.height > maxCompletedRowsHeight) {
                        maxCompletedRowsHeight = state.height;
                    }
                    for (int j = 1; j < state.colspan; j++) {
                        states[i + j].height = state.height;
                    }
                    i += state.colspan;
                }
                float maxTotalHeight = 0.0f;
                for (ColumnMeasurementState state2 : states) {
                    if (state2.height > maxTotalHeight) {
                        maxTotalHeight = state2.height;
                    }
                }
                row.setFinalMaxHeights(maxCompletedRowsHeight - completedRowsHeight);
                if (isSplitLate()) {
                    f = maxTotalHeight;
                } else {
                    f = maxCompletedRowsHeight;
                }
                if (availableHeight - f < 0.0f) {
                    break;
                }
                correctedHeightsForLastRow.put(Integer.valueOf(k), Float.valueOf(maxTotalHeight - completedRowsHeight));
                completedRowsHeight = maxCompletedRowsHeight;
                totalHeight = maxTotalHeight;
                k++;
            }
            this.rowsNotChecked = false;
            return new FittingRows(startIdx, k - 1, totalHeight, completedRowsHeight, correctedHeightsForLastRow);
        }
        throw new AssertionError();
    }
}
