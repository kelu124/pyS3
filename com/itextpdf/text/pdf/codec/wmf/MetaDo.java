package com.itextpdf.text.pdf.codec.wmf;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.error_messages.MessageLocalization;
import com.itextpdf.text.pdf.BaseField;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.codec.BmpImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import org.bytedeco.javacpp.opencv_core;

public class MetaDo {
    public static final int META_ANIMATEPALETTE = 1078;
    public static final int META_ARC = 2071;
    public static final int META_BITBLT = 2338;
    public static final int META_CHORD = 2096;
    public static final int META_CREATEBRUSHINDIRECT = 764;
    public static final int META_CREATEFONTINDIRECT = 763;
    public static final int META_CREATEPALETTE = 247;
    public static final int META_CREATEPATTERNBRUSH = 505;
    public static final int META_CREATEPENINDIRECT = 762;
    public static final int META_CREATEREGION = 1791;
    public static final int META_DELETEOBJECT = 496;
    public static final int META_DIBBITBLT = 2368;
    public static final int META_DIBCREATEPATTERNBRUSH = 322;
    public static final int META_DIBSTRETCHBLT = 2881;
    public static final int META_ELLIPSE = 1048;
    public static final int META_ESCAPE = 1574;
    public static final int META_EXCLUDECLIPRECT = 1045;
    public static final int META_EXTFLOODFILL = 1352;
    public static final int META_EXTTEXTOUT = 2610;
    public static final int META_FILLREGION = 552;
    public static final int META_FLOODFILL = 1049;
    public static final int META_FRAMEREGION = 1065;
    public static final int META_INTERSECTCLIPRECT = 1046;
    public static final int META_INVERTREGION = 298;
    public static final int META_LINETO = 531;
    public static final int META_MOVETO = 532;
    public static final int META_OFFSETCLIPRGN = 544;
    public static final int META_OFFSETVIEWPORTORG = 529;
    public static final int META_OFFSETWINDOWORG = 527;
    public static final int META_PAINTREGION = 299;
    public static final int META_PATBLT = 1565;
    public static final int META_PIE = 2074;
    public static final int META_POLYGON = 804;
    public static final int META_POLYLINE = 805;
    public static final int META_POLYPOLYGON = 1336;
    public static final int META_REALIZEPALETTE = 53;
    public static final int META_RECTANGLE = 1051;
    public static final int META_RESIZEPALETTE = 313;
    public static final int META_RESTOREDC = 295;
    public static final int META_ROUNDRECT = 1564;
    public static final int META_SAVEDC = 30;
    public static final int META_SCALEVIEWPORTEXT = 1042;
    public static final int META_SCALEWINDOWEXT = 1040;
    public static final int META_SELECTCLIPREGION = 300;
    public static final int META_SELECTOBJECT = 301;
    public static final int META_SELECTPALETTE = 564;
    public static final int META_SETBKCOLOR = 513;
    public static final int META_SETBKMODE = 258;
    public static final int META_SETDIBTODEV = 3379;
    public static final int META_SETMAPMODE = 259;
    public static final int META_SETMAPPERFLAGS = 561;
    public static final int META_SETPALENTRIES = 55;
    public static final int META_SETPIXEL = 1055;
    public static final int META_SETPOLYFILLMODE = 262;
    public static final int META_SETRELABS = 261;
    public static final int META_SETROP2 = 260;
    public static final int META_SETSTRETCHBLTMODE = 263;
    public static final int META_SETTEXTALIGN = 302;
    public static final int META_SETTEXTCHAREXTRA = 264;
    public static final int META_SETTEXTCOLOR = 521;
    public static final int META_SETTEXTJUSTIFICATION = 522;
    public static final int META_SETVIEWPORTEXT = 526;
    public static final int META_SETVIEWPORTORG = 525;
    public static final int META_SETWINDOWEXT = 524;
    public static final int META_SETWINDOWORG = 523;
    public static final int META_STRETCHBLT = 2851;
    public static final int META_STRETCHDIB = 3907;
    public static final int META_TEXTOUT = 1313;
    int bottom;
    public PdfContentByte cb;
    public InputMeta in;
    int inch;
    int left;
    int right;
    MetaState state = new MetaState();
    int top;

    public MetaDo(InputStream in, PdfContentByte cb) {
        this.cb = cb;
        this.in = new InputMeta(in);
    }

