package com.itextpdf.text;

import com.itextpdf.text.api.Indentable;
import com.itextpdf.text.api.Spaceable;
import com.itextpdf.text.error_messages.MessageLocalization;
import com.itextpdf.text.io.RandomAccessSourceFactory;
import com.itextpdf.text.pdf.BaseField;
import com.itextpdf.text.pdf.ICC_Profile;
import com.itextpdf.text.pdf.PRIndirectReference;
import com.itextpdf.text.pdf.PdfArray;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfIndirectReference;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfNumber;
import com.itextpdf.text.pdf.PdfOCG;
import com.itextpdf.text.pdf.PdfObject;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfString;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.RandomAccessFileOrArray;
import com.itextpdf.text.pdf.codec.BmpImage;
import com.itextpdf.text.pdf.codec.CCITTG4Encoder;
import com.itextpdf.text.pdf.codec.GifImage;
import com.itextpdf.text.pdf.codec.JBIG2Image;
import com.itextpdf.text.pdf.codec.PngImage;
import com.itextpdf.text.pdf.codec.TiffImage;
import com.itextpdf.text.pdf.interfaces.IAccessibleElement;
import com.itextpdf.text.pdf.interfaces.IAlternateDescription;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import org.bytedeco.javacpp.avutil;
import org.bytedeco.javacpp.opencv_core;

public abstract class Image extends Rectangle implements Indentable, Spaceable, IAccessibleElement, IAlternateDescription {
    public static final int AX = 0;
    public static final int AY = 1;
    public static final int BX = 2;
    public static final int BY = 3;
    public static final int CX = 4;
    public static final int CY = 5;
    public static final int DEFAULT = 0;
    public static final int DX = 6;
    public static final int DY = 7;
    public static final int LEFT = 0;
    public static final int MIDDLE = 1;
    public static final int ORIGINAL_BMP = 4;
    public static final int ORIGINAL_GIF = 3;
    public static final int ORIGINAL_JBIG2 = 9;
    public static final int ORIGINAL_JPEG = 1;
    public static final int ORIGINAL_JPEG2000 = 8;
    public static final int ORIGINAL_NONE = 0;
    public static final int ORIGINAL_PNG = 2;
    public static final int ORIGINAL_PS = 7;
    public static final int ORIGINAL_TIFF = 5;
    public static final int ORIGINAL_WMF = 6;
    public static final int RIGHT = 2;
    public static final int TEXTWRAP = 4;
    public static final int UNDERLYING = 8;
    static long serialId = 0;
    private float XYRatio;
    protected float absoluteX;
    protected float absoluteY;
    protected HashMap<PdfName, PdfObject> accessibleAttributes;
    private PdfDictionary additional;
    protected int alignment;
    protected String alt;
    protected Annotation annotation;
    protected int bpc;
    protected int colorspace;
    protected int colortransform;
    protected int compressionLevel;
    protected boolean deflated;
    private PdfIndirectReference directReference;
    protected int dpiX;
    protected int dpiY;
    private AccessibleElementId id;
    protected Image imageMask;
    protected float indentationLeft;
    protected float indentationRight;
    private float initialRotation;
    protected boolean interpolation;
    protected boolean invert;
    protected PdfOCG layer;
    protected boolean mask;
    protected Long mySerialId;
    protected byte[] originalData;
    protected int originalType;
    protected float plainHeight;
    protected float plainWidth;
    protected ICC_Profile profile;
    protected byte[] rawData;
    protected PdfName role;
    protected float rotationRadians;
    protected boolean scaleToFitHeight;
    protected boolean scaleToFitLineWhenOverflow;
    protected float scaledHeight;
    protected float scaledWidth;
    private boolean smask;
    protected float spacingAfter;
    protected float spacingBefore;
    protected PdfTemplate[] template;
    protected int[] transparency;
    protected int type;
    protected URL url;
    private float widthPercentage;

    public Image(URL url) {
        super(0.0f, 0.0f);
        this.bpc = 1;
        this.template = new PdfTemplate[1];
        this.absoluteX = Float.NaN;
        this.absoluteY = Float.NaN;
        this.compressionLevel = -1;
        this.mySerialId = getSerialId();
        this.role = PdfName.FIGURE;
        this.accessibleAttributes = null;
        this.id = null;
        this.indentationLeft = 0.0f;
        this.indentationRight = 0.0f;
        this.widthPercentage = 100.0f;
        this.scaleToFitHeight = true;
        this.annotation = null;
        this.originalType = 0;
        this.deflated = false;
        this.dpiX = 0;
        this.dpiY = 0;
        this.XYRatio = 0.0f;
        this.colorspace = -1;
        this.colortransform = 1;
        this.invert = false;
        this.profile = null;
        this.additional = null;
        this.mask = false;
        this.url = url;
        this.alignment = 0;
        this.rotationRadians = 0.0f;
    }

