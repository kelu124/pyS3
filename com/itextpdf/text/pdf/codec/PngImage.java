package com.itextpdf.text.pdf.codec;

import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.Image;
import com.itextpdf.text.ImgRaw;
import com.itextpdf.text.Utilities;
import com.itextpdf.text.error_messages.MessageLocalization;
import com.itextpdf.text.pdf.BaseField;
import com.itextpdf.text.pdf.ByteBuffer;
import com.itextpdf.text.pdf.ICC_Profile;
import com.itextpdf.text.pdf.PdfArray;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfLiteral;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfNumber;
import com.itextpdf.text.pdf.PdfObject;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfString;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

public class PngImage {
    public static final String IDAT = "IDAT";
    public static final String IEND = "IEND";
    public static final String IHDR = "IHDR";
    public static final String PLTE = "PLTE";
    public static final int[] PNGID = new int[]{137, 80, 78, 71, 13, 10, 26, 10};
    private static final int PNG_FILTER_AVERAGE = 3;
    private static final int PNG_FILTER_NONE = 0;
    private static final int PNG_FILTER_PAETH = 4;
    private static final int PNG_FILTER_SUB = 1;
    private static final int PNG_FILTER_UP = 2;
    private static final int TRANSFERSIZE = 4096;
    public static final String cHRM = "cHRM";
    public static final String gAMA = "gAMA";
    public static final String iCCP = "iCCP";
    private static final PdfName[] intents = new PdfName[]{PdfName.PERCEPTUAL, PdfName.RELATIVECOLORIMETRIC, PdfName.SATURATION, PdfName.ABSOLUTECOLORIMETRIC};
    public static final String pHYs = "pHYs";
    public static final String sRGB = "sRGB";
    public static final String tRNS = "tRNS";
    float XYRatio;
    PdfDictionary additional = new PdfDictionary();
    int bitDepth;
    int bytesPerPixel;
    byte[] colorTable;
    int colorType;
    int compressionMethod;
    DataInputStream dataStream;
    int dpiX;
    int dpiY;
    int filterMethod;
    float gamma = BaseField.BORDER_WIDTH_THIN;
    boolean genBWMask;
    boolean hasCHRM = false;
    int height;
    ICC_Profile icc_profile;
    NewByteArrayOutputStream idat = new NewByteArrayOutputStream();
    byte[] image;
    int inputBands;
    PdfName intent;
    int interlaceMethod;
    InputStream is;
    boolean palShades;
    byte[] smask;
    byte[] trans;
    int transBlue = -1;
    int transGreen = -1;
    int transRedGray = -1;
    int width;
    float xB;
    float xG;
    float xR;
    float xW;
    float yB;
    float yG;
    float yR;
    float yW;

    static class NewByteArrayOutputStream extends ByteArrayOutputStream {
        NewByteArrayOutputStream() {
        }

        public byte[] getBuf() {
            return this.buf;
        }
    }

    PngImage(InputStream is) {
        this.is = is;
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
        return new PngImage(is).getImage();
    }

    public static Image getImage(String file) throws IOException {
        return getImage(Utilities.toURL(file));
    }

    public static Image getImage(byte[] data) throws IOException {
        Image img = getImage(new ByteArrayInputStream(data));
        img.setOriginalData(data);
        return img;
    }

    boolean checkMarker(String s) {
        if (s.length() != 4) {
            return false;
        }
        for (int k = 0; k < 4; k++) {
            char c = s.charAt(k);
            if ((c < 'a' || c > 'z') && (c < 'A' || c > 'Z')) {
                return false;
            }
        }
        return true;
    }

