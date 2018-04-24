package org.apache.poi.sl.draw;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.apache.poi.sl.draw.geom.Outline;
import org.apache.poi.sl.draw.geom.Path;
import org.apache.poi.sl.usermodel.FillStyle;
import org.apache.poi.sl.usermodel.FreeformShape;
import org.apache.poi.sl.usermodel.StrokeStyle;

public class DrawFreeformShape extends DrawAutoShape {
    public DrawFreeformShape(FreeformShape<?, ?> shape) {
        super(shape);
    }

    protected Collection<Outline> computeOutlines(Graphics2D graphics) {
        boolean z;
        boolean z2 = true;
        List<Outline> lst = new ArrayList();
        Shape sh = getShape().getPath();
        FillStyle fs = getShape().getFillStyle();
        StrokeStyle ss = getShape().getStrokeStyle();
        if (fs != null) {
            z = true;
        } else {
            z = false;
        }
        if (ss == null) {
            z2 = false;
        }
        lst.add(new Outline(sh, new Path(z, z2)));
        return lst;
    }

    protected FreeformShape<?, ?> getShape() {
        return (FreeformShape) this.shape;
    }
}
