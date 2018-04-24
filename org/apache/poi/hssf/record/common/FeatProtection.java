package org.apache.poi.hssf.record.common;

import org.apache.poi.hssf.record.RecordInputStream;
import org.apache.poi.util.LittleEndianOutput;
import org.apache.poi.util.StringUtil;

public final class FeatProtection implements SharedFeature {
    public static long HAS_SELF_RELATIVE_SECURITY_FEATURE = 1;
    public static long NO_SELF_RELATIVE_SECURITY_FEATURE = 0;
    private int fSD;
    private int passwordVerifier;
    private byte[] securityDescriptor;
    private String title;

    public FeatProtection() {
        this.securityDescriptor = new byte[0];
    }

    public FeatProtection(RecordInputStream in) {
        this.fSD = in.readInt();
        this.passwordVerifier = in.readInt();
        this.title = StringUtil.readUnicodeString(in);
        this.securityDescriptor = in.readRemainder();
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(" [FEATURE PROTECTION]\n");
        buffer.append("   Self Relative = " + this.fSD);
        buffer.append("   Password Verifier = " + this.passwordVerifier);
        buffer.append("   Title = " + this.title);
        buffer.append("   Security Descriptor Size = " + this.securityDescriptor.length);
        buffer.append(" [/FEATURE PROTECTION]\n");
        return buffer.toString();
    }

    public void serialize(LittleEndianOutput out) {
        out.writeInt(this.fSD);
        out.writeInt(this.passwordVerifier);
        StringUtil.writeUnicodeString(out, this.title);
        out.write(this.securityDescriptor);
    }

    public int getDataSize() {
        return (StringUtil.getEncodedSize(this.title) + 8) + this.securityDescriptor.length;
    }

    public int getPasswordVerifier() {
        return this.passwordVerifier;
    }

    public void setPasswordVerifier(int passwordVerifier) {
        this.passwordVerifier = passwordVerifier;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getFSD() {
        return this.fSD;
    }
}
