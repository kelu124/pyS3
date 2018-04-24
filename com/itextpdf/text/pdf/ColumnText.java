package com.itextpdf.text.pdf;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.List;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.error_messages.MessageLocalization;
import com.itextpdf.text.log.Logger;
import com.itextpdf.text.log.LoggerFactory;
import com.itextpdf.text.pdf.PdfPTable.FittingRows;
import com.itextpdf.text.pdf.draw.DrawInterface;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Stack;

public class ColumnText {
    public static final int AR_COMPOSEDTASHKEEL = 4;
    public static final int AR_LIG = 8;
    public static final int AR_NOVOWEL = 1;
    public static final int DIGITS_AN2EN = 64;
    public static final int DIGITS_EN2AN = 32;
    public static final int DIGITS_EN2AN_INIT_AL = 128;
    public static final int DIGITS_EN2AN_INIT_LR = 96;
    public static final int DIGIT_TYPE_AN = 0;
    public static final int DIGIT_TYPE_AN_EXTENDED = 256;
    public static final float GLOBAL_SPACE_CHAR_RATIO = 0.0f;
    protected static final int LINE_STATUS_NOLINE = 2;
    protected static final int LINE_STATUS_OFFLIMITS = 1;
    protected static final int LINE_STATUS_OK = 0;
    public static final int NO_MORE_COLUMN = 2;
    public static final int NO_MORE_TEXT = 1;
    public static final int START_COLUMN = 0;
    private final Logger LOGGER = LoggerFactory.getLogger(ColumnText.class);
    private boolean adjustFirstLine = true;
    protected int alignment = 0;
    private int arabicOptions = 0;
    protected BidiLine bidiLine;
    protected PdfContentByte canvas;
    protected PdfContentByte[] canvases;
    protected boolean composite = false;
    protected ColumnText compositeColumn;
    protected LinkedList<Element> compositeElements;
    protected float currentLeading = 16.0f;
    protected float descender;
    protected float extraParagraphSpace = 0.0f;
    private float filledWidth;
    private float firstLineY;
    private boolean firstLineYDone = false;
    protected float fixedLeading = 16.0f;
    protected float followingIndent = 0.0f;
    protected float indent = 0.0f;
    private boolean inheritGraphicState = false;
    protected boolean isWordSplit;
    private boolean lastWasNewline = true;
    protected float lastX;
    protected ArrayList<float[]> leftWall;
    protected float leftX;
    protected int lineStatus;
    private int linesWritten;
    protected int listIdx = 0;
    protected float maxY;
    protected float minY;
    protected float multipliedLeading = 0.0f;
    protected boolean rectangularMode = false;
    protected float rectangularWidth = -1.0f;
    private boolean repeatFirstLineIndent = true;
    protected float rightIndent = 0.0f;
    protected ArrayList<float[]> rightWall;
    protected float rightX;
    protected int rowIdx = 0;
    protected int runDirection = 0;
    private float spaceCharRatio = 0.0f;
    private int splittedRow = -1;
    private boolean useAscender = false;
    protected Phrase waitPhrase;
    protected float yLine;

    public ColumnText(PdfContentByte canvas) {
        this.canvas = canvas;
    }

    public static ColumnText duplicate(ColumnText org) {
        ColumnText ct = new ColumnText(null);
        ct.setACopy(org);
        return ct;
    }

    public ColumnText setACopy(ColumnText org) {
        setSimpleVars(org);
        if (org.bidiLine != null) {
            this.bidiLine = new BidiLine(org.bidiLine);
        }
        return this;
    }

    protected void setSimpleVars(ColumnText org) {
        this.maxY = org.maxY;
        this.minY = org.minY;
        this.alignment = org.alignment;
        this.leftWall = null;
        if (org.leftWall != null) {
            this.leftWall = new ArrayList(org.leftWall);
        }
        this.rightWall = null;
        if (org.rightWall != null) {
            this.rightWall = new ArrayList(org.rightWall);
        }
        this.yLine = org.yLine;
        this.currentLeading = org.currentLeading;
        this.fixedLeading = org.fixedLeading;
        this.multipliedLeading = org.multipliedLeading;
        this.canvas = org.canvas;
        this.canvases = org.canvases;
        this.lineStatus = org.lineStatus;
        this.indent = org.indent;
        this.followingIndent = org.followingIndent;
        this.rightIndent = org.rightIndent;
        this.extraParagraphSpace = org.extraParagraphSpace;
        this.rectangularWidth = org.rectangularWidth;
        this.rectangularMode = org.rectangularMode;
        this.spaceCharRatio = org.spaceCharRatio;
        this.lastWasNewline = org.lastWasNewline;
        this.repeatFirstLineIndent = org.repeatFirstLineIndent;
        this.linesWritten = org.linesWritten;
        this.arabicOptions = org.arabicOptions;
        this.runDirection = org.runDirection;
        this.descender = org.descender;
        this.composite = org.composite;
        this.splittedRow = org.splittedRow;
        if (org.composite) {
            this.compositeElements = new LinkedList();
            Iterator i$ = org.compositeElements.iterator();
            while (i$.hasNext()) {
                Element element = (Element) i$.next();
                if (element instanceof PdfPTable) {
                    this.compositeElements.add(new PdfPTable((PdfPTable) element));
                } else {
                    this.compositeElements.add(element);
                }
            }
            if (org.compositeColumn != null) {
                this.compositeColumn = duplicate(org.compositeColumn);
            }
        }
        this.listIdx = org.listIdx;
        this.rowIdx = org.rowIdx;
        this.firstLineY = org.firstLineY;
        this.leftX = org.leftX;
        this.rightX = org.rightX;
        this.firstLineYDone = org.firstLineYDone;
        this.waitPhrase = org.waitPhrase;
        this.useAscender = org.useAscender;
        this.filledWidth = org.filledWidth;
        this.adjustFirstLine = org.adjustFirstLine;
        this.inheritGraphicState = org.inheritGraphicState;
    }

    private void addWaitingPhrase() {
        if (this.bidiLine == null && this.waitPhrase != null) {
            this.bidiLine = new BidiLine();
            for (Chunk c : this.waitPhrase.getChunks()) {
                this.bidiLine.addChunk(new PdfChunk(c, null, this.waitPhrase.getTabSettings()));
            }
            this.waitPhrase = null;
        }
    }

    public void addText(Phrase phrase) {
        if (phrase != null && !this.composite) {
            addWaitingPhrase();
            if (this.bidiLine == null) {
                this.waitPhrase = phrase;
                return;
            }
            for (Chunk pdfChunk : phrase.getChunks()) {
                this.bidiLine.addChunk(new PdfChunk(pdfChunk, null, phrase.getTabSettings()));
            }
        }
    }

    public void setText(Phrase phrase) {
        this.bidiLine = null;
        this.composite = false;
        this.compositeColumn = null;
        this.compositeElements = null;
        this.listIdx = 0;
        this.rowIdx = 0;
        this.splittedRow = -1;
        this.waitPhrase = phrase;
    }

    public void addText(Chunk chunk) {
        if (chunk != null && !this.composite) {
            addText(new Phrase(chunk));
        }
    }

    public void addElement(Element element) {
        if (element != null) {
            if (element instanceof Image) {
                Image img = (Image) element;
                PdfPTable t = new PdfPTable(1);
                float w = img.getWidthPercentage();
                if (w == 0.0f) {
                    t.setTotalWidth(img.getScaledWidth());
                    t.setLockedWidth(true);
                } else {
                    t.setWidthPercentage(w);
                }
                t.setSpacingAfter(img.getSpacingAfter());
                t.setSpacingBefore(img.getSpacingBefore());
                switch (img.getAlignment()) {
                    case 0:
                        t.setHorizontalAlignment(0);
                        break;
                    case 2:
                        t.setHorizontalAlignment(2);
                        break;
                    default:
                        t.setHorizontalAlignment(1);
                        break;
                }
                PdfPCell c = new PdfPCell(img, true);
                c.setPadding(0.0f);
                c.setBorder(img.getBorder());
                c.setBorderColor(img.getBorderColor());
                c.setBorderWidth(img.getBorderWidth());
                c.setBackgroundColor(img.getBackgroundColor());
                t.addCell(c);
                element = t;
            }
            if (element.type() == 10) {
                element = new Paragraph((Chunk) element);
            } else if (element.type() == 11) {
                element = new Paragraph((Phrase) element);
            }
            if (element.type() == 12 || element.type() == 14 || element.type() == 23 || element.type() == 55 || element.type() == 37) {
                if (!this.composite) {
                    this.composite = true;
                    this.compositeElements = new LinkedList();
                    this.bidiLine = null;
                    this.waitPhrase = null;
                }
                if (element.type() == 12) {
                    this.compositeElements.addAll(((Paragraph) element).breakUp());
                    return;
                }
                this.compositeElements.add(element);
                return;
            }
            throw new IllegalArgumentException(MessageLocalization.getComposedMessage("element.not.allowed", new Object[0]));
        }
    }

