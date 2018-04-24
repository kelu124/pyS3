package org.apache.poi.ss.formula.functions;

public final class FinanceLib {
    private FinanceLib() {
    }

    public static double fv(double r, double n, double y, double p, boolean t) {
        if (r == 0.0d) {
            return -1.0d * ((n * y) + p);
        }
        double r1 = r + 1.0d;
        return ((((t ? r1 : 1.0d) * (1.0d - Math.pow(r1, n))) * y) / r) - (Math.pow(r1, n) * p);
    }

    public static double pv(double r, double n, double y, double f, boolean t) {
        if (r == 0.0d) {
            return -1.0d * ((n * y) + f);
        }
        double r1 = r + 1.0d;
        return ((((t ? r1 : 1.0d) * ((1.0d - Math.pow(r1, n)) / r)) * y) - f) / Math.pow(r1, n);
    }

    public static double npv(double r, double[] cfs) {
        double npv = 0.0d;
        double r1 = r + 1.0d;
        double trate = r1;
        for (double d : cfs) {
            npv += d / trate;
            trate *= r1;
        }
        return npv;
    }

    public static double pmt(double r, double n, double p, double f, boolean t) {
        if (r == 0.0d) {
            return (-1.0d * (f + p)) / n;
        }
        double r1 = r + 1.0d;
        return (((Math.pow(r1, n) * p) + f) * r) / ((t ? r1 : 1.0d) * (1.0d - Math.pow(r1, n)));
    }

    public static double nper(double r, double y, double p, double f, boolean t) {
        if (r == 0.0d) {
            return (-1.0d * (f + p)) / y;
        }
        double r1 = r + 1.0d;
        double ryr = ((t ? r1 : 1.0d) * y) / r;
        return ((ryr - f < 0.0d ? Math.log(f - ryr) : Math.log(ryr - f)) - (ryr - f < 0.0d ? Math.log((-p) - ryr) : Math.log(p + ryr))) / Math.log(r1);
    }
}
