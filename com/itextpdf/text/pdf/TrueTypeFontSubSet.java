package com.itextpdf.text.pdf;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.error_messages.MessageLocalization;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

class TrueTypeFontSubSet {
    static final int ARG_1_AND_2_ARE_WORDS = 1;
    static final int HEAD_LOCA_FORMAT_OFFSET = 51;
    static final int MORE_COMPONENTS = 32;
    static final int TABLE_CHECKSUM = 0;
    static final int TABLE_LENGTH = 2;
    static final int TABLE_OFFSET = 1;
    static final int WE_HAVE_AN_X_AND_Y_SCALE = 64;
    static final int WE_HAVE_A_SCALE = 8;
    static final int WE_HAVE_A_TWO_BY_TWO = 128;
    static final int[] entrySelectors = new int[]{0, 0, 1, 1, 2, 2, 2, 2, 3, 3, 3, 3, 3, 3, 3, 3, 4, 4, 4, 4, 4};
    static final String[] tableNamesCmap = new String[]{"cmap", "cvt ", "fpgm", "glyf", "head", "hhea", "hmtx", "loca", "maxp", "prep"};
    static final String[] tableNamesExtra = new String[]{"OS/2", "cmap", "cvt ", "fpgm", "glyf", "head", "hhea", "hmtx", "loca", "maxp", "name, prep"};
    static final String[] tableNamesSimple = new String[]{"cvt ", "fpgm", "glyf", "head", "hhea", "hmtx", "loca", "maxp", "prep"};
    protected int directoryOffset;
    protected String fileName;
    protected int fontPtr;
    protected int glyfTableRealSize;
    protected ArrayList<Integer> glyphsInList;
    protected HashSet<Integer> glyphsUsed;
    protected boolean includeCmap;
    protected boolean includeExtras;
    protected boolean locaShortTable;
    protected int[] locaTable;
    protected int locaTableRealSize;
    protected byte[] newGlyfTable;
    protected int[] newLocaTable;
    protected byte[] newLocaTableOut;
    protected byte[] outFont;
    protected RandomAccessFileOrArray rf;
    protected HashMap<String, int[]> tableDirectory;
    protected int tableGlyphOffset;

    TrueTypeFontSubSet(String fileName, RandomAccessFileOrArray rf, HashSet<Integer> glyphsUsed, int directoryOffset, boolean includeCmap, boolean includeExtras) {
        this.fileName = fileName;
        this.rf = rf;
        this.glyphsUsed = glyphsUsed;
        this.includeCmap = includeCmap;
        this.includeExtras = includeExtras;
        this.directoryOffset = directoryOffset;
        this.glyphsInList = new ArrayList(glyphsUsed);
    }

    byte[] process() throws IOException, DocumentException {
        try {
            this.rf.reOpen();
            createTableDirectory();
            readLoca();
            flatGlyphs();
            createNewGlyphTables();
            locaTobytes();
            assembleFont();
            byte[] bArr = this.outFont;
            return bArr;
        } finally {
            try {
                this.rf.close();
            } catch (Exception e) {
            }
        }
    }

    protected void assembleFont() throws IOException {
        String[] tableNames;
        int[] tableLocation;
        int fullFontSize = 0;
        if (this.includeExtras) {
            tableNames = tableNamesExtra;
        } else if (this.includeCmap) {
            tableNames = tableNamesCmap;
        } else {
            tableNames = tableNamesSimple;
        }
        int tablesUsed = 2;
        for (String name : tableNames) {
            if (!(name.equals("glyf") || name.equals("loca"))) {
                tableLocation = (int[]) this.tableDirectory.get(name);
                if (tableLocation != null) {
                    tablesUsed++;
                    fullFontSize += (tableLocation[2] + 3) & -4;
                }
            }
        }
        int ref = (tablesUsed * 16) + 12;
        this.outFont = new byte[(((fullFontSize + this.newLocaTableOut.length) + this.newGlyfTable.length) + ref)];
        this.fontPtr = 0;
        writeFontInt(65536);
        writeFontShort(tablesUsed);
        int selector = entrySelectors[tablesUsed];
        writeFontShort((1 << selector) * 16);
        writeFontShort(selector);
        writeFontShort((tablesUsed - (1 << selector)) * 16);
        for (String name2 : tableNames) {
            tableLocation = (int[]) this.tableDirectory.get(name2);
            if (tableLocation != null) {
                int len;
                writeFontString(name2);
                if (name2.equals("glyf")) {
                    writeFontInt(calculateChecksum(this.newGlyfTable));
                    len = this.glyfTableRealSize;
                } else if (name2.equals("loca")) {
                    writeFontInt(calculateChecksum(this.newLocaTableOut));
                    len = this.locaTableRealSize;
                } else {
                    writeFontInt(tableLocation[0]);
                    len = tableLocation[2];
                }
                writeFontInt(ref);
                writeFontInt(len);
                ref += (len + 3) & -4;
            }
        }
        for (String name22 : tableNames) {
            tableLocation = (int[]) this.tableDirectory.get(name22);
            if (tableLocation != null) {
                if (name22.equals("glyf")) {
                    System.arraycopy(this.newGlyfTable, 0, this.outFont, this.fontPtr, this.newGlyfTable.length);
                    this.fontPtr += this.newGlyfTable.length;
                    this.newGlyfTable = null;
                } else if (name22.equals("loca")) {
                    System.arraycopy(this.newLocaTableOut, 0, this.outFont, this.fontPtr, this.newLocaTableOut.length);
                    this.fontPtr += this.newLocaTableOut.length;
                    this.newLocaTableOut = null;
                } else {
                    this.rf.seek((long) tableLocation[1]);
                    this.rf.readFully(this.outFont, this.fontPtr, tableLocation[2]);
                    this.fontPtr += (tableLocation[2] + 3) & -4;
                }
            }
        }
    }

