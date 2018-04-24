package com.itextpdf.text.pdf;

import com.itextpdf.text.AccessibleElementId;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.log.Logger;
import com.itextpdf.text.log.LoggerFactory;
import com.itextpdf.text.pdf.interfaces.IAccessibleElement;
import java.util.HashMap;

public class PdfPRow implements IAccessibleElement {
    static final /* synthetic */ boolean $assertionsDisabled = (!PdfPRow.class.desiredAssertionStatus());
    public static final float BOTTOM_LIMIT = -1.07374182E9f;
    public static final float RIGHT_LIMIT = 20000.0f;
    private final Logger LOGGER;
    protected HashMap<PdfName, PdfObject> accessibleAttributes;
    protected boolean adjusted;
    protected boolean calculated;
    private int[] canvasesPos;
    protected PdfPCell[] cells;
    protected float[] extraHeights;
    protected AccessibleElementId id;
    protected float maxHeight;
    public boolean mayNotBreak;
    protected PdfName role;
    protected float[] widths;

    public PdfPRow(PdfPCell[] cells) {
        this(cells, null);
    }

    public PdfPRow(PdfPCell[] cells, PdfPRow source) {
        this.LOGGER = LoggerFactory.getLogger(PdfPRow.class);
        this.mayNotBreak = false;
        this.maxHeight = 0.0f;
        this.calculated = false;
        this.adjusted = false;
        this.role = PdfName.TR;
        this.accessibleAttributes = null;
        this.id = new AccessibleElementId();
        this.cells = cells;
        this.widths = new float[cells.length];
        initExtraHeights();
        if (source != null) {
            this.id = source.id;
            this.role = source.role;
            if (source.accessibleAttributes != null) {
                this.accessibleAttributes = new HashMap(source.accessibleAttributes);
            }
        }
    }

    public PdfPRow(PdfPRow row) {
        this.LOGGER = LoggerFactory.getLogger(PdfPRow.class);
        this.mayNotBreak = false;
        this.maxHeight = 0.0f;
        this.calculated = false;
        this.adjusted = false;
        this.role = PdfName.TR;
        this.accessibleAttributes = null;
        this.id = new AccessibleElementId();
        this.mayNotBreak = row.mayNotBreak;
        this.maxHeight = row.maxHeight;
        this.calculated = row.calculated;
        this.cells = new PdfPCell[row.cells.length];
        for (int k = 0; k < this.cells.length; k++) {
            if (row.cells[k] != null) {
                if (row.cells[k] instanceof PdfPHeaderCell) {
                    this.cells[k] = new PdfPHeaderCell((PdfPHeaderCell) row.cells[k]);
                } else {
                    this.cells[k] = new PdfPCell(row.cells[k]);
                }
            }
        }
        this.widths = new float[this.cells.length];
        System.arraycopy(row.widths, 0, this.widths, 0, this.cells.length);
        initExtraHeights();
        this.id = row.id;
        this.role = row.role;
        if (row.accessibleAttributes != null) {
            this.accessibleAttributes = new HashMap(row.accessibleAttributes);
        }
    }

    public boolean setWidths(float[] widths) {
        if (widths.length != this.cells.length) {
            return false;
        }
        System.arraycopy(widths, 0, this.widths, 0, this.cells.length);
        float total = 0.0f;
        this.calculated = false;
        int k = 0;
        while (k < widths.length) {
            PdfPCell cell = this.cells[k];
            if (cell == null) {
                total += widths[k];
            } else {
                cell.setLeft(total);
                int last = k + cell.getColspan();
                while (k < last) {
                    total += widths[k];
                    k++;
                }
                k--;
                cell.setRight(total);
                cell.setTop(0.0f);
            }
            k++;
        }
        return true;
    }

    protected void initExtraHeights() {
        this.extraHeights = new float[this.cells.length];
        for (int i = 0; i < this.extraHeights.length; i++) {
            this.extraHeights[i] = 0.0f;
        }
    }

    public void setExtraHeight(int cell, float height) {
        if (cell >= 0 && cell < this.cells.length) {
            this.extraHeights[cell] = height;
        }
    }

