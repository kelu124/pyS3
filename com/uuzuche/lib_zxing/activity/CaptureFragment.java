package com.uuzuche.lib_zxing.activity;

import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.uuzuche.lib_zxing.C1038R;
import com.uuzuche.lib_zxing.activity.CodeUtils.AnalyzeCallback;
import com.uuzuche.lib_zxing.camera.CameraManager;
import com.uuzuche.lib_zxing.decoding.CaptureActivityHandler;
import com.uuzuche.lib_zxing.decoding.InactivityTimer;
import com.uuzuche.lib_zxing.view.ViewfinderView;
import java.io.IOException;
import java.util.Vector;

public class CaptureFragment extends Fragment implements Callback {
    private static final float BEEP_VOLUME = 0.1f;
    private static final long VIBRATE_DURATION = 200;
    private AnalyzeCallback analyzeCallback;
    private final OnCompletionListener beepListener = new C10401();
    private Camera camera;
    private String characterSet;
    private Vector<BarcodeFormat> decodeFormats;
    private CaptureActivityHandler handler;
    private boolean hasSurface;
    private InactivityTimer inactivityTimer;
    private MediaPlayer mediaPlayer;
    private boolean playBeep;
    private SurfaceHolder surfaceHolder;
    private SurfaceView surfaceView;
    private boolean vibrate;
    private ViewfinderView viewfinderView;

    class C10401 implements OnCompletionListener {
        C10401() {
        }

        public void onCompletion(MediaPlayer mediaPlayer) {
            mediaPlayer.seekTo(0);
        }
    }

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CameraManager.init(getActivity().getApplication());
        this.hasSurface = false;
        this.inactivityTimer = new InactivityTimer(getActivity());
    }

    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        View view = null;
        if (bundle != null) {
            int layoutId = bundle.getInt(CodeUtils.LAYOUT_ID);
            if (layoutId != -1) {
                view = inflater.inflate(layoutId, null);
            }
        }
        if (view == null) {
            view = inflater.inflate(C1038R.layout.fragment_capture, null);
        }
        this.viewfinderView = (ViewfinderView) view.findViewById(C1038R.id.viewfinder_view);
        this.surfaceView = (SurfaceView) view.findViewById(C1038R.id.preview_view);
        this.surfaceHolder = this.surfaceView.getHolder();
        return view;
    }

    public void onResume() {
        super.onResume();
        if (this.hasSurface) {
            initCamera(this.surfaceHolder);
        } else {
            this.surfaceHolder.addCallback(this);
            this.surfaceHolder.setType(3);
        }
        this.decodeFormats = null;
        this.characterSet = null;
        this.playBeep = true;
        FragmentActivity activity = getActivity();
        getActivity();
        if (((AudioManager) activity.getSystemService("audio")).getRingerMode() != 2) {
            this.playBeep = false;
        }
        initBeepSound();
        this.vibrate = true;
    }

    public void onPause() {
        super.onPause();
        if (this.handler != null) {
            this.handler.quitSynchronously();
            this.handler = null;
        }
        CameraManager.get().closeDriver();
    }

    public void onDestroy() {
        super.onDestroy();
        this.inactivityTimer.shutdown();
        super.onDestroy();
    }

    public void handleDecode(Result result, Bitmap barcode) {
        this.inactivityTimer.onActivity();
        playBeepSoundAndVibrate();
        if (result == null || TextUtils.isEmpty(result.getText())) {
            if (this.analyzeCallback != null) {
                this.analyzeCallback.onAnalyzeFailed();
            }
        } else if (this.analyzeCallback != null) {
            this.analyzeCallback.onAnalyzeSuccess(barcode, result.getText());
        }
    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        try {
            CameraManager.get().openDriver(surfaceHolder);
            this.camera = CameraManager.get().getCamera();
            if (this.handler == null) {
                this.handler = new CaptureActivityHandler(this, this.decodeFormats, this.characterSet, this.viewfinderView);
            }
        } catch (IOException e) {
        } catch (RuntimeException e2) {
        }
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    public void surfaceCreated(SurfaceHolder holder) {
        if (!this.hasSurface) {
            this.hasSurface = true;
            initCamera(holder);
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        this.hasSurface = false;
        if (this.camera != null && this.camera != null && CameraManager.get().isPreviewing()) {
            if (!CameraManager.get().isUseOneShotPreviewCallback()) {
                this.camera.setPreviewCallback(null);
            }
            this.camera.stopPreview();
            CameraManager.get().getPreviewCallback().setHandler(null, 0);
            CameraManager.get().getAutoFocusCallback().setHandler(null, 0);
            CameraManager.get().setPreviewing(false);
        }
    }

    public Handler getHandler() {
        return this.handler;
    }

    public void drawViewfinder() {
        this.viewfinderView.drawViewfinder();
    }

    private void initBeepSound() {
        if (this.playBeep && this.mediaPlayer == null) {
            getActivity().setVolumeControlStream(3);
            this.mediaPlayer = new MediaPlayer();
            this.mediaPlayer.setAudioStreamType(3);
            this.mediaPlayer.setOnCompletionListener(this.beepListener);
            AssetFileDescriptor file = getResources().openRawResourceFd(C1038R.raw.beep);
            try {
                this.mediaPlayer.setDataSource(file.getFileDescriptor(), file.getStartOffset(), file.getLength());
                file.close();
                this.mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
                this.mediaPlayer.prepare();
            } catch (IOException e) {
                this.mediaPlayer = null;
            }
        }
    }

    private void playBeepSoundAndVibrate() {
        if (this.playBeep && this.mediaPlayer != null) {
            this.mediaPlayer.start();
        }
        if (this.vibrate) {
            FragmentActivity activity = getActivity();
            getActivity();
            ((Vibrator) activity.getSystemService("vibrator")).vibrate(200);
        }
    }

    public AnalyzeCallback getAnalyzeCallback() {
        return this.analyzeCallback;
    }

    public void setAnalyzeCallback(AnalyzeCallback analyzeCallback) {
        this.analyzeCallback = analyzeCallback;
    }
}
