package org.apache.poi.sl.draw.geom;

import java.awt.geom.Path2D.Double;

public class ClosePathCommand implements PathCommand {
    ClosePathCommand() {
    }

    public void execute(Double path, Context ctx) {
        path.closePath();
    }
}
