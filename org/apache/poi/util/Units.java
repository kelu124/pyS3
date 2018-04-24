package org.apache.poi.util;

public class Units {
    public static final int EMU_PER_CENTIMETER = 360000;
    public static final int EMU_PER_PIXEL = 9525;
    public static final int EMU_PER_POINT = 12700;
    public static final int MASTER_DPI = 576;
    public static final int PIXEL_DPI = 96;
    public static final int POINT_DPI = 72;

    public static int toEMU(double points) {
        return (int) Math.rint(12700.0d * points);
    }

    public static int pixelToEMU(int pixels) {
        return pixels * 9525;
    }

    public static double toPoints(long emu) {
        return ((double) emu) / 12700.0d;
    }

    public static double fixedPointToDouble(int fixedPoint) {
        return ((double) (fixedPoint >> 16)) + (((double) (fixedPoint & 65535)) / 65536.0d);
    }

    public static int doubleToFixedPoint(double floatPoint) {
        double fractionalPart = floatPoint % 1.0d;
        return (((int) Math.floor(floatPoint - fractionalPart)) << 16) | (65535 & ((int) Math.rint(65536.0d * fractionalPart)));
    }

    public static double masterToPoints(int masterDPI) {
        return (((double) masterDPI) * 72.0d) / 576.0d;
    }

    public static int pointsToMaster(double points) {
        return (int) Math.rint((points * 576.0d) / 72.0d);
    }

    public static int pointsToPixel(double points) {
        return (int) Math.rint((points * 96.0d) / 72.0d);
    }

    public static double pixelToPoints(int pixel) {
        return (((double) pixel) * 72.0d) / 96.0d;
    }
}