    protected void calculateHeights() {
        this.maxHeight = 0.0f;
        for (PdfPCell cell : this.cells) {
            if (cell != null) {
                float height = cell.getMaxHeight();
                if (height > this.maxHeight && cell.getRowspan() == 1) {
                    this.maxHeight = height;
                }
            }
        }
        this.calculated = true;
    }

    public void setMayNotBreak(boolean mayNotBreak) {
        this.mayNotBreak = mayNotBreak;
    }

    public boolean isMayNotBreak() {
        return this.mayNotBreak;
    }

    public void writeBorderAndBackground(float xPos, float yPos, float currentMaxHeight, PdfPCell cell, PdfContentByte[] canvases) {
        BaseColor background = cell.getBackgroundColor();
        if (background != null || cell.hasBorders()) {
            float right = cell.getRight() + xPos;
            float top = cell.getTop() + yPos;
            float left = cell.getLeft() + xPos;
            float bottom = top - currentMaxHeight;
            if (background != null) {
                PdfContentByte backgr = canvases[1];
                backgr.setColorFill(background);
                backgr.rectangle(left, bottom, right - left, top - bottom);
                backgr.fill();
            }
            if (cell.hasBorders()) {
                Rectangle newRect = new Rectangle(left, bottom, right, top);
                newRect.cloneNonPositionParameters(cell);
                newRect.setBackgroundColor(null);
                canvases[2].rectangle(newRect);
            }
        }
    }

    protected void saveAndRotateCanvases(PdfContentByte[] canvases, float a, float b, float c, float d, float e, float f) {
        if (this.canvasesPos == null) {
            this.canvasesPos = new int[8];
        }
        for (int k = 0; k < 4; k++) {
            ByteBuffer bb = canvases[k].getInternalBuffer();
            this.canvasesPos[k * 2] = bb.size();
            canvases[k].saveState();
            canvases[k].concatCTM(a, b, c, d, e, f);
            this.canvasesPos[(k * 2) + 1] = bb.size();
        }
    }

    protected void restoreCanvases(PdfContentByte[] canvases) {
        for (int k = 0; k < 4; k++) {
            ByteBuffer bb = canvases[k].getInternalBuffer();
            int p1 = bb.size();
            canvases[k].restoreState();
            if (p1 == this.canvasesPos[(k * 2) + 1]) {
                bb.setSize(this.canvasesPos[k * 2]);
            }
        }
    }

    public static float setColumn(ColumnText ct, float left, float bottom, float right, float top) {
        if (left > right) {
            right = left;
        }
        if (bottom > top) {
            top = bottom;
        }
        ct.setSimpleColumn(left, bottom, right, top);
        return top;
    }

