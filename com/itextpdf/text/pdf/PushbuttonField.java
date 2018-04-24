package com.itextpdf.text.pdf;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.error_messages.MessageLocalization;
import com.itextpdf.text.html.HtmlUtilities;
import java.io.IOException;

public class PushbuttonField extends BaseField {
    public static final int LAYOUT_ICON_LEFT_LABEL_RIGHT = 5;
    public static final int LAYOUT_ICON_ONLY = 2;
    public static final int LAYOUT_ICON_TOP_LABEL_BOTTOM = 3;
    public static final int LAYOUT_LABEL_LEFT_ICON_RIGHT = 6;
    public static final int LAYOUT_LABEL_ONLY = 1;
    public static final int LAYOUT_LABEL_OVER_ICON = 7;
    public static final int LAYOUT_LABEL_TOP_ICON_BOTTOM = 4;
    public static final int SCALE_ICON_ALWAYS = 1;
    public static final int SCALE_ICON_IS_TOO_BIG = 3;
    public static final int SCALE_ICON_IS_TOO_SMALL = 4;
    public static final int SCALE_ICON_NEVER = 2;
    private boolean iconFitToBounds;
    private float iconHorizontalAdjustment = 0.5f;
    private PRIndirectReference iconReference;
    private float iconVerticalAdjustment = 0.5f;
    private Image image;
    private int layout = 1;
    private boolean proportionalIcon = true;
    private int scaleIcon = 1;
    private PdfTemplate template;
    private PdfTemplate tp;

    public PushbuttonField(PdfWriter writer, Rectangle box, String fieldName) {
        super(writer, box, fieldName);
    }

    public int getLayout() {
        return this.layout;
    }

    public void setLayout(int layout) {
        if (layout < 1 || layout > 7) {
            throw new IllegalArgumentException(MessageLocalization.getComposedMessage("layout.out.of.bounds", new Object[0]));
        }
        this.layout = layout;
    }

    public Image getImage() {
        return this.image;
    }

    public void setImage(Image image) {
        this.image = image;
        this.template = null;
    }

    public PdfTemplate getTemplate() {
        return this.template;
    }

    public void setTemplate(PdfTemplate template) {
        this.template = template;
        this.image = null;
    }

    public int getScaleIcon() {
        return this.scaleIcon;
    }

    public void setScaleIcon(int scaleIcon) {
        if (scaleIcon < 1 || scaleIcon > 4) {
            scaleIcon = 1;
        }
        this.scaleIcon = scaleIcon;
    }

    public boolean isProportionalIcon() {
        return this.proportionalIcon;
    }

    public void setProportionalIcon(boolean proportionalIcon) {
        this.proportionalIcon = proportionalIcon;
    }

    public float getIconVerticalAdjustment() {
        return this.iconVerticalAdjustment;
    }

    public void setIconVerticalAdjustment(float iconVerticalAdjustment) {
        if (iconVerticalAdjustment < 0.0f) {
            iconVerticalAdjustment = 0.0f;
        } else if (iconVerticalAdjustment > BaseField.BORDER_WIDTH_THIN) {
            iconVerticalAdjustment = BaseField.BORDER_WIDTH_THIN;
        }
        this.iconVerticalAdjustment = iconVerticalAdjustment;
    }

    public float getIconHorizontalAdjustment() {
        return this.iconHorizontalAdjustment;
    }

    public void setIconHorizontalAdjustment(float iconHorizontalAdjustment) {
        if (iconHorizontalAdjustment < 0.0f) {
            iconHorizontalAdjustment = 0.0f;
        } else if (iconHorizontalAdjustment > BaseField.BORDER_WIDTH_THIN) {
            iconHorizontalAdjustment = BaseField.BORDER_WIDTH_THIN;
        }
        this.iconHorizontalAdjustment = iconHorizontalAdjustment;
    }

    private float calculateFontSize(float w, float h) throws IOException, DocumentException {
        BaseFont ufont = getRealFont();
        float fsize = this.fontSize;
        if (fsize != 0.0f) {
            return fsize;
        }
        float bw = ufont.getWidthPoint(this.text, (float) BaseField.BORDER_WIDTH_THIN);
        if (bw == 0.0f) {
            fsize = HtmlUtilities.DEFAULT_FONT_SIZE;
        } else {
            fsize = w / bw;
        }
        fsize = Math.min(fsize, h / (BaseField.BORDER_WIDTH_THIN - ufont.getFontDescriptor(3, BaseField.BORDER_WIDTH_THIN)));
        if (fsize < 4.0f) {
            return 4.0f;
        }
        return fsize;
    }

