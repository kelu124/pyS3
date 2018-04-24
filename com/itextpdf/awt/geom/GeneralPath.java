package com.itextpdf.awt.geom;

import com.itextpdf.awt.geom.Point2D.Float;
import com.itextpdf.awt.geom.gl.Crossing;
import com.itextpdf.awt.geom.misc.Messages;
import java.util.NoSuchElementException;

public final class GeneralPath implements Shape, Cloneable {
    private static final int BUFFER_CAPACITY = 10;
    private static final int BUFFER_SIZE = 10;
    public static final int WIND_EVEN_ODD = 0;
    public static final int WIND_NON_ZERO = 1;
    static int[] pointShift = new int[]{2, 2, 4, 6, 0};
    int pointSize;
    float[] points;
    int rule;
    int typeSize;
    byte[] types;

    class Iterator implements PathIterator {
        GeneralPath f83p;
        int pointIndex;
        AffineTransform f84t;
        int typeIndex;

        Iterator(GeneralPath generalPath, GeneralPath path) {
            this(path, null);
        }

        Iterator(GeneralPath path, AffineTransform at) {
            this.f83p = path;
            this.f84t = at;
        }

        public int getWindingRule() {
            return this.f83p.getWindingRule();
        }

        public boolean isDone() {
            return this.typeIndex >= this.f83p.typeSize;
        }

        public void next() {
            this.typeIndex++;
        }

        public int currentSegment(double[] coords) {
            if (isDone()) {
                throw new NoSuchElementException(Messages.getString("awt.4B"));
            }
            int type = this.f83p.types[this.typeIndex];
            int count = GeneralPath.pointShift[type];
            for (int i = 0; i < count; i++) {
                coords[i] = (double) this.f83p.points[this.pointIndex + i];
            }
            if (this.f84t != null) {
                this.f84t.transform(coords, 0, coords, 0, count / 2);
            }
            this.pointIndex += count;
            return type;
        }

        public int currentSegment(float[] coords) {
            if (isDone()) {
                throw new NoSuchElementException(Messages.getString("awt.4B"));
            }
            int type = this.f83p.types[this.typeIndex];
            int count = GeneralPath.pointShift[type];
            System.arraycopy(this.f83p.points, this.pointIndex, coords, 0, count);
            if (this.f84t != null) {
                this.f84t.transform(coords, 0, coords, 0, count / 2);
            }
            this.pointIndex += count;
            return type;
        }
    }

    public GeneralPath() {
        this(1, 10);
    }

    public GeneralPath(int rule) {
        this(rule, 10);
    }

    public GeneralPath(int rule, int initialCapacity) {
        setWindingRule(rule);
        this.types = new byte[initialCapacity];
        this.points = new float[(initialCapacity * 2)];
    }

    public GeneralPath(Shape shape) {
        this(1, 10);
        PathIterator p = shape.getPathIterator(null);
        setWindingRule(p.getWindingRule());
        append(p, false);
    }

    public void setWindingRule(int rule) {
        if (rule == 0 || rule == 1) {
            this.rule = rule;
            return;
        }
        throw new IllegalArgumentException(Messages.getString("awt.209"));
    }

    public int getWindingRule() {
        return this.rule;
    }

    void checkBuf(int pointCount, boolean checkMove) {
        if (checkMove && this.typeSize == 0) {
            throw new IllegalPathStateException(Messages.getString("awt.20A"));
        }
        if (this.typeSize == this.types.length) {
            byte[] tmp = new byte[(this.typeSize + 10)];
            System.arraycopy(this.types, 0, tmp, 0, this.typeSize);
            this.types = tmp;
        }
        if (this.pointSize + pointCount > this.points.length) {
            float[] tmp2 = new float[(this.pointSize + Math.max(20, pointCount))];
            System.arraycopy(this.points, 0, tmp2, 0, this.pointSize);
            this.points = tmp2;
        }
    }

