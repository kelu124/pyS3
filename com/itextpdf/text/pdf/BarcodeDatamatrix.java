package com.itextpdf.text.pdf;

import android.support.v4.view.InputDeviceCompat;
import android.support.v7.widget.helper.ItemTouchHelper.Callback;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Jpeg;
import com.itextpdf.text.pdf.codec.CCITTG4Encoder;
import com.itextpdf.text.pdf.codec.TIFFConstants;
import com.itextpdf.text.pdf.codec.wmf.MetaDo;
import com.itextpdf.xmp.XMPError;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Hashtable;
import org.apache.poi.hssf.record.UnknownRecord;
import org.apache.poi.hssf.usermodel.HSSFShapeTypes;
import org.bytedeco.javacpp.avcodec.AVCodecContext;

public class BarcodeDatamatrix {
    public static final int DM_ASCII = 1;
    public static final int DM_AUTO = 0;
    public static final int DM_B256 = 4;
    public static final int DM_C40 = 2;
    public static final int DM_EDIFACT = 6;
    public static final int DM_ERROR_EXTENSION = 5;
    public static final int DM_ERROR_INVALID_SQUARE = 3;
    public static final int DM_ERROR_TEXT_TOO_BIG = 1;
    public static final int DM_EXTENSION = 32;
    public static final int DM_NO_ERROR = 0;
    public static final int DM_RAW = 7;
    public static final int DM_TEST = 64;
    public static final int DM_TEXT = 3;
    public static final int DM_X21 = 5;
    private static final DmParams[] dmSizes = new DmParams[]{new DmParams(10, 10, 10, 10, 3, 3, 5), new DmParams(12, 12, 12, 12, 5, 5, 7), new DmParams(8, 18, 8, 18, 5, 5, 7), new DmParams(14, 14, 14, 14, 8, 8, 10), new DmParams(8, 32, 8, 16, 10, 10, 11), new DmParams(16, 16, 16, 16, 12, 12, 12), new DmParams(12, 26, 12, 26, 16, 16, 14), new DmParams(18, 18, 18, 18, 18, 18, 14), new DmParams(20, 20, 20, 20, 22, 22, 18), new DmParams(12, 36, 12, 18, 22, 22, 18), new DmParams(22, 22, 22, 22, 30, 30, 20), new DmParams(16, 36, 16, 18, 32, 32, 24), new DmParams(24, 24, 24, 24, 36, 36, 24), new DmParams(26, 26, 26, 26, 44, 44, 28), new DmParams(16, 48, 16, 24, 49, 49, 28), new DmParams(32, 32, 16, 16, 62, 62, 36), new DmParams(36, 36, 18, 18, 86, 86, 42), new DmParams(40, 40, 20, 20, 114, 114, 48), new DmParams(44, 44, 22, 22, 144, 144, 56), new DmParams(48, 48, 24, 24, 174, 174, 68), new DmParams(52, 52, 26, 26, XMPError.BADSTREAM, 102, 42), new DmParams(64, 64, 16, 16, TIFFConstants.TIFFTAG_MINSAMPLEVALUE, 140, 56), new DmParams(72, 72, 18, 18, 368, 92, 36), new DmParams(80, 80, 20, 20, 456, 114, 48), new DmParams(88, 88, 22, 22, 576, 144, 56), new DmParams(96, 96, 24, 24, 696, 174, 68), new DmParams(104, 104, 26, 26, 816, 136, 56), new DmParams(120, 120, 20, 20, 1050, 175, 68), new DmParams(132, 132, 22, 22, 1304, 163, 62), new DmParams(144, 144, 24, 24, 1558, 156, 62)};
    private static final String x12 = "\r*> 0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private int extOut;
    private int height;
    private byte[] image;
    private int options;
    private short[] place;
    private int width;
    private int ws;

    private static class DmParams {
        int dataBlock;
        int dataSize;
        int errorBlock;
        int height;
        int heightSection;
        int width;
        int widthSection;

        DmParams(int height, int width, int heightSection, int widthSection, int dataSize, int dataBlock, int errorBlock) {
            this.height = height;
            this.width = width;
            this.heightSection = heightSection;
            this.widthSection = widthSection;
            this.dataSize = dataSize;
            this.dataBlock = dataBlock;
            this.errorBlock = errorBlock;
        }
    }

    static class Placement {
        private static final Hashtable<Integer, short[]> cache = new Hashtable();
        private short[] array;
        private int ncol;
        private int nrow;

        private Placement() {
        }

        static short[] doPlacement(int nrow, int ncol) {
            Integer key = Integer.valueOf((nrow * 1000) + ncol);
            short[] pc = (short[]) cache.get(key);
            if (pc != null) {
                return pc;
            }
            Placement p = new Placement();
            p.nrow = nrow;
            p.ncol = ncol;
            p.array = new short[(nrow * ncol)];
            p.ecc200();
            cache.put(key, p.array);
            return p.array;
        }

        private void module(int row, int col, int chr, int bit) {
            if (row < 0) {
                row += this.nrow;
                col += 4 - ((this.nrow + 4) % 8);
            }
            if (col < 0) {
                col += this.ncol;
                row += 4 - ((this.ncol + 4) % 8);
            }
            this.array[(this.ncol * row) + col] = (short) ((chr * 8) + bit);
        }

        private void utah(int row, int col, int chr) {
            module(row - 2, col - 2, chr, 0);
            module(row - 2, col - 1, chr, 1);
            module(row - 1, col - 2, chr, 2);
            module(row - 1, col - 1, chr, 3);
            module(row - 1, col, chr, 4);
            module(row, col - 2, chr, 5);
            module(row, col - 1, chr, 6);
            module(row, col, chr, 7);
        }

        private void corner1(int chr) {
            module(this.nrow - 1, 0, chr, 0);
            module(this.nrow - 1, 1, chr, 1);
            module(this.nrow - 1, 2, chr, 2);
            module(0, this.ncol - 2, chr, 3);
            module(0, this.ncol - 1, chr, 4);
            module(1, this.ncol - 1, chr, 5);
            module(2, this.ncol - 1, chr, 6);
            module(3, this.ncol - 1, chr, 7);
        }

