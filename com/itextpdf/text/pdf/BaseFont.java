package com.itextpdf.text.pdf;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.error_messages.MessageLocalization;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;
import org.apache.poi.hssf.record.BOFRecord;

public abstract class BaseFont {
    public static final int ASCENT = 1;
    public static final int AWT_ASCENT = 9;
    public static final int AWT_DESCENT = 10;
    public static final int AWT_LEADING = 11;
    public static final int AWT_MAXADVANCE = 12;
    public static final int BBOXLLX = 5;
    public static final int BBOXLLY = 6;
    public static final int BBOXURX = 7;
    public static final int BBOXURY = 8;
    protected static final HashMap<String, PdfName> BuiltinFonts14 = new HashMap();
    public static final boolean CACHED = true;
    public static final int CAPHEIGHT = 2;
    public static final int[] CHAR_RANGE_ARABIC = new int[]{0, 127, BOFRecord.VERSION, 1663, 8352, 8399, 64336, 64511, 65136, 65279};
    public static final int[] CHAR_RANGE_CYRILLIC = new int[]{0, 127, 1024, 1327, 8192, 8303, 8352, 8399};
    public static final int[] CHAR_RANGE_HEBREW = new int[]{0, 127, 1424, 1535, 8352, 8399, 64285, 64335};
    public static final int[] CHAR_RANGE_LATIN = new int[]{0, 383, 8192, 8303, 8352, 8399, 64256, 64262};
    public static final char CID_NEWLINE = '翿';
    public static final String COURIER = "Courier";
    public static final String COURIER_BOLD = "Courier-Bold";
    public static final String COURIER_BOLDOBLIQUE = "Courier-BoldOblique";
    public static final String COURIER_OBLIQUE = "Courier-Oblique";
    public static final String CP1250 = "Cp1250";
    public static final String CP1252 = "Cp1252";
    public static final String CP1257 = "Cp1257";
    public static final int DESCENT = 3;
    public static final boolean EMBEDDED = true;
    public static final int FONT_TYPE_CJK = 2;
    public static final int FONT_TYPE_DOCUMENT = 4;
    public static final int FONT_TYPE_T1 = 0;
    public static final int FONT_TYPE_T3 = 5;
    public static final int FONT_TYPE_TT = 1;
    public static final int FONT_TYPE_TTUNI = 3;
    public static final int FONT_WEIGHT = 23;
    public static final String HELVETICA = "Helvetica";
    public static final String HELVETICA_BOLD = "Helvetica-Bold";
    public static final String HELVETICA_BOLDOBLIQUE = "Helvetica-BoldOblique";
    public static final String HELVETICA_OBLIQUE = "Helvetica-Oblique";
    public static final String IDENTITY_H = "Identity-H";
    public static final String IDENTITY_V = "Identity-V";
    public static final int ITALICANGLE = 4;
    public static final String MACROMAN = "MacRoman";
    public static final boolean NOT_CACHED = false;
    public static final boolean NOT_EMBEDDED = false;
    public static final char PARAGRAPH_SEPARATOR = ' ';
    public static final String RESOURCE_PATH = "com/itextpdf/text/pdf/fonts/";
    public static final int STRIKETHROUGH_POSITION = 15;
    public static final int STRIKETHROUGH_THICKNESS = 16;
    public static final int SUBSCRIPT_OFFSET = 18;
    public static final int SUBSCRIPT_SIZE = 17;
    public static final int SUPERSCRIPT_OFFSET = 20;
    public static final int SUPERSCRIPT_SIZE = 19;
    public static final String SYMBOL = "Symbol";
    public static final String TIMES_BOLD = "Times-Bold";
    public static final String TIMES_BOLDITALIC = "Times-BoldItalic";
    public static final String TIMES_ITALIC = "Times-Italic";
    public static final String TIMES_ROMAN = "Times-Roman";
    public static final int UNDERLINE_POSITION = 13;
    public static final int UNDERLINE_THICKNESS = 14;
    public static final int WEIGHT_CLASS = 21;
    public static final int WIDTH_CLASS = 22;
    public static final String WINANSI = "Cp1252";
    public static final String ZAPFDINGBATS = "ZapfDingbats";
    protected static HashMap<String, BaseFont> fontCache = new HashMap();
    public static final String notdef = ".notdef";
    protected int[][] charBBoxes = new int[256][];
    protected int compressionLevel = -1;
    protected String[] differences = new String[256];
    protected boolean directTextToByte = false;
    protected boolean embedded;
    protected String encoding;
    protected boolean fastWinansi = false;
    protected boolean fontSpecific = true;
    int fontType;
    protected boolean forceWidthsOutput = false;
    protected IntHashtable specialMap;
    protected boolean subset = true;
    protected ArrayList<int[]> subsetRanges;
    protected char[] unicodeDifferences = new char[256];
    protected boolean vertical = false;
    protected int[] widths = new int[256];

