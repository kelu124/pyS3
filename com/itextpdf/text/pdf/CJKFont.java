package com.itextpdf.text.pdf;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.Utilities;
import com.itextpdf.text.error_messages.MessageLocalization;
import com.itextpdf.text.io.StreamUtil;
import com.itextpdf.text.pdf.fonts.cmaps.CMapCache;
import com.itextpdf.text.pdf.fonts.cmaps.CMapCidByte;
import com.itextpdf.text.pdf.fonts.cmaps.CMapCidUni;
import com.itextpdf.text.pdf.fonts.cmaps.CMapUniCid;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;
import org.bytedeco.javacpp.avutil;

class CJKFont extends BaseFont {
    private static final int BRACKET = 1;
    static final String CJK_ENCODING = "UnicodeBigUnmarked";
    private static final int FIRST = 0;
    public static final String RESOURCE_PATH_CMAP = "com/itextpdf/text/pdf/fonts/cmaps/";
    private static final int SERIAL = 2;
    private static final int V1Y = 880;
    private static final HashMap<String, HashMap<String, Object>> allFonts = new HashMap();
    static Properties cjkEncodings = new Properties();
    static Properties cjkFonts = new Properties();
    private static boolean propertiesLoaded = false;
    private static final HashMap<String, Set<String>> registryNames = new HashMap();
    private String CMap;
    private CMapCidByte cidByte;
    private boolean cidDirect = false;
    private CMapCidUni cidUni;
    private HashMap<String, Object> fontDesc;
    private String fontName;
    private IntHashtable hMetrics;
    private String style = "";
    private CMapUniCid uniCid;
    private String uniMap;
    private IntHashtable vMetrics;

    private static void loadProperties() {
        if (!propertiesLoaded) {
            synchronized (allFonts) {
                if (propertiesLoaded) {
                    return;
                }
                try {
                    loadRegistry();
                    for (String font : (Set) registryNames.get("fonts")) {
                        allFonts.put(font, readFontProperties(font));
                    }
                } catch (Exception e) {
                }
                propertiesLoaded = true;
            }
        }
    }

    private static void loadRegistry() throws IOException {
        InputStream is = StreamUtil.getResourceStream("com/itextpdf/text/pdf/fonts/cmaps/cjk_registry.properties");
        Properties p = new Properties();
        p.load(is);
        is.close();
        for (Object key : p.keySet()) {
            String[] sp = p.getProperty((String) key).split(" ");
            Set<String> hs = new HashSet();
            for (String s : sp) {
                if (s.length() > 0) {
                    hs.add(s);
                }
            }
            registryNames.put((String) key, hs);
        }
    }

    CJKFont(String fontName, String enc, boolean emb) throws DocumentException {
        loadProperties();
        this.fontType = 2;
        String nameBase = BaseFont.getBaseName(fontName);
        if (isCJKFont(nameBase, enc)) {
            if (nameBase.length() < fontName.length()) {
                this.style = fontName.substring(nameBase.length());
                fontName = nameBase;
            }
            this.fontName = fontName;
            this.encoding = CJK_ENCODING;
            this.vertical = enc.endsWith("V");
            this.CMap = enc;
            if (enc.equals(BaseFont.IDENTITY_H) || enc.equals(BaseFont.IDENTITY_V)) {
                this.cidDirect = true;
            }
            loadCMaps();
            return;
        }
        throw new DocumentException(MessageLocalization.getComposedMessage("font.1.with.2.encoding.is.not.a.cjk.font", fontName, enc));
    }

    String getUniMap() {
        return this.uniMap;
    }

    private void loadCMaps() throws DocumentException {
        try {
            this.fontDesc = (HashMap) allFonts.get(this.fontName);
            this.hMetrics = (IntHashtable) this.fontDesc.get("W");
            this.vMetrics = (IntHashtable) this.fontDesc.get("W2");
            String registry = (String) this.fontDesc.get("Registry");
            this.uniMap = "";
            for (String name : (Set) registryNames.get(registry + "_Uni")) {
                this.uniMap = name;
                if (!name.endsWith("V") || !this.vertical) {
                    if (!name.endsWith("V") && !this.vertical) {
                        break;
                    }
                } else {
                    break;
                }
            }
            if (this.cidDirect) {
                this.cidUni = CMapCache.getCachedCMapCidUni(this.uniMap);
                return;
            }
            this.uniCid = CMapCache.getCachedCMapUniCid(this.uniMap);
            this.cidByte = CMapCache.getCachedCMapCidByte(this.CMap);
        } catch (Exception ex) {
            throw new DocumentException(ex);
        }
    }

