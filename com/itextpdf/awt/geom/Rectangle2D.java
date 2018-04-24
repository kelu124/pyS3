package com.itextpdf.awt.geom;

import com.itextpdf.awt.geom.misc.HashCode;
import com.itextpdf.awt.geom.misc.Messages;
import java.util.NoSuchElementException;

public abstract class Rectangle2D extends RectangularShape {
    public static final int OUT_BOTTOM = 8;
    public static final int OUT_LEFT = 1;
    public static final int OUT_RIGHT = 4;
    public static final int OUT_TOP = 2;

    public static class Double extends Rectangle2D {
        public double height;
        public double width;
        public double f94x;
        public double f95y;

        public Double(double x, double y, double width, double height) {
            setRect(x, y, width, height);
        }

        public double getX() {
            return this.f94x;
        }

        public double getY() {
            return this.f95y;
        }

        public double getWidth() {
            return this.width;
        }

        public double getHeight() {
            return this.height;
        }

        public boolean isEmpty() {
            return this.width <= 0.0d || this.height <= 0.0d;
        }

        public void setRect(double x, double y, double width, double height) {
            this.f94x = x;
            this.f95y = y;
            this.width = width;
            this.height = height;
        }

        public void setRect(Rectangle2D r) {
            this.f94x = r.getX();
            this.f95y = r.getY();
            this.width = r.getWidth();
            this.height = r.getHeight();
        }

        public int outcode(double px, double py) {
            int code = 0;
            if (this.width <= 0.0d) {
                code = 0 | 5;
            } else if (px < this.f94x) {
                code = 0 | 1;
            } else if (px > this.f94x + this.width) {
                code = 0 | 4;
            }
            if (this.height <= 0.0d) {
                return code | 10;
            }
            if (py < this.f95y) {
                return code | 2;
            }
            if (py > this.f95y + this.height) {
                return code | 8;
            }
            return code;
        }

        public Rectangle2D getBounds2D() {
            return new Double(this.f94x, this.f95y, this.width, this.height);
        }

        public Rectangle2D createIntersection(Rectangle2D r) {
            Rectangle2D dst = new Double();
            Rectangle2D.intersect(this, r, dst);
            return dst;
        }

        public Rectangle2D createUnion(Rectangle2D r) {
            Rectangle2D dest = new Double();
            Rectangle2D.union(this, r, dest);
            return dest;
        }

        public String toString() {
            return getClass().getName() + "[x=" + this.f94x + ",y=" + this.f95y + ",width=" + this.width + ",height=" + this.height + "]";
        }
    }

    public static class Float extends Rectangle2D {
        public float height;
        public float width;
        public float f96x;
        public float f97y;

        public Float(float x, float y, float width, float height) {
            setRect(x, y, width, height);
        }

        public double getX() {
            return (double) this.f96x;
        }

        public double getY() {
            return (double) this.f97y;
        }

        public double getWidth() {
            return (double) this.width;
        }

        public double getHeight() {
            return (double) this.height;
        }

        public boolean isEmpty() {
            return this.width <= 0.0f || this.height <= 0.0f;
        }

        public void setRect(float x, float y, float width, float height) {
            this.f96x = x;
            this.f97y = y;
            this.width = width;
            this.height = height;
        }

        public void setRect(double x, double y, double width, double height) {
            this.f96x = (float) x;
            this.f97y = (float) y;
            this.width = (float) width;
            this.height = (float) height;
        }

        public void setRect(Rectangle2D r) {
            this.f96x = (float) r.getX();
            this.f97y = (float) r.getY();
            this.width = (float) r.getWidth();
            this.height = (float) r.getHeight();
        }

        public int outcode(double px, double py) {
            int code = 0;
            if (this.width <= 0.0f) {
                code = 0 | 5;
            } else if (px < ((double) this.f96x)) {
                code = 0 | 1;
            } else if (px > ((double) (this.f96x + this.width))) {
                code = 0 | 4;
            }
            if (this.height <= 0.0f) {
                return code | 10;
            }
            if (py < ((double) this.f97y)) {
                return code | 2;
            }
            if (py > ((double) (this.f97y + this.height))) {
                return code | 8;
            }
            return code;
        }

