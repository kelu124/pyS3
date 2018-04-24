package com.itextpdf.text.pdf;

import com.itextpdf.text.Image;
import com.itextpdf.text.error_messages.MessageLocalization;
import com.itextpdf.text.html.HtmlTags;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class PdfImage extends PdfStream {
    static final int TRANSFERSIZE = 4096;
    protected Image image = null;
    protected PdfName name = null;

    public PdfImage(Image image, String name, PdfIndirectReference maskRef) throws BadPdfFormatException {
        IOException ioe;
        Throwable th;
        this.image = image;
        if (name == null) {
            generateImgResName(image);
        } else {
            this.name = new PdfName(name);
        }
        put(PdfName.TYPE, PdfName.XOBJECT);
        put(PdfName.SUBTYPE, PdfName.IMAGE);
        put(PdfName.WIDTH, new PdfNumber(image.getWidth()));
        put(PdfName.HEIGHT, new PdfNumber(image.getHeight()));
        if (image.getLayer() != null) {
            put(PdfName.OC, image.getLayer().getRef());
        }
        if (image.isMask() && (image.getBpc() == 1 || image.getBpc() > 255)) {
            put(PdfName.IMAGEMASK, PdfBoolean.PDFTRUE);
        }
        if (maskRef != null) {
            if (image.isSmask()) {
                put(PdfName.SMASK, maskRef);
            } else {
                put(PdfName.MASK, maskRef);
            }
        }
        if (image.isMask() && image.isInverted()) {
            put(PdfName.DECODE, new PdfLiteral("[1 0]"));
        }
        if (image.isInterpolation()) {
            put(PdfName.INTERPOLATE, PdfBoolean.PDFTRUE);
        }
        InputStream is = null;
        try {
            int k;
            int[] transparency = image.getTransparency();
            if (!(transparency == null || image.isMask() || maskRef != null)) {
                StringBuilder s = new StringBuilder("[");
                for (int append : transparency) {
                    s.append(append).append(" ");
                }
                s.append("]");
                put(PdfName.MASK, new PdfLiteral(s.toString()));
            }
            PdfDictionary decodeparms;
            if (image.isImgRaw()) {
                int colorspace = image.getColorspace();
                this.bytes = image.getRawData();
                put(PdfName.LENGTH, new PdfNumber(this.bytes.length));
                int bpc = image.getBpc();
                if (bpc > 255) {
                    if (!image.isMask()) {
                        put(PdfName.COLORSPACE, PdfName.DEVICEGRAY);
                    }
                    put(PdfName.BITSPERCOMPONENT, new PdfNumber(1));
                    put(PdfName.FILTER, PdfName.CCITTFAXDECODE);
                    k = bpc - 257;
                    decodeparms = new PdfDictionary();
                    if (k != 0) {
                        decodeparms.put(PdfName.f125K, new PdfNumber(k));
                    }
                    if ((colorspace & 1) != 0) {
                        decodeparms.put(PdfName.BLACKIS1, PdfBoolean.PDFTRUE);
                    }
                    if ((colorspace & 2) != 0) {
                        decodeparms.put(PdfName.ENCODEDBYTEALIGN, PdfBoolean.PDFTRUE);
                    }
                    if ((colorspace & 4) != 0) {
                        decodeparms.put(PdfName.ENDOFLINE, PdfBoolean.PDFTRUE);
                    }
                    if ((colorspace & 8) != 0) {
                        decodeparms.put(PdfName.ENDOFBLOCK, PdfBoolean.PDFFALSE);
                    }
                    decodeparms.put(PdfName.COLUMNS, new PdfNumber(image.getWidth()));
                    decodeparms.put(PdfName.ROWS, new PdfNumber(image.getHeight()));
                    put(PdfName.DECODEPARMS, decodeparms);
                } else {
                    switch (colorspace) {
                        case 1:
                            put(PdfName.COLORSPACE, PdfName.DEVICEGRAY);
                            if (image.isInverted()) {
                                put(PdfName.DECODE, new PdfLiteral("[1 0]"));
                                break;
                            }
                            break;
                        case 3:
                            put(PdfName.COLORSPACE, PdfName.DEVICERGB);
                            if (image.isInverted()) {
                                put(PdfName.DECODE, new PdfLiteral("[1 0 1 0 1 0]"));
                                break;
                            }
                            break;
                        default:
                            put(PdfName.COLORSPACE, PdfName.DEVICECMYK);
                            if (image.isInverted()) {
                                put(PdfName.DECODE, new PdfLiteral("[1 0 1 0 1 0 1 0]"));
                                break;
                            }
                            break;
                    }
                    PdfDictionary additional = image.getAdditional();
                    if (additional != null) {
                        putAll(additional);
                    }
                    if (image.isMask() && (image.getBpc() == 1 || image.getBpc() > 8)) {
                        remove(PdfName.COLORSPACE);
                    }
                    put(PdfName.BITSPERCOMPONENT, new PdfNumber(image.getBpc()));
                    if (image.isDeflated()) {
                        put(PdfName.FILTER, PdfName.FLATEDECODE);
                    } else {
                        flateCompress(image.getCompressionLevel());
                    }
                }
                if (is != null) {
                    try {
                        is.close();
                        return;
                    } catch (Exception e) {
                        return;
                    }
                }
                return;
            }
            String errorID;
            if (image.getRawData() == null) {
                is = image.getUrl().openStream();
                errorID = image.getUrl().toString();
            } else {
                InputStream is2 = new ByteArrayInputStream(image.getRawData());
                try {
                    errorID = "Byte array";
                    is = is2;
                } catch (IOException e2) {
                    ioe = e2;
                    is = is2;
                    try {
                        throw new BadPdfFormatException(ioe.getMessage());
                    } catch (Throwable th2) {
                        th = th2;
                        if (is != null) {
                            try {
                                is.close();
                            } catch (Exception e3) {
                            }
                        }
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    is = is2;
                    if (is != null) {
                        is.close();
                    }
                    throw th;
                }
            }
            switch (image.type()) {
                case 32:
                    put(PdfName.FILTER, PdfName.DCTDECODE);
                    if (image.getColorTransform() == 0) {
                        decodeparms = new PdfDictionary();
                        decodeparms.put(PdfName.COLORTRANSFORM, new PdfNumber(0));
                        put(PdfName.DECODEPARMS, decodeparms);
                    }
                    switch (image.getColorspace()) {
                        case 1:
                            put(PdfName.COLORSPACE, PdfName.DEVICEGRAY);
                            break;
                        case 3:
                            put(PdfName.COLORSPACE, PdfName.DEVICERGB);
                            break;
                        default:
                            put(PdfName.COLORSPACE, PdfName.DEVICECMYK);
                            if (image.isInverted()) {
                                put(PdfName.DECODE, new PdfLiteral("[1 0 1 0 1 0 1 0]"));
                                break;
                            }
                            break;
                    }
                    put(PdfName.BITSPERCOMPONENT, new PdfNumber(8));
                    if (image.getRawData() == null) {
                        this.streamBytes = new ByteArrayOutputStream();
                        transferBytes(is, this.streamBytes, -1);
                        break;
                    }
                    this.bytes = image.getRawData();
                    put(PdfName.LENGTH, new PdfNumber(this.bytes.length));
                    if (is != null) {
                        try {
                            is.close();
                            return;
                        } catch (Exception e4) {
                            return;
                        }
                    }
                    return;
                case 33:
                    put(PdfName.FILTER, PdfName.JPXDECODE);
                    if (image.getColorspace() > 0) {
                        switch (image.getColorspace()) {
                            case 1:
                                put(PdfName.COLORSPACE, PdfName.DEVICEGRAY);
                                break;
                            case 3:
                                put(PdfName.COLORSPACE, PdfName.DEVICERGB);
                                break;
                            default:
                                put(PdfName.COLORSPACE, PdfName.DEVICECMYK);
                                break;
                        }
                        put(PdfName.BITSPERCOMPONENT, new PdfNumber(image.getBpc()));
                    }
                    if (image.getRawData() == null) {
                        this.streamBytes = new ByteArrayOutputStream();
                        transferBytes(is, this.streamBytes, -1);
                        break;
                    }
                    this.bytes = image.getRawData();
                    put(PdfName.LENGTH, new PdfNumber(this.bytes.length));
                    if (is != null) {
                        try {
                            is.close();
                            return;
                        } catch (Exception e5) {
                            return;
                        }
                    }
                    return;
                case 36:
                    put(PdfName.FILTER, PdfName.JBIG2DECODE);
                    put(PdfName.COLORSPACE, PdfName.DEVICEGRAY);
                    put(PdfName.BITSPERCOMPONENT, new PdfNumber(1));
                    if (image.getRawData() == null) {
                        this.streamBytes = new ByteArrayOutputStream();
                        transferBytes(is, this.streamBytes, -1);
                        break;
                    }
                    this.bytes = image.getRawData();
                    put(PdfName.LENGTH, new PdfNumber(this.bytes.length));
                    if (is != null) {
                        try {
                            is.close();
                            return;
                        } catch (Exception e6) {
                            return;
                        }
                    }
                    return;
                default:
                    throw new BadPdfFormatException(MessageLocalization.getComposedMessage("1.is.an.unknown.image.format", errorID));
            }
            if (image.getCompressionLevel() > 0) {
                flateCompress(image.getCompressionLevel());
            }
            put(PdfName.LENGTH, new PdfNumber(this.streamBytes.size()));
            if (is != null) {
                try {
                    is.close();
                } catch (Exception e7) {
                }
            }
        } catch (IOException e8) {
            ioe = e8;
            throw new BadPdfFormatException(ioe.getMessage());
        }
    }

    public PdfName name() {
        return this.name;
    }

    public Image getImage() {
        return this.image;
    }

    static void transferBytes(InputStream in, OutputStream out, int len) throws IOException {
        byte[] buffer = new byte[4096];
        if (len < 0) {
            len = 2147418112;
        }
        while (len != 0) {
            int size = in.read(buffer, 0, Math.min(len, 4096));
            if (size >= 0) {
                out.write(buffer, 0, size);
                len -= size;
            } else {
                return;
            }
        }
    }

    protected void importAll(PdfImage dup) {
        this.name = dup.name;
        this.compressed = dup.compressed;
        this.compressionLevel = dup.compressionLevel;
        this.streamBytes = dup.streamBytes;
        this.bytes = dup.bytes;
        this.hashMap = dup.hashMap;
    }

    private void generateImgResName(Image img) {
        this.name = new PdfName(HtmlTags.IMG + Long.toHexString(img.getMySerialId().longValue()));
    }
}
