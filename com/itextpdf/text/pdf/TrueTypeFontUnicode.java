package com.itextpdf.text.pdf;

import android.support.v4.view.InputDeviceCompat;
import android.support.v4.view.MotionEventCompat;
import com.google.common.primitives.UnsignedBytes;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Utilities;
import com.itextpdf.text.error_messages.MessageLocalization;
import com.itextpdf.text.pdf.fonts.otf.GlyphSubstitutionTableReader;
import com.itextpdf.text.pdf.fonts.otf.Language;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.bytedeco.javacpp.avcodec;

class TrueTypeFontUnicode extends TrueTypeFont implements Comparator<int[]> {
    private static final List<Language> SUPPORTED_LANGUAGES_FOR_OTF = Arrays.asList(new Language[]{Language.BENGALI});
    private static final byte[] rotbits = new byte[]{UnsignedBytes.MAX_POWER_OF_TWO, (byte) 64, (byte) 32, (byte) 16, (byte) 8, (byte) 4, (byte) 2, (byte) 1};
    private Map<String, Glyph> glyphSubstitutionMap;
    private Language supportedLanguage;

    TrueTypeFontUnicode(String ttFile, String enc, boolean emb, byte[] ttfAfm, boolean forceRead) throws DocumentException, IOException {
        String nameBase = BaseFont.getBaseName(ttFile);
        String ttcName = TrueTypeFont.getTTCName(nameBase);
        if (nameBase.length() < ttFile.length()) {
            this.style = ttFile.substring(nameBase.length());
        }
        this.encoding = enc;
        this.embedded = emb;
        this.fileName = ttcName;
        this.ttcIndex = "";
        if (ttcName.length() < nameBase.length()) {
            this.ttcIndex = nameBase.substring(ttcName.length() + 1);
        }
        this.fontType = 3;
        if ((this.fileName.toLowerCase().endsWith(".ttf") || this.fileName.toLowerCase().endsWith(".otf") || this.fileName.toLowerCase().endsWith(".ttc")) && ((enc.equals(BaseFont.IDENTITY_H) || enc.equals(BaseFont.IDENTITY_V)) && emb)) {
            process(ttfAfm, forceRead);
            if (this.os_2.fsType == (short) 2) {
                throw new DocumentException(MessageLocalization.getComposedMessage("1.cannot.be.embedded.due.to.licensing.restrictions", this.fileName + this.style));
            }
            if ((this.cmap31 == null && !this.fontSpecific) || (this.cmap10 == null && this.fontSpecific)) {
                this.directTextToByte = true;
            }
            if (this.fontSpecific) {
                this.fontSpecific = false;
                String tempEncoding = this.encoding;
                this.encoding = "";
                createEncoding();
                this.encoding = tempEncoding;
                this.fontSpecific = true;
            }
            this.vertical = enc.endsWith("V");
            return;
        }
        throw new DocumentException(MessageLocalization.getComposedMessage("1.2.is.not.a.ttf.font.file", this.fileName, this.style));
    }

    void process(byte[] ttfAfm, boolean preload) throws DocumentException, IOException {
        super.process(ttfAfm, preload);
    }

    public int getWidth(int char1) {
        if (this.vertical) {
            return 1000;
        }
        if (!this.fontSpecific) {
            return getRawWidth(char1, this.encoding);
        }
        if ((char1 & MotionEventCompat.ACTION_POINTER_INDEX_MASK) == 0 || (char1 & MotionEventCompat.ACTION_POINTER_INDEX_MASK) == avcodec.MB_TYPE_L0L1) {
            return getRawWidth(char1 & 255, null);
        }
        return 0;
    }

    public int getWidth(String text) {
        if (this.vertical) {
            return text.length() * 1000;
        }
        int total = 0;
        int k;
        int len;
        if (this.fontSpecific) {
            for (char c : text.toCharArray()) {
                if ((c & MotionEventCompat.ACTION_POINTER_INDEX_MASK) == 0 || (c & MotionEventCompat.ACTION_POINTER_INDEX_MASK) == avcodec.MB_TYPE_L0L1) {
                    total += getRawWidth(c & 255, null);
                }
            }
            return total;
        }
        len = text.length();
        k = 0;
        while (k < len) {
            if (Utilities.isSurrogatePair(text, k)) {
                total += getRawWidth(Utilities.convertToUtf32(text, k), this.encoding);
                k++;
            } else {
                total += getRawWidth(text.charAt(k), this.encoding);
            }
            k++;
        }
        return total;
    }