    static class StreamFont extends PdfStream {
        public StreamFont(byte[] contents, int[] lengths, int compressionLevel) throws DocumentException {
            try {
                this.bytes = contents;
                put(PdfName.LENGTH, new PdfNumber(this.bytes.length));
                for (int k = 0; k < lengths.length; k++) {
                    put(new PdfName("Length" + (k + 1)), new PdfNumber(lengths[k]));
                }
                flateCompress(compressionLevel);
            } catch (Exception e) {
                throw new DocumentException(e);
            }
        }

        public StreamFont(byte[] contents, String subType, int compressionLevel) throws DocumentException {
            try {
                this.bytes = contents;
                put(PdfName.LENGTH, new PdfNumber(this.bytes.length));
                if (subType != null) {
                    put(PdfName.SUBTYPE, new PdfName(subType));
                }
                flateCompress(compressionLevel);
            } catch (Exception e) {
                throw new DocumentException(e);
            }
        }
    }

    public abstract String[][] getAllNameEntries();

    public abstract String[][] getFamilyFontName();

    public abstract float getFontDescriptor(int i, float f);

    public abstract String[][] getFullFontName();

    abstract PdfStream getFullFontStream() throws IOException, DocumentException;

    public abstract int getKerning(int i, int i2);

    public abstract String getPostscriptFontName();

    protected abstract int[] getRawCharBBox(int i, String str);

    abstract int getRawWidth(int i, String str);

    public abstract boolean hasKernPairs();

    public abstract boolean setKerning(int i, int i2, int i3);

    public abstract void setPostscriptFontName(String str);

    abstract void writeFont(PdfWriter pdfWriter, PdfIndirectReference pdfIndirectReference, Object[] objArr) throws DocumentException, IOException;

    static {
        BuiltinFonts14.put("Courier", PdfName.COURIER);
        BuiltinFonts14.put("Courier-Bold", PdfName.COURIER_BOLD);
        BuiltinFonts14.put("Courier-BoldOblique", PdfName.COURIER_BOLDOBLIQUE);
        BuiltinFonts14.put("Courier-Oblique", PdfName.COURIER_OBLIQUE);
        BuiltinFonts14.put("Helvetica", PdfName.HELVETICA);
        BuiltinFonts14.put("Helvetica-Bold", PdfName.HELVETICA_BOLD);
        BuiltinFonts14.put("Helvetica-BoldOblique", PdfName.HELVETICA_BOLDOBLIQUE);
        BuiltinFonts14.put("Helvetica-Oblique", PdfName.HELVETICA_OBLIQUE);
        BuiltinFonts14.put("Symbol", PdfName.SYMBOL);
        BuiltinFonts14.put("Times-Roman", PdfName.TIMES_ROMAN);
        BuiltinFonts14.put("Times-Bold", PdfName.TIMES_BOLD);
        BuiltinFonts14.put("Times-BoldItalic", PdfName.TIMES_BOLDITALIC);
        BuiltinFonts14.put("Times-Italic", PdfName.TIMES_ITALIC);
        BuiltinFonts14.put("ZapfDingbats", PdfName.ZAPFDINGBATS);
    }

