package org.apache.poi.sl.usermodel;

import java.util.List;

public interface TextShape<S extends Shape<S, P>, P extends TextParagraph<S, P, ?>> extends SimpleShape<S, P>, Iterable<P> {

    public enum TextAutofit {
        NONE,
        NORMAL,
        SHAPE
    }

    public enum TextDirection {
        HORIZONTAL,
        VERTICAL,
        VERTICAL_270,
        STACKED
    }

    public enum TextPlaceholder {
        TITLE,
        BODY,
        CENTER_TITLE,
        CENTER_BODY,
        HALF_BODY,
        QUARTER_BODY,
        NOTES,
        OTHER
    }

    TextRun appendText(String str, boolean z);

    Insets2D getInsets();

    String getText();

    TextDirection getTextDirection();

    double getTextHeight();

    List<? extends TextParagraph<S, P, ?>> getTextParagraphs();

    TextPlaceholder getTextPlaceholder();

    Double getTextRotation();

    VerticalAlignment getVerticalAlignment();

    boolean getWordWrap();

    boolean isHorizontalCentered();

    void setHorizontalCentered(Boolean bool);

    void setInsets(Insets2D insets2D);

    TextRun setText(String str);

    void setTextDirection(TextDirection textDirection);

    void setTextPlaceholder(TextPlaceholder textPlaceholder);

    void setTextRotation(Double d);

    void setVerticalAlignment(VerticalAlignment verticalAlignment);

    void setWordWrap(boolean z);
}
