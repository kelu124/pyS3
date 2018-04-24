package com.itextpdf.text.pdf;

import com.itextpdf.awt.geom.AffineTransform;
import com.itextpdf.text.AccessibleElementId;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.error_messages.MessageLocalization;
import com.itextpdf.text.pdf.interfaces.IAccessibleElement;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;

public class PdfAnnotation extends PdfDictionary implements IAccessibleElement {
    public static final PdfName AA_BLUR = PdfName.BL;
    public static final PdfName AA_DOWN = PdfName.f120D;
    public static final PdfName AA_ENTER = PdfName.f121E;
    public static final PdfName AA_EXIT = PdfName.f138X;
    public static final PdfName AA_FOCUS = PdfName.FO;
    public static final PdfName AA_JS_CHANGE = PdfName.f136V;
    public static final PdfName AA_JS_FORMAT = PdfName.f122F;
    public static final PdfName AA_JS_KEY = PdfName.f125K;
    public static final PdfName AA_JS_OTHER_CHANGE = PdfName.f119C;
    public static final PdfName AA_UP = PdfName.f135U;
    public static final PdfName APPEARANCE_DOWN = PdfName.f120D;
    public static final PdfName APPEARANCE_NORMAL = PdfName.f128N;
    public static final PdfName APPEARANCE_ROLLOVER = PdfName.f132R;
    public static final int FLAGS_HIDDEN = 2;
    public static final int FLAGS_INVISIBLE = 1;
    public static final int FLAGS_LOCKED = 128;
    public static final int FLAGS_LOCKEDCONTENTS = 512;
    public static final int FLAGS_NOROTATE = 16;
    public static final int FLAGS_NOVIEW = 32;
    public static final int FLAGS_NOZOOM = 8;
    public static final int FLAGS_PRINT = 4;
    public static final int FLAGS_READONLY = 64;
    public static final int FLAGS_TOGGLENOVIEW = 256;
    public static final PdfName HIGHLIGHT_INVERT = PdfName.f124I;
    public static final PdfName HIGHLIGHT_NONE = PdfName.f128N;
    public static final PdfName HIGHLIGHT_OUTLINE = PdfName.f129O;
    public static final PdfName HIGHLIGHT_PUSH = PdfName.f130P;
    public static final PdfName HIGHLIGHT_TOGGLE = PdfName.f134T;
    public static final int MARKUP_HIGHLIGHT = 0;
    public static final int MARKUP_SQUIGGLY = 3;
    public static final int MARKUP_STRIKEOUT = 2;
    public static final int MARKUP_UNDERLINE = 1;
    protected HashMap<PdfName, PdfObject> accessibleAttributes = null;
    protected boolean annotation = true;
    protected boolean form = false;
    private AccessibleElementId id = null;
    private int placeInPage = -1;
    protected PdfIndirectReference reference;
    protected PdfName role = null;
    protected HashSet<PdfTemplate> templates;
    protected boolean used = false;
    protected PdfWriter writer;

    public static class PdfImportedLink {
        PdfArray destination = null;
        float llx;
        float lly;
        int newPage = 0;
        HashMap<PdfName, PdfObject> parameters = new HashMap();
        PdfArray rect;
        float urx;
        float ury;

        PdfImportedLink(PdfDictionary annotation) {
            this.parameters.putAll(annotation.hashMap);
            try {
                this.destination = (PdfArray) this.parameters.remove(PdfName.DEST);
                if (this.destination != null) {
                    this.destination = new PdfArray(this.destination);
                }
                PdfArray rc = (PdfArray) this.parameters.remove(PdfName.RECT);
                this.llx = rc.getAsNumber(0).floatValue();
                this.lly = rc.getAsNumber(1).floatValue();
                this.urx = rc.getAsNumber(2).floatValue();
                this.ury = rc.getAsNumber(3).floatValue();
                this.rect = new PdfArray(rc);
            } catch (ClassCastException e) {
                throw new IllegalArgumentException(MessageLocalization.getComposedMessage("you.have.to.consolidate.the.named.destinations.of.your.reader", new Object[0]));
            }
        }

        public Map<PdfName, PdfObject> getParameters() {
            return new HashMap(this.parameters);
        }

        public PdfArray getRect() {
            return new PdfArray(this.rect);
        }

        public boolean isInternal() {
            return this.destination != null;
        }