    protected void createTableDirectory() throws IOException, DocumentException {
        this.tableDirectory = new HashMap();
        this.rf.seek((long) this.directoryOffset);
        if (this.rf.readInt() != 65536) {
            throw new DocumentException(MessageLocalization.getComposedMessage("1.is.not.a.true.type.file", this.fileName));
        }
        int num_tables = this.rf.readUnsignedShort();
        this.rf.skipBytes(6);
        for (int k = 0; k < num_tables; k++) {
            this.tableDirectory.put(readStandardString(4), new int[]{this.rf.readInt(), this.rf.readInt(), this.rf.readInt()});
        }
    }

    protected void readLoca() throws IOException, DocumentException {
        int[] tableLocation = (int[]) this.tableDirectory.get("head");
        if (tableLocation == null) {
            throw new DocumentException(MessageLocalization.getComposedMessage("table.1.does.not.exist.in.2", "head", this.fileName));
        }
        boolean z;
        this.rf.seek((long) (tableLocation[1] + 51));
        if (this.rf.readUnsignedShort() == 0) {
            z = true;
        } else {
            z = false;
        }
        this.locaShortTable = z;
        tableLocation = (int[]) this.tableDirectory.get("loca");
        if (tableLocation == null) {
            throw new DocumentException(MessageLocalization.getComposedMessage("table.1.does.not.exist.in.2", "loca", this.fileName));
        }
        this.rf.seek((long) tableLocation[1]);
        int k;
        if (this.locaShortTable) {
            int entries;
            entries = tableLocation[2] / 2;
            this.locaTable = new int[entries];
            for (k = 0; k < entries; k++) {
                this.locaTable[k] = this.rf.readUnsignedShort() * 2;
            }
            return;
        }
        entries = tableLocation[2] / 4;
        this.locaTable = new int[entries];
        for (k = 0; k < entries; k++) {
            this.locaTable[k] = this.rf.readInt();
        }
    }

    protected void createNewGlyphTables() throws IOException {
        int k;
        this.newLocaTable = new int[this.locaTable.length];
        int[] activeGlyphs = new int[this.glyphsInList.size()];
        for (k = 0; k < activeGlyphs.length; k++) {
            activeGlyphs[k] = ((Integer) this.glyphsInList.get(k)).intValue();
        }
        Arrays.sort(activeGlyphs);
        int glyfSize = 0;
        for (int glyph : activeGlyphs) {
            glyfSize += this.locaTable[glyph + 1] - this.locaTable[glyph];
        }
        this.glyfTableRealSize = glyfSize;
        this.newGlyfTable = new byte[((glyfSize + 3) & -4)];
        int glyfPtr = 0;
        int listGlyf = 0;
        k = 0;
        while (k < this.newLocaTable.length) {
            this.newLocaTable[k] = glyfPtr;
            if (listGlyf < activeGlyphs.length && activeGlyphs[listGlyf] == k) {
                listGlyf++;
                this.newLocaTable[k] = glyfPtr;
                int start = this.locaTable[k];
                int len = this.locaTable[k + 1] - start;
                if (len > 0) {
                    this.rf.seek((long) (this.tableGlyphOffset + start));
                    this.rf.readFully(this.newGlyfTable, glyfPtr, len);
                    glyfPtr += len;
                }
            }
            k++;
        }
    }

