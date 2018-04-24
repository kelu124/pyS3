package com.itextpdf.text.pdf.codec;

import com.google.common.base.Ascii;
import com.google.common.primitives.UnsignedBytes;
import com.itextpdf.text.DocWriter;
import com.itextpdf.text.error_messages.MessageLocalization;
import com.itextpdf.text.exceptions.InvalidImageException;
import com.itextpdf.text.pdf.ByteBuffer;
import com.lee.ultrascan.library.ProbeParams;
import org.apache.poi.ddf.EscherProperties;
import org.apache.poi.hssf.record.CodepageRecord;
import org.apache.poi.hssf.record.EscherAggregate;
import org.apache.poi.hssf.record.InterfaceEndRecord;
import org.apache.poi.hssf.record.PaletteRecord;
import org.apache.poi.ss.formula.ptg.Area3DPtg;
import org.apache.poi.ss.formula.ptg.AreaErrPtg;
import org.apache.poi.ss.formula.ptg.MemFuncPtg;
import org.apache.poi.ss.formula.ptg.Ref3DPtg;
import org.apache.poi.ss.formula.ptg.RefErrorPtg;
import org.apache.poi.ss.formula.ptg.RefNPtg;
import org.apache.poi.ss.formula.ptg.RefPtg;

public class TIFFFaxDecoder {
    static short[] additionalMakeup = new short[]{(short) 28679, (short) 28679, (short) 31752, (short) -32759, (short) -31735, (short) -30711, (short) -29687, (short) -28663, (short) 29703, (short) 29703, (short) 30727, (short) 30727, (short) -27639, (short) -26615, (short) -25591, (short) -24567};
    static short[] black = new short[]{(short) 62, (short) 62, (short) 30, (short) 30, (short) 0, (short) 0, (short) 0, (short) 0, (short) 0, (short) 0, (short) 0, (short) 0, (short) 0, (short) 0, (short) 0, (short) 0, (short) 0, (short) 0, (short) 0, (short) 0, (short) 0, (short) 0, (short) 0, (short) 0, (short) 0, (short) 0, (short) 0, (short) 0, (short) 0, (short) 0, (short) 0, (short) 0, (short) 3225, (short) 3225, (short) 3225, (short) 3225, (short) 3225, (short) 3225, (short) 3225, (short) 3225, (short) 3225, (short) 3225, (short) 3225, (short) 3225, (short) 3225, (short) 3225, (short) 3225, (short) 3225, (short) 3225, (short) 3225, (short) 3225, (short) 3225, (short) 3225, (short) 3225, (short) 3225, (short) 3225, (short) 3225, (short) 3225, (short) 3225, (short) 3225, (short) 3225, (short) 3225, (short) 3225, (short) 3225, (short) 588, (short) 588, (short) 588, (short) 588, (short) 588, (short) 588, (short) 588, (short) 588, (short) 1680, (short) 1680, (short) 20499, (short) 22547, (short) 24595, (short) 26643, (short) 1776, (short) 1776, (short) 1808, (short) 1808, (short) -24557, (short) -22509, (short) -20461, (short) -18413, (short) 1904, (short) 1904, (short) 1936, (short) 1936, (short) -16365, (short) -14317, (short) 782, (short) 782, (short) 782, (short) 782, (short) 814, (short) 814, (short) 814, (short) 814, (short) -12269, (short) -10221, (short) 10257, (short) 10257, (short) 12305, (short) 12305, (short) 14353, (short) 14353, (short) 16403, (short) 18451, (short) 1712, (short) 1712, (short) 1744, (short) 1744, (short) 28691, (short) 30739, (short) -32749, (short) -30701, (short) -28653, (short) -26605, (short) 2061, (short) 2061, (short) 2061, (short) 2061, (short) 2061, (short) 2061, (short) 2061, (short) 2061, (short) 424, (short) 424, (short) 424, (short) 424, (short) 424, (short) 424, (short) 424, (short) 424, (short) 424, (short) 424, (short) 424, (short) 424, (short) 424, (short) 424, (short) 424, (short) 424, (short) 424, (short) 424, (short) 424, (short) 424, (short) 424, (short) 424, (short) 424, (short) 424, (short) 424, (short) 424, (short) 424, (short) 424, (short) 424, (short) 424, (short) 424, (short) 424, (short) 750, (short) 750, (short) 750, (short) 750, (short) 1616, (short) 1616, (short) 1648, (short) 1648, (short) 1424, (short) 1424, (short) 1456, (short) 1456, (short) 1488, (short) 1488, (short) 1520, (short) 1520, (short) 1840, (short) 1840, (short) 1872, (short) 1872, (short) 1968, (short) 1968, (short) 8209, (short) 8209, EscherProperties.SHADOWSTYLE__SCALEYTOY, EscherProperties.SHADOWSTYLE__SCALEYTOY, EscherProperties.SHADOWSTYLE__SCALEYTOY, EscherProperties.SHADOWSTYLE__SCALEYTOY, EscherProperties.SHADOWSTYLE__SCALEYTOY, EscherProperties.SHADOWSTYLE__SCALEYTOY, EscherProperties.SHADOWSTYLE__SCALEYTOY, EscherProperties.SHADOWSTYLE__SCALEYTOY, (short) 556, (short) 556, (short) 556, (short) 556, (short) 556, (short) 556, (short) 556, (short) 556, (short) 1552, (short) 1552, (short) 1584, (short) 1584, (short) 2000, (short) 2000, (short) 2032, (short) 2032, (short) 976, (short) 976, (short) 1008, (short) 1008, (short) 1040, (short) 1040, (short) 1072, (short) 1072, (short) 1296, (short) 1296, (short) 1328, (short) 1328, EscherProperties.THREEDSTYLE__ORIGINX, EscherProperties.THREEDSTYLE__ORIGINX, EscherProperties.THREEDSTYLE__ORIGINX, EscherProperties.THREEDSTYLE__ORIGINX, EscherProperties.LINESTYLE__FILLWIDTH, EscherProperties.LINESTYLE__FILLWIDTH, EscherProperties.LINESTYLE__FILLWIDTH, EscherProperties.LINESTYLE__FILLWIDTH, EscherProperties.LINESTYLE__FILLWIDTH, EscherProperties.LINESTYLE__FILLWIDTH, EscherProperties.LINESTYLE__FILLWIDTH, EscherProperties.LINESTYLE__FILLWIDTH, EscherProperties.LINESTYLE__FILLWIDTH, EscherProperties.LINESTYLE__FILLWIDTH, EscherProperties.LINESTYLE__FILLWIDTH, EscherProperties.LINESTYLE__FILLWIDTH, EscherProperties.LINESTYLE__FILLWIDTH, EscherProperties.LINESTYLE__FILLWIDTH, EscherProperties.LINESTYLE__FILLWIDTH, EscherProperties.LINESTYLE__FILLWIDTH, EscherProperties.LINESTYLE__FILLWIDTH, EscherProperties.LINESTYLE__FILLWIDTH, EscherProperties.LINESTYLE__FILLWIDTH, EscherProperties.LINESTYLE__FILLWIDTH, EscherProperties.LINESTYLE__FILLWIDTH, EscherProperties.LINESTYLE__FILLWIDTH, EscherProperties.LINESTYLE__FILLWIDTH, EscherProperties.LINESTYLE__FILLWIDTH, EscherProperties.LINESTYLE__FILLWIDTH, EscherProperties.LINESTYLE__FILLWIDTH, EscherProperties.LINESTYLE__FILLWIDTH, EscherProperties.LINESTYLE__FILLWIDTH, EscherProperties.LINESTYLE__FILLWIDTH, EscherProperties.LINESTYLE__FILLWIDTH, EscherProperties.LINESTYLE__FILLWIDTH, EscherProperties.LINESTYLE__FILLWIDTH, EscherProperties.GEOMETRY__SEGMENTINFO, EscherProperties.GEOMETRY__SEGMENTINFO, EscherProperties.GEOMETRY__SEGMENTINFO, EscherProperties.GEOMETRY__SEGMENTINFO, EscherProperties.GEOMETRY__SEGMENTINFO, EscherProperties.GEOMETRY__SEGMENTINFO, EscherProperties.GEOMETRY__SEGMENTINFO, EscherProperties.GEOMETRY__SEGMENTINFO, EscherProperties.GEOMETRY__SEGMENTINFO, EscherProperties.GEOMETRY__SEGMENTINFO, EscherProperties.GEOMETRY__SEGMENTINFO, EscherProperties.GEOMETRY__SEGMENTINFO, EscherProperties.GEOMETRY__SEGMENTINFO, EscherProperties.GEOMETRY__SEGMENTINFO, EscherProperties.GEOMETRY__SEGMENTINFO, EscherProperties.GEOMETRY__SEGMENTINFO, EscherProperties.GEOMETRY__SEGMENTINFO, EscherProperties.GEOMETRY__SEGMENTINFO, EscherProperties.GEOMETRY__SEGMENTINFO, EscherProperties.GEOMETRY__SEGMENTINFO, EscherProperties.GEOMETRY__SEGMENTINFO, EscherProperties.GEOMETRY__SEGMENTINFO, EscherProperties.GEOMETRY__SEGMENTINFO, EscherProperties.GEOMETRY__SEGMENTINFO, EscherProperties.GEOMETRY__SEGMENTINFO, EscherProperties.GEOMETRY__SEGMENTINFO, EscherProperties.GEOMETRY__SEGMENTINFO, EscherProperties.GEOMETRY__SEGMENTINFO, EscherProperties.GEOMETRY__SEGMENTINFO, EscherProperties.GEOMETRY__SEGMENTINFO, EscherProperties.GEOMETRY__SEGMENTINFO, EscherProperties.GEOMETRY__SEGMENTINFO, EscherProperties.GEOMETRY__SEGMENTINFO, EscherProperties.GEOMETRY__SEGMENTINFO, EscherProperties.GEOMETRY__SEGMENTINFO, EscherProperties.GEOMETRY__SEGMENTINFO, EscherProperties.GEOMETRY__SEGMENTINFO, EscherProperties.GEOMETRY__SEGMENTINFO, EscherProperties.GEOMETRY__SEGMENTINFO, EscherProperties.GEOMETRY__SEGMENTINFO, EscherProperties.GEOMETRY__SEGMENTINFO, EscherProperties.GEOMETRY__SEGMENTINFO, EscherProperties.GEOMETRY__SEGMENTINFO, EscherProperties.GEOMETRY__SEGMENTINFO, EscherProperties.GEOMETRY__SEGMENTINFO, EscherProperties.GEOMETRY__SEGMENTINFO, EscherProperties.GEOMETRY__SEGMENTINFO, EscherProperties.GEOMETRY__SEGMENTINFO, EscherProperties.GEOMETRY__SEGMENTINFO, EscherProperties.GEOMETRY__SEGMENTINFO, EscherProperties.GEOMETRY__SEGMENTINFO, EscherProperties.GEOMETRY__SEGMENTINFO, EscherProperties.GEOMETRY__SEGMENTINFO, EscherProperties.GEOMETRY__SEGMENTINFO, EscherProperties.GEOMETRY__SEGMENTINFO, EscherProperties.GEOMETRY__SEGMENTINFO, EscherProperties.GEOMETRY__SEGMENTINFO, EscherProperties.GEOMETRY__SEGMENTINFO, EscherProperties.GEOMETRY__SEGMENTINFO, EscherProperties.GEOMETRY__SEGMENTINFO, EscherProperties.GEOMETRY__SEGMENTINFO, EscherProperties.GEOMETRY__SEGMENTINFO, EscherProperties.GEOMETRY__SEGMENTINFO, EscherProperties.GEOMETRY__SEGMENTINFO, (short) 358, (short) 358, (short) 358, (short) 358, (short) 358, (short) 358, (short) 358, (short) 358, (short) 358, (short) 358, (short) 358, (short) 358, (short) 358, (short) 358, (short) 358, (short) 358, (short) 358, (short) 358, (short) 358, (short) 358, (short) 358, (short) 358, (short) 358, (short) 358, (short) 358, (short) 358, (short) 358, (short) 358, (short) 358, (short) 358, (short) 358, (short) 358, (short) 358, (short) 358, (short) 358, (short) 358, (short) 358, (short) 358, (short) 358, (short) 358, (short) 358, (short) 358, (short) 358, (short) 358, (short) 358, (short) 358, (short) 358, (short) 358, (short) 358, (short) 358, (short) 358, (short) 358, (short) 358, (short) 358, (short) 358, (short) 358, (short) 358, (short) 358, (short) 358, (short) 358, (short) 358, (short) 358, (short) 358, (short) 358, (short) 490, (short) 490, (short) 490, (short) 490, (short) 490, (short) 490, (short) 490, (short) 490, (short) 490, (short) 490, (short) 490, (short) 490, (short) 490, (short) 490, (short) 490, (short) 490, (short) 4113, (short) 4113, (short) 6161, (short) 6161, (short) 848, (short) 848, (short) 880, (short) 880, EscherProperties.GROUPSHAPE__POSRELH, EscherProperties.GROUPSHAPE__POSRELH, (short) 944, (short) 944, (short) 622, (short) 622, (short) 622, (short) 622, (short) 654, (short) 654, (short) 654, (short) 654, (short) 1104, (short) 1104, (short) 1136, (short) 1136, (short) 1168, (short) 1168, CodepageRecord.CODEPAGE, CodepageRecord.CODEPAGE, (short) 1232, (short) 1232, (short) 1264, (short) 1264, (short) 686, (short) 686, (short) 686, (short) 686, (short) 1360, (short) 1360, (short) 1392, (short) 1392, (short) 12, (short) 12, (short) 12, (short) 12, (short) 12, (short) 12, (short) 12, (short) 12, (short) 390, (short) 390, (short) 390, (short) 390, (short) 390, (short) 390, (short) 390, (short) 390, (short) 390, (short) 390, (short) 390, (short) 390, (short) 390, (short) 390, (short) 390, (short) 390, (short) 390, (short) 390, (short) 390, (short) 390, (short) 390, (short) 390, (short) 390, (short) 390, (short) 390, (short) 390, (short) 390, (short) 390, (short) 390, (short) 390, (short) 390, (short) 390, (short) 390, (short) 390, (short) 390, (short) 390, (short) 390, (short) 390, (short) 390, (short) 390, (short) 390, (short) 390, (short) 390, (short) 390, (short) 390, (short) 390, (short) 390, (short) 390, (short) 390, (short) 390, (short) 390, (short) 390, (short) 390, (short) 390, (short) 390, (short) 390, (short) 390, (short) 390, (short) 390, (short) 390, (short) 390, (short) 390, (short) 390, (short) 390};
    static byte[] flipTable = new byte[]{(byte) 0, UnsignedBytes.MAX_POWER_OF_TWO, (byte) 64, (byte) -64, (byte) 32, ProbeParams.ALERT_VOTAGE_LOW_DEFAULT, (byte) 96, (byte) -32, (byte) 16, (byte) -112, (byte) 80, (byte) -48, ByteBuffer.ZERO, (byte) -80, (byte) 112, (byte) -16, (byte) 8, (byte) -120, (byte) 72, (byte) -56, (byte) 40, ProbeParams.ALERT_VOTAGE_CANCEL_DEFAULT, (byte) 104, (byte) -24, Ascii.CAN, (byte) -104, (byte) 88, (byte) -40, PaletteRecord.STANDARD_PALETTE_SIZE, (byte) -72, (byte) 120, (byte) -8, (byte) 4, (byte) -124, (byte) 68, (byte) -60, RefPtg.sid, (byte) -92, (byte) 100, (byte) -28, (byte) 20, (byte) -108, (byte) 84, (byte) -44, (byte) 52, (byte) -76, (byte) 116, (byte) -12, (byte) 12, (byte) -116, (byte) 76, (byte) -52, RefNPtg.sid, (byte) -84, (byte) 108, (byte) -20, Ascii.FS, (byte) -100, (byte) 92, (byte) -36, (byte) 60, (byte) -68, (byte) 124, (byte) -4, (byte) 2, (byte) -126, (byte) 66, (byte) -62, (byte) 34, (byte) -94, (byte) 98, (byte) -30, (byte) 18, (byte) -110, (byte) 82, (byte) -46, (byte) 50, (byte) -78, (byte) 114, (byte) -14, (byte) 10, (byte) -118, (byte) 74, (byte) -54, RefErrorPtg.sid, (byte) -86, (byte) 106, (byte) -22, Ascii.SUB, (byte) -102, (byte) 90, (byte) -38, Ref3DPtg.sid, (byte) -70, (byte) 122, (byte) -6, (byte) 6, (byte) -122, (byte) 70, (byte) -58, (byte) 38, (byte) -90, (byte) 102, (byte) -26, (byte) 22, (byte) -106, (byte) 86, (byte) -42, (byte) 54, (byte) -74, (byte) 118, (byte) -10, (byte) 14, (byte) -114, (byte) 78, (byte) -50, (byte) 46, (byte) -82, (byte) 110, (byte) -18, (byte) 30, (byte) -98, (byte) 94, (byte) -34, DocWriter.GT, (byte) -66, (byte) 126, (byte) -2, (byte) 1, (byte) -127, (byte) 65, (byte) -63, (byte) 33, (byte) -95, (byte) 97, (byte) -31, (byte) 17, (byte) -111, (byte) 81, (byte) -47, (byte) 49, (byte) -79, (byte) 113, (byte) -15, (byte) 9, (byte) -119, (byte) 73, (byte) -55, MemFuncPtg.sid, (byte) -87, (byte) 105, (byte) -23, (byte) 25, (byte) -103, (byte) 89, (byte) -39, (byte) 57, (byte) -71, (byte) 121, (byte) -7, (byte) 5, (byte) -123, (byte) 69, (byte) -59, (byte) 37, (byte) -91, (byte) 101, (byte) -27, (byte) 21, (byte) -107, (byte) 85, (byte) -43, (byte) 53, (byte) -75, (byte) 117, (byte) -11, (byte) 13, (byte) -115, (byte) 77, (byte) -51, (byte) 45, (byte) -83, (byte) 109, (byte) -19, (byte) 29, (byte) -99, (byte) 93, (byte) -35, (byte) 61, (byte) -67, (byte) 125, (byte) -3, (byte) 3, (byte) -125, (byte) 67, (byte) -61, (byte) 35, (byte) -93, (byte) 99, (byte) -29, (byte) 19, (byte) -109, (byte) 83, (byte) -45, (byte) 51, (byte) -77, (byte) 115, (byte) -13, (byte) 11, (byte) -117, (byte) 75, (byte) -53, AreaErrPtg.sid, (byte) -85, (byte) 107, (byte) -21, Ascii.ESC, (byte) -101, (byte) 91, (byte) -37, Area3DPtg.sid, (byte) -69, (byte) 123, (byte) -5, (byte) 7, (byte) -121, (byte) 71, (byte) -57, (byte) 39, (byte) -89, (byte) 103, (byte) -25, (byte) 23, (byte) -105, (byte) 87, (byte) -41, (byte) 55, (byte) -73, (byte) 119, (byte) -9, (byte) 15, (byte) -113, (byte) 79, (byte) -49, DocWriter.FORWARD, (byte) -81, (byte) 111, (byte) -17, (byte) 31, (byte) -97, (byte) 95, (byte) -33, (byte) 63, (byte) -65, Ascii.DEL, (byte) -1};
    static short[] initBlack = new short[]{(short) 3226, (short) 6412, EscherAggregate.ST_ACTIONBUTTONMOVIE, EscherAggregate.ST_TEXTFADERIGHT, (short) 38, (short) 38, (short) 134, (short) 134, (short) 100, (short) 100, (short) 100, (short) 100, (short) 68, (short) 68, (short) 68, (short) 68};
    static int[] table1 = new int[]{0, 1, 3, 7, 15, 31, 63, 127, 255};
    static int[] table2 = new int[]{0, 128, 192, 224, 240, 248, 252, 254, 255};
    static short[] twoBitBlack = new short[]{(short) 292, (short) 260, InterfaceEndRecord.sid, InterfaceEndRecord.sid};
    static byte[] twoDCodes = new byte[]{(byte) 80, (byte) 88, (byte) 23, (byte) 71, (byte) 30, (byte) 30, DocWriter.GT, DocWriter.GT, (byte) 4, (byte) 4, (byte) 4, (byte) 4, (byte) 4, (byte) 4, (byte) 4, (byte) 4, (byte) 11, (byte) 11, (byte) 11, (byte) 11, (byte) 11, (byte) 11, (byte) 11, (byte) 11, (byte) 11, (byte) 11, (byte) 11, (byte) 11, (byte) 11, (byte) 11, (byte) 11, (byte) 11, (byte) 35, (byte) 35, (byte) 35, (byte) 35, (byte) 35, (byte) 35, (byte) 35, (byte) 35, (byte) 35, (byte) 35, (byte) 35, (byte) 35, (byte) 35, (byte) 35, (byte) 35, (byte) 35, (byte) 51, (byte) 51, (byte) 51, (byte) 51, (byte) 51, (byte) 51, (byte) 51, (byte) 51, (byte) 51, (byte) 51, (byte) 51, (byte) 51, (byte) 51, (byte) 51, (byte) 51, (byte) 51, MemFuncPtg.sid, MemFuncPtg.sid, MemFuncPtg.sid, MemFuncPtg.sid, MemFuncPtg.sid, MemFuncPtg.sid, MemFuncPtg.sid, MemFuncPtg.sid, MemFuncPtg.sid, MemFuncPtg.sid, MemFuncPtg.sid, MemFuncPtg.sid, MemFuncPtg.sid, MemFuncPtg.sid, MemFuncPtg.sid, MemFuncPtg.sid, MemFuncPtg.sid, MemFuncPtg.sid, MemFuncPtg.sid, MemFuncPtg.sid, MemFuncPtg.sid, MemFuncPtg.sid, MemFuncPtg.sid, MemFuncPtg.sid, MemFuncPtg.sid, MemFuncPtg.sid, MemFuncPtg.sid, MemFuncPtg.sid, MemFuncPtg.sid, MemFuncPtg.sid, MemFuncPtg.sid, MemFuncPtg.sid, MemFuncPtg.sid, MemFuncPtg.sid, MemFuncPtg.sid, MemFuncPtg.sid, MemFuncPtg.sid, MemFuncPtg.sid, MemFuncPtg.sid, MemFuncPtg.sid, MemFuncPtg.sid, MemFuncPtg.sid, MemFuncPtg.sid, MemFuncPtg.sid, MemFuncPtg.sid, MemFuncPtg.sid, MemFuncPtg.sid, MemFuncPtg.sid, MemFuncPtg.sid, MemFuncPtg.sid, MemFuncPtg.sid, MemFuncPtg.sid, MemFuncPtg.sid, MemFuncPtg.sid, MemFuncPtg.sid, MemFuncPtg.sid, MemFuncPtg.sid, MemFuncPtg.sid, MemFuncPtg.sid, MemFuncPtg.sid, MemFuncPtg.sid, MemFuncPtg.sid, MemFuncPtg.sid, MemFuncPtg.sid};
    static short[] white = new short[]{(short) 6430, (short) 6400, (short) 6400, (short) 6400, (short) 3225, (short) 3225, (short) 3225, (short) 3225, (short) 944, (short) 944, (short) 944, (short) 944, (short) 976, (short) 976, (short) 976, (short) 976, (short) 1456, (short) 1456, (short) 1456, (short) 1456, (short) 1488, (short) 1488, (short) 1488, (short) 1488, EscherProperties.THREEDSTYLE__ORIGINX, EscherProperties.THREEDSTYLE__ORIGINX, EscherProperties.THREEDSTYLE__ORIGINX, EscherProperties.THREEDSTYLE__ORIGINX, EscherProperties.THREEDSTYLE__ORIGINX, EscherProperties.THREEDSTYLE__ORIGINX, EscherProperties.THREEDSTYLE__ORIGINX, EscherProperties.THREEDSTYLE__ORIGINX, (short) 750, (short) 750, (short) 750, (short) 750, (short) 750, (short) 750, (short) 750, (short) 750, (short) 1520, (short) 1520, (short) 1520, (short) 1520, (short) 1552, (short) 1552, (short) 1552, (short) 1552, (short) 428, (short) 428, (short) 428, (short) 428, (short) 428, (short) 428, (short) 428, (short) 428, (short) 428, (short) 428, (short) 428, (short) 428, (short) 428, (short) 428, (short) 428, (short) 428, (short) 654, (short) 654, (short) 654, (short) 654, (short) 654, (short) 654, (short) 654, (short) 654, (short) 1072, (short) 1072, (short) 1072, (short) 1072, (short) 1104, (short) 1104, (short) 1104, (short) 1104, (short) 1136, (short) 1136, (short) 1136, (short) 1136, (short) 1168, (short) 1168, (short) 1168, (short) 1168, CodepageRecord.CODEPAGE, CodepageRecord.CODEPAGE, CodepageRecord.CODEPAGE, CodepageRecord.CODEPAGE, (short) 1232, (short) 1232, (short) 1232, (short) 1232, (short) 622, (short) 622, (short) 622, (short) 622, (short) 622, (short) 622, (short) 622, (short) 622, (short) 1008, (short) 1008, (short) 1008, (short) 1008, (short) 1040, (short) 1040, (short) 1040, (short) 1040, (short) 44, (short) 44, (short) 44, (short) 44, (short) 44, (short) 44, (short) 44, (short) 44, (short) 44, (short) 44, (short) 44, (short) 44, (short) 44, (short) 44, (short) 44, (short) 44, EscherProperties.FILL__FOCUS, EscherProperties.FILL__FOCUS, EscherProperties.FILL__FOCUS, EscherProperties.FILL__FOCUS, EscherProperties.FILL__FOCUS, EscherProperties.FILL__FOCUS, EscherProperties.FILL__FOCUS, EscherProperties.FILL__FOCUS, EscherProperties.FILL__FOCUS, EscherProperties.FILL__FOCUS, EscherProperties.FILL__FOCUS, EscherProperties.FILL__FOCUS, EscherProperties.FILL__FOCUS, EscherProperties.FILL__FOCUS, EscherProperties.FILL__FOCUS, EscherProperties.FILL__FOCUS, (short) 1712, (short) 1712, (short) 1712, (short) 1712, (short) 1744, (short) 1744, (short) 1744, (short) 1744, (short) 846, (short) 846, (short) 846, (short) 846, (short) 846, (short) 846, (short) 846, (short) 846, (short) 1264, (short) 1264, (short) 1264, (short) 1264, (short) 1296, (short) 1296, (short) 1296, (short) 1296, (short) 1328, (short) 1328, (short) 1328, (short) 1328, (short) 1360, (short) 1360, (short) 1360, (short) 1360, (short) 1392, (short) 1392, (short) 1392, (short) 1392, (short) 1424, (short) 1424, (short) 1424, (short) 1424, (short) 686, (short) 686, (short) 686, (short) 686, (short) 686, (short) 686, (short) 686, (short) 686, EscherProperties.GROUPSHAPE__SCRIPT, EscherProperties.GROUPSHAPE__SCRIPT, EscherProperties.GROUPSHAPE__SCRIPT, EscherProperties.GROUPSHAPE__SCRIPT, EscherProperties.GROUPSHAPE__SCRIPT, EscherProperties.GROUPSHAPE__SCRIPT, EscherProperties.GROUPSHAPE__SCRIPT, EscherProperties.GROUPSHAPE__SCRIPT, (short) 1968, (short) 1968, (short) 1968, (short) 1968, (short) 2000, (short) 2000, (short) 2000, (short) 2000, (short) 2032, (short) 2032, (short) 2032, (short) 2032, (short) 16, (short) 16, (short) 16, (short) 16, (short) 10257, (short) 10257, (short) 10257, (short) 10257, (short) 12305, (short) 12305, (short) 12305, (short) 12305, EscherProperties.GEOMETRY__ADJUST4VALUE, EscherProperties.GEOMETRY__ADJUST4VALUE, EscherProperties.GEOMETRY__ADJUST4VALUE, EscherProperties.GEOMETRY__ADJUST4VALUE, EscherProperties.GEOMETRY__ADJUST4VALUE, EscherProperties.GEOMETRY__ADJUST4VALUE, EscherProperties.GEOMETRY__ADJUST4VALUE, EscherProperties.GEOMETRY__ADJUST4VALUE, EscherProperties.GEOMETRY__ADJUST4VALUE, EscherProperties.GEOMETRY__ADJUST4VALUE, EscherProperties.GEOMETRY__ADJUST4VALUE, EscherProperties.GEOMETRY__ADJUST4VALUE, EscherProperties.GEOMETRY__ADJUST4VALUE, EscherProperties.GEOMETRY__ADJUST4VALUE, EscherProperties.GEOMETRY__ADJUST4VALUE, EscherProperties.GEOMETRY__ADJUST4VALUE, EscherProperties.GEOMETRY__ADJUST4VALUE, EscherProperties.GEOMETRY__ADJUST4VALUE, EscherProperties.GEOMETRY__ADJUST4VALUE, EscherProperties.GEOMETRY__ADJUST4VALUE, EscherProperties.GEOMETRY__ADJUST4VALUE, EscherProperties.GEOMETRY__ADJUST4VALUE, EscherProperties.GEOMETRY__ADJUST4VALUE, EscherProperties.GEOMETRY__ADJUST4VALUE, EscherProperties.GEOMETRY__ADJUST4VALUE, EscherProperties.GEOMETRY__ADJUST4VALUE, EscherProperties.GEOMETRY__ADJUST4VALUE, EscherProperties.GEOMETRY__ADJUST4VALUE, EscherProperties.GEOMETRY__ADJUST4VALUE, EscherProperties.GEOMETRY__ADJUST4VALUE, EscherProperties.GEOMETRY__ADJUST4VALUE, EscherProperties.GEOMETRY__ADJUST4VALUE, (short) 362, (short) 362, (short) 362, (short) 362, (short) 362, (short) 362, (short) 362, (short) 362, (short) 362, (short) 362, (short) 362, (short) 362, (short) 362, (short) 362, (short) 362, (short) 362, (short) 362, (short) 362, (short) 362, (short) 362, (short) 362, (short) 362, (short) 362, (short) 362, (short) 362, (short) 362, (short) 362, (short) 362, (short) 362, (short) 362, (short) 362, (short) 362, (short) 878, (short) 878, (short) 878, (short) 878, (short) 878, (short) 878, (short) 878, (short) 878, (short) 1904, (short) 1904, (short) 1904, (short) 1904, (short) 1936, (short) 1936, (short) 1936, (short) 1936, (short) -18413, (short) -18413, (short) -16365, (short) -16365, (short) -14317, (short) -14317, (short) -10221, (short) -10221, (short) 590, (short) 590, (short) 590, (short) 590, (short) 590, (short) 590, (short) 590, (short) 590, (short) 782, (short) 782, (short) 782, (short) 782, (short) 782, (short) 782, (short) 782, (short) 782, (short) 1584, (short) 1584, (short) 1584, (short) 1584, (short) 1616, (short) 1616, (short) 1616, (short) 1616, (short) 1648, (short) 1648, (short) 1648, (short) 1648, (short) 1680, (short) 1680, (short) 1680, (short) 1680, (short) 814, (short) 814, (short) 814, (short) 814, (short) 814, (short) 814, (short) 814, (short) 814, (short) 1776, (short) 1776, (short) 1776, (short) 1776, (short) 1808, (short) 1808, (short) 1808, (short) 1808, (short) 1840, (short) 1840, (short) 1840, (short) 1840, (short) 1872, (short) 1872, (short) 1872, (short) 1872, (short) 6157, (short) 6157, (short) 6157, (short) 6157, (short) 6157, (short) 6157, (short) 6157, (short) 6157, (short) 6157, (short) 6157, (short) 6157, (short) 6157, (short) 6157, (short) 6157, (short) 6157, (short) 6157, (short) -12275, (short) -12275, (short) -12275, (short) -12275, (short) -12275, (short) -12275, (short) -12275, (short) -12275, (short) -12275, (short) -12275, (short) -12275, (short) -12275, (short) -12275, (short) -12275, (short) -12275, (short) -12275, (short) 14353, (short) 14353, (short) 14353, (short) 14353, (short) 16401, (short) 16401, (short) 16401, (short) 16401, (short) 22547, (short) 22547, (short) 24595, (short) 24595, (short) 20497, (short) 20497, (short) 20497, (short) 20497, (short) 18449, (short) 18449, (short) 18449, (short) 18449, (short) 26643, (short) 26643, (short) 28691, (short) 28691, (short) 30739, (short) 30739, (short) -32749, (short) -32749, (short) -30701, (short) -30701, (short) -28653, (short) -28653, (short) -26605, (short) -26605, (short) -24557, (short) -24557, (short) -22509, (short) -22509, (short) -20461, (short) -20461, (short) 8207, (short) 8207, (short) 8207, (short) 8207, (short) 8207, (short) 8207, (short) 8207, (short) 8207, (short) 72, (short) 72, (short) 72, (short) 72, (short) 72, (short) 72, (short) 72, (short) 72, (short) 72, (short) 72, (short) 72, (short) 72, (short) 72, (short) 72, (short) 72, (short) 72, (short) 72, (short) 72, (short) 72, (short) 72, (short) 72, (short) 72, (short) 72, (short) 72, (short) 72, (short) 72, (short) 72, (short) 72, (short) 72, (short) 72, (short) 72, (short) 72, (short) 72, (short) 72, (short) 72, (short) 72, (short) 72, (short) 72, (short) 72, (short) 72, (short) 72, (short) 72, (short) 72, (short) 72, (short) 72, (short) 72, (short) 72, (short) 72, (short) 72, (short) 72, (short) 72, (short) 72, (short) 72, (short) 72, (short) 72, (short) 72, (short) 72, (short) 72, (short) 72, (short) 72, (short) 72, (short) 72, (short) 72, (short) 72, EscherAggregate.ST_CURVEDUPARROW, EscherAggregate.ST_CURVEDUPARROW, EscherAggregate.ST_CURVEDUPARROW, EscherAggregate.ST_CURVEDUPARROW, EscherAggregate.ST_CURVEDUPARROW, EscherAggregate.ST_CURVEDUPARROW, EscherAggregate.ST_CURVEDUPARROW, EscherAggregate.ST_CURVEDUPARROW, EscherAggregate.ST_CURVEDUPARROW, EscherAggregate.ST_CURVEDUPARROW, EscherAggregate.ST_CURVEDUPARROW, EscherAggregate.ST_CURVEDUPARROW, EscherAggregate.ST_CURVEDUPARROW, EscherAggregate.ST_CURVEDUPARROW, EscherAggregate.ST_CURVEDUPARROW, EscherAggregate.ST_CURVEDUPARROW, EscherAggregate.ST_CURVEDUPARROW, EscherAggregate.ST_CURVEDUPARROW, EscherAggregate.ST_CURVEDUPARROW, EscherAggregate.ST_CURVEDUPARROW, EscherAggregate.ST_CURVEDUPARROW, EscherAggregate.ST_CURVEDUPARROW, EscherAggregate.ST_CURVEDUPARROW, EscherAggregate.ST_CURVEDUPARROW, EscherAggregate.ST_CURVEDUPARROW, EscherAggregate.ST_CURVEDUPARROW, EscherAggregate.ST_CURVEDUPARROW, EscherAggregate.ST_CURVEDUPARROW, EscherAggregate.ST_CURVEDUPARROW, EscherAggregate.ST_CURVEDUPARROW, EscherAggregate.ST_CURVEDUPARROW, EscherAggregate.ST_CURVEDUPARROW, EscherAggregate.ST_CURVEDUPARROW, EscherAggregate.ST_CURVEDUPARROW, EscherAggregate.ST_CURVEDUPARROW, EscherAggregate.ST_CURVEDUPARROW, EscherAggregate.ST_CURVEDUPARROW, EscherAggregate.ST_CURVEDUPARROW, EscherAggregate.ST_CURVEDUPARROW, EscherAggregate.ST_CURVEDUPARROW, EscherAggregate.ST_CURVEDUPARROW, EscherAggregate.ST_CURVEDUPARROW, EscherAggregate.ST_CURVEDUPARROW, EscherAggregate.ST_CURVEDUPARROW, EscherAggregate.ST_CURVEDUPARROW, EscherAggregate.ST_CURVEDUPARROW, EscherAggregate.ST_CURVEDUPARROW, EscherAggregate.ST_CURVEDUPARROW, EscherAggregate.ST_CURVEDUPARROW, EscherAggregate.ST_CURVEDUPARROW, EscherAggregate.ST_CURVEDUPARROW, EscherAggregate.ST_CURVEDUPARROW, EscherAggregate.ST_CURVEDUPARROW, EscherAggregate.ST_CURVEDUPARROW, EscherAggregate.ST_CURVEDUPARROW, EscherAggregate.ST_CURVEDUPARROW, EscherAggregate.ST_CURVEDUPARROW, EscherAggregate.ST_CURVEDUPARROW, EscherAggregate.ST_CURVEDUPARROW, EscherAggregate.ST_CURVEDUPARROW, EscherAggregate.ST_CURVEDUPARROW, EscherAggregate.ST_CURVEDUPARROW, EscherAggregate.ST_CURVEDUPARROW, EscherAggregate.ST_CURVEDUPARROW, (short) 4107, (short) 4107, (short) 4107, (short) 4107, (short) 4107, (short) 4107, (short) 4107, (short) 4107, (short) 4107, (short) 4107, (short) 4107, (short) 4107, (short) 4107, (short) 4107, (short) 4107, (short) 4107, (short) 4107, (short) 4107, (short) 4107, (short) 4107, (short) 4107, (short) 4107, (short) 4107, (short) 4107, (short) 4107, (short) 4107, (short) 4107, (short) 4107, (short) 4107, (short) 4107, (short) 4107, (short) 4107, EscherProperties.BLIP__GAMMA, EscherProperties.BLIP__GAMMA, EscherProperties.BLIP__GAMMA, EscherProperties.BLIP__GAMMA, EscherProperties.BLIP__GAMMA, EscherProperties.BLIP__GAMMA, EscherProperties.BLIP__GAMMA, EscherProperties.BLIP__GAMMA, EscherProperties.BLIP__GAMMA, EscherProperties.BLIP__GAMMA, EscherProperties.BLIP__GAMMA, EscherProperties.BLIP__GAMMA, EscherProperties.BLIP__GAMMA, EscherProperties.BLIP__GAMMA, EscherProperties.BLIP__GAMMA, EscherProperties.BLIP__GAMMA, EscherProperties.BLIP__GAMMA, EscherProperties.BLIP__GAMMA, EscherProperties.BLIP__GAMMA, EscherProperties.BLIP__GAMMA, EscherProperties.BLIP__GAMMA, EscherProperties.BLIP__GAMMA, EscherProperties.BLIP__GAMMA, EscherProperties.BLIP__GAMMA, EscherProperties.BLIP__GAMMA, EscherProperties.BLIP__GAMMA, EscherProperties.BLIP__GAMMA, EscherProperties.BLIP__GAMMA, EscherProperties.BLIP__GAMMA, EscherProperties.BLIP__GAMMA, EscherProperties.BLIP__GAMMA, EscherProperties.BLIP__GAMMA, (short) 298, (short) 298, (short) 298, (short) 298, (short) 298, (short) 298, (short) 298, (short) 298, (short) 298, (short) 298, (short) 298, (short) 298, (short) 298, (short) 298, (short) 298, (short) 298, (short) 298, (short) 298, (short) 298, (short) 298, (short) 298, (short) 298, (short) 298, (short) 298, (short) 298, (short) 298, (short) 298, (short) 298, (short) 298, (short) 298, (short) 298, (short) 298, EscherProperties.SHADOWSTYLE__SCALEYTOY, EscherProperties.SHADOWSTYLE__SCALEYTOY, EscherProperties.SHADOWSTYLE__SCALEYTOY, EscherProperties.SHADOWSTYLE__SCALEYTOY, EscherProperties.SHADOWSTYLE__SCALEYTOY, EscherProperties.SHADOWSTYLE__SCALEYTOY, EscherProperties.SHADOWSTYLE__SCALEYTOY, EscherProperties.SHADOWSTYLE__SCALEYTOY, EscherProperties.SHADOWSTYLE__SCALEYTOY, EscherProperties.SHADOWSTYLE__SCALEYTOY, EscherProperties.SHADOWSTYLE__SCALEYTOY, EscherProperties.SHADOWSTYLE__SCALEYTOY, EscherProperties.SHADOWSTYLE__SCALEYTOY, EscherProperties.SHADOWSTYLE__SCALEYTOY, EscherProperties.SHADOWSTYLE__SCALEYTOY, EscherProperties.SHADOWSTYLE__SCALEYTOY, (short) 556, (short) 556, (short) 556, (short) 556, (short) 556, (short) 556, (short) 556, (short) 556, (short) 556, (short) 556, (short) 556, (short) 556, (short) 556, (short) 556, (short) 556, (short) 556, (short) 136, (short) 136, (short) 136, (short) 136, (short) 136, (short) 136, (short) 136, (short) 136, (short) 136, (short) 136, (short) 136, (short) 136, (short) 136, (short) 136, (short) 136, (short) 136, (short) 136, (short) 136, (short) 136, (short) 136, (short) 136, (short) 136, (short) 136, (short) 136, (short) 136, (short) 136, (short) 136, (short) 136, (short) 136, (short) 136, (short) 136, (short) 136, (short) 136, (short) 136, (short) 136, (short) 136, (short) 136, (short) 136, (short) 136, (short) 136, (short) 136, (short) 136, (short) 136, (short) 136, (short) 136, (short) 136, (short) 136, (short) 136, (short) 136, (short) 136, (short) 136, (short) 136, (short) 136, (short) 136, (short) 136, (short) 136, (short) 136, (short) 136, (short) 136, (short) 136, (short) 136, (short) 136, (short) 136, (short) 136, EscherAggregate.ST_TEXTFADERIGHT, EscherAggregate.ST_TEXTFADERIGHT, EscherAggregate.ST_TEXTFADERIGHT, EscherAggregate.ST_TEXTFADERIGHT, EscherAggregate.ST_TEXTFADERIGHT, EscherAggregate.ST_TEXTFADERIGHT, EscherAggregate.ST_TEXTFADERIGHT, EscherAggregate.ST_TEXTFADERIGHT, EscherAggregate.ST_TEXTFADERIGHT, EscherAggregate.ST_TEXTFADERIGHT, EscherAggregate.ST_TEXTFADERIGHT, EscherAggregate.ST_TEXTFADERIGHT, EscherAggregate.ST_TEXTFADERIGHT, EscherAggregate.ST_TEXTFADERIGHT, EscherAggregate.ST_TEXTFADERIGHT, EscherAggregate.ST_TEXTFADERIGHT, EscherAggregate.ST_TEXTFADERIGHT, EscherAggregate.ST_TEXTFADERIGHT, EscherAggregate.ST_TEXTFADERIGHT, EscherAggregate.ST_TEXTFADERIGHT, EscherAggregate.ST_TEXTFADERIGHT, EscherAggregate.ST_TEXTFADERIGHT, EscherAggregate.ST_TEXTFADERIGHT, EscherAggregate.ST_TEXTFADERIGHT, EscherAggregate.ST_TEXTFADERIGHT, EscherAggregate.ST_TEXTFADERIGHT, EscherAggregate.ST_TEXTFADERIGHT, EscherAggregate.ST_TEXTFADERIGHT, EscherAggregate.ST_TEXTFADERIGHT, EscherAggregate.ST_TEXTFADERIGHT, EscherAggregate.ST_TEXTFADERIGHT, EscherAggregate.ST_TEXTFADERIGHT, EscherAggregate.ST_TEXTFADERIGHT, EscherAggregate.ST_TEXTFADERIGHT, EscherAggregate.ST_TEXTFADERIGHT, EscherAggregate.ST_TEXTFADERIGHT, EscherAggregate.ST_TEXTFADERIGHT, EscherAggregate.ST_TEXTFADERIGHT, EscherAggregate.ST_TEXTFADERIGHT, EscherAggregate.ST_TEXTFADERIGHT, EscherAggregate.ST_TEXTFADERIGHT, EscherAggregate.ST_TEXTFADERIGHT, EscherAggregate.ST_TEXTFADERIGHT, EscherAggregate.ST_TEXTFADERIGHT, EscherAggregate.ST_TEXTFADERIGHT, EscherAggregate.ST_TEXTFADERIGHT, EscherAggregate.ST_TEXTFADERIGHT, EscherAggregate.ST_TEXTFADERIGHT, EscherAggregate.ST_TEXTFADERIGHT, EscherAggregate.ST_TEXTFADERIGHT, EscherAggregate.ST_TEXTFADERIGHT, EscherAggregate.ST_TEXTFADERIGHT, EscherAggregate.ST_TEXTFADERIGHT, EscherAggregate.ST_TEXTFADERIGHT, EscherAggregate.ST_TEXTFADERIGHT, EscherAggregate.ST_TEXTFADERIGHT, EscherAggregate.ST_TEXTFADERIGHT, EscherAggregate.ST_TEXTFADERIGHT, EscherAggregate.ST_TEXTFADERIGHT, EscherAggregate.ST_TEXTFADERIGHT, EscherAggregate.ST_TEXTFADERIGHT, EscherAggregate.ST_TEXTFADERIGHT, EscherAggregate.ST_TEXTFADERIGHT, EscherAggregate.ST_TEXTFADERIGHT, EscherProperties.LINESTYLE__LINEMITERLIMIT, EscherProperties.LINESTYLE__LINEMITERLIMIT, EscherProperties.LINESTYLE__LINEMITERLIMIT, EscherProperties.LINESTYLE__LINEMITERLIMIT, EscherProperties.LINESTYLE__LINEMITERLIMIT, EscherProperties.LINESTYLE__LINEMITERLIMIT, EscherProperties.LINESTYLE__LINEMITERLIMIT, EscherProperties.LINESTYLE__LINEMITERLIMIT, EscherProperties.LINESTYLE__LINEMITERLIMIT, EscherProperties.LINESTYLE__LINEMITERLIMIT, EscherProperties.LINESTYLE__LINEMITERLIMIT, EscherProperties.LINESTYLE__LINEMITERLIMIT, EscherProperties.LINESTYLE__LINEMITERLIMIT, EscherProperties.LINESTYLE__LINEMITERLIMIT, EscherProperties.LINESTYLE__LINEMITERLIMIT, EscherProperties.LINESTYLE__LINEMITERLIMIT, (short) 492, (short) 492, (short) 492, (short) 492, (short) 492, (short) 492, (short) 492, (short) 492, (short) 492, (short) 492, (short) 492, (short) 492, (short) 492, (short) 492, (short) 492, (short) 492, (short) 2059, (short) 2059, (short) 2059, (short) 2059, (short) 2059, (short) 2059, (short) 2059, (short) 2059, (short) 2059, (short) 2059, (short) 2059, (short) 2059, (short) 2059, (short) 2059, (short) 2059, (short) 2059, (short) 2059, (short) 2059, (short) 2059, (short) 2059, (short) 2059, (short) 2059, (short) 2059, (short) 2059, (short) 2059, (short) 2059, (short) 2059, (short) 2059, (short) 2059, (short) 2059, (short) 2059, (short) 2059, EscherAggregate.ST_ACTIONBUTTONMOVIE, EscherAggregate.ST_ACTIONBUTTONMOVIE, EscherAggregate.ST_ACTIONBUTTONMOVIE, EscherAggregate.ST_ACTIONBUTTONMOVIE, EscherAggregate.ST_ACTIONBUTTONMOVIE, EscherAggregate.ST_ACTIONBUTTONMOVIE, EscherAggregate.ST_ACTIONBUTTONMOVIE, EscherAggregate.ST_ACTIONBUTTONMOVIE, EscherAggregate.ST_ACTIONBUTTONMOVIE, EscherAggregate.ST_ACTIONBUTTONMOVIE, EscherAggregate.ST_ACTIONBUTTONMOVIE, EscherAggregate.ST_ACTIONBUTTONMOVIE, EscherAggregate.ST_ACTIONBUTTONMOVIE, EscherAggregate.ST_ACTIONBUTTONMOVIE, EscherAggregate.ST_ACTIONBUTTONMOVIE, EscherAggregate.ST_ACTIONBUTTONMOVIE, EscherAggregate.ST_ACTIONBUTTONMOVIE, EscherAggregate.ST_ACTIONBUTTONMOVIE, EscherAggregate.ST_ACTIONBUTTONMOVIE, EscherAggregate.ST_ACTIONBUTTONMOVIE, EscherAggregate.ST_ACTIONBUTTONMOVIE, EscherAggregate.ST_ACTIONBUTTONMOVIE, EscherAggregate.ST_ACTIONBUTTONMOVIE, EscherAggregate.ST_ACTIONBUTTONMOVIE, EscherAggregate.ST_ACTIONBUTTONMOVIE, EscherAggregate.ST_ACTIONBUTTONMOVIE, EscherAggregate.ST_ACTIONBUTTONMOVIE, EscherAggregate.ST_ACTIONBUTTONMOVIE, EscherAggregate.ST_ACTIONBUTTONMOVIE, EscherAggregate.ST_ACTIONBUTTONMOVIE, EscherAggregate.ST_ACTIONBUTTONMOVIE, EscherAggregate.ST_ACTIONBUTTONMOVIE, EscherAggregate.ST_ACTIONBUTTONMOVIE, EscherAggregate.ST_ACTIONBUTTONMOVIE, EscherAggregate.ST_ACTIONBUTTONMOVIE, EscherAggregate.ST_ACTIONBUTTONMOVIE, EscherAggregate.ST_ACTIONBUTTONMOVIE, EscherAggregate.ST_ACTIONBUTTONMOVIE, EscherAggregate.ST_ACTIONBUTTONMOVIE, EscherAggregate.ST_ACTIONBUTTONMOVIE, EscherAggregate.ST_ACTIONBUTTONMOVIE, EscherAggregate.ST_ACTIONBUTTONMOVIE, EscherAggregate.ST_ACTIONBUTTONMOVIE, EscherAggregate.ST_ACTIONBUTTONMOVIE, EscherAggregate.ST_ACTIONBUTTONMOVIE, EscherAggregate.ST_ACTIONBUTTONMOVIE, EscherAggregate.ST_ACTIONBUTTONMOVIE, EscherAggregate.ST_ACTIONBUTTONMOVIE, EscherAggregate.ST_ACTIONBUTTONMOVIE, EscherAggregate.ST_ACTIONBUTTONMOVIE, EscherAggregate.ST_ACTIONBUTTONMOVIE, EscherAggregate.ST_ACTIONBUTTONMOVIE, EscherAggregate.ST_ACTIONBUTTONMOVIE, EscherAggregate.ST_ACTIONBUTTONMOVIE, EscherAggregate.ST_ACTIONBUTTONMOVIE, EscherAggregate.ST_ACTIONBUTTONMOVIE, EscherAggregate.ST_ACTIONBUTTONMOVIE, EscherAggregate.ST_ACTIONBUTTONMOVIE, EscherAggregate.ST_ACTIONBUTTONMOVIE, EscherAggregate.ST_ACTIONBUTTONMOVIE, EscherAggregate.ST_ACTIONBUTTONMOVIE, EscherAggregate.ST_ACTIONBUTTONMOVIE, EscherAggregate.ST_ACTIONBUTTONMOVIE, EscherAggregate.ST_ACTIONBUTTONMOVIE, (short) 232, (short) 232, (short) 232, (short) 232, (short) 232, (short) 232, (short) 232, (short) 232, (short) 232, (short) 232, (short) 232, (short) 232, (short) 232, (short) 232, (short) 232, (short) 232, (short) 232, (short) 232, (short) 232, (short) 232, (short) 232, (short) 232, (short) 232, (short) 232, (short) 232, (short) 232, (short) 232, (short) 232, (short) 232, (short) 232, (short) 232, (short) 232, (short) 232, (short) 232, (short) 232, (short) 232, (short) 232, (short) 232, (short) 232, (short) 232, (short) 232, (short) 232, (short) 232, (short) 232, (short) 232, (short) 232, (short) 232, (short) 232, (short) 232, (short) 232, (short) 232, (short) 232, (short) 232, (short) 232, (short) 232, (short) 232, (short) 232, (short) 232, (short) 232, (short) 232, (short) 232, (short) 232, (short) 232, (short) 232};
    private int bitPointer;
    private int bytePointer;
    private int changingElemSize = 0;
    private int compression = 2;
    private int[] currChangingElems;
    private byte[] data;
    private int fillBits = 0;
    private int fillOrder;
    private int f147h;
    private int lastChangingElement = 0;
    private int oneD;
    private int[] prevChangingElems;
    private boolean recoverFromImageError;
    private int uncompressedMode = 0;
    private int f148w;