        public int getDestinationPage() {
            if (!isInternal()) {
                return 0;
            }
            PRIndirectReference pr = (PRIndirectReference) this.destination.getAsIndirectObject(0);
            PdfReader r = pr.getReader();
            for (int i = 1; i <= r.getNumberOfPages(); i++) {
                PRIndirectReference pp = r.getPageOrigRef(i);
                if (pp.getGeneration() == pr.getGeneration() && pp.getNumber() == pr.getNumber()) {
                    return i;
                }
            }
            throw new IllegalArgumentException(MessageLocalization.getComposedMessage("page.not.found", new Object[0]));
        }

        public void setDestinationPage(int newPage) {
            if (isInternal()) {
                this.newPage = newPage;
                return;
            }
            throw new IllegalArgumentException(MessageLocalization.getComposedMessage("cannot.change.destination.of.external.link", new Object[0]));
        }

        public void transformDestination(float a, float b, float c, float d, float e, float f) {
            if (!isInternal()) {
                throw new IllegalArgumentException(MessageLocalization.getComposedMessage("cannot.change.destination.of.external.link", new Object[0]));
            } else if (this.destination.getAsName(1).equals(PdfName.XYZ)) {
                float x = this.destination.getAsNumber(2).floatValue();
                float y = this.destination.getAsNumber(3).floatValue();
                float yy = ((x * b) + (y * d)) + f;
                this.destination.set(2, new PdfNumber(((x * a) + (y * c)) + e));
                this.destination.set(3, new PdfNumber(yy));
            }
        }

        public void transformRect(float a, float b, float c, float d, float e, float f) {
            float y = ((this.llx * b) + (this.lly * d)) + f;
            this.llx = ((this.llx * a) + (this.lly * c)) + e;
            this.lly = y;
            y = ((this.urx * b) + (this.ury * d)) + f;
            this.urx = ((this.urx * a) + (this.ury * c)) + e;
            this.ury = y;
        }

        public PdfAnnotation createAnnotation(PdfWriter writer) {
            PdfAnnotation annotation = writer.createAnnotation(new Rectangle(this.llx, this.lly, this.urx, this.ury), null);
            if (this.newPage != 0) {
                this.destination.set(0, writer.getPageReference(this.newPage));
            }
            if (this.destination != null) {
                annotation.put(PdfName.DEST, this.destination);
            }
            annotation.hashMap.putAll(this.parameters);
            return annotation;
        }

        public String toString() {
            StringBuffer buf = new StringBuffer("Imported link: location [");
            buf.append(this.llx);
            buf.append(' ');
            buf.append(this.lly);
            buf.append(' ');
            buf.append(this.urx);
            buf.append(' ');
            buf.append(this.ury);
            buf.append("] destination ");
            buf.append(this.destination);
            buf.append(" parameters ");
            buf.append(this.parameters);
            if (this.parameters != null) {
                appendDictionary(buf, this.parameters);
            }
            return buf.toString();
        }

        private void appendDictionary(StringBuffer buf, HashMap<PdfName, PdfObject> dict) {
            buf.append(" <<");
            for (Entry<PdfName, PdfObject> entry : dict.entrySet()) {
                buf.append(entry.getKey());
                buf.append(":");
                if (entry.getValue() instanceof PdfDictionary) {
                    appendDictionary(buf, ((PdfDictionary) entry.getValue()).hashMap);
                } else {
                    buf.append(entry.getValue());
                }
                buf.append(" ");
            }
            buf.append(">> ");
        }
    }

    public PdfAnnotation(PdfWriter writer, Rectangle rect) {
        this.writer = writer;
        if (rect != null) {
            put(PdfName.RECT, new PdfRectangle(rect));
        }
    }

    public PdfAnnotation(PdfWriter writer, float llx, float lly, float urx, float ury, PdfString title, PdfString content) {
        this.writer = writer;
        put(PdfName.SUBTYPE, PdfName.TEXT);
        put(PdfName.f134T, title);
        put(PdfName.RECT, new PdfRectangle(llx, lly, urx, ury));
        put(PdfName.CONTENTS, content);
    }

    public PdfAnnotation(PdfWriter writer, float llx, float lly, float urx, float ury, PdfAction action) {
        this.writer = writer;
        put(PdfName.SUBTYPE, PdfName.LINK);
        put(PdfName.RECT, new PdfRectangle(llx, lly, urx, ury));
        put(PdfName.f117A, action);
        put(PdfName.BORDER, new PdfBorderArray(0.0f, 0.0f, 0.0f));
        put(PdfName.f119C, new PdfColor(0, 0, 255));
    }