    protected void locaTobytes() {
        if (this.locaShortTable) {
            this.locaTableRealSize = this.newLocaTable.length * 2;
        } else {
            this.locaTableRealSize = this.newLocaTable.length * 4;
        }
        this.newLocaTableOut = new byte[((this.locaTableRealSize + 3) & -4)];
        this.outFont = this.newLocaTableOut;
        this.fontPtr = 0;
        for (int k = 0; k < this.newLocaTable.length; k++) {
            if (this.locaShortTable) {
                writeFontShort(this.newLocaTable[k] / 2);
            } else {
                writeFontInt(this.newLocaTable[k]);
            }
        }
    }

    protected void flatGlyphs() throws IOException, DocumentException {
        int[] tableLocation = (int[]) this.tableDirectory.get("glyf");
        if (tableLocation == null) {
            throw new DocumentException(MessageLocalization.getComposedMessage("table.1.does.not.exist.in.2", "glyf", this.fileName));
        }
        Integer glyph0 = Integer.valueOf(0);
        if (!this.glyphsUsed.contains(glyph0)) {
            this.glyphsUsed.add(glyph0);
            this.glyphsInList.add(glyph0);
        }
        this.tableGlyphOffset = tableLocation[1];
        for (int k = 0; k < this.glyphsInList.size(); k++) {
            checkGlyphComposite(((Integer) this.glyphsInList.get(k)).intValue());
        }
    }

    protected void checkGlyphComposite(int glyph) throws IOException {
        int start = this.locaTable[glyph];
        if (start != this.locaTable[glyph + 1]) {
            this.rf.seek((long) (this.tableGlyphOffset + start));
            if (this.rf.readShort() < 0) {
                this.rf.skipBytes(8);
                while (true) {
                    int flags = this.rf.readUnsignedShort();
                    Integer cGlyph = Integer.valueOf(this.rf.readUnsignedShort());
                    if (!this.glyphsUsed.contains(cGlyph)) {
                        this.glyphsUsed.add(cGlyph);
                        this.glyphsInList.add(cGlyph);
                    }
                    if ((flags & 32) != 0) {
                        int skip;
                        if ((flags & 1) != 0) {
                            skip = 4;
                        } else {
                            skip = 2;
                        }
                        if ((flags & 8) != 0) {
                            skip += 2;
                        } else if ((flags & 64) != 0) {
                            skip += 4;
                        }
                        if ((flags & 128) != 0) {
                            skip += 8;
                        }
                        this.rf.skipBytes(skip);
                    } else {
                        return;
                    }
                }
            }
        }
    }

    protected String readStandardString(int length) throws IOException {
        byte[] buf = new byte[length];
        this.rf.readFully(buf);
        try {
            return new String(buf, "Cp1252");
        } catch (Exception e) {
            throw new ExceptionConverter(e);
        }
    }

    protected void writeFontShort(int n) {
        byte[] bArr = this.outFont;
        int i = this.fontPtr;
        this.fontPtr = i + 1;
        bArr[i] = (byte) (n >> 8);
        bArr = this.outFont;
        i = this.fontPtr;
        this.fontPtr = i + 1;
        bArr[i] = (byte) n;
    }

    protected void writeFontInt(int n) {
        byte[] bArr = this.outFont;
        int i = this.fontPtr;
        this.fontPtr = i + 1;
        bArr[i] = (byte) (n >> 24);
        bArr = this.outFont;
        i = this.fontPtr;
        this.fontPtr = i + 1;
        bArr[i] = (byte) (n >> 16);
        bArr = this.outFont;
        i = this.fontPtr;
        this.fontPtr = i + 1;
        bArr[i] = (byte) (n >> 8);
        bArr = this.outFont;
        i = this.fontPtr;
        this.fontPtr = i + 1;
        bArr[i] = (byte) n;
    }

    protected void writeFontString(String s) {
        byte[] b = PdfEncodings.convertToBytes(s, "Cp1252");
        System.arraycopy(b, 0, this.outFont, this.fontPtr, b.length);
        this.fontPtr += b.length;
    }

    protected int calculateChecksum(byte[] b) {
        int v0 = 0;
        int v1 = 0;
        int v2 = 0;
        int v3 = 0;
        int ptr = 0;
        for (int k = 0; k < b.length / 4; k++) {
            int ptr2 = ptr + 1;
            v3 += b[ptr] & 255;
            ptr = ptr2 + 1;
            v2 += b[ptr2] & 255;
            ptr2 = ptr + 1;
            v1 += b[ptr] & 255;
            ptr = ptr2 + 1;
            v0 += b[ptr2] & 255;
        }
        return (((v1 << 8) + v0) + (v2 << 16)) + (v3 << 24);
    }
}