    protected BaseFont() {
    }

    public static BaseFont createFont() throws DocumentException, IOException {
        return createFont("Helvetica", "Cp1252", false);
    }

    public static BaseFont createFont(String name, String encoding, boolean embedded) throws DocumentException, IOException {
        return createFont(name, encoding, embedded, true, null, null, false);
    }

    public static BaseFont createFont(String name, String encoding, boolean embedded, boolean forceRead) throws DocumentException, IOException {
        return createFont(name, encoding, embedded, true, null, null, forceRead);
    }

    public static BaseFont createFont(String name, String encoding, boolean embedded, boolean cached, byte[] ttfAfm, byte[] pfb) throws DocumentException, IOException {
        return createFont(name, encoding, embedded, cached, ttfAfm, pfb, false);
    }

    public static BaseFont createFont(String name, String encoding, boolean embedded, boolean cached, byte[] ttfAfm, byte[] pfb, boolean noThrow) throws DocumentException, IOException {
        return createFont(name, encoding, embedded, cached, ttfAfm, pfb, noThrow, false);
    }

    public static BaseFont createFont(String name, String encoding, boolean embedded, boolean cached, byte[] ttfAfm, byte[] pfb, boolean noThrow, boolean forceRead) throws DocumentException, IOException {
        BaseFont fontFound;
        BaseFont fontBuilt;
        String nameBase = getBaseName(name);
        encoding = normalizeEncoding(encoding);
        boolean isBuiltinFonts14 = BuiltinFonts14.containsKey(name);
        boolean isCJKFont = isBuiltinFonts14 ? false : CJKFont.isCJKFont(nameBase, encoding);
        if (isBuiltinFonts14 || isCJKFont) {
            embedded = false;
        } else if (encoding.equals(IDENTITY_H) || encoding.equals(IDENTITY_V)) {
            embedded = true;
        }
        String key = name + "\n" + encoding + "\n" + embedded;
        if (cached) {
            synchronized (fontCache) {
                fontFound = (BaseFont) fontCache.get(key);
            }
            if (fontFound != null) {
                return fontFound;
            }
        }
        if (isBuiltinFonts14 || name.toLowerCase().endsWith(".afm") || name.toLowerCase().endsWith(".pfm")) {
            fontBuilt = new Type1Font(name, encoding, embedded, ttfAfm, pfb, forceRead);
            fontBuilt.fastWinansi = encoding.equals("Cp1252");
        } else if (nameBase.toLowerCase().endsWith(".ttf") || nameBase.toLowerCase().endsWith(".otf") || nameBase.toLowerCase().indexOf(".ttc,") > 0) {
            if (encoding.equals(IDENTITY_H) || encoding.equals(IDENTITY_V)) {
                fontBuilt = new TrueTypeFontUnicode(name, encoding, embedded, ttfAfm, forceRead);
            } else {
                fontBuilt = new TrueTypeFont(name, encoding, embedded, ttfAfm, false, forceRead);
                fontBuilt.fastWinansi = encoding.equals("Cp1252");
            }
        } else if (isCJKFont) {
            fontBuilt = new CJKFont(name, encoding, embedded);
        } else if (noThrow) {
            return null;
        } else {
            throw new DocumentException(MessageLocalization.getComposedMessage("font.1.with.2.is.not.recognized", name, encoding));
        }
        if (cached) {
            synchronized (fontCache) {
                fontFound = (BaseFont) fontCache.get(key);
                if (fontFound != null) {
                    return fontFound;
                }
                fontCache.put(key, fontBuilt);
            }
        }
        return fontBuilt;
    }

