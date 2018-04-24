package org.opencv.core;

public class TermCriteria {
    public static final int COUNT = 1;
    public static final int EPS = 2;
    public static final int MAX_ITER = 1;
    public double epsilon;
    public int maxCount;
    public int type;

    public TermCriteria(int type, int maxCount, double epsilon) {
        this.type = type;
        this.maxCount = maxCount;
        this.epsilon = epsilon;
    }

    public TermCriteria() {
        this(0, 0, 0.0d);
    }

    public TermCriteria(double[] vals) {
        set(vals);
    }

    public void set(double[] vals) {
        int i = 0;
        if (vals != null) {
            double d;
            this.type = vals.length > 0 ? (int) vals[0] : 0;
            if (vals.length > 1) {
                i = (int) vals[1];
            }
            this.maxCount = i;
            if (vals.length > 2) {
                d = vals[2];
            } else {
                d = 0.0d;
            }
            this.epsilon = d;
            return;
        }
        this.type = 0;
        this.maxCount = 0;
        this.epsilon = 0.0d;
    }

    public TermCriteria clone() {
        return new TermCriteria(this.type, this.maxCount, this.epsilon);
    }

    public int hashCode() {
        long temp = Double.doubleToLongBits((double) this.type);
        int result = ((int) ((temp >>> 32) ^ temp)) + 31;
        temp = Double.doubleToLongBits((double) this.maxCount);
        result = (result * 31) + ((int) ((temp >>> 32) ^ temp));
        temp = Double.doubleToLongBits(this.epsilon);
        return (result * 31) + ((int) ((temp >>> 32) ^ temp));
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof TermCriteria)) {
            return false;
        }
        TermCriteria it = (TermCriteria) obj;
        if (this.type == it.type && this.maxCount == it.maxCount && this.epsilon == it.epsilon) {
            return true;
        }
        return false;
    }

    public String toString() {
        if (this == null) {
            return "null";
        }
        return "{ type: " + this.type + ", maxCount: " + this.maxCount + ", epsilon: " + this.epsilon + "}";
    }
}
