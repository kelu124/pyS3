package org.opencv.android;

import android.content.Context;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.Size;
import android.os.Build.VERSION;
import android.util.AttributeSet;
import android.util.Log;
import java.util.List;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.CameraBridgeViewBase.ListItemAccessor;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

public class JavaCameraView extends CameraBridgeViewBase implements PreviewCallback {
    private static final int MAGIC_TEXTURE_ID = 10;
    private static final String TAG = "JavaCameraView";
    private byte[] mBuffer;
    protected Camera mCamera;
    protected JavaCameraFrame[] mCameraFrame;
    private int mChainIdx = 0;
    private Mat[] mFrameChain;
    private boolean mStopThread;
    private SurfaceTexture mSurfaceTexture;
    private Thread mThread;

    private class CameraWorker implements Runnable {
        private CameraWorker() {
        }

        public void run() {
            do {
                synchronized (JavaCameraView.this) {
                    try {
                        JavaCameraView.this.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if (!JavaCameraView.this.mStopThread) {
                    if (!JavaCameraView.this.mFrameChain[JavaCameraView.this.mChainIdx].empty()) {
                        JavaCameraView.this.deliverAndDrawFrame(JavaCameraView.this.mCameraFrame[JavaCameraView.this.mChainIdx]);
                    }
                    JavaCameraView.this.mChainIdx = 1 - JavaCameraView.this.mChainIdx;
                }
            } while (!JavaCameraView.this.mStopThread);
            Log.d(JavaCameraView.TAG, "Finish processing thread");
        }
    }

    private class JavaCameraFrame implements CvCameraViewFrame {
        private int mHeight;
        private Mat mRgba = new Mat();
        private int mWidth;
        private Mat mYuvFrameData;

        public Mat gray() {
            return this.mYuvFrameData.submat(0, this.mHeight, 0, this.mWidth);
        }

        public Mat rgba() {
            Imgproc.cvtColor(this.mYuvFrameData, this.mRgba, 91, 4);
            return this.mRgba;
        }

        public JavaCameraFrame(Mat Yuv420sp, int width, int height) {
            this.mWidth = width;
            this.mHeight = height;
            this.mYuvFrameData = Yuv420sp;
        }

        public void release() {
            this.mRgba.release();
        }
    }

    public static class JavaCameraSizeAccessor implements ListItemAccessor {
        public int getWidth(Object obj) {
            return ((Size) obj).width;
        }

        public int getHeight(Object obj) {
            return ((Size) obj).height;
        }
    }

    public JavaCameraView(Context context, int cameraId) {
        super(context, cameraId);
    }

    public JavaCameraView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    protected boolean initializeCamera(int width, int height) {
        Log.d(TAG, "Initialize java camera");
        boolean result = true;
        synchronized (this) {
            this.mCamera = null;
            int camIdx;
            if (this.mCameraIndex == -1) {
                Log.d(TAG, "Trying to open camera with old open()");
                try {
                    this.mCamera = Camera.open();
                } catch (Exception e) {
                    Log.e(TAG, "Camera is not available (in use or does not exist): " + e.getLocalizedMessage());
                }
                if (this.mCamera == null && VERSION.SDK_INT >= 9) {
                    boolean connected = false;
                    camIdx = 0;
                    while (camIdx < Camera.getNumberOfCameras()) {
                        Log.d(TAG, "Trying to open camera with new open(" + Integer.valueOf(camIdx) + ")");
                        try {
                            this.mCamera = Camera.open(camIdx);
                            connected = true;
                        } catch (RuntimeException e2) {
                            Log.e(TAG, "Camera #" + camIdx + "failed to open: " + e2.getLocalizedMessage());
                        }
                        if (!connected) {
                            camIdx++;
                        }
                    }
                }
            } else if (VERSION.SDK_INT >= 9) {
                int localCameraIndex = this.mCameraIndex;
                CameraInfo cameraInfo;
                if (this.mCameraIndex == 99) {
                    Log.i(TAG, "Trying to open back camera");
                    cameraInfo = new CameraInfo();
                    for (camIdx = 0; camIdx < Camera.getNumberOfCameras(); camIdx++) {
                        Camera.getCameraInfo(camIdx, cameraInfo);
                        if (cameraInfo.facing == 0) {
                            localCameraIndex = camIdx;
                            break;
                        }
                    }
                } else if (this.mCameraIndex == 98) {
                    Log.i(TAG, "Trying to open front camera");
                    cameraInfo = new CameraInfo();
                    for (camIdx = 0; camIdx < Camera.getNumberOfCameras(); camIdx++) {
                        Camera.getCameraInfo(camIdx, cameraInfo);
                        if (cameraInfo.facing == 1) {
                            localCameraIndex = camIdx;
                            break;
                        }
                    }
                }
                if (localCameraIndex == 99) {
                    Log.e(TAG, "Back camera not found!");
                } else if (localCameraIndex == 98) {
                    Log.e(TAG, "Front camera not found!");
                } else {
                    Log.d(TAG, "Trying to open camera with new open(" + Integer.valueOf(localCameraIndex) + ")");
                    try {
                        this.mCamera = Camera.open(localCameraIndex);
                    } catch (RuntimeException e22) {
                        Log.e(TAG, "Camera #" + localCameraIndex + "failed to open: " + e22.getLocalizedMessage());
                    }
                }
            }
            if (this.mCamera == null) {
                return false;
            }
            try {
                Parameters params = this.mCamera.getParameters();
                Log.d(TAG, "getSupportedPreviewSizes()");
                List<Size> sizes = params.getSupportedPreviewSizes();
                if (sizes != null) {
                    org.opencv.core.Size frameSize = calculateCameraFrameSize(sizes, new JavaCameraSizeAccessor(), width, height);
                    params.setPreviewFormat(17);
                    Log.d(TAG, "Set preview size to " + Integer.valueOf((int) frameSize.width) + "x" + Integer.valueOf((int) frameSize.height));
                    params.setPreviewSize((int) frameSize.width, (int) frameSize.height);
                    if (VERSION.SDK_INT >= 14) {
                        params.setRecordingHint(true);
                    }
                    List<String> FocusModes = params.getSupportedFocusModes();
                    if (FocusModes != null && FocusModes.contains("continuous-video")) {
                        params.setFocusMode("continuous-video");
                    }
                    this.mCamera.setParameters(params);
                    params = this.mCamera.getParameters();
                    this.mFrameWidth = params.getPreviewSize().width;
                    this.mFrameHeight = params.getPreviewSize().height;
                    if (getLayoutParams().width == -1 && getLayoutParams().height == -1) {
                        this.mScale = Math.min(((float) height) / ((float) this.mFrameHeight), ((float) width) / ((float) this.mFrameWidth));
                    } else {
                        this.mScale = 0.0f;
                    }
                    if (this.mFpsMeter != null) {
                        this.mFpsMeter.setResolution(this.mFrameWidth, this.mFrameHeight);
                    }
                    this.mBuffer = new byte[((ImageFormat.getBitsPerPixel(params.getPreviewFormat()) * (this.mFrameWidth * this.mFrameHeight)) / 8)];
                    this.mCamera.addCallbackBuffer(this.mBuffer);
                    this.mCamera.setPreviewCallbackWithBuffer(this);
                    this.mFrameChain = new Mat[2];
                    this.mFrameChain[0] = new Mat(this.mFrameHeight + (this.mFrameHeight / 2), this.mFrameWidth, CvType.CV_8UC1);
                    this.mFrameChain[1] = new Mat(this.mFrameHeight + (this.mFrameHeight / 2), this.mFrameWidth, CvType.CV_8UC1);
                    AllocateCache();
                    this.mCameraFrame = new JavaCameraFrame[2];
                    this.mCameraFrame[0] = new JavaCameraFrame(this.mFrameChain[0], this.mFrameWidth, this.mFrameHeight);
                    this.mCameraFrame[1] = new JavaCameraFrame(this.mFrameChain[1], this.mFrameWidth, this.mFrameHeight);
                    if (VERSION.SDK_INT >= 11) {
                        this.mSurfaceTexture = new SurfaceTexture(10);
                        this.mCamera.setPreviewTexture(this.mSurfaceTexture);
                    } else {
                        this.mCamera.setPreviewDisplay(null);
                    }
                    Log.d(TAG, "startPreview");
                    this.mCamera.startPreview();
                } else {
                    result = false;
                }
            } catch (Exception e3) {
                result = false;
                e3.printStackTrace();
            }
            return result;
        }
    }

    protected void releaseCamera() {
        synchronized (this) {
            if (this.mCamera != null) {
                this.mCamera.stopPreview();
                this.mCamera.setPreviewCallback(null);
                this.mCamera.release();
            }
            this.mCamera = null;
            if (this.mFrameChain != null) {
                this.mFrameChain[0].release();
                this.mFrameChain[1].release();
            }
            if (this.mCameraFrame != null) {
                this.mCameraFrame[0].release();
                this.mCameraFrame[1].release();
            }
        }
    }

    protected boolean connectCamera(int width, int height) {
        Log.d(TAG, "Connecting to camera");
        if (!initializeCamera(width, height)) {
            return false;
        }
        Log.d(TAG, "Starting processing thread");
        this.mStopThread = false;
        this.mThread = new Thread(new CameraWorker());
        this.mThread.start();
        return true;
    }

    protected void disconnectCamera() {
        Log.d(TAG, "Disconnecting from camera");
        try {
            this.mStopThread = true;
            Log.d(TAG, "Notify thread");
            synchronized (this) {
                notify();
            }
            Log.d(TAG, "Wating for thread");
            if (this.mThread != null) {
                this.mThread.join();
            }
            this.mThread = null;
        } catch (InterruptedException e) {
            try {
                e.printStackTrace();
            } finally {
                this.mThread = null;
            }
        }
        releaseCamera();
    }

    public void onPreviewFrame(byte[] frame, Camera arg1) {
        Log.d(TAG, "Preview Frame received. Frame size: " + frame.length);
        synchronized (this) {
            this.mFrameChain[1 - this.mChainIdx].put(0, 0, frame);
            notify();
        }
        if (this.mCamera != null) {
            this.mCamera.addCallbackBuffer(this.mBuffer);
        }
    }
}