    public static Image getInstance(URL url) throws BadElementException, MalformedURLException, IOException {
        return getInstance(url, false);
    }

    public static Image getInstance(URL url, boolean recoverFromImageError) throws BadElementException, MalformedURLException, IOException {
        Image img;
        InputStream is = null;
        RandomAccessSourceFactory randomAccessSourceFactory = new RandomAccessSourceFactory();
        RandomAccessFileOrArray ra;
        try {
            is = url.openStream();
            int c1 = is.read();
            int c2 = is.read();
            int c3 = is.read();
            int c4 = is.read();
            int c5 = is.read();
            int c6 = is.read();
            int c7 = is.read();
            int c8 = is.read();
            is.close();
            is = null;
            if (c1 == 71 && c2 == 73 && c3 == 70) {
                img = new GifImage(url).getImage(1);
                if (is != null) {
                    is.close();
                }
            } else if (c1 == 255 && c2 == 216) {
                img = new Jpeg(url);
                if (is != null) {
                    is.close();
                }
            } else if (c1 == 0 && c2 == 0 && c3 == 0 && c4 == 12) {
                img = new Jpeg2000(url);
                if (is != null) {
                    is.close();
                }
            } else if (c1 == 255 && c2 == 79 && c3 == 255 && c4 == 81) {
                img = new Jpeg2000(url);
                if (is != null) {
                    is.close();
                }
            } else if (c1 == PngImage.PNGID[0] && c2 == PngImage.PNGID[1] && c3 == PngImage.PNGID[2] && c4 == PngImage.PNGID[3]) {
                img = PngImage.getImage(url);
                if (is != null) {
                    is.close();
                }
            } else if (c1 == 215 && c2 == 205) {
                img = new ImgWMF(url);
                if (is != null) {
                    is.close();
                }
            } else if (c1 == 66 && c2 == 77) {
                img = BmpImage.getImage(url);
                if (is != null) {
                    is.close();
                }
            } else if ((c1 == 77 && c2 == 77 && c3 == 0 && c4 == 42) || (c1 == 73 && c2 == 73 && c3 == 42 && c4 == 0)) {
                ra = null;
                if (url.getProtocol().equals(Annotation.FILE)) {
                    ra = new RandomAccessFileOrArray(randomAccessSourceFactory.createBestSource(Utilities.unEscapeURL(url.getFile())));
                } else {
                    ra = new RandomAccessFileOrArray(randomAccessSourceFactory.createSource(url));
                }
                img = TiffImage.getTiffImage(ra, 1);
                img.url = url;
                if (ra != null) {
                    ra.close();
                }
                if (is != null) {
                    is.close();
                }
            } else if (c1 == 151 && c2 == 74 && c3 == 66 && c4 == 50 && c5 == 13 && c6 == 10 && c7 == 26 && c8 == 10) {
                ra = null;
                if (url.getProtocol().equals(Annotation.FILE)) {
                    ra = new RandomAccessFileOrArray(randomAccessSourceFactory.createBestSource(Utilities.unEscapeURL(url.getFile())));
                } else {
                    ra = new RandomAccessFileOrArray(randomAccessSourceFactory.createSource(url));
                }
                img = JBIG2Image.getJbig2Image(ra, 1);
                img.url = url;
                if (ra != null) {
                    ra.close();
                }
                if (is != null) {
                    is.close();
                }
            } else {
                throw new IOException(MessageLocalization.getComposedMessage("unknown.image.format", url.toString()));
            }
        } catch (RuntimeException e) {
            if (recoverFromImageError) {
                img = TiffImage.getTiffImage(ra, recoverFromImageError, 1);
                img.url = url;
                if (ra != null) {
                    ra.close();
                }
                if (is != null) {
                    is.close();
                }
            } else {
                throw e;
            }
        } catch (Throwable th) {
            if (is != null) {
                is.close();
            }
        }
        return img;
    }

