package com.itextpdf.text.pdf;

import android.support.v4.widget.AutoScrollHelper;
import com.itextpdf.awt.geom.AffineTransform;
import com.itextpdf.awt.geom.Point2D;
import com.itextpdf.awt.geom.Point2D.Float;
import com.itextpdf.text.Annotation;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.Image;
import com.itextpdf.text.ImgJBIG2;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.error_messages.MessageLocalization;
import com.itextpdf.text.exceptions.IllegalPdfSyntaxException;
import com.itextpdf.text.html.HtmlTags;
import com.itextpdf.text.pdf.interfaces.IAccessibleElement;
import com.itextpdf.text.pdf.internal.PdfAnnotationsImp;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

public class PdfContentByte {
    public static final int ALIGN_CENTER = 1;
    public static final int ALIGN_LEFT = 0;
    public static final int ALIGN_RIGHT = 2;
    public static final int LINE_CAP_BUTT = 0;
    public static final int LINE_CAP_PROJECTING_SQUARE = 2;
    public static final int LINE_CAP_ROUND = 1;
    public static final int LINE_JOIN_BEVEL = 2;
    public static final int LINE_JOIN_MITER = 0;
    public static final int LINE_JOIN_ROUND = 1;
    public static final int TEXT_RENDER_MODE_CLIP = 7;
    public static final int TEXT_RENDER_MODE_FILL = 0;
    public static final int TEXT_RENDER_MODE_FILL_CLIP = 4;
    public static final int TEXT_RENDER_MODE_FILL_STROKE = 2;
    public static final int TEXT_RENDER_MODE_FILL_STROKE_CLIP = 6;
    public static final int TEXT_RENDER_MODE_INVISIBLE = 3;
    public static final int TEXT_RENDER_MODE_STROKE = 1;
    public static final int TEXT_RENDER_MODE_STROKE_CLIP = 5;
    private static HashMap<PdfName, String> abrev = new HashMap();
    private static final float[] unitRect = new float[]{0.0f, 0.0f, 0.0f, BaseField.BORDER_WIDTH_THIN, BaseField.BORDER_WIDTH_THIN, 0.0f, BaseField.BORDER_WIDTH_THIN, BaseField.BORDER_WIDTH_THIN};
    protected ByteBuffer content = new ByteBuffer();
    protected PdfContentByte duplicatedFrom = null;
    private boolean inText = false;
    protected ArrayList<Integer> layerDepth;
    protected int markedContentSize = 0;
    private int mcDepth = 0;
    private ArrayList<IAccessibleElement> mcElements = new ArrayList();
    protected PdfDocument pdf;
    protected int separator = 10;
    protected GraphicState state = new GraphicState();
    protected ArrayList<GraphicState> stateList = new ArrayList();
    protected PdfWriter writer;

    public static class GraphicState {
        protected AffineTransform CTM = new AffineTransform();
        protected float aTLM = BaseField.BORDER_WIDTH_THIN;
        protected float bTLM = 0.0f;
        protected float cTLM = 0.0f;
        protected float charSpace = 0.0f;
        ColorDetails colorDetails;
        protected BaseColor colorFill = new GrayColor(0);
        protected BaseColor colorStroke = new GrayColor(0);
        protected float dTLM = BaseField.BORDER_WIDTH_THIN;
        protected PdfObject extGState = null;
        FontDetails fontDetails;
        protected float leading = 0.0f;
        protected float scale = 100.0f;
        float size;
        protected BaseColor textColorFill = new GrayColor(0);
        protected BaseColor textColorStroke = new GrayColor(0);
        protected int textRenderMode = 0;
        protected float tx = 0.0f;
        protected float wordSpace = 0.0f;
        protected float xTLM = 0.0f;
        protected float yTLM = 0.0f;

        GraphicState() {
        }

        GraphicState(GraphicState cp) {
            copyParameters(cp);
        }

        void copyParameters(GraphicState cp) {
            this.fontDetails = cp.fontDetails;
            this.colorDetails = cp.colorDetails;
            this.size = cp.size;
            this.xTLM = cp.xTLM;
            this.yTLM = cp.yTLM;
            this.aTLM = cp.aTLM;
            this.bTLM = cp.bTLM;
            this.cTLM = cp.cTLM;
            this.dTLM = cp.dTLM;
            this.tx = cp.tx;
            this.leading = cp.leading;
            this.scale = cp.scale;
            this.charSpace = cp.charSpace;
            this.wordSpace = cp.wordSpace;
            this.textColorFill = cp.textColorFill;
            this.colorFill = cp.colorFill;
            this.textColorStroke = cp.textColorStroke;
            this.colorStroke = cp.colorStroke;
            this.CTM = new AffineTransform(cp.CTM);
            this.textRenderMode = cp.textRenderMode;
            this.extGState = cp.extGState;
        }

        void restore(GraphicState restore) {
            copyParameters(restore);
        }
    }

    static class UncoloredPattern extends PatternColor {
        protected BaseColor color;
        protected float tint;

        protected UncoloredPattern(PdfPatternPainter p, BaseColor color, float tint) {
            super(p);
            this.color = color;
            this.tint = tint;
        }

        public boolean equals(Object obj) {
            return (obj instanceof UncoloredPattern) && ((UncoloredPattern) obj).painter.equals(this.painter) && ((UncoloredPattern) obj).color.equals(this.color) && ((UncoloredPattern) obj).tint == this.tint;
        }
    }

    static {
        abrev.put(PdfName.BITSPERCOMPONENT, "/BPC ");
        abrev.put(PdfName.COLORSPACE, "/CS ");
        abrev.put(PdfName.DECODE, "/D ");
        abrev.put(PdfName.DECODEPARMS, "/DP ");
        abrev.put(PdfName.FILTER, "/F ");
        abrev.put(PdfName.HEIGHT, "/H ");
        abrev.put(PdfName.IMAGEMASK, "/IM ");
        abrev.put(PdfName.INTENT, "/Intent ");
        abrev.put(PdfName.INTERPOLATE, "/I ");
        abrev.put(PdfName.WIDTH, "/W ");
    }

    public PdfContentByte(PdfWriter wr) {
        if (wr != null) {
            this.writer = wr;
            this.pdf = this.writer.getPdfDocument();
        }
    }

    public String toString() {
        return this.content.toString();
    }

    public boolean isTagged() {
        return this.writer != null && this.writer.isTagged();
    }

    public ByteBuffer getInternalBuffer() {
        return this.content;
    }

    public byte[] toPdf(PdfWriter writer) {
        sanityCheck();
        return this.content.toByteArray();
    }

    public void add(PdfContentByte other) {
        if (other.writer == null || this.writer == other.writer) {
            this.content.append(other.content);
            this.markedContentSize += other.markedContentSize;
            return;
        }
        throw new RuntimeException(MessageLocalization.getComposedMessage("inconsistent.writers.are.you.mixing.two.documents", new Object[0]));
    }

    public float getXTLM() {
        return this.state.xTLM;
    }

    public float getYTLM() {
        return this.state.yTLM;
    }

    public float getLeading() {
        return this.state.leading;
    }

    public float getCharacterSpacing() {
        return this.state.charSpace;
    }

    public float getWordSpacing() {
        return this.state.wordSpace;
    }

    public float getHorizontalScaling() {
        return this.state.scale;
    }

    public void setFlatness(float flatness) {
        if (flatness >= 0.0f && flatness <= 100.0f) {
            this.content.append(flatness).append(" i").append_i(this.separator);
        }
    }

    public void setLineCap(int style) {
        if (style >= 0 && style <= 2) {
            this.content.append(style).append(" J").append_i(this.separator);
        }
    }

    public void setRenderingIntent(PdfName ri) {
        this.content.append(ri.getBytes()).append(" ri").append_i(this.separator);
    }

    public void setLineDash(float phase) {
        this.content.append("[] ").append(phase).append(" d").append_i(this.separator);
    }

    public void setLineDash(float unitsOn, float phase) {
        this.content.append("[").append(unitsOn).append("] ").append(phase).append(" d").append_i(this.separator);
    }

    public void setLineDash(float unitsOn, float unitsOff, float phase) {
        this.content.append("[").append(unitsOn).append(' ').append(unitsOff).append("] ").append(phase).append(" d").append_i(this.separator);
    }

    public final void setLineDash(float[] array, float phase) {
        this.content.append("[");
        for (int i = 0; i < array.length; i++) {
            this.content.append(array[i]);
            if (i < array.length - 1) {
                this.content.append(' ');
            }
        }
        this.content.append("] ").append(phase).append(" d").append_i(this.separator);
    }

    public void setLineJoin(int style) {
        if (style >= 0 && style <= 2) {
            this.content.append(style).append(" j").append_i(this.separator);
        }
    }

    public void setLineWidth(float w) {
        this.content.append(w).append(" w").append_i(this.separator);
    }

    public void setMiterLimit(float miterLimit) {
        if (miterLimit > BaseField.BORDER_WIDTH_THIN) {
            this.content.append(miterLimit).append(" M").append_i(this.separator);
        }
    }

    public void clip() {
        if (this.inText && isTagged()) {
            endText();
        }
        this.content.append("W").append_i(this.separator);
    }

    public void eoClip() {
        if (this.inText && isTagged()) {
            endText();
        }
        this.content.append("W*").append_i(this.separator);
    }

    public void setGrayFill(float gray) {
        saveColor(new GrayColor(gray), true);
        this.content.append(gray).append(" g").append_i(this.separator);
    }

    public void resetGrayFill() {
        saveColor(new GrayColor(0), true);
        this.content.append("0 g").append_i(this.separator);
    }

    public void setGrayStroke(float gray) {
        saveColor(new GrayColor(gray), false);
        this.content.append(gray).append(" G").append_i(this.separator);
    }

    public void resetGrayStroke() {
        saveColor(new GrayColor(0), false);
        this.content.append("0 G").append_i(this.separator);
    }

    private void HelperRGB(float red, float green, float blue) {
        if (red < 0.0f) {
            red = 0.0f;
        } else if (red > BaseField.BORDER_WIDTH_THIN) {
            red = BaseField.BORDER_WIDTH_THIN;
        }
        if (green < 0.0f) {
            green = 0.0f;
        } else if (green > BaseField.BORDER_WIDTH_THIN) {
            green = BaseField.BORDER_WIDTH_THIN;
        }
        if (blue < 0.0f) {
            blue = 0.0f;
        } else if (blue > BaseField.BORDER_WIDTH_THIN) {
            blue = BaseField.BORDER_WIDTH_THIN;
        }
        this.content.append(red).append(' ').append(green).append(' ').append(blue);
    }

    public void setRGBColorFillF(float red, float green, float blue) {
        saveColor(new BaseColor(red, green, blue), true);
        HelperRGB(red, green, blue);
        this.content.append(" rg").append_i(this.separator);
    }

    public void resetRGBColorFill() {
        resetGrayFill();
    }

    public void setRGBColorStrokeF(float red, float green, float blue) {
        saveColor(new BaseColor(red, green, blue), false);
        HelperRGB(red, green, blue);
        this.content.append(" RG").append_i(this.separator);
    }

    public void resetRGBColorStroke() {
        resetGrayStroke();
    }

    private void HelperCMYK(float cyan, float magenta, float yellow, float black) {
        if (cyan < 0.0f) {
            cyan = 0.0f;
        } else if (cyan > BaseField.BORDER_WIDTH_THIN) {
            cyan = BaseField.BORDER_WIDTH_THIN;
        }
        if (magenta < 0.0f) {
            magenta = 0.0f;
        } else if (magenta > BaseField.BORDER_WIDTH_THIN) {
            magenta = BaseField.BORDER_WIDTH_THIN;
        }
        if (yellow < 0.0f) {
            yellow = 0.0f;
        } else if (yellow > BaseField.BORDER_WIDTH_THIN) {
            yellow = BaseField.BORDER_WIDTH_THIN;
        }
        if (black < 0.0f) {
            black = 0.0f;
        } else if (black > BaseField.BORDER_WIDTH_THIN) {
            black = BaseField.BORDER_WIDTH_THIN;
        }
        this.content.append(cyan).append(' ').append(magenta).append(' ').append(yellow).append(' ').append(black);
    }

    public void setCMYKColorFillF(float cyan, float magenta, float yellow, float black) {
        saveColor(new CMYKColor(cyan, magenta, yellow, black), true);
        HelperCMYK(cyan, magenta, yellow, black);
        this.content.append(" k").append_i(this.separator);
    }

