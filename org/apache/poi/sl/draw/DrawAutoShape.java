package org.apache.poi.sl.draw;

import org.apache.poi.sl.usermodel.AutoShape;

public class DrawAutoShape extends DrawTextShape {
    public DrawAutoShape(AutoShape<?, ?> shape) {
        super(shape);
    }
}