    public static Image getInstance(String filename) throws BadElementException, MalformedURLException, IOException {
        return getInstance(Utilities.toURL(filename));
    }

    public static Image getInstance(String filename, boolean recoverFromImageError) throws IOException, BadElementException {
        return getInstance(Utilities.toURL(filename), recoverFromImageError);
    }

    public static Image getInstance(byte[] imgb) throws BadElementException, MalformedURLException, IOException {
        return getInstance(imgb, false);
    }

    public static Image getInstance(byte[] imgb, boolean recoverFromImageError) throws BadElementException, MalformedURLException, IOException {
        RandomAccessFileOrArray ra;
        RandomAccessFileOrArray randomAccessFileOrArray;
        RuntimeException e;
        Throwable th;
        InputStream inputStream = null;
        RandomAccessSourceFactory randomAccessSourceFactory = new RandomAccessSourceFactory();
        InputStream is = new ByteArrayInputStream(imgb);
        try {
            Image image;
            int c1 = is.read();
            int c2 = is.read();
            int c3 = is.read();
            int c4 = is.read();
            is.close();
            inputStream = null;
            if (c1 == 71 && c2 == 73 && c3 == 70) {
                image = new GifImage(imgb).getImage(1);
                if (inputStream != null) {
                    inputStream.close();
                }
            } else if (c1 == 255 && c2 == 216) {
                image = new Jpeg(imgb);
                if (inputStream != null) {
                    inputStream.close();
                }
            } else if (c1 == 0 && c2 == 0 && c3 == 0 && c4 == 12) {
                image = new Jpeg2000(imgb);
                if (inputStream != null) {
                    inputStream.close();
                }
            } else if (c1 == 255 && c2 == 79 && c3 == 255 && c4 == 81) {
                image = new Jpeg2000(imgb);
                if (inputStream != null) {
                    inputStream.close();
                }
            } else {
                try {
                    if (c1 == PngImage.PNGID[0] && c2 == PngImage.PNGID[1] && c3 == PngImage.PNGID[2] && c4 == PngImage.PNGID[3]) {
                        image = PngImage.getImage(imgb);
                        if (inputStream != null) {
                            inputStream.close();
                        }
                    } else if (c1 == 215 && c2 == 205) {
                        image = new ImgWMF(imgb);
                        if (inputStream != null) {
                            inputStream.close();
                        }
                    } else if (c1 == 66 && c2 == 77) {
                        image = BmpImage.getImage(imgb);
                        if (inputStream != null) {
                            inputStream.close();
                        }
                    } else if ((c1 == 77 && c2 == 77 && c3 == 0 && c4 == 42) || (c1 == 73 && c2 == 73 && c3 == 42 && c4 == 0)) {
                        ra = null;
                        try {
                            randomAccessFileOrArray = new RandomAccessFileOrArray(randomAccessSourceFactory.createSource(imgb));
                            try {
                                image = TiffImage.getTiffImage(randomAccessFileOrArray, 1);
                                if (image.getOriginalData() == null) {
                                    image.setOriginalData(imgb);
                                }
                                if (randomAccessFileOrArray != null) {
                                    randomAccessFileOrArray.close();
                                }
                                if (inputStream != null) {
                                    inputStream.close();
                                }
                            } catch (RuntimeException e2) {
                                e = e2;
                                ra = randomAccessFileOrArray;
                                if (recoverFromImageError) {
                                    throw e;
                                } else {
                                    try {
                                        image = TiffImage.getTiffImage(ra, recoverFromImageError, 1);
                                        if (image.getOriginalData() == null) {
                                            image.setOriginalData(imgb);
                                        }
                                        if (ra != null) {
                                            ra.close();
                                        }
                                        if (inputStream != null) {
                                            inputStream.close();
                                        }
                                        return image;
                                    } catch (Throwable th2) {
                                        th = th2;
                                    }
                                }
                            } catch (Throwable th3) {
                                th = th3;
                                ra = randomAccessFileOrArray;
                                if (ra != null) {
                                    ra.close();
                                }
                                throw th;
                            }
                        } catch (RuntimeException e3) {
                            e = e3;
                            if (recoverFromImageError) {
                                throw e;
                            } else {
                                image = TiffImage.getTiffImage(ra, recoverFromImageError, 1);
                                if (image.getOriginalData() == null) {
                                    image.setOriginalData(imgb);
                                }
                                if (ra != null) {
                                    ra.close();
                                }
                                if (inputStream != null) {
                                    inputStream.close();
                                }
                                return image;
                            }
                        }
                    } else {
                        if (c1 == 151 && c2 == 74 && c3 == 66 && c4 == 50) {
                            is = new ByteArrayInputStream(imgb);
                            is.skip(4);
                            int c5 = is.read();
                            int c6 = is.read();
                            int c7 = is.read();
                            int c8 = is.read();
                            is.close();
                            if (c5 == 13 && c6 == 10 && c7 == 26 && c8 == 10) {
                                ra = null;
                                try {
                                    randomAccessFileOrArray = new RandomAccessFileOrArray(randomAccessSourceFactory.createSource(imgb));
                                    try {
                                        image = JBIG2Image.getJbig2Image(randomAccessFileOrArray, 1);
                                        if (image.getOriginalData() == null) {
                                            image.setOriginalData(imgb);
                                        }
                                        if (randomAccessFileOrArray != null) {
                                            randomAccessFileOrArray.close();
                                        }
                                        if (is != null) {
                                            is.close();
                                        }
                                        inputStream = is;
                                    } catch (Throwable th4) {
                                        th = th4;
                                        ra = randomAccessFileOrArray;
                                        if (ra != null) {
                                            ra.close();
                                        }
                                        throw th;
                                    }
                                } catch (Throwable th5) {
                                    th = th5;
                                    if (ra != null) {
                                        ra.close();
                                    }
                                    throw th;
                                }
                            }
                            inputStream = is;
                        }
                        throw new IOException(MessageLocalization.getComposedMessage("the.byte.array.is.not.a.recognized.imageformat", new Object[0]));
                    }
                } catch (Throwable th6) {
                    th = th6;
                    if (inputStream != null) {
                        inputStream.close();
                    }
                    throw th;
                }
            }
            return image;
        } catch (Throwable th7) {
            th = th7;
            inputStream = is;
            if (inputStream != null) {
                inputStream.close();
            }
            throw th;
        }
    }

