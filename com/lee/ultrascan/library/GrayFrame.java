package com.lee.ultrascan.library;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import com.google.common.base.Preconditions;

public class GrayFrame {
    private final int mHeight;
    private boolean mIsReleased;
    private final long mNativeHandler;
    private final int mWidth;

    private native long nativeCopy(long j);

    private native long nativeCreate(int i, int i2);

    private native void nativeGetRgbaColors(long j, Bitmap bitmap);

    private native void nativeRelease(long j);

    private native void nativeSetGrayValues(long j, Bitmap bitmap);

    public GrayFrame(int width, int height) {
        this.mWidth = width;
        this.mHeight = height;
        this.mNativeHandler = nativeCreate(width, height);
        this.mIsReleased = false;
    }

    public GrayFrame(Bitmap inBitmap) {
        if (inBitmap.getConfig() != Config.ARGB_8888) {
            throw new RuntimeException("outBitmap config must be ARGB_8888!");
        }
        this.mWidth = inBitmap.getWidth();
        this.mHeight = inBitmap.getHeight();
        this.mNativeHandler = nativeCreate(this.mWidth, this.mHeight);
        this.mIsReleased = false;
        setPixels(inBitmap);
    }

    private GrayFrame(int width, int height, long nativeHandler) {
        this.mWidth = width;
        this.mHeight = height;
        this.mNativeHandler = nativeHandler;
        this.mIsReleased = false;
    }

    public GrayFrame copy() {
        Preconditions.checkState(!this.mIsReleased);
        return new GrayFrame(this.mWidth, this.mHeight, nativeCopy(this.mNativeHandler));
    }

    public int getWidth() {
        return this.mWidth;
    }

    public int getHeight() {
        return this.mHeight;
    }

    public long getNativeHandler() {
        Preconditions.checkState(!this.mIsReleased);
        return this.mNativeHandler;
    }

    public void setPixels(Bitmap inBitmap) {
        boolean z = true;
        if (inBitmap.getConfig() != Config.ARGB_8888) {
            throw new RuntimeException("outBitmap config must be ARGB_8888!");
        }
        boolean z2;
        if (inBitmap.getWidth() == this.mWidth) {
            z2 = true;
        } else {
            z2 = false;
        }
        Preconditions.checkArgument(z2);
        if (inBitmap.getHeight() == this.mHeight) {
            z2 = true;
        } else {
            z2 = false;
        }
        Preconditions.checkArgument(z2);
        if (this.mIsReleased) {
            z = false;
        }
        Preconditions.checkState(z);
        nativeSetGrayValues(this.mNativeHandler, inBitmap);
    }

    public void getPixels(Bitmap outBitmap) {
        boolean z = true;
        if (outBitmap.getConfig() != Config.ARGB_8888) {
            throw new RuntimeException("outBitmap config must be ARGB_8888!");
        }
        boolean z2;
        if (outBitmap.getWidth() == this.mWidth) {
            z2 = true;
        } else {
            z2 = false;
        }
        Preconditions.checkArgument(z2);
        if (outBitmap.getHeight() == this.mHeight) {
            z2 = true;
        } else {
            z2 = false;
        }
        Preconditions.checkArgument(z2);
        if (this.mIsReleased) {
            z = false;
        }
        Preconditions.checkState(z);
        nativeGetRgbaColors(this.mNativeHandler, outBitmap);
    }

    public void release() {
        Preconditions.checkState(!this.mIsReleased);
        nativeRelease(this.mNativeHandler);
        this.mIsReleased = true;
    }

    protected void finalize() throws Throwable {
        super.finalize();
        if (!this.mIsReleased) {
            throw new RuntimeException("GrayFrame has not been manually released!");
        }
    }
}