    public TIFFFaxDecoder(int fillOrder, int w, int h) {
        this.fillOrder = fillOrder;
        this.f148w = w;
        this.f147h = h;
        this.bitPointer = 0;
        this.bytePointer = 0;
        this.prevChangingElems = new int[(w * 2)];
        this.currChangingElems = new int[(w * 2)];
    }

    public static void reverseBits(byte[] b) {
        for (int k = 0; k < b.length; k++) {
            b[k] = flipTable[b[k] & 255];
        }
    }

    public void decode1D(byte[] buffer, byte[] compData, int startX, int height) {
        this.data = compData;
        int lineOffset = 0;
        int scanlineStride = (this.f148w + 7) / 8;
        this.bitPointer = 0;
        this.bytePointer = 0;
        for (int i = 0; i < height; i++) {
            decodeNextScanline(buffer, lineOffset, startX);
            lineOffset += scanlineStride;
        }
    }

    public void decodeNextScanline(byte[] buffer, int lineOffset, int bitOffset) {
        int[] iArr;
        int i;
        boolean isWhite = true;
        this.changingElemSize = 0;
        while (bitOffset < this.f148w) {
            while (isWhite) {
                int current = nextNBits(10);
                int entry = white[current];
                int isT = entry & 1;
                int bits = (entry >>> 1) & 15;
                if (bits == 12) {
                    entry = additionalMakeup[((current << 2) & 12) | nextLesserThan8Bits(2)];
                    bitOffset += (entry >>> 4) & 4095;
                    updatePointer(4 - ((entry >>> 1) & 7));
                } else if (bits == 0) {
                    throw new RuntimeException(MessageLocalization.getComposedMessage("invalid.code.encountered", new Object[0]));
                } else if (bits == 15) {
                    throw new RuntimeException(MessageLocalization.getComposedMessage("eol.code.word.encountered.in.white.run", new Object[0]));
                } else {
                    bitOffset += (entry >>> 5) & 2047;
                    updatePointer(10 - bits);
                    if (isT == 0) {
                        isWhite = false;
                        iArr = this.currChangingElems;
                        i = this.changingElemSize;
                        this.changingElemSize = i + 1;
                        iArr[i] = bitOffset;
                    }
                }
            }
            if (bitOffset != this.f148w) {
                while (!isWhite) {
                    entry = initBlack[nextLesserThan8Bits(4)];
                    isT = entry & 1;
                    bits = (entry >>> 1) & 15;
                    int code = (entry >>> 5) & 2047;
                    if (code == 100) {
                        entry = black[nextNBits(9)];
                        isT = entry & 1;
                        bits = (entry >>> 1) & 15;
                        code = (entry >>> 5) & 2047;
                        if (bits == 12) {
                            updatePointer(5);
                            entry = additionalMakeup[nextLesserThan8Bits(4)];
                            bits = (entry >>> 1) & 7;
                            code = (entry >>> 4) & 4095;
                            setToBlack(buffer, lineOffset, bitOffset, code);
                            bitOffset += code;
                            updatePointer(4 - bits);
                        } else if (bits == 15) {
                            throw new RuntimeException(MessageLocalization.getComposedMessage("eol.code.word.encountered.in.black.run", new Object[0]));
                        } else {
                            setToBlack(buffer, lineOffset, bitOffset, code);
                            bitOffset += code;
                            updatePointer(9 - bits);
                            if (isT == 0) {
                                isWhite = true;
                                iArr = this.currChangingElems;
                                i = this.changingElemSize;
                                this.changingElemSize = i + 1;
                                iArr[i] = bitOffset;
                            }
                        }
                    } else if (code == 200) {
                        entry = twoBitBlack[nextLesserThan8Bits(2)];
                        code = (entry >>> 5) & 2047;
                        bits = (entry >>> 1) & 15;
                        setToBlack(buffer, lineOffset, bitOffset, code);
                        bitOffset += code;
                        updatePointer(2 - bits);
                        isWhite = true;
                        iArr = this.currChangingElems;
                        i = this.changingElemSize;
                        this.changingElemSize = i + 1;
                        iArr[i] = bitOffset;
                    } else {
                        setToBlack(buffer, lineOffset, bitOffset, code);
                        bitOffset += code;
                        updatePointer(4 - bits);
                        isWhite = true;
                        iArr = this.currChangingElems;
                        i = this.changingElemSize;
                        this.changingElemSize = i + 1;
                        iArr[i] = bitOffset;
                    }
                }
                if (bitOffset == this.f148w) {
                    if (this.compression == 2) {
                        advancePointer();
                    }
                }
            } else if (this.compression == 2) {
                advancePointer();
            }
            iArr = this.currChangingElems;
            i = this.changingElemSize;
            this.changingElemSize = i + 1;
            iArr[i] = bitOffset;
        }
        iArr = this.currChangingElems;
        i = this.changingElemSize;
        this.changingElemSize = i + 1;
        iArr[i] = bitOffset;
    }

