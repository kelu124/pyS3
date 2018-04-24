package org.apache.poi.sl.usermodel;

import java.util.List;

public interface Notes<S extends Shape<S, P>, P extends TextParagraph<S, P, ?>> extends Sheet<S, P> {
    List<? extends List<P>> getTextParagraphs();
}
