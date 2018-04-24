package com.itextpdf.text.pdf;

import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.html.HtmlTags;
import java.util.Iterator;
import java.util.LinkedList;
import org.apache.poi.hssf.model.InternalWorkbook;
import org.apache.poi.ss.formula.functions.Complex;

public class CFFFont {
    static final String[] operatorNames = new String[]{"version", "Notice", "FullName", "FamilyName", "Weight", "FontBBox", "BlueValues", "OtherBlues", "FamilyBlues", "FamilyOtherBlues", "StdHW", "StdVW", "UNKNOWN_12", "UniqueID", "XUID", "charset", "Encoding", "CharStrings", "Private", "Subrs", "defaultWidthX", "nominalWidthX", "UNKNOWN_22", "UNKNOWN_23", "UNKNOWN_24", "UNKNOWN_25", "UNKNOWN_26", "UNKNOWN_27", "UNKNOWN_28", "UNKNOWN_29", "UNKNOWN_30", "UNKNOWN_31", "Copyright", "isFixedPitch", "ItalicAngle", "UnderlinePosition", "UnderlineThickness", "PaintType", "CharstringType", "FontMatrix", "StrokeWidth", "BlueScale", "BlueShift", "BlueFuzz", "StemSnapH", "StemSnapV", "ForceBold", "UNKNOWN_12_15", "UNKNOWN_12_16", "LanguageGroup", "ExpansionFactor", "initialRandomSeed", "SyntheticBase", "PostScript", "BaseFontName", "BaseFontBlend", "UNKNOWN_12_24", "UNKNOWN_12_25", "UNKNOWN_12_26", "UNKNOWN_12_27", "UNKNOWN_12_28", "UNKNOWN_12_29", "ROS", "CIDFontVersion", "CIDFontRevision", "CIDFontType", "CIDCount", "UIDBase", "FDArray", "FDSelect", "FontName"};
    static final String[] standardStrings = new String[]{BaseFont.notdef, "space", "exclam", "quotedbl", "numbersign", "dollar", "percent", "ampersand", "quoteright", "parenleft", "parenright", "asterisk", "plus", "comma", "hyphen", "period", "slash", "zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "colon", "semicolon", "less", "equal", "greater", "question", "at", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "bracketleft", "backslash", "bracketright", "asciicircum", "underscore", "quoteleft", HtmlTags.f32A, HtmlTags.f33B, "c", "d", "e", "f", "g", "h", "i", Complex.SUPPORTED_SUFFIX, "k", "l", "m", "n", "o", HtmlTags.f35P, "q", "r", HtmlTags.f36S, "t", HtmlTags.f37U, "v", "w", "x", "y", "z", "braceleft", "bar", "braceright", "asciitilde", "exclamdown", "cent", "sterling", "fraction", "yen", "florin", "section", "currency", "quotesingle", "quotedblleft", "guillemotleft", "guilsinglleft", "guilsinglright", "fi", "fl", "endash", "dagger", "daggerdbl", "periodcentered", "paragraph", "bullet", "quotesinglbase", "quotedblbase", "quotedblright", "guillemotright", "ellipsis", "perthousand", "questiondown", "grave", "acute", "circumflex", "tilde", "macron", "breve", "dotaccent", "dieresis", "ring", "cedilla", "hungarumlaut", "ogonek", "caron", "emdash", "AE", "ordfeminine", "Lslash", "Oslash", "OE", "ordmasculine", "ae", "dotlessi", "lslash", "oslash", "oe", "germandbls", "onesuperior", "logicalnot", "mu", "trademark", "Eth", "onehalf", "plusminus", "Thorn", "onequarter", "divide", "brokenbar", "degree", "thorn", "threequarters", "twosuperior", "registered", "minus", "eth", "multiply", "threesuperior", "copyright", "Aacute", "Acircumflex", "Adieresis", "Agrave", "Aring", "Atilde", "Ccedilla", "Eacute", "Ecircumflex", "Edieresis", "Egrave", "Iacute", "Icircumflex", "Idieresis", "Igrave", "Ntilde", "Oacute", "Ocircumflex", "Odieresis", "Ograve", "Otilde", "Scaron", "Uacute", "Ucircumflex", "Udieresis", "Ugrave", "Yacute", "Ydieresis", "Zcaron", "aacute", "acircumflex", "adieresis", "agrave", "aring", "atilde", "ccedilla", "eacute", "ecircumflex", "edieresis", "egrave", "iacute", "icircumflex", "idieresis", "igrave", "ntilde", "oacute", "ocircumflex", "odieresis", "ograve", "otilde", "scaron", "uacute", "ucircumflex", "udieresis", "ugrave", "yacute", "ydieresis", "zcaron", "exclamsmall", "Hungarumlautsmall", "dollaroldstyle", "dollarsuperior", "ampersandsmall", "Acutesmall", "parenleftsuperior", "parenrightsuperior", "twodotenleader", "onedotenleader", "zerooldstyle", "oneoldstyle", "twooldstyle", "threeoldstyle", "fouroldstyle", "fiveoldstyle", "sixoldstyle", "sevenoldstyle", "eightoldstyle", "nineoldstyle", "commasuperior", "threequartersemdash", "periodsuperior", "questionsmall", "asuperior", "bsuperior", "centsuperior", "dsuperior", "esuperior", "isuperior", "lsuperior", "msuperior", "nsuperior", "osuperior", "rsuperior", "ssuperior", "tsuperior", "ff", "ffi", "ffl", "parenleftinferior", "parenrightinferior", "Circumflexsmall", "hyphensuperior", "Gravesmall", "Asmall", "Bsmall", "Csmall", "Dsmall", "Esmall", "Fsmall", "Gsmall", "Hsmall", "Ismall", "Jsmall", "Ksmall", "Lsmall", "Msmall", "Nsmall", "Osmall", "Psmall", "Qsmall", "Rsmall", "Ssmall", "Tsmall", "Usmall", "Vsmall", "Wsmall", "Xsmall", "Ysmall", "Zsmall", "colonmonetary", "onefitted", "rupiah", "Tildesmall", "exclamdownsmall", "centoldstyle", "Lslashsmall", "Scaronsmall", "Zcaronsmall", "Dieresissmall", "Brevesmall", "Caronsmall", "Dotaccentsmall", "Macronsmall", "figuredash", "hypheninferior", "Ogoneksmall", "Ringsmall", "Cedillasmall", "questiondownsmall", "oneeighth", "threeeighths", "fiveeighths", "seveneighths", "onethird", "twothirds", "zerosuperior", "foursuperior", "fivesuperior", "sixsuperior", "sevensuperior", "eightsuperior", "ninesuperior", "zeroinferior", "oneinferior", "twoinferior", "threeinferior", "fourinferior", "fiveinferior", "sixinferior", "seveninferior", "eightinferior", "nineinferior", "centinferior", "dollarinferior", "periodinferior", "commainferior", "Agravesmall", "Aacutesmall", "Acircumflexsmall", "Atildesmall", "Adieresissmall", "Aringsmall", "AEsmall", "Ccedillasmall", "Egravesmall", "Eacutesmall", "Ecircumflexsmall", "Edieresissmall", "Igravesmall", "Iacutesmall", "Icircumflexsmall", "Idieresissmall", "Ethsmall", "Ntildesmall", "Ogravesmall", "Oacutesmall", "Ocircumflexsmall", "Otildesmall", "Odieresissmall", "OEsmall", "Oslashsmall", "Ugravesmall", "Uacutesmall", "Ucircumflexsmall", "Udieresissmall", "Yacutesmall", "Thornsmall", "Ydieresissmall", "001.000", "001.001", "001.002", "001.003", "Black", "Bold", InternalWorkbook.OLD_WORKBOOK_DIR_ENTRY_NAME, "Light", "Medium", "Regular", "Roman", "Semibold"};
    protected int arg_count = 0;
    protected Object[] args = new Object[48];
    protected RandomAccessFileOrArray buf;
    protected Font[] fonts;
    protected int gsubrIndexOffset;
    protected int[] gsubrOffsets;
    protected String key;
    protected int nameIndexOffset;
    protected int[] nameOffsets;
    int nextIndexOffset;
    private int offSize;
    protected int stringIndexOffset;
    protected int[] stringOffsets;
    protected int topdictIndexOffset;
    protected int[] topdictOffsets;

    protected static abstract class Item {
        protected int myOffset = -1;

        protected Item() {
        }

        public void increment(int[] currentOffset) {
            this.myOffset = currentOffset[0];
        }

        public void emit(byte[] buffer) {
        }

        public void xref() {
        }
    }

    protected static final class DictNumberItem extends Item {
        public int size = 5;
        public final int value;

        public DictNumberItem(int value) {
            this.value = value;
        }

        public void increment(int[] currentOffset) {
            super.increment(currentOffset);
            currentOffset[0] = currentOffset[0] + this.size;
        }

        public void emit(byte[] buffer) {
            if (this.size == 5) {
                buffer[this.myOffset] = (byte) 29;
                buffer[this.myOffset + 1] = (byte) ((this.value >>> 24) & 255);
                buffer[this.myOffset + 2] = (byte) ((this.value >>> 16) & 255);
                buffer[this.myOffset + 3] = (byte) ((this.value >>> 8) & 255);
                buffer[this.myOffset + 4] = (byte) ((this.value >>> 0) & 255);
            }
        }
    }

    protected static abstract class OffsetItem extends Item {
        public int value;

        protected OffsetItem() {
        }

        public void set(int offset) {
            this.value = offset;
        }
    }

    protected static final class DictOffsetItem extends OffsetItem {
        public final int size = 5;

        public void increment(int[] currentOffset) {
            super.increment(currentOffset);
            currentOffset[0] = currentOffset[0] + this.size;
        }

        public void emit(byte[] buffer) {
            if (this.size == 5) {
                buffer[this.myOffset] = (byte) 29;
                buffer[this.myOffset + 1] = (byte) ((this.value >>> 24) & 255);
                buffer[this.myOffset + 2] = (byte) ((this.value >>> 16) & 255);
                buffer[this.myOffset + 3] = (byte) ((this.value >>> 8) & 255);
                buffer[this.myOffset + 4] = (byte) ((this.value >>> 0) & 255);
            }
        }
    }

    protected final class Font {
        public int CharsetLength;
        public int CharstringType = 2;
        public int FDArrayCount;
        public int[] FDArrayOffsets;
        public int FDArrayOffsize;
        public int[] FDSelect;
        public int FDSelectFormat;
        public int FDSelectLength;
        public int[] PrivateSubrsOffset;
        public int[][] PrivateSubrsOffsetsArray;
        public int[] SubrsOffsets;
        public int[] charset;
        public int charsetOffset = -1;
        public int charstringsOffset = -1;
        public int[] charstringsOffsets;
        public int encodingOffset = -1;
        public int fdarrayOffset = -1;
        public int[] fdprivateLengths;
        public int[] fdprivateOffsets;
        public int[] fdprivateSubrs;
        public int fdselectOffset = -1;
        public String fullName;
        public boolean isCID = false;
        public String name;
        public int nglyphs;
        public int nstrings;
        public int privateLength = -1;
        public int privateOffset = -1;
        public int privateSubrs = -1;

        protected Font() {
        }
    }

    protected static final class IndexBaseItem extends Item {
    }

    protected static final class IndexMarkerItem extends Item {
        private IndexBaseItem indexBase;
        private OffsetItem offItem;

        public IndexMarkerItem(OffsetItem offItem, IndexBaseItem indexBase) {
            this.offItem = offItem;
            this.indexBase = indexBase;
        }

        public void xref() {
            this.offItem.set((this.myOffset - this.indexBase.myOffset) + 1);
        }
    }

    protected static final class IndexOffsetItem extends OffsetItem {
        public final int size;

        public IndexOffsetItem(int size, int value) {
            this.size = size;
            this.value = value;
        }

        public IndexOffsetItem(int size) {
            this.size = size;
        }

        public void increment(int[] currentOffset) {
            super.increment(currentOffset);
            currentOffset[0] = currentOffset[0] + this.size;
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void emit(byte[] r4) {
            /*
            r3 = this;
            r0 = 0;
            r1 = r3.size;
            switch(r1) {
                case 1: goto L_0x0031;
                case 2: goto L_0x0023;
                case 3: goto L_0x0015;
                case 4: goto L_0x0007;
                default: goto L_0x0006;
            };
        L_0x0006:
            return;
        L_0x0007:
            r1 = r3.myOffset;
            r1 = r1 + r0;
            r2 = r3.value;
            r2 = r2 >>> 24;
            r2 = r2 & 255;
            r2 = (byte) r2;
            r4[r1] = r2;
            r0 = r0 + 1;
        L_0x0015:
            r1 = r3.myOffset;
            r1 = r1 + r0;
            r2 = r3.value;
            r2 = r2 >>> 16;
            r2 = r2 & 255;
            r2 = (byte) r2;
            r4[r1] = r2;
            r0 = r0 + 1;
        L_0x0023:
            r1 = r3.myOffset;
            r1 = r1 + r0;
            r2 = r3.value;
            r2 = r2 >>> 8;
            r2 = r2 & 255;
            r2 = (byte) r2;
            r4[r1] = r2;
            r0 = r0 + 1;
        L_0x0031:
            r1 = r3.myOffset;
            r1 = r1 + r0;
            r2 = r3.value;
            r2 = r2 >>> 0;
            r2 = r2 & 255;
            r2 = (byte) r2;
            r4[r1] = r2;
            r0 = r0 + 1;
            goto L_0x0006;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.itextpdf.text.pdf.CFFFont.IndexOffsetItem.emit(byte[]):void");
        }
    }

    protected static final class MarkerItem extends Item {
        OffsetItem f112p;

        public MarkerItem(OffsetItem pointerToMarker) {
            this.f112p = pointerToMarker;
        }

        public void xref() {
            this.f112p.set(this.myOffset);
        }
    }

    protected static final class RangeItem extends Item {
        private RandomAccessFileOrArray buf;
        public int length;
        public int offset;

        public RangeItem(RandomAccessFileOrArray buf, int offset, int length) {
            this.offset = offset;
            this.length = length;
            this.buf = buf;
        }

        public void increment(int[] currentOffset) {
            super.increment(currentOffset);
            currentOffset[0] = currentOffset[0] + this.length;
        }

        public void emit(byte[] buffer) {
            try {
                this.buf.seek((long) this.offset);
                for (int i = this.myOffset; i < this.myOffset + this.length; i++) {
                    buffer[i] = this.buf.readByte();
                }
            } catch (Exception e) {
                throw new ExceptionConverter(e);
            }
        }
    }

    protected static final class StringItem extends Item {
        public String f113s;

        public StringItem(String s) {
            this.f113s = s;
        }

        public void increment(int[] currentOffset) {
            super.increment(currentOffset);
            currentOffset[0] = currentOffset[0] + this.f113s.length();
        }

        public void emit(byte[] buffer) {
            for (int i = 0; i < this.f113s.length(); i++) {
                buffer[this.myOffset + i] = (byte) (this.f113s.charAt(i) & 255);
            }
        }
    }

    protected static final class SubrMarkerItem extends Item {
        private IndexBaseItem indexBase;
        private OffsetItem offItem;

        public SubrMarkerItem(OffsetItem offItem, IndexBaseItem indexBase) {
            this.offItem = offItem;
            this.indexBase = indexBase;
        }

        public void xref() {
            this.offItem.set(this.myOffset - this.indexBase.myOffset);
        }
    }

    protected static final class UInt16Item extends Item {
        public char value;

        public UInt16Item(char value) {
            this.value = value;
        }

        public void increment(int[] currentOffset) {
            super.increment(currentOffset);
            currentOffset[0] = currentOffset[0] + 2;
        }

        public void emit(byte[] buffer) {
            buffer[this.myOffset + 0] = (byte) ((this.value >>> 8) & 255);
            buffer[this.myOffset + 1] = (byte) ((this.value >>> 0) & 255);
        }
    }

    protected static final class UInt24Item extends Item {
        public int value;

        public UInt24Item(int value) {
            this.value = value;
        }

        public void increment(int[] currentOffset) {
            super.increment(currentOffset);
            currentOffset[0] = currentOffset[0] + 3;
        }

        public void emit(byte[] buffer) {
            buffer[this.myOffset + 0] = (byte) ((this.value >>> 16) & 255);
            buffer[this.myOffset + 1] = (byte) ((this.value >>> 8) & 255);
            buffer[this.myOffset + 2] = (byte) ((this.value >>> 0) & 255);
        }
    }

    protected static final class UInt32Item extends Item {
        public int value;

        public UInt32Item(int value) {
            this.value = value;
        }

        public void increment(int[] currentOffset) {
            super.increment(currentOffset);
            currentOffset[0] = currentOffset[0] + 4;
        }

        public void emit(byte[] buffer) {
            buffer[this.myOffset + 0] = (byte) ((this.value >>> 24) & 255);
            buffer[this.myOffset + 1] = (byte) ((this.value >>> 16) & 255);
            buffer[this.myOffset + 2] = (byte) ((this.value >>> 8) & 255);
            buffer[this.myOffset + 3] = (byte) ((this.value >>> 0) & 255);
        }
    }

    protected static final class UInt8Item extends Item {
        public char value;

        public UInt8Item(char value) {
            this.value = value;
        }

        public void increment(int[] currentOffset) {
            super.increment(currentOffset);
            currentOffset[0] = currentOffset[0] + 1;
        }

        public void emit(byte[] buffer) {
            buffer[this.myOffset + 0] = (byte) ((this.value >>> 0) & 255);
        }
    }

    public String getString(char sid) {
        if (sid < standardStrings.length) {
            return standardStrings[sid];
        }
        if (sid >= (standardStrings.length + this.stringOffsets.length) - 1) {
            return null;
        }
        int j = sid - standardStrings.length;
        int p = getPosition();
        seek(this.stringOffsets[j]);
        StringBuffer s = new StringBuffer();
        for (int k = this.stringOffsets[j]; k < this.stringOffsets[j + 1]; k++) {
            s.append(getCard8());
        }
        seek(p);
        return s.toString();
    }

    char getCard8() {
        try {
            return (char) (this.buf.readByte() & 255);
        } catch (Exception e) {
            throw new ExceptionConverter(e);
        }
    }

    char getCard16() {
        try {
            return this.buf.readChar();
        } catch (Exception e) {
            throw new ExceptionConverter(e);
        }
    }

    int getOffset(int offSize) {
        int offset = 0;
        for (int i = 0; i < offSize; i++) {
            offset = (offset * 256) + getCard8();
        }
        return offset;
    }

    void seek(int offset) {
        try {
            this.buf.seek((long) offset);
        } catch (Exception e) {
            throw new ExceptionConverter(e);
        }
    }

    short getShort() {
        try {
            return this.buf.readShort();
        } catch (Exception e) {
            throw new ExceptionConverter(e);
        }
    }

    int getInt() {
        try {
            return this.buf.readInt();
        } catch (Exception e) {
            throw new ExceptionConverter(e);
        }
    }

    int getPosition() {
        try {
            return (int) this.buf.getFilePointer();
        } catch (Exception e) {
            throw new ExceptionConverter(e);
        }
    }

    int[] getIndex(int nextIndexOffset) {
        seek(nextIndexOffset);
        int count = getCard16();
        int[] offsets = new int[(count + 1)];
        if (count == 0) {
            offsets[0] = -1;
            nextIndexOffset += 2;
        } else {
            int indexOffSize = getCard8();
            for (int j = 0; j <= count; j++) {
                offsets[j] = ((((nextIndexOffset + 2) + 1) + ((count + 1) * indexOffSize)) - 1) + getOffset(indexOffSize);
            }
        }
        return offsets;
    }

    protected void getDictItem() {
        for (int i = 0; i < this.arg_count; i++) {
            this.args[i] = null;
        }
        this.arg_count = 0;
        this.key = null;
        boolean gotKey = false;
        while (!gotKey) {
            char b0 = getCard8();
            if (b0 == '\u001d') {
                this.args[this.arg_count] = Integer.valueOf(getInt());
                this.arg_count++;
            } else if (b0 == '\u001c') {
                this.args[this.arg_count] = Integer.valueOf(getShort());
                this.arg_count++;
            } else if (b0 >= ' ' && b0 <= 'ö') {
                this.args[this.arg_count] = Integer.valueOf((byte) (b0 - 139));
                this.arg_count++;
            } else if (b0 >= '÷' && b0 <= 'ú') {
                this.args[this.arg_count] = Integer.valueOf((short) ((((b0 - 247) * 256) + getCard8()) + 108));
                this.arg_count++;
            } else if (b0 >= 'û' && b0 <= 'þ') {
                this.args[this.arg_count] = Integer.valueOf((short) ((((-(b0 - 251)) * 256) - getCard8()) - 108));
                this.arg_count++;
            } else if (b0 == '\u001e') {
                StringBuilder item = new StringBuilder("");
                boolean done = false;
                char buffer = '\u0000';
                byte avail = (byte) 0;
                int nibble = 0;
                while (!done) {
                    if (avail == (byte) 0) {
                        buffer = getCard8();
                        avail = (byte) 2;
                    }
                    if (avail == (byte) 1) {
                        nibble = buffer / 16;
                        avail = (byte) (avail - 1);
                    }
                    if (avail == (byte) 2) {
                        nibble = buffer % 16;
                        avail = (byte) (avail - 1);
                    }
                    switch (nibble) {
                        case 10:
                            item.append(".");
                            break;
                        case 11:
                            item.append("E");
                            break;
                        case 12:
                            item.append("E-");
                            break;
                        case 14:
                            item.append("-");
                            break;
                        case 15:
                            done = true;
                            break;
                        default:
                            if (nibble >= 0 && nibble <= 9) {
                                item.append(String.valueOf(nibble));
                                break;
                            }
                            item.append("<NIBBLE ERROR: ").append(nibble).append('>');
                            done = true;
                            break;
                    }
                }
                this.args[this.arg_count] = item.toString();
                this.arg_count++;
            } else if (b0 <= '\u0015') {
                gotKey = true;
                if (b0 != '\f') {
                    this.key = operatorNames[b0];
                } else {
                    this.key = operatorNames[getCard8() + 32];
                }
            }
        }
    }

    protected RangeItem getEntireIndexRange(int indexOffset) {
        seek(indexOffset);
        int count = getCard16();
        if (count == 0) {
            return new RangeItem(this.buf, indexOffset, 2);
        }
        int indexOffSize = getCard8();
        seek(((indexOffset + 2) + 1) + (count * indexOffSize));
        return new RangeItem(this.buf, indexOffset, (((count + 1) * indexOffSize) + 3) + (getOffset(indexOffSize) - 1));
    }

    public byte[] getCID(String fontName) {
        int j = 0;
        while (j < this.fonts.length) {
            if (fontName.equals(this.fonts[j].name)) {
                break;
            }
            j++;
        }
        if (j == this.fonts.length) {
            return null;
        }
        LinkedList<Item> l = new LinkedList();
        seek(0);
        int major = getCard8();
        int minor = getCard8();
        int hdrSize = getCard8();
        int offSize = getCard8();
        this.nextIndexOffset = hdrSize;
        l.addLast(new RangeItem(this.buf, 0, hdrSize));
        int nglyphs = -1;
        int nstrings = -1;
        if (!this.fonts[j].isCID) {
            seek(this.fonts[j].charstringsOffset);
            nglyphs = getCard16();
            seek(this.stringIndexOffset);
            nstrings = getCard16() + standardStrings.length;
        }
        l.addLast(new UInt16Item('\u0001'));
        l.addLast(new UInt8Item('\u0001'));
        l.addLast(new UInt8Item('\u0001'));
        l.addLast(new UInt8Item((char) (this.fonts[j].name.length() + 1)));
        l.addLast(new StringItem(this.fonts[j].name));
        l.addLast(new UInt16Item('\u0001'));
        l.addLast(new UInt8Item('\u0002'));
        l.addLast(new UInt16Item('\u0001'));
        OffsetItem topdictIndex1Ref = new IndexOffsetItem(2);
        l.addLast(topdictIndex1Ref);
        IndexBaseItem topdictBase = new IndexBaseItem();
        l.addLast(topdictBase);
        OffsetItem charsetRef = new DictOffsetItem();
        OffsetItem charstringsRef = new DictOffsetItem();
        OffsetItem fdarrayRef = new DictOffsetItem();
        OffsetItem fdselectRef = new DictOffsetItem();
        if (!this.fonts[j].isCID) {
            l.addLast(new DictNumberItem(nstrings));
            l.addLast(new DictNumberItem(nstrings + 1));
            l.addLast(new DictNumberItem(0));
            l.addLast(new UInt8Item('\f'));
            l.addLast(new UInt8Item('\u001e'));
            l.addLast(new DictNumberItem(nglyphs));
            l.addLast(new UInt8Item('\f'));
            l.addLast(new UInt8Item('\"'));
        }
        l.addLast(fdarrayRef);
        l.addLast(new UInt8Item('\f'));
        l.addLast(new UInt8Item('$'));
        l.addLast(fdselectRef);
        l.addLast(new UInt8Item('\f'));
        l.addLast(new UInt8Item('%'));
        l.addLast(charsetRef);
        l.addLast(new UInt8Item('\u000f'));
        l.addLast(charstringsRef);
        l.addLast(new UInt8Item('\u0011'));
        seek(this.topdictOffsets[j]);
        while (getPosition() < this.topdictOffsets[j + 1]) {
            int p1 = getPosition();
            getDictItem();
            int p2 = getPosition();
            if (!(this.key == "Encoding" || this.key == "Private" || this.key == "FDSelect" || this.key == "FDArray" || this.key == "charset" || this.key == "CharStrings")) {
                l.add(new RangeItem(this.buf, p1, p2 - p1));
            }
        }
        l.addLast(new IndexMarkerItem(topdictIndex1Ref, topdictBase));
        if (this.fonts[j].isCID) {
            l.addLast(getEntireIndexRange(this.stringIndexOffset));
        } else {
            byte stringsIndexOffSize;
            String fdFontName = this.fonts[j].name + "-OneRange";
            if (fdFontName.length() > 127) {
                fdFontName = fdFontName.substring(0, 127);
            }
            String extraStrings = "AdobeIdentity" + fdFontName;
            int origStringsLen = this.stringOffsets[this.stringOffsets.length - 1] - this.stringOffsets[0];
            int stringsBaseOffset = this.stringOffsets[0] - 1;
            if (extraStrings.length() + origStringsLen <= 255) {
                stringsIndexOffSize = (byte) 1;
            } else if (extraStrings.length() + origStringsLen <= 65535) {
                stringsIndexOffSize = (byte) 2;
            } else if (extraStrings.length() + origStringsLen <= 16777215) {
                stringsIndexOffSize = (byte) 3;
            } else {
                stringsIndexOffSize = (byte) 4;
            }
            l.addLast(new UInt16Item((char) ((this.stringOffsets.length - 1) + 3)));
            l.addLast(new UInt8Item((char) stringsIndexOffSize));
            for (int stringOffset : this.stringOffsets) {
                l.addLast(new IndexOffsetItem(stringsIndexOffSize, stringOffset - stringsBaseOffset));
            }
            int currentStringsOffset = (this.stringOffsets[this.stringOffsets.length - 1] - stringsBaseOffset) + "Adobe".length();
            l.addLast(new IndexOffsetItem(stringsIndexOffSize, currentStringsOffset));
            currentStringsOffset += "Identity".length();
            l.addLast(new IndexOffsetItem(stringsIndexOffSize, currentStringsOffset));
            l.addLast(new IndexOffsetItem(stringsIndexOffSize, currentStringsOffset + fdFontName.length()));
            l.addLast(new RangeItem(this.buf, this.stringOffsets[0], origStringsLen));
            l.addLast(new StringItem(extraStrings));
        }
        l.addLast(getEntireIndexRange(this.gsubrIndexOffset));
        if (!this.fonts[j].isCID) {
            l.addLast(new MarkerItem(fdselectRef));
            l.addLast(new UInt8Item('\u0003'));
            l.addLast(new UInt16Item('\u0001'));
            l.addLast(new UInt16Item('\u0000'));
            l.addLast(new UInt8Item('\u0000'));
            l.addLast(new UInt16Item((char) nglyphs));
            l.addLast(new MarkerItem(charsetRef));
            l.addLast(new UInt8Item('\u0002'));
            l.addLast(new UInt16Item('\u0001'));
            l.addLast(new UInt16Item((char) (nglyphs - 1)));
            l.addLast(new MarkerItem(fdarrayRef));
            l.addLast(new UInt16Item('\u0001'));
            l.addLast(new UInt8Item('\u0001'));
            l.addLast(new UInt8Item('\u0001'));
            OffsetItem indexOffsetItem = new IndexOffsetItem(1);
            l.addLast(indexOffsetItem);
            IndexBaseItem privateBase = new IndexBaseItem();
            l.addLast(privateBase);
            l.addLast(new DictNumberItem(this.fonts[j].privateLength));
            OffsetItem privateRef = new DictOffsetItem();
            l.addLast(privateRef);
            l.addLast(new UInt8Item('\u0012'));
            l.addLast(new IndexMarkerItem(indexOffsetItem, privateBase));
            l.addLast(new MarkerItem(privateRef));
            l.addLast(new RangeItem(this.buf, this.fonts[j].privateOffset, this.fonts[j].privateLength));
            if (this.fonts[j].privateSubrs >= 0) {
                l.addLast(getEntireIndexRange(this.fonts[j].privateSubrs));
            }
        }
        l.addLast(new MarkerItem(charstringsRef));
        l.addLast(getEntireIndexRange(this.fonts[j].charstringsOffset));
        int[] currentOffset = new int[]{0};
        Iterator<Item> listIter = l.iterator();
        while (listIter.hasNext()) {
            ((Item) listIter.next()).increment(currentOffset);
        }
        listIter = l.iterator();
        while (listIter.hasNext()) {
            ((Item) listIter.next()).xref();
        }
        byte[] b = new byte[currentOffset[0]];
        listIter = l.iterator();
        while (listIter.hasNext()) {
            ((Item) listIter.next()).emit(b);
        }
        return b;
    }

    public boolean isCID(String fontName) {
        for (int j = 0; j < this.fonts.length; j++) {
            if (fontName.equals(this.fonts[j].name)) {
                return this.fonts[j].isCID;
            }
        }
        return false;
    }

    public boolean exists(String fontName) {
        for (Font font : this.fonts) {
            if (fontName.equals(font.name)) {
                return true;
            }
        }
        return false;
    }

    public String[] getNames() {
        String[] names = new String[this.fonts.length];
        for (int i = 0; i < this.fonts.length; i++) {
            names[i] = this.fonts[i].name;
        }
        return names;
    }

    public CFFFont(RandomAccessFileOrArray inputbuffer) {
        int j;
        int k;
        this.buf = inputbuffer;
        seek(0);
        int major = getCard8();
        int minor = getCard8();
        int hdrSize = getCard8();
        this.offSize = getCard8();
        this.nameIndexOffset = hdrSize;
        this.nameOffsets = getIndex(this.nameIndexOffset);
        this.topdictIndexOffset = this.nameOffsets[this.nameOffsets.length - 1];
        this.topdictOffsets = getIndex(this.topdictIndexOffset);
        this.stringIndexOffset = this.topdictOffsets[this.topdictOffsets.length - 1];
        this.stringOffsets = getIndex(this.stringIndexOffset);
        this.gsubrIndexOffset = this.stringOffsets[this.stringOffsets.length - 1];
        this.gsubrOffsets = getIndex(this.gsubrIndexOffset);
        this.fonts = new Font[(this.nameOffsets.length - 1)];
        for (j = 0; j < this.nameOffsets.length - 1; j++) {
            this.fonts[j] = new Font();
            seek(this.nameOffsets[j]);
            this.fonts[j].name = "";
            for (k = this.nameOffsets[j]; k < this.nameOffsets[j + 1]; k++) {
                StringBuilder stringBuilder = new StringBuilder();
                Font font = this.fonts[j];
                font.name = stringBuilder.append(font.name).append(getCard8()).toString();
            }
        }
        for (j = 0; j < this.topdictOffsets.length - 1; j++) {
            seek(this.topdictOffsets[j]);
            while (getPosition() < this.topdictOffsets[j + 1]) {
                getDictItem();
                if (this.key == "FullName") {
                    this.fonts[j].fullName = getString((char) ((Integer) this.args[0]).intValue());
                } else if (this.key == "ROS") {
                    this.fonts[j].isCID = true;
                } else if (this.key == "Private") {
                    this.fonts[j].privateLength = ((Integer) this.args[0]).intValue();
                    this.fonts[j].privateOffset = ((Integer) this.args[1]).intValue();
                } else if (this.key == "charset") {
                    this.fonts[j].charsetOffset = ((Integer) this.args[0]).intValue();
                } else if (this.key == "CharStrings") {
                    this.fonts[j].charstringsOffset = ((Integer) this.args[0]).intValue();
                    int p = getPosition();
                    this.fonts[j].charstringsOffsets = getIndex(this.fonts[j].charstringsOffset);
                    seek(p);
                } else if (this.key == "FDArray") {
                    this.fonts[j].fdarrayOffset = ((Integer) this.args[0]).intValue();
                } else if (this.key == "FDSelect") {
                    this.fonts[j].fdselectOffset = ((Integer) this.args[0]).intValue();
                } else if (this.key == "CharstringType") {
                    this.fonts[j].CharstringType = ((Integer) this.args[0]).intValue();
                }
            }
            if (this.fonts[j].privateOffset >= 0) {
                seek(this.fonts[j].privateOffset);
                while (getPosition() < this.fonts[j].privateOffset + this.fonts[j].privateLength) {
                    getDictItem();
                    if (this.key == "Subrs") {
                        this.fonts[j].privateSubrs = ((Integer) this.args[0]).intValue() + this.fonts[j].privateOffset;
                    }
                }
            }
            if (this.fonts[j].fdarrayOffset >= 0) {
                int[] fdarrayOffsets = getIndex(this.fonts[j].fdarrayOffset);
                this.fonts[j].fdprivateOffsets = new int[(fdarrayOffsets.length - 1)];
                this.fonts[j].fdprivateLengths = new int[(fdarrayOffsets.length - 1)];
                for (k = 0; k < fdarrayOffsets.length - 1; k++) {
                    seek(fdarrayOffsets[k]);
                    while (getPosition() < fdarrayOffsets[k + 1]) {
                        getDictItem();
                        if (this.key == "Private") {
                            this.fonts[j].fdprivateLengths[k] = ((Integer) this.args[0]).intValue();
                            this.fonts[j].fdprivateOffsets[k] = ((Integer) this.args[1]).intValue();
                        }
                    }
                }
            }
        }
    }

    void ReadEncoding(int nextIndexOffset) {
        seek(nextIndexOffset);
        int format = getCard8();
    }
}