    public static Image getInstance(int width, int height, int components, int bpc, byte[] data) throws BadElementException {
        return getInstance(width, height, components, bpc, data, null);
    }

    public static Image getInstance(int width, int height, byte[] data, byte[] globals) {
        return new ImgJBIG2(width, height, data, globals);
    }

    public static Image getInstance(int width, int height, boolean reverseBits, int typeCCITT, int parameters, byte[] data) throws BadElementException {
        return getInstance(width, height, reverseBits, typeCCITT, parameters, data, null);
    }

    public static Image getInstance(int width, int height, boolean reverseBits, int typeCCITT, int parameters, byte[] data, int[] transparency) throws BadElementException {
        if (transparency == null || transparency.length == 2) {
            Image img = new ImgCCITT(width, height, reverseBits, typeCCITT, parameters, data);
            img.transparency = transparency;
            return img;
        }
        throw new BadElementException(MessageLocalization.getComposedMessage("transparency.length.must.be.equal.to.2.with.ccitt.images", new Object[0]));
    }

    public static Image getInstance(int width, int height, int components, int bpc, byte[] data, int[] transparency) throws BadElementException {
        if (transparency != null && transparency.length != components * 2) {
            throw new BadElementException(MessageLocalization.getComposedMessage("transparency.length.must.be.equal.to.componentes.2", new Object[0]));
        } else if (components == 1 && bpc == 1) {
            return getInstance(width, height, false, 256, 1, CCITTG4Encoder.compress(data, width, height), transparency);
        } else {
            Image img = new ImgRaw(width, height, components, bpc, data);
            img.transparency = transparency;
            return img;
        }
    }

    public static Image getInstance(PdfTemplate template) throws BadElementException {
        return new ImgTemplate(template);
    }

    public PdfIndirectReference getDirectReference() {
        return this.directReference;
    }

    public void setDirectReference(PdfIndirectReference directReference) {
        this.directReference = directReference;
    }

