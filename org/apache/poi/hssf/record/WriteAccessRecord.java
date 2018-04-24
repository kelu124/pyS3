package org.apache.poi.hssf.record;

import java.util.Arrays;
import org.apache.poi.util.LittleEndian;
import org.apache.poi.util.LittleEndianOutput;
import org.apache.poi.util.StringUtil;

public final class WriteAccessRecord extends StandardRecord {
    private static final int DATA_SIZE = 112;
    private static final byte[] PADDING = new byte[112];
    private static final byte PAD_CHAR = (byte) 32;
    public static final short sid = (short) 92;
    private String field_1_username;

    static {
        Arrays.fill(PADDING, (byte) 32);
    }

    public WriteAccessRecord() {
        setUsername("");
    }

    public WriteAccessRecord(RecordInputStream in) {
        if (in.remaining() > 112) {
            throw new RecordFormatException("Expected data size (112) but got (" + in.remaining() + ")");
        }
        int nChars = in.readUShort();
        int is16BitFlag = in.readUByte();
        if (nChars > 112 || (is16BitFlag & 254) != 0) {
            byte[] data = new byte[(in.remaining() + 3)];
            LittleEndian.putUShort(data, 0, nChars);
            LittleEndian.putByte(data, 2, is16BitFlag);
            in.readFully(data, 3, data.length - 3);
            setUsername(new String(data, StringUtil.UTF8).trim());
            return;
        }
        String rawText;
        if ((is16BitFlag & 1) == 0) {
            rawText = StringUtil.readCompressedUnicode(in, nChars);
        } else {
            rawText = StringUtil.readUnicodeLE(in, nChars);
        }
        this.field_1_username = rawText.trim();
        for (int padSize = in.remaining(); padSize > 0; padSize--) {
            in.readUByte();
        }
    }

    public void setUsername(String username) {
        boolean is16bit = StringUtil.hasMultibyte(username);
        if (112 - (((is16bit ? 2 : 1) * username.length()) + 3) < 0) {
            throw new IllegalArgumentException("Name is too long: " + username);
        }
        this.field_1_username = username;
    }

    public String getUsername() {
        return this.field_1_username;
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[WRITEACCESS]\n");
        buffer.append("    .name = ").append(this.field_1_username).append("\n");
        buffer.append("[/WRITEACCESS]\n");
        return buffer.toString();
    }

    public void serialize(LittleEndianOutput out) {
        int i;
        int i2 = 1;
        String username = getUsername();
        boolean is16bit = StringUtil.hasMultibyte(username);
        out.writeShort(username.length());
        if (is16bit) {
            i = 1;
        } else {
            i = 0;
        }
        out.writeByte(i);
        if (is16bit) {
            StringUtil.putUnicodeLE(username, out);
        } else {
            StringUtil.putCompressedUnicode(username, out);
        }
        i = username.length();
        if (is16bit) {
            i2 = 2;
        }
        out.write(PADDING, 0, 112 - ((i * i2) + 3));
    }

    protected int getDataSize() {
        return 112;
    }

    public short getSid() {
        return (short) 92;
    }
}