    public static BaseFont createFont(PRIndirectReference fontRef) {
        return new DocumentFont(fontRef);
    }

    public boolean isVertical() {
        return this.vertical;
    }

    protected static String getBaseName(String name) {
        if (name.endsWith(",Bold")) {
            return name.substring(0, name.length() - 5);
        }
        if (name.endsWith(",Italic")) {
            return name.substring(0, name.length() - 7);
        }
        if (name.endsWith(",BoldItalic")) {
            return name.substring(0, name.length() - 11);
        }
        return name;
    }

    protected static String normalizeEncoding(String enc) {
        if (enc.equals("winansi") || enc.equals("")) {
            return "Cp1252";
        }
        if (enc.equals("macroman")) {
            return MACROMAN;
        }
        return enc;
    }

    protected void createEncoding() {
        String name;
        int k;
        if (this.encoding.startsWith("#")) {
            this.specialMap = new IntHashtable();
            StringTokenizer tok = new StringTokenizer(this.encoding.substring(1), " ,\t\n\r\f");
            if (tok.nextToken().equals("full")) {
                while (tok.hasMoreTokens()) {
                    int orderK;
                    String order = tok.nextToken();
                    name = tok.nextToken();
                    char uni = (char) Integer.parseInt(tok.nextToken(), 16);
                    if (order.startsWith("'")) {
                        orderK = order.charAt(1);
                    } else {
                        orderK = Integer.parseInt(order);
                    }
                    orderK %= 256;
                    this.specialMap.put(uni, orderK);
                    this.differences[orderK] = name;
                    this.unicodeDifferences[orderK] = uni;
                    this.widths[orderK] = getRawWidth(uni, name);
                    this.charBBoxes[orderK] = getRawCharBBox(uni, name);
                }
            } else {
                k = 0;
                if (tok.hasMoreTokens()) {
                    k = Integer.parseInt(tok.nextToken());
                }
                while (tok.hasMoreTokens() && k < 256) {
                    int uni2 = Integer.parseInt(tok.nextToken(), 16) % 65536;
                    name = GlyphList.unicodeToName(uni2);
                    if (name != null) {
                        this.specialMap.put(uni2, k);
                        this.differences[k] = name;
                        this.unicodeDifferences[k] = (char) uni2;
                        this.widths[k] = getRawWidth(uni2, name);
                        this.charBBoxes[k] = getRawCharBBox(uni2, name);
                        k++;
                    }
                }
            }
            for (k = 0; k < 256; k++) {
                if (this.differences[k] == null) {
                    this.differences[k] = notdef;
                }
            }
        } else if (this.fontSpecific) {
            for (k = 0; k < 256; k++) {
                this.widths[k] = getRawWidth(k, null);
                this.charBBoxes[k] = getRawCharBBox(k, null);
            }
        } else {
            byte[] b = new byte[1];
            for (k = 0; k < 256; k++) {
                char c;
                b[0] = (byte) k;
                String s = PdfEncodings.convertToString(b, this.encoding);
                if (s.length() > 0) {
                    c = s.charAt(0);
                } else {
                    c = '?';
                }
                name = GlyphList.unicodeToName(c);
                if (name == null) {
                    name = notdef;
                }
                this.differences[k] = name;
                this.unicodeDifferences[k] = c;
                this.widths[k] = getRawWidth(c, name);
                this.charBBoxes[k] = getRawCharBBox(c, name);
            }
        }
    }

    public int getWidth(int char1) {
        if (!this.fastWinansi) {
            int total = 0;
            for (byte b : convertToBytes(char1)) {
                total += this.widths[b & 255];
            }
            return total;
        } else if (char1 < 128 || (char1 >= 160 && char1 <= 255)) {
            return this.widths[char1];
        } else {
            return this.widths[PdfEncodings.winansi.get(char1)];
        }
    }