    public void readAll() throws IOException, DocumentException {
        if (this.in.readInt() != -1698247209) {
            throw new DocumentException(MessageLocalization.getComposedMessage("not.a.placeable.windows.metafile", new Object[0]));
        }
        this.in.readWord();
        this.left = this.in.readShort();
        this.top = this.in.readShort();
        this.right = this.in.readShort();
        this.bottom = this.in.readShort();
        this.inch = this.in.readWord();
        this.state.setScalingX((((float) (this.right - this.left)) / ((float) this.inch)) * 72.0f);
        this.state.setScalingY((((float) (this.bottom - this.top)) / ((float) this.inch)) * 72.0f);
        this.state.setOffsetWx(this.left);
        this.state.setOffsetWy(this.top);
        this.state.setExtentWx(this.right - this.left);
        this.state.setExtentWy(this.bottom - this.top);
        this.in.readInt();
        this.in.readWord();
        this.in.skip(18);
        this.cb.setLineCap(1);
        this.cb.setLineJoin(1);
        while (true) {
            int lenMarker = this.in.getLength();
            int tsize = this.in.readInt();
            if (tsize < 3) {
                this.state.cleanup(this.cb);
                return;
            }
            int function = this.in.readWord();
            int y;
            int x;
            int len;
            int sx;
            int sy;
            int k;
            float b;
            float r;
            float t;
            float l;
            int count;
            byte[] text;
            byte c;
            String str;
            float yend;
            float xend;
            float ystart;
            float xstart;
            float cx;
            float cy;
            float arc1;
            float arc2;
            ArrayList<float[]> ar;
            float[] pt;
            switch (function) {
                case 30:
                    this.state.saveState(this.cb);
                    break;
                case META_CREATEPALETTE /*247*/:
                case 322:
                case META_CREATEREGION /*1791*/:
                    this.state.addMetaObject(new MetaObject());
                    break;
                case 258:
                    this.state.setBackgroundMode(this.in.readWord());
                    break;
                case 262:
                    this.state.setPolyFillMode(this.in.readWord());
                    break;
                case 295:
                    this.state.restoreState(this.in.readShort(), this.cb);
                    break;
                case 301:
                    this.state.selectMetaObject(this.in.readWord(), this.cb);
                    break;
                case 302:
                    this.state.setTextAlign(this.in.readWord());
                    break;
                case META_DELETEOBJECT /*496*/:
                    this.state.deleteMetaObject(this.in.readWord());
                    break;
                case 513:
                    this.state.setCurrentBackgroundColor(this.in.readColor());
                    break;
                case 521:
                    this.state.setCurrentTextColor(this.in.readColor());
                    break;
                case META_SETWINDOWORG /*523*/:
                    this.state.setOffsetWy(this.in.readShort());
                    this.state.setOffsetWx(this.in.readShort());
                    break;
                case META_SETWINDOWEXT /*524*/:
                    this.state.setExtentWy(this.in.readShort());
                    this.state.setExtentWx(this.in.readShort());
                    break;
                case 531:
                    y = this.in.readShort();
                    x = this.in.readShort();
                    Point p = this.state.getCurrentPoint();
                    this.cb.moveTo(this.state.transformX(p.f153x), this.state.transformY(p.f154y));
                    this.cb.lineTo(this.state.transformX(x), this.state.transformY(y));
                    this.cb.stroke();
                    this.state.setCurrentPoint(new Point(x, y));
                    break;
                case 532:
                    this.state.setCurrentPoint(new Point(this.in.readShort(), this.in.readShort()));
                    break;
                case META_CREATEPENINDIRECT /*762*/:
                    MetaObject pen = new MetaPen();
                    pen.init(this.in);
                    this.state.addMetaObject(pen);
                    break;
                case META_CREATEFONTINDIRECT /*763*/:
                    MetaObject font = new MetaFont();
                    font.init(this.in);
                    this.state.addMetaObject(font);
                    break;
                case META_CREATEBRUSHINDIRECT /*764*/:
                    MetaObject brush = new MetaBrush();
                    brush.init(this.in);
                    this.state.addMetaObject(brush);
                    break;
                case META_POLYGON /*804*/:
                    if (!isNullStrokeFill(false)) {
                        len = this.in.readWord();
                        sx = this.in.readShort();
                        sy = this.in.readShort();
                        this.cb.moveTo(this.state.transformX(sx), this.state.transformY(sy));
                        for (k = 1; k < len; k++) {
                            this.cb.lineTo(this.state.transformX(this.in.readShort()), this.state.transformY(this.in.readShort()));
                        }
                        this.cb.lineTo(this.state.transformX(sx), this.state.transformY(sy));
                        strokeAndFill();
                        break;
                    }
                    break;
                case META_POLYLINE /*805*/:
                    this.state.setLineJoinPolygon(this.cb);
                    len = this.in.readWord();
                    this.cb.moveTo(this.state.transformX(this.in.readShort()), this.state.transformY(this.in.readShort()));
                    for (k = 1; k < len; k++) {
                        this.cb.lineTo(this.state.transformX(this.in.readShort()), this.state.transformY(this.in.readShort()));
                    }
                    this.cb.stroke();
                    break;
                case META_INTERSECTCLIPRECT /*1046*/:
                    b = this.state.transformY(this.in.readShort());
                    r = this.state.transformX(this.in.readShort());
                    t = this.state.transformY(this.in.readShort());
                    l = this.state.transformX(this.in.readShort());
                    this.cb.rectangle(l, b, r - l, t - b);
                    this.cb.eoClip();
                    this.cb.newPath();
                    break;
                case META_ELLIPSE /*1048*/:
                    if (!isNullStrokeFill(this.state.getLineNeutral())) {
                        this.cb.arc(this.state.transformX(this.in.readShort()), this.state.transformY(this.in.readShort()), this.state.transformX(this.in.readShort()), this.state.transformY(this.in.readShort()), 0.0f, 360.0f);
                        strokeAndFill();
                        break;
                    }
                    break;
                case META_RECTANGLE /*1051*/:
                    if (!isNullStrokeFill(true)) {
                        b = this.state.transformY(this.in.readShort());
                        r = this.state.transformX(this.in.readShort());
                        t = this.state.transformY(this.in.readShort());
                        l = this.state.transformX(this.in.readShort());
                        this.cb.rectangle(l, b, r - l, t - b);
                        strokeAndFill();
                        break;
                    }
                    break;
                case META_SETPIXEL /*1055*/:
                    BaseColor color = this.in.readColor();
                    y = this.in.readShort();
                    x = this.in.readShort();
                    this.cb.saveState();
                    this.cb.setColorFill(color);
                    this.cb.rectangle(this.state.transformX(x), this.state.transformY(y), 0.2f, 0.2f);
                    this.cb.fill();
                    this.cb.restoreState();
                    break;
                case META_TEXTOUT /*1313*/:
                    count = this.in.readWord();
                    text = new byte[count];
                    k = 0;
                    while (k < count) {
                        c = (byte) this.in.readByte();
                        if (c != (byte) 0) {
                            text[k] = c;
                            k++;
                        }
                    }
                    try {
                        str = new String(text, 0, k, "Cp1252");
                    } catch (UnsupportedEncodingException e) {
                        str = new String(text, 0, k);
                    }
                    this.in.skip(((count + 1) & 65534) - k);
                    outputText(this.in.readShort(), this.in.readShort(), 0, 0, 0, 0, 0, s);
                    break;
                case META_POLYPOLYGON /*1336*/:
                    if (!isNullStrokeFill(false)) {
                        int[] lens = new int[this.in.readWord()];
                        for (k = 0; k < lens.length; k++) {
                            lens[k] = this.in.readWord();
                        }
                        for (int len2 : lens) {
                            sx = this.in.readShort();
                            sy = this.in.readShort();
                            this.cb.moveTo(this.state.transformX(sx), this.state.transformY(sy));
                            for (k = 1; k < len2; k++) {
                                this.cb.lineTo(this.state.transformX(this.in.readShort()), this.state.transformY(this.in.readShort()));
                            }
                            this.cb.lineTo(this.state.transformX(sx), this.state.transformY(sy));
                        }
                        strokeAndFill();
                        break;
                    }
                    break;
                case META_ROUNDRECT /*1564*/:
                    if (!isNullStrokeFill(true)) {
                        float h = this.state.transformY(0) - this.state.transformY(this.in.readShort());
                        float w = this.state.transformX(this.in.readShort()) - this.state.transformX(0);
                        b = this.state.transformY(this.in.readShort());
                        r = this.state.transformX(this.in.readShort());
                        t = this.state.transformY(this.in.readShort());
                        l = this.state.transformX(this.in.readShort());
                        this.cb.roundRectangle(l, b, r - l, t - b, (h + w) / 4.0f);
                        strokeAndFill();
                        break;
                    }
                    break;
                case META_ARC /*2071*/:
                    if (!isNullStrokeFill(this.state.getLineNeutral())) {
                        yend = this.state.transformY(this.in.readShort());
                        xend = this.state.transformX(this.in.readShort());
                        ystart = this.state.transformY(this.in.readShort());
                        xstart = this.state.transformX(this.in.readShort());
                        b = this.state.transformY(this.in.readShort());
                        r = this.state.transformX(this.in.readShort());
                        t = this.state.transformY(this.in.readShort());
                        l = this.state.transformX(this.in.readShort());
                        cx = (r + l) / BaseField.BORDER_WIDTH_MEDIUM;
                        cy = (t + b) / BaseField.BORDER_WIDTH_MEDIUM;
                        arc1 = getArc(cx, cy, xstart, ystart);
                        arc2 = getArc(cx, cy, xend, yend) - arc1;
                        if (arc2 <= 0.0f) {
                            arc2 += 360.0f;
                        }
                        this.cb.arc(l, b, r, t, arc1, arc2);
                        this.cb.stroke();
                        break;
                    }
                    break;
                case META_PIE /*2074*/:
                    if (!isNullStrokeFill(this.state.getLineNeutral())) {
                        yend = this.state.transformY(this.in.readShort());
                        xend = this.state.transformX(this.in.readShort());
                        ystart = this.state.transformY(this.in.readShort());
                        xstart = this.state.transformX(this.in.readShort());
                        b = this.state.transformY(this.in.readShort());
                        r = this.state.transformX(this.in.readShort());
                        t = this.state.transformY(this.in.readShort());
                        l = this.state.transformX(this.in.readShort());
                        cx = (r + l) / BaseField.BORDER_WIDTH_MEDIUM;
                        cy = (t + b) / BaseField.BORDER_WIDTH_MEDIUM;
                        arc1 = getArc(cx, cy, xstart, ystart);
                        arc2 = getArc(cx, cy, xend, yend) - arc1;
                        if (arc2 <= 0.0f) {
                            arc2 += 360.0f;
                        }
                        ar = PdfContentByte.bezierArc(l, b, r, t, arc1, arc2);
                        if (!ar.isEmpty()) {
                            pt = (float[]) ar.get(0);
                            this.cb.moveTo(cx, cy);
                            this.cb.lineTo(pt[0], pt[1]);
                            for (k = 0; k < ar.size(); k++) {
                                pt = (float[]) ar.get(k);
                                this.cb.curveTo(pt[2], pt[3], pt[4], pt[5], pt[6], pt[7]);
                            }
                            this.cb.lineTo(cx, cy);
                            strokeAndFill();
                            break;
                        }
                        break;
                    }
                    break;
                case META_CHORD /*2096*/:
                    if (!isNullStrokeFill(this.state.getLineNeutral())) {
                        yend = this.state.transformY(this.in.readShort());
                        xend = this.state.transformX(this.in.readShort());
                        ystart = this.state.transformY(this.in.readShort());
                        xstart = this.state.transformX(this.in.readShort());
                        b = this.state.transformY(this.in.readShort());
                        r = this.state.transformX(this.in.readShort());
                        t = this.state.transformY(this.in.readShort());
                        l = this.state.transformX(this.in.readShort());
                        cx = (r + l) / BaseField.BORDER_WIDTH_MEDIUM;
                        cy = (t + b) / BaseField.BORDER_WIDTH_MEDIUM;
                        arc1 = getArc(cx, cy, xstart, ystart);
                        arc2 = getArc(cx, cy, xend, yend) - arc1;
                        if (arc2 <= 0.0f) {
                            arc2 += 360.0f;
                        }
                        ar = PdfContentByte.bezierArc(l, b, r, t, arc1, arc2);
                        if (!ar.isEmpty()) {
                            pt = (float[]) ar.get(0);
                            cx = pt[0];
                            cy = pt[1];
                            this.cb.moveTo(cx, cy);
                            for (k = 0; k < ar.size(); k++) {
                                pt = (float[]) ar.get(k);
                                this.cb.curveTo(pt[2], pt[3], pt[4], pt[5], pt[6], pt[7]);
                            }
                            this.cb.lineTo(cx, cy);
                            strokeAndFill();
                            break;
                        }
                        break;
                    }
                    break;
                case META_EXTTEXTOUT /*2610*/:
                    y = this.in.readShort();
                    x = this.in.readShort();
                    count = this.in.readWord();
                    int flag = this.in.readWord();
                    int x1 = 0;
                    int y1 = 0;
                    int x2 = 0;
                    int y2 = 0;
                    if ((flag & 6) != 0) {
                        x1 = this.in.readShort();
                        y1 = this.in.readShort();
                        x2 = this.in.readShort();
                        y2 = this.in.readShort();
                    }
                    text = new byte[count];
                    k = 0;
                    while (k < count) {
                        c = (byte) this.in.readByte();
                        if (c != (byte) 0) {
                            text[k] = c;
                            k++;
                        }
                    }
                    try {
                        str = new String(text, 0, k, "Cp1252");
                    } catch (UnsupportedEncodingException e2) {
                        str = new String(text, 0, k);
                    }
                    outputText(x, y, flag, x1, y1, x2, y2, s);
                    break;
                case META_DIBSTRETCHBLT /*2881*/:
                case META_STRETCHDIB /*3907*/:
                    int rop = this.in.readInt();
                    if (function == 3907) {
                        this.in.readWord();
                    }
                    int srcHeight = this.in.readShort();
                    int srcWidth = this.in.readShort();
                    int ySrc = this.in.readShort();
                    int xSrc = this.in.readShort();
                    float destHeight = this.state.transformY(this.in.readShort()) - this.state.transformY(0);
                    float destWidth = this.state.transformX(this.in.readShort()) - this.state.transformX(0);
                    float yDest = this.state.transformY(this.in.readShort());
                    float xDest = this.state.transformX(this.in.readShort());
                    byte[] b2 = new byte[((tsize * 2) - (this.in.getLength() - lenMarker))];
                    for (k = 0; k < b2.length; k++) {
                        b2[k] = (byte) this.in.readByte();
                    }
                    try {
                        Image bmp = BmpImage.getImage(new ByteArrayInputStream(b2), true, b2.length);
                        this.cb.saveState();
                        this.cb.rectangle(xDest, yDest, destWidth, destHeight);
                        this.cb.clip();
                        this.cb.newPath();
                        bmp.scaleAbsolute((bmp.getWidth() * destWidth) / ((float) srcWidth), ((-destHeight) * bmp.getHeight()) / ((float) srcHeight));
                        bmp.setAbsolutePosition(xDest - ((((float) xSrc) * destWidth) / ((float) srcWidth)), (((((float) ySrc) * destHeight) / ((float) srcHeight)) + yDest) - bmp.getScaledHeight());
                        this.cb.addImage(bmp);
                        this.cb.restoreState();
                        break;
                    } catch (Exception e3) {
                        break;
                    }
                default:
                    break;
            }
            this.in.skip((tsize * 2) - (this.in.getLength() - lenMarker));
        }
    }

