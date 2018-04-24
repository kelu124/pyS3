package com.itextpdf.text.pdf.codec;

import android.support.v4.view.MotionEventCompat;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.Image;
import com.itextpdf.text.ImgRaw;
import com.itextpdf.text.Utilities;
import com.itextpdf.text.error_messages.MessageLocalization;
import com.itextpdf.text.pdf.PdfArray;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfNumber;
import com.itextpdf.text.pdf.PdfString;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;

public class BmpImage {
    private static final int BI_BITFIELDS = 3;
    private static final int BI_RGB = 0;
    private static final int BI_RLE4 = 2;
    private static final int BI_RLE8 = 1;
    private static final int LCS_CALIBRATED_RGB = 0;
    private static final int LCS_CMYK = 2;
    private static final int LCS_sRGB = 1;
    private static final int VERSION_2_1_BIT = 0;
    private static final int VERSION_2_24_BIT = 3;
    private static final int VERSION_2_4_BIT = 1;
    private static final int VERSION_2_8_BIT = 2;
    private static final int VERSION_3_1_BIT = 4;
    private static final int VERSION_3_24_BIT = 7;
    private static final int VERSION_3_4_BIT = 5;
    private static final int VERSION_3_8_BIT = 6;
    private static final int VERSION_3_NT_16_BIT = 8;
    private static final int VERSION_3_NT_32_BIT = 9;
    private static final int VERSION_4_16_BIT = 13;
    private static final int VERSION_4_1_BIT = 10;
    private static final int VERSION_4_24_BIT = 14;
    private static final int VERSION_4_32_BIT = 15;
    private static final int VERSION_4_4_BIT = 11;
    private static final int VERSION_4_8_BIT = 12;
    private int alphaMask;
    private long bitmapFileSize;
    private long bitmapOffset;
    private int bitsPerPixel;
    private int blueMask;
    private long compression;
    private int greenMask;
    int height;
    private long imageSize;
    private int imageType;
    private InputStream inputStream;
    private boolean isBottomUp;
    private int numBands;
    private byte[] palette;
    public HashMap<String, Object> properties = new HashMap();
    private int redMask;
    int width;
    private long xPelsPerMeter;
    private long yPelsPerMeter;

    BmpImage(InputStream is, boolean noHeader, int size) throws IOException {
        this.bitmapFileSize = (long) size;
        this.bitmapOffset = 0;
        process(is, noHeader);
    }