    public int getWidth(String text) {
        int total = 0;
        if (this.fastWinansi) {
            int len = text.length();
            int k;
            for (k = 0; k < len; k++) {
                char char1 = text.charAt(k);
                if (char1 < '' || (char1 >= ' ' && char1 <= 'ÿ')) {
                    total += this.widths[char1];
                } else {
                    total += this.widths[PdfEncodings.winansi.get(char1)];
                }
            }
            return total;
        }
        for (byte b : convertToBytes(text)) {
            total += this.widths[b & 255];
        }
        return total;
    }

    public int getDescent(String text) {
        int min = 0;
        char[] chars = text.toCharArray();
        for (char charBBox : chars) {
            int[] bbox = getCharBBox(charBBox);
            if (bbox != null && bbox[1] < min) {
                min = bbox[1];
            }
        }
        return min;
    }

    public int getAscent(String text) {
        int max = 0;
        char[] chars = text.toCharArray();
        for (char charBBox : chars) {
            int[] bbox = getCharBBox(charBBox);
            if (bbox != null && bbox[3] > max) {
                max = bbox[3];
            }
        }
        return max;
    }

    public float getDescentPoint(String text, float fontSize) {
        return (((float) getDescent(text)) * 0.001f) * fontSize;
    }

    public float getAscentPoint(String text, float fontSize) {
        return (((float) getAscent(text)) * 0.001f) * fontSize;
    }

    public float getWidthPointKerned(String text, float fontSize) {
        float size = (((float) getWidth(text)) * 0.001f) * fontSize;
        if (!hasKernPairs()) {
            return size;
        }
        int len = text.length() - 1;
        int kern = 0;
        char[] c = text.toCharArray();
        for (int k = 0; k < len; k++) {
            kern += getKerning(c[k], c[k + 1]);
        }
        return size + ((((float) kern) * 0.001f) * fontSize);
    }

    public float getWidthPoint(String text, float fontSize) {
        return (((float) getWidth(text)) * 0.001f) * fontSize;
    }

    public float getWidthPoint(int char1, float fontSize) {
        return (((float) getWidth(char1)) * 0.001f) * fontSize;
    }

    public byte[] convertToBytes(String text) {
        if (this.directTextToByte) {
            return PdfEncodings.convertToBytes(text, null);
        }
        if (this.specialMap == null) {
            return PdfEncodings.convertToBytes(text, this.encoding);
        }
        byte[] b = new byte[text.length()];
        int length = text.length();
        int k = 0;
        int ptr = 0;
        while (k < length) {
            int ptr2;
            char c = text.charAt(k);
            if (this.specialMap.containsKey(c)) {
                ptr2 = ptr + 1;
                b[ptr] = (byte) this.specialMap.get(c);
            } else {
                ptr2 = ptr;
            }
            k++;
            ptr = ptr2;
        }
        if (ptr >= length) {
            return b;
        }
        byte[] b2 = new byte[ptr];
        System.arraycopy(b, 0, b2, 0, ptr);
        return b2;
    }

    byte[] convertToBytes(int char1) {
        if (this.directTextToByte) {
            return PdfEncodings.convertToBytes((char) char1, null);
        }
        if (this.specialMap == null) {
            return PdfEncodings.convertToBytes((char) char1, this.encoding);
        }
        if (!this.specialMap.containsKey(char1)) {
            return new byte[0];
        }
        return new byte[]{(byte) this.specialMap.get(char1)};
    }

    public String getEncoding() {
        return this.encoding;
    }

    public void setFontDescriptor(int key, float value) {
    }

    public int getFontType() {
        return this.fontType;
    }

    public boolean isEmbedded() {
        return this.embedded;
    }

    public boolean isFontSpecific() {
        return this.fontSpecific;
    }

    public static String createSubsetPrefix() {
        StringBuilder s = new StringBuilder("");
        for (int k = 0; k < 6; k++) {
            s.append((char) ((int) ((Math.random() * 26.0d) + 65.0d)));
        }
        return s + "+";
    }

