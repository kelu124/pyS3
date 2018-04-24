package org.bytedeco.javacv;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.nio.ShortBuffer;
import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.DoublePointer;
import org.bytedeco.javacpp.FloatPointer;
import org.bytedeco.javacpp.IntPointer;
import org.bytedeco.javacpp.LongPointer;
import org.bytedeco.javacpp.ShortPointer;
import org.bytedeco.javacpp.indexer.ByteIndexer;
import org.bytedeco.javacpp.indexer.DoubleIndexer;
import org.bytedeco.javacpp.indexer.FloatIndexer;
import org.bytedeco.javacpp.indexer.Indexable;
import org.bytedeco.javacpp.indexer.Indexer;
import org.bytedeco.javacpp.indexer.IntIndexer;
import org.bytedeco.javacpp.indexer.LongIndexer;
import org.bytedeco.javacpp.indexer.ShortIndexer;
import org.bytedeco.javacpp.indexer.UByteIndexer;
import org.bytedeco.javacpp.indexer.UShortIndexer;

public class Frame implements Indexable {
    static final /* synthetic */ boolean $assertionsDisabled = (!Frame.class.desiredAssertionStatus());
    public static final int DEPTH_BYTE = -8;
    public static final int DEPTH_DOUBLE = 64;
    public static final int DEPTH_FLOAT = 32;
    public static final int DEPTH_INT = -32;
    public static final int DEPTH_LONG = -64;
    public static final int DEPTH_SHORT = -16;
    public static final int DEPTH_UBYTE = 8;
    public static final int DEPTH_USHORT = 16;
    public int audioChannels;
    public Buffer[] image;
    public int imageChannels;
    public int imageDepth;
    public int imageHeight;
    public int imageStride;
    public int imageWidth;
    public boolean keyFrame;
    public Object opaque;
    public int sampleRate;
    public Buffer[] samples;
    public long timestamp;

    public Frame(int width, int height, int depth, int channels) {
        int pixelSize = Math.abs(depth) / 8;
        this.imageWidth = width;
        this.imageHeight = height;
        this.imageDepth = depth;
        this.imageChannels = channels;
        this.imageStride = ((((this.imageWidth * this.imageChannels) * pixelSize) + 7) & -8) / pixelSize;
        this.image = new Buffer[1];
        ByteBuffer buffer = ByteBuffer.allocateDirect((this.imageHeight * this.imageStride) * pixelSize).order(ByteOrder.nativeOrder());
        switch (this.imageDepth) {
            case DEPTH_LONG /*-64*/:
                this.image[0] = buffer.asLongBuffer();
                return;
            case -32:
                this.image[0] = buffer.asIntBuffer();
                return;
            case -16:
            case 16:
                this.image[0] = buffer.asShortBuffer();
                return;
            case -8:
            case 8:
                this.image[0] = buffer;
                return;
            case 32:
                this.image[0] = buffer.asFloatBuffer();
                return;
            case 64:
                this.image[0] = buffer.asDoubleBuffer();
                return;
            default:
                throw new UnsupportedOperationException("Unsupported depth value: " + this.imageDepth);
        }
    }

    public <I extends Indexer> I createIndexer() {
        return createIndexer(true, 0);
    }

    public <I extends Indexer> I createIndexer(boolean direct) {
        return createIndexer(direct, 0);
    }

    public <I extends Indexer> I createIndexer(boolean direct, int i) {
        Object array;
        long[] sizes = new long[]{(long) this.imageHeight, (long) this.imageWidth, (long) this.imageChannels};
        long[] strides = new long[]{(long) this.imageStride, (long) this.imageChannels, 1};
        Buffer buffer = this.image[i];
        if (buffer.hasArray()) {
            array = buffer.array();
        } else {
            array = null;
        }
        switch (this.imageDepth) {
            case DEPTH_LONG /*-64*/:
                if (array != null) {
                    return LongIndexer.create((long[]) array, sizes, strides).indexable(this);
                }
                if (direct) {
                    return LongIndexer.create((LongBuffer) buffer, sizes, strides).indexable(this);
                }
                return LongIndexer.create(new LongPointer((LongBuffer) buffer), sizes, strides, false).indexable(this);
            case -32:
                if (array != null) {
                    return IntIndexer.create((int[]) array, sizes, strides).indexable(this);
                }
                if (direct) {
                    return IntIndexer.create((IntBuffer) buffer, sizes, strides).indexable(this);
                }
                return IntIndexer.create(new IntPointer((IntBuffer) buffer), sizes, strides, false).indexable(this);
            case -16:
                if (array != null) {
                    return ShortIndexer.create((short[]) array, sizes, strides).indexable(this);
                }
                if (direct) {
                    return ShortIndexer.create((ShortBuffer) buffer, sizes, strides).indexable(this);
                }
                return ShortIndexer.create(new ShortPointer((ShortBuffer) buffer), sizes, strides, false).indexable(this);
            case -8:
                if (array != null) {
                    return ByteIndexer.create((byte[]) array, sizes, strides).indexable(this);
                }
                if (direct) {
                    return ByteIndexer.create((ByteBuffer) buffer, sizes, strides).indexable(this);
                }
                return ByteIndexer.create(new BytePointer((ByteBuffer) buffer), sizes, strides, false).indexable(this);
            case 8:
                if (array != null) {
                    return UByteIndexer.create((byte[]) array, sizes, strides).indexable(this);
                }
                if (direct) {
                    return UByteIndexer.create((ByteBuffer) buffer, sizes, strides).indexable(this);
                }
                return UByteIndexer.create(new BytePointer((ByteBuffer) buffer), sizes, strides, false).indexable(this);
            case 16:
                if (array != null) {
                    return UShortIndexer.create((short[]) array, sizes, strides).indexable(this);
                }
                if (direct) {
                    return UShortIndexer.create((ShortBuffer) buffer, sizes, strides).indexable(this);
                }
                return UShortIndexer.create(new ShortPointer((ShortBuffer) buffer), sizes, strides, false).indexable(this);
            case 32:
                if (array != null) {
                    return FloatIndexer.create((float[]) array, sizes, strides).indexable(this);
                }
                if (direct) {
                    return FloatIndexer.create((FloatBuffer) buffer, sizes, strides).indexable(this);
                }
                return FloatIndexer.create(new FloatPointer((FloatBuffer) buffer), sizes, strides, false).indexable(this);
            case 64:
                if (array != null) {
                    return DoubleIndexer.create((double[]) array, sizes, strides).indexable(this);
                }
                if (direct) {
                    return DoubleIndexer.create((DoubleBuffer) buffer, sizes, strides).indexable(this);
                }
                return DoubleIndexer.create(new DoublePointer((DoubleBuffer) buffer), sizes, strides, false).indexable(this);
            default:
                if ($assertionsDisabled) {
                    return null;
                }
                throw new AssertionError();
        }
    }

