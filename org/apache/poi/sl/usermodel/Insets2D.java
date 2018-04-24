package org.apache.poi.sl.usermodel;

public final class Insets2D implements Cloneable {
    public double bottom;
    public double left;
    public double right;
    public double top;

    public Insets2D(double top, double left, double bottom, double right) {
        this.top = top;
        this.left = left;
        this.bottom = bottom;
        this.right = right;
    }

    public void set(double top, double left, double bottom, double right) {
        this.top = top;
        this.left = left;
        this.bottom = bottom;
        this.right = right;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof Insets2D)) {
            return false;
        }
        Insets2D insets = (Insets2D) obj;
        if (this.top == insets.top && this.left == insets.left && this.bottom == insets.bottom && this.right == insets.right) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        double sum1 = this.left + this.bottom;
        double sum2 = this.right + this.top;
        double val2 = (((1.0d + sum2) * sum2) / 2.0d) + this.top;
        double sum3 = ((((1.0d + sum1) * sum1) / 2.0d) + this.left) + val2;
        return (int) ((((1.0d + sum3) * sum3) / 2.0d) + val2);
    }

    public String toString() {
        return getClass().getName() + "[top=" + this.top + ",left=" + this.left + ",bottom=" + this.bottom + ",right=" + this.right + "]";
    }

    public Insets2D clone() {
        return new Insets2D(this.top, this.left, this.bottom, this.right);
    }
}
