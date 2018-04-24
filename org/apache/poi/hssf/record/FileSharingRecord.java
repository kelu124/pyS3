package org.apache.poi.hssf.record;

import com.itextpdf.text.pdf.PdfBoolean;
import org.apache.poi.util.LittleEndianOutput;
import org.apache.poi.util.StringUtil;

public final class FileSharingRecord extends StandardRecord implements Cloneable {
    public static final short sid = (short) 91;
    private short field_1_readonly;
    private short field_2_password;
    private byte field_3_username_unicode_options;
    private String field_3_username_value;

    public FileSharingRecord(RecordInputStream in) {
        this.field_1_readonly = in.readShort();
        this.field_2_password = in.readShort();
        int nameLen = in.readShort();
        if (nameLen > 0) {
            this.field_3_username_unicode_options = in.readByte();
            this.field_3_username_value = in.readCompressedUnicode(nameLen);
            return;
        }
        this.field_3_username_value = "";
    }

    public void setReadOnly(short readonly) {
        this.field_1_readonly = readonly;
    }

    public short getReadOnly() {
        return this.field_1_readonly;
    }

    public void setPassword(short password) {
        this.field_2_password = password;
    }

    public short getPassword() {
        return this.field_2_password;
    }

    public String getUsername() {
        return this.field_3_username_value;
    }

    public void setUsername(String username) {
        this.field_3_username_value = username;
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[FILESHARING]\n");
        buffer.append("    .readonly       = ").append(getReadOnly() == (short) 1 ? PdfBoolean.TRUE : PdfBoolean.FALSE).append("\n");
        buffer.append("    .password       = ").append(Integer.toHexString(getPassword())).append("\n");
        buffer.append("    .username       = ").append(getUsername()).append("\n");
        buffer.append("[/FILESHARING]\n");
        return buffer.toString();
    }

    public void serialize(LittleEndianOutput out) {
        out.writeShort(getReadOnly());
        out.writeShort(getPassword());
        out.writeShort(this.field_3_username_value.length());
        if (this.field_3_username_value.length() > 0) {
            out.writeByte(this.field_3_username_unicode_options);
            StringUtil.putCompressedUnicode(getUsername(), out);
        }
    }

    protected int getDataSize() {
        int nameLen = this.field_3_username_value.length();
        if (nameLen < 1) {
            return 6;
        }
        return nameLen + 7;
    }

    public short getSid() {
        return (short) 91;
    }

    public FileSharingRecord clone() {
        FileSharingRecord clone = new FileSharingRecord();
        clone.setReadOnly(this.field_1_readonly);
        clone.setPassword(this.field_2_password);
        clone.setUsername(this.field_3_username_value);
        return clone;
    }
}
