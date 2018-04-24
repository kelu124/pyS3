package com.itextpdf.text.pdf;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.error_messages.MessageLocalization;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import org.bytedeco.javacpp.dc1394;

public abstract class BaseField {
    public static final float BORDER_WIDTH_MEDIUM = 2.0f;
    public static final float BORDER_WIDTH_THICK = 3.0f;
    public static final float BORDER_WIDTH_THIN = 1.0f;
    public static final int COMB = 16777216;
    public static final int DO_NOT_SCROLL = 8388608;
    public static final int DO_NOT_SPELL_CHECK = 4194304;
    public static final int EDIT = 262144;
    public static final int FILE_SELECTION = 1048576;
    public static final int HIDDEN = 1;
    public static final int HIDDEN_BUT_PRINTABLE = 3;
    public static final int MULTILINE = 4096;
    public static final int MULTISELECT = 2097152;
    public static final int PASSWORD = 8192;
    public static final int READ_ONLY = 1;
    public static final int REQUIRED = 2;
    public static final int VISIBLE = 0;
    public static final int VISIBLE_BUT_DOES_NOT_PRINT = 2;
    private static final HashMap<PdfName, Integer> fieldKeys = new HashMap();
    protected int alignment = 0;
    protected BaseColor backgroundColor;
    protected BaseColor borderColor;
    protected int borderStyle = 0;
    protected float borderWidth = BORDER_WIDTH_THIN;
    protected Rectangle box;
    protected String fieldName;
    protected BaseFont font;
    protected float fontSize = 0.0f;
    protected int maxCharacterLength;
    protected int options;
    protected int rotation = 0;
    protected String text;
    protected BaseColor textColor;
    protected int visibility;
    protected PdfWriter writer;

    static {
        fieldKeys.putAll(PdfCopyFieldsImp.fieldKeys);
        fieldKeys.put(PdfName.f134T, Integer.valueOf(1));
    }

    public BaseField(PdfWriter writer, Rectangle box, String fieldName) {
        this.writer = writer;
        setBox(box);
        this.fieldName = fieldName;
    }

    protected BaseFont getRealFont() throws IOException, DocumentException {
        if (this.font == null) {
            return BaseFont.createFont("Helvetica", "Cp1252", false);
        }
        return this.font;
    }

    protected PdfAppearance getBorderAppearance() {
        PdfAppearance app = PdfAppearance.createAppearance(this.writer, this.box.getWidth(), this.box.getHeight());
        switch (this.rotation) {
            case 90:
                app.setMatrix(0.0f, BORDER_WIDTH_THIN, -1.0f, 0.0f, this.box.getHeight(), 0.0f);
                break;
            case 180:
                app.setMatrix(-1.0f, 0.0f, 0.0f, -1.0f, this.box.getWidth(), this.box.getHeight());
                break;
            case 270:
                app.setMatrix(0.0f, -1.0f, BORDER_WIDTH_THIN, 0.0f, 0.0f, this.box.getWidth());
                break;
        }
        app.saveState();
        if (this.backgroundColor != null) {
            app.setColorFill(this.backgroundColor);
            app.rectangle(0.0f, 0.0f, this.box.getWidth(), this.box.getHeight());
            app.fill();
        }
        if (this.borderStyle == 4) {
            if (!(this.borderWidth == 0.0f || this.borderColor == null)) {
                app.setColorStroke(this.borderColor);
                app.setLineWidth(this.borderWidth);
                app.moveTo(0.0f, this.borderWidth / BORDER_WIDTH_MEDIUM);
                app.lineTo(this.box.getWidth(), this.borderWidth / BORDER_WIDTH_MEDIUM);
                app.stroke();
            }
        } else if (this.borderStyle == 2) {
            if (!(this.borderWidth == 0.0f || this.borderColor == null)) {
                app.setColorStroke(this.borderColor);
                app.setLineWidth(this.borderWidth);
                app.rectangle(this.borderWidth / BORDER_WIDTH_MEDIUM, this.borderWidth / BORDER_WIDTH_MEDIUM, this.box.getWidth() - this.borderWidth, this.box.getHeight() - this.borderWidth);
                app.stroke();
            }
            BaseColor actual = this.backgroundColor;
            if (actual == null) {
                actual = BaseColor.WHITE;
            }
            app.setGrayFill(BORDER_WIDTH_THIN);
            drawTopFrame(app);
            app.setColorFill(actual.darker());
            drawBottomFrame(app);
        } else if (this.borderStyle == 3) {
            if (!(this.borderWidth == 0.0f || this.borderColor == null)) {
                app.setColorStroke(this.borderColor);
                app.setLineWidth(this.borderWidth);
                app.rectangle(this.borderWidth / BORDER_WIDTH_MEDIUM, this.borderWidth / BORDER_WIDTH_MEDIUM, this.box.getWidth() - this.borderWidth, this.box.getHeight() - this.borderWidth);
                app.stroke();
            }
            app.setGrayFill(0.5f);
            drawTopFrame(app);
            app.setGrayFill(0.75f);
            drawBottomFrame(app);
        } else if (!(this.borderWidth == 0.0f || this.borderColor == null)) {
            if (this.borderStyle == 1) {
                app.setLineDash((float) BORDER_WIDTH_THICK, 0.0f);
            }
            app.setColorStroke(this.borderColor);
            app.setLineWidth(this.borderWidth);
            app.rectangle(this.borderWidth / BORDER_WIDTH_MEDIUM, this.borderWidth / BORDER_WIDTH_MEDIUM, this.box.getWidth() - this.borderWidth, this.box.getHeight() - this.borderWidth);
            app.stroke();
            if ((this.options & 16777216) != 0 && this.maxCharacterLength > 1) {
                float step = this.box.getWidth() / ((float) this.maxCharacterLength);
                float yb = this.borderWidth / BORDER_WIDTH_MEDIUM;
                float yt = this.box.getHeight() - (this.borderWidth / BORDER_WIDTH_MEDIUM);
                for (int k = 1; k < this.maxCharacterLength; k++) {
                    float x = step * ((float) k);
                    app.moveTo(x, yb);
                    app.lineTo(x, yt);
                }
                app.stroke();
            }
        }
        app.restoreState();
        return app;
    }

