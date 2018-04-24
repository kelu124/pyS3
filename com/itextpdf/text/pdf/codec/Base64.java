package com.itextpdf.text.pdf.codec;

import android.support.v4.view.MotionEventCompat;
import com.google.common.base.Ascii;
import com.itextpdf.text.DocWriter;
import com.itextpdf.text.error_messages.MessageLocalization;
import com.itextpdf.text.pdf.ByteBuffer;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilterInputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import org.apache.poi.hssf.record.PaletteRecord;
import org.apache.poi.ss.formula.ptg.Area3DPtg;
import org.apache.poi.ss.formula.ptg.AreaErrPtg;
import org.apache.poi.ss.formula.ptg.MemFuncPtg;
import org.apache.poi.ss.formula.ptg.Ref3DPtg;
import org.apache.poi.ss.formula.ptg.RefErrorPtg;
import org.apache.poi.ss.formula.ptg.RefNPtg;
import org.apache.poi.ss.formula.ptg.RefPtg;

public class Base64 {
    public static final int DECODE = 0;
    public static final int DONT_BREAK_LINES = 8;
    public static final int ENCODE = 1;
    private static final byte EQUALS_SIGN = (byte) 61;
    private static final byte EQUALS_SIGN_ENC = (byte) -1;
    public static final int GZIP = 2;
    private static final int MAX_LINE_LENGTH = 76;
    private static final byte NEW_LINE = (byte) 10;
    public static final int NO_OPTIONS = 0;
    public static final int ORDERED = 32;
    private static final String PREFERRED_ENCODING = "UTF-8";
    public static final int URL_SAFE = 16;
    private static final byte WHITE_SPACE_ENC = (byte) -5;
    private static final byte[] _ORDERED_ALPHABET = new byte[]{(byte) 45, ByteBuffer.ZERO, (byte) 49, (byte) 50, (byte) 51, (byte) 52, (byte) 53, (byte) 54, (byte) 55, PaletteRecord.STANDARD_PALETTE_SIZE, (byte) 57, (byte) 65, (byte) 66, (byte) 67, (byte) 68, (byte) 69, (byte) 70, (byte) 71, (byte) 72, (byte) 73, (byte) 74, (byte) 75, (byte) 76, (byte) 77, (byte) 78, (byte) 79, (byte) 80, (byte) 81, (byte) 82, (byte) 83, (byte) 84, (byte) 85, (byte) 86, (byte) 87, (byte) 88, (byte) 89, (byte) 90, (byte) 95, (byte) 97, (byte) 98, (byte) 99, (byte) 100, (byte) 101, (byte) 102, (byte) 103, (byte) 104, (byte) 105, (byte) 106, (byte) 107, (byte) 108, (byte) 109, (byte) 110, (byte) 111, (byte) 112, (byte) 113, (byte) 114, (byte) 115, (byte) 116, (byte) 117, (byte) 118, (byte) 119, (byte) 120, (byte) 121, (byte) 122};
    private static final byte[] _ORDERED_DECODABET = new byte[]{(byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, WHITE_SPACE_ENC, WHITE_SPACE_ENC, (byte) -9, (byte) -9, WHITE_SPACE_ENC, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, WHITE_SPACE_ENC, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) 0, (byte) -9, (byte) -9, (byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8, (byte) 9, (byte) 10, (byte) -9, (byte) -9, (byte) -9, (byte) -1, (byte) -9, (byte) -9, (byte) -9, (byte) 11, (byte) 12, (byte) 13, (byte) 14, (byte) 15, (byte) 16, (byte) 17, (byte) 18, (byte) 19, (byte) 20, (byte) 21, (byte) 22, (byte) 23, Ascii.CAN, (byte) 25, Ascii.SUB, Ascii.ESC, Ascii.FS, (byte) 29, (byte) 30, (byte) 31, (byte) 32, (byte) 33, (byte) 34, (byte) 35, RefPtg.sid, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) 37, (byte) -9, (byte) 38, (byte) 39, (byte) 40, MemFuncPtg.sid, RefErrorPtg.sid, AreaErrPtg.sid, RefNPtg.sid, (byte) 45, (byte) 46, DocWriter.FORWARD, ByteBuffer.ZERO, (byte) 49, (byte) 50, (byte) 51, (byte) 52, (byte) 53, (byte) 54, (byte) 55, PaletteRecord.STANDARD_PALETTE_SIZE, (byte) 57, Ref3DPtg.sid, Area3DPtg.sid, (byte) 60, (byte) 61, DocWriter.GT, (byte) 63, (byte) -9, (byte) -9, (byte) -9, (byte) -9};
    private static final byte[] _STANDARD_ALPHABET = new byte[]{(byte) 65, (byte) 66, (byte) 67, (byte) 68, (byte) 69, (byte) 70, (byte) 71, (byte) 72, (byte) 73, (byte) 74, (byte) 75, (byte) 76, (byte) 77, (byte) 78, (byte) 79, (byte) 80, (byte) 81, (byte) 82, (byte) 83, (byte) 84, (byte) 85, (byte) 86, (byte) 87, (byte) 88, (byte) 89, (byte) 90, (byte) 97, (byte) 98, (byte) 99, (byte) 100, (byte) 101, (byte) 102, (byte) 103, (byte) 104, (byte) 105, (byte) 106, (byte) 107, (byte) 108, (byte) 109, (byte) 110, (byte) 111, (byte) 112, (byte) 113, (byte) 114, (byte) 115, (byte) 116, (byte) 117, (byte) 118, (byte) 119, (byte) 120, (byte) 121, (byte) 122, ByteBuffer.ZERO, (byte) 49, (byte) 50, (byte) 51, (byte) 52, (byte) 53, (byte) 54, (byte) 55, PaletteRecord.STANDARD_PALETTE_SIZE, (byte) 57, AreaErrPtg.sid, DocWriter.FORWARD};
    private static final byte[] _STANDARD_DECODABET = new byte[]{(byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, WHITE_SPACE_ENC, WHITE_SPACE_ENC, (byte) -9, (byte) -9, WHITE_SPACE_ENC, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, WHITE_SPACE_ENC, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, DocWriter.GT, (byte) -9, (byte) -9, (byte) -9, (byte) 63, (byte) 52, (byte) 53, (byte) 54, (byte) 55, PaletteRecord.STANDARD_PALETTE_SIZE, (byte) 57, Ref3DPtg.sid, Area3DPtg.sid, (byte) 60, (byte) 61, (byte) -9, (byte) -9, (byte) -9, (byte) -1, (byte) -9, (byte) -9, (byte) -9, (byte) 0, (byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8, (byte) 9, (byte) 10, (byte) 11, (byte) 12, (byte) 13, (byte) 14, (byte) 15, (byte) 16, (byte) 17, (byte) 18, (byte) 19, (byte) 20, (byte) 21, (byte) 22, (byte) 23, Ascii.CAN, (byte) 25, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, Ascii.SUB, Ascii.ESC, Ascii.FS, (byte) 29, (byte) 30, (byte) 31, (byte) 32, (byte) 33, (byte) 34, (byte) 35, RefPtg.sid, (byte) 37, (byte) 38, (byte) 39, (byte) 40, MemFuncPtg.sid, RefErrorPtg.sid, AreaErrPtg.sid, RefNPtg.sid, (byte) 45, (byte) 46, DocWriter.FORWARD, ByteBuffer.ZERO, (byte) 49, (byte) 50, (byte) 51, (byte) -9, (byte) -9, (byte) -9, (byte) -9};
    private static final byte[] _URL_SAFE_ALPHABET = new byte[]{(byte) 65, (byte) 66, (byte) 67, (byte) 68, (byte) 69, (byte) 70, (byte) 71, (byte) 72, (byte) 73, (byte) 74, (byte) 75, (byte) 76, (byte) 77, (byte) 78, (byte) 79, (byte) 80, (byte) 81, (byte) 82, (byte) 83, (byte) 84, (byte) 85, (byte) 86, (byte) 87, (byte) 88, (byte) 89, (byte) 90, (byte) 97, (byte) 98, (byte) 99, (byte) 100, (byte) 101, (byte) 102, (byte) 103, (byte) 104, (byte) 105, (byte) 106, (byte) 107, (byte) 108, (byte) 109, (byte) 110, (byte) 111, (byte) 112, (byte) 113, (byte) 114, (byte) 115, (byte) 116, (byte) 117, (byte) 118, (byte) 119, (byte) 120, (byte) 121, (byte) 122, ByteBuffer.ZERO, (byte) 49, (byte) 50, (byte) 51, (byte) 52, (byte) 53, (byte) 54, (byte) 55, PaletteRecord.STANDARD_PALETTE_SIZE, (byte) 57, (byte) 45, (byte) 95};
    private static final byte[] _URL_SAFE_DECODABET = new byte[]{(byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, WHITE_SPACE_ENC, WHITE_SPACE_ENC, (byte) -9, (byte) -9, WHITE_SPACE_ENC, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, WHITE_SPACE_ENC, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, DocWriter.GT, (byte) -9, (byte) -9, (byte) 52, (byte) 53, (byte) 54, (byte) 55, PaletteRecord.STANDARD_PALETTE_SIZE, (byte) 57, Ref3DPtg.sid, Area3DPtg.sid, (byte) 60, (byte) 61, (byte) -9, (byte) -9, (byte) -9, (byte) -1, (byte) -9, (byte) -9, (byte) -9, (byte) 0, (byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8, (byte) 9, (byte) 10, (byte) 11, (byte) 12, (byte) 13, (byte) 14, (byte) 15, (byte) 16, (byte) 17, (byte) 18, (byte) 19, (byte) 20, (byte) 21, (byte) 22, (byte) 23, Ascii.CAN, (byte) 25, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) 63, (byte) -9, Ascii.SUB, Ascii.ESC, Ascii.FS, (byte) 29, (byte) 30, (byte) 31, (byte) 32, (byte) 33, (byte) 34, (byte) 35, RefPtg.sid, (byte) 37, (byte) 38, (byte) 39, (byte) 40, MemFuncPtg.sid, RefErrorPtg.sid, AreaErrPtg.sid, RefNPtg.sid, (byte) 45, (byte) 46, DocWriter.FORWARD, ByteBuffer.ZERO, (byte) 49, (byte) 50, (byte) 51, (byte) -9, (byte) -9, (byte) -9, (byte) -9};

    public static class InputStream extends FilterInputStream {
        private byte[] alphabet;
        private boolean breakLines;
        private byte[] buffer;
        private int bufferLength;
        private byte[] decodabet;
        private boolean encode;
        private int lineLength;
        private int numSigBytes;
        private int options;
        private int position;

        public InputStream(java.io.InputStream in) {
            this(in, 0);
        }

        public InputStream(java.io.InputStream in, int options) {
            boolean z = true;
            super(in);
            this.breakLines = (options & 8) != 8;
            if ((options & 1) != 1) {
                z = false;
            }
            this.encode = z;
            this.bufferLength = this.encode ? 4 : 3;
            this.buffer = new byte[this.bufferLength];
            this.position = -1;
            this.lineLength = 0;
            this.options = options;
            this.alphabet = Base64.getAlphabet(options);
            this.decodabet = Base64.getDecodabet(options);
        }

        public int read() throws IOException {
            int b;
            if (this.position < 0) {
                int i;
                if (this.encode) {
                    byte[] b3 = new byte[3];
                    int numBinaryBytes = 0;
                    for (i = 0; i < 3; i++) {
                        try {
                            b = this.in.read();
                            if (b >= 0) {
                                b3[i] = (byte) b;
                                numBinaryBytes++;
                            }
                        } catch (IOException e) {
                            if (i == 0) {
                                throw e;
                            }
                        }
                    }
                    if (numBinaryBytes <= 0) {
                        return -1;
                    }
                    Base64.encode3to4(b3, 0, numBinaryBytes, this.buffer, 0, this.options);
                    this.position = 0;
                    this.numSigBytes = 4;
                } else {
                    byte[] b4 = new byte[4];
                    i = 0;
                    while (i < 4) {
                        do {
                            b = this.in.read();
                            if (b < 0) {
                                break;
                            }
                        } while (this.decodabet[b & 127] <= Base64.WHITE_SPACE_ENC);
                        if (b < 0) {
                            break;
                        }
                        b4[i] = (byte) b;
                        i++;
                    }
                    if (i == 4) {
                        this.numSigBytes = Base64.decode4to3(b4, 0, this.buffer, 0, this.options);
                        this.position = 0;
                    } else if (i == 0) {
                        return -1;
                    } else {
                        throw new IOException(MessageLocalization.getComposedMessage("improperly.padded.base64.input", new Object[0]));
                    }
                }
            }
            if (this.position < 0) {
                throw new IOException(MessageLocalization.getComposedMessage("error.in.base64.code.reading.stream", new Object[0]));
            } else if (this.position >= this.numSigBytes) {
                return -1;
            } else {
                if (this.encode && this.breakLines && this.lineLength >= 76) {
                    this.lineLength = 0;
                    return 10;
                }
                this.lineLength++;
                byte[] bArr = this.buffer;
                int i2 = this.position;
                this.position = i2 + 1;
                b = bArr[i2];
                if (this.position >= this.bufferLength) {
                    this.position = -1;
                }
                return b & 255;
            }
        }

        public int read(byte[] dest, int off, int len) throws IOException {
            int i = 0;
            while (i < len) {
                int b = read();
                if (b >= 0) {
                    dest[off + i] = (byte) b;
                    i++;
                } else if (i == 0) {
                    return -1;
                } else {
                    return i;
                }
            }
            return i;
        }
    }

    public static class OutputStream extends FilterOutputStream {
        private byte[] alphabet;
        private byte[] b4;
        private boolean breakLines;
        private byte[] buffer;
        private int bufferLength;
        private byte[] decodabet;
        private boolean encode;
        private int lineLength;
        private int options;
        private int position;
        private boolean suspendEncoding;

        public OutputStream(java.io.OutputStream out) {
            this(out, 1);
        }

        public OutputStream(java.io.OutputStream out, int options) {
            int i;
            boolean z = true;
            super(out);
            this.breakLines = (options & 8) != 8;
            if ((options & 1) != 1) {
                z = false;
            }
            this.encode = z;
            if (this.encode) {
                i = 3;
            } else {
                i = 4;
            }
            this.bufferLength = i;
            this.buffer = new byte[this.bufferLength];
            this.position = 0;
            this.lineLength = 0;
            this.suspendEncoding = false;
            this.b4 = new byte[4];
            this.options = options;
            this.alphabet = Base64.getAlphabet(options);
            this.decodabet = Base64.getDecodabet(options);
        }

        public void write(int theByte) throws IOException {
            if (this.suspendEncoding) {
                this.out.write(theByte);
            } else if (this.encode) {
                r1 = this.buffer;
                r2 = this.position;
                this.position = r2 + 1;
                r1[r2] = (byte) theByte;
                if (this.position >= this.bufferLength) {
                    this.out.write(Base64.encode3to4(this.b4, this.buffer, this.bufferLength, this.options));
                    this.lineLength += 4;
                    if (this.breakLines && this.lineLength >= 76) {
                        this.out.write(10);
                        this.lineLength = 0;
                    }
                    this.position = 0;
                }
            } else if (this.decodabet[theByte & 127] > Base64.WHITE_SPACE_ENC) {
                r1 = this.buffer;
                r2 = this.position;
                this.position = r2 + 1;
                r1[r2] = (byte) theByte;
                if (this.position >= this.bufferLength) {
                    this.out.write(this.b4, 0, Base64.decode4to3(this.buffer, 0, this.b4, 0, this.options));
                    this.position = 0;
                }
            } else if (this.decodabet[theByte & 127] != Base64.WHITE_SPACE_ENC) {
                throw new IOException(MessageLocalization.getComposedMessage("invalid.character.in.base64.data", new Object[0]));
            }
        }

        public void write(byte[] theBytes, int off, int len) throws IOException {
            if (this.suspendEncoding) {
                this.out.write(theBytes, off, len);
                return;
            }
            for (int i = 0; i < len; i++) {
                write(theBytes[off + i]);
            }
        }

        public void flushBase64() throws IOException {
            if (this.position <= 0) {
                return;
            }
            if (this.encode) {
                this.out.write(Base64.encode3to4(this.b4, this.buffer, this.position, this.options));
                this.position = 0;
                return;
            }
            throw new IOException(MessageLocalization.getComposedMessage("base64.input.not.properly.padded", new Object[0]));
        }

        public void close() throws IOException {
            flushBase64();
            super.close();
            this.buffer = null;
            this.out = null;
        }

        public void suspendEncoding() throws IOException {
            flushBase64();
            this.suspendEncoding = true;
        }

        public void resumeEncoding() {
            this.suspendEncoding = false;
        }
    }

    private static final byte[] getAlphabet(int options) {
        if ((options & 16) == 16) {
            return _URL_SAFE_ALPHABET;
        }
        if ((options & 32) == 32) {
            return _ORDERED_ALPHABET;
        }
        return _STANDARD_ALPHABET;
    }

    private static final byte[] getDecodabet(int options) {
        if ((options & 16) == 16) {
            return _URL_SAFE_DECODABET;
        }
        if ((options & 32) == 32) {
            return _ORDERED_DECODABET;
        }
        return _STANDARD_DECODABET;
    }

    private Base64() {
    }

    private static final void usage(String msg) {
        System.err.println(msg);
        System.err.println("Usage: java Base64 -e|-d inputfile outputfile");
    }

    private static byte[] encode3to4(byte[] b4, byte[] threeBytes, int numSigBytes, int options) {
        encode3to4(threeBytes, 0, numSigBytes, b4, 0, options);
        return b4;
    }

    private static byte[] encode3to4(byte[] source, int srcOffset, int numSigBytes, byte[] destination, int destOffset, int options) {
        int i;
        int i2 = 0;
        byte[] ALPHABET = getAlphabet(options);
        if (numSigBytes > 0) {
            i = (source[srcOffset] << 24) >>> 8;
        } else {
            i = 0;
        }
        int i3 = (numSigBytes > 1 ? (source[srcOffset + 1] << 24) >>> 16 : 0) | i;
        if (numSigBytes > 2) {
            i2 = (source[srcOffset + 2] << 24) >>> 24;
        }
        int inBuff = i3 | i2;
        switch (numSigBytes) {
            case 1:
                destination[destOffset] = ALPHABET[inBuff >>> 18];
                destination[destOffset + 1] = ALPHABET[(inBuff >>> 12) & 63];
                destination[destOffset + 2] = (byte) 61;
                destination[destOffset + 3] = (byte) 61;
                break;
            case 2:
                destination[destOffset] = ALPHABET[inBuff >>> 18];
                destination[destOffset + 1] = ALPHABET[(inBuff >>> 12) & 63];
                destination[destOffset + 2] = ALPHABET[(inBuff >>> 6) & 63];
                destination[destOffset + 3] = (byte) 61;
                break;
            case 3:
                destination[destOffset] = ALPHABET[inBuff >>> 18];
                destination[destOffset + 1] = ALPHABET[(inBuff >>> 12) & 63];
                destination[destOffset + 2] = ALPHABET[(inBuff >>> 6) & 63];
                destination[destOffset + 3] = ALPHABET[inBuff & 63];
                break;
        }
        return destination;
    }

    public static String encodeObject(Serializable serializableObject) {
        return encodeObject(serializableObject, 0);
    }

    public static String encodeObject(Serializable serializableObject, int options) {
        IOException e;
        Throwable th;
        ByteArrayOutputStream baos = null;
        java.io.OutputStream b64os = null;
        ObjectOutputStream oos = null;
        GZIPOutputStream gzos = null;
        int gzip = options & 2;
        int dontBreakLines = options & 8;
        try {
            ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
            try {
                java.io.OutputStream b64os2 = new OutputStream(baos2, options | 1);
                if (gzip == 2) {
                    try {
                        GZIPOutputStream gzos2 = new GZIPOutputStream(b64os2);
                        try {
                            gzos = gzos2;
                            oos = new ObjectOutputStream(gzos2);
                        } catch (IOException e2) {
                            e = e2;
                            gzos = gzos2;
                            b64os = b64os2;
                            baos = baos2;
                            try {
                                e.printStackTrace();
                                try {
                                    oos.close();
                                } catch (Exception e3) {
                                }
                                try {
                                    gzos.close();
                                } catch (Exception e4) {
                                }
                                try {
                                    b64os.close();
                                } catch (Exception e5) {
                                }
                                try {
                                    baos.close();
                                    return null;
                                } catch (Exception e6) {
                                    return null;
                                }
                            } catch (Throwable th2) {
                                th = th2;
                                try {
                                    oos.close();
                                } catch (Exception e7) {
                                }
                                try {
                                    gzos.close();
                                } catch (Exception e8) {
                                }
                                try {
                                    b64os.close();
                                } catch (Exception e9) {
                                }
                                try {
                                    baos.close();
                                } catch (Exception e10) {
                                }
                                throw th;
                            }
                        } catch (Throwable th3) {
                            th = th3;
                            gzos = gzos2;
                            b64os = b64os2;
                            baos = baos2;
                            oos.close();
                            gzos.close();
                            b64os.close();
                            baos.close();
                            throw th;
                        }
                    } catch (IOException e11) {
                        e = e11;
                        b64os = b64os2;
                        baos = baos2;
                        e.printStackTrace();
                        oos.close();
                        gzos.close();
                        b64os.close();
                        baos.close();
                        return null;
                    } catch (Throwable th4) {
                        th = th4;
                        b64os = b64os2;
                        baos = baos2;
                        oos.close();
                        gzos.close();
                        b64os.close();
                        baos.close();
                        throw th;
                    }
                }
                oos = new ObjectOutputStream(b64os2);
                oos.writeObject(serializableObject);
                try {
                    oos.close();
                } catch (Exception e12) {
                }
                try {
                    gzos.close();
                } catch (Exception e13) {
                }
                try {
                    b64os2.close();
                } catch (Exception e14) {
                }
                try {
                    baos2.close();
                } catch (Exception e15) {
                }
                try {
                    b64os = b64os2;
                    baos = baos2;
                    return new String(baos2.toByteArray(), "UTF-8");
                } catch (UnsupportedEncodingException e16) {
                    b64os = b64os2;
                    baos = baos2;
                    return new String(baos2.toByteArray());
                }
            } catch (IOException e17) {
                e = e17;
                baos = baos2;
                e.printStackTrace();
                oos.close();
                gzos.close();
                b64os.close();
                baos.close();
                return null;
            } catch (Throwable th5) {
                th = th5;
                baos = baos2;
                oos.close();
                gzos.close();
                b64os.close();
                baos.close();
                throw th;
            }
        } catch (IOException e18) {
            e = e18;
            e.printStackTrace();
            oos.close();
            gzos.close();
            b64os.close();
            baos.close();
            return null;
        }
    }

    public static String encodeBytes(byte[] source) {
        return encodeBytes(source, 0, source.length, 0);
    }

    public static String encodeBytes(byte[] source, int options) {
        return encodeBytes(source, 0, source.length, options);
    }

    public static String encodeBytes(byte[] source, int off, int len) {
        return encodeBytes(source, off, len, 0);
    }

    public static String encodeBytes(byte[] source, int off, int len, int options) {
        Throwable th;
        int dontBreakLines = options & 8;
        IOException e;
        if ((options & 2) == 2) {
            ByteArrayOutputStream baos = null;
            GZIPOutputStream gzos = null;
            OutputStream b64os = null;
            try {
                OutputStream b64os2;
                GZIPOutputStream gZIPOutputStream;
                ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
                try {
                    b64os2 = new OutputStream(baos2, options | 1);
                    try {
                        gZIPOutputStream = new GZIPOutputStream(b64os2);
                    } catch (IOException e2) {
                        e = e2;
                        b64os = b64os2;
                        baos = baos2;
                        try {
                            e.printStackTrace();
                            try {
                                gzos.close();
                            } catch (Exception e3) {
                            }
                            try {
                                b64os.close();
                            } catch (Exception e4) {
                            }
                            try {
                                baos.close();
                                return null;
                            } catch (Exception e5) {
                                return null;
                            }
                        } catch (Throwable th2) {
                            th = th2;
                            try {
                                gzos.close();
                            } catch (Exception e6) {
                            }
                            try {
                                b64os.close();
                            } catch (Exception e7) {
                            }
                            try {
                                baos.close();
                            } catch (Exception e8) {
                            }
                            throw th;
                        }
                    } catch (Throwable th3) {
                        th = th3;
                        b64os = b64os2;
                        baos = baos2;
                        gzos.close();
                        b64os.close();
                        baos.close();
                        throw th;
                    }
                } catch (IOException e9) {
                    e = e9;
                    baos = baos2;
                    e.printStackTrace();
                    gzos.close();
                    b64os.close();
                    baos.close();
                    return null;
                } catch (Throwable th4) {
                    th = th4;
                    baos = baos2;
                    gzos.close();
                    b64os.close();
                    baos.close();
                    throw th;
                }
                try {
                    gZIPOutputStream.write(source, off, len);
                    gZIPOutputStream.close();
                    try {
                        gZIPOutputStream.close();
                    } catch (Exception e10) {
                    }
                    try {
                        b64os2.close();
                    } catch (Exception e11) {
                    }
                    try {
                        baos2.close();
                    } catch (Exception e12) {
                    }
                    try {
                        return new String(baos2.toByteArray(), "UTF-8");
                    } catch (UnsupportedEncodingException e13) {
                        return new String(baos2.toByteArray());
                    }
                } catch (IOException e14) {
                    e = e14;
                    b64os = b64os2;
                    gzos = gZIPOutputStream;
                    baos = baos2;
                    e.printStackTrace();
                    gzos.close();
                    b64os.close();
                    baos.close();
                    return null;
                } catch (Throwable th5) {
                    th = th5;
                    b64os = b64os2;
                    gzos = gZIPOutputStream;
                    baos = baos2;
                    gzos.close();
                    b64os.close();
                    baos.close();
                    throw th;
                }
            } catch (IOException e15) {
                e = e15;
                e.printStackTrace();
                gzos.close();
                b64os.close();
                baos.close();
                return null;
            }
        }
        int i;
        boolean breakLines = dontBreakLines == 0;
        int len43 = (len * 4) / 3;
        int i2 = len43 + (len % 3 > 0 ? 4 : 0);
        if (breakLines) {
            i = len43 / 76;
        } else {
            i = 0;
        }
        byte[] outBuff = new byte[(i + i2)];
        int d = 0;
        e = 0;
        int len2 = len - 2;
        int lineLength = 0;
        while (d < len2) {
            int e16;
            encode3to4(source, d + off, 3, outBuff, e, options);
            lineLength += 4;
            if (breakLines && lineLength == 76) {
                outBuff[e + 4] = (byte) 10;
                e16 = e + 1;
                lineLength = 0;
            }
            d += 3;
            e = e16 + 4;
        }
        if (d < len) {
            encode3to4(source, d + off, len - d, outBuff, e, options);
            e += 4;
        }
        try {
            return new String(outBuff, 0, e, "UTF-8");
        } catch (UnsupportedEncodingException e17) {
            return new String(outBuff, 0, e);
        }
    }

    private static int decode4to3(byte[] source, int srcOffset, byte[] destination, int destOffset, int options) {
        byte[] DECODABET = getDecodabet(options);
        if (source[srcOffset + 2] == (byte) 61) {
            destination[destOffset] = (byte) ((((DECODABET[source[srcOffset]] & 255) << 18) | ((DECODABET[source[srcOffset + 1]] & 255) << 12)) >>> 16);
            return 1;
        } else if (source[srcOffset + 3] == (byte) 61) {
            outBuff = (((DECODABET[source[srcOffset]] & 255) << 18) | ((DECODABET[source[srcOffset + 1]] & 255) << 12)) | ((DECODABET[source[srcOffset + 2]] & 255) << 6);
            destination[destOffset] = (byte) (outBuff >>> 16);
            destination[destOffset + 1] = (byte) (outBuff >>> 8);
            return 2;
        } else {
            try {
                outBuff = ((((DECODABET[source[srcOffset]] & 255) << 18) | ((DECODABET[source[srcOffset + 1]] & 255) << 12)) | ((DECODABET[source[srcOffset + 2]] & 255) << 6)) | (DECODABET[source[srcOffset + 3]] & 255);
                destination[destOffset] = (byte) (outBuff >> 16);
                destination[destOffset + 1] = (byte) (outBuff >> 8);
                destination[destOffset + 2] = (byte) outBuff;
                return 3;
            } catch (Exception e) {
                System.out.println("" + source[srcOffset] + ": " + DECODABET[source[srcOffset]]);
                System.out.println("" + source[srcOffset + 1] + ": " + DECODABET[source[srcOffset + 1]]);
                System.out.println("" + source[srcOffset + 2] + ": " + DECODABET[source[srcOffset + 2]]);
                System.out.println("" + source[srcOffset + 3] + ": " + DECODABET[source[srcOffset + 3]]);
                return -1;
            }
        }
    }

    public static byte[] decode(byte[] source, int off, int len, int options) {
        int b4Posn;
        byte[] DECODABET = getDecodabet(options);
        byte[] outBuff = new byte[((len * 3) / 4)];
        int outBuffPosn = 0;
        byte[] b4 = new byte[4];
        int i = off;
        int b4Posn2 = 0;
        while (i < off + len) {
            byte sbiCrop = (byte) (source[i] & 127);
            byte sbiDecode = DECODABET[sbiCrop];
            if (sbiDecode >= WHITE_SPACE_ENC) {
                if (sbiDecode >= (byte) -1) {
                    b4Posn = b4Posn2 + 1;
                    b4[b4Posn2] = sbiCrop;
                    if (b4Posn > 3) {
                        outBuffPosn += decode4to3(b4, 0, outBuff, outBuffPosn, options);
                        b4Posn = 0;
                        if (sbiCrop == (byte) 61) {
                            break;
                        }
                    } else {
                        continue;
                    }
                } else {
                    b4Posn = b4Posn2;
                }
                i++;
                b4Posn2 = b4Posn;
            } else {
                System.err.println("Bad Base64 input character at " + i + ": " + source[i] + "(decimal)");
                b4Posn = b4Posn2;
                return null;
            }
        }
        b4Posn = b4Posn2;
        byte[] out = new byte[outBuffPosn];
        System.arraycopy(outBuff, 0, out, 0, outBuffPosn);
        return out;
    }

    public static byte[] decode(String s) {
        return decode(s, 0);
    }

    public static byte[] decode(String s, int options) {
        byte[] bytes;
        Throwable th;
        try {
            bytes = s.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            bytes = s.getBytes();
        }
        bytes = decode(bytes, 0, bytes.length, options);
        if (bytes != null && bytes.length >= 4 && 35615 == ((bytes[0] & 255) | ((bytes[1] << 8) & MotionEventCompat.ACTION_POINTER_INDEX_MASK))) {
            ByteArrayInputStream bais = null;
            GZIPInputStream gzis = null;
            ByteArrayOutputStream baos = null;
            byte[] buffer = new byte[2048];
            try {
                ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
                try {
                    ByteArrayInputStream bais2 = new ByteArrayInputStream(bytes);
                    try {
                        GZIPInputStream gzis2 = new GZIPInputStream(bais2);
                        while (true) {
                            try {
                                int length = gzis2.read(buffer);
                                if (length < 0) {
                                    break;
                                }
                                baos2.write(buffer, 0, length);
                            } catch (IOException e2) {
                                baos = baos2;
                                gzis = gzis2;
                                bais = bais2;
                            } catch (Throwable th2) {
                                th = th2;
                                baos = baos2;
                                gzis = gzis2;
                                bais = bais2;
                            }
                        }
                        bytes = baos2.toByteArray();
                        try {
                            baos2.close();
                        } catch (Exception e3) {
                        }
                        try {
                            gzis2.close();
                        } catch (Exception e4) {
                        }
                        try {
                            bais2.close();
                        } catch (Exception e5) {
                        }
                    } catch (IOException e6) {
                        baos = baos2;
                        bais = bais2;
                        try {
                            baos.close();
                        } catch (Exception e7) {
                        }
                        try {
                            gzis.close();
                        } catch (Exception e8) {
                        }
                        try {
                            bais.close();
                        } catch (Exception e9) {
                        }
                        return bytes;
                    } catch (Throwable th3) {
                        th = th3;
                        baos = baos2;
                        bais = bais2;
                        try {
                            baos.close();
                        } catch (Exception e10) {
                        }
                        try {
                            gzis.close();
                        } catch (Exception e11) {
                        }
                        try {
                            bais.close();
                        } catch (Exception e12) {
                        }
                        throw th;
                    }
                } catch (IOException e13) {
                    baos = baos2;
                    baos.close();
                    gzis.close();
                    bais.close();
                    return bytes;
                } catch (Throwable th4) {
                    th = th4;
                    baos = baos2;
                    baos.close();
                    gzis.close();
                    bais.close();
                    throw th;
                }
            } catch (IOException e14) {
                baos.close();
                gzis.close();
                bais.close();
                return bytes;
            } catch (Throwable th5) {
                th = th5;
                baos.close();
                gzis.close();
                bais.close();
                throw th;
            }
        }
        return bytes;
    }

    public static Object decodeToObject(String encodedObject) {
        IOException e;
        Throwable th;
        ClassNotFoundException e2;
        ByteArrayInputStream bais = null;
        ObjectInputStream ois = null;
        Object obj = null;
        try {
            ObjectInputStream ois2;
            ByteArrayInputStream bais2 = new ByteArrayInputStream(decode(encodedObject));
            try {
                ois2 = new ObjectInputStream(bais2);
            } catch (IOException e3) {
                e = e3;
                bais = bais2;
                try {
                    e.printStackTrace();
                    try {
                        bais.close();
                    } catch (Exception e4) {
                    }
                    try {
                        ois.close();
                    } catch (Exception e5) {
                    }
                    return obj;
                } catch (Throwable th2) {
                    th = th2;
                    try {
                        bais.close();
                    } catch (Exception e6) {
                    }
                    try {
                        ois.close();
                    } catch (Exception e7) {
                    }
                    throw th;
                }
            } catch (ClassNotFoundException e8) {
                e2 = e8;
                bais = bais2;
                e2.printStackTrace();
                try {
                    bais.close();
                } catch (Exception e9) {
                }
                try {
                    ois.close();
                } catch (Exception e10) {
                }
                return obj;
            } catch (Throwable th3) {
                th = th3;
                bais = bais2;
                bais.close();
                ois.close();
                throw th;
            }
            try {
                obj = ois2.readObject();
                try {
                    bais2.close();
                } catch (Exception e11) {
                }
                try {
                    ois2.close();
                    ois = ois2;
                    bais = bais2;
                } catch (Exception e12) {
                    ois = ois2;
                    bais = bais2;
                }
            } catch (IOException e13) {
                e = e13;
                ois = ois2;
                bais = bais2;
                e.printStackTrace();
                bais.close();
                ois.close();
                return obj;
            } catch (ClassNotFoundException e14) {
                e2 = e14;
                ois = ois2;
                bais = bais2;
                e2.printStackTrace();
                bais.close();
                ois.close();
                return obj;
            } catch (Throwable th4) {
                th = th4;
                ois = ois2;
                bais = bais2;
                bais.close();
                ois.close();
                throw th;
            }
        } catch (IOException e15) {
            e = e15;
            e.printStackTrace();
            bais.close();
            ois.close();
            return obj;
        } catch (ClassNotFoundException e16) {
            e2 = e16;
            e2.printStackTrace();
            bais.close();
            ois.close();
            return obj;
        }
        return obj;
    }

    public static boolean encodeToFile(byte[] dataToEncode, String filename) {
        boolean success;
        Throwable th;
        OutputStream bos = null;
        try {
            OutputStream bos2 = new OutputStream(new FileOutputStream(filename), 1);
            try {
                bos2.write(dataToEncode);
                success = true;
                try {
                    bos2.close();
                    bos = bos2;
                } catch (Exception e) {
                    bos = bos2;
                }
            } catch (IOException e2) {
                bos = bos2;
                success = false;
                try {
                    bos.close();
                } catch (Exception e3) {
                }
                return success;
            } catch (Throwable th2) {
                th = th2;
                bos = bos2;
                try {
                    bos.close();
                } catch (Exception e4) {
                }
                throw th;
            }
        } catch (IOException e5) {
            success = false;
            bos.close();
            return success;
        } catch (Throwable th3) {
            th = th3;
            bos.close();
            throw th;
        }
        return success;
    }

    public static boolean decodeToFile(String dataToDecode, String filename) {
        boolean success;
        Throwable th;
        OutputStream bos = null;
        try {
            OutputStream bos2 = new OutputStream(new FileOutputStream(filename), 0);
            try {
                bos2.write(dataToDecode.getBytes("UTF-8"));
                success = true;
                try {
                    bos2.close();
                    bos = bos2;
                } catch (Exception e) {
                    bos = bos2;
                }
            } catch (IOException e2) {
                bos = bos2;
                success = false;
                try {
                    bos.close();
                } catch (Exception e3) {
                }
                return success;
            } catch (Throwable th2) {
                th = th2;
                bos = bos2;
                try {
                    bos.close();
                } catch (Exception e4) {
                }
                throw th;
            }
        } catch (IOException e5) {
            success = false;
            bos.close();
            return success;
        } catch (Throwable th3) {
            th = th3;
            bos.close();
            throw th;
        }
        return success;
    }

    public static byte[] decodeFromFile(String filename) {
        Throwable th;
        byte[] decodedData = null;
        InputStream bis = null;
        try {
            File file = new File(filename);
            int length = 0;
            if (file.length() > 2147483647L) {
                System.err.println("File is too big for this convenience method (" + file.length() + " bytes).");
                if (bis == null) {
                    return null;
                }
                try {
                    bis.close();
                    return null;
                } catch (Exception e) {
                    return null;
                }
            }
            byte[] buffer = new byte[((int) file.length())];
            InputStream bis2 = new InputStream(new BufferedInputStream(new FileInputStream(file)), 0);
            while (true) {
                try {
                    int numBytes = bis2.read(buffer, length, 4096);
                    if (numBytes < 0) {
                        break;
                    }
                    length += numBytes;
                } catch (IOException e2) {
                    bis = bis2;
                } catch (Throwable th2) {
                    th = th2;
                    bis = bis2;
                }
            }
            decodedData = new byte[length];
            System.arraycopy(buffer, 0, decodedData, 0, length);
            if (bis2 != null) {
                try {
                    bis2.close();
                    bis = bis2;
                } catch (Exception e3) {
                    bis = bis2;
                }
            }
            return decodedData;
        } catch (IOException e4) {
            try {
                System.err.println("Error decoding from file " + filename);
                if (bis != null) {
                    try {
                        bis.close();
                    } catch (Exception e5) {
                    }
                }
                return decodedData;
            } catch (Throwable th3) {
                th = th3;
                if (bis != null) {
                    try {
                        bis.close();
                    } catch (Exception e6) {
                    }
                }
                throw th;
            }
        }
    }

    public static String encodeFromFile(String filename) {
        Throwable th;
        InputStream bis = null;
        try {
            File file = new File(filename);
            byte[] buffer = new byte[Math.max((int) (((double) file.length()) * 1.4d), 40)];
            int length = 0;
            InputStream bis2 = new InputStream(new BufferedInputStream(new FileInputStream(file)), 1);
            while (true) {
                try {
                    int numBytes = bis2.read(buffer, length, 4096);
                    if (numBytes >= 0) {
                        length += numBytes;
                    } else {
                        String encodedData = new String(buffer, 0, length, "UTF-8");
                        try {
                            bis2.close();
                            bis = bis2;
                            return encodedData;
                        } catch (Exception e) {
                            bis = bis2;
                            return encodedData;
                        }
                    }
                } catch (IOException e2) {
                    bis = bis2;
                } catch (Throwable th2) {
                    th = th2;
                    bis = bis2;
                }
            }
        } catch (IOException e3) {
            try {
                System.err.println("Error encoding from file " + filename);
                try {
                    bis.close();
                    return null;
                } catch (Exception e4) {
                    return null;
                }
            } catch (Throwable th3) {
                th = th3;
                try {
                    bis.close();
                } catch (Exception e5) {
                }
                throw th;
            }
        }
    }

    public static void encodeFileToFile(String infile, String outfile) {
        IOException ex;
        Throwable th;
        String encoded = encodeFromFile(infile);
        java.io.OutputStream out = null;
        try {
            java.io.OutputStream out2 = new BufferedOutputStream(new FileOutputStream(outfile));
            try {
                out2.write(encoded.getBytes("US-ASCII"));
                try {
                    out2.close();
                    out = out2;
                } catch (Exception e) {
                    out = out2;
                }
            } catch (IOException e2) {
                ex = e2;
                out = out2;
                try {
                    ex.printStackTrace();
                    try {
                        out.close();
                    } catch (Exception e3) {
                    }
                } catch (Throwable th2) {
                    th = th2;
                    try {
                        out.close();
                    } catch (Exception e4) {
                    }
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                out = out2;
                out.close();
                throw th;
            }
        } catch (IOException e5) {
            ex = e5;
            ex.printStackTrace();
            out.close();
        }
    }

    public static void decodeFileToFile(String infile, String outfile) {
        IOException ex;
        Throwable th;
        byte[] decoded = decodeFromFile(infile);
        java.io.OutputStream out = null;
        try {
            java.io.OutputStream out2 = new BufferedOutputStream(new FileOutputStream(outfile));
            try {
                out2.write(decoded);
                try {
                    out2.close();
                    out = out2;
                } catch (Exception e) {
                    out = out2;
                }
            } catch (IOException e2) {
                ex = e2;
                out = out2;
                try {
                    ex.printStackTrace();
                    try {
                        out.close();
                    } catch (Exception e3) {
                    }
                } catch (Throwable th2) {
                    th = th2;
                    try {
                        out.close();
                    } catch (Exception e4) {
                    }
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                out = out2;
                out.close();
                throw th;
            }
        } catch (IOException e5) {
            ex = e5;
            ex.printStackTrace();
            out.close();
        }
    }
}