    public void decode2D(byte[] buffer, byte[] compData, int startX, int height, long tiffT4Options) {
        this.data = compData;
        this.compression = 3;
        this.bitPointer = 0;
        this.bytePointer = 0;
        int scanlineStride = (this.f148w + 7) / 8;
        int[] b = new int[2];
        this.oneD = (int) (1 & tiffT4Options);
        this.uncompressedMode = (int) ((2 & tiffT4Options) >> 1);
        this.fillBits = (int) ((4 & tiffT4Options) >> 2);
        if (readEOL(true) != 1) {
            throw new RuntimeException(MessageLocalization.getComposedMessage("first.scanline.must.be.1d.encoded", new Object[0]));
        }
        decodeNextScanline(buffer, 0, startX);
        int lineOffset = 0 + scanlineStride;
        for (int lines = 1; lines < height; lines++) {
            if (readEOL(false) == 0) {
                int currIndex;
                int[] temp = this.prevChangingElems;
                this.prevChangingElems = this.currChangingElems;
                this.currChangingElems = temp;
                int currIndex2 = 0;
                int a0 = -1;
                boolean isWhite = true;
                int bitOffset = startX;
                this.lastChangingElement = 0;
                while (bitOffset < this.f148w) {
                    getNextChangingElement(a0, isWhite, b);
                    int b1 = b[0];
                    int b2 = b[1];
                    int entry = twoDCodes[nextLesserThan8Bits(7)] & 255;
                    int code = (entry & 120) >>> 3;
                    int bits = entry & 7;
                    if (code == 0) {
                        if (!isWhite) {
                            setToBlack(buffer, lineOffset, bitOffset, b2 - bitOffset);
                        }
                        a0 = b2;
                        bitOffset = b2;
                        updatePointer(7 - bits);
                    } else if (code == 1) {
                        updatePointer(7 - bits);
                        int number;
                        if (isWhite) {
                            bitOffset += decodeWhiteCodeWord();
                            currIndex = currIndex2 + 1;
                            this.currChangingElems[currIndex2] = bitOffset;
                            number = decodeBlackCodeWord();
                            setToBlack(buffer, lineOffset, bitOffset, number);
                            bitOffset += number;
                            currIndex2 = currIndex + 1;
                            this.currChangingElems[currIndex] = bitOffset;
                        } else {
                            number = decodeBlackCodeWord();
                            setToBlack(buffer, lineOffset, bitOffset, number);
                            bitOffset += number;
                            currIndex = currIndex2 + 1;
                            this.currChangingElems[currIndex2] = bitOffset;
                            bitOffset += decodeWhiteCodeWord();
                            currIndex2 = currIndex + 1;
                            this.currChangingElems[currIndex] = bitOffset;
                        }
                        a0 = bitOffset;
                    } else if (code <= 8) {
                        int a1 = b1 + (code - 5);
                        currIndex = currIndex2 + 1;
                        this.currChangingElems[currIndex2] = a1;
                        if (!isWhite) {
                            setToBlack(buffer, lineOffset, bitOffset, a1 - bitOffset);
                        }
                        a0 = a1;
                        bitOffset = a1;
                        isWhite = !isWhite;
                        updatePointer(7 - bits);
                        currIndex2 = currIndex;
                    } else {
                        throw new RuntimeException(MessageLocalization.getComposedMessage("invalid.code.encountered.while.decoding.2d.group.3.compressed.data", new Object[0]));
                    }
                }
                currIndex = currIndex2 + 1;
                this.currChangingElems[currIndex2] = bitOffset;
                this.changingElemSize = currIndex;
                currIndex2 = currIndex;
            } else {
                decodeNextScanline(buffer, lineOffset, startX);
            }
            lineOffset += scanlineStride;
        }
    }

