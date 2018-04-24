package com.itextpdf.text.pdf;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.error_messages.MessageLocalization;
import java.util.Arrays;

public class PdfLabColor implements ICachedColorSpace {
    float[] blackPoint;
    float[] range;
    float[] whitePoint;

    public PdfLabColor() {
        this.whitePoint = new float[]{0.9505f, BaseField.BORDER_WIDTH_THIN, 1.089f};
        this.blackPoint = null;
        this.range = null;
    }

    public PdfLabColor(float[] whitePoint) {
        this.whitePoint = new float[]{0.9505f, BaseField.BORDER_WIDTH_THIN, 1.089f};
        this.blackPoint = null;
        this.range = null;
        if (whitePoint == null || whitePoint.length != 3 || whitePoint[0] < 1.0E-6f || whitePoint[2] < 1.0E-6f || whitePoint[1] < 0.999999f || whitePoint[1] > 1.000001f) {
            throw new RuntimeException(MessageLocalization.getComposedMessage("lab.cs.white.point", new Object[0]));
        }
        this.whitePoint = whitePoint;
    }

    public PdfLabColor(float[] whitePoint, float[] blackPoint) {
        this(whitePoint);
        this.blackPoint = blackPoint;
    }

    public PdfLabColor(float[] whitePoint, float[] blackPoint, float[] range) {
        this(whitePoint, blackPoint);
        this.range = range;
    }

    public PdfObject getPdfObject(PdfWriter writer) {
        PdfArray array = new PdfArray(PdfName.LAB);
        PdfDictionary dictionary = new PdfDictionary();
        if (this.whitePoint == null || this.whitePoint.length != 3 || this.whitePoint[0] < 1.0E-6f || this.whitePoint[2] < 1.0E-6f || this.whitePoint[1] < 0.999999f || this.whitePoint[1] > 1.000001f) {
            throw new RuntimeException(MessageLocalization.getComposedMessage("lab.cs.white.point", new Object[0]));
        }
        dictionary.put(PdfName.WHITEPOINT, new PdfArray(this.whitePoint));
        if (this.blackPoint != null) {
            if (this.blackPoint.length != 3 || this.blackPoint[0] < -1.0E-6f || this.blackPoint[1] < -1.0E-6f || this.blackPoint[2] < -1.0E-6f) {
                throw new RuntimeException(MessageLocalization.getComposedMessage("lab.cs.black.point", new Object[0]));
            }
            dictionary.put(PdfName.BLACKPOINT, new PdfArray(this.blackPoint));
        }
        if (this.range != null) {
            if (this.range.length != 4 || this.range[0] > this.range[1] || this.range[2] > this.range[3]) {
                throw new RuntimeException(MessageLocalization.getComposedMessage("lab.cs.range", new Object[0]));
            }
            dictionary.put(PdfName.RANGE, new PdfArray(this.range));
        }
        array.add(dictionary);
        return array;
    }

    public BaseColor lab2Rgb(float l, float a, float b) {
        double[] clinear = lab2RgbLinear(l, a, b);
        return new BaseColor((float) clinear[0], (float) clinear[1], (float) clinear[2]);
    }

    CMYKColor lab2Cmyk(float l, float a, float b) {
        double computedK;
        double[] clinear = lab2RgbLinear(l, a, b);
        double r = clinear[0];
        double g = clinear[1];
        double bee = clinear[2];
        double computedC = 0.0d;
        double computedM = 0.0d;
        double computedY = 0.0d;
        if (r == 0.0d && g == 0.0d && b == 0.0f) {
            computedK = 1.0d;
        } else {
            computedC = 1.0d - r;
            computedM = 1.0d - g;
            computedY = 1.0d - bee;
            double minCMY = Math.min(computedC, Math.min(computedM, computedY));
            computedC = (computedC - minCMY) / (1.0d - minCMY);
            computedM = (computedM - minCMY) / (1.0d - minCMY);
            computedY = (computedY - minCMY) / (1.0d - minCMY);
            computedK = minCMY;
        }
        return new CMYKColor((float) computedC, (float) computedM, (float) computedY, (float) computedK);
    }