    protected static ArrayList<String> getHardBreaks(String text) {
        ArrayList<String> arr = new ArrayList();
        char[] cs = text.toCharArray();
        int len = cs.length;
        StringBuffer buf = new StringBuffer();
        int k = 0;
        while (k < len) {
            char c = cs[k];
            if (c == '\r') {
                if (k + 1 < len && cs[k + 1] == '\n') {
                    k++;
                }
                arr.add(buf.toString());
                buf = new StringBuffer();
            } else if (c == '\n') {
                arr.add(buf.toString());
                buf = new StringBuffer();
            } else {
                buf.append(c);
            }
            k++;
        }
        arr.add(buf.toString());
        return arr;
    }

    protected static void trimRight(StringBuffer buf) {
        int len = buf.length();
        while (len != 0) {
            len--;
            if (buf.charAt(len) == ' ') {
                buf.setLength(len);
            } else {
                return;
            }
        }
    }

    protected static ArrayList<String> breakLines(ArrayList<String> breaks, BaseFont font, float fontSize, float width) {
        ArrayList<String> lines = new ArrayList();
        StringBuffer buf = new StringBuffer();
        for (int ck = 0; ck < breaks.size(); ck++) {
            buf.setLength(0);
            float w = 0.0f;
            char[] cs = ((String) breaks.get(ck)).toCharArray();
            int len = cs.length;
            int state = 0;
            int lastspace = -1;
            int refk = 0;
            int k = 0;
            while (k < len) {
                char c = cs[k];
                switch (state) {
                    case 0:
                        w += font.getWidthPoint((int) c, fontSize);
                        buf.append(c);
                        if (w <= width) {
                            if (c == ' ') {
                                break;
                            }
                            state = 1;
                            break;
                        }
                        w = 0.0f;
                        if (buf.length() > 1) {
                            k--;
                            buf.setLength(buf.length() - 1);
                        }
                        lines.add(buf.toString());
                        buf.setLength(0);
                        refk = k;
                        if (c != ' ') {
                            state = 1;
                            break;
                        }
                        state = 2;
                        break;
                    case 1:
                        w += font.getWidthPoint((int) c, fontSize);
                        buf.append(c);
                        if (c == ' ') {
                            lastspace = k;
                        }
                        if (w <= width) {
                            break;
                        }
                        w = 0.0f;
                        if (lastspace < 0) {
                            if (buf.length() > 1) {
                                k--;
                                buf.setLength(buf.length() - 1);
                            }
                            lines.add(buf.toString());
                            buf.setLength(0);
                            refk = k;
                            if (c != ' ') {
                                break;
                            }
                            state = 2;
                            break;
                        }
                        k = lastspace;
                        buf.setLength(lastspace - refk);
                        trimRight(buf);
                        lines.add(buf.toString());
                        buf.setLength(0);
                        refk = k;
                        lastspace = -1;
                        state = 2;
                        break;
                    case 2:
                        if (c == ' ') {
                            break;
                        }
                        w = 0.0f;
                        k--;
                        state = 1;
                        break;
                    default:
                        break;
                }
                k++;
            }
            trimRight(buf);
            lines.add(buf.toString());
        }
        return lines;
    }