        private void corner2(int chr) {
            module(this.nrow - 3, 0, chr, 0);
            module(this.nrow - 2, 0, chr, 1);
            module(this.nrow - 1, 0, chr, 2);
            module(0, this.ncol - 4, chr, 3);
            module(0, this.ncol - 3, chr, 4);
            module(0, this.ncol - 2, chr, 5);
            module(0, this.ncol - 1, chr, 6);
            module(1, this.ncol - 1, chr, 7);
        }

        private void corner3(int chr) {
            module(this.nrow - 3, 0, chr, 0);
            module(this.nrow - 2, 0, chr, 1);
            module(this.nrow - 1, 0, chr, 2);
            module(0, this.ncol - 2, chr, 3);
            module(0, this.ncol - 1, chr, 4);
            module(1, this.ncol - 1, chr, 5);
            module(2, this.ncol - 1, chr, 6);
            module(3, this.ncol - 1, chr, 7);
        }

        private void corner4(int chr) {
            module(this.nrow - 1, 0, chr, 0);
            module(this.nrow - 1, this.ncol - 1, chr, 1);
            module(0, this.ncol - 3, chr, 2);
            module(0, this.ncol - 2, chr, 3);
            module(0, this.ncol - 1, chr, 4);
            module(1, this.ncol - 3, chr, 5);
            module(1, this.ncol - 2, chr, 6);
            module(1, this.ncol - 1, chr, 7);
        }

        private void ecc200() {
            Arrays.fill(this.array, (short) 0);
            int chr = 1;
            int row = 4;
            int col = 0;
            while (true) {
                int chr2;
                if (row == this.nrow && col == 0) {
                    chr2 = chr + 1;
                    corner1(chr);
                    chr = chr2;
                }
                if (row == this.nrow - 2 && col == 0 && this.ncol % 4 != 0) {
                    chr2 = chr + 1;
                    corner2(chr);
                    chr = chr2;
                }
                if (row == this.nrow - 2 && col == 0 && this.ncol % 8 == 4) {
                    chr2 = chr + 1;
                    corner3(chr);
                    chr = chr2;
                }
                if (row == this.nrow + 4 && col == 2 && this.ncol % 8 == 0) {
                    chr2 = chr + 1;
                    corner4(chr);
                    chr = chr2;
                }
                do {
                    if (row < this.nrow && col >= 0 && this.array[(this.ncol * row) + col] == (short) 0) {
                        chr2 = chr + 1;
                        utah(row, col, chr);
                        chr = chr2;
                    }
                    row -= 2;
                    col += 2;
                    if (row < 0) {
                        break;
                    }
                } while (col < this.ncol);
                row++;
                col += 3;
                chr2 = chr;
                while (true) {
                    if (row < 0 || col >= this.ncol || this.array[(this.ncol * row) + col] != (short) 0) {
                        chr = chr2;
                    } else {
                        chr = chr2 + 1;
                        utah(row, col, chr2);
                    }
                    row += 2;
                    col -= 2;
                    if (row >= this.nrow || col < 0) {
                        row += 3;
                        col++;
                    } else {
                        chr2 = chr;
                    }
                }
                row += 3;
                col++;
                if (row >= this.nrow && col >= this.ncol) {
                    break;
                }
            }
            if (this.array[(this.nrow * this.ncol) - 1] == (short) 0) {
                short[] sArr = this.array;
                int i = (this.nrow * this.ncol) - 1;
                this.array[((this.nrow * this.ncol) - this.ncol) - 2] = (short) 1;
                sArr[i] = (short) 1;
            }
        }
    }