    void readPng() throws IOException {
        for (int i : PNGID) {
            if (i != this.is.read()) {
                throw new IOException(MessageLocalization.getComposedMessage("file.is.not.a.valid.png", new Object[0]));
            }
        }
        byte[] buffer = new byte[4096];
        while (true) {
            int len = getInt(this.is);
            String marker = getString(this.is);
            if (len < 0 || !checkMarker(marker)) {
                throw new IOException(MessageLocalization.getComposedMessage("corrupted.png.file", new Object[0]));
            }
            if (IDAT.equals(marker)) {
                while (len != 0) {
                    int size = this.is.read(buffer, 0, Math.min(len, 4096));
                    if (size >= 0) {
                        this.idat.write(buffer, 0, size);
                        len -= size;
                    } else {
                        return;
                    }
                }
                continue;
            } else if (tRNS.equals(marker)) {
                switch (this.colorType) {
                    case 0:
                        if (len >= 2) {
                            len -= 2;
                            int gray = getWord(this.is);
                            if (this.bitDepth != 16) {
                                this.additional.put(PdfName.MASK, new PdfLiteral("[" + gray + " " + gray + "]"));
                                break;
                            } else {
                                this.transRedGray = gray;
                                break;
                            }
                        }
                        break;
                    case 2:
                        if (len >= 6) {
                            len -= 6;
                            int red = getWord(this.is);
                            int green = getWord(this.is);
                            int blue = getWord(this.is);
                            if (this.bitDepth != 16) {
                                this.additional.put(PdfName.MASK, new PdfLiteral("[" + red + " " + red + " " + green + " " + green + " " + blue + " " + blue + "]"));
                                break;
                            }
                            this.transRedGray = red;
                            this.transGreen = green;
                            this.transBlue = blue;
                            break;
                        }
                        break;
                    case 3:
                        if (len > 0) {
                            this.trans = new byte[len];
                            for (int k = 0; k < len; k++) {
                                this.trans[k] = (byte) this.is.read();
                            }
                            len = 0;
                            break;
                        }
                        break;
                }
                Utilities.skip(this.is, len);
            } else if (IHDR.equals(marker)) {
                this.width = getInt(this.is);
                this.height = getInt(this.is);
                this.bitDepth = this.is.read();
                this.colorType = this.is.read();
                this.compressionMethod = this.is.read();
                this.filterMethod = this.is.read();
                this.interlaceMethod = this.is.read();
            } else if (PLTE.equals(marker)) {
                if (this.colorType == 3) {
                    PdfArray colorspace = new PdfArray();
                    colorspace.add(PdfName.INDEXED);
                    colorspace.add(getColorspace());
                    colorspace.add(new PdfNumber((len / 3) - 1));
                    ByteBuffer colortable = new ByteBuffer();
                    int len2 = len;
                    while (true) {
                        len = len2 - 1;
                        if (len2 > 0) {
                            colortable.append_i(this.is.read());
                            len2 = len;
                        } else {
                            byte[] toByteArray = colortable.toByteArray();
                            this.colorTable = toByteArray;
                            colorspace.add(new PdfString(toByteArray));
                            this.additional.put(PdfName.COLORSPACE, colorspace);
                        }
                    }
                } else {
                    Utilities.skip(this.is, len);
                }
            } else if (pHYs.equals(marker)) {
                int dx = getInt(this.is);
                int dy = getInt(this.is);
                if (this.is.read() == 1) {
                    this.dpiX = (int) ((((float) dx) * 0.0254f) + 0.5f);
                    this.dpiY = (int) ((((float) dy) * 0.0254f) + 0.5f);
                } else if (dy != 0) {
                    this.XYRatio = ((float) dx) / ((float) dy);
                }
            } else if (cHRM.equals(marker)) {
                this.xW = ((float) getInt(this.is)) / 100000.0f;
                this.yW = ((float) getInt(this.is)) / 100000.0f;
                this.xR = ((float) getInt(this.is)) / 100000.0f;
                this.yR = ((float) getInt(this.is)) / 100000.0f;
                this.xG = ((float) getInt(this.is)) / 100000.0f;
                this.yG = ((float) getInt(this.is)) / 100000.0f;
                this.xB = ((float) getInt(this.is)) / 100000.0f;
                this.yB = ((float) getInt(this.is)) / 100000.0f;
                boolean z = Math.abs(this.xW) >= 1.0E-4f && Math.abs(this.yW) >= 1.0E-4f && Math.abs(this.xR) >= 1.0E-4f && Math.abs(this.yR) >= 1.0E-4f && Math.abs(this.xG) >= 1.0E-4f && Math.abs(this.yG) >= 1.0E-4f && Math.abs(this.xB) >= 1.0E-4f && Math.abs(this.yB) >= 1.0E-4f;
                this.hasCHRM = z;
            } else if (sRGB.equals(marker)) {
                this.intent = intents[this.is.read()];
                this.gamma = 2.2f;
                this.xW = 0.3127f;
                this.yW = 0.329f;
                this.xR = 0.64f;
                this.yR = 0.33f;
                this.xG = 0.3f;
                this.yG = 0.6f;
                this.xB = 0.15f;
                this.yB = 0.06f;
                this.hasCHRM = true;
            } else if (gAMA.equals(marker)) {
                int gm = getInt(this.is);
                if (gm != 0) {
                    this.gamma = 100000.0f / ((float) gm);
                    if (!this.hasCHRM) {
                        this.xW = 0.3127f;
                        this.yW = 0.329f;
                        this.xR = 0.64f;
                        this.yR = 0.33f;
                        this.xG = 0.3f;
                        this.yG = 0.6f;
                        this.xB = 0.15f;
                        this.yB = 0.06f;
                        this.hasCHRM = true;
                    }
                }
            } else if (iCCP.equals(marker)) {
                do {
                    len--;
                } while (this.is.read() != 0);
                this.is.read();
                len--;
                byte[] icccom = new byte[len];
                int p = 0;
                while (len > 0) {
                    int r = this.is.read(icccom, p, len);
                    if (r < 0) {
                        throw new IOException(MessageLocalization.getComposedMessage("premature.end.of.file", new Object[0]));
                    }
                    p += r;
                    len -= r;
                }
                try {
                    this.icc_profile = ICC_Profile.getInstance(PdfReader.FlateDecode(icccom, true));
                } catch (RuntimeException e) {
                    this.icc_profile = null;
                }
            } else if (!IEND.equals(marker)) {
                Utilities.skip(this.is, len);
            } else {
                return;
            }
            Utilities.skip(this.is, 4);
        }
    }

