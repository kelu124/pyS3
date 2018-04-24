package org.apache.poi.sl.draw.geom;

import java.awt.geom.Arc2D;
import java.awt.geom.Path2D.Double;
import java.awt.geom.Point2D;
import org.apache.poi.sl.draw.binding.CTPath2DArcTo;

public class ArcToCommand implements PathCommand {
    private String hr;
    private String stAng;
    private String swAng;
    private String wr;

    ArcToCommand(CTPath2DArcTo arc) {
        this.hr = arc.getHR().toString();
        this.wr = arc.getWR().toString();
        this.stAng = arc.getStAng().toString();
        this.swAng = arc.getSwAng().toString();
    }

    public void execute(Double path, Context ctx) {
        double rx = ctx.getValue(this.wr);
        double ry = ctx.getValue(this.hr);
        double start = ctx.getValue(this.stAng) / 60000.0d;
        double extent = ctx.getValue(this.swAng) / 60000.0d;
        Point2D pt = path.getCurrentPoint();
        path.append(new Arc2D.Double((pt.getX() - rx) - (Math.cos(Math.toRadians(start)) * rx), (pt.getY() - ry) - (Math.sin(Math.toRadians(start)) * ry), 2.0d * rx, 2.0d * ry, -start, -extent, 0), true);
    }
}