    static class ReedSolomon {
        private static final int[] alog = new int[]{1, 2, 4, 8, 16, 32, 64, 128, 45, 90, 180, 69, 138, 57, 114, 228, 229, 231, 227, 235, 251, 219, 155, 27, 54, 108, 216, 157, 23, 46, 92, 184, 93, 186, 89, 178, 73, 146, 9, 18, 36, 72, 144, 13, 26, 52, 104, 208, 141, 55, 110, 220, 149, 7, 14, 28, 56, 112, 224, Jpeg.M_APPD, MetaDo.META_CREATEPALETTE, HSSFShapeTypes.ActionButtonEnd, 171, 123, 246, HSSFShapeTypes.ActionButtonForwardNext, 175, 115, 230, 225, UnknownRecord.PHONETICPR_00EF, 243, XMPError.BADXMP, 187, 91, 182, 65, 130, 41, 82, 164, 101, 202, 185, 95, 190, 81, 162, 105, 210, 137, 63, 126, 252, 213, 135, 35, 70, 140, 53, 106, 212, 133, 39, 78, 156, 21, 42, 84, 168, 125, Callback.DEFAULT_SWIPE_ANIMATION_DURATION, 217, 159, 19, 38, 76, 152, 29, 58, 116, 232, 253, 215, 131, 43, 86, 172, 117, 234, 249, 223, 147, 11, 22, 44, 88, 176, 77, 154, 25, 50, 100, 200, 189, 87, 174, 113, Jpeg.M_APP2, UnknownRecord.BITMAP_00E9, 255, 211, 139, 59, 118, 236, 245, HSSFShapeTypes.ActionButtonSound, 163, 107, 214, 129, 47, 94, 188, 85, 170, 121, 242, 201, 191, 83, 166, 97, HSSFShapeTypes.ActionButtonBackPrevious, 169, 127, 254, 209, 143, 51, 102, XMPError.BADSTREAM, 181, 71, 142, 49, 98, HSSFShapeTypes.ActionButtonBeginning, 165, 103, 206, 177, 79, 158, 17, 34, 68, 136, 61, 122, AVCodecContext.FF_PROFILE_H264_HIGH_444_PREDICTIVE, HSSFShapeTypes.ActionButtonReturn, 167, 99, HSSFShapeTypes.ActionButtonDocument, 161, 111, 222, 145, 15, 30, 60, 120, 240, 205, 183, 67, 134, 33, 66, 132, 37, 74, 148, 5, 10, 20, 40, 80, 160, 109, 218, 153, 31, 62, 124, 248, 221, 151, 3, 6, 12, 24, 48, 96, 192, 173, 119, Jpeg.M_APPE, 241, 207, 179, 75, 150, 1};
        private static final int[] log = new int[]{0, 255, 1, 240, 2, 225, 241, 53, 3, 38, Jpeg.M_APP2, 133, 242, 43, 54, 210, 4, HSSFShapeTypes.ActionButtonEnd, 39, 114, 227, 106, 134, 28, 243, 140, 44, 23, 55, 118, 211, 234, 5, 219, HSSFShapeTypes.ActionButtonBeginning, 96, 40, 222, 115, 103, 228, 78, 107, 125, 135, 8, 29, 162, AVCodecContext.FF_PROFILE_H264_HIGH_444_PREDICTIVE, 186, 141, 180, 45, 99, 24, 49, 56, 13, 119, 153, 212, HSSFShapeTypes.ActionButtonSound, 235, 91, 6, 76, 220, 217, HSSFShapeTypes.ActionButtonReturn, 11, 97, 184, 41, 36, 223, 253, 116, 138, 104, HSSFShapeTypes.ActionButtonForwardNext, 229, 86, 79, 171, 108, 165, 126, 145, 136, 34, 9, 74, 30, 32, 163, 84, 245, 173, 187, XMPError.BADSTREAM, 142, 81, 181, 190, 46, 88, 100, 159, 25, 231, 50, 207, 57, 147, 14, 67, 120, 128, 154, 248, 213, 167, 200, 63, 236, 110, 92, 176, 7, 161, 77, 124, 221, 102, 218, 95, HSSFShapeTypes.ActionButtonDocument, 90, 12, 152, 98, 48, 185, 179, 42, 209, 37, 132, 224, 52, 254, UnknownRecord.PHONETICPR_00EF, 117, UnknownRecord.BITMAP_00E9, 139, 22, 105, 27, HSSFShapeTypes.ActionButtonBackPrevious, 113, 230, 206, 87, 158, 80, 189, 172, XMPError.BADXMP, 109, 175, 166, 62, 127, MetaDo.META_CREATEPALETTE, 146, 66, 137, 192, 35, 252, 10, 183, 75, 216, 31, 83, 33, 73, 164, 144, 85, 170, 246, 65, 174, 61, 188, 202, 205, 157, 143, 169, 82, 72, 182, 215, 191, 251, 47, 178, 89, 151, 101, 94, 160, 123, 26, 112, 232, 21, 51, Jpeg.M_APPE, 208, 131, 58, 69, 148, 18, 15, 16, 68, 17, 121, 149, 129, 19, 155, 59, 249, 70, 214, Callback.DEFAULT_SWIPE_ANIMATION_DURATION, 168, 71, 201, 156, 64, 60, Jpeg.M_APPD, 130, 111, 20, 93, 122, 177, 150};
        private static final int[] poly10 = new int[]{28, 24, 185, 166, 223, 248, 116, 255, 110, 61};
        private static final int[] poly11 = new int[]{175, 138, 205, 12, HSSFShapeTypes.ActionButtonBackPrevious, 168, 39, 245, 60, 97, 120};
        private static final int[] poly12 = new int[]{41, 153, 158, 91, 61, 42, 142, 213, 97, 178, 100, 242};
        private static final int[] poly14 = new int[]{156, 97, 192, 252, 95, 9, 157, 119, 138, 45, 18, 186, 83, 185};
        private static final int[] poly18 = new int[]{83, HSSFShapeTypes.ActionButtonEnd, 100, 39, 188, 75, 66, 61, 241, 213, 109, 129, 94, 254, 225, 48, 90, 188};
        private static final int[] poly20 = new int[]{15, HSSFShapeTypes.ActionButtonEnd, AVCodecContext.FF_PROFILE_H264_HIGH_444_PREDICTIVE, 9, UnknownRecord.BITMAP_00E9, 71, 168, 2, 188, 160, 153, 145, 253, 79, 108, 82, 27, 174, 186, 172};
        private static final int[] poly24 = new int[]{52, 190, 88, 205, 109, 39, 176, 21, 155, HSSFShapeTypes.ActionButtonReturn, 251, 223, 155, 21, 5, 172, 254, 124, 12, 181, 184, 96, 50, HSSFShapeTypes.ActionButtonForwardNext};
        private static final int[] poly28 = new int[]{211, 231, 43, 97, 71, 96, 103, 174, 37, 151, 170, 53, 75, 34, 249, 121, 17, 138, 110, 213, 141, 136, 120, 151, UnknownRecord.BITMAP_00E9, 168, 93, 255};
        private static final int[] poly36 = new int[]{245, 127, 242, 218, 130, Callback.DEFAULT_SWIPE_ANIMATION_DURATION, 162, 181, 102, 120, 84, 179, 220, 251, 80, 182, 229, 18, 2, 4, 68, 33, 101, 137, 95, 119, 115, 44, 175, 184, 59, 25, 225, 98, 81, 112};
        private static final int[] poly42 = new int[]{77, HSSFShapeTypes.ActionButtonForwardNext, 137, 31, 19, 38, 22, 153, MetaDo.META_CREATEPALETTE, 105, 122, 2, 245, 133, 242, 8, 175, 95, 100, 9, 167, 105, 214, 111, 57, 121, 21, 1, 253, 57, 54, 101, 248, 202, 69, 50, 150, 177, Jpeg.M_APP2, 5, 9, 5};
        private static final int[] poly48 = new int[]{245, 132, 172, 223, 96, 32, 117, 22, Jpeg.M_APPE, 133, Jpeg.M_APPE, 231, 205, 188, Jpeg.M_APPD, 87, 191, 106, 16, 147, 118, 23, 37, 90, 170, 205, 131, 88, 120, 100, 66, 138, 186, 240, 82, 44, 176, 87, 187, 147, 160, 175, 69, 213, 92, 253, 225, 19};
        private static final int[] poly5 = new int[]{228, 48, 15, 111, 62};
        private static final int[] poly56 = new int[]{175, 9, 223, Jpeg.M_APPE, 12, 17, 220, 208, 100, 29, 175, 170, 230, 192, 215, 235, 150, 159, 36, 223, 38, 200, 132, 54, 228, 146, 218, 234, 117, XMPError.BADXMP, 29, 232, 144, Jpeg.M_APPE, 22, 150, 201, 117, 62, 207, 164, 13, 137, 245, 127, 67, MetaDo.META_CREATEPALETTE, 28, 155, 43, XMPError.BADXMP, 107, UnknownRecord.BITMAP_00E9, 53, 143, 46};
        private static final int[] poly62 = new int[]{242, 93, 169, 50, 144, 210, 39, 118, 202, 188, 201, 189, 143, 108, HSSFShapeTypes.ActionButtonBeginning, 37, 185, 112, 134, 230, 245, 63, HSSFShapeTypes.ActionButtonReturn, 190, Callback.DEFAULT_SWIPE_ANIMATION_DURATION, 106, 185, 221, 175, 64, 114, 71, 161, 44, 147, 6, 27, 218, 51, 63, 87, 10, 40, 130, 188, 17, 163, 31, 176, 170, 4, 107, 232, 7, 94, 166, 224, 124, 86, 47, 11, XMPError.BADSTREAM};
        private static final int[] poly68 = new int[]{220, 228, 173, 89, 251, 149, 159, 56, 89, 33, 147, AVCodecContext.FF_PROFILE_H264_HIGH_444_PREDICTIVE, 154, 36, 73, 127, 213, 136, 248, 180, 234, HSSFShapeTypes.ActionButtonReturn, 158, 177, 68, 122, 93, 213, 15, 160, 227, 236, 66, 139, 153, 185, 202, 167, 179, 25, 220, 232, 96, 210, 231, 136, 223, UnknownRecord.PHONETICPR_00EF, 181, 241, 59, 52, 172, 25, 49, 232, 211, 189, 64, 54, 108, 153, 132, 63, 96, 103, 82, 186};
        private static final int[] poly7 = new int[]{23, 68, 144, 134, 240, 92, 254};

