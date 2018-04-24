package org.opencv.core;

public class Point {
    public double f203x;
    public double f204y;

    public Point(double x, double y) {
        this.f203x = x;
        this.f204y = y;
    }

    public Point() {
        this(0.0d, 0.0d);
    }

    public Point(double[] vals) {
        this();
        set(vals);
    }

    public void set(double[] vals) {
        double d = 0.0d;
        if (vals != null) {
            this.f203x = vals.length > 0 ? vals[0] : 0.0d;
            if (vals.length > 1) {
                d = vals[1];
            }
            this.f204y = d;
            return;
        }
        this.f203x = 0.0d;
        this.f204y = 0.0d;
    }

    public Point clone() {
        return new Point(this.f203x, this.f204y);
    }

    public double dot(Point p) {
        return (this.f203x * p.f203x) + (this.f204y * p.f204y);
    }

    public int hashCode() {
        long temp = Double.doubleToLongBits(this.f203x);
        int result = ((int) ((temp >>> 32) ^ temp)) + 31;
        temp = Double.doubleToLongBits(this.f204y);
        return (result * 31) + ((int) ((temp >>> 32) ^ temp));
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Point)) {
            return false;
        }
        Point it = (Point) obj;
        if (this.f203x == it.f203x && this.f204y == it.f204y) {
            return true;
        }
        return false;
    }

    public boolean inside(Rect r) {
        return r.contains(this);
    }

    public String toString() {
        return "{" + this.f203x + ", " + this.f204y + "}";
    }
}
