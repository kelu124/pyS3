package org.apache.poi.sl.usermodel;

import java.awt.geom.Rectangle2D;

public interface PlaceableShape<S extends Shape<S, P>, P extends TextParagraph<S, P, ?>> {
    Rectangle2D getAnchor();

    boolean getFlipHorizontal();

    boolean getFlipVertical();

    ShapeContainer<S, P> getParent();

    double getRotation();

    Sheet<S, P> getSheet();

    void setAnchor(Rectangle2D rectangle2D);

    void setFlipHorizontal(boolean z);

    void setFlipVertical(boolean z);

    void setRotation(double d);
}
