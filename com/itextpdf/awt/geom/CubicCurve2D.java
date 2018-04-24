package com.itextpdf.awt.geom;

import com.itextpdf.awt.geom.gl.Crossing;
import com.itextpdf.awt.geom.misc.Messages;
import java.util.NoSuchElementException;

public abstract class CubicCurve2D implements Shape, Cloneable {

    public static class Double extends CubicCurve2D {
        public double ctrlx1;
        public double ctrlx2;
        public double ctrly1;
        public double ctrly2;
        public double x1;
        public double x2;
        public double y1;
        public double y2;

        public Double(double x1, double y1, double ctrlx1, double ctrly1, double ctrlx2, double ctrly2, double x2, double y2) {
            setCurve(x1, y1, ctrlx1, ctrly1, ctrlx2, ctrly2, x2, y2);
        }

        public double getX1() {
            return this.x1;
        }

        public double getY1() {
            return this.y1;
        }

        public double getCtrlX1() {
            return this.ctrlx1;
        }

        public double getCtrlY1() {
            return this.ctrly1;
        }

        public double getCtrlX2() {
            return this.ctrlx2;
        }

        public double getCtrlY2() {
            return this.ctrly2;
        }

        public double getX2() {
            return this.x2;
        }

        public double getY2() {
            return this.y2;
        }

        public Point2D getP1() {
            return new com.itextpdf.awt.geom.Point2D.Double(this.x1, this.y1);
        }

        public Point2D getCtrlP1() {
            return new com.itextpdf.awt.geom.Point2D.Double(this.ctrlx1, this.ctrly1);
        }

        public Point2D getCtrlP2() {
            return new com.itextpdf.awt.geom.Point2D.Double(this.ctrlx2, this.ctrly2);
        }

        public Point2D getP2() {
            return new com.itextpdf.awt.geom.Point2D.Double(this.x2, this.y2);
        }

        public void setCurve(double x1, double y1, double ctrlx1, double ctrly1, double ctrlx2, double ctrly2, double x2, double y2) {
            this.x1 = x1;
            this.y1 = y1;
            this.ctrlx1 = ctrlx1;
            this.ctrly1 = ctrly1;
            this.ctrlx2 = ctrlx2;
            this.ctrly2 = ctrly2;
            this.x2 = x2;
            this.y2 = y2;
        }

        public Rectangle2D getBounds2D() {
            double rx1 = Math.min(Math.min(this.x1, this.x2), Math.min(this.ctrlx1, this.ctrlx2));
            double ry1 = Math.min(Math.min(this.y1, this.y2), Math.min(this.ctrly1, this.ctrly2));
            return new com.itextpdf.awt.geom.Rectangle2D.Double(rx1, ry1, Math.max(Math.max(this.x1, this.x2), Math.max(this.ctrlx1, this.ctrlx2)) - rx1, Math.max(Math.max(this.y1, this.y2), Math.max(this.ctrly1, this.ctrly2)) - ry1);
        }
    }

    public static class Float extends CubicCurve2D {
        public float ctrlx1;
        public float ctrlx2;
        public float ctrly1;
        public float ctrly2;
        public float x1;
        public float x2;
        public float y1;
        public float y2;

        public Float(float x1, float y1, float ctrlx1, float ctrly1, float ctrlx2, float ctrly2, float x2, float y2) {
            setCurve(x1, y1, ctrlx1, ctrly1, ctrlx2, ctrly2, x2, y2);
        }

        public double getX1() {
            return (double) this.x1;
        }

        public double getY1() {
            return (double) this.y1;
        }

        public double getCtrlX1() {
            return (double) this.ctrlx1;
        }

        public double getCtrlY1() {
            return (double) this.ctrly1;
        }

        public double getCtrlX2() {
            return (double) this.ctrlx2;
        }

        public double getCtrlY2() {
            return (double) this.ctrly2;
        }

        public double getX2() {
            return (double) this.x2;
        }

        public double getY2() {
            return (double) this.y2;
        }

        public Point2D getP1() {
            return new com.itextpdf.awt.geom.Point2D.Float(this.x1, this.y1);
        }