    public static String GetCompatibleFont(String enc) {
        loadProperties();
        for (Entry<String, Set<String>> e : registryNames.entrySet()) {
            if (((Set) e.getValue()).contains(enc)) {
                String registry = (String) e.getKey();
                for (Entry<String, HashMap<String, Object>> e1 : allFonts.entrySet()) {
                    if (registry.equals(((HashMap) e1.getValue()).get("Registry"))) {
                        return (String) e1.getKey();
                    }
                }
                continue;
            }
        }
        return null;
    }

    public static boolean isCJKFont(String fontName, String enc) {
        loadProperties();
        if (!registryNames.containsKey("fonts") || !((Set) registryNames.get("fonts")).contains(fontName)) {
            return false;
        }
        if (enc.equals(BaseFont.IDENTITY_H) || enc.equals(BaseFont.IDENTITY_V)) {
            return true;
        }
        Set<String> encodings = (Set) registryNames.get((String) ((HashMap) allFonts.get(fontName)).get("Registry"));
        boolean z = encodings != null && encodings.contains(enc);
        return z;
    }

    public int getWidth(int char1) {
        int v;
        int c = char1;
        if (!this.cidDirect) {
            c = this.uniCid.lookup(char1);
        }
        if (this.vertical) {
            v = this.vMetrics.get(c);
        } else {
            v = this.hMetrics.get(c);
        }
        return v > 0 ? v : 1000;
    }

    public int getWidth(String text) {
        int total = 0;
        int k;
        if (this.cidDirect) {
            for (k = 0; k < text.length(); k++) {
                total += getWidth(text.charAt(k));
            }
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
        }
        return total;
    }

    int getRawWidth(int c, String name) {
        return 0;
    }

    public int getKerning(int char1, int char2) {
        return 0;
    }

    private PdfDictionary getFontDescriptor() {
        PdfDictionary dic = new PdfDictionary(PdfName.FONTDESCRIPTOR);
        dic.put(PdfName.ASCENT, new PdfLiteral((String) this.fontDesc.get("Ascent")));
        dic.put(PdfName.CAPHEIGHT, new PdfLiteral((String) this.fontDesc.get("CapHeight")));
        dic.put(PdfName.DESCENT, new PdfLiteral((String) this.fontDesc.get("Descent")));
        dic.put(PdfName.FLAGS, new PdfLiteral((String) this.fontDesc.get("Flags")));
        dic.put(PdfName.FONTBBOX, new PdfLiteral((String) this.fontDesc.get("FontBBox")));
        dic.put(PdfName.FONTNAME, new PdfName(this.fontName + this.style));
        dic.put(PdfName.ITALICANGLE, new PdfLiteral((String) this.fontDesc.get("ItalicAngle")));
        dic.put(PdfName.STEMV, new PdfLiteral((String) this.fontDesc.get("StemV")));
        PdfDictionary pdic = new PdfDictionary();
        pdic.put(PdfName.PANOSE, new PdfString((String) this.fontDesc.get("Panose"), null));
        dic.put(PdfName.STYLE, pdic);
        return dic;
    }

    private PdfDictionary getCIDFont(PdfIndirectReference fontDescriptor, IntHashtable cjkTag) {
        PdfDictionary dic = new PdfDictionary(PdfName.FONT);
        dic.put(PdfName.SUBTYPE, PdfName.CIDFONTTYPE0);
        dic.put(PdfName.BASEFONT, new PdfName(this.fontName + this.style));
        dic.put(PdfName.FONTDESCRIPTOR, fontDescriptor);
        int[] keys = cjkTag.toOrderedKeys();
        String w = convertToHCIDMetrics(keys, this.hMetrics);
        if (w != null) {
            dic.put(PdfName.f137W, new PdfLiteral(w));
        }
        if (this.vertical) {
            w = convertToVCIDMetrics(keys, this.vMetrics, this.hMetrics);
            if (w != null) {
                dic.put(PdfName.W2, new PdfLiteral(w));
            }
        } else {
            dic.put(PdfName.DW, new PdfNumber(1000));
        }
        PdfDictionary cdic = new PdfDictionary();
        if (this.cidDirect) {
            cdic.put(PdfName.REGISTRY, new PdfString(this.cidUni.getRegistry(), null));
            cdic.put(PdfName.ORDERING, new PdfString(this.cidUni.getOrdering(), null));
            cdic.put(PdfName.SUPPLEMENT, new PdfNumber(this.cidUni.getSupplement()));
        } else {
            cdic.put(PdfName.REGISTRY, new PdfString(this.cidByte.getRegistry(), null));
            cdic.put(PdfName.ORDERING, new PdfString(this.cidByte.getOrdering(), null));
            cdic.put(PdfName.SUPPLEMENT, new PdfNumber(this.cidByte.getSupplement()));
        }
        dic.put(PdfName.CIDSYSTEMINFO, cdic);
        return dic;
    }