    public void outputText(int x, int y, int flag, int x1, int y1, int x2, int y2, String text) {
        float ty;
        MetaFont font = this.state.getCurrentFont();
        float refX = this.state.transformX(x);
        float refY = this.state.transformY(y);
        float angle = this.state.transformAngle(font.getAngle());
        float sin = (float) Math.sin((double) angle);
        float cos = (float) Math.cos((double) angle);
        float fontSize = font.getFontSize(this.state);
        BaseFont bf = font.getFont();
        int align = this.state.getTextAlign();
        float textWidth = bf.getWidthPoint(text, fontSize);
        float tx = 0.0f;
        float descender = bf.getFontDescriptor(3, fontSize);
        float ury = bf.getFontDescriptor(8, fontSize);
        this.cb.saveState();
        this.cb.concatCTM(cos, sin, -sin, cos, refX, refY);
        if ((align & 6) == 6) {
            tx = (-textWidth) / BaseField.BORDER_WIDTH_MEDIUM;
        } else if ((align & 2) == 2) {
            tx = -textWidth;
        }
        if ((align & 24) == 24) {
            ty = 0.0f;
        } else if ((align & 8) == 8) {
            ty = -descender;
        } else {
            ty = -ury;
        }
        if (this.state.getBackgroundMode() == 2) {
            this.cb.setColorFill(this.state.getCurrentBackgroundColor());
            this.cb.rectangle(tx, ty + descender, textWidth, ury - descender);
            this.cb.fill();
        }
        this.cb.setColorFill(this.state.getCurrentTextColor());
        this.cb.beginText();
        this.cb.setFontAndSize(bf, fontSize);
        this.cb.setTextMatrix(tx, ty);
        this.cb.showText(text);
        this.cb.endText();
        if (font.isUnderline()) {
            this.cb.rectangle(tx, ty - (fontSize / 4.0f), textWidth, fontSize / 15.0f);
            this.cb.fill();
        }
        if (font.isStrikeout()) {
            this.cb.rectangle(tx, (fontSize / BaseField.BORDER_WIDTH_THICK) + ty, textWidth, fontSize / 15.0f);
            this.cb.fill();
        }
        this.cb.restoreState();
    }