    public void resetCMYKColorFill() {
        saveColor(new CMYKColor(0, 0, 0, 1), true);
        this.content.append("0 0 0 1 k").append_i(this.separator);
    }

    public void setCMYKColorStrokeF(float cyan, float magenta, float yellow, float black) {
        saveColor(new CMYKColor(cyan, magenta, yellow, black), false);
        HelperCMYK(cyan, magenta, yellow, black);
        this.content.append(" K").append_i(this.separator);
    }

    public void resetCMYKColorStroke() {
        saveColor(new CMYKColor(0, 0, 0, 1), false);
        this.content.append("0 0 0 1 K").append_i(this.separator);
    }

    public void moveTo(float x, float y) {
        if (this.inText) {
            if (isTagged()) {
                endText();
            } else {
                throw new IllegalPdfSyntaxException(MessageLocalization.getComposedMessage("path.construction.operator.inside.text.object", new Object[0]));
            }
        }
        this.content.append(x).append(' ').append(y).append(" m").append_i(this.separator);
    }

    public void lineTo(float x, float y) {
        if (this.inText) {
            if (isTagged()) {
                endText();
            } else {
                throw new IllegalPdfSyntaxException(MessageLocalization.getComposedMessage("path.construction.operator.inside.text.object", new Object[0]));
            }
        }
        this.content.append(x).append(' ').append(y).append(" l").append_i(this.separator);
    }

    public void curveTo(float x1, float y1, float x2, float y2, float x3, float y3) {
        if (this.inText) {
            if (isTagged()) {
                endText();
            } else {
                throw new IllegalPdfSyntaxException(MessageLocalization.getComposedMessage("path.construction.operator.inside.text.object", new Object[0]));
            }
        }
        this.content.append(x1).append(' ').append(y1).append(' ').append(x2).append(' ').append(y2).append(' ').append(x3).append(' ').append(y3).append(" c").append_i(this.separator);
    }

    public void curveTo(float x2, float y2, float x3, float y3) {
        if (this.inText) {
            if (isTagged()) {
                endText();
            } else {
                throw new IllegalPdfSyntaxException(MessageLocalization.getComposedMessage("path.construction.operator.inside.text.object", new Object[0]));
            }
        }
        this.content.append(x2).append(' ').append(y2).append(' ').append(x3).append(' ').append(y3).append(" v").append_i(this.separator);
    }

    public void curveFromTo(float x1, float y1, float x3, float y3) {
        if (this.inText) {
            if (isTagged()) {
                endText();
            } else {
                throw new IllegalPdfSyntaxException(MessageLocalization.getComposedMessage("path.construction.operator.inside.text.object", new Object[0]));
            }
        }
        this.content.append(x1).append(' ').append(y1).append(' ').append(x3).append(' ').append(y3).append(" y").append_i(this.separator);
    }

    public void circle(float x, float y, float r) {
        moveTo(x + r, y);
        curveTo(x + r, y + (r * 0.5523f), x + (r * 0.5523f), y + r, x, y + r);
        curveTo(x - (r * 0.5523f), y + r, x - r, y + (r * 0.5523f), x - r, y);
        curveTo(x - r, y - (r * 0.5523f), x - (r * 0.5523f), y - r, x, y - r);
        curveTo(x + (r * 0.5523f), y - r, x + r, y - (r * 0.5523f), x + r, y);
    }

    public void rectangle(float x, float y, float w, float h) {
        if (this.inText) {
            if (isTagged()) {
                endText();
            } else {
                throw new IllegalPdfSyntaxException(MessageLocalization.getComposedMessage("path.construction.operator.inside.text.object", new Object[0]));
            }
        }
        this.content.append(x).append(' ').append(y).append(' ').append(w).append(' ').append(h).append(" re").append_i(this.separator);
    }

    private boolean compareColors(BaseColor c1, BaseColor c2) {
        if (c1 == null && c2 == null) {
            return true;
        }
        if (c1 == null || c2 == null) {
            return false;
        }
        if (c1 instanceof ExtendedColor) {
            return c1.equals(c2);
        }
        return c2.equals(c1);
    }

    public void variableRectangle(Rectangle rect) {
        boolean bt;
        boolean bb;
        float t = rect.getTop();
        float b = rect.getBottom();
        float r = rect.getRight();
        float l = rect.getLeft();
        float wt = rect.getBorderWidthTop();
        float wb = rect.getBorderWidthBottom();
        float wr = rect.getBorderWidthRight();
        float wl = rect.getBorderWidthLeft();
        BaseColor ct = rect.getBorderColorTop();
        BaseColor cb = rect.getBorderColorBottom();
        BaseColor cr = rect.getBorderColorRight();
        BaseColor cl = rect.getBorderColorLeft();
        saveState();
        setLineCap(0);
        setLineJoin(0);
        float clw = 0.0f;
        boolean cdef = false;
        BaseColor ccol = null;
        boolean cdefi = false;
        BaseColor cfil = null;
        if (wt > 0.0f) {
            clw = wt;
            setLineWidth(wt);
            cdef = true;
            if (ct == null) {
                resetRGBColorStroke();
            } else {
                setColorStroke(ct);
            }
            ccol = ct;
            moveTo(l, t - (wt / BaseField.BORDER_WIDTH_MEDIUM));
            lineTo(r, t - (wt / BaseField.BORDER_WIDTH_MEDIUM));
            stroke();
        }
        if (wb > 0.0f) {
            if (wb != clw) {
                clw = wb;
                setLineWidth(wb);
            }
            if (!(cdef && compareColors(ccol, cb))) {
                cdef = true;
                if (cb == null) {
                    resetRGBColorStroke();
                } else {
                    setColorStroke(cb);
                }
                ccol = cb;
            }
            moveTo(r, (wb / BaseField.BORDER_WIDTH_MEDIUM) + b);
            lineTo(l, (wb / BaseField.BORDER_WIDTH_MEDIUM) + b);
            stroke();
        }
        if (wr > 0.0f) {
            if (wr != clw) {
                clw = wr;
                setLineWidth(wr);
            }
            if (!(cdef && compareColors(ccol, cr))) {
                cdef = true;
                if (cr == null) {
                    resetRGBColorStroke();
                } else {
                    setColorStroke(cr);
                }
                ccol = cr;
            }
            bt = compareColors(ct, cr);
            bb = compareColors(cb, cr);
            moveTo(r - (wr / BaseField.BORDER_WIDTH_MEDIUM), bt ? t : t - wt);
            lineTo(r - (wr / BaseField.BORDER_WIDTH_MEDIUM), bb ? b : b + wb);
            stroke();
            if (!(bt && bb)) {
                cdefi = true;
                if (cr == null) {
                    resetRGBColorFill();
                } else {
                    setColorFill(cr);
                }
                cfil = cr;
                if (!bt) {
                    moveTo(r, t);
                    lineTo(r, t - wt);
                    lineTo(r - wr, t - wt);
                    fill();
                }
                if (!bb) {
                    moveTo(r, b);
                    lineTo(r, b + wb);
                    lineTo(r - wr, b + wb);
                    fill();
                }
            }
        }
        if (wl > 0.0f) {
            if (wl != clw) {
                setLineWidth(wl);
            }
            if (!(cdef && compareColors(ccol, cl))) {
                if (cl == null) {
                    resetRGBColorStroke();
                } else {
                    setColorStroke(cl);
                }
            }
            bt = compareColors(ct, cl);
            bb = compareColors(cb, cl);
            moveTo(l + (wl / BaseField.BORDER_WIDTH_MEDIUM), bt ? t : t - wt);
            lineTo(l + (wl / BaseField.BORDER_WIDTH_MEDIUM), bb ? b : b + wb);
            stroke();
            if (!(bt && bb)) {
                if (!(cdefi && compareColors(cfil, cl))) {
                    if (cl == null) {
                        resetRGBColorFill();
                    } else {
                        setColorFill(cl);
                    }
                }
                if (!bt) {
                    moveTo(l, t);
                    lineTo(l, t - wt);
                    lineTo(l + wl, t - wt);
                    fill();
                }
                if (!bb) {
                    moveTo(l, b);
                    lineTo(l, b + wb);
                    lineTo(l + wl, b + wb);
                    fill();
                }
            }
        }
        restoreState();
    }

    public void rectangle(Rectangle rectangle) {
        float x1 = rectangle.getLeft();
        float y1 = rectangle.getBottom();
        float x2 = rectangle.getRight();
        float y2 = rectangle.getTop();
        BaseColor background = rectangle.getBackgroundColor();
        if (background != null) {
            saveState();
            setColorFill(background);
            rectangle(x1, y1, x2 - x1, y2 - y1);
            fill();
            restoreState();
        }
        if (!rectangle.hasBorders()) {
            return;
        }
        if (rectangle.isUseVariableBorders()) {
            variableRectangle(rectangle);
            return;
        }
        if (rectangle.getBorderWidth() != -1.0f) {
            setLineWidth(rectangle.getBorderWidth());
        }
        BaseColor color = rectangle.getBorderColor();
        if (color != null) {
            setColorStroke(color);
        }
        if (rectangle.hasBorder(15)) {
            rectangle(x1, y1, x2 - x1, y2 - y1);
        } else {
            if (rectangle.hasBorder(8)) {
                moveTo(x2, y1);
                lineTo(x2, y2);
            }
            if (rectangle.hasBorder(4)) {
                moveTo(x1, y1);
                lineTo(x1, y2);
            }
            if (rectangle.hasBorder(2)) {
                moveTo(x1, y1);
                lineTo(x2, y1);
            }
            if (rectangle.hasBorder(1)) {
                moveTo(x1, y2);
                lineTo(x2, y2);
            }
        }
        stroke();
        if (color != null) {
            resetRGBColorStroke();
        }
    }

    public void closePath() {
        if (this.inText) {
            if (isTagged()) {
                endText();
            } else {
                throw new IllegalPdfSyntaxException(MessageLocalization.getComposedMessage("path.construction.operator.inside.text.object", new Object[0]));
            }
        }
        this.content.append("h").append_i(this.separator);
    }

    public void newPath() {
        if (this.inText) {
            if (isTagged()) {
                endText();
            } else {
                throw new IllegalPdfSyntaxException(MessageLocalization.getComposedMessage("path.construction.operator.inside.text.object", new Object[0]));
            }
        }
        this.content.append("n").append_i(this.separator);
    }

    public void stroke() {
        if (this.inText) {
            if (isTagged()) {
                endText();
            } else {
                throw new IllegalPdfSyntaxException(MessageLocalization.getComposedMessage("path.construction.operator.inside.text.object", new Object[0]));
            }
        }
        PdfWriter.checkPdfIsoConformance(this.writer, 1, this.state.colorStroke);
        PdfWriter.checkPdfIsoConformance(this.writer, 6, this.state.extGState);
        this.content.append("S").append_i(this.separator);
    }

    public void closePathStroke() {
        if (this.inText) {
            if (isTagged()) {
                endText();
            } else {
                throw new IllegalPdfSyntaxException(MessageLocalization.getComposedMessage("path.construction.operator.inside.text.object", new Object[0]));
            }
        }
        PdfWriter.checkPdfIsoConformance(this.writer, 1, this.state.colorStroke);
        PdfWriter.checkPdfIsoConformance(this.writer, 6, this.state.extGState);
        this.content.append(HtmlTags.f36S).append_i(this.separator);
    }

    public void fill() {
        if (this.inText) {
            if (isTagged()) {
                endText();
            } else {
                throw new IllegalPdfSyntaxException(MessageLocalization.getComposedMessage("path.construction.operator.inside.text.object", new Object[0]));
            }
        }
        PdfWriter.checkPdfIsoConformance(this.writer, 1, this.state.colorFill);
        PdfWriter.checkPdfIsoConformance(this.writer, 6, this.state.extGState);
        this.content.append("f").append_i(this.separator);
    }

    public void eoFill() {
        if (this.inText) {
            if (isTagged()) {
                endText();
            } else {
                throw new IllegalPdfSyntaxException(MessageLocalization.getComposedMessage("path.construction.operator.inside.text.object", new Object[0]));
            }
        }
        PdfWriter.checkPdfIsoConformance(this.writer, 1, this.state.colorFill);
        PdfWriter.checkPdfIsoConformance(this.writer, 6, this.state.extGState);
        this.content.append("f*").append_i(this.separator);
    }