    PdfObject getColorspace() {
        if (this.icc_profile != null) {
            if ((this.colorType & 2) == 0) {
                return PdfName.DEVICEGRAY;
            }
            return PdfName.DEVICERGB;
        } else if (this.gamma != BaseField.BORDER_WIDTH_THIN || this.hasCHRM) {
            PdfObject array = new PdfArray();
            PdfDictionary dic = new PdfDictionary();
            if ((this.colorType & 2) != 0) {
                PdfObject wp;
                PdfObject pdfLiteral = new PdfLiteral("[1 1 1]");
                array.add(PdfName.CALRGB);
                if (this.gamma != BaseField.BORDER_WIDTH_THIN) {
                    PdfObject gm = new PdfArray();
                    pdfLiteral = new PdfNumber(this.gamma);
                    gm.add(pdfLiteral);
                    gm.add(pdfLiteral);
                    gm.add(pdfLiteral);
                    dic.put(PdfName.GAMMA, gm);
                }
                if (this.hasCHRM) {
                    float z = this.yW * ((((this.xG - this.xB) * this.yR) - ((this.xR - this.xB) * this.yG)) + ((this.xR - this.xG) * this.yB));
                    float YA = (this.yR * ((((this.xG - this.xB) * this.yW) - ((this.xW - this.xB) * this.yG)) + ((this.xW - this.xG) * this.yB))) / z;
                    float XA = (this.xR * YA) / this.yR;
                    float ZA = YA * (((BaseField.BORDER_WIDTH_THIN - this.xR) / this.yR) - BaseField.BORDER_WIDTH_THIN);
                    float YB = ((-this.yG) * ((((this.xR - this.xB) * this.yW) - ((this.xW - this.xB) * this.yR)) + ((this.xW - this.xR) * this.yB))) / z;
                    float XB = (this.xG * YB) / this.yG;
                    float ZB = YB * (((BaseField.BORDER_WIDTH_THIN - this.xG) / this.yG) - BaseField.BORDER_WIDTH_THIN);
                    float YC = (this.yB * ((((this.xR - this.xG) * this.yW) - ((this.xW - this.xG) * this.yW)) + ((this.xW - this.xR) * this.yG))) / z;
                    float XC = (this.xB * YC) / this.yB;
                    float ZC = YC * (((BaseField.BORDER_WIDTH_THIN - this.xB) / this.yB) - BaseField.BORDER_WIDTH_THIN);
                    float XW = (XA + XB) + XC;
                    float ZW = (ZA + ZB) + ZC;
                    PdfObject wpa = new PdfArray();
                    wpa.add(new PdfNumber(XW));
                    wpa.add(new PdfNumber((float) BaseField.BORDER_WIDTH_THIN));
                    wpa.add(new PdfNumber(ZW));
                    wp = wpa;
                    PdfObject matrix = new PdfArray();
                    matrix.add(new PdfNumber(XA));
                    matrix.add(new PdfNumber(YA));
                    matrix.add(new PdfNumber(ZA));
                    matrix.add(new PdfNumber(XB));
                    matrix.add(new PdfNumber(YB));
                    matrix.add(new PdfNumber(ZB));
                    matrix.add(new PdfNumber(XC));
                    matrix.add(new PdfNumber(YC));
                    matrix.add(new PdfNumber(ZC));
                    dic.put(PdfName.MATRIX, matrix);
                }
                dic.put(PdfName.WHITEPOINT, wp);
                array.add(dic);
                return array;
            } else if (this.gamma == BaseField.BORDER_WIDTH_THIN) {
                return PdfName.DEVICEGRAY;
            } else {
                array.add(PdfName.CALGRAY);
                dic.put(PdfName.GAMMA, new PdfNumber(this.gamma));
                dic.put(PdfName.WHITEPOINT, new PdfLiteral("[1 1 1]"));
                array.add(dic);
                return array;
            }
        } else if ((this.colorType & 2) == 0) {
            return PdfName.DEVICEGRAY;
        } else {
            return PdfName.DEVICERGB;
        }
    }

