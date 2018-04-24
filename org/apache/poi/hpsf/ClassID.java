package org.apache.poi.hpsf;

import org.apache.poi.util.HexDump;
import org.apache.poi.util.StringUtil;

public class ClassID {
    public static final ClassID EQUATION30 = new ClassID("{0002CE02-0000-0000-C000-000000000046}");
    public static final ClassID EXCEL95 = new ClassID("{00020810-0000-0000-C000-000000000046}");
    public static final ClassID EXCEL97 = new ClassID("{00020820-0000-0000-C000-000000000046}");
    public static final int LENGTH = 16;
    public static final ClassID OLE10_PACKAGE = new ClassID("{0003000C-0000-0000-C000-000000000046}");
    public static final ClassID POWERPOINT95 = new ClassID("{EA7BAE70-FB3B-11CD-A903-00AA00510EA3}");
    public static final ClassID POWERPOINT97 = new ClassID("{64818D10-4F9B-11CF-86EA-00AA00B929E8}");
    public static final ClassID PPT_SHOW = new ClassID("{64818D10-4F9B-11CF-86EA-00AA00B929E8}");
    public static final ClassID TXT_ONLY = new ClassID("{5e941d80-bf96-11cd-b579-08002b30bfeb}");
    public static final ClassID WORD95 = new ClassID("{00020900-0000-0000-C000-000000000046}");
    public static final ClassID WORD97 = new ClassID("{00020906-0000-0000-C000-000000000046}");
    public static final ClassID XLS_WORKBOOK = new ClassID("{00020841-0000-0000-C000-000000000046}");
    protected byte[] bytes;

    public ClassID(byte[] src, int offset) {
        read(src, offset);
    }

    public ClassID() {
        this.bytes = new byte[16];
        for (int i = 0; i < 16; i++) {
            this.bytes[i] = (byte) 0;
        }
    }

    public ClassID(String externalForm) {
        this.bytes = new byte[16];
        String clsStr = externalForm.replaceAll("[{}-]", "");
        for (int i = 0; i < clsStr.length(); i += 2) {
            this.bytes[i / 2] = (byte) Integer.parseInt(clsStr.substring(i, i + 2), 16);
        }
    }

    public int length() {
        return 16;
    }

    public byte[] getBytes() {
        return this.bytes;
    }

    public void setBytes(byte[] bytes) {
        for (int i = 0; i < this.bytes.length; i++) {
            this.bytes[i] = bytes[i];
        }
    }

    public byte[] read(byte[] src, int offset) {
        this.bytes = new byte[16];
        this.bytes[0] = src[offset + 3];
        this.bytes[1] = src[offset + 2];
        this.bytes[2] = src[offset + 1];
        this.bytes[3] = src[offset + 0];
        this.bytes[4] = src[offset + 5];
        this.bytes[5] = src[offset + 4];
        this.bytes[6] = src[offset + 7];
        this.bytes[7] = src[offset + 6];
        for (int i = 8; i < 16; i++) {
            this.bytes[i] = src[i + offset];
        }
        return this.bytes;
    }

    public void write(byte[] dst, int offset) throws ArrayStoreException {
        if (dst.length < 16) {
            throw new ArrayStoreException("Destination byte[] must have room for at least 16 bytes, but has a length of only " + dst.length + ".");
        }
        dst[offset + 0] = this.bytes[3];
        dst[offset + 1] = this.bytes[2];
        dst[offset + 2] = this.bytes[1];
        dst[offset + 3] = this.bytes[0];
        dst[offset + 4] = this.bytes[5];
        dst[offset + 5] = this.bytes[4];
        dst[offset + 6] = this.bytes[7];
        dst[offset + 7] = this.bytes[6];
        for (int i = 8; i < 16; i++) {
            dst[i + offset] = this.bytes[i];
        }
    }

    public boolean equals(Object o) {
        if (o == null || !(o instanceof ClassID)) {
            return false;
        }
        ClassID cid = (ClassID) o;
        if (this.bytes.length != cid.bytes.length) {
            return false;
        }
        for (int i = 0; i < this.bytes.length; i++) {
            if (this.bytes[i] != cid.bytes[i]) {
                return false;
            }
        }
        return true;
    }

    public int hashCode() {
        return new String(this.bytes, StringUtil.UTF8).hashCode();
    }

    public String toString() {
        StringBuffer sbClassId = new StringBuffer(38);
        sbClassId.append('{');
        int i = 0;
        while (i < 16) {
            sbClassId.append(HexDump.toHex(this.bytes[i]));
            if (i == 3 || i == 5 || i == 7 || i == 9) {
                sbClassId.append('-');
            }
            i++;
        }
        sbClassId.append('}');
        return sbClassId.toString();
    }
}
