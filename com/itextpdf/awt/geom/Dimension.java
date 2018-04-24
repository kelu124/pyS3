package com.itextpdf.awt.geom;

import com.itextpdf.awt.geom.misc.HashCode;
import java.io.Serializable;

public class Dimension extends Dimension2D implements Serializable {
    private static final long serialVersionUID = 4723952579491349524L;
    public double height;
    public double width;

    public Dimension(Dimension d) {
        this(d.width, d.height);
    }

    public Dimension() {
        this(0, 0);
    }

    public Dimension(double width, double height) {
        setSize(width, height);
    }

    public Dimension(int width, int height) {
        setSize(width, height);
    }

    public int hashCode() {
        HashCode hash = new HashCode();
        hash.append(this.width);
        hash.append(this.height);
        return hash.hashCode();
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Dimension)) {
            return false;
        }
        Dimension d = (Dimension) obj;
        if (d.width == this.width && d.height == this.height) {
            return true;
        }
        return false;
    }

    public String toString() {
        return getClass().getName() + "[width=" + this.width + ",height=" + this.height + "]";
    }

    public void setSize(int width, int height) {
        this.width = (double) width;
        this.height = (double) height;
    }

    public void setSize(Dimension d) {
        setSize(d.width, d.height);
    }

    public void setSize(double width, double height) {
        setSize((int) Math.ceil(width), (int) Math.ceil(height));
    }

    public Dimension getSize() {
        return new Dimension(this.width, this.height);
    }

    public double getHeight() {
        return this.height;
    }

    public double getWidth() {
        return this.width;
    }
}