    public void writeCells(int colStart, int colEnd, float xPos, float yPos, PdfContentByte[] canvases, boolean reusable) {
        if (!this.calculated) {
            calculateHeights();
        }
        if (colEnd < 0) {
            colEnd = this.cells.length;
        } else {
            colEnd = Math.min(colEnd, this.cells.length);
        }
        if (colStart < 0) {
            colStart = 0;
        }
        if (colStart < colEnd) {
            int newStart = colStart;
            while (newStart >= 0 && this.cells[newStart] == null) {
                if (newStart > 0) {
                    xPos -= this.widths[newStart - 1];
                }
                newStart--;
            }
            if (newStart < 0) {
                newStart = 0;
            }
            if (this.cells[newStart] != null) {
                xPos -= this.cells[newStart].getLeft();
            }
            if (isTagged(canvases[3])) {
                canvases[3].openMCBlock(this);
            }
            for (int k = newStart; k < colEnd; k++) {
                PdfPCell cell = this.cells[k];
                if (cell != null) {
                    if (isTagged(canvases[3])) {
                        canvases[3].openMCBlock(cell);
                    }
                    float currentMaxHeight = this.maxHeight + this.extraHeights[k];
                    writeBorderAndBackground(xPos, yPos, currentMaxHeight, cell, canvases);
                    Image img = cell.getImage();
                    float tly = (cell.getTop() + yPos) - cell.getEffectivePaddingTop();
                    if (cell.getHeight() <= currentMaxHeight) {
                        switch (cell.getVerticalAlignment()) {
                            case 5:
                                tly = ((cell.getTop() + yPos) + ((cell.getHeight() - currentMaxHeight) / BaseField.BORDER_WIDTH_MEDIUM)) - cell.getEffectivePaddingTop();
                                break;
                            case 6:
                                tly = (((cell.getTop() + yPos) - currentMaxHeight) + cell.getHeight()) - cell.getEffectivePaddingTop();
                                break;
                        }
                    }
                    if (img != null) {
                        if (cell.getRotation() != 0) {
                            img = Image.getInstance(img);
                            img.setRotation(img.getImageRotation() + ((float) ((((double) cell.getRotation()) * 3.141592653589793d) / 180.0d)));
                        }
                        boolean vf = false;
                        if (cell.getHeight() > currentMaxHeight) {
                            if (img.isScaleToFitHeight()) {
                                img.scalePercent(100.0f);
                                img.scalePercent(100.0f * (((currentMaxHeight - cell.getEffectivePaddingTop()) - cell.getEffectivePaddingBottom()) / img.getScaledHeight()));
                                vf = true;
                            } else {
                                continue;
                            }
                        }
                        float left = (cell.getLeft() + xPos) + cell.getEffectivePaddingLeft();
                        if (vf) {
                            switch (cell.getHorizontalAlignment()) {
                                case 1:
                                    left = xPos + (((((cell.getLeft() + cell.getEffectivePaddingLeft()) + cell.getRight()) - cell.getEffectivePaddingRight()) - img.getScaledWidth()) / BaseField.BORDER_WIDTH_MEDIUM);
                                    break;
                                case 2:
                                    left = ((cell.getRight() + xPos) - cell.getEffectivePaddingRight()) - img.getScaledWidth();
                                    break;
                            }
                            tly = (cell.getTop() + yPos) - cell.getEffectivePaddingTop();
                        }
                        img.setAbsolutePosition(left, tly - img.getScaledHeight());
                        try {
                            if (isTagged(canvases[3])) {
                                canvases[3].openMCBlock(img);
                            }
                            canvases[3].addImage(img);
                            if (isTagged(canvases[3])) {
                                canvases[3].closeMCBlock(img);
                            }
                        } catch (Exception e) {
                            throw new ExceptionConverter(e);
                        }
                    } else if (cell.getRotation() == 90 || cell.getRotation() == 270) {
                        float netWidth = (currentMaxHeight - cell.getEffectivePaddingTop()) - cell.getEffectivePaddingBottom();
                        float netHeight = (cell.getWidth() - cell.getEffectivePaddingLeft()) - cell.getEffectivePaddingRight();
                        ct = ColumnText.duplicate(cell.getColumn());
                        ct.setCanvases(canvases);
                        ColumnText columnText = ct;
                        columnText.setSimpleColumn(0.0f, 0.0f, 0.001f + netWidth, -netHeight);
                        try {
                            ct.go(true);
                            float calcHeight = -ct.getYLine();
                            if (netWidth <= 0.0f || netHeight <= 0.0f) {
                                calcHeight = 0.0f;
                            }
                            if (calcHeight > 0.0f) {
                                if (cell.isUseDescender()) {
                                    calcHeight -= ct.getDescender();
                                }
                                if (reusable) {
                                    ct = ColumnText.duplicate(cell.getColumn());
                                } else {
                                    ct = cell.getColumn();
                                }
                                ct.setCanvases(canvases);
                                ct.setSimpleColumn(-0.003f, -0.001f, 0.003f + netWidth, calcHeight);
                                float pivotY;
                                float pivotX;
                                if (cell.getRotation() == 90) {
                                    pivotY = ((cell.getTop() + yPos) - currentMaxHeight) + cell.getEffectivePaddingBottom();
                                    switch (cell.getVerticalAlignment()) {
                                        case 5:
                                            pivotX = (cell.getLeft() + xPos) + ((((cell.getWidth() + cell.getEffectivePaddingLeft()) - cell.getEffectivePaddingRight()) + calcHeight) / BaseField.BORDER_WIDTH_MEDIUM);
                                            break;
                                        case 6:
                                            pivotX = ((cell.getLeft() + xPos) + cell.getWidth()) - cell.getEffectivePaddingRight();
                                            break;
                                        default:
                                            pivotX = ((cell.getLeft() + xPos) + cell.getEffectivePaddingLeft()) + calcHeight;
                                            break;
                                    }
                                    saveAndRotateCanvases(canvases, 0.0f, BaseField.BORDER_WIDTH_THIN, -1.0f, 0.0f, pivotX, pivotY);
                                } else {
                                    pivotY = (cell.getTop() + yPos) - cell.getEffectivePaddingTop();
                                    switch (cell.getVerticalAlignment()) {
                                        case 5:
                                            pivotX = (cell.getLeft() + xPos) + ((((cell.getWidth() + cell.getEffectivePaddingLeft()) - cell.getEffectivePaddingRight()) - calcHeight) / BaseField.BORDER_WIDTH_MEDIUM);
                                            break;
                                        case 6:
                                            pivotX = (cell.getLeft() + xPos) + cell.getEffectivePaddingLeft();
                                            break;
                                        default:
                                            pivotX = (((cell.getLeft() + xPos) + cell.getWidth()) - cell.getEffectivePaddingRight()) - calcHeight;
                                            break;
                                    }
                                    saveAndRotateCanvases(canvases, 0.0f, -1.0f, BaseField.BORDER_WIDTH_THIN, 0.0f, pivotX, pivotY);
                                }
                                try {
                                    ct.go();
                                    restoreCanvases(canvases);
                                } catch (Exception e2) {
                                    throw new ExceptionConverter(e2);
                                } catch (Throwable th) {
                                    restoreCanvases(canvases);
                                }
                            }
                        } catch (Exception e22) {
                            throw new ExceptionConverter(e22);
                        }
                    } else {
                        float fixedHeight = cell.getFixedHeight();
                        float rightLimit = (cell.getRight() + xPos) - cell.getEffectivePaddingRight();
                        float leftLimit = (cell.getLeft() + xPos) + cell.getEffectivePaddingLeft();
                        if (cell.isNoWrap()) {
                            switch (cell.getHorizontalAlignment()) {
                                case 1:
                                    rightLimit += 10000.0f;
                                    leftLimit -= 10000.0f;
                                    break;
                                case 2:
                                    if (cell.getRotation() != 180) {
                                        leftLimit -= RIGHT_LIMIT;
                                        break;
                                    } else {
                                        rightLimit += RIGHT_LIMIT;
                                        break;
                                    }
                                default:
                                    if (cell.getRotation() != 180) {
                                        rightLimit += RIGHT_LIMIT;
                                        break;
                                    } else {
                                        leftLimit -= RIGHT_LIMIT;
                                        break;
                                    }
                            }
                        }
                        if (reusable) {
                            ct = ColumnText.duplicate(cell.getColumn());
                        } else {
                            ct = cell.getColumn();
                        }
                        ct.setCanvases(canvases);
                        float bry = tly - ((currentMaxHeight - cell.getEffectivePaddingTop()) - cell.getEffectivePaddingBottom());
                        if (fixedHeight > 0.0f && cell.getHeight() > currentMaxHeight) {
                            tly = (cell.getTop() + yPos) - cell.getEffectivePaddingTop();
                            bry = ((cell.getTop() + yPos) - currentMaxHeight) + cell.getEffectivePaddingBottom();
                        }
                        if ((tly > bry || ct.zeroHeightElement()) && leftLimit < rightLimit) {
                            ct.setSimpleColumn(leftLimit, bry - 0.001f, rightLimit, tly);
                            if (cell.getRotation() == 180) {
                                saveAndRotateCanvases(canvases, -1.0f, 0.0f, 0.0f, -1.0f, leftLimit + rightLimit, (((yPos + yPos) - currentMaxHeight) + cell.getEffectivePaddingBottom()) - cell.getEffectivePaddingTop());
                            }
                            try {
                                ct.go();
                                if (cell.getRotation() == 180) {
                                    restoreCanvases(canvases);
                                }
                            } catch (Exception e222) {
                                throw new ExceptionConverter(e222);
                            } catch (Throwable th2) {
                                if (cell.getRotation() == 180) {
                                    restoreCanvases(canvases);
                                }
                            }
                        }
                    }
                    PdfPCellEvent evt = cell.getCellEvent();
                    if (evt != null) {
                        evt.cellLayout(cell, new Rectangle(cell.getLeft() + xPos, (cell.getTop() + yPos) - currentMaxHeight, cell.getRight() + xPos, cell.getTop() + yPos), canvases);
                    }
                    if (isTagged(canvases[3])) {
                        canvases[3].closeMCBlock(cell);
                    }
                }
            }
            if (isTagged(canvases[3])) {
                canvases[3].closeMCBlock(this);
            }
        }
    }