    public static boolean isAllowedElement(Element element) {
        int type = element.type();
        if (type == 10 || type == 11 || type == 37 || type == 12 || type == 14 || type == 55 || type == 23 || (element instanceof Image)) {
            return true;
        }
        return false;
    }

    protected ArrayList<float[]> convertColumn(float[] cLine) {
        if (cLine.length < 4) {
            throw new RuntimeException(MessageLocalization.getComposedMessage("no.valid.column.line.found", new Object[0]));
        }
        ArrayList<float[]> cc = new ArrayList();
        for (int k = 0; k < cLine.length - 2; k += 2) {
            float x1 = cLine[k];
            float y1 = cLine[k + 1];
            float x2 = cLine[k + 2];
            float y2 = cLine[k + 3];
            if (y1 != y2) {
                float b = x1 - (((x1 - x2) / (y1 - y2)) * y1);
                float[] r = new float[]{Math.min(y1, y2), Math.max(y1, y2), (x1 - x2) / (y1 - y2), b};
                cc.add(r);
                this.maxY = Math.max(this.maxY, r[1]);
                this.minY = Math.min(this.minY, r[0]);
            }
        }
        if (!cc.isEmpty()) {
            return cc;
        }
        throw new RuntimeException(MessageLocalization.getComposedMessage("no.valid.column.line.found", new Object[0]));
    }

    protected float findLimitsPoint(ArrayList<float[]> wall) {
        this.lineStatus = 0;
        if (this.yLine < this.minY || this.yLine > this.maxY) {
            this.lineStatus = 1;
            return 0.0f;
        }
        for (int k = 0; k < wall.size(); k++) {
            float[] r = (float[]) wall.get(k);
            if (this.yLine >= r[0] && this.yLine <= r[1]) {
                return (r[2] * this.yLine) + r[3];
            }
        }
        this.lineStatus = 2;
        return 0.0f;
    }

    protected float[] findLimitsOneLine() {
        float x1 = findLimitsPoint(this.leftWall);
        if (this.lineStatus == 1 || this.lineStatus == 2) {
            return null;
        }
        float x2 = findLimitsPoint(this.rightWall);
        if (this.lineStatus == 2) {
            return null;
        }
        return new float[]{x1, x2};
    }

    protected float[] findLimitsTwoLines() {
        boolean repeat = false;
        while (true) {
            if (repeat && this.currentLeading == 0.0f) {
                return null;
            }
            repeat = true;
            float[] x1 = findLimitsOneLine();
            if (this.lineStatus == 1) {
                return null;
            }
            this.yLine -= this.currentLeading;
            if (this.lineStatus != 2) {
                float[] x2 = findLimitsOneLine();
                if (this.lineStatus == 1) {
                    return null;
                }
                if (this.lineStatus == 2) {
                    this.yLine -= this.currentLeading;
                } else if (x1[0] < x2[1] && x2[0] < x1[1]) {
                    return new float[]{x1[0], x1[1], x2[0], x2[1]};
                }
            }
        }
    }

    public void setColumns(float[] leftLine, float[] rightLine) {
        this.maxY = -1.0E21f;
        this.minY = 1.0E21f;
        setYLine(Math.max(leftLine[1], leftLine[leftLine.length - 1]));
        this.rightWall = convertColumn(rightLine);
        this.leftWall = convertColumn(leftLine);
        this.rectangularWidth = -1.0f;
        this.rectangularMode = false;
    }

    public void setSimpleColumn(Phrase phrase, float llx, float lly, float urx, float ury, float leading, int alignment) {
        addText(phrase);
        setSimpleColumn(llx, lly, urx, ury, leading, alignment);
    }

    public void setSimpleColumn(float llx, float lly, float urx, float ury, float leading, int alignment) {
        setLeading(leading);
        this.alignment = alignment;
        setSimpleColumn(llx, lly, urx, ury);
    }

    public void setSimpleColumn(float llx, float lly, float urx, float ury) {
        this.leftX = Math.min(llx, urx);
        this.maxY = Math.max(lly, ury);
        this.minY = Math.min(lly, ury);
        this.rightX = Math.max(llx, urx);
        this.yLine = this.maxY;
        this.rectangularWidth = this.rightX - this.leftX;
        if (this.rectangularWidth < 0.0f) {
            this.rectangularWidth = 0.0f;
        }
        this.rectangularMode = true;
    }

    public void setSimpleColumn(Rectangle rect) {
        setSimpleColumn(rect.getLeft(), rect.getBottom(), rect.getRight(), rect.getTop());
    }

    public void setLeading(float leading) {
        this.fixedLeading = leading;
        this.multipliedLeading = 0.0f;
    }

    public void setLeading(float fixedLeading, float multipliedLeading) {
        this.fixedLeading = fixedLeading;
        this.multipliedLeading = multipliedLeading;
    }

    public float getLeading() {
        return this.fixedLeading;
    }

    public float getMultipliedLeading() {
        return this.multipliedLeading;
    }

    public void setYLine(float yLine) {
        this.yLine = yLine;
    }

    public float getYLine() {
        return this.yLine;
    }

    public int getRowsDrawn() {
        return this.rowIdx;
    }

    public void setAlignment(int alignment) {
        this.alignment = alignment;
    }

    public int getAlignment() {
        return this.alignment;
    }

    public void setIndent(float indent) {
        setIndent(indent, true);
    }

    public void setIndent(float indent, boolean repeatFirstLineIndent) {
        this.indent = indent;
        this.lastWasNewline = true;
        this.repeatFirstLineIndent = repeatFirstLineIndent;
    }

    public float getIndent() {
        return this.indent;
    }

    public void setFollowingIndent(float indent) {
        this.followingIndent = indent;
        this.lastWasNewline = true;
    }

    public float getFollowingIndent() {
        return this.followingIndent;
    }

    public void setRightIndent(float indent) {
        this.rightIndent = indent;
        this.lastWasNewline = true;
    }

    public float getRightIndent() {
        return this.rightIndent;
    }

    public float getCurrentLeading() {
        return this.currentLeading;
    }

    public boolean getInheritGraphicState() {
        return this.inheritGraphicState;
    }

    public void setInheritGraphicState(boolean inheritGraphicState) {
        this.inheritGraphicState = inheritGraphicState;
    }

    public int go() throws DocumentException {
        return go(false);
    }

