package com.itextpdf.text.pdf;

import android.support.v7.widget.helper.ItemTouchHelper.Callback;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.error_messages.MessageLocalization;
import com.itextpdf.text.io.StreamUtil;
import com.itextpdf.text.pdf.fonts.FontsResourceAnchor;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.StringTokenizer;

class Type1Font extends BaseFont {
    private static final int[] PFB_TYPES = new int[]{1, 2, 1};
    private static FontsResourceAnchor resourceAnchor;
    private int Ascender = 800;
    private int CapHeight = 700;
    private HashMap<Object, Object[]> CharMetrics = new HashMap();
    private String CharacterSet;
    private int Descender = -200;
    private String EncodingScheme = "FontSpecific";
    private String FamilyName;
    private String FontName;
    private String FullName;
    private boolean IsFixedPitch = false;
    private float ItalicAngle = 0.0f;
    private HashMap<String, Object[]> KernPairs = new HashMap();
    private int StdHW;
    private int StdVW = 80;
    private int UnderlinePosition = -100;
    private int UnderlineThickness = 50;
    private String Weight = "";
    private int XHeight = 480;
    private boolean builtinFont = false;
    private String fileName;
    private int llx = -50;
    private int lly = -200;
    protected byte[] pfb;
    private int urx = 1000;
    private int ury = 900;