    public void fillStroke() {
        if (this.inText) {
            if (isTagged()) {
                endText();
            } else {
                throw new IllegalPdfSyntaxException(MessageLocalization.getComposedMessage("path.construction.operator.inside.text.object", new Object[0]));
            }
        }
        PdfWriter.checkPdfIsoConformance(this.writer, 1, this.state.colorFill);
        PdfWriter.checkPdfIsoConformance(this.writer, 1, this.state.colorStroke);
        PdfWriter.checkPdfIsoConformance(this.writer, 6, this.state.extGState);
        this.content.append("B").append_i(this.separator);
    }

    public void closePathFillStroke() {
        if (this.inText) {
            if (isTagged()) {
                endText();
            } else {
                throw new IllegalPdfSyntaxException(MessageLocalization.getComposedMessage("path.construction.operator.inside.text.object", new Object[0]));
            }
        }
        PdfWriter.checkPdfIsoConformance(this.writer, 1, this.state.colorFill);
        PdfWriter.checkPdfIsoConformance(this.writer, 1, this.state.colorStroke);
        PdfWriter.checkPdfIsoConformance(this.writer, 6, this.state.extGState);
        this.content.append(HtmlTags.f33B).append_i(this.separator);
    }

    public void eoFillStroke() {
        if (this.inText) {
            if (isTagged()) {
                endText();
            } else {
                throw new IllegalPdfSyntaxException(MessageLocalization.getComposedMessage("path.construction.operator.inside.text.object", new Object[0]));
            }
        }
        PdfWriter.checkPdfIsoConformance(this.writer, 1, this.state.colorFill);
        PdfWriter.checkPdfIsoConformance(this.writer, 1, this.state.colorStroke);
        PdfWriter.checkPdfIsoConformance(this.writer, 6, this.state.extGState);
        this.content.append("B*").append_i(this.separator);
    }

    public void closePathEoFillStroke() {
        if (this.inText) {
            if (isTagged()) {
                endText();
            } else {
                throw new IllegalPdfSyntaxException(MessageLocalization.getComposedMessage("path.construction.operator.inside.text.object", new Object[0]));
            }
        }
        PdfWriter.checkPdfIsoConformance(this.writer, 1, this.state.colorFill);
        PdfWriter.checkPdfIsoConformance(this.writer, 1, this.state.colorStroke);
        PdfWriter.checkPdfIsoConformance(this.writer, 6, this.state.extGState);
        this.content.append("b*").append_i(this.separator);
    }

    public void addImage(Image image) throws DocumentException {
        addImage(image, false);
    }

    public void addImage(Image image, boolean inlineImage) throws DocumentException {
        if (image.hasAbsoluteY()) {
            float[] matrix = image.matrix();
            matrix[4] = image.getAbsoluteX() - matrix[4];
            matrix[5] = image.getAbsoluteY() - matrix[5];
            addImage(image, matrix[0], matrix[1], matrix[2], matrix[3], matrix[4], matrix[5], inlineImage);
            return;
        }
        throw new DocumentException(MessageLocalization.getComposedMessage("the.image.must.have.absolute.positioning", new Object[0]));
    }

    public void addImage(Image image, float a, float b, float c, float d, float e, float f) throws DocumentException {
        addImage(image, a, b, c, d, e, f, false);
    }

    public void addImage(Image image, AffineTransform transform) throws DocumentException {
        double[] matrix = new double[6];
        transform.getMatrix(matrix);
        addImage(image, (float) matrix[0], (float) matrix[1], (float) matrix[2], (float) matrix[3], (float) matrix[4], (float) matrix[5], false);
    }

    public void addImage(Image image, float a, float b, float c, float d, float e, float f, boolean inlineImage) throws DocumentException {
        try {
            float w;
            float h;
            if (image.getLayer() != null) {
                beginLayer(image.getLayer());
            }
            if (isTagged()) {
                if (this.inText) {
                    endText();
                }
                Point2D[] dst = new Float[4];
                new AffineTransform(a, b, c, d, e, f).transform(new Float[]{new Float(0.0f, 0.0f), new Float(BaseField.BORDER_WIDTH_THIN, 0.0f), new Float(BaseField.BORDER_WIDTH_THIN, BaseField.BORDER_WIDTH_THIN), new Float(0.0f, BaseField.BORDER_WIDTH_THIN)}, 0, dst, 0, 4);
                float left = AutoScrollHelper.NO_MAX;
                float right = -3.4028235E38f;
                float bottom = AutoScrollHelper.NO_MAX;
                float top = -3.4028235E38f;
                for (int i = 0; i < 4; i++) {
                    if (dst[i].getX() < ((double) left)) {
                        left = (float) dst[i].getX();
                    }
                    if (dst[i].getX() > ((double) right)) {
                        right = (float) dst[i].getX();
                    }
                    if (dst[i].getY() < ((double) bottom)) {
                        bottom = (float) dst[i].getY();
                    }
                    if (dst[i].getY() > ((double) top)) {
                        top = (float) dst[i].getY();
                    }
                }
                Image image2 = image;
                image2.setAccessibleAttribute(PdfName.BBOX, new PdfArray(new float[]{left, bottom, right, top}));
            }
            if (this.writer == null || !image.isImgTemplate()) {
                this.content.append("q ");
                this.content.append(a).append(' ');
                this.content.append(b).append(' ');
                this.content.append(c).append(' ');
                this.content.append(d).append(' ');
                this.content.append(e).append(' ');
                this.content.append(f).append(" cm");
                if (inlineImage) {
                    this.content.append("\nBI\n");
                    PdfImage pdfImage = new PdfImage(image, "", null);
                    if (image instanceof ImgJBIG2) {
                        byte[] globals = ((ImgJBIG2) image).getGlobalBytes();
                        if (globals != null) {
                            PdfObject decodeparms = new PdfDictionary();
                            decodeparms.put(PdfName.JBIG2GLOBALS, this.writer.getReferenceJBIG2Globals(globals));
                            pdfImage.put(PdfName.DECODEPARMS, decodeparms);
                        }
                    }
                    PdfWriter.checkPdfIsoConformance(this.writer, 17, pdfImage);
                    for (PdfName key : pdfImage.getKeys()) {
                        PdfObject value = pdfImage.get(key);
                        String s = (String) abrev.get(key);
                        if (s != null) {
                            this.content.append(s);
                            boolean check = true;
                            if (key.equals(PdfName.COLORSPACE) && value.isArray()) {
                                PdfArray ar = (PdfArray) value;
                                if (ar.size() == 4 && PdfName.INDEXED.equals(ar.getAsName(0)) && ar.getPdfObject(1).isName() && ar.getPdfObject(2).isNumber() && ar.getPdfObject(3).isString()) {
                                    check = false;
                                }
                            }
                            if (check) {
                                if (key.equals(PdfName.COLORSPACE) && !value.isName()) {
                                    PdfObject cs = this.writer.getColorspaceName();
                                    getPageResources().addColor(cs, this.writer.addToBody(value).getIndirectReference());
                                    value = cs;
                                }
                            }
                            value.toPdf(null, this.content);
                            this.content.append('\n');
                        }
                    }
                    OutputStream baos = new ByteArrayOutputStream();
                    pdfImage.writeContent(baos);
                    byte[] imageBytes = baos.toByteArray();
                    this.content.append(String.format("/L %s\n", new Object[]{Integer.valueOf(imageBytes.length)}));
                    this.content.append("ID\n");
                    this.content.append(imageBytes);
                    this.content.append("\nEI\nQ").append_i(this.separator);
                } else {
                    PdfName name;
                    PageResources prs = getPageResources();
                    Image maskImage = image.getImageMask();
                    if (maskImage != null) {
                        name = this.writer.addDirectImageSimple(maskImage);
                        prs.addXObject(name, this.writer.getImageReference(name));
                    }
                    name = this.writer.addDirectImageSimple(image);
                    this.content.append(' ').append(prs.addXObject(name, this.writer.getImageReference(name)).getBytes()).append(" Do Q").append_i(this.separator);
                }
            } else {
                this.writer.addDirectImageSimple(image);
                PdfTemplate template = image.getTemplateData();
                if (image.getAccessibleAttributes() != null) {
                    for (PdfName key2 : image.getAccessibleAttributes().keySet()) {
                        template.setAccessibleAttribute(key2, image.getAccessibleAttribute(key2));
                    }
                }
                w = template.getWidth();
                h = template.getHeight();
                addTemplate(template, a / w, b / w, c / h, d / h, e, f);
            }
            if (image.hasBorders()) {
                saveState();
                w = image.getWidth();
                h = image.getHeight();
                concatCTM(a / w, b / w, c / h, d / h, e, f);
                rectangle(image);
                restoreState();
            }
            if (image.getLayer() != null) {
                endLayer();
            }
            Annotation annot = image.getAnnotation();
            if (annot != null) {
                int k;
                float[] r = new float[unitRect.length];
                for (k = 0; k < unitRect.length; k += 2) {
                    r[k] = ((unitRect[k] * a) + (unitRect[k + 1] * c)) + e;
                    r[k + 1] = ((unitRect[k] * b) + (unitRect[k + 1] * d)) + f;
                }
                float llx = r[0];
                float lly = r[1];
                float urx = llx;
                float ury = lly;
                for (k = 2; k < r.length; k += 2) {
                    llx = Math.min(llx, r[k]);
                    lly = Math.min(lly, r[k + 1]);
                    urx = Math.max(urx, r[k]);
                    ury = Math.max(ury, r[k + 1]);
                }
                Annotation annotation = new Annotation(annot);
                annotation.setDimensions(llx, lly, urx, ury);
                PdfAnnotation an = PdfAnnotationsImp.convertAnnotation(this.writer, annotation, new Rectangle(llx, lly, urx, ury));
                if (an == null) {
                    annot = annotation;
                    return;
                }
                addAnnotation(an);
                annot = annotation;
            }
        } catch (Exception ee) {
            throw new DocumentException(ee);
        }
    }

    public void reset() {
        reset(true);
    }

    public void reset(boolean validateContent) {
        this.content.reset();
        this.markedContentSize = 0;
        if (validateContent) {
            sanityCheck();
        }
        this.state = new GraphicState();
        this.stateList = new ArrayList();
    }

    protected void beginText(boolean restoreTM) {
        if (!this.inText) {
            this.inText = true;
            this.content.append("BT").append_i(this.separator);
            if (restoreTM) {
                float xTLM = this.state.xTLM;
                float tx = this.state.tx;
                setTextMatrix(this.state.aTLM, this.state.bTLM, this.state.cTLM, this.state.dTLM, this.state.tx, this.state.yTLM);
                this.state.xTLM = xTLM;
                this.state.tx = tx;
            } else {
                this.state.xTLM = 0.0f;
                this.state.yTLM = 0.0f;
                this.state.tx = 0.0f;
            }
            if (isTagged()) {
                try {
                    restoreColor();
                } catch (IOException e) {
                }
            }
        } else if (!isTagged()) {
            throw new IllegalPdfSyntaxException(MessageLocalization.getComposedMessage("unbalanced.begin.end.text.operators", new Object[0]));
        }
    }

    public void beginText() {
        beginText(false);
    }

    public void endText() {
        if (this.inText) {
            this.inText = false;
            this.content.append("ET").append_i(this.separator);
            if (isTagged()) {
                try {
                    restoreColor();
                } catch (IOException e) {
                }
            }
        } else if (!isTagged()) {
            throw new IllegalPdfSyntaxException(MessageLocalization.getComposedMessage("unbalanced.begin.end.text.operators", new Object[0]));
        }
    }

    public void saveState() {
        PdfWriter.checkPdfIsoConformance(this.writer, 12, "q");
        if (this.inText && isTagged()) {
            endText();
        }
        this.content.append("q").append_i(this.separator);
        this.stateList.add(new GraphicState(this.state));
    }

    public void restoreState() {
        PdfWriter.checkPdfIsoConformance(this.writer, 12, "Q");
        if (this.inText && isTagged()) {
            endText();
        }
        this.content.append("Q").append_i(this.separator);
        int idx = this.stateList.size() - 1;
        if (idx < 0) {
            throw new IllegalPdfSyntaxException(MessageLocalization.getComposedMessage("unbalanced.save.restore.state.operators", new Object[0]));
        }
        this.state.restore((GraphicState) this.stateList.get(idx));
        this.stateList.remove(idx);
    }

