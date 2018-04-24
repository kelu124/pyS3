package org.bytedeco.javacv;

import java.awt.Rectangle;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.ComponentSampleModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferDouble;
import java.awt.image.DataBufferFloat;
import java.awt.image.DataBufferInt;
import java.awt.image.DataBufferShort;
import java.awt.image.DataBufferUShort;
import java.awt.image.MultiPixelPackedSampleModel;
import java.awt.image.Raster;
import java.awt.image.SampleModel;
import java.awt.image.SinglePixelPackedSampleModel;
import java.awt.image.WritableRaster;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

public class Java2DFrameConverter extends FrameConverter<BufferedImage> {
    static final /* synthetic */ boolean $assertionsDisabled;
    public static final byte[] gamma22 = new byte[256];
    public static final byte[] gamma22inv = new byte[256];
    protected BufferedImage bufferedImage = null;

    static {
        boolean z;
        if (Java2DFrameConverter.class.desiredAssertionStatus()) {
            z = false;
        } else {
            z = true;
        }
        $assertionsDisabled = z;
        for (int i = 0; i < 256; i++) {
            gamma22[i] = (byte) ((int) Math.round(Math.pow(((double) i) / 255.0d, 2.2d) * 255.0d));
            gamma22inv[i] = (byte) ((int) Math.round(Math.pow(((double) i) / 255.0d, 0.45454545454545453d) * 255.0d));
        }
    }

    public Frame convert(BufferedImage img) {
        return getFrame(img);
    }

    public BufferedImage convert(Frame frame) {
        return getBufferedImage(frame);
    }

    public static BufferedImage cloneBufferedImage(BufferedImage bufferedImage) {
        if (bufferedImage == null) {
            return null;
        }
        BufferedImage bi = bufferedImage;
        int type = bi.getType();
        if (type == 0) {
            return new BufferedImage(bi.getColorModel(), bi.copyData(null), bi.isAlphaPremultiplied(), null);
        }
        return new BufferedImage(bi.getWidth(), bi.getHeight(), type);
    }

    public static int decodeGamma22(int value) {
        return gamma22[value & 255] & 255;
    }

    public static int encodeGamma22(int value) {
        return gamma22inv[value & 255] & 255;
    }

    public static void flipCopyWithGamma(ByteBuffer srcBuf, int srcStep, ByteBuffer dstBuf, int dstStep, boolean signed, double gamma, boolean flip, int channels) {
        if ($assertionsDisabled || srcBuf != dstBuf) {
            int w = Math.min(srcStep, dstStep);
            int srcLine = srcBuf.position();
            int dstLine = dstBuf.position();
            byte[] buffer = new byte[channels];
            while (srcLine < srcBuf.capacity() && dstLine < dstBuf.capacity()) {
                if (flip) {
                    srcBuf.position((srcBuf.capacity() - srcLine) - srcStep);
                } else {
                    srcBuf.position(srcLine);
                }
                dstBuf.position(dstLine);
                w = Math.min(Math.min(w, srcBuf.remaining()), dstBuf.remaining());
                int x;
                int z;
                int in;
                byte out;
                if (signed) {
                    if (channels > 1) {
                        x = 0;
                        while (x < w) {
                            for (z = 0; z < channels; z++) {
                                in = srcBuf.get();
                                if (gamma == 1.0d) {
                                    out = (byte) in;
                                } else {
                                    out = (byte) ((int) Math.round(Math.pow(((double) in) / 127.0d, gamma) * 127.0d));
                                }
                                buffer[z] = out;
                            }
                            for (z = channels - 1; z >= 0; z--) {
                                dstBuf.put(buffer[z]);
                            }
                            x += channels;
                        }
                    } else {
                        for (x = 0; x < w; x++) {
                            in = srcBuf.get();
                            if (gamma == 1.0d) {
                                out = (byte) in;
                            } else {
                                out = (byte) ((int) Math.round(Math.pow(((double) in) / 127.0d, gamma) * 127.0d));
                            }
                            dstBuf.put(out);
                        }
                    }
                } else if (channels > 1) {
                    x = 0;
                    while (x < w) {
                        for (z = 0; z < channels; z++) {
                            in = srcBuf.get() & 255;
                            if (gamma == 1.0d) {
                                out = (byte) in;
                            } else if (gamma == 2.2d) {
                                out = gamma22[in];
                            } else if (gamma == 0.45454545454545453d) {
                                out = gamma22inv[in];
                            } else {
                                out = (byte) ((int) Math.round(Math.pow(((double) in) / 255.0d, gamma) * 255.0d));
                            }
                            buffer[z] = out;
                        }
                        for (z = channels - 1; z >= 0; z--) {
                            dstBuf.put(buffer[z]);
                        }
                        x += channels;
                    }
                } else {
                    for (x = 0; x < w; x++) {
                        in = srcBuf.get() & 255;
                        if (gamma == 1.0d) {
                            out = (byte) in;
                        } else if (gamma == 2.2d) {
                            out = gamma22[in];
                        } else if (gamma == 0.45454545454545453d) {
                            out = gamma22inv[in];
                        } else {
                            out = (byte) ((int) Math.round(Math.pow(((double) in) / 255.0d, gamma) * 255.0d));
                        }
                        dstBuf.put(out);
                    }
                }
                srcLine += srcStep;
                dstLine += dstStep;
            }
            return;
        }
        throw new AssertionError();
    }

