package com.itextpdf.awt.geom;

import com.itextpdf.awt.geom.gl.Crossing;
import com.itextpdf.awt.geom.misc.Messages;
import java.util.NoSuchElementException;

public abstract class QuadCurve2D implements Shape, Cloneable {

    public static class Double extends QuadCurve2D {
        public double ctrlx;
        public double ctrly;
        public double x1;
        public double x2;
        public double y1;
        public double y2;

        public Double(double x1, double y1, double ctrlx, double ctrly, double x2, double y2) {
            setCurve(x1, y1, ctrlx, ctrly, x2, y2);
        }

        public double getX1() {
            return this.x1;
        }

        public double getY1() {
            return this.y1;
        }

        public double getCtrlX() {
            return this.ctrlx;
        }

        public double getCtrlY() {
            return this.ctrly;
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

        public Point2D getCtrlPt() {
            return new com.itextpdf.awt.geom.Point2D.Double(this.ctrlx, this.ctrly);
        }

        public Point2D getP2() {
            return new com.itextpdf.awt.geom.Point2D.Double(this.x2, this.y2);
        }

        public void setCurve(double x1, double y1, double ctrlx, double ctrly, double x2, double y2) {
            this.x1 = x1;
            this.y1 = y1;
            this.ctrlx = ctrlx;
            this.ctrly = ctrly;
            this.x2 = x2;
            this.y2 = y2;
        }

        public Rectangle2D getBounds2D() {
            double rx0 = Math.min(Math.min(this.x1, this.x2), this.ctrlx);
            double ry0 = Math.min(Math.min(this.y1, this.y2), this.ctrly);
            return new com.itextpdf.awt.geom.Rectangle2D.Double(rx0, ry0, Math.max(Math.max(this.x1, this.x2), this.ctrlx) - rx0, Math.max(Math.max(this.y1, this.y2), this.ctrly) - ry0);
        }
    }

    public static class Float extends QuadCurve2D {
        public float ctrlx;
        public float ctrly;
        public float x1;
        public float x2;
        public float y1;
        public float y2;

        public Float(float x1, float y1, float ctrlx, float ctrly, float x2, float y2) {
            setCurve(x1, y1, ctrlx, ctrly, x2, y2);
        }

        public double getX1() {
            return (double) this.x1;
        }

        public double getY1() {
            return (double) this.y1;
        }

        public double getCtrlX() {
            return (double) this.ctrlx;
        }

        public double getCtrlY() {
            return (double) this.ctrly;
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

        public Point2D getCtrlPt() {
            return new com.itextpdf.awt.geom.Point2D.Float(this.ctrlx, this.ctrly);
        }

        public Point2D getP2() {
            return new com.itextpdf.awt.geom.Point2D.Float(this.x2, this.y2);
        }

        public void setCurve(double x1, double y1, double ctrlx, double ctrly, double x2, double y2) {
            this.x1 = (float) x1;
            this.y1 = (float) y1;
            this.ctrlx = (float) ctrlx;
            this.ctrly = (float) ctrly;
            this.x2 = (float) x2;
            this.y2 = (float) y2;
        }

        public void setCurve(float x1, float y1, float ctrlx, float ctrly, float x2, float y2) {
            this.x1 = x1;
            this.y1 = y1;
            this.ctrlx = ctrlx;
            this.ctrly = ctrly;
            this.x2 = x2;
            this.y2 = y2;
        }

        public Rectangle2D getBounds2D() {
            float rx0 = Math.min(Math.min(this.x1, this.x2), this.ctrlx);
            float ry0 = Math.min(Math.min(this.y1, this.y2), this.ctrly);
            return new com.itextpdf.awt.geom.Rectangle2D.Float(rx0, ry0, Math.max(Math.max(this.x1, this.x2), this.ctrlx) - rx0, Math.max(Math.max(this.y1, this.y2), this.ctrly) - ry0);
        }
    }

    class Iterator implements PathIterator {
        QuadCurve2D f92c;
        int index;
        AffineTransform f93t;

        Iterator(QuadCurve2D q, AffineTransform t) {
            this.f92c = q;
            this.f93t = t;
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
                coords[0] = this.f92c.getX1();
                coords[1] = this.f92c.getY1();
                count = 1;
            } else {
                type = 2;
                coords[0] = this.f92c.getCtrlX();
                coords[1] = this.f92c.getCtrlY();
                coords[2] = this.f92c.getX2();
                coords[3] = this.f92c.getY2();
                count = 2;
            }
            if (this.f93t != null) {
                this.f93t.transform(coords, 0, coords, 0, count);
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
                coords[0] = (float) this.f92c.getX1();
                coords[1] = (float) this.f92c.getY1();
                count = 1;
            } else {
                type = 2;
                coords[0] = (float) this.f92c.getCtrlX();
                coords[1] = (float) this.f92c.getCtrlY();
                coords[2] = (float) this.f92c.getX2();
                coords[3] = (float) this.f92c.getY2();
                count = 2;
            }
            if (this.f93t != null) {
                this.f93t.transform(coords, 0, coords, 0, count);
            }
            return type;
        }
    }

    public abstract Point2D getCtrlPt();