    public static PdfAnnotation createScreen(PdfWriter writer, Rectangle rect, String clipTitle, PdfFileSpecification fs, String mimeType, boolean playOnDisplay) throws IOException {
        PdfAnnotation ann = writer.createAnnotation(rect, PdfName.SCREEN);
        ann.put(PdfName.f122F, new PdfNumber(4));
        ann.put(PdfName.TYPE, PdfName.ANNOT);
        ann.setPage();
        PdfIndirectReference actionRef = writer.addToBody(PdfAction.rendition(clipTitle, fs, mimeType, ann.getIndirectReference())).getIndirectReference();
        if (playOnDisplay) {
            PdfDictionary aa = new PdfDictionary();
            aa.put(new PdfName("PV"), actionRef);
            ann.put(PdfName.AA, aa);
        }
        ann.put(PdfName.f117A, actionRef);
        return ann;
    }

    public PdfIndirectReference getIndirectReference() {
        if (this.reference == null) {
            this.reference = this.writer.getPdfIndirectReference();
        }
        return this.reference;
    }

    public static PdfAnnotation createText(PdfWriter writer, Rectangle rect, String title, String contents, boolean open, String icon) {
        PdfAnnotation annot = writer.createAnnotation(rect, PdfName.TEXT);
        if (title != null) {
            annot.put(PdfName.f134T, new PdfString(title, PdfObject.TEXT_UNICODE));
        }
        if (contents != null) {
            annot.put(PdfName.CONTENTS, new PdfString(contents, PdfObject.TEXT_UNICODE));
        }
        if (open) {
            annot.put(PdfName.OPEN, PdfBoolean.PDFTRUE);
        }
        if (icon != null) {
            annot.put(PdfName.NAME, new PdfName(icon));
        }
        return annot;
    }

    protected static PdfAnnotation createLink(PdfWriter writer, Rectangle rect, PdfName highlight) {
        PdfAnnotation annot = writer.createAnnotation(rect, PdfName.LINK);
        if (!highlight.equals(HIGHLIGHT_INVERT)) {
            annot.put(PdfName.f123H, highlight);
        }
        return annot;
    }

    public static PdfAnnotation createLink(PdfWriter writer, Rectangle rect, PdfName highlight, PdfAction action) {
        PdfAnnotation annot = createLink(writer, rect, highlight);
        annot.putEx(PdfName.f117A, action);
        return annot;
    }

    public static PdfAnnotation createLink(PdfWriter writer, Rectangle rect, PdfName highlight, String namedDestination) {
        PdfAnnotation annot = createLink(writer, rect, highlight);
        annot.put(PdfName.DEST, new PdfString(namedDestination, PdfObject.TEXT_UNICODE));
        return annot;
    }

    public static PdfAnnotation createLink(PdfWriter writer, Rectangle rect, PdfName highlight, int page, PdfDestination dest) {
        PdfAnnotation annot = createLink(writer, rect, highlight);
        dest.addPage(writer.getPageReference(page));
        annot.put(PdfName.DEST, dest);
        return annot;
    }

    public static PdfAnnotation createFreeText(PdfWriter writer, Rectangle rect, String contents, PdfContentByte defaultAppearance) {
        PdfAnnotation annot = writer.createAnnotation(rect, PdfName.FREETEXT);
        annot.put(PdfName.CONTENTS, new PdfString(contents, PdfObject.TEXT_UNICODE));
        annot.setDefaultAppearanceString(defaultAppearance);
        return annot;
    }

    public static PdfAnnotation createLine(PdfWriter writer, Rectangle rect, String contents, float x1, float y1, float x2, float y2) {
        PdfAnnotation annot = writer.createAnnotation(rect, PdfName.LINE);
        annot.put(PdfName.CONTENTS, new PdfString(contents, PdfObject.TEXT_UNICODE));
        PdfArray array = new PdfArray(new PdfNumber(x1));
        array.add(new PdfNumber(y1));
        array.add(new PdfNumber(x2));
        array.add(new PdfNumber(y2));
        annot.put(PdfName.f126L, array);
        return annot;
    }