    public static void flipCopyWithGamma(ShortBuffer srcBuf, int srcStep, ShortBuffer dstBuf, int dstStep, boolean signed, double gamma, boolean flip, int channels) {
        if ($assertionsDisabled || srcBuf != dstBuf) {
            int w = Math.min(srcStep, dstStep);
            int srcLine = srcBuf.position();
            int dstLine = dstBuf.position();
            short[] buffer = new short[channels];
            while (srcLine < srcBuf.capacity() && dstLine < dstBuf.capacity()) {
                if (flip) {
                    srcBuf.position((srcBuf.capacity() - srcLine) - srcStep);
                } else {
                    srcBuf.position(srcLine);
                }
                dstBuf.position(dstLine);
                w = Math.min(Math.min(w, srcBuf.remaining()), dstBuf.remaining());
                int x;
                int z;
                int in;
                short out;
                if (signed) {
                    if (channels > 1) {
                        x = 0;
                        while (x < w) {
                            for (z = 0; z < channels; z++) {
                                in = srcBuf.get();
                                if (gamma == 1.0d) {
                                    out = (short) in;
                                } else {
                                    out = (short) ((int) Math.round(Math.pow(((double) in) / 32767.0d, gamma) * 32767.0d));
                                }
                                buffer[z] = out;
                            }
                            for (z = channels - 1; z >= 0; z--) {
                                dstBuf.put(buffer[z]);
                            }
                            x += channels;
                        }
                    } else {
                        for (x = 0; x < w; x++) {
                            in = srcBuf.get();
                            if (gamma == 1.0d) {
                                out = (short) in;
                            } else {
                                out = (short) ((int) Math.round(Math.pow(((double) in) / 32767.0d, gamma) * 32767.0d));
                            }
                            dstBuf.put(out);
                        }
                    }
                } else if (channels > 1) {
                    x = 0;
                    while (x < w) {
                        for (z = 0; z < channels; z++) {
                            in = srcBuf.get();
                            if (gamma == 1.0d) {
                                out = (short) in;
                            } else {
                                out = (short) ((int) Math.round(Math.pow(((double) in) / 65535.0d, gamma) * 65535.0d));
                            }
                            buffer[z] = out;
                        }
                        for (z = channels - 1; z >= 0; z--) {
                            dstBuf.put(buffer[z]);
                        }
                        x += channels;
                    }
                } else {
                    for (x = 0; x < w; x++) {
                        in = srcBuf.get() & 65535;
                        if (gamma == 1.0d) {
                            out = (short) in;
                        } else {
                            out = (short) ((int) Math.round(Math.pow(((double) in) / 65535.0d, gamma) * 65535.0d));
                        }
                        dstBuf.put(out);
                    }
                }
                srcLine += srcStep;
                dstLine += dstStep;
            }
            return;
        }
        throw new AssertionError();
    }

