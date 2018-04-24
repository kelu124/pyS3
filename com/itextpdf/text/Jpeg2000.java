package com.itextpdf.text;

import com.itextpdf.text.error_messages.MessageLocalization;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

public class Jpeg2000 extends Image {
    public static final int JP2_BPCC = 1651532643;
    public static final int JP2_COLR = 1668246642;
    public static final int JP2_DBTL = 1685348972;
    public static final int JP2_FTYP = 1718909296;
    public static final int JP2_IHDR = 1768449138;
    public static final int JP2_JP = 1783636000;
    public static final int JP2_JP2 = 1785737760;
    public static final int JP2_JP2C = 1785737827;
    public static final int JP2_JP2H = 1785737832;
    public static final int JP2_URL = 1970433056;
    public static final int JPIP_JPIP = 1785751920;
    int boxLength;
    int boxType;
    byte[] bpcBoxData;
    ArrayList<ColorSpecBox> colorSpecBoxes;
    InputStream inp;
    boolean isJp2;
    int numOfComps;

    public static class ColorSpecBox extends ArrayList<Integer> {
        private byte[] colorProfile;

        public int getMeth() {
            return ((Integer) get(0)).intValue();
        }

        public int getPrec() {
            return ((Integer) get(1)).intValue();
        }

        public int getApprox() {
            return ((Integer) get(2)).intValue();
        }

        public int getEnumCs() {
            return ((Integer) get(3)).intValue();
        }

        public byte[] getColorProfile() {
            return this.colorProfile;
        }

        void setColorProfile(byte[] colorProfile) {
            this.colorProfile = colorProfile;
        }
    }

    private class ZeroBoxSizeException extends IOException {
        public ZeroBoxSizeException(String s) {
            super(s);
        }
    }

    Jpeg2000(Image image) {
        super(image);
        this.colorSpecBoxes = null;
        this.isJp2 = false;
        if (image instanceof Jpeg2000) {
            Jpeg2000 jpeg2000 = (Jpeg2000) image;
            this.numOfComps = jpeg2000.numOfComps;
            if (this.colorSpecBoxes != null) {
                this.colorSpecBoxes = (ArrayList) jpeg2000.colorSpecBoxes.clone();
            }
            this.isJp2 = jpeg2000.isJp2;
            if (this.bpcBoxData != null) {
                this.bpcBoxData = (byte[]) jpeg2000.bpcBoxData.clone();
            }
        }
    }

    public Jpeg2000(URL url) throws BadElementException, IOException {
        super(url);
        this.colorSpecBoxes = null;
        this.isJp2 = false;
        processParameters();
    }

    public Jpeg2000(byte[] img) throws BadElementException, IOException {
        super((URL) null);
        this.colorSpecBoxes = null;
        this.isJp2 = false;
        this.rawData = img;
        this.originalData = img;
        processParameters();
    }

    public Jpeg2000(byte[] img, float width, float height) throws BadElementException, IOException {
        this(img);
        this.scaledWidth = width;
        this.scaledHeight = height;
    }

    private int cio_read(int n) throws IOException {
        int v = 0;
        for (int i = n - 1; i >= 0; i--) {
            v += this.inp.read() << (i << 3);
        }
        return v;
    }

    public void jp2_read_boxhdr() throws IOException {
        this.boxLength = cio_read(4);
        this.boxType = cio_read(4);
        if (this.boxLength == 1) {
            if (cio_read(4) != 0) {
                throw new IOException(MessageLocalization.getComposedMessage("cannot.handle.box.sizes.higher.than.2.32", new Object[0]));
            }
            this.boxLength = cio_read(4);
            if (this.boxLength == 0) {
                throw new IOException(MessageLocalization.getComposedMessage("unsupported.box.size.eq.eq.0", new Object[0]));
            }
        } else if (this.boxLength == 0) {
            throw new ZeroBoxSizeException(MessageLocalization.getComposedMessage("unsupported.box.size.eq.eq.0", new Object[0]));
        }
    }

