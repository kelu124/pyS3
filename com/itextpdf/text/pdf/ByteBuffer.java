package com.itextpdf.text.pdf;

import com.itextpdf.text.DocWriter;
import com.itextpdf.text.error_messages.MessageLocalization;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import org.apache.poi.hssf.record.PaletteRecord;
import org.bytedeco.javacpp.ARToolKitPlus;
import org.bytedeco.javacpp.avutil;

public class ByteBuffer extends OutputStream {
    public static boolean HIGH_PRECISION = false;
    public static final byte ZERO = (byte) 48;
    private static byte[][] byteCache = new byte[byteCacheSize][];
    private static int byteCacheSize = 0;
    private static final byte[] bytes = new byte[]{ZERO, (byte) 49, (byte) 50, (byte) 51, (byte) 52, (byte) 53, (byte) 54, (byte) 55, PaletteRecord.STANDARD_PALETTE_SIZE, (byte) 57, (byte) 97, (byte) 98, (byte) 99, (byte) 100, (byte) 101, (byte) 102};
    private static final char[] chars = new char[]{'0', '1', PdfWriter.VERSION_1_2, PdfWriter.VERSION_1_3, PdfWriter.VERSION_1_4, PdfWriter.VERSION_1_5, PdfWriter.VERSION_1_6, PdfWriter.VERSION_1_7, '8', '9'};
    private static final DecimalFormatSymbols dfs = new DecimalFormatSymbols(Locale.US);
    protected byte[] buf;
    protected int count;

    public ByteBuffer() {
        this(128);
    }

    public ByteBuffer(int size) {
        if (size < 1) {
            size = 128;
        }
        this.buf = new byte[size];
    }

    public static void setCacheSize(int size) {
        if (size > 3276700) {
            size = 3276700;
        }
        if (size > byteCacheSize) {
            byte[][] tmpCache = new byte[size][];
            System.arraycopy(byteCache, 0, tmpCache, 0, byteCacheSize);
            byteCache = tmpCache;
            byteCacheSize = size;
        }
    }

    public static void fillCache(int decimals) {
        int step = 1;
        switch (decimals) {
            case 0:
                step = 100;
                break;
            case 1:
                step = 10;
                break;
        }
        for (int i = 1; i < byteCacheSize; i += step) {
            if (byteCache[i] == null) {
                byteCache[i] = convertToBytes(i);
            }
        }
    }

    private static byte[] convertToBytes(int i) {
        int size = (int) Math.floor(Math.log((double) i) / Math.log(10.0d));
        if (i % 100 != 0) {
            size += 2;
        }
        if (i % 10 != 0) {
            size++;
        }
        if (i < 100) {
            size++;
            if (i < 10) {
                size++;
            }
        }
        size--;
        byte[] cache = new byte[size];
        size--;
        if (i < 100) {
            cache[0] = ZERO;
        }
        if (i % 10 != 0) {
            int size2 = size - 1;
            cache[size] = bytes[i % 10];
            size = size2;
        }
        if (i % 100 != 0) {
            size2 = size - 1;
            cache[size] = bytes[(i / 10) % 10];
            size = size2 - 1;
            cache[size2] = (byte) 46;
        }
        size = ((int) Math.floor(Math.log((double) i) / Math.log(10.0d))) - 1;
        for (int add = 0; add < size; add++) {
            cache[add] = bytes[(i / ((int) Math.pow(10.0d, (double) ((size - add) + 1)))) % 10];
        }
        return cache;
    }

    public ByteBuffer append_i(int b) {
        int newcount = this.count + 1;
        if (newcount > this.buf.length) {
            byte[] newbuf = new byte[Math.max(this.buf.length << 1, newcount)];
            System.arraycopy(this.buf, 0, newbuf, 0, this.count);
            this.buf = newbuf;
        }
        this.buf[this.count] = (byte) b;
        this.count = newcount;
        return this;
    }

    public ByteBuffer append(byte[] b, int off, int len) {
        if (off >= 0 && off <= b.length && len >= 0 && off + len <= b.length && off + len >= 0 && len != 0) {
            int newcount = this.count + len;
            if (newcount > this.buf.length) {
                byte[] newbuf = new byte[Math.max(this.buf.length << 1, newcount)];
                System.arraycopy(this.buf, 0, newbuf, 0, this.count);
                this.buf = newbuf;
            }
            System.arraycopy(b, off, this.buf, this.count, len);
            this.count = newcount;
        }
        return this;
    }

