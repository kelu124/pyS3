package com.itextpdf.text.pdf.codec.wmf;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.pdf.BaseField;
import com.itextpdf.text.pdf.PdfContentByte;
import java.util.ArrayList;
import java.util.Stack;

public class MetaState {
    public static final int ALTERNATE = 1;
    public static final int OPAQUE = 2;
    public static final int TA_BASELINE = 24;
    public static final int TA_BOTTOM = 8;
    public static final int TA_CENTER = 6;
    public static final int TA_LEFT = 0;
    public static final int TA_NOUPDATECP = 0;
    public static final int TA_RIGHT = 2;
    public static final int TA_TOP = 0;
    public static final int TA_UPDATECP = 1;
    public static final int TRANSPARENT = 1;
    public static final int WINDING = 2;
    public ArrayList<MetaObject> MetaObjects;
    public int backgroundMode;
    public BaseColor currentBackgroundColor;
    public MetaBrush currentBrush;
    public MetaFont currentFont;
    public MetaPen currentPen;
    public Point currentPoint;
    public BaseColor currentTextColor;
    public int extentWx;
    public int extentWy;
    public int lineJoin;
    public int offsetWx;
    public int offsetWy;
    public int polyFillMode;
    public Stack<MetaState> savedStates;
    public float scalingX;
    public float scalingY;
    public int textAlign;

    public MetaState() {
        this.currentBackgroundColor = BaseColor.WHITE;
        this.currentTextColor = BaseColor.BLACK;
        this.backgroundMode = 2;
        this.polyFillMode = 1;
        this.lineJoin = 1;
        this.savedStates = new Stack();
        this.MetaObjects = new ArrayList();
        this.currentPoint = new Point(0, 0);
        this.currentPen = new MetaPen();
        this.currentBrush = new MetaBrush();
        this.currentFont = new MetaFont();
    }

    public MetaState(MetaState state) {
        this.currentBackgroundColor = BaseColor.WHITE;
        this.currentTextColor = BaseColor.BLACK;
        this.backgroundMode = 2;
        this.polyFillMode = 1;
        this.lineJoin = 1;
        setMetaState(state);
    }

    public void setMetaState(MetaState state) {
        this.savedStates = state.savedStates;
        this.MetaObjects = state.MetaObjects;
        this.currentPoint = state.currentPoint;
        this.currentPen = state.currentPen;
        this.currentBrush = state.currentBrush;
        this.currentFont = state.currentFont;
        this.currentBackgroundColor = state.currentBackgroundColor;
        this.currentTextColor = state.currentTextColor;
        this.backgroundMode = state.backgroundMode;
        this.polyFillMode = state.polyFillMode;
        this.textAlign = state.textAlign;
        this.lineJoin = state.lineJoin;
        this.offsetWx = state.offsetWx;
        this.offsetWy = state.offsetWy;
        this.extentWx = state.extentWx;
        this.extentWy = state.extentWy;
        this.scalingX = state.scalingX;
        this.scalingY = state.scalingY;
    }

    public void addMetaObject(MetaObject object) {
        for (int k = 0; k < this.MetaObjects.size(); k++) {
            if (this.MetaObjects.get(k) == null) {
                this.MetaObjects.set(k, object);
                return;
            }
        }
        this.MetaObjects.add(object);
    }

    public void selectMetaObject(int index, PdfContentByte cb) {
        MetaObject obj = (MetaObject) this.MetaObjects.get(index);
        if (obj != null) {
            int style;
            switch (obj.getType()) {
                case 1:
                    this.currentPen = (MetaPen) obj;
                    style = this.currentPen.getStyle();
                    if (style != 5) {
                        cb.setColorStroke(this.currentPen.getColor());
                        cb.setLineWidth(Math.abs((((float) this.currentPen.getPenWidth()) * this.scalingX) / ((float) this.extentWx)));
                        switch (style) {
                            case 1:
                                cb.setLineDash(18.0f, 6.0f, 0.0f);
                                return;
                            case 2:
                                cb.setLineDash((float) BaseField.BORDER_WIDTH_THICK, 0.0f);
                                return;
                            case 3:
                                cb.setLiteral("[9 6 3 6]0 d\n");
                                return;
                            case 4:
                                cb.setLiteral("[9 3 3 3 3 3]0 d\n");
                                return;
                            default:
                                cb.setLineDash(0.0f);
                                return;
                        }
                    }
                    return;
                case 2:
                    this.currentBrush = (MetaBrush) obj;
                    style = this.currentBrush.getStyle();
                    if (style == 0) {
                        cb.setColorFill(this.currentBrush.getColor());
                        return;
                    } else if (style == 2) {
                        cb.setColorFill(this.currentBackgroundColor);
                        return;
                    } else {
                        return;
                    }
                case 3:
                    this.currentFont = (MetaFont) obj;
                    return;
                default:
                    return;
            }
        }
    }

