package org.apache.poi.ss.formula.functions;

public class Finance {
    public static double pmt(double r, int nper, double pv, double fv, int type) {
        return ((-r) * ((Math.pow(1.0d + r, (double) nper) * pv) + fv)) / ((1.0d + (((double) type) * r)) * (Math.pow(1.0d + r, (double) nper) - 1.0d));
    }

    public static double pmt(double r, int nper, double pv, double fv) {
        return pmt(r, nper, pv, fv, 0);
    }

    public static double pmt(double r, int nper, double pv) {
        return pmt(r, nper, pv, 0.0d);
    }

    public static double ipmt(double r, int per, int nper, double pv, double fv, int type) {
        double ipmt = fv(r, per - 1, pmt(r, nper, pv, fv, type), pv, type) * r;
        if (type == 1) {
            return ipmt / (1.0d + r);
        }
        return ipmt;
    }

    public static double ipmt(double r, int per, int nper, double pv, double fv) {
        return ipmt(r, per, nper, pv, fv, 0);
    }

    public static double ipmt(double r, int per, int nper, double pv) {
        return ipmt(r, per, nper, pv, 0.0d);
    }

    public static double ppmt(double r, int per, int nper, double pv, double fv, int type) {
        return pmt(r, nper, pv, fv, type) - ipmt(r, per, nper, pv, fv, type);
    }

    public static double ppmt(double r, int per, int nper, double pv, double fv) {
        return pmt(r, nper, pv, fv) - ipmt(r, per, nper, pv, fv);
    }

    public static double ppmt(double r, int per, int nper, double pv) {
        return pmt(r, nper, pv) - ipmt(r, per, nper, pv);
    }

    public static double fv(double r, int nper, double pmt, double pv, int type) {
        return -((Math.pow(1.0d + r, (double) nper) * pv) + ((((1.0d + (((double) type) * r)) * pmt) * (Math.pow(1.0d + r, (double) nper) - 1.0d)) / r));
    }

    public static double fv(double r, int nper, double c, double pv) {
        return fv(r, nper, c, pv, 0);
    }
}