    Image getImage() throws IOException {
        readPng();
        int pal0 = 0;
        int palIdx = 0;
        try {
            Image img;
            Image im2;
            this.palShades = false;
            if (this.trans != null) {
                for (int k = 0; k < this.trans.length; k++) {
                    int n = this.trans[k] & 255;
                    if (n == 0) {
                        pal0++;
                        palIdx = k;
                    }
                    if (n != 0 && n != 255) {
                        this.palShades = true;
                        break;
                    }
                }
            }
            if ((this.colorType & 4) != 0) {
                this.palShades = true;
            }
            boolean z = !this.palShades && (pal0 > 1 || this.transRedGray >= 0);
            this.genBWMask = z;
            if (!(this.palShades || this.genBWMask || pal0 != 1)) {
                this.additional.put(PdfName.MASK, new PdfLiteral("[" + palIdx + " " + palIdx + "]"));
            }
            boolean needDecode = this.interlaceMethod == 1 || this.bitDepth == 16 || (this.colorType & 4) != 0 || this.palShades || this.genBWMask;
            switch (this.colorType) {
                case 0:
                    this.inputBands = 1;
                    break;
                case 2:
                    this.inputBands = 3;
                    break;
                case 3:
                    this.inputBands = 1;
                    break;
                case 4:
                    this.inputBands = 2;
                    break;
                case 6:
                    this.inputBands = 4;
                    break;
            }
            if (needDecode) {
                decodeIdat();
            }
            int components = this.inputBands;
            if ((this.colorType & 4) != 0) {
                components--;
            }
            int bpc = this.bitDepth;
            if (bpc == 16) {
                bpc = 8;
            }
            if (this.image == null) {
                img = new ImgRaw(this.width, this.height, components, bpc, this.idat.toByteArray());
                img.setDeflated(true);
                PdfDictionary decodeparms = new PdfDictionary();
                decodeparms.put(PdfName.BITSPERCOMPONENT, new PdfNumber(this.bitDepth));
                decodeparms.put(PdfName.PREDICTOR, new PdfNumber(15));
                decodeparms.put(PdfName.COLUMNS, new PdfNumber(this.width));
                PdfName pdfName = PdfName.COLORS;
                int i = (this.colorType == 3 || (this.colorType & 2) == 0) ? 1 : 3;
                decodeparms.put(pdfName, new PdfNumber(i));
                this.additional.put(PdfName.DECODEPARMS, decodeparms);
            } else if (this.colorType == 3) {
                img = new ImgRaw(this.width, this.height, components, bpc, this.image);
            } else {
                img = Image.getInstance(this.width, this.height, components, bpc, this.image);
            }
            if (this.additional.get(PdfName.COLORSPACE) == null) {
                this.additional.put(PdfName.COLORSPACE, getColorspace());
            }
            if (this.intent != null) {
                this.additional.put(PdfName.INTENT, this.intent);
            }
            if (this.additional.size() > 0) {
                img.setAdditional(this.additional);
            }
            if (this.icc_profile != null) {
                img.tagICC(this.icc_profile);
            }
            if (this.palShades) {
                im2 = Image.getInstance(this.width, this.height, 1, 8, this.smask);
                im2.makeMask();
                img.setImageMask(im2);
            }
            if (this.genBWMask) {
                im2 = Image.getInstance(this.width, this.height, 1, 1, this.smask);
                im2.makeMask();
                img.setImageMask(im2);
            }
            img.setDpi(this.dpiX, this.dpiY);
            img.setXYRatio(this.XYRatio);
            img.setOriginalType(2);
            return img;
        } catch (Exception e) {
            throw new ExceptionConverter(e);
        }
    }