    public boolean isNullStrokeFill(boolean isRectangle) {
        boolean noPen;
        boolean result;
        MetaPen pen = this.state.getCurrentPen();
        MetaBrush brush = this.state.getCurrentBrush();
        if (pen.getStyle() == 5) {
            noPen = true;
        } else {
            noPen = false;
        }
        int style = brush.getStyle();
        boolean isBrush;
        if (style == 0 || (style == 2 && this.state.getBackgroundMode() == 2)) {
            isBrush = true;
        } else {
            isBrush = false;
        }
        if (!noPen || isBrush) {
            result = false;
        } else {
            result = true;
        }
        if (!noPen) {
            if (isRectangle) {
                this.state.setLineJoinRectangle(this.cb);
            } else {
                this.state.setLineJoinPolygon(this.cb);
            }
        }
        return result;
    }

    public void strokeAndFill() {
        MetaPen pen = this.state.getCurrentPen();
        MetaBrush brush = this.state.getCurrentBrush();
        int penStyle = pen.getStyle();
        int brushStyle = brush.getStyle();
        if (penStyle == 5) {
            this.cb.closePath();
            if (this.state.getPolyFillMode() == 1) {
                this.cb.eoFill();
                return;
            } else {
                this.cb.fill();
                return;
            }
        }
        boolean isBrush = brushStyle == 0 || (brushStyle == 2 && this.state.getBackgroundMode() == 2);
        if (!isBrush) {
            this.cb.closePathStroke();
        } else if (this.state.getPolyFillMode() == 1) {
            this.cb.closePathEoFillStroke();
        } else {
            this.cb.closePathFillStroke();
        }
    }