        public Point2D getCtrlP1() {
            return new com.itextpdf.awt.geom.Point2D.Float(this.ctrlx1, this.ctrly1);
        }

        public Point2D getCtrlP2() {
            return new com.itextpdf.awt.geom.Point2D.Float(this.ctrlx2, this.ctrly2);
        }

        public Point2D getP2() {
            return new com.itextpdf.awt.geom.Point2D.Float(this.x2, this.y2);
        }

        public void setCurve(double x1, double y1, double ctrlx1, double ctrly1, double ctrlx2, double ctrly2, double x2, double y2) {
            this.x1 = (float) x1;
            this.y1 = (float) y1;
            this.ctrlx1 = (float) ctrlx1;
            this.ctrly1 = (float) ctrly1;
            this.ctrlx2 = (float) ctrlx2;
            this.ctrly2 = (float) ctrly2;
            this.x2 = (float) x2;
            this.y2 = (float) y2;
        }

        public void setCurve(float x1, float y1, float ctrlx1, float ctrly1, float ctrlx2, float ctrly2, float x2, float y2) {
            this.x1 = x1;
            this.y1 = y1;
            this.ctrlx1 = ctrlx1;
            this.ctrly1 = ctrly1;
            this.ctrlx2 = ctrlx2;
            this.ctrly2 = ctrly2;
            this.x2 = x2;
            this.y2 = y2;
        }

        public Rectangle2D getBounds2D() {
            float rx1 = Math.min(Math.min(this.x1, this.x2), Math.min(this.ctrlx1, this.ctrlx2));
            float ry1 = Math.min(Math.min(this.y1, this.y2), Math.min(this.ctrly1, this.ctrly2));
            return new com.itextpdf.awt.geom.Rectangle2D.Float(rx1, ry1, Math.max(Math.max(this.x1, this.x2), Math.max(this.ctrlx1, this.ctrlx2)) - rx1, Math.max(Math.max(this.y1, this.y2), Math.max(this.ctrly1, this.ctrly2)) - ry1);
        }
    }

    class Iterator implements PathIterator {
        CubicCurve2D f80c;
        int index;
        AffineTransform f81t;

        Iterator(CubicCurve2D c, AffineTransform t) {
            this.f80c = c;
            this.f81t = t;
        }

        public int getWindingRule() {
            return 1;
        }

        public boolean isDone() {
            return this.index > 1;
        }

        public void next() {
            this.index++;
        }

        public int currentSegment(double[] coords) {
            if (isDone()) {
                throw new NoSuchElementException(Messages.getString("awt.4B"));
            }
            int type;
            int count;
            if (this.index == 0) {
                type = 0;
                coords[0] = this.f80c.getX1();
                coords[1] = this.f80c.getY1();
                count = 1;
            } else {
                type = 3;
                coords[0] = this.f80c.getCtrlX1();
                coords[1] = this.f80c.getCtrlY1();
                coords[2] = this.f80c.getCtrlX2();
                coords[3] = this.f80c.getCtrlY2();
                coords[4] = this.f80c.getX2();
                coords[5] = this.f80c.getY2();
                count = 3;
            }
            if (this.f81t != null) {
                this.f81t.transform(coords, 0, coords, 0, count);
            }
            return type;
        }

        public int currentSegment(float[] coords) {
            if (isDone()) {
                throw new NoSuchElementException(Messages.getString("awt.4B"));
            }
            int type;
            int count;
            if (this.index == 0) {
                type = 0;
                coords[0] = (float) this.f80c.getX1();
                coords[1] = (float) this.f80c.getY1();
                count = 1;
            } else {
                type = 3;
                coords[0] = (float) this.f80c.getCtrlX1();
                coords[1] = (float) this.f80c.getCtrlY1();
                coords[2] = (float) this.f80c.getCtrlX2();
                coords[3] = (float) this.f80c.getCtrlY2();
                coords[4] = (float) this.f80c.getX2();
                coords[5] = (float) this.f80c.getY2();
                count = 3;
            }
            if (this.f81t != null) {
                this.f81t.transform(coords, 0, coords, 0, count);
            }
            return type;
        }
    }

