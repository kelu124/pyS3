package com.itextpdf.text.pdf;

import com.google.common.base.Ascii;
import com.google.common.primitives.UnsignedBytes;
import com.itextpdf.text.error_messages.MessageLocalization;
import com.itextpdf.text.exceptions.UnsupportedPdfException;
import com.itextpdf.text.pdf.codec.TIFFFaxDecoder;
import com.itextpdf.text.pdf.codec.TIFFFaxDecompressor;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class FilterHandlers {
    private static final Map<PdfName, FilterHandler> defaults;

    public interface FilterHandler {
        byte[] decode(byte[] bArr, PdfName pdfName, PdfObject pdfObject, PdfDictionary pdfDictionary) throws IOException;
    }

    private static class Filter_ASCII85DECODE implements FilterHandler {
        private Filter_ASCII85DECODE() {
        }

        public byte[] decode(byte[] b, PdfName filterName, PdfObject decodeParams, PdfDictionary streamDictionary) throws IOException {
            return PdfReader.ASCII85Decode(b);
        }
    }

    private static class Filter_ASCIIHEXDECODE implements FilterHandler {
        private Filter_ASCIIHEXDECODE() {
        }

        public byte[] decode(byte[] b, PdfName filterName, PdfObject decodeParams, PdfDictionary streamDictionary) throws IOException {
            return PdfReader.ASCIIHexDecode(b);
        }
    }

    private static class Filter_CCITTFAXDECODE implements FilterHandler {
        private Filter_CCITTFAXDECODE() {
        }

        public byte[] decode(byte[] b, PdfName filterName, PdfObject decodeParams, PdfDictionary streamDictionary) throws IOException {
            PdfNumber wn = (PdfNumber) PdfReader.getPdfObjectRelease(streamDictionary.get(PdfName.WIDTH));
            PdfNumber hn = (PdfNumber) PdfReader.getPdfObjectRelease(streamDictionary.get(PdfName.HEIGHT));
            if (wn == null || hn == null) {
                throw new UnsupportedPdfException(MessageLocalization.getComposedMessage("filter.ccittfaxdecode.is.only.supported.for.images", new Object[0]));
            }
            int width = wn.intValue();
            int height = hn.intValue();
            PdfDictionary param = decodeParams instanceof PdfDictionary ? (PdfDictionary) decodeParams : null;
            int k = 0;
            boolean blackIs1 = false;
            boolean byteAlign = false;
            if (param != null) {
                PdfNumber kn = param.getAsNumber(PdfName.f125K);
                if (kn != null) {
                    k = kn.intValue();
                }
                PdfBoolean bo = param.getAsBoolean(PdfName.BLACKIS1);
                if (bo != null) {
                    blackIs1 = bo.booleanValue();
                }
                bo = param.getAsBoolean(PdfName.ENCODEDBYTEALIGN);
                if (bo != null) {
                    byteAlign = bo.booleanValue();
                }
            }
            byte[] outBuf = new byte[(((width + 7) / 8) * height)];
            TIFFFaxDecompressor decoder = new TIFFFaxDecompressor();
            if (k == 0 || k > 0) {
                int tiffT4Options = (k > 0 ? 1 : 0) | (byteAlign ? 4 : 0);
                decoder.SetOptions(1, 3, tiffT4Options, 0);
                decoder.decodeRaw(outBuf, b, width, height);
                if (decoder.fails > 0) {
                    byte[] outBuf2 = new byte[(((width + 7) / 8) * height)];
                    int oldFails = decoder.fails;
                    decoder.SetOptions(1, 2, tiffT4Options, 0);
                    decoder.decodeRaw(outBuf2, b, width, height);
                    if (decoder.fails < oldFails) {
                        outBuf = outBuf2;
                    }
                }
            } else {
                new TIFFFaxDecoder(1, width, height).decodeT6(outBuf, b, 0, height, 0);
            }
            if (!blackIs1) {
                int len = outBuf.length;
                for (int t = 0; t < len; t++) {
                    outBuf[t] = (byte) (outBuf[t] ^ 255);
                }
            }
            return outBuf;
        }
    }

    private static class Filter_DoNothing implements FilterHandler {
        private Filter_DoNothing() {
        }

        public byte[] decode(byte[] b, PdfName filterName, PdfObject decodeParams, PdfDictionary streamDictionary) throws IOException {
            return b;
        }
    }

    private static class Filter_FLATEDECODE implements FilterHandler {
        private Filter_FLATEDECODE() {
        }

        public byte[] decode(byte[] b, PdfName filterName, PdfObject decodeParams, PdfDictionary streamDictionary) throws IOException {
            return PdfReader.decodePredictor(PdfReader.FlateDecode(b), decodeParams);
        }
    }

    private static class Filter_LZWDECODE implements FilterHandler {
        private Filter_LZWDECODE() {
        }

        public byte[] decode(byte[] b, PdfName filterName, PdfObject decodeParams, PdfDictionary streamDictionary) throws IOException {
            return PdfReader.decodePredictor(PdfReader.LZWDecode(b), decodeParams);
        }
    }

    private static class Filter_RUNLENGTHDECODE implements FilterHandler {
        private Filter_RUNLENGTHDECODE() {
        }

        public byte[] decode(byte[] b, PdfName filterName, PdfObject decodeParams, PdfDictionary streamDictionary) throws IOException {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int i = 0;
            while (i < b.length) {
                byte dupCount = b[i];
                if (dupCount == UnsignedBytes.MAX_POWER_OF_TWO) {
                    break;
                }
                if (dupCount < (byte) 0 || dupCount > Ascii.DEL) {
                    i++;
                    for (int j = 0; j < 1 - dupCount; j++) {
                        baos.write(b[i]);
                    }
                } else {
                    int bytesToCopy = dupCount + 1;
                    baos.write(b, i, bytesToCopy);
                    i += bytesToCopy;
                }
                i++;
            }
            return baos.toByteArray();
        }
    }

    static {
        HashMap<PdfName, FilterHandler> map = new HashMap();
        map.put(PdfName.FLATEDECODE, new Filter_FLATEDECODE());
        map.put(PdfName.FL, new Filter_FLATEDECODE());
        map.put(PdfName.ASCIIHEXDECODE, new Filter_ASCIIHEXDECODE());
        map.put(PdfName.AHX, new Filter_ASCIIHEXDECODE());
        map.put(PdfName.ASCII85DECODE, new Filter_ASCII85DECODE());
        map.put(PdfName.A85, new Filter_ASCII85DECODE());
        map.put(PdfName.LZWDECODE, new Filter_LZWDECODE());
        map.put(PdfName.CCITTFAXDECODE, new Filter_CCITTFAXDECODE());
        map.put(PdfName.CRYPT, new Filter_DoNothing());
        map.put(PdfName.RUNLENGTHDECODE, new Filter_RUNLENGTHDECODE());
        defaults = Collections.unmodifiableMap(map);
    }

    public static Map<PdfName, FilterHandler> getDefaultFilterHandlers() {
        return defaults;
    }
}
