package org.opencv.core;

public class Rect {
    public int height;
    public int width;
    public int f205x;
    public int f206y;

    public Rect(int x, int y, int width, int height) {
        this.f205x = x;
        this.f206y = y;
        this.width = width;
        this.height = height;
    }

    public Rect() {
        this(0, 0, 0, 0);
    }

    public Rect(Point p1, Point p2) {
        this.f205x = (int) (p1.f203x < p2.f203x ? p1.f203x : p2.f203x);
        this.f206y = (int) (p1.f204y < p2.f204y ? p1.f204y : p2.f204y);
        this.width = ((int) (p1.f203x > p2.f203x ? p1.f203x : p2.f203x)) - this.f205x;
        this.height = ((int) (p1.f204y > p2.f204y ? p1.f204y : p2.f204y)) - this.f206y;
    }

    public Rect(Point p, Size s) {
        this((int) p.f203x, (int) p.f204y, (int) s.width, (int) s.height);
    }

    public Rect(double[] vals) {
        set(vals);
    }

    public void set(double[] vals) {
        int i = 0;
        if (vals != null) {
            int i2;
            this.f205x = vals.length > 0 ? (int) vals[0] : 0;
            if (vals.length > 1) {
                i2 = (int) vals[1];
            } else {
                i2 = 0;
            }
            this.f206y = i2;
            if (vals.length > 2) {
                i2 = (int) vals[2];
            } else {
                i2 = 0;
            }
            this.width = i2;
            if (vals.length > 3) {
                i = (int) vals[3];
            }
            this.height = i;
            return;
        }
        this.f205x = 0;
        this.f206y = 0;
        this.width = 0;
        this.height = 0;
    }

    public Rect clone() {
        return new Rect(this.f205x, this.f206y, this.width, this.height);
    }

    public Point tl() {
        return new Point((double) this.f205x, (double) this.f206y);
    }

    public Point br() {
        return new Point((double) (this.f205x + this.width), (double) (this.f206y + this.height));
    }

    public Size size() {
        return new Size((double) this.width, (double) this.height);
    }

    public double area() {
        return (double) (this.width * this.height);
    }

    public boolean contains(Point p) {
        return ((double) this.f205x) <= p.f203x && p.f203x < ((double) (this.f205x + this.width)) && ((double) this.f206y) <= p.f204y && p.f204y < ((double) (this.f206y + this.height));
    }

    public int hashCode() {
        long temp = Double.doubleToLongBits((double) this.height);
        int result = ((int) ((temp >>> 32) ^ temp)) + 31;
        temp = Double.doubleToLongBits((double) this.width);
        result = (result * 31) + ((int) ((temp >>> 32) ^ temp));
        temp = Double.doubleToLongBits((double) this.f205x);
        result = (result * 31) + ((int) ((temp >>> 32) ^ temp));
        temp = Double.doubleToLongBits((double) this.f206y);
        return (result * 31) + ((int) ((temp >>> 32) ^ temp));
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Rect)) {
            return false;
        }
        Rect it = (Rect) obj;
        if (this.f205x == it.f205x && this.f206y == it.f206y && this.width == it.width && this.height == it.height) {
            return true;
        }
        return false;
    }

    public String toString() {
        return "{" + this.f205x + ", " + this.f206y + ", " + this.width + "x" + this.height + "}";
    }
}