    public static void flipCopyWithGamma(IntBuffer srcBuf, int srcStep, IntBuffer dstBuf, int dstStep, double gamma, boolean flip, int channels) {
        if ($assertionsDisabled || srcBuf != dstBuf) {
            int w = Math.min(srcStep, dstStep);
            int srcLine = srcBuf.position();
            int dstLine = dstBuf.position();
            int[] buffer = new int[channels];
            while (srcLine < srcBuf.capacity() && dstLine < dstBuf.capacity()) {
                if (flip) {
                    srcBuf.position((srcBuf.capacity() - srcLine) - srcStep);
                } else {
                    srcBuf.position(srcLine);
                }
                dstBuf.position(dstLine);
                w = Math.min(Math.min(w, srcBuf.remaining()), dstBuf.remaining());
                int x;
                int in;
                int out;
                if (channels > 1) {
                    x = 0;
                    while (x < w) {
                        int z;
                        for (z = 0; z < channels; z++) {
                            in = srcBuf.get();
                            if (gamma == 1.0d) {
                                out = in;
                            } else {
                                out = (int) Math.round(Math.pow(((double) in) / 2.147483647E9d, gamma) * 2.147483647E9d);
                            }
                            buffer[z] = out;
                        }
                        for (z = channels - 1; z >= 0; z--) {
                            dstBuf.put(buffer[z]);
                        }
                        x += channels;
                    }
                } else {
                    for (x = 0; x < w; x++) {
                        in = srcBuf.get();
                        if (gamma == 1.0d) {
                            out = in;
                        } else {
                            out = (int) Math.round(Math.pow(((double) in) / 2.147483647E9d, gamma) * 2.147483647E9d);
                        }
                        dstBuf.put(out);
                    }
                }
                srcLine += srcStep;
                dstLine += dstStep;
            }
            return;
        }
        throw new AssertionError();
    }

    public static void flipCopyWithGamma(FloatBuffer srcBuf, int srcStep, FloatBuffer dstBuf, int dstStep, double gamma, boolean flip, int channels) {
        if ($assertionsDisabled || srcBuf != dstBuf) {
            int w = Math.min(srcStep, dstStep);
            int srcLine = srcBuf.position();
            int dstLine = dstBuf.position();
            float[] buffer = new float[channels];
            while (srcLine < srcBuf.capacity() && dstLine < dstBuf.capacity()) {
                if (flip) {
                    srcBuf.position((srcBuf.capacity() - srcLine) - srcStep);
                } else {
                    srcBuf.position(srcLine);
                }
                dstBuf.position(dstLine);
                w = Math.min(Math.min(w, srcBuf.remaining()), dstBuf.remaining());
                int x;
                float in;
                float out;
                if (channels > 1) {
                    x = 0;
                    while (x < w) {
                        int z;
                        for (z = 0; z < channels; z++) {
                            in = srcBuf.get();
                            if (gamma == 1.0d) {
                                out = in;
                            } else {
                                out = (float) Math.pow((double) in, gamma);
                            }
                            buffer[z] = out;
                        }
                        for (z = channels - 1; z >= 0; z--) {
                            dstBuf.put(buffer[z]);
                        }
                        x += channels;
                    }
                } else {
                    for (x = 0; x < w; x++) {
                        in = srcBuf.get();
                        if (gamma == 1.0d) {
                            out = in;
                        } else {
                            out = (float) Math.pow((double) in, gamma);
                        }
                        dstBuf.put(out);
                    }
                }
                srcLine += srcStep;
                dstLine += dstStep;
            }
            return;
        }
        throw new AssertionError();
    }