    public void decodeT6(byte[] buffer, byte[] compData, int startX, int height, long tiffT6Options) {
        this.data = compData;
        this.compression = 4;
        this.bitPointer = 0;
        this.bytePointer = 0;
        int scanlineStride = (this.f148w + 7) / 8;
        int[] b = new int[2];
        this.uncompressedMode = (int) ((2 & tiffT6Options) >> 1);
        int[] cce = this.currChangingElems;
        this.changingElemSize = 0;
        int i = this.changingElemSize;
        this.changingElemSize = i + 1;
        cce[i] = this.f148w;
        i = this.changingElemSize;
        this.changingElemSize = i + 1;
        cce[i] = this.f148w;
        int lineOffset = 0;
        for (int lines = 0; lines < height; lines++) {
            int currIndex;
            int a0 = -1;
            boolean isWhite = true;
            int[] temp = this.prevChangingElems;
            this.prevChangingElems = this.currChangingElems;
            this.currChangingElems = temp;
            cce = temp;
            int currIndex2 = 0;
            int bitOffset = startX;
            this.lastChangingElement = 0;
            while (bitOffset < this.f148w && this.bytePointer < this.data.length) {
                getNextChangingElement(a0, isWhite, b);
                int b1 = b[0];
                int b2 = b[1];
                int entry = twoDCodes[nextLesserThan8Bits(7)] & 255;
                int code = (entry & 120) >>> 3;
                int bits = entry & 7;
                if (code == 0) {
                    if (!isWhite) {
                        setToBlack(buffer, lineOffset, bitOffset, b2 - bitOffset);
                    }
                    a0 = b2;
                    bitOffset = b2;
                    updatePointer(7 - bits);
                } else if (code == 1) {
                    updatePointer(7 - bits);
                    int number;
                    if (isWhite) {
                        bitOffset += decodeWhiteCodeWord();
                        currIndex = currIndex2 + 1;
                        cce[currIndex2] = bitOffset;
                        number = decodeBlackCodeWord();
                        setToBlack(buffer, lineOffset, bitOffset, number);
                        bitOffset += number;
                        currIndex2 = currIndex + 1;
                        cce[currIndex] = bitOffset;
                    } else {
                        number = decodeBlackCodeWord();
                        setToBlack(buffer, lineOffset, bitOffset, number);
                        bitOffset += number;
                        currIndex = currIndex2 + 1;
                        cce[currIndex2] = bitOffset;
                        bitOffset += decodeWhiteCodeWord();
                        currIndex2 = currIndex + 1;
                        cce[currIndex] = bitOffset;
                    }
                    a0 = bitOffset;
                } else if (code <= 8) {
                    int a1 = b1 + (code - 5);
                    currIndex = currIndex2 + 1;
                    cce[currIndex2] = a1;
                    if (!isWhite) {
                        setToBlack(buffer, lineOffset, bitOffset, a1 - bitOffset);
                    }
                    a0 = a1;
                    bitOffset = a1;
                    isWhite = !isWhite;
                    updatePointer(7 - bits);
                    currIndex2 = currIndex;
                } else if (code != 11) {
                    bitOffset = this.f148w;
                    updatePointer(7 - bits);
                } else if (nextLesserThan8Bits(3) != 7) {
                    throw new InvalidImageException(MessageLocalization.getComposedMessage("invalid.code.encountered.while.decoding.2d.group.4.compressed.data", new Object[0]));
                } else {
                    int zeros = 0;
                    boolean exit = false;
                    currIndex = currIndex2;
                    while (!exit) {
                        while (nextLesserThan8Bits(1) != 1) {
                            zeros++;
                        }
                        if (zeros > 5) {
                            zeros -= 6;
                            if (!isWhite && zeros > 0) {
                                currIndex2 = currIndex + 1;
                                cce[currIndex] = bitOffset;
                                currIndex = currIndex2;
                            }
                            bitOffset += zeros;
                            if (zeros > 0) {
                                isWhite = true;
                            }
                            if (nextLesserThan8Bits(1) == 0) {
                                if (isWhite) {
                                    currIndex2 = currIndex;
                                } else {
                                    currIndex2 = currIndex + 1;
                                    cce[currIndex] = bitOffset;
                                }
                                isWhite = true;
                            } else {
                                if (isWhite) {
                                    currIndex2 = currIndex + 1;
                                    cce[currIndex] = bitOffset;
                                } else {
                                    currIndex2 = currIndex;
                                }
                                isWhite = false;
                            }
                            exit = true;
                            currIndex = currIndex2;
                        }
                        if (zeros == 5) {
                            if (isWhite) {
                                currIndex2 = currIndex;
                            } else {
                                currIndex2 = currIndex + 1;
                                cce[currIndex] = bitOffset;
                            }
                            bitOffset += zeros;
                            isWhite = true;
                            currIndex = currIndex2;
                        } else {
                            bitOffset += zeros;
                            currIndex2 = currIndex + 1;
                            cce[currIndex] = bitOffset;
                            setToBlack(buffer, lineOffset, bitOffset, 1);
                            bitOffset++;
                            isWhite = false;
                            currIndex = currIndex2;
                        }
                    }
                    currIndex2 = currIndex;
                }
            }
            if (currIndex2 < cce.length) {
                currIndex = currIndex2 + 1;
                cce[currIndex2] = bitOffset;
                currIndex2 = currIndex;
            }
            this.changingElemSize = currIndex2;
            lineOffset += scanlineStride;
        }
    }

