package org.apache.poi.ss.format;

public class SimpleFraction {
    private final int denominator;
    private final int numerator;

    public static SimpleFraction buildFractionExactDenominator(double val, int exactDenom) {
        return new SimpleFraction((int) Math.round(((double) exactDenom) * val), exactDenom);
    }

    public static SimpleFraction buildFractionMaxDenominator(double value, int maxDenominator) {
        return buildFractionMaxDenominator(value, 0.0d, maxDenominator, 100);
    }

    private static SimpleFraction buildFractionMaxDenominator(double value, double epsilon, int maxDenominator, int maxIterations) {
        double r0 = value;
        long a0 = (long) Math.floor(r0);
        if (a0 > 2147483647L) {
            throw new IllegalArgumentException("Overflow trying to convert " + value + " to fraction (" + a0 + "/" + 1 + ")");
        } else if (Math.abs(((double) a0) - value) < epsilon) {
            return new SimpleFraction((int) a0, 1);
        } else {
            long p2;
            long q2;
            long p0 = 1;
            long q0 = 0;
            long p1 = a0;
            long q1 = 1;
            int n = 0;
            boolean stop = false;
            do {
                n++;
                double r1 = 1.0d / (r0 - ((double) a0));
                long a1 = (long) Math.floor(r1);
                p2 = (a1 * p1) + p0;
                q2 = (a1 * q1) + q0;
                if (epsilon == 0.0d && maxDenominator > 0 && Math.abs(q2) > ((long) maxDenominator) && Math.abs(q1) < ((long) maxDenominator)) {
                    return new SimpleFraction((int) p1, (int) q1);
                }
                if (p2 > 2147483647L || q2 > 2147483647L) {
                    throw new RuntimeException("Overflow trying to convert " + value + " to fraction (" + p2 + "/" + q2 + ")");
                }
                double convergent = ((double) p2) / ((double) q2);
                if (n >= maxIterations || Math.abs(convergent - value) <= epsilon || q2 >= ((long) maxDenominator)) {
                    stop = true;
                    continue;
                } else {
                    p0 = p1;
                    p1 = p2;
                    q0 = q1;
                    q1 = q2;
                    a0 = a1;
                    r0 = r1;
                    continue;
                }
            } while (!stop);
            if (n >= maxIterations) {
                throw new RuntimeException("Unable to convert " + value + " to fraction after " + maxIterations + " iterations");
            } else if (q2 < ((long) maxDenominator)) {
                return new SimpleFraction((int) p2, (int) q2);
            } else {
                return new SimpleFraction((int) p1, (int) q1);
            }
        }
    }

    public SimpleFraction(int numerator, int denominator) {
        this.numerator = numerator;
        this.denominator = denominator;
    }

    public int getDenominator() {
        return this.denominator;
    }

    public int getNumerator() {
        return this.numerator;
    }
}
