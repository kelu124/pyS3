package com.itextpdf.text.pdf.codec;

import com.google.common.base.Ascii;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.Image;
import com.itextpdf.text.ImgRaw;
import com.itextpdf.text.Jpeg;
import com.itextpdf.text.error_messages.MessageLocalization;
import com.itextpdf.text.exceptions.InvalidImageException;
import com.itextpdf.text.pdf.ICC_Profile;
import com.itextpdf.text.pdf.PdfArray;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfNumber;
import com.itextpdf.text.pdf.PdfObject;
import com.itextpdf.text.pdf.PdfString;
import com.itextpdf.text.pdf.RandomAccessFileOrArray;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.DataFormatException;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.Inflater;

public class TiffImage {
    public static int getNumberOfPages(RandomAccessFileOrArray s) {
        try {
            return TIFFDirectory.getNumDirectories(s);
        } catch (Exception e) {
            throw new ExceptionConverter(e);
        }
    }

    static int getDpi(TIFFField fd, int resolutionUnit) {
        if (fd == null) {
            return 0;
        }
        long[] res = fd.getAsRational(0);
        float frac = ((float) res[0]) / ((float) res[1]);
        switch (resolutionUnit) {
            case 1:
            case 2:
                return (int) (((double) frac) + 0.5d);
            case 3:
                return (int) ((((double) frac) * 2.54d) + 0.5d);
            default:
                return 0;
        }
    }

