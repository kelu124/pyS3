package com.itextpdf.awt.geom;

import com.itextpdf.awt.geom.misc.Messages;
import java.util.NoSuchElementException;

public class FlatteningPathIterator implements PathIterator {
    private static final int BUFFER_CAPACITY = 16;
    private static final int BUFFER_LIMIT = 16;
    private static final int BUFFER_SIZE = 16;
    double[] buf;
    boolean bufEmpty;
    int bufIndex;
    int bufLimit;
    int bufSize;
    int bufSubdiv;
    int bufType;
    double[] coords;
    double flatness;
    double flatness2;
    PathIterator f82p;
    double px;
    double py;

    public FlatteningPathIterator(PathIterator path, double flatness) {
        this(path, flatness, 16);
    }

    public FlatteningPathIterator(PathIterator path, double flatness, int limit) {
        this.bufEmpty = true;
        this.coords = new double[6];
        if (flatness < 0.0d) {
            throw new IllegalArgumentException(Messages.getString("awt.206"));
        } else if (limit < 0) {
            throw new IllegalArgumentException(Messages.getString("awt.207"));
        } else if (path == null) {
            throw new NullPointerException(Messages.getString("awt.208"));
        } else {
            this.f82p = path;
            this.flatness = flatness;
            this.flatness2 = flatness * flatness;
            this.bufLimit = limit;
            this.bufSize = Math.min(this.bufLimit, 16);
            this.buf = new double[this.bufSize];
            this.bufIndex = this.bufSize;
        }
    }

    public double getFlatness() {
        return this.flatness;
    }

    public int getRecursionLimit() {
        return this.bufLimit;
    }

    public int getWindingRule() {
        return this.f82p.getWindingRule();
    }

    public boolean isDone() {
        return this.bufEmpty && this.f82p.isDone();
    }

    void evaluate() {
        boolean z = false;
        if (this.bufEmpty) {
            this.bufType = this.f82p.currentSegment(this.coords);
        }
        double[] tmp;
        switch (this.bufType) {
            case 0:
            case 1:
                this.px = this.coords[0];
                this.py = this.coords[1];
                return;
            case 2:
                boolean z2;
                if (this.bufEmpty) {
                    this.bufIndex -= 6;
                    this.buf[this.bufIndex + 0] = this.px;
                    this.buf[this.bufIndex + 1] = this.py;
                    System.arraycopy(this.coords, 0, this.buf, this.bufIndex + 2, 4);
                    this.bufSubdiv = 0;
                }
                while (this.bufSubdiv < this.bufLimit && QuadCurve2D.getFlatnessSq(this.buf, this.bufIndex) >= this.flatness2) {
                    if (this.bufIndex <= 4) {
                        tmp = new double[(this.bufSize + 16)];
                        System.arraycopy(this.buf, this.bufIndex, tmp, this.bufIndex + 16, this.bufSize - this.bufIndex);
                        this.buf = tmp;
                        this.bufSize += 16;
                        this.bufIndex += 16;
                    }
                    QuadCurve2D.subdivide(this.buf, this.bufIndex, this.buf, this.bufIndex - 4, this.buf, this.bufIndex);
                    this.bufIndex -= 4;
                    this.bufSubdiv++;
                }
                this.bufIndex += 4;
                this.px = this.buf[this.bufIndex];
                this.py = this.buf[this.bufIndex + 1];
                if (this.bufIndex == this.bufSize - 2) {
                    z2 = true;
                } else {
                    z2 = false;
                }
                this.bufEmpty = z2;
                if (this.bufEmpty) {
                    this.bufIndex = this.bufSize;
                    this.bufType = 1;
                    return;
                }
                return;
            case 3:
                if (this.bufEmpty) {
                    this.bufIndex -= 8;
                    this.buf[this.bufIndex + 0] = this.px;
                    this.buf[this.bufIndex + 1] = this.py;
                    System.arraycopy(this.coords, 0, this.buf, this.bufIndex + 2, 6);
                    this.bufSubdiv = 0;
                }
                while (this.bufSubdiv < this.bufLimit && CubicCurve2D.getFlatnessSq(this.buf, this.bufIndex) >= this.flatness2) {
                    if (this.bufIndex <= 6) {
                        tmp = new double[(this.bufSize + 16)];
                        System.arraycopy(this.buf, this.bufIndex, tmp, this.bufIndex + 16, this.bufSize - this.bufIndex);
                        this.buf = tmp;
                        this.bufSize += 16;
                        this.bufIndex += 16;
                    }
                    CubicCurve2D.subdivide(this.buf, this.bufIndex, this.buf, this.bufIndex - 6, this.buf, this.bufIndex);
                    this.bufIndex -= 6;
                    this.bufSubdiv++;
                }
                this.bufIndex += 6;
                this.px = this.buf[this.bufIndex];
                this.py = this.buf[this.bufIndex + 1];
                if (this.bufIndex == this.bufSize - 2) {
                    z = true;
                }
                this.bufEmpty = z;
                if (this.bufEmpty) {
                    this.bufIndex = this.bufSize;
                    this.bufType = 1;
                    return;
                }
                return;
            default:
                return;
        }
    }

    public void next() {
        if (this.bufEmpty) {
            this.f82p.next();
        }
    }

    public int currentSegment(float[] coords) {
        if (isDone()) {
            throw new NoSuchElementException(Messages.getString("awt.4Bx"));
        }
        evaluate();
        int type = this.bufType;
        if (type == 4) {
            return type;
        }
        coords[0] = (float) this.px;
        coords[1] = (float) this.py;
        if (type != 0) {
            return 1;
        }
        return type;
    }

    public int currentSegment(double[] coords) {
        if (isDone()) {
            throw new NoSuchElementException(Messages.getString("awt.4B"));
        }
        evaluate();
        int type = this.bufType;
        if (type == 4) {
            return type;
        }
        coords[0] = this.px;
        coords[1] = this.py;
        if (type != 0) {
            return 1;
        }
        return type;
    }
}
