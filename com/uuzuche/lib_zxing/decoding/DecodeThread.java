package com.uuzuche.lib_zxing.decoding;

import android.os.Handler;
import android.os.Looper;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.DecodeHintType;
import com.google.zxing.ResultPointCallback;
import com.uuzuche.lib_zxing.activity.CaptureFragment;
import java.util.Hashtable;
import java.util.Vector;
import java.util.concurrent.CountDownLatch;

final class DecodeThread extends Thread {
    public static final String BARCODE_BITMAP = "barcode_bitmap";
    private final CaptureFragment fragment;
    private Handler handler;
    private final CountDownLatch handlerInitLatch = new CountDownLatch(1);
    private final Hashtable<DecodeHintType, Object> hints = new Hashtable(3);

    DecodeThread(CaptureFragment fragment, Vector<BarcodeFormat> decodeFormats, String characterSet, ResultPointCallback resultPointCallback) {
        this.fragment = fragment;
        if (decodeFormats == null || decodeFormats.isEmpty()) {
            decodeFormats = new Vector();
            decodeFormats.addAll(DecodeFormatManager.ONE_D_FORMATS);
            decodeFormats.addAll(DecodeFormatManager.QR_CODE_FORMATS);
            decodeFormats.addAll(DecodeFormatManager.DATA_MATRIX_FORMATS);
        }
        this.hints.put(DecodeHintType.POSSIBLE_FORMATS, decodeFormats);
        if (characterSet != null) {
            this.hints.put(DecodeHintType.CHARACTER_SET, characterSet);
        }
        this.hints.put(DecodeHintType.NEED_RESULT_POINT_CALLBACK, resultPointCallback);
    }

    Handler getHandler() {
        try {
            this.handlerInitLatch.await();
        } catch (InterruptedException e) {
        }
        return this.handler;
    }

    public void run() {
        Looper.prepare();
        this.handler = new DecodeHandler(this.fragment, this.hints);
        this.handlerInitLatch.countDown();
        Looper.loop();
    }
}
