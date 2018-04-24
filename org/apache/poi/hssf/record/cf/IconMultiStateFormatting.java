package org.apache.poi.hssf.record.cf;

import org.apache.poi.ss.usermodel.IconMultiStateFormatting.IconSet;
import org.apache.poi.util.BitField;
import org.apache.poi.util.BitFieldFactory;
import org.apache.poi.util.LittleEndianInput;
import org.apache.poi.util.LittleEndianOutput;
import org.apache.poi.util.POILogFactory;
import org.apache.poi.util.POILogger;

public final class IconMultiStateFormatting implements Cloneable {
    private static BitField iconOnly = BitFieldFactory.getInstance(1);
    private static POILogger log = POILogFactory.getLogger(IconMultiStateFormatting.class);
    private static BitField reversed = BitFieldFactory.getInstance(4);
    private IconSet iconSet;
    private byte options;
    private Threshold[] thresholds;

    public IconMultiStateFormatting() {
        this.iconSet = IconSet.GYR_3_TRAFFIC_LIGHTS;
        this.options = (byte) 0;
        this.thresholds = new Threshold[this.iconSet.num];
    }

    public IconMultiStateFormatting(LittleEndianInput in) {
        in.readShort();
        in.readByte();
        int num = in.readByte();
        this.iconSet = IconSet.byId(in.readByte());
        if (this.iconSet.num != num) {
            log.log(5, new Object[]{"Inconsistent Icon Set defintion, found " + this.iconSet + " but defined as " + num + " entries"});
        }
        this.options = in.readByte();
        this.thresholds = new Threshold[this.iconSet.num];
        for (int i = 0; i < this.thresholds.length; i++) {
            this.thresholds[i] = new IconMultiStateThreshold(in);
        }
    }

    public IconSet getIconSet() {
        return this.iconSet;
    }

    public void setIconSet(IconSet set) {
        this.iconSet = set;
    }

    public Threshold[] getThresholds() {
        return this.thresholds;
    }

    public void setThresholds(Threshold[] thresholds) {
        this.thresholds = thresholds == null ? null : (Threshold[]) thresholds.clone();
    }

    public boolean isIconOnly() {
        return getOptionFlag(iconOnly);
    }

    public void setIconOnly(boolean only) {
        setOptionFlag(only, iconOnly);
    }

    public boolean isReversed() {
        return getOptionFlag(reversed);
    }

    public void setReversed(boolean rev) {
        setOptionFlag(rev, reversed);
    }

    private boolean getOptionFlag(BitField field) {
        return field.getValue(this.options) != 0;
    }

    private void setOptionFlag(boolean option, BitField field) {
        this.options = field.setByteBoolean(this.options, option);
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("    [Icon Formatting]\n");
        buffer.append("          .icon_set = ").append(this.iconSet).append("\n");
        buffer.append("          .icon_only= ").append(isIconOnly()).append("\n");
        buffer.append("          .reversed = ").append(isReversed()).append("\n");
        for (Threshold t : this.thresholds) {
            buffer.append(t.toString());
        }
        buffer.append("    [/Icon Formatting]\n");
        return buffer.toString();
    }

    public Object clone() {
        IconMultiStateFormatting rec = new IconMultiStateFormatting();
        rec.iconSet = this.iconSet;
        rec.options = this.options;
        rec.thresholds = new Threshold[this.thresholds.length];
        System.arraycopy(this.thresholds, 0, rec.thresholds, 0, this.thresholds.length);
        return rec;
    }

    public int getDataLength() {
        int len = 6;
        for (Threshold t : this.thresholds) {
            len += t.getDataLength();
        }
        return len;
    }

    public void serialize(LittleEndianOutput out) {
        out.writeShort(0);
        out.writeByte(0);
        out.writeByte(this.iconSet.num);
        out.writeByte(this.iconSet.id);
        out.writeByte(this.options);
        for (Threshold t : this.thresholds) {
            t.serialize(out);
        }
    }
}
