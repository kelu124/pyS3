package com.itextpdf.text.pdf;

import com.itextpdf.awt.geom.AffineTransform;
import com.itextpdf.text.AccessibleElementId;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.ElementListener;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.api.Spaceable;
import com.itextpdf.text.pdf.interfaces.IAccessibleElement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PdfDiv implements Element, Spaceable, IAccessibleElement {
    protected HashMap<PdfName, PdfObject> accessibleAttributes = null;
    private BaseColor backgroundColor = null;
    private Float bottom = null;
    private ArrayList<Element> content = new ArrayList();
    private float contentHeight = 0.0f;
    private float contentWidth = 0.0f;
    private FloatLayout floatLayout = null;
    private FloatType floatType = FloatType.NONE;
    private Float height = null;
    protected AccessibleElementId id = new AccessibleElementId();
    private Float left = null;
    private float paddingBottom = 0.0f;
    private float paddingLeft = 0.0f;
    private float paddingRight = 0.0f;
    private float paddingTop = 0.0f;
    private Float percentageHeight = null;
    private Float percentageWidth = null;
    private PositionType position = PositionType.STATIC;
    private Float right = null;
    protected PdfName role = PdfName.DIV;
    protected float spacingAfter;
    protected float spacingBefore;
    private int textAlignment = -1;
    private Float top = null;
    private Float width = null;
    private float yLine;

    public enum FloatType {
        NONE,
        LEFT,
        RIGHT
    }

    public enum PositionType {
        STATIC,
        ABSOLUTE,
        FIXED,
        RELATIVE
    }

    public float getContentWidth() {
        return this.contentWidth;
    }

    public void setContentWidth(float contentWidth) {
        this.contentWidth = contentWidth;
    }

    public float getContentHeight() {
        return this.contentHeight;
    }

    public void setContentHeight(float contentHeight) {
        this.contentHeight = contentHeight;
    }

    public float getActualHeight() {
        return (this.height == null || this.height.floatValue() < this.contentHeight) ? this.contentHeight : this.height.floatValue();
    }

    public float getActualWidth() {
        return (this.width == null || this.width.floatValue() < this.contentWidth) ? this.contentWidth : this.width.floatValue();
    }

    public Float getPercentageHeight() {
        return this.percentageHeight;
    }

    public void setPercentageHeight(Float percentageHeight) {
        this.percentageHeight = percentageHeight;
    }

    public Float getPercentageWidth() {
        return this.percentageWidth;
    }

    public void setPercentageWidth(Float percentageWidth) {
        this.percentageWidth = percentageWidth;
    }

    public BaseColor getBackgroundColor() {
        return this.backgroundColor;
    }

    public void setBackgroundColor(BaseColor backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public float getYLine() {
        return this.yLine;
    }

    public List<Chunk> getChunks() {
        return new ArrayList();
    }

    public int type() {
        return 37;
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

    public void setSpacingBefore(float spacing) {
        this.spacingBefore = spacing;
    }

    public void setSpacingAfter(float spacing) {
        this.spacingAfter = spacing;
    }

    public float getSpacingBefore() {
        return this.spacingBefore;
    }

    public float getSpacingAfter() {
        return this.spacingAfter;
    }

    public int getTextAlignment() {
        return this.textAlignment;
    }

    public void setTextAlignment(int textAlignment) {
        this.textAlignment = textAlignment;
    }

    public void addElement(Element element) {
        this.content.add(element);
    }

    public Float getLeft() {
        return this.left;
    }

    public void setLeft(Float left) {
        this.left = left;
    }

    public Float getRight() {
        return this.right;
    }

    public void setRight(Float right) {
        this.right = right;
    }

    public Float getTop() {
        return this.top;
    }

    public void setTop(Float top) {
        this.top = top;
    }

    public Float getBottom() {
        return this.bottom;
    }

    public void setBottom(Float bottom) {
        this.bottom = bottom;
    }

    public Float getWidth() {
        return this.width;
    }

    public void setWidth(Float width) {
        this.width = width;
    }

    public Float getHeight() {
        return this.height;
    }

    public void setHeight(Float height) {
        this.height = height;
    }

    public float getPaddingLeft() {
        return this.paddingLeft;
    }

    public void setPaddingLeft(float paddingLeft) {
        this.paddingLeft = paddingLeft;
    }

    public float getPaddingRight() {
        return this.paddingRight;
    }

    public void setPaddingRight(float paddingRight) {
        this.paddingRight = paddingRight;
    }

    public float getPaddingTop() {
        return this.paddingTop;
    }

    public void setPaddingTop(float paddingTop) {
        this.paddingTop = paddingTop;
    }

    public float getPaddingBottom() {
        return this.paddingBottom;
    }

    public void setPaddingBottom(float paddingBottom) {
        this.paddingBottom = paddingBottom;
    }

    public FloatType getFloatType() {
        return this.floatType;
    }

    public void setFloatType(FloatType floatType) {
        this.floatType = floatType;
    }

    public PositionType getPosition() {
        return this.position;
    }

    public void setPosition(PositionType position) {
        this.position = position;
    }

    public ArrayList<Element> getContent() {
        return this.content;
    }

    public void setContent(ArrayList<Element> content) {
        this.content = content;
    }

    public int layout(PdfContentByte canvas, boolean useAscender, boolean simulate, float llx, float lly, float urx, float ury) throws DocumentException {
        float leftX = Math.min(llx, urx);
        float maxY = Math.max(lly, ury);
        float minY = Math.min(lly, ury);
        float rightX = Math.max(llx, urx);
        this.yLine = maxY;
        boolean contentCutByFixedHeight = false;
        if (this.width == null || this.width.floatValue() <= 0.0f) {
            if (this.percentageWidth != null) {
                this.contentWidth = (rightX - leftX) * this.percentageWidth.floatValue();
                rightX = leftX + this.contentWidth;
            }
        } else if (this.width.floatValue() < rightX - leftX) {
            rightX = leftX + this.width.floatValue();
        } else if (this.width.floatValue() > rightX - leftX) {
            return 2;
        }
        if (this.height == null || this.height.floatValue() <= 0.0f) {
            if (this.percentageHeight != null) {
                if (((double) this.percentageHeight.floatValue()) < 1.0d) {
                    contentCutByFixedHeight = true;
                }
                this.contentHeight = (maxY - minY) * this.percentageHeight.floatValue();
                minY = maxY - this.contentHeight;
            }
        } else if (this.height.floatValue() < maxY - minY) {
            minY = maxY - this.height.floatValue();
            contentCutByFixedHeight = true;
        } else if (this.height.floatValue() > maxY - minY) {
            return 2;
        }
        if (!simulate && this.position == PositionType.RELATIVE) {
            Float translationX;
            Float translationY;
            if (this.left != null) {
                translationX = this.left;
            } else if (this.right != null) {
                translationX = Float.valueOf(-this.right.floatValue());
            } else {
                translationX = Float.valueOf(0.0f);
            }
            if (this.top != null) {
                translationY = Float.valueOf(-this.top.floatValue());
            } else if (this.bottom != null) {
                translationY = this.bottom;
            } else {
                translationY = Float.valueOf(0.0f);
            }
            canvas.saveState();
            canvas.transform(new AffineTransform((float) BaseField.BORDER_WIDTH_THIN, 0.0f, 0.0f, (float) BaseField.BORDER_WIDTH_THIN, translationX.floatValue(), translationY.floatValue()));
        }
        if (!simulate && this.backgroundColor != null && getActualWidth() > 0.0f && getActualHeight() > 0.0f) {
            float backgroundWidth = getActualWidth();
            float backgroundHeight = getActualHeight();
            if (this.width != null) {
                backgroundWidth = this.width.floatValue() > 0.0f ? this.width.floatValue() : 0.0f;
            }
            if (this.height != null) {
                backgroundHeight = this.height.floatValue() > 0.0f ? this.height.floatValue() : 0.0f;
            }
            if (backgroundWidth > 0.0f && backgroundHeight > 0.0f) {
                Rectangle background = new Rectangle(leftX, maxY - backgroundHeight, leftX + backgroundWidth, maxY);
                background.setBackgroundColor(this.backgroundColor);
                PdfArtifact artifact = new PdfArtifact();
                canvas.openMCBlock(artifact);
                canvas.rectangle(background);
                canvas.closeMCBlock(artifact);
            }
        }
        if (this.percentageWidth == null) {
            this.contentWidth = 0.0f;
        }
        if (this.percentageHeight == null) {
            this.contentHeight = 0.0f;
        }
        minY += this.paddingBottom;
        leftX += this.paddingLeft;
        rightX -= this.paddingRight;
        this.yLine -= this.paddingTop;
        int status = 1;
        if (!this.content.isEmpty()) {
            if (this.floatLayout == null) {
                this.floatLayout = new FloatLayout(new ArrayList(this.content), useAscender);
            }
            this.floatLayout.setSimpleColumn(leftX, minY, rightX, this.yLine);
            status = this.floatLayout.layout(canvas, simulate);
            this.yLine = this.floatLayout.getYLine();
            if (this.percentageWidth == null && this.contentWidth < this.floatLayout.getFilledWidth()) {
                this.contentWidth = this.floatLayout.getFilledWidth();
            }
        }
        if (!simulate && this.position == PositionType.RELATIVE) {
            canvas.restoreState();
        }
        this.yLine -= this.paddingBottom;
        if (this.percentageHeight == null) {
            this.contentHeight = maxY - this.yLine;
        }
        if (this.percentageWidth == null) {
            this.contentWidth += this.paddingLeft + this.paddingRight;
        }
        return contentCutByFixedHeight ? 1 : status;
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
}