    protected double[] lab2RgbLinear(float l, float a, float b) {
        if (this.range != null && this.range.length == 4) {
            if (a < this.range[0]) {
                a = this.range[0];
            }
            if (a > this.range[1]) {
                a = this.range[1];
            }
            if (b < this.range[2]) {
                b = this.range[2];
            }
            if (b > this.range[3]) {
                b = this.range[3];
            }
        }
        double fy = ((double) (16.0f + l)) / 116.0d;
        double fx = fy + (((double) a) / 500.0d);
        double fz = fy - (((double) b) / 200.0d);
        double x = fx > 0.20689655172413793d ? ((double) this.whitePoint[0]) * ((fx * fx) * fx) : (((fx - 0.13793103448275862d) * 3.0d) * (0.20689655172413793d * 0.20689655172413793d)) * ((double) this.whitePoint[0]);
        double y = fy > 0.20689655172413793d ? ((double) this.whitePoint[1]) * ((fy * fy) * fy) : (((fy - 0.13793103448275862d) * 3.0d) * (0.20689655172413793d * 0.20689655172413793d)) * ((double) this.whitePoint[1]);
        double z = fz > 0.20689655172413793d ? ((double) this.whitePoint[2]) * ((fz * fz) * fz) : (((fz - 0.13793103448275862d) * 3.0d) * (0.20689655172413793d * 0.20689655172413793d)) * ((double) this.whitePoint[2]);
        double[] clinear = new double[]{((3.241d * x) - (1.5374d * y)) - (0.4986d * z), (((-x) * 0.9692d) + (1.876d * y)) - (0.0416d * z), ((0.0556d * x) - (0.204d * y)) + (1.057d * z)};
        for (int i = 0; i < 3; i++) {
            clinear[i] = clinear[i] <= 0.0031308d ? 12.92d * clinear[i] : (1.055d * Math.pow(clinear[i], 0.4166666666666667d)) - 0.055d;
            if (clinear[i] < 0.0d) {
                clinear[i] = 0.0d;
            } else if (clinear[i] > 1.0d) {
                clinear[i] = 1.0d;
            }
        }
        return clinear;
    }

    public LabColor rgb2lab(BaseColor baseColor) {
        double rLinear = (double) (((float) baseColor.getRed()) / 255.0f);
        double gLinear = (double) (((float) baseColor.getGreen()) / 255.0f);
        double bLinear = (double) (((float) baseColor.getBlue()) / 255.0f);
        double r = rLinear > 0.04045d ? Math.pow((0.055d + rLinear) / 1.055d, 2.2d) : rLinear / 12.92d;
        double g = gLinear > 0.04045d ? Math.pow((0.055d + gLinear) / 1.055d, 2.2d) : gLinear / 12.92d;
        double b = bLinear > 0.04045d ? Math.pow((0.055d + bLinear) / 1.055d, 2.2d) : bLinear / 12.92d;
        double y = ((0.2126d * r) + (0.7152d * g)) + (0.0722d * b);
        return new LabColor(this, ((float) Math.round(((116.0d * fXyz(y / ((double) this.whitePoint[1]))) - 16.0d) * 1000.0d)) / 1000.0f, ((float) Math.round((500.0d * (fXyz((((0.4124d * r) + (0.3576d * g)) + (0.1805d * b)) / ((double) this.whitePoint[0])) - fXyz(y / ((double) this.whitePoint[1])))) * 1000.0d)) / 1000.0f, ((float) Math.round((200.0d * (fXyz(y / ((double) this.whitePoint[1])) - fXyz((((0.0193d * r) + (0.1192d * g)) + (0.9505d * b)) / ((double) this.whitePoint[2])))) * 1000.0d)) / 1000.0f);
    }

    private static double fXyz(double t) {
        return t > 0.008856d ? Math.pow(t, 0.3333333333333333d) : (7.787d * t) + 0.13793103448275862d;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PdfLabColor)) {
            return false;
        }
        PdfLabColor that = (PdfLabColor) o;
        if (!Arrays.equals(this.blackPoint, that.blackPoint)) {
            return false;
        }
        if (!Arrays.equals(this.range, that.range)) {
            return false;
        }
        if (Arrays.equals(this.whitePoint, that.whitePoint)) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        int hashCode;
        int i = 0;
        int hashCode2 = Arrays.hashCode(this.whitePoint) * 31;
        if (this.blackPoint != null) {
            hashCode = Arrays.hashCode(this.blackPoint);
        } else {
            hashCode = 0;
        }
        hashCode = (hashCode2 + hashCode) * 31;
        if (this.range != null) {
            i = Arrays.hashCode(this.range);
        }
        return hashCode + i;
    }
}
