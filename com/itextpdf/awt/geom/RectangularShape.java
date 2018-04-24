package com.itextpdf.awt.geom;

import com.itextpdf.awt.geom.Rectangle2D.Double;

public abstract class RectangularShape implements Shape, Cloneable {
    public abstract double getHeight();

    public abstract double getWidth();

    public abstract double getX();

    public abstract double getY();

    public abstract boolean isEmpty();

    public abstract void setFrame(double d, double d2, double d3, double d4);

    protected RectangularShape() {
    }

    public double getMinX() {
        return getX();
    }

    public double getMinY() {
        return getY();
    }

    public double getMaxX() {
        return getX() + getWidth();
    }

    public double getMaxY() {
        return getY() + getHeight();
    }

    public double getCenterX() {
        return getX() + (getWidth() / 2.0d);
    }

    public double getCenterY() {
        return getY() + (getHeight() / 2.0d);
    }

    public Rectangle2D getFrame() {
        return new Double(getX(), getY(), getWidth(), getHeight());
    }

    public void setFrame(Point2D loc, Dimension2D size) {
        setFrame(loc.getX(), loc.getY(), size.getWidth(), size.getHeight());
    }

    public void setFrame(Rectangle2D r) {
        setFrame(r.getX(), r.getY(), r.getWidth(), r.getHeight());
    }

    public void setFrameFromDiagonal(double x1, double y1, double x2, double y2) {
        double rx;
        double rw;
        double ry;
        double rh;
        if (x1 < x2) {
            rx = x1;
            rw = x2 - x1;
        } else {
            rx = x2;
            rw = x1 - x2;
        }
        if (y1 < y2) {
            ry = y1;
            rh = y2 - y1;
        } else {
            ry = y2;
            rh = y1 - y2;
        }
        setFrame(rx, ry, rw, rh);
    }

    public void setFrameFromDiagonal(Point2D p1, Point2D p2) {
        setFrameFromDiagonal(p1.getX(), p1.getY(), p2.getX(), p2.getY());
    }

    public void setFrameFromCenter(double centerX, double centerY, double cornerX, double cornerY) {
        double width = Math.abs(cornerX - centerX);
        double height = Math.abs(cornerY - centerY);
        setFrame(centerX - width, centerY - height, width * 2.0d, height * 2.0d);
    }

    public void setFrameFromCenter(Point2D center, Point2D corner) {
        setFrameFromCenter(center.getX(), center.getY(), corner.getX(), corner.getY());
    }

    public boolean contains(Point2D point) {
        return contains(point.getX(), point.getY());
    }

    public boolean intersects(Rectangle2D rect) {
        return intersects(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
    }

    public boolean contains(Rectangle2D rect) {
        return contains(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
    }

    public Rectangle getBounds() {
        int x1 = (int) Math.floor(getMinX());
        int y1 = (int) Math.floor(getMinY());
        return new Rectangle((double) x1, (double) y1, (double) (((int) Math.ceil(getMaxX())) - x1), (double) (((int) Math.ceil(getMaxY())) - y1));
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