    char getUnicodeDifferences(int index) {
        return this.unicodeDifferences[index];
    }

    public static String[][] getFullFontName(String name, String encoding, byte[] ttfAfm) throws DocumentException, IOException {
        BaseFont fontBuilt;
        String nameBase = getBaseName(name);
        if (nameBase.toLowerCase().endsWith(".ttf") || nameBase.toLowerCase().endsWith(".otf") || nameBase.toLowerCase().indexOf(".ttc,") > 0) {
            fontBuilt = new TrueTypeFont(name, "Cp1252", false, ttfAfm, true, false);
        } else {
            fontBuilt = createFont(name, encoding, false, false, ttfAfm, null);
        }
        return fontBuilt.getFullFontName();
    }

    public static Object[] getAllFontNames(String name, String encoding, byte[] ttfAfm) throws DocumentException, IOException {
        BaseFont fontBuilt;
        String nameBase = getBaseName(name);
        if (nameBase.toLowerCase().endsWith(".ttf") || nameBase.toLowerCase().endsWith(".otf") || nameBase.toLowerCase().indexOf(".ttc,") > 0) {
            fontBuilt = new TrueTypeFont(name, "Cp1252", false, ttfAfm, true, false);
        } else {
            fontBuilt = createFont(name, encoding, false, false, ttfAfm, null);
        }
        return new Object[]{fontBuilt.getPostscriptFontName(), fontBuilt.getFamilyFontName(), fontBuilt.getFullFontName()};
    }

    public static String[][] getAllNameEntries(String name, String encoding, byte[] ttfAfm) throws DocumentException, IOException {
        BaseFont fontBuilt;
        String nameBase = getBaseName(name);
        if (nameBase.toLowerCase().endsWith(".ttf") || nameBase.toLowerCase().endsWith(".otf") || nameBase.toLowerCase().indexOf(".ttc,") > 0) {
            fontBuilt = new TrueTypeFont(name, "Cp1252", false, ttfAfm, true, false);
        } else {
            fontBuilt = createFont(name, encoding, false, false, ttfAfm, null);
        }
        return fontBuilt.getAllNameEntries();
    }

    public String[] getCodePagesSupported() {
        return new String[0];
    }

    public static String[] enumerateTTCNames(String ttcFile) throws DocumentException, IOException {
        return new EnumerateTTC(ttcFile).getNames();
    }

    public static String[] enumerateTTCNames(byte[] ttcArray) throws DocumentException, IOException {
        return new EnumerateTTC(ttcArray).getNames();
    }

    public int[] getWidths() {
        return this.widths;
    }

    public String[] getDifferences() {
        return this.differences;
    }

    public char[] getUnicodeDifferences() {
        return this.unicodeDifferences;
    }

    public boolean isForceWidthsOutput() {
        return this.forceWidthsOutput;
    }

    public void setForceWidthsOutput(boolean forceWidthsOutput) {
        this.forceWidthsOutput = forceWidthsOutput;
    }

    public boolean isDirectTextToByte() {
        return this.directTextToByte;
    }

    public void setDirectTextToByte(boolean directTextToByte) {
        this.directTextToByte = directTextToByte;
    }

    public boolean isSubset() {
        return this.subset;
    }

    public void setSubset(boolean subset) {
        this.subset = subset;
    }

    public int getUnicodeEquivalent(int c) {
        return c;
    }

    public int getCidCode(int c) {
        return c;
    }

    public boolean charExists(int c) {
        return convertToBytes(c).length > 0;
    }

    public boolean setCharAdvance(int c, int advance) {
        byte[] b = convertToBytes(c);
        if (b.length == 0) {
            return false;
        }
        this.widths[b[0] & 255] = advance;
        return true;
    }