    static float getArc(float xCenter, float yCenter, float xDot, float yDot) {
        double s = Math.atan2((double) (yDot - yCenter), (double) (xDot - xCenter));
        if (s < 0.0d) {
            s += opencv_core.CV_2PI;
        }
        return (float) ((s / 3.141592653589793d) * 180.0d);
    }

    public static byte[] wrapBMP(Image image) throws IOException {
        if (image.getOriginalType() != 4) {
            throw new IOException(MessageLocalization.getComposedMessage("only.bmp.can.be.wrapped.in.wmf", new Object[0]));
        }
        byte[] data;
        if (image.getOriginalData() == null) {
            InputStream imgIn = image.getUrl().openStream();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            while (true) {
                int b = imgIn.read();
                if (b == -1) {
                    break;
                }
                out.write(b);
            }
            imgIn.close();
            data = out.toByteArray();
        } else {
            data = image.getOriginalData();
        }
        int sizeBmpWords = ((data.length - 14) + 1) >>> 1;
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        writeWord(os, 1);
        writeWord(os, 9);
        writeWord(os, 768);
        writeDWord(os, (sizeBmpWords + 36) + 3);
        writeWord(os, 1);
        writeDWord(os, sizeBmpWords + 14);
        writeWord(os, 0);
        writeDWord(os, 4);
        writeWord(os, 259);
        writeWord(os, 8);
        writeDWord(os, 5);
        writeWord(os, META_SETWINDOWORG);
        writeWord(os, 0);
        writeWord(os, 0);
        writeDWord(os, 5);
        writeWord(os, META_SETWINDOWEXT);
        writeWord(os, (int) image.getHeight());
        writeWord(os, (int) image.getWidth());
        writeDWord(os, sizeBmpWords + 13);
        writeWord(os, META_DIBSTRETCHBLT);
        writeDWord(os, 13369376);
        writeWord(os, (int) image.getHeight());
        writeWord(os, (int) image.getWidth());
        writeWord(os, 0);
        writeWord(os, 0);
        writeWord(os, (int) image.getHeight());
        writeWord(os, (int) image.getWidth());
        writeWord(os, 0);
        writeWord(os, 0);
        os.write(data, 14, data.length - 14);
        if ((data.length & 1) == 1) {
            os.write(0);
        }
        writeDWord(os, 3);
        writeWord(os, 0);
        os.close();
        return os.toByteArray();
    }

    public static void writeWord(OutputStream os, int v) throws IOException {
        os.write(v & 255);
        os.write((v >>> 8) & 255);
    }

    public static void writeDWord(OutputStream os, int v) throws IOException {
        writeWord(os, v & 65535);
        writeWord(os, (v >>> 16) & 65535);
    }
}