    public static void flipCopyWithGamma(DoubleBuffer srcBuf, int srcStep, DoubleBuffer dstBuf, int dstStep, double gamma, boolean flip, int channels) {
        if ($assertionsDisabled || srcBuf != dstBuf) {
            int w = Math.min(srcStep, dstStep);
            int srcLine = srcBuf.position();
            int dstLine = dstBuf.position();
            double[] buffer = new double[channels];
            while (srcLine < srcBuf.capacity() && dstLine < dstBuf.capacity()) {
                if (flip) {
                    srcBuf.position((srcBuf.capacity() - srcLine) - srcStep);
                } else {
                    srcBuf.position(srcLine);
                }
                dstBuf.position(dstLine);
                w = Math.min(Math.min(w, srcBuf.remaining()), dstBuf.remaining());
                int x;
                double in;
                double out;
                if (channels > 1) {
                    x = 0;
                    while (x < w) {
                        int z;
                        for (z = 0; z < channels; z++) {
                            in = srcBuf.get();
                            if (gamma == 1.0d) {
                                out = in;
                            } else {
                                out = Math.pow(in, gamma);
                            }
                            buffer[z] = out;
                        }
                        for (z = channels - 1; z >= 0; z--) {
                            dstBuf.put(buffer[z]);
                        }
                        x += channels;
                    }
                } else {
                    for (x = 0; x < w; x++) {
                        in = srcBuf.get();
                        if (gamma == 1.0d) {
                            out = in;
                        } else {
                            out = Math.pow(in, gamma);
                        }
                        dstBuf.put(out);
                    }
                }
                srcLine += srcStep;
                dstLine += dstStep;
            }
            return;
        }
        throw new AssertionError();
    }

    public static void applyGamma(Frame frame, double gamma) {
        applyGamma(frame.image[0].position(0), frame.imageDepth, frame.imageStride, gamma);
    }

    public static void applyGamma(Buffer buffer, int depth, int stride, double gamma) {
        if (gamma != 1.0d) {
            switch (depth) {
                case -32:
                    flipCopyWithGamma(((IntBuffer) buffer).asReadOnlyBuffer(), stride, (IntBuffer) buffer, stride, gamma, false, 0);
                    return;
                case -16:
                    flipCopyWithGamma(((ShortBuffer) buffer).asReadOnlyBuffer(), stride, (ShortBuffer) buffer, stride, true, gamma, false, 0);
                    return;
                case -8:
                    flipCopyWithGamma(((ByteBuffer) buffer).asReadOnlyBuffer(), stride, (ByteBuffer) buffer, stride, true, gamma, false, 0);
                    return;
                case 8:
                    flipCopyWithGamma(((ByteBuffer) buffer).asReadOnlyBuffer(), stride, (ByteBuffer) buffer, stride, false, gamma, false, 0);
                    return;
                case 16:
                    flipCopyWithGamma(((ShortBuffer) buffer).asReadOnlyBuffer(), stride, (ShortBuffer) buffer, stride, false, gamma, false, 0);
                    return;
                case 32:
                    flipCopyWithGamma(((FloatBuffer) buffer).asReadOnlyBuffer(), stride, (FloatBuffer) buffer, stride, gamma, false, 0);
                    return;
                case 64:
                    flipCopyWithGamma(((DoubleBuffer) buffer).asReadOnlyBuffer(), stride, (DoubleBuffer) buffer, stride, gamma, false, 0);
                    return;
                default:
                    if (!$assertionsDisabled) {
                        throw new AssertionError();
                    }
                    return;
            }
        }
    }

    public static void copy(Frame frame, BufferedImage bufferedImage) {
        copy(frame, bufferedImage, 1.0d);
    }

    public static void copy(Frame frame, BufferedImage bufferedImage, double gamma) {
        copy(frame, bufferedImage, gamma, false, null);
    }