    private void setToBlack(byte[] buffer, int lineOffset, int bitOffset, int numBits) {
        int bitNum = (lineOffset * 8) + bitOffset;
        int lastBit = bitNum + numBits;
        int byteNum = bitNum >> 3;
        int shift = bitNum & 7;
        if (shift > 0) {
            int maskVal = 1 << (7 - shift);
            byte val = buffer[byteNum];
            while (maskVal > 0 && bitNum < lastBit) {
                val = (byte) (val | maskVal);
                maskVal >>= 1;
                bitNum++;
            }
            buffer[byteNum] = val;
        }
        int byteNum2 = bitNum >> 3;
        while (bitNum < lastBit - 7) {
            byteNum = byteNum2 + 1;
            buffer[byteNum2] = (byte) -1;
            bitNum += 8;
            byteNum2 = byteNum;
        }
        while (bitNum < lastBit) {
            byteNum = bitNum >> 3;
            if (!this.recoverFromImageError || byteNum < buffer.length) {
                buffer[byteNum] = (byte) (buffer[byteNum] | (1 << (7 - (bitNum & 7))));
            }
            bitNum++;
        }
    }

    private int decodeWhiteCodeWord() {
        int runLength = 0;
        boolean isWhite = true;
        while (isWhite) {
            int current = nextNBits(10);
            int entry = white[current];
            int isT = entry & 1;
            int bits = (entry >>> 1) & 15;
            if (bits == 12) {
                entry = additionalMakeup[((current << 2) & 12) | nextLesserThan8Bits(2)];
                runLength += (entry >>> 4) & 4095;
                updatePointer(4 - ((entry >>> 1) & 7));
            } else if (bits == 0) {
                throw new InvalidImageException(MessageLocalization.getComposedMessage("invalid.code.encountered", new Object[0]));
            } else if (bits != 15) {
                runLength += (entry >>> 5) & 2047;
                updatePointer(10 - bits);
                if (isT == 0) {
                    isWhite = false;
                }
            } else if (runLength == 0) {
                isWhite = false;
            } else {
                throw new RuntimeException(MessageLocalization.getComposedMessage("eol.code.word.encountered.in.white.run", new Object[0]));
            }
        }
        return runLength;
    }

