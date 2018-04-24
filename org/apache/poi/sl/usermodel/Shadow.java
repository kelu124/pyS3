package org.apache.poi.sl.usermodel;

import org.apache.poi.sl.usermodel.PaintStyle.SolidPaint;

public interface Shadow<S extends Shape<S, P>, P extends TextParagraph<S, P, ?>> {
    double getAngle();

    double getBlur();

    double getDistance();

    SolidPaint getFillStyle();

    SimpleShape<S, P> getShadowParent();
}
