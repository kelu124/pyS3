package com.itextpdf.text.pdf;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.error_messages.MessageLocalization;
import com.itextpdf.text.html.HtmlTags;
import com.itextpdf.text.html.HtmlUtilities;
import java.io.IOException;

public class RadioCheckField extends BaseField {
    public static final int TYPE_CHECK = 1;
    public static final int TYPE_CIRCLE = 2;
    public static final int TYPE_CROSS = 3;
    public static final int TYPE_DIAMOND = 4;
    public static final int TYPE_SQUARE = 5;
    public static final int TYPE_STAR = 6;
    protected static String[] typeChars = new String[]{"4", "l", "8", HtmlTags.f37U, "n", "H"};
    protected int checkType;
    private boolean checked;
    private String onValue;

    public RadioCheckField(PdfWriter writer, Rectangle box, String fieldName, String onValue) {
        super(writer, box, fieldName);
        setOnValue(onValue);
        setCheckType(2);
    }

    public int getCheckType() {
        return this.checkType;
    }

    public void setCheckType(int checkType) {
        if (checkType < 1 || checkType > 6) {
            checkType = 2;
        }
        this.checkType = checkType;
        setText(typeChars[checkType - 1]);
        try {
            setFont(BaseFont.createFont("ZapfDingbats", "Cp1252", false));
        } catch (Exception e) {
            throw new ExceptionConverter(e);
        }
    }

    public String getOnValue() {
        return this.onValue;
    }

    public void setOnValue(String onValue) {
        this.onValue = onValue;
    }

