package com.itextpdf.text;

import com.itextpdf.text.error_messages.MessageLocalization;
import com.itextpdf.text.pdf.BaseField;

public class BaseColor {
    public static final BaseColor BLACK = new BaseColor(0, 0, 0);
    public static final BaseColor BLUE = new BaseColor(0, 0, 255);
    public static final BaseColor CYAN = new BaseColor(0, 255, 255);
    public static final BaseColor DARK_GRAY = new BaseColor(64, 64, 64);
    private static final double FACTOR = 0.7d;
    public static final BaseColor GRAY = new BaseColor(128, 128, 128);
    public static final BaseColor GREEN = new BaseColor(0, 255, 0);
    public static final BaseColor LIGHT_GRAY = new BaseColor(192, 192, 192);
    public static final BaseColor MAGENTA = new BaseColor(255, 0, 255);
    public static final BaseColor ORANGE = new BaseColor(255, 200, 0);
    public static final BaseColor PINK = new BaseColor(255, 175, 175);
    public static final BaseColor RED = new BaseColor(255, 0, 0);
    public static final BaseColor WHITE = new BaseColor(255, 255, 255);
    public static final BaseColor YELLOW = new BaseColor(255, 255, 0);
    private int value;

    public BaseColor(int red, int green, int blue, int alpha) {
        setValue(red, green, blue, alpha);
    }

    public BaseColor(int red, int green, int blue) {
        this(red, green, blue, 255);
    }

    public BaseColor(float red, float green, float blue, float alpha) {
        this((int) (((double) (red * 255.0f)) + 0.5d), (int) (((double) (green * 255.0f)) + 0.5d), (int) (((double) (blue * 255.0f)) + 0.5d), (int) (((double) (alpha * 255.0f)) + 0.5d));
    }

    public BaseColor(float red, float green, float blue) {
        this(red, green, blue, (float) BaseField.BORDER_WIDTH_THIN);
    }

    public BaseColor(int argb) {
        this.value = argb;
    }

    public int getRGB() {
        return this.value;
    }

    public int getRed() {
        return (getRGB() >> 16) & 255;
    }

    public int getGreen() {
        return (getRGB() >> 8) & 255;
    }

    public int getBlue() {
        return (getRGB() >> 0) & 255;
    }

    public int getAlpha() {
        return (getRGB() >> 24) & 255;
    }

    public BaseColor brighter() {
        int r = getRed();
        int g = getGreen();
        int b = getBlue();
        if (r == 0 && g == 0 && b == 0) {
            return new BaseColor(3, 3, 3);
        }
        if (r > 0 && r < 3) {
            r = 3;
        }
        if (g > 0 && g < 3) {
            g = 3;
        }
        if (b > 0 && b < 3) {
            b = 3;
        }
        return new BaseColor(Math.min((int) (((double) r) / FACTOR), 255), Math.min((int) (((double) g) / FACTOR), 255), Math.min((int) (((double) b) / FACTOR), 255));
    }

    public BaseColor darker() {
        return new BaseColor(Math.max((int) (((double) getRed()) * FACTOR), 0), Math.max((int) (((double) getGreen()) * FACTOR), 0), Math.max((int) (((double) getBlue()) * FACTOR), 0));
    }

    public boolean equals(Object obj) {
        return (obj instanceof BaseColor) && ((BaseColor) obj).value == this.value;
    }

    public int hashCode() {
        return this.value;
    }

    protected void setValue(int red, int green, int blue, int alpha) {
        validate(red);
        validate(green);
        validate(blue);
        validate(alpha);
        this.value = ((((alpha & 255) << 24) | ((red & 255) << 16)) | ((green & 255) << 8)) | ((blue & 255) << 0);
    }

    private static void validate(int value) {
        if (value < 0 || value > 255) {
            throw new IllegalArgumentException(MessageLocalization.getComposedMessage("color.value.outside.range.0.255", new Object[0]));
        }
    }

    public String toString() {
        return "Color value[" + Integer.toString(this.value, 16) + "]";
    }
}