    public static Image getTiffImage(RandomAccessFileOrArray s, boolean recoverFromImageError, int page, boolean direct) {
        float rotation;
        Image img;
        if (page < 1) {
            throw new IllegalArgumentException(MessageLocalization.getComposedMessage("the.page.number.must.be.gt.eq.1", new Object[0]));
        }
        byte[] im;
        TIFFDirectory tIFFDirectory = new TIFFDirectory(s, page - 1);
        if (tIFFDirectory.isTagPresent(322)) {
            throw new IllegalArgumentException(MessageLocalization.getComposedMessage("tiles.are.not.supported", new Object[0]));
        }
        int compression = (int) tIFFDirectory.getFieldAsLong(259);
        switch (compression) {
            case 2:
            case 3:
            case 4:
            case 32771:
                rotation = 0.0f;
                if (tIFFDirectory.isTagPresent(TIFFConstants.TIFFTAG_ORIENTATION)) {
                    int rot = (int) tIFFDirectory.getFieldAsLong(TIFFConstants.TIFFTAG_ORIENTATION);
                    if (rot == 3 || rot == 4) {
                        rotation = 3.1415927f;
                    } else if (rot == 5 || rot == 8) {
                        rotation = 1.5707964f;
                    } else if (rot == 6 || rot == 7) {
                        rotation = -1.5707964f;
                    }
                }
                long tiffT4Options = 0;
                long tiffT6Options = 0;
                int fillOrder = 1;
                h = (int) tIFFDirectory.getFieldAsLong(257);
                w = (int) tIFFDirectory.getFieldAsLong(256);
                XYRatio = 0.0f;
                int resolutionUnit = 2;
                if (tIFFDirectory.isTagPresent(296)) {
                    resolutionUnit = (int) tIFFDirectory.getFieldAsLong(296);
                }
                dpiX = getDpi(tIFFDirectory.getField(TIFFConstants.TIFFTAG_XRESOLUTION), resolutionUnit);
                dpiY = getDpi(tIFFDirectory.getField(TIFFConstants.TIFFTAG_YRESOLUTION), resolutionUnit);
                if (resolutionUnit == 1) {
                    if (dpiY != 0) {
                        XYRatio = ((float) dpiX) / ((float) dpiY);
                    }
                    dpiX = 0;
                    dpiY = 0;
                }
                int rowsStrip = h;
                if (tIFFDirectory.isTagPresent(TIFFConstants.TIFFTAG_ROWSPERSTRIP)) {
                    rowsStrip = (int) tIFFDirectory.getFieldAsLong(TIFFConstants.TIFFTAG_ROWSPERSTRIP);
                }
                if (rowsStrip <= 0 || rowsStrip > h) {
                    rowsStrip = h;
                }
                offset = getArrayLongShort(tIFFDirectory, TIFFConstants.TIFFTAG_STRIPOFFSETS);
                size = getArrayLongShort(tIFFDirectory, TIFFConstants.TIFFTAG_STRIPBYTECOUNTS);
                if ((size == null || (size.length == 1 && (size[0] == 0 || size[0] + offset[0] > s.length()))) && h == rowsStrip) {
                    size = new long[]{s.length() - ((long) ((int) offset[0]))};
                }
                TIFFField fillOrderField = tIFFDirectory.getField(TIFFConstants.TIFFTAG_FILLORDER);
                if (fillOrderField != null) {
                    fillOrder = fillOrderField.getAsInt(0);
                }
                if (fillOrder == 2) {
                }
                params = 0;
                if (tIFFDirectory.isTagPresent(262) && tIFFDirectory.getFieldAsLong(262) == 1) {
                    params = 0 | 1;
                }
                imagecomp = 0;
                switch (compression) {
                    case 2:
                    case 32771:
                        imagecomp = 257;
                        params |= 10;
                        break;
                    case 3:
                        imagecomp = 257;
                        params |= 12;
                        TIFFField t4OptionsField = tIFFDirectory.getField(TIFFConstants.TIFFTAG_GROUP3OPTIONS);
                        if (t4OptionsField != null) {
                            tiffT4Options = t4OptionsField.getAsLong(0);
                            if ((1 & tiffT4Options) != 0) {
                                imagecomp = 258;
                            }
                            if ((4 & tiffT4Options) != 0) {
                                params |= 2;
                                break;
                            }
                        }
                        break;
                    case 4:
                        imagecomp = 256;
                        TIFFField t6OptionsField = tIFFDirectory.getField(TIFFConstants.TIFFTAG_GROUP4OPTIONS);
                        if (t6OptionsField != null) {
                            tiffT6Options = t6OptionsField.getAsLong(0);
                            break;
                        }
                        break;
                }
                if (direct && rowsStrip == h) {
                    im = new byte[((int) size[0])];
                    s.seek(offset[0]);
                    s.readFully(im);
                    img = Image.getInstance(w, h, false, imagecomp, params, im);
                    img.setInverted(true);
                } else {
                    int rowsLeft = h;
                    CCITTG4Encoder cCITTG4Encoder = new CCITTG4Encoder(w);
                    for (int k = 0; k < offset.length; k++) {
                        im = new byte[((int) size[k])];
                        s.seek(offset[k]);
                        s.readFully(im);
                        int height = Math.min(rowsStrip, rowsLeft);
                        TIFFFaxDecoder decoder = new TIFFFaxDecoder(fillOrder, w, height);
                        decoder.setRecoverFromImageError(recoverFromImageError);
                        byte[] outBuf = new byte[(((w + 7) / 8) * height)];
                        switch (compression) {
                            case 2:
                            case 32771:
                                decoder.decode1D(outBuf, im, 0, height);
                                cCITTG4Encoder.fax4Encode(outBuf, height);
                                break;
                            case 3:
                                try {
                                    decoder.decode2D(outBuf, im, 0, height, tiffT4Options);
                                } catch (RuntimeException e) {
                                    tiffT4Options ^= 4;
                                    try {
                                        decoder.decode2D(outBuf, im, 0, height, tiffT4Options);
                                    } catch (InvalidImageException e2) {
                                        if (!recoverFromImageError) {
                                            throw e2;
                                        }
                                    } catch (RuntimeException e3) {
                                        if (!recoverFromImageError) {
                                            throw e;
                                        } else if (rowsStrip == 1) {
                                            throw e;
                                        } else {
                                            int h;
                                            int w;
                                            float XYRatio;
                                            int dpiX;
                                            int dpiY;
                                            long[] offset;
                                            long[] size;
                                            int params;
                                            int imagecomp;
                                            im = new byte[((int) size[0])];
                                            s.seek(offset[0]);
                                            s.readFully(im);
                                            img = Image.getInstance(w, h, false, imagecomp, params, im);
                                            img.setInverted(true);
                                            img.setDpi(dpiX, dpiY);
                                            img.setXYRatio(XYRatio);
                                            img.setOriginalType(5);
                                            if (rotation == 0.0f) {
                                                return img;
                                            }
                                            img.setInitialRotation(rotation);
                                            return img;
                                        }
                                    } catch (Exception e4) {
                                        throw new ExceptionConverter(e4);
                                    }
                                }
                                cCITTG4Encoder.fax4Encode(outBuf, height);
                                break;
                            case 4:
                                decoder.decodeT6(outBuf, im, 0, height, tiffT6Options);
                                cCITTG4Encoder.fax4Encode(outBuf, height);
                                break;
                            default:
                                break;
                        }
                        rowsLeft -= rowsStrip;
                    }
                    int i = w;
                    int i2 = h;
                    img = Image.getInstance(i, i2, false, 256, params & 1, cCITTG4Encoder.close());
                }
                img.setDpi(dpiX, dpiY);
                img.setXYRatio(XYRatio);
                if (tIFFDirectory.isTagPresent(TIFFConstants.TIFFTAG_ICCPROFILE)) {
                    try {
                        ICC_Profile icc_prof = ICC_Profile.getInstance(tIFFDirectory.getField(TIFFConstants.TIFFTAG_ICCPROFILE).getAsBytes());
                        if (icc_prof.getNumComponents() == 1) {
                            img.tagICC(icc_prof);
                        }
                    } catch (RuntimeException e5) {
                    }
                }
                img.setOriginalType(5);
                if (rotation == 0.0f) {
                    return img;
                }
                img.setInitialRotation(rotation);
                return img;
            default:
                return getTiffImageColor(tIFFDirectory, s);
        }
        throw new ExceptionConverter(e4);
    }