    public boolean isChecked() {
        return this.checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public PdfAppearance getAppearance(boolean isRadio, boolean on) throws IOException, DocumentException {
        if (isRadio && this.checkType == 2) {
            return getAppearanceRadioCircle(on);
        }
        PdfAppearance app = getBorderAppearance();
        if (!on) {
            return app;
        }
        float offsetX;
        BaseFont ufont = getRealFont();
        boolean borderExtra = this.borderStyle == 2 || this.borderStyle == 3;
        float h = this.box.getHeight() - (this.borderWidth * BaseField.BORDER_WIDTH_MEDIUM);
        float bw2 = this.borderWidth;
        if (borderExtra) {
            h -= this.borderWidth * BaseField.BORDER_WIDTH_MEDIUM;
            bw2 *= BaseField.BORDER_WIDTH_MEDIUM;
        }
        if (borderExtra) {
            offsetX = BaseField.BORDER_WIDTH_MEDIUM * this.borderWidth;
        } else {
            offsetX = this.borderWidth;
        }
        float offX = Math.min(bw2, Math.max(offsetX, BaseField.BORDER_WIDTH_THIN));
        float wt = this.box.getWidth() - (BaseField.BORDER_WIDTH_MEDIUM * offX);
        float ht = this.box.getHeight() - (BaseField.BORDER_WIDTH_MEDIUM * offX);
        float fsize = this.fontSize;
        if (fsize == 0.0f) {
            float bw = ufont.getWidthPoint(this.text, (float) BaseField.BORDER_WIDTH_THIN);
            if (bw == 0.0f) {
                fsize = HtmlUtilities.DEFAULT_FONT_SIZE;
            } else {
                fsize = wt / bw;
            }
            fsize = Math.min(fsize, h / ufont.getFontDescriptor(1, BaseField.BORDER_WIDTH_THIN));
        }
        app.saveState();
        app.rectangle(offX, offX, wt, ht);
        app.clip();
        app.newPath();
        if (this.textColor == null) {
            app.resetGrayFill();
        } else {
            app.setColorFill(this.textColor);
        }
        app.beginText();
        app.setFontAndSize(ufont, fsize);
        app.setTextMatrix((this.box.getWidth() - ufont.getWidthPoint(this.text, fsize)) / BaseField.BORDER_WIDTH_MEDIUM, (this.box.getHeight() - ufont.getAscentPoint(this.text, fsize)) / BaseField.BORDER_WIDTH_MEDIUM);
        app.showText(this.text);
        app.endText();
        app.restoreState();
        return app;
    }

    public PdfAppearance getAppearanceRadioCircle(boolean on) {
        PdfAppearance app = PdfAppearance.createAppearance(this.writer, this.box.getWidth(), this.box.getHeight());
        switch (this.rotation) {
            case 90:
                app.setMatrix(0.0f, BaseField.BORDER_WIDTH_THIN, -1.0f, 0.0f, this.box.getHeight(), 0.0f);
                break;
            case 180:
                app.setMatrix(-1.0f, 0.0f, 0.0f, -1.0f, this.box.getWidth(), this.box.getHeight());
                break;
            case 270:
                app.setMatrix(0.0f, -1.0f, BaseField.BORDER_WIDTH_THIN, 0.0f, 0.0f, this.box.getWidth());
                break;
        }
        Rectangle box = new Rectangle(app.getBoundingBox());
        float cx = box.getWidth() / BaseField.BORDER_WIDTH_MEDIUM;
        float cy = box.getHeight() / BaseField.BORDER_WIDTH_MEDIUM;
        float r = (Math.min(box.getWidth(), box.getHeight()) - this.borderWidth) / BaseField.BORDER_WIDTH_MEDIUM;
        if (r > 0.0f) {
            if (this.backgroundColor != null) {
                app.setColorFill(this.backgroundColor);
                app.circle(cx, cy, (this.borderWidth / BaseField.BORDER_WIDTH_MEDIUM) + r);
                app.fill();
            }
            if (this.borderWidth > 0.0f && this.borderColor != null) {
                app.setLineWidth(this.borderWidth);
                app.setColorStroke(this.borderColor);
                app.circle(cx, cy, r);
                app.stroke();
            }
            if (on) {
                if (this.textColor == null) {
                    app.resetGrayFill();
                } else {
                    app.setColorFill(this.textColor);
                }
                app.circle(cx, cy, r / BaseField.BORDER_WIDTH_MEDIUM);
                app.fill();
            }
        }
        return app;
    }

    public PdfFormField getRadioGroup(boolean noToggleToOff, boolean radiosInUnison) {
        PdfFormField field = PdfFormField.createRadioButton(this.writer, noToggleToOff);
        if (radiosInUnison) {
            field.setFieldFlags(33554432);
        }
        field.setFieldName(this.fieldName);
        if ((this.options & 1) != 0) {
            field.setFieldFlags(1);
        }
        if ((this.options & 2) != 0) {
            field.setFieldFlags(2);
        }
        field.setValueAsName(this.checked ? this.onValue : "Off");
        return field;
    }

    public PdfFormField getRadioField() throws IOException, DocumentException {
        return getField(true);
    }

    public PdfFormField getCheckField() throws IOException, DocumentException {
        return getField(false);
    }

    protected PdfFormField getField(boolean isRadio) throws IOException, DocumentException {
        PdfFormField field;
        if (isRadio) {
            field = PdfFormField.createEmpty(this.writer);
        } else {
            field = PdfFormField.createCheckBox(this.writer);
        }
        field.setWidget(this.box, PdfAnnotation.HIGHLIGHT_INVERT);
        if (!isRadio) {
            if ("Yes".equals(this.onValue)) {
                field.setFieldName(this.fieldName);
                if ((this.options & 1) != 0) {
                    field.setFieldFlags(1);
                }
                if ((this.options & 2) != 0) {
                    field.setFieldFlags(2);
                }
                field.setValueAsName(this.checked ? this.onValue : "Off");
                setCheckType(this.checkType);
            } else {
                throw new DocumentException(MessageLocalization.getComposedMessage("1.is.not.a.valid.name.for.checkbox.appearance", this.onValue));
            }
        }
        if (this.text != null) {
            field.setMKNormalCaption(this.text);
        }
        if (this.rotation != 0) {
            field.setMKRotation(this.rotation);
        }
        field.setBorderStyle(new PdfBorderDictionary(this.borderWidth, this.borderStyle, new PdfDashPattern(BaseField.BORDER_WIDTH_THICK)));
        PdfAppearance tpon = getAppearance(isRadio, true);
        PdfAppearance tpoff = getAppearance(isRadio, false);
        field.setAppearance(PdfAnnotation.APPEARANCE_NORMAL, this.onValue, tpon);
        field.setAppearance(PdfAnnotation.APPEARANCE_NORMAL, "Off", tpoff);
        field.setAppearanceState(this.checked ? this.onValue : "Off");
        PdfAppearance da = (PdfAppearance) tpon.getDuplicate();
        if (getRealFont() != null) {
            da.setFontAndSize(getRealFont(), this.fontSize);
        }
        if (this.textColor == null) {
            da.setGrayFill(0.0f);
        } else {
            da.setColorFill(this.textColor);
        }
        field.setDefaultAppearanceString(da);
        if (this.borderColor != null) {
            field.setMKBorderColor(this.borderColor);
        }
        if (this.backgroundColor != null) {
            field.setMKBackgroundColor(this.backgroundColor);
        }
        switch (this.visibility) {
            case 1:
                field.setFlags(6);
                break;
            case 2:
                break;
            case 3:
                field.setFlags(36);
                break;
            default:
                field.setFlags(4);
                break;
        }
        return field;
    }
}