    private PdfDictionary getFontBaseType(PdfIndirectReference CIDFont) {
        PdfDictionary dic = new PdfDictionary(PdfName.FONT);
        dic.put(PdfName.SUBTYPE, PdfName.TYPE0);
        String name = this.fontName;
        if (this.style.length() > 0) {
            name = name + "-" + this.style.substring(1);
        }
        dic.put(PdfName.BASEFONT, new PdfName(name + "-" + this.CMap));
        dic.put(PdfName.ENCODING, new PdfName(this.CMap));
        dic.put(PdfName.DESCENDANTFONTS, new PdfArray(CIDFont));
        return dic;
    }

    void writeFont(PdfWriter writer, PdfIndirectReference ref, Object[] params) throws DocumentException, IOException {
        IntHashtable cjkTag = params[0];
        PdfIndirectReference ind_font = null;
        PdfObject pobj = getFontDescriptor();
        if (pobj != null) {
            ind_font = writer.addToBody(pobj).getIndirectReference();
        }
        pobj = getCIDFont(ind_font, cjkTag);
        if (pobj != null) {
            ind_font = writer.addToBody(pobj).getIndirectReference();
        }
        writer.addToBody(getFontBaseType(ind_font), ref);
    }

    public PdfStream getFullFontStream() {
        return null;
    }

    private float getDescNumber(String name) {
        return (float) Integer.parseInt((String) this.fontDesc.get(name));
    }

    private float getBBox(int idx) {
        StringTokenizer tk = new StringTokenizer((String) this.fontDesc.get("FontBBox"), " []\r\n\t\f");
        String ret = tk.nextToken();
        for (int k = 0; k < idx; k++) {
            ret = tk.nextToken();
        }
        return (float) Integer.parseInt(ret);
    }

    public float getFontDescriptor(int key, float fontSize) {
        switch (key) {
            case 1:
            case 9:
                return (getDescNumber("Ascent") * fontSize) / 1000.0f;
            case 2:
                return (getDescNumber("CapHeight") * fontSize) / 1000.0f;
            case 3:
            case 10:
                return (getDescNumber("Descent") * fontSize) / 1000.0f;
            case 4:
                return getDescNumber("ItalicAngle");
            case 5:
                return (getBBox(0) * fontSize) / 1000.0f;
            case 6:
                return (getBBox(1) * fontSize) / 1000.0f;
            case 7:
                return (getBBox(2) * fontSize) / 1000.0f;
            case 8:
                return (getBBox(3) * fontSize) / 1000.0f;
            case 12:
                return ((getBBox(2) - getBBox(0)) * fontSize) / 1000.0f;
            default:
                return 0.0f;
        }
    }