    public abstract double getCtrlX();

    public abstract double getCtrlY();

    public abstract Point2D getP1();

    public abstract Point2D getP2();

    public abstract double getX1();

    public abstract double getX2();

    public abstract double getY1();

    public abstract double getY2();

    public abstract void setCurve(double d, double d2, double d3, double d4, double d5, double d6);

    protected QuadCurve2D() {
    }

    public void setCurve(Point2D p1, Point2D cp, Point2D p2) {
        setCurve(p1.getX(), p1.getY(), cp.getX(), cp.getY(), p2.getX(), p2.getY());
    }

    public void setCurve(double[] coords, int offset) {
        setCurve(coords[offset + 0], coords[offset + 1], coords[offset + 2], coords[offset + 3], coords[offset + 4], coords[offset + 5]);
    }

    public void setCurve(Point2D[] points, int offset) {
        setCurve(points[offset + 0].getX(), points[offset + 0].getY(), points[offset + 1].getX(), points[offset + 1].getY(), points[offset + 2].getX(), points[offset + 2].getY());
    }

    public void setCurve(QuadCurve2D curve) {
        setCurve(curve.getX1(), curve.getY1(), curve.getCtrlX(), curve.getCtrlY(), curve.getX2(), curve.getY2());
    }

    public double getFlatnessSq() {
        return Line2D.ptSegDistSq(getX1(), getY1(), getX2(), getY2(), getCtrlX(), getCtrlY());
    }

    public static double getFlatnessSq(double x1, double y1, double ctrlx, double ctrly, double x2, double y2) {
        return Line2D.ptSegDistSq(x1, y1, x2, y2, ctrlx, ctrly);
    }

    public static double getFlatnessSq(double[] coords, int offset) {
        return Line2D.ptSegDistSq(coords[offset + 0], coords[offset + 1], coords[offset + 4], coords[offset + 5], coords[offset + 2], coords[offset + 3]);
    }

    public double getFlatness() {
        return Line2D.ptSegDist(getX1(), getY1(), getX2(), getY2(), getCtrlX(), getCtrlY());
    }

    public static double getFlatness(double x1, double y1, double ctrlx, double ctrly, double x2, double y2) {
        return Line2D.ptSegDist(x1, y1, x2, y2, ctrlx, ctrly);
    }

    public static double getFlatness(double[] coords, int offset) {
        return Line2D.ptSegDist(coords[offset + 0], coords[offset + 1], coords[offset + 4], coords[offset + 5], coords[offset + 2], coords[offset + 3]);
    }

    public void subdivide(QuadCurve2D left, QuadCurve2D right) {
        subdivide(this, left, right);
    }

    public static void subdivide(QuadCurve2D src, QuadCurve2D left, QuadCurve2D right) {
        double x1 = src.getX1();
        double y1 = src.getY1();
        double cx = src.getCtrlX();
        double cy = src.getCtrlY();
        double x2 = src.getX2();
        double y2 = src.getY2();
        double cx1 = (x1 + cx) / 2.0d;
        double cy1 = (y1 + cy) / 2.0d;
        double cx2 = (x2 + cx) / 2.0d;
        double cy2 = (y2 + cy) / 2.0d;
        cx = (cx1 + cx2) / 2.0d;
        cy = (cy1 + cy2) / 2.0d;
        if (left != null) {
            left.setCurve(x1, y1, cx1, cy1, cx, cy);
        }
        if (right != null) {
            right.setCurve(cx, cy, cx2, cy2, x2, y2);
        }
    }

    public static void subdivide(double[] src, int srcoff, double[] left, int leftOff, double[] right, int rightOff) {
        double x1 = src[srcoff + 0];
        double y1 = src[srcoff + 1];
        double cx = src[srcoff + 2];
        double cy = src[srcoff + 3];
        double x2 = src[srcoff + 4];
        double y2 = src[srcoff + 5];
        double cx1 = (x1 + cx) / 2.0d;
        double cy1 = (y1 + cy) / 2.0d;
        double cx2 = (x2 + cx) / 2.0d;
        double cy2 = (y2 + cy) / 2.0d;
        cx = (cx1 + cx2) / 2.0d;
        cy = (cy1 + cy2) / 2.0d;
        if (left != null) {
            left[leftOff + 0] = x1;
            left[leftOff + 1] = y1;
            left[leftOff + 2] = cx1;
            left[leftOff + 3] = cy1;
            left[leftOff + 4] = cx;
            left[leftOff + 5] = cy;
        }
        if (right != null) {
            right[rightOff + 0] = cx;
            right[rightOff + 1] = cy;
            right[rightOff + 2] = cx2;
            right[rightOff + 3] = cy2;
            right[rightOff + 4] = x2;
            right[rightOff + 5] = y2;
        }
    }

    public static int solveQuadratic(double[] eqn) {
        return solveQuadratic(eqn, eqn);
    }

    public static int solveQuadratic(double[] eqn, double[] res) {
        return Crossing.solveQuad(eqn, res);
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

    public PathIterator getPathIterator(AffineTransform t, double flatness) {
        return new FlatteningPathIterator(getPathIterator(t), flatness);
    }

    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            throw new InternalError();
        }
    }
}
