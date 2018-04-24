package org.apache.poi.hssf.record;

public final class FooterRecord extends HeaderFooterBase implements Cloneable {
    public static final short sid = (short) 21;

    public FooterRecord(String text) {
        super(text);
    }

    public FooterRecord(RecordInputStream in) {
        super(in);
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[FOOTER]\n");
        buffer.append("    .footer = ").append(getText()).append("\n");
        buffer.append("[/FOOTER]\n");
        return buffer.toString();
    }

    public short getSid() {
        return (short) 21;
    }

    public FooterRecord clone() {
        return new FooterRecord(getText());
    }
}