    public static void copy(Frame frame, BufferedImage bufferedImage, double gamma, boolean flipChannels, Rectangle roi) {
        int i;
        Buffer buffer = frame.image[0];
        if (roi == null) {
            i = 0;
        } else {
            i = (roi.y * frame.imageStride) + (roi.x * frame.imageChannels);
        }
        Buffer in = buffer.position(i);
        SampleModel sm = bufferedImage.getSampleModel();
        Raster r = bufferedImage.getRaster();
        DataBuffer out = r.getDataBuffer();
        int x = -r.getSampleModelTranslateX();
        int y = -r.getSampleModelTranslateY();
        int step = sm.getWidth() * sm.getNumBands();
        int channels = sm.getNumBands();
        if (sm instanceof ComponentSampleModel) {
            step = ((ComponentSampleModel) sm).getScanlineStride();
            channels = ((ComponentSampleModel) sm).getPixelStride();
        } else if (sm instanceof SinglePixelPackedSampleModel) {
            step = ((SinglePixelPackedSampleModel) sm).getScanlineStride();
            channels = 1;
        } else if (sm instanceof MultiPixelPackedSampleModel) {
            step = ((MultiPixelPackedSampleModel) sm).getScanlineStride();
            channels = ((MultiPixelPackedSampleModel) sm).getPixelBitStride() / 8;
        }
        int start = (y * step) + (x * channels);
        int i2;
        if (out instanceof DataBufferByte) {
            int i3;
            byte[] a = ((DataBufferByte) out).getData();
            ByteBuffer byteBuffer = (ByteBuffer) in;
            i2 = frame.imageStride;
            ByteBuffer wrap = ByteBuffer.wrap(a, start, a.length - start);
            if (flipChannels) {
                i3 = channels;
            } else {
                i3 = 0;
            }
            flipCopyWithGamma(byteBuffer, i2, wrap, step, false, gamma, false, i3);
        } else if (out instanceof DataBufferDouble) {
            double[] a2 = ((DataBufferDouble) out).getData();
            flipCopyWithGamma((DoubleBuffer) in, frame.imageStride, DoubleBuffer.wrap(a2, start, a2.length - start), step, gamma, false, flipChannels ? channels : 0);
        } else if (out instanceof DataBufferFloat) {
            float[] a3 = ((DataBufferFloat) out).getData();
            flipCopyWithGamma((FloatBuffer) in, frame.imageStride, FloatBuffer.wrap(a3, start, a3.length - start), step, gamma, false, flipChannels ? channels : 0);
        } else if (out instanceof DataBufferInt) {
            int[] a4 = ((DataBufferInt) out).getData();
            i2 = frame.imageStride;
            if (in instanceof ByteBuffer) {
                in = ((ByteBuffer) in).order(flipChannels ? ByteOrder.LITTLE_ENDIAN : ByteOrder.BIG_ENDIAN).asIntBuffer();
                i2 /= 4;
            }
            flipCopyWithGamma((IntBuffer) in, i2, IntBuffer.wrap(a4, start, a4.length - start), step, gamma, false, flipChannels ? channels : 0);
        } else if (out instanceof DataBufferShort) {
            a = ((DataBufferShort) out).getData();
            flipCopyWithGamma((ShortBuffer) in, frame.imageStride, ShortBuffer.wrap(a, start, a.length - start), step, true, gamma, false, flipChannels ? channels : 0);
        } else if (out instanceof DataBufferUShort) {
            a = ((DataBufferUShort) out).getData();
            flipCopyWithGamma((ShortBuffer) in, frame.imageStride, ShortBuffer.wrap(a, start, a.length - start), step, false, gamma, false, flipChannels ? channels : 0);
        } else if (!$assertionsDisabled) {
            throw new AssertionError();
        }
    }

    public static void copy(BufferedImage image, Frame frame) {
        copy(image, frame, 1.0d);
    }

    public static void copy(BufferedImage image, Frame frame, double gamma) {
        copy(image, frame, gamma, false, null);
    }