    public void setCharacterSpacing(float charSpace) {
        if (!this.inText && isTagged()) {
            beginText(true);
        }
        this.state.charSpace = charSpace;
        this.content.append(charSpace).append(" Tc").append_i(this.separator);
    }

    public void setWordSpacing(float wordSpace) {
        if (!this.inText && isTagged()) {
            beginText(true);
        }
        this.state.wordSpace = wordSpace;
        this.content.append(wordSpace).append(" Tw").append_i(this.separator);
    }

    public void setHorizontalScaling(float scale) {
        if (!this.inText && isTagged()) {
            beginText(true);
        }
        this.state.scale = scale;
        this.content.append(scale).append(" Tz").append_i(this.separator);
    }

    public void setLeading(float leading) {
        if (!this.inText && isTagged()) {
            beginText(true);
        }
        this.state.leading = leading;
        this.content.append(leading).append(" TL").append_i(this.separator);
    }

    public void setFontAndSize(BaseFont bf, float size) {
        if (!this.inText && isTagged()) {
            beginText(true);
        }
        checkWriter();
        if (size >= 1.0E-4f || size <= -1.0E-4f) {
            this.state.size = size;
            this.state.fontDetails = this.writer.addSimple(bf);
            this.content.append(getPageResources().addFont(this.state.fontDetails.getFontName(), this.state.fontDetails.getIndirectReference()).getBytes()).append(' ').append(size).append(" Tf").append_i(this.separator);
            return;
        }
        throw new IllegalArgumentException(MessageLocalization.getComposedMessage("font.size.too.small.1", String.valueOf(size)));
    }

    public void setTextRenderingMode(int rendering) {
        if (!this.inText && isTagged()) {
            beginText(true);
        }
        this.state.textRenderMode = rendering;
        this.content.append(rendering).append(" Tr").append_i(this.separator);
    }

    public void setTextRise(float rise) {
        if (!this.inText && isTagged()) {
            beginText(true);
        }
        this.content.append(rise).append(" Ts").append_i(this.separator);
    }

    private void showText2(String text) {
        if (this.state.fontDetails == null) {
            throw new NullPointerException(MessageLocalization.getComposedMessage("font.and.size.must.be.set.before.writing.any.text", new Object[0]));
        }
        StringUtils.escapeString(this.state.fontDetails.convertToBytes(text), this.content);
    }

    public void showText(String text) {
        checkState();
        if (!this.inText && isTagged()) {
            beginText(true);
        }
        showText2(text);
        updateTx(text, 0.0f);
        this.content.append("Tj").append_i(this.separator);
    }

    public void showTextGid(String gids) {
        checkState();
        if (!this.inText && isTagged()) {
            beginText(true);
        }
        if (this.state.fontDetails == null) {
            throw new NullPointerException(MessageLocalization.getComposedMessage("font.and.size.must.be.set.before.writing.any.text", new Object[0]));
        }
        Object[] objs = this.state.fontDetails.convertToBytesGid(gids);
        StringUtils.escapeString((byte[]) objs[0], this.content);
        GraphicState graphicState = this.state;
        graphicState.tx = ((((float) ((Integer) objs[2]).intValue()) * 0.001f) * this.state.size) + graphicState.tx;
        this.content.append("Tj").append_i(this.separator);
    }

    public static PdfTextArray getKernArray(String text, BaseFont font) {
        PdfTextArray pa = new PdfTextArray();
        StringBuffer acc = new StringBuffer();
        int len = text.length() - 1;
        char[] c = text.toCharArray();
        if (len >= 0) {
            acc.append(c, 0, 1);
        }
        for (int k = 0; k < len; k++) {
            char c2 = c[k + 1];
            int kern = font.getKerning(c[k], c2);
            if (kern == 0) {
                acc.append(c2);
            } else {
                pa.add(acc.toString());
                acc.setLength(0);
                acc.append(c, k + 1, 1);
                pa.add((float) (-kern));
            }
        }
        pa.add(acc.toString());
        return pa;
    }

    public void showTextKerned(String text) {
        if (this.state.fontDetails == null) {
            throw new NullPointerException(MessageLocalization.getComposedMessage("font.and.size.must.be.set.before.writing.any.text", new Object[0]));
        }
        BaseFont bf = this.state.fontDetails.getBaseFont();
        if (bf.hasKernPairs()) {
            showText(getKernArray(text, bf));
        } else {
            showText(text);
        }
    }

    public void newlineShowText(String text) {
        checkState();
        if (!this.inText && isTagged()) {
            beginText(true);
        }
        GraphicState graphicState = this.state;
        graphicState.yTLM -= this.state.leading;
        showText2(text);
        this.content.append("'").append_i(this.separator);
        this.state.tx = this.state.xTLM;
        updateTx(text, 0.0f);
    }

    public void newlineShowText(float wordSpacing, float charSpacing, String text) {
        checkState();
        if (!this.inText && isTagged()) {
            beginText(true);
        }
        GraphicState graphicState = this.state;
        graphicState.yTLM -= this.state.leading;
        this.content.append(wordSpacing).append(' ').append(charSpacing);
        showText2(text);
        this.content.append("\"").append_i(this.separator);
        this.state.charSpace = charSpacing;
        this.state.wordSpace = wordSpacing;
        this.state.tx = this.state.xTLM;
        updateTx(text, 0.0f);
    }

    public void setTextMatrix(float a, float b, float c, float d, float x, float y) {
        if (!this.inText && isTagged()) {
            beginText(true);
        }
        this.state.xTLM = x;
        this.state.yTLM = y;
        this.state.aTLM = a;
        this.state.bTLM = b;
        this.state.cTLM = c;
        this.state.dTLM = d;
        this.state.tx = this.state.xTLM;
        this.content.append(a).append(' ').append(b).append_i(32).append(c).append_i(32).append(d).append_i(32).append(x).append_i(32).append(y).append(" Tm").append_i(this.separator);
    }

    public void setTextMatrix(AffineTransform transform) {
        double[] matrix = new double[6];
        transform.getMatrix(matrix);
        setTextMatrix((float) matrix[0], (float) matrix[1], (float) matrix[2], (float) matrix[3], (float) matrix[4], (float) matrix[5]);
    }

    public void setTextMatrix(float x, float y) {
        setTextMatrix(BaseField.BORDER_WIDTH_THIN, 0.0f, 0.0f, BaseField.BORDER_WIDTH_THIN, x, y);
    }

    public void moveText(float x, float y) {
        if (!this.inText && isTagged()) {
            beginText(true);
        }
        GraphicState graphicState = this.state;
        graphicState.xTLM += x;
        graphicState = this.state;
        graphicState.yTLM += y;
        if (!isTagged() || this.state.xTLM == this.state.tx) {
            this.content.append(x).append(' ').append(y).append(" Td").append_i(this.separator);
            return;
        }
        setTextMatrix(this.state.aTLM, this.state.bTLM, this.state.cTLM, this.state.dTLM, this.state.xTLM, this.state.yTLM);
    }

    public void moveTextWithLeading(float x, float y) {
        if (!this.inText && isTagged()) {
            beginText(true);
        }
        GraphicState graphicState = this.state;
        graphicState.xTLM += x;
        graphicState = this.state;
        graphicState.yTLM += y;
        this.state.leading = -y;
        if (!isTagged() || this.state.xTLM == this.state.tx) {
            this.content.append(x).append(' ').append(y).append(" TD").append_i(this.separator);
            return;
        }
        setTextMatrix(this.state.aTLM, this.state.bTLM, this.state.cTLM, this.state.dTLM, this.state.xTLM, this.state.yTLM);
    }

    public void newlineText() {
        if (!this.inText && isTagged()) {
            beginText(true);
        }
        if (isTagged() && this.state.xTLM != this.state.tx) {
            setTextMatrix(this.state.aTLM, this.state.bTLM, this.state.cTLM, this.state.dTLM, this.state.xTLM, this.state.yTLM);
        }
        GraphicState graphicState = this.state;
        graphicState.yTLM -= this.state.leading;
        this.content.append("T*").append_i(this.separator);
    }

    int size() {
        return size(true);
    }

    int size(boolean includeMarkedContentSize) {
        if (includeMarkedContentSize) {
            return this.content.size();
        }
        return this.content.size() - this.markedContentSize;
    }

    public void addOutline(PdfOutline outline, String name) {
        checkWriter();
        this.pdf.addOutline(outline, name);
    }

    public PdfOutline getRootOutline() {
        checkWriter();
        return this.pdf.getRootOutline();
    }

    public float getEffectiveStringWidth(String text, boolean kerned) {
        float w;
        BaseFont bf = this.state.fontDetails.getBaseFont();
        if (kerned) {
            w = bf.getWidthPointKerned(text, this.state.size);
        } else {
            w = bf.getWidthPoint(text, this.state.size);
        }
        if (this.state.charSpace != 0.0f && text.length() > 1) {
            w += this.state.charSpace * ((float) (text.length() - 1));
        }
        if (!(this.state.wordSpace == 0.0f || bf.isVertical())) {
            for (int i = 0; i < text.length() - 1; i++) {
                if (text.charAt(i) == ' ') {
                    w += this.state.wordSpace;
                }
            }
        }
        if (((double) this.state.scale) != 100.0d) {
            return (this.state.scale * w) / 100.0f;
        }
        return w;
    }

    private float getEffectiveStringWidth(String text, boolean kerned, float kerning) {
        float w;
        BaseFont bf = this.state.fontDetails.getBaseFont();
        if (kerned) {
            w = bf.getWidthPointKerned(text, this.state.size);
        } else {
            w = bf.getWidthPoint(text, this.state.size);
        }
        if (this.state.charSpace != 0.0f && text.length() > 0) {
            w += this.state.charSpace * ((float) text.length());
        }
        if (!(this.state.wordSpace == 0.0f || bf.isVertical())) {
            for (int i = 0; i < text.length(); i++) {
                if (text.charAt(i) == ' ') {
                    w += this.state.wordSpace;
                }
            }
        }
        w -= (kerning / 1000.0f) * this.state.size;
        if (((double) this.state.scale) != 100.0d) {
            return (this.state.scale * w) / 100.0f;
        }
        return w;
    }

    public void showTextAligned(int alignment, String text, float x, float y, float rotation) {
        showTextAligned(alignment, text, x, y, rotation, false);
    }

    private void showTextAligned(int alignment, String text, float x, float y, float rotation, boolean kerned) {
        if (this.state.fontDetails == null) {
            throw new NullPointerException(MessageLocalization.getComposedMessage("font.and.size.must.be.set.before.writing.any.text", new Object[0]));
        } else if (rotation == 0.0f) {
            switch (alignment) {
                case 1:
                    x -= getEffectiveStringWidth(text, kerned) / BaseField.BORDER_WIDTH_MEDIUM;
                    break;
                case 2:
                    x -= getEffectiveStringWidth(text, kerned);
                    break;
            }
            setTextMatrix(x, y);
            if (kerned) {
                showTextKerned(text);
            } else {
                showText(text);
            }
        } else {
            double alpha = (((double) rotation) * 3.141592653589793d) / 180.0d;
            float cos = (float) Math.cos(alpha);
            float sin = (float) Math.sin(alpha);
            float len;
            switch (alignment) {
                case 1:
                    len = getEffectiveStringWidth(text, kerned) / BaseField.BORDER_WIDTH_MEDIUM;
                    x -= len * cos;
                    y -= len * sin;
                    break;
                case 2:
                    len = getEffectiveStringWidth(text, kerned);
                    x -= len * cos;
                    y -= len * sin;
                    break;
            }
            setTextMatrix(cos, sin, -sin, cos, x, y);
            if (kerned) {
                showTextKerned(text);
            } else {
                showText(text);
            }
            setTextMatrix(0.0f, 0.0f);
        }
    }

    public void showTextAlignedKerned(int alignment, String text, float x, float y, float rotation) {
        showTextAligned(alignment, text, x, y, rotation, true);
    }

    public void concatCTM(float a, float b, float c, float d, float e, float f) {
        if (this.inText && isTagged()) {
            endText();
        }
        this.state.CTM.concatenate(new AffineTransform(a, b, c, d, e, f));
        this.content.append(a).append(' ').append(b).append(' ').append(c).append(' ');
        this.content.append(d).append(' ').append(e).append(' ').append(f).append(" cm").append_i(this.separator);
    }

