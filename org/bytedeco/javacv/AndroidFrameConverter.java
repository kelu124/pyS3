package org.bytedeco.javacv;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class AndroidFrameConverter extends FrameConverter<Bitmap> {
    static final /* synthetic */ boolean $assertionsDisabled = (!AndroidFrameConverter.class.desiredAssertionStatus());
    Bitmap bitmap;
    ByteBuffer buffer;
    byte[] row;

    static /* synthetic */ class C12371 {
        static final /* synthetic */ int[] $SwitchMap$android$graphics$Bitmap$Config = new int[Config.values().length];

        static {
            try {
                $SwitchMap$android$graphics$Bitmap$Config[Config.ALPHA_8.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$android$graphics$Bitmap$Config[Config.RGB_565.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$android$graphics$Bitmap$Config[Config.ARGB_4444.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$android$graphics$Bitmap$Config[Config.ARGB_8888.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
        }
    }

    public Frame convert(byte[] data, int width, int height) {
        if (!(this.frame != null && this.frame.imageWidth == width && this.frame.imageHeight == height && this.frame.imageChannels == 3)) {
            this.frame = new Frame(width, height, 8, 3);
        }
        ByteBuffer out = this.frame.image[0];
        int stride = this.frame.imageStride;
        int offset = height * width;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int Y = (data[(i * width) + j] & 255) - 16;
                int U = (data[((((i / 2) * width) + offset) + ((j / 2) * 2)) + 1] & 255) - 128;
                int V = (data[(((i / 2) * width) + offset) + ((j / 2) * 2)] & 255) - 128;
                if (Y < 0) {
                    Y = 0;
                }
                int B = (Y * 1192) + (U * 2066);
                int G = ((Y * 1192) - (V * 833)) - (U * 400);
                int R = Math.min(262143, Math.max(0, (Y * 1192) + (V * 1634)));
                R = (R >> 10) & 255;
                G = (Math.min(262143, Math.max(0, G)) >> 10) & 255;
                out.put((i * stride) + (j * 3), (byte) ((Math.min(262143, Math.max(0, B)) >> 10) & 255));
                out.put(((i * stride) + (j * 3)) + 1, (byte) G);
                out.put(((i * stride) + (j * 3)) + 2, (byte) R);
            }
        }
        return this.frame;
    }

    public Frame convert(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        int channels = 0;
        switch (C12371.$SwitchMap$android$graphics$Bitmap$Config[bitmap.getConfig().ordinal()]) {
            case 1:
                channels = 1;
                break;
            case 2:
            case 3:
                channels = 2;
                break;
            case 4:
                channels = 4;
                break;
            default:
                if (!$assertionsDisabled) {
                    throw new AssertionError();
                }
                break;
        }
        if (!(this.frame != null && this.frame.imageWidth == bitmap.getWidth() && this.frame.imageHeight == bitmap.getHeight() && this.frame.imageChannels == channels)) {
            this.frame = new Frame(bitmap.getWidth(), bitmap.getHeight(), 8, channels);
        }
        bitmap.copyPixelsToBuffer(this.frame.image[0].position(0));
        return this.frame;
    }

    ByteBuffer gray2rgba(ByteBuffer in, int width, int height, int stride, int rowBytes) {
        if (this.buffer == null || this.buffer.capacity() < height * rowBytes) {
            this.buffer = ByteBuffer.allocate(height * rowBytes);
        }
        if (this.row == null || this.row.length != stride) {
            this.row = new byte[stride];
        }
        for (int y = 0; y < height; y++) {
            in.position(y * stride);
            in.get(this.row);
            for (int x = 0; x < width; x++) {
                byte B = this.row[x];
                this.buffer.putInt((y * rowBytes) + (x * 4), ((((B & 255) << 24) | ((B & 255) << 16)) | ((B & 255) << 8)) | 255);
            }
        }
        return this.buffer;
    }

    ByteBuffer bgr2rgba(ByteBuffer in, int width, int height, int stride, int rowBytes) {
        if (!in.order().equals(ByteOrder.LITTLE_ENDIAN)) {
            in = in.order(ByteOrder.LITTLE_ENDIAN);
        }
        if (this.buffer == null || this.buffer.capacity() < height * rowBytes) {
            this.buffer = ByteBuffer.allocate(height * rowBytes);
        }
        int y = 0;
        while (y < height) {
            for (int x = 0; x < width; x++) {
                int rgb;
                if (x < width - 1 || y < height - 1) {
                    rgb = in.getInt((y * stride) + (x * 3));
                } else {
                    rgb = (((in.get(((y * stride) + (x * 3)) + 2) & 255) << 16) | ((in.get(((y * stride) + (x * 3)) + 1) & 255) << 8)) | (in.get((y * stride) + (x * 3)) & 255);
                }
                this.buffer.putInt((y * rowBytes) + (x * 4), (rgb << 8) | 255);
            }
            y++;
        }
        return this.buffer;
    }

    public Bitmap convert(Frame frame) {
        if (frame == null || frame.image == null) {
            return null;
        }
        Config config = null;
        switch (frame.imageChannels) {
            case 1:
            case 3:
            case 4:
                config = Config.ARGB_8888;
                break;
            case 2:
                config = Config.RGB_565;
                break;
            default:
                if (!$assertionsDisabled) {
                    throw new AssertionError();
                }
                break;
        }
        if (!(this.bitmap != null && this.bitmap.getWidth() == frame.imageWidth && this.bitmap.getHeight() == frame.imageHeight && this.bitmap.getConfig() == config)) {
            this.bitmap = Bitmap.createBitmap(frame.imageWidth, frame.imageHeight, config);
        }
        ByteBuffer in = frame.image[0];
        int width = frame.imageWidth;
        int height = frame.imageHeight;
        int stride = frame.imageStride;
        int rowBytes = this.bitmap.getRowBytes();
        if (frame.imageChannels == 1) {
            gray2rgba(in, width, height, stride, rowBytes);
            this.bitmap.copyPixelsFromBuffer(this.buffer.position(0));
        } else if (frame.imageChannels == 3) {
            bgr2rgba(in, width, height, stride, rowBytes);
            this.bitmap.copyPixelsFromBuffer(this.buffer.position(0));
        } else {
            this.bitmap.copyPixelsFromBuffer(in.position(0));
        }
        return this.bitmap;
    }
}
