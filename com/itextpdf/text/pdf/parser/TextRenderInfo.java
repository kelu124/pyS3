package com.itextpdf.text.pdf.parser;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.pdf.BaseField;
import com.itextpdf.text.pdf.DocumentFont;
import com.itextpdf.text.pdf.PdfString;
import com.itextpdf.text.xml.xmp.XmpWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TextRenderInfo {
    private final GraphicsState gs;
    private final Collection<MarkedContentInfo> markedContentInfos;
    private final PdfString string;
    private String text = null;
    private final Matrix textToUserSpaceTransformMatrix;
    private Float unscaledWidth = null;

    TextRenderInfo(PdfString string, GraphicsState gs, Matrix textMatrix, Collection<MarkedContentInfo> markedContentInfo) {
        this.string = string;
        this.textToUserSpaceTransformMatrix = textMatrix.multiply(gs.ctm);
        this.gs = gs;
        this.markedContentInfos = new ArrayList(markedContentInfo);
    }

    private TextRenderInfo(TextRenderInfo parent, PdfString string, float horizontalOffset) {
        this.string = string;
        this.textToUserSpaceTransformMatrix = new Matrix(horizontalOffset, 0.0f).multiply(parent.textToUserSpaceTransformMatrix);
        this.gs = parent.gs;
        this.markedContentInfos = parent.markedContentInfos;
    }

    public String getText() {
        if (this.text == null) {
            this.text = decode(this.string);
        }
        return this.text;
    }

    public PdfString getPdfString() {
        return this.string;
    }

    public boolean hasMcid(int mcid) {
        return hasMcid(mcid, false);
    }

    public boolean hasMcid(int mcid, boolean checkTheTopmostLevelOnly) {
        if (!checkTheTopmostLevelOnly) {
            for (MarkedContentInfo info : this.markedContentInfos) {
                if (info.hasMcid() && info.getMcid() == mcid) {
                    return true;
                }
            }
        } else if (this.markedContentInfos instanceof ArrayList) {
            Integer infoMcid = getMcid();
            if (infoMcid == null) {
                return false;
            }
            if (infoMcid.intValue() == mcid) {
                return true;
            }
            return false;
        }
        return false;
    }

    public Integer getMcid() {
        if (!(this.markedContentInfos instanceof ArrayList)) {
            return null;
        }
        MarkedContentInfo info;
        ArrayList<MarkedContentInfo> mci = this.markedContentInfos;
        if (mci.size() > 0) {
            info = (MarkedContentInfo) mci.get(mci.size() - 1);
        } else {
            info = null;
        }
        if (info == null || !info.hasMcid()) {
            return null;
        }
        return Integer.valueOf(info.getMcid());
    }

    float getUnscaledWidth() {
        if (this.unscaledWidth == null) {
            this.unscaledWidth = Float.valueOf(getPdfStringWidth(this.string, false));
        }
        return this.unscaledWidth.floatValue();
    }

    public LineSegment getBaseline() {
        return getUnscaledBaselineWithOffset(0.0f + this.gs.rise).transformBy(this.textToUserSpaceTransformMatrix);
    }

    public LineSegment getAscentLine() {
        return getUnscaledBaselineWithOffset(this.gs.rise + this.gs.getFont().getFontDescriptor(1, this.gs.getFontSize())).transformBy(this.textToUserSpaceTransformMatrix);
    }

    public LineSegment getDescentLine() {
        return getUnscaledBaselineWithOffset(this.gs.rise + this.gs.getFont().getFontDescriptor(3, this.gs.getFontSize())).transformBy(this.textToUserSpaceTransformMatrix);
    }

    private LineSegment getUnscaledBaselineWithOffset(float yOffset) {
        return new LineSegment(new Vector(0.0f, yOffset, BaseField.BORDER_WIDTH_THIN), new Vector(getUnscaledWidth() - (this.gs.characterSpacing * this.gs.horizontalScaling), yOffset, BaseField.BORDER_WIDTH_THIN));
    }

    public DocumentFont getFont() {
        return this.gs.getFont();
    }

    public float getRise() {
        if (this.gs.rise == 0.0f) {
            return 0.0f;
        }
        return convertHeightFromTextSpaceToUserSpace(this.gs.rise);
    }

    private float convertWidthFromTextSpaceToUserSpace(float width) {
        return new LineSegment(new Vector(0.0f, 0.0f, BaseField.BORDER_WIDTH_THIN), new Vector(width, 0.0f, BaseField.BORDER_WIDTH_THIN)).transformBy(this.textToUserSpaceTransformMatrix).getLength();
    }

    private float convertHeightFromTextSpaceToUserSpace(float height) {
        return new LineSegment(new Vector(0.0f, 0.0f, BaseField.BORDER_WIDTH_THIN), new Vector(0.0f, height, BaseField.BORDER_WIDTH_THIN)).transformBy(this.textToUserSpaceTransformMatrix).getLength();
    }

    public float getSingleSpaceWidth() {
        return convertWidthFromTextSpaceToUserSpace(getUnscaledFontSpaceWidth());
    }

    public int getTextRenderMode() {
        return this.gs.renderMode;
    }

    public BaseColor getFillColor() {
        return this.gs.fillColor;
    }

    public BaseColor getStrokeColor() {
        return this.gs.strokeColor;
    }

    private float getUnscaledFontSpaceWidth() {
        char charToUse = ' ';
        if (this.gs.font.getWidth(32) == 0) {
            charToUse = ' ';
        }
        return getStringWidth(String.valueOf(charToUse));
    }

    private float getStringWidth(String string) {
        float totalWidth = 0.0f;
        for (int i = 0; i < string.length(); i++) {
            char c = string.charAt(i);
            totalWidth += (((this.gs.fontSize * (((float) this.gs.font.getWidth(c)) / 1000.0f)) + this.gs.characterSpacing) + (c == ' ' ? this.gs.wordSpacing : 0.0f)) * this.gs.horizontalScaling;
        }
        return totalWidth;
    }

    private float getPdfStringWidth(PdfString string, boolean singleCharString) {
        if (singleCharString) {
            float[] widthAndWordSpacing = getWidthAndWordSpacing(string, singleCharString);
            return (((widthAndWordSpacing[0] * this.gs.fontSize) + this.gs.characterSpacing) + widthAndWordSpacing[1]) * this.gs.horizontalScaling;
        }
        float totalWidth = 0.0f;
        for (PdfString str : splitString(string)) {
            totalWidth += getPdfStringWidth(str, true);
        }
        return totalWidth;
    }

    public List<TextRenderInfo> getCharacterRenderInfos() {
        List<TextRenderInfo> rslt = new ArrayList(this.string.length());
        PdfString[] strings = splitString(this.string);
        float totalWidth = 0.0f;
        for (int i = 0; i < strings.length; i++) {
            float[] widthAndWordSpacing = getWidthAndWordSpacing(strings[i], true);
            rslt.add(new TextRenderInfo(this, strings[i], totalWidth));
            totalWidth += (((widthAndWordSpacing[0] * this.gs.fontSize) + this.gs.characterSpacing) + widthAndWordSpacing[1]) * this.gs.horizontalScaling;
        }
        for (TextRenderInfo tri : rslt) {
            tri.getUnscaledWidth();
        }
        return rslt;
    }

    private float[] getWidthAndWordSpacing(PdfString string, boolean singleCharString) {
        if (singleCharString) {
            result = new float[2];
            String decoded = decode(string);
            result[0] = ((float) this.gs.font.getWidth(getCharCode(decoded))) / 1000.0f;
            result[1] = decoded.equals(" ") ? this.gs.wordSpacing : 0.0f;
            return result;
        }
        throw new UnsupportedOperationException();
    }

    private String decode(PdfString in) {
        byte[] bytes = in.getBytes();
        return this.gs.font.decode(bytes, 0, bytes.length);
    }

    private int getCharCode(String string) {
        try {
            byte[] b = string.getBytes(XmpWriter.UTF16BE);
            int value = 0;
            for (int i = 0; i < b.length - 1; i++) {
                value = (value + (b[i] & 255)) << 8;
            }
            return value + (b[b.length - 1] & 255);
        } catch (UnsupportedEncodingException e) {
            return 0;
        }
    }

    private PdfString[] splitString(PdfString string) {
        List<PdfString> strings = new ArrayList();
        String stringValue = string.toString();
        int i = 0;
        while (i < stringValue.length()) {
            PdfString newString = new PdfString(stringValue.substring(i, i + 1), string.getEncoding());
            if (decode(newString).length() == 0 && i < stringValue.length() - 1) {
                newString = new PdfString(stringValue.substring(i, i + 2), string.getEncoding());
                i++;
            }
            strings.add(newString);
            i++;
        }
        return (PdfString[]) strings.toArray(new PdfString[strings.size()]);
    }
}