    public static Image getTiffImage(RandomAccessFileOrArray s, boolean recoverFromImageError, int page) {
        return getTiffImage(s, recoverFromImageError, page, false);
    }

    public static Image getTiffImage(RandomAccessFileOrArray s, int page) {
        return getTiffImage(s, page, false);
    }

    public static Image getTiffImage(RandomAccessFileOrArray s, int page, boolean direct) {
        return getTiffImage(s, false, page, direct);
    }

    protected static Image getTiffImageColor(TIFFDirectory dir, RandomAccessFileOrArray s) {
        try {
            int compression = (int) dir.getFieldAsLong(259);
            int predictor = 1;
            TIFFLZWDecoder lzwDecoder = null;
            switch (compression) {
                case 1:
                case 5:
                case 6:
                case 7:
                case 8:
                case 32773:
                case TIFFConstants.COMPRESSION_DEFLATE /*32946*/:
                    int photometric = (int) dir.getFieldAsLong(262);
                    switch (photometric) {
                        case 0:
                        case 1:
                        case 2:
                        case 3:
                        case 5:
                            break;
                        default:
                            if (!(compression == 6 || compression == 7)) {
                                throw new IllegalArgumentException(MessageLocalization.getComposedMessage("the.photometric.1.is.not.supported", photometric));
                            }
                    }
                    float rotation = 0.0f;
                    if (dir.isTagPresent(TIFFConstants.TIFFTAG_ORIENTATION)) {
                        int rot = (int) dir.getFieldAsLong(TIFFConstants.TIFFTAG_ORIENTATION);
                        if (rot == 3 || rot == 4) {
                            rotation = 3.1415927f;
                        } else if (rot == 5 || rot == 8) {
                            rotation = 1.5707964f;
                        } else if (rot == 6 || rot == 7) {
                            rotation = -1.5707964f;
                        }
                    }
                    if (dir.isTagPresent(TIFFConstants.TIFFTAG_PLANARCONFIG) && dir.getFieldAsLong(TIFFConstants.TIFFTAG_PLANARCONFIG) == 2) {
                        throw new IllegalArgumentException(MessageLocalization.getComposedMessage("planar.images.are.not.supported", new Object[0]));
                    }
                    int extraSamples = 0;
                    if (dir.isTagPresent(338)) {
                        extraSamples = 1;
                    }
                    int samplePerPixel = 1;
                    if (dir.isTagPresent(TIFFConstants.TIFFTAG_SAMPLESPERPIXEL)) {
                        samplePerPixel = (int) dir.getFieldAsLong(TIFFConstants.TIFFTAG_SAMPLESPERPIXEL);
                    }
                    int bitsPerSample = 1;
                    if (dir.isTagPresent(258)) {
                        bitsPerSample = (int) dir.getFieldAsLong(258);
                    }
                    switch (bitsPerSample) {
                        case 1:
                        case 2:
                        case 4:
                        case 8:
                            Image img;
                            int k;
                            int h = (int) dir.getFieldAsLong(257);
                            int w = (int) dir.getFieldAsLong(256);
                            int resolutionUnit = 2;
                            if (dir.isTagPresent(296)) {
                                resolutionUnit = (int) dir.getFieldAsLong(296);
                            }
                            int dpiX = getDpi(dir.getField(TIFFConstants.TIFFTAG_XRESOLUTION), resolutionUnit);
                            int dpiY = getDpi(dir.getField(TIFFConstants.TIFFTAG_YRESOLUTION), resolutionUnit);
                            int fillOrder = 1;
                            TIFFField fillOrderField = dir.getField(TIFFConstants.TIFFTAG_FILLORDER);
                            if (fillOrderField != null) {
                                fillOrder = fillOrderField.getAsInt(0);
                            }
                            boolean reverse = fillOrder == 2;
                            int rowsStrip = h;
                            if (dir.isTagPresent(TIFFConstants.TIFFTAG_ROWSPERSTRIP)) {
                                rowsStrip = (int) dir.getFieldAsLong(TIFFConstants.TIFFTAG_ROWSPERSTRIP);
                            }
                            if (rowsStrip <= 0 || rowsStrip > h) {
                                rowsStrip = h;
                            }
                            long[] offset = getArrayLongShort(dir, TIFFConstants.TIFFTAG_STRIPOFFSETS);
                            long[] size = getArrayLongShort(dir, TIFFConstants.TIFFTAG_STRIPBYTECOUNTS);
                            if ((size == null || (size.length == 1 && (size[0] == 0 || size[0] + offset[0] > s.length()))) && h == rowsStrip) {
                                size = new long[]{s.length() - ((long) ((int) offset[0]))};
                            }
                            if (compression == 5 || compression == 32946 || compression == 8) {
                                TIFFField predictorField = dir.getField(317);
                                if (predictorField != null) {
                                    predictor = predictorField.getAsInt(0);
                                    if (predictor != 1 && predictor != 2) {
                                        throw new RuntimeException(MessageLocalization.getComposedMessage("illegal.value.for.predictor.in.tiff.file", new Object[0]));
                                    } else if (predictor == 2 && bitsPerSample != 8) {
                                        throw new RuntimeException(MessageLocalization.getComposedMessage("1.bit.samples.are.not.supported.for.horizontal.differencing.predictor", bitsPerSample));
                                    }
                                }
                            }
                            if (compression == 5) {
                                TIFFLZWDecoder tIFFLZWDecoder = new TIFFLZWDecoder(w, predictor, samplePerPixel);
                            }
                            int rowsLeft = h;
                            ByteArrayOutputStream stream = null;
                            ByteArrayOutputStream mstream = null;
                            DeflaterOutputStream zip = null;
                            DeflaterOutputStream mzip = null;
                            if (extraSamples > 0) {
                                mstream = new ByteArrayOutputStream();
                                mzip = new DeflaterOutputStream(mstream);
                            }
                            CCITTG4Encoder g4 = null;
                            if (bitsPerSample == 1 && samplePerPixel == 1 && photometric != 3) {
                                CCITTG4Encoder cCITTG4Encoder = new CCITTG4Encoder(w);
                            } else {
                                OutputStream stream2 = new ByteArrayOutputStream();
                                if (!(compression == 6 || compression == 7)) {
                                    zip = new DeflaterOutputStream(stream2);
                                }
                            }
                            byte[] jpeg;
                            if (compression == 6) {
                                if (dir.isTagPresent(513)) {
                                    int jpegOffset = (int) dir.getFieldAsLong(513);
                                    int jpegLength = ((int) s.length()) - jpegOffset;
                                    if (dir.isTagPresent(514)) {
                                        jpegLength = ((int) dir.getFieldAsLong(514)) + ((int) size[0]);
                                    }
                                    jpeg = new byte[Math.min(jpegLength, ((int) s.length()) - jpegOffset)];
                                    s.seek((long) (((int) s.getFilePointer()) + jpegOffset));
                                    s.readFully(jpeg);
                                    img = new Jpeg(jpeg);
                                } else {
                                    throw new IOException(MessageLocalization.getComposedMessage("missing.tag.s.for.ojpeg.compression", new Object[0]));
                                }
                            } else if (compression != 7) {
                                for (k = 0; k < offset.length; k++) {
                                    byte[] im = new byte[((int) size[k])];
                                    s.seek(offset[k]);
                                    s.readFully(im);
                                    int height = Math.min(rowsStrip, rowsLeft);
                                    byte[] outBuf = null;
                                    if (compression != 1) {
                                        outBuf = new byte[(((((w * bitsPerSample) * samplePerPixel) + 7) / 8) * height)];
                                    }
                                    if (reverse) {
                                        TIFFFaxDecoder.reverseBits(im);
                                    }
                                    switch (compression) {
                                        case 1:
                                            outBuf = im;
                                            break;
                                        case 5:
                                            lzwDecoder.decode(im, outBuf, height);
                                            break;
                                        case 8:
                                        case TIFFConstants.COMPRESSION_DEFLATE /*32946*/:
                                            inflate(im, outBuf);
                                            applyPredictor(outBuf, predictor, w, height, samplePerPixel);
                                            break;
                                        case 32773:
                                            decodePackbits(im, outBuf);
                                            break;
                                    }
                                    if (bitsPerSample == 1 && samplePerPixel == 1 && photometric != 3) {
                                        g4.fax4Encode(outBuf, height);
                                    } else if (extraSamples > 0) {
                                        ProcessExtraSamples(zip, mzip, outBuf, samplePerPixel, bitsPerSample, w, height);
                                    } else {
                                        zip.write(outBuf);
                                    }
                                    rowsLeft -= rowsStrip;
                                }
                                if (bitsPerSample == 1 && samplePerPixel == 1 && photometric != 3) {
                                    img = Image.getInstance(w, h, false, 256, photometric == 1 ? 1 : 0, g4.close());
                                } else {
                                    zip.close();
                                    img = new ImgRaw(w, h, samplePerPixel - extraSamples, bitsPerSample, stream.toByteArray());
                                    img.setDeflated(true);
                                }
                            } else if (size.length > 1) {
                                throw new IOException(MessageLocalization.getComposedMessage("compression.jpeg.is.only.supported.with.a.single.strip.this.image.has.1.strips", size.length));
                            } else {
                                jpeg = new byte[((int) size[0])];
                                s.seek(offset[0]);
                                s.readFully(jpeg);
                                TIFFField jpegtables = dir.getField(TIFFConstants.TIFFTAG_JPEGTABLES);
                                if (jpegtables != null) {
                                    Object temp = jpegtables.getAsBytes();
                                    int tableoffset = 0;
                                    int tablelength = temp.length;
                                    if (temp[0] == (byte) -1 && temp[1] == (byte) -40) {
                                        tableoffset = 2;
                                        tablelength -= 2;
                                    }
                                    if (temp[temp.length - 2] == (byte) -1 && temp[temp.length - 1] == (byte) -39) {
                                        tablelength -= 2;
                                    }
                                    Object tables = new byte[tablelength];
                                    System.arraycopy(temp, tableoffset, tables, 0, tablelength);
                                    Object jpegwithtables = new byte[(jpeg.length + tables.length)];
                                    System.arraycopy(jpeg, 0, jpegwithtables, 0, 2);
                                    System.arraycopy(tables, 0, jpegwithtables, 2, tables.length);
                                    System.arraycopy(jpeg, 2, jpegwithtables, tables.length + 2, jpeg.length - 2);
                                    jpeg = jpegwithtables;
                                }
                                img = new Jpeg(jpeg);
                                if (photometric == 2) {
                                    img.setColorTransform(0);
                                }
                            }
                            img.setDpi(dpiX, dpiY);
                            if (!(compression == 6 || compression == 7)) {
                                if (dir.isTagPresent(TIFFConstants.TIFFTAG_ICCPROFILE)) {
                                    try {
                                        ICC_Profile icc_prof = ICC_Profile.getInstance(dir.getField(TIFFConstants.TIFFTAG_ICCPROFILE).getAsBytes());
                                        if (samplePerPixel - extraSamples == icc_prof.getNumComponents()) {
                                            img.tagICC(icc_prof);
                                        }
                                    } catch (RuntimeException e) {
                                    }
                                }
                                if (dir.isTagPresent(320)) {
                                    PdfObject indexed;
                                    PdfDictionary additional;
                                    char[] rgb = dir.getField(320).getAsChars();
                                    byte[] palette = new byte[rgb.length];
                                    int gColor = rgb.length / 3;
                                    int bColor = gColor * 2;
                                    for (k = 0; k < gColor; k++) {
                                        palette[k * 3] = (byte) (rgb[k] >>> 8);
                                        palette[(k * 3) + 1] = (byte) (rgb[k + gColor] >>> 8);
                                        palette[(k * 3) + 2] = (byte) (rgb[k + bColor] >>> 8);
                                    }
                                    boolean colormapBroken = true;
                                    k = 0;
                                    while (k < palette.length) {
                                        if (palette[k] != (byte) 0) {
                                            colormapBroken = false;
                                            if (colormapBroken) {
                                                for (k = 0; k < gColor; k++) {
                                                    palette[k * 3] = (byte) rgb[k];
                                                    palette[(k * 3) + 1] = (byte) rgb[k + gColor];
                                                    palette[(k * 3) + 2] = (byte) rgb[k + bColor];
                                                }
                                            }
                                            indexed = new PdfArray();
                                            indexed.add(PdfName.INDEXED);
                                            indexed.add(PdfName.DEVICERGB);
                                            indexed.add(new PdfNumber(gColor - 1));
                                            indexed.add(new PdfString(palette));
                                            additional = new PdfDictionary();
                                            additional.put(PdfName.COLORSPACE, indexed);
                                            img.setAdditional(additional);
                                        } else {
                                            k++;
                                        }
                                    }
                                    if (colormapBroken) {
                                        for (k = 0; k < gColor; k++) {
                                            palette[k * 3] = (byte) rgb[k];
                                            palette[(k * 3) + 1] = (byte) rgb[k + gColor];
                                            palette[(k * 3) + 2] = (byte) rgb[k + bColor];
                                        }
                                    }
                                    indexed = new PdfArray();
                                    indexed.add(PdfName.INDEXED);
                                    indexed.add(PdfName.DEVICERGB);
                                    indexed.add(new PdfNumber(gColor - 1));
                                    indexed.add(new PdfString(palette));
                                    additional = new PdfDictionary();
                                    additional.put(PdfName.COLORSPACE, indexed);
                                    img.setAdditional(additional);
                                }
                                img.setOriginalType(5);
                            }
                            if (photometric == 0) {
                                img.setInverted(true);
                            }
                            if (rotation != 0.0f) {
                                img.setInitialRotation(rotation);
                            }
                            if (extraSamples > 0) {
                                mzip.close();
                                Image mimg = Image.getInstance(w, h, 1, bitsPerSample, mstream.toByteArray());
                                mimg.makeMask();
                                mimg.setDeflated(true);
                                img.setImageMask(mimg);
                            }
                            return img;
                        default:
                            throw new IllegalArgumentException(MessageLocalization.getComposedMessage("bits.per.sample.1.is.not.supported", bitsPerSample));
                    }
                default:
                    throw new IllegalArgumentException(MessageLocalization.getComposedMessage("the.compression.1.is.not.supported", compression));
            }
        } catch (Exception e2) {
            throw new ExceptionConverter(e2);
        }
        throw new ExceptionConverter(e2);
    }