    public static void copy(BufferedImage image, Frame frame, double gamma, boolean flipChannels, Rectangle roi) {
        int i;
        Buffer buffer = frame.image[0];
        if (roi == null) {
            i = 0;
        } else {
            i = (roi.y * frame.imageStride) + (roi.x * frame.imageChannels);
        }
        Buffer out = buffer.position(i);
        SampleModel sm = image.getSampleModel();
        Raster r = image.getRaster();
        DataBuffer in = r.getDataBuffer();
        int x = -r.getSampleModelTranslateX();
        int y = -r.getSampleModelTranslateY();
        int step = sm.getWidth() * sm.getNumBands();
        int channels = sm.getNumBands();
        if (sm instanceof ComponentSampleModel) {
            step = ((ComponentSampleModel) sm).getScanlineStride();
            channels = ((ComponentSampleModel) sm).getPixelStride();
        } else if (sm instanceof SinglePixelPackedSampleModel) {
            step = ((SinglePixelPackedSampleModel) sm).getScanlineStride();
            channels = 1;
        } else if (sm instanceof MultiPixelPackedSampleModel) {
            step = ((MultiPixelPackedSampleModel) sm).getScanlineStride();
            channels = ((MultiPixelPackedSampleModel) sm).getPixelBitStride() / 8;
        }
        int start = (y * step) + (x * channels);
        int i2;
        if (in instanceof DataBufferByte) {
            int i3;
            byte[] a = ((DataBufferByte) in).getData();
            ByteBuffer wrap = ByteBuffer.wrap(a, start, a.length - start);
            ByteBuffer byteBuffer = (ByteBuffer) out;
            i2 = frame.imageStride;
            if (flipChannels) {
                i3 = channels;
            } else {
                i3 = 0;
            }
            flipCopyWithGamma(wrap, step, byteBuffer, i2, false, gamma, false, i3);
        } else if (in instanceof DataBufferDouble) {
            double[] a2 = ((DataBufferDouble) in).getData();
            flipCopyWithGamma(DoubleBuffer.wrap(a2, start, a2.length - start), step, (DoubleBuffer) out, frame.imageStride, gamma, false, flipChannels ? channels : 0);
        } else if (in instanceof DataBufferFloat) {
            float[] a3 = ((DataBufferFloat) in).getData();
            flipCopyWithGamma(FloatBuffer.wrap(a3, start, a3.length - start), step, (FloatBuffer) out, frame.imageStride, gamma, false, flipChannels ? channels : 0);
        } else if (in instanceof DataBufferInt) {
            int[] a4 = ((DataBufferInt) in).getData();
            i2 = frame.imageStride;
            if (out instanceof ByteBuffer) {
                out = ((ByteBuffer) out).order(flipChannels ? ByteOrder.LITTLE_ENDIAN : ByteOrder.BIG_ENDIAN).asIntBuffer();
                i2 /= 4;
            }
            flipCopyWithGamma(IntBuffer.wrap(a4, start, a4.length - start), step, (IntBuffer) out, i2, gamma, false, flipChannels ? channels : 0);
        } else if (in instanceof DataBufferShort) {
            a = ((DataBufferShort) in).getData();
            flipCopyWithGamma(ShortBuffer.wrap(a, start, a.length - start), step, (ShortBuffer) out, frame.imageStride, true, gamma, false, flipChannels ? channels : 0);
        } else if (in instanceof DataBufferUShort) {
            a = ((DataBufferUShort) in).getData();
            flipCopyWithGamma(ShortBuffer.wrap(a, start, a.length - start), step, (ShortBuffer) out, frame.imageStride, false, gamma, false, flipChannels ? channels : 0);
        } else if (!$assertionsDisabled) {
            throw new AssertionError();
        }
    }

    public static int getBufferedImageType(Frame frame) {
        if (frame.imageChannels == 1) {
            if (frame.imageDepth == 8 || frame.imageDepth == -8) {
                return 10;
            }
            if (frame.imageDepth == 16) {
                return 11;
            }
            return 0;
        } else if (frame.imageChannels == 3) {
            if (frame.imageDepth == 8 || frame.imageDepth == -8) {
                return 5;
            }
            return 0;
        } else if (frame.imageChannels != 4) {
            return 0;
        } else {
            if (frame.imageDepth == 8 || frame.imageDepth == -8) {
                return 6;
            }
            return 0;
        }
    }

    public BufferedImage getBufferedImage(Frame frame) {
        return getBufferedImage(frame, 1.0d);
    }

    public BufferedImage getBufferedImage(Frame frame, double gamma) {
        return getBufferedImage(frame, gamma, false, null);
    }