    public static Image getImage(URL url) throws IOException {
        InputStream inputStream = null;
        try {
            inputStream = url.openStream();
            Image img = getImage(inputStream);
            img.setUrl(url);
            return img;
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }

    public static Image getImage(InputStream is) throws IOException {
        return getImage(is, false, 0);
    }

    public static Image getImage(InputStream is, boolean noHeader, int size) throws IOException {
        BmpImage bmp = new BmpImage(is, noHeader, size);
        try {
            Image img = bmp.getImage();
            img.setDpi((int) ((((double) bmp.xPelsPerMeter) * 0.0254d) + 0.5d), (int) ((((double) bmp.yPelsPerMeter) * 0.0254d) + 0.5d));
            img.setOriginalType(4);
            return img;
        } catch (BadElementException be) {
            throw new ExceptionConverter(be);
        }
    }

    public static Image getImage(String file) throws IOException {
        return getImage(Utilities.toURL(file));
    }

    public static Image getImage(byte[] data) throws IOException {
        Image img = getImage(new ByteArrayInputStream(data));
        img.setOriginalData(data);
        return img;
    }

    protected void process(InputStream stream, boolean noHeader) throws IOException {
        if (noHeader || (stream instanceof BufferedInputStream)) {
            this.inputStream = stream;
        } else {
            this.inputStream = new BufferedInputStream(stream);
        }
        if (!noHeader) {
            if (readUnsignedByte(this.inputStream) == 66) {
                if (readUnsignedByte(this.inputStream) == 77) {
                    this.bitmapFileSize = readDWord(this.inputStream);
                    readWord(this.inputStream);
                    readWord(this.inputStream);
                    this.bitmapOffset = readDWord(this.inputStream);
                }
            }
            throw new RuntimeException(MessageLocalization.getComposedMessage("invalid.magic.value.for.bmp.file", new Object[0]));
        }
        long size = readDWord(this.inputStream);
        if (size == 12) {
            this.width = readWord(this.inputStream);
            this.height = readWord(this.inputStream);
        } else {
            this.width = readLong(this.inputStream);
            this.height = readLong(this.inputStream);
        }
        int planes = readWord(this.inputStream);
        this.bitsPerPixel = readWord(this.inputStream);
        this.properties.put("color_planes", Integer.valueOf(planes));
        this.properties.put("bits_per_pixel", Integer.valueOf(this.bitsPerPixel));
        this.numBands = 3;
        if (this.bitmapOffset == 0) {
            this.bitmapOffset = size;
        }
        int sizeOfPalette;
        if (size == 12) {
            this.properties.put("bmp_version", "BMP v. 2.x");
            if (this.bitsPerPixel == 1) {
                this.imageType = 0;
            } else if (this.bitsPerPixel == 4) {
                this.imageType = 1;
            } else if (this.bitsPerPixel == 8) {
                this.imageType = 2;
            } else if (this.bitsPerPixel == 24) {
                this.imageType = 3;
            }
            sizeOfPalette = ((int) (((this.bitmapOffset - 14) - size) / 3)) * 3;
            if (this.bitmapOffset == size) {
                switch (this.imageType) {
                    case 0:
                        sizeOfPalette = 6;
                        break;
                    case 1:
                        sizeOfPalette = 48;
                        break;
                    case 2:
                        sizeOfPalette = 768;
                        break;
                    case 3:
                        sizeOfPalette = 0;
                        break;
                }
                this.bitmapOffset = ((long) sizeOfPalette) + size;
            }
            readPalette(sizeOfPalette);
        } else {
            this.compression = readDWord(this.inputStream);
            this.imageSize = readDWord(this.inputStream);
            this.xPelsPerMeter = (long) readLong(this.inputStream);
            this.yPelsPerMeter = (long) readLong(this.inputStream);
            long colorsUsed = readDWord(this.inputStream);
            long colorsImportant = readDWord(this.inputStream);
            switch ((int) this.compression) {
                case 0:
                    this.properties.put("compression", "BI_RGB");
                    break;
                case 1:
                    this.properties.put("compression", "BI_RLE8");
                    break;
                case 2:
                    this.properties.put("compression", "BI_RLE4");
                    break;
                case 3:
                    this.properties.put("compression", "BI_BITFIELDS");
                    break;
            }
            this.properties.put("x_pixels_per_meter", Long.valueOf(this.xPelsPerMeter));
            this.properties.put("y_pixels_per_meter", Long.valueOf(this.yPelsPerMeter));
            this.properties.put("colors_used", Long.valueOf(colorsUsed));
            this.properties.put("colors_important", Long.valueOf(colorsImportant));
            if (size == 40 || size == 52 || size == 56) {
                switch ((int) this.compression) {
                    case 0:
                    case 1:
                    case 2:
                        if (this.bitsPerPixel == 1) {
                            this.imageType = 4;
                        } else if (this.bitsPerPixel == 4) {
                            this.imageType = 5;
                        } else if (this.bitsPerPixel == 8) {
                            this.imageType = 6;
                        } else if (this.bitsPerPixel == 24) {
                            this.imageType = 7;
                        } else if (this.bitsPerPixel == 16) {
                            this.imageType = 8;
                            this.redMask = 31744;
                            this.greenMask = 992;
                            this.blueMask = 31;
                            this.properties.put("red_mask", Integer.valueOf(this.redMask));
                            this.properties.put("green_mask", Integer.valueOf(this.greenMask));
                            this.properties.put("blue_mask", Integer.valueOf(this.blueMask));
                        } else if (this.bitsPerPixel == 32) {
                            this.imageType = 9;
                            this.redMask = 16711680;
                            this.greenMask = MotionEventCompat.ACTION_POINTER_INDEX_MASK;
                            this.blueMask = 255;
                            this.properties.put("red_mask", Integer.valueOf(this.redMask));
                            this.properties.put("green_mask", Integer.valueOf(this.greenMask));
                            this.properties.put("blue_mask", Integer.valueOf(this.blueMask));
                        }
                        if (size >= 52) {
                            this.redMask = (int) readDWord(this.inputStream);
                            this.greenMask = (int) readDWord(this.inputStream);
                            this.blueMask = (int) readDWord(this.inputStream);
                            this.properties.put("red_mask", Integer.valueOf(this.redMask));
                            this.properties.put("green_mask", Integer.valueOf(this.greenMask));
                            this.properties.put("blue_mask", Integer.valueOf(this.blueMask));
                        }
                        if (size == 56) {
                            this.alphaMask = (int) readDWord(this.inputStream);
                            this.properties.put("alpha_mask", Integer.valueOf(this.alphaMask));
                        }
                        sizeOfPalette = ((int) (((this.bitmapOffset - 14) - size) / 4)) * 4;
                        if (this.bitmapOffset == size) {
                            switch (this.imageType) {
                                case 4:
                                    if (colorsUsed == 0) {
                                        colorsUsed = 2;
                                    }
                                    sizeOfPalette = ((int) colorsUsed) * 4;
                                    break;
                                case 5:
                                    if (colorsUsed == 0) {
                                        colorsUsed = 16;
                                    }
                                    sizeOfPalette = ((int) colorsUsed) * 4;
                                    break;
                                case 6:
                                    if (colorsUsed == 0) {
                                        colorsUsed = 256;
                                    }
                                    sizeOfPalette = ((int) colorsUsed) * 4;
                                    break;
                                default:
                                    sizeOfPalette = 0;
                                    break;
                            }
                            this.bitmapOffset = ((long) sizeOfPalette) + size;
                        }
                        readPalette(sizeOfPalette);
                        this.properties.put("bmp_version", "BMP v. 3.x");
                        break;
                    case 3:
                        if (this.bitsPerPixel == 16) {
                            this.imageType = 8;
                        } else if (this.bitsPerPixel == 32) {
                            this.imageType = 9;
                        }
                        this.redMask = (int) readDWord(this.inputStream);
                        this.greenMask = (int) readDWord(this.inputStream);
                        this.blueMask = (int) readDWord(this.inputStream);
                        if (size == 56) {
                            this.alphaMask = (int) readDWord(this.inputStream);
                            this.properties.put("alpha_mask", Integer.valueOf(this.alphaMask));
                        }
                        this.properties.put("red_mask", Integer.valueOf(this.redMask));
                        this.properties.put("green_mask", Integer.valueOf(this.greenMask));
                        this.properties.put("blue_mask", Integer.valueOf(this.blueMask));
                        if (colorsUsed != 0) {
                            readPalette(((int) colorsUsed) * 4);
                        }
                        this.properties.put("bmp_version", "BMP v. 3.x NT");
                        break;
                    default:
                        throw new RuntimeException("Invalid compression specified in BMP file.");
                }
            } else if (size == 108) {
                this.properties.put("bmp_version", "BMP v. 4.x");
                this.redMask = (int) readDWord(this.inputStream);
                this.greenMask = (int) readDWord(this.inputStream);
                this.blueMask = (int) readDWord(this.inputStream);
                this.alphaMask = (int) readDWord(this.inputStream);
                long csType = readDWord(this.inputStream);
                int redX = readLong(this.inputStream);
                int redY = readLong(this.inputStream);
                int redZ = readLong(this.inputStream);
                int greenX = readLong(this.inputStream);
                int greenY = readLong(this.inputStream);
                int greenZ = readLong(this.inputStream);
                int blueX = readLong(this.inputStream);
                int blueY = readLong(this.inputStream);
                int blueZ = readLong(this.inputStream);
                long gammaRed = readDWord(this.inputStream);
                long gammaGreen = readDWord(this.inputStream);
                long gammaBlue = readDWord(this.inputStream);
                if (this.bitsPerPixel == 1) {
                    this.imageType = 10;
                } else if (this.bitsPerPixel == 4) {
                    this.imageType = 11;
                } else if (this.bitsPerPixel == 8) {
                    this.imageType = 12;
                } else if (this.bitsPerPixel == 16) {
                    this.imageType = 13;
                    if (((int) this.compression) == 0) {
                        this.redMask = 31744;
                        this.greenMask = 992;
                        this.blueMask = 31;
                    }
                } else if (this.bitsPerPixel == 24) {
                    this.imageType = 14;
                } else if (this.bitsPerPixel == 32) {
                    this.imageType = 15;
                    if (((int) this.compression) == 0) {
                        this.redMask = 16711680;
                        this.greenMask = MotionEventCompat.ACTION_POINTER_INDEX_MASK;
                        this.blueMask = 255;
                    }
                }
                this.properties.put("red_mask", Integer.valueOf(this.redMask));
                this.properties.put("green_mask", Integer.valueOf(this.greenMask));
                this.properties.put("blue_mask", Integer.valueOf(this.blueMask));
                this.properties.put("alpha_mask", Integer.valueOf(this.alphaMask));
                sizeOfPalette = ((int) (((this.bitmapOffset - 14) - size) / 4)) * 4;
                if (this.bitmapOffset == size) {
                    switch (this.imageType) {
                        case 10:
                            if (colorsUsed == 0) {
                                colorsUsed = 2;
                            }
                            sizeOfPalette = ((int) colorsUsed) * 4;
                            break;
                        case 11:
                            if (colorsUsed == 0) {
                                colorsUsed = 16;
                            }
                            sizeOfPalette = ((int) colorsUsed) * 4;
                            break;
                        case 12:
                            if (colorsUsed == 0) {
                                colorsUsed = 256;
                            }
                            sizeOfPalette = ((int) colorsUsed) * 4;
                            break;
                        default:
                            sizeOfPalette = 0;
                            break;
                    }
                    this.bitmapOffset = ((long) sizeOfPalette) + size;
                }
                readPalette(sizeOfPalette);
                switch ((int) csType) {
                    case 0:
                        this.properties.put("color_space", "LCS_CALIBRATED_RGB");
                        this.properties.put("redX", Integer.valueOf(redX));
                        this.properties.put("redY", Integer.valueOf(redY));
                        this.properties.put("redZ", Integer.valueOf(redZ));
                        this.properties.put("greenX", Integer.valueOf(greenX));
                        this.properties.put("greenY", Integer.valueOf(greenY));
                        this.properties.put("greenZ", Integer.valueOf(greenZ));
                        this.properties.put("blueX", Integer.valueOf(blueX));
                        this.properties.put("blueY", Integer.valueOf(blueY));
                        this.properties.put("blueZ", Integer.valueOf(blueZ));
                        this.properties.put("gamma_red", Long.valueOf(gammaRed));
                        this.properties.put("gamma_green", Long.valueOf(gammaGreen));
                        this.properties.put("gamma_blue", Long.valueOf(gammaBlue));
                        throw new RuntimeException("Not implemented yet.");
                    case 1:
                        this.properties.put("color_space", "LCS_sRGB");
                        break;
                    case 2:
                        this.properties.put("color_space", "LCS_CMYK");
                        throw new RuntimeException("Not implemented yet.");
                    default:
                        break;
                }
            } else {
                this.properties.put("bmp_version", "BMP v. 5.x");
                throw new RuntimeException("BMP version 5 not implemented yet.");
            }
        }
        if (this.height > 0) {
            this.isBottomUp = true;
        } else {
            this.isBottomUp = false;
            this.height = Math.abs(this.height);
        }
        if (this.bitsPerPixel == 1 || this.bitsPerPixel == 4 || this.bitsPerPixel == 8) {
            this.numBands = 1;
            int sizep;
            byte[] r;
            byte[] g;
            byte[] b;
            int i;
            int off;
            if (this.imageType == 0 || this.imageType == 1 || this.imageType == 2) {
                sizep = this.palette.length / 3;
                if (sizep > 256) {
                    sizep = 256;
                }
                r = new byte[sizep];
                g = new byte[sizep];
                b = new byte[sizep];
                for (i = 0; i < sizep; i++) {
                    off = i * 3;
                    b[i] = this.palette[off];
                    g[i] = this.palette[off + 1];
                    r[i] = this.palette[off + 2];
                }
                return;
            }
            sizep = this.palette.length / 4;
            if (sizep > 256) {
                sizep = 256;
            }
            r = new byte[sizep];
            g = new byte[sizep];
            b = new byte[sizep];
            for (i = 0; i < sizep; i++) {
                off = i * 4;
                b[i] = this.palette[off];
                g[i] = this.palette[off + 1];
                r[i] = this.palette[off + 2];
            }
        } else if (this.bitsPerPixel == 16) {
            this.numBands = 3;
        } else if (this.bitsPerPixel == 32) {
            this.numBands = this.alphaMask == 0 ? 3 : 4;
        } else {
            this.numBands = 3;
        }
    }

    private byte[] getPalette(int group) {
        if (this.palette == null) {
            return null;
        }
        byte[] np = new byte[((this.palette.length / group) * 3)];
        int e = this.palette.length / group;
        for (int k = 0; k < e; k++) {
            int src = k * group;
            int dest = k * 3;
            int src2 = src + 1;
            np[dest + 2] = this.palette[src];
            src = src2 + 1;
            np[dest + 1] = this.palette[src2];
            np[dest] = this.palette[src];
        }
        return np;
    }

    private Image getImage() throws IOException, BadElementException {
        byte[] bdata;
        switch (this.imageType) {
            case 0:
                return read1Bit(3);
            case 1:
                return read4Bit(3);
            case 2:
                return read8Bit(3);
            case 3:
                bdata = new byte[((this.width * this.height) * 3)];
                read24Bit(bdata);
                return new ImgRaw(this.width, this.height, 3, 8, bdata);
            case 4:
                return read1Bit(4);
            case 5:
                switch ((int) this.compression) {
                    case 0:
                        return read4Bit(4);
                    case 2:
                        return readRLE4();
                    default:
                        throw new RuntimeException("Invalid compression specified for BMP file.");
                }
            case 6:
                switch ((int) this.compression) {
                    case 0:
                        return read8Bit(4);
                    case 1:
                        return readRLE8();
                    default:
                        throw new RuntimeException("Invalid compression specified for BMP file.");
                }
            case 7:
                bdata = new byte[((this.width * this.height) * 3)];
                read24Bit(bdata);
                return new ImgRaw(this.width, this.height, 3, 8, bdata);
            case 8:
                return read1632Bit(false);
            case 9:
                return read1632Bit(true);
            case 10:
                return read1Bit(4);
            case 11:
                switch ((int) this.compression) {
                    case 0:
                        return read4Bit(4);
                    case 2:
                        return readRLE4();
                    default:
                        throw new RuntimeException("Invalid compression specified for BMP file.");
                }
            case 12:
                switch ((int) this.compression) {
                    case 0:
                        return read8Bit(4);
                    case 1:
                        return readRLE8();
                    default:
                        throw new RuntimeException("Invalid compression specified for BMP file.");
                }
            case 13:
                return read1632Bit(false);
            case 14:
                bdata = new byte[((this.width * this.height) * 3)];
                read24Bit(bdata);
                return new ImgRaw(this.width, this.height, 3, 8, bdata);
            case 15:
                return read1632Bit(true);
            default:
                return null;
        }
    }

    private Image indexedModel(byte[] bdata, int bpc, int paletteEntries) throws BadElementException {
        Image img = new ImgRaw(this.width, this.height, 1, bpc, bdata);
        PdfArray colorspace = new PdfArray();
        colorspace.add(PdfName.INDEXED);
        colorspace.add(PdfName.DEVICERGB);
        byte[] np = getPalette(paletteEntries);
        colorspace.add(new PdfNumber((np.length / 3) - 1));
        colorspace.add(new PdfString(np));
        PdfDictionary ad = new PdfDictionary();
        ad.put(PdfName.COLORSPACE, colorspace);
        img.setAdditional(ad);
        return img;
    }

    private void readPalette(int sizeOfPalette) throws IOException {
        if (sizeOfPalette != 0) {
            this.palette = new byte[sizeOfPalette];
            int bytesRead = 0;
            while (bytesRead < sizeOfPalette) {
                int r = this.inputStream.read(this.palette, bytesRead, sizeOfPalette - bytesRead);
                if (r < 0) {
                    throw new RuntimeException(MessageLocalization.getComposedMessage("incomplete.palette", new Object[0]));
                }
                bytesRead += r;
            }
            this.properties.put("palette", this.palette);
        }
    }

    private Image read1Bit(int paletteEntries) throws IOException, BadElementException {
        byte[] bdata = new byte[(((this.width + 7) / 8) * this.height)];
        int padding = 0;
        int bytesPerScanline = (int) Math.ceil(((double) this.width) / 8.0d);
        int remainder = bytesPerScanline % 4;
        if (remainder != 0) {
            padding = 4 - remainder;
        }
        int imSize = (bytesPerScanline + padding) * this.height;
        byte[] values = new byte[imSize];
        int bytesRead = 0;
        while (bytesRead < imSize) {
            bytesRead += this.inputStream.read(values, bytesRead, imSize - bytesRead);
        }
        int i;
        if (this.isBottomUp) {
            for (i = 0; i < this.height; i++) {
                System.arraycopy(values, imSize - ((i + 1) * (bytesPerScanline + padding)), bdata, i * bytesPerScanline, bytesPerScanline);
            }
        } else {
            for (i = 0; i < this.height; i++) {
                System.arraycopy(values, (bytesPerScanline + padding) * i, bdata, i * bytesPerScanline, bytesPerScanline);
            }
        }
        return indexedModel(bdata, 1, paletteEntries);
    }

    private Image read4Bit(int paletteEntries) throws IOException, BadElementException {
        byte[] bdata = new byte[(((this.width + 1) / 2) * this.height)];
        int padding = 0;
        int bytesPerScanline = (int) Math.ceil(((double) this.width) / 2.0d);
        int remainder = bytesPerScanline % 4;
        if (remainder != 0) {
            padding = 4 - remainder;
        }
        int imSize = (bytesPerScanline + padding) * this.height;
        byte[] values = new byte[imSize];
        int bytesRead = 0;
        while (bytesRead < imSize) {
            bytesRead += this.inputStream.read(values, bytesRead, imSize - bytesRead);
        }
        int i;
        if (this.isBottomUp) {
            for (i = 0; i < this.height; i++) {
                System.arraycopy(values, imSize - ((i + 1) * (bytesPerScanline + padding)), bdata, i * bytesPerScanline, bytesPerScanline);
            }
        } else {
            for (i = 0; i < this.height; i++) {
                System.arraycopy(values, (bytesPerScanline + padding) * i, bdata, i * bytesPerScanline, bytesPerScanline);
            }
        }
        return indexedModel(bdata, 4, paletteEntries);
    }

    private Image read8Bit(int paletteEntries) throws IOException, BadElementException {
        byte[] bdata = new byte[(this.width * this.height)];
        int padding = 0;
        int bitsPerScanline = this.width * 8;
        if (bitsPerScanline % 32 != 0) {
            padding = (int) Math.ceil(((double) ((((bitsPerScanline / 32) + 1) * 32) - bitsPerScanline)) / 8.0d);
        }
        int imSize = (this.width + padding) * this.height;
        byte[] values = new byte[imSize];
        int bytesRead = 0;
        while (bytesRead < imSize) {
            bytesRead += this.inputStream.read(values, bytesRead, imSize - bytesRead);
        }
        int i;
        if (this.isBottomUp) {
            for (i = 0; i < this.height; i++) {
                System.arraycopy(values, imSize - ((i + 1) * (this.width + padding)), bdata, this.width * i, this.width);
            }
        } else {
            for (i = 0; i < this.height; i++) {
                System.arraycopy(values, (this.width + padding) * i, bdata, this.width * i, this.width);
            }
        }
        return indexedModel(bdata, 8, paletteEntries);
    }

    private void read24Bit(byte[] bdata) {
        int padding = 0;
        int bitsPerScanline = this.width * 24;
        if (bitsPerScanline % 32 != 0) {
            padding = (int) Math.ceil(((double) ((((bitsPerScanline / 32) + 1) * 32) - bitsPerScanline)) / 8.0d);
        }
        int imSize = ((((this.width * 3) + 3) / 4) * 4) * this.height;
        byte[] values = new byte[imSize];
        int bytesRead = 0;
        while (bytesRead < imSize) {
            try {
                int r = this.inputStream.read(values, bytesRead, imSize - bytesRead);
                if (r < 0) {
                    break;
                }
                bytesRead += r;
            } catch (IOException ioe) {
                throw new ExceptionConverter(ioe);
            }
        }
        int l = 0;
        int count;
        int i;
        int j;
        if (this.isBottomUp) {
            int max = ((this.width * this.height) * 3) - 1;
            count = -padding;
            for (i = 0; i < this.height; i++) {
                l = (max - (((i + 1) * this.width) * 3)) + 1;
                count += padding;
                j = 0;
                while (j < this.width) {
                    int count2 = count + 1;
                    bdata[l + 2] = values[count];
                    count = count2 + 1;
                    bdata[l + 1] = values[count2];
                    count2 = count + 1;
                    bdata[l] = values[count];
                    l += 3;
                    j++;
                    count = count2;
                }
            }
            return;
        }
        count = -padding;
        for (i = 0; i < this.height; i++) {
            count += padding;
            j = 0;
            while (j < this.width) {
                count2 = count + 1;
                bdata[l + 2] = values[count];
                count = count2 + 1;
                bdata[l + 1] = values[count2];
                count2 = count + 1;
                bdata[l] = values[count];
                l += 3;
                j++;
                count = count2;
            }
        }
    }

    private int findMask(int mask) {
        for (int k = 0; k < 32 && (mask & 1) != 1; k++) {
            mask >>>= 1;
        }
        return mask;
    }

    private int findShift(int mask) {
        int k = 0;
        while (k < 32 && (mask & 1) != 1) {
            mask >>>= 1;
            k++;
        }
        return k;
    }

    private Image read1632Bit(boolean is32) throws IOException, BadElementException {
        int red_mask = findMask(this.redMask);
        int red_shift = findShift(this.redMask);
        int red_factor = red_mask + 1;
        int green_mask = findMask(this.greenMask);
        int green_shift = findShift(this.greenMask);
        int green_factor = green_mask + 1;
        int blue_mask = findMask(this.blueMask);
        int blue_shift = findShift(this.blueMask);
        int blue_factor = blue_mask + 1;
        byte[] bdata = new byte[((this.width * this.height) * 3)];
        int padding = 0;
        if (!is32) {
            int bitsPerScanline = this.width * 16;
            if (bitsPerScanline % 32 != 0) {
                padding = (int) Math.ceil(((double) ((((bitsPerScanline / 32) + 1) * 32) - bitsPerScanline)) / 8.0d);
            }
        }
        if (((int) this.imageSize) == 0) {
            int imSize = (int) (this.bitmapFileSize - this.bitmapOffset);
        }
        int i = 0;
        int i2;
        int j;
        int v;
        int i3;
        int m;
        if (this.isBottomUp) {
            for (i2 = this.height - 1; i2 >= 0; i2--) {
                i = (this.width * 3) * i2;
                j = 0;
                while (j < this.width) {
                    if (is32) {
                        v = (int) readDWord(this.inputStream);
                    } else {
                        v = readWord(this.inputStream);
                    }
                    i3 = i + 1;
                    bdata[i] = (byte) ((((v >>> red_shift) & red_mask) * 256) / red_factor);
                    i = i3 + 1;
                    bdata[i3] = (byte) ((((v >>> green_shift) & green_mask) * 256) / green_factor);
                    i3 = i + 1;
                    bdata[i] = (byte) ((((v >>> blue_shift) & blue_mask) * 256) / blue_factor);
                    j++;
                    i = i3;
                }
                for (m = 0; m < padding; m++) {
                    this.inputStream.read();
                }
            }
        } else {
            for (i2 = 0; i2 < this.height; i2++) {
                j = 0;
                while (j < this.width) {
                    if (is32) {
                        v = (int) readDWord(this.inputStream);
                    } else {
                        v = readWord(this.inputStream);
                    }
                    i3 = i + 1;
                    bdata[i] = (byte) ((((v >>> red_shift) & red_mask) * 256) / red_factor);
                    i = i3 + 1;
                    bdata[i3] = (byte) ((((v >>> green_shift) & green_mask) * 256) / green_factor);
                    i3 = i + 1;
                    bdata[i] = (byte) ((((v >>> blue_shift) & blue_mask) * 256) / blue_factor);
                    j++;
                    i = i3;
                }
                for (m = 0; m < padding; m++) {
                    this.inputStream.read();
                }
            }
        }
        return new ImgRaw(this.width, this.height, 3, 8, bdata);
    }

    private Image readRLE8() throws IOException, BadElementException {
        int imSize = (int) this.imageSize;
        if (imSize == 0) {
            imSize = (int) (this.bitmapFileSize - this.bitmapOffset);
        }
        byte[] values = new byte[imSize];
        int bytesRead = 0;
        while (bytesRead < imSize) {
            bytesRead += this.inputStream.read(values, bytesRead, imSize - bytesRead);
        }
        byte[] val = decodeRLE(true, values);
        imSize = this.width * this.height;
        if (this.isBottomUp) {
            byte[] temp = new byte[val.length];
            int bytesPerScanline = this.width;
            for (int i = 0; i < this.height; i++) {
                System.arraycopy(val, imSize - ((i + 1) * bytesPerScanline), temp, i * bytesPerScanline, bytesPerScanline);
            }
            val = temp;
        }
        return indexedModel(val, 8, 4);
    }

    private Image readRLE4() throws IOException, BadElementException {
        int imSize = (int) this.imageSize;
        if (imSize == 0) {
            imSize = (int) (this.bitmapFileSize - this.bitmapOffset);
        }
        byte[] values = new byte[imSize];
        int bytesRead = 0;
        while (bytesRead < imSize) {
            bytesRead += this.inputStream.read(values, bytesRead, imSize - bytesRead);
        }
        byte[] val = decodeRLE(false, values);
        if (this.isBottomUp) {
            byte[] inverted = val;
            val = new byte[(this.width * this.height)];
            int l = 0;
            int i = this.height - 1;
            while (i >= 0) {
                int lineEnd = l + this.width;
                int index = i * this.width;
                int l2 = l;
                while (l2 != lineEnd) {
                    l = l2 + 1;
                    int index2 = index + 1;
                    val[l2] = inverted[index];
                    index = index2;
                    l2 = l;
                }
                i--;
                l = l2;
            }
        }
        int stride = (this.width + 1) / 2;
        byte[] bdata = new byte[(this.height * stride)];
        int ptr = 0;
        int sh = 0;
        for (int h = 0; h < this.height; h++) {
            for (int w = 0; w < this.width; w++) {
                int ptr2;
                if ((w & 1) == 0) {
                    ptr2 = ptr + 1;
                    bdata[(w / 2) + sh] = (byte) (val[ptr] << 4);
                    ptr = ptr2;
                } else {
                    int i2 = (w / 2) + sh;
                    ptr2 = ptr + 1;
                    bdata[i2] = (byte) (bdata[i2] | ((byte) (val[ptr] & 15)));
                    ptr = ptr2;
                }
            }
            sh += stride;
        }
        return indexedModel(bdata, 4, 4);
    }

    private byte[] decodeRLE(boolean is8, byte[] values) {
        int ptr;
        byte[] val = new byte[(this.width * this.height)];
        int x = 0;
        int q = 0;
        int y = 0;
        int ptr2 = 0;
        while (y < this.height && ptr2 < values.length) {
            ptr = ptr2 + 1;
            int count = values[ptr2] & 255;
            int bt;
            int i;
            int q2;
            if (count != 0) {
                ptr2 = ptr + 1;
                bt = values[ptr] & 255;
                if (is8) {
                    i = count;
                    q2 = q;
                    while (i != 0) {
                        q = q2 + 1;
                        val[q2] = (byte) bt;
                        i--;
                        q2 = q;
                    }
                    q = q2;
                } else {
                    i = 0;
                    q2 = q;
                    while (i < count) {
                        q = q2 + 1;
                        val[q2] = (byte) ((i & 1) == 1 ? bt & 15 : (bt >>> 4) & 15);
                        i++;
                        q2 = q;
                    }
                    q = q2;
                }
                x += count;
                ptr = ptr2;
            } else {
                ptr2 = ptr + 1;
                count = values[ptr] & 255;
                if (count == 1) {
                    ptr = ptr2;
                    return val;
                }
                switch (count) {
                    case 0:
                        x = 0;
                        y++;
                        q = y * this.width;
                        ptr = ptr2;
                        continue;
                    case 2:
                        ptr = ptr2 + 1;
                        x += values[ptr2] & 255;
                        ptr2 = ptr + 1;
                        try {
                            y += values[ptr] & 255;
                            q = (this.width * y) + x;
                            ptr = ptr2;
                            continue;
                        } catch (RuntimeException e) {
                            ptr = ptr2;
                            break;
                        }
                    default:
                        if (is8) {
                            i = count;
                            q2 = q;
                            while (i != 0) {
                                q = q2 + 1;
                                ptr = ptr2 + 1;
                                val[q2] = (byte) (values[ptr2] & 255);
                                i--;
                                q2 = q;
                                ptr2 = ptr;
                            }
                            q = q2;
                            ptr = ptr2;
                        } else {
                            bt = 0;
                            i = 0;
                            q2 = q;
                            while (i < count) {
                                if ((i & 1) == 0) {
                                    ptr = ptr2 + 1;
                                    try {
                                        bt = values[ptr2] & 255;
                                    } catch (RuntimeException e2) {
                                        q = q2;
                                        break;
                                    }
                                }
                                ptr = ptr2;
                                q = q2 + 1;
                                try {
                                    val[q2] = (byte) ((i & 1) == 1 ? bt & 15 : (bt >>> 4) & 15);
                                    i++;
                                    q2 = q;
                                    ptr2 = ptr;
                                } catch (RuntimeException e3) {
                                    break;
                                }
                            }
                            q = q2;
                            ptr = ptr2;
                        }
                        x += count;
                        if (!is8) {
                            if ((count & 3) != 1 && (count & 3) != 2) {
                                break;
                            }
                            ptr++;
                            break;
                        } else if ((count & 1) == 1) {
                            ptr++;
                            break;
                        } else {
                            continue;
                        }
                        break;
                }
                return val;
            }
            ptr2 = ptr;
        }
        ptr = ptr2;
        return val;
    }

    private int readUnsignedByte(InputStream stream) throws IOException {
        return stream.read() & 255;
    }

    private int readUnsignedShort(InputStream stream) throws IOException {
        return ((readUnsignedByte(stream) << 8) | readUnsignedByte(stream)) & 65535;
    }

    private int readShort(InputStream stream) throws IOException {
        return (readUnsignedByte(stream) << 8) | readUnsignedByte(stream);
    }

    private int readWord(InputStream stream) throws IOException {
        return readUnsignedShort(stream);
    }

    private long readUnsignedInt(InputStream stream) throws IOException {
        int b1 = readUnsignedByte(stream);
        int b2 = readUnsignedByte(stream);
        return -1 & ((long) ((((readUnsignedByte(stream) << 24) | (readUnsignedByte(stream) << 16)) | (b2 << 8)) | b1));
    }

    private int readInt(InputStream stream) throws IOException {
        int b1 = readUnsignedByte(stream);
        int b2 = readUnsignedByte(stream);
        return (((readUnsignedByte(stream) << 24) | (readUnsignedByte(stream) << 16)) | (b2 << 8)) | b1;
    }

    private long readDWord(InputStream stream) throws IOException {
        return readUnsignedInt(stream);
    }

    private int readLong(InputStream stream) throws IOException {
        return readInt(stream);
    }
}
