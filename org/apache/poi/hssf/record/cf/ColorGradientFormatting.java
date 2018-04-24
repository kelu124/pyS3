package org.apache.poi.hssf.record.cf;

import org.apache.poi.hssf.record.common.ExtendedColor;
import org.apache.poi.util.BitField;
import org.apache.poi.util.BitFieldFactory;
import org.apache.poi.util.LittleEndianInput;
import org.apache.poi.util.LittleEndianOutput;
import org.apache.poi.util.POILogFactory;
import org.apache.poi.util.POILogger;

public final class ColorGradientFormatting implements Cloneable {
    private static BitField background = BitFieldFactory.getInstance(2);
    private static BitField clamp = BitFieldFactory.getInstance(1);
    private static POILogger log = POILogFactory.getLogger(ColorGradientFormatting.class);
    private ExtendedColor[] colors;
    private byte options;
    private ColorGradientThreshold[] thresholds;

    public ColorGradientFormatting() {
        this.options = (byte) 0;
        this.options = (byte) 3;
        this.thresholds = new ColorGradientThreshold[3];
        this.colors = new ExtendedColor[3];
    }

    public ColorGradientFormatting(LittleEndianInput in) {
        int i;
        this.options = (byte) 0;
        in.readShort();
        in.readByte();
        int numI = in.readByte();
        int numG = in.readByte();
        if (numI != numG) {
            log.log(5, new Object[]{"Inconsistent Color Gradient defintion, found " + numI + " vs " + numG + " entries"});
        }
        this.options = in.readByte();
        this.thresholds = new ColorGradientThreshold[numI];
        for (i = 0; i < this.thresholds.length; i++) {
            this.thresholds[i] = new ColorGradientThreshold(in);
        }
        this.colors = new ExtendedColor[numG];
        for (i = 0; i < this.colors.length; i++) {
            in.readDouble();
            this.colors[i] = new ExtendedColor(in);
        }
    }

    public int getNumControlPoints() {
        return this.thresholds.length;
    }

    public void setNumControlPoints(int num) {
        if (num != this.thresholds.length) {
            ColorGradientThreshold[] nt = new ColorGradientThreshold[num];
            ExtendedColor[] nc = new ExtendedColor[num];
            int copy = Math.min(this.thresholds.length, num);
            System.arraycopy(this.thresholds, 0, nt, 0, copy);
            System.arraycopy(this.colors, 0, nc, 0, copy);
            this.thresholds = nt;
            this.colors = nc;
            updateThresholdPositions();
        }
    }

    public ColorGradientThreshold[] getThresholds() {
        return this.thresholds;
    }

    public void setThresholds(ColorGradientThreshold[] thresholds) {
        this.thresholds = thresholds == null ? null : (ColorGradientThreshold[]) thresholds.clone();
        updateThresholdPositions();
    }

    public ExtendedColor[] getColors() {
        return this.colors;
    }

    public void setColors(ExtendedColor[] colors) {
        this.colors = colors == null ? null : (ExtendedColor[]) colors.clone();
    }

    public boolean isClampToCurve() {
        return getOptionFlag(clamp);
    }

    public boolean isAppliesToBackground() {
        return getOptionFlag(background);
    }

    private boolean getOptionFlag(BitField field) {
        return field.getValue(this.options) != 0;
    }

    private void updateThresholdPositions() {
        double step = 1.0d / ((double) (this.thresholds.length - 1));
        for (int i = 0; i < this.thresholds.length; i++) {
            this.thresholds[i].setPosition(((double) i) * step);
        }
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("    [Color Gradient Formatting]\n");
        buffer.append("          .clamp     = ").append(isClampToCurve()).append("\n");
        buffer.append("          .background= ").append(isAppliesToBackground()).append("\n");
        for (Threshold t : this.thresholds) {
            buffer.append(t.toString());
        }
        for (ExtendedColor c : this.colors) {
            buffer.append(c.toString());
        }
        buffer.append("    [/Color Gradient Formatting]\n");
        return buffer.toString();
    }

    public Object clone() {
        ColorGradientFormatting rec = new ColorGradientFormatting();
        rec.options = this.options;
        rec.thresholds = new ColorGradientThreshold[this.thresholds.length];
        rec.colors = new ExtendedColor[this.colors.length];
        System.arraycopy(this.thresholds, 0, rec.thresholds, 0, this.thresholds.length);
        System.arraycopy(this.colors, 0, rec.colors, 0, this.colors.length);
        return rec;
    }

    public int getDataLength() {
        int len = 6;
        for (Threshold t : this.thresholds) {
            len += t.getDataLength();
        }
        for (ExtendedColor c : this.colors) {
            len = (len + c.getDataLength()) + 8;
        }
        return len;
    }

    public void serialize(LittleEndianOutput out) {
        out.writeShort(0);
        out.writeByte(0);
        out.writeByte(this.thresholds.length);
        out.writeByte(this.thresholds.length);
        out.writeByte(this.options);
        for (ColorGradientThreshold t : this.thresholds) {
            t.serialize(out);
        }
        double step = 1.0d / ((double) (this.colors.length - 1));
        for (int i = 0; i < this.colors.length; i++) {
            out.writeDouble(((double) i) * step);
            this.colors[i].serialize(out);
        }
    }
}