    private void drawTopFrame(PdfAppearance app) {
        app.moveTo(this.borderWidth, this.borderWidth);
        app.lineTo(this.borderWidth, this.box.getHeight() - this.borderWidth);
        app.lineTo(this.box.getWidth() - this.borderWidth, this.box.getHeight() - this.borderWidth);
        app.lineTo(this.box.getWidth() - (this.borderWidth * BORDER_WIDTH_MEDIUM), this.box.getHeight() - (this.borderWidth * BORDER_WIDTH_MEDIUM));
        app.lineTo(this.borderWidth * BORDER_WIDTH_MEDIUM, this.box.getHeight() - (this.borderWidth * BORDER_WIDTH_MEDIUM));
        app.lineTo(this.borderWidth * BORDER_WIDTH_MEDIUM, this.borderWidth * BORDER_WIDTH_MEDIUM);
        app.lineTo(this.borderWidth, this.borderWidth);
        app.fill();
    }

    private void drawBottomFrame(PdfAppearance app) {
        app.moveTo(this.borderWidth, this.borderWidth);
        app.lineTo(this.box.getWidth() - this.borderWidth, this.borderWidth);
        app.lineTo(this.box.getWidth() - this.borderWidth, this.box.getHeight() - this.borderWidth);
        app.lineTo(this.box.getWidth() - (this.borderWidth * BORDER_WIDTH_MEDIUM), this.box.getHeight() - (this.borderWidth * BORDER_WIDTH_MEDIUM));
        app.lineTo(this.box.getWidth() - (this.borderWidth * BORDER_WIDTH_MEDIUM), this.borderWidth * BORDER_WIDTH_MEDIUM);
        app.lineTo(this.borderWidth * BORDER_WIDTH_MEDIUM, this.borderWidth * BORDER_WIDTH_MEDIUM);
        app.lineTo(this.borderWidth, this.borderWidth);
        app.fill();
    }

    public float getBorderWidth() {
        return this.borderWidth;
    }

    public void setBorderWidth(float borderWidth) {
        this.borderWidth = borderWidth;
    }

    public int getBorderStyle() {
        return this.borderStyle;
    }

    public void setBorderStyle(int borderStyle) {
        this.borderStyle = borderStyle;
    }

    public BaseColor getBorderColor() {
        return this.borderColor;
    }

    public void setBorderColor(BaseColor borderColor) {
        this.borderColor = borderColor;
    }

    public BaseColor getBackgroundColor() {
        return this.backgroundColor;
    }

    public void setBackgroundColor(BaseColor backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public BaseColor getTextColor() {
        return this.textColor;
    }

    public void setTextColor(BaseColor textColor) {
        this.textColor = textColor;
    }

    public BaseFont getFont() {
        return this.font;
    }

    public void setFont(BaseFont font) {
        this.font = font;
    }

    public float getFontSize() {
        return this.fontSize;
    }

    public void setFontSize(float fontSize) {
        this.fontSize = fontSize;
    }

    public int getAlignment() {
        return this.alignment;
    }

    public void setAlignment(int alignment) {
        this.alignment = alignment;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Rectangle getBox() {
        return this.box;
    }

    public void setBox(Rectangle box) {
        if (box == null) {
            this.box = null;
            return;
        }
        this.box = new Rectangle(box);
        this.box.normalize();
    }

    public int getRotation() {
        return this.rotation;
    }

    public void setRotation(int rotation) {
        if (rotation % 90 != 0) {
            throw new IllegalArgumentException(MessageLocalization.getComposedMessage("rotation.must.be.a.multiple.of.90", new Object[0]));
        }
        rotation %= dc1394.DC1394_COLOR_CODING_RGB16S;
        if (rotation < 0) {
            rotation += dc1394.DC1394_COLOR_CODING_RGB16S;
        }
        this.rotation = rotation;
    }

    public void setRotationFromPage(Rectangle page) {
        setRotation(page.getRotation());
    }

    public int getVisibility() {
        return this.visibility;
    }

    public void setVisibility(int visibility) {
        this.visibility = visibility;
    }

    public String getFieldName() {
        return this.fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public int getOptions() {
        return this.options;
    }

    public void setOptions(int options) {
        this.options = options;
    }

    public int getMaxCharacterLength() {
        return this.maxCharacterLength;
    }

    public void setMaxCharacterLength(int maxCharacterLength) {
        this.maxCharacterLength = maxCharacterLength;
    }

    public PdfWriter getWriter() {
        return this.writer;
    }

    public void setWriter(PdfWriter writer) {
        this.writer = writer;
    }

    public static void moveFields(PdfDictionary from, PdfDictionary to) {
        Iterator<PdfName> i = from.getKeys().iterator();
        while (i.hasNext()) {
            PdfName key = (PdfName) i.next();
            if (fieldKeys.containsKey(key)) {
                if (to != null) {
                    to.put(key, from.get(key));
                }
                i.remove();
            }
        }
    }
}