    public void concatCTM(AffineTransform transform) {
        double[] matrix = new double[6];
        transform.getMatrix(matrix);
        concatCTM((float) matrix[0], (float) matrix[1], (float) matrix[2], (float) matrix[3], (float) matrix[4], (float) matrix[5]);
    }

    public static ArrayList<float[]> bezierArc(float x1, float y1, float x2, float y2, float startAng, float extent) {
        float fragAngle;
        int Nfrag;
        if (x1 > x2) {
            float tmp = x1;
            x1 = x2;
            x2 = tmp;
        }
        if (y2 > y1) {
            tmp = y1;
            y1 = y2;
            y2 = tmp;
        }
        if (Math.abs(extent) <= 90.0f) {
            fragAngle = extent;
            Nfrag = 1;
        } else {
            Nfrag = (int) Math.ceil((double) (Math.abs(extent) / 90.0f));
            fragAngle = extent / ((float) Nfrag);
        }
        float x_cen = (x1 + x2) / BaseField.BORDER_WIDTH_MEDIUM;
        float y_cen = (y1 + y2) / BaseField.BORDER_WIDTH_MEDIUM;
        float rx = (x2 - x1) / BaseField.BORDER_WIDTH_MEDIUM;
        float ry = (y2 - y1) / BaseField.BORDER_WIDTH_MEDIUM;
        float halfAng = (float) ((((double) fragAngle) * 3.141592653589793d) / 360.0d);
        float kappa = (float) Math.abs((1.3333333333333333d * (1.0d - Math.cos((double) halfAng))) / Math.sin((double) halfAng));
        ArrayList<float[]> pointList = new ArrayList();
        for (int i = 0; i < Nfrag; i++) {
            float theta0 = (float) ((((double) ((((float) i) * fragAngle) + startAng)) * 3.141592653589793d) / 180.0d);
            float theta1 = (float) ((((double) ((((float) (i + 1)) * fragAngle) + startAng)) * 3.141592653589793d) / 180.0d);
            float cos0 = (float) Math.cos((double) theta0);
            float cos1 = (float) Math.cos((double) theta1);
            float sin0 = (float) Math.sin((double) theta0);
            float sin1 = (float) Math.sin((double) theta1);
            if (fragAngle > 0.0f) {
                pointList.add(new float[]{(rx * cos0) + x_cen, y_cen - (ry * sin0), ((cos0 - (kappa * sin0)) * rx) + x_cen, y_cen - (((kappa * cos0) + sin0) * ry), (((kappa * sin1) + cos1) * rx) + x_cen, y_cen - ((sin1 - (kappa * cos1)) * ry), (rx * cos1) + x_cen, y_cen - (ry * sin1)});
            } else {
                pointList.add(new float[]{(rx * cos0) + x_cen, y_cen - (ry * sin0), (((kappa * sin0) + cos0) * rx) + x_cen, y_cen - ((sin0 - (kappa * cos0)) * ry), ((cos1 - (kappa * sin1)) * rx) + x_cen, y_cen - (((kappa * cos1) + sin1) * ry), (rx * cos1) + x_cen, y_cen - (ry * sin1)});
            }
        }
        return pointList;
    }

    public void arc(float x1, float y1, float x2, float y2, float startAng, float extent) {
        ArrayList<float[]> ar = bezierArc(x1, y1, x2, y2, startAng, extent);
        if (!ar.isEmpty()) {
            float[] pt = (float[]) ar.get(0);
            moveTo(pt[0], pt[1]);
            for (int k = 0; k < ar.size(); k++) {
                pt = (float[]) ar.get(k);
                curveTo(pt[2], pt[3], pt[4], pt[5], pt[6], pt[7]);
            }
        }
    }

    public void ellipse(float x1, float y1, float x2, float y2) {
        arc(x1, y1, x2, y2, 0.0f, 360.0f);
    }

    public PdfPatternPainter createPattern(float width, float height, float xstep, float ystep) {
        checkWriter();
        if (xstep == 0.0f || ystep == 0.0f) {
            throw new RuntimeException(MessageLocalization.getComposedMessage("xstep.or.ystep.can.not.be.zero", new Object[0]));
        }
        PdfPatternPainter painter = new PdfPatternPainter(this.writer);
        painter.setWidth(width);
        painter.setHeight(height);
        painter.setXStep(xstep);
        painter.setYStep(ystep);
        this.writer.addSimplePattern(painter);
        return painter;
    }

    public PdfPatternPainter createPattern(float width, float height) {
        return createPattern(width, height, width, height);
    }

    public PdfPatternPainter createPattern(float width, float height, float xstep, float ystep, BaseColor color) {
        checkWriter();
        if (xstep == 0.0f || ystep == 0.0f) {
            throw new RuntimeException(MessageLocalization.getComposedMessage("xstep.or.ystep.can.not.be.zero", new Object[0]));
        }
        PdfPatternPainter painter = new PdfPatternPainter(this.writer, color);
        painter.setWidth(width);
        painter.setHeight(height);
        painter.setXStep(xstep);
        painter.setYStep(ystep);
        this.writer.addSimplePattern(painter);
        return painter;
    }

    public PdfPatternPainter createPattern(float width, float height, BaseColor color) {
        return createPattern(width, height, width, height, color);
    }

    public PdfTemplate createTemplate(float width, float height) {
        return createTemplate(width, height, null);
    }

    PdfTemplate createTemplate(float width, float height, PdfName forcedName) {
        checkWriter();
        PdfTemplate template = new PdfTemplate(this.writer);
        template.setWidth(width);
        template.setHeight(height);
        this.writer.addDirectTemplateSimple(template, forcedName);
        return template;
    }

    public PdfAppearance createAppearance(float width, float height) {
        return createAppearance(width, height, null);
    }

    PdfAppearance createAppearance(float width, float height, PdfName forcedName) {
        checkWriter();
        PdfAppearance template = new PdfAppearance(this.writer);
        template.setWidth(width);
        template.setHeight(height);
        this.writer.addDirectTemplateSimple(template, forcedName);
        return template;
    }

    public void addPSXObject(PdfPSXObject psobject) {
        if (this.inText && isTagged()) {
            endText();
        }
        checkWriter();
        this.content.append(getPageResources().addXObject(this.writer.addDirectTemplateSimple(psobject, null), psobject.getIndirectReference()).getBytes()).append(" Do").append_i(this.separator);
    }

    public void addTemplate(PdfTemplate template, float a, float b, float c, float d, float e, float f) {
        addTemplate(template, a, b, c, d, e, f, false);
    }

    public void addTemplate(PdfTemplate template, float a, float b, float c, float d, float e, float f, boolean tagContent) {
        checkWriter();
        checkNoPattern(template);
        PdfWriter.checkPdfIsoConformance(this.writer, 20, template);
        PdfName name = getPageResources().addXObject(this.writer.addDirectTemplateSimple(template, null), template.getIndirectReference());
        if (isTagged()) {
            if (this.inText) {
                endText();
            }
            if (template.isContentTagged() || (template.getPageReference() != null && tagContent)) {
                throw new RuntimeException(MessageLocalization.getComposedMessage("template.with.tagged.could.not.be.used.more.than.once", new Object[0]));
            }
            template.setPageReference(this.writer.getCurrentPage());
            if (tagContent) {
                template.setContentTagged(true);
                ArrayList<IAccessibleElement> allMcElements = getMcElements();
                if (allMcElements != null && allMcElements.size() > 0) {
                    template.getMcElements().add(allMcElements.get(allMcElements.size() - 1));
                }
            } else {
                openMCBlock(template);
            }
        }
        this.content.append("q ");
        this.content.append(a).append(' ');
        this.content.append(b).append(' ');
        this.content.append(c).append(' ');
        this.content.append(d).append(' ');
        this.content.append(e).append(' ');
        this.content.append(f).append(" cm ");
        this.content.append(name.getBytes()).append(" Do Q").append_i(this.separator);
        if (isTagged() && !tagContent) {
            closeMCBlock(template);
            template.setId(null);
        }
    }

    public PdfName addFormXObj(PdfStream formXObj, PdfName name, float a, float b, float c, float d, float e, float f) throws IOException {
        checkWriter();
        PdfWriter.checkPdfIsoConformance(this.writer, 9, formXObj);
        PdfName translatedName = getPageResources().addXObject(name, this.writer.addToBody(formXObj).getIndirectReference());
        PdfArtifact artifact = null;
        if (isTagged()) {
            if (this.inText) {
                endText();
            }
            artifact = new PdfArtifact();
            openMCBlock(artifact);
        }
        this.content.append("q ");
        this.content.append(a).append(' ');
        this.content.append(b).append(' ');
        this.content.append(c).append(' ');
        this.content.append(d).append(' ');
        this.content.append(e).append(' ');
        this.content.append(f).append(" cm ");
        this.content.append(translatedName.getBytes()).append(" Do Q").append_i(this.separator);
        if (isTagged()) {
            closeMCBlock(artifact);
        }
        return translatedName;
    }

    public void addTemplate(PdfTemplate template, AffineTransform transform) {
        addTemplate(template, transform, false);
    }

    public void addTemplate(PdfTemplate template, AffineTransform transform, boolean tagContent) {
        double[] matrix = new double[6];
        transform.getMatrix(matrix);
        addTemplate(template, (float) matrix[0], (float) matrix[1], (float) matrix[2], (float) matrix[3], (float) matrix[4], (float) matrix[5], tagContent);
    }

    void addTemplateReference(PdfIndirectReference template, PdfName name, float a, float b, float c, float d, float e, float f) {
        if (this.inText && isTagged()) {
            endText();
        }
        checkWriter();
        name = getPageResources().addXObject(name, template);
        this.content.append("q ");
        this.content.append(a).append(' ');
        this.content.append(b).append(' ');
        this.content.append(c).append(' ');
        this.content.append(d).append(' ');
        this.content.append(e).append(' ');
        this.content.append(f).append(" cm ");
        this.content.append(name.getBytes()).append(" Do Q").append_i(this.separator);
    }

    public void addTemplate(PdfTemplate template, float x, float y) {
        addTemplate(template, BaseField.BORDER_WIDTH_THIN, 0.0f, 0.0f, BaseField.BORDER_WIDTH_THIN, x, y);
    }

    public void addTemplate(PdfTemplate template, float x, float y, boolean tagContent) {
        addTemplate(template, BaseField.BORDER_WIDTH_THIN, 0.0f, 0.0f, BaseField.BORDER_WIDTH_THIN, x, y, tagContent);
    }

    public void setCMYKColorFill(int cyan, int magenta, int yellow, int black) {
        saveColor(new CMYKColor(cyan, magenta, yellow, black), true);
        this.content.append(((float) (cyan & 255)) / 255.0f);
        this.content.append(' ');
        this.content.append(((float) (magenta & 255)) / 255.0f);
        this.content.append(' ');
        this.content.append(((float) (yellow & 255)) / 255.0f);
        this.content.append(' ');
        this.content.append(((float) (black & 255)) / 255.0f);
        this.content.append(" k").append_i(this.separator);
    }

    public void setCMYKColorStroke(int cyan, int magenta, int yellow, int black) {
        saveColor(new CMYKColor(cyan, magenta, yellow, black), false);
        this.content.append(((float) (cyan & 255)) / 255.0f);
        this.content.append(' ');
        this.content.append(((float) (magenta & 255)) / 255.0f);
        this.content.append(' ');
        this.content.append(((float) (yellow & 255)) / 255.0f);
        this.content.append(' ');
        this.content.append(((float) (black & 255)) / 255.0f);
        this.content.append(" K").append_i(this.separator);
    }

    public void setRGBColorFill(int red, int green, int blue) {
        saveColor(new BaseColor(red, green, blue), true);
        HelperRGB(((float) (red & 255)) / 255.0f, ((float) (green & 255)) / 255.0f, ((float) (blue & 255)) / 255.0f);
        this.content.append(" rg").append_i(this.separator);
    }

    public void setRGBColorStroke(int red, int green, int blue) {
        saveColor(new BaseColor(red, green, blue), false);
        HelperRGB(((float) (red & 255)) / 255.0f, ((float) (green & 255)) / 255.0f, ((float) (blue & 255)) / 255.0f);
        this.content.append(" RG").append_i(this.separator);
    }

