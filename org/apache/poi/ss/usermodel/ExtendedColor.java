package org.apache.poi.ss.usermodel;

import java.awt.Color;
import java.util.Locale;
import org.apache.poi.hssf.util.HSSFColor;

public abstract class ExtendedColor implements Color {
    public abstract byte[] getARGB();

    public abstract short getIndex();

    public abstract byte[] getRGB();

    protected abstract byte[] getStoredRBG();

    public abstract int getTheme();

    public abstract double getTint();

    public abstract boolean isAuto();

    public abstract boolean isIndexed();

    public abstract boolean isRGB();

    public abstract boolean isThemed();

    public abstract void setRGB(byte[] bArr);

    public abstract void setTint(double d);

    protected void setColor(Color clr) {
        setRGB(new byte[]{(byte) clr.getRed(), (byte) clr.getGreen(), (byte) clr.getBlue()});
    }

    protected byte[] getRGBOrARGB() {
        if (isIndexed() && getIndex() > (short) 0) {
            if (((HSSFColor) HSSFColor.getIndexHash().get(Integer.valueOf(getIndex()))) != null) {
                return new byte[]{(byte) ((HSSFColor) HSSFColor.getIndexHash().get(Integer.valueOf(getIndex()))).getTriplet()[0], (byte) ((HSSFColor) HSSFColor.getIndexHash().get(Integer.valueOf(getIndex()))).getTriplet()[1], (byte) ((HSSFColor) HSSFColor.getIndexHash().get(Integer.valueOf(getIndex()))).getTriplet()[2]};
            }
        }
        return getStoredRBG();
    }

    public byte[] getRGBWithTint() {
        byte[] rgb = getStoredRBG();
        if (rgb != null) {
            if (rgb.length == 4) {
                byte[] tmp = new byte[3];
                System.arraycopy(rgb, 1, tmp, 0, 3);
                rgb = tmp;
            }
            double tint = getTint();
            for (int i = 0; i < rgb.length; i++) {
                rgb[i] = applyTint(rgb[i] & 255, tint);
            }
        }
        return rgb;
    }

    public String getARGBHex() {
        byte[] rgb = getARGB();
        if (rgb == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (byte c : rgb) {
            String cs = Integer.toHexString(c & 255);
            if (cs.length() == 1) {
                sb.append('0');
            }
            sb.append(cs);
        }
        return sb.toString().toUpperCase(Locale.ROOT);
    }

    public void setARGBHex(String argb) {
        if (argb.length() == 6 || argb.length() == 8) {
            byte[] rgb = new byte[(argb.length() / 2)];
            for (int i = 0; i < rgb.length; i++) {
                rgb[i] = (byte) Integer.parseInt(argb.substring(i * 2, (i + 1) * 2), 16);
            }
            setRGB(rgb);
            return;
        }
        throw new IllegalArgumentException("Must be of the form 112233 or FFEEDDCC");
    }

    private static byte applyTint(int lum, double tint) {
        if (tint > 0.0d) {
            return (byte) ((int) ((((double) lum) * (1.0d - tint)) + (255.0d - ((1.0d - tint) * 255.0d))));
        }
        if (tint < 0.0d) {
            return (byte) ((int) (((double) lum) * (1.0d + tint)));
        }
        return (byte) lum;
    }
}
