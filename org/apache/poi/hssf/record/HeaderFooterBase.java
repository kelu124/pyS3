package org.apache.poi.hssf.record;

import org.apache.poi.util.LittleEndianOutput;
import org.apache.poi.util.StringUtil;

public abstract class HeaderFooterBase extends StandardRecord {
    private boolean field_2_hasMultibyte;
    private String field_3_text;

    protected HeaderFooterBase(String text) {
        setText(text);
    }

    protected HeaderFooterBase(RecordInputStream in) {
        if (in.remaining() > 0) {
            int field_1_footer_len = in.readShort();
            this.field_2_hasMultibyte = in.readByte() != (byte) 0;
            if (this.field_2_hasMultibyte) {
                this.field_3_text = in.readUnicodeLEString(field_1_footer_len);
                return;
            } else {
                this.field_3_text = in.readCompressedUnicode(field_1_footer_len);
                return;
            }
        }
        this.field_3_text = "";
    }

    public final void setText(String text) {
        if (text == null) {
            throw new IllegalArgumentException("text must not be null");
        }
        this.field_2_hasMultibyte = StringUtil.hasMultibyte(text);
        this.field_3_text = text;
        if (getDataSize() > 8224) {
            throw new IllegalArgumentException("Header/Footer string too long (limit is 8224 bytes)");
        }
    }

    private int getTextLength() {
        return this.field_3_text.length();
    }

    public final String getText() {
        return this.field_3_text;
    }

    public final void serialize(LittleEndianOutput out) {
        if (getTextLength() > 0) {
            out.writeShort(getTextLength());
            out.writeByte(this.field_2_hasMultibyte ? 1 : 0);
            if (this.field_2_hasMultibyte) {
                StringUtil.putUnicodeLE(this.field_3_text, out);
            } else {
                StringUtil.putCompressedUnicode(this.field_3_text, out);
            }
        }
    }

    protected final int getDataSize() {
        int i = 1;
        if (getTextLength() < 1) {
            return 0;
        }
        int textLength = getTextLength();
        if (this.field_2_hasMultibyte) {
            i = 2;
        }
        return (i * textLength) + 3;
    }
}
