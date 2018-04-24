package com.itextpdf.text.pdf;

import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.error_messages.MessageLocalization;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

public class ICC_Profile {
    private static HashMap<String, Integer> cstags = new HashMap();
    protected byte[] data;
    protected int numComponents;

    static {
        cstags.put("XYZ ", Integer.valueOf(3));
        cstags.put("Lab ", Integer.valueOf(3));
        cstags.put("Luv ", Integer.valueOf(3));
        cstags.put("YCbr", Integer.valueOf(3));
        cstags.put("Yxy ", Integer.valueOf(3));
        cstags.put("RGB ", Integer.valueOf(3));
        cstags.put("GRAY", Integer.valueOf(1));
        cstags.put("HSV ", Integer.valueOf(3));
        cstags.put("HLS ", Integer.valueOf(3));
        cstags.put("CMYK", Integer.valueOf(4));
        cstags.put("CMY ", Integer.valueOf(3));
        cstags.put("2CLR", Integer.valueOf(2));
        cstags.put("3CLR", Integer.valueOf(3));
        cstags.put("4CLR", Integer.valueOf(4));
        cstags.put("5CLR", Integer.valueOf(5));
        cstags.put("6CLR", Integer.valueOf(6));
        cstags.put("7CLR", Integer.valueOf(7));
        cstags.put("8CLR", Integer.valueOf(8));
        cstags.put("9CLR", Integer.valueOf(9));
        cstags.put("ACLR", Integer.valueOf(10));
        cstags.put("BCLR", Integer.valueOf(11));
        cstags.put("CCLR", Integer.valueOf(12));
        cstags.put("DCLR", Integer.valueOf(13));
        cstags.put("ECLR", Integer.valueOf(14));
        cstags.put("FCLR", Integer.valueOf(15));
    }

    protected ICC_Profile() {
    }

    public static ICC_Profile getInstance(byte[] data, int numComponents) {
        int nc = 0;
        if (data.length >= 128 && data[36] == (byte) 97 && data[37] == (byte) 99 && data[38] == (byte) 115 && data[39] == (byte) 112) {
            try {
                ICC_Profile icc = new ICC_Profile();
                icc.data = data;
                Integer cs = (Integer) cstags.get(new String(data, 16, 4, "US-ASCII"));
                if (cs != null) {
                    nc = cs.intValue();
                }
                icc.numComponents = nc;
                if (nc == numComponents) {
                    return icc;
                }
                throw new IllegalArgumentException("ICC profile contains " + nc + " component(s), the image data contains " + numComponents + " component(s)");
            } catch (UnsupportedEncodingException e) {
                throw new ExceptionConverter(e);
            }
        }
        throw new IllegalArgumentException(MessageLocalization.getComposedMessage("invalid.icc.profile", new Object[0]));
    }

    public static ICC_Profile getInstance(byte[] data) {
        try {
            Integer cs = (Integer) cstags.get(new String(data, 16, 4, "US-ASCII"));
            return getInstance(data, cs == null ? 0 : cs.intValue());
        } catch (UnsupportedEncodingException e) {
            throw new ExceptionConverter(e);
        }
    }

    public static ICC_Profile getInstance(InputStream file) {
        try {
            int n;
            byte[] head = new byte[128];
            int remain = head.length;
            int ptr = 0;
            while (remain > 0) {
                n = file.read(head, ptr, remain);
                if (n < 0) {
                    throw new IllegalArgumentException(MessageLocalization.getComposedMessage("invalid.icc.profile", new Object[0]));
                }
                remain -= n;
                ptr += n;
            }
            if (head[36] == (byte) 97 && head[37] == (byte) 99 && head[38] == (byte) 115 && head[39] == (byte) 112) {
                remain = ((((head[0] & 255) << 24) | ((head[1] & 255) << 16)) | ((head[2] & 255) << 8)) | (head[3] & 255);
                byte[] icc = new byte[remain];
                System.arraycopy(head, 0, icc, 0, head.length);
                remain -= head.length;
                ptr = head.length;
                while (remain > 0) {
                    n = file.read(icc, ptr, remain);
                    if (n < 0) {
                        throw new IllegalArgumentException(MessageLocalization.getComposedMessage("invalid.icc.profile", new Object[0]));
                    }
                    remain -= n;
                    ptr += n;
                }
                return getInstance(icc);
            }
            throw new IllegalArgumentException(MessageLocalization.getComposedMessage("invalid.icc.profile", new Object[0]));
        } catch (Exception ex) {
            throw new ExceptionConverter(ex);
        }
    }

    public static ICC_Profile GetInstance(String fname) {
        Exception ex;
        InputStream fs;
        Throwable th;
        FileInputStream fs2 = null;
        try {
            InputStream fs3 = new FileInputStream(fname);
            try {
                ICC_Profile icc = getInstance(fs3);
                try {
                    fs3.close();
                } catch (Exception e) {
                }
                return icc;
            } catch (Exception e2) {
                ex = e2;
                fs = fs3;
                try {
                    throw new ExceptionConverter(ex);
                } catch (Throwable th2) {
                    th = th2;
                    try {
                        fs2.close();
                    } catch (Exception e3) {
                    }
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                fs = fs3;
                fs2.close();
                throw th;
            }
        } catch (Exception e4) {
            ex = e4;
            throw new ExceptionConverter(ex);
        }
    }

    public byte[] getData() {
        return this.data;
    }

    public int getNumComponents() {
        return this.numComponents;
    }
}
