package org.apache.poi.hssf.usermodel;

import java.util.Locale;
import org.apache.poi.hssf.record.PaletteRecord;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.hssf.util.HSSFColor.AUTOMATIC;

public final class HSSFPalette {
    private PaletteRecord _palette;

    private static final class CustomColor extends HSSFColor {
        private byte _blue;
        private short _byteOffset;
        private byte _green;
        private byte _red;

        public CustomColor(short byteOffset, byte[] colors) {
            this(byteOffset, colors[0], colors[1], colors[2]);
        }

        private CustomColor(short byteOffset, byte red, byte green, byte blue) {
            this._byteOffset = byteOffset;
            this._red = red;
            this._green = green;
            this._blue = blue;
        }

        public short getIndex() {
            return this._byteOffset;
        }

        public short[] getTriplet() {
            return new short[]{(short) (this._red & 255), (short) (this._green & 255), (short) (this._blue & 255)};
        }

        public String getHexString() {
            StringBuffer sb = new StringBuffer();
            sb.append(getGnumericPart(this._red));
            sb.append(':');
            sb.append(getGnumericPart(this._green));
            sb.append(':');
            sb.append(getGnumericPart(this._blue));
            return sb.toString();
        }

        private String getGnumericPart(byte color) {
            if (color == (byte) 0) {
                return "0";
            }
            int c = color & 255;
            String s = Integer.toHexString(c | (c << 8)).toUpperCase(Locale.ROOT);
            while (s.length() < 4) {
                s = "0" + s;
            }
            return s;
        }
    }

    protected HSSFPalette(PaletteRecord palette) {
        this._palette = palette;
    }

    public HSSFColor getColor(short index) {
        if (index == (short) 64) {
            return AUTOMATIC.getInstance();
        }
        byte[] b = this._palette.getColor(index);
        if (b != null) {
            return new CustomColor(index, b);
        }
        return null;
    }

    public HSSFColor getColor(int index) {
        return getColor((short) index);
    }

    public HSSFColor findColor(byte red, byte green, byte blue) {
        byte[] b = this._palette.getColor(8);
        short i = (short) 8;
        while (b != null) {
            if (b[0] == red && b[1] == green && b[2] == blue) {
                return new CustomColor(i, b);
            }
            i = (short) (i + 1);
            b = this._palette.getColor(i);
        }
        return null;
    }

    public HSSFColor findSimilarColor(byte red, byte green, byte blue) {
        return findSimilarColor(unsignedInt(red), unsignedInt(green), unsignedInt(blue));
    }

    public HSSFColor findSimilarColor(int red, int green, int blue) {
        HSSFColor result = null;
        int minColorDistance = Integer.MAX_VALUE;
        byte[] b = this._palette.getColor(8);
        short i = (short) 8;
        while (b != null) {
            int colorDistance = (Math.abs(red - unsignedInt(b[0])) + Math.abs(green - unsignedInt(b[1]))) + Math.abs(blue - unsignedInt(b[2]));
            if (colorDistance < minColorDistance) {
                minColorDistance = colorDistance;
                result = getColor(i);
            }
            i = (short) (i + 1);
            b = this._palette.getColor(i);
        }
        return result;
    }

    private int unsignedInt(byte b) {
        return b & 255;
    }

    public void setColorAtIndex(short index, byte red, byte green, byte blue) {
        this._palette.setColor(index, red, green, blue);
    }

    public HSSFColor addColor(byte red, byte green, byte blue) {
        byte[] b = this._palette.getColor(8);
        short i = (short) 8;
        while (i < (short) 64) {
            if (b == null) {
                setColorAtIndex(i, red, green, blue);
                return getColor(i);
            }
            i = (short) (i + 1);
            b = this._palette.getColor(i);
        }
        throw new RuntimeException("Could not find free color index");
    }
}