    public boolean isCalculated() {
        return this.calculated;
    }

    public float getMaxHeights() {
        if (!this.calculated) {
            calculateHeights();
        }
        return this.maxHeight;
    }

    public void setMaxHeights(float maxHeight) {
        this.maxHeight = maxHeight;
    }

    float[] getEventWidth(float xPos, float[] absoluteWidths) {
        int n = 1;
        int k = 0;
        while (k < this.cells.length) {
            if (this.cells[k] != null) {
                n++;
                k += this.cells[k].getColspan();
            } else {
                while (k < this.cells.length && this.cells[k] == null) {
                    n++;
                    k++;
                }
            }
        }
        float[] width = new float[n];
        width[0] = xPos;
        n = 1;
        k = 0;
        while (k < this.cells.length && n < width.length) {
            int k2;
            if (this.cells[k] != null) {
                int colspan = this.cells[k].getColspan();
                width[n] = width[n - 1];
                int i = 0;
                k2 = k;
                while (i < colspan && k2 < absoluteWidths.length) {
                    k = k2 + 1;
                    width[n] = width[n] + absoluteWidths[k2];
                    i++;
                    k2 = k;
                }
                n++;
                k = k2;
            } else {
                width[n] = width[n - 1];
                while (k < this.cells.length && this.cells[k] == null) {
                    k2 = k + 1;
                    width[n] = width[n] + absoluteWidths[k];
                    k = k2;
                }
                n++;
            }
        }
        return width;
    }