    public PdfStream getToUnicode(Object[] metrics) {
        if (metrics.length == 0) {
            return null;
        }
        StringBuffer buf = new StringBuffer("/CIDInit /ProcSet findresource begin\n12 dict begin\nbegincmap\n/CIDSystemInfo\n<< /Registry (TTX+0)\n/Ordering (T42UV)\n/Supplement 0\n>> def\n/CMapName /TTX+0 def\n/CMapType 2 def\n1 begincodespacerange\n<0000><FFFF>\nendcodespacerange\n");
        int size = 0;
        for (int k = 0; k < metrics.length; k++) {
            if (size == 0) {
                if (k != 0) {
                    buf.append("endbfrange\n");
                }
                size = Math.min(100, metrics.length - k);
                buf.append(size).append(" beginbfrange\n");
            }
            size--;
            int[] metric = (int[]) metrics[k];
            String fromTo = toHex(metric[0]);
            buf.append(fromTo).append(fromTo).append(toHex(metric[2])).append('\n');
        }
        buf.append("endbfrange\nendcmap\nCMapName currentdict /CMap defineresource pop\nend end\n");
        PdfStream stream = new PdfStream(PdfEncodings.convertToBytes(buf.toString(), null));
        stream.flateCompress(this.compressionLevel);
        return stream;
    }

    private static String toHex4(int n) {
        String s = "0000" + Integer.toHexString(n);
        return s.substring(s.length() - 4);
    }

    static String toHex(int n) {
        if (n < 65536) {
            return "<" + toHex4(n) + ">";
        }
        n -= 65536;
        return "[<" + toHex4((n / 1024) + 55296) + toHex4((n % 1024) + 56320) + ">]";
    }

    public PdfDictionary getCIDFontType2(PdfIndirectReference fontDescriptor, String subsetPrefix, Object[] metrics) {
        PdfDictionary dic = new PdfDictionary(PdfName.FONT);
        if (this.cff) {
            dic.put(PdfName.SUBTYPE, PdfName.CIDFONTTYPE0);
            dic.put(PdfName.BASEFONT, new PdfName(subsetPrefix + this.fontName + "-" + this.encoding));
        } else {
            dic.put(PdfName.SUBTYPE, PdfName.CIDFONTTYPE2);
            dic.put(PdfName.BASEFONT, new PdfName(subsetPrefix + this.fontName));
        }
        dic.put(PdfName.FONTDESCRIPTOR, fontDescriptor);
        if (!this.cff) {
            dic.put(PdfName.CIDTOGIDMAP, PdfName.IDENTITY);
        }
        PdfDictionary cdic = new PdfDictionary();
        cdic.put(PdfName.REGISTRY, new PdfString("Adobe"));
        cdic.put(PdfName.ORDERING, new PdfString("Identity"));
        cdic.put(PdfName.SUPPLEMENT, new PdfNumber(0));
        dic.put(PdfName.CIDSYSTEMINFO, cdic);
        if (!this.vertical) {
            dic.put(PdfName.DW, new PdfNumber(1000));
            StringBuffer buf = new StringBuffer("[");
            int lastNumber = -10;
            boolean firstTime = true;
            for (Object obj : metrics) {
                int[] metric = (int[]) obj;
                if (metric[1] != 1000) {
                    int m = metric[0];
                    if (m == lastNumber + 1) {
                        buf.append(' ').append(metric[1]);
                    } else {
                        if (!firstTime) {
                            buf.append(']');
                        }
                        firstTime = false;
                        buf.append(m).append('[').append(metric[1]);
                    }
                    lastNumber = m;
                }
            }
            if (buf.length() > 1) {
                buf.append("]]");
                dic.put(PdfName.f137W, new PdfLiteral(buf.toString()));
            }
        }
        return dic;
    }

