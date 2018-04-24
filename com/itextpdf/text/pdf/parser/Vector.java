package com.itextpdf.text.pdf.parser;

import java.util.Arrays;

public class Vector {
    public static final int I1 = 0;
    public static final int I2 = 1;
    public static final int I3 = 2;
    private final float[] vals = new float[]{0.0f, 0.0f, 0.0f};

    public Vector(float x, float y, float z) {
        this.vals[0] = x;
        this.vals[1] = y;
        this.vals[2] = z;
    }

    public float get(int index) {
        return this.vals[index];
    }

    public Vector cross(Matrix by) {
        return new Vector(((this.vals[0] * by.get(0)) + (this.vals[1] * by.get(3))) + (this.vals[2] * by.get(6)), ((this.vals[0] * by.get(1)) + (this.vals[1] * by.get(4))) + (this.vals[2] * by.get(7)), ((this.vals[0] * by.get(2)) + (this.vals[1] * by.get(5))) + (this.vals[2] * by.get(8)));
    }

    public Vector subtract(Vector v) {
        return new Vector(this.vals[0] - v.vals[0], this.vals[1] - v.vals[1], this.vals[2] - v.vals[2]);
    }

    public Vector cross(Vector with) {
        return new Vector((this.vals[1] * with.vals[2]) - (this.vals[2] * with.vals[1]), (this.vals[2] * with.vals[0]) - (this.vals[0] * with.vals[2]), (this.vals[0] * with.vals[1]) - (this.vals[1] * with.vals[0]));
    }

    public Vector normalize() {
        float l = length();
        return new Vector(this.vals[0] / l, this.vals[1] / l, this.vals[2] / l);
    }

    public Vector multiply(float by) {
        return new Vector(this.vals[0] * by, this.vals[1] * by, this.vals[2] * by);
    }

    public float dot(Vector with) {
        return ((this.vals[0] * with.vals[0]) + (this.vals[1] * with.vals[1])) + (this.vals[2] * with.vals[2]);
    }

    public float length() {
        return (float) Math.sqrt((double) lengthSquared());
    }

    public float lengthSquared() {
        return ((this.vals[0] * this.vals[0]) + (this.vals[1] * this.vals[1])) + (this.vals[2] * this.vals[2]);
    }

    public String toString() {
        return this.vals[0] + "," + this.vals[1] + "," + this.vals[2];
    }

    public int hashCode() {
        return Arrays.hashCode(this.vals) + 31;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        if (Arrays.equals(this.vals, ((Vector) obj).vals)) {
            return true;
        }
        return false;
    }
}
