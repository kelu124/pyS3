package com.uuzuche.lib_zxing.decoding;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.ReaderException;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.uuzuche.lib_zxing.C1038R;
import com.uuzuche.lib_zxing.activity.CaptureFragment;
import com.uuzuche.lib_zxing.camera.CameraManager;
import com.uuzuche.lib_zxing.camera.PlanarYUVLuminanceSource;
import java.util.Hashtable;

final class DecodeHandler extends Handler {
    private static final String TAG = DecodeHandler.class.getSimpleName();
    private final CaptureFragment fragment;
    private final MultiFormatReader multiFormatReader = new MultiFormatReader();

    DecodeHandler(CaptureFragment fragment, Hashtable<DecodeHintType, Object> hints) {
        this.multiFormatReader.setHints(hints);
        this.fragment = fragment;
    }

    public void handleMessage(Message message) {
        if (message.what == C1038R.id.decode) {
            decode((byte[]) message.obj, message.arg1, message.arg2);
        } else if (message.what == C1038R.id.quit) {
            Looper.myLooper().quit();
        }
    }

    private void decode(byte[] data, int width, int height) {
        long start = System.currentTimeMillis();
        Result rawResult = null;
        byte[] rotatedData = new byte[data.length];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                rotatedData[(((x * height) + height) - y) - 1] = data[(y * width) + x];
            }
        }
        int tmp = width;
        PlanarYUVLuminanceSource source = CameraManager.get().buildLuminanceSource(rotatedData, height, tmp);
        try {
            rawResult = this.multiFormatReader.decodeWithState(new BinaryBitmap(new HybridBinarizer(source)));
        } catch (ReaderException e) {
        } finally {
            this.multiFormatReader.reset();
        }
        if (rawResult != null) {
            Log.d(TAG, "Found barcode (" + (System.currentTimeMillis() - start) + " ms):\n" + rawResult.toString());
            Message message = Message.obtain(this.fragment.getHandler(), C1038R.id.decode_succeeded, rawResult);
            Bundle bundle = new Bundle();
            bundle.putParcelable(DecodeThread.BARCODE_BITMAP, source.renderCroppedGreyscaleBitmap());
            message.setData(bundle);
            message.sendToTarget();
            return;
        }
        Message.obtain(this.fragment.getHandler(), C1038R.id.decode_failed).sendToTarget();
    }
}