    public void moveTo(float x, float y) {
        if (this.typeSize <= 0 || this.types[this.typeSize - 1] != (byte) 0) {
            checkBuf(2, false);
            byte[] bArr = this.types;
            int i = this.typeSize;
            this.typeSize = i + 1;
            bArr[i] = (byte) 0;
            float[] fArr = this.points;
            i = this.pointSize;
            this.pointSize = i + 1;
            fArr[i] = x;
            fArr = this.points;
            i = this.pointSize;
            this.pointSize = i + 1;
            fArr[i] = y;
            return;
        }
        this.points[this.pointSize - 2] = x;
        this.points[this.pointSize - 1] = y;
    }

    public void lineTo(float x, float y) {
        checkBuf(2, true);
        byte[] bArr = this.types;
        int i = this.typeSize;
        this.typeSize = i + 1;
        bArr[i] = (byte) 1;
        float[] fArr = this.points;
        i = this.pointSize;
        this.pointSize = i + 1;
        fArr[i] = x;
        fArr = this.points;
        i = this.pointSize;
        this.pointSize = i + 1;
        fArr[i] = y;
    }

    public void quadTo(float x1, float y1, float x2, float y2) {
        checkBuf(4, true);
        byte[] bArr = this.types;
        int i = this.typeSize;
        this.typeSize = i + 1;
        bArr[i] = (byte) 2;
        float[] fArr = this.points;
        i = this.pointSize;
        this.pointSize = i + 1;
        fArr[i] = x1;
        fArr = this.points;
        i = this.pointSize;
        this.pointSize = i + 1;
        fArr[i] = y1;
        fArr = this.points;
        i = this.pointSize;
        this.pointSize = i + 1;
        fArr[i] = x2;
        fArr = this.points;
        i = this.pointSize;
        this.pointSize = i + 1;
        fArr[i] = y2;
    }

    public void curveTo(float x1, float y1, float x2, float y2, float x3, float y3) {
        checkBuf(6, true);
        byte[] bArr = this.types;
        int i = this.typeSize;
        this.typeSize = i + 1;
        bArr[i] = (byte) 3;
        float[] fArr = this.points;
        i = this.pointSize;
        this.pointSize = i + 1;
        fArr[i] = x1;
        fArr = this.points;
        i = this.pointSize;
        this.pointSize = i + 1;
        fArr[i] = y1;
        fArr = this.points;
        i = this.pointSize;
        this.pointSize = i + 1;
        fArr[i] = x2;
        fArr = this.points;
        i = this.pointSize;
        this.pointSize = i + 1;
        fArr[i] = y2;
        fArr = this.points;
        i = this.pointSize;
        this.pointSize = i + 1;
        fArr[i] = x3;
        fArr = this.points;
        i = this.pointSize;
        this.pointSize = i + 1;
        fArr[i] = y3;
    }

    public void closePath() {
        if (this.typeSize == 0 || this.types[this.typeSize - 1] != (byte) 4) {
            checkBuf(0, true);
            byte[] bArr = this.types;
            int i = this.typeSize;
            this.typeSize = i + 1;
            bArr[i] = (byte) 4;
        }
    }

    public void append(Shape shape, boolean connect) {
        append(shape.getPathIterator(null), connect);
    }

    public void append(PathIterator path, boolean connect) {
        while (!path.isDone()) {
            float[] coords = new float[6];
            switch (path.currentSegment(coords)) {
                case 0:
                    if (connect && this.typeSize != 0) {
                        if (this.types[this.typeSize - 1] != (byte) 4 && this.points[this.pointSize - 2] == coords[0] && this.points[this.pointSize - 1] == coords[1]) {
                            break;
                        }
                    } else {
                        moveTo(coords[0], coords[1]);
                        break;
                    }
                    break;
                case 1:
                    lineTo(coords[0], coords[1]);
                    break;
                case 2:
                    quadTo(coords[0], coords[1], coords[2], coords[3]);
                    break;
                case 3:
                    curveTo(coords[0], coords[1], coords[2], coords[3], coords[4], coords[5]);
                    break;
                case 4:
                    closePath();
                    break;
                default:
                    break;
            }
            path.next();
            connect = false;
        }
    }