    public static PdfAnnotation createSquareCircle(PdfWriter writer, Rectangle rect, String contents, boolean square) {
        PdfAnnotation annot;
        if (square) {
            annot = writer.createAnnotation(rect, PdfName.SQUARE);
        } else {
            annot = writer.createAnnotation(rect, PdfName.CIRCLE);
        }
        annot.put(PdfName.CONTENTS, new PdfString(contents, PdfObject.TEXT_UNICODE));
        return annot;
    }

    public static PdfAnnotation createMarkup(PdfWriter writer, Rectangle rect, String contents, int type, float[] quadPoints) {
        PdfName name = PdfName.HIGHLIGHT;
        switch (type) {
            case 1:
                name = PdfName.UNDERLINE;
                break;
            case 2:
                name = PdfName.STRIKEOUT;
                break;
            case 3:
                name = PdfName.SQUIGGLY;
                break;
        }
        PdfAnnotation annot = writer.createAnnotation(rect, name);
        annot.put(PdfName.CONTENTS, new PdfString(contents, PdfObject.TEXT_UNICODE));
        PdfArray array = new PdfArray();
        for (float pdfNumber : quadPoints) {
            array.add(new PdfNumber(pdfNumber));
        }
        annot.put(PdfName.QUADPOINTS, array);
        return annot;
    }

    public static PdfAnnotation createStamp(PdfWriter writer, Rectangle rect, String contents, String name) {
        PdfAnnotation annot = writer.createAnnotation(rect, PdfName.STAMP);
        annot.put(PdfName.CONTENTS, new PdfString(contents, PdfObject.TEXT_UNICODE));
        annot.put(PdfName.NAME, new PdfName(name));
        return annot;
    }

    public static PdfAnnotation createInk(PdfWriter writer, Rectangle rect, String contents, float[][] inkList) {
        PdfAnnotation annot = writer.createAnnotation(rect, PdfName.INK);
        annot.put(PdfName.CONTENTS, new PdfString(contents, PdfObject.TEXT_UNICODE));
        PdfArray outer = new PdfArray();
        for (float[] deep : inkList) {
            PdfArray inner = new PdfArray();
            for (float pdfNumber : deep) {
                inner.add(new PdfNumber(pdfNumber));
            }
            outer.add(inner);
        }
        annot.put(PdfName.INKLIST, outer);
        return annot;
    }

    public static PdfAnnotation createFileAttachment(PdfWriter writer, Rectangle rect, String contents, byte[] fileStore, String file, String fileDisplay) throws IOException {
        return createFileAttachment(writer, rect, contents, PdfFileSpecification.fileEmbedded(writer, file, fileDisplay, fileStore));
    }

    public static PdfAnnotation createFileAttachment(PdfWriter writer, Rectangle rect, String contents, PdfFileSpecification fs) throws IOException {
        PdfAnnotation annot = writer.createAnnotation(rect, PdfName.FILEATTACHMENT);
        if (contents != null) {
            annot.put(PdfName.CONTENTS, new PdfString(contents, PdfObject.TEXT_UNICODE));
        }
        annot.put(PdfName.FS, fs.getReference());
        return annot;
    }

    public static PdfAnnotation createPopup(PdfWriter writer, Rectangle rect, String contents, boolean open) {
        PdfAnnotation annot = writer.createAnnotation(rect, PdfName.POPUP);
        if (contents != null) {
            annot.put(PdfName.CONTENTS, new PdfString(contents, PdfObject.TEXT_UNICODE));
        }
        if (open) {
            annot.put(PdfName.OPEN, PdfBoolean.PDFTRUE);
        }
        return annot;
    }

    public static PdfAnnotation createPolygonPolyline(PdfWriter writer, Rectangle rect, String contents, boolean polygon, PdfArray vertices) {
        PdfAnnotation annot;
        if (polygon) {
            annot = writer.createAnnotation(rect, PdfName.POLYGON);
        } else {
            annot = writer.createAnnotation(rect, PdfName.POLYLINE);
        }
        annot.put(PdfName.CONTENTS, new PdfString(contents, PdfObject.TEXT_UNICODE));
        annot.put(PdfName.VERTICES, new PdfArray(vertices));
        return annot;
    }

    public void setDefaultAppearanceString(PdfContentByte cb) {
        byte[] b = cb.getInternalBuffer().toByteArray();
        int len = b.length;
        for (int k = 0; k < len; k++) {
            if (b[k] == (byte) 10) {
                b[k] = (byte) 32;
            }
        }
        put(PdfName.DA, new PdfString(b));
    }

