package org.apache.poi.hssf.record.common;

import org.apache.poi.util.HexDump;
import org.apache.poi.util.LittleEndianInput;
import org.apache.poi.util.LittleEndianOutput;

public final class ExtendedColor implements Cloneable {
    public static final int THEME_ACCENT_1 = 4;
    public static final int THEME_ACCENT_2 = 5;
    public static final int THEME_ACCENT_3 = 6;
    public static final int THEME_ACCENT_4 = 7;
    public static final int THEME_ACCENT_5 = 8;
    public static final int THEME_ACCENT_6 = 9;
    public static final int THEME_DARK_1 = 0;
    public static final int THEME_DARK_2 = 2;
    public static final int THEME_FOLLOWED_HYPERLINK = 11;
    public static final int THEME_HYPERLINK = 10;
    public static final int THEME_LIGHT_1 = 1;
    public static final int THEME_LIGHT_2 = 3;
    public static final int TYPE_AUTO = 0;
    public static final int TYPE_INDEXED = 1;
    public static final int TYPE_RGB = 2;
    public static final int TYPE_THEMED = 3;
    public static final int TYPE_UNSET = 4;
    private int colorIndex;
    private byte[] rgba;
    private int themeIndex;
    private double tint;
    private int type;

    public ExtendedColor() {
        this.type = 1;
        this.colorIndex = 0;
        this.tint = 0.0d;
    }

    public ExtendedColor(LittleEndianInput in) {
        this.type = in.readInt();
        if (this.type == 1) {
            this.colorIndex = in.readInt();
        } else if (this.type == 2) {
            this.rgba = new byte[4];
            in.readFully(this.rgba);
        } else if (this.type == 3) {
            this.themeIndex = in.readInt();
        } else {
            in.readInt();
        }
        this.tint = in.readDouble();
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getColorIndex() {
        return this.colorIndex;
    }

    public void setColorIndex(int colorIndex) {
        this.colorIndex = colorIndex;
    }

    public byte[] getRGBA() {
        return this.rgba;
    }

    public void setRGBA(byte[] rgba) {
        this.rgba = rgba == null ? null : (byte[]) rgba.clone();
    }

    public int getThemeIndex() {
        return this.themeIndex;
    }

    public void setThemeIndex(int themeIndex) {
        this.themeIndex = themeIndex;
    }

    public double getTint() {
        return this.tint;
    }

    public void setTint(double tint) {
        if (tint < -1.0d || tint > 1.0d) {
            throw new IllegalArgumentException("Tint/Shade must be between -1 and +1");
        }
        this.tint = tint;
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("    [Extended Color]\n");
        buffer.append("          .type  = ").append(this.type).append("\n");
        buffer.append("          .tint  = ").append(this.tint).append("\n");
        buffer.append("          .c_idx = ").append(this.colorIndex).append("\n");
        buffer.append("          .rgba  = ").append(HexDump.toHex(this.rgba)).append("\n");
        buffer.append("          .t_idx = ").append(this.themeIndex).append("\n");
        buffer.append("    [/Extended Color]\n");
        return buffer.toString();
    }

    public ExtendedColor clone() {
        ExtendedColor exc = new ExtendedColor();
        exc.type = this.type;
        exc.tint = this.tint;
        if (this.type == 1) {
            exc.colorIndex = this.colorIndex;
        } else if (this.type == 2) {
            exc.rgba = new byte[4];
            System.arraycopy(this.rgba, 0, exc.rgba, 0, 4);
        } else if (this.type == 3) {
            exc.themeIndex = this.themeIndex;
        }
        return exc;
    }

    public int getDataLength() {
        return 16;
    }

    public void serialize(LittleEndianOutput out) {
        out.writeInt(this.type);
        if (this.type == 1) {
            out.writeInt(this.colorIndex);
        } else if (this.type == 2) {
            out.write(this.rgba);
        } else if (this.type == 3) {
            out.writeInt(this.themeIndex);
        } else {
            out.writeInt(0);
        }
        out.writeDouble(this.tint);
    }
}
