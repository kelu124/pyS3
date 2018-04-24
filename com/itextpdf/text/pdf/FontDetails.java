package com.itextpdf.text.pdf;

import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.Utilities;
import com.itextpdf.text.pdf.fonts.otf.Language;
import com.itextpdf.text.pdf.languages.BanglaGlyphRepositioner;
import com.itextpdf.text.pdf.languages.GlyphRepositioner;
import com.itextpdf.text.pdf.languages.IndicCompositeCharacterComparator;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

class FontDetails {
    BaseFont baseFont;
    CJKFont cjkFont;
    IntHashtable cjkTag;
    PdfName fontName;
    int fontType;
    PdfIndirectReference indirectReference;
    HashMap<Integer, int[]> longTag;
    byte[] shortTag;
    protected boolean subset = true;
    boolean symbolic;
    TrueTypeFontUnicode ttu;

    FontDetails(PdfName fontName, PdfIndirectReference indirectReference, BaseFont baseFont) {
        this.fontName = fontName;
        this.indirectReference = indirectReference;
        this.baseFont = baseFont;
        this.fontType = baseFont.getFontType();
        switch (this.fontType) {
            case 0:
            case 1:
                this.shortTag = new byte[256];
                return;
            case 2:
                this.cjkTag = new IntHashtable();
                this.cjkFont = (CJKFont) baseFont;
                return;
            case 3:
                this.longTag = new HashMap();
                this.ttu = (TrueTypeFontUnicode) baseFont;
                this.symbolic = baseFont.isFontSpecific();
                return;
            default:
                return;
        }
    }

    PdfIndirectReference getIndirectReference() {
        return this.indirectReference;
    }

    PdfName getFontName() {
        return this.fontName;
    }

    BaseFont getBaseFont() {
        return this.baseFont;
    }

    Object[] convertToBytesGid(String gids) {
        if (this.fontType != 3) {
            throw new IllegalArgumentException("GID require TT Unicode");
        }
        try {
            StringBuilder sb = new StringBuilder();
            int totalWidth = 0;
            for (char gid : gids.toCharArray()) {
                totalWidth += this.ttu.getGlyphWidth(gid);
                int vchar = this.ttu.GetCharFromGlyphId(gid);
                if (vchar != 0) {
                    sb.append(Utilities.convertFromUtf32(vchar));
                }
                Integer gl = Integer.valueOf(gid);
                if (!this.longTag.containsKey(gl)) {
                    this.longTag.put(gl, new int[]{gid, width, vchar});
                }
            }
            return new Object[]{gids.getBytes("UnicodeBigUnmarked"), sb.toString(), Integer.valueOf(totalWidth)};
        } catch (Exception e) {
            throw new ExceptionConverter(e);
        }
    }

    byte[] convertToBytes(String text) {
        byte[] b = null;
        int k;
        int len;
        int val;
        switch (this.fontType) {
            case 0:
            case 1:
                b = this.baseFont.convertToBytes(text);
                for (byte b2 : b) {
                    this.shortTag[b2 & 255] = (byte) 1;
                }
                break;
            case 2:
                len = text.length();
                if (this.cjkFont.isIdentity()) {
                    for (k = 0; k < len; k++) {
                        this.cjkTag.put(text.charAt(k), 0);
                    }
                } else {
                    k = 0;
                    while (k < len) {
                        if (Utilities.isSurrogatePair(text, k)) {
                            val = Utilities.convertToUtf32(text, k);
                            k++;
                        } else {
                            val = text.charAt(k);
                        }
                        this.cjkTag.put(this.cjkFont.getCidCode(val), 0);
                        k++;
                    }
                }
                b = this.cjkFont.convertToBytes(text);
                break;
            case 3:
                try {
                    int i;
                    len = text.length();
                    char[] glyph = new char[len];
                    int i2;
                    int[] metrics;
                    if (this.symbolic) {
                        b = PdfEncodings.convertToBytes(text, "symboltt");
                        len = b.length;
                        k = 0;
                        i2 = 0;
                        while (k < len) {
                            metrics = this.ttu.getMetricsTT(b[k] & 255);
                            if (metrics == null) {
                                i = i2;
                            } else {
                                this.longTag.put(Integer.valueOf(metrics[0]), new int[]{metrics[0], metrics[1], this.ttu.getUnicodeDifferences(b[k] & 255)});
                                i = i2 + 1;
                                glyph[i2] = (char) metrics[0];
                            }
                            k++;
                            i2 = i;
                        }
                        i = i2;
                    } else if (canApplyGlyphSubstitution()) {
                        return convertToBytesAfterGlyphSubstitution(text);
                    } else {
                        k = 0;
                        i2 = 0;
                        while (k < len) {
                            if (Utilities.isSurrogatePair(text, k)) {
                                val = Utilities.convertToUtf32(text, k);
                                k++;
                            } else {
                                val = text.charAt(k);
                            }
                            metrics = this.ttu.getMetricsTT(val);
                            if (metrics == null) {
                                i = i2;
                            } else {
                                int m0 = metrics[0];
                                Integer gl = Integer.valueOf(m0);
                                if (!this.longTag.containsKey(gl)) {
                                    this.longTag.put(gl, new int[]{m0, metrics[1], val});
                                }
                                i = i2 + 1;
                                glyph[i2] = (char) m0;
                            }
                            k++;
                            i2 = i;
                        }
                        i = i2;
                    }
                    b = new String(glyph, 0, i).getBytes("UnicodeBigUnmarked");
                    break;
                } catch (UnsupportedEncodingException e) {
                    throw new ExceptionConverter(e);
                }
            case 4:
                b = this.baseFont.convertToBytes(text);
                break;
            case 5:
                return this.baseFont.convertToBytes(text);
        }
        return b;
    }

