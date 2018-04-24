package com.itextpdf.text.pdf;

import android.support.v4.view.MotionEventCompat;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.error_messages.MessageLocalization;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import org.bytedeco.javacpp.avcodec;

class TrueTypeFont extends BaseFont {
    static final String[] codePages = new String[]{"1252 Latin 1", "1250 Latin 2: Eastern Europe", "1251 Cyrillic", "1253 Greek", "1254 Turkish", "1255 Hebrew", "1256 Arabic", "1257 Windows Baltic", "1258 Vietnamese", null, null, null, null, null, null, null, "874 Thai", "932 JIS/Japan", "936 Chinese: Simplified chars--PRC and Singapore", "949 Korean Wansung", "950 Chinese: Traditional chars--Taiwan and Hong Kong", "1361 Korean Johab", null, null, null, null, null, null, null, "Macintosh Character Set (US Roman)", "OEM Character Set", "Symbol Character Set", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, "869 IBM Greek", "866 MS-DOS Russian", "865 MS-DOS Nordic", "864 Arabic", "863 MS-DOS Canadian French", "862 Hebrew", "861 MS-DOS Icelandic", "860 MS-DOS Portuguese", "857 IBM Turkish", "855 IBM Cyrillic; primarily Russian", "852 Latin 2", "775 MS-DOS Baltic", "737 Greek; former 437 G", "708 Arabic; ASMO 708", "850 WE/Latin 1", "437 US"};
    protected String[][] allNameEntries;
    protected int[][] bboxes;
    protected boolean cff = false;
    protected int cffLength;
    protected int cffOffset;
    protected HashMap<Integer, int[]> cmap10;
    protected HashMap<Integer, int[]> cmap31;
    protected HashMap<Integer, int[]> cmapExt;
    protected int directoryOffset;
    protected String[][] familyName;
    protected String fileName;
    protected String fontName;
    protected String[][] fullName;
    protected int[] glyphIdToChar;
    protected int[] glyphWidthsByIndex;
    protected FontHeader head = new FontHeader();
    protected HorizontalHeader hhea = new HorizontalHeader();
    protected boolean isFixedPitch = false;
    protected double italicAngle;
    protected boolean justNames = false;
    protected IntHashtable kerning = new IntHashtable();
    protected int maxGlyphId;
    protected WindowsMetrics os_2 = new WindowsMetrics();
    protected RandomAccessFileOrArray rf;
    protected String style = "";
    protected HashMap<String, int[]> tables;
    protected String ttcIndex;
    protected int underlinePosition;
    protected int underlineThickness;

    protected static class FontHeader {
        int flags;
        int macStyle;
        int unitsPerEm;
        short xMax;
        short xMin;
        short yMax;
        short yMin;

        protected FontHeader() {
        }
    }

    protected static class HorizontalHeader {
        short Ascender;
        short Descender;
        short LineGap;
        int advanceWidthMax;
        short caretSlopeRise;
        short caretSlopeRun;
        short minLeftSideBearing;
        short minRightSideBearing;
        int numberOfHMetrics;
        short xMaxExtent;

        protected HorizontalHeader() {
        }
    }

    protected static class WindowsMetrics {
        byte[] achVendID = new byte[4];
        int fsSelection;
        short fsType;
        byte[] panose = new byte[10];
        int sCapHeight;
        short sFamilyClass;
        short sTypoAscender;
        short sTypoDescender;
        short sTypoLineGap;
        int ulCodePageRange1;
        int ulCodePageRange2;
        int usFirstCharIndex;
        int usLastCharIndex;
        int usWeightClass;
        int usWidthClass;
        int usWinAscent;
        int usWinDescent;
        short xAvgCharWidth;
        short yStrikeoutPosition;
        short yStrikeoutSize;
        short ySubscriptXOffset;
        short ySubscriptXSize;
        short ySubscriptYOffset;
        short ySubscriptYSize;
        short ySuperscriptXOffset;
        short ySuperscriptXSize;
        short ySuperscriptYOffset;
        short ySuperscriptYSize;

        protected WindowsMetrics() {
        }
    }