    public PdfAppearance getAppearance() throws IOException, DocumentException {
        PdfAppearance app = getBorderAppearance();
        Rectangle rectangle = new Rectangle(app.getBoundingBox());
        if (!(((this.text == null || this.text.length() == 0) && (this.layout == 1 || (this.image == null && this.template == null && this.iconReference == null))) || (this.layout == 2 && this.image == null && this.template == null && this.iconReference == null))) {
            BaseFont ufont = getRealFont();
            boolean borderExtra = this.borderStyle == 2 || this.borderStyle == 3;
            float h = rectangle.getHeight() - (this.borderWidth * BaseField.BORDER_WIDTH_MEDIUM);
            float bw2 = this.borderWidth;
            if (borderExtra) {
                h -= this.borderWidth * BaseField.BORDER_WIDTH_MEDIUM;
                bw2 *= BaseField.BORDER_WIDTH_MEDIUM;
            }
            float offX = Math.min(bw2, Math.max(borderExtra ? BaseField.BORDER_WIDTH_MEDIUM * this.borderWidth : this.borderWidth, BaseField.BORDER_WIDTH_THIN));
            this.tp = null;
            float textX = Float.NaN;
            float textY = 0.0f;
            float fsize = this.fontSize;
            float wt = (rectangle.getWidth() - (BaseField.BORDER_WIDTH_MEDIUM * offX)) - BaseField.BORDER_WIDTH_MEDIUM;
            float ht = rectangle.getHeight() - (BaseField.BORDER_WIDTH_MEDIUM * offX);
            float adj = this.iconFitToBounds ? 0.0f : offX + BaseField.BORDER_WIDTH_THIN;
            int nlayout = this.layout;
            if (this.image == null && this.template == null && this.iconReference == null) {
                nlayout = 1;
            }
            Rectangle iconBox = null;
            while (true) {
                float nht;
                float nw;
                switch (nlayout) {
                    case 1:
                    case 7:
                        if (this.text != null && this.text.length() > 0 && wt > 0.0f && ht > 0.0f) {
                            fsize = calculateFontSize(wt, ht);
                            textX = (rectangle.getWidth() - ufont.getWidthPoint(this.text, fsize)) / BaseField.BORDER_WIDTH_MEDIUM;
                            textY = (rectangle.getHeight() - ufont.getFontDescriptor(1, fsize)) / BaseField.BORDER_WIDTH_MEDIUM;
                            break;
                        }
                    case 2:
                        break;
                    case 3:
                        if (this.text != null && this.text.length() != 0 && wt > 0.0f && ht > 0.0f) {
                            nht = (rectangle.getHeight() * 0.35f) - offX;
                            if (nht > 0.0f) {
                                fsize = calculateFontSize(wt, nht);
                            } else {
                                fsize = 4.0f;
                            }
                            textX = (rectangle.getWidth() - ufont.getWidthPoint(this.text, fsize)) / BaseField.BORDER_WIDTH_MEDIUM;
                            textY = offX - ufont.getFontDescriptor(3, fsize);
                            rectangle = new Rectangle(rectangle.getLeft() + adj, textY + fsize, rectangle.getRight() - adj, rectangle.getTop() - adj);
                            break;
                        }
                        nlayout = 2;
                        continue;
                        break;
                    case 4:
                        if (this.text != null && this.text.length() != 0 && wt > 0.0f && ht > 0.0f) {
                            nht = (rectangle.getHeight() * 0.35f) - offX;
                            if (nht > 0.0f) {
                                fsize = calculateFontSize(wt, nht);
                            } else {
                                fsize = 4.0f;
                            }
                            textX = (rectangle.getWidth() - ufont.getWidthPoint(this.text, fsize)) / BaseField.BORDER_WIDTH_MEDIUM;
                            textY = (rectangle.getHeight() - offX) - fsize;
                            if (textY < offX) {
                                textY = offX;
                            }
                            rectangle = new Rectangle(rectangle.getLeft() + adj, rectangle.getBottom() + adj, rectangle.getRight() - adj, ufont.getFontDescriptor(3, fsize) + textY);
                            break;
                        }
                        nlayout = 2;
                        continue;
                    case 5:
                        if (this.text != null && this.text.length() != 0 && wt > 0.0f && ht > 0.0f) {
                            nw = (rectangle.getWidth() * 0.35f) - offX;
                            if (nw > 0.0f) {
                                fsize = calculateFontSize(wt, nw);
                            } else {
                                fsize = 4.0f;
                            }
                            if (ufont.getWidthPoint(this.text, fsize) < wt) {
                                textX = ((rectangle.getWidth() - ufont.getWidthPoint(this.text, fsize)) - offX) - BaseField.BORDER_WIDTH_THIN;
                                textY = (rectangle.getHeight() - ufont.getFontDescriptor(1, fsize)) / BaseField.BORDER_WIDTH_MEDIUM;
                                rectangle = new Rectangle(rectangle.getLeft() + adj, rectangle.getBottom() + adj, textX - BaseField.BORDER_WIDTH_THIN, rectangle.getTop() - adj);
                                break;
                            }
                            nlayout = 1;
                            fsize = this.fontSize;
                            break;
                        }
                        nlayout = 2;
                        continue;
                        break;
                    case 6:
                        if (this.text != null && this.text.length() != 0 && wt > 0.0f && ht > 0.0f) {
                            nw = (rectangle.getWidth() * 0.35f) - offX;
                            if (nw > 0.0f) {
                                fsize = calculateFontSize(wt, nw);
                            } else {
                                fsize = 4.0f;
                            }
                            if (ufont.getWidthPoint(this.text, fsize) < wt) {
                                textX = offX + BaseField.BORDER_WIDTH_THIN;
                                textY = (rectangle.getHeight() - ufont.getFontDescriptor(1, fsize)) / BaseField.BORDER_WIDTH_MEDIUM;
                                rectangle = new Rectangle(ufont.getWidthPoint(this.text, fsize) + textX, rectangle.getBottom() + adj, rectangle.getRight() - adj, rectangle.getTop() - adj);
                                break;
                            }
                            nlayout = 1;
                            fsize = this.fontSize;
                            break;
                        }
                        nlayout = 2;
                        continue;
                    default:
                        break;
                }
                if (nlayout == 7 || nlayout == 2) {
                    rectangle = new Rectangle(rectangle.getLeft() + adj, rectangle.getBottom() + adj, rectangle.getRight() - adj, rectangle.getTop() - adj);
                }
                if (textY < rectangle.getBottom() + offX) {
                    textY = rectangle.getBottom() + offX;
                }
                if (iconBox != null && (iconBox.getWidth() <= 0.0f || iconBox.getHeight() <= 0.0f)) {
                    iconBox = null;
                }
                boolean haveIcon = false;
                float boundingBoxWidth = 0.0f;
                float boundingBoxHeight = 0.0f;
                PdfArray matrix = null;
                if (iconBox != null) {
                    if (this.image != null) {
                        this.tp = new PdfTemplate(this.writer);
                        this.tp.setBoundingBox(new Rectangle(this.image));
                        this.writer.addDirectTemplateSimple(this.tp, PdfName.FRM);
                        this.tp.addImage(this.image, this.image.getWidth(), 0.0f, 0.0f, this.image.getHeight(), 0.0f, 0.0f);
                        haveIcon = true;
                        boundingBoxWidth = this.tp.getBoundingBox().getWidth();
                        boundingBoxHeight = this.tp.getBoundingBox().getHeight();
                    } else if (this.template != null) {
                        this.tp = new PdfTemplate(this.writer);
                        this.tp.setBoundingBox(new Rectangle(this.template.getWidth(), this.template.getHeight()));
                        this.writer.addDirectTemplateSimple(this.tp, PdfName.FRM);
                        this.tp.addTemplate(this.template, this.template.getBoundingBox().getLeft(), this.template.getBoundingBox().getBottom());
                        haveIcon = true;
                        boundingBoxWidth = this.tp.getBoundingBox().getWidth();
                        boundingBoxHeight = this.tp.getBoundingBox().getHeight();
                    } else if (this.iconReference != null) {
                        PdfDictionary dic = (PdfDictionary) PdfReader.getPdfObject(this.iconReference);
                        if (dic != null) {
                            Rectangle r2 = PdfReader.getNormalizedRectangle(dic.getAsArray(PdfName.BBOX));
                            matrix = dic.getAsArray(PdfName.MATRIX);
                            haveIcon = true;
                            boundingBoxWidth = r2.getWidth();
                            boundingBoxHeight = r2.getHeight();
                        }
                    }
                }
                if (haveIcon) {
                    float icx = iconBox.getWidth() / boundingBoxWidth;
                    float icy = iconBox.getHeight() / boundingBoxHeight;
                    if (!this.proportionalIcon) {
                        switch (this.scaleIcon) {
                            case 2:
                                icy = BaseField.BORDER_WIDTH_THIN;
                                icx = BaseField.BORDER_WIDTH_THIN;
                                break;
                            case 3:
                                icx = Math.min(icx, BaseField.BORDER_WIDTH_THIN);
                                icy = Math.min(icy, BaseField.BORDER_WIDTH_THIN);
                                break;
                            case 4:
                                icx = Math.max(icx, BaseField.BORDER_WIDTH_THIN);
                                icy = Math.max(icy, BaseField.BORDER_WIDTH_THIN);
                                break;
                            default:
                                break;
                        }
                    }
                    switch (this.scaleIcon) {
                        case 2:
                            icx = BaseField.BORDER_WIDTH_THIN;
                            break;
                        case 3:
                            icx = Math.min(Math.min(icx, icy), BaseField.BORDER_WIDTH_THIN);
                            break;
                        case 4:
                            icx = Math.max(Math.min(icx, icy), BaseField.BORDER_WIDTH_THIN);
                            break;
                        default:
                            icx = Math.min(icx, icy);
                            break;
                    }
                    icy = icx;
                    float xpos = iconBox.getLeft() + ((iconBox.getWidth() - (boundingBoxWidth * icx)) * this.iconHorizontalAdjustment);
                    float ypos = iconBox.getBottom() + ((iconBox.getHeight() - (boundingBoxHeight * icy)) * this.iconVerticalAdjustment);
                    app.saveState();
                    app.rectangle(iconBox.getLeft(), iconBox.getBottom(), iconBox.getWidth(), iconBox.getHeight());
                    app.clip();
                    app.newPath();
                    if (this.tp != null) {
                        app.addTemplate(this.tp, icx, 0.0f, 0.0f, icy, xpos, ypos);
                    } else {
                        float cox = 0.0f;
                        float coy = 0.0f;
                        if (matrix != null && matrix.size() == 6) {
                            PdfNumber nm = matrix.getAsNumber(4);
                            if (nm != null) {
                                cox = nm.floatValue();
                            }
                            nm = matrix.getAsNumber(5);
                            if (nm != null) {
                                coy = nm.floatValue();
                            }
                        }
                        app.addTemplateReference(this.iconReference, PdfName.FRM, icx, 0.0f, 0.0f, icy, xpos - (cox * icx), ypos - (coy * icy));
                    }
                    app.restoreState();
                }
                if (!Float.isNaN(textX)) {
                    app.saveState();
                    app.rectangle(offX, offX, rectangle.getWidth() - (BaseField.BORDER_WIDTH_MEDIUM * offX), rectangle.getHeight() - (BaseField.BORDER_WIDTH_MEDIUM * offX));
                    app.clip();
                    app.newPath();
                    if (this.textColor == null) {
                        app.resetGrayFill();
                    } else {
                        app.setColorFill(this.textColor);
                    }
                    app.beginText();
                    app.setFontAndSize(ufont, fsize);
                    app.setTextMatrix(textX, textY);
                    app.showText(this.text);
                    app.endText();
                    app.restoreState();
                }
            }
        }
        return app;
    }

