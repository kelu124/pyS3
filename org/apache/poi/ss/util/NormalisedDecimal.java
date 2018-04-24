package org.apache.poi.ss.util;

import java.math.BigDecimal;
import java.math.BigInteger;

final class NormalisedDecimal {
    private static final BigDecimal BD_2_POW_24 = new BigDecimal(BigInteger.ONE.shiftLeft(24));
    private static final int C_2_POW_19 = 524288;
    private static final int EXPONENT_OFFSET = 14;
    private static final int FRAC_HALF = 8388608;
    private static final int LOG_BASE_10_OF_2_TIMES_2_POW_20 = 315653;
    private static final long MAX_REP_WHOLE_PART = 1000000000000000L;
    private final int _fractionalPart;
    private final int _relativeDecimalExponent;
    private final long _wholePart;

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static org.apache.poi.ss.util.NormalisedDecimal create(java.math.BigInteger r6, int r7) {
        /*
        r3 = 49;
        if (r7 > r3) goto L_0x0008;
    L_0x0004:
        r3 = 46;
        if (r7 >= r3) goto L_0x004b;
    L_0x0008:
        r3 = 15204352; // 0xe80000 float:2.1305835E-38 double:7.511948E-317;
        r4 = 315653; // 0x4d105 float:4.42324E-40 double:1.559533E-318;
        r4 = r4 * r7;
        r2 = r3 - r4;
        r3 = 524288; // 0x80000 float:7.34684E-40 double:2.590327E-318;
        r2 = r2 + r3;
        r3 = r2 >> 20;
        r1 = -r3;
    L_0x0016:
        r0 = new org.apache.poi.ss.util.MutableFPNumber;
        r0.<init>(r6, r7);
        if (r1 == 0) goto L_0x0021;
    L_0x001d:
        r3 = -r1;
        r0.multiplyByPowerOfTen(r3);
    L_0x0021:
        r3 = r0.get64BitNormalisedExponent();
        switch(r3) {
            case 44: goto L_0x005b;
            case 45: goto L_0x005b;
            case 46: goto L_0x004d;
            case 47: goto L_0x0053;
            case 48: goto L_0x0053;
            case 49: goto L_0x0062;
            case 50: goto L_0x0068;
            default: goto L_0x0028;
        };
    L_0x0028:
        r3 = new java.lang.IllegalStateException;
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r5 = "Bad binary exp ";
        r4 = r4.append(r5);
        r5 = r0.get64BitNormalisedExponent();
        r4 = r4.append(r5);
        r5 = ".";
        r4 = r4.append(r5);
        r4 = r4.toString();
        r3.<init>(r4);
        throw r3;
    L_0x004b:
        r1 = 0;
        goto L_0x0016;
    L_0x004d:
        r3 = r0.isAboveMinRep();
        if (r3 == 0) goto L_0x005b;
    L_0x0053:
        r0.normalise64bit();
        r3 = r0.createNormalisedDecimal(r1);
        return r3;
    L_0x005b:
        r3 = 1;
        r0.multiplyByPowerOfTen(r3);
        r1 = r1 + -1;
        goto L_0x0053;
    L_0x0062:
        r3 = r0.isBelowMaxRep();
        if (r3 != 0) goto L_0x0053;
    L_0x0068:
        r3 = -1;
        r0.multiplyByPowerOfTen(r3);
        r1 = r1 + 1;
        goto L_0x0053;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.poi.ss.util.NormalisedDecimal.create(java.math.BigInteger, int):org.apache.poi.ss.util.NormalisedDecimal");
    }

    public NormalisedDecimal roundUnits() {
        long wholePart = this._wholePart;
        if (this._fractionalPart >= 8388608) {
            wholePart++;
        }
        int de = this._relativeDecimalExponent;
        if (wholePart < MAX_REP_WHOLE_PART) {
            return new NormalisedDecimal(wholePart, 0, de);
        }
        return new NormalisedDecimal(wholePart / 10, 0, de + 1);
    }

    NormalisedDecimal(long wholePart, int fracPart, int decimalExponent) {
        this._wholePart = wholePart;
        this._fractionalPart = fracPart;
        this._relativeDecimalExponent = decimalExponent;
    }

    public ExpandedDouble normaliseBaseTwo() {
        MutableFPNumber cc = new MutableFPNumber(composeFrac(), 39);
        cc.multiplyByPowerOfTen(this._relativeDecimalExponent);
        cc.normalise64bit();
        return cc.createExpandedDouble();
    }

    BigInteger composeFrac() {
        long wp = this._wholePart;
        int fp = this._fractionalPart;
        return new BigInteger(new byte[]{(byte) ((int) (wp >> 56)), (byte) ((int) (wp >> 48)), (byte) ((int) (wp >> 40)), (byte) ((int) (wp >> 32)), (byte) ((int) (wp >> 24)), (byte) ((int) (wp >> 16)), (byte) ((int) (wp >> 8)), (byte) ((int) (wp >> 0)), (byte) (fp >> 16), (byte) (fp >> 8), (byte) (fp >> 0)});
    }

    public String getSignificantDecimalDigits() {
        return Long.toString(this._wholePart);
    }

    public String getSignificantDecimalDigitsLastDigitRounded() {
        long wp = this._wholePart + 5;
        StringBuilder sb = new StringBuilder(24);
        sb.append(wp);
        sb.setCharAt(sb.length() - 1, '0');
        return sb.toString();
    }

    public int getDecimalExponent() {
        return this._relativeDecimalExponent + 14;
    }

    public int compareNormalised(NormalisedDecimal other) {
        int cmp = this._relativeDecimalExponent - other._relativeDecimalExponent;
        if (cmp != 0) {
            return cmp;
        }
        if (this._wholePart > other._wholePart) {
            return 1;
        }
        if (this._wholePart < other._wholePart) {
            return -1;
        }
        return this._fractionalPart - other._fractionalPart;
    }

    public BigDecimal getFractionalPart() {
        return new BigDecimal(this._fractionalPart).divide(BD_2_POW_24);
    }

    private String getFractionalDigits() {
        if (this._fractionalPart == 0) {
            return "0";
        }
        return getFractionalPart().toString().substring(2);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getName());
        sb.append(" [");
        String ws = String.valueOf(this._wholePart);
        sb.append(ws.charAt(0));
        sb.append('.');
        sb.append(ws.substring(1));
        sb.append(' ');
        sb.append(getFractionalDigits());
        sb.append("E");
        sb.append(getDecimalExponent());
        sb.append("]");
        return sb.toString();
    }
}