    static Image ProcessExtraSamples(DeflaterOutputStream zip, DeflaterOutputStream mzip, byte[] outBuf, int samplePerPixel, int bitsPerSample, int width, int height) throws IOException {
        if (bitsPerSample == 8) {
            byte[] mask = new byte[(width * height)];
            int optr = 0;
            int total = (width * height) * samplePerPixel;
            int k = 0;
            int mptr = 0;
            while (k < total) {
                int s = 0;
                int optr2 = optr;
                while (s < samplePerPixel - 1) {
                    optr = optr2 + 1;
                    outBuf[optr2] = outBuf[k + s];
                    s++;
                    optr2 = optr;
                }
                int mptr2 = mptr + 1;
                mask[mptr] = outBuf[(k + samplePerPixel) - 1];
                k += samplePerPixel;
                optr = optr2;
                mptr = mptr2;
            }
            zip.write(outBuf, 0, optr);
            mzip.write(mask, 0, mptr);
            return null;
        }
        throw new IllegalArgumentException(MessageLocalization.getComposedMessage("extra.samples.are.not.supported", new Object[0]));
    }

    static long[] getArrayLongShort(TIFFDirectory dir, int tag) {
        TIFFField field = dir.getField(tag);
        if (field == null) {
            return null;
        }
        if (field.getType() == 4) {
            return field.getAsLongs();
        }
        char[] temp = field.getAsChars();
        long[] offset = new long[temp.length];
        for (int k = 0; k < temp.length; k++) {
            offset[k] = (long) temp[k];
        }
        return offset;
    }

