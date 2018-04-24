package com.itextpdf.xmp.impl;

import com.itextpdf.text.DocWriter;
import com.itextpdf.text.pdf.ByteBuffer;
import org.apache.poi.hssf.record.PaletteRecord;
import org.apache.poi.ss.formula.ptg.AreaErrPtg;

public class Base64 {
    private static final byte EQUAL = (byte) -3;
    private static final byte INVALID = (byte) -1;
    private static final byte WHITESPACE = (byte) -2;
    private static byte[] ascii = new byte[255];
    private static byte[] base64 = new byte[]{(byte) 65, (byte) 66, (byte) 67, (byte) 68, (byte) 69, (byte) 70, (byte) 71, (byte) 72, (byte) 73, (byte) 74, (byte) 75, (byte) 76, (byte) 77, (byte) 78, (byte) 79, (byte) 80, (byte) 81, (byte) 82, (byte) 83, (byte) 84, (byte) 85, (byte) 86, (byte) 87, (byte) 88, (byte) 89, (byte) 90, (byte) 97, (byte) 98, (byte) 99, (byte) 100, (byte) 101, (byte) 102, (byte) 103, (byte) 104, (byte) 105, (byte) 106, (byte) 107, (byte) 108, (byte) 109, (byte) 110, (byte) 111, (byte) 112, (byte) 113, (byte) 114, (byte) 115, (byte) 116, (byte) 117, (byte) 118, (byte) 119, (byte) 120, (byte) 121, (byte) 122, ByteBuffer.ZERO, (byte) 49, (byte) 50, (byte) 51, (byte) 52, (byte) 53, (byte) 54, (byte) 55, PaletteRecord.STANDARD_PALETTE_SIZE, (byte) 57, AreaErrPtg.sid, DocWriter.FORWARD};

    static {
        int idx;
        for (idx = 0; idx < 255; idx++) {
            ascii[idx] = (byte) -1;
        }
        for (idx = 0; idx < base64.length; idx++) {
            ascii[base64[idx]] = (byte) idx;
        }
        ascii[9] = WHITESPACE;
        ascii[10] = WHITESPACE;
        ascii[13] = WHITESPACE;
        ascii[32] = WHITESPACE;
        ascii[61] = EQUAL;
    }

    public static final byte[] encode(byte[] src) {
        return encode(src, 0);
    }

    public static final byte[] encode(byte[] src, int lineFeed) {
        lineFeed = (lineFeed / 4) * 4;
        if (lineFeed < 0) {
            lineFeed = 0;
        }
        int codeLength = ((src.length + 2) / 3) * 4;
        if (lineFeed > 0) {
            codeLength += (codeLength - 1) / lineFeed;
        }
        byte[] dst = new byte[codeLength];
        int didx = 0;
        int sidx = 0;
        int lf = 0;
        while (sidx + 3 <= src.length) {
            int sidx2 = sidx + 1;
            sidx = sidx2 + 1;
            sidx2 = sidx + 1;
            int bits24 = (((src[sidx] & 255) << 16) | ((src[sidx2] & 255) << 8)) | ((src[sidx] & 255) << 0);
            int i = didx + 1;
            dst[didx] = base64[(bits24 & 16515072) >> 18];
            didx = i + 1;
            dst[i] = base64[(bits24 & 258048) >> 12];
            i = didx + 1;
            dst[didx] = base64[(bits24 & 4032) >> 6];
            didx = i + 1;
            dst[i] = base64[bits24 & 63];
            lf += 4;
            if (didx >= codeLength || lineFeed <= 0 || lf % lineFeed != 0) {
                sidx = sidx2;
            } else {
                i = didx + 1;
                dst[didx] = (byte) 10;
                sidx = sidx2;
                didx = i;
            }
        }
        if (src.length - sidx == 2) {
            bits24 = ((src[sidx] & 255) << 16) | ((src[sidx + 1] & 255) << 8);
            i = didx + 1;
            dst[didx] = base64[(bits24 & 16515072) >> 18];
            didx = i + 1;
            dst[i] = base64[(bits24 & 258048) >> 12];
            i = didx + 1;
            dst[didx] = base64[(bits24 & 4032) >> 6];
            didx = i + 1;
            dst[i] = (byte) 61;
        } else if (src.length - sidx == 1) {
            bits24 = (src[sidx] & 255) << 16;
            i = didx + 1;
            dst[didx] = base64[(bits24 & 16515072) >> 18];
            didx = i + 1;
            dst[i] = base64[(bits24 & 258048) >> 12];
            i = didx + 1;
            dst[didx] = (byte) 61;
            didx = i + 1;
            dst[i] = (byte) 61;
        }
        return dst;
    }

    public static final String encode(String src) {
        return new String(encode(src.getBytes()));
    }

    public static final byte[] decode(byte[] src) throws IllegalArgumentException {
        int srcLen = 0;
        for (byte b : src) {
            byte val = ascii[b];
            if (val >= (byte) 0) {
                int srcLen2 = srcLen + 1;
                src[srcLen] = val;
                srcLen = srcLen2;
            } else if (val == (byte) -1) {
                throw new IllegalArgumentException("Invalid base 64 string");
            }
        }
        while (srcLen > 0 && src[srcLen - 1] == EQUAL) {
            srcLen--;
        }
        byte[] dst = new byte[((srcLen * 3) / 4)];
        int sidx = 0;
        int didx = 0;
        while (didx < dst.length - 2) {
            dst[didx] = (byte) (((src[sidx] << 2) & 255) | ((src[sidx + 1] >>> 4) & 3));
            dst[didx + 1] = (byte) (((src[sidx + 1] << 4) & 255) | ((src[sidx + 2] >>> 2) & 15));
            dst[didx + 2] = (byte) (((src[sidx + 2] << 6) & 255) | (src[sidx + 3] & 63));
            sidx += 4;
            didx += 3;
        }
        if (didx < dst.length) {
            dst[didx] = (byte) (((src[sidx] << 2) & 255) | ((src[sidx + 1] >>> 4) & 3));
        }
        didx++;
        if (didx < dst.length) {
            dst[didx] = (byte) (((src[sidx + 1] << 4) & 255) | ((src[sidx + 2] >>> 2) & 15));
        }
        return dst;
    }

    public static final String decode(String src) {
        return new String(decode(src.getBytes()));
    }
}
