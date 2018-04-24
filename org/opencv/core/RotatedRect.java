package org.opencv.core;

public class RotatedRect {
    public double angle;
    public Point center;
    public Size size;

    public RotatedRect() {
        this.center = new Point();
        this.size = new Size();
        this.angle = 0.0d;
    }

    public RotatedRect(Point c, Size s, double a) {
        this.center = c.clone();
        this.size = s.clone();
        this.angle = a;
    }

    public RotatedRect(double[] vals) {
        this();
        set(vals);
    }

    public void set(double[] vals) {
        double d = 0.0d;
        if (vals != null) {
            double d2;
            this.center.f203x = vals.length > 0 ? vals[0] : 0.0d;
            Point point = this.center;
            if (vals.length > 1) {
                d2 = vals[1];
            } else {
                d2 = 0.0d;
            }
            point.f204y = d2;
            Size size = this.size;
            if (vals.length > 2) {
                d2 = vals[2];
            } else {
                d2 = 0.0d;
            }
            size.width = d2;
            size = this.size;
            if (vals.length > 3) {
                d2 = vals[3];
            } else {
                d2 = 0.0d;
            }
            size.height = d2;
            if (vals.length > 4) {
                d = vals[4];
            }
            this.angle = d;
            return;
        }
        this.center.f203x = 0.0d;
        this.center.f203x = 0.0d;
        this.size.width = 0.0d;
        this.size.height = 0.0d;
        this.angle = 0.0d;
    }

    public void points(Point[] pt) {
        double _angle = (this.angle * 3.141592653589793d) / 180.0d;
        double b = Math.cos(_angle) * 0.5d;
        double a = Math.sin(_angle) * 0.5d;
        pt[0] = new Point((this.center.f203x - (this.size.height * a)) - (this.size.width * b), (this.center.f204y + (this.size.height * b)) - (this.size.width * a));
        pt[1] = new Point((this.center.f203x + (this.size.height * a)) - (this.size.width * b), (this.center.f204y - (this.size.height * b)) - (this.size.width * a));
        pt[2] = new Point((2.0d * this.center.f203x) - pt[0].f203x, (2.0d * this.center.f204y) - pt[0].f204y);
        pt[3] = new Point((2.0d * this.center.f203x) - pt[1].f203x, (2.0d * this.center.f204y) - pt[1].f204y);
    }

    public Rect boundingRect() {
        Point[] pt = new Point[4];
        points(pt);
        Rect r = new Rect((int) Math.floor(Math.min(Math.min(Math.min(pt[0].f203x, pt[1].f203x), pt[2].f203x), pt[3].f203x)), (int) Math.floor(Math.min(Math.min(Math.min(pt[0].f204y, pt[1].f204y), pt[2].f204y), pt[3].f204y)), (int) Math.ceil(Math.max(Math.max(Math.max(pt[0].f203x, pt[1].f203x), pt[2].f203x), pt[3].f203x)), (int) Math.ceil(Math.max(Math.max(Math.max(pt[0].f204y, pt[1].f204y), pt[2].f204y), pt[3].f204y)));
        r.width -= r.f205x - 1;
        r.height -= r.f206y - 1;
        return r;
    }

    public RotatedRect clone() {
        return new RotatedRect(this.center, this.size, this.angle);
    }

    public int hashCode() {
        long temp = Double.doubleToLongBits(this.center.f203x);
        int result = ((int) ((temp >>> 32) ^ temp)) + 31;
        temp = Double.doubleToLongBits(this.center.f204y);
        result = (result * 31) + ((int) ((temp >>> 32) ^ temp));
        temp = Double.doubleToLongBits(this.size.width);
        result = (result * 31) + ((int) ((temp >>> 32) ^ temp));
        temp = Double.doubleToLongBits(this.size.height);
        result = (result * 31) + ((int) ((temp >>> 32) ^ temp));
        temp = Double.doubleToLongBits(this.angle);
        return (result * 31) + ((int) ((temp >>> 32) ^ temp));
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof RotatedRect)) {
            return false;
        }
        RotatedRect it = (RotatedRect) obj;
        if (this.center.equals(it.center) && this.size.equals(it.size) && this.angle == it.angle) {
            return true;
        }
        return false;
    }

    public String toString() {
        return "{ " + this.center + " " + this.size + " * " + this.angle + " }";
    }
}
