package org.opencv.core;

import java.util.Arrays;

public class Scalar {
    public double[] val;

    public Scalar(double v0, double v1, double v2, double v3) {
        this.val = new double[]{v0, v1, v2, v3};
    }

    public Scalar(double v0, double v1, double v2) {
        this.val = new double[]{v0, v1, v2, 0.0d};
    }

    public Scalar(double v0, double v1) {
        this.val = new double[]{v0, v1, 0.0d, 0.0d};
    }

    public Scalar(double v0) {
        this.val = new double[]{v0, 0.0d, 0.0d, 0.0d};
    }

    public Scalar(double[] vals) {
        if (vals == null || vals.length != 4) {
            this.val = new double[4];
            set(vals);
            return;
        }
        this.val = (double[]) vals.clone();
    }

    public void set(double[] vals) {
        double d = 0.0d;
        if (vals != null) {
            double d2;
            this.val[0] = vals.length > 0 ? vals[0] : 0.0d;
            double[] dArr = this.val;
            if (vals.length > 1) {
                d2 = vals[1];
            } else {
                d2 = 0.0d;
            }
            dArr[1] = d2;
            dArr = this.val;
            if (vals.length > 2) {
                d2 = vals[2];
            } else {
                d2 = 0.0d;
            }
            dArr[2] = d2;
            double[] dArr2 = this.val;
            if (vals.length > 3) {
                d = vals[3];
            }
            dArr2[3] = d;
            return;
        }
        dArr2 = this.val;
        double[] dArr3 = this.val;
        dArr = this.val;
        this.val[3] = 0.0d;
        dArr[2] = 0.0d;
        dArr3[1] = 0.0d;
        dArr2[0] = 0.0d;
    }

    public static Scalar all(double v) {
        return new Scalar(v, v, v, v);
    }

    public Scalar clone() {
        return new Scalar(this.val);
    }

    public Scalar mul(Scalar it, double scale) {
        return new Scalar((this.val[0] * it.val[0]) * scale, (this.val[1] * it.val[1]) * scale, (this.val[2] * it.val[2]) * scale, (this.val[3] * it.val[3]) * scale);
    }

    public Scalar mul(Scalar it) {
        return mul(it, 1.0d);
    }

    public Scalar conj() {
        return new Scalar(this.val[0], -this.val[1], -this.val[2], -this.val[3]);
    }

    public boolean isReal() {
        return this.val[1] == 0.0d && this.val[2] == 0.0d && this.val[3] == 0.0d;
    }

    public int hashCode() {
        return Arrays.hashCode(this.val) + 31;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Scalar)) {
            return false;
        }
        if (Arrays.equals(this.val, ((Scalar) obj).val)) {
            return true;
        }
        return false;
    }

    public String toString() {
        return "[" + this.val[0] + ", " + this.val[1] + ", " + this.val[2] + ", " + this.val[3] + "]";
    }
}