    public PdfDictionary getFontBaseType(PdfIndirectReference descendant, String subsetPrefix, PdfIndirectReference toUnicode) {
        PdfDictionary dic = new PdfDictionary(PdfName.FONT);
        dic.put(PdfName.SUBTYPE, PdfName.TYPE0);
        if (this.cff) {
            dic.put(PdfName.BASEFONT, new PdfName(subsetPrefix + this.fontName + "-" + this.encoding));
        } else {
            dic.put(PdfName.BASEFONT, new PdfName(subsetPrefix + this.fontName));
        }
        dic.put(PdfName.ENCODING, new PdfName(this.encoding));
        dic.put(PdfName.DESCENDANTFONTS, new PdfArray(descendant));
        if (toUnicode != null) {
            dic.put(PdfName.TOUNICODE, toUnicode);
        }
        return dic;
    }

    public int GetCharFromGlyphId(int gid) {
        if (this.glyphIdToChar == null) {
            int[] g2 = new int[this.maxGlyphId];
            HashMap<Integer, int[]> map = null;
            if (this.cmapExt != null) {
                map = this.cmapExt;
            } else if (this.cmap31 != null) {
                map = this.cmap31;
            }
            if (map != null) {
                for (Entry<Integer, int[]> entry : map.entrySet()) {
                    g2[((int[]) entry.getValue())[0]] = ((Integer) entry.getKey()).intValue();
                }
            }
            this.glyphIdToChar = g2;
        }
        return this.glyphIdToChar[gid];
    }

    public int compare(int[] o1, int[] o2) {
        int m1 = o1[0];
        int m2 = o2[0];
        if (m1 < m2) {
            return -1;
        }
        if (m1 != m2) {
            return 1;
        }
        return 0;
    }

    void writeFont(PdfWriter writer, PdfIndirectReference ref, Object[] params) throws DocumentException, IOException {
        writer.getTtfUnicodeWriter().writeFont(this, ref, params, rotbits);
    }

    public PdfStream getFullFontStream() throws IOException, DocumentException {
        if (this.cff) {
            return new StreamFont(readCffFont(), "CIDFontType0C", this.compressionLevel);
        }
        return super.getFullFontStream();
    }

    public byte[] convertToBytes(String text) {
        return null;
    }

    byte[] convertToBytes(int char1) {
        return null;
    }

    public int[] getMetricsTT(int c) {
        if (this.cmapExt != null) {
            return (int[]) this.cmapExt.get(Integer.valueOf(c));
        }
        HashMap<Integer, int[]> map;
        if (this.fontSpecific) {
            map = this.cmap10;
        } else {
            map = this.cmap31;
        }
        if (map == null) {
            return null;
        }
        if (!this.fontSpecific) {
            return (int[]) map.get(Integer.valueOf(c));
        }
        if ((c & InputDeviceCompat.SOURCE_ANY) == 0 || (c & InputDeviceCompat.SOURCE_ANY) == avcodec.MB_TYPE_L0L1) {
            return (int[]) map.get(Integer.valueOf(c & 255));
        }
        return null;
    }

    public boolean charExists(int c) {
        return getMetricsTT(c) != null;
    }

    public boolean setCharAdvance(int c, int advance) {
        int[] m = getMetricsTT(c);
        if (m == null) {
            return false;
        }
        m[1] = advance;
        return true;
    }

    public int[] getCharBBox(int c) {
        if (this.bboxes == null) {
            return null;
        }
        int[] m = getMetricsTT(c);
        if (m != null) {
            return this.bboxes[m[0]];
        }
        return null;
    }

    protected Map<String, Glyph> getGlyphSubstitutionMap() {
        return this.glyphSubstitutionMap;
    }

    Language getSupportedLanguage() {
        return this.supportedLanguage;
    }

    private void readGsubTable() throws IOException {
        if (this.tables.get("GSUB") != null) {
            Map<Integer, Character> glyphToCharacterMap = new HashMap(this.cmap31.size());
            for (Integer charCode : this.cmap31.keySet()) {
                glyphToCharacterMap.put(Integer.valueOf(((int[]) this.cmap31.get(charCode))[0]), Character.valueOf((char) charCode.intValue()));
            }
            GlyphSubstitutionTableReader gsubReader = new GlyphSubstitutionTableReader(this.fileName, ((int[]) this.tables.get("GSUB"))[0], glyphToCharacterMap, this.glyphWidthsByIndex);
            try {
                gsubReader.read();
                this.supportedLanguage = gsubReader.getSupportedLanguage();
                if (SUPPORTED_LANGUAGES_FOR_OTF.contains(this.supportedLanguage)) {
                    this.glyphSubstitutionMap = gsubReader.getGlyphSubstitutionMap();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
