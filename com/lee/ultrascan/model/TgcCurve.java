package com.lee.ultrascan.model;

import com.lee.ultrascan.library.ProbeParams;

public enum TgcCurve {
    TGC_CURVE_3_5MHz(ProbeParams.TGC_TABLE_3_5MHZ),
    TGC_CURVE_5_0MHz(ProbeParams.TGC_TABLE_5_0MHZ);
    
    public static final short MAX_TGC_VALUE = (short) 300;
    public static final int TGC_CURVE_SIZE = 935;
    private static short curveOffset;
    private short[] tgc_curve_values;

    static {
        curveOffset = (short) 100;
    }

    private TgcCurve(short[] tgc_curve_values) {
        this.tgc_curve_values = null;
        this.tgc_curve_values = tgc_curve_values;
    }

    public void setOffset(short curveOffset) {
        curveOffset = clampCurveOffset(curveOffset);
    }

    public short getOffset() {
        return curveOffset;
    }

    public short[] getTgcCurveData() {
        short[] newCurve = new short[TGC_CURVE_SIZE];
        for (int i = 0; i < newCurve.length; i++) {
            newCurve[i] = (short) (this.tgc_curve_values[i] + curveOffset);
            newCurve[i] = clampTgcValue(newCurve[i]);
        }
        return newCurve;
    }

    protected short clampCurveOffset(short offset) {
        if (offset > MAX_TGC_VALUE) {
            offset = MAX_TGC_VALUE;
        }
        if (offset < (short) 0) {
            return (short) 0;
        }
        return offset;
    }

    protected short clampTgcValue(short value) {
        if (value > (short) 1023) {
            value = (short) 1023;
        }
        if (value < (short) 0) {
            return (short) 0;
        }
        return value;
    }
}