    void process(byte[] r14, boolean r15) throws com.itextpdf.text.DocumentException, java.io.IOException {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: Can't find block by offset: 0x014e in list [B:41:0x0146]
	at jadx.core.utils.BlockUtils.getBlockByOffset(BlockUtils.java:42)
	at jadx.core.dex.instructions.IfNode.initBlocks(IfNode.java:60)
	at jadx.core.dex.visitors.blocksmaker.BlockFinish.initBlocksInIfNodes(BlockFinish.java:48)
	at jadx.core.dex.visitors.blocksmaker.BlockFinish.visit(BlockFinish.java:33)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
        /*
        r13 = this;
        r8 = new java.util.HashMap;
        r8.<init>();
        r13.tables = r8;
        if (r14 != 0) goto L_0x0046;
    L_0x0009:
        r8 = new com.itextpdf.text.pdf.RandomAccessFileOrArray;
        r9 = r13.fileName;
        r10 = com.itextpdf.text.Document.plainRandomAccess;
        r8.<init>(r9, r15, r10);
        r13.rf = r8;
    L_0x0014:
        r8 = r13.ttcIndex;	 Catch:{ all -> 0x0038 }
        r8 = r8.length();	 Catch:{ all -> 0x0038 }
        if (r8 <= 0) goto L_0x00b0;	 Catch:{ all -> 0x0038 }
    L_0x001c:
        r8 = r13.ttcIndex;	 Catch:{ all -> 0x0038 }
        r1 = java.lang.Integer.parseInt(r8);	 Catch:{ all -> 0x0038 }
        if (r1 >= 0) goto L_0x004e;	 Catch:{ all -> 0x0038 }
    L_0x0024:
        r8 = new com.itextpdf.text.DocumentException;	 Catch:{ all -> 0x0038 }
        r9 = "the.font.index.for.1.must.be.positive";	 Catch:{ all -> 0x0038 }
        r10 = 1;	 Catch:{ all -> 0x0038 }
        r10 = new java.lang.Object[r10];	 Catch:{ all -> 0x0038 }
        r11 = 0;	 Catch:{ all -> 0x0038 }
        r12 = r13.fileName;	 Catch:{ all -> 0x0038 }
        r10[r11] = r12;	 Catch:{ all -> 0x0038 }
        r9 = com.itextpdf.text.error_messages.MessageLocalization.getComposedMessage(r9, r10);	 Catch:{ all -> 0x0038 }
        r8.<init>(r9);	 Catch:{ all -> 0x0038 }
        throw r8;	 Catch:{ all -> 0x0038 }
    L_0x0038:
        r8 = move-exception;
        r9 = r13.embedded;
        if (r9 != 0) goto L_0x0045;
    L_0x003d:
        r9 = r13.rf;
        r9.close();
        r9 = 0;
        r13.rf = r9;
    L_0x0045:
        throw r8;
    L_0x0046:
        r8 = new com.itextpdf.text.pdf.RandomAccessFileOrArray;
        r8.<init>(r14);
        r13.rf = r8;
        goto L_0x0014;
    L_0x004e:
        r8 = 4;
        r3 = r13.readStandardString(r8);	 Catch:{ all -> 0x0038 }
        r8 = "ttcf";	 Catch:{ all -> 0x0038 }
        r8 = r3.equals(r8);	 Catch:{ all -> 0x0038 }
        if (r8 != 0) goto L_0x006f;	 Catch:{ all -> 0x0038 }
    L_0x005b:
        r8 = new com.itextpdf.text.DocumentException;	 Catch:{ all -> 0x0038 }
        r9 = "1.is.not.a.valid.ttc.file";	 Catch:{ all -> 0x0038 }
        r10 = 1;	 Catch:{ all -> 0x0038 }
        r10 = new java.lang.Object[r10];	 Catch:{ all -> 0x0038 }
        r11 = 0;	 Catch:{ all -> 0x0038 }
        r12 = r13.fileName;	 Catch:{ all -> 0x0038 }
        r10[r11] = r12;	 Catch:{ all -> 0x0038 }
        r9 = com.itextpdf.text.error_messages.MessageLocalization.getComposedMessage(r9, r10);	 Catch:{ all -> 0x0038 }
        r8.<init>(r9);	 Catch:{ all -> 0x0038 }
        throw r8;	 Catch:{ all -> 0x0038 }
    L_0x006f:
        r8 = r13.rf;	 Catch:{ all -> 0x0038 }
        r9 = 4;	 Catch:{ all -> 0x0038 }
        r8.skipBytes(r9);	 Catch:{ all -> 0x0038 }
        r8 = r13.rf;	 Catch:{ all -> 0x0038 }
        r0 = r8.readInt();	 Catch:{ all -> 0x0038 }
        if (r1 < r0) goto L_0x00a1;	 Catch:{ all -> 0x0038 }
    L_0x007d:
        r8 = new com.itextpdf.text.DocumentException;	 Catch:{ all -> 0x0038 }
        r9 = "the.font.index.for.1.must.be.between.0.and.2.it.was.3";	 Catch:{ all -> 0x0038 }
        r10 = 3;	 Catch:{ all -> 0x0038 }
        r10 = new java.lang.Object[r10];	 Catch:{ all -> 0x0038 }
        r11 = 0;	 Catch:{ all -> 0x0038 }
        r12 = r13.fileName;	 Catch:{ all -> 0x0038 }
        r10[r11] = r12;	 Catch:{ all -> 0x0038 }
        r11 = 1;	 Catch:{ all -> 0x0038 }
        r12 = r0 + -1;	 Catch:{ all -> 0x0038 }
        r12 = java.lang.String.valueOf(r12);	 Catch:{ all -> 0x0038 }
        r10[r11] = r12;	 Catch:{ all -> 0x0038 }
        r11 = 2;	 Catch:{ all -> 0x0038 }
        r12 = java.lang.String.valueOf(r1);	 Catch:{ all -> 0x0038 }
        r10[r11] = r12;	 Catch:{ all -> 0x0038 }
        r9 = com.itextpdf.text.error_messages.MessageLocalization.getComposedMessage(r9, r10);	 Catch:{ all -> 0x0038 }
        r8.<init>(r9);	 Catch:{ all -> 0x0038 }
        throw r8;	 Catch:{ all -> 0x0038 }
    L_0x00a1:
        r8 = r13.rf;	 Catch:{ all -> 0x0038 }
        r9 = r1 * 4;	 Catch:{ all -> 0x0038 }
        r8.skipBytes(r9);	 Catch:{ all -> 0x0038 }
        r8 = r13.rf;	 Catch:{ all -> 0x0038 }
        r8 = r8.readInt();	 Catch:{ all -> 0x0038 }
        r13.directoryOffset = r8;	 Catch:{ all -> 0x0038 }
    L_0x00b0:
        r8 = r13.rf;	 Catch:{ all -> 0x0038 }
        r9 = r13.directoryOffset;	 Catch:{ all -> 0x0038 }
        r10 = (long) r9;	 Catch:{ all -> 0x0038 }
        r8.seek(r10);	 Catch:{ all -> 0x0038 }
        r8 = r13.rf;	 Catch:{ all -> 0x0038 }
        r7 = r8.readInt();	 Catch:{ all -> 0x0038 }
        r8 = 65536; // 0x10000 float:9.18355E-41 double:3.2379E-319;	 Catch:{ all -> 0x0038 }
        if (r7 == r8) goto L_0x00db;	 Catch:{ all -> 0x0038 }
    L_0x00c2:
        r8 = 1330926671; // 0x4f54544f float:3.56229504E9 double:6.575651453E-315;	 Catch:{ all -> 0x0038 }
        if (r7 == r8) goto L_0x00db;	 Catch:{ all -> 0x0038 }
    L_0x00c7:
        r8 = new com.itextpdf.text.DocumentException;	 Catch:{ all -> 0x0038 }
        r9 = "1.is.not.a.valid.ttf.or.otf.file";	 Catch:{ all -> 0x0038 }
        r10 = 1;	 Catch:{ all -> 0x0038 }
        r10 = new java.lang.Object[r10];	 Catch:{ all -> 0x0038 }
        r11 = 0;	 Catch:{ all -> 0x0038 }
        r12 = r13.fileName;	 Catch:{ all -> 0x0038 }
        r10[r11] = r12;	 Catch:{ all -> 0x0038 }
        r9 = com.itextpdf.text.error_messages.MessageLocalization.getComposedMessage(r9, r10);	 Catch:{ all -> 0x0038 }
        r8.<init>(r9);	 Catch:{ all -> 0x0038 }
        throw r8;	 Catch:{ all -> 0x0038 }
    L_0x00db:
        r8 = r13.rf;	 Catch:{ all -> 0x0038 }
        r4 = r8.readUnsignedShort();	 Catch:{ all -> 0x0038 }
        r8 = r13.rf;	 Catch:{ all -> 0x0038 }
        r9 = 6;	 Catch:{ all -> 0x0038 }
        r8.skipBytes(r9);	 Catch:{ all -> 0x0038 }
        r2 = 0;	 Catch:{ all -> 0x0038 }
    L_0x00e8:
        if (r2 >= r4) goto L_0x0112;	 Catch:{ all -> 0x0038 }
    L_0x00ea:
        r8 = 4;	 Catch:{ all -> 0x0038 }
        r6 = r13.readStandardString(r8);	 Catch:{ all -> 0x0038 }
        r8 = r13.rf;	 Catch:{ all -> 0x0038 }
        r9 = 4;	 Catch:{ all -> 0x0038 }
        r8.skipBytes(r9);	 Catch:{ all -> 0x0038 }
        r8 = 2;	 Catch:{ all -> 0x0038 }
        r5 = new int[r8];	 Catch:{ all -> 0x0038 }
        r8 = 0;	 Catch:{ all -> 0x0038 }
        r9 = r13.rf;	 Catch:{ all -> 0x0038 }
        r9 = r9.readInt();	 Catch:{ all -> 0x0038 }
        r5[r8] = r9;	 Catch:{ all -> 0x0038 }
        r8 = 1;	 Catch:{ all -> 0x0038 }
        r9 = r13.rf;	 Catch:{ all -> 0x0038 }
        r9 = r9.readInt();	 Catch:{ all -> 0x0038 }
        r5[r8] = r9;	 Catch:{ all -> 0x0038 }
        r8 = r13.tables;	 Catch:{ all -> 0x0038 }
        r8.put(r6, r5);	 Catch:{ all -> 0x0038 }
        r2 = r2 + 1;	 Catch:{ all -> 0x0038 }
        goto L_0x00e8;	 Catch:{ all -> 0x0038 }
    L_0x0112:
        r13.checkCff();	 Catch:{ all -> 0x0038 }
        r8 = r13.getBaseFont();	 Catch:{ all -> 0x0038 }
        r13.fontName = r8;	 Catch:{ all -> 0x0038 }
        r8 = 4;	 Catch:{ all -> 0x0038 }
        r8 = r13.getNames(r8);	 Catch:{ all -> 0x0038 }
        r13.fullName = r8;	 Catch:{ all -> 0x0038 }
        r8 = 1;	 Catch:{ all -> 0x0038 }
        r8 = r13.getNames(r8);	 Catch:{ all -> 0x0038 }
        r13.familyName = r8;	 Catch:{ all -> 0x0038 }
        r8 = r13.getAllNames();	 Catch:{ all -> 0x0038 }
        r13.allNameEntries = r8;	 Catch:{ all -> 0x0038 }
        r8 = r13.justNames;	 Catch:{ all -> 0x0038 }
        if (r8 != 0) goto L_0x0142;	 Catch:{ all -> 0x0038 }
    L_0x0133:
        r13.fillTables();	 Catch:{ all -> 0x0038 }
        r13.readGlyphWidths();	 Catch:{ all -> 0x0038 }
        r13.readCMaps();	 Catch:{ all -> 0x0038 }
        r13.readKerning();	 Catch:{ all -> 0x0038 }
        r13.readBbox();	 Catch:{ all -> 0x0038 }
    L_0x0142:
        r8 = r13.embedded;
        if (r8 != 0) goto L_0x014e;
    L_0x0146:
        r8 = r13.rf;
        r8.close();
        r8 = 0;
        r13.rf = r8;
    L_0x014e:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.itextpdf.text.pdf.TrueTypeFont.process(byte[], boolean):void");
    }

    protected TrueTypeFont() {
    }

    TrueTypeFont(String ttFile, String enc, boolean emb, byte[] ttfAfm, boolean justNames, boolean forceRead) throws DocumentException, IOException {
        this.justNames = justNames;
        String nameBase = BaseFont.getBaseName(ttFile);
        String ttcName = getTTCName(nameBase);
        if (nameBase.length() < ttFile.length()) {
            this.style = ttFile.substring(nameBase.length());
        }
        this.encoding = enc;
        this.embedded = emb;
        this.fileName = ttcName;
        this.fontType = 1;
        this.ttcIndex = "";
        if (ttcName.length() < nameBase.length()) {
            this.ttcIndex = nameBase.substring(ttcName.length() + 1);
        }
        if (this.fileName.toLowerCase().endsWith(".ttf") || this.fileName.toLowerCase().endsWith(".otf") || this.fileName.toLowerCase().endsWith(".ttc")) {
            process(ttfAfm, forceRead);
            if (!justNames && this.embedded && this.os_2.fsType == (short) 2) {
                throw new DocumentException(MessageLocalization.getComposedMessage("1.cannot.be.embedded.due.to.licensing.restrictions", this.fileName + this.style));
            }
            if (!this.encoding.startsWith("#")) {
                PdfEncodings.convertToBytes(" ", enc);
            }
            createEncoding();
            return;
        }
        throw new DocumentException(MessageLocalization.getComposedMessage("1.is.not.a.ttf.otf.or.ttc.font.file", this.fileName + this.style));
    }

    protected static String getTTCName(String name) {
        int idx = name.toLowerCase().indexOf(".ttc,");
        return idx < 0 ? name : name.substring(0, idx + 4);
    }

    void fillTables() throws DocumentException, IOException {
        boolean z = true;
        int[] table_location = (int[]) this.tables.get("head");
        if (table_location == null) {
            throw new DocumentException(MessageLocalization.getComposedMessage("table.1.does.not.exist.in.2", "head", this.fileName + this.style));
        }
        this.rf.seek((long) (table_location[0] + 16));
        this.head.flags = this.rf.readUnsignedShort();
        this.head.unitsPerEm = this.rf.readUnsignedShort();
        this.rf.skipBytes(16);
        this.head.xMin = this.rf.readShort();
        this.head.yMin = this.rf.readShort();
        this.head.xMax = this.rf.readShort();
        this.head.yMax = this.rf.readShort();
        this.head.macStyle = this.rf.readUnsignedShort();
        table_location = (int[]) this.tables.get("hhea");
        if (table_location == null) {
            throw new DocumentException(MessageLocalization.getComposedMessage("table.1.does.not.exist.in.2", "hhea", this.fileName + this.style));
        }
        this.rf.seek((long) (table_location[0] + 4));
        this.hhea.Ascender = this.rf.readShort();
        this.hhea.Descender = this.rf.readShort();
        this.hhea.LineGap = this.rf.readShort();
        this.hhea.advanceWidthMax = this.rf.readUnsignedShort();
        this.hhea.minLeftSideBearing = this.rf.readShort();
        this.hhea.minRightSideBearing = this.rf.readShort();
        this.hhea.xMaxExtent = this.rf.readShort();
        this.hhea.caretSlopeRise = this.rf.readShort();
        this.hhea.caretSlopeRun = this.rf.readShort();
        this.rf.skipBytes(12);
        this.hhea.numberOfHMetrics = this.rf.readUnsignedShort();
        table_location = (int[]) this.tables.get("OS/2");
        if (table_location == null) {
            throw new DocumentException(MessageLocalization.getComposedMessage("table.1.does.not.exist.in.2", "OS/2", this.fileName + this.style));
        }
        this.rf.seek((long) table_location[0]);
        int version = this.rf.readUnsignedShort();
        this.os_2.xAvgCharWidth = this.rf.readShort();
        this.os_2.usWeightClass = this.rf.readUnsignedShort();
        this.os_2.usWidthClass = this.rf.readUnsignedShort();
        this.os_2.fsType = this.rf.readShort();
        this.os_2.ySubscriptXSize = this.rf.readShort();
        this.os_2.ySubscriptYSize = this.rf.readShort();
        this.os_2.ySubscriptXOffset = this.rf.readShort();
        this.os_2.ySubscriptYOffset = this.rf.readShort();
        this.os_2.ySuperscriptXSize = this.rf.readShort();
        this.os_2.ySuperscriptYSize = this.rf.readShort();
        this.os_2.ySuperscriptXOffset = this.rf.readShort();
        this.os_2.ySuperscriptYOffset = this.rf.readShort();
        this.os_2.yStrikeoutSize = this.rf.readShort();
        this.os_2.yStrikeoutPosition = this.rf.readShort();
        this.os_2.sFamilyClass = this.rf.readShort();
        this.rf.readFully(this.os_2.panose);
        this.rf.skipBytes(16);
        this.rf.readFully(this.os_2.achVendID);
        this.os_2.fsSelection = this.rf.readUnsignedShort();
        this.os_2.usFirstCharIndex = this.rf.readUnsignedShort();
        this.os_2.usLastCharIndex = this.rf.readUnsignedShort();
        this.os_2.sTypoAscender = this.rf.readShort();
        this.os_2.sTypoDescender = this.rf.readShort();
        if (this.os_2.sTypoDescender > (short) 0) {
            this.os_2.sTypoDescender = (short) (-this.os_2.sTypoDescender);
        }
        this.os_2.sTypoLineGap = this.rf.readShort();
        this.os_2.usWinAscent = this.rf.readUnsignedShort();
        this.os_2.usWinDescent = this.rf.readUnsignedShort();
        this.os_2.ulCodePageRange1 = 0;
        this.os_2.ulCodePageRange2 = 0;
        if (version > 0) {
            this.os_2.ulCodePageRange1 = this.rf.readInt();
            this.os_2.ulCodePageRange2 = this.rf.readInt();
        }
        if (version > 1) {
            this.rf.skipBytes(2);
            this.os_2.sCapHeight = this.rf.readShort();
        } else {
            this.os_2.sCapHeight = (int) (0.7d * ((double) this.head.unitsPerEm));
        }
        table_location = (int[]) this.tables.get("post");
        if (table_location == null) {
            this.italicAngle = ((-Math.atan2((double) this.hhea.caretSlopeRun, (double) this.hhea.caretSlopeRise)) * 180.0d) / 3.141592653589793d;
        } else {
            this.rf.seek((long) (table_location[0] + 4));
            this.italicAngle = ((double) this.rf.readShort()) + (((double) this.rf.readUnsignedShort()) / 16384.0d);
            this.underlinePosition = this.rf.readShort();
            this.underlineThickness = this.rf.readShort();
            if (this.rf.readInt() == 0) {
                z = false;
            }
            this.isFixedPitch = z;
        }
        table_location = (int[]) this.tables.get("maxp");
        if (table_location == null) {
            this.maxGlyphId = 65536;
            return;
        }
        this.rf.seek((long) (table_location[0] + 4));
        this.maxGlyphId = this.rf.readUnsignedShort();
    }

    String getBaseFont() throws DocumentException, IOException {
        int[] table_location = (int[]) this.tables.get("name");
        if (table_location == null) {
            throw new DocumentException(MessageLocalization.getComposedMessage("table.1.does.not.exist.in.2", "name", this.fileName + this.style));
        }
        this.rf.seek((long) (table_location[0] + 2));
        int numRecords = this.rf.readUnsignedShort();
        int startOfStorage = this.rf.readUnsignedShort();
        for (int k = 0; k < numRecords; k++) {
            int platformID = this.rf.readUnsignedShort();
            int platformEncodingID = this.rf.readUnsignedShort();
            int languageID = this.rf.readUnsignedShort();
            int nameID = this.rf.readUnsignedShort();
            int length = this.rf.readUnsignedShort();
            int offset = this.rf.readUnsignedShort();
            if (nameID == 6) {
                this.rf.seek((long) ((table_location[0] + startOfStorage) + offset));
                if (platformID == 0 || platformID == 3) {
                    return readUnicodeString(length);
                }
                return readStandardString(length);
            }
        }
        return new File(this.fileName).getName().replace(' ', '-');
    }

    String[][] getNames(int id) throws DocumentException, IOException {
        int[] table_location = (int[]) this.tables.get("name");
        if (table_location == null) {
            throw new DocumentException(MessageLocalization.getComposedMessage("table.1.does.not.exist.in.2", "name", this.fileName + this.style));
        }
        int k;
        this.rf.seek((long) (table_location[0] + 2));
        int numRecords = this.rf.readUnsignedShort();
        int startOfStorage = this.rf.readUnsignedShort();
        ArrayList<String[]> names = new ArrayList();
        for (k = 0; k < numRecords; k++) {
            int platformID = this.rf.readUnsignedShort();
            int platformEncodingID = this.rf.readUnsignedShort();
            int languageID = this.rf.readUnsignedShort();
            int nameID = this.rf.readUnsignedShort();
            int length = this.rf.readUnsignedShort();
            int offset = this.rf.readUnsignedShort();
            if (nameID == id) {
                String name;
                int pos = (int) this.rf.getFilePointer();
                this.rf.seek((long) ((table_location[0] + startOfStorage) + offset));
                if (platformID == 0 || platformID == 3 || (platformID == 2 && platformEncodingID == 1)) {
                    name = readUnicodeString(length);
                } else {
                    name = readStandardString(length);
                }
                names.add(new String[]{String.valueOf(platformID), String.valueOf(platformEncodingID), String.valueOf(languageID), name});
                this.rf.seek((long) pos);
            }
        }
        String[][] thisName = new String[names.size()][];
        for (k = 0; k < names.size(); k++) {
            thisName[k] = (String[]) names.get(k);
        }
        return thisName;
    }

    String[][] getAllNames() throws DocumentException, IOException {
        int[] table_location = (int[]) this.tables.get("name");
        if (table_location == null) {
            throw new DocumentException(MessageLocalization.getComposedMessage("table.1.does.not.exist.in.2", "name", this.fileName + this.style));
        }
        int k;
        this.rf.seek((long) (table_location[0] + 2));
        int numRecords = this.rf.readUnsignedShort();
        int startOfStorage = this.rf.readUnsignedShort();
        ArrayList<String[]> names = new ArrayList();
        for (k = 0; k < numRecords; k++) {
            String name;
            int platformID = this.rf.readUnsignedShort();
            int platformEncodingID = this.rf.readUnsignedShort();
            int languageID = this.rf.readUnsignedShort();
            int nameID = this.rf.readUnsignedShort();
            int length = this.rf.readUnsignedShort();
            int offset = this.rf.readUnsignedShort();
            int pos = (int) this.rf.getFilePointer();
            this.rf.seek((long) ((table_location[0] + startOfStorage) + offset));
            if (platformID == 0 || platformID == 3 || (platformID == 2 && platformEncodingID == 1)) {
                name = readUnicodeString(length);
            } else {
                name = readStandardString(length);
            }
            names.add(new String[]{String.valueOf(nameID), String.valueOf(platformID), String.valueOf(platformEncodingID), String.valueOf(languageID), name});
            this.rf.seek((long) pos);
        }
        String[][] thisName = new String[names.size()][];
        for (k = 0; k < names.size(); k++) {
            thisName[k] = (String[]) names.get(k);
        }
        return thisName;
    }

    void checkCff() {
        int[] table_location = (int[]) this.tables.get("CFF ");
        if (table_location != null) {
            this.cff = true;
            this.cffOffset = table_location[0];
            this.cffLength = table_location[1];
        }
    }

    protected String readStandardString(int length) throws IOException {
        return this.rf.readString(length, "Cp1252");
    }

    protected String readUnicodeString(int length) throws IOException {
        StringBuffer buf = new StringBuffer();
        length /= 2;
        for (int k = 0; k < length; k++) {
            buf.append(this.rf.readChar());
        }
        return buf.toString();
    }

    protected void readGlyphWidths() throws DocumentException, IOException {
        int[] table_location = (int[]) this.tables.get("hmtx");
        if (table_location == null) {
            throw new DocumentException(MessageLocalization.getComposedMessage("table.1.does.not.exist.in.2", "hmtx", this.fileName + this.style));
        }
        this.rf.seek((long) table_location[0]);
        this.glyphWidthsByIndex = new int[this.hhea.numberOfHMetrics];
        for (int k = 0; k < this.hhea.numberOfHMetrics; k++) {
            this.glyphWidthsByIndex[k] = (this.rf.readUnsignedShort() * 1000) / this.head.unitsPerEm;
            int readShort = (this.rf.readShort() * 1000) / this.head.unitsPerEm;
        }
    }

    protected int getGlyphWidth(int glyph) {
        if (glyph >= this.glyphWidthsByIndex.length) {
            glyph = this.glyphWidthsByIndex.length - 1;
        }
        return this.glyphWidthsByIndex[glyph];
    }

    private void readBbox() throws DocumentException, IOException {
        int[] tableLocation = (int[]) this.tables.get("head");
        if (tableLocation == null) {
            throw new DocumentException(MessageLocalization.getComposedMessage("table.1.does.not.exist.in.2", "head", this.fileName + this.style));
        }
        this.rf.seek((long) (tableLocation[0] + 51));
        boolean locaShortTable = this.rf.readUnsignedShort() == 0;
        tableLocation = (int[]) this.tables.get("loca");
        if (tableLocation != null) {
            int[] locaTable;
            this.rf.seek((long) tableLocation[0]);
            int entries;
            int k;
            if (locaShortTable) {
                entries = tableLocation[1] / 2;
                locaTable = new int[entries];
                for (k = 0; k < entries; k++) {
                    locaTable[k] = this.rf.readUnsignedShort() * 2;
                }
            } else {
                entries = tableLocation[1] / 4;
                locaTable = new int[entries];
                for (k = 0; k < entries; k++) {
                    locaTable[k] = this.rf.readInt();
                }
            }
            tableLocation = (int[]) this.tables.get("glyf");
            if (tableLocation == null) {
                throw new DocumentException(MessageLocalization.getComposedMessage("table.1.does.not.exist.in.2", "glyf", this.fileName + this.style));
            }
            int tableGlyphOffset = tableLocation[0];
            this.bboxes = new int[(locaTable.length - 1)][];
            for (int glyph = 0; glyph < locaTable.length - 1; glyph++) {
                int start = locaTable[glyph];
                if (start != locaTable[glyph + 1]) {
                    this.rf.seek((long) ((tableGlyphOffset + start) + 2));
                    this.bboxes[glyph] = new int[]{(this.rf.readShort() * 1000) / this.head.unitsPerEm, (this.rf.readShort() * 1000) / this.head.unitsPerEm, (this.rf.readShort() * 1000) / this.head.unitsPerEm, (this.rf.readShort() * 1000) / this.head.unitsPerEm};
                }
            }
        }
    }

    void readCMaps() throws DocumentException, IOException {
        int[] table_location = (int[]) this.tables.get("cmap");
        if (table_location == null) {
            throw new DocumentException(MessageLocalization.getComposedMessage("table.1.does.not.exist.in.2", "cmap", this.fileName + this.style));
        }
        this.rf.seek((long) table_location[0]);
        this.rf.skipBytes(2);
        int num_tables = this.rf.readUnsignedShort();
        this.fontSpecific = false;
        int map10 = 0;
        int map31 = 0;
        int map30 = 0;
        int mapExt = 0;
        for (int k = 0; k < num_tables; k++) {
            int platId = this.rf.readUnsignedShort();
            int platSpecId = this.rf.readUnsignedShort();
            int offset = this.rf.readInt();
            if (platId == 3 && platSpecId == 0) {
                this.fontSpecific = true;
                map30 = offset;
            } else if (platId == 3 && platSpecId == 1) {
                map31 = offset;
            } else if (platId == 3 && platSpecId == 10) {
                mapExt = offset;
            }
            if (platId == 1 && platSpecId == 0) {
                map10 = offset;
            }
        }
        if (map10 > 0) {
            this.rf.seek((long) (table_location[0] + map10));
            switch (this.rf.readUnsignedShort()) {
                case 0:
                    this.cmap10 = readFormat0();
                    break;
                case 4:
                    this.cmap10 = readFormat4();
                    break;
                case 6:
                    this.cmap10 = readFormat6();
                    break;
            }
        }
        if (map31 > 0) {
            this.rf.seek((long) (table_location[0] + map31));
            if (this.rf.readUnsignedShort() == 4) {
                this.cmap31 = readFormat4();
            }
        }
        if (map30 > 0) {
            this.rf.seek((long) (table_location[0] + map30));
            if (this.rf.readUnsignedShort() == 4) {
                this.cmap10 = readFormat4();
            }
        }
        if (mapExt > 0) {
            this.rf.seek((long) (table_location[0] + mapExt));
            switch (this.rf.readUnsignedShort()) {
                case 0:
                    this.cmapExt = readFormat0();
                    return;
                case 4:
                    this.cmapExt = readFormat4();
                    return;
                case 6:
                    this.cmapExt = readFormat6();
                    return;
                case 12:
                    this.cmapExt = readFormat12();
                    return;
                default:
                    return;
            }
        }
    }

    HashMap<Integer, int[]> readFormat12() throws IOException {
        HashMap<Integer, int[]> h = new HashMap();
        this.rf.skipBytes(2);
        int table_lenght = this.rf.readInt();
        this.rf.skipBytes(4);
        int nGroups = this.rf.readInt();
        for (int k = 0; k < nGroups; k++) {
            int startCharCode = this.rf.readInt();
            int endCharCode = this.rf.readInt();
            int startGlyphID = this.rf.readInt();
            for (int i = startCharCode; i <= endCharCode; i++) {
                h.put(Integer.valueOf(i), new int[]{startGlyphID, getGlyphWidth(r[0])});
                startGlyphID++;
            }
        }
        return h;
    }

    HashMap<Integer, int[]> readFormat0() throws IOException {
        HashMap<Integer, int[]> h = new HashMap();
        this.rf.skipBytes(4);
        for (int k = 0; k < 256; k++) {
            h.put(Integer.valueOf(k), new int[]{this.rf.readUnsignedByte(), getGlyphWidth(r[0])});
        }
        return h;
    }

    HashMap<Integer, int[]> readFormat4() throws IOException {
        int k;
        HashMap<Integer, int[]> h = new HashMap();
        int table_lenght = this.rf.readUnsignedShort();
        this.rf.skipBytes(2);
        int segCount = this.rf.readUnsignedShort() / 2;
        this.rf.skipBytes(6);
        int[] endCount = new int[segCount];
        for (k = 0; k < segCount; k++) {
            endCount[k] = this.rf.readUnsignedShort();
        }
        this.rf.skipBytes(2);
        int[] startCount = new int[segCount];
        for (k = 0; k < segCount; k++) {
            startCount[k] = this.rf.readUnsignedShort();
        }
        int[] idDelta = new int[segCount];
        for (k = 0; k < segCount; k++) {
            idDelta[k] = this.rf.readUnsignedShort();
        }
        int[] idRO = new int[segCount];
        for (k = 0; k < segCount; k++) {
            idRO[k] = this.rf.readUnsignedShort();
        }
        int[] glyphId = new int[(((table_lenght / 2) - 8) - (segCount * 4))];
        for (k = 0; k < glyphId.length; k++) {
            glyphId[k] = this.rf.readUnsignedShort();
        }
        for (k = 0; k < segCount; k++) {
            int j = startCount[k];
            while (j <= endCount[k] && j != 65535) {
                int glyph;
                int i;
                if (idRO[k] == 0) {
                    glyph = (idDelta[k] + j) & 65535;
                } else {
                    int idx = ((((idRO[k] / 2) + k) - segCount) + j) - startCount[k];
                    if (idx < glyphId.length) {
                        glyph = (glyphId[idx] + idDelta[k]) & 65535;
                    } else {
                        j++;
                    }
                }
                int[] r = new int[]{glyph, getGlyphWidth(r[0])};
                if (!this.fontSpecific) {
                    i = j;
                } else if ((MotionEventCompat.ACTION_POINTER_INDEX_MASK & j) == avcodec.MB_TYPE_L0L1) {
                    i = j & 255;
                } else {
                    i = j;
                }
                h.put(Integer.valueOf(i), r);
                j++;
            }
        }
        return h;
    }

    HashMap<Integer, int[]> readFormat6() throws IOException {
        HashMap<Integer, int[]> h = new HashMap();
        this.rf.skipBytes(4);
        int start_code = this.rf.readUnsignedShort();
        int code_count = this.rf.readUnsignedShort();
        for (int k = 0; k < code_count; k++) {
            h.put(Integer.valueOf(k + start_code), new int[]{this.rf.readUnsignedShort(), getGlyphWidth(r[0])});
        }
        return h;
    }

    void readKerning() throws IOException {
        int[] table_location = (int[]) this.tables.get("kern");
        if (table_location != null) {
            this.rf.seek((long) (table_location[0] + 2));
            int nTables = this.rf.readUnsignedShort();
            int checkpoint = table_location[0] + 4;
            int length = 0;
            for (int k = 0; k < nTables; k++) {
                checkpoint += length;
                this.rf.seek((long) checkpoint);
                this.rf.skipBytes(2);
                length = this.rf.readUnsignedShort();
                if ((65527 & this.rf.readUnsignedShort()) == 1) {
                    int nPairs = this.rf.readUnsignedShort();
                    this.rf.skipBytes(6);
                    for (int j = 0; j < nPairs; j++) {
                        this.kerning.put(this.rf.readInt(), (this.rf.readShort() * 1000) / this.head.unitsPerEm);
                    }
                }
            }
        }
    }

    public int getKerning(int char1, int char2) {
        int[] metrics = getMetricsTT(char1);
        if (metrics == null) {
            return 0;
        }
        int c1 = metrics[0];
        metrics = getMetricsTT(char2);
        if (metrics == null) {
            return 0;
        }
        return this.kerning.get((c1 << 16) + metrics[0]);
    }

    int getRawWidth(int c, String name) {
        int[] metric = getMetricsTT(c);
        if (metric == null) {
            return 0;
        }
        return metric[1];
    }

    protected PdfDictionary getFontDescriptor(PdfIndirectReference fontStream, String subsetPrefix, PdfIndirectReference cidset) {
        PdfDictionary dic = new PdfDictionary(PdfName.FONTDESCRIPTOR);
        dic.put(PdfName.ASCENT, new PdfNumber((this.os_2.sTypoAscender * 1000) / this.head.unitsPerEm));
        dic.put(PdfName.CAPHEIGHT, new PdfNumber((this.os_2.sCapHeight * 1000) / this.head.unitsPerEm));
        dic.put(PdfName.DESCENT, new PdfNumber((this.os_2.sTypoDescender * 1000) / this.head.unitsPerEm));
        dic.put(PdfName.FONTBBOX, new PdfRectangle((float) ((this.head.xMin * 1000) / this.head.unitsPerEm), (float) ((this.head.yMin * 1000) / this.head.unitsPerEm), (float) ((this.head.xMax * 1000) / this.head.unitsPerEm), (float) ((this.head.yMax * 1000) / this.head.unitsPerEm)));
        if (cidset != null) {
            dic.put(PdfName.CIDSET, cidset);
        }
        if (!this.cff) {
            dic.put(PdfName.FONTNAME, new PdfName(subsetPrefix + this.fontName + this.style));
        } else if (this.encoding.startsWith("Identity-")) {
            dic.put(PdfName.FONTNAME, new PdfName(subsetPrefix + this.fontName + "-" + this.encoding));
        } else {
            dic.put(PdfName.FONTNAME, new PdfName(subsetPrefix + this.fontName + this.style));
        }
        dic.put(PdfName.ITALICANGLE, new PdfNumber(this.italicAngle));
        dic.put(PdfName.STEMV, new PdfNumber(80));
        if (fontStream != null) {
            if (this.cff) {
                dic.put(PdfName.FONTFILE3, fontStream);
            } else {
                dic.put(PdfName.FONTFILE2, fontStream);
            }
        }
        int flags = 0;
        if (this.isFixedPitch) {
            flags = 0 | 1;
        }
        flags |= this.fontSpecific ? 4 : 32;
        if ((this.head.macStyle & 2) != 0) {
            flags |= 64;
        }
        if ((this.head.macStyle & 1) != 0) {
            flags |= 262144;
        }
        dic.put(PdfName.FLAGS, new PdfNumber(flags));
        return dic;
    }

    protected PdfDictionary getFontBaseType(PdfIndirectReference fontDescriptor, String subsetPrefix, int firstChar, int lastChar, byte[] shortTag) {
        int k;
        PdfDictionary dic = new PdfDictionary(PdfName.FONT);
        if (this.cff) {
            dic.put(PdfName.SUBTYPE, PdfName.TYPE1);
            dic.put(PdfName.BASEFONT, new PdfName(this.fontName + this.style));
        } else {
            dic.put(PdfName.SUBTYPE, PdfName.TRUETYPE);
            dic.put(PdfName.BASEFONT, new PdfName(subsetPrefix + this.fontName + this.style));
        }
        dic.put(PdfName.BASEFONT, new PdfName(subsetPrefix + this.fontName + this.style));
        if (!this.fontSpecific) {
            for (k = firstChar; k <= lastChar; k++) {
                if (!this.differences[k].equals(BaseFont.notdef)) {
                    firstChar = k;
                    break;
                }
            }
            if (this.encoding.equals("Cp1252") || this.encoding.equals(BaseFont.MACROMAN)) {
                PdfObject pdfObject;
                PdfName pdfName = PdfName.ENCODING;
                if (this.encoding.equals("Cp1252")) {
                    pdfObject = PdfName.WIN_ANSI_ENCODING;
                } else {
                    pdfObject = PdfName.MAC_ROMAN_ENCODING;
                }
                dic.put(pdfName, pdfObject);
            } else {
                PdfDictionary enc = new PdfDictionary(PdfName.ENCODING);
                PdfArray dif = new PdfArray();
                boolean gap = true;
                for (k = firstChar; k <= lastChar; k++) {
                    if (shortTag[k] != (byte) 0) {
                        if (gap) {
                            dif.add(new PdfNumber(k));
                            gap = false;
                        }
                        dif.add(new PdfName(this.differences[k]));
                    } else {
                        gap = true;
                    }
                }
                enc.put(PdfName.DIFFERENCES, dif);
                dic.put(PdfName.ENCODING, enc);
            }
        }
        dic.put(PdfName.FIRSTCHAR, new PdfNumber(firstChar));
        dic.put(PdfName.LASTCHAR, new PdfNumber(lastChar));
        PdfArray wd = new PdfArray();
        for (k = firstChar; k <= lastChar; k++) {
            if (shortTag[k] == (byte) 0) {
                wd.add(new PdfNumber(0));
            } else {
                wd.add(new PdfNumber(this.widths[k]));
            }
        }
        dic.put(PdfName.WIDTHS, wd);
        if (fontDescriptor != null) {
            dic.put(PdfName.FONTDESCRIPTOR, fontDescriptor);
        }
        return dic;
    }

    protected byte[] getFullFont() throws IOException {
        Throwable th;
        RandomAccessFileOrArray rf2 = null;
        try {
            RandomAccessFileOrArray rf22 = new RandomAccessFileOrArray(this.rf);
            try {
                rf22.reOpen();
                byte[] b = new byte[((int) rf22.length())];
                rf22.readFully(b);
                if (rf22 != null) {
                    try {
                        rf22.close();
                    } catch (Exception e) {
                    }
                }
                return b;
            } catch (Throwable th2) {
                th = th2;
                rf2 = rf22;
                if (rf2 != null) {
                    try {
                        rf2.close();
                    } catch (Exception e2) {
                    }
                }
                throw th;
            }
        } catch (Throwable th3) {
            th = th3;
            if (rf2 != null) {
                rf2.close();
            }
            throw th;
        }
    }

    protected synchronized byte[] getSubSet(HashSet glyphs, boolean subsetp) throws IOException, DocumentException {
        byte[] process;
        boolean z = true;
        synchronized (this) {
            String str = this.fileName;
            RandomAccessFileOrArray randomAccessFileOrArray = new RandomAccessFileOrArray(this.rf);
            int i = this.directoryOffset;
            if (subsetp) {
                z = false;
            }
            process = new TrueTypeFontSubSet(str, randomAccessFileOrArray, glyphs, i, true, z).process();
        }
        return process;
    }

    protected static int[] compactRanges(ArrayList<int[]> ranges) {
        int k;
        ArrayList<int[]> simp = new ArrayList();
        for (k = 0; k < ranges.size(); k++) {
            int[] r = (int[]) ranges.get(k);
            for (int j = 0; j < r.length; j += 2) {
                simp.add(new int[]{Math.max(0, Math.min(r[j], r[j + 1])), Math.min(65535, Math.max(r[j], r[j + 1]))});
            }
        }
        for (int k1 = 0; k1 < simp.size() - 1; k1++) {
            int k2 = k1 + 1;
            while (k2 < simp.size()) {
                int[] r1 = (int[]) simp.get(k1);
                int[] r2 = (int[]) simp.get(k2);
                if ((r1[0] >= r2[0] && r1[0] <= r2[1]) || (r1[1] >= r2[0] && r1[0] <= r2[1])) {
                    r1[0] = Math.min(r1[0], r2[0]);
                    r1[1] = Math.max(r1[1], r2[1]);
                    simp.remove(k2);
                    k2--;
                }
                k2++;
            }
        }
        int[] s = new int[(simp.size() * 2)];
        for (k = 0; k < simp.size(); k++) {
            r = (int[]) simp.get(k);
            s[k * 2] = r[0];
            s[(k * 2) + 1] = r[1];
        }
        return s;
    }

    protected void addRangeUni(HashMap<Integer, int[]> longTag, boolean includeMetrics, boolean subsetp) {
        if (!subsetp) {
            if (this.subsetRanges != null || this.directoryOffset > 0) {
                int[] rg = (this.subsetRanges != null || this.directoryOffset <= 0) ? compactRanges(this.subsetRanges) : new int[]{0, 65535};
                HashMap<Integer, int[]> usemap;
                if (!this.fontSpecific && this.cmap31 != null) {
                    usemap = this.cmap31;
                } else if (this.fontSpecific && this.cmap10 != null) {
                    usemap = this.cmap10;
                } else if (this.cmap31 != null) {
                    usemap = this.cmap31;
                } else {
                    usemap = this.cmap10;
                }
                for (Entry<Integer, int[]> e : usemap.entrySet()) {
                    Integer gi = Integer.valueOf(((int[]) e.getValue())[0]);
                    if (!longTag.containsKey(gi)) {
                        int c = ((Integer) e.getKey()).intValue();
                        boolean skip = true;
                        int k = 0;
                        while (k < rg.length) {
                            if (c >= rg[k] && c <= rg[k + 1]) {
                                skip = false;
                                break;
                            }
                            k += 2;
                        }
                        if (!skip) {
                            longTag.put(gi, includeMetrics ? new int[]{v[0], v[1], c} : null);
                        }
                    }
                }
            }
        }
    }

    protected void addRangeUni(HashSet<Integer> longTag, boolean subsetp) {
        if (!subsetp) {
            if (this.subsetRanges != null || this.directoryOffset > 0) {
                int[] rg = (this.subsetRanges != null || this.directoryOffset <= 0) ? compactRanges(this.subsetRanges) : new int[]{0, 65535};
                HashMap<Integer, int[]> usemap;
                if (!this.fontSpecific && this.cmap31 != null) {
                    usemap = this.cmap31;
                } else if (this.fontSpecific && this.cmap10 != null) {
                    usemap = this.cmap10;
                } else if (this.cmap31 != null) {
                    usemap = this.cmap31;
                } else {
                    usemap = this.cmap10;
                }
                for (Entry<Integer, int[]> e : usemap.entrySet()) {
                    Integer gi = Integer.valueOf(((int[]) e.getValue())[0]);
                    if (!longTag.contains(gi)) {
                        int c = ((Integer) e.getKey()).intValue();
                        boolean skip = true;
                        int k = 0;
                        while (k < rg.length) {
                            if (c >= rg[k] && c <= rg[k + 1]) {
                                skip = false;
                                break;
                            }
                            k += 2;
                        }
                        if (!skip) {
                            longTag.add(gi);
                        }
                    }
                }
            }
        }
    }

    void writeFont(PdfWriter writer, PdfIndirectReference ref, Object[] params) throws DocumentException, IOException {
        int k;
        int firstChar = ((Integer) params[0]).intValue();
        int lastChar = ((Integer) params[1]).intValue();
        byte[] shortTag = (byte[]) params[2];
        boolean subsetp = ((Boolean) params[3]).booleanValue() && this.subset;
        if (!subsetp) {
            firstChar = 0;
            lastChar = shortTag.length - 1;
            for (k = 0; k < shortTag.length; k++) {
                shortTag[k] = (byte) 1;
            }
        }
        PdfIndirectReference ind_font = null;
        String subsetPrefix = "";
        if (this.embedded) {
            if (this.cff) {
                ind_font = writer.addToBody(new StreamFont(readCffFont(), "Type1C", this.compressionLevel)).getIndirectReference();
            } else {
                byte[] b;
                if (subsetp) {
                    subsetPrefix = BaseFont.createSubsetPrefix();
                }
                HashSet<Integer> glyphs = new HashSet();
                for (k = firstChar; k <= lastChar; k++) {
                    if (shortTag[k] != (byte) 0) {
                        int[] metrics = null;
                        if (this.specialMap != null) {
                            int[] cd = GlyphList.nameToUnicode(this.differences[k]);
                            if (cd != null) {
                                metrics = getMetricsTT(cd[0]);
                            }
                        } else if (this.fontSpecific) {
                            metrics = getMetricsTT(k);
                        } else {
                            metrics = getMetricsTT(this.unicodeDifferences[k]);
                        }
                        if (metrics != null) {
                            glyphs.add(Integer.valueOf(metrics[0]));
                        }
                    }
                }
                addRangeUni(glyphs, subsetp);
                if (!subsetp && this.directoryOffset == 0 && this.subsetRanges == null) {
                    b = getFullFont();
                } else {
                    b = getSubSet(new HashSet(glyphs), subsetp);
                }
                ind_font = writer.addToBody(new StreamFont(b, new int[]{b.length}, this.compressionLevel)).getIndirectReference();
            }
        }
        PdfObject pobj = getFontDescriptor(ind_font, subsetPrefix, null);
        if (pobj != null) {
            ind_font = writer.addToBody(pobj).getIndirectReference();
        }
        writer.addToBody(getFontBaseType(ind_font, subsetPrefix, firstChar, lastChar, shortTag), ref);
    }

    protected byte[] readCffFont() throws IOException {
        RandomAccessFileOrArray rf2 = new RandomAccessFileOrArray(this.rf);
        byte[] b = new byte[this.cffLength];
        try {
            rf2.reOpen();
            rf2.seek((long) this.cffOffset);
            rf2.readFully(b);
            return b;
        } finally {
            try {
                rf2.close();
            } catch (Exception e) {
            }
        }
    }

    public PdfStream getFullFontStream() throws IOException, DocumentException {
        if (this.cff) {
            return new StreamFont(readCffFont(), "Type1C", this.compressionLevel);
        }
        return new StreamFont(getFullFont(), new int[]{getFullFont().length}, this.compressionLevel);
    }

    public float getFontDescriptor(int key, float fontSize) {
        switch (key) {
            case 1:
                return (((float) this.os_2.sTypoAscender) * fontSize) / ((float) this.head.unitsPerEm);
            case 2:
                return (((float) this.os_2.sCapHeight) * fontSize) / ((float) this.head.unitsPerEm);
            case 3:
                return (((float) this.os_2.sTypoDescender) * fontSize) / ((float) this.head.unitsPerEm);
            case 4:
                return (float) this.italicAngle;
            case 5:
                return (((float) this.head.xMin) * fontSize) / ((float) this.head.unitsPerEm);
            case 6:
                return (((float) this.head.yMin) * fontSize) / ((float) this.head.unitsPerEm);
            case 7:
                return (((float) this.head.xMax) * fontSize) / ((float) this.head.unitsPerEm);
            case 8:
                return (((float) this.head.yMax) * fontSize) / ((float) this.head.unitsPerEm);
            case 9:
                return (((float) this.hhea.Ascender) * fontSize) / ((float) this.head.unitsPerEm);
            case 10:
                return (((float) this.hhea.Descender) * fontSize) / ((float) this.head.unitsPerEm);
            case 11:
                return (((float) this.hhea.LineGap) * fontSize) / ((float) this.head.unitsPerEm);
            case 12:
                return (((float) this.hhea.advanceWidthMax) * fontSize) / ((float) this.head.unitsPerEm);
            case 13:
                return (((float) (this.underlinePosition - (this.underlineThickness / 2))) * fontSize) / ((float) this.head.unitsPerEm);
            case 14:
                return (((float) this.underlineThickness) * fontSize) / ((float) this.head.unitsPerEm);
            case 15:
                return (((float) this.os_2.yStrikeoutPosition) * fontSize) / ((float) this.head.unitsPerEm);
            case 16:
                return (((float) this.os_2.yStrikeoutSize) * fontSize) / ((float) this.head.unitsPerEm);
            case 17:
                return (((float) this.os_2.ySubscriptYSize) * fontSize) / ((float) this.head.unitsPerEm);
            case 18:
                return (((float) (-this.os_2.ySubscriptYOffset)) * fontSize) / ((float) this.head.unitsPerEm);
            case 19:
                return (((float) this.os_2.ySuperscriptYSize) * fontSize) / ((float) this.head.unitsPerEm);
            case 20:
                return (((float) this.os_2.ySuperscriptYOffset) * fontSize) / ((float) this.head.unitsPerEm);
            case 21:
                return (float) this.os_2.usWeightClass;
            case 22:
                return (float) this.os_2.usWidthClass;
            default:
                return 0.0f;
        }
    }

    public int[] getMetricsTT(int c) {
        if (this.cmapExt != null) {
            return (int[]) this.cmapExt.get(Integer.valueOf(c));
        }
        if (!this.fontSpecific && this.cmap31 != null) {
            return (int[]) this.cmap31.get(Integer.valueOf(c));
        }
        if (this.fontSpecific && this.cmap10 != null) {
            return (int[]) this.cmap10.get(Integer.valueOf(c));
        }
        if (this.cmap31 != null) {
            return (int[]) this.cmap31.get(Integer.valueOf(c));
        }
        if (this.cmap10 != null) {
            return (int[]) this.cmap10.get(Integer.valueOf(c));
        }
        return null;
    }

    public String getPostscriptFontName() {
        return this.fontName;
    }

    public String[] getCodePagesSupported() {
        long cp = (((long) this.os_2.ulCodePageRange2) << 32) + (((long) this.os_2.ulCodePageRange1) & 4294967295L);
        int count = 0;
        long bit = 1;
        int k = 0;
        while (k < 64) {
            if (!((cp & bit) == 0 || codePages[k] == null)) {
                count++;
            }
            bit <<= 1;
            k++;
        }
        String[] ret = new String[count];
        bit = 1;
        k = 0;
        int count2 = 0;
        while (k < 64) {
            if ((cp & bit) == 0 || codePages[k] == null) {
                count = count2;
            } else {
                count = count2 + 1;
                ret[count2] = codePages[k];
            }
            bit <<= 1;
            k++;
            count2 = count;
        }
        return ret;
    }

    public String[][] getFullFontName() {
        return this.fullName;
    }

    public String[][] getAllNameEntries() {
        return this.allNameEntries;
    }

    public String[][] getFamilyFontName() {
        return this.familyName;
    }

    public boolean hasKernPairs() {
        return this.kerning.size() > 0;
    }

    public void setPostscriptFontName(String name) {
        this.fontName = name;
    }

    public boolean setKerning(int char1, int char2, int kern) {
        int[] metrics = getMetricsTT(char1);
        if (metrics == null) {
            return false;
        }
        int c1 = metrics[0];
        metrics = getMetricsTT(char2);
        if (metrics == null) {
            return false;
        }
        this.kerning.put((c1 << 16) + metrics[0], kern);
        return true;
    }

    protected int[] getRawCharBBox(int c, String name) {
        HashMap<Integer, int[]> map;
        if (name == null || this.cmap31 == null) {
            map = this.cmap10;
        } else {
            map = this.cmap31;
        }
        if (map == null) {
            return null;
        }
        int[] metric = (int[]) map.get(Integer.valueOf(c));
        if (metric == null || this.bboxes == null) {
            return null;
        }
        return this.bboxes[metric[0]];
    }
}