    void decodeIdat() {
        int nbitDepth = this.bitDepth;
        if (nbitDepth == 16) {
            nbitDepth = 8;
        }
        int size = -1;
        this.bytesPerPixel = this.bitDepth == 16 ? 2 : 1;
        switch (this.colorType) {
            case 0:
                size = (((this.width * nbitDepth) + 7) / 8) * this.height;
                break;
            case 2:
                size = (this.width * 3) * this.height;
                this.bytesPerPixel *= 3;
                break;
            case 3:
                if (this.interlaceMethod == 1) {
                    size = (((this.width * nbitDepth) + 7) / 8) * this.height;
                }
                this.bytesPerPixel = 1;
                break;
            case 4:
                size = this.width * this.height;
                this.bytesPerPixel *= 2;
                break;
            case 6:
                size = (this.width * 3) * this.height;
                this.bytesPerPixel *= 4;
                break;
        }
        if (size >= 0) {
            this.image = new byte[size];
        }
        if (this.palShades) {
            this.smask = new byte[(this.width * this.height)];
        } else if (this.genBWMask) {
            this.smask = new byte[(((this.width + 7) / 8) * this.height)];
        }
        this.dataStream = new DataInputStream(new InflaterInputStream(new ByteArrayInputStream(this.idat.getBuf(), 0, this.idat.size()), new Inflater()));
        if (this.interlaceMethod != 1) {
            decodePass(0, 0, 1, 1, this.width, this.height);
            return;
        }
        decodePass(0, 0, 8, 8, (this.width + 7) / 8, (this.height + 7) / 8);
        decodePass(4, 0, 8, 8, (this.width + 3) / 8, (this.height + 7) / 8);
        decodePass(0, 4, 4, 8, (this.width + 3) / 4, (this.height + 3) / 8);
        decodePass(2, 0, 4, 4, (this.width + 1) / 4, (this.height + 3) / 4);
        decodePass(0, 2, 2, 4, (this.width + 1) / 2, (this.height + 1) / 4);
        decodePass(1, 0, 2, 2, this.width / 2, (this.height + 1) / 2);
        decodePass(0, 1, 1, 2, this.width, this.height / 2);
    }

    void decodePass(int xOffset, int yOffset, int xStep, int yStep, int passWidth, int passHeight) {
        if (passWidth != 0 && passHeight != 0) {
            int bytesPerRow = (((this.inputBands * passWidth) * this.bitDepth) + 7) / 8;
            byte[] curr = new byte[bytesPerRow];
            byte[] prior = new byte[bytesPerRow];
            int srcY = 0;
            int dstY = yOffset;
            while (srcY < passHeight) {
                int filter = 0;
                try {
                    filter = this.dataStream.read();
                    this.dataStream.readFully(curr, 0, bytesPerRow);
                } catch (Exception e) {
                }
                switch (filter) {
                    case 0:
                        break;
                    case 1:
                        decodeSubFilter(curr, bytesPerRow, this.bytesPerPixel);
                        break;
                    case 2:
                        decodeUpFilter(curr, prior, bytesPerRow);
                        break;
                    case 3:
                        decodeAverageFilter(curr, prior, bytesPerRow, this.bytesPerPixel);
                        break;
                    case 4:
                        decodePaethFilter(curr, prior, bytesPerRow, this.bytesPerPixel);
                        break;
                    default:
                        throw new RuntimeException(MessageLocalization.getComposedMessage("png.filter.unknown", new Object[0]));
                }
                processPixels(curr, xOffset, xStep, dstY, passWidth);
                byte[] tmp = prior;
                prior = curr;
                curr = tmp;
                srcY++;
                dstY += yStep;
            }
        }
    }