    public static void decodePackbits(byte[] data, byte[] dst) {
        int srcCount;
        int dstCount = 0;
        int srcCount2 = 0;
        while (dstCount < dst.length) {
            try {
                srcCount = srcCount2 + 1;
                try {
                    byte b = data[srcCount2];
                    int i;
                    int dstCount2;
                    if (b >= (byte) 0 && b <= Ascii.DEL) {
                        i = 0;
                        dstCount2 = dstCount;
                        srcCount2 = srcCount;
                        while (i < b + 1) {
                            dstCount = dstCount2 + 1;
                            srcCount = srcCount2 + 1;
                            dst[dstCount2] = data[srcCount2];
                            i++;
                            dstCount2 = dstCount;
                            srcCount2 = srcCount;
                        }
                        dstCount = dstCount2;
                    } else if (b > (byte) -1 || b < (byte) -127) {
                        srcCount2 = srcCount + 1;
                    } else {
                        srcCount2 = srcCount + 1;
                        byte repeat = data[srcCount];
                        i = 0;
                        dstCount2 = dstCount;
                        while (i < (-b) + 1) {
                            dstCount = dstCount2 + 1;
                            dst[dstCount2] = repeat;
                            i++;
                            dstCount2 = dstCount;
                        }
                        dstCount = dstCount2;
                    }
                } catch (Exception e) {
                    return;
                }
            } catch (Exception e2) {
                srcCount = srcCount2;
                return;
            }
        }
        srcCount = srcCount2;
    }

    public static void inflate(byte[] deflated, byte[] inflated) {
        Inflater inflater = new Inflater();
        inflater.setInput(deflated);
        try {
            inflater.inflate(inflated);
        } catch (DataFormatException dfe) {
            throw new ExceptionConverter(dfe);
        }
    }

    public static void applyPredictor(byte[] uncompData, int predictor, int w, int h, int samplesPerPixel) {
        if (predictor == 2) {
            for (int j = 0; j < h; j++) {
                int count = samplesPerPixel * ((j * w) + 1);
                for (int i = samplesPerPixel; i < w * samplesPerPixel; i++) {
                    uncompData[count] = (byte) (uncompData[count] + uncompData[count - samplesPerPixel]);
                    count++;
                }
            }
        }
    }
}
