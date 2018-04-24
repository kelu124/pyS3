package com.itextpdf.text.pdf.codec;

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
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

public class GifImage {
    protected static final int MaxStackSize = 4096;
    protected int bgColor;
    protected int bgIndex;
    protected byte[] block;
    protected int blockSize;
    protected int delay;
    protected int dispose;
    protected ArrayList<GifFrame> frames;
    protected byte[] fromData;
    protected URL fromUrl;
    protected boolean gctFlag;
    protected int height;
    protected int ih;
    protected DataInputStream in;
    protected boolean interlace;
    protected int iw;
    protected int ix;
    protected int iy;
    protected boolean lctFlag;
    protected int lctSize;
    protected int m_bpc;
    protected byte[] m_curr_table;
    protected int m_gbpc;
    protected byte[] m_global_table;
    protected int m_line_stride;
    protected byte[] m_local_table;
    protected byte[] m_out;
    protected int pixelAspect;
    protected byte[] pixelStack;
    protected byte[] pixels;
    protected short[] prefix;
    protected byte[] suffix;
    protected int transIndex;
    protected boolean transparency;
    protected int width;

    static class GifFrame {
        Image image;
        int ix;
        int iy;

        GifFrame() {
        }
    }

    public GifImage(URL url) throws IOException {
        Throwable th;
        this.block = new byte[256];
        this.blockSize = 0;
        this.dispose = 0;
        this.transparency = false;
        this.delay = 0;
        this.frames = new ArrayList();
        this.fromUrl = url;
        InputStream inputStream = null;
        try {
            inputStream = url.openStream();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] bytes = new byte[1024];
            while (true) {
                int read = inputStream.read(bytes);
                if (read == -1) {
                    break;
                }
                baos.write(bytes, 0, read);
            }
            inputStream.close();
            InputStream is = new ByteArrayInputStream(baos.toByteArray());
            try {
                baos.flush();
                baos.close();
                process(is);
                if (is != null) {
                    is.close();
                }
            } catch (Throwable th2) {
                th = th2;
                inputStream = is;
                if (inputStream != null) {
                    inputStream.close();
                }
                throw th;
            }
        } catch (Throwable th3) {
            th = th3;
            if (inputStream != null) {
                inputStream.close();
            }
            throw th;
        }
    }

    public GifImage(String file) throws IOException {
        this(Utilities.toURL(file));
    }

    public GifImage(byte[] data) throws IOException {
        Throwable th;
        this.block = new byte[256];
        this.blockSize = 0;
        this.dispose = 0;
        this.transparency = false;
        this.delay = 0;
        this.frames = new ArrayList();
        this.fromData = data;
        InputStream is = null;
        try {
            InputStream is2 = new ByteArrayInputStream(data);
            try {
                process(is2);
                if (is2 != null) {
                    is2.close();
                }
            } catch (Throwable th2) {
                th = th2;
                is = is2;
                if (is != null) {
                    is.close();
                }
                throw th;
            }
        } catch (Throwable th3) {
            th = th3;
            if (is != null) {
                is.close();
            }
            throw th;
        }
    }

    public GifImage(InputStream is) throws IOException {
        this.block = new byte[256];
        this.blockSize = 0;
        this.dispose = 0;
        this.transparency = false;
        this.delay = 0;
        this.frames = new ArrayList();
        process(is);
    }

    public int getFrameCount() {
        return this.frames.size();
    }

    public Image getImage(int frame) {
        return ((GifFrame) this.frames.get(frame - 1)).image;
    }

    public int[] getFramePosition(int frame) {
        GifFrame gf = (GifFrame) this.frames.get(frame - 1);
        return new int[]{gf.ix, gf.iy};
    }

    public int[] getLogicalScreen() {
        return new int[]{this.width, this.height};
    }

    void process(InputStream is) throws IOException {
        this.in = new DataInputStream(new BufferedInputStream(is));
        readHeader();
        readContents();
        if (this.frames.isEmpty()) {
            throw new IOException(MessageLocalization.getComposedMessage("the.file.does.not.contain.any.valid.image", new Object[0]));
        }
    }

    protected void readHeader() throws IOException {
        StringBuilder id = new StringBuilder("");
        for (int i = 0; i < 6; i++) {
            id.append((char) this.in.read());
        }
        if (id.toString().startsWith("GIF8")) {
            readLSD();
            if (this.gctFlag) {
                this.m_global_table = readColorTable(this.m_gbpc);
                return;
            }
            return;
        }
        throw new IOException(MessageLocalization.getComposedMessage("gif.signature.nor.found", new Object[0]));
    }

    protected void readLSD() throws IOException {
        this.width = readShort();
        this.height = readShort();
        int packed = this.in.read();
        this.gctFlag = (packed & 128) != 0;
        this.m_gbpc = (packed & 7) + 1;
        this.bgIndex = this.in.read();
        this.pixelAspect = this.in.read();
    }

    protected int readShort() throws IOException {
        return this.in.read() | (this.in.read() << 8);
    }

    protected int readBlock() throws IOException {
        this.blockSize = this.in.read();
        if (this.blockSize <= 0) {
            this.blockSize = 0;
            return 0;
        }
        this.blockSize = this.in.read(this.block, 0, this.blockSize);
        return this.blockSize;
    }

    protected byte[] readColorTable(int bpc) throws IOException {
        byte[] table = new byte[((1 << newBpc(bpc)) * 3)];
        this.in.readFully(table, 0, (1 << bpc) * 3);
        return table;
    }

    protected static int newBpc(int bpc) {
        switch (bpc) {
            case 1:
            case 2:
            case 4:
                return bpc;
            case 3:
                return 4;
            default:
                return 8;
        }
    }

    protected void readContents() throws IOException {
        boolean done = false;
        while (!done) {
            switch (this.in.read()) {
                case 33:
                    switch (this.in.read()) {
                        case 249:
                            readGraphicControlExt();
                            break;
                        case 255:
                            readBlock();
                            skip();
                            break;
                        default:
                            skip();
                            break;
                    }
                case 44:
                    readImage();
                    break;
                default:
                    done = true;
                    break;
            }
        }
    }

    protected void readImage() throws IOException {
        boolean z;
        Exception e;
        this.ix = readShort();
        this.iy = readShort();
        this.iw = readShort();
        this.ih = readShort();
        int packed = this.in.read();
        if ((packed & 128) != 0) {
            z = true;
        } else {
            z = false;
        }
        this.lctFlag = z;
        if ((packed & 64) != 0) {
            z = true;
        } else {
            z = false;
        }
        this.interlace = z;
        this.lctSize = 2 << (packed & 7);
        this.m_bpc = newBpc(this.m_gbpc);
        if (this.lctFlag) {
            this.m_curr_table = readColorTable((packed & 7) + 1);
            this.m_bpc = newBpc((packed & 7) + 1);
        } else {
            this.m_curr_table = this.m_global_table;
        }
        if (this.transparency && this.transIndex >= this.m_curr_table.length / 3) {
            this.transparency = false;
        }
        if (this.transparency && this.m_bpc == 1) {
            byte[] tp = new byte[12];
            System.arraycopy(this.m_curr_table, 0, tp, 0, 6);
            this.m_curr_table = tp;
            this.m_bpc = 2;
        }
        if (!decodeImageData()) {
            skip();
        }
        Image img;
        try {
            img = new ImgRaw(this.iw, this.ih, 1, this.m_bpc, this.m_out);
            try {
                PdfArray colorspace = new PdfArray();
                colorspace.add(PdfName.INDEXED);
                colorspace.add(PdfName.DEVICERGB);
                colorspace.add(new PdfNumber((this.m_curr_table.length / 3) - 1));
                colorspace.add(new PdfString(this.m_curr_table));
                PdfDictionary ad = new PdfDictionary();
                ad.put(PdfName.COLORSPACE, colorspace);
                img.setAdditional(ad);
                if (this.transparency) {
                    img.setTransparency(new int[]{this.transIndex, this.transIndex});
                }
                img.setOriginalType(3);
                img.setOriginalData(this.fromData);
                img.setUrl(this.fromUrl);
                GifFrame gf = new GifFrame();
                gf.image = img;
                gf.ix = this.ix;
                gf.iy = this.iy;
                this.frames.add(gf);
            } catch (Exception e2) {
                e = e2;
                throw new ExceptionConverter(e);
            }
        } catch (Exception e3) {
            e = e3;
            img = null;
            throw new ExceptionConverter(e);
        }
    }

    protected boolean decodeImageData() throws IOException {
        int code;
        int top;
        int npix = this.iw * this.ih;
        if (this.prefix == null) {
            this.prefix = new short[4096];
        }
        if (this.suffix == null) {
            this.suffix = new byte[4096];
        }
        if (this.pixelStack == null) {
            this.pixelStack = new byte[4097];
        }
        this.m_line_stride = ((this.iw * this.m_bpc) + 7) / 8;
        this.m_out = new byte[(this.m_line_stride * this.ih)];
        int pass = 1;
        int inc = this.interlace ? 8 : 1;
        int line = 0;
        int xpos = 0;
        int data_size = this.in.read();
        int clear = 1 << data_size;
        int end_of_information = clear + 1;
        int available = clear + 2;
        int old_code = -1;
        int code_size = data_size + 1;
        int code_mask = (1 << code_size) - 1;
        for (code = 0; code < clear; code++) {
            this.prefix[code] = (short) 0;
            this.suffix[code] = (byte) code;
        }
        int bi = 0;
        int first = 0;
        int count = 0;
        int bits = 0;
        int datum = 0;
        int i = 0;
        int top2 = 0;
        while (i < npix) {
            if (top2 != 0) {
                top = top2;
            } else if (bits < code_size) {
                if (count == 0) {
                    count = readBlock();
                    if (count <= 0) {
                        top = top2;
                        return true;
                    }
                    bi = 0;
                }
                datum += (this.block[bi] & 255) << bits;
                bits += 8;
                bi++;
                count--;
            } else {
                code = datum & code_mask;
                datum >>= code_size;
                bits -= code_size;
                if (code > available) {
                    top = top2;
                    return false;
                } else if (code == end_of_information) {
                    top = top2;
                    return false;
                } else if (code == clear) {
                    code_size = data_size + 1;
                    code_mask = (1 << code_size) - 1;
                    available = clear + 2;
                    old_code = -1;
                } else if (old_code == -1) {
                    top = top2 + 1;
                    this.pixelStack[top2] = this.suffix[code];
                    old_code = code;
                    first = code;
                    top2 = top;
                } else {
                    int in_code = code;
                    if (code == available) {
                        top = top2 + 1;
                        this.pixelStack[top2] = (byte) first;
                        code = old_code;
                        top2 = top;
                    }
                    while (code > clear) {
                        top = top2 + 1;
                        this.pixelStack[top2] = this.suffix[code];
                        code = this.prefix[code];
                        top2 = top;
                    }
                    first = this.suffix[code] & 255;
                    if (available >= 4096) {
                        top = top2;
                        return false;
                    }
                    top = top2 + 1;
                    this.pixelStack[top2] = (byte) first;
                    this.prefix[available] = (short) old_code;
                    this.suffix[available] = (byte) first;
                    available++;
                    if ((available & code_mask) == 0 && available < 4096) {
                        code_size++;
                        code_mask += available;
                    }
                    old_code = in_code;
                }
            }
            top--;
            i++;
            setPixel(xpos, line, this.pixelStack[top]);
            xpos++;
            if (xpos >= this.iw) {
                xpos = 0;
                line += inc;
                if (line >= this.ih) {
                    if (this.interlace) {
                        do {
                            pass++;
                            switch (pass) {
                                case 2:
                                    line = 4;
                                    break;
                                case 3:
                                    line = 2;
                                    inc = 4;
                                    break;
                                case 4:
                                    line = 1;
                                    inc = 2;
                                    break;
                                default:
                                    line = this.ih - 1;
                                    inc = 0;
                                    break;
                            }
                        } while (line >= this.ih);
                        top2 = top;
                    } else {
                        line = this.ih - 1;
                        inc = 0;
                        top2 = top;
                    }
                }
            }
            top2 = top;
        }
        top = top2;
        return false;
    }

    protected void setPixel(int x, int y, int v) {
        if (this.m_bpc == 8) {
            this.m_out[x + (this.iw * y)] = (byte) v;
            return;
        }
        int pos = (this.m_line_stride * y) + (x / (8 / this.m_bpc));
        int vout = v << ((8 - (this.m_bpc * (x % (8 / this.m_bpc)))) - this.m_bpc);
        byte[] bArr = this.m_out;
        bArr[pos] = (byte) (bArr[pos] | vout);
    }

    protected void resetFrame() {
    }

    protected void readGraphicControlExt() throws IOException {
        boolean z = true;
        this.in.read();
        int packed = this.in.read();
        this.dispose = (packed & 28) >> 2;
        if (this.dispose == 0) {
            this.dispose = 1;
        }
        if ((packed & 1) == 0) {
            z = false;
        }
        this.transparency = z;
        this.delay = readShort() * 10;
        this.transIndex = this.in.read();
        this.in.read();
    }

    protected void skip() throws IOException {
        do {
            readBlock();
        } while (this.blockSize > 0);
    }
}