    void processPixels(byte[] curr, int xOffset, int step, int y, int width) {
        int dstX;
        int yStride;
        int srcX;
        int[] out = getPixel(curr);
        int sizes = 0;
        switch (this.colorType) {
            case 0:
            case 3:
            case 4:
                sizes = 1;
                break;
            case 2:
            case 6:
                sizes = 3;
                break;
        }
        if (this.image != null) {
            dstX = xOffset;
            yStride = (((this.bitDepth == 16 ? 8 : this.bitDepth) * (sizes * this.width)) + 7) / 8;
            for (srcX = 0; srcX < width; srcX++) {
                setPixel(this.image, out, this.inputBands * srcX, sizes, dstX, y, this.bitDepth, yStride);
                dstX += step;
            }
        }
        int i;
        int[] v;
        int idx;
        if (this.palShades) {
            if ((this.colorType & 4) != 0) {
                if (this.bitDepth == 16) {
                    for (int k = 0; k < width; k++) {
                        i = (this.inputBands * k) + sizes;
                        out[i] = out[i] >>> 8;
                    }
                }
                yStride = this.width;
                dstX = xOffset;
                for (srcX = 0; srcX < width; srcX++) {
                    setPixel(this.smask, out, (this.inputBands * srcX) + sizes, 1, dstX, y, 8, yStride);
                    dstX += step;
                }
                return;
            }
            yStride = this.width;
            v = new int[1];
            dstX = xOffset;
            for (srcX = 0; srcX < width; srcX++) {
                idx = out[srcX];
                if (idx < this.trans.length) {
                    v[0] = this.trans[idx];
                } else {
                    v[0] = 255;
                }
                setPixel(this.smask, v, 0, 1, dstX, y, 8, yStride);
                dstX += step;
            }
        } else if (this.genBWMask) {
            switch (this.colorType) {
                case 0:
                    yStride = (this.width + 7) / 8;
                    v = new int[1];
                    dstX = xOffset;
                    for (srcX = 0; srcX < width; srcX++) {
                        v[0] = out[srcX] == this.transRedGray ? 1 : 0;
                        setPixel(this.smask, v, 0, 1, dstX, y, 1, yStride);
                        dstX += step;
                    }
                    return;
                case 2:
                    yStride = (this.width + 7) / 8;
                    v = new int[1];
                    dstX = xOffset;
                    for (srcX = 0; srcX < width; srcX++) {
                        int markRed = this.inputBands * srcX;
                        i = (out[markRed] == this.transRedGray && out[markRed + 1] == this.transGreen && out[markRed + 2] == this.transBlue) ? 1 : 0;
                        v[0] = i;
                        setPixel(this.smask, v, 0, 1, dstX, y, 1, yStride);
                        dstX += step;
                    }
                    return;
                case 3:
                    yStride = (this.width + 7) / 8;
                    v = new int[1];
                    dstX = xOffset;
                    for (srcX = 0; srcX < width; srcX++) {
                        idx = out[srcX];
                        i = (idx >= this.trans.length || this.trans[idx] != (byte) 0) ? 0 : 1;
                        v[0] = i;
                        setPixel(this.smask, v, 0, 1, dstX, y, 1, yStride);
                        dstX += step;
                    }
                    return;
                default:
                    return;
            }
        }
    }

    static int getPixel(byte[] image, int x, int y, int bitDepth, int bytesPerRow) {
        if (bitDepth == 8) {
            return image[(bytesPerRow * y) + x] & 255;
        }
        return ((1 << bitDepth) - 1) & (image[(bytesPerRow * y) + (x / (8 / bitDepth))] >> ((8 - ((x % (8 / bitDepth)) * bitDepth)) - bitDepth));
    }

