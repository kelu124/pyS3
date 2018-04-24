package com.itextpdf.text.pdf;

public class GrayColor extends ExtendedColor {
    public static final GrayColor GRAYBLACK = new GrayColor(0.0f);
    public static final GrayColor GRAYWHITE = new GrayColor((float) BaseField.BORDER_WIDTH_THIN);
    private static final long serialVersionUID = -6571835680819282746L;
    private float gray;

    public GrayColor(int intGray) {
        this(((float) intGray) / 255.0f);
    }

    public GrayColor(float floatGray) {
        super(1, floatGray, floatGray, floatGray);
        this.gray = ExtendedColor.normalize(floatGray);
    }

    public float getGray() {
        return this.gray;
    }

    public boolean equals(Object obj) {
        return (obj instanceof GrayColor) && ((GrayColor) obj).gray == this.gray;
    }

    public int hashCode() {
        return Float.floatToIntBits(this.gray);
    }
}
