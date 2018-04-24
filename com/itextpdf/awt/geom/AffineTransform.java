package com.itextpdf.awt.geom;

import com.itextpdf.awt.geom.Point2D.Double;
import com.itextpdf.awt.geom.Point2D.Float;
import com.itextpdf.awt.geom.misc.HashCode;
import com.itextpdf.awt.geom.misc.Messages;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class AffineTransform implements Cloneable, Serializable {
    public static final int TYPE_FLIP = 64;
    public static final int TYPE_GENERAL_ROTATION = 16;
    public static final int TYPE_GENERAL_SCALE = 4;
    public static final int TYPE_GENERAL_TRANSFORM = 32;
    public static final int TYPE_IDENTITY = 0;
    public static final int TYPE_MASK_ROTATION = 24;
    public static final int TYPE_MASK_SCALE = 6;
    public static final int TYPE_QUADRANT_ROTATION = 8;
    public static final int TYPE_TRANSLATION = 1;
    public static final int TYPE_UNIFORM_SCALE = 2;
    static final int TYPE_UNKNOWN = -1;
    static final double ZERO = 1.0E-10d;
    private static final long serialVersionUID = 1330973210523860834L;
    double m00;
    double m01;
    double m02;
    double m10;
    double m11;
    double m12;
    transient int type;

    public AffineTransform() {
        this.type = 0;
        this.m11 = 1.0d;
        this.m00 = 1.0d;
        this.m12 = 0.0d;
        this.m02 = 0.0d;
        this.m01 = 0.0d;
        this.m10 = 0.0d;
    }

    public AffineTransform(AffineTransform t) {
        this.type = t.type;
        this.m00 = t.m00;
        this.m10 = t.m10;
        this.m01 = t.m01;
        this.m11 = t.m11;
        this.m02 = t.m02;
        this.m12 = t.m12;
    }

    public AffineTransform(float m00, float m10, float m01, float m11, float m02, float m12) {
        this.type = -1;
        this.m00 = (double) m00;
        this.m10 = (double) m10;
        this.m01 = (double) m01;
        this.m11 = (double) m11;
        this.m02 = (double) m02;
        this.m12 = (double) m12;
    }

    public AffineTransform(double m00, double m10, double m01, double m11, double m02, double m12) {
        this.type = -1;
        this.m00 = m00;
        this.m10 = m10;
        this.m01 = m01;
        this.m11 = m11;
        this.m02 = m02;
        this.m12 = m12;
    }

    public AffineTransform(float[] matrix) {
        this.type = -1;
        this.m00 = (double) matrix[0];
        this.m10 = (double) matrix[1];
        this.m01 = (double) matrix[2];
        this.m11 = (double) matrix[3];
        if (matrix.length > 4) {
            this.m02 = (double) matrix[4];
            this.m12 = (double) matrix[5];
        }
    }

    public AffineTransform(double[] matrix) {
        this.type = -1;
        this.m00 = matrix[0];
        this.m10 = matrix[1];
        this.m01 = matrix[2];
        this.m11 = matrix[3];
        if (matrix.length > 4) {
            this.m02 = matrix[4];
            this.m12 = matrix[5];
        }
    }

    public int getType() {
        if (this.type != -1) {
            return this.type;
        }
        int type = 0;
        if ((this.m00 * this.m01) + (this.m10 * this.m11) != 0.0d) {
            return 0 | 32;
        }
        if (this.m02 != 0.0d || this.m12 != 0.0d) {
            type = 0 | 1;
        } else if (this.m00 == 1.0d && this.m11 == 1.0d && this.m01 == 0.0d && this.m10 == 0.0d) {
            return 0;
        }
        if ((this.m00 * this.m11) - (this.m01 * this.m10) < 0.0d) {
            type |= 64;
        }
        double dx = (this.m00 * this.m00) + (this.m10 * this.m10);
        if (dx != (this.m01 * this.m01) + (this.m11 * this.m11)) {
            type |= 4;
        } else if (dx != 1.0d) {
            type |= 2;
        }
        if ((this.m00 == 0.0d && this.m11 == 0.0d) || (this.m10 == 0.0d && this.m01 == 0.0d && (this.m00 < 0.0d || this.m11 < 0.0d))) {
            return type | 8;
        }
        if (this.m01 == 0.0d && this.m10 == 0.0d) {
            return type;
        }
        return type | 16;
    }

    public double getScaleX() {
        return this.m00;
    }

    public double getScaleY() {
        return this.m11;
    }

    public double getShearX() {
        return this.m01;
    }

    public double getShearY() {
        return this.m10;
    }

    public double getTranslateX() {
        return this.m02;
    }

    public double getTranslateY() {
        return this.m12;
    }

    public boolean isIdentity() {
        return getType() == 0;
    }

    public void getMatrix(double[] matrix) {
        matrix[0] = this.m00;
        matrix[1] = this.m10;
        matrix[2] = this.m01;
        matrix[3] = this.m11;
        if (matrix.length > 4) {
            matrix[4] = this.m02;
            matrix[5] = this.m12;
        }
    }

    public double getDeterminant() {
        return (this.m00 * this.m11) - (this.m01 * this.m10);
    }

    public void setTransform(double m00, double m10, double m01, double m11, double m02, double m12) {
        this.type = -1;
        this.m00 = m00;
        this.m10 = m10;
        this.m01 = m01;
        this.m11 = m11;
        this.m02 = m02;
        this.m12 = m12;
    }

    public void setTransform(AffineTransform t) {
        this.type = t.type;
        setTransform(t.m00, t.m10, t.m01, t.m11, t.m02, t.m12);
    }

    public void setToIdentity() {
        this.type = 0;
        this.m11 = 1.0d;
        this.m00 = 1.0d;
        this.m12 = 0.0d;
        this.m02 = 0.0d;
        this.m01 = 0.0d;
        this.m10 = 0.0d;
    }

    public void setToTranslation(double mx, double my) {
        this.m11 = 1.0d;
        this.m00 = 1.0d;
        this.m10 = 0.0d;
        this.m01 = 0.0d;
        this.m02 = mx;
        this.m12 = my;
        if (mx == 0.0d && my == 0.0d) {
            this.type = 0;
        } else {
            this.type = 1;
        }
    }

    public void setToScale(double scx, double scy) {
        this.m00 = scx;
        this.m11 = scy;
        this.m12 = 0.0d;
        this.m02 = 0.0d;
        this.m01 = 0.0d;
        this.m10 = 0.0d;
        if (scx == 1.0d && scy == 1.0d) {
            this.type = 0;
        } else {
            this.type = -1;
        }
    }

    public void setToShear(double shx, double shy) {
        this.m11 = 1.0d;
        this.m00 = 1.0d;
        this.m12 = 0.0d;
        this.m02 = 0.0d;
        this.m01 = shx;
        this.m10 = shy;
        if (shx == 0.0d && shy == 0.0d) {
            this.type = 0;
        } else {
            this.type = -1;
        }
    }

    public void setToRotation(double angle) {
        double sin = Math.sin(angle);
        double cos = Math.cos(angle);
        if (Math.abs(cos) < ZERO) {
            cos = 0.0d;
            sin = sin > 0.0d ? 1.0d : -1.0d;
        } else if (Math.abs(sin) < ZERO) {
            sin = 0.0d;
            cos = cos > 0.0d ? 1.0d : -1.0d;
        }
        this.m11 = cos;
        this.m00 = cos;
        this.m01 = -sin;
        this.m10 = sin;
        this.m12 = 0.0d;
        this.m02 = 0.0d;
        this.type = -1;
    }

    public void setToRotation(double angle, double px, double py) {
        setToRotation(angle);
        this.m02 = ((1.0d - this.m00) * px) + (this.m10 * py);
        this.m12 = ((1.0d - this.m00) * py) - (this.m10 * px);
        this.type = -1;
    }

    public static AffineTransform getTranslateInstance(double mx, double my) {
        AffineTransform t = new AffineTransform();
        t.setToTranslation(mx, my);
        return t;
    }

    public static AffineTransform getScaleInstance(double scx, double scY) {
        AffineTransform t = new AffineTransform();
        t.setToScale(scx, scY);
        return t;
    }

    public static AffineTransform getShearInstance(double shx, double shy) {
        AffineTransform m = new AffineTransform();
        m.setToShear(shx, shy);
        return m;
    }

    public static AffineTransform getRotateInstance(double angle) {
        AffineTransform t = new AffineTransform();
        t.setToRotation(angle);
        return t;
    }

    public static AffineTransform getRotateInstance(double angle, double x, double y) {
        AffineTransform t = new AffineTransform();
        t.setToRotation(angle, x, y);
        return t;
    }

    public void translate(double mx, double my) {
        concatenate(getTranslateInstance(mx, my));
    }

    public void scale(double scx, double scy) {
        concatenate(getScaleInstance(scx, scy));
    }

    public void shear(double shx, double shy) {
        concatenate(getShearInstance(shx, shy));
    }

    public void rotate(double angle) {
        concatenate(getRotateInstance(angle));
    }

    public void rotate(double angle, double px, double py) {
        concatenate(getRotateInstance(angle, px, py));
    }

    AffineTransform multiply(AffineTransform t1, AffineTransform t2) {
        return new AffineTransform((t1.m00 * t2.m00) + (t1.m10 * t2.m01), (t1.m00 * t2.m10) + (t1.m10 * t2.m11), (t1.m01 * t2.m00) + (t1.m11 * t2.m01), (t1.m01 * t2.m10) + (t1.m11 * t2.m11), ((t1.m02 * t2.m00) + (t1.m12 * t2.m01)) + t2.m02, ((t1.m02 * t2.m10) + (t1.m12 * t2.m11)) + t2.m12);
    }

    public void concatenate(AffineTransform t) {
        setTransform(multiply(t, this));
    }

    public void preConcatenate(AffineTransform t) {
        setTransform(multiply(this, t));
    }

    public AffineTransform createInverse() throws NoninvertibleTransformException {
        double det = getDeterminant();
        if (Math.abs(det) >= ZERO) {
            return new AffineTransform(this.m11 / det, (-this.m10) / det, (-this.m01) / det, this.m00 / det, ((this.m01 * this.m12) - (this.m11 * this.m02)) / det, ((this.m10 * this.m02) - (this.m00 * this.m12)) / det);
        }
        throw new NoninvertibleTransformException(Messages.getString("awt.204"));
    }

    public Point2D transform(Point2D src, Point2D dst) {
        if (dst == null) {
            if (src instanceof Double) {
                dst = new Double();
            } else {
                dst = new Float();
            }
        }
        double x = src.getX();
        double y = src.getY();
        dst.setLocation(((this.m00 * x) + (this.m01 * y)) + this.m02, ((this.m10 * x) + (this.m11 * y)) + this.m12);
        return dst;
    }

    public void transform(Point2D[] src, int srcOff, Point2D[] dst, int dstOff, int length) {
        int dstOff2 = dstOff;
        int srcOff2 = srcOff;
        while (true) {
            length--;
            if (length >= 0) {
                srcOff = srcOff2 + 1;
                Point2D srcPoint = src[srcOff2];
                double x = srcPoint.getX();
                double y = srcPoint.getY();
                Point2D dstPoint = dst[dstOff2];
                if (dstPoint == null) {
                    if (srcPoint instanceof Double) {
                        dstPoint = new Double();
                    } else {
                        dstPoint = new Float();
                    }
                }
                dstPoint.setLocation(((this.m00 * x) + (this.m01 * y)) + this.m02, ((this.m10 * x) + (this.m11 * y)) + this.m12);
                dstOff = dstOff2 + 1;
                dst[dstOff2] = dstPoint;
                dstOff2 = dstOff;
                srcOff2 = srcOff;
            } else {
                return;
            }
        }
    }

    public void transform(double[] src, int srcOff, double[] dst, int dstOff, int length) {
        int step = 2;
        if (src == dst && srcOff < dstOff && dstOff < (length * 2) + srcOff) {
            srcOff = ((length * 2) + srcOff) - 2;
            dstOff = ((length * 2) + dstOff) - 2;
            step = -2;
        }
        while (true) {
            length--;
            if (length >= 0) {
                double x = src[srcOff + 0];
                double y = src[srcOff + 1];
                dst[dstOff + 0] = ((this.m00 * x) + (this.m01 * y)) + this.m02;
                dst[dstOff + 1] = ((this.m10 * x) + (this.m11 * y)) + this.m12;
                srcOff += step;
                dstOff += step;
            } else {
                return;
            }
        }
    }

    public void transform(float[] src, int srcOff, float[] dst, int dstOff, int length) {
        int step = 2;
        if (src == dst && srcOff < dstOff && dstOff < (length * 2) + srcOff) {
            srcOff = ((length * 2) + srcOff) - 2;
            dstOff = ((length * 2) + dstOff) - 2;
            step = -2;
        }
        while (true) {
            length--;
            if (length >= 0) {
                float x = src[srcOff + 0];
                float y = src[srcOff + 1];
                dst[dstOff + 0] = (float) (((((double) x) * this.m00) + (((double) y) * this.m01)) + this.m02);
                dst[dstOff + 1] = (float) (((((double) x) * this.m10) + (((double) y) * this.m11)) + this.m12);
                srcOff += step;
                dstOff += step;
            } else {
                return;
            }
        }
    }

    public void transform(float[] src, int srcOff, double[] dst, int dstOff, int length) {
        int dstOff2 = dstOff;
        int srcOff2 = srcOff;
        while (true) {
            length--;
            if (length >= 0) {
                srcOff = srcOff2 + 1;
                float x = src[srcOff2];
                srcOff2 = srcOff + 1;
                float y = src[srcOff];
                dstOff = dstOff2 + 1;
                dst[dstOff2] = ((((double) x) * this.m00) + (((double) y) * this.m01)) + this.m02;
                dstOff2 = dstOff + 1;
                dst[dstOff] = ((((double) x) * this.m10) + (((double) y) * this.m11)) + this.m12;
            } else {
                return;
            }
        }
    }

    public void transform(double[] src, int srcOff, float[] dst, int dstOff, int length) {
        int dstOff2 = dstOff;
        int srcOff2 = srcOff;
        while (true) {
            length--;
            if (length >= 0) {
                srcOff = srcOff2 + 1;
                double x = src[srcOff2];
                srcOff2 = srcOff + 1;
                double y = src[srcOff];
                dstOff = dstOff2 + 1;
                dst[dstOff2] = (float) (((this.m00 * x) + (this.m01 * y)) + this.m02);
                dstOff2 = dstOff + 1;
                dst[dstOff] = (float) (((this.m10 * x) + (this.m11 * y)) + this.m12);
            } else {
                return;
            }
        }
    }

    public Point2D deltaTransform(Point2D src, Point2D dst) {
        if (dst == null) {
            if (src instanceof Double) {
                dst = new Double();
            } else {
                dst = new Float();
            }
        }
        double x = src.getX();
        double y = src.getY();
        dst.setLocation((this.m00 * x) + (this.m01 * y), (this.m10 * x) + (this.m11 * y));
        return dst;
    }

    public void deltaTransform(double[] src, int srcOff, double[] dst, int dstOff, int length) {
        int dstOff2 = dstOff;
        int srcOff2 = srcOff;
        while (true) {
            length--;
            if (length >= 0) {
                srcOff = srcOff2 + 1;
                double x = src[srcOff2];
                srcOff2 = srcOff + 1;
                double y = src[srcOff];
                dstOff = dstOff2 + 1;
                dst[dstOff2] = (this.m00 * x) + (this.m01 * y);
                dstOff2 = dstOff + 1;
                dst[dstOff] = (this.m10 * x) + (this.m11 * y);
            } else {
                return;
            }
        }
    }

    public Point2D inverseTransform(Point2D src, Point2D dst) throws NoninvertibleTransformException {
        double det = getDeterminant();
        if (Math.abs(det) < ZERO) {
            throw new NoninvertibleTransformException(Messages.getString("awt.204"));
        }
        if (dst == null) {
            if (src instanceof Double) {
                dst = new Double();
            } else {
                dst = new Float();
            }
        }
        double x = src.getX() - this.m02;
        double y = src.getY() - this.m12;
        dst.setLocation(((this.m11 * x) - (this.m01 * y)) / det, ((this.m00 * y) - (this.m10 * x)) / det);
        return dst;
    }

    public void inverseTransform(double[] src, int srcOff, double[] dst, int dstOff, int length) throws NoninvertibleTransformException {
        double det = getDeterminant();
        if (Math.abs(det) < ZERO) {
            throw new NoninvertibleTransformException(Messages.getString("awt.204"));
        }
        int dstOff2 = dstOff;
        int srcOff2 = srcOff;
        while (true) {
            length--;
            if (length >= 0) {
                srcOff = srcOff2 + 1;
                double x = src[srcOff2] - this.m02;
                srcOff2 = srcOff + 1;
                double y = src[srcOff] - this.m12;
                dstOff = dstOff2 + 1;
                dst[dstOff2] = ((this.m11 * x) - (this.m01 * y)) / det;
                dstOff2 = dstOff + 1;
                dst[dstOff] = ((this.m00 * y) - (this.m10 * x)) / det;
            } else {
                return;
            }
        }
    }

    public void inverseTransform(float[] src, int srcOff, float[] dst, int dstOff, int length) throws NoninvertibleTransformException {
        float det = (float) getDeterminant();
        if (((double) Math.abs(det)) < ZERO) {
            throw new NoninvertibleTransformException(Messages.getString("awt.204"));
        }
        int dstOff2 = dstOff;
        int srcOff2 = srcOff;
        while (true) {
            length--;
            if (length >= 0) {
                srcOff = srcOff2 + 1;
                float x = src[srcOff2] - ((float) this.m02);
                srcOff2 = srcOff + 1;
                float y = src[srcOff] - ((float) this.m12);
                dstOff = dstOff2 + 1;
                dst[dstOff2] = ((((float) this.m11) * x) - (((float) this.m01) * y)) / det;
                dstOff2 = dstOff + 1;
                dst[dstOff] = ((((float) this.m00) * y) - (((float) this.m10) * x)) / det;
            } else {
                return;
            }
        }
    }

    public Shape createTransformedShape(Shape src) {
        if (src == null) {
            return null;
        }
        if (src instanceof GeneralPath) {
            return ((GeneralPath) src).createTransformedShape(this);
        }
        PathIterator path = src.getPathIterator(this);
        Shape dst = new GeneralPath(path.getWindingRule());
        dst.append(path, false);
        return dst;
    }

    public String toString() {
        return getClass().getName() + "[[" + this.m00 + ", " + this.m01 + ", " + this.m02 + "], [" + this.m10 + ", " + this.m11 + ", " + this.m12 + "]]";
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
        hash.append(this.m00);
        hash.append(this.m01);
        hash.append(this.m02);
        hash.append(this.m10);
        hash.append(this.m11);
        hash.append(this.m12);
        return hash.hashCode();
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof AffineTransform)) {
            return false;
        }
        AffineTransform t = (AffineTransform) obj;
        if (this.m00 == t.m00 && this.m01 == t.m01 && this.m02 == t.m02 && this.m10 == t.m10 && this.m11 == t.m11 && this.m12 == t.m12) {
            return true;
        }
        return false;
    }

    private void writeObject(ObjectOutputStream stream) throws IOException {
        stream.defaultWriteObject();
    }

    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        this.type = -1;
    }
}