    public PdfFormField getField() throws IOException, DocumentException {
        PdfFormField field = PdfFormField.createPushButton(this.writer);
        field.setWidget(this.box, PdfAnnotation.HIGHLIGHT_INVERT);
        if (this.fieldName != null) {
            field.setFieldName(this.fieldName);
            if ((this.options & 1) != 0) {
                field.setFieldFlags(1);
            }
            if ((this.options & 2) != 0) {
                field.setFieldFlags(2);
            }
        }
        if (this.text != null) {
            field.setMKNormalCaption(this.text);
        }
        if (this.rotation != 0) {
            field.setMKRotation(this.rotation);
        }
        field.setBorderStyle(new PdfBorderDictionary(this.borderWidth, this.borderStyle, new PdfDashPattern(BaseField.BORDER_WIDTH_THICK)));
        PdfAppearance tpa = getAppearance();
        field.setAppearance(PdfAnnotation.APPEARANCE_NORMAL, tpa);
        PdfAppearance da = (PdfAppearance) tpa.getDuplicate();
        da.setFontAndSize(getRealFont(), this.fontSize);
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
        if (this.tp != null) {
            field.setMKNormalIcon(this.tp);
        }
        field.setMKTextPosition(this.layout - 1);
        PdfName scale = PdfName.f117A;
        if (this.scaleIcon == 3) {
            scale = PdfName.f118B;
        } else if (this.scaleIcon == 4) {
            scale = PdfName.f133S;
        } else if (this.scaleIcon == 2) {
            scale = PdfName.f128N;
        }
        field.setMKIconFit(scale, this.proportionalIcon ? PdfName.f130P : PdfName.f117A, this.iconHorizontalAdjustment, this.iconVerticalAdjustment, this.iconFitToBounds);
        return field;
    }

    public boolean isIconFitToBounds() {
        return this.iconFitToBounds;
    }

    public void setIconFitToBounds(boolean iconFitToBounds) {
        this.iconFitToBounds = iconFitToBounds;
    }

    public PRIndirectReference getIconReference() {
        return this.iconReference;
    }

    public void setIconReference(PRIndirectReference iconReference) {
        this.iconReference = iconReference;
    }
}
