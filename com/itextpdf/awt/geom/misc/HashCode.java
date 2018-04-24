package com.itextpdf.awt.geom.misc;

public final class HashCode {
    public static final int EMPTY_HASH_CODE = 1;
    private int hashCode = 1;

    public final int hashCode() {
        return this.hashCode;
    }

    public static int combine(int hashCode, boolean value) {
        return combine(hashCode, value ? 1231 : 1237);
    }

    public static int combine(int hashCode, long value) {
        return combine(hashCode, (int) ((value >>> 32) ^ value));
    }

    public static int combine(int hashCode, float value) {
        return combine(hashCode, Float.floatToIntBits(value));
    }

    public static int combine(int hashCode, double value) {
        return combine(hashCode, Double.doubleToLongBits(value));
    }

    public static int combine(int hashCode, Object value) {
        return combine(hashCode, value.hashCode());
    }

    public static int combine(int hashCode, int value) {
        return (hashCode * 31) + value;
    }

    public final HashCode append(int value) {
        this.hashCode = combine(this.hashCode, value);
        return this;
    }

    public final HashCode append(long value) {
        this.hashCode = combine(this.hashCode, value);
        return this;
    }

    public final HashCode append(float value) {
        this.hashCode = combine(this.hashCode, value);
        return this;
    }

    public final HashCode append(double value) {
        this.hashCode = combine(this.hashCode, value);
        return this;
    }

    public final HashCode append(boolean value) {
        this.hashCode = combine(this.hashCode, value);
        return this;
    }

    public final HashCode append(Object value) {
        this.hashCode = combine(this.hashCode, value);
        return this;
    }
}