    public ByteBuffer append(byte[] b) {
        return append(b, 0, b.length);
    }

    public ByteBuffer append(String str) {
        if (str != null) {
            return append(DocWriter.getISOBytes(str));
        }
        return this;
    }

    public ByteBuffer append(char c) {
        return append_i(c);
    }

    public ByteBuffer append(ByteBuffer buf) {
        return append(buf.buf, 0, buf.count);
    }

    public ByteBuffer append(int i) {
        return append((double) i);
    }

    public ByteBuffer append(long i) {
        return append(Long.toString(i));
    }

    public ByteBuffer append(byte b) {
        return append_i(b);
    }

    public ByteBuffer appendHex(byte b) {
        append(bytes[(b >> 4) & 15]);
        return append(bytes[b & 15]);
    }

    public ByteBuffer append(float i) {
        return append((double) i);
    }

    public ByteBuffer append(double d) {
        append(formatDouble(d, this));
        return this;
    }

    public static String formatDouble(double d) {
        return formatDouble(d, null);
    }

    public static String formatDouble(double d, ByteBuffer buf) {
        if (HIGH_PRECISION) {
            String sform = new DecimalFormat("0.######", dfs).format(d);
            if (buf == null) {
                return sform;
            }
            buf.append(sform);
            return null;
        }
        boolean negative = false;
        if (Math.abs(d) >= 1.5E-5d) {
            if (d < 0.0d) {
                negative = true;
                d = -d;
            }
            int v;
            StringBuilder res;
            if (d < 1.0d) {
                d += 5.0E-6d;
                if (d >= 1.0d) {
                    if (negative) {
                        if (buf == null) {
                            return "-1";
                        }
                        buf.append((byte) 45);
                        buf.append((byte) 49);
                        return null;
                    } else if (buf == null) {
                        return "1";
                    } else {
                        buf.append((byte) 49);
                        return null;
                    }
                } else if (buf != null) {
                    v = (int) (100000.0d * d);
                    if (negative) {
                        buf.append((byte) 45);
                    }
                    buf.append((byte) ZERO);
                    buf.append((byte) 46);
                    buf.append((byte) ((v / 10000) + 48));
                    if (v % 10000 != 0) {
                        buf.append((byte) (((v / 1000) % 10) + 48));
                        if (v % 1000 != 0) {
                            buf.append((byte) (((v / 100) % 10) + 48));
                            if (v % 100 != 0) {
                                buf.append((byte) (((v / 10) % 10) + 48));
                                if (v % 10 != 0) {
                                    buf.append((byte) ((v % 10) + 48));
                                }
                            }
                        }
                    }
                    return null;
                } else {
                    int x = ARToolKitPlus.AR_AREA_MAX;
                    v = (int) (((double) 100000) * d);
                    res = new StringBuilder();
                    if (negative) {
                        res.append('-');
                    }
                    res.append("0.");
                    while (v < x / 10) {
                        res.append('0');
                        x /= 10;
                    }
                    res.append(v);
                    int cut = res.length() - 1;
                    while (res.charAt(cut) == '0') {
                        cut--;
                    }
                    res.setLength(cut + 1);
                    return res.toString();
                }
            } else if (d <= 32767.0d) {
                v = (int) (100.0d * (d + 0.005d));
                if (v >= byteCacheSize || byteCache[v] == null) {
                    if (buf != null) {
                        if (v < byteCacheSize) {
                            int add;
                            int add2;
                            int size = 0;
                            if (v >= avutil.AV_TIME_BASE) {
                                size = 0 + 5;
                            } else if (v >= ARToolKitPlus.AR_AREA_MAX) {
                                size = 0 + 4;
                            } else if (v >= 10000) {
                                size = 0 + 3;
                            } else if (v >= 1000) {
                                size = 0 + 2;
                            } else if (v >= 100) {
                                size = 0 + 1;
                            }
                            if (v % 100 != 0) {
                                size += 2;
                            }
                            if (v % 10 != 0) {
                                size++;
                            }
                            byte[] cache = new byte[size];
                            if (v >= avutil.AV_TIME_BASE) {
                                add = 0 + 1;
                                cache[0] = bytes[v / avutil.AV_TIME_BASE];
                            } else {
                                add = 0;
                            }
                            if (v >= ARToolKitPlus.AR_AREA_MAX) {
                                add2 = add + 1;
                                cache[add] = bytes[(v / ARToolKitPlus.AR_AREA_MAX) % 10];
                                add = add2;
                            }
                            if (v >= 10000) {
                                add2 = add + 1;
                                cache[add] = bytes[(v / 10000) % 10];
                                add = add2;
                            }
                            if (v >= 1000) {
                                add2 = add + 1;
                                cache[add] = bytes[(v / 1000) % 10];
                                add = add2;
                            }
                            if (v >= 100) {
                                add2 = add + 1;
                                cache[add] = bytes[(v / 100) % 10];
                            } else {
                                add2 = add;
                            }
                            if (v % 100 != 0) {
                                add = add2 + 1;
                                cache[add2] = (byte) 46;
                                add2 = add + 1;
                                cache[add] = bytes[(v / 10) % 10];
                                if (v % 10 != 0) {
                                    add = add2 + 1;
                                    cache[add2] = bytes[v % 10];
                                    add2 = add;
                                }
                            }
                            byteCache[v] = cache;
                        }
                        if (negative) {
                            buf.append((byte) 45);
                        }
                        if (v >= avutil.AV_TIME_BASE) {
                            buf.append(bytes[v / avutil.AV_TIME_BASE]);
                        }
                        if (v >= ARToolKitPlus.AR_AREA_MAX) {
                            buf.append(bytes[(v / ARToolKitPlus.AR_AREA_MAX) % 10]);
                        }
                        if (v >= 10000) {
                            buf.append(bytes[(v / 10000) % 10]);
                        }
                        if (v >= 1000) {
                            buf.append(bytes[(v / 1000) % 10]);
                        }
                        if (v >= 100) {
                            buf.append(bytes[(v / 100) % 10]);
                        }
                        if (v % 100 != 0) {
                            buf.append((byte) 46);
                            buf.append(bytes[(v / 10) % 10]);
                            if (v % 10 != 0) {
                                buf.append(bytes[v % 10]);
                            }
                        }
                        return null;
                    }
                    res = new StringBuilder();
                    if (negative) {
                        res.append('-');
                    }
                    if (v >= avutil.AV_TIME_BASE) {
                        res.append(chars[v / avutil.AV_TIME_BASE]);
                    }
                    if (v >= ARToolKitPlus.AR_AREA_MAX) {
                        res.append(chars[(v / ARToolKitPlus.AR_AREA_MAX) % 10]);
                    }
                    if (v >= 10000) {
                        res.append(chars[(v / 10000) % 10]);
                    }
                    if (v >= 1000) {
                        res.append(chars[(v / 1000) % 10]);
                    }
                    if (v >= 100) {
                        res.append(chars[(v / 100) % 10]);
                    }
                    if (v % 100 != 0) {
                        res.append('.');
                        res.append(chars[(v / 10) % 10]);
                        if (v % 10 != 0) {
                            res.append(chars[v % 10]);
                        }
                    }
                    return res.toString();
                } else if (buf != null) {
                    if (negative) {
                        buf.append((byte) 45);
                    }
                    buf.append(byteCache[v]);
                    return null;
                } else {
                    String tmp = PdfEncodings.convertToString(byteCache[v], null);
                    if (negative) {
                        tmp = "-" + tmp;
                    }
                    return tmp;
                }
            } else {
                v = (long) (d + 0.5d);
                if (negative) {
                    return "-" + Long.toString(v);
                }
                return Long.toString(v);
            }
        } else if (buf == null) {
            return "0";
        } else {
            buf.append((byte) ZERO);
            return null;
        }
    }

    public void reset() {
        this.count = 0;
    }

    public byte[] toByteArray() {
        byte[] newbuf = new byte[this.count];
        System.arraycopy(this.buf, 0, newbuf, 0, this.count);
        return newbuf;
    }

    public int size() {
        return this.count;
    }

    public void setSize(int size) {
        if (size > this.count || size < 0) {
            throw new IndexOutOfBoundsException(MessageLocalization.getComposedMessage("the.new.size.must.be.positive.and.lt.eq.of.the.current.size", new Object[0]));
        }
        this.count = size;
    }

    public String toString() {
        return new String(this.buf, 0, this.count);
    }

    public String toString(String enc) throws UnsupportedEncodingException {
        return new String(this.buf, 0, this.count, enc);
    }

    public void writeTo(OutputStream out) throws IOException {
        out.write(this.buf, 0, this.count);
    }

    public void write(int b) throws IOException {
        append((byte) b);
    }

    public void write(byte[] b, int off, int len) {
        append(b, off, len);
    }

    public byte[] getBuffer() {
        return this.buf;
    }
}