    public void setFlags(int flags) {
        if (flags == 0) {
            remove(PdfName.f122F);
        } else {
            put(PdfName.f122F, new PdfNumber(flags));
        }
    }

    public void setBorder(PdfBorderArray border) {
        put(PdfName.BORDER, border);
    }

    public void setBorderStyle(PdfBorderDictionary border) {
        put(PdfName.BS, border);
    }

    public void setHighlighting(PdfName highlight) {
        if (highlight.equals(HIGHLIGHT_INVERT)) {
            remove(PdfName.f123H);
        } else {
            put(PdfName.f123H, highlight);
        }
    }

    public void setAppearance(PdfName ap, PdfTemplate template) {
        PdfDictionary dic = (PdfDictionary) get(PdfName.AP);
        if (dic == null) {
            dic = new PdfDictionary();
        }
        dic.put(ap, template.getIndirectReference());
        put(PdfName.AP, dic);
        if (this.form) {
            if (this.templates == null) {
                this.templates = new HashSet();
            }
            this.templates.add(template);
        }
    }

    public void setAppearance(PdfName ap, String state, PdfTemplate template) {
        PdfDictionary dic;
        PdfDictionary dicAp = (PdfDictionary) get(PdfName.AP);
        if (dicAp == null) {
            dicAp = new PdfDictionary();
        }
        PdfObject obj = dicAp.get(ap);
        if (obj == null || !obj.isDictionary()) {
            dic = new PdfDictionary();
        } else {
            dic = (PdfDictionary) obj;
        }
        dic.put(new PdfName(state), template.getIndirectReference());
        dicAp.put(ap, dic);
        put(PdfName.AP, dicAp);
        if (this.form) {
            if (this.templates == null) {
                this.templates = new HashSet();
            }
            this.templates.add(template);
        }
    }

    public void setAppearanceState(String state) {
        if (state == null) {
            remove(PdfName.AS);
        } else {
            put(PdfName.AS, new PdfName(state));
        }
    }

    public void setColor(BaseColor color) {
        put(PdfName.f119C, new PdfColor(color));
    }

    public void setTitle(String title) {
        if (title == null) {
            remove(PdfName.f134T);
        } else {
            put(PdfName.f134T, new PdfString(title, PdfObject.TEXT_UNICODE));
        }
    }

    public void setPopup(PdfAnnotation popup) {
        put(PdfName.POPUP, popup.getIndirectReference());
        popup.put(PdfName.PARENT, getIndirectReference());
    }

    public void setAction(PdfAction action) {
        put(PdfName.f117A, action);
    }

    public void setAdditionalActions(PdfName key, PdfAction action) {
        PdfDictionary dic;
        PdfObject obj = get(PdfName.AA);
        if (obj == null || !obj.isDictionary()) {
            dic = new PdfDictionary();
        } else {
            dic = (PdfDictionary) obj;
        }
        dic.put(key, action);
        put(PdfName.AA, dic);
    }

    public boolean isUsed() {
        return this.used;
    }

    public void setUsed() {
        this.used = true;
    }

    public HashSet<PdfTemplate> getTemplates() {
        return this.templates;
    }

    public boolean isForm() {
        return this.form;
    }

    public boolean isAnnotation() {
        return this.annotation;
    }

    public void setPage(int page) {
        put(PdfName.f130P, this.writer.getPageReference(page));
    }

    public void setPage() {
        put(PdfName.f130P, this.writer.getCurrentPage());
    }

    public int getPlaceInPage() {
        return this.placeInPage;
    }

    public void setPlaceInPage(int placeInPage) {
        this.placeInPage = placeInPage;
    }

    public void setRotate(int v) {
        put(PdfName.ROTATE, new PdfNumber(v));
    }

    PdfDictionary getMK() {
        PdfDictionary mk = (PdfDictionary) get(PdfName.MK);
        if (mk != null) {
            return mk;
        }
        mk = new PdfDictionary();
        put(PdfName.MK, mk);
        return mk;
    }

    public void setMKRotation(int rotation) {
        getMK().put(PdfName.f132R, new PdfNumber(rotation));
    }