    Type1Font(String afmFile, String enc, boolean emb, byte[] ttfAfm, byte[] pfb, boolean forceRead) throws DocumentException, IOException {
        Throwable th;
        if (emb && ttfAfm != null && pfb == null) {
            throw new DocumentException(MessageLocalization.getComposedMessage("two.byte.arrays.are.needed.if.the.type1.font.is.embedded", new Object[0]));
        }
        if (emb && ttfAfm != null) {
            this.pfb = pfb;
        }
        this.encoding = enc;
        this.embedded = emb;
        this.fileName = afmFile;
        this.fontType = 0;
        RandomAccessFileOrArray rf = null;
        InputStream is = null;
        RandomAccessFileOrArray rf2;
        if (BuiltinFonts14.containsKey(afmFile)) {
            this.embedded = false;
            this.builtinFont = true;
            byte[] buf = new byte[1024];
            try {
                if (resourceAnchor == null) {
                    resourceAnchor = new FontsResourceAnchor();
                }
                is = StreamUtil.getResourceStream(BaseFont.RESOURCE_PATH + afmFile + ".afm", resourceAnchor.getClass().getClassLoader());
                if (is == null) {
                    String msg = MessageLocalization.getComposedMessage("1.not.found.as.resource", afmFile);
                    System.err.println(msg);
                    throw new DocumentException(msg);
                }
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                while (true) {
                    int size = is.read(buf);
                    if (size < 0) {
                        break;
                    }
                    out.write(buf, 0, size);
                }
                buf = out.toByteArray();
                if (is != null) {
                    try {
                        is.close();
                    } catch (Exception e) {
                    }
                }
                try {
                    rf2 = new RandomAccessFileOrArray(buf);
                    try {
                        process(rf2);
                        if (rf2 != null) {
                            try {
                                rf2.close();
                            } catch (Exception e2) {
                            }
                        }
                        rf = rf2;
                    } catch (Throwable th2) {
                        th = th2;
                        rf = rf2;
                        if (rf != null) {
                            try {
                                rf.close();
                            } catch (Exception e3) {
                            }
                        }
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    if (rf != null) {
                        rf.close();
                    }
                    throw th;
                }
            } catch (Throwable th4) {
                if (is != null) {
                    try {
                        is.close();
                    } catch (Exception e4) {
                    }
                }
            }
        } else if (afmFile.toLowerCase().endsWith(".afm")) {
            if (ttfAfm == null) {
                try {
                    rf = new RandomAccessFileOrArray(afmFile, forceRead, Document.plainRandomAccess);
                } catch (Throwable th5) {
                    if (rf != null) {
                        try {
                            rf.close();
                        } catch (Exception e5) {
                        }
                    }
                }
            } else {
                rf = new RandomAccessFileOrArray(ttfAfm);
            }
            process(rf);
            if (rf != null) {
                try {
                    rf.close();
                } catch (Exception e6) {
                }
            }
        } else if (afmFile.toLowerCase().endsWith(".pfm")) {
            try {
                ByteArrayOutputStream ba = new ByteArrayOutputStream();
                if (ttfAfm == null) {
                    rf2 = new RandomAccessFileOrArray(afmFile, forceRead, Document.plainRandomAccess);
                } else {
                    rf2 = new RandomAccessFileOrArray(ttfAfm);
                }
                try {
                    Pfm2afm.convert(rf2, ba);
                    rf2.close();
                    rf = new RandomAccessFileOrArray(ba.toByteArray());
                    process(rf);
                    if (rf != null) {
                        try {
                            rf.close();
                        } catch (Exception e7) {
                        }
                    }
                } catch (Throwable th6) {
                    th = th6;
                    rf = rf2;
                    if (rf != null) {
                        try {
                            rf.close();
                        } catch (Exception e8) {
                        }
                    }
                    throw th;
                }
            } catch (Throwable th7) {
                th = th7;
                if (rf != null) {
                    rf.close();
                }
                throw th;
            }
        } else {
            throw new DocumentException(MessageLocalization.getComposedMessage("1.is.not.an.afm.or.pfm.font.file", afmFile));
        }
        this.EncodingScheme = this.EncodingScheme.trim();
        if (this.EncodingScheme.equals("AdobeStandardEncoding") || this.EncodingScheme.equals("StandardEncoding")) {
            this.fontSpecific = false;
        }
        if (!this.encoding.startsWith("#")) {
            PdfEncodings.convertToBytes(" ", enc);
        }
        createEncoding();
    }

    int getRawWidth(int c, String name) {
        Object[] metrics;
        if (name == null) {
            metrics = (Object[]) this.CharMetrics.get(Integer.valueOf(c));
        } else if (name.equals(BaseFont.notdef)) {
            return 0;
        } else {
            metrics = (Object[]) this.CharMetrics.get(name);
        }
        if (metrics != null) {
            return ((Integer) metrics[1]).intValue();
        }
        return 0;
    }

    public int getKerning(int char1, int char2) {
        String first = GlyphList.unicodeToName(char1);
        if (first == null) {
            return 0;
        }
        String second = GlyphList.unicodeToName(char2);
        if (second == null) {
            return 0;
        }
        Object[] obj = (Object[]) this.KernPairs.get(first);
        if (obj == null) {
            return 0;
        }
        for (int k = 0; k < obj.length; k += 2) {
            if (second.equals(obj[k])) {
                return ((Integer) obj[k + 1]).intValue();
            }
        }
        return 0;
    }

    public void process(RandomAccessFileOrArray rf) throws DocumentException, IOException {
        String ident;
        boolean isMetrics = false;
        while (true) {
            String line = rf.readLine();
            if (line == null) {
                break;
            }
            StringTokenizer stringTokenizer = new StringTokenizer(line, " ,\n\r\t\f");
            if (stringTokenizer.hasMoreTokens()) {
                ident = stringTokenizer.nextToken();
                if (ident.equals("FontName")) {
                    this.FontName = stringTokenizer.nextToken("ÿ").substring(1);
                } else if (ident.equals("FullName")) {
                    this.FullName = stringTokenizer.nextToken("ÿ").substring(1);
                } else if (ident.equals("FamilyName")) {
                    this.FamilyName = stringTokenizer.nextToken("ÿ").substring(1);
                } else if (ident.equals("Weight")) {
                    this.Weight = stringTokenizer.nextToken("ÿ").substring(1);
                } else if (ident.equals("ItalicAngle")) {
                    this.ItalicAngle = Float.parseFloat(stringTokenizer.nextToken());
                } else if (ident.equals("IsFixedPitch")) {
                    this.IsFixedPitch = stringTokenizer.nextToken().equals(PdfBoolean.TRUE);
                } else if (ident.equals("CharacterSet")) {
                    this.CharacterSet = stringTokenizer.nextToken("ÿ").substring(1);
                } else if (ident.equals("FontBBox")) {
                    this.llx = (int) Float.parseFloat(stringTokenizer.nextToken());
                    this.lly = (int) Float.parseFloat(stringTokenizer.nextToken());
                    this.urx = (int) Float.parseFloat(stringTokenizer.nextToken());
                    this.ury = (int) Float.parseFloat(stringTokenizer.nextToken());
                } else if (ident.equals("UnderlinePosition")) {
                    this.UnderlinePosition = (int) Float.parseFloat(stringTokenizer.nextToken());
                } else if (ident.equals("UnderlineThickness")) {
                    this.UnderlineThickness = (int) Float.parseFloat(stringTokenizer.nextToken());
                } else if (ident.equals("EncodingScheme")) {
                    this.EncodingScheme = stringTokenizer.nextToken("ÿ").substring(1);
                } else if (ident.equals("CapHeight")) {
                    this.CapHeight = (int) Float.parseFloat(stringTokenizer.nextToken());
                } else if (ident.equals("XHeight")) {
                    this.XHeight = (int) Float.parseFloat(stringTokenizer.nextToken());
                } else if (ident.equals("Ascender")) {
                    this.Ascender = (int) Float.parseFloat(stringTokenizer.nextToken());
                } else if (ident.equals("Descender")) {
                    this.Descender = (int) Float.parseFloat(stringTokenizer.nextToken());
                } else if (ident.equals("StdHW")) {
                    this.StdHW = (int) Float.parseFloat(stringTokenizer.nextToken());
                } else if (ident.equals("StdVW")) {
                    this.StdVW = (int) Float.parseFloat(stringTokenizer.nextToken());
                } else if (ident.equals("StartCharMetrics")) {
                    break;
                }
            }
        }
        isMetrics = true;
        if (isMetrics) {
            while (true) {
                line = rf.readLine();
                if (line == null) {
                    break;
                }
                stringTokenizer = new StringTokenizer(line);
                if (stringTokenizer.hasMoreTokens()) {
                    if (stringTokenizer.nextToken().equals("EndCharMetrics")) {
                        break;
                    }
                    Integer C = Integer.valueOf(-1);
                    Integer WX = Integer.valueOf(Callback.DEFAULT_SWIPE_ANIMATION_DURATION);
                    String N = "";
                    int[] B = null;
                    stringTokenizer = new StringTokenizer(line, ";");
                    while (stringTokenizer.hasMoreTokens()) {
                        stringTokenizer = new StringTokenizer(stringTokenizer.nextToken());
                        if (stringTokenizer.hasMoreTokens()) {
                            ident = stringTokenizer.nextToken();
                            if (ident.equals("C")) {
                                C = Integer.valueOf(stringTokenizer.nextToken());
                            } else if (ident.equals("WX")) {
                                WX = Integer.valueOf((int) Float.parseFloat(stringTokenizer.nextToken()));
                            } else if (ident.equals("N")) {
                                N = stringTokenizer.nextToken();
                            } else if (ident.equals("B")) {
                                B = new int[]{Integer.parseInt(stringTokenizer.nextToken()), Integer.parseInt(stringTokenizer.nextToken()), Integer.parseInt(stringTokenizer.nextToken()), Integer.parseInt(stringTokenizer.nextToken())};
                            }
                        }
                    }
                    Object[] metrics = new Object[]{C, WX, N, B};
                    if (C.intValue() >= 0) {
                        this.CharMetrics.put(C, metrics);
                    }
                    this.CharMetrics.put(N, metrics);
                }
            }
            isMetrics = false;
            if (isMetrics) {
                throw new DocumentException(MessageLocalization.getComposedMessage("missing.endcharmetrics.in.1", this.fileName));
            }
            if (!this.CharMetrics.containsKey("nonbreakingspace")) {
                Object[] space = (Object[]) this.CharMetrics.get("space");
                if (space != null) {
                    this.CharMetrics.put("nonbreakingspace", space);
                }
            }
            while (true) {
                line = rf.readLine();
                if (line == null) {
                    break;
                }
                stringTokenizer = new StringTokenizer(line);
                if (stringTokenizer.hasMoreTokens()) {
                    ident = stringTokenizer.nextToken();
                    if (!ident.equals("EndFontMetrics")) {
                        if (ident.equals("StartKernPairs")) {
                            break;
                        }
                    } else {
                        return;
                    }
                }
            }
            isMetrics = true;
            if (isMetrics) {
                while (true) {
                    line = rf.readLine();
                    if (line == null) {
                        break;
                    }
                    stringTokenizer = new StringTokenizer(line);
                    if (stringTokenizer.hasMoreTokens()) {
                        ident = stringTokenizer.nextToken();
                        if (ident.equals("KPX")) {
                            String first = stringTokenizer.nextToken();
                            String second = stringTokenizer.nextToken();
                            Integer width = Integer.valueOf((int) Float.parseFloat(stringTokenizer.nextToken()));
                            Object[] relates = (Object[]) this.KernPairs.get(first);
                            if (relates == null) {
                                this.KernPairs.put(first, new Object[]{second, width});
                            } else {
                                int n = relates.length;
                                Object[] relates2 = new Object[(n + 2)];
                                System.arraycopy(relates, 0, relates2, 0, n);
                                relates2[n] = second;
                                relates2[n + 1] = width;
                                this.KernPairs.put(first, relates2);
                            }
                        } else if (ident.equals("EndKernPairs")) {
                            break;
                        }
                    }
                }
                isMetrics = false;
                if (isMetrics) {
                    throw new DocumentException(MessageLocalization.getComposedMessage("missing.endkernpairs.in.1", this.fileName));
                } else {
                    rf.close();
                    return;
                }
            }
            throw new DocumentException(MessageLocalization.getComposedMessage("missing.endfontmetrics.in.1", this.fileName));
        }
        throw new DocumentException(MessageLocalization.getComposedMessage("missing.startcharmetrics.in.1", this.fileName));
    }

    public PdfStream getFullFontStream() throws DocumentException {
        if (this.builtinFont || !this.embedded) {
            return null;
        }
        RandomAccessFileOrArray rf = null;
        try {
            String filePfb = this.fileName.substring(0, this.fileName.length() - 3) + "pfb";
            if (this.pfb == null) {
                rf = new RandomAccessFileOrArray(filePfb, true, Document.plainRandomAccess);
            } else {
                rf = new RandomAccessFileOrArray(this.pfb);
            }
            byte[] st = new byte[(((int) rf.length()) - 18)];
            int[] lengths = new int[3];
            int bytePtr = 0;
            int k = 0;
            while (k < 3) {
                if (rf.read() != 128) {
                    throw new DocumentException(MessageLocalization.getComposedMessage("start.marker.missing.in.1", filePfb));
                } else if (rf.read() != PFB_TYPES[k]) {
                    throw new DocumentException(MessageLocalization.getComposedMessage("incorrect.segment.type.in.1", filePfb));
                } else {
                    int size = ((rf.read() + (rf.read() << 8)) + (rf.read() << 16)) + (rf.read() << 24);
                    lengths[k] = size;
                    while (size != 0) {
                        int got = rf.read(st, bytePtr, size);
                        if (got < 0) {
                            throw new DocumentException(MessageLocalization.getComposedMessage("premature.end.in.1", filePfb));
                        }
                        bytePtr += got;
                        size -= got;
                    }
                    k++;
                }
            }
            PdfStream streamFont = new StreamFont(st, lengths, this.compressionLevel);
            if (rf == null) {
                return streamFont;
            }
            try {
                rf.close();
                return streamFont;
            } catch (Exception e) {
                return streamFont;
            }
        } catch (Exception e2) {
            throw new DocumentException(e2);
        } catch (Throwable th) {
            if (rf != null) {
                try {
                    rf.close();
                } catch (Exception e3) {
                }
            }
        }
    }

    private PdfDictionary getFontDescriptor(PdfIndirectReference fontStream) {
        if (this.builtinFont) {
            return null;
        }
        PdfDictionary dic = new PdfDictionary(PdfName.FONTDESCRIPTOR);
        dic.put(PdfName.ASCENT, new PdfNumber(this.Ascender));
        dic.put(PdfName.CAPHEIGHT, new PdfNumber(this.CapHeight));
        dic.put(PdfName.DESCENT, new PdfNumber(this.Descender));
        dic.put(PdfName.FONTBBOX, new PdfRectangle((float) this.llx, (float) this.lly, (float) this.urx, (float) this.ury));
        dic.put(PdfName.FONTNAME, new PdfName(this.FontName));
        dic.put(PdfName.ITALICANGLE, new PdfNumber(this.ItalicAngle));
        dic.put(PdfName.STEMV, new PdfNumber(this.StdVW));
        if (fontStream != null) {
            dic.put(PdfName.FONTFILE, fontStream);
        }
        int flags = 0;
        if (this.IsFixedPitch) {
            flags = 0 | 1;
        }
        flags |= this.fontSpecific ? 4 : 32;
        if (this.ItalicAngle < 0.0f) {
            flags |= 64;
        }
        if (this.FontName.indexOf("Caps") >= 0 || this.FontName.endsWith("SC")) {
            flags |= 131072;
        }
        if (this.Weight.equals("Bold")) {
            flags |= 262144;
        }
        dic.put(PdfName.FLAGS, new PdfNumber(flags));
        return dic;
    }

    private PdfDictionary getFontBaseType(PdfIndirectReference fontDescriptor, int firstChar, int lastChar, byte[] shortTag) {
        boolean stdEncoding;
        int k;
        PdfDictionary dic = new PdfDictionary(PdfName.FONT);
        dic.put(PdfName.SUBTYPE, PdfName.TYPE1);
        dic.put(PdfName.BASEFONT, new PdfName(this.FontName));
        if (this.encoding.equals("Cp1252") || this.encoding.equals(BaseFont.MACROMAN)) {
            stdEncoding = true;
        } else {
            stdEncoding = false;
        }
        if (!(this.fontSpecific && this.specialMap == null)) {
            for (k = firstChar; k <= lastChar; k++) {
                if (!this.differences[k].equals(BaseFont.notdef)) {
                    firstChar = k;
                    break;
                }
            }
            if (stdEncoding) {
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
        if (!(this.specialMap == null && !this.forceWidthsOutput && this.builtinFont && (this.fontSpecific || stdEncoding))) {
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
        }
        if (!(this.builtinFont || fontDescriptor == null)) {
            dic.put(PdfName.FONTDESCRIPTOR, fontDescriptor);
        }
        return dic;
    }

    void writeFont(PdfWriter writer, PdfIndirectReference ref, Object[] params) throws DocumentException, IOException {
        boolean subsetp = false;
        int firstChar = ((Integer) params[0]).intValue();
        int lastChar = ((Integer) params[1]).intValue();
        byte[] shortTag = (byte[]) params[2];
        if (((Boolean) params[3]).booleanValue() && this.subset) {
            subsetp = true;
        }
        if (!subsetp) {
            firstChar = 0;
            lastChar = shortTag.length - 1;
            for (int k = 0; k < shortTag.length; k++) {
                shortTag[k] = (byte) 1;
            }
        }
        PdfIndirectReference ind_font = null;
        PdfObject pobj = getFullFontStream();
        if (pobj != null) {
            ind_font = writer.addToBody(pobj).getIndirectReference();
        }
        pobj = getFontDescriptor(ind_font);
        if (pobj != null) {
            ind_font = writer.addToBody(pobj).getIndirectReference();
        }
        writer.addToBody(getFontBaseType(ind_font, firstChar, lastChar, shortTag), ref);
    }

    public float getFontDescriptor(int key, float fontSize) {
        switch (key) {
            case 1:
            case 9:
                return (((float) this.Ascender) * fontSize) / 1000.0f;
            case 2:
                return (((float) this.CapHeight) * fontSize) / 1000.0f;
            case 3:
            case 10:
                return (((float) this.Descender) * fontSize) / 1000.0f;
            case 4:
                return this.ItalicAngle;
            case 5:
                return (((float) this.llx) * fontSize) / 1000.0f;
            case 6:
                return (((float) this.lly) * fontSize) / 1000.0f;
            case 7:
                return (((float) this.urx) * fontSize) / 1000.0f;
            case 8:
                return (((float) this.ury) * fontSize) / 1000.0f;
            case 12:
                return (((float) (this.urx - this.llx)) * fontSize) / 1000.0f;
            case 13:
                return (((float) this.UnderlinePosition) * fontSize) / 1000.0f;
            case 14:
                return (((float) this.UnderlineThickness) * fontSize) / 1000.0f;
            default:
                return 0.0f;
        }
    }

    public void setFontDescriptor(int key, float value) {
        switch (key) {
            case 1:
            case 9:
                this.Ascender = (int) value;
                return;
            case 3:
            case 10:
                this.Descender = (int) value;
                return;
            default:
                return;
        }
    }

    public String getPostscriptFontName() {
        return this.FontName;
    }

    public String[][] getFullFontName() {
        String[][] strArr = new String[1][];
        strArr[0] = new String[]{"", "", "", this.FullName};
        return strArr;
    }

    public String[][] getAllNameEntries() {
        String[][] strArr = new String[1][];
        strArr[0] = new String[]{"4", "", "", "", this.FullName};
        return strArr;
    }

    public String[][] getFamilyFontName() {
        String[][] strArr = new String[1][];
        strArr[0] = new String[]{"", "", "", this.FamilyName};
        return strArr;
    }

    public boolean hasKernPairs() {
        return !this.KernPairs.isEmpty();
    }

    public void setPostscriptFontName(String name) {
        this.FontName = name;
    }

    public boolean setKerning(int char1, int char2, int kern) {
        String first = GlyphList.unicodeToName(char1);
        if (first == null) {
            return false;
        }
        String second = GlyphList.unicodeToName(char2);
        if (second == null) {
            return false;
        }
        Object[] obj = (Object[]) this.KernPairs.get(first);
        if (obj == null) {
            this.KernPairs.put(first, new Object[]{second, Integer.valueOf(kern)});
            return true;
        }
        for (int k = 0; k < obj.length; k += 2) {
            if (second.equals(obj[k])) {
                obj[k + 1] = Integer.valueOf(kern);
                return true;
            }
        }
        int size = obj.length;
        Object[] obj2 = new Object[(size + 2)];
        System.arraycopy(obj, 0, obj2, 0, size);
        obj2[size] = second;
        obj2[size + 1] = Integer.valueOf(kern);
        this.KernPairs.put(first, obj2);
        return true;
    }

    protected int[] getRawCharBBox(int c, String name) {
        Object[] metrics;
        if (name == null) {
            metrics = (Object[]) this.CharMetrics.get(Integer.valueOf(c));
        } else if (name.equals(BaseFont.notdef)) {
            return null;
        } else {
            metrics = (Object[]) this.CharMetrics.get(name);
        }
        if (metrics != null) {
            return (int[]) metrics[3];
        }
        return null;
    }
}