    private void processParameters() throws IOException {
        this.type = 33;
        this.originalType = 8;
        this.inp = null;
        try {
            if (this.rawData == null) {
                this.inp = this.url.openStream();
            } else {
                this.inp = new ByteArrayInputStream(this.rawData);
            }
            this.boxLength = cio_read(4);
            if (this.boxLength == 12) {
                this.isJp2 = true;
                this.boxType = cio_read(4);
                if (JP2_JP != this.boxType) {
                    throw new IOException(MessageLocalization.getComposedMessage("expected.jp.marker", new Object[0]));
                } else if (218793738 != cio_read(4)) {
                    throw new IOException(MessageLocalization.getComposedMessage("error.with.jp.marker", new Object[0]));
                } else {
                    jp2_read_boxhdr();
                    if (JP2_FTYP != this.boxType) {
                        throw new IOException(MessageLocalization.getComposedMessage("expected.ftyp.marker", new Object[0]));
                    }
                    Utilities.skip(this.inp, this.boxLength - 8);
                    jp2_read_boxhdr();
                    do {
                        if (JP2_JP2H != this.boxType) {
                            if (this.boxType == JP2_JP2C) {
                                throw new IOException(MessageLocalization.getComposedMessage("expected.jp2h.marker", new Object[0]));
                            }
                            Utilities.skip(this.inp, this.boxLength - 8);
                            jp2_read_boxhdr();
                        }
                    } while (JP2_JP2H != this.boxType);
                    jp2_read_boxhdr();
                    if (JP2_IHDR != this.boxType) {
                        throw new IOException(MessageLocalization.getComposedMessage("expected.ihdr.marker", new Object[0]));
                    }
                    this.scaledHeight = (float) cio_read(4);
                    setTop(this.scaledHeight);
                    this.scaledWidth = (float) cio_read(4);
                    setRight(this.scaledWidth);
                    this.numOfComps = cio_read(2);
                    this.bpc = -1;
                    this.bpc = cio_read(1);
                    Utilities.skip(this.inp, 3);
                    jp2_read_boxhdr();
                    if (this.boxType == JP2_BPCC) {
                        this.bpcBoxData = new byte[(this.boxLength - 8)];
                        this.inp.read(this.bpcBoxData, 0, this.boxLength - 8);
                    } else if (this.boxType == JP2_COLR) {
                        do {
                            if (this.colorSpecBoxes == null) {
                                this.colorSpecBoxes = new ArrayList();
                            }
                            this.colorSpecBoxes.add(jp2_read_colr());
                            try {
                                jp2_read_boxhdr();
                            } catch (ZeroBoxSizeException e) {
                            }
                        } while (JP2_COLR == this.boxType);
                    }
                }
            } else if (this.boxLength == -11534511) {
                Utilities.skip(this.inp, 4);
                int x1 = cio_read(4);
                int y1 = cio_read(4);
                int x0 = cio_read(4);
                int y0 = cio_read(4);
                Utilities.skip(this.inp, 16);
                this.colorspace = cio_read(2);
                this.bpc = 8;
                this.scaledHeight = (float) (y1 - y0);
                setTop(this.scaledHeight);
                this.scaledWidth = (float) (x1 - x0);
                setRight(this.scaledWidth);
            } else {
                throw new IOException(MessageLocalization.getComposedMessage("not.a.valid.jpeg2000.file", new Object[0]));
            }
            if (this.inp != null) {
                try {
                    this.inp.close();
                } catch (Exception e2) {
                }
                this.inp = null;
            }
            this.plainWidth = getWidth();
            this.plainHeight = getHeight();
        } catch (Throwable th) {
            if (this.inp != null) {
                try {
                    this.inp.close();
                } catch (Exception e3) {
                }
                this.inp = null;
            }
        }
    }

    private ColorSpecBox jp2_read_colr() throws IOException {
        int readBytes = 8;
        ColorSpecBox colr = new ColorSpecBox();
        for (int i = 0; i < 3; i++) {
            colr.add(Integer.valueOf(cio_read(1)));
            readBytes++;
        }
        if (colr.getMeth() == 1) {
            colr.add(Integer.valueOf(cio_read(4)));
            readBytes += 4;
        } else {
            colr.add(Integer.valueOf(0));
        }
        if (this.boxLength - readBytes > 0) {
            byte[] colorProfile = new byte[(this.boxLength - readBytes)];
            this.inp.read(colorProfile, 0, this.boxLength - readBytes);
            colr.setColorProfile(colorProfile);
        }
        return colr;
    }

    public int getNumOfComps() {
        return this.numOfComps;
    }

    public byte[] getBpcBoxData() {
        return this.bpcBoxData;
    }

    public ArrayList<ColorSpecBox> getColorSpecBoxes() {
        return this.colorSpecBoxes;
    }

    public boolean isJp2() {
        return this.isJp2;
    }
}
