package com.lee.ultrascan.library;

import com.itextpdf.text.html.HtmlUtilities;

public enum DepthType {
    D_10CM(10.0f, (short) 432, (short) 258),
    D_12CM(HtmlUtilities.DEFAULT_FONT_SIZE, (short) 390, (short) 259),
    D_14CM(14.0f, (short) 454, (short) 259),
    D_16CM(16.0f, ProbeParams.RX_GATE_SIZE_16CM, (short) 260),
    D_18CM(18.0f, (short) 468, (short) 260);
    
    private final short deciRate;
    private final short gateSize;
    private final float value;

    private DepthType(float value, short gateSize, short deciRate) {
        this.value = value;
        this.gateSize = gateSize;
        this.deciRate = deciRate;
    }

    public DepthType getNext() {
        switch (this) {
            case D_10CM:
                return D_12CM;
            case D_12CM:
                return D_14CM;
            case D_14CM:
                return D_16CM;
            case D_16CM:
                return D_18CM;
            case D_18CM:
                return D_18CM;
            default:
                throw new RuntimeException("unknown depth type.");
        }
    }

    public DepthType getPrev() {
        switch (this) {
            case D_10CM:
                return D_10CM;
            case D_12CM:
                return D_10CM;
            case D_14CM:
                return D_12CM;
            case D_16CM:
                return D_14CM;
            case D_18CM:
                return D_16CM;
            default:
                throw new RuntimeException("unknown depth type.");
        }
    }

    public float getValueInCM() {
        return this.value;
    }

    public short getGateSize() {
        return this.gateSize;
    }

    public short getDeciRate() {
        return this.deciRate;
    }
}