    public int go(boolean simulate) throws DocumentException {
        return go(simulate, null);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int go(boolean r35, com.itextpdf.text.pdf.interfaces.IAccessibleElement r36) throws com.itextpdf.text.DocumentException {
        /*
        r34 = this;
        r2 = 0;
        r0 = r34;
        r0.isWordSplit = r2;
        r0 = r34;
        r2 = r0.composite;
        if (r2 == 0) goto L_0x0010;
    L_0x000b:
        r28 = r34.goComposite(r35);
    L_0x000f:
        return r28;
    L_0x0010:
        r20 = 0;
        r0 = r34;
        r2 = r0.canvas;
        r2 = isTagged(r2);
        if (r2 == 0) goto L_0x002a;
    L_0x001c:
        r0 = r36;
        r2 = r0 instanceof com.itextpdf.text.ListItem;
        if (r2 == 0) goto L_0x002a;
    L_0x0022:
        r2 = r36;
        r2 = (com.itextpdf.text.ListItem) r2;
        r20 = r2.getListBody();
    L_0x002a:
        r34.addWaitingPhrase();
        r0 = r34;
        r2 = r0.bidiLine;
        if (r2 != 0) goto L_0x0036;
    L_0x0033:
        r28 = 1;
        goto L_0x000f;
    L_0x0036:
        r2 = 0;
        r0 = r34;
        r0.descender = r2;
        r2 = 0;
        r0 = r34;
        r0.linesWritten = r2;
        r2 = 0;
        r0 = r34;
        r0.lastX = r2;
        r16 = 0;
        r0 = r34;
        r0 = r0.spaceCharRatio;
        r27 = r0;
        r2 = 2;
        r15 = new java.lang.Object[r2];
        r14 = 0;
        r21 = new java.lang.Float;
        r2 = 0;
        r0 = r21;
        r0.<init>(r2);
        r2 = 1;
        r15[r2] = r21;
        r26 = 0;
        r18 = 0;
        r30 = 0;
        r2 = 2143289344; // 0x7fc00000 float:NaN double:1.058925634E-314;
        r0 = r34;
        r0.firstLineY = r2;
        r6 = 1;
        r0 = r34;
        r2 = r0.runDirection;
        if (r2 == 0) goto L_0x0073;
    L_0x006f:
        r0 = r34;
        r6 = r0.runDirection;
    L_0x0073:
        r0 = r34;
        r2 = r0.canvas;
        if (r2 == 0) goto L_0x00dc;
    L_0x0079:
        r0 = r34;
        r0 = r0.canvas;
        r18 = r0;
        r0 = r34;
        r2 = r0.canvas;
        r26 = r2.getPdfDocument();
        r0 = r34;
        r2 = r0.canvas;
        r2 = isTagged(r2);
        if (r2 != 0) goto L_0x00d5;
    L_0x0091:
        r0 = r34;
        r2 = r0.canvas;
        r0 = r34;
        r4 = r0.inheritGraphicState;
        r30 = r2.getDuplicate(r4);
    L_0x009d:
        if (r35 != 0) goto L_0x00ac;
    L_0x009f:
        r2 = 0;
        r2 = (r27 > r2 ? 1 : (r27 == r2 ? 0 : -1));
        if (r2 != 0) goto L_0x00ed;
    L_0x00a4:
        r2 = r30.getPdfWriter();
        r27 = r2.getSpaceCharRatio();
    L_0x00ac:
        r0 = r34;
        r2 = r0.rectangularMode;
        if (r2 != 0) goto L_0x0107;
    L_0x00b2:
        r24 = 0;
        r0 = r34;
        r2 = r0.bidiLine;
        r2 = r2.chunks;
        r19 = r2.iterator();
    L_0x00be:
        r2 = r19.hasNext();
        if (r2 == 0) goto L_0x00f8;
    L_0x00c4:
        r13 = r19.next();
        r13 = (com.itextpdf.text.pdf.PdfChunk) r13;
        r2 = r13.height();
        r0 = r24;
        r24 = java.lang.Math.max(r0, r2);
        goto L_0x00be;
    L_0x00d5:
        r0 = r34;
        r0 = r0.canvas;
        r30 = r0;
        goto L_0x009d;
    L_0x00dc:
        if (r35 != 0) goto L_0x009d;
    L_0x00de:
        r2 = new java.lang.NullPointerException;
        r4 = "columntext.go.with.simulate.eq.eq.false.and.text.eq.eq.null";
        r5 = 0;
        r5 = new java.lang.Object[r5];
        r4 = com.itextpdf.text.error_messages.MessageLocalization.getComposedMessage(r4, r5);
        r2.<init>(r4);
        throw r2;
    L_0x00ed:
        r2 = 981668463; // 0x3a83126f float:0.001 double:4.85008663E-315;
        r2 = (r27 > r2 ? 1 : (r27 == r2 ? 0 : -1));
        if (r2 >= 0) goto L_0x00ac;
    L_0x00f4:
        r27 = 981668463; // 0x3a83126f float:0.001 double:4.85008663E-315;
        goto L_0x00ac;
    L_0x00f8:
        r0 = r34;
        r2 = r0.fixedLeading;
        r0 = r34;
        r4 = r0.multipliedLeading;
        r4 = r4 * r24;
        r2 = r2 + r4;
        r0 = r34;
        r0.currentLeading = r2;
    L_0x0107:
        r17 = 0;
        r28 = 0;
    L_0x010b:
        r0 = r34;
        r2 = r0.lastWasNewline;
        if (r2 == 0) goto L_0x0151;
    L_0x0111:
        r0 = r34;
        r0 = r0.indent;
        r17 = r0;
    L_0x0117:
        r0 = r34;
        r2 = r0.rectangularMode;
        if (r2 == 0) goto L_0x0332;
    L_0x011d:
        r0 = r34;
        r2 = r0.rectangularWidth;
        r0 = r34;
        r4 = r0.rightIndent;
        r4 = r4 + r17;
        r2 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1));
        if (r2 > 0) goto L_0x0158;
    L_0x012b:
        r28 = 2;
        r0 = r34;
        r2 = r0.bidiLine;
        r2 = r2.isEmpty();
        if (r2 == 0) goto L_0x0139;
    L_0x0137:
        r28 = r28 | 1;
    L_0x0139:
        if (r16 == 0) goto L_0x000f;
    L_0x013b:
        r30.endText();
        r0 = r34;
        r2 = r0.canvas;
        r0 = r30;
        if (r2 == r0) goto L_0x000f;
    L_0x0146:
        r0 = r34;
        r2 = r0.canvas;
        r0 = r30;
        r2.add(r0);
        goto L_0x000f;
    L_0x0151:
        r0 = r34;
        r0 = r0.followingIndent;
        r17 = r0;
        goto L_0x0117;
    L_0x0158:
        r0 = r34;
        r2 = r0.bidiLine;
        r2 = r2.isEmpty();
        if (r2 == 0) goto L_0x0165;
    L_0x0162:
        r28 = 1;
        goto L_0x0139;
    L_0x0165:
        r0 = r34;
        r2 = r0.bidiLine;
        r0 = r34;
        r3 = r0.leftX;
        r0 = r34;
        r4 = r0.rectangularWidth;
        r4 = r4 - r17;
        r0 = r34;
        r5 = r0.rightIndent;
        r4 = r4 - r5;
        r0 = r34;
        r5 = r0.alignment;
        r0 = r34;
        r7 = r0.arabicOptions;
        r0 = r34;
        r8 = r0.minY;
        r0 = r34;
        r9 = r0.yLine;
        r0 = r34;
        r10 = r0.descender;
        r23 = r2.processLine(r3, r4, r5, r6, r7, r8, r9, r10);
        r0 = r34;
        r2 = r0.isWordSplit;
        r0 = r34;
        r4 = r0.bidiLine;
        r4 = r4.isWordSplit();
        r2 = r2 | r4;
        r0 = r34;
        r0.isWordSplit = r2;
        if (r23 != 0) goto L_0x01a6;
    L_0x01a3:
        r28 = 1;
        goto L_0x0139;
    L_0x01a6:
        r0 = r34;
        r2 = r0.fixedLeading;
        r0 = r34;
        r4 = r0.multipliedLeading;
        r0 = r23;
        r25 = r0.getMaxSize(r2, r4);
        r2 = r34.isUseAscender();
        if (r2 == 0) goto L_0x01f4;
    L_0x01ba:
        r0 = r34;
        r2 = r0.firstLineY;
        r2 = java.lang.Float.isNaN(r2);
        if (r2 == 0) goto L_0x01f4;
    L_0x01c4:
        r2 = r23.getAscender();
        r0 = r34;
        r0.currentLeading = r2;
    L_0x01cc:
        r0 = r34;
        r2 = r0.yLine;
        r0 = r34;
        r4 = r0.maxY;
        r2 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1));
        if (r2 > 0) goto L_0x01e9;
    L_0x01d8:
        r0 = r34;
        r2 = r0.yLine;
        r0 = r34;
        r4 = r0.currentLeading;
        r2 = r2 - r4;
        r0 = r34;
        r4 = r0.minY;
        r2 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1));
        if (r2 >= 0) goto L_0x0208;
    L_0x01e9:
        r28 = 2;
        r0 = r34;
        r2 = r0.bidiLine;
        r2.restore();
        goto L_0x0139;
    L_0x01f4:
        r2 = 0;
        r2 = r25[r2];
        r4 = 1;
        r4 = r25[r4];
        r0 = r34;
        r5 = r0.descender;
        r4 = r4 - r5;
        r2 = java.lang.Math.max(r2, r4);
        r0 = r34;
        r0.currentLeading = r2;
        goto L_0x01cc;
    L_0x0208:
        r0 = r34;
        r2 = r0.yLine;
        r0 = r34;
        r4 = r0.currentLeading;
        r2 = r2 - r4;
        r0 = r34;
        r0.yLine = r2;
        if (r35 != 0) goto L_0x021e;
    L_0x0217:
        if (r16 != 0) goto L_0x021e;
    L_0x0219:
        r30.beginText();
        r16 = 1;
    L_0x021e:
        r0 = r34;
        r2 = r0.firstLineY;
        r2 = java.lang.Float.isNaN(r2);
        if (r2 == 0) goto L_0x0230;
    L_0x0228:
        r0 = r34;
        r2 = r0.yLine;
        r0 = r34;
        r0.firstLineY = r2;
    L_0x0230:
        r0 = r34;
        r2 = r0.rectangularWidth;
        r4 = r23.widthLeft();
        r2 = r2 - r4;
        r0 = r34;
        r0.updateFilledWidth(r2);
        r0 = r34;
        r3 = r0.leftX;
    L_0x0242:
        r0 = r34;
        r2 = r0.canvas;
        r2 = isTagged(r2);
        if (r2 == 0) goto L_0x02b6;
    L_0x024c:
        r0 = r36;
        r2 = r0 instanceof com.itextpdf.text.ListItem;
        if (r2 == 0) goto L_0x02b6;
    L_0x0252:
        r0 = r34;
        r2 = r0.firstLineY;
        r2 = java.lang.Float.isNaN(r2);
        if (r2 != 0) goto L_0x02b6;
    L_0x025c:
        r0 = r34;
        r2 = r0.firstLineYDone;
        if (r2 != 0) goto L_0x02b6;
    L_0x0262:
        if (r35 != 0) goto L_0x02b1;
    L_0x0264:
        r2 = r36;
        r2 = (com.itextpdf.text.ListItem) r2;
        r22 = r2.getListLabel();
        r0 = r34;
        r2 = r0.canvas;
        r0 = r22;
        r2.openMCBlock(r0);
        r29 = new com.itextpdf.text.Chunk;
        r2 = r36;
        r2 = (com.itextpdf.text.ListItem) r2;
        r2 = r2.getListSymbol();
        r0 = r29;
        r0.<init>(r2);
        r2 = 0;
        r0 = r29;
        r0.setRole(r2);
        r0 = r34;
        r7 = r0.canvas;
        r8 = 0;
        r9 = new com.itextpdf.text.Phrase;
        r0 = r29;
        r9.<init>(r0);
        r0 = r34;
        r2 = r0.leftX;
        r4 = r22.getIndentation();
        r10 = r2 + r4;
        r0 = r34;
        r11 = r0.firstLineY;
        r12 = 0;
        showTextAligned(r7, r8, r9, r10, r11, r12);
        r0 = r34;
        r2 = r0.canvas;
        r0 = r22;
        r2.closeMCBlock(r0);
    L_0x02b1:
        r2 = 1;
        r0 = r34;
        r0.firstLineYDone = r2;
    L_0x02b6:
        if (r35 != 0) goto L_0x02f9;
    L_0x02b8:
        if (r20 == 0) goto L_0x02c5;
    L_0x02ba:
        r0 = r34;
        r2 = r0.canvas;
        r0 = r20;
        r2.openMCBlock(r0);
        r20 = 0;
    L_0x02c5:
        r2 = 0;
        r15[r2] = r14;
        r2 = r23.isRTL();
        if (r2 == 0) goto L_0x03c6;
    L_0x02ce:
        r0 = r34;
        r2 = r0.rightIndent;
    L_0x02d2:
        r2 = r2 + r3;
        r4 = r23.indentLeft();
        r2 = r2 + r4;
        r0 = r34;
        r4 = r0.yLine;
        r0 = r30;
        r0.setTextMatrix(r2, r4);
        r7 = r26;
        r8 = r23;
        r9 = r30;
        r10 = r18;
        r11 = r15;
        r12 = r27;
        r2 = r7.writeLineToContent(r8, r9, r10, r11, r12);
        r0 = r34;
        r0.lastX = r2;
        r2 = 0;
        r14 = r15[r2];
        r14 = (com.itextpdf.text.pdf.PdfFont) r14;
    L_0x02f9:
        r0 = r34;
        r2 = r0.repeatFirstLineIndent;
        if (r2 == 0) goto L_0x03ca;
    L_0x02ff:
        r2 = r23.isNewlineSplit();
        if (r2 == 0) goto L_0x03ca;
    L_0x0305:
        r2 = 1;
    L_0x0306:
        r0 = r34;
        r0.lastWasNewline = r2;
        r0 = r34;
        r4 = r0.yLine;
        r2 = r23.isNewlineSplit();
        if (r2 == 0) goto L_0x03cd;
    L_0x0314:
        r0 = r34;
        r2 = r0.extraParagraphSpace;
    L_0x0318:
        r2 = r4 - r2;
        r0 = r34;
        r0.yLine = r2;
        r0 = r34;
        r2 = r0.linesWritten;
        r2 = r2 + 1;
        r0 = r34;
        r0.linesWritten = r2;
        r2 = r23.getDescender();
        r0 = r34;
        r0.descender = r2;
        goto L_0x010b;
    L_0x0332:
        r0 = r34;
        r2 = r0.yLine;
        r0 = r34;
        r4 = r0.currentLeading;
        r33 = r2 - r4;
        r32 = r34.findLimitsTwoLines();
        if (r32 != 0) goto L_0x0358;
    L_0x0342:
        r28 = 2;
        r0 = r34;
        r2 = r0.bidiLine;
        r2 = r2.isEmpty();
        if (r2 == 0) goto L_0x0350;
    L_0x034e:
        r28 = r28 | 1;
    L_0x0350:
        r0 = r33;
        r1 = r34;
        r1.yLine = r0;
        goto L_0x0139;
    L_0x0358:
        r0 = r34;
        r2 = r0.bidiLine;
        r2 = r2.isEmpty();
        if (r2 == 0) goto L_0x036c;
    L_0x0362:
        r28 = 1;
        r0 = r33;
        r1 = r34;
        r1.yLine = r0;
        goto L_0x0139;
    L_0x036c:
        r2 = 0;
        r2 = r32[r2];
        r4 = 2;
        r4 = r32[r4];
        r3 = java.lang.Math.max(r2, r4);
        r2 = 1;
        r2 = r32[r2];
        r4 = 3;
        r4 = r32[r4];
        r31 = java.lang.Math.min(r2, r4);
        r2 = r31 - r3;
        r0 = r34;
        r4 = r0.rightIndent;
        r4 = r4 + r17;
        r2 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1));
        if (r2 <= 0) goto L_0x010b;
    L_0x038c:
        if (r35 != 0) goto L_0x0395;
    L_0x038e:
        if (r16 != 0) goto L_0x0395;
    L_0x0390:
        r30.beginText();
        r16 = 1;
    L_0x0395:
        r0 = r34;
        r2 = r0.bidiLine;
        r4 = r31 - r3;
        r4 = r4 - r17;
        r0 = r34;
        r5 = r0.rightIndent;
        r4 = r4 - r5;
        r0 = r34;
        r5 = r0.alignment;
        r0 = r34;
        r7 = r0.arabicOptions;
        r0 = r34;
        r8 = r0.minY;
        r0 = r34;
        r9 = r0.yLine;
        r0 = r34;
        r10 = r0.descender;
        r23 = r2.processLine(r3, r4, r5, r6, r7, r8, r9, r10);
        if (r23 != 0) goto L_0x0242;
    L_0x03bc:
        r28 = 1;
        r0 = r33;
        r1 = r34;
        r1.yLine = r0;
        goto L_0x0139;
    L_0x03c6:
        r2 = r17;
        goto L_0x02d2;
    L_0x03ca:
        r2 = 0;
        goto L_0x0306;
    L_0x03cd:
        r2 = 0;
        goto L_0x0318;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.itextpdf.text.pdf.ColumnText.go(boolean, com.itextpdf.text.pdf.interfaces.IAccessibleElement):int");
    }

    public boolean isWordSplit() {
        return this.isWordSplit;
    }

    public float getExtraParagraphSpace() {
        return this.extraParagraphSpace;
    }

    public void setExtraParagraphSpace(float extraParagraphSpace) {
        this.extraParagraphSpace = extraParagraphSpace;
    }

    public void clearChunks() {
        if (this.bidiLine != null) {
            this.bidiLine.clearChunks();
        }
    }

    public float getSpaceCharRatio() {
        return this.spaceCharRatio;
    }

    public void setSpaceCharRatio(float spaceCharRatio) {
        this.spaceCharRatio = spaceCharRatio;
    }

    public void setRunDirection(int runDirection) {
        if (runDirection < 0 || runDirection > 3) {
            throw new RuntimeException(MessageLocalization.getComposedMessage("invalid.run.direction.1", runDirection));
        }
        this.runDirection = runDirection;
    }

    public int getRunDirection() {
        return this.runDirection;
    }

    public int getLinesWritten() {
        return this.linesWritten;
    }

    public float getLastX() {
        return this.lastX;
    }

    public int getArabicOptions() {
        return this.arabicOptions;
    }

    public void setArabicOptions(int arabicOptions) {
        this.arabicOptions = arabicOptions;
    }

    public float getDescender() {
        return this.descender;
    }

    public static float getWidth(Phrase phrase, int runDirection, int arabicOptions) {
        ColumnText ct = new ColumnText(null);
        ct.addText(phrase);
        ct.addWaitingPhrase();
        PdfLine line = ct.bidiLine.processLine(0.0f, PdfPRow.RIGHT_LIMIT, 0, runDirection, arabicOptions, 0.0f, 0.0f, 0.0f);
        if (line == null) {
            return 0.0f;
        }
        return PdfPRow.RIGHT_LIMIT - line.widthLeft();
    }

    public static float getWidth(Phrase phrase) {
        return getWidth(phrase, 1, 0);
    }

    public static void showTextAligned(PdfContentByte canvas, int alignment, Phrase phrase, float x, float y, float rotation, int runDirection, int arabicOptions) {
        float urx;
        float llx;
        float llx2;
        float lly;
        if (!(alignment == 0 || alignment == 1 || alignment == 2)) {
            alignment = 0;
        }
        canvas.saveState();
        ColumnText ct = new ColumnText(canvas);
        float ury = BaseField.BORDER_WIDTH_MEDIUM;
        switch (alignment) {
            case 0:
                urx = PdfPRow.RIGHT_LIMIT;
                llx = 0.0f;
                break;
            case 2:
                urx = 0.0f;
                llx = -20000.0f;
                break;
            default:
                urx = PdfPRow.RIGHT_LIMIT;
                llx = -20000.0f;
                break;
        }
        if (rotation == 0.0f) {
            llx2 = llx + x;
            lly = -1.0f + y;
            urx += x;
            ury = BaseField.BORDER_WIDTH_MEDIUM + y;
        } else {
            double alpha = (((double) rotation) * 3.141592653589793d) / 180.0d;
            float cos = (float) Math.cos(alpha);
            float sin = (float) Math.sin(alpha);
            canvas.concatCTM(cos, sin, -sin, cos, x, y);
            llx2 = llx;
            lly = -1.0f;
        }
        ct.setSimpleColumn(phrase, llx2, lly, urx, ury, BaseField.BORDER_WIDTH_MEDIUM, alignment);
        if (runDirection == 3) {
            if (alignment == 0) {
                alignment = 2;
            } else if (alignment == 2) {
                alignment = 0;
            }
        }
        ct.setAlignment(alignment);
        ct.setArabicOptions(arabicOptions);
        ct.setRunDirection(runDirection);
        try {
            ct.go();
            canvas.restoreState();
        } catch (Exception e) {
            throw new ExceptionConverter(e);
        }
    }

    public static void showTextAligned(PdfContentByte canvas, int alignment, Phrase phrase, float x, float y, float rotation) {
        showTextAligned(canvas, alignment, phrase, x, y, rotation, 1, 0);
    }

    public static float fitText(Font font, String text, Rectangle rect, float maxFontSize, int runDirection) {
        int k;
        Exception e;
        ColumnText columnText;
        ColumnText ct = null;
        if (maxFontSize <= 0.0f) {
            int cr = 0;
            int lf = 0;
            try {
                char[] t = text.toCharArray();
                for (k = 0; k < t.length; k++) {
                    if (t[k] == '\n') {
                        lf++;
                    } else if (t[k] == '\r') {
                        cr++;
                    }
                }
                maxFontSize = (Math.abs(rect.getHeight()) / ((float) (Math.max(cr, lf) + 1))) - 0.001f;
            } catch (Exception e2) {
                e = e2;
                columnText = ct;
            }
        }
        font.setSize(maxFontSize);
        Phrase ph = new Phrase(text, font);
        columnText = new ColumnText(null);
        try {
            columnText.setSimpleColumn(ph, rect.getLeft(), rect.getBottom(), rect.getRight(), rect.getTop(), maxFontSize, 0);
            columnText.setRunDirection(runDirection);
            if ((columnText.go(true) & 1) != 0) {
                return maxFontSize;
            }
            float min = 0.0f;
            float max = maxFontSize;
            float size = maxFontSize;
            k = 0;
            ct = columnText;
            while (k < 50) {
                size = (min + max) / BaseField.BORDER_WIDTH_MEDIUM;
                columnText = new ColumnText(null);
                font.setSize(size);
                columnText.setSimpleColumn(new Phrase(text, font), rect.getLeft(), rect.getBottom(), rect.getRight(), rect.getTop(), size, 0);
                columnText.setRunDirection(runDirection);
                if ((columnText.go(true) & 1) == 0) {
                    max = size;
                } else if (max - min < size * 0.1f) {
                    return size;
                } else {
                    min = size;
                }
                k++;
                ct = columnText;
            }
            return size;
        } catch (Exception e3) {
            e = e3;
        }
        throw new ExceptionConverter(e);
    }

    protected int goComposite(boolean simulate) throws DocumentException {
        if (this.canvas != null) {
            PdfDocument pdf = this.canvas.pdf;
        }
        if (this.rectangularMode) {
            this.linesWritten = 0;
            this.descender = 0.0f;
            boolean firstPass = true;
            boolean isRTL = this.runDirection == 3;
            while (!this.compositeElements.isEmpty()) {
                Element element = (Element) this.compositeElements.getFirst();
                int status;
                int keep;
                float lastY;
                boolean createHere;
                ColumnText columnText;
                boolean z;
                boolean keepCandidate;
                boolean s;
                if (element.type() == 12) {
                    Phrase para = (Paragraph) element;
                    status = 0;
                    keep = 0;
                    while (keep < 2) {
                        lastY = this.yLine;
                        createHere = false;
                        if (this.compositeColumn == null) {
                            this.compositeColumn = new ColumnText(this.canvas);
                            this.compositeColumn.setAlignment(para.getAlignment());
                            this.compositeColumn.setIndent(para.getIndentationLeft() + para.getFirstLineIndent(), false);
                            this.compositeColumn.setExtraParagraphSpace(para.getExtraParagraphSpace());
                            this.compositeColumn.setFollowingIndent(para.getIndentationLeft());
                            this.compositeColumn.setRightIndent(para.getIndentationRight());
                            this.compositeColumn.setLeading(para.getLeading(), para.getMultipliedLeading());
                            this.compositeColumn.setRunDirection(this.runDirection);
                            this.compositeColumn.setArabicOptions(this.arabicOptions);
                            this.compositeColumn.setSpaceCharRatio(this.spaceCharRatio);
                            this.compositeColumn.addText(para);
                            if (!(firstPass && this.adjustFirstLine)) {
                                this.yLine -= para.getSpacingBefore();
                            }
                            createHere = true;
                        }
                        columnText = this.compositeColumn;
                        z = ((firstPass || this.descender == 0.0f) && this.adjustFirstLine) ? this.useAscender : false;
                        columnText.setUseAscender(z);
                        this.compositeColumn.setInheritGraphicState(this.inheritGraphicState);
                        this.compositeColumn.leftX = this.leftX;
                        this.compositeColumn.rightX = this.rightX;
                        this.compositeColumn.yLine = this.yLine;
                        this.compositeColumn.rectangularWidth = this.rectangularWidth;
                        this.compositeColumn.rectangularMode = this.rectangularMode;
                        this.compositeColumn.minY = this.minY;
                        this.compositeColumn.maxY = this.maxY;
                        keepCandidate = para.getKeepTogether() && createHere && !(firstPass && this.adjustFirstLine);
                        s = simulate || (keepCandidate && keep == 0);
                        if (isTagged(this.canvas) && !s) {
                            this.canvas.openMCBlock(para);
                        }
                        status = this.compositeColumn.go(s);
                        if (isTagged(this.canvas) && !s) {
                            this.canvas.closeMCBlock(para);
                        }
                        this.lastX = this.compositeColumn.getLastX();
                        updateFilledWidth(this.compositeColumn.filledWidth);
                        if ((status & 1) == 0 && keepCandidate) {
                            this.compositeColumn = null;
                            this.yLine = lastY;
                            return 2;
                        }
                        if (!simulate && keepCandidate) {
                            if (keep == 0) {
                                this.compositeColumn = null;
                                this.yLine = lastY;
                            }
                            keep++;
                        }
                        firstPass = false;
                        if (this.compositeColumn.getLinesWritten() > 0) {
                            this.yLine = this.compositeColumn.yLine;
                            this.linesWritten += this.compositeColumn.linesWritten;
                            this.descender = this.compositeColumn.descender;
                            this.isWordSplit |= this.compositeColumn.isWordSplit();
                        }
                        this.currentLeading = this.compositeColumn.currentLeading;
                        if ((status & 1) != 0) {
                            this.compositeColumn = null;
                            this.compositeElements.removeFirst();
                            this.yLine -= para.getSpacingAfter();
                        }
                        if ((status & 2) != 0) {
                            return 2;
                        }
                    }
                    firstPass = false;
                    if (this.compositeColumn.getLinesWritten() > 0) {
                        this.yLine = this.compositeColumn.yLine;
                        this.linesWritten += this.compositeColumn.linesWritten;
                        this.descender = this.compositeColumn.descender;
                        this.isWordSplit |= this.compositeColumn.isWordSplit();
                    }
                    this.currentLeading = this.compositeColumn.currentLeading;
                    if ((status & 1) != 0) {
                        this.compositeColumn = null;
                        this.compositeElements.removeFirst();
                        this.yLine -= para.getSpacingAfter();
                    }
                    if ((status & 2) != 0) {
                        return 2;
                    }
                } else if (element.type() == 14) {
                    List list = (List) element;
                    ArrayList<Element> items = list.getItems();
                    Phrase item = null;
                    float listIndentation = list.getIndentationLeft();
                    int count = 0;
                    Stack<Object[]> stack = new Stack();
                    k = 0;
                    while (k < items.size()) {
                        ListItem obj = items.get(k);
                        if (obj instanceof ListItem) {
                            if (count == this.listIdx) {
                                item = obj;
                                status = 0;
                                keep = 0;
                                while (keep < 2) {
                                    lastY = this.yLine;
                                    createHere = false;
                                    if (this.compositeColumn == null) {
                                        if (item != null) {
                                            this.listIdx = 0;
                                            this.compositeElements.removeFirst();
                                        } else {
                                            this.compositeColumn = new ColumnText(this.canvas);
                                            columnText = this.compositeColumn;
                                            z = ((firstPass || this.descender == 0.0f) && this.adjustFirstLine) ? this.useAscender : false;
                                            columnText.setUseAscender(z);
                                            this.compositeColumn.setInheritGraphicState(this.inheritGraphicState);
                                            this.compositeColumn.setAlignment(item.getAlignment());
                                            this.compositeColumn.setIndent((item.getIndentationLeft() + listIndentation) + item.getFirstLineIndent(), false);
                                            this.compositeColumn.setExtraParagraphSpace(item.getExtraParagraphSpace());
                                            this.compositeColumn.setFollowingIndent(this.compositeColumn.getIndent());
                                            this.compositeColumn.setRightIndent(item.getIndentationRight() + list.getIndentationRight());
                                            this.compositeColumn.setLeading(item.getLeading(), item.getMultipliedLeading());
                                            this.compositeColumn.setRunDirection(this.runDirection);
                                            this.compositeColumn.setArabicOptions(this.arabicOptions);
                                            this.compositeColumn.setSpaceCharRatio(this.spaceCharRatio);
                                            this.compositeColumn.addText(item);
                                            if (!(firstPass && this.adjustFirstLine)) {
                                                this.yLine -= item.getSpacingBefore();
                                            }
                                            createHere = true;
                                        }
                                    }
                                    this.compositeColumn.leftX = this.leftX;
                                    this.compositeColumn.rightX = this.rightX;
                                    this.compositeColumn.yLine = this.yLine;
                                    this.compositeColumn.rectangularWidth = this.rectangularWidth;
                                    this.compositeColumn.rectangularMode = this.rectangularMode;
                                    this.compositeColumn.minY = this.minY;
                                    this.compositeColumn.maxY = this.maxY;
                                    keepCandidate = item.getKeepTogether() && createHere && !(firstPass && this.adjustFirstLine);
                                    s = simulate || (keepCandidate && keep == 0);
                                    if (isTagged(this.canvas) && !s) {
                                        item.getListLabel().setIndentation(listIndentation);
                                        if (list.getFirstItem() == item || !(this.compositeColumn == null || this.compositeColumn.bidiLine == null)) {
                                            this.canvas.openMCBlock(list);
                                        }
                                        this.canvas.openMCBlock(item);
                                    }
                                    columnText = this.compositeColumn;
                                    z = simulate || (keepCandidate && keep == 0);
                                    status = columnText.go(z, item);
                                    if (isTagged(this.canvas) && !s) {
                                        this.canvas.closeMCBlock(item.getListBody());
                                        this.canvas.closeMCBlock(item);
                                        if ((list.getLastItem() == item && (status & 1) != 0) || (status & 2) != 0) {
                                            this.canvas.closeMCBlock(list);
                                        }
                                    }
                                    this.lastX = this.compositeColumn.getLastX();
                                    updateFilledWidth(this.compositeColumn.filledWidth);
                                    if ((status & 1) == 0 || !keepCandidate) {
                                        if (!simulate && keepCandidate) {
                                            if (keep == 0) {
                                                this.compositeColumn = null;
                                                this.yLine = lastY;
                                            }
                                            keep++;
                                        }
                                        firstPass = false;
                                        this.yLine = this.compositeColumn.yLine;
                                        this.linesWritten += this.compositeColumn.linesWritten;
                                        this.descender = this.compositeColumn.descender;
                                        this.currentLeading = this.compositeColumn.currentLeading;
                                        if (!(isTagged(this.canvas) || Float.isNaN(this.compositeColumn.firstLineY) || this.compositeColumn.firstLineYDone)) {
                                            if (!simulate) {
                                                if (isRTL) {
                                                    showTextAligned(this.canvas, 2, new Phrase(item.getListSymbol()), this.compositeColumn.lastX + item.getIndentationLeft(), this.compositeColumn.firstLineY, 0.0f, this.runDirection, this.arabicOptions);
                                                } else {
                                                    showTextAligned(this.canvas, 0, new Phrase(item.getListSymbol()), this.compositeColumn.leftX + listIndentation, this.compositeColumn.firstLineY, 0.0f);
                                                }
                                            }
                                            this.compositeColumn.firstLineYDone = true;
                                        }
                                        if ((status & 1) != 0) {
                                            this.compositeColumn = null;
                                            this.listIdx++;
                                            this.yLine -= item.getSpacingAfter();
                                        }
                                        if ((status & 2) == 0) {
                                            return 2;
                                        }
                                    } else {
                                        this.compositeColumn = null;
                                        this.yLine = lastY;
                                        return 2;
                                    }
                                }
                                firstPass = false;
                                this.yLine = this.compositeColumn.yLine;
                                this.linesWritten += this.compositeColumn.linesWritten;
                                this.descender = this.compositeColumn.descender;
                                this.currentLeading = this.compositeColumn.currentLeading;
                                if (simulate) {
                                    if (isRTL) {
                                        showTextAligned(this.canvas, 0, new Phrase(item.getListSymbol()), this.compositeColumn.leftX + listIndentation, this.compositeColumn.firstLineY, 0.0f);
                                    } else {
                                        showTextAligned(this.canvas, 2, new Phrase(item.getListSymbol()), this.compositeColumn.lastX + item.getIndentationLeft(), this.compositeColumn.firstLineY, 0.0f, this.runDirection, this.arabicOptions);
                                    }
                                }
                                this.compositeColumn.firstLineYDone = true;
                                if ((status & 1) != 0) {
                                    this.compositeColumn = null;
                                    this.listIdx++;
                                    this.yLine -= item.getSpacingAfter();
                                }
                                if ((status & 2) == 0) {
                                    return 2;
                                }
                            } else {
                                count++;
                            }
                        } else if (obj instanceof List) {
                            stack.push(new Object[]{list, Integer.valueOf(k), new Float(listIndentation)});
                            list = (List) obj;
                            items = list.getItems();
                            listIndentation += list.getIndentationLeft();
                            k = -1;
                            k++;
                        }
                        if (k == items.size() - 1 && !stack.isEmpty()) {
                            Object[] objs = (Object[]) stack.pop();
                            list = objs[0];
                            items = list.getItems();
                            k = ((Integer) objs[1]).intValue();
                            listIndentation = ((Float) objs[2]).floatValue();
                        }
                        k++;
                    }
                    status = 0;
                    keep = 0;
                    while (keep < 2) {
                        lastY = this.yLine;
                        createHere = false;
                        if (this.compositeColumn == null) {
                            if (item != null) {
                                this.compositeColumn = new ColumnText(this.canvas);
                                columnText = this.compositeColumn;
                                if (!firstPass) {
                                }
                                columnText.setUseAscender(z);
                                this.compositeColumn.setInheritGraphicState(this.inheritGraphicState);
                                this.compositeColumn.setAlignment(item.getAlignment());
                                this.compositeColumn.setIndent((item.getIndentationLeft() + listIndentation) + item.getFirstLineIndent(), false);
                                this.compositeColumn.setExtraParagraphSpace(item.getExtraParagraphSpace());
                                this.compositeColumn.setFollowingIndent(this.compositeColumn.getIndent());
                                this.compositeColumn.setRightIndent(item.getIndentationRight() + list.getIndentationRight());
                                this.compositeColumn.setLeading(item.getLeading(), item.getMultipliedLeading());
                                this.compositeColumn.setRunDirection(this.runDirection);
                                this.compositeColumn.setArabicOptions(this.arabicOptions);
                                this.compositeColumn.setSpaceCharRatio(this.spaceCharRatio);
                                this.compositeColumn.addText(item);
                                this.yLine -= item.getSpacingBefore();
                                createHere = true;
                            } else {
                                this.listIdx = 0;
                                this.compositeElements.removeFirst();
                            }
                        }
                        this.compositeColumn.leftX = this.leftX;
                        this.compositeColumn.rightX = this.rightX;
                        this.compositeColumn.yLine = this.yLine;
                        this.compositeColumn.rectangularWidth = this.rectangularWidth;
                        this.compositeColumn.rectangularMode = this.rectangularMode;
                        this.compositeColumn.minY = this.minY;
                        this.compositeColumn.maxY = this.maxY;
                        if (!item.getKeepTogether()) {
                        }
                        if (!simulate) {
                        }
                        item.getListLabel().setIndentation(listIndentation);
                        this.canvas.openMCBlock(list);
                        this.canvas.openMCBlock(item);
                        columnText = this.compositeColumn;
                        if (!simulate) {
                        }
                        status = columnText.go(z, item);
                        this.canvas.closeMCBlock(item.getListBody());
                        this.canvas.closeMCBlock(item);
                        this.canvas.closeMCBlock(list);
                        this.lastX = this.compositeColumn.getLastX();
                        updateFilledWidth(this.compositeColumn.filledWidth);
                        if ((status & 1) == 0) {
                        }
                        if (keep == 0) {
                            this.compositeColumn = null;
                            this.yLine = lastY;
                        }
                        keep++;
                    }
                    firstPass = false;
                    this.yLine = this.compositeColumn.yLine;
                    this.linesWritten += this.compositeColumn.linesWritten;
                    this.descender = this.compositeColumn.descender;
                    this.currentLeading = this.compositeColumn.currentLeading;
                    if (simulate) {
                        if (isRTL) {
                            showTextAligned(this.canvas, 2, new Phrase(item.getListSymbol()), this.compositeColumn.lastX + item.getIndentationLeft(), this.compositeColumn.firstLineY, 0.0f, this.runDirection, this.arabicOptions);
                        } else {
                            showTextAligned(this.canvas, 0, new Phrase(item.getListSymbol()), this.compositeColumn.leftX + listIndentation, this.compositeColumn.firstLineY, 0.0f);
                        }
                    }
                    this.compositeColumn.firstLineYDone = true;
                    if ((status & 1) != 0) {
                        this.compositeColumn = null;
                        this.listIdx++;
                        this.yLine -= item.getSpacingAfter();
                    }
                    if ((status & 2) == 0) {
                        return 2;
                    }
                } else if (element.type() == 23) {
                    PdfPTable table = (PdfPTable) element;
                    if (table.size() <= table.getHeaderRows()) {
                        this.compositeElements.removeFirst();
                    } else {
                        float yTemp = this.yLine + this.descender;
                        if (this.rowIdx == 0 && this.adjustFirstLine) {
                            yTemp -= table.spacingBefore();
                        }
                        if (yTemp < this.minY || yTemp > this.maxY) {
                            return 2;
                        }
                        float tableWidth;
                        float yLineWrite = yTemp;
                        float x1 = this.leftX;
                        this.currentLeading = 0.0f;
                        if (table.isLockedWidth()) {
                            tableWidth = table.getTotalWidth();
                            updateFilledWidth(tableWidth);
                        } else {
                            tableWidth = (this.rectangularWidth * table.getWidthPercentage()) / 100.0f;
                            table.setTotalWidth(tableWidth);
                        }
                        table.normalizeHeadersFooters();
                        int headerRows = table.getHeaderRows();
                        int footerRows = table.getFooterRows();
                        int realHeaderRows = headerRows - footerRows;
                        float headerHeight = table.getHeaderHeight();
                        float footerHeight = table.getFooterHeight();
                        boolean skipHeader = table.isSkipFirstHeader() && this.rowIdx <= realHeaderRows && (table.isComplete() || this.rowIdx != realHeaderRows);
                        if (table.isComplete() || table.getTotalHeight() - headerHeight <= yTemp - this.minY) {
                            ArrayList<PdfPRow> rows;
                            int i;
                            if (!skipHeader) {
                                yTemp -= headerHeight;
                                if (yTemp < this.minY || yTemp > this.maxY) {
                                    return 2;
                                }
                            }
                            if (this.rowIdx < headerRows) {
                                this.rowIdx = headerRows;
                            }
                            if (!table.isComplete()) {
                                yTemp -= footerHeight;
                            }
                            FittingRows fittingRows = table.getFittingRows(yTemp - this.minY, this.rowIdx);
                            k = fittingRows.lastRow + 1;
                            yTemp -= fittingRows.height;
                            this.LOGGER.info("Want to split at row " + k);
                            int kTemp = k;
                            while (kTemp > this.rowIdx && kTemp < table.size() && table.getRow(kTemp).isMayNotBreak()) {
                                kTemp--;
                            }
                            if ((kTemp > this.rowIdx && kTemp < k) || (kTemp == 0 && table.getRow(0).isMayNotBreak() && table.isLoopCheck())) {
                                yTemp = this.minY;
                                k = kTemp;
                                table.setLoopCheck(false);
                            }
                            this.LOGGER.info("Will split at row " + k);
                            if (table.isSplitLate() && k > 0) {
                                fittingRows.correctLastRowChosen(table, k - 1);
                            }
                            if (!table.isComplete()) {
                                yTemp += footerHeight;
                            }
                            if (!table.isSplitRows()) {
                                this.splittedRow = -1;
                                if (k == this.rowIdx) {
                                    if (k == table.size()) {
                                        this.compositeElements.removeFirst();
                                    } else {
                                        table.getRows().remove(k);
                                        return 2;
                                    }
                                }
                            } else if (table.isSplitLate() && this.rowIdx < k) {
                                this.splittedRow = -1;
                            } else if (k < table.size()) {
                                yTemp -= fittingRows.completedRowsHeight - fittingRows.height;
                                PdfPRow newRow = table.getRow(k).splitRow(table, k, yTemp - this.minY);
                                if (newRow == null) {
                                    this.LOGGER.info("Didn't split row!");
                                    this.splittedRow = -1;
                                    if (this.rowIdx == k) {
                                        return 2;
                                    }
                                }
                                if (k != this.splittedRow) {
                                    this.splittedRow = k + 1;
                                    PdfPTable pdfPTable = new PdfPTable(table);
                                    this.compositeElements.set(0, pdfPTable);
                                    rows = pdfPTable.getRows();
                                    for (i = headerRows; i < this.rowIdx; i++) {
                                        rows.set(i, null);
                                    }
                                    table = pdfPTable;
                                }
                                yTemp = this.minY;
                                k++;
                                table.getRows().add(k, newRow);
                                this.LOGGER.info("Inserting row at position " + k);
                            }
                            firstPass = false;
                            if (!simulate) {
                                PdfPTableEvent tableEvent;
                                switch (table.getHorizontalAlignment()) {
                                    case 1:
                                        x1 += (this.rectangularWidth - tableWidth) / BaseField.BORDER_WIDTH_MEDIUM;
                                        break;
                                    case 2:
                                        if (!isRTL) {
                                            x1 += this.rectangularWidth - tableWidth;
                                            break;
                                        }
                                        break;
                                    default:
                                        if (isRTL) {
                                            x1 += this.rectangularWidth - tableWidth;
                                            break;
                                        }
                                        break;
                                }
                                PdfPTable nt = PdfPTable.shallowCopy(table);
                                ArrayList<PdfPRow> sub = nt.getRows();
                                if (skipHeader || realHeaderRows <= 0) {
                                    nt.setHeaderRows(footerRows);
                                } else {
                                    rows = table.getRows(0, realHeaderRows);
                                    if (isTagged(this.canvas)) {
                                        nt.getHeader().rows = rows;
                                    }
                                    sub.addAll(rows);
                                }
                                rows = table.getRows(this.rowIdx, k);
                                if (isTagged(this.canvas)) {
                                    nt.getBody().rows = rows;
                                }
                                sub.addAll(rows);
                                boolean showFooter = !table.isSkipLastFooter();
                                boolean newPageFollows = false;
                                if (k < table.size()) {
                                    nt.setComplete(true);
                                    showFooter = true;
                                    newPageFollows = true;
                                }
                                if (footerRows > 0 && nt.isComplete() && showFooter) {
                                    rows = table.getRows(realHeaderRows, realHeaderRows + footerRows);
                                    if (isTagged(this.canvas)) {
                                        nt.getFooter().rows = rows;
                                    }
                                    sub.addAll(rows);
                                } else {
                                    footerRows = 0;
                                }
                                float rowHeight = 0.0f;
                                int lastIdx = (sub.size() - 1) - footerRows;
                                PdfPRow last = (PdfPRow) sub.get(lastIdx);
                                if (table.isExtendLastRow(newPageFollows)) {
                                    rowHeight = last.getMaxHeights();
                                    last.setMaxHeights((yTemp - this.minY) + rowHeight);
                                    yTemp = this.minY;
                                }
                                if (newPageFollows) {
                                    tableEvent = table.getTableEvent();
                                    if (tableEvent instanceof PdfPTableEventSplit) {
                                        ((PdfPTableEventSplit) tableEvent).splitTable(table);
                                    }
                                }
                                if (this.canvases != null) {
                                    if (isTagged(this.canvases[3])) {
                                        this.canvases[3].openMCBlock(table);
                                    }
                                    nt.writeSelectedRows(0, -1, 0, -1, x1, yLineWrite, this.canvases, false);
                                    if (isTagged(this.canvases[3])) {
                                        this.canvases[3].closeMCBlock(table);
                                    }
                                } else {
                                    if (isTagged(this.canvas)) {
                                        this.canvas.openMCBlock(table);
                                    }
                                    nt.writeSelectedRows(0, -1, 0, -1, x1, yLineWrite, this.canvas, false);
                                    if (isTagged(this.canvas)) {
                                        this.canvas.closeMCBlock(table);
                                    }
                                }
                                if (!table.isComplete()) {
                                    table.addNumberOfRowsWritten(k);
                                }
                                if (this.splittedRow == k && k < table.size()) {
                                    ((PdfPRow) table.getRows().get(k)).copyRowContent(nt, lastIdx);
                                } else if (k > 0 && k < table.size()) {
                                    table.getRow(k).splitRowspans(table, k - 1, nt, lastIdx);
                                }
                                if (table.isExtendLastRow(newPageFollows)) {
                                    last.setMaxHeights(rowHeight);
                                }
                                if (newPageFollows) {
                                    tableEvent = table.getTableEvent();
                                    if (tableEvent instanceof PdfPTableEventAfterSplit) {
                                        ((PdfPTableEventAfterSplit) tableEvent).afterSplitTable(table, table.getRow(k), k);
                                    }
                                }
                            } else if (table.isExtendLastRow() && this.minY > PdfPRow.BOTTOM_LIMIT) {
                                yTemp = this.minY;
                            }
                            this.yLine = yTemp;
                            this.descender = 0.0f;
                            this.currentLeading = 0.0f;
                            if (!(skipHeader || table.isComplete())) {
                                this.yLine += footerHeight;
                            }
                            while (k < table.size() && table.getRowHeight(k) <= 0.0f && !table.hasRowspan(k)) {
                                k++;
                            }
                            if (k >= table.size()) {
                                if (this.yLine - table.spacingAfter() < this.minY) {
                                    this.yLine = this.minY;
                                } else {
                                    this.yLine -= table.spacingAfter();
                                }
                                this.compositeElements.removeFirst();
                                this.splittedRow = -1;
                                this.rowIdx = 0;
                            } else {
                                if (this.splittedRow != -1) {
                                    rows = table.getRows();
                                    for (i = this.rowIdx; i < k; i++) {
                                        rows.set(i, null);
                                    }
                                }
                                this.rowIdx = k;
                                return 2;
                            }
                        }
                        table.setSkipFirstHeader(false);
                        return 2;
                    }
                } else if (element.type() == 55) {
                    if (!simulate) {
                        ((DrawInterface) element).draw(this.canvas, this.leftX, this.minY, this.rightX, this.maxY, this.yLine);
                    }
                    this.compositeElements.removeFirst();
                } else if (element.type() == 37) {
                    FloatLayout floatLayout;
                    ArrayList<Element> floatingElements = new ArrayList();
                    do {
                        floatingElements.add(element);
                        this.compositeElements.removeFirst();
                        element = !this.compositeElements.isEmpty() ? (Element) this.compositeElements.getFirst() : null;
                        if (element != null) {
                        }
                        floatLayout = new FloatLayout(floatingElements, this.useAscender);
                        floatLayout.setSimpleColumn(this.leftX, this.minY, this.rightX, this.yLine);
                        status = floatLayout.layout(this.canvas, simulate);
                        this.yLine = floatLayout.getYLine();
                        this.descender = 0.0f;
                        if ((status & 1) == 0) {
                            this.compositeElements.addAll(floatingElements);
                            return status;
                        }
                    } while (element.type() == 37);
                    floatLayout = new FloatLayout(floatingElements, this.useAscender);
                    floatLayout.setSimpleColumn(this.leftX, this.minY, this.rightX, this.yLine);
                    status = floatLayout.layout(this.canvas, simulate);
                    this.yLine = floatLayout.getYLine();
                    this.descender = 0.0f;
                    if ((status & 1) == 0) {
                        this.compositeElements.addAll(floatingElements);
                        return status;
                    }
                } else {
                    this.compositeElements.removeFirst();
                }
            }
            return 1;
        }
        throw new DocumentException(MessageLocalization.getComposedMessage("irregular.columns.are.not.supported.in.composite.mode", new Object[0]));
    }

    public PdfContentByte getCanvas() {
        return this.canvas;
    }

    public void setCanvas(PdfContentByte canvas) {
        this.canvas = canvas;
        this.canvases = null;
        if (this.compositeColumn != null) {
            this.compositeColumn.setCanvas(canvas);
        }
    }

    public void setCanvases(PdfContentByte[] canvases) {
        this.canvases = canvases;
        this.canvas = canvases[3];
        if (this.compositeColumn != null) {
            this.compositeColumn.setCanvases(canvases);
        }
    }

    public PdfContentByte[] getCanvases() {
        return this.canvases;
    }

    public boolean zeroHeightElement() {
        return this.composite && !this.compositeElements.isEmpty() && ((Element) this.compositeElements.getFirst()).type() == 55;
    }

    public java.util.List<Element> getCompositeElements() {
        return this.compositeElements;
    }

    public boolean isUseAscender() {
        return this.useAscender;
    }

    public void setUseAscender(boolean useAscender) {
        this.useAscender = useAscender;
    }

    public static boolean hasMoreText(int status) {
        return (status & 1) == 0;
    }

    public float getFilledWidth() {
        return this.filledWidth;
    }

    public void setFilledWidth(float filledWidth) {
        this.filledWidth = filledWidth;
    }

    public void updateFilledWidth(float w) {
        if (w > this.filledWidth) {
            this.filledWidth = w;
        }
    }

    public boolean isAdjustFirstLine() {
        return this.adjustFirstLine;
    }

    public void setAdjustFirstLine(boolean adjustFirstLine) {
        this.adjustFirstLine = adjustFirstLine;
    }

    private static boolean isTagged(PdfContentByte canvas) {
        return (canvas == null || canvas.pdf == null || canvas.writer == null || !canvas.writer.isTagged()) ? false : true;
    }
}