    private static void addFont(PRIndirectReference fontRef, IntHashtable hits, ArrayList<Object[]> fonts) {
        PdfObject obj = PdfReader.getPdfObject((PdfObject) fontRef);
        if (obj != null && obj.isDictionary()) {
            PdfDictionary font = (PdfDictionary) obj;
            PdfName subtype = font.getAsName(PdfName.SUBTYPE);
            if (PdfName.TYPE1.equals(subtype) || PdfName.TRUETYPE.equals(subtype) || PdfName.TYPE0.equals(subtype)) {
                PdfName name = font.getAsName(PdfName.BASEFONT);
                fonts.add(new Object[]{PdfName.decodeName(name.toString()), fontRef});
                hits.put(fontRef.getNumber(), 1);
            }
        }
    }

    private static void recourseFonts(PdfDictionary page, IntHashtable hits, ArrayList<Object[]> fonts, int level) {
        level++;
        if (level <= 50 && page != null) {
            PdfDictionary resources = page.getAsDict(PdfName.RESOURCES);
            if (resources != null) {
                PdfDictionary font = resources.getAsDict(PdfName.FONT);
                if (font != null) {
                    for (PdfName key : font.getKeys()) {
                        PdfObject ft = font.get(key);
                        if (!(ft == null || !ft.isIndirect() || hits.containsKey(((PRIndirectReference) ft).getNumber()))) {
                            addFont((PRIndirectReference) ft, hits, fonts);
                        }
                    }
                }
                PdfDictionary xobj = resources.getAsDict(PdfName.XOBJECT);
                if (xobj != null) {
                    for (PdfName key2 : xobj.getKeys()) {
                        PdfObject po = xobj.getDirectObject(key2);
                        if (po instanceof PdfDictionary) {
                            recourseFonts((PdfDictionary) po, hits, fonts, level);
                        }
                    }
                }
            }
        }
    }

    public static ArrayList<Object[]> getDocumentFonts(PdfReader reader) {
        IntHashtable hits = new IntHashtable();
        ArrayList<Object[]> fonts = new ArrayList();
        int npages = reader.getNumberOfPages();
        for (int k = 1; k <= npages; k++) {
            recourseFonts(reader.getPageN(k), hits, fonts, 1);
        }
        return fonts;
    }

    public static ArrayList<Object[]> getDocumentFonts(PdfReader reader, int page) {
        IntHashtable hits = new IntHashtable();
        ArrayList<Object[]> fonts = new ArrayList();
        recourseFonts(reader.getPageN(page), hits, fonts, 1);
        return fonts;
    }

    public int[] getCharBBox(int c) {
        byte[] b = convertToBytes(c);
        if (b.length == 0) {
            return null;
        }
        return this.charBBoxes[b[0] & 255];
    }

    public void correctArabicAdvance() {
        char c;
        for (c = 'ً'; c <= '٘'; c = (char) (c + 1)) {
            setCharAdvance(c, 0);
        }
        setCharAdvance(1648, 0);
        for (c = 'ۖ'; c <= 'ۜ'; c = (char) (c + 1)) {
            setCharAdvance(c, 0);
        }
        for (c = '۟'; c <= 'ۤ'; c = (char) (c + 1)) {
            setCharAdvance(c, 0);
        }
        for (c = 'ۧ'; c <= 'ۨ'; c = (char) (c + 1)) {
            setCharAdvance(c, 0);
        }
        for (c = '۪'; c <= 'ۭ'; c = (char) (c + 1)) {
            setCharAdvance(c, 0);
        }
    }

    public void addSubsetRange(int[] range) {
        if (this.subsetRanges == null) {
            this.subsetRanges = new ArrayList();
        }
        this.subsetRanges.add(range);
    }

    public int getCompressionLevel() {
        return this.compressionLevel;
    }

    public void setCompressionLevel(int compressionLevel) {
        if (compressionLevel < 0 || compressionLevel > 9) {
            this.compressionLevel = -1;
        } else {
            this.compressionLevel = compressionLevel;
        }
    }
}
