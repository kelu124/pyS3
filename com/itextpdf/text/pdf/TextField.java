package com.itextpdf.text.pdf;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.html.HtmlUtilities;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class TextField extends BaseField {
    private String[] choiceExports;
    private ArrayList<Integer> choiceSelections = new ArrayList();
    private String[] choices;
    private String defaultText;
    private BaseFont extensionFont;
    private float extraMarginLeft;
    private float extraMarginTop;
    private ArrayList<BaseFont> substitutionFonts;
    private int topFirst;
    private int visibleTopChoice = -1;

    public TextField(PdfWriter writer, Rectangle box, String fieldName) {
        super(writer, box, fieldName);
    }

    private static boolean checkRTL(String text) {
        if (text == null || text.length() == 0) {
            return false;
        }
        char[] cc = text.toCharArray();
        for (int c : cc) {
            if (c >= 1424 && c < 1920) {
                return true;
            }
        }
        return false;
    }

    private static void changeFontSize(Phrase p, float size) {
        for (int k = 0; k < p.size(); k++) {
            ((Chunk) p.get(k)).getFont().setSize(size);
        }
    }

    private Phrase composePhrase(String text, BaseFont ufont, BaseColor color, float fontSize) {
        if (this.extensionFont == null && (this.substitutionFonts == null || this.substitutionFonts.isEmpty())) {
            return new Phrase(new Chunk(text, new Font(ufont, fontSize, 0, color)));
        }
        FontSelector fs = new FontSelector();
        fs.addFont(new Font(ufont, fontSize, 0, color));
        if (this.extensionFont != null) {
            fs.addFont(new Font(this.extensionFont, fontSize, 0, color));
        }
        if (this.substitutionFonts != null) {
            for (int k = 0; k < this.substitutionFonts.size(); k++) {
                fs.addFont(new Font((BaseFont) this.substitutionFonts.get(k), fontSize, 0, color));
            }
        }
        return fs.process(text);
    }

    public static String removeCRLF(String text) {
        if (text.indexOf(10) < 0 && text.indexOf(13) < 0) {
            return text;
        }
        char[] p = text.toCharArray();
        StringBuffer sb = new StringBuffer(p.length);
        int k = 0;
        while (k < p.length) {
            char c = p[k];
            if (c == '\n') {
                sb.append(' ');
            } else if (c == '\r') {
                sb.append(' ');
                if (k < p.length - 1 && p[k + 1] == '\n') {
                    k++;
                }
            } else {
                sb.append(c);
            }
            k++;
        }
        return sb.toString();
    }

    public static String obfuscatePassword(String text) {
        char[] pchar = new char[text.length()];
        for (int i = 0; i < text.length(); i++) {
            pchar[i] = '*';
        }
        return new String(pchar);
    }

    public PdfAppearance getAppearance() throws IOException, DocumentException {
        PdfAppearance app = getBorderAppearance();
        app.beginVariableText();
        if (this.text == null || this.text.length() == 0) {
            app.endVariableText();
        } else {
            String ptext;
            boolean borderExtra = this.borderStyle == 2 || this.borderStyle == 3;
            float h = (this.box.getHeight() - (this.borderWidth * BaseField.BORDER_WIDTH_MEDIUM)) - this.extraMarginTop;
            float bw2 = this.borderWidth;
            if (borderExtra) {
                h -= this.borderWidth * BaseField.BORDER_WIDTH_MEDIUM;
                bw2 *= BaseField.BORDER_WIDTH_MEDIUM;
            }
            float offsetX = Math.max(bw2, BaseField.BORDER_WIDTH_THIN);
            float offX = Math.min(bw2, offsetX);
            app.saveState();
            app.rectangle(offX, offX, this.box.getWidth() - (BaseField.BORDER_WIDTH_MEDIUM * offX), this.box.getHeight() - (BaseField.BORDER_WIDTH_MEDIUM * offX));
            app.clip();
            app.newPath();
            if ((this.options & 8192) != 0) {
                ptext = obfuscatePassword(this.text);
            } else if ((this.options & 4096) == 0) {
                ptext = removeCRLF(this.text);
            } else {
                ptext = this.text;
            }
            BaseFont ufont = getRealFont();
            BaseColor fcolor = this.textColor == null ? GrayColor.GRAYBLACK : this.textColor;
            int rtl = checkRTL(ptext) ? 2 : 1;
            float usize = this.fontSize;
            Phrase phrase = composePhrase(ptext, ufont, fcolor, usize);
            float step;
            if ((this.options & 4096) != 0) {
                float width = (this.box.getWidth() - (4.0f * offsetX)) - this.extraMarginLeft;
                float factor = ufont.getFontDescriptor(8, BaseField.BORDER_WIDTH_THIN) - ufont.getFontDescriptor(6, BaseField.BORDER_WIDTH_THIN);
                ColumnText columnText = new ColumnText(null);
                if (usize == 0.0f) {
                    usize = h / factor;
                    if (usize > 4.0f) {
                        if (usize > HtmlUtilities.DEFAULT_FONT_SIZE) {
                            usize = HtmlUtilities.DEFAULT_FONT_SIZE;
                        }
                        step = Math.max((usize - 4.0f) / 10.0f, 0.2f);
                        columnText.setSimpleColumn(0.0f, -h, width, 0.0f);
                        columnText.setAlignment(this.alignment);
                        columnText.setRunDirection(rtl);
                        while (usize > 4.0f) {
                            columnText.setYLine(0.0f);
                            changeFontSize(phrase, usize);
                            columnText.setText(phrase);
                            columnText.setLeading(factor * usize);
                            if ((columnText.go(true) & 2) == 0) {
                                break;
                            }
                            usize -= step;
                        }
                    }
                    if (usize < 4.0f) {
                        usize = 4.0f;
                    }
                }
                changeFontSize(phrase, usize);
                columnText.setCanvas(app);
                float leading = usize * factor;
                columnText.setSimpleColumn(this.extraMarginLeft + (BaseField.BORDER_WIDTH_MEDIUM * offsetX), -20000.0f, this.box.getWidth() - (BaseField.BORDER_WIDTH_MEDIUM * offsetX), ((offsetX + h) - ufont.getFontDescriptor(8, usize)) + leading);
                columnText.setLeading(leading);
                columnText.setAlignment(this.alignment);
                columnText.setRunDirection(rtl);
                columnText.setText(phrase);
                columnText.go();
            } else {
                if (usize == 0.0f) {
                    float maxCalculatedSize = h / (ufont.getFontDescriptor(7, BaseField.BORDER_WIDTH_THIN) - ufont.getFontDescriptor(6, BaseField.BORDER_WIDTH_THIN));
                    changeFontSize(phrase, BaseField.BORDER_WIDTH_THIN);
                    float wd = ColumnText.getWidth(phrase, rtl, 0);
                    if (wd == 0.0f) {
                        usize = maxCalculatedSize;
                    } else {
                        usize = Math.min(maxCalculatedSize, ((this.box.getWidth() - this.extraMarginLeft) - (4.0f * offsetX)) / wd);
                    }
                    if (usize < 4.0f) {
                        usize = 4.0f;
                    }
                }
                changeFontSize(phrase, usize);
                float offsetY = offX + (((this.box.getHeight() - (BaseField.BORDER_WIDTH_MEDIUM * offX)) - ufont.getFontDescriptor(1, usize)) / BaseField.BORDER_WIDTH_MEDIUM);
                if (offsetY < offX) {
                    offsetY = offX;
                }
                if (offsetY - offX < (-ufont.getFontDescriptor(3, usize))) {
                    offsetY = Math.min((-ufont.getFontDescriptor(3, usize)) + offX, Math.max(offsetY, (this.box.getHeight() - offX) - ufont.getFontDescriptor(1, usize)));
                }
                if ((this.options & 16777216) == 0 || this.maxCharacterLength <= 0) {
                    float x;
                    switch (this.alignment) {
                        case 1:
                            x = this.extraMarginLeft + (this.box.getWidth() / BaseField.BORDER_WIDTH_MEDIUM);
                            break;
                        case 2:
                            x = (this.extraMarginLeft + this.box.getWidth()) - (BaseField.BORDER_WIDTH_MEDIUM * offsetX);
                            break;
                        default:
                            x = this.extraMarginLeft + (BaseField.BORDER_WIDTH_MEDIUM * offsetX);
                            break;
                    }
                    ColumnText.showTextAligned(app, this.alignment, phrase, x, offsetY - this.extraMarginTop, 0.0f, rtl, 0);
                } else {
                    int textLen = Math.min(this.maxCharacterLength, ptext.length());
                    int position = 0;
                    if (this.alignment == 2) {
                        position = this.maxCharacterLength - textLen;
                    } else if (this.alignment == 1) {
                        position = (this.maxCharacterLength - textLen) / 2;
                    }
                    step = (this.box.getWidth() - this.extraMarginLeft) / ((float) this.maxCharacterLength);
                    float start = (step / BaseField.BORDER_WIDTH_MEDIUM) + (((float) position) * step);
                    if (this.textColor == null) {
                        app.setGrayFill(0.0f);
                    } else {
                        app.setColorFill(this.textColor);
                    }
                    app.beginText();
                    for (int k = 0; k < phrase.size(); k++) {
                        Chunk ck = (Chunk) phrase.get(k);
                        BaseFont bf = ck.getFont().getBaseFont();
                        app.setFontAndSize(bf, usize);
                        StringBuffer sb = ck.append("");
                        for (int j = 0; j < sb.length(); j++) {
                            String c = sb.substring(j, j + 1);
                            app.setTextMatrix((this.extraMarginLeft + start) - (bf.getWidthPoint(c, usize) / BaseField.BORDER_WIDTH_MEDIUM), offsetY - this.extraMarginTop);
                            app.showText(c);
                            start += step;
                        }
                    }
                    app.endText();
                }
            }
            app.restoreState();
            app.endVariableText();
        }
        return app;
    }

    PdfAppearance getListAppearance() throws IOException, DocumentException {
        PdfAppearance app = getBorderAppearance();
        if (!(this.choices == null || this.choices.length == 0)) {
            BaseColor fcolor;
            app.beginVariableText();
            int topChoice = getTopChoice();
            BaseFont ufont = getRealFont();
            float usize = this.fontSize;
            if (usize == 0.0f) {
                usize = HtmlUtilities.DEFAULT_FONT_SIZE;
            }
            boolean borderExtra = this.borderStyle == 2 || this.borderStyle == 3;
            float h = this.box.getHeight() - (this.borderWidth * BaseField.BORDER_WIDTH_MEDIUM);
            float offsetX = this.borderWidth;
            if (borderExtra) {
                h -= this.borderWidth * BaseField.BORDER_WIDTH_MEDIUM;
                offsetX *= BaseField.BORDER_WIDTH_MEDIUM;
            }
            float leading = ufont.getFontDescriptor(8, usize) - ufont.getFontDescriptor(6, usize);
            int first = topChoice;
            int last = first + (((int) (h / leading)) + 1);
            if (last > this.choices.length) {
                last = this.choices.length;
            }
            this.topFirst = first;
            app.saveState();
            app.rectangle(offsetX, offsetX, this.box.getWidth() - (BaseField.BORDER_WIDTH_MEDIUM * offsetX), this.box.getHeight() - (BaseField.BORDER_WIDTH_MEDIUM * offsetX));
            app.clip();
            app.newPath();
            if (this.textColor == null) {
                fcolor = GrayColor.GRAYBLACK;
            } else {
                fcolor = this.textColor;
            }
            app.setColorFill(new BaseColor(10, 36, 106));
            for (int curVal = 0; curVal < this.choiceSelections.size(); curVal++) {
                int curChoice = ((Integer) this.choiceSelections.get(curVal)).intValue();
                if (curChoice >= first && curChoice <= last) {
                    app.rectangle(offsetX, (offsetX + h) - (((float) ((curChoice - first) + 1)) * leading), this.box.getWidth() - (BaseField.BORDER_WIDTH_MEDIUM * offsetX), leading);
                    app.fill();
                }
            }
            float xp = offsetX * BaseField.BORDER_WIDTH_MEDIUM;
            float yp = (offsetX + h) - ufont.getFontDescriptor(8, usize);
            int idx = first;
            while (idx < last) {
                BaseColor textCol;
                String ptext = this.choices[idx];
                int rtl = checkRTL(ptext) ? 2 : 1;
                ptext = removeCRLF(ptext);
                if (this.choiceSelections.contains(Integer.valueOf(idx))) {
                    textCol = GrayColor.GRAYWHITE;
                } else {
                    textCol = fcolor;
                }
                ColumnText.showTextAligned(app, 0, composePhrase(ptext, ufont, textCol, usize), xp, yp, 0.0f, rtl, 0);
                idx++;
                yp -= leading;
            }
            app.restoreState();
            app.endVariableText();
        }
        return app;
    }

    public PdfFormField getTextField() throws IOException, DocumentException {
        if (this.maxCharacterLength <= 0) {
            this.options &= -16777217;
        }
        if ((this.options & 16777216) != 0) {
            this.options &= -4097;
        }
        PdfFormField field = PdfFormField.createTextField(this.writer, false, false, this.maxCharacterLength);
        field.setWidget(this.box, PdfAnnotation.HIGHLIGHT_INVERT);
        switch (this.alignment) {
            case 1:
                field.setQuadding(1);
                break;
            case 2:
                field.setQuadding(2);
                break;
        }
        if (this.rotation != 0) {
            field.setMKRotation(this.rotation);
        }
        if (this.fieldName != null) {
            field.setFieldName(this.fieldName);
            if (!"".equals(this.text)) {
                field.setValueAsString(this.text);
            }
            if (this.defaultText != null) {
                field.setDefaultValueAsString(this.defaultText);
            }
            if ((this.options & 1) != 0) {
                field.setFieldFlags(1);
            }
            if ((this.options & 2) != 0) {
                field.setFieldFlags(2);
            }
            if ((this.options & 4096) != 0) {
                field.setFieldFlags(4096);
            }
            if ((this.options & 8388608) != 0) {
                field.setFieldFlags(8388608);
            }
            if ((this.options & 8192) != 0) {
                field.setFieldFlags(8192);
            }
            if ((this.options & 1048576) != 0) {
                field.setFieldFlags(1048576);
            }
            if ((this.options & 4194304) != 0) {
                field.setFieldFlags(4194304);
            }
            if ((this.options & 16777216) != 0) {
                field.setFieldFlags(16777216);
            }
        }
        field.setBorderStyle(new PdfBorderDictionary(this.borderWidth, this.borderStyle, new PdfDashPattern(BaseField.BORDER_WIDTH_THICK)));
        PdfAppearance tp = getAppearance();
        field.setAppearance(PdfAnnotation.APPEARANCE_NORMAL, tp);
        PdfAppearance da = (PdfAppearance) tp.getDuplicate();
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
        return field;
    }

    public PdfFormField getComboField() throws IOException, DocumentException {
        return getChoiceField(false);
    }

    public PdfFormField getListField() throws IOException, DocumentException {
        return getChoiceField(true);
    }

    private int getTopChoice() {
        if (this.choiceSelections == null || this.choiceSelections.size() == 0) {
            return 0;
        }
        Integer firstValue = (Integer) this.choiceSelections.get(0);
        if (firstValue == null) {
            return 0;
        }
        if (this.choices == null) {
            return 0;
        }
        if (this.visibleTopChoice != -1) {
            return this.visibleTopChoice;
        }
        return Math.max(0, Math.min(firstValue.intValue(), this.choices.length));
    }

    protected PdfFormField getChoiceField(boolean isList) throws IOException, DocumentException {
        PdfFormField field;
        PdfAppearance tp;
        this.options &= -16781313;
        String[] uchoices = this.choices;
        if (uchoices == null) {
            uchoices = new String[0];
        }
        int topChoice = getTopChoice();
        if (uchoices.length > 0 && topChoice >= 0) {
            this.text = uchoices[topChoice];
        }
        if (this.text == null) {
            this.text = "";
        }
        String[][] mix = null;
        if (this.choiceExports != null) {
            int k;
            mix = (String[][]) Array.newInstance(String.class, new int[]{uchoices.length, 2});
            for (k = 0; k < mix.length; k++) {
                String[] strArr = mix[k];
                String[] strArr2 = mix[k];
                String str = uchoices[k];
                strArr2[1] = str;
                strArr[0] = str;
            }
            int top = Math.min(uchoices.length, this.choiceExports.length);
            for (k = 0; k < top; k++) {
                if (this.choiceExports[k] != null) {
                    mix[k][0] = this.choiceExports[k];
                }
            }
            if (isList) {
                field = PdfFormField.createList(this.writer, mix, topChoice);
            } else {
                field = PdfFormField.createCombo(this.writer, (this.options & 262144) != 0, mix, topChoice);
            }
        } else if (isList) {
            field = PdfFormField.createList(this.writer, uchoices, topChoice);
        } else {
            field = PdfFormField.createCombo(this.writer, (this.options & 262144) != 0, uchoices, topChoice);
        }
        field.setWidget(this.box, PdfAnnotation.HIGHLIGHT_INVERT);
        if (this.rotation != 0) {
            field.setMKRotation(this.rotation);
        }
        if (this.fieldName != null) {
            field.setFieldName(this.fieldName);
            if (uchoices.length > 0) {
                if (mix != null) {
                    if (this.choiceSelections.size() < 2) {
                        field.setValueAsString(mix[topChoice][0]);
                        field.setDefaultValueAsString(mix[topChoice][0]);
                    } else {
                        writeMultipleValues(field, mix);
                    }
                } else if (this.choiceSelections.size() < 2) {
                    field.setValueAsString(this.text);
                    field.setDefaultValueAsString(this.text);
                } else {
                    writeMultipleValues(field, (String[][]) null);
                }
            }
            if ((this.options & 1) != 0) {
                field.setFieldFlags(1);
            }
            if ((this.options & 2) != 0) {
                field.setFieldFlags(2);
            }
            if ((this.options & 4194304) != 0) {
                field.setFieldFlags(4194304);
            }
            if ((this.options & 2097152) != 0) {
                field.setFieldFlags(2097152);
            }
        }
        field.setBorderStyle(new PdfBorderDictionary(this.borderWidth, this.borderStyle, new PdfDashPattern(BaseField.BORDER_WIDTH_THICK)));
        if (isList) {
            tp = getListAppearance();
            if (this.topFirst > 0) {
                field.put(PdfName.TI, new PdfNumber(this.topFirst));
            }
        } else {
            tp = getAppearance();
        }
        field.setAppearance(PdfAnnotation.APPEARANCE_NORMAL, tp);
        PdfAppearance da = (PdfAppearance) tp.getDuplicate();
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
        return field;
    }

    private void writeMultipleValues(PdfFormField field, String[][] mix) {
        PdfArray indexes = new PdfArray();
        PdfArray values = new PdfArray();
        for (int i = 0; i < this.choiceSelections.size(); i++) {
            int idx = ((Integer) this.choiceSelections.get(i)).intValue();
            indexes.add(new PdfNumber(idx));
            if (mix != null) {
                values.add(new PdfString(mix[idx][0]));
            } else if (this.choices != null) {
                values.add(new PdfString(this.choices[idx]));
            }
        }
        field.put(PdfName.f136V, values);
        field.put(PdfName.f124I, indexes);
    }

    public String getDefaultText() {
        return this.defaultText;
    }

    public void setDefaultText(String defaultText) {
        this.defaultText = defaultText;
    }

    public String[] getChoices() {
        return this.choices;
    }

    public void setChoices(String[] choices) {
        this.choices = choices;
    }

    public String[] getChoiceExports() {
        return this.choiceExports;
    }

    public void setChoiceExports(String[] choiceExports) {
        this.choiceExports = choiceExports;
    }

    public int getChoiceSelection() {
        return getTopChoice();
    }

    public ArrayList<Integer> getChoiceSelections() {
        return this.choiceSelections;
    }

    public void setVisibleTopChoice(int visibleTopChoice) {
        if (visibleTopChoice >= 0 && this.choices != null && visibleTopChoice < this.choices.length) {
            this.visibleTopChoice = visibleTopChoice;
        }
    }

    public int getVisibleTopChoice() {
        return this.visibleTopChoice;
    }

    public void setChoiceSelection(int choiceSelection) {
        this.choiceSelections = new ArrayList();
        this.choiceSelections.add(Integer.valueOf(choiceSelection));
    }

    public void addChoiceSelection(int selection) {
        if ((this.options & 2097152) != 0) {
            this.choiceSelections.add(Integer.valueOf(selection));
        }
    }

    public void setChoiceSelections(ArrayList<Integer> selections) {
        if (selections != null) {
            this.choiceSelections = new ArrayList(selections);
            if (this.choiceSelections.size() > 1 && (this.options & 2097152) == 0) {
                while (this.choiceSelections.size() > 1) {
                    this.choiceSelections.remove(1);
                }
                return;
            }
            return;
        }
        this.choiceSelections.clear();
    }

    int getTopFirst() {
        return this.topFirst;
    }

    public void setExtraMargin(float extraMarginLeft, float extraMarginTop) {
        this.extraMarginLeft = extraMarginLeft;
        this.extraMarginTop = extraMarginTop;
    }

    public ArrayList<BaseFont> getSubstitutionFonts() {
        return this.substitutionFonts;
    }

    public void setSubstitutionFonts(ArrayList<BaseFont> substitutionFonts) {
        this.substitutionFonts = substitutionFonts;
    }

    public BaseFont getExtensionFont() {
        return this.extensionFont;
    }

    public void setExtensionFont(BaseFont extensionFont) {
        this.extensionFont = extensionFont;
    }
}
