package com.itextpdf.text.pdf;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.api.Spaceable;
import com.itextpdf.text.pdf.PdfDiv.FloatType;
import java.util.ArrayList;
import java.util.List;

public class FloatLayout {
    protected final ColumnText compositeColumn = new ColumnText(null);
    protected final List<Element> content;
    protected float filledWidth;
    protected float floatLeftX;
    protected float floatRightX;
    protected float leftX;
    protected float maxY;
    protected float minY;
    protected float rightX;
    protected final boolean useAscender;
    protected float yLine;

    public float getYLine() {
        return this.yLine;
    }

    public void setYLine(float yLine) {
        this.yLine = yLine;
    }

    public float getFilledWidth() {
        return this.filledWidth;
    }

    public void setFilledWidth(float filledWidth) {
        this.filledWidth = filledWidth;
    }

    public FloatLayout(List<Element> elements, boolean useAscender) {
        this.compositeColumn.setUseAscender(useAscender);
        this.useAscender = useAscender;
        this.content = elements;
    }

    public void setSimpleColumn(float llx, float lly, float urx, float ury) {
        this.leftX = Math.min(llx, urx);
        this.maxY = Math.max(lly, ury);
        this.minY = Math.min(lly, ury);
        this.rightX = Math.max(llx, urx);
        this.floatLeftX = this.leftX;
        this.floatRightX = this.rightX;
        this.yLine = this.maxY;
        this.filledWidth = 0.0f;
    }

    public int layout(PdfContentByte canvas, boolean simulate) throws DocumentException {
        this.compositeColumn.setCanvas(canvas);
        int status = 1;
        ArrayList<Element> floatingElements = new ArrayList();
        List<Element> content = simulate ? new ArrayList(this.content) : this.content;
        while (!content.isEmpty()) {
            if (content.get(0) instanceof PdfDiv) {
                PdfDiv floatingElement = (PdfDiv) content.get(0);
                if (floatingElement.getFloatType() == FloatType.LEFT || floatingElement.getFloatType() == FloatType.RIGHT) {
                    floatingElements.add(floatingElement);
                    content.remove(0);
                } else {
                    if (!floatingElements.isEmpty()) {
                        status = floatingLayout(floatingElements, simulate);
                        if ((status & 1) == 0) {
                            break;
                        }
                    }
                    content.remove(0);
                    status = floatingElement.layout(canvas, this.useAscender, true, this.floatLeftX, this.minY, this.floatRightX, this.yLine);
                    if (!simulate) {
                        canvas.openMCBlock(floatingElement);
                        status = floatingElement.layout(canvas, this.useAscender, simulate, this.floatLeftX, this.minY, this.floatRightX, this.yLine);
                        canvas.closeMCBlock(floatingElement);
                    }
                    if (floatingElement.getActualWidth() > this.filledWidth) {
                        this.filledWidth = floatingElement.getActualWidth();
                    }
                    if ((status & 1) == 0) {
                        content.add(0, floatingElement);
                        this.yLine = floatingElement.getYLine();
                        break;
                    }
                    this.yLine -= floatingElement.getActualHeight();
                }
            } else {
                floatingElements.add(content.get(0));
                content.remove(0);
            }
        }
        if (!((status & 1) == 0 || floatingElements.isEmpty())) {
            status = floatingLayout(floatingElements, simulate);
        }
        content.addAll(0, floatingElements);
        return status;
    }

