package org.apache.poi.sl.draw.geom;

import java.awt.geom.Path2D.Double;
import org.apache.poi.sl.draw.binding.CTAdjPoint2D;

public class QuadToCommand implements PathCommand {
    private String arg1;
    private String arg2;
    private String arg3;
    private String arg4;

    QuadToCommand(CTAdjPoint2D pt1, CTAdjPoint2D pt2) {
        this.arg1 = pt1.getX().toString();
        this.arg2 = pt1.getY().toString();
        this.arg3 = pt2.getX().toString();
        this.arg4 = pt2.getY().toString();
    }

    public void execute(Double path, Context ctx) {
        path.quadTo(ctx.getValue(this.arg1), ctx.getValue(this.arg2), ctx.getValue(this.arg3), ctx.getValue(this.arg4));
    }
}
