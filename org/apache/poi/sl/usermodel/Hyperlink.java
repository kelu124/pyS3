package org.apache.poi.sl.usermodel;

public interface Hyperlink<S extends Shape<S, P>, P extends TextParagraph<S, P, ?>> extends org.apache.poi.common.usermodel.Hyperlink {
    void linkToEmail(String str);

    void linkToFirstSlide();

    void linkToLastSlide();

    void linkToNextSlide();

    void linkToPreviousSlide();

    void linkToSlide(Slide<S, P> slide);

    void linkToUrl(String str);
}