    private int decodeBlackCodeWord() {
        int runLength = 0;
        boolean isWhite = false;
        while (!isWhite) {
            int entry = initBlack[nextLesserThan8Bits(4)];
            int isT = entry & 1;
            int bits = (entry >>> 1) & 15;
            int code = (entry >>> 5) & 2047;
            if (code == 100) {
                entry = black[nextNBits(9)];
                isT = entry & 1;
                bits = (entry >>> 1) & 15;
                code = (entry >>> 5) & 2047;
                if (bits == 12) {
                    updatePointer(5);
                    entry = additionalMakeup[nextLesserThan8Bits(4)];
                    runLength += (entry >>> 4) & 4095;
                    updatePointer(4 - ((entry >>> 1) & 7));
                } else if (bits == 15) {
                    throw new RuntimeException(MessageLocalization.getComposedMessage("eol.code.word.encountered.in.black.run", new Object[0]));
                } else {
                    runLength += code;
                    updatePointer(9 - bits);
                    if (isT == 0) {
                        isWhite = true;
                    }
                }
            } else if (code == 200) {
                entry = twoBitBlack[nextLesserThan8Bits(2)];
                runLength += (entry >>> 5) & 2047;
                updatePointer(2 - ((entry >>> 1) & 15));
                isWhite = true;
            } else {
                runLength += code;
                updatePointer(4 - bits);
                isWhite = true;
            }
        }
        return runLength;
    }

