package com.itextpdf.text;

import com.itextpdf.text.error_messages.MessageLocalization;
import com.itextpdf.text.pdf.ICC_Profile;
import com.itextpdf.xmp.XMPError;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import org.apache.poi.hssf.record.PaletteRecord;
import org.apache.poi.hssf.usermodel.HSSFShapeTypes;

public class Jpeg extends Image {
    public static final byte[] JFIF_ID = new byte[]{(byte) 74, (byte) 70, (byte) 73, (byte) 70, (byte) 0};
    public static final int M_APP0 = 224;
    public static final int M_APP2 = 226;
    public static final int M_APPD = 237;
    public static final int M_APPE = 238;
    public static final int NOPARAM_MARKER = 2;
    public static final int[] NOPARAM_MARKERS = new int[]{208, 209, 210, 211, 212, 213, 214, 215, 216, 1};
    public static final int NOT_A_MARKER = -1;
    public static final byte[] PS_8BIM_RESO = new byte[]{PaletteRecord.STANDARD_PALETTE_SIZE, (byte) 66, (byte) 73, (byte) 77, (byte) 3, (byte) -19};
    public static final int UNSUPPORTED_MARKER = 1;
    public static final int[] UNSUPPORTED_MARKERS = new int[]{HSSFShapeTypes.ActionButtonEnd, HSSFShapeTypes.ActionButtonReturn, HSSFShapeTypes.ActionButtonDocument, HSSFShapeTypes.ActionButtonSound, 200, 201, 202, XMPError.BADXMP, 205, 206, 207};
    public static final int VALID_MARKER = 0;
    public static final int[] VALID_MARKERS = new int[]{192, HSSFShapeTypes.ActionButtonForwardNext, HSSFShapeTypes.ActionButtonBackPrevious};
    private byte[][] icc;

    Jpeg(Image image) {
        super(image);
    }

    public Jpeg(URL url) throws BadElementException, IOException {
        super(url);
        processParameters();
    }

    public Jpeg(byte[] img) throws BadElementException, IOException {
        super((URL) null);
        this.rawData = img;
        this.originalData = img;
        processParameters();
    }

    public Jpeg(byte[] img, float width, float height) throws BadElementException, IOException {
        this(img);
        this.scaledWidth = width;
        this.scaledHeight = height;
    }

    private static final int getShort(InputStream is) throws IOException {
        return (is.read() << 8) + is.read();
    }

    private static final int marker(int marker) {
        for (int i : VALID_MARKERS) {
            if (marker == i) {
                return 0;
            }
        }
        for (int i2 : NOPARAM_MARKERS) {
            if (marker == i2) {
                return 2;
            }
        }
        for (int i22 : UNSUPPORTED_MARKERS) {
            if (marker == i22) {
                return 1;
            }
        }
        return -1;
    }

