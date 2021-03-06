package org.apache.poi.sl.draw.geom;

import java.awt.geom.Path2D.Double;
import org.apache.poi.sl.draw.binding.CTAdjPoint2D;

public class MoveToCommand implements PathCommand {
    private String arg1;
    private String arg2;

    MoveToCommand(CTAdjPoint2D pt) {
        this.arg1 = pt.getX().toString();
        this.arg2 = pt.getY().toString();
    }

    MoveToCommand(String s1, String s2) {
        this.arg1 = s1;
        this.arg2 = s2;
    }

    public void execute(Double path, Context ctx) {
        path.moveTo(ctx.getValue(this.arg1), ctx.getValue(this.arg2));
    }
}
