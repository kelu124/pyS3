package org.apache.poi.hssf.record;

import com.itextpdf.xmp.XMPError;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.util.LittleEndianOutput;

public final class PaletteRecord extends StandardRecord {
    public static final short FIRST_COLOR_INDEX = (short) 8;
    public static final byte STANDARD_PALETTE_SIZE = (byte) 56;
    public static final short sid = (short) 146;
    private final List<PColor> _colors;

    private static final class PColor {
        public static final short ENCODED_SIZE = (short) 4;
        private final int _blue;
        private final int _green;
        private final int _red;

        public PColor(int red, int green, int blue) {
            this._red = red;
            this._green = green;
            this._blue = blue;
        }

        public byte[] getTriplet() {
            return new byte[]{(byte) this._red, (byte) this._green, (byte) this._blue};
        }

        public PColor(RecordInputStream in) {
            this._red = in.readByte();
            this._green = in.readByte();
            this._blue = in.readByte();
            in.readByte();
        }

        public void serialize(LittleEndianOutput out) {
            out.writeByte(this._red);
            out.writeByte(this._green);
            out.writeByte(this._blue);
            out.writeByte(0);
        }

        public String toString() {
            StringBuffer buffer = new StringBuffer();
            buffer.append("  red   = ").append(this._red & 255).append('\n');
            buffer.append("  green = ").append(this._green & 255).append('\n');
            buffer.append("  blue  = ").append(this._blue & 255).append('\n');
            return buffer.toString();
        }
    }

    public PaletteRecord() {
        PColor[] defaultPalette = createDefaultPalette();
        this._colors = new ArrayList(defaultPalette.length);
        for (PColor element : defaultPalette) {
            this._colors.add(element);
        }
    }

    public PaletteRecord(RecordInputStream in) {
        int field_1_numcolors = in.readShort();
        this._colors = new ArrayList(field_1_numcolors);
        for (int k = 0; k < field_1_numcolors; k++) {
            this._colors.add(new PColor(in));
        }
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[PALETTE]\n");
        buffer.append("  numcolors     = ").append(this._colors.size()).append('\n');
        for (int i = 0; i < this._colors.size(); i++) {
            PColor c = (PColor) this._colors.get(i);
            buffer.append("* colornum      = ").append(i).append('\n');
            buffer.append(c.toString());
            buffer.append("/*colornum      = ").append(i).append('\n');
        }
        buffer.append("[/PALETTE]\n");
        return buffer.toString();
    }

    public void serialize(LittleEndianOutput out) {
        out.writeShort(this._colors.size());
        for (int i = 0; i < this._colors.size(); i++) {
            ((PColor) this._colors.get(i)).serialize(out);
        }
    }

    protected int getDataSize() {
        return (this._colors.size() * 4) + 2;
    }

    public short getSid() {
        return (short) 146;
    }

    public byte[] getColor(int byteIndex) {
        int i = byteIndex - 8;
        if (i < 0 || i >= this._colors.size()) {
            return null;
        }
        return ((PColor) this._colors.get(i)).getTriplet();
    }

    public void setColor(short byteIndex, byte red, byte green, byte blue) {
        int i = byteIndex - 8;
        if (i >= 0 && i < 56) {
            while (this._colors.size() <= i) {
                this._colors.add(new PColor(0, 0, 0));
            }
            this._colors.set(i, new PColor(red, green, blue));
        }
    }

    private static PColor[] createDefaultPalette() {
        return new PColor[]{pc(0, 0, 0), pc(255, 255, 255), pc(255, 0, 0), pc(0, 255, 0), pc(0, 0, 255), pc(255, 255, 0), pc(255, 0, 255), pc(0, 255, 255), pc(128, 0, 0), pc(0, 128, 0), pc(0, 0, 128), pc(128, 128, 0), pc(128, 0, 128), pc(0, 128, 128), pc(192, 192, 192), pc(128, 128, 128), pc(153, 153, 255), pc(153, 51, 102), pc(255, 255, XMPError.BADSTREAM), pc(XMPError.BADSTREAM, 255, 255), pc(102, 0, 102), pc(255, 128, 128), pc(0, 102, XMPError.BADSTREAM), pc(XMPError.BADSTREAM, XMPError.BADSTREAM, 255), pc(0, 0, 128), pc(255, 0, 255), pc(255, 255, 0), pc(0, 255, 255), pc(128, 0, 128), pc(128, 0, 0), pc(0, 128, 128), pc(0, 0, 255), pc(0, XMPError.BADSTREAM, 255), pc(XMPError.BADSTREAM, 255, 255), pc(XMPError.BADSTREAM, 255, XMPError.BADSTREAM), pc(255, 255, 153), pc(153, XMPError.BADSTREAM, 255), pc(255, 153, XMPError.BADSTREAM), pc(XMPError.BADSTREAM, 153, 255), pc(255, XMPError.BADSTREAM, 153), pc(51, 102, 255), pc(51, XMPError.BADSTREAM, XMPError.BADSTREAM), pc(153, XMPError.BADSTREAM, 0), pc(255, XMPError.BADSTREAM, 0), pc(255, 153, 0), pc(255, 102, 0), pc(102, 102, 153), pc(150, 150, 150), pc(0, 51, 102), pc(51, 153, 102), pc(0, 51, 0), pc(51, 51, 0), pc(153, 51, 0), pc(153, 51, 102), pc(51, 51, 153), pc(51, 51, 51)};
    }

    private static PColor pc(int r, int g, int b) {
        return new PColor(r, g, b);
    }
}
