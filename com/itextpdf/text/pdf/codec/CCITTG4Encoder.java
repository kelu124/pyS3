package com.itextpdf.text.pdf.codec;

import com.itextpdf.text.pdf.ByteBuffer;
import com.itextpdf.text.pdf.codec.wmf.MetaDo;
import com.itextpdf.xmp.XMPError;
import org.apache.poi.hssf.record.BOFRecord;
import org.bytedeco.javacpp.opencv_videoio;

public class CCITTG4Encoder {
    private static final int CODE = 1;
    private static final int EOL = 1;
    private static final int G3CODE_EOF = -3;
    private static final int G3CODE_EOL = -1;
    private static final int G3CODE_INCOMP = -4;
    private static final int G3CODE_INVALID = -2;
    private static final int LENGTH = 0;
    private static final int RUNLEN = 2;
    private static byte[] oneruns = new byte[]{(byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 3, (byte) 3, (byte) 3, (byte) 3, (byte) 3, (byte) 3, (byte) 3, (byte) 3, (byte) 3, (byte) 3, (byte) 3, (byte) 3, (byte) 3, (byte) 3, (byte) 3, (byte) 3, (byte) 4, (byte) 4, (byte) 4, (byte) 4, (byte) 4, (byte) 4, (byte) 4, (byte) 4, (byte) 5, (byte) 5, (byte) 5, (byte) 5, (byte) 6, (byte) 6, (byte) 7, (byte) 8};
    private static byte[] zeroruns = new byte[]{(byte) 8, (byte) 7, (byte) 6, (byte) 6, (byte) 5, (byte) 5, (byte) 5, (byte) 5, (byte) 4, (byte) 4, (byte) 4, (byte) 4, (byte) 4, (byte) 4, (byte) 4, (byte) 4, (byte) 3, (byte) 3, (byte) 3, (byte) 3, (byte) 3, (byte) 3, (byte) 3, (byte) 3, (byte) 3, (byte) 3, (byte) 3, (byte) 3, (byte) 3, (byte) 3, (byte) 3, (byte) 3, (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0};
    private int[][] TIFFFaxBlackCodes = new int[][]{new int[]{10, 55, 0}, new int[]{3, 2, 1}, new int[]{2, 3, 2}, new int[]{2, 2, 3}, new int[]{3, 3, 4}, new int[]{4, 3, 5}, new int[]{4, 2, 6}, new int[]{5, 3, 7}, new int[]{6, 5, 8}, new int[]{6, 4, 9}, new int[]{7, 4, 10}, new int[]{7, 5, 11}, new int[]{7, 7, 12}, new int[]{8, 4, 13}, new int[]{8, 7, 14}, new int[]{9, 24, 15}, new int[]{10, 23, 16}, new int[]{10, 24, 17}, new int[]{10, 8, 18}, new int[]{11, 103, 19}, new int[]{11, 104, 20}, new int[]{11, 108, 21}, new int[]{11, 55, 22}, new int[]{11, 40, 23}, new int[]{11, 23, 24}, new int[]{11, 24, 25}, new int[]{12, 202, 26}, new int[]{12, XMPError.BADXMP, 27}, new int[]{12, XMPError.BADSTREAM, 28}, new int[]{12, 205, 29}, new int[]{12, 104, 30}, new int[]{12, 105, 31}, new int[]{12, 106, 32}, new int[]{12, 107, 33}, new int[]{12, 210, 34}, new int[]{12, 211, 35}, new int[]{12, 212, 36}, new int[]{12, 213, 37}, new int[]{12, 214, 38}, new int[]{12, 215, 39}, new int[]{12, 108, 40}, new int[]{12, 109, 41}, new int[]{12, 218, 42}, new int[]{12, 219, 43}, new int[]{12, 84, 44}, new int[]{12, 85, 45}, new int[]{12, 86, 46}, new int[]{12, 87, 47}, new int[]{12, 100, 48}, new int[]{12, 101, 49}, new int[]{12, 82, 50}, new int[]{12, 83, 51}, new int[]{12, 36, 52}, new int[]{12, 55, 53}, new int[]{12, 56, 54}, new int[]{12, 39, 55}, new int[]{12, 40, 56}, new int[]{12, 88, 57}, new int[]{12, 89, 58}, new int[]{12, 43, 59}, new int[]{12, 44, 60}, new int[]{12, 90, 61}, new int[]{12, 102, 62}, new int[]{12, 103, 63}, new int[]{10, 15, 64}, new int[]{12, 200, 128}, new int[]{12, 201, 192}, new int[]{12, 91, 256}, new int[]{12, 51, 320}, new int[]{12, 52, 384}, new int[]{12, 53, opencv_videoio.CV_CAP_PROP_XI_WB_KR}, new int[]{13, 108, 512}, new int[]{13, 109, 576}, new int[]{13, 74, 640}, new int[]{13, 75, 704}, new int[]{13, 76, 768}, new int[]{13, 77, 832}, new int[]{13, 114, 896}, new int[]{13, 115, 960}, new int[]{13, 116, 1024}, new int[]{13, 117, 1088}, new int[]{13, 118, 1152}, new int[]{13, 119, 1216}, new int[]{13, 82, 1280}, new int[]{13, 83, 1344}, new int[]{13, 84, 1408}, new int[]{13, 85, 1472}, new int[]{13, 90, BOFRecord.VERSION}, new int[]{13, 91, 1600}, new int[]{13, 100, 1664}, new int[]{13, 101, 1728}, new int[]{11, 8, 1792}, new int[]{11, 12, 1856}, new int[]{11, 13, 1920}, new int[]{12, 18, 1984}, new int[]{12, 19, 2048}, new int[]{12, 20, 2112}, new int[]{12, 21, 2176}, new int[]{12, 22, 2240}, new int[]{12, 23, 2304}, new int[]{12, 28, MetaDo.META_DIBBITBLT}, new int[]{12, 29, 2432}, new int[]{12, 30, 2496}, new int[]{12, 31, 2560}, new int[]{12, 1, -1}, new int[]{9, 1, -2}, new int[]{10, 1, -2}, new int[]{11, 1, -2}, new int[]{12, 0, -2}};
    private int[][] TIFFFaxWhiteCodes = new int[][]{new int[]{8, 53, 0}, new int[]{6, 7, 1}, new int[]{4, 7, 2}, new int[]{4, 8, 3}, new int[]{4, 11, 4}, new int[]{4, 12, 5}, new int[]{4, 14, 6}, new int[]{4, 15, 7}, new int[]{5, 19, 8}, new int[]{5, 20, 9}, new int[]{5, 7, 10}, new int[]{5, 8, 11}, new int[]{6, 8, 12}, new int[]{6, 3, 13}, new int[]{6, 52, 14}, new int[]{6, 53, 15}, new int[]{6, 42, 16}, new int[]{6, 43, 17}, new int[]{7, 39, 18}, new int[]{7, 12, 19}, new int[]{7, 8, 20}, new int[]{7, 23, 21}, new int[]{7, 3, 22}, new int[]{7, 4, 23}, new int[]{7, 40, 24}, new int[]{7, 43, 25}, new int[]{7, 19, 26}, new int[]{7, 36, 27}, new int[]{7, 24, 28}, new int[]{8, 2, 29}, new int[]{8, 3, 30}, new int[]{8, 26, 31}, new int[]{8, 27, 32}, new int[]{8, 18, 33}, new int[]{8, 19, 34}, new int[]{8, 20, 35}, new int[]{8, 21, 36}, new int[]{8, 22, 37}, new int[]{8, 23, 38}, new int[]{8, 40, 39}, new int[]{8, 41, 40}, new int[]{8, 42, 41}, new int[]{8, 43, 42}, new int[]{8, 44, 43}, new int[]{8, 45, 44}, new int[]{8, 4, 45}, new int[]{8, 5, 46}, new int[]{8, 10, 47}, new int[]{8, 11, 48}, new int[]{8, 82, 49}, new int[]{8, 83, 50}, new int[]{8, 84, 51}, new int[]{8, 85, 52}, new int[]{8, 36, 53}, new int[]{8, 37, 54}, new int[]{8, 88, 55}, new int[]{8, 89, 56}, new int[]{8, 90, 57}, new int[]{8, 91, 58}, new int[]{8, 74, 59}, new int[]{8, 75, 60}, new int[]{8, 50, 61}, new int[]{8, 51, 62}, new int[]{8, 52, 63}, new int[]{5, 27, 64}, new int[]{5, 18, 128}, new int[]{6, 23, 192}, new int[]{7, 55, 256}, new int[]{8, 54, 320}, new int[]{8, 55, 384}, new int[]{8, 100, opencv_videoio.CV_CAP_PROP_XI_WB_KR}, new int[]{8, 101, 512}, new int[]{8, 104, 576}, new int[]{8, 103, 640}, new int[]{9, XMPError.BADSTREAM, 704}, new int[]{9, 205, 768}, new int[]{9, 210, 832}, new int[]{9, 211, 896}, new int[]{9, 212, 960}, new int[]{9, 213, 1024}, new int[]{9, 214, 1088}, new int[]{9, 215, 1152}, new int[]{9, 216, 1216}, new int[]{9, 217, 1280}, new int[]{9, 218, 1344}, new int[]{9, 219, 1408}, new int[]{9, 152, 1472}, new int[]{9, 153, BOFRecord.VERSION}, new int[]{9, 154, 1600}, new int[]{6, 24, 1664}, new int[]{9, 155, 1728}, new int[]{11, 8, 1792}, new int[]{11, 12, 1856}, new int[]{11, 13, 1920}, new int[]{12, 18, 1984}, new int[]{12, 19, 2048}, new int[]{12, 20, 2112}, new int[]{12, 21, 2176}, new int[]{12, 22, 2240}, new int[]{12, 23, 2304}, new int[]{12, 28, MetaDo.META_DIBBITBLT}, new int[]{12, 29, 2432}, new int[]{12, 30, 2496}, new int[]{12, 31, 2560}, new int[]{12, 1, -1}, new int[]{9, 1, -2}, new int[]{10, 1, -2}, new int[]{11, 1, -2}, new int[]{12, 0, -2}};
    private int bit = 8;
    private int data;
    private byte[] dataBp;
    private int[] horizcode = new int[]{3, 1, 0};
    private int[] msbmask = new int[]{0, 1, 3, 7, 15, 31, 63, 127, 255};
    private int offsetData;
    private ByteBuffer outBuf = new ByteBuffer(1024);
    private int[] passcode = new int[]{4, 1, 0};
    private byte[] refline;
    private int rowbytes;
    private int rowpixels;
    private int sizeData;
    private int[][] vcodes = new int[][]{new int[]{7, 3, 0}, new int[]{6, 3, 0}, new int[]{3, 3, 0}, new int[]{1, 1, 0}, new int[]{3, 2, 0}, new int[]{6, 2, 0}, new int[]{7, 2, 0}};

    public CCITTG4Encoder(int width) {
        this.rowpixels = width;
        this.rowbytes = (this.rowpixels + 7) / 8;
        this.refline = new byte[this.rowbytes];
    }

    public void fax4Encode(byte[] data, int offset, int size) {
        this.dataBp = data;
        this.offsetData = offset;
        this.sizeData = size;
        while (this.sizeData > 0) {
            Fax3Encode2DRow();
            System.arraycopy(this.dataBp, this.offsetData, this.refline, 0, this.rowbytes);
            this.offsetData += this.rowbytes;
            this.sizeData -= this.rowbytes;
        }
    }

    public static byte[] compress(byte[] data, int width, int height) {
        CCITTG4Encoder g4 = new CCITTG4Encoder(width);
        g4.fax4Encode(data, 0, g4.rowbytes * height);
        return g4.close();
    }

    public void fax4Encode(byte[] data, int height) {
        fax4Encode(data, 0, this.rowbytes * height);
    }

    private void putcode(int[] table) {
        putBits(table[1], table[0]);
    }

    private void putspan(int span, int[][] tab) {
        while (span >= 2624) {
            int[] te = tab[103];
            putBits(te[1], te[0]);
            span -= te[2];
        }
        if (span >= 64) {
            te = tab[(span >> 6) + 63];
            putBits(te[1], te[0]);
            span -= te[2];
        }
        putBits(tab[span][1], tab[span][0]);
    }

    private void putBits(int bits, int length) {
        while (length > this.bit) {
            this.data |= bits >> (length - this.bit);
            length -= this.bit;
            this.outBuf.append((byte) this.data);
            this.data = 0;
            this.bit = 8;
        }
        this.data |= (this.msbmask[length] & bits) << (this.bit - length);
        this.bit -= length;
        if (this.bit == 0) {
            this.outBuf.append((byte) this.data);
            this.data = 0;
            this.bit = 8;
        }
    }

    private void Fax3Encode2DRow() {
        int b1;
        int a0 = 0;
        int a1 = pixel(this.dataBp, this.offsetData, 0) != 0 ? 0 : finddiff(this.dataBp, this.offsetData, 0, this.rowpixels, 0);
        if (pixel(this.refline, 0, 0) != 0) {
            b1 = 0;
        } else {
            b1 = finddiff(this.refline, 0, 0, this.rowpixels, 0);
        }
        while (true) {
            int b2 = finddiff2(this.refline, 0, b1, this.rowpixels, pixel(this.refline, 0, b1));
            if (b2 >= a1) {
                int d = b1 - a1;
                if (-3 > d || d > 3) {
                    int a2 = finddiff2(this.dataBp, this.offsetData, a1, this.rowpixels, pixel(this.dataBp, this.offsetData, a1));
                    putcode(this.horizcode);
                    if (a0 + a1 == 0 || pixel(this.dataBp, this.offsetData, a0) == 0) {
                        putspan(a1 - a0, this.TIFFFaxWhiteCodes);
                        putspan(a2 - a1, this.TIFFFaxBlackCodes);
                    } else {
                        putspan(a1 - a0, this.TIFFFaxBlackCodes);
                        putspan(a2 - a1, this.TIFFFaxWhiteCodes);
                    }
                    a0 = a2;
                } else {
                    putcode(this.vcodes[d + 3]);
                    a0 = a1;
                }
            } else {
                putcode(this.passcode);
                a0 = b2;
            }
            if (a0 < this.rowpixels) {
                a1 = finddiff(this.dataBp, this.offsetData, a0, this.rowpixels, pixel(this.dataBp, this.offsetData, a0));
                b1 = finddiff(this.refline, 0, finddiff(this.refline, 0, a0, this.rowpixels, pixel(this.dataBp, this.offsetData, a0) ^ 1), this.rowpixels, pixel(this.dataBp, this.offsetData, a0));
            } else {
                return;
            }
        }
    }

    private void Fax4PostEncode() {
        putBits(1, 12);
        putBits(1, 12);
        if (this.bit != 8) {
            this.outBuf.append((byte) this.data);
            this.data = 0;
            this.bit = 8;
        }
    }

    public byte[] close() {
        Fax4PostEncode();
        return this.outBuf.toByteArray();
    }

    private int pixel(byte[] data, int offset, int bit) {
        if (bit >= this.rowpixels) {
            return 0;
        }
        return ((data[(bit >> 3) + offset] & 255) >> (7 - (bit & 7))) & 1;
    }

    private static int find1span(byte[] bp, int offset, int bs, int be) {
        int n;
        int span;
        int bits = be - bs;
        int pos = offset + (bs >> 3);
        if (bits > 0) {
            n = bs & 7;
            if (n != 0) {
                span = oneruns[(bp[pos] << n) & 255];
                if (span > 8 - n) {
                    span = 8 - n;
                }
                if (span > bits) {
                    span = bits;
                }
                if (n + span < 8) {
                    return span;
                }
                bits -= span;
                pos++;
                while (bits >= 8) {
                    if (bp[pos] != (byte) -1) {
                        return oneruns[bp[pos] & 255] + span;
                    }
                    span += 8;
                    bits -= 8;
                    pos++;
                }
                if (bits > 0) {
                    n = oneruns[bp[pos] & 255];
                    if (n > bits) {
                        n = bits;
                    }
                    span += n;
                }
                return span;
            }
        }
        span = 0;
        while (bits >= 8) {
            if (bp[pos] != (byte) -1) {
                return oneruns[bp[pos] & 255] + span;
            }
            span += 8;
            bits -= 8;
            pos++;
        }
        if (bits > 0) {
            n = oneruns[bp[pos] & 255];
            if (n > bits) {
                n = bits;
            }
            span += n;
        }
        return span;
    }

    private static int find0span(byte[] bp, int offset, int bs, int be) {
        int n;
        int span;
        int bits = be - bs;
        int pos = offset + (bs >> 3);
        if (bits > 0) {
            n = bs & 7;
            if (n != 0) {
                span = zeroruns[(bp[pos] << n) & 255];
                if (span > 8 - n) {
                    span = 8 - n;
                }
                if (span > bits) {
                    span = bits;
                }
                if (n + span < 8) {
                    return span;
                }
                bits -= span;
                pos++;
                while (bits >= 8) {
                    if (bp[pos] != (byte) 0) {
                        return zeroruns[bp[pos] & 255] + span;
                    }
                    span += 8;
                    bits -= 8;
                    pos++;
                }
                if (bits > 0) {
                    n = zeroruns[bp[pos] & 255];
                    if (n > bits) {
                        n = bits;
                    }
                    span += n;
                }
                return span;
            }
        }
        span = 0;
        while (bits >= 8) {
            if (bp[pos] != (byte) 0) {
                return zeroruns[bp[pos] & 255] + span;
            }
            span += 8;
            bits -= 8;
            pos++;
        }
        if (bits > 0) {
            n = zeroruns[bp[pos] & 255];
            if (n > bits) {
                n = bits;
            }
            span += n;
        }
        return span;
    }

    private static int finddiff(byte[] bp, int offset, int bs, int be, int color) {
        return (color != 0 ? find1span(bp, offset, bs, be) : find0span(bp, offset, bs, be)) + bs;
    }

    private static int finddiff2(byte[] bp, int offset, int bs, int be, int color) {
        return bs < be ? finddiff(bp, offset, bs, be, color) : be;
    }
}
