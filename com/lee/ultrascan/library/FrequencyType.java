package com.lee.ultrascan.library;

import com.lee.ultrascan.model.TgcCurve;

public enum FrequencyType {
    F_3_5MHz(3.5f, TgcCurve.TGC_CURVE_3_5MHz, (short) 12),
    F_5_0MHz(5.0f, TgcCurve.TGC_CURVE_5_0MHz, (short) 8);
    
    private final TgcCurve tgcCurve;
    private final short txHalfPeriod;
    private final float value;

    private FrequencyType(float value, TgcCurve tgcCurve, short txHalfPeriod) {
        this.value = value;
        this.tgcCurve = tgcCurve;
        this.txHalfPeriod = txHalfPeriod;
    }

    public float getValue() {
        return this.value;
    }

    public TgcCurve getTgcCurve() {
        return this.tgcCurve;
    }

    public short getTxHalfPeriod() {
        return this.txHalfPeriod;
    }
}