        ReedSolomon() {
        }

        private static int[] getPoly(int nc) {
            switch (nc) {
                case 5:
                    return poly5;
                case 7:
                    return poly7;
                case 10:
                    return poly10;
                case 11:
                    return poly11;
                case 12:
                    return poly12;
                case 14:
                    return poly14;
                case 18:
                    return poly18;
                case 20:
                    return poly20;
                case 24:
                    return poly24;
                case 28:
                    return poly28;
                case 36:
                    return poly36;
                case 42:
                    return poly42;
                case 48:
                    return poly48;
                case 56:
                    return poly56;
                case 62:
                    return poly62;
                case 68:
                    return poly68;
                default:
                    return null;
            }
        }

        private static void reedSolomonBlock(byte[] wd, int nd, byte[] ncout, int nc, int[] c) {
            int i;
            for (i = 0; i <= nc; i++) {
                ncout[i] = (byte) 0;
            }
            for (i = 0; i < nd; i++) {
                int k = (ncout[0] ^ wd[i]) & 255;
                for (int j = 0; j < nc; j++) {
                    ncout[j] = (byte) ((k == 0 ? 0 : (byte) alog[(log[k] + log[c[(nc - j) - 1]]) % 255]) ^ ncout[j + 1]);
                }
            }
        }

        static void generateECC(byte[] wd, int nd, int datablock, int nc) {
            int blocks = (nd + 2) / datablock;
            byte[] buf = new byte[256];
            byte[] ecc = new byte[256];
            int[] c = getPoly(nc);
            for (int b = 0; b < blocks; b++) {
                int n = b;
                int p = 0;
                while (n < nd) {
                    int p2 = p + 1;
                    buf[p] = wd[n];
                    n += blocks;
                    p = p2;
                }
                reedSolomonBlock(buf, p, ecc, nc, c);
                n = b;
                p = 0;
                while (n < nc * blocks) {
                    p2 = p + 1;
                    wd[nd + n] = ecc[p];
                    n += blocks;
                    p = p2;
                }
            }
        }
    }

    private void setBit(int x, int y, int xByte) {
        byte[] bArr = this.image;
        int i = (y * xByte) + (x / 8);
        bArr[i] = (byte) (bArr[i] | ((byte) (128 >> (x & 7))));
    }

    private void draw(byte[] data, int dataSize, DmParams dm) {
        int j;
        int xByte = ((dm.width + (this.ws * 2)) + 7) / 8;
        Arrays.fill(this.image, (byte) 0);
        int i = this.ws;
        while (i < dm.height + this.ws) {
            for (j = this.ws; j < dm.width + this.ws; j += 2) {
                setBit(j, i, xByte);
            }
            i += dm.heightSection;
        }
        i = (dm.heightSection - 1) + this.ws;
        while (i < dm.height + this.ws) {
            for (j = this.ws; j < dm.width + this.ws; j++) {
                setBit(j, i, xByte);
            }
            i += dm.heightSection;
        }
        i = this.ws;
        while (i < dm.width + this.ws) {
            for (j = this.ws; j < dm.height + this.ws; j++) {
                setBit(i, j, xByte);
            }
            i += dm.widthSection;
        }
        i = (dm.widthSection - 1) + this.ws;
        while (i < dm.width + this.ws) {
            for (j = this.ws + 1; j < dm.height + this.ws; j += 2) {
                setBit(i, j, xByte);
            }
            i += dm.widthSection;
        }
        int p = 0;
        int ys = 0;
        while (ys < dm.height) {
            for (int y = 1; y < dm.heightSection - 1; y++) {
                int xs = 0;
                while (xs < dm.width) {
                    int x = 1;
                    while (x < dm.widthSection - 1) {
                        int p2 = p + 1;
                        int z = this.place[p];
                        if (z == 1 || (z > 1 && ((data[(z / 8) - 1] & 255) & (128 >> (z % 8))) != 0)) {
                            setBit((x + xs) + this.ws, (y + ys) + this.ws, xByte);
                        }
                        x++;
                        p = p2;
                    }
                    xs += dm.widthSection;
                }
            }
            ys += dm.heightSection;
        }
    }

    private static void makePadding(byte[] data, int position, int count) {
        if (count > 0) {
            int position2 = position + 1;
            data[position] = (byte) -127;
            while (true) {
                count--;
                if (count > 0) {
                    int t = ((((position2 + 1) * 149) % 253) + 129) + 1;
                    if (t > 254) {
                        t -= 254;
                    }
                    position = position2 + 1;
                    data[position2] = (byte) t;
                    position2 = position;
                } else {
                    position = position2;
                    return;
                }
            }
        }
    }

