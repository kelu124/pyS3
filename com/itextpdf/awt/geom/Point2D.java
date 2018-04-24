package com.itextpdf.awt.geom;

import com.itextpdf.awt.geom.misc.HashCode;

public abstract class Point2D implements Cloneable {

    public static class Double extends Point2D {
        public double f86x;
        public double f87y;

        public Double(double x, double y) {
            this.f86x = x;
            this.f87y = y;
        }

        public double getX() {
            return this.f86x;
        }

        public double getY() {
            return this.f87y;
        }

        public void setLocation(double x, double y) {
            this.f86x = x;
            this.f87y = y;
        }

        public String toString() {
            return getClass().getName() + "[x=" + this.f86x + ",y=" + this.f87y + "]";
        }
    }

    public static class Float extends Point2D {
        public float f88x;
        public float f89y;

        public Float(float x, float y) {
            this.f88x = x;
            this.f89y = y;
        }

        public double getX() {
            return (double) this.f88x;
        }

        public double getY() {
            return (double) this.f89y;
        }

        public void setLocation(float x, float y) {
            this.f88x = x;
            this.f89y = y;
        }

        public void setLocation(double x, double y) {
            this.f88x = (float) x;
            this.f89y = (float) y;
        }

        public String toString() {
            return getClass().getName() + "[x=" + this.f88x + ",y=" + this.f89y + "]";
        }
    }

    public abstract double getX();

    public abstract double getY();

    public abstract void setLocation(double d, double d2);

    protected Point2D() {
    }

    public void setLocation(Point2D p) {
        setLocation(p.getX(), p.getY());
    }

    public static double distanceSq(double x1, double y1, double x2, double y2) {
        x2 -= x1;
        y2 -= y1;
        return (x2 * x2) + (y2 * y2);
    }

    public double distanceSq(double px, double py) {
        return distanceSq(getX(), getY(), px, py);
    }

    public double distanceSq(Point2D p) {
        return distanceSq(getX(), getY(), p.getX(), p.getY());
    }

    public static double distance(double x1, double y1, double x2, double y2) {
        return Math.sqrt(distanceSq(x1, y1, x2, y2));
    }

    public double distance(double px, double py) {
        return Math.sqrt(distanceSq(px, py));
    }

    public double distance(Point2D p) {
        return Math.sqrt(distanceSq(p));
    }

    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            throw new InternalError();
        }
    }

    public int hashCode() {
        HashCode hash = new HashCode();
        hash.append(getX());
        hash.append(getY());
        return hash.hashCode();
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Point2D)) {
            return false;
        }
        Point2D p = (Point2D) obj;
        if (getX() == p.getX() && getY() == p.getY()) {
            return true;
        }
        return false;
    }
}
