package org.opencv.core;

public class Point3 {
    public double f200x;
    public double f201y;
    public double f202z;

    public Point3(double x, double y, double z) {
        this.f200x = x;
        this.f201y = y;
        this.f202z = z;
    }

    public Point3() {
        this(0.0d, 0.0d, 0.0d);
    }

    public Point3(Point p) {
        this.f200x = p.f203x;
        this.f201y = p.f204y;
        this.f202z = 0.0d;
    }

    public Point3(double[] vals) {
        this();
        set(vals);
    }

    public void set(double[] vals) {
        double d = 0.0d;
        if (vals != null) {
            double d2;
            this.f200x = vals.length > 0 ? vals[0] : 0.0d;
            if (vals.length > 1) {
                d2 = vals[1];
            } else {
                d2 = 0.0d;
            }
            this.f201y = d2;
            if (vals.length > 2) {
                d = vals[2];
            }
            this.f202z = d;
            return;
        }
        this.f200x = 0.0d;
        this.f201y = 0.0d;
        this.f202z = 0.0d;
    }

    public Point3 clone() {
        return new Point3(this.f200x, this.f201y, this.f202z);
    }

    public double dot(Point3 p) {
        return ((this.f200x * p.f200x) + (this.f201y * p.f201y)) + (this.f202z * p.f202z);
    }

    public Point3 cross(Point3 p) {
        return new Point3((this.f201y * p.f202z) - (this.f202z * p.f201y), (this.f202z * p.f200x) - (this.f200x * p.f202z), (this.f200x * p.f201y) - (this.f201y * p.f200x));
    }

    public int hashCode() {
        long temp = Double.doubleToLongBits(this.f200x);
        int result = ((int) ((temp >>> 32) ^ temp)) + 31;
        temp = Double.doubleToLongBits(this.f201y);
        result = (result * 31) + ((int) ((temp >>> 32) ^ temp));
        temp = Double.doubleToLongBits(this.f202z);
        return (result * 31) + ((int) ((temp >>> 32) ^ temp));
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Point3)) {
            return false;
        }
        Point3 it = (Point3) obj;
        if (this.f200x == it.f200x && this.f201y == it.f201y && this.f202z == it.f202z) {
            return true;
        }
        return false;
    }

    public String toString() {
        return "{" + this.f200x + ", " + this.f201y + ", " + this.f202z + "}";
    }
}
