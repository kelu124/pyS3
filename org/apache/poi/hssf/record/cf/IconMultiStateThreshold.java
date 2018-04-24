package org.apache.poi.hssf.record.cf;

import org.apache.poi.util.LittleEndianInput;
import org.apache.poi.util.LittleEndianOutput;

public final class IconMultiStateThreshold extends Threshold implements Cloneable {
    public static final byte EQUALS_EXCLUDE = (byte) 0;
    public static final byte EQUALS_INCLUDE = (byte) 1;
    private byte equals;

    public IconMultiStateThreshold() {
        this.equals = (byte) 1;
    }

    public IconMultiStateThreshold(LittleEndianInput in) {
        super(in);
        this.equals = in.readByte();
        in.readInt();
    }

    public byte getEquals() {
        return this.equals;
    }

    public void setEquals(byte equals) {
        this.equals = equals;
    }

    public int getDataLength() {
        return super.getDataLength() + 5;
    }

    public IconMultiStateThreshold clone() {
        IconMultiStateThreshold rec = new IconMultiStateThreshold();
        super.copyTo(rec);
        rec.equals = this.equals;
        return rec;
    }

    public void serialize(LittleEndianOutput out) {
        super.serialize(out);
        out.writeByte(this.equals);
        out.writeInt(0);
    }
}
