package com.itextpdf.text.pdf;

public class CMYKColor extends ExtendedColor {
    private static final long serialVersionUID = 5940378778276468452L;
    float black;
    float cyan;
    float magenta;
    float yellow;

    public CMYKColor(int intCyan, int intMagenta, int intYellow, int intBlack) {
        this(((float) intCyan) / 255.0f, ((float) intMagenta) / 255.0f, ((float) intYellow) / 255.0f, ((float) intBlack) / 255.0f);
    }

    public CMYKColor(float floatCyan, float floatMagenta, float floatYellow, float floatBlack) {
        super(2, (BaseField.BORDER_WIDTH_THIN - floatCyan) - floatBlack, (BaseField.BORDER_WIDTH_THIN - floatMagenta) - floatBlack, (BaseField.BORDER_WIDTH_THIN - floatYellow) - floatBlack);
        this.cyan = ExtendedColor.normalize(floatCyan);
        this.magenta = ExtendedColor.normalize(floatMagenta);
        this.yellow = ExtendedColor.normalize(floatYellow);
        this.black = ExtendedColor.normalize(floatBlack);
    }

    public float getCyan() {
        return this.cyan;
    }

    public float getMagenta() {
        return this.magenta;
    }

    public float getYellow() {
        return this.yellow;
    }

    public float getBlack() {
        return this.black;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof CMYKColor)) {
            return false;
        }
        CMYKColor c2 = (CMYKColor) obj;
        if (this.cyan == c2.cyan && this.magenta == c2.magenta && this.yellow == c2.yellow && this.black == c2.black) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return ((Float.floatToIntBits(this.cyan) ^ Float.floatToIntBits(this.magenta)) ^ Float.floatToIntBits(this.yellow)) ^ Float.floatToIntBits(this.black);
    }
}