    public void copyRowContent(PdfPTable table, int idx) {
        if (table != null) {
            for (int i = 0; i < this.cells.length; i++) {
                int lastRow = idx;
                PdfPCell copy = table.getRow(lastRow).getCells()[i];
                while (copy == null && lastRow > 0) {
                    lastRow--;
                    copy = table.getRow(lastRow).getCells()[i];
                }
                if (!(this.cells[i] == null || copy == null)) {
                    this.cells[i].setColumn(copy.getColumn());
                    this.calculated = false;
                }
            }
        }
    }

    public PdfPRow splitRow(PdfPTable table, int rowIndex, float new_height) {
        this.LOGGER.info("Splitting " + rowIndex + " " + new_height);
        PdfPCell[] newCells = new PdfPCell[this.cells.length];
        float[] fixHs = new float[this.cells.length];
        float[] minHs = new float[this.cells.length];
        boolean allEmpty = true;
        int k = 0;
        while (k < this.cells.length) {
            float newHeight = new_height;
            PdfPCell cell = this.cells[k];
            if (cell == null) {
                int index = rowIndex;
                if (table.rowSpanAbove(index, k)) {
                    while (true) {
                        index--;
                        if (table.rowSpanAbove(index, k)) {
                            newHeight += table.getRow(index).getMaxHeights();
                        } else {
                            PdfPRow row = table.getRow(index);
                            if (!(row == null || row.getCells()[k] == null)) {
                                newCells[k] = new PdfPCell(row.getCells()[k]);
                                newCells[k].setColumn(null);
                                newCells[k].setRowspan((row.getCells()[k].getRowspan() - rowIndex) + index);
                                allEmpty = false;
                            }
                        }
                    }
                }
            } else {
                fixHs[k] = cell.getFixedHeight();
                minHs[k] = cell.getMinimumHeight();
                Image img = cell.getImage();
                PdfPCell newCell = new PdfPCell(cell);
                if (img != null) {
                    float padding = (cell.getEffectivePaddingBottom() + cell.getEffectivePaddingTop()) + BaseField.BORDER_WIDTH_MEDIUM;
                    if ((img.isScaleToFitHeight() || img.getScaledHeight() + padding < newHeight) && newHeight > padding) {
                        newCell.setPhrase(null);
                        allEmpty = false;
                    }
                } else {
                    float y;
                    ColumnText ct = ColumnText.duplicate(cell.getColumn());
                    float left = cell.getLeft() + cell.getEffectivePaddingLeft();
                    float bottom = (cell.getTop() + cell.getEffectivePaddingBottom()) - newHeight;
                    float right = cell.getRight() - cell.getEffectivePaddingRight();
                    float top = cell.getTop() - cell.getEffectivePaddingTop();
                    switch (cell.getRotation()) {
                        case 90:
                        case 270:
                            y = setColumn(ct, bottom, left, top, right);
                            break;
                        default:
                            float f = 1.0E-5f + bottom;
                            if (cell.isNoWrap()) {
                                right = RIGHT_LIMIT;
                            }
                            y = setColumn(ct, left, f, right, top);
                            break;
                    }
                    try {
                        int status = ct.go(true);
                        boolean thisEmpty = ct.getYLine() == y;
                        if (thisEmpty) {
                            newCell.setColumn(ColumnText.duplicate(cell.getColumn()));
                            ct.setFilledWidth(0.0f);
                        } else if ((status & 1) == 0) {
                            newCell.setColumn(ct);
                            ct.setFilledWidth(0.0f);
                        } else {
                            newCell.setPhrase(null);
                        }
                        if (allEmpty && thisEmpty) {
                            allEmpty = true;
                        } else {
                            allEmpty = false;
                        }
                    } catch (DocumentException e) {
                        throw new ExceptionConverter(e);
                    }
                }
                newCells[k] = newCell;
                cell.setFixedHeight(newHeight);
            }
            k++;
        }
        if (allEmpty) {
            for (k = 0; k < this.cells.length; k++) {
                cell = this.cells[k];
                if (cell != null) {
                    if (fixHs[k] > 0.0f) {
                        cell.setFixedHeight(fixHs[k]);
                    } else {
                        cell.setMinimumHeight(minHs[k]);
                    }
                }
            }
            return null;
        }
        calculateHeights();
        PdfPRow pdfPRow = new PdfPRow(newCells, this);
        pdfPRow.widths = (float[]) this.widths.clone();
        return pdfPRow;
    }