    public void setColorStroke(BaseColor color) {
        switch (ExtendedColor.getType(color)) {
            case 1:
                setGrayStroke(((GrayColor) color).getGray());
                return;
            case 2:
                CMYKColor cmyk = (CMYKColor) color;
                setCMYKColorStrokeF(cmyk.getCyan(), cmyk.getMagenta(), cmyk.getYellow(), cmyk.getBlack());
                return;
            case 3:
                SpotColor spot = (SpotColor) color;
                setColorStroke(spot.getPdfSpotColor(), spot.getTint());
                return;
            case 4:
                setPatternStroke(((PatternColor) color).getPainter());
                return;
            case 5:
                setShadingStroke(((ShadingColor) color).getPdfShadingPattern());
                return;
            case 6:
                DeviceNColor devicen = (DeviceNColor) color;
                setColorStroke(devicen.getPdfDeviceNColor(), devicen.getTints());
                return;
            case 7:
                LabColor lab = (LabColor) color;
                setColorStroke(lab.getLabColorSpace(), lab.getL(), lab.getA(), lab.getB());
                return;
            default:
                setRGBColorStroke(color.getRed(), color.getGreen(), color.getBlue());
                return;
        }
    }

    public void setColorFill(BaseColor color) {
        switch (ExtendedColor.getType(color)) {
            case 1:
                setGrayFill(((GrayColor) color).getGray());
                return;
            case 2:
                CMYKColor cmyk = (CMYKColor) color;
                setCMYKColorFillF(cmyk.getCyan(), cmyk.getMagenta(), cmyk.getYellow(), cmyk.getBlack());
                return;
            case 3:
                SpotColor spot = (SpotColor) color;
                setColorFill(spot.getPdfSpotColor(), spot.getTint());
                return;
            case 4:
                setPatternFill(((PatternColor) color).getPainter());
                return;
            case 5:
                setShadingFill(((ShadingColor) color).getPdfShadingPattern());
                return;
            case 6:
                DeviceNColor devicen = (DeviceNColor) color;
                setColorFill(devicen.getPdfDeviceNColor(), devicen.getTints());
                return;
            case 7:
                LabColor lab = (LabColor) color;
                setColorFill(lab.getLabColorSpace(), lab.getL(), lab.getA(), lab.getB());
                return;
            default:
                setRGBColorFill(color.getRed(), color.getGreen(), color.getBlue());
                return;
        }
    }

    public void setColorFill(PdfSpotColor sp, float tint) {
        checkWriter();
        this.state.colorDetails = this.writer.addSimple(sp);
        PdfName name = getPageResources().addColor(this.state.colorDetails.getColorSpaceName(), this.state.colorDetails.getIndirectReference());
        saveColor(new SpotColor(sp, tint), true);
        this.content.append(name.getBytes()).append(" cs ").append(tint).append(" scn").append_i(this.separator);
    }

    public void setColorFill(PdfDeviceNColor dn, float[] tints) {
        checkWriter();
        this.state.colorDetails = this.writer.addSimple(dn);
        PdfName name = getPageResources().addColor(this.state.colorDetails.getColorSpaceName(), this.state.colorDetails.getIndirectReference());
        saveColor(new DeviceNColor(dn, tints), true);
        this.content.append(name.getBytes()).append(" cs ");
        for (float tint : tints) {
            this.content.append(tint + " ");
        }
        this.content.append("scn").append_i(this.separator);
    }

    public void setColorFill(PdfLabColor lab, float l, float a, float b) {
        checkWriter();
        this.state.colorDetails = this.writer.addSimple(lab);
        PdfName name = getPageResources().addColor(this.state.colorDetails.getColorSpaceName(), this.state.colorDetails.getIndirectReference());
        saveColor(new LabColor(lab, l, a, b), true);
        this.content.append(name.getBytes()).append(" cs ");
        this.content.append(l + " " + a + " " + b + " ");
        this.content.append("scn").append_i(this.separator);
    }

    public void setColorStroke(PdfSpotColor sp, float tint) {
        checkWriter();
        this.state.colorDetails = this.writer.addSimple(sp);
        PdfName name = getPageResources().addColor(this.state.colorDetails.getColorSpaceName(), this.state.colorDetails.getIndirectReference());
        saveColor(new SpotColor(sp, tint), false);
        this.content.append(name.getBytes()).append(" CS ").append(tint).append(" SCN").append_i(this.separator);
    }

    public void setColorStroke(PdfDeviceNColor sp, float[] tints) {
        checkWriter();
        this.state.colorDetails = this.writer.addSimple(sp);
        PdfName name = getPageResources().addColor(this.state.colorDetails.getColorSpaceName(), this.state.colorDetails.getIndirectReference());
        saveColor(new DeviceNColor(sp, tints), true);
        this.content.append(name.getBytes()).append(" CS ");
        for (float tint : tints) {
            this.content.append(tint + " ");
        }
        this.content.append("SCN").append_i(this.separator);
    }

    public void setColorStroke(PdfLabColor lab, float l, float a, float b) {
        checkWriter();
        this.state.colorDetails = this.writer.addSimple(lab);
        PdfName name = getPageResources().addColor(this.state.colorDetails.getColorSpaceName(), this.state.colorDetails.getIndirectReference());
        saveColor(new LabColor(lab, l, a, b), true);
        this.content.append(name.getBytes()).append(" CS ");
        this.content.append(l + " " + a + " " + b + " ");
        this.content.append("SCN").append_i(this.separator);
    }

    public void setPatternFill(PdfPatternPainter p) {
        if (p.isStencil()) {
            setPatternFill(p, p.getDefaultColor());
            return;
        }
        checkWriter();
        PdfName name = getPageResources().addPattern(this.writer.addSimplePattern(p), p.getIndirectReference());
        saveColor(new PatternColor(p), true);
        this.content.append(PdfName.PATTERN.getBytes()).append(" cs ").append(name.getBytes()).append(" scn").append_i(this.separator);
    }

    void outputColorNumbers(BaseColor color, float tint) {
        PdfWriter.checkPdfIsoConformance(this.writer, 1, color);
        switch (ExtendedColor.getType(color)) {
            case 0:
                this.content.append(((float) color.getRed()) / 255.0f);
                this.content.append(' ');
                this.content.append(((float) color.getGreen()) / 255.0f);
                this.content.append(' ');
                this.content.append(((float) color.getBlue()) / 255.0f);
                return;
            case 1:
                this.content.append(((GrayColor) color).getGray());
                return;
            case 2:
                CMYKColor cmyk = (CMYKColor) color;
                this.content.append(cmyk.getCyan()).append(' ').append(cmyk.getMagenta());
                this.content.append(' ').append(cmyk.getYellow()).append(' ').append(cmyk.getBlack());
                return;
            case 3:
                this.content.append(tint);
                return;
            default:
                throw new RuntimeException(MessageLocalization.getComposedMessage("invalid.color.type", new Object[0]));
        }
    }

    public void setPatternFill(PdfPatternPainter p, BaseColor color) {
        if (ExtendedColor.getType(color) == 3) {
            setPatternFill(p, color, ((SpotColor) color).getTint());
        } else {
            setPatternFill(p, color, 0.0f);
        }
    }

    public void setPatternFill(PdfPatternPainter p, BaseColor color, float tint) {
        checkWriter();
        if (p.isStencil()) {
            PageResources prs = getPageResources();
            PdfName name = prs.addPattern(this.writer.addSimplePattern(p), p.getIndirectReference());
            ColorDetails csDetail = this.writer.addSimplePatternColorspace(color);
            PdfName cName = prs.addColor(csDetail.getColorSpaceName(), csDetail.getIndirectReference());
            saveColor(new UncoloredPattern(p, color, tint), true);
            this.content.append(cName.getBytes()).append(" cs").append_i(this.separator);
            outputColorNumbers(color, tint);
            this.content.append(' ').append(name.getBytes()).append(" scn").append_i(this.separator);
            return;
        }
        throw new RuntimeException(MessageLocalization.getComposedMessage("an.uncolored.pattern.was.expected", new Object[0]));
    }

    public void setPatternStroke(PdfPatternPainter p, BaseColor color) {
        if (ExtendedColor.getType(color) == 3) {
            setPatternStroke(p, color, ((SpotColor) color).getTint());
        } else {
            setPatternStroke(p, color, 0.0f);
        }
    }

    public void setPatternStroke(PdfPatternPainter p, BaseColor color, float tint) {
        checkWriter();
        if (p.isStencil()) {
            PageResources prs = getPageResources();
            PdfName name = prs.addPattern(this.writer.addSimplePattern(p), p.getIndirectReference());
            ColorDetails csDetail = this.writer.addSimplePatternColorspace(color);
            PdfName cName = prs.addColor(csDetail.getColorSpaceName(), csDetail.getIndirectReference());
            saveColor(new UncoloredPattern(p, color, tint), false);
            this.content.append(cName.getBytes()).append(" CS").append_i(this.separator);
            outputColorNumbers(color, tint);
            this.content.append(' ').append(name.getBytes()).append(" SCN").append_i(this.separator);
            return;
        }
        throw new RuntimeException(MessageLocalization.getComposedMessage("an.uncolored.pattern.was.expected", new Object[0]));
    }

    public void setPatternStroke(PdfPatternPainter p) {
        if (p.isStencil()) {
            setPatternStroke(p, p.getDefaultColor());
            return;
        }
        checkWriter();
        PdfName name = getPageResources().addPattern(this.writer.addSimplePattern(p), p.getIndirectReference());
        saveColor(new PatternColor(p), false);
        this.content.append(PdfName.PATTERN.getBytes()).append(" CS ").append(name.getBytes()).append(" SCN").append_i(this.separator);
    }

    public void paintShading(PdfShading shading) {
        this.writer.addSimpleShading(shading);
        PageResources prs = getPageResources();
        this.content.append(prs.addShading(shading.getShadingName(), shading.getShadingReference()).getBytes()).append(" sh").append_i(this.separator);
        ColorDetails details = shading.getColorDetails();
        if (details != null) {
            prs.addColor(details.getColorSpaceName(), details.getIndirectReference());
        }
    }

    public void paintShading(PdfShadingPattern shading) {
        paintShading(shading.getShading());
    }

    public void setShadingFill(PdfShadingPattern shading) {
        this.writer.addSimpleShadingPattern(shading);
        PageResources prs = getPageResources();
        PdfName name = prs.addPattern(shading.getPatternName(), shading.getPatternReference());
        saveColor(new ShadingColor(shading), true);
        this.content.append(PdfName.PATTERN.getBytes()).append(" cs ").append(name.getBytes()).append(" scn").append_i(this.separator);
        ColorDetails details = shading.getColorDetails();
        if (details != null) {
            prs.addColor(details.getColorSpaceName(), details.getIndirectReference());
        }
    }

    public void setShadingStroke(PdfShadingPattern shading) {
        this.writer.addSimpleShadingPattern(shading);
        PageResources prs = getPageResources();
        PdfName name = prs.addPattern(shading.getPatternName(), shading.getPatternReference());
        saveColor(new ShadingColor(shading), false);
        this.content.append(PdfName.PATTERN.getBytes()).append(" CS ").append(name.getBytes()).append(" SCN").append_i(this.separator);
        ColorDetails details = shading.getColorDetails();
        if (details != null) {
            prs.addColor(details.getColorSpaceName(), details.getIndirectReference());
        }
    }

    protected void checkWriter() {
        if (this.writer == null) {
            throw new NullPointerException(MessageLocalization.getComposedMessage("the.writer.in.pdfcontentbyte.is.null", new Object[0]));
        }
    }

    public void showText(PdfTextArray text) {
        checkState();
        if (!this.inText && isTagged()) {
            beginText(true);
        }
        if (this.state.fontDetails == null) {
            throw new NullPointerException(MessageLocalization.getComposedMessage("font.and.size.must.be.set.before.writing.any.text", new Object[0]));
        }
        this.content.append("[");
        boolean lastWasNumber = false;
        Iterator i$ = text.getArrayList().iterator();
        while (i$.hasNext()) {
            Object obj = i$.next();
            if (obj instanceof String) {
                showText2((String) obj);
                updateTx((String) obj, 0.0f);
                lastWasNumber = false;
            } else {
                if (lastWasNumber) {
                    this.content.append(' ');
                } else {
                    lastWasNumber = true;
                }
                this.content.append(((Float) obj).floatValue());
                updateTx("", ((Float) obj).floatValue());
            }
        }
        this.content.append("]TJ").append_i(this.separator);
    }

