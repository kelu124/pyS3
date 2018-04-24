package com.itextpdf.awt.geom;

import com.itextpdf.awt.geom.Rectangle2D.Double;
import java.io.Serializable;

public class Rectangle extends Rectangle2D implements Shape, Serializable {
    private static final long serialVersionUID = -4345857070255674764L;
    public double height;
    public double width;
    public double f101x;
    public double f102y;

    public Rectangle() {
        setBounds(0, 0, 0, 0);
    }

    public Rectangle(Point p) {
        setBounds(p.f90x, p.f91y, 0.0d, 0.0d);
    }

    public Rectangle(Point p, Dimension d) {
        setBounds(p.f90x, p.f91y, d.width, d.height);
    }

    public Rectangle(double x, double y, double width, double height) {
        setBounds(x, y, width, height);
    }

    public Rectangle(int width, int height) {
        setBounds(0, 0, width, height);
    }

    public Rectangle(Rectangle r) {
        setBounds(r.f101x, r.f102y, r.width, r.height);
    }

    public Rectangle(com.itextpdf.text.Rectangle r) {
        r.normalize();
        setBounds((double) r.getLeft(), (double) r.getBottom(), (double) r.getWidth(), (double) r.getHeight());
    }

    public Rectangle(Dimension d) {
        setBounds(0.0d, 0.0d, d.width, d.height);
    }

    public double getX() {
        return this.f101x;
    }

    public double getY() {
        return this.f102y;
    }

    public double getHeight() {
        return this.height;
    }

    public double getWidth() {
        return this.width;
    }

    public boolean isEmpty() {
        return this.width <= 0.0d || this.height <= 0.0d;
    }

    public Dimension getSize() {
        return new Dimension(this.width, this.height);
    }

    public void setSize(int mx, int my) {
        setSize((double) mx, (double) my);
    }

    public void setSize(double width, double height) {
        this.width = width;
        this.height = height;
    }

    public void setSize(Dimension d) {
        setSize(d.width, d.height);
    }

    public Point getLocation() {
        return new Point(this.f101x, this.f102y);
    }

    public void setLocation(int mx, int my) {
        setLocation((double) mx, (double) my);
    }

    public void setLocation(double x, double y) {
        this.f101x = x;
        this.f102y = y;
    }

    public void setLocation(Point p) {
        setLocation(p.f90x, p.f91y);
    }

    public void setRect(double x, double y, double width, double height) {
        int x1 = (int) Math.floor(x);
        int y1 = (int) Math.floor(y);
        setBounds(x1, y1, ((int) Math.ceil(x + width)) - x1, ((int) Math.ceil(y + height)) - y1);
    }

    public Rectangle getBounds() {
        return new Rectangle(this.f101x, this.f102y, this.width, this.height);
    }

    public Rectangle2D getBounds2D() {
        return getBounds();
    }

    public void setBounds(int x, int y, int width, int height) {
        setBounds((double) x, (double) y, (double) width, (double) height);
    }

    public void setBounds(double x, double y, double width, double height) {
        this.f101x = x;
        this.f102y = y;
        this.height = height;
        this.width = width;
    }

    public void setBounds(Rectangle r) {
        setBounds(r.f101x, r.f102y, r.width, r.height);
    }

    public void grow(int mx, int my) {
        translate((double) mx, (double) my);
    }

    public void grow(double dx, double dy) {
        this.f101x -= dx;
        this.f102y -= dy;
        this.width += dx + dx;
        this.height += dy + dy;
    }

    public void translate(int mx, int my) {
        translate((double) mx, (double) my);
    }

    public void translate(double mx, double my) {
        this.f101x += mx;
        this.f102y += my;
    }

    public void add(int px, int py) {
        add((double) px, (double) py);
    }

    public void add(double px, double py) {
        double x1 = Math.min(this.f101x, px);
        double x2 = Math.max(this.f101x + this.width, px);
        double y1 = Math.min(this.f102y, py);
        setBounds(x1, y1, x2 - x1, Math.max(this.f102y + this.height, py) - y1);
    }

    public void add(Point p) {
        add(p.f90x, p.f91y);
    }

    public void add(Rectangle r) {
        double x1 = Math.min(this.f101x, r.f101x);
        double x2 = Math.max(this.f101x + this.width, r.f101x + r.width);
        double y1 = Math.min(this.f102y, r.f102y);
        setBounds(x1, y1, x2 - x1, Math.max(this.f102y + this.height, r.f102y + r.height) - y1);
    }

    public boolean contains(int px, int py) {
        return contains((double) px, (double) py);
    }

    public boolean contains(double px, double py) {
        if (isEmpty() || px < this.f101x || py < this.f102y) {
            return false;
        }
        py -= this.f102y;
        if (px - this.f101x >= this.width || py >= this.height) {
            return false;
        }
        return true;
    }

    public boolean contains(Point p) {
        return contains(p.f90x, p.f91y);
    }

    public boolean contains(int rx, int ry, int rw, int rh) {
        return contains(rx, ry) && contains((rx + rw) - 1, (ry + rh) - 1);
    }

    public boolean contains(double rx, double ry, double rw, double rh) {
        return contains(rx, ry) && contains((rx + rw) - 0.01d, (ry + rh) - 0.01d);
    }

    public boolean contains(Rectangle r) {
        return contains(r.f101x, r.f102y, r.width, r.height);
    }

    public Rectangle2D createIntersection(Rectangle2D r) {
        if (r instanceof Rectangle) {
            return intersection((Rectangle) r);
        }
        Rectangle2D dst = new Double();
        Rectangle2D.intersect(this, r, dst);
        return dst;
    }

    public Rectangle intersection(Rectangle r) {
        double x1 = Math.max(this.f101x, r.f101x);
        double y1 = Math.max(this.f102y, r.f102y);
        return new Rectangle(x1, y1, Math.min(this.f101x + this.width, r.f101x + r.width) - x1, Math.min(this.f102y + this.height, r.f102y + r.height) - y1);
    }

    public boolean intersects(Rectangle r) {
        return !intersection(r).isEmpty();
    }

    public int outcode(double px, double py) {
        int code = 0;
        if (this.width <= 0.0d) {
            code = 0 | 5;
        } else if (px < this.f101x) {
            code = 0 | 1;
        } else if (px > this.f101x + this.width) {
            code = 0 | 4;
        }
        if (this.height <= 0.0d) {
            return code | 10;
        }
        if (py < this.f102y) {
            return code | 2;
        }
        if (py > this.f102y + this.height) {
            return code | 8;
        }
        return code;
    }

    public Rectangle2D createUnion(Rectangle2D r) {
        if (r instanceof Rectangle) {
            return union((Rectangle) r);
        }
        Rectangle2D dst = new Double();
        Rectangle2D.union(this, r, dst);
        return dst;
    }

    public Rectangle union(Rectangle r) {
        Rectangle dst = new Rectangle(this);
        dst.add(r);
        return dst;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Rectangle)) {
            return false;
        }
        Rectangle r = (Rectangle) obj;
        if (r.f101x == this.f101x && r.f102y == this.f102y && r.width == this.width && r.height == this.height) {
            return true;
        }
        return false;
    }

    public String toString() {
        return getClass().getName() + "[x=" + this.f101x + ",y=" + this.f102y + ",width=" + this.width + ",height=" + this.height + "]";
    }
}