        public Rectangle2D getBounds2D() {
            return new Float(this.f96x, this.f97y, this.width, this.height);
        }

        public Rectangle2D createIntersection(Rectangle2D r) {
            Rectangle2D dst;
            if (r instanceof Double) {
                dst = new Double();
            } else {
                dst = new Float();
            }
            Rectangle2D.intersect(this, r, dst);
            return dst;
        }

        public Rectangle2D createUnion(Rectangle2D r) {
            Rectangle2D dst;
            if (r instanceof Double) {
                dst = new Double();
            } else {
                dst = new Float();
            }
            Rectangle2D.union(this, r, dst);
            return dst;
        }

        public String toString() {
            return getClass().getName() + "[x=" + this.f96x + ",y=" + this.f97y + ",width=" + this.width + ",height=" + this.height + "]";
        }
    }

    class Iterator implements PathIterator {
        double height;
        int index;
        AffineTransform f98t;
        double width;
        double f99x;
        double f100y;

        Iterator(Rectangle2D r, AffineTransform at) {
            this.f99x = r.getX();
            this.f100y = r.getY();
            this.width = r.getWidth();
            this.height = r.getHeight();
            this.f98t = at;
            if (this.width < 0.0d || this.height < 0.0d) {
                this.index = 6;
            }
        }

        public int getWindingRule() {
            return 1;
        }

        public boolean isDone() {
            return this.index > 5;
        }

        public void next() {
            this.index++;
        }

        public int currentSegment(double[] coords) {
            if (isDone()) {
                throw new NoSuchElementException(Messages.getString("awt.4B"));
            } else if (this.index == 5) {
                return 4;
            } else {
                int type;
                if (this.index != 0) {
                    type = 1;
                    switch (this.index) {
                        case 1:
                            coords[0] = this.f99x + this.width;
                            coords[1] = this.f100y;
                            break;
                        case 2:
                            coords[0] = this.f99x + this.width;
                            coords[1] = this.f100y + this.height;
                            break;
                        case 3:
                            coords[0] = this.f99x;
                            coords[1] = this.f100y + this.height;
                            break;
                        case 4:
                            coords[0] = this.f99x;
                            coords[1] = this.f100y;
                            break;
                        default:
                            break;
                    }
                }
                type = 0;
                coords[0] = this.f99x;
                coords[1] = this.f100y;
                if (this.f98t == null) {
                    return type;
                }
                this.f98t.transform(coords, 0, coords, 0, 1);
                return type;
            }
        }

        public int currentSegment(float[] coords) {
            if (isDone()) {
                throw new NoSuchElementException(Messages.getString("awt.4B"));
            } else if (this.index == 5) {
                return 4;
            } else {
                int type;
                if (this.index != 0) {
                    type = 1;
                    switch (this.index) {
                        case 1:
                            coords[0] = (float) (this.f99x + this.width);
                            coords[1] = (float) this.f100y;
                            break;
                        case 2:
                            coords[0] = (float) (this.f99x + this.width);
                            coords[1] = (float) (this.f100y + this.height);
                            break;
                        case 3:
                            coords[0] = (float) this.f99x;
                            coords[1] = (float) (this.f100y + this.height);
                            break;
                        case 4:
                            coords[0] = (float) this.f99x;
                            coords[1] = (float) this.f100y;
                            break;
                        default:
                            break;
                    }
                }
                coords[0] = (float) this.f99x;
                coords[1] = (float) this.f100y;
                type = 0;
                if (this.f98t == null) {
                    return type;
                }
                this.f98t.transform(coords, 0, coords, 0, 1);
                return type;
            }
        }
    }

    public abstract Rectangle2D createIntersection(Rectangle2D rectangle2D);

    public abstract Rectangle2D createUnion(Rectangle2D rectangle2D);

    public abstract int outcode(double d, double d2);

    public abstract void setRect(double d, double d2, double d3, double d4);

    protected Rectangle2D() {
    }

    public void setRect(Rectangle2D r) {
        setRect(r.getX(), r.getY(), r.getWidth(), r.getHeight());
    }

