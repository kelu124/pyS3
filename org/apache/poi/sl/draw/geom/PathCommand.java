package org.apache.poi.sl.draw.geom;

import java.awt.geom.Path2D.Double;

public interface PathCommand {
    void execute(Double doubleR, Context context);
}
