package org.apache.poi.sl.draw.geom;

import java.awt.geom.Path2D.Double;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.sl.draw.binding.CTAdjPoint2D;
import org.apache.poi.sl.draw.binding.CTPath2D;
import org.apache.poi.sl.draw.binding.CTPath2DArcTo;
import org.apache.poi.sl.draw.binding.CTPath2DClose;
import org.apache.poi.sl.draw.binding.CTPath2DCubicBezierTo;
import org.apache.poi.sl.draw.binding.CTPath2DLineTo;
import org.apache.poi.sl.draw.binding.CTPath2DMoveTo;
import org.apache.poi.sl.draw.binding.CTPath2DQuadBezierTo;
import org.apache.poi.sl.draw.binding.STPathFillMode;

public class Path {
    boolean _fill;
    long _h;
    boolean _stroke;
    long _w;
    private final List<PathCommand> commands;

    public Path() {
        this(true, true);
    }

    public Path(boolean fill, boolean stroke) {
        this.commands = new ArrayList();
        this._w = -1;
        this._h = -1;
        this._fill = fill;
        this._stroke = stroke;
    }

    public Path(CTPath2D spPath) {
        this._fill = spPath.getFill() != STPathFillMode.NONE;
        this._stroke = spPath.isStroke();
        this._w = spPath.isSetW() ? spPath.getW() : -1;
        this._h = spPath.isSetH() ? spPath.getH() : -1;
        this.commands = new ArrayList();
        for (CTPath2DArcTo ch : spPath.getCloseOrMoveToOrLnTo()) {
            if (ch instanceof CTPath2DMoveTo) {
                this.commands.add(new MoveToCommand(((CTPath2DMoveTo) ch).getPt()));
            } else if (ch instanceof CTPath2DLineTo) {
                this.commands.add(new LineToCommand(((CTPath2DLineTo) ch).getPt()));
            } else if (ch instanceof CTPath2DArcTo) {
                this.commands.add(new ArcToCommand(ch));
            } else if (ch instanceof CTPath2DQuadBezierTo) {
                CTPath2DQuadBezierTo bez = (CTPath2DQuadBezierTo) ch;
                this.commands.add(new QuadToCommand((CTAdjPoint2D) bez.getPt().get(0), (CTAdjPoint2D) bez.getPt().get(1)));
            } else if (ch instanceof CTPath2DCubicBezierTo) {
                CTPath2DCubicBezierTo bez2 = (CTPath2DCubicBezierTo) ch;
                this.commands.add(new CurveToCommand((CTAdjPoint2D) bez2.getPt().get(0), (CTAdjPoint2D) bez2.getPt().get(1), (CTAdjPoint2D) bez2.getPt().get(2)));
            } else if (ch instanceof CTPath2DClose) {
                this.commands.add(new ClosePathCommand());
            } else {
                throw new IllegalStateException("Unsupported path segment: " + ch);
            }
        }
    }

    public void addCommand(PathCommand cmd) {
        this.commands.add(cmd);
    }

    public Double getPath(Context ctx) {
        Double path = new Double();
        for (PathCommand cmd : this.commands) {
            cmd.execute(path, ctx);
        }
        return path;
    }

    public boolean isStroked() {
        return this._stroke;
    }

    public boolean isFilled() {
        return this._fill;
    }

    public long getW() {
        return this._w;
    }

    public long getH() {
        return this._h;
    }
}