    private void processParameters() throws BadElementException, IOException {
        Throwable th;
        this.type = 32;
        this.originalType = 1;
        InputStream is = null;
        try {
            String errorID;
            if (this.rawData == null) {
                is = this.url.openStream();
                errorID = this.url.toString();
            } else {
                InputStream byteArrayInputStream = new ByteArrayInputStream(this.rawData);
                try {
                    errorID = "Byte array";
                    is = byteArrayInputStream;
                } catch (Throwable th2) {
                    th = th2;
                    is = byteArrayInputStream;
                    if (is != null) {
                        is.close();
                    }
                    throw th;
                }
            }
            if (is.read() == 255 && is.read() == 216) {
                int k;
                boolean firstPass = true;
                while (true) {
                    int v = is.read();
                    if (v < 0) {
                        throw new IOException(MessageLocalization.getComposedMessage("premature.eof.while.reading.jpg", new Object[0]));
                    } else if (v == 255) {
                        int marker = is.read();
                        int len;
                        boolean found;
                        int dx;
                        int dy;
                        if (firstPass && marker == 224) {
                            firstPass = false;
                            len = getShort(is);
                            if (len < 16) {
                                Utilities.skip(is, len - 2);
                            } else {
                                byte[] bcomp = new byte[JFIF_ID.length];
                                if (is.read(bcomp) != bcomp.length) {
                                    throw new BadElementException(MessageLocalization.getComposedMessage("1.corrupted.jfif.marker", errorID));
                                }
                                found = true;
                                for (k = 0; k < bcomp.length; k++) {
                                    if (bcomp[k] != JFIF_ID[k]) {
                                        found = false;
                                        break;
                                    }
                                }
                                if (found) {
                                    Utilities.skip(is, 2);
                                    int units = is.read();
                                    dx = getShort(is);
                                    dy = getShort(is);
                                    if (units == 1) {
                                        this.dpiX = dx;
                                        this.dpiY = dy;
                                    } else if (units == 2) {
                                        this.dpiX = (int) ((((float) dx) * 2.54f) + 0.5f);
                                        this.dpiY = (int) ((((float) dy) * 2.54f) + 0.5f);
                                    }
                                    Utilities.skip(is, ((len - 2) - bcomp.length) - 7);
                                } else {
                                    Utilities.skip(is, (len - 2) - bcomp.length);
                                }
                            }
                        } else if (marker == 238) {
                            len = getShort(is) - 2;
                            byte[] byteappe = new byte[len];
                            for (k = 0; k < len; k++) {
                                byteappe[k] = (byte) is.read();
                            }
                            if (byteappe.length >= 12 && new String(byteappe, 0, 5, "ISO-8859-1").equals("Adobe")) {
                                this.invert = true;
                            }
                        } else if (marker == 226) {
                            len = getShort(is) - 2;
                            byte[] byteapp2 = new byte[len];
                            for (k = 0; k < len; k++) {
                                byteapp2[k] = (byte) is.read();
                            }
                            if (byteapp2.length >= 14 && new String(byteapp2, 0, 11, "ISO-8859-1").equals("ICC_PROFILE")) {
                                int order = byteapp2[12] & 255;
                                int count = byteapp2[13] & 255;
                                if (order < 1) {
                                    order = 1;
                                }
                                if (count < 1) {
                                    count = 1;
                                }
                                if (this.icc == null) {
                                    this.icc = new byte[count][];
                                }
                                this.icc[order - 1] = byteapp2;
                            }
                        } else if (marker == 237) {
                            len = getShort(is) - 2;
                            byte[] byteappd = new byte[len];
                            for (k = 0; k < len; k++) {
                                byteappd[k] = (byte) is.read();
                            }
                            k = 0;
                            while (k < len - PS_8BIM_RESO.length) {
                                found = true;
                                for (int j = 0; j < PS_8BIM_RESO.length; j++) {
                                    if (byteappd[k + j] != PS_8BIM_RESO[j]) {
                                        found = false;
                                        break;
                                    }
                                }
                                if (found) {
                                    break;
                                }
                                k++;
                            }
                            k += PS_8BIM_RESO.length;
                            if (k < len - PS_8BIM_RESO.length) {
                                byte namelength = (byte) (byteappd[k] + 1);
                                if (namelength % 2 == 1) {
                                    namelength = (byte) (namelength + 1);
                                }
                                k += namelength;
                                if ((((byteappd[k] << 24) + (byteappd[k + 1] << 16)) + (byteappd[k + 2] << 8)) + byteappd[k + 3] == 16) {
                                    k += 4;
                                    dx = (byteappd[k] << 8) + (byteappd[k + 1] & 255);
                                    k = (k + 2) + 2;
                                    int unitsx = (byteappd[k] << 8) + (byteappd[k + 1] & 255);
                                    k = (k + 2) + 2;
                                    dy = (byteappd[k] << 8) + (byteappd[k + 1] & 255);
                                    k = (k + 2) + 2;
                                    int unitsy = (byteappd[k] << 8) + (byteappd[k + 1] & 255);
                                    if (unitsx == 1 || unitsx == 2) {
                                        if (unitsx == 2) {
                                            dx = (int) ((((float) dx) * 2.54f) + 0.5f);
                                        }
                                        if (this.dpiX == 0 || this.dpiX == dx) {
                                            this.dpiX = dx;
                                        }
                                    }
                                    if (unitsy == 1 || unitsy == 2) {
                                        if (unitsy == 2) {
                                            dy = (int) ((((float) dy) * 2.54f) + 0.5f);
                                        }
                                        if (this.dpiY == 0 || this.dpiY == dy) {
                                            this.dpiY = dy;
                                        }
                                    }
                                }
                            }
                        } else {
                            firstPass = false;
                            int markertype = marker(marker);
                            if (markertype == 0) {
                                break;
                            } else if (markertype == 1) {
                                throw new BadElementException(MessageLocalization.getComposedMessage("1.unsupported.jpeg.marker.2", errorID, String.valueOf(marker)));
                            } else if (markertype != 2) {
                                Utilities.skip(is, getShort(is) - 2);
                            }
                        }
                    }
                }
                Utilities.skip(is, 2);
                if (is.read() != 8) {
                    throw new BadElementException(MessageLocalization.getComposedMessage("1.must.have.8.bits.per.component", errorID));
                }
                this.scaledHeight = (float) getShort(is);
                setTop(this.scaledHeight);
                this.scaledWidth = (float) getShort(is);
                setRight(this.scaledWidth);
                this.colorspace = is.read();
                this.bpc = 8;
                if (is != null) {
                    is.close();
                }
                this.plainWidth = getWidth();
                this.plainHeight = getHeight();
                if (this.icc != null) {
                    int total = 0;
                    k = 0;
                    while (k < this.icc.length) {
                        if (this.icc[k] == null) {
                            this.icc = (byte[][]) null;
                            return;
                        } else {
                            total += this.icc[k].length - 14;
                            k++;
                        }
                    }
                    byte[] ficc = new byte[total];
                    total = 0;
                    for (k = 0; k < this.icc.length; k++) {
                        System.arraycopy(this.icc[k], 14, ficc, total, this.icc[k].length - 14);
                        total += this.icc[k].length - 14;
                    }
                    try {
                        tagICC(ICC_Profile.getInstance(ficc, this.colorspace));
                    } catch (IllegalArgumentException e) {
                    }
                    this.icc = (byte[][]) null;
                    return;
                }
                return;
            }
            throw new BadElementException(MessageLocalization.getComposedMessage("1.is.not.a.valid.jpeg.file", errorID));
        } catch (Throwable th3) {
            th = th3;
        }
    }
}