    public abstract Point2D getCtrlP1();

    public abstract Point2D getCtrlP2();

    public abstract double getCtrlX1();

    public abstract double getCtrlX2();

    public abstract double getCtrlY1();

    public abstract double getCtrlY2();

    public abstract Point2D getP1();

    public abstract Point2D getP2();

    public abstract double getX1();

    public abstract double getX2();

    public abstract double getY1();

    public abstract double getY2();

    public abstract void setCurve(double d, double d2, double d3, double d4, double d5, double d6, double d7, double d8);

    protected CubicCurve2D() {
    }

    public void setCurve(Point2D p1, Point2D cp1, Point2D cp2, Point2D p2) {
        setCurve(p1.getX(), p1.getY(), cp1.getX(), cp1.getY(), cp2.getX(), cp2.getY(), p2.getX(), p2.getY());
    }

    public void setCurve(double[] coords, int offset) {
        setCurve(coords[offset + 0], coords[offset + 1], coords[offset + 2], coords[offset + 3], coords[offset + 4], coords[offset + 5], coords[offset + 6], coords[offset + 7]);
    }

    public void setCurve(Point2D[] points, int offset) {
        setCurve(points[offset + 0].getX(), points[offset + 0].getY(), points[offset + 1].getX(), points[offset + 1].getY(), points[offset + 2].getX(), points[offset + 2].getY(), points[offset + 3].getX(), points[offset + 3].getY());
    }

    public void setCurve(CubicCurve2D curve) {
        setCurve(curve.getX1(), curve.getY1(), curve.getCtrlX1(), curve.getCtrlY1(), curve.getCtrlX2(), curve.getCtrlY2(), curve.getX2(), curve.getY2());
    }

    public double getFlatnessSq() {
        return getFlatnessSq(getX1(), getY1(), getCtrlX1(), getCtrlY1(), getCtrlX2(), getCtrlY2(), getX2(), getY2());
    }

    public static double getFlatnessSq(double x1, double y1, double ctrlx1, double ctrly1, double ctrlx2, double ctrly2, double x2, double y2) {
        return Math.max(Line2D.ptSegDistSq(x1, y1, x2, y2, ctrlx1, ctrly1), Line2D.ptSegDistSq(x1, y1, x2, y2, ctrlx2, ctrly2));
    }

    public static double getFlatnessSq(double[] coords, int offset) {
        return getFlatnessSq(coords[offset + 0], coords[offset + 1], coords[offset + 2], coords[offset + 3], coords[offset + 4], coords[offset + 5], coords[offset + 6], coords[offset + 7]);
    }

    public double getFlatness() {
        return getFlatness(getX1(), getY1(), getCtrlX1(), getCtrlY1(), getCtrlX2(), getCtrlY2(), getX2(), getY2());
    }

    public static double getFlatness(double x1, double y1, double ctrlx1, double ctrly1, double ctrlx2, double ctrly2, double x2, double y2) {
        return Math.sqrt(getFlatnessSq(x1, y1, ctrlx1, ctrly1, ctrlx2, ctrly2, x2, y2));
    }

    public static double getFlatness(double[] coords, int offset) {
        return getFlatness(coords[offset + 0], coords[offset + 1], coords[offset + 2], coords[offset + 3], coords[offset + 4], coords[offset + 5], coords[offset + 6], coords[offset + 7]);
    }

    public void subdivide(CubicCurve2D left, CubicCurve2D right) {
        subdivide(this, left, right);
    }