    public Point2D getCurrentPoint() {
        if (this.typeSize == 0) {
            return null;
        }
        int j = this.pointSize - 2;
        if (this.types[this.typeSize - 1] == (byte) 4) {
            for (int i = this.typeSize - 2; i > 0; i--) {
                int type = this.types[i];
                if (type == 0) {
                    break;
                }
                j -= pointShift[type];
            }
        }
        return new Float(this.points[j], this.points[j + 1]);
    }

    public void reset() {
        this.typeSize = 0;
        this.pointSize = 0;
    }

    public void transform(AffineTransform t) {
        t.transform(this.points, 0, this.points, 0, this.pointSize / 2);
    }

    public Shape createTransformedShape(AffineTransform t) {
        GeneralPath p = (GeneralPath) clone();
        if (t != null) {
            p.transform(t);
        }
        return p;
    }

    public Rectangle2D getBounds2D() {
        float ry2;
        float rx2;
        float ry1;
        float rx1;
        if (this.pointSize == 0) {
            ry2 = 0.0f;
            rx2 = 0.0f;
            ry1 = 0.0f;
            rx1 = 0.0f;
        } else {
            int i = this.pointSize - 1;
            int i2 = i - 1;
            ry2 = this.points[i];
            ry1 = ry2;
            i = i2 - 1;
            rx2 = this.points[i2];
            rx1 = rx2;
            i2 = i;
            while (i2 > 0) {
                i = i2 - 1;
                float y = this.points[i2];
                i2 = i - 1;
                float x = this.points[i];
                if (x < rx1) {
                    rx1 = x;
                } else if (x > rx2) {
                    rx2 = x;
                }
                if (y < ry1) {
                    ry1 = y;
                } else if (y > ry2) {
                    ry2 = y;
                }
            }
        }
        return new Rectangle2D.Float(rx1, ry1, rx2 - rx1, ry2 - ry1);
    }

    public Rectangle getBounds() {
        return getBounds2D().getBounds();
    }

    boolean isInside(int cross) {
        if (this.rule == 1) {
            return Crossing.isInsideNonZero(cross);
        }
        return Crossing.isInsideEvenOdd(cross);
    }

    public boolean contains(double px, double py) {
        return isInside(Crossing.crossShape(this, px, py));
    }

    public boolean contains(double rx, double ry, double rw, double rh) {
        int cross = Crossing.intersectShape(this, rx, ry, rw, rh);
        return cross != 255 && isInside(cross);
    }

    public boolean intersects(double rx, double ry, double rw, double rh) {
        int cross = Crossing.intersectShape(this, rx, ry, rw, rh);
        return cross == 255 || isInside(cross);
    }

    public boolean contains(Point2D p) {
        return contains(p.getX(), p.getY());
    }

    public boolean contains(Rectangle2D r) {
        return contains(r.getX(), r.getY(), r.getWidth(), r.getHeight());
    }

    public boolean intersects(Rectangle2D r) {
        return intersects(r.getX(), r.getY(), r.getWidth(), r.getHeight());
    }

    public PathIterator getPathIterator(AffineTransform t) {
        return new Iterator(this, t);
    }

    public PathIterator getPathIterator(AffineTransform t, double flatness) {
        return new FlatteningPathIterator(getPathIterator(t), flatness);
    }

    public Object clone() {
        try {
            GeneralPath p = (GeneralPath) super.clone();
            p.types = (byte[]) this.types.clone();
            p.points = (float[]) this.points.clone();
            return p;
        } catch (CloneNotSupportedException e) {
            throw new InternalError();
        }
    }
}
