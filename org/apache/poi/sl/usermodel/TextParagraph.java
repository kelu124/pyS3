package org.apache.poi.sl.usermodel;

import java.awt.Color;
import java.util.List;

public interface TextParagraph<S extends Shape<S, P>, P extends TextParagraph<S, P, T>, T extends TextRun> extends Iterable<T> {

    public interface BulletStyle {
        AutoNumberingScheme getAutoNumberingScheme();

        Integer getAutoNumberingStartAt();

        String getBulletCharacter();

        String getBulletFont();

        PaintStyle getBulletFontColor();

        Double getBulletFontSize();

        void setBulletFontColor(Color color);

        void setBulletFontColor(PaintStyle paintStyle);
    }

    public enum FontAlign {
        AUTO,
        TOP,
        CENTER,
        BASELINE,
        BOTTOM
    }

    public enum TextAlign {
        LEFT,
        CENTER,
        RIGHT,
        JUSTIFY,
        JUSTIFY_LOW,
        DIST,
        THAI_DIST
    }

    BulletStyle getBulletStyle();

    String getDefaultFontFamily();

    Double getDefaultFontSize();

    Double getDefaultTabSize();

    FontAlign getFontAlign();

    Double getIndent();

    int getIndentLevel();

    Double getLeftMargin();

    Double getLineSpacing();

    TextShape<S, P> getParentShape();

    Double getRightMargin();

    Double getSpaceAfter();

    Double getSpaceBefore();

    TextAlign getTextAlign();

    List<T> getTextRuns();

    boolean isHeaderOrFooter();

    void setBulletStyle(Object... objArr);

    void setIndent(Double d);

    void setIndentLevel(int i);

    void setLeftMargin(Double d);

    void setLineSpacing(Double d);

    void setRightMargin(Double d);

    void setSpaceAfter(Double d);

    void setSpaceBefore(Double d);

    void setTextAlign(TextAlign textAlign);
}