    private static boolean isDigit(int c) {
        return c >= 48 && c <= 57;
    }

    private static int asciiEncodation(byte[] text, int textOffset, int textLength, byte[] data, int dataOffset, int dataLength) {
        int i;
        textLength += textOffset;
        dataLength += dataOffset;
        int ptrOut = dataOffset;
        int ptrIn = textOffset;
        while (ptrIn < textLength) {
            if (ptrOut >= dataLength) {
                i = ptrIn;
                return -1;
            }
            i = ptrIn + 1;
            int c = text[ptrIn] & 255;
            int ptrOut2;
            if (isDigit(c) && i < textLength && isDigit(text[i] & 255)) {
                ptrOut2 = ptrOut + 1;
                ptrIn = i + 1;
                data[ptrOut] = (byte) (((((c - 48) * 10) + (text[i] & 255)) - 48) + 130);
                ptrOut = ptrOut2;
            } else if (c <= 127) {
                ptrOut2 = ptrOut + 1;
                data[ptrOut] = (byte) (c + 1);
                ptrOut = ptrOut2;
                ptrIn = i;
            } else if (ptrOut + 1 >= dataLength) {
                return -1;
            } else {
                ptrOut2 = ptrOut + 1;
                data[ptrOut] = (byte) -21;
                ptrOut = ptrOut2 + 1;
                data[ptrOut2] = (byte) ((c - 128) + 1);
                ptrIn = i;
            }
        }
        i = ptrIn;
        return ptrOut - dataOffset;
    }

    private static int b256Encodation(byte[] text, int textOffset, int textLength, byte[] data, int dataOffset, int dataLength) {
        if (textLength == 0) {
            return 0;
        }
        if (textLength < Callback.DEFAULT_SWIPE_ANIMATION_DURATION && textLength + 2 > dataLength) {
            return -1;
        }
        if (textLength >= Callback.DEFAULT_SWIPE_ANIMATION_DURATION && textLength + 3 > dataLength) {
            return -1;
        }
        int k;
        data[dataOffset] = (byte) -25;
        if (textLength < Callback.DEFAULT_SWIPE_ANIMATION_DURATION) {
            data[dataOffset + 1] = (byte) textLength;
            k = 2;
        } else {
            data[dataOffset + 1] = (byte) ((textLength / Callback.DEFAULT_SWIPE_ANIMATION_DURATION) + 249);
            data[dataOffset + 2] = (byte) (textLength % Callback.DEFAULT_SWIPE_ANIMATION_DURATION);
            k = 3;
        }
        System.arraycopy(text, textOffset, data, k + dataOffset, textLength);
        k += textLength + dataOffset;
        for (int j = dataOffset + 1; j < k; j++) {
            int tv = (data[j] & 255) + ((((j + 1) * 149) % 255) + 1);
            if (tv > 255) {
                tv += InputDeviceCompat.SOURCE_ANY;
            }
            data[j] = (byte) tv;
        }
        return k - dataOffset;
    }

    private static int X12Encodation(byte[] text, int textOffset, int textLength, byte[] data, int dataOffset, int dataLength) {
        if (textLength == 0) {
            return 0;
        }
        byte c;
        int ptrIn = 0;
        byte[] x = new byte[textLength];
        int count = 0;
        while (ptrIn < textLength) {
            int k;
            int i = x12.indexOf((char) text[ptrIn + textOffset]);
            if (i >= 0) {
                x[ptrIn] = (byte) i;
                count++;
            } else {
                x[ptrIn] = (byte) 100;
                if (count >= 6) {
                    count -= (count / 3) * 3;
                }
                for (k = 0; k < count; k++) {
                    x[(ptrIn - k) - 1] = (byte) 100;
                }
                count = 0;
            }
            ptrIn++;
        }
        if (count >= 6) {
            count -= (count / 3) * 3;
        }
        for (k = 0; k < count; k++) {
            x[(ptrIn - k) - 1] = (byte) 100;
        }
        ptrIn = 0;
        int ptrOut = 0;
        while (ptrIn < textLength) {
            c = x[ptrIn];
            if (ptrOut >= dataLength) {
                break;
            }
            int ptrOut2;
            if (c >= (byte) 40) {
                if (ptrIn <= 0 || x[ptrIn - 1] >= (byte) 40) {
                    ptrOut2 = ptrOut;
                } else {
                    ptrOut2 = ptrOut + 1;
                    data[dataOffset + ptrOut] = (byte) -2;
                }
                int ci = text[ptrIn + textOffset] & 255;
                if (ci > 127) {
                    ptrOut = ptrOut2 + 1;
                    data[dataOffset + ptrOut2] = (byte) -21;
                    ci -= 128;
                } else {
                    ptrOut = ptrOut2;
                }
                if (ptrOut >= dataLength) {
                    break;
                }
                ptrOut2 = ptrOut + 1;
                data[dataOffset + ptrOut] = (byte) (ci + 1);
            } else {
                if (ptrIn == 0 || (ptrIn > 0 && x[ptrIn - 1] > (byte) 40)) {
                    ptrOut2 = ptrOut + 1;
                    data[dataOffset + ptrOut] = (byte) -18;
                    ptrOut = ptrOut2;
                }
                if (ptrOut + 2 > dataLength) {
                    break;
                }
                int n = (((x[ptrIn] * 1600) + (x[ptrIn + 1] * 40)) + x[ptrIn + 2]) + 1;
                ptrOut2 = ptrOut + 1;
                data[dataOffset + ptrOut] = (byte) (n / 256);
                ptrOut = ptrOut2 + 1;
                data[dataOffset + ptrOut2] = (byte) n;
                ptrIn += 2;
                ptrOut2 = ptrOut;
            }
            ptrIn++;
            ptrOut = ptrOut2;
        }
        c = (byte) 100;
        if (textLength > 0) {
            c = x[textLength - 1];
        }
        if (ptrIn != textLength || (c < (byte) 40 && ptrOut >= dataLength)) {
            return -1;
        }
        if (c >= (byte) 40) {
            return ptrOut;
        }
        ptrOut2 = ptrOut + 1;
        data[dataOffset + ptrOut] = (byte) -2;
        return ptrOut2;
    }