    public Frame clone() {
        Frame newFrame = new Frame();
        newFrame.imageWidth = this.imageWidth;
        newFrame.imageHeight = this.imageHeight;
        newFrame.imageDepth = this.imageDepth;
        newFrame.imageChannels = this.imageChannels;
        newFrame.imageStride = this.imageStride;
        newFrame.keyFrame = this.keyFrame;
        newFrame.opaque = this.opaque;
        newFrame.image = cloneBufferArray(this.image);
        newFrame.audioChannels = this.audioChannels;
        newFrame.sampleRate = this.sampleRate;
        newFrame.samples = cloneBufferArray(this.samples);
        newFrame.timestamp = this.timestamp;
        return newFrame;
    }

    private static Buffer[] cloneBufferArray(Buffer[] srcBuffers) {
        Buffer[] clonedBuffers = null;
        if (srcBuffers != null) {
            int i;
            clonedBuffers = new Buffer[srcBuffers.length];
            for (Buffer rewind : srcBuffers) {
                rewind.rewind();
            }
            if (srcBuffers[0] instanceof ByteBuffer) {
                for (i = 0; i < srcBuffers.length; i++) {
                    clonedBuffers[i] = ByteBuffer.allocateDirect(srcBuffers[i].capacity()).put((ByteBuffer) srcBuffers[i]).rewind();
                }
            } else if (srcBuffers[0] instanceof ShortBuffer) {
                for (i = 0; i < srcBuffers.length; i++) {
                    clonedBuffers[i] = ByteBuffer.allocateDirect(srcBuffers[i].capacity() * (short) 2).order(ByteOrder.nativeOrder()).asShortBuffer().put((ShortBuffer) srcBuffers[i]).rewind();
                }
            } else if (srcBuffers[0] instanceof IntBuffer) {
                for (i = 0; i < srcBuffers.length; i++) {
                    clonedBuffers[i] = ByteBuffer.allocateDirect(srcBuffers[i].capacity() * (short) 4).order(ByteOrder.nativeOrder()).asIntBuffer().put((IntBuffer) srcBuffers[i]).rewind();
                }
            } else if (srcBuffers[0] instanceof LongBuffer) {
                for (i = 0; i < srcBuffers.length; i++) {
                    clonedBuffers[i] = ByteBuffer.allocateDirect(srcBuffers[i].capacity() * (short) 8).order(ByteOrder.nativeOrder()).asLongBuffer().put((LongBuffer) srcBuffers[i]).rewind();
                }
            } else if (srcBuffers[0] instanceof FloatBuffer) {
                for (i = 0; i < srcBuffers.length; i++) {
                    clonedBuffers[i] = ByteBuffer.allocateDirect(srcBuffers[i].capacity() * (short) 4).order(ByteOrder.nativeOrder()).asFloatBuffer().put((FloatBuffer) srcBuffers[i]).rewind();
                }
            } else if (srcBuffers[0] instanceof DoubleBuffer) {
                for (i = 0; i < srcBuffers.length; i++) {
                    clonedBuffers[i] = ByteBuffer.allocateDirect(srcBuffers[i].capacity() * (short) 8).order(ByteOrder.nativeOrder()).asDoubleBuffer().put((DoubleBuffer) srcBuffers[i]).rewind();
                }
            }
            for (Buffer rewind2 : srcBuffers) {
                rewind2.rewind();
            }
        }
        return clonedBuffers;
    }
}