    public PdfWriter getPdfWriter() {
        return this.writer;
    }

    public PdfDocument getPdfDocument() {
        return this.pdf;
    }

    public void localGoto(String name, float llx, float lly, float urx, float ury) {
        this.pdf.localGoto(name, llx, lly, urx, ury);
    }

    public boolean localDestination(String name, PdfDestination destination) {
        return this.pdf.localDestination(name, destination);
    }

    public PdfContentByte getDuplicate() {
        PdfContentByte cb = new PdfContentByte(this.writer);
        cb.duplicatedFrom = this;
        return cb;
    }

    public PdfContentByte getDuplicate(boolean inheritGraphicState) {
        PdfContentByte cb = getDuplicate();
        if (inheritGraphicState) {
            cb.state = this.state;
            cb.stateList = this.stateList;
        }
        return cb;
    }

    public void inheritGraphicState(PdfContentByte parentCanvas) {
        this.state = parentCanvas.state;
        this.stateList = parentCanvas.stateList;
    }

    public void remoteGoto(String filename, String name, float llx, float lly, float urx, float ury) {
        this.pdf.remoteGoto(filename, name, llx, lly, urx, ury);
    }

    public void remoteGoto(String filename, int page, float llx, float lly, float urx, float ury) {
        this.pdf.remoteGoto(filename, page, llx, lly, urx, ury);
    }

    public void roundRectangle(float x, float y, float w, float h, float r) {
        if (w < 0.0f) {
            x += w;
            w = -w;
        }
        if (h < 0.0f) {
            y += h;
            h = -h;
        }
        if (r < 0.0f) {
            r = -r;
        }
        moveTo(x + r, y);
        lineTo((x + w) - r, y);
        curveTo((x + w) - (r * 0.4477f), y, x + w, y + (r * 0.4477f), x + w, y + r);
        lineTo(x + w, (y + h) - r);
        curveTo(x + w, (y + h) - (r * 0.4477f), (x + w) - (r * 0.4477f), y + h, (x + w) - r, y + h);
        lineTo(x + r, y + h);
        curveTo(x + (r * 0.4477f), y + h, x, (y + h) - (r * 0.4477f), x, (y + h) - r);
        lineTo(x, y + r);
        curveTo(x, y + (r * 0.4477f), x + (r * 0.4477f), y, x + r, y);
    }

    public void setAction(PdfAction action, float llx, float lly, float urx, float ury) {
        this.pdf.setAction(action, llx, lly, urx, ury);
    }

    public void setLiteral(String s) {
        this.content.append(s);
    }

    public void setLiteral(char c) {
        this.content.append(c);
    }

    public void setLiteral(float n) {
        this.content.append(n);
    }

    void checkNoPattern(PdfTemplate t) {
        if (t.getType() == 3) {
            throw new RuntimeException(MessageLocalization.getComposedMessage("invalid.use.of.a.pattern.a.template.was.expected", new Object[0]));
        }
    }

    public void drawRadioField(float llx, float lly, float urx, float ury, boolean on) {
        if (llx > urx) {
            float x = llx;
            llx = urx;
            urx = x;
        }
        if (lly > ury) {
            float y = lly;
            lly = ury;
            ury = y;
        }
        saveState();
        setLineWidth(BaseField.BORDER_WIDTH_THIN);
        setLineCap(1);
        setColorStroke(new BaseColor(192, 192, 192));
        arc(llx + BaseField.BORDER_WIDTH_THIN, lly + BaseField.BORDER_WIDTH_THIN, urx - BaseField.BORDER_WIDTH_THIN, ury - BaseField.BORDER_WIDTH_THIN, 0.0f, 360.0f);
        stroke();
        setLineWidth(BaseField.BORDER_WIDTH_THIN);
        setLineCap(1);
        setColorStroke(new BaseColor(160, 160, 160));
        arc(llx + 0.5f, lly + 0.5f, urx - 0.5f, ury - 0.5f, 45.0f, 180.0f);
        stroke();
        setLineWidth(BaseField.BORDER_WIDTH_THIN);
        setLineCap(1);
        setColorStroke(new BaseColor(0, 0, 0));
        arc(llx + 1.5f, lly + 1.5f, urx - 1.5f, ury - 1.5f, 45.0f, 180.0f);
        stroke();
        if (on) {
            setLineWidth(BaseField.BORDER_WIDTH_THIN);
            setLineCap(1);
            setColorFill(new BaseColor(0, 0, 0));
            arc(llx + 4.0f, lly + 4.0f, urx - 4.0f, ury - 4.0f, 0.0f, 360.0f);
            fill();
        }
        restoreState();
    }

    public void drawTextField(float llx, float lly, float urx, float ury) {
        if (llx > urx) {
            float x = llx;
            llx = urx;
            urx = x;
        }
        if (lly > ury) {
            float y = lly;
            lly = ury;
            ury = y;
        }
        saveState();
        setColorStroke(new BaseColor(192, 192, 192));
        setLineWidth(BaseField.BORDER_WIDTH_THIN);
        setLineCap(0);
        rectangle(llx, lly, urx - llx, ury - lly);
        stroke();
        setLineWidth(BaseField.BORDER_WIDTH_THIN);
        setLineCap(0);
        setColorFill(new BaseColor(255, 255, 255));
        rectangle(0.5f + llx, 0.5f + lly, (urx - llx) - BaseField.BORDER_WIDTH_THIN, (ury - lly) - BaseField.BORDER_WIDTH_THIN);
        fill();
        setColorStroke(new BaseColor(192, 192, 192));
        setLineWidth(BaseField.BORDER_WIDTH_THIN);
        setLineCap(0);
        moveTo(llx + BaseField.BORDER_WIDTH_THIN, lly + 1.5f);
        lineTo(urx - 1.5f, lly + 1.5f);
        lineTo(urx - 1.5f, ury - BaseField.BORDER_WIDTH_THIN);
        stroke();
        setColorStroke(new BaseColor(160, 160, 160));
        setLineWidth(BaseField.BORDER_WIDTH_THIN);
        setLineCap(0);
        moveTo(llx + BaseField.BORDER_WIDTH_THIN, lly + BaseField.BORDER_WIDTH_THIN);
        lineTo(llx + BaseField.BORDER_WIDTH_THIN, ury - BaseField.BORDER_WIDTH_THIN);
        lineTo(urx - BaseField.BORDER_WIDTH_THIN, ury - BaseField.BORDER_WIDTH_THIN);
        stroke();
        setColorStroke(new BaseColor(0, 0, 0));
        setLineWidth(BaseField.BORDER_WIDTH_THIN);
        setLineCap(0);
        moveTo(llx + BaseField.BORDER_WIDTH_MEDIUM, lly + BaseField.BORDER_WIDTH_MEDIUM);
        lineTo(llx + BaseField.BORDER_WIDTH_MEDIUM, ury - BaseField.BORDER_WIDTH_MEDIUM);
        lineTo(urx - BaseField.BORDER_WIDTH_MEDIUM, ury - BaseField.BORDER_WIDTH_MEDIUM);
        stroke();
        restoreState();
    }

    public void drawButton(float llx, float lly, float urx, float ury, String text, BaseFont bf, float size) {
        if (llx > urx) {
            float x = llx;
            llx = urx;
            urx = x;
        }
        if (lly > ury) {
            float y = lly;
            lly = ury;
            ury = y;
        }
        saveState();
        setColorStroke(new BaseColor(0, 0, 0));
        setLineWidth(BaseField.BORDER_WIDTH_THIN);
        setLineCap(0);
        rectangle(llx, lly, urx - llx, ury - lly);
        stroke();
        setLineWidth(BaseField.BORDER_WIDTH_THIN);
        setLineCap(0);
        setColorFill(new BaseColor(192, 192, 192));
        rectangle(0.5f + llx, 0.5f + lly, (urx - llx) - BaseField.BORDER_WIDTH_THIN, (ury - lly) - BaseField.BORDER_WIDTH_THIN);
        fill();
        setColorStroke(new BaseColor(255, 255, 255));
        setLineWidth(BaseField.BORDER_WIDTH_THIN);
        setLineCap(0);
        moveTo(BaseField.BORDER_WIDTH_THIN + llx, BaseField.BORDER_WIDTH_THIN + lly);
        lineTo(BaseField.BORDER_WIDTH_THIN + llx, ury - BaseField.BORDER_WIDTH_THIN);
        lineTo(urx - BaseField.BORDER_WIDTH_THIN, ury - BaseField.BORDER_WIDTH_THIN);
        stroke();
        setColorStroke(new BaseColor(160, 160, 160));
        setLineWidth(BaseField.BORDER_WIDTH_THIN);
        setLineCap(0);
        moveTo(BaseField.BORDER_WIDTH_THIN + llx, BaseField.BORDER_WIDTH_THIN + lly);
        lineTo(urx - BaseField.BORDER_WIDTH_THIN, BaseField.BORDER_WIDTH_THIN + lly);
        lineTo(urx - BaseField.BORDER_WIDTH_THIN, ury - BaseField.BORDER_WIDTH_THIN);
        stroke();
        resetRGBColorFill();
        beginText();
        setFontAndSize(bf, size);
        showTextAligned(1, text, llx + ((urx - llx) / BaseField.BORDER_WIDTH_MEDIUM), lly + (((ury - lly) - size) / BaseField.BORDER_WIDTH_MEDIUM), 0.0f);
        endText();
        restoreState();
    }

    PageResources getPageResources() {
        return this.pdf.getPageResources();
    }

    public void setGState(PdfGState gstate) {
        PdfObject[] obj = this.writer.addSimpleExtGState(gstate);
        PdfName name = getPageResources().addExtGState((PdfName) obj[0], (PdfIndirectReference) obj[1]);
        this.state.extGState = gstate;
        this.content.append(name.getBytes()).append(" gs").append_i(this.separator);
    }

    public void beginLayer(PdfOCG layer) {
        if (!(layer instanceof PdfLayer) || ((PdfLayer) layer).getTitle() == null) {
            if (this.layerDepth == null) {
                this.layerDepth = new ArrayList();
            }
            if (layer instanceof PdfLayerMembership) {
                this.layerDepth.add(Integer.valueOf(1));
                beginLayer2(layer);
                return;
            }
            int n = 0;
            for (PdfLayer la = (PdfLayer) layer; la != null; la = la.getParent()) {
                if (la.getTitle() == null) {
                    beginLayer2(la);
                    n++;
                }
            }
            this.layerDepth.add(Integer.valueOf(n));
            return;
        }
        throw new IllegalArgumentException(MessageLocalization.getComposedMessage("a.title.is.not.a.layer", new Object[0]));
    }

    private void beginLayer2(PdfOCG layer) {
        this.content.append("/OC ").append(getPageResources().addProperty(this.writer.addSimpleProperty(layer, layer.getRef())[0], layer.getRef()).getBytes()).append(" BDC").append_i(this.separator);
    }

    public void endLayer() {
        if (this.layerDepth == null || this.layerDepth.isEmpty()) {
            throw new IllegalPdfSyntaxException(MessageLocalization.getComposedMessage("unbalanced.layer.operators", new Object[0]));
        }
        int n = ((Integer) this.layerDepth.get(this.layerDepth.size() - 1)).intValue();
        this.layerDepth.remove(this.layerDepth.size() - 1);
        int n2 = n;
        while (true) {
            n = n2 - 1;
            if (n2 > 0) {
                this.content.append("EMC").append_i(this.separator);
                n2 = n;
            } else {
                return;
            }
        }
    }

    public void transform(AffineTransform af) {
        if (this.inText && isTagged()) {
            endText();
        }
        double[] matrix = new double[6];
        af.getMatrix(matrix);
        this.state.CTM.concatenate(af);
        this.content.append(matrix[0]).append(' ').append(matrix[1]).append(' ').append(matrix[2]).append(' ');
        this.content.append(matrix[3]).append(' ').append(matrix[4]).append(' ').append(matrix[5]).append(" cm").append_i(this.separator);
    }

    void addAnnotation(PdfAnnotation annot) {
        boolean needToTag = isTagged() && annot.getRole() != null && (!(annot instanceof PdfFormField) || ((PdfFormField) annot).getKids() == null);
        if (needToTag) {
            openMCBlock(annot);
        }
        this.writer.addAnnotation(annot);
        if (needToTag) {
            PdfStructureElement strucElem = (PdfStructureElement) this.pdf.structElements.get(annot.getId());
            if (strucElem != null) {
                int structParent = this.pdf.getStructParentIndex(annot);
                annot.put(PdfName.STRUCTPARENT, new PdfNumber(structParent));
                strucElem.setAnnotation(annot, getCurrentPage());
                this.writer.getStructureTreeRoot().setAnnotationMark(structParent, strucElem.getReference());
            }
            closeMCBlock(annot);
        }
    }