    private int readEOL(boolean isFirstEOL) {
        if (this.fillBits == 0) {
            int next12Bits = nextNBits(12);
            if (isFirstEOL && next12Bits == 0 && nextNBits(4) == 1) {
                this.fillBits = 1;
                return 1;
            } else if (next12Bits != 1) {
                throw new RuntimeException(MessageLocalization.getComposedMessage("scanline.must.begin.with.eol.code.word", new Object[0]));
            }
        } else if (this.fillBits == 1) {
            int bitsLeft = 8 - this.bitPointer;
            if (nextNBits(bitsLeft) != 0) {
                throw new RuntimeException(MessageLocalization.getComposedMessage("all.fill.bits.preceding.eol.code.must.be.0", new Object[0]));
            } else if (bitsLeft >= 4 || nextNBits(8) == 0) {
                int n;
                do {
                    n = nextNBits(8);
                    if (n != 1) {
                    }
                } while (n == 0);
                throw new RuntimeException(MessageLocalization.getComposedMessage("all.fill.bits.preceding.eol.code.must.be.0", new Object[0]));
            } else {
                throw new RuntimeException(MessageLocalization.getComposedMessage("all.fill.bits.preceding.eol.code.must.be.0", new Object[0]));
            }
        }
        if (this.oneD != 0) {
            return nextLesserThan8Bits(1);
        }
        return 1;
    }