    public float getMaxRowHeightsWithoutCalculating() {
        return this.maxHeight;
    }

    public void setFinalMaxHeights(float maxHeight) {
        setMaxHeights(maxHeight);
        this.calculated = true;
    }

    public void splitRowspans(PdfPTable original, int originalIdx, PdfPTable part, int partIdx) {
        if (original != null && part != null) {
            int i = 0;
            while (i < this.cells.length) {
                if (this.cells[i] == null) {
                    int splittedRowIdx = original.getCellStartRowIndex(originalIdx, i);
                    int copyRowIdx = part.getCellStartRowIndex(partIdx, i);
                    PdfPCell splitted = original.getRow(splittedRowIdx).getCells()[i];
                    PdfPCell copy = part.getRow(copyRowIdx).getCells()[i];
                    if (splitted != null) {
                        if ($assertionsDisabled || copy != null) {
                            this.cells[i] = new PdfPCell(copy);
                            int rowspanOnPreviousPage = (partIdx - copyRowIdx) + 1;
                            this.cells[i].setRowspan(copy.getRowspan() - rowspanOnPreviousPage);
                            splitted.setRowspan(rowspanOnPreviousPage);
                            this.calculated = false;
                        } else {
                            throw new AssertionError();
                        }
                    }
                    i++;
                } else {
                    i += this.cells[i].getColspan();
                }
            }
        }
    }

    public PdfPCell[] getCells() {
        return this.cells;
    }

    public boolean hasRowspan() {
        int i = 0;
        while (i < this.cells.length) {
            if (this.cells[i] != null && this.cells[i].getRowspan() > 1) {
                return true;
            }
            i++;
        }
        return false;
    }

    public boolean isAdjusted() {
        return this.adjusted;
    }

    public void setAdjusted(boolean adjusted) {
        this.adjusted = adjusted;
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

    private static boolean isTagged(PdfContentByte canvas) {
        return (canvas == null || canvas.writer == null || !canvas.writer.isTagged()) ? false : true;
    }

    public boolean isInline() {
        return false;
    }
}
