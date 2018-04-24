package com.itextpdf.text.pdf;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.Utilities;
import com.itextpdf.text.io.RandomAccessSourceFactory;
import com.itextpdf.text.pdf.fonts.cmaps.CMapParserEx;
import com.itextpdf.text.pdf.fonts.cmaps.CMapToUnicode;
import com.itextpdf.text.pdf.fonts.cmaps.CidLocationFromByte;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;
import org.apache.poi.hssf.usermodel.HSSFShapeTypes;

public class DocumentFont extends BaseFont {
    private static final int[] stdEnc = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 32, 33, 34, 35, 36, 37, 38, 8217, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 91, 92, 93, 94, 95, 8216, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 123, 124, 125, 126, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 161, 162, 163, 8260, 165, 402, 167, 164, 39, 8220, 171, 8249, 8250, 64257, 64258, 0, 8211, 8224, 8225, 183, 0, 182, 8226, 8218, 8222, 8221, 187, 8230, 8240, 0, 191, 0, 96, 180, 710, 732, 175, 728, 729, 168, 0, 730, 184, 0, 733, 731, 711, 8212, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, HSSFShapeTypes.ActionButtonDocument, 0, 170, 0, 0, 0, 0, 321, 216, 338, 186, 0, 0, 0, 0, 0, 230, 0, 0, 0, 305, 0, 0, 322, 248, 339, 223, 0, 0, 0, 0};
    private float ascender;
    private IntHashtable byte2uni;
    private float capHeight;
    protected String cjkEncoding;
    private BaseFont cjkMirror;
    protected int defaultWidth;
    private float descender;
    private IntHashtable diffmap;
    private PdfDictionary font;
    private String fontName;
    private float fontWeight;
    private IntHashtable hMetrics;
    protected boolean isType0;
    private float italicAngle;
    private float llx;
    private float lly;
    private HashMap<Integer, int[]> metrics;
    private PRIndirectReference refFont;
    private IntHashtable uni2byte;
    protected String uniMap;
    private float urx;
    private float ury;

    DocumentFont(PdfDictionary font) {
        this.metrics = new HashMap();
        this.uni2byte = new IntHashtable();
        this.byte2uni = new IntHashtable();
        this.ascender = 800.0f;
        this.capHeight = 700.0f;
        this.descender = -200.0f;
        this.italicAngle = 0.0f;
        this.fontWeight = 0.0f;
        this.llx = -50.0f;
        this.lly = -200.0f;
        this.urx = 100.0f;
        this.ury = 900.0f;
        this.isType0 = false;
        this.defaultWidth = 1000;
        this.refFont = null;
        this.font = font;
        init();
    }

    DocumentFont(PRIndirectReference refFont) {
        this.metrics = new HashMap();
        this.uni2byte = new IntHashtable();
        this.byte2uni = new IntHashtable();
        this.ascender = 800.0f;
        this.capHeight = 700.0f;
        this.descender = -200.0f;
        this.italicAngle = 0.0f;
        this.fontWeight = 0.0f;
        this.llx = -50.0f;
        this.lly = -200.0f;
        this.urx = 100.0f;
        this.ury = 900.0f;
        this.isType0 = false;
        this.defaultWidth = 1000;
        this.refFont = refFont;
        this.font = (PdfDictionary) PdfReader.getPdfObject((PdfObject) refFont);
        init();
    }

    public PdfDictionary getFontDictionary() {
        return this.font;
    }

    private void init() {
        this.encoding = "";
        this.fontSpecific = false;
        this.fontType = 4;
        PdfName baseFont = this.font.getAsName(PdfName.BASEFONT);
        this.fontName = baseFont != null ? PdfName.decodeName(baseFont.toString()) : "Unspecified Font Name";
        PdfName subType = this.font.getAsName(PdfName.SUBTYPE);
        if (PdfName.TYPE1.equals(subType) || PdfName.TRUETYPE.equals(subType)) {
            doType1TT();
        } else if (PdfName.TYPE3.equals(subType)) {
            fillEncoding(null);
        } else {
            PdfName encodingName = this.font.getAsName(PdfName.ENCODING);
            if (encodingName != null) {
                String enc = PdfName.decodeName(encodingName.toString());
                String ffontname = CJKFont.GetCompatibleFont(enc);
                if (ffontname != null) {
                    try {
                        this.cjkMirror = BaseFont.createFont(ffontname, enc, false);
                        this.cjkEncoding = enc;
                        this.uniMap = ((CJKFont) this.cjkMirror).getUniMap();
                    } catch (Exception e) {
                        throw new ExceptionConverter(e);
                    }
                }
                if (PdfName.TYPE0.equals(subType)) {
                    this.isType0 = true;
                    if (enc.equals(BaseFont.IDENTITY_H) || this.cjkMirror == null) {
                        processType0(this.font);
                        return;
                    }
                    PdfDictionary cidft = (PdfDictionary) PdfReader.getPdfObjectRelease(((PdfArray) PdfReader.getPdfObjectRelease(this.font.get(PdfName.DESCENDANTFONTS))).getPdfObject(0));
                    PdfNumber dwo = (PdfNumber) PdfReader.getPdfObjectRelease(cidft.get(PdfName.DW));
                    if (dwo != null) {
                        this.defaultWidth = dwo.intValue();
                    }
                    this.hMetrics = readWidths((PdfArray) PdfReader.getPdfObjectRelease(cidft.get(PdfName.f137W)));
                    fillFontDesc((PdfDictionary) PdfReader.getPdfObjectRelease(cidft.get(PdfName.FONTDESCRIPTOR)));
                }
            }
        }
    }

    private void processType0(PdfDictionary font) {
        try {
            PdfObject toUniObject = PdfReader.getPdfObjectRelease(font.get(PdfName.TOUNICODE));
            PdfDictionary cidft = (PdfDictionary) PdfReader.getPdfObjectRelease(((PdfArray) PdfReader.getPdfObjectRelease(font.get(PdfName.DESCENDANTFONTS))).getPdfObject(0));
            PdfNumber dwo = (PdfNumber) PdfReader.getPdfObjectRelease(cidft.get(PdfName.DW));
            int dw = 1000;
            if (dwo != null) {
                dw = dwo.intValue();
            }
            IntHashtable widths = readWidths((PdfArray) PdfReader.getPdfObjectRelease(cidft.get(PdfName.f137W)));
            fillFontDesc((PdfDictionary) PdfReader.getPdfObjectRelease(cidft.get(PdfName.FONTDESCRIPTOR)));
            if (toUniObject instanceof PRStream) {
                fillMetrics(PdfReader.getStreamBytes((PRStream) toUniObject), widths, dw);
            }
        } catch (Exception e) {
            throw new ExceptionConverter(e);
        }
    }

    private IntHashtable readWidths(PdfArray ws) {
        IntHashtable hh = new IntHashtable();
        if (ws != null) {
            int k = 0;
            while (k < ws.size()) {
                int c1 = ((PdfNumber) PdfReader.getPdfObjectRelease(ws.getPdfObject(k))).intValue();
                k++;
                PdfObject obj = PdfReader.getPdfObjectRelease(ws.getPdfObject(k));
                if (obj.isArray()) {
                    PdfArray a2 = (PdfArray) obj;
                    int j = 0;
                    while (j < a2.size()) {
                        int c12 = c1 + 1;
                        hh.put(c1, ((PdfNumber) PdfReader.getPdfObjectRelease(a2.getPdfObject(j))).intValue());
                        j++;
                        c1 = c12;
                    }
                } else {
                    int c2 = ((PdfNumber) obj).intValue();
                    k++;
                    int w = ((PdfNumber) PdfReader.getPdfObjectRelease(ws.getPdfObject(k))).intValue();
                    while (c1 <= c2) {
                        hh.put(c1, w);
                        c1++;
                    }
                }
                k++;
            }
        }
        return hh;
    }

    private String decodeString(PdfString ps) {
        if (ps.isHexWriting()) {
            return PdfEncodings.convertToString(ps.getBytes(), "UnicodeBigUnmarked");
        }
        return ps.toUnicodeString();
    }

    private void fillMetrics(byte[] touni, IntHashtable widths, int dw) {
        try {
            PdfContentParser pdfContentParser = new PdfContentParser(new PRTokeniser(new RandomAccessFileOrArray(new RandomAccessSourceFactory().createSource(touni))));
            boolean notFound = true;
            int nestLevel = 0;
            int maxExc = 50;
            while (true) {
                if (notFound || nestLevel > 0) {
                    try {
                        PdfObject ob = pdfContentParser.readPRObject();
                        if (ob != null) {
                            if (ob.type() != 200) {
                                continue;
                            } else if (ob.toString().equals("begin")) {
                                notFound = false;
                                nestLevel++;
                            } else if (ob.toString().equals("end")) {
                                nestLevel--;
                            } else if (ob.toString().equals("beginbfchar")) {
                                while (true) {
                                    nx = pdfContentParser.readPRObject();
                                    if (nx.toString().equals("endbfchar")) {
                                        break;
                                    }
                                    String cid = decodeString((PdfString) nx);
                                    uni = decodeString((PdfString) pdfContentParser.readPRObject());
                                    if (uni.length() == 1) {
                                        int cidc = cid.charAt(0);
                                        unic = uni.charAt(uni.length() - 1);
                                        w = dw;
                                        if (widths.containsKey(cidc)) {
                                            w = widths.get(cidc);
                                        }
                                        this.metrics.put(Integer.valueOf(unic), new int[]{cidc, w});
                                    }
                                }
                            } else if (ob.toString().equals("beginbfrange")) {
                                while (true) {
                                    nx = pdfContentParser.readPRObject();
                                    if (nx.toString().equals("endbfrange")) {
                                        break;
                                    }
                                    String cid1 = decodeString((PdfString) nx);
                                    String cid2 = decodeString((PdfString) pdfContentParser.readPRObject());
                                    int cid1c = cid1.charAt(0);
                                    int cid2c = cid2.charAt(0);
                                    PdfObject ob2 = pdfContentParser.readPRObject();
                                    if (ob2.isString()) {
                                        uni = decodeString((PdfString) ob2);
                                        if (uni.length() == 1) {
                                            unic = uni.charAt(uni.length() - 1);
                                            while (cid1c <= cid2c) {
                                                w = dw;
                                                if (widths.containsKey(cid1c)) {
                                                    w = widths.get(cid1c);
                                                }
                                                this.metrics.put(Integer.valueOf(unic), new int[]{cid1c, w});
                                                cid1c++;
                                                unic++;
                                            }
                                        }
                                    } else {
                                        PdfArray a = (PdfArray) ob2;
                                        int j = 0;
                                        while (j < a.size()) {
                                            uni = decodeString(a.getAsString(j));
                                            if (uni.length() == 1) {
                                                unic = uni.charAt(uni.length() - 1);
                                                w = dw;
                                                if (widths.containsKey(cid1c)) {
                                                    w = widths.get(cid1c);
                                                }
                                                this.metrics.put(Integer.valueOf(unic), new int[]{cid1c, w});
                                            }
                                            j++;
                                            cid1c++;
                                        }
                                    }
                                }
                            }
                        } else {
                            return;
                        }
                    } catch (Exception e) {
                        maxExc--;
                        if (maxExc < 0) {
                            return;
                        }
                    }
                } else {
                    return;
                }
            }
        } catch (Exception e2) {
            throw new ExceptionConverter(e2);
        }
    }

    private void doType1TT() {
        int k;
        CMapToUnicode toUnicode = null;
        PdfObject enc = PdfReader.getPdfObject(this.font.get(PdfName.ENCODING));
        if (enc == null) {
            PdfName baseFont = this.font.getAsName(PdfName.BASEFONT);
            if (BuiltinFonts14.containsKey(this.fontName) && (PdfName.SYMBOL.equals(baseFont) || PdfName.ZAPFDINGBATS.equals(baseFont))) {
                fillEncoding(baseFont);
            } else {
                fillEncoding(null);
            }
            try {
                toUnicode = processToUnicode();
                if (toUnicode != null) {
                    for (Entry<Integer, Integer> kv : toUnicode.createReverseMapping().entrySet()) {
                        this.uni2byte.put(((Integer) kv.getKey()).intValue(), ((Integer) kv.getValue()).intValue());
                        this.byte2uni.put(((Integer) kv.getValue()).intValue(), ((Integer) kv.getKey()).intValue());
                    }
                }
            } catch (Exception ex) {
                throw new ExceptionConverter(ex);
            }
        } else if (enc.isName()) {
            fillEncoding((PdfName) enc);
        } else if (enc.isDictionary()) {
            PdfDictionary encDic = (PdfDictionary) enc;
            enc = PdfReader.getPdfObject(encDic.get(PdfName.BASEENCODING));
            if (enc == null) {
                fillEncoding(null);
            } else {
                fillEncoding((PdfName) enc);
            }
            PdfArray diffs = encDic.getAsArray(PdfName.DIFFERENCES);
            if (diffs != null) {
                this.diffmap = new IntHashtable();
                int currentNumber = 0;
                for (k = 0; k < diffs.size(); k++) {
                    PdfObject obj = diffs.getPdfObject(k);
                    if (obj.isNumber()) {
                        currentNumber = ((PdfNumber) obj).intValue();
                    } else {
                        int[] c = GlyphList.nameToUnicode(PdfName.decodeName(((PdfName) obj).toString()));
                        if (c == null || c.length <= 0) {
                            if (toUnicode == null) {
                                toUnicode = processToUnicode();
                                if (toUnicode == null) {
                                    toUnicode = new CMapToUnicode();
                                }
                            }
                            String unicode = toUnicode.lookup(new byte[]{(byte) currentNumber}, 0, 1);
                            if (unicode != null && unicode.length() == 1) {
                                this.uni2byte.put(unicode.charAt(0), currentNumber);
                                this.byte2uni.put(currentNumber, unicode.charAt(0));
                                this.diffmap.put(unicode.charAt(0), currentNumber);
                            }
                        } else {
                            this.uni2byte.put(c[0], currentNumber);
                            this.byte2uni.put(currentNumber, c[0]);
                            this.diffmap.put(c[0], currentNumber);
                        }
                        currentNumber++;
                    }
                }
            }
        }
        PdfArray newWidths = this.font.getAsArray(PdfName.WIDTHS);
        PdfNumber first = this.font.getAsNumber(PdfName.FIRSTCHAR);
        PdfNumber last = this.font.getAsNumber(PdfName.LASTCHAR);
        if (BuiltinFonts14.containsKey(this.fontName)) {
            int[] e;
            try {
                int n;
                BaseFont bf = BaseFont.createFont(this.fontName, "Cp1252", false);
                e = this.uni2byte.toOrderedKeys();
                for (k = 0; k < e.length; k++) {
                    n = this.uni2byte.get(e[k]);
                    this.widths[n] = bf.getRawWidth(n, GlyphList.unicodeToName(e[k]));
                }
                if (this.diffmap != null) {
                    e = this.diffmap.toOrderedKeys();
                    for (k = 0; k < e.length; k++) {
                        n = this.diffmap.get(e[k]);
                        this.widths[n] = bf.getRawWidth(n, GlyphList.unicodeToName(e[k]));
                    }
                    this.diffmap = null;
                }
                this.ascender = bf.getFontDescriptor(1, 1000.0f);
                this.capHeight = bf.getFontDescriptor(2, 1000.0f);
                this.descender = bf.getFontDescriptor(3, 1000.0f);
                this.italicAngle = bf.getFontDescriptor(4, 1000.0f);
                this.fontWeight = bf.getFontDescriptor(23, 1000.0f);
                this.llx = bf.getFontDescriptor(5, 1000.0f);
                this.lly = bf.getFontDescriptor(6, 1000.0f);
                this.urx = bf.getFontDescriptor(7, 1000.0f);
                this.ury = bf.getFontDescriptor(8, 1000.0f);
            } catch (int[] e2) {
                throw new ExceptionConverter(e2);
            }
        }
        if (!(first == null || last == null || newWidths == null)) {
            int f = first.intValue();
            int nSize = f + newWidths.size();
            if (this.widths.length < nSize) {
                Object tmp = new int[nSize];
                System.arraycopy(this.widths, 0, tmp, 0, f);
                this.widths = tmp;
            }
            for (k = 0; k < newWidths.size(); k++) {
                this.widths[f + k] = newWidths.getAsNumber(k).intValue();
            }
        }
        fillFontDesc(this.font.getAsDict(PdfName.FONTDESCRIPTOR));
    }

    private CMapToUnicode processToUnicode() {
        PdfObject toUni = PdfReader.getPdfObjectRelease(this.font.get(PdfName.TOUNICODE));
        if (!(toUni instanceof PRStream)) {
            return null;
        }
        try {
            CidLocationFromByte lb = new CidLocationFromByte(PdfReader.getStreamBytes((PRStream) toUni));
            CMapToUnicode cmapRet = new CMapToUnicode();
            try {
                CMapParserEx.parseCid("", cmapRet, lb);
                return cmapRet;
            } catch (Exception e) {
                CMapToUnicode cMapToUnicode = cmapRet;
                return null;
            }
        } catch (Exception e2) {
            return null;
        }
    }

    private void fillFontDesc(PdfDictionary fontDesc) {
        if (fontDesc != null) {
            PdfNumber v = fontDesc.getAsNumber(PdfName.ASCENT);
            if (v != null) {
                this.ascender = v.floatValue();
            }
            v = fontDesc.getAsNumber(PdfName.CAPHEIGHT);
            if (v != null) {
                this.capHeight = v.floatValue();
            }
            v = fontDesc.getAsNumber(PdfName.DESCENT);
            if (v != null) {
                this.descender = v.floatValue();
            }
            v = fontDesc.getAsNumber(PdfName.ITALICANGLE);
            if (v != null) {
                this.italicAngle = v.floatValue();
            }
            v = fontDesc.getAsNumber(PdfName.FONTWEIGHT);
            if (v != null) {
                this.fontWeight = v.floatValue();
            }
            PdfArray bbox = fontDesc.getAsArray(PdfName.FONTBBOX);
            if (bbox != null) {
                float t;
                this.llx = bbox.getAsNumber(0).floatValue();
                this.lly = bbox.getAsNumber(1).floatValue();
                this.urx = bbox.getAsNumber(2).floatValue();
                this.ury = bbox.getAsNumber(3).floatValue();
                if (this.llx > this.urx) {
                    t = this.llx;
                    this.llx = this.urx;
                    this.urx = t;
                }
                if (this.lly > this.ury) {
                    t = this.lly;
                    this.lly = this.ury;
                    this.ury = t;
                }
            }
            float maxAscent = Math.max(this.ury, this.ascender);
            float minDescent = Math.min(this.lly, this.descender);
            this.ascender = (maxAscent * 1000.0f) / (maxAscent - minDescent);
            this.descender = (minDescent * 1000.0f) / (maxAscent - minDescent);
        }
    }

    private void fillEncoding(PdfName encoding) {
        int k;
        if (encoding == null && isSymbolic()) {
            for (k = 0; k < 256; k++) {
                this.uni2byte.put(k, k);
                this.byte2uni.put(k, k);
            }
        } else if (PdfName.MAC_ROMAN_ENCODING.equals(encoding) || PdfName.WIN_ANSI_ENCODING.equals(encoding) || PdfName.SYMBOL.equals(encoding) || PdfName.ZAPFDINGBATS.equals(encoding)) {
            byte[] b = new byte[256];
            for (k = 0; k < 256; k++) {
                b[k] = (byte) k;
            }
            String enc = "Cp1252";
            if (PdfName.MAC_ROMAN_ENCODING.equals(encoding)) {
                enc = BaseFont.MACROMAN;
            } else if (PdfName.SYMBOL.equals(encoding)) {
                enc = "Symbol";
            } else if (PdfName.ZAPFDINGBATS.equals(encoding)) {
                enc = "ZapfDingbats";
            }
            char[] arr = PdfEncodings.convertToString(b, enc).toCharArray();
            for (k = 0; k < 256; k++) {
                this.uni2byte.put(arr[k], k);
                this.byte2uni.put(k, arr[k]);
            }
            this.encoding = enc;
        } else {
            for (k = 0; k < 256; k++) {
                this.uni2byte.put(stdEnc[k], k);
                this.byte2uni.put(k, stdEnc[k]);
            }
        }
    }

    public String[][] getFamilyFontName() {
        return getFullFontName();
    }

    public float getFontDescriptor(int key, float fontSize) {
        if (this.cjkMirror != null) {
            return this.cjkMirror.getFontDescriptor(key, fontSize);
        }
        switch (key) {
            case 1:
            case 9:
                return (this.ascender * fontSize) / 1000.0f;
            case 2:
                return (this.capHeight * fontSize) / 1000.0f;
            case 3:
            case 10:
                return (this.descender * fontSize) / 1000.0f;
            case 4:
                return this.italicAngle;
            case 5:
                return (this.llx * fontSize) / 1000.0f;
            case 6:
                return (this.lly * fontSize) / 1000.0f;
            case 7:
                return (this.urx * fontSize) / 1000.0f;
            case 8:
                return (this.ury * fontSize) / 1000.0f;
            case 11:
                return 0.0f;
            case 12:
                return ((this.urx - this.llx) * fontSize) / 1000.0f;
            case 23:
                return (this.fontWeight * fontSize) / 1000.0f;
            default:
                return 0.0f;
        }
    }

    public String[][] getFullFontName() {
        String[][] strArr = new String[1][];
        strArr[0] = new String[]{"", "", "", this.fontName};
        return strArr;
    }

    public String[][] getAllNameEntries() {
        String[][] strArr = new String[1][];
        strArr[0] = new String[]{"4", "", "", "", this.fontName};
        return strArr;
    }

    public int getKerning(int char1, int char2) {
        return 0;
    }

    public String getPostscriptFontName() {
        return this.fontName;
    }

    int getRawWidth(int c, String name) {
        return 0;
    }

    public boolean hasKernPairs() {
        return false;
    }

    void writeFont(PdfWriter writer, PdfIndirectReference ref, Object[] params) throws DocumentException, IOException {
    }

    public PdfStream getFullFontStream() {
        return null;
    }

    public int getWidth(int char1) {
        if (this.isType0) {
            if (this.hMetrics == null || this.cjkMirror == null || this.cjkMirror.isVertical()) {
                int[] ws = (int[]) this.metrics.get(Integer.valueOf(char1));
                if (ws != null) {
                    return ws[1];
                }
                return 0;
            }
            int v = this.hMetrics.get(this.cjkMirror.getCidCode(char1));
            if (v > 0) {
                return v;
            }
            return this.defaultWidth;
        } else if (this.cjkMirror != null) {
            return this.cjkMirror.getWidth(char1);
        } else {
            return super.getWidth(char1);
        }
    }

    public int getWidth(String text) {
        if (this.isType0) {
            int total = 0;
            int k;
            if (this.hMetrics == null || this.cjkMirror == null || this.cjkMirror.isVertical()) {
                for (char valueOf : text.toCharArray()) {
                    int[] ws = (int[]) this.metrics.get(Integer.valueOf(valueOf));
                    if (ws != null) {
                        total += ws[1];
                    }
                }
                return total;
            } else if (((CJKFont) this.cjkMirror).isIdentity()) {
                for (k = 0; k < text.length(); k++) {
                    total += getWidth(text.charAt(k));
                }
                return total;
            } else {
                k = 0;
                while (k < text.length()) {
                    int val;
                    if (Utilities.isSurrogatePair(text, k)) {
                        val = Utilities.convertToUtf32(text, k);
                        k++;
                    } else {
                        val = text.charAt(k);
                    }
                    total += getWidth(val);
                    k++;
                }
                return total;
            }
        } else if (this.cjkMirror != null) {
            return this.cjkMirror.getWidth(text);
        } else {
            return super.getWidth(text);
        }
    }

    public byte[] convertToBytes(String text) {
        if (this.cjkMirror != null) {
            return this.cjkMirror.convertToBytes(text);
        }
        byte[] b;
        int k;
        if (this.isType0) {
            char[] chars = text.toCharArray();
            int len = chars.length;
            b = new byte[(len * 2)];
            k = 0;
            int bptr = 0;
            while (k < len) {
                int[] ws = (int[]) this.metrics.get(Integer.valueOf(chars[k]));
                if (ws != null) {
                    int g = ws[0];
                    int i = bptr + 1;
                    b[bptr] = (byte) (g / 256);
                    bptr = i + 1;
                    b[i] = (byte) g;
                }
                k++;
                bptr = bptr;
            }
            if (bptr == b.length) {
                return b;
            }
            byte[] nb = new byte[bptr];
            System.arraycopy(b, 0, nb, 0, bptr);
            return nb;
        }
        char[] cc = text.toCharArray();
        b = new byte[cc.length];
        int ptr = 0;
        for (k = 0; k < cc.length; k++) {
            if (this.uni2byte.containsKey(cc[k])) {
                int ptr2 = ptr + 1;
                b[ptr] = (byte) this.uni2byte.get(cc[k]);
                ptr = ptr2;
            }
        }
        if (ptr == b.length) {
            return b;
        }
        byte[] b2 = new byte[ptr];
        System.arraycopy(b, 0, b2, 0, ptr);
        return b2;
    }

    byte[] convertToBytes(int char1) {
        if (this.cjkMirror != null) {
            return this.cjkMirror.convertToBytes(char1);
        }
        if (this.isType0) {
            int[] ws = (int[]) this.metrics.get(Integer.valueOf(char1));
            if (ws == null) {
                return new byte[0];
            }
            int g = ws[0];
            return new byte[]{(byte) (g / 256), (byte) g};
        } else if (!this.uni2byte.containsKey(char1)) {
            return new byte[0];
        } else {
            return new byte[]{(byte) this.uni2byte.get(char1)};
        }
    }

    PdfIndirectReference getIndirectReference() {
        if (this.refFont != null) {
            return this.refFont;
        }
        throw new IllegalArgumentException("Font reuse not allowed with direct font objects.");
    }

    public boolean charExists(int c) {
        if (this.cjkMirror != null) {
            return this.cjkMirror.charExists(c);
        }
        if (this.isType0) {
            return this.metrics.containsKey(Integer.valueOf(c));
        }
        return super.charExists(c);
    }

    public void setPostscriptFontName(String name) {
    }

    public boolean setKerning(int char1, int char2, int kern) {
        return false;
    }

    public int[] getCharBBox(int c) {
        return null;
    }

    protected int[] getRawCharBBox(int c, String name) {
        return null;
    }

    public boolean isVertical() {
        if (this.cjkMirror != null) {
            return this.cjkMirror.isVertical();
        }
        return super.isVertical();
    }

    IntHashtable getUni2Byte() {
        return this.uni2byte;
    }

    IntHashtable getByte2Uni() {
        return this.byte2uni;
    }

    IntHashtable getDiffmap() {
        return this.diffmap;
    }

    boolean isSymbolic() {
        PdfDictionary fontDescriptor = this.font.getAsDict(PdfName.FONTDESCRIPTOR);
        if (fontDescriptor == null) {
            return false;
        }
        PdfNumber flags = fontDescriptor.getAsNumber(PdfName.FLAGS);
        if (flags == null || (flags.intValue() & 4) == 0) {
            return false;
        }
        return true;
    }
}
