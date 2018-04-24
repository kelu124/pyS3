package org.apache.poi.ss.util;

final class IEEEDouble {
    public static final int BIASED_EXPONENT_SPECIAL_VALUE = 2047;
    public static final int EXPONENT_BIAS = 1023;
    private static final long EXPONENT_MASK = 9218868437227405312L;
    private static final int EXPONENT_SHIFT = 52;
    public static final long FRAC_ASSUMED_HIGH_BIT = 4503599627370496L;
    public static final long FRAC_MASK = 4503599627370495L;

    IEEEDouble() {
    }

    public static int getBiasedExponent(long rawBits) {
        return (int) ((EXPONENT_MASK & rawBits) >> 52);
    }
}