    public void setFrame(double x, double y, double width, double height) {
        setRect(x, y, width, height);
    }

    public Rectangle2D getBounds2D() {
        return (Rectangle2D) clone();
    }

    public boolean intersectsLine(double x1, double y1, double x2, double y2) {
        double rx1 = getX();
        double ry1 = getY();
        double rx2 = rx1 + getWidth();
        double ry2 = ry1 + getHeight();
        return (rx1 <= x1 && x1 <= rx2 && ry1 <= y1 && y1 <= ry2) || ((rx1 <= x2 && x2 <= rx2 && ry1 <= y2 && y2 <= ry2) || Line2D.linesIntersect(rx1, ry1, rx2, ry2, x1, y1, x2, y2) || Line2D.linesIntersect(rx2, ry1, rx1, ry2, x1, y1, x2, y2));
    }

    public boolean intersectsLine(Line2D l) {
        return intersectsLine(l.getX1(), l.getY1(), l.getX2(), l.getY2());
    }

    public int outcode(Point2D p) {
        return outcode(p.getX(), p.getY());
    }

    public boolean contains(double x, double y) {
        if (isEmpty()) {
            return false;
        }
        double x1 = getX();
        double y1 = getY();
        return x1 <= x && x < x1 + getWidth() && y1 <= y && y < y1 + getHeight();
    }

    public boolean intersects(double x, double y, double width, double height) {
        if (isEmpty() || width <= 0.0d || height <= 0.0d) {
            return false;
        }
        double x1 = getX();
        double y1 = getY();
        return x + width > x1 && x < x1 + getWidth() && y + height > y1 && y < y1 + getHeight();
    }

    public boolean contains(double x, double y, double width, double height) {
        if (isEmpty() || width <= 0.0d || height <= 0.0d) {
            return false;
        }
        double x1 = getX();
        double y1 = getY();
        return x1 <= x && x + width <= x1 + getWidth() && y1 <= y && y + height <= y1 + getHeight();
    }

    public static void intersect(Rectangle2D src1, Rectangle2D src2, Rectangle2D dst) {
        double x1 = Math.max(src1.getMinX(), src2.getMinX());
        double y1 = Math.max(src1.getMinY(), src2.getMinY());
        Rectangle2D rectangle2D = dst;
        rectangle2D.setFrame(x1, y1, Math.min(src1.getMaxX(), src2.getMaxX()) - x1, Math.min(src1.getMaxY(), src2.getMaxY()) - y1);
    }

    public static void union(Rectangle2D src1, Rectangle2D src2, Rectangle2D dst) {
        double x1 = Math.min(src1.getMinX(), src2.getMinX());
        double y1 = Math.min(src1.getMinY(), src2.getMinY());
        Rectangle2D rectangle2D = dst;
        rectangle2D.setFrame(x1, y1, Math.max(src1.getMaxX(), src2.getMaxX()) - x1, Math.max(src1.getMaxY(), src2.getMaxY()) - y1);
    }

    public void add(double x, double y) {
        double x1 = Math.min(getMinX(), x);
        double y1 = Math.min(getMinY(), y);
        setRect(x1, y1, Math.max(getMaxX(), x) - x1, Math.max(getMaxY(), y) - y1);
    }

    public void add(Point2D p) {
        add(p.getX(), p.getY());
    }

    public void add(Rectangle2D r) {
        union(this, r, this);
    }

    public PathIterator getPathIterator(AffineTransform t) {
        return new Iterator(this, t);
    }

    public PathIterator getPathIterator(AffineTransform t, double flatness) {
        return new Iterator(this, t);
    }

    public int hashCode() {
        HashCode hash = new HashCode();
        hash.append(getX());
        hash.append(getY());
        hash.append(getWidth());
        hash.append(getHeight());
        return hash.hashCode();
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Rectangle2D)) {
            return false;
        }
        Rectangle2D r = (Rectangle2D) obj;
        if (getX() == r.getX() && getY() == r.getY() && getWidth() == r.getWidth() && getHeight() == r.getHeight()) {
            return true;
        }
        return false;
    }
}