    public String getPostscriptFontName() {
        return this.fontName;
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

    public String[][] getFamilyFontName() {
        return getFullFontName();
    }

    static IntHashtable createMetric(String s) {
        IntHashtable h = new IntHashtable();
        StringTokenizer tk = new StringTokenizer(s);
        while (tk.hasMoreTokens()) {
            h.put(Integer.parseInt(tk.nextToken()), Integer.parseInt(tk.nextToken()));
        }
        return h;
    }

    static String convertToHCIDMetrics(int[] keys, IntHashtable h) {
        if (keys.length == 0) {
            return null;
        }
        int lastCid = 0;
        int lastValue = 0;
        int start = 0;
        while (start < keys.length) {
            lastCid = keys[start];
            lastValue = h.get(lastCid);
            if (lastValue != 0) {
                start++;
                break;
            }
            start++;
        }
        if (lastValue == 0) {
            return null;
        }
        StringBuilder buf = new StringBuilder();
        buf.append('[');
        buf.append(lastCid);
        int state = 0;
        for (int k = start; k < keys.length; k++) {
            int cid = keys[k];
            int value = h.get(cid);
            if (value != 0) {
                switch (state) {
                    case 0:
                        if (cid != lastCid + 1 || value != lastValue) {
                            if (cid != lastCid + 1) {
                                buf.append('[').append(lastValue).append(']').append(cid);
                                break;
                            }
                            state = 1;
                            buf.append('[').append(lastValue);
                            break;
                        }
                        state = 2;
                        break;
                        break;
                    case 1:
                        if (cid != lastCid + 1 || value != lastValue) {
                            if (cid != lastCid + 1) {
                                state = 0;
                                buf.append(' ').append(lastValue).append(']').append(cid);
                                break;
                            }
                            buf.append(' ').append(lastValue);
                            break;
                        }
                        state = 2;
                        buf.append(']').append(lastCid);
                        break;
                        break;
                    case 2:
                        if (!(cid == lastCid + 1 && value == lastValue)) {
                            buf.append(' ').append(lastCid).append(' ').append(lastValue).append(' ').append(cid);
                            state = 0;
                            break;
                        }
                }
                lastValue = value;
                lastCid = cid;
            }
        }
        switch (state) {
            case 0:
                buf.append('[').append(lastValue).append("]]");
                break;
            case 1:
                buf.append(' ').append(lastValue).append("]]");
                break;
            case 2:
                buf.append(' ').append(lastCid).append(' ').append(lastValue).append(']');
                break;
        }
        return buf.toString();
    }

    static String convertToVCIDMetrics(int[] keys, IntHashtable v, IntHashtable h) {
        if (keys.length == 0) {
            return null;
        }
        int lastCid = 0;
        int lastValue = 0;
        int lastHValue = 0;
        int start = 0;
        while (start < keys.length) {
            lastCid = keys[start];
            lastValue = v.get(lastCid);
            if (lastValue != 0) {
                start++;
                break;
            }
            lastHValue = h.get(lastCid);
            start++;
        }
        if (lastValue == 0) {
            return null;
        }
        if (lastHValue == 0) {
            lastHValue = 1000;
        }
        StringBuilder buf = new StringBuilder();
        buf.append('[');
        buf.append(lastCid);
        int state = 0;
        for (int k = start; k < keys.length; k++) {
            int cid = keys[k];
            int value = v.get(cid);
            if (value != 0) {
                int hValue = h.get(lastCid);
                if (hValue == 0) {
                    hValue = 1000;
                }
                switch (state) {
                    case 0:
                        if (cid != lastCid + 1 || value != lastValue || hValue != lastHValue) {
                            buf.append(' ').append(lastCid).append(' ').append(-lastValue).append(' ').append(lastHValue / 2).append(' ').append(V1Y).append(' ').append(cid);
                            break;
                        }
                        state = 2;
                        break;
                        break;
                    case 2:
                        if (!(cid == lastCid + 1 && value == lastValue && hValue == lastHValue)) {
                            buf.append(' ').append(lastCid).append(' ').append(-lastValue).append(' ').append(lastHValue / 2).append(' ').append(V1Y).append(' ').append(cid);
                            state = 0;
                            break;
                        }
                }
                lastValue = value;
                lastCid = cid;
                lastHValue = hValue;
            }
        }
        buf.append(' ').append(lastCid).append(' ').append(-lastValue).append(' ').append(lastHValue / 2).append(' ').append(V1Y).append(" ]");
        return buf.toString();
    }

    private static HashMap<String, Object> readFontProperties(String name) throws IOException {
        InputStream is = StreamUtil.getResourceStream(RESOURCE_PATH_CMAP + (name + ".properties"));
        Properties p = new Properties();
        p.load(is);
        is.close();
        IntHashtable W = createMetric(p.getProperty("W"));
        p.remove("W");
        IntHashtable W2 = createMetric(p.getProperty("W2"));
        p.remove("W2");
        HashMap<String, Object> map = new HashMap();
        Enumeration<Object> e = p.keys();
        while (e.hasMoreElements()) {
            Object obj = e.nextElement();
            map.put((String) obj, p.getProperty((String) obj));
        }
        map.put("W", W);
        map.put("W2", W2);
        return map;
    }

    public int getUnicodeEquivalent(int c) {
        if (!this.cidDirect) {
            return c;
        }
        if (c == avutil.FF_LAMBDA_MAX) {
            return 10;
        }
        return this.cidUni.lookup(c);
    }

    public int getCidCode(int c) {
        return this.cidDirect ? c : this.uniCid.lookup(c);
    }

    public boolean hasKernPairs() {
        return false;
    }

    public boolean charExists(int c) {
        if (!this.cidDirect && this.cidByte.lookup(this.uniCid.lookup(c)).length <= 0) {
            return false;
        }
        return true;
    }

    public boolean setCharAdvance(int c, int advance) {
        return false;
    }

    public void setPostscriptFontName(String name) {
        this.fontName = name;
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

    public byte[] convertToBytes(String text) {
        if (this.cidDirect) {
            return super.convertToBytes(text);
        }
        try {
            if (text.length() == 1) {
                return convertToBytes(text.charAt(0));
            }
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            int k = 0;
            while (k < text.length()) {
                int val;
                if (Utilities.isSurrogatePair(text, k)) {
                    val = Utilities.convertToUtf32(text, k);
                    k++;
                } else {
                    val = text.charAt(k);
                }
                bout.write(convertToBytes(val));
                k++;
            }
            return bout.toByteArray();
        } catch (Exception ex) {
            throw new ExceptionConverter(ex);
        }
    }

    byte[] convertToBytes(int char1) {
        if (this.cidDirect) {
            return super.convertToBytes(char1);
        }
        return this.cidByte.lookup(this.uniCid.lookup(char1));
    }

    public boolean isIdentity() {
        return this.cidDirect;
    }
}