    private void getNextChangingElement(int a0, boolean isWhite, int[] ret) {
        int start;
        int[] pce = this.prevChangingElems;
        int ces = this.changingElemSize;
        if (this.lastChangingElement > 0) {
            start = this.lastChangingElement - 1;
        } else {
            start = 0;
        }
        if (isWhite) {
            start &= -2;
        } else {
            start |= 1;
        }
        int i = start;
        while (i < ces) {
            int temp = pce[i];
            if (temp > a0) {
                this.lastChangingElement = i;
                ret[0] = temp;
                break;
            }
            i += 2;
        }
        if (i + 1 < ces) {
            ret[1] = pce[i + 1];
        }
    }

    private int nextNBits(int bitsToGet) {
        byte b;
        byte next;
        byte next2next;
        int l = this.data.length - 1;
        int bp = this.bytePointer;
        if (this.fillOrder == 1) {
            b = this.data[bp];
            if (bp == l) {
                next = (byte) 0;
                next2next = (byte) 0;
            } else if (bp + 1 == l) {
                next = this.data[bp + 1];
                next2next = (byte) 0;
            } else {
                next = this.data[bp + 1];
                next2next = this.data[bp + 2];
            }
        } else if (this.fillOrder == 2) {
            b = flipTable[this.data[bp] & 255];
            if (bp == l) {
                next = (byte) 0;
                next2next = (byte) 0;
            } else if (bp + 1 == l) {
                next = flipTable[this.data[bp + 1] & 255];
                next2next = (byte) 0;
            } else {
                next = flipTable[this.data[bp + 1] & 255];
                next2next = flipTable[this.data[bp + 2] & 255];
            }
        } else {
            throw new RuntimeException(MessageLocalization.getComposedMessage("tiff.fill.order.tag.must.be.either.1.or.2", new Object[0]));
        }
        int bitsLeft = 8 - this.bitPointer;
        int bitsFromNextByte = bitsToGet - bitsLeft;
        int bitsFromNext2NextByte = 0;
        if (bitsFromNextByte > 8) {
            bitsFromNext2NextByte = bitsFromNextByte - 8;
            bitsFromNextByte = 8;
        }
        this.bytePointer++;
        int i1 = (table1[bitsLeft] & b) << (bitsToGet - bitsLeft);
        int i2 = (table2[bitsFromNextByte] & next) >>> (8 - bitsFromNextByte);
        if (bitsFromNext2NextByte != 0) {
            i2 = (i2 << bitsFromNext2NextByte) | ((table2[bitsFromNext2NextByte] & next2next) >>> (8 - bitsFromNext2NextByte));
            this.bytePointer++;
            this.bitPointer = bitsFromNext2NextByte;
        } else if (bitsFromNextByte == 8) {
            this.bitPointer = 0;
            this.bytePointer++;
        } else {
            this.bitPointer = bitsFromNextByte;
        }
        return i1 | i2;
    }

    private int nextLesserThan8Bits(int bitsToGet) {
        byte b = (byte) 0;
        byte next = (byte) 0;
        int l = this.data.length - 1;
        int bp = this.bytePointer;
        if (this.fillOrder == 1) {
            b = this.data[bp];
            if (bp == l) {
                next = (byte) 0;
            } else {
                next = this.data[bp + 1];
            }
        } else if (this.fillOrder != 2) {
            throw new RuntimeException(MessageLocalization.getComposedMessage("tiff.fill.order.tag.must.be.either.1.or.2", new Object[0]));
        } else if (!this.recoverFromImageError || bp < this.data.length) {
            b = flipTable[this.data[bp] & 255];
            if (bp == l) {
                next = (byte) 0;
            } else {
                next = flipTable[this.data[bp + 1] & 255];
            }
        }
        int bitsLeft = 8 - this.bitPointer;
        int bitsFromNextByte = bitsToGet - bitsLeft;
        int shift = bitsLeft - bitsToGet;
        int i1;
        if (shift >= 0) {
            i1 = (table1[bitsLeft] & b) >>> shift;
            this.bitPointer += bitsToGet;
            if (this.bitPointer != 8) {
                return i1;
            }
            this.bitPointer = 0;
            this.bytePointer++;
            return i1;
        }
        i1 = ((table1[bitsLeft] & b) << (-shift)) | ((table2[bitsFromNextByte] & next) >>> (8 - bitsFromNextByte));
        this.bytePointer++;
        this.bitPointer = bitsFromNextByte;
        return i1;
    }

    private void updatePointer(int bitsToMoveBack) {
        int i = this.bitPointer - bitsToMoveBack;
        if (i < 0) {
            this.bytePointer--;
            this.bitPointer = i + 8;
            return;
        }
        this.bitPointer = i;
    }

    private boolean advancePointer() {
        if (this.bitPointer != 0) {
            this.bytePointer++;
            this.bitPointer = 0;
        }
        return true;
    }

    public void setRecoverFromImageError(boolean recoverFromImageError) {
        this.recoverFromImageError = recoverFromImageError;
    }
}