    private int floatingLayout(List<Element> floatingElements, boolean simulate) throws DocumentException {
        int status = 1;
        float minYLine = this.yLine;
        float leftWidth = 0.0f;
        float rightWidth = 0.0f;
        ColumnText currentCompositeColumn = this.compositeColumn;
        if (simulate) {
            currentCompositeColumn = ColumnText.duplicate(this.compositeColumn);
        }
        while (!floatingElements.isEmpty()) {
            Element nextElement = (Element) floatingElements.get(0);
            floatingElements.remove(0);
            if (nextElement instanceof PdfDiv) {
                PdfDiv floatingElement = (PdfDiv) nextElement;
                status = floatingElement.layout(this.compositeColumn.getCanvas(), this.useAscender, true, this.floatLeftX, this.minY, this.floatRightX, this.yLine);
                if ((status & 1) == 0) {
                    this.yLine = minYLine;
                    this.floatLeftX = this.leftX;
                    this.floatRightX = this.rightX;
                    status = floatingElement.layout(this.compositeColumn.getCanvas(), this.useAscender, true, this.floatLeftX, this.minY, this.floatRightX, this.yLine);
                    if ((status & 1) == 0) {
                        floatingElements.add(0, floatingElement);
                        break;
                    }
                }
                if (floatingElement.getFloatType() == FloatType.LEFT) {
                    status = floatingElement.layout(this.compositeColumn.getCanvas(), this.useAscender, simulate, this.floatLeftX, this.minY, this.floatRightX, this.yLine);
                    this.floatLeftX += floatingElement.getActualWidth();
                    leftWidth += floatingElement.getActualWidth();
                } else if (floatingElement.getFloatType() == FloatType.RIGHT) {
                    status = floatingElement.layout(this.compositeColumn.getCanvas(), this.useAscender, simulate, (this.floatRightX - floatingElement.getActualWidth()) - 0.01f, this.minY, this.floatRightX, this.yLine);
                    this.floatRightX -= floatingElement.getActualWidth();
                    rightWidth += floatingElement.getActualWidth();
                }
                minYLine = Math.min(minYLine, this.yLine - floatingElement.getActualHeight());
            } else if (this.minY > minYLine) {
                status = 2;
                floatingElements.add(0, nextElement);
                if (currentCompositeColumn != null) {
                    currentCompositeColumn.setText(null);
                }
            } else {
                if (nextElement instanceof Spaceable) {
                    this.yLine -= ((Spaceable) nextElement).getSpacingBefore();
                }
                if (!simulate) {
                    currentCompositeColumn.addElement(nextElement);
                } else if (nextElement instanceof PdfPTable) {
                    currentCompositeColumn.addElement(new PdfPTable((PdfPTable) nextElement));
                } else {
                    currentCompositeColumn.addElement(nextElement);
                }
                if (this.yLine > minYLine) {
                    currentCompositeColumn.setSimpleColumn(this.floatLeftX, this.yLine, this.floatRightX, minYLine);
                } else {
                    currentCompositeColumn.setSimpleColumn(this.floatLeftX, this.yLine, this.floatRightX, this.minY);
                }
                currentCompositeColumn.setFilledWidth(0.0f);
                status = currentCompositeColumn.go(simulate);
                if (this.yLine <= minYLine || ((this.floatLeftX <= this.leftX && this.floatRightX >= this.rightX) || (status & 1) != 0)) {
                    if (rightWidth > 0.0f) {
                        rightWidth += currentCompositeColumn.getFilledWidth();
                    } else if (leftWidth > 0.0f) {
                        leftWidth += currentCompositeColumn.getFilledWidth();
                    } else if (currentCompositeColumn.getFilledWidth() > this.filledWidth) {
                        this.filledWidth = currentCompositeColumn.getFilledWidth();
                    }
                    minYLine = Math.min(currentCompositeColumn.getYLine() + currentCompositeColumn.getDescender(), minYLine);
                    this.yLine = currentCompositeColumn.getYLine() + currentCompositeColumn.getDescender();
                } else {
                    this.yLine = minYLine;
                    this.floatLeftX = this.leftX;
                    this.floatRightX = this.rightX;
                    if (leftWidth == 0.0f || rightWidth == 0.0f) {
                        if (leftWidth > this.filledWidth) {
                            this.filledWidth = leftWidth;
                        }
                        if (rightWidth > this.filledWidth) {
                            this.filledWidth = rightWidth;
                        }
                    } else {
                        this.filledWidth = this.rightX - this.leftX;
                    }
                    leftWidth = 0.0f;
                    rightWidth = 0.0f;
                    if (simulate && (nextElement instanceof PdfPTable)) {
                        currentCompositeColumn.addElement(new PdfPTable((PdfPTable) nextElement));
                    }
                    currentCompositeColumn.setSimpleColumn(this.floatLeftX, this.yLine, this.floatRightX, this.minY);
                    status = currentCompositeColumn.go(simulate);
                    minYLine = currentCompositeColumn.getYLine() + currentCompositeColumn.getDescender();
                    this.yLine = minYLine;
                    if (currentCompositeColumn.getFilledWidth() > this.filledWidth) {
                        this.filledWidth = currentCompositeColumn.getFilledWidth();
                    }
                }
                if ((status & 1) != 0) {
                    currentCompositeColumn.setText(null);
                } else if (simulate) {
                    floatingElements.add(0, nextElement);
                    currentCompositeColumn.setText(null);
                } else {
                    floatingElements.addAll(0, currentCompositeColumn.getCompositeElements());
                    currentCompositeColumn.getCompositeElements().clear();
                }
            }
        }
        if (leftWidth == 0.0f || rightWidth == 0.0f) {
            if (leftWidth > this.filledWidth) {
                this.filledWidth = leftWidth;
            }
            if (rightWidth > this.filledWidth) {
                this.filledWidth = rightWidth;
            }
        } else {
            this.filledWidth = this.rightX - this.leftX;
        }
        this.yLine = minYLine;
        this.floatLeftX = this.leftX;
        this.floatRightX = this.rightX;
        return status;
    }
}
