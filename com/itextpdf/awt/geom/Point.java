package com.itextpdf.awt.geom;

import java.io.Serializable;

public class Point extends Point2D implements Serializable {
    private static final long serialVersionUID = -5276940640259749850L;
    public double f90x;
    public double f91y;

    public Point() {
        setLocation(0, 0);
    }

    public Point(int x, int y) {
        setLocation(x, y);
    }

    public Point(double x, double y) {
        setLocation(x, y);
    }

    public Point(Point p) {
        setLocation(p.f90x, p.f91y);
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Point)) {
            return false;
        }
        Point p = (Point) obj;
        if (this.f90x == p.f90x && this.f91y == p.f91y) {
            return true;
        }
        return false;
    }

    public String toString() {
        return getClass().getName() + "[x=" + this.f90x + ",y=" + this.f91y + "]";
    }

    public double getX() {
        return this.f90x;
    }

    public double getY() {
        return this.f91y;
    }

    public Point getLocation() {
        return new Point(this.f90x, this.f91y);
    }

    public void setLocation(Point p) {
        setLocation(p.f90x, p.f91y);
    }

    public void setLocation(int x, int y) {
        setLocation((double) x, (double) y);
    }

    public void setLocation(double x, double y) {
        this.f90x = x;
        this.f91y = y;
    }

    public void move(int x, int y) {
        move((double) x, (double) y);
    }

    public void move(double x, double y) {
        setLocation(x, y);
    }

    public void translate(int dx, int dy) {
        translate((double) dx, (double) dy);
    }

    public void translate(double dx, double dy) {
        this.f90x += dx;
        this.f91y += dy;
    }
}