    public static Image getInstance(PRIndirectReference ref) throws BadElementException {
        PdfDictionary dic = (PdfDictionary) PdfReader.getPdfObjectRelease((PdfObject) ref);
        int width = ((PdfNumber) PdfReader.getPdfObjectRelease(dic.get(PdfName.WIDTH))).intValue();
        int height = ((PdfNumber) PdfReader.getPdfObjectRelease(dic.get(PdfName.HEIGHT))).intValue();
        Image imask = null;
        PdfObject obj = dic.get(PdfName.SMASK);
        if (obj == null || !obj.isIndirect()) {
            obj = dic.get(PdfName.MASK);
            if (obj != null && obj.isIndirect() && (PdfReader.getPdfObjectRelease(obj) instanceof PdfDictionary)) {
                imask = getInstance((PRIndirectReference) obj);
            }
        } else {
            imask = getInstance((PRIndirectReference) obj);
        }
        Image img = new ImgRaw(width, height, 1, 1, null);
        img.imageMask = imask;
        img.directReference = ref;
        return img;
    }

    protected Image(Image image) {
        super((Rectangle) image);
        this.bpc = 1;
        this.template = new PdfTemplate[1];
        this.absoluteX = Float.NaN;
        this.absoluteY = Float.NaN;
        this.compressionLevel = -1;
        this.mySerialId = getSerialId();
        this.role = PdfName.FIGURE;
        this.accessibleAttributes = null;
        this.id = null;
        this.indentationLeft = 0.0f;
        this.indentationRight = 0.0f;
        this.widthPercentage = 100.0f;
        this.scaleToFitHeight = true;
        this.annotation = null;
        this.originalType = 0;
        this.deflated = false;
        this.dpiX = 0;
        this.dpiY = 0;
        this.XYRatio = 0.0f;
        this.colorspace = -1;
        this.colortransform = 1;
        this.invert = false;
        this.profile = null;
        this.additional = null;
        this.mask = false;
        this.type = image.type;
        this.url = image.url;
        this.rawData = image.rawData;
        this.bpc = image.bpc;
        this.template = image.template;
        this.alignment = image.alignment;
        this.alt = image.alt;
        this.absoluteX = image.absoluteX;
        this.absoluteY = image.absoluteY;
        this.plainWidth = image.plainWidth;
        this.plainHeight = image.plainHeight;
        this.scaledWidth = image.scaledWidth;
        this.scaledHeight = image.scaledHeight;
        this.mySerialId = image.mySerialId;
        this.directReference = image.directReference;
        this.rotationRadians = image.rotationRadians;
        this.initialRotation = image.initialRotation;
        this.indentationLeft = image.indentationLeft;
        this.indentationRight = image.indentationRight;
        this.spacingBefore = image.spacingBefore;
        this.spacingAfter = image.spacingAfter;
        this.widthPercentage = image.widthPercentage;
        this.scaleToFitLineWhenOverflow = image.scaleToFitLineWhenOverflow;
        this.scaleToFitHeight = image.scaleToFitHeight;
        this.annotation = image.annotation;
        this.layer = image.layer;
        this.interpolation = image.interpolation;
        this.originalType = image.originalType;
        this.originalData = image.originalData;
        this.deflated = image.deflated;
        this.dpiX = image.dpiX;
        this.dpiY = image.dpiY;
        this.XYRatio = image.XYRatio;
        this.colorspace = image.colorspace;
        this.invert = image.invert;
        this.profile = image.profile;
        this.additional = image.additional;
        this.mask = image.mask;
        this.imageMask = image.imageMask;
        this.smask = image.smask;
        this.transparency = image.transparency;
        this.role = image.role;
        if (image.accessibleAttributes != null) {
            this.accessibleAttributes = new HashMap(image.accessibleAttributes);
        }
        setId(image.getId());
    }

    public static Image getInstance(Image image) {
        if (image == null) {
            return null;
        }
        try {
            return (Image) image.getClass().getDeclaredConstructor(new Class[]{Image.class}).newInstance(new Object[]{image});
        } catch (Exception e) {
            throw new ExceptionConverter(e);
        }
    }

    public int type() {
        return this.type;
    }

    public boolean isNestable() {
        return true;
    }

    public boolean isJpeg() {
        return this.type == 32;
    }

    public boolean isImgRaw() {
        return this.type == 34;
    }

    public boolean isImgTemplate() {
        return this.type == 35;
    }

