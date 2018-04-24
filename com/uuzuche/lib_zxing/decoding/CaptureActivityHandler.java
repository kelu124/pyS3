package com.uuzuche.lib_zxing.decoding;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.uuzuche.lib_zxing.C1038R;
import com.uuzuche.lib_zxing.activity.CaptureFragment;
import com.uuzuche.lib_zxing.camera.CameraManager;
import com.uuzuche.lib_zxing.view.ViewfinderResultPointCallback;
import com.uuzuche.lib_zxing.view.ViewfinderView;
import java.util.Vector;

public final class CaptureActivityHandler extends Handler {
    private static final String TAG = CaptureActivityHandler.class.getSimpleName();
    private final DecodeThread decodeThread;
    private final CaptureFragment fragment;
    private State state = State.SUCCESS;

    private enum State {
        PREVIEW,
        SUCCESS,
        DONE
    }

    public CaptureActivityHandler(CaptureFragment fragment, Vector<BarcodeFormat> decodeFormats, String characterSet, ViewfinderView viewfinderView) {
        this.fragment = fragment;
        this.decodeThread = new DecodeThread(fragment, decodeFormats, characterSet, new ViewfinderResultPointCallback(viewfinderView));
        this.decodeThread.start();
        CameraManager.get().startPreview();
        restartPreviewAndDecode();
    }

    public void handleMessage(Message message) {
        if (message.what == C1038R.id.auto_focus) {
            if (this.state == State.PREVIEW) {
                CameraManager.get().requestAutoFocus(this, C1038R.id.auto_focus);
            }
        } else if (message.what == C1038R.id.restart_preview) {
            Log.d(TAG, "Got restart preview message");
            restartPreviewAndDecode();
        } else if (message.what == C1038R.id.decode_succeeded) {
            Bitmap barcode;
            Log.d(TAG, "Got decode succeeded message");
            this.state = State.SUCCESS;
            Bundle bundle = message.getData();
            if (bundle == null) {
                barcode = null;
            } else {
                barcode = (Bitmap) bundle.getParcelable(DecodeThread.BARCODE_BITMAP);
            }
            this.fragment.handleDecode((Result) message.obj, barcode);
        } else if (message.what == C1038R.id.decode_failed) {
            this.state = State.PREVIEW;
            CameraManager.get().requestPreviewFrame(this.decodeThread.getHandler(), C1038R.id.decode);
        } else if (message.what == C1038R.id.return_scan_result) {
            Log.d(TAG, "Got return scan result message");
            this.fragment.getActivity().setResult(-1, (Intent) message.obj);
            this.fragment.getActivity().finish();
        } else if (message.what == C1038R.id.launch_product_query) {
            Log.d(TAG, "Got product query message");
            Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(message.obj));
            intent.addFlags(524288);
            this.fragment.getActivity().startActivity(intent);
        }
    }

    public void quitSynchronously() {
        this.state = State.DONE;
        CameraManager.get().stopPreview();
        Message.obtain(this.decodeThread.getHandler(), C1038R.id.quit).sendToTarget();
        try {
            this.decodeThread.join();
        } catch (InterruptedException e) {
        }
        removeMessages(C1038R.id.decode_succeeded);
        removeMessages(C1038R.id.decode_failed);
    }

    private void restartPreviewAndDecode() {
        if (this.state == State.SUCCESS) {
            this.state = State.PREVIEW;
            CameraManager.get().requestPreviewFrame(this.decodeThread.getHandler(), C1038R.id.decode);
            CameraManager.get().requestAutoFocus(this, C1038R.id.auto_focus);
            this.fragment.drawViewfinder();
        }
    }
}
