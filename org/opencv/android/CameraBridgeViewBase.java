package org.opencv.android;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.PorterDuff.Mode;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import com.itextpdf.text.pdf.BaseField;
import java.util.List;
import org.opencv.C1269R;
import org.opencv.core.Mat;
import org.opencv.core.Size;

public abstract class CameraBridgeViewBase extends SurfaceView implements Callback {
    public static final int CAMERA_ID_ANY = -1;
    public static final int CAMERA_ID_BACK = 99;
    public static final int CAMERA_ID_FRONT = 98;
    private static final int MAX_UNSPECIFIED = -1;
    private static final int STARTED = 1;
    private static final int STOPPED = 0;
    private static final String TAG = "CameraBridge";
    private Bitmap mCacheBitmap;
    protected int mCameraIndex = -1;
    protected boolean mEnabled;
    protected FpsMeter mFpsMeter = null;
    protected int mFrameHeight;
    protected int mFrameWidth;
    private CvCameraViewListener2 mListener;
    protected int mMaxHeight;
    protected int mMaxWidth;
    protected int mPreviewFormat = 4;
    protected float mScale = 0.0f;
    private int mState = 0;
    private boolean mSurfaceExist;
    private Object mSyncObject = new Object();

    class C12821 implements OnClickListener {
        C12821() {
        }

        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
            ((Activity) CameraBridgeViewBase.this.getContext()).finish();
        }
    }

    public interface CvCameraViewFrame {
        Mat gray();

        Mat rgba();
    }

    public interface CvCameraViewListener2 {
        Mat onCameraFrame(CvCameraViewFrame cvCameraViewFrame);

        void onCameraViewStarted(int i, int i2);

        void onCameraViewStopped();
    }

    public interface CvCameraViewListener {
        Mat onCameraFrame(Mat mat);

        void onCameraViewStarted(int i, int i2);

        void onCameraViewStopped();
    }

    protected class CvCameraViewListenerAdapter implements CvCameraViewListener2 {
        private CvCameraViewListener mOldStyleListener;
        private int mPreviewFormat = 4;

        public CvCameraViewListenerAdapter(CvCameraViewListener oldStypeListener) {
            this.mOldStyleListener = oldStypeListener;
        }

        public void onCameraViewStarted(int width, int height) {
            this.mOldStyleListener.onCameraViewStarted(width, height);
        }

        public void onCameraViewStopped() {
            this.mOldStyleListener.onCameraViewStopped();
        }

        public Mat onCameraFrame(CvCameraViewFrame inputFrame) {
            switch (this.mPreviewFormat) {
                case 1:
                    return this.mOldStyleListener.onCameraFrame(inputFrame.gray());
                case 4:
                    return this.mOldStyleListener.onCameraFrame(inputFrame.rgba());
                default:
                    Log.e(CameraBridgeViewBase.TAG, "Invalid frame format! Only RGBA and Gray Scale are supported!");
                    return null;
            }
        }

        public void setFrameFormat(int format) {
            this.mPreviewFormat = format;
        }
    }

    public interface ListItemAccessor {
        int getHeight(Object obj);

        int getWidth(Object obj);
    }

    protected abstract boolean connectCamera(int i, int i2);

    protected abstract void disconnectCamera();

    public CameraBridgeViewBase(Context context, int cameraId) {
        super(context);
        this.mCameraIndex = cameraId;
        getHolder().addCallback(this);
        this.mMaxWidth = -1;
        this.mMaxHeight = -1;
    }

    public CameraBridgeViewBase(Context context, AttributeSet attrs) {
        super(context, attrs);
        Log.d(TAG, "Attr count: " + Integer.valueOf(attrs.getAttributeCount()));
        TypedArray styledAttrs = getContext().obtainStyledAttributes(attrs, C1269R.styleable.CameraBridgeViewBase);
        if (styledAttrs.getBoolean(0, false)) {
            enableFpsMeter();
        }
        this.mCameraIndex = styledAttrs.getInt(1, -1);
        getHolder().addCallback(this);
        this.mMaxWidth = -1;
        this.mMaxHeight = -1;
        styledAttrs.recycle();
    }

    public void setCameraIndex(int cameraIndex) {
        this.mCameraIndex = cameraIndex;
    }

    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
        Log.d(TAG, "call surfaceChanged event");
        synchronized (this.mSyncObject) {
            if (this.mSurfaceExist) {
                this.mSurfaceExist = false;
                checkCurrentState();
                this.mSurfaceExist = true;
                checkCurrentState();
            } else {
                this.mSurfaceExist = true;
                checkCurrentState();
            }
        }
    }

    public void surfaceCreated(SurfaceHolder holder) {
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        synchronized (this.mSyncObject) {
            this.mSurfaceExist = false;
            checkCurrentState();
        }
    }

    public void enableView() {
        synchronized (this.mSyncObject) {
            this.mEnabled = true;
            checkCurrentState();
        }
    }

    public void disableView() {
        synchronized (this.mSyncObject) {
            this.mEnabled = false;
            checkCurrentState();
        }
    }

    public void enableFpsMeter() {
        if (this.mFpsMeter == null) {
            this.mFpsMeter = new FpsMeter();
            this.mFpsMeter.setResolution(this.mFrameWidth, this.mFrameHeight);
        }
    }

    public void disableFpsMeter() {
        this.mFpsMeter = null;
    }

    public void setCvCameraViewListener(CvCameraViewListener2 listener) {
        this.mListener = listener;
    }

    public void setCvCameraViewListener(CvCameraViewListener listener) {
        CvCameraViewListenerAdapter adapter = new CvCameraViewListenerAdapter(listener);
        adapter.setFrameFormat(this.mPreviewFormat);
        this.mListener = adapter;
    }

    public void setMaxFrameSize(int maxWidth, int maxHeight) {
        this.mMaxWidth = maxWidth;
        this.mMaxHeight = maxHeight;
    }

    public void SetCaptureFormat(int format) {
        this.mPreviewFormat = format;
        if (this.mListener instanceof CvCameraViewListenerAdapter) {
            this.mListener.setFrameFormat(this.mPreviewFormat);
        }
    }

    private void checkCurrentState() {
        int targetState;
        if (this.mEnabled && this.mSurfaceExist && getVisibility() == 0) {
            targetState = 1;
        } else {
            targetState = 0;
        }
        if (targetState != this.mState) {
            processExitState(this.mState);
            this.mState = targetState;
            processEnterState(this.mState);
        }
    }

    private void processEnterState(int state) {
        switch (state) {
            case 0:
                onEnterStoppedState();
                if (this.mListener != null) {
                    this.mListener.onCameraViewStopped();
                    return;
                }
                return;
            case 1:
                onEnterStartedState();
                if (this.mListener != null) {
                    this.mListener.onCameraViewStarted(this.mFrameWidth, this.mFrameHeight);
                    return;
                }
                return;
            default:
                return;
        }
    }

    private void processExitState(int state) {
        switch (state) {
            case 0:
                onExitStoppedState();
                return;
            case 1:
                onExitStartedState();
                return;
            default:
                return;
        }
    }

    private void onEnterStoppedState() {
    }

    private void onExitStoppedState() {
    }

    private void onEnterStartedState() {
        if (!connectCamera(getWidth(), getHeight())) {
            AlertDialog ad = new Builder(getContext()).create();
            ad.setCancelable(false);
            ad.setMessage("It seems that you device does not support camera (or it is locked). Application will be closed.");
            ad.setButton(-3, "OK", new C12821());
            ad.show();
        }
    }

    private void onExitStartedState() {
        disconnectCamera();
        if (this.mCacheBitmap != null) {
            this.mCacheBitmap.recycle();
        }
    }

    protected void deliverAndDrawFrame(CvCameraViewFrame frame) {
        Mat modified;
        if (this.mListener != null) {
            modified = this.mListener.onCameraFrame(frame);
        } else {
            modified = frame.rgba();
        }
        boolean bmpValid = true;
        if (modified != null) {
            try {
                Utils.matToBitmap(modified, this.mCacheBitmap);
            } catch (Exception e) {
                Log.e(TAG, "Mat type: " + modified);
                Log.e(TAG, "Bitmap type: " + this.mCacheBitmap.getWidth() + "*" + this.mCacheBitmap.getHeight());
                Log.e(TAG, "Utils.matToBitmap() throws an exception: " + e.getMessage());
                bmpValid = false;
            }
        }
        if (bmpValid && this.mCacheBitmap != null) {
            Canvas canvas = getHolder().lockCanvas();
            if (canvas != null) {
                canvas.drawColor(0, Mode.CLEAR);
                Log.d(TAG, "mStretch value: " + this.mScale);
                if (this.mScale != 0.0f) {
                    canvas.drawBitmap(this.mCacheBitmap, new Rect(0, 0, this.mCacheBitmap.getWidth(), this.mCacheBitmap.getHeight()), new Rect((int) ((((float) canvas.getWidth()) - (this.mScale * ((float) this.mCacheBitmap.getWidth()))) / BaseField.BORDER_WIDTH_MEDIUM), (int) ((((float) canvas.getHeight()) - (this.mScale * ((float) this.mCacheBitmap.getHeight()))) / BaseField.BORDER_WIDTH_MEDIUM), (int) (((((float) canvas.getWidth()) - (this.mScale * ((float) this.mCacheBitmap.getWidth()))) / BaseField.BORDER_WIDTH_MEDIUM) + (this.mScale * ((float) this.mCacheBitmap.getWidth()))), (int) (((((float) canvas.getHeight()) - (this.mScale * ((float) this.mCacheBitmap.getHeight()))) / BaseField.BORDER_WIDTH_MEDIUM) + (this.mScale * ((float) this.mCacheBitmap.getHeight())))), null);
                } else {
                    canvas.drawBitmap(this.mCacheBitmap, new Rect(0, 0, this.mCacheBitmap.getWidth(), this.mCacheBitmap.getHeight()), new Rect((canvas.getWidth() - this.mCacheBitmap.getWidth()) / 2, (canvas.getHeight() - this.mCacheBitmap.getHeight()) / 2, ((canvas.getWidth() - this.mCacheBitmap.getWidth()) / 2) + this.mCacheBitmap.getWidth(), ((canvas.getHeight() - this.mCacheBitmap.getHeight()) / 2) + this.mCacheBitmap.getHeight()), null);
                }
                if (this.mFpsMeter != null) {
                    this.mFpsMeter.measure();
                    this.mFpsMeter.draw(canvas, 20.0f, 30.0f);
                }
                getHolder().unlockCanvasAndPost(canvas);
            }
        }
    }

    protected void AllocateCache() {
        this.mCacheBitmap = Bitmap.createBitmap(this.mFrameWidth, this.mFrameHeight, Config.ARGB_8888);
    }

    protected Size calculateCameraFrameSize(List<?> supportedSizes, ListItemAccessor accessor, int surfaceWidth, int surfaceHeight) {
        int maxAllowedWidth;
        int calcWidth = 0;
        int calcHeight = 0;
        if (this.mMaxWidth == -1 || this.mMaxWidth >= surfaceWidth) {
            maxAllowedWidth = surfaceWidth;
        } else {
            maxAllowedWidth = this.mMaxWidth;
        }
        int maxAllowedHeight;
        if (this.mMaxHeight == -1 || this.mMaxHeight >= surfaceHeight) {
            maxAllowedHeight = surfaceHeight;
        } else {
            maxAllowedHeight = this.mMaxHeight;
        }
        for (Object size : supportedSizes) {
            int width = accessor.getWidth(size);
            int height = accessor.getHeight(size);
            if (width <= maxAllowedWidth && height <= maxAllowedHeight && width >= calcWidth && height >= calcHeight) {
                calcWidth = width;
                calcHeight = height;
            }
        }
        return new Size((double) calcWidth, (double) calcHeight);
    }
}