    public void deleteMetaObject(int index) {
        this.MetaObjects.set(index, null);
    }

    public void saveState(PdfContentByte cb) {
        cb.saveState();
        this.savedStates.push(new MetaState(this));
    }

    public void restoreState(int index, PdfContentByte cb) {
        int pops;
        if (index < 0) {
            pops = Math.min(-index, this.savedStates.size());
        } else {
            pops = Math.max(this.savedStates.size() - index, 0);
        }
        if (pops != 0) {
            MetaState state = null;
            int pops2 = pops;
            while (true) {
                pops = pops2 - 1;
                if (pops2 != 0) {
                    cb.restoreState();
                    state = (MetaState) this.savedStates.pop();
                    pops2 = pops;
                } else {
                    setMetaState(state);
                    return;
                }
            }
        }
    }

    public void cleanup(PdfContentByte cb) {
        int k = this.savedStates.size();
        while (true) {
            int k2 = k - 1;
            if (k > 0) {
                cb.restoreState();
                k = k2;
            } else {
                return;
            }
        }
    }

    public float transformX(int x) {
        return ((((float) x) - ((float) this.offsetWx)) * this.scalingX) / ((float) this.extentWx);
    }

    public float transformY(int y) {
        return (BaseField.BORDER_WIDTH_THIN - ((((float) y) - ((float) this.offsetWy)) / ((float) this.extentWy))) * this.scalingY;
    }

    public void setScalingX(float scalingX) {
        this.scalingX = scalingX;
    }

    public void setScalingY(float scalingY) {
        this.scalingY = scalingY;
    }

    public void setOffsetWx(int offsetWx) {
        this.offsetWx = offsetWx;
    }

    public void setOffsetWy(int offsetWy) {
        this.offsetWy = offsetWy;
    }

    public void setExtentWx(int extentWx) {
        this.extentWx = extentWx;
    }

    public void setExtentWy(int extentWy) {
        this.extentWy = extentWy;
    }

    public float transformAngle(float angle) {
        float ta;
        if (this.scalingY < 0.0f) {
            ta = -angle;
        } else {
            ta = angle;
        }
        return (float) (this.scalingX < 0.0f ? 3.141592653589793d - ((double) ta) : (double) ta);
    }

    public void setCurrentPoint(Point p) {
        this.currentPoint = p;
    }

    public Point getCurrentPoint() {
        return this.currentPoint;
    }

    public MetaBrush getCurrentBrush() {
        return this.currentBrush;
    }

    public MetaPen getCurrentPen() {
        return this.currentPen;
    }

    public MetaFont getCurrentFont() {
        return this.currentFont;
    }

    public BaseColor getCurrentBackgroundColor() {
        return this.currentBackgroundColor;
    }

    public void setCurrentBackgroundColor(BaseColor currentBackgroundColor) {
        this.currentBackgroundColor = currentBackgroundColor;
    }

    public BaseColor getCurrentTextColor() {
        return this.currentTextColor;
    }

    public void setCurrentTextColor(BaseColor currentTextColor) {
        this.currentTextColor = currentTextColor;
    }

    public int getBackgroundMode() {
        return this.backgroundMode;
    }

    public void setBackgroundMode(int backgroundMode) {
        this.backgroundMode = backgroundMode;
    }

    public int getTextAlign() {
        return this.textAlign;
    }

    public void setTextAlign(int textAlign) {
        this.textAlign = textAlign;
    }

    public int getPolyFillMode() {
        return this.polyFillMode;
    }

    public void setPolyFillMode(int polyFillMode) {
        this.polyFillMode = polyFillMode;
    }

    public void setLineJoinRectangle(PdfContentByte cb) {
        if (this.lineJoin != 0) {
            this.lineJoin = 0;
            cb.setLineJoin(0);
        }
    }

    public void setLineJoinPolygon(PdfContentByte cb) {
        if (this.lineJoin == 0) {
            this.lineJoin = 1;
            cb.setLineJoin(1);
        }
    }

    public boolean getLineNeutral() {
        return this.lineJoin == 0;
    }
}