    public BufferedImage getBufferedImage(Frame frame, double gamma, boolean flipChannels, ColorSpace cs) {
        if (frame == null || frame.image == null) {
            return null;
        }
        int type = getBufferedImageType(frame);
        if (!(this.bufferedImage != null && this.bufferedImage.getWidth() == frame.imageWidth && this.bufferedImage.getHeight() == frame.imageHeight && this.bufferedImage.getType() == type)) {
            BufferedImage bufferedImage;
            if (type == 0 || cs != null) {
                bufferedImage = null;
            } else {
                bufferedImage = new BufferedImage(frame.imageWidth, frame.imageHeight, type);
            }
            this.bufferedImage = bufferedImage;
        }
        if (this.bufferedImage == null) {
            boolean alpha = false;
            int[] offsets = null;
            if (frame.imageChannels == 1) {
                alpha = false;
                if (cs == null) {
                    cs = ColorSpace.getInstance(1003);
                }
                offsets = new int[]{0};
            } else if (frame.imageChannels == 3) {
                alpha = false;
                if (cs == null) {
                    cs = ColorSpace.getInstance(1004);
                }
                offsets = new int[]{2, 1, 0};
            } else if (frame.imageChannels == 4) {
                alpha = true;
                if (cs == null) {
                    cs = ColorSpace.getInstance(1004);
                }
                offsets = new int[]{0, 1, 2, 3};
            } else if (!$assertionsDisabled) {
                throw new AssertionError();
            }
            ColorModel cm = null;
            WritableRaster wr = null;
            if (frame.imageDepth == 8 || frame.imageDepth == -8) {
                cm = new ComponentColorModel(cs, alpha, false, 1, 0);
                wr = Raster.createWritableRaster(new ComponentSampleModel(0, frame.imageWidth, frame.imageHeight, frame.imageChannels, frame.imageStride, offsets), null);
            } else if (frame.imageDepth == 16) {
                cm = new ComponentColorModel(cs, alpha, false, 1, 1);
                wr = Raster.createWritableRaster(new ComponentSampleModel(1, frame.imageWidth, frame.imageHeight, frame.imageChannels, frame.imageStride, offsets), null);
            } else if (frame.imageDepth == -16) {
                cm = new ComponentColorModel(cs, alpha, false, 1, 2);
                wr = Raster.createWritableRaster(new ComponentSampleModel(2, frame.imageWidth, frame.imageHeight, frame.imageChannels, frame.imageStride, offsets), null);
            } else if (frame.imageDepth == -32) {
                cm = new ComponentColorModel(cs, alpha, false, 1, 3);
                wr = Raster.createWritableRaster(new ComponentSampleModel(3, frame.imageWidth, frame.imageHeight, frame.imageChannels, frame.imageStride, offsets), null);
            } else if (frame.imageDepth == 32) {
                cm = new ComponentColorModel(cs, alpha, false, 1, 4);
                wr = Raster.createWritableRaster(new ComponentSampleModel(4, frame.imageWidth, frame.imageHeight, frame.imageChannels, frame.imageStride, offsets), null);
            } else if (frame.imageDepth == 64) {
                cm = new ComponentColorModel(cs, alpha, false, 1, 5);
                wr = Raster.createWritableRaster(new ComponentSampleModel(5, frame.imageWidth, frame.imageHeight, frame.imageChannels, frame.imageStride, offsets), null);
            } else if (!$assertionsDisabled) {
                throw new AssertionError();
            }
            this.bufferedImage = new BufferedImage(cm, wr, false, null);
        }
        if (this.bufferedImage != null) {
            copy(frame, this.bufferedImage, gamma, flipChannels, null);
        }
        return this.bufferedImage;
    }

    public Frame getFrame(BufferedImage image) {
        return getFrame(image, 1.0d);
    }

    public Frame getFrame(BufferedImage image, double gamma) {
        return getFrame(image, gamma, false);
    }

    public Frame getFrame(BufferedImage image, double gamma, boolean flipChannels) {
        if (image == null) {
            return null;
        }
        SampleModel sm = image.getSampleModel();
        int depth = 0;
        int numChannels = sm.getNumBands();
        switch (image.getType()) {
            case 1:
            case 2:
            case 3:
            case 4:
                depth = 8;
                numChannels = 4;
                break;
        }
        if (depth == 0 || numChannels == 0) {
            switch (sm.getDataType()) {
                case 0:
                    depth = 8;
                    break;
                case 1:
                    depth = 16;
                    break;
                case 2:
                    depth = -16;
                    break;
                case 3:
                    depth = -32;
                    break;
                case 4:
                    depth = 32;
                    break;
                case 5:
                    depth = 64;
                    break;
                default:
                    if (!$assertionsDisabled) {
                        throw new AssertionError();
                    }
                    break;
            }
        }
        if (!(this.frame != null && this.frame.imageWidth == image.getWidth() && this.frame.imageHeight == image.getHeight() && this.frame.imageDepth == depth && this.frame.imageChannels == numChannels)) {
            this.frame = new Frame(image.getWidth(), image.getHeight(), depth, numChannels);
        }
        copy(image, this.frame, gamma, flipChannels, null);
        return this.frame;
    }
}
