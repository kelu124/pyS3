package org.apache.poi.sl.draw;

import org.apache.poi.sl.usermodel.MasterSheet;
import org.apache.poi.sl.usermodel.Shape;
import org.apache.poi.sl.usermodel.SimpleShape;

public class DrawMasterSheet extends DrawSheet {
    public DrawMasterSheet(MasterSheet<?, ?> sheet) {
        super(sheet);
    }

    protected boolean canDraw(Shape<?, ?> shape) {
        if (!(shape instanceof SimpleShape) || ((SimpleShape) shape).getPlaceholder() == null) {
            return true;
        }
        return false;
    }
}
