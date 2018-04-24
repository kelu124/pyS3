package org.apache.poi.sl.usermodel;

import java.awt.geom.Path2D.Double;

public interface FreeformShape<S extends Shape<S, P>, P extends TextParagraph<S, P, ?>> extends AutoShape<S, P> {
    Double getPath();

    int setPath(Double doubleR);
}
