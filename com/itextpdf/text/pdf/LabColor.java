package com.itextpdf.text.pdf;

import com.itextpdf.text.BaseColor;

public class LabColor extends ExtendedColor {
    private float f114a;
    private float f115b;
    private float f116l;
    PdfLabColor labColorSpace;

    public LabColor(PdfLabColor labColorSpace, float l, float a, float b) {
        super(7);
        this.labColorSpace = labColorSpace;
        this.f116l = l;
        this.f114a = a;
        this.f115b = b;
        BaseColor altRgbColor = labColorSpace.lab2Rgb(l, a, b);
        setValue(altRgbColor.getRed(), altRgbColor.getGreen(), altRgbColor.getBlue(), 255);
    }

    public PdfLabColor getLabColorSpace() {
        return this.labColorSpace;
    }

    public float getL() {
        return this.f116l;
    }

    public float getA() {
        return this.f114a;
    }

    public float getB() {
        return this.f115b;
    }

    public BaseColor toRgb() {
        return this.labColorSpace.lab2Rgb(this.f116l, this.f114a, this.f115b);
    }

    CMYKColor toCmyk() {
        return this.labColorSpace.lab2Cmyk(this.f116l, this.f114a, this.f115b);
    }
}
