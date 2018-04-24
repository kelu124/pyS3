package com.itextpdf.awt.geom;

public abstract class Dimension2D implements Cloneable {
    public abstract double getHeight();

    public abstract double getWidth();

    public abstract void setSize(double d, double d2);

    protected Dimension2D() {
    }

    public void setSize(Dimension2D d) {
        setSize(d.getWidth(), d.getHeight());
    }

    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            throw new InternalError();
        }
    }
}
