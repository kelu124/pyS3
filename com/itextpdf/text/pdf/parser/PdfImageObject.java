package com.itextpdf.text.pdf.parser;

import com.itextpdf.text.Version;
import com.itextpdf.text.error_messages.MessageLocalization;
import com.itextpdf.text.exceptions.UnsupportedPdfException;
import com.itextpdf.text.pdf.FilterHandlers;
import com.itextpdf.text.pdf.FilterHandlers.FilterHandler;
import com.itextpdf.text.pdf.PRStream;
import com.itextpdf.text.pdf.PdfArray;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfObject;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfString;
import com.itextpdf.text.pdf.codec.PngWriter;
import com.itextpdf.text.pdf.codec.TIFFConstants;
import com.itextpdf.text.pdf.codec.TiffWriter;
import com.itextpdf.text.pdf.codec.TiffWriter.FieldAscii;
import com.itextpdf.text.pdf.codec.TiffWriter.FieldImage;
import com.itextpdf.text.pdf.codec.TiffWriter.FieldLong;
import com.itextpdf.text.pdf.codec.TiffWriter.FieldRational;
import com.itextpdf.text.pdf.codec.TiffWriter.FieldShort;
import com.itextpdf.text.pdf.codec.TiffWriter.FieldUndefined;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PdfImageObject {
    private int bpc;
    private PdfDictionary colorSpaceDic;
    private PdfDictionary dictionary;
    private int height;
    private byte[] icc;
    private byte[] imageBytes;
    private byte[] palette;
    private int pngBitDepth;
    private int pngColorType;
    private ImageBytesType streamContentType;
    private int stride;
    private int width;

    public enum ImageBytesType {
        PNG("png"),
        JPG("jpg"),
        JP2("jp2"),
        CCITT("tif"),
        JBIG2("jbig2");
        
        private final String fileExtension;

        private ImageBytesType(String fileExtension) {
            this.fileExtension = fileExtension;
        }

        public String getFileExtension() {
            return this.fileExtension;
        }
    }

    private static class TrackingFilter implements FilterHandler {
        public PdfName lastFilterName;

        private TrackingFilter() {
            this.lastFilterName = null;
        }

        public byte[] decode(byte[] b, PdfName filterName, PdfObject decodeParams, PdfDictionary streamDictionary) throws IOException {
            this.lastFilterName = filterName;
            return b;
        }
    }

    public String getFileType() {
        return this.streamContentType.getFileExtension();
    }

    public ImageBytesType getImageBytesType() {
        return this.streamContentType;
    }

    public PdfImageObject(PRStream stream) throws IOException {
        this(stream, PdfReader.getStreamBytesRaw(stream), null);
    }

    public PdfImageObject(PRStream stream, PdfDictionary colorSpaceDic) throws IOException {
        this(stream, PdfReader.getStreamBytesRaw(stream), colorSpaceDic);
    }

    protected PdfImageObject(PdfDictionary dictionary, byte[] samples, PdfDictionary colorSpaceDic) throws IOException {
        this.pngColorType = -1;
        this.streamContentType = null;
        this.dictionary = dictionary;
        this.colorSpaceDic = colorSpaceDic;
        TrackingFilter trackingFilter = new TrackingFilter();
        Map<PdfName, FilterHandler> handlers = new HashMap(FilterHandlers.getDefaultFilterHandlers());
        handlers.put(PdfName.JBIG2DECODE, trackingFilter);
        handlers.put(PdfName.DCTDECODE, trackingFilter);
        handlers.put(PdfName.JPXDECODE, trackingFilter);
        this.imageBytes = PdfReader.decodeBytes(samples, dictionary, handlers);
        if (trackingFilter.lastFilterName == null) {
            decodeImageBytes();
        } else if (PdfName.JBIG2DECODE.equals(trackingFilter.lastFilterName)) {
            this.streamContentType = ImageBytesType.JBIG2;
        } else if (PdfName.DCTDECODE.equals(trackingFilter.lastFilterName)) {
            this.streamContentType = ImageBytesType.JPG;
        } else if (PdfName.JPXDECODE.equals(trackingFilter.lastFilterName)) {
            this.streamContentType = ImageBytesType.JP2;
        }
    }

    public PdfObject get(PdfName key) {
        return this.dictionary.get(key);
    }

    public PdfDictionary getDictionary() {
        return this.dictionary;
    }

    private void findColorspace(PdfObject colorspace, boolean allowIndexed) throws IOException {
        if (colorspace == null && this.bpc == 1) {
            this.stride = ((this.width * this.bpc) + 7) / 8;
            this.pngColorType = 0;
        } else if (PdfName.DEVICEGRAY.equals(colorspace)) {
            this.stride = ((this.width * this.bpc) + 7) / 8;
            this.pngColorType = 0;
        } else if (PdfName.DEVICERGB.equals(colorspace)) {
            if (this.bpc == 8 || this.bpc == 16) {
                this.stride = (((this.width * this.bpc) * 3) + 7) / 8;
                this.pngColorType = 2;
            }
        } else if (colorspace instanceof PdfArray) {
            PdfArray ca = (PdfArray) colorspace;
            PdfObject tyca = ca.getDirectObject(0);
            if (PdfName.CALGRAY.equals(tyca)) {
                this.stride = ((this.width * this.bpc) + 7) / 8;
                this.pngColorType = 0;
            } else if (PdfName.CALRGB.equals(tyca)) {
                if (this.bpc == 8 || this.bpc == 16) {
                    this.stride = (((this.width * this.bpc) * 3) + 7) / 8;
                    this.pngColorType = 2;
                }
            } else if (PdfName.ICCBASED.equals(tyca)) {
                PRStream pr = (PRStream) ca.getDirectObject(1);
                int n = pr.getAsNumber(PdfName.f128N).intValue();
                if (n == 1) {
                    this.stride = ((this.width * this.bpc) + 7) / 8;
                    this.pngColorType = 0;
                    this.icc = PdfReader.getStreamBytes(pr);
                } else if (n == 3) {
                    this.stride = (((this.width * this.bpc) * 3) + 7) / 8;
                    this.pngColorType = 2;
                    this.icc = PdfReader.getStreamBytes(pr);
                }
            } else if (allowIndexed && PdfName.INDEXED.equals(tyca)) {
                findColorspace(ca.getDirectObject(1), false);
                if (this.pngColorType == 2) {
                    PdfObject id2 = ca.getDirectObject(3);
                    if (id2 instanceof PdfString) {
                        this.palette = ((PdfString) id2).getBytes();
                    } else if (id2 instanceof PRStream) {
                        this.palette = PdfReader.getStreamBytes((PRStream) id2);
                    }
                    this.stride = ((this.width * this.bpc) + 7) / 8;
                    this.pngColorType = 3;
                }
            }
        }
    }

    private void decodeImageBytes() throws IOException {
        if (this.streamContentType != null) {
            throw new IllegalStateException(MessageLocalization.getComposedMessage("Decoding.can't.happen.on.this.type.of.stream.(.1.)", this.streamContentType));
        }
        this.pngColorType = -1;
        PdfArray decode = this.dictionary.getAsArray(PdfName.DECODE);
        this.width = this.dictionary.getAsNumber(PdfName.WIDTH).intValue();
        this.height = this.dictionary.getAsNumber(PdfName.HEIGHT).intValue();
        this.bpc = this.dictionary.getAsNumber(PdfName.BITSPERCOMPONENT).intValue();
        this.pngBitDepth = this.bpc;
        PdfObject colorspace = this.dictionary.getDirectObject(PdfName.COLORSPACE);
        if ((colorspace instanceof PdfName) && this.colorSpaceDic != null) {
            PdfObject csLookup = this.colorSpaceDic.getDirectObject((PdfName) colorspace);
            if (csLookup != null) {
                colorspace = csLookup;
            }
        }
        this.palette = null;
        this.icc = null;
        this.stride = 0;
        findColorspace(colorspace, true);
        ByteArrayOutputStream ms = new ByteArrayOutputStream();
        if (this.pngColorType >= 0) {
            PngWriter png = new PngWriter(ms);
            if (decode != null && this.pngBitDepth == 1 && decode.getAsNumber(0).intValue() == 1 && decode.getAsNumber(1).intValue() == 0) {
                int len = this.imageBytes.length;
                for (int t = 0; t < len; t++) {
                    byte[] bArr = this.imageBytes;
                    bArr[t] = (byte) (bArr[t] ^ 255);
                }
            }
            png.writeHeader(this.width, this.height, this.pngBitDepth, this.pngColorType);
            if (this.icc != null) {
                png.writeIccProfile(this.icc);
            }
            if (this.palette != null) {
                png.writePalette(this.palette);
            }
            png.writeData(this.imageBytes, this.stride);
            png.writeEnd();
            this.streamContentType = ImageBytesType.PNG;
            this.imageBytes = ms.toByteArray();
        } else if (this.bpc != 8) {
            throw new UnsupportedPdfException(MessageLocalization.getComposedMessage("the.color.depth.1.is.not.supported", this.bpc));
        } else {
            if (!PdfName.DEVICECMYK.equals(colorspace)) {
                if (colorspace instanceof PdfArray) {
                    PdfArray ca = (PdfArray) colorspace;
                    if (PdfName.ICCBASED.equals(ca.getDirectObject(0))) {
                        PRStream pr = (PRStream) ca.getDirectObject(1);
                        int n = pr.getAsNumber(PdfName.f128N).intValue();
                        if (n != 4) {
                            throw new UnsupportedPdfException(MessageLocalization.getComposedMessage("N.value.1.is.not.supported", n));
                        }
                        this.icc = PdfReader.getStreamBytes(pr);
                    } else {
                        throw new UnsupportedPdfException(MessageLocalization.getComposedMessage("the.color.space.1.is.not.supported", colorspace));
                    }
                }
                throw new UnsupportedPdfException(MessageLocalization.getComposedMessage("the.color.space.1.is.not.supported", colorspace));
            }
            this.stride = this.width * 4;
            TiffWriter wr = new TiffWriter();
            wr.addField(new FieldShort((int) TIFFConstants.TIFFTAG_SAMPLESPERPIXEL, 4));
            wr.addField(new FieldShort(258, new int[]{8, 8, 8, 8}));
            wr.addField(new FieldShort(262, 5));
            wr.addField(new FieldLong(256, this.width));
            wr.addField(new FieldLong(257, this.height));
            wr.addField(new FieldShort(259, 5));
            wr.addField(new FieldShort(317, 2));
            wr.addField(new FieldLong((int) TIFFConstants.TIFFTAG_ROWSPERSTRIP, this.height));
            wr.addField(new FieldRational((int) TIFFConstants.TIFFTAG_XRESOLUTION, new int[]{300, 1}));
            wr.addField(new FieldRational((int) TIFFConstants.TIFFTAG_YRESOLUTION, new int[]{300, 1}));
            wr.addField(new FieldShort(296, 2));
            wr.addField(new FieldAscii(305, Version.getInstance().getVersion()));
            ByteArrayOutputStream comp = new ByteArrayOutputStream();
            TiffWriter.compressLZW(comp, 2, this.imageBytes, this.height, 4, this.stride);
            byte[] buf = comp.toByteArray();
            wr.addField(new FieldImage(buf));
            wr.addField(new FieldLong((int) TIFFConstants.TIFFTAG_STRIPBYTECOUNTS, buf.length));
            if (this.icc != null) {
                wr.addField(new FieldUndefined(TIFFConstants.TIFFTAG_ICCPROFILE, this.icc));
            }
            wr.writeFile(ms);
            this.streamContentType = ImageBytesType.CCITT;
            this.imageBytes = ms.toByteArray();
        }
    }

    public byte[] getImageAsBytes() {
        return this.imageBytes;
    }
}