    public static PdfArray getMKColor(BaseColor color) {
        PdfArray array = new PdfArray();
        switch (ExtendedColor.getType(color)) {
            case 1:
                array.add(new PdfNumber(((GrayColor) color).getGray()));
                break;
            case 2:
                CMYKColor cmyk = (CMYKColor) color;
                array.add(new PdfNumber(cmyk.getCyan()));
                array.add(new PdfNumber(cmyk.getMagenta()));
                array.add(new PdfNumber(cmyk.getYellow()));
                array.add(new PdfNumber(cmyk.getBlack()));
                break;
            case 3:
            case 4:
            case 5:
                throw new RuntimeException(MessageLocalization.getComposedMessage("separations.patterns.and.shadings.are.not.allowed.in.mk.dictionary", new Object[0]));
            default:
                array.add(new PdfNumber(((float) color.getRed()) / 255.0f));
                array.add(new PdfNumber(((float) color.getGreen()) / 255.0f));
                array.add(new PdfNumber(((float) color.getBlue()) / 255.0f));
                break;
        }
        return array;
    }

    public void setMKBorderColor(BaseColor color) {
        if (color == null) {
            getMK().remove(PdfName.BC);
        } else {
            getMK().put(PdfName.BC, getMKColor(color));
        }
    }

    public void setMKBackgroundColor(BaseColor color) {
        if (color == null) {
            getMK().remove(PdfName.BG);
        } else {
            getMK().put(PdfName.BG, getMKColor(color));
        }
    }

    public void setMKNormalCaption(String caption) {
        getMK().put(PdfName.CA, new PdfString(caption, PdfObject.TEXT_UNICODE));
    }

    public void setMKRolloverCaption(String caption) {
        getMK().put(PdfName.RC, new PdfString(caption, PdfObject.TEXT_UNICODE));
    }

    public void setMKAlternateCaption(String caption) {
        getMK().put(PdfName.AC, new PdfString(caption, PdfObject.TEXT_UNICODE));
    }

    public void setMKNormalIcon(PdfTemplate template) {
        getMK().put(PdfName.f124I, template.getIndirectReference());
    }

    public void setMKRolloverIcon(PdfTemplate template) {
        getMK().put(PdfName.RI, template.getIndirectReference());
    }

    public void setMKAlternateIcon(PdfTemplate template) {
        getMK().put(PdfName.IX, template.getIndirectReference());
    }

    public void setMKIconFit(PdfName scale, PdfName scalingType, float leftoverLeft, float leftoverBottom, boolean fitInBounds) {
        PdfDictionary dic = new PdfDictionary();
        if (!scale.equals(PdfName.f117A)) {
            dic.put(PdfName.SW, scale);
        }
        if (!scalingType.equals(PdfName.f130P)) {
            dic.put(PdfName.f133S, scalingType);
        }
        if (!(leftoverLeft == 0.5f && leftoverBottom == 0.5f)) {
            PdfArray array = new PdfArray(new PdfNumber(leftoverLeft));
            array.add(new PdfNumber(leftoverBottom));
            dic.put(PdfName.f117A, array);
        }
        if (fitInBounds) {
            dic.put(PdfName.FB, PdfBoolean.PDFTRUE);
        }
        getMK().put(PdfName.IF, dic);
    }

    public void setMKTextPosition(int tp) {
        getMK().put(PdfName.TP, new PdfNumber(tp));
    }

    public void setLayer(PdfOCG layer) {
        put(PdfName.OC, layer.getRef());
    }

    public void setName(String name) {
        put(PdfName.NM, new PdfString(name));
    }

    public void applyCTM(AffineTransform ctm) {
        PdfArray origRect = getAsArray(PdfName.RECT);
        if (origRect != null) {
            PdfRectangle rect;
            if (origRect.size() == 4) {
                rect = new PdfRectangle(origRect.getAsNumber(0).floatValue(), origRect.getAsNumber(1).floatValue(), origRect.getAsNumber(2).floatValue(), origRect.getAsNumber(3).floatValue());
            } else {
                rect = new PdfRectangle(origRect.getAsNumber(0).floatValue(), origRect.getAsNumber(1).floatValue());
            }
            put(PdfName.RECT, rect.transform(ctm));
        }
    }

    public void toPdf(PdfWriter writer, OutputStream os) throws IOException {
        PdfWriter.checkPdfIsoConformance(writer, 13, this);
        super.toPdf(writer, os);
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
        if (this.id == null) {
            this.id = new AccessibleElementId();
        }
        return this.id;
    }

    public void setId(AccessibleElementId id) {
        this.id = id;
    }

    public boolean isInline() {
        return false;
    }
}