    static void setPixel(byte[] image, int[] data, int offset, int size, int x, int y, int bitDepth, int bytesPerRow) {
        int pos;
        int k;
        if (bitDepth == 8) {
            pos = (bytesPerRow * y) + (size * x);
            for (k = 0; k < size; k++) {
                image[pos + k] = (byte) data[k + offset];
            }
        } else if (bitDepth == 16) {
            pos = (bytesPerRow * y) + (size * x);
            for (k = 0; k < size; k++) {
                image[pos + k] = (byte) (data[k + offset] >>> 8);
            }
        } else {
            pos = (bytesPerRow * y) + (x / (8 / bitDepth));
            image[pos] = (byte) (image[pos] | (data[offset] << ((8 - ((x % (8 / bitDepth)) * bitDepth)) - bitDepth)));
        }
    }

    int[] getPixel(byte[] curr) {
        int[] out;
        int k;
        switch (this.bitDepth) {
            case 8:
                out = new int[curr.length];
                for (k = 0; k < out.length; k++) {
                    out[k] = curr[k] & 255;
                }
                return out;
            case 16:
                out = new int[(curr.length / 2)];
                for (k = 0; k < out.length; k++) {
                    out[k] = ((curr[k * 2] & 255) << 8) + (curr[(k * 2) + 1] & 255);
                }
                return out;
            default:
                out = new int[((curr.length * 8) / this.bitDepth)];
                int idx = 0;
                int passes = 8 / this.bitDepth;
                int mask = (1 << this.bitDepth) - 1;
                k = 0;
                while (k < curr.length) {
                    int j = passes - 1;
                    int idx2 = idx;
                    while (j >= 0) {
                        idx = idx2 + 1;
                        out[idx2] = (curr[k] >>> (this.bitDepth * j)) & mask;
                        j--;
                        idx2 = idx;
                    }
                    k++;
                    idx = idx2;
                }
                return out;
        }
    }

    private static void decodeSubFilter(byte[] curr, int count, int bpp) {
        for (int i = bpp; i < count; i++) {
            curr[i] = (byte) ((curr[i] & 255) + (curr[i - bpp] & 255));
        }
    }

    private static void decodeUpFilter(byte[] curr, byte[] prev, int count) {
        for (int i = 0; i < count; i++) {
            curr[i] = (byte) ((curr[i] & 255) + (prev[i] & 255));
        }
    }

    private static void decodeAverageFilter(byte[] curr, byte[] prev, int count, int bpp) {
        int i;
        for (i = 0; i < bpp; i++) {
            curr[i] = (byte) (((prev[i] & 255) / 2) + (curr[i] & 255));
        }
        for (i = bpp; i < count; i++) {
            curr[i] = (byte) ((((curr[i - bpp] & 255) + (prev[i] & 255)) / 2) + (curr[i] & 255));
        }
    }

    private static int paethPredictor(int a, int b, int c) {
        int p = (a + b) - c;
        int pa = Math.abs(p - a);
        int pb = Math.abs(p - b);
        int pc = Math.abs(p - c);
        if (pa <= pb && pa <= pc) {
            return a;
        }
        if (pb <= pc) {
            return b;
        }
        return c;
    }

    private static void decodePaethFilter(byte[] curr, byte[] prev, int count, int bpp) {
        int i;
        for (i = 0; i < bpp; i++) {
            curr[i] = (byte) ((curr[i] & 255) + (prev[i] & 255));
        }
        for (i = bpp; i < count; i++) {
            curr[i] = (byte) (paethPredictor(curr[i - bpp] & 255, prev[i] & 255, prev[i - bpp] & 255) + (curr[i] & 255));
        }
    }

    public static final int getInt(InputStream is) throws IOException {
        return (((is.read() << 24) + (is.read() << 16)) + (is.read() << 8)) + is.read();
    }

    public static final int getWord(InputStream is) throws IOException {
        return (is.read() << 8) + is.read();
    }

    public static final String getString(InputStream is) throws IOException {
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < 4; i++) {
            buf.append((char) is.read());
        }
        return buf.toString();
    }
}