    public URL getUrl() {
        return this.url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public byte[] getRawData() {
        return this.rawData;
    }

    public int getBpc() {
        return this.bpc;
    }

    public PdfTemplate getTemplateData() {
        return this.template[0];
    }

    public void setTemplateData(PdfTemplate template) {
        this.template[0] = template;
    }

    public int getAlignment() {
        return this.alignment;
    }

    public void setAlignment(int alignment) {
        this.alignment = alignment;
    }

    public String getAlt() {
        return this.alt;
    }

    public void setAlt(String alt) {
        this.alt = alt;
        setAccessibleAttribute(PdfName.ALT, new PdfString(alt));
    }

    public void setAbsolutePosition(float absoluteX, float absoluteY) {
        this.absoluteX = absoluteX;
        this.absoluteY = absoluteY;
    }

    public boolean hasAbsoluteX() {
        return !Float.isNaN(this.absoluteX);
    }

    public float getAbsoluteX() {
        return this.absoluteX;
    }

    public boolean hasAbsoluteY() {
        return !Float.isNaN(this.absoluteY);
    }

    public float getAbsoluteY() {
        return this.absoluteY;
    }

    public float getScaledWidth() {
        return this.scaledWidth;
    }

    public float getScaledHeight() {
        return this.scaledHeight;
    }

    public float getPlainWidth() {
        return this.plainWidth;
    }

    public float getPlainHeight() {
        return this.plainHeight;
    }

    public void scaleAbsolute(Rectangle rectangle) {
        scaleAbsolute(rectangle.getWidth(), rectangle.getHeight());
    }

    public void scaleAbsolute(float newWidth, float newHeight) {
        this.plainWidth = newWidth;
        this.plainHeight = newHeight;
        float[] matrix = matrix();
        this.scaledWidth = matrix[6] - matrix[4];
        this.scaledHeight = matrix[7] - matrix[5];
        setWidthPercentage(0.0f);
    }

    public void scaleAbsoluteWidth(float newWidth) {
        this.plainWidth = newWidth;
        float[] matrix = matrix();
        this.scaledWidth = matrix[6] - matrix[4];
        this.scaledHeight = matrix[7] - matrix[5];
        setWidthPercentage(0.0f);
    }

    public void scaleAbsoluteHeight(float newHeight) {
        this.plainHeight = newHeight;
        float[] matrix = matrix();
        this.scaledWidth = matrix[6] - matrix[4];
        this.scaledHeight = matrix[7] - matrix[5];
        setWidthPercentage(0.0f);
    }

    public void scalePercent(float percent) {
        scalePercent(percent, percent);
    }

    public void scalePercent(float percentX, float percentY) {
        this.plainWidth = (getWidth() * percentX) / 100.0f;
        this.plainHeight = (getHeight() * percentY) / 100.0f;
        float[] matrix = matrix();
        this.scaledWidth = matrix[6] - matrix[4];
        this.scaledHeight = matrix[7] - matrix[5];
        setWidthPercentage(0.0f);
    }

    public void scaleToFit(Rectangle rectangle) {
        scaleToFit(rectangle.getWidth(), rectangle.getHeight());
    }

    public void scaleToFit(float fitWidth, float fitHeight) {
        scalePercent(100.0f);
        float percentX = (fitWidth * 100.0f) / getScaledWidth();
        float percentY = (fitHeight * 100.0f) / getScaledHeight();
        if (percentX >= percentY) {
            percentX = percentY;
        }
        scalePercent(percentX);
        setWidthPercentage(0.0f);
    }

    public float[] matrix() {
        return matrix(BaseField.BORDER_WIDTH_THIN);
    }

    public float[] matrix(float scalePercentage) {
        float[] matrix = new float[8];
        float cosX = (float) Math.cos((double) this.rotationRadians);
        float sinX = (float) Math.sin((double) this.rotationRadians);
        matrix[0] = (this.plainWidth * cosX) * scalePercentage;
        matrix[1] = (this.plainWidth * sinX) * scalePercentage;
        matrix[2] = ((-this.plainHeight) * sinX) * scalePercentage;
        matrix[3] = (this.plainHeight * cosX) * scalePercentage;
        if (((double) this.rotationRadians) < avutil.M_PI_2) {
            matrix[4] = matrix[2];
            matrix[5] = 0.0f;
            matrix[6] = matrix[0];
            matrix[7] = matrix[1] + matrix[3];
        } else if (((double) this.rotationRadians) < 3.141592653589793d) {
            matrix[4] = matrix[0] + matrix[2];
            matrix[5] = matrix[3];
            matrix[6] = 0.0f;
            matrix[7] = matrix[1];
        } else if (((double) this.rotationRadians) < 4.71238898038469d) {
            matrix[4] = matrix[0];
            matrix[5] = matrix[1] + matrix[3];
            matrix[6] = matrix[2];
            matrix[7] = 0.0f;
        } else {
            matrix[4] = 0.0f;
            matrix[5] = matrix[1];
            matrix[6] = matrix[0] + matrix[2];
            matrix[7] = matrix[3];
        }
        return matrix;
    }

    protected static synchronized Long getSerialId() {
        Long valueOf;
        synchronized (Image.class) {
            serialId++;
            valueOf = Long.valueOf(serialId);
        }
        return valueOf;
    }

    public Long getMySerialId() {
        return this.mySerialId;
    }

    public float getImageRotation() {
        float rot = (float) (((double) (this.rotationRadians - this.initialRotation)) % opencv_core.CV_2PI);
        if (rot < 0.0f) {
            return (float) (((double) rot) + opencv_core.CV_2PI);
        }
        return rot;
    }

    public void setRotation(float r) {
        this.rotationRadians = (float) (((double) (this.initialRotation + r)) % opencv_core.CV_2PI);
        if (this.rotationRadians < 0.0f) {
            this.rotationRadians = (float) (((double) this.rotationRadians) + opencv_core.CV_2PI);
        }
        float[] matrix = matrix();
        this.scaledWidth = matrix[6] - matrix[4];
        this.scaledHeight = matrix[7] - matrix[5];
    }

    public void setRotationDegrees(float deg) {
        setRotation((deg / 180.0f) * ((float) 3.141592653589793d));
    }

    public float getInitialRotation() {
        return this.initialRotation;
    }

    public void setInitialRotation(float initialRotation) {
        float old_rot = this.rotationRadians - this.initialRotation;
        this.initialRotation = initialRotation;
        setRotation(old_rot);
    }

    public float getIndentationLeft() {
        return this.indentationLeft;
    }

    public void setIndentationLeft(float f) {
        this.indentationLeft = f;
    }

    public float getIndentationRight() {
        return this.indentationRight;
    }

    public void setIndentationRight(float f) {
        this.indentationRight = f;
    }

    public float getSpacingBefore() {
        return this.spacingBefore;
    }

    public void setSpacingBefore(float spacing) {
        this.spacingBefore = spacing;
    }

    public float getSpacingAfter() {
        return this.spacingAfter;
    }

    public void setSpacingAfter(float spacing) {
        this.spacingAfter = spacing;
    }

    public float getWidthPercentage() {
        return this.widthPercentage;
    }

    public void setWidthPercentage(float widthPercentage) {
        this.widthPercentage = widthPercentage;
    }

    public boolean isScaleToFitLineWhenOverflow() {
        return this.scaleToFitLineWhenOverflow;
    }

    public void setScaleToFitLineWhenOverflow(boolean scaleToFitLineWhenOverflow) {
        this.scaleToFitLineWhenOverflow = scaleToFitLineWhenOverflow;
    }

    public boolean isScaleToFitHeight() {
        return this.scaleToFitHeight;
    }

    public void setScaleToFitHeight(boolean scaleToFitHeight) {
        this.scaleToFitHeight = scaleToFitHeight;
    }

    public void setAnnotation(Annotation annotation) {
        this.annotation = annotation;
    }

    public Annotation getAnnotation() {
        return this.annotation;
    }

    public PdfOCG getLayer() {
        return this.layer;
    }

    public void setLayer(PdfOCG layer) {
        this.layer = layer;
    }

    public boolean isInterpolation() {
        return this.interpolation;
    }

    public void setInterpolation(boolean interpolation) {
        this.interpolation = interpolation;
    }

    public int getOriginalType() {
        return this.originalType;
    }

    public void setOriginalType(int originalType) {
        this.originalType = originalType;
    }

    public byte[] getOriginalData() {
        return this.originalData;
    }

    public void setOriginalData(byte[] originalData) {
        this.originalData = originalData;
    }

    public boolean isDeflated() {
        return this.deflated;
    }

    public void setDeflated(boolean deflated) {
        this.deflated = deflated;
    }

    public int getDpiX() {
        return this.dpiX;
    }

    public int getDpiY() {
        return this.dpiY;
    }

    public void setDpi(int dpiX, int dpiY) {
        this.dpiX = dpiX;
        this.dpiY = dpiY;
    }

    public float getXYRatio() {
        return this.XYRatio;
    }

    public void setXYRatio(float XYRatio) {
        this.XYRatio = XYRatio;
    }

    public int getColorspace() {
        return this.colorspace;
    }

    public void setColorTransform(int c) {
        this.colortransform = c;
    }

    public int getColorTransform() {
        return this.colortransform;
    }

    public boolean isInverted() {
        return this.invert;
    }

    public void setInverted(boolean invert) {
        this.invert = invert;
    }

    public void tagICC(ICC_Profile profile) {
        this.profile = profile;
    }

    public boolean hasICCProfile() {
        return this.profile != null;
    }

    public ICC_Profile getICCProfile() {
        return this.profile;
    }

    public PdfDictionary getAdditional() {
        return this.additional;
    }

    public void setAdditional(PdfDictionary additional) {
        this.additional = additional;
    }

    public void simplifyColorspace() {
        if (this.additional != null) {
            PdfObject value = this.additional.getAsArray(PdfName.COLORSPACE);
            if (value != null) {
                PdfObject newValue;
                PdfObject cs = simplifyColorspace(value);
                if (cs.isName()) {
                    newValue = cs;
                } else {
                    newValue = value;
                    if (PdfName.INDEXED.equals(value.getAsName(0)) && value.size() >= 2) {
                        PdfArray second = value.getAsArray(1);
                        if (second != null) {
                            value.set(1, simplifyColorspace(second));
                        }
                    }
                }
                this.additional.put(PdfName.COLORSPACE, newValue);
            }
        }
    }

    private PdfObject simplifyColorspace(PdfArray obj) {
        if (obj == null) {
            return obj;
        }
        PdfName first = obj.getAsName(0);
        if (PdfName.CALGRAY.equals(first)) {
            return PdfName.DEVICEGRAY;
        }
        if (PdfName.CALRGB.equals(first)) {
            return PdfName.DEVICERGB;
        }
        return obj;
    }

    public boolean isMask() {
        return this.mask;
    }

    public void makeMask() throws DocumentException {
        if (isMaskCandidate()) {
            this.mask = true;
            return;
        }
        throw new DocumentException(MessageLocalization.getComposedMessage("this.image.can.not.be.an.image.mask", new Object[0]));
    }

    public boolean isMaskCandidate() {
        if ((this.type != 34 || this.bpc <= 255) && this.colorspace != 1) {
            return false;
        }
        return true;
    }

    public Image getImageMask() {
        return this.imageMask;
    }

    public void setImageMask(Image mask) throws DocumentException {
        boolean z = true;
        if (this.mask) {
            throw new DocumentException(MessageLocalization.getComposedMessage("an.image.mask.cannot.contain.another.image.mask", new Object[0]));
        } else if (mask.mask) {
            this.imageMask = mask;
            if (mask.bpc <= 1 || mask.bpc > 8) {
                z = false;
            }
            this.smask = z;
        } else {
            throw new DocumentException(MessageLocalization.getComposedMessage("the.image.mask.is.not.a.mask.did.you.do.makemask", new Object[0]));
        }
    }

    public boolean isSmask() {
        return this.smask;
    }

    public void setSmask(boolean smask) {
        this.smask = smask;
    }

    public int[] getTransparency() {
        return this.transparency;
    }

    public void setTransparency(int[] transparency) {
        this.transparency = transparency;
    }

    public int getCompressionLevel() {
        return this.compressionLevel;
    }

    public void setCompressionLevel(int compressionLevel) {
        if (compressionLevel < 0 || compressionLevel > 9) {
            this.compressionLevel = -1;
        } else {
            this.compressionLevel = compressionLevel;
        }
    }

    public PdfObject getAccessibleAttribute(PdfName key) {
        if (this.accessibleAttributes != null) {
            return (PdfObject) this.accessibleAttributes.get(key);
        }
        return null;
    }

    public void setAccessibleAttribute(PdfName key, PdfObject value) {
        if (this.accessibleAttributes == null) {
            this.accessibleAttributes = new HashMap();
        }
        this.accessibleAttributes.put(key, value);
    }

    public HashMap<PdfName, PdfObject> getAccessibleAttributes() {
        return this.accessibleAttributes;
    }

    public PdfName getRole() {
        return this.role;
    }

    public void setRole(PdfName role) {
        this.role = role;
    }

    public AccessibleElementId getId() {
        if (this.id == null) {
            this.id = new AccessibleElementId();
        }
        return this.id;
    }

    public void setId(AccessibleElementId id) {
        this.id = id;
    }

    public boolean isInline() {
        return true;
    }
}