    public static void subdivide(CubicCurve2D src, CubicCurve2D left, CubicCurve2D right) {
        double x1 = src.getX1();
        double y1 = src.getY1();
        double cx1 = src.getCtrlX1();
        double cy1 = src.getCtrlY1();
        double cx2 = src.getCtrlX2();
        double cy2 = src.getCtrlY2();
        double x2 = src.getX2();
        double y2 = src.getY2();
        double cx = (cx1 + cx2) / 2.0d;
        double cy = (cy1 + cy2) / 2.0d;
        cx1 = (x1 + cx1) / 2.0d;
        cy1 = (y1 + cy1) / 2.0d;
        cx2 = (x2 + cx2) / 2.0d;
        cy2 = (y2 + cy2) / 2.0d;
        double ax = (cx1 + cx) / 2.0d;
        double ay = (cy1 + cy) / 2.0d;
        double bx = (cx2 + cx) / 2.0d;
        double by = (cy2 + cy) / 2.0d;
        cx = (ax + bx) / 2.0d;
        cy = (ay + by) / 2.0d;
        if (left != null) {
            left.setCurve(x1, y1, cx1, cy1, ax, ay, cx, cy);
        }
        if (right != null) {
            right.setCurve(cx, cy, bx, by, cx2, cy2, x2, y2);
        }
    }

    public static void subdivide(double[] src, int srcOff, double[] left, int leftOff, double[] right, int rightOff) {
        double x1 = src[srcOff + 0];
        double y1 = src[srcOff + 1];
        double cx1 = src[srcOff + 2];
        double cy1 = src[srcOff + 3];
        double cx2 = src[srcOff + 4];
        double cy2 = src[srcOff + 5];
        double x2 = src[srcOff + 6];
        double y2 = src[srcOff + 7];
        double cx = (cx1 + cx2) / 2.0d;
        double cy = (cy1 + cy2) / 2.0d;
        cx1 = (x1 + cx1) / 2.0d;
        cy1 = (y1 + cy1) / 2.0d;
        cx2 = (x2 + cx2) / 2.0d;
        cy2 = (y2 + cy2) / 2.0d;
        double ax = (cx1 + cx) / 2.0d;
        double ay = (cy1 + cy) / 2.0d;
        double bx = (cx2 + cx) / 2.0d;
        double by = (cy2 + cy) / 2.0d;
        cx = (ax + bx) / 2.0d;
        cy = (ay + by) / 2.0d;
        if (left != null) {
            left[leftOff + 0] = x1;
            left[leftOff + 1] = y1;
            left[leftOff + 2] = cx1;
            left[leftOff + 3] = cy1;
            left[leftOff + 4] = ax;
            left[leftOff + 5] = ay;
            left[leftOff + 6] = cx;
            left[leftOff + 7] = cy;
        }
        if (right != null) {
            right[rightOff + 0] = cx;
            right[rightOff + 1] = cy;
            right[rightOff + 2] = bx;
            right[rightOff + 3] = by;
            right[rightOff + 4] = cx2;
            right[rightOff + 5] = cy2;
            right[rightOff + 6] = x2;
            right[rightOff + 7] = y2;
        }
    }

    public static int solveCubic(double[] eqn) {
        return solveCubic(eqn, eqn);
    }

    public static int solveCubic(double[] eqn, double[] res) {
        return Crossing.solveCubic(eqn, res);
    }

    public boolean contains(double px, double py) {
        return Crossing.isInsideEvenOdd(Crossing.crossShape(this, px, py));
    }

    public boolean contains(double rx, double ry, double rw, double rh) {
        int cross = Crossing.intersectShape(this, rx, ry, rw, rh);
        return cross != 255 && Crossing.isInsideEvenOdd(cross);
    }

    public boolean intersects(double rx, double ry, double rw, double rh) {
        int cross = Crossing.intersectShape(this, rx, ry, rw, rh);
        return cross == 255 || Crossing.isInsideEvenOdd(cross);
    }

    public boolean contains(Point2D p) {
        return contains(p.getX(), p.getY());
    }

    public boolean intersects(Rectangle2D r) {
        return intersects(r.getX(), r.getY(), r.getWidth(), r.getHeight());
    }

    public boolean contains(Rectangle2D r) {
        return contains(r.getX(), r.getY(), r.getWidth(), r.getHeight());
    }

    public Rectangle getBounds() {
        return getBounds2D().getBounds();
    }

    public PathIterator getPathIterator(AffineTransform t) {
        return new Iterator(this, t);
    }

    public PathIterator getPathIterator(AffineTransform at, double flatness) {
        return new FlatteningPathIterator(getPathIterator(at), flatness);
    }

    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            throw new InternalError();
        }
    }
}