    private boolean canApplyGlyphSubstitution() {
        return this.fontType == 3 && this.ttu.getGlyphSubstitutionMap() != null;
    }

    private byte[] convertToBytesAfterGlyphSubstitution(String text) throws UnsupportedEncodingException {
        if (canApplyGlyphSubstitution()) {
            Map<String, Glyph> glyphSubstitutionMap = this.ttu.getGlyphSubstitutionMap();
            Set<String> compositeCharacters = new TreeSet(new IndicCompositeCharacterComparator());
            compositeCharacters.addAll(glyphSubstitutionMap.keySet());
            String[] tokens = new ArrayBasedStringTokenizer((String[]) compositeCharacters.toArray(new String[0])).tokenize(text);
            List<Glyph> glyphList = new ArrayList(50);
            for (String token : tokens) {
                Glyph subsGlyph = (Glyph) glyphSubstitutionMap.get(token);
                if (subsGlyph != null) {
                    glyphList.add(subsGlyph);
                } else {
                    for (char c : token.toCharArray()) {
                        int[] metrics = this.ttu.getMetricsTT(c);
                        glyphList.add(new Glyph(metrics[0], metrics[1], String.valueOf(c)));
                    }
                }
            }
            GlyphRepositioner glyphRepositioner = getGlyphRepositioner();
            if (glyphRepositioner != null) {
                glyphRepositioner.repositionGlyphs(glyphList);
            }
            char[] charEncodedGlyphCodes = new char[glyphList.size()];
            for (int i = 0; i < glyphList.size(); i++) {
                Glyph glyph = (Glyph) glyphList.get(i);
                charEncodedGlyphCodes[i] = (char) glyph.code;
                Integer glyphCode = Integer.valueOf(glyph.code);
                if (!this.longTag.containsKey(glyphCode)) {
                    this.longTag.put(glyphCode, new int[]{glyph.code, glyph.width, glyph.chars.charAt(0)});
                }
            }
            return new String(charEncodedGlyphCodes).getBytes("UnicodeBigUnmarked");
        }
        throw new IllegalArgumentException("Make sure the font type if TTF Unicode and a valid GlyphSubstitutionTable exists!");
    }

    private GlyphRepositioner getGlyphRepositioner() {
        Language language = this.ttu.getSupportedLanguage();
        if (language == null) {
            throw new IllegalArgumentException("The supported language field cannot be null in " + this.ttu.getClass().getName());
        }
        switch (language) {
            case BENGALI:
                return new BanglaGlyphRepositioner(Collections.unmodifiableMap(this.ttu.cmap31), this.ttu.getGlyphSubstitutionMap());
            default:
                return null;
        }
    }

    public void writeFont(PdfWriter writer) {
        try {
            switch (this.fontType) {
                case 0:
                case 1:
                    int lastChar;
                    int firstChar = 0;
                    while (firstChar < 256) {
                        if (this.shortTag[firstChar] != (byte) 0) {
                            lastChar = 255;
                            while (lastChar >= firstChar && this.shortTag[lastChar] == (byte) 0) {
                                lastChar--;
                            }
                            if (firstChar > 255) {
                                firstChar = 255;
                                lastChar = 255;
                            }
                            this.baseFont.writeFont(writer, this.indirectReference, new Object[]{Integer.valueOf(firstChar), Integer.valueOf(lastChar), this.shortTag, Boolean.valueOf(this.subset)});
                            return;
                        }
                        firstChar++;
                    }
                    lastChar = 255;
                    while (lastChar >= firstChar) {
                        lastChar--;
                        break;
                    }
                    if (firstChar > 255) {
                        firstChar = 255;
                        lastChar = 255;
                    }
                    this.baseFont.writeFont(writer, this.indirectReference, new Object[]{Integer.valueOf(firstChar), Integer.valueOf(lastChar), this.shortTag, Boolean.valueOf(this.subset)});
                    return;
                case 2:
                    this.baseFont.writeFont(writer, this.indirectReference, new Object[]{this.cjkTag});
                    return;
                case 3:
                    this.baseFont.writeFont(writer, this.indirectReference, new Object[]{this.longTag, Boolean.valueOf(this.subset)});
                    return;
                case 5:
                    this.baseFont.writeFont(writer, this.indirectReference, null);
                    return;
                default:
                    return;
            }
        } catch (Exception e) {
            throw new ExceptionConverter(e);
        }
        throw new ExceptionConverter(e);
    }

    public boolean isSubset() {
        return this.subset;
    }

    public void setSubset(boolean subset) {
        this.subset = subset;
    }
}
