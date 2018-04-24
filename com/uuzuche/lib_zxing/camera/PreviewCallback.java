package com.uuzuche.lib_zxing.camera;

import android.graphics.Point;
import android.hardware.Camera;
import android.os.Handler;
import android.util.Log;

public final class PreviewCallback implements android.hardware.Camera.PreviewCallback {
    private static final String TAG = PreviewCallback.class.getSimpleName();
    private final CameraConfigurationManager configManager;
    private Handler previewHandler;
    private int previewMessage;
    private final boolean useOneShotPreviewCallback;

    PreviewCallback(CameraConfigurationManager configManager, boolean useOneShotPreviewCallback) {
        this.configManager = configManager;
        this.useOneShotPreviewCallback = useOneShotPreviewCallback;
    }

    public void setHandler(Handler previewHandler, int previewMessage) {
        this.previewHandler = previewHandler;
        this.previewMessage = previewMessage;
    }

    public void onPreviewFrame(byte[] data, Camera camera) {
        Point cameraResolution = this.configManager.getCameraResolution();
        if (!this.useOneShotPreviewCallback) {
            camera.setPreviewCallback(null);
        }
        if (this.previewHandler != null) {
            this.previewHandler.obtainMessage(this.previewMessage, cameraResolution.x, cameraResolution.y, data).sendToTarget();
            this.previewHandler = null;
            return;
        }
        Log.d(TAG, "Got preview callback, but no handler for it");
    }
}