    private static int EdifactEncodation(byte[] text, int textOffset, int textLength, byte[] data, int dataOffset, int dataLength) {
        if (textLength == 0) {
            return 0;
        }
        int ptrIn = 0;
        int edi = 0;
        int pedi = 18;
        boolean ascii = true;
        int ptrOut = 0;
        while (ptrIn < textLength) {
            int ptrOut2;
            int c = text[ptrIn + textOffset] & 255;
            if (((c & 224) != 64 && (c & 224) != 32) || c == 95) {
                if (!ascii) {
                    edi |= 31 << pedi;
                    if ((ptrOut + 3) - (pedi / 8) > dataLength) {
                        break;
                    }
                    ptrOut2 = ptrOut + 1;
                    data[dataOffset + ptrOut] = (byte) (edi >> 16);
                    if (pedi <= 12) {
                        ptrOut = ptrOut2 + 1;
                        data[dataOffset + ptrOut2] = (byte) (edi >> 8);
                    } else {
                        ptrOut = ptrOut2;
                    }
                    if (pedi <= 6) {
                        ptrOut2 = ptrOut + 1;
                        data[dataOffset + ptrOut] = (byte) edi;
                    } else {
                        ptrOut2 = ptrOut;
                    }
                    ascii = true;
                    pedi = 18;
                    edi = 0;
                    ptrOut = ptrOut2;
                }
                if (c > 127) {
                    if (ptrOut >= dataLength) {
                        break;
                    }
                    ptrOut2 = ptrOut + 1;
                    data[dataOffset + ptrOut] = (byte) -21;
                    c -= 128;
                    ptrOut = ptrOut2;
                }
                if (ptrOut >= dataLength) {
                    break;
                }
                ptrOut2 = ptrOut + 1;
                data[dataOffset + ptrOut] = (byte) (c + 1);
            } else {
                if (ascii) {
                    if (ptrOut + 1 > dataLength) {
                        break;
                    }
                    ptrOut2 = ptrOut + 1;
                    data[dataOffset + ptrOut] = (byte) -16;
                    ascii = false;
                    ptrOut = ptrOut2;
                }
                edi |= (c & 63) << pedi;
                if (pedi == 0) {
                    if (ptrOut + 3 > dataLength) {
                        break;
                    }
                    ptrOut2 = ptrOut + 1;
                    data[dataOffset + ptrOut] = (byte) (edi >> 16);
                    ptrOut = ptrOut2 + 1;
                    data[dataOffset + ptrOut2] = (byte) (edi >> 8);
                    ptrOut2 = ptrOut + 1;
                    data[dataOffset + ptrOut] = (byte) edi;
                    edi = 0;
                    pedi = 18;
                } else {
                    pedi -= 6;
                    ptrOut2 = ptrOut;
                }
            }
            ptrIn++;
            ptrOut = ptrOut2;
        }
        if (ptrIn != textLength) {
            return -1;
        }
        int dataSize = Integer.MAX_VALUE;
        for (int i = 0; i < dmSizes.length; i++) {
            if (dmSizes[i].dataSize >= (dataOffset + ptrOut) + (3 - (pedi / 6))) {
                dataSize = dmSizes[i].dataSize;
                break;
            }
        }
        if ((dataSize - dataOffset) - ptrOut <= 2 && pedi >= 6) {
            byte val;
            if (pedi <= 12) {
                val = (byte) ((edi >> 18) & 63);
                if ((val & 32) == 0) {
                    val = (byte) (val | 64);
                }
                ptrOut2 = ptrOut + 1;
                data[dataOffset + ptrOut] = (byte) (val + 1);
                ptrOut = ptrOut2;
            }
            if (pedi <= 6) {
                val = (byte) ((edi >> 12) & 63);
                if ((val & 32) == 0) {
                    val = (byte) (val | 64);
                }
                ptrOut2 = ptrOut + 1;
                data[dataOffset + ptrOut] = (byte) (val + 1);
                return ptrOut2;
            }
        } else if (!ascii) {
            edi |= 31 << pedi;
            if ((ptrOut + 3) - (pedi / 8) > dataLength) {
                return -1;
            }
            ptrOut2 = ptrOut + 1;
            data[dataOffset + ptrOut] = (byte) (edi >> 16);
            if (pedi <= 12) {
                ptrOut = ptrOut2 + 1;
                data[dataOffset + ptrOut2] = (byte) (edi >> 8);
            } else {
                ptrOut = ptrOut2;
            }
            if (pedi <= 6) {
                ptrOut2 = ptrOut + 1;
                data[dataOffset + ptrOut] = (byte) edi;
                return ptrOut2;
            }
        }
        return ptrOut;
    }

