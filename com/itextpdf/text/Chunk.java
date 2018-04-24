package com.itextpdf.text;

import com.itextpdf.text.error_messages.MessageLocalization;
import com.itextpdf.text.pdf.BaseField;
import com.itextpdf.text.pdf.HyphenationEvent;
import com.itextpdf.text.pdf.PdfAction;
import com.itextpdf.text.pdf.PdfAnnotation;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfObject;
import com.itextpdf.text.pdf.PdfString;
import com.itextpdf.text.pdf.draw.DrawInterface;
import com.itextpdf.text.pdf.interfaces.IAccessibleElement;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Chunk implements Element, IAccessibleElement {
    public static final String ACTION = "ACTION";
    public static final String BACKGROUND = "BACKGROUND";
    public static final String CHAR_SPACING = "CHAR_SPACING";
    public static final String COLOR = "COLOR";
    public static final String ENCODING = "ENCODING";
    public static final String GENERICTAG = "GENERICTAG";
    public static final String HSCALE = "HSCALE";
    public static final String HYPHENATION = "HYPHENATION";
    public static final String IMAGE = "IMAGE";
    public static final String LINEHEIGHT = "LINEHEIGHT";
    public static final String LOCALDESTINATION = "LOCALDESTINATION";
    public static final String LOCALGOTO = "LOCALGOTO";
    public static final Chunk NEWLINE = new Chunk("\n");
    public static final String NEWPAGE = "NEWPAGE";
    public static final Chunk NEXTPAGE = new Chunk("");
    public static final String OBJECT_REPLACEMENT_CHARACTER = "￼";
    public static final String PDFANNOTATION = "PDFANNOTATION";
    public static final String REMOTEGOTO = "REMOTEGOTO";
    public static final String SEPARATOR = "SEPARATOR";
    public static final String SKEW = "SKEW";
    public static final Chunk SPACETABBING = new Chunk(Float.valueOf(Float.NaN), true);
    public static final String SPLITCHARACTER = "SPLITCHARACTER";
    public static final String SUBSUPSCRIPT = "SUBSUPSCRIPT";
    public static final String TAB = "TAB";
    public static final Chunk TABBING = new Chunk(Float.valueOf(Float.NaN), false);
    public static final String TABSETTINGS = "TABSETTINGS";
    public static final String TEXTRENDERMODE = "TEXTRENDERMODE";
    public static final String UNDERLINE = "UNDERLINE";
    public static final String WHITESPACE = "WHITESPACE";
    public static final String WORD_SPACING = "WORD_SPACING";
    protected HashMap<PdfName, PdfObject> accessibleAttributes;
    protected HashMap<String, Object> attributes;
    protected StringBuffer content;
    private String contentWithNoTabs;
    protected Font font;
    private AccessibleElementId id;
    protected PdfName role;

    static {
        NEWLINE.setRole(PdfName.P);
        NEXTPAGE.setNewPage();
    }

    public Chunk() {
        this.content = null;
        this.font = null;
        this.attributes = null;
        this.role = null;
        this.accessibleAttributes = null;
        this.id = null;
        this.contentWithNoTabs = null;
        this.content = new StringBuffer();
        this.font = new Font();
        this.role = PdfName.SPAN;
    }

    public Chunk(Chunk ck) {
        this.content = null;
        this.font = null;
        this.attributes = null;
        this.role = null;
        this.accessibleAttributes = null;
        this.id = null;
        this.contentWithNoTabs = null;
        if (ck.content != null) {
            this.content = new StringBuffer(ck.content.toString());
        }
        if (ck.font != null) {
            this.font = new Font(ck.font);
        }
        if (ck.attributes != null) {
            this.attributes = new HashMap(ck.attributes);
        }
        this.role = ck.role;
        if (ck.accessibleAttributes != null) {
            this.accessibleAttributes = new HashMap(ck.accessibleAttributes);
        }
        this.id = ck.getId();
    }

    public Chunk(String content, Font font) {
        this.content = null;
        this.font = null;
        this.attributes = null;
        this.role = null;
        this.accessibleAttributes = null;
        this.id = null;
        this.contentWithNoTabs = null;
        this.content = new StringBuffer(content);
        this.font = font;
        this.role = PdfName.SPAN;
    }

    public Chunk(String content) {
        this(content, new Font());
    }

    public Chunk(char c, Font font) {
        this.content = null;
        this.font = null;
        this.attributes = null;
        this.role = null;
        this.accessibleAttributes = null;
        this.id = null;
        this.contentWithNoTabs = null;
        this.content = new StringBuffer();
        this.content.append(c);
        this.font = font;
        this.role = PdfName.SPAN;
    }

    public Chunk(char c) {
        this(c, new Font());
    }

    public Chunk(Image image, float offsetX, float offsetY) {
        this(OBJECT_REPLACEMENT_CHARACTER, new Font());
        Image.getInstance(image).setAbsolutePosition(Float.NaN, Float.NaN);
        setAttribute(IMAGE, new Object[]{copyImage, new Float(offsetX), new Float(offsetY), Boolean.FALSE});
        this.role = null;
    }

    public Chunk(DrawInterface separator) {
        this(separator, false);
    }

    public Chunk(DrawInterface separator, boolean vertical) {
        this(OBJECT_REPLACEMENT_CHARACTER, new Font());
        setAttribute(SEPARATOR, new Object[]{separator, Boolean.valueOf(vertical)});
        this.role = null;
    }

    @Deprecated
    public Chunk(DrawInterface separator, float tabPosition) {
        this(separator, tabPosition, false);
    }

    @Deprecated
    public Chunk(DrawInterface separator, float tabPosition, boolean newline) {
        this(OBJECT_REPLACEMENT_CHARACTER, new Font());
        if (tabPosition < 0.0f) {
            throw new IllegalArgumentException(MessageLocalization.getComposedMessage("a.tab.position.may.not.be.lower.than.0.yours.is.1", new Object[]{String.valueOf(tabPosition)}));
        }
        setAttribute(TAB, new Object[]{separator, new Float(tabPosition), Boolean.valueOf(newline), new Float(0.0f)});
        this.role = PdfName.ARTIFACT;
    }

    private Chunk(Float tabInterval, boolean isWhitespace) {
        this(OBJECT_REPLACEMENT_CHARACTER, new Font());
        if (tabInterval.floatValue() < 0.0f) {
            throw new IllegalArgumentException(MessageLocalization.getComposedMessage("a.tab.position.may.not.be.lower.than.0.yours.is.1", new Object[]{String.valueOf(tabInterval)}));
        }
        setAttribute(TAB, new Object[]{tabInterval, Boolean.valueOf(isWhitespace)});
        setAttribute(SPLITCHARACTER, TabSplitCharacter.TAB);
        setAttribute(TABSETTINGS, null);
        this.role = PdfName.ARTIFACT;
    }

    public Chunk(Image image, float offsetX, float offsetY, boolean changeLeading) {
        this(OBJECT_REPLACEMENT_CHARACTER, new Font());
        setAttribute(IMAGE, new Object[]{image, new Float(offsetX), new Float(offsetY), Boolean.valueOf(changeLeading)});
        this.role = PdfName.ARTIFACT;
    }

    public boolean process(ElementListener listener) {
        try {
            return listener.add(this);
        } catch (DocumentException e) {
            return false;
        }
    }

    public int type() {
        return 10;
    }

    public List<Chunk> getChunks() {
        List<Chunk> tmp = new ArrayList();
        tmp.add(this);
        return tmp;
    }

    public StringBuffer append(String string) {
        this.contentWithNoTabs = null;
        return this.content.append(string);
    }

    public void setFont(Font font) {
        this.font = font;
    }

    public Font getFont() {
        return this.font;
    }

    public String getContent() {
        if (this.contentWithNoTabs == null) {
            this.contentWithNoTabs = this.content.toString().replaceAll("\t", "");
        }
        return this.contentWithNoTabs;
    }

    public String toString() {
        return getContent();
    }

    public boolean isEmpty() {
        return this.content.toString().trim().length() == 0 && this.content.toString().indexOf("\n") == -1 && this.attributes == null;
    }

    public float getWidthPoint() {
        if (getImage() != null) {
            return getImage().getScaledWidth();
        }
        return this.font.getCalculatedBaseFont(true).getWidthPoint(getContent(), this.font.getCalculatedSize()) * getHorizontalScaling();
    }

    public boolean hasAttributes() {
        return this.attributes != null;
    }

    public HashMap<String, Object> getAttributes() {
        return this.attributes;
    }

    public void setAttributes(HashMap<String, Object> attributes) {
        this.attributes = attributes;
    }

    private Chunk setAttribute(String name, Object obj) {
        if (this.attributes == null) {
            this.attributes = new HashMap();
        }
        this.attributes.put(name, obj);
        return this;
    }

    public Chunk setHorizontalScaling(float scale) {
        return setAttribute(HSCALE, new Float(scale));
    }

    public float getHorizontalScaling() {
        if (this.attributes == null) {
            return BaseField.BORDER_WIDTH_THIN;
        }
        Float f = (Float) this.attributes.get(HSCALE);
        if (f != null) {
            return f.floatValue();
        }
        return BaseField.BORDER_WIDTH_THIN;
    }

    public Chunk setUnderline(float thickness, float yPosition) {
        return setUnderline(null, thickness, 0.0f, yPosition, 0.0f, 0);
    }

    public Chunk setUnderline(BaseColor color, float thickness, float thicknessMul, float yPosition, float yPositionMul, int cap) {
        if (this.attributes == null) {
            this.attributes = new HashMap();
        }
        Object[] obj = new Object[2];
        obj[0] = color;
        obj[1] = new float[]{thickness, thicknessMul, yPosition, yPositionMul, (float) cap};
        return setAttribute(UNDERLINE, Utilities.addToArray((Object[][]) this.attributes.get(UNDERLINE), obj));
    }

    public Chunk setTextRise(float rise) {
        return setAttribute(SUBSUPSCRIPT, new Float(rise));
    }

    public float getTextRise() {
        if (this.attributes == null || !this.attributes.containsKey(SUBSUPSCRIPT)) {
            return 0.0f;
        }
        return ((Float) this.attributes.get(SUBSUPSCRIPT)).floatValue();
    }

    public Chunk setSkew(float alpha, float beta) {
        alpha = (float) Math.tan((((double) alpha) * 3.141592653589793d) / 180.0d);
        beta = (float) Math.tan((((double) beta) * 3.141592653589793d) / 180.0d);
        return setAttribute(SKEW, new float[]{alpha, beta});
    }

    public Chunk setBackground(BaseColor color) {
        return setBackground(color, 0.0f, 0.0f, 0.0f, 0.0f);
    }

    public Chunk setBackground(BaseColor color, float extraLeft, float extraBottom, float extraRight, float extraTop) {
        String str = BACKGROUND;
        Object obj = new Object[2];
        obj[0] = color;
        obj[1] = new float[]{extraLeft, extraBottom, extraRight, extraTop};
        return setAttribute(str, obj);
    }

    public Chunk setTextRenderMode(int mode, float strokeWidth, BaseColor strokeColor) {
        return setAttribute(TEXTRENDERMODE, new Object[]{Integer.valueOf(mode), new Float(strokeWidth), strokeColor});
    }

    public Chunk setSplitCharacter(SplitCharacter splitCharacter) {
        return setAttribute(SPLITCHARACTER, splitCharacter);
    }

    public Chunk setHyphenation(HyphenationEvent hyphenation) {
        return setAttribute(HYPHENATION, hyphenation);
    }

    public Chunk setRemoteGoto(String filename, String name) {
        return setAttribute(REMOTEGOTO, new Object[]{filename, name});
    }

    public Chunk setRemoteGoto(String filename, int page) {
        return setAttribute(REMOTEGOTO, new Object[]{filename, Integer.valueOf(page)});
    }

    public Chunk setLocalGoto(String name) {
        return setAttribute(LOCALGOTO, name);
    }

    public Chunk setLocalDestination(String name) {
        return setAttribute(LOCALDESTINATION, name);
    }

    public Chunk setGenericTag(String text) {
        return setAttribute(GENERICTAG, text);
    }

    public Chunk setLineHeight(float lineheight) {
        return setAttribute(LINEHEIGHT, Float.valueOf(lineheight));
    }

    public Image getImage() {
        if (this.attributes == null) {
            return null;
        }
        Object[] obj = (Object[]) this.attributes.get(IMAGE);
        if (obj == null) {
            return null;
        }
        return (Image) obj[0];
    }

    public Chunk setAction(PdfAction action) {
        setRole(PdfName.LINK);
        return setAttribute(ACTION, action);
    }

    public Chunk setAnchor(URL url) {
        setRole(PdfName.LINK);
        String urlStr = url.toExternalForm();
        setAccessibleAttribute(PdfName.ALT, new PdfString(urlStr));
        return setAttribute(ACTION, new PdfAction(urlStr));
    }

    public Chunk setAnchor(String url) {
        setRole(PdfName.LINK);
        setAccessibleAttribute(PdfName.ALT, new PdfString(url));
        return setAttribute(ACTION, new PdfAction(url));
    }

    public Chunk setNewPage() {
        return setAttribute(NEWPAGE, null);
    }

    public Chunk setAnnotation(PdfAnnotation annotation) {
        return setAttribute(PDFANNOTATION, annotation);
    }

    public boolean isContent() {
        return true;
    }

    public boolean isNestable() {
        return true;
    }

    public HyphenationEvent getHyphenation() {
        if (this.attributes == null) {
            return null;
        }
        return (HyphenationEvent) this.attributes.get(HYPHENATION);
    }

    public Chunk setCharacterSpacing(float charSpace) {
        return setAttribute(CHAR_SPACING, new Float(charSpace));
    }

    public float getCharacterSpacing() {
        if (this.attributes == null || !this.attributes.containsKey(CHAR_SPACING)) {
            return 0.0f;
        }
        return ((Float) this.attributes.get(CHAR_SPACING)).floatValue();
    }

    public Chunk setWordSpacing(float wordSpace) {
        return setAttribute(WORD_SPACING, new Float(wordSpace));
    }

    public float getWordSpacing() {
        if (this.attributes == null || !this.attributes.containsKey(WORD_SPACING)) {
            return 0.0f;
        }
        return ((Float) this.attributes.get(WORD_SPACING)).floatValue();
    }

    public static Chunk createWhitespace(String content) {
        return createWhitespace(content, false);
    }

    public static Chunk createWhitespace(String content, boolean preserve) {
        if (preserve) {
            return new Chunk(content);
        }
        Chunk whitespace = new Chunk(' ');
        whitespace.setAttribute(WHITESPACE, content);
        return whitespace;
    }

    public boolean isWhitespace() {
        return this.attributes != null && this.attributes.containsKey(WHITESPACE);
    }

    @Deprecated
    public static Chunk createTabspace() {
        return createTabspace(60.0f);
    }

    @Deprecated
    public static Chunk createTabspace(float spacing) {
        return new Chunk(Float.valueOf(spacing), true);
    }

    @Deprecated
    public boolean isTabspace() {
        return this.attributes != null && this.attributes.containsKey(TAB);
    }

    public PdfObject getAccessibleAttribute(PdfName key) {
        if (getImage() != null) {
            return getImage().getAccessibleAttribute(key);
        }
        if (this.accessibleAttributes != null) {
            return (PdfObject) this.accessibleAttributes.get(key);
        }
        return null;
    }

    public void setAccessibleAttribute(PdfName key, PdfObject value) {
        if (getImage() != null) {
            getImage().setAccessibleAttribute(key, value);
            return;
        }
        if (this.accessibleAttributes == null) {
            this.accessibleAttributes = new HashMap();
        }
        this.accessibleAttributes.put(key, value);
    }

    public HashMap<PdfName, PdfObject> getAccessibleAttributes() {
        if (getImage() != null) {
            return getImage().getAccessibleAttributes();
        }
        return this.accessibleAttributes;
    }

    public PdfName getRole() {
        if (getImage() != null) {
            return getImage().getRole();
        }
        return this.role;
    }

    public void setRole(PdfName role) {
        if (getImage() != null) {
            getImage().setRole(role);
        } else {
            this.role = role;
        }
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
        return true;
    }

    public String getTextExpansion() {
        PdfObject o = getAccessibleAttribute(PdfName.E);
        if (o instanceof PdfString) {
            return ((PdfString) o).toUnicodeString();
        }
        return null;
    }

    public void setTextExpansion(String value) {
        setAccessibleAttribute(PdfName.E, new PdfString(value));
    }
}
