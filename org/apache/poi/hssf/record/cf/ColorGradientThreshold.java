package org.apache.poi.hssf.record.cf;

import org.apache.poi.util.LittleEndianInput;
import org.apache.poi.util.LittleEndianOutput;

public final class ColorGradientThreshold extends Threshold implements Cloneable {
    private double position;

    public ColorGradientThreshold() {
        this.position = 0.0d;
    }

    public ColorGradientThreshold(LittleEndianInput in) {
        super(in);
        this.position = in.readDouble();
    }

    public double getPosition() {
        return this.position;
    }

    public void setPosition(double position) {
        this.position = position;
    }

    public int getDataLength() {
        return super.getDataLength() + 8;
    }

    public ColorGradientThreshold clone() {
        ColorGradientThreshold rec = new ColorGradientThreshold();
        super.copyTo(rec);
        rec.position = this.position;
        return rec;
    }

    public void serialize(LittleEndianOutput out) {
        super.serialize(out);
        out.writeDouble(this.position);
    }
}