    private static int C40OrTextEncodation(byte[] text, int textOffset, int textLength, byte[] data, int dataOffset, int dataLength, boolean c40) {
        if (textLength == 0) {
            return 0;
        }
        int ptrOut;
        int ptrOut2;
        String basic;
        String shift3;
        if (c40) {
            ptrOut = 0 + 1;
            data[dataOffset + 0] = (byte) -26;
            ptrOut2 = ptrOut;
        } else {
            ptrOut = 0 + 1;
            data[dataOffset + 0] = (byte) -17;
            ptrOut2 = ptrOut;
        }
        String shift2 = "!\"#$%&'()*+,-./:;<=>?@[\\]^_";
        if (c40) {
            basic = " 0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
            shift3 = "`abcdefghijklmnopqrstuvwxyz{|}~";
        } else {
            basic = " 0123456789abcdefghijklmnopqrstuvwxyz";
            shift3 = "`ABCDEFGHIJKLMNOPQRSTUVWXYZ{|}~";
        }
        int[] enc = new int[((textLength * 4) + 10)];
        int last0 = 0;
        int last1 = 0;
        int encPtr = 0;
        int ptrIn = 0;
        while (ptrIn < textLength) {
            int i;
            if (encPtr % 3 == 0) {
                last0 = ptrIn;
                last1 = encPtr;
            }
            int ptrIn2 = ptrIn + 1;
            int c = text[textOffset + ptrIn] & 255;
            if (c > 127) {
                c -= 128;
                i = encPtr + 1;
                enc[encPtr] = 1;
                encPtr = i + 1;
                enc[i] = 30;
            }
            i = encPtr;
            int idx = basic.indexOf((char) c);
            if (idx >= 0) {
                encPtr = i + 1;
                enc[i] = idx + 3;
                i = encPtr;
            } else if (c < 32) {
                encPtr = i + 1;
                enc[i] = 0;
                i = encPtr + 1;
                enc[encPtr] = c;
            } else {
                idx = shift2.indexOf((char) c);
                if (idx >= 0) {
                    encPtr = i + 1;
                    enc[i] = 1;
                    i = encPtr + 1;
                    enc[encPtr] = idx;
                } else {
                    idx = shift3.indexOf((char) c);
                    if (idx >= 0) {
                        encPtr = i + 1;
                        enc[i] = 2;
                        i = encPtr + 1;
                        enc[encPtr] = idx;
                    }
                }
            }
            encPtr = i;
            ptrIn = ptrIn2;
        }
        if (encPtr % 3 != 0) {
            ptrIn2 = last0;
            i = last1;
        } else {
            i = encPtr;
            ptrIn2 = ptrIn;
        }
        if ((i / 3) * 2 > dataLength - 2) {
            return -1;
        }
        int i2;
        ptrOut = ptrOut2;
        for (i2 = 0; i2 < i; i2 += 3) {
            int a = (((enc[i2] * 1600) + (enc[i2 + 1] * 40)) + enc[i2 + 2]) + 1;
            ptrOut2 = ptrOut + 1;
            data[dataOffset + ptrOut] = (byte) (a / 256);
            ptrOut = ptrOut2 + 1;
            data[dataOffset + ptrOut2] = (byte) a;
        }
        ptrOut2 = ptrOut + 1;
        data[ptrOut] = (byte) -2;
        i2 = asciiEncodation(text, ptrIn2, textLength - ptrIn2, data, ptrOut2, dataLength - ptrOut2);
        return i2 >= 0 ? i2 + ptrOut2 : i2;
    }

    private static int getEncodation(byte[] text, int textOffset, int textSize, byte[] data, int dataOffset, int dataSize, int options, boolean firstMatch) {
        int[] e1 = new int[6];
        if (dataSize < 0) {
            return -1;
        }
        options &= 7;
        if (options == 0) {
            e1[0] = asciiEncodation(text, textOffset, textSize, data, dataOffset, dataSize);
            if (firstMatch && e1[0] >= 0) {
                return e1[0];
            }
            e1[1] = C40OrTextEncodation(text, textOffset, textSize, data, dataOffset, dataSize, false);
            if (firstMatch && e1[1] >= 0) {
                return e1[1];
            }
            e1[2] = C40OrTextEncodation(text, textOffset, textSize, data, dataOffset, dataSize, true);
            if (firstMatch && e1[2] >= 0) {
                return e1[2];
            }
            e1[3] = b256Encodation(text, textOffset, textSize, data, dataOffset, dataSize);
            if (firstMatch && e1[3] >= 0) {
                return e1[3];
            }
            e1[4] = X12Encodation(text, textOffset, textSize, data, dataOffset, dataSize);
            if (firstMatch && e1[4] >= 0) {
                return e1[4];
            }
            e1[5] = EdifactEncodation(text, textOffset, textSize, data, dataOffset, dataSize);
            if (firstMatch && e1[5] >= 0) {
                return e1[5];
            }
            if (e1[0] < 0 && e1[1] < 0 && e1[2] < 0 && e1[3] < 0 && e1[4] < 0 && e1[5] < 0) {
                return -1;
            }
            int j = 0;
            int e = 99999;
            int k = 0;
            while (k < 6) {
                if (e1[k] >= 0 && e1[k] < e) {
                    e = e1[k];
                    j = k;
                }
                k++;
            }
            if (j == 0) {
                return asciiEncodation(text, textOffset, textSize, data, dataOffset, dataSize);
            }
            if (j == 1) {
                return C40OrTextEncodation(text, textOffset, textSize, data, dataOffset, dataSize, false);
            }
            if (j == 2) {
                return C40OrTextEncodation(text, textOffset, textSize, data, dataOffset, dataSize, true);
            }
            if (j == 3) {
                return b256Encodation(text, textOffset, textSize, data, dataOffset, dataSize);
            }
            if (j == 4) {
                return X12Encodation(text, textOffset, textSize, data, dataOffset, dataSize);
            }
            return e;
        }
        switch (options) {
            case 1:
                return asciiEncodation(text, textOffset, textSize, data, dataOffset, dataSize);
            case 2:
                return C40OrTextEncodation(text, textOffset, textSize, data, dataOffset, dataSize, true);
            case 3:
                return C40OrTextEncodation(text, textOffset, textSize, data, dataOffset, dataSize, false);
            case 4:
                return b256Encodation(text, textOffset, textSize, data, dataOffset, dataSize);
            case 5:
                return X12Encodation(text, textOffset, textSize, data, dataOffset, dataSize);
            case 6:
                return EdifactEncodation(text, textOffset, textSize, data, dataOffset, dataSize);
            case 7:
                if (textSize > dataSize) {
                    return -1;
                }
                System.arraycopy(text, textOffset, data, dataOffset, textSize);
                return textSize;
            default:
                return -1;
        }
    }

    private static int getNumber(byte[] text, int ptrIn, int n) {
        int v = 0;
        int j = 0;
        int ptrIn2 = ptrIn;
        while (j < n) {
            ptrIn = ptrIn2 + 1;
            int c = text[ptrIn2] & 255;
            if (c < 48 || c > 57) {
                return -1;
            }
            v = ((v * 10) + c) - 48;
            j++;
            ptrIn2 = ptrIn;
        }
        ptrIn = ptrIn2;
        return v;
    }