    public void addAnnotation(PdfAnnotation annot, boolean applyCTM) {
        if (applyCTM && this.state.CTM.getType() != 0) {
            annot.applyCTM(this.state.CTM);
        }
        addAnnotation(annot);
    }

    public void setDefaultColorspace(PdfName name, PdfObject obj) {
        getPageResources().addDefaultColor(name, obj);
    }

    public void beginMarkedContentSequence(PdfStructureElement struc) {
        PdfObject obj = struc.get(PdfName.f125K);
        int[] structParentMarkPoint = this.pdf.getStructParentIndexAndNextMarkPoint(getCurrentPage());
        int structParent = structParentMarkPoint[0];
        int mark = structParentMarkPoint[1];
        if (obj != null) {
            PdfArray ar;
            if (obj.isNumber()) {
                ar = new PdfArray();
                ar.add(obj);
                struc.put(PdfName.f125K, ar);
            } else if (obj.isArray()) {
                ar = (PdfArray) obj;
            } else {
                throw new IllegalArgumentException(MessageLocalization.getComposedMessage("unknown.object.at.k.1", obj.getClass().toString()));
            }
            if (ar.getAsNumber(0) != null) {
                PdfDictionary dic = new PdfDictionary(PdfName.MCR);
                dic.put(PdfName.PG, getCurrentPage());
                dic.put(PdfName.MCID, new PdfNumber(mark));
                ar.add(dic);
            }
            struc.setPageMark(this.pdf.getStructParentIndex(getCurrentPage()), -1);
        } else {
            struc.setPageMark(structParent, mark);
            struc.put(PdfName.PG, getCurrentPage());
        }
        setMcDepth(getMcDepth() + 1);
        int contentSize = this.content.size();
        this.content.append(struc.get(PdfName.f133S).getBytes()).append(" <</MCID ").append(mark).append(">> BDC").append_i(this.separator);
        this.markedContentSize += this.content.size() - contentSize;
    }

    protected PdfIndirectReference getCurrentPage() {
        return this.writer.getCurrentPage();
    }

    public void endMarkedContentSequence() {
        if (getMcDepth() == 0) {
            throw new IllegalPdfSyntaxException(MessageLocalization.getComposedMessage("unbalanced.begin.end.marked.content.operators", new Object[0]));
        }
        int contentSize = this.content.size();
        setMcDepth(getMcDepth() - 1);
        this.content.append("EMC").append_i(this.separator);
        this.markedContentSize += this.content.size() - contentSize;
    }

    public void beginMarkedContentSequence(PdfName tag, PdfDictionary property, boolean inline) {
        int contentSize = this.content.size();
        if (property == null) {
            this.content.append(tag.getBytes()).append(" BMC").append_i(this.separator);
            setMcDepth(getMcDepth() + 1);
        } else {
            this.content.append(tag.getBytes()).append(' ');
            if (inline) {
                try {
                    property.toPdf(this.writer, this.content);
                } catch (Exception e) {
                    throw new ExceptionConverter(e);
                }
            }
            PdfObject[] objs;
            if (this.writer.propertyExists(property)) {
                objs = this.writer.addSimpleProperty(property, null);
            } else {
                objs = this.writer.addSimpleProperty(property, this.writer.getPdfIndirectReference());
            }
            this.content.append(getPageResources().addProperty(objs[0], (PdfIndirectReference) objs[1]).getBytes());
            this.content.append(" BDC").append_i(this.separator);
            setMcDepth(getMcDepth() + 1);
        }
        this.markedContentSize += this.content.size() - contentSize;
    }

    public void beginMarkedContentSequence(PdfName tag) {
        beginMarkedContentSequence(tag, null, false);
    }

    public void sanityCheck() {
        if (getMcDepth() != 0) {
            throw new IllegalPdfSyntaxException(MessageLocalization.getComposedMessage("unbalanced.marked.content.operators", new Object[0]));
        }
        if (this.inText) {
            if (isTagged()) {
                endText();
            } else {
                throw new IllegalPdfSyntaxException(MessageLocalization.getComposedMessage("unbalanced.begin.end.text.operators", new Object[0]));
            }
        }
        if (this.layerDepth != null && !this.layerDepth.isEmpty()) {
            throw new IllegalPdfSyntaxException(MessageLocalization.getComposedMessage("unbalanced.layer.operators", new Object[0]));
        } else if (!this.stateList.isEmpty()) {
            throw new IllegalPdfSyntaxException(MessageLocalization.getComposedMessage("unbalanced.save.restore.state.operators", new Object[0]));
        }
    }

    public void openMCBlock(IAccessibleElement element) {
        if (isTagged()) {
            if (this.pdf.openMCDocument) {
                this.pdf.openMCDocument = false;
                this.writer.getDirectContentUnder().openMCBlock(this.pdf);
            }
            if (element != null && !getMcElements().contains(element)) {
                PdfStructureElement structureElement = openMCBlockInt(element);
                getMcElements().add(element);
                if (structureElement != null) {
                    this.pdf.structElements.put(element.getId(), structureElement);
                }
            }
        }
    }

    private PdfDictionary getParentStructureElement() {
        PdfDictionary parent = null;
        if (getMcElements().size() > 0) {
            parent = (PdfDictionary) this.pdf.structElements.get(((IAccessibleElement) getMcElements().get(getMcElements().size() - 1)).getId());
        }
        if (parent == null) {
            return this.writer.getStructureTreeRoot();
        }
        return parent;
    }

    private PdfStructureElement openMCBlockInt(IAccessibleElement element) {
        PdfStructureElement structureElement = null;
        if (isTagged()) {
            IAccessibleElement parent = null;
            if (getMcElements().size() > 0) {
                parent = (IAccessibleElement) getMcElements().get(getMcElements().size() - 1);
            }
            this.writer.checkElementRole(element, parent);
            if (element.getRole() != null) {
                if (!PdfName.ARTIFACT.equals(element.getRole())) {
                    structureElement = (PdfStructureElement) this.pdf.structElements.get(element.getId());
                    if (structureElement == null) {
                        structureElement = new PdfStructureElement(getParentStructureElement(), element.getRole());
                    }
                }
                boolean inTextLocal;
                if (PdfName.ARTIFACT.equals(element.getRole())) {
                    HashMap<PdfName, PdfObject> properties = element.getAccessibleAttributes();
                    PdfDictionary propertiesDict = null;
                    if (!(properties == null || properties.isEmpty())) {
                        propertiesDict = new PdfDictionary();
                        for (Entry<PdfName, PdfObject> entry : properties.entrySet()) {
                            propertiesDict.put((PdfName) entry.getKey(), (PdfObject) entry.getValue());
                        }
                    }
                    inTextLocal = this.inText;
                    if (this.inText) {
                        endText();
                    }
                    beginMarkedContentSequence(element.getRole(), propertiesDict, true);
                    if (inTextLocal) {
                        beginText(true);
                    }
                } else if (this.writer.needToBeMarkedInContent(element)) {
                    inTextLocal = this.inText;
                    if (this.inText) {
                        endText();
                    }
                    beginMarkedContentSequence(structureElement);
                    if (inTextLocal) {
                        beginText(true);
                    }
                }
            }
        }
        return structureElement;
    }

    public void closeMCBlock(IAccessibleElement element) {
        if (isTagged() && element != null && getMcElements().contains(element)) {
            closeMCBlockInt(element);
            getMcElements().remove(element);
        }
    }

    private void closeMCBlockInt(IAccessibleElement element) {
        if (isTagged() && element.getRole() != null) {
            PdfStructureElement structureElement = (PdfStructureElement) this.pdf.structElements.get(element.getId());
            if (structureElement != null) {
                structureElement.writeAttributes(element);
            }
            if (this.writer.needToBeMarkedInContent(element)) {
                boolean inTextLocal = this.inText;
                if (this.inText) {
                    endText();
                }
                endMarkedContentSequence();
                if (inTextLocal) {
                    beginText(true);
                }
            }
        }
    }

    protected ArrayList<IAccessibleElement> saveMCBlocks() {
        ArrayList<IAccessibleElement> mc = new ArrayList();
        if (isTagged()) {
            mc = getMcElements();
            for (int i = 0; i < mc.size(); i++) {
                closeMCBlockInt((IAccessibleElement) mc.get(i));
            }
            setMcElements(new ArrayList());
        }
        return mc;
    }

    protected void restoreMCBlocks(ArrayList<IAccessibleElement> mcElements) {
        if (isTagged() && mcElements != null) {
            setMcElements(mcElements);
            for (int i = 0; i < getMcElements().size(); i++) {
                openMCBlockInt((IAccessibleElement) getMcElements().get(i));
            }
        }
    }

    protected int getMcDepth() {
        if (this.duplicatedFrom != null) {
            return this.duplicatedFrom.getMcDepth();
        }
        return this.mcDepth;
    }

    protected void setMcDepth(int value) {
        if (this.duplicatedFrom != null) {
            this.duplicatedFrom.setMcDepth(value);
        } else {
            this.mcDepth = value;
        }
    }

    protected ArrayList<IAccessibleElement> getMcElements() {
        if (this.duplicatedFrom != null) {
            return this.duplicatedFrom.getMcElements();
        }
        return this.mcElements;
    }

    protected void setMcElements(ArrayList<IAccessibleElement> value) {
        if (this.duplicatedFrom != null) {
            this.duplicatedFrom.setMcElements(value);
        } else {
            this.mcElements = value;
        }
    }

    protected void updateTx(String text, float Tj) {
        GraphicState graphicState = this.state;
        graphicState.tx += getEffectiveStringWidth(text, false, Tj);
    }

    private void saveColor(BaseColor color, boolean fill) {
        if (isTagged()) {
            if (this.inText) {
                if (fill) {
                    this.state.textColorFill = color;
                } else {
                    this.state.textColorStroke = color;
                }
            } else if (fill) {
                this.state.colorFill = color;
            } else {
                this.state.colorStroke = color;
            }
        } else if (fill) {
            this.state.colorFill = color;
        } else {
            this.state.colorStroke = color;
        }
    }

    private void restoreColor(BaseColor color, boolean fill) throws IOException {
        if (!isTagged()) {
            return;
        }
        if (color instanceof UncoloredPattern) {
            UncoloredPattern c = (UncoloredPattern) color;
            if (fill) {
                setPatternFill(c.getPainter(), c.color, c.tint);
            } else {
                setPatternStroke(c.getPainter(), c.color, c.tint);
            }
        } else if (fill) {
            setColorFill(color);
        } else {
            setColorStroke(color);
        }
    }

    private void restoreColor() throws IOException {
        if (!isTagged()) {
            return;
        }
        if (this.inText) {
            if (!this.state.textColorFill.equals(this.state.colorFill)) {
                restoreColor(this.state.textColorFill, true);
            }
            if (!this.state.textColorStroke.equals(this.state.colorStroke)) {
                restoreColor(this.state.textColorStroke, false);
                return;
            }
            return;
        }
        if (!this.state.textColorFill.equals(this.state.colorFill)) {
            restoreColor(this.state.colorFill, true);
        }
        if (!this.state.textColorStroke.equals(this.state.colorStroke)) {
            restoreColor(this.state.colorStroke, false);
        }
    }

    protected boolean getInText() {
        return this.inText;
    }

    protected void checkState() {
        boolean stroke = false;
        boolean fill = false;
        if (this.state.textRenderMode == 0) {
            fill = true;
        } else if (this.state.textRenderMode == 1) {
            stroke = true;
        } else if (this.state.textRenderMode == 2) {
            fill = true;
            stroke = true;
        }
        if (fill) {
            PdfWriter.checkPdfIsoConformance(this.writer, 1, isTagged() ? this.state.textColorFill : this.state.colorFill);
        }
        if (stroke) {
            PdfWriter.checkPdfIsoConformance(this.writer, 1, isTagged() ? this.state.textColorStroke : this.state.colorStroke);
        }
        PdfWriter.checkPdfIsoConformance(this.writer, 6, this.state.extGState);
    }
}
