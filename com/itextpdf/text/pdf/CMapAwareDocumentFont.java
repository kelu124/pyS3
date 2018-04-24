package com.itextpdf.text.pdf;

import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.Utilities;
import com.itextpdf.text.error_messages.MessageLocalization;
import com.itextpdf.text.pdf.fonts.cmaps.CMapByteCid;
import com.itextpdf.text.pdf.fonts.cmaps.CMapCache;
import com.itextpdf.text.pdf.fonts.cmaps.CMapCidUni;
import com.itextpdf.text.pdf.fonts.cmaps.CMapParserEx;
import com.itextpdf.text.pdf.fonts.cmaps.CMapSequence;
import com.itextpdf.text.pdf.fonts.cmaps.CMapToUnicode;
import com.itextpdf.text.pdf.fonts.cmaps.CidLocationFromByte;
import com.itextpdf.text.pdf.fonts.cmaps.IdentityToUnicode;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

public class CMapAwareDocumentFont extends DocumentFont {
    private CMapByteCid byteCid;
    private CMapCidUni cidUni;
    private char[] cidbyte2uni;
    private PdfDictionary fontDic;
    private int spaceWidth;
    private CMapToUnicode toUnicodeCmap;
    private Map<Integer, Integer> uni2cid;

    public CMapAwareDocumentFont(PdfDictionary font) {
        super(font);
        this.fontDic = font;
        initFont();
    }

    public CMapAwareDocumentFont(PRIndirectReference refFont) {
        super(refFont);
        this.fontDic = (PdfDictionary) PdfReader.getPdfObjectRelease((PdfObject) refFont);
        initFont();
    }

    private void initFont() {
        processToUnicode();
        try {
            processUni2Byte();
            this.spaceWidth = super.getWidth(32);
            if (this.spaceWidth == 0) {
                this.spaceWidth = computeAverageWidth();
            }
            if (this.cjkEncoding != null) {
                this.byteCid = CMapCache.getCachedCMapByteCid(this.cjkEncoding);
                this.cidUni = CMapCache.getCachedCMapCidUni(this.uniMap);
            }
        } catch (Exception ex) {
            throw new ExceptionConverter(ex);
        }
    }

    private void processToUnicode() {
        PdfObject toUni = PdfReader.getPdfObjectRelease(this.fontDic.get(PdfName.TOUNICODE));
        if (toUni instanceof PRStream) {
            try {
                CidLocationFromByte lb = new CidLocationFromByte(PdfReader.getStreamBytes((PRStream) toUni));
                this.toUnicodeCmap = new CMapToUnicode();
                CMapParserEx.parseCid("", this.toUnicodeCmap, lb);
                this.uni2cid = this.toUnicodeCmap.createReverseMapping();
            } catch (IOException e) {
                this.toUnicodeCmap = null;
                this.uni2cid = null;
            }
        } else if (this.isType0) {
            try {
                PdfName encodingName = this.fontDic.getAsName(PdfName.ENCODING);
                if (encodingName != null && PdfName.decodeName(encodingName.toString()).equals(BaseFont.IDENTITY_H)) {
                    PdfDictionary cidinfo = ((PdfDictionary) PdfReader.getPdfObjectRelease(((PdfArray) PdfReader.getPdfObjectRelease(this.fontDic.get(PdfName.DESCENDANTFONTS))).getPdfObject(0))).getAsDict(PdfName.CIDSYSTEMINFO);
                    if (cidinfo != null) {
                        PdfString ordering = cidinfo.getAsString(PdfName.ORDERING);
                        if (ordering != null) {
                            CMapToUnicode touni = IdentityToUnicode.GetMapFromOrdering(ordering.toUnicodeString());
                            if (touni != null) {
                                this.toUnicodeCmap = touni;
                                this.uni2cid = this.toUnicodeCmap.createReverseMapping();
                            }
                        }
                    }
                }
            } catch (IOException e2) {
                this.toUnicodeCmap = null;
                this.uni2cid = null;
            }
        }
    }

    private void processUni2Byte() throws IOException {
        IntHashtable byte2uni = getByte2Uni();
        int[] e = byte2uni.toOrderedKeys();
        if (e.length != 0) {
            int k;
            this.cidbyte2uni = new char[256];
            for (int key : e) {
                this.cidbyte2uni[key] = (char) byte2uni.get(key);
            }
            if (this.toUnicodeCmap != null) {
                for (Entry<Integer, Integer> kv : this.toUnicodeCmap.createDirectMapping().entrySet()) {
                    if (((Integer) kv.getKey()).intValue() < 256) {
                        this.cidbyte2uni[((Integer) kv.getKey()).intValue()] = (char) ((Integer) kv.getValue()).intValue();
                    }
                }
            }
            IntHashtable diffmap = getDiffmap();
            if (diffmap != null) {
                e = diffmap.toOrderedKeys();
                for (k = 0; k < e.length; k++) {
                    int n = diffmap.get(e[k]);
                    if (n < 256) {
                        this.cidbyte2uni[n] = (char) e[k];
                    }
                }
            }
        }
    }

    private int computeAverageWidth() {
        int count = 0;
        int total = 0;
        for (int i = 0; i < this.widths.length; i++) {
            if (this.widths[i] != 0) {
                total += this.widths[i];
                count++;
            }
        }
        return count != 0 ? total / count : 0;
    }

    public int getWidth(int char1) {
        if (char1 == 32) {
            return this.spaceWidth != 0 ? this.spaceWidth : this.defaultWidth;
        } else {
            return super.getWidth(char1);
        }
    }

    private String decodeSingleCID(byte[] bytes, int offset, int len) {
        if (this.toUnicodeCmap != null) {
            if (offset + len > bytes.length) {
                throw new ArrayIndexOutOfBoundsException(MessageLocalization.getComposedMessage("invalid.index.1", offset + len));
            }
            String s = this.toUnicodeCmap.lookup(bytes, offset, len);
            if (s != null) {
                return s;
            }
            if (len != 1 || this.cidbyte2uni == null) {
                return null;
            }
        }
        if (len != 1) {
            throw new Error("Multi-byte glyphs not implemented yet");
        } else if (this.cidbyte2uni == null) {
            return "";
        } else {
            return new String(this.cidbyte2uni, bytes[offset] & 255, 1);
        }
    }

    public String decode(byte[] cidbytes, int offset, int len) {
        StringBuilder sb = new StringBuilder();
        if (this.toUnicodeCmap != null || this.byteCid == null) {
            int i = offset;
            while (i < offset + len) {
                String rslt = decodeSingleCID(cidbytes, i, 1);
                if (rslt == null && i < (offset + len) - 1) {
                    rslt = decodeSingleCID(cidbytes, i, 2);
                    i++;
                }
                if (rslt != null) {
                    sb.append(rslt);
                }
                i++;
            }
        } else {
            String cid = this.byteCid.decodeSequence(new CMapSequence(cidbytes, offset, len));
            for (int k = 0; k < cid.length(); k++) {
                int c = this.cidUni.lookup(cid.charAt(k));
                if (c > 0) {
                    sb.append(Utilities.convertFromUtf32(c));
                }
            }
        }
        return sb.toString();
    }

    public String encode(byte[] bytes, int offset, int len) {
        return decode(bytes, offset, len);
    }
}