    private int processExtensions(byte[] text, int textOffset, int textSize, byte[] data) {
        if ((this.options & 32) == 0) {
            return 0;
        }
        int order = 0;
        int ptrOut = 0;
        int ptrIn = 0;
        while (ptrIn < textSize) {
            if (order > 20) {
                return -1;
            }
            int i;
            int ptrIn2 = ptrIn + 1;
            order++;
            switch (text[textOffset + ptrIn] & 255) {
                case 46:
                    this.extOut = ptrIn2;
                    return ptrOut;
                case 101:
                    if (ptrIn2 + 6 <= textSize) {
                        int eci = getNumber(text, textOffset + ptrIn2, 6);
                        if (eci >= 0) {
                            ptrIn2 += 6;
                            i = ptrOut + 1;
                            data[ptrOut] = (byte) -15;
                            if (eci >= 127) {
                                if (eci >= 16383) {
                                    ptrOut = i + 1;
                                    data[i] = (byte) (((eci - 16383) / 64516) + 192);
                                    i = ptrOut + 1;
                                    data[ptrOut] = (byte) ((((eci - 16383) / 254) % 254) + 1);
                                    ptrOut = i + 1;
                                    data[i] = (byte) (((eci - 16383) % 254) + 1);
                                    i = ptrOut;
                                    break;
                                }
                                ptrOut = i + 1;
                                data[i] = (byte) (((eci - 127) / 254) + 128);
                                i = ptrOut + 1;
                                data[ptrOut] = (byte) (((eci - 127) % 254) + 1);
                                break;
                            }
                            ptrOut = i + 1;
                            data[i] = (byte) (eci + 1);
                            i = ptrOut;
                            break;
                        }
                        return -1;
                    }
                    return -1;
                case 102:
                    if (order == 1 || (order == 2 && (text[textOffset] == (byte) 115 || text[textOffset] == (byte) 109))) {
                        i = ptrOut + 1;
                        data[ptrOut] = (byte) -24;
                        break;
                    }
                    return -1;
                case 109:
                    if (order != 1) {
                        return -1;
                    }
                    if (ptrIn2 + 1 > textSize) {
                        return -1;
                    }
                    ptrIn = ptrIn2 + 1;
                    int c = text[textOffset + ptrIn2] & 255;
                    if (c == 53 || c == 53) {
                        i = ptrOut + 1;
                        data[ptrOut] = (byte) -22;
                        ptrOut = i + 1;
                        data[i] = (byte) (c == 53 ? 236 : Jpeg.M_APPD);
                        i = ptrOut;
                        ptrIn2 = ptrIn;
                        break;
                    }
                    return -1;
                    break;
                case 112:
                    if (order == 1) {
                        i = ptrOut + 1;
                        data[ptrOut] = (byte) -22;
                        break;
                    }
                    return -1;
                case 115:
                    if (order == 1) {
                        if (ptrIn2 + 9 <= textSize) {
                            int fn = getNumber(text, textOffset + ptrIn2, 2);
                            if (fn > 0 && fn <= 16) {
                                ptrIn2 += 2;
                                int ft = getNumber(text, textOffset + ptrIn2, 2);
                                if (ft > 1 && ft <= 16) {
                                    ptrIn2 += 2;
                                    int fi = getNumber(text, textOffset + ptrIn2, 5);
                                    if (fi >= 0 && fn < 64516) {
                                        ptrIn2 += 5;
                                        i = ptrOut + 1;
                                        data[ptrOut] = (byte) -23;
                                        ptrOut = i + 1;
                                        data[i] = (byte) (((fn - 1) << 4) | (17 - ft));
                                        i = ptrOut + 1;
                                        data[ptrOut] = (byte) ((fi / 254) + 1);
                                        ptrOut = i + 1;
                                        data[i] = (byte) ((fi % 254) + 1);
                                        i = ptrOut;
                                        break;
                                    }
                                    return -1;
                                }
                                return -1;
                            }
                            return -1;
                        }
                        return -1;
                    }
                    return -1;
                default:
                    i = ptrOut;
                    break;
            }
            ptrOut = i;
            ptrIn = ptrIn2;
        }
        return -1;
    }

    public int generate(String text) throws UnsupportedEncodingException {
        byte[] t = text.getBytes("iso-8859-1");
        return generate(t, 0, t.length);
    }

    public int generate(byte[] text, int textOffset, int textSize) {
        byte[] data = new byte[2500];
        this.extOut = 0;
        int extCount = processExtensions(text, textOffset, textSize, data);
        if (extCount < 0) {
            return 5;
        }
        int e;
        DmParams dm;
        int k;
        if (this.height == 0 || this.width == 0) {
            byte[] bArr = text;
            e = getEncodation(bArr, textOffset + this.extOut, textSize - this.extOut, data, extCount, dmSizes[dmSizes.length - 1].dataSize - extCount, this.options, false);
            if (e < 0) {
                return 1;
            }
            e += extCount;
            k = 0;
            while (k < dmSizes.length && dmSizes[k].dataSize < e) {
                k++;
            }
            dm = dmSizes[k];
            this.height = dm.height;
            this.width = dm.width;
        } else {
            k = 0;
            while (k < dmSizes.length && (this.height != dmSizes[k].height || this.width != dmSizes[k].width)) {
                k++;
            }
            if (k == dmSizes.length) {
                return 3;
            }
            dm = dmSizes[k];
            e = getEncodation(text, textOffset + this.extOut, textSize - this.extOut, data, extCount, dm.dataSize - extCount, this.options, true);
            if (e < 0) {
                return 1;
            }
            e += extCount;
        }
        if ((this.options & 64) != 0) {
            return 0;
        }
        this.image = new byte[((((dm.width + (this.ws * 2)) + 7) / 8) * (dm.height + (this.ws * 2)))];
        makePadding(data, e, dm.dataSize - e);
        this.place = Placement.doPlacement(dm.height - ((dm.height / dm.heightSection) * 2), dm.width - ((dm.width / dm.widthSection) * 2));
        int full = dm.dataSize + (((dm.dataSize + 2) / dm.dataBlock) * dm.errorBlock);
        ReedSolomon.generateECC(data, dm.dataSize, dm.dataBlock, dm.errorBlock);
        draw(data, full, dm);
        return 0;
    }

    public Image createImage() throws BadElementException {
        if (this.image == null) {
            return null;
        }
        return Image.getInstance(this.width + (this.ws * 2), this.height + (this.ws * 2), false, 256, 0, CCITTG4Encoder.compress(this.image, this.width + (this.ws * 2), this.height + (this.ws * 2)), null);
    }

    public byte[] getImage() {
        return this.image;
    }

    public int getHeight() {
        return this.height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return this.width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getWs() {
        return this.ws;
    }

    public void setWs(int ws) {
        this.ws = ws;
    }

    public int getOptions() {
        return this.options;
    }

    public void setOptions(int options) {
        this.options = options;
    }
}
