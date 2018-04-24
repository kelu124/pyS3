package com.lee.ultrascan.utils;

import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;

public class ByteUtils {
    static final /* synthetic */ boolean $assertionsDisabled = (!ByteUtils.class.desiredAssertionStatus());

    private ByteUtils() {
        throw new AssertionError();
    }

    public static short bytes2ShortLittleEndian(byte[] bytes, int offset) {
        if ($assertionsDisabled || bytes.length - 2 >= offset) {
            return (short) ((MotionEventCompat.ACTION_POINTER_INDEX_MASK & (bytes[offset + 1] << 8)) | ((short) ((bytes[offset] & 255) | (short) 0)));
        }
        throw new AssertionError();
    }

    public static int bytes2IntLittleEndian(byte[] bytes, int offset) {
        if ($assertionsDisabled || bytes.length - 4 >= offset) {
            return (((0 | (bytes[offset] & 255)) | (MotionEventCompat.ACTION_POINTER_INDEX_MASK & (bytes[offset + 1] << 8))) | (16711680 & (bytes[offset + 2] << 16))) | (ViewCompat.MEASURED_STATE_MASK & (bytes[offset + 3] << 24));
        }
        throw new AssertionError();
    }

    public static void fillShort2Bytes(byte[] bytes, int offset, short value) {
        if ($assertionsDisabled || bytes.length - 2 >= offset) {
            bytes[offset] = (byte) (value & 255);
            bytes[offset + 1] = (byte) (MotionEventCompat.ACTION_POINTER_INDEX_MASK & value);
            return;
        }
        throw new AssertionError();
    }
}
