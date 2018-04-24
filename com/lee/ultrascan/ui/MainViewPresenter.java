package com.lee.ultrascan.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.net.wifi.ScanResult;
import android.os.Build.VERSION;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.HandlerThread;
import android.os.Message;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.BaseField;
import com.itextpdf.text.pdf.PdfWriter;
import com.lee.ultrascan.C0796R;
import com.lee.ultrascan.interactor.FrameDataInteractor;
import com.lee.ultrascan.library.DepthType;
import com.lee.ultrascan.library.FatDetectUtil2;
import com.lee.ultrascan.library.FrameProcessUtil;
import com.lee.ultrascan.library.FrequencyType;
import com.lee.ultrascan.library.GrayFrame;
import com.lee.ultrascan.library.ProbeStatus;
import com.lee.ultrascan.library.ScanConversionLibrary;
import com.lee.ultrascan.library.ScanConversionParams;
import com.lee.ultrascan.model.RFFrame;
import com.lee.ultrascan.model.SaveRFHelper;
import com.lee.ultrascan.model.TgcCurve;
import com.lee.ultrascan.service.ProbeService;
import com.lee.ultrascan.service.ProbeService.ProbeStateListener;
import com.lee.ultrascan.service.RxBus;
import com.lee.ultrascan.service.WifiStateBroadcastReceiver.WifiState;
import com.lee.ultrascan.service.net.WifiHelper;
import com.lee.ultrascan.utils.AverageFilterBuffer;
import com.lee.ultrascan.utils.LogUtils;
import com.lee.ultrascan.utils.ReportHelper;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.opencv.core.Point;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;

public class MainViewPresenter implements IMainViewPresenter, ProbeStateListener {
    private static final int BATTERY_FILTER_SIZE = 5;
    private static final float BF_CONTRAST_ENHANCE_FACTOR = 1.2f;
    private static final float BF_CORRELATION_ENHANCE_FACTOR = 0.3f;
    private static final int BF_MODE = 2;
    private static final int EMPTY_VOLTAGE = 350;
    private static final int FRAME_MESSAGE_ID = 1;
    private static final int FULL_VOLTAGE = 400;
    private static final String PROBE_AP_DEFAULT_NAME = "AscannerDAP";
    private static final int PROBE_AP_NAME_LENGTH = 12;
    private static final String PROBE_AP_NAME_PREFIX = "UScan";
    private static final String PROBE_DEFAUL_AP_PWD = "12345678";
    private static final float PT_CONTRAST_ENHANCE_FACTOR = 1.1f;
    private static final float PT_CORRELATION_ENHANCE_FACTOR = 0.2f;
    private static final int PT_MODE = 1;
    private static final String TAG = "MainViewPresenter";
    private static final short TGC_CHANGE_STEP = (short) 40;
    private List<String> apNames = new ArrayList();
    private Context appContext;
    private AverageFilterBuffer batteryFilterBuffer = new AverageFilterBuffer(5);
    private GrayFrame currentDisplayFrame;
    private FrameDataInteractor frameDataInteractor;
    private Handler frameProcessHandler;
    private Callback frameProcessHandlerCallback = new C10293();
    private HandlerThread frameProcessThread = new HandlerThread("FrameEnhanceHandlerThread");
    private Handler frameScanConversionHandler;
    private Callback frameScanConversionHandlerCallback = new C10272();
    private HandlerThread frameScanConversionThread = new HandlerThread("FrameHandlerThread");
    private boolean isConnected = false;
    private volatile boolean isReplaying = false;
    private GrayFrame lastCorrelationEnhancedFrame = null;
    private Handler mainHandler = new Handler();
    private RFFrame prevRF;
    private ProbeService probeService;
    private volatile int ptbfMode = 1;
    private Runnable replayFrameRunnable = new C10304();
    private int replayProgress = 0;
    private Bitmap reusedDisplayFrameBitmap;
    private SaveRFHelper saveRFHelper;
    private IMainView view;
    private WifiHelper wifiHelper;
    private Subscription wifiStateSubscription;

    class C10272 implements Callback {
        C10272() {
        }

        public boolean handleMessage(Message msg) {
            Object[] frame_and_scan_conversion_params = (Object[]) msg.obj;
            RFFrame frame = frame_and_scan_conversion_params[0];
            ScanConversionParams scanConversionParams = frame_and_scan_conversion_params[1];
            long sc_start = System.currentTimeMillis();
            GrayFrame grayFrame = ScanConversionLibrary.rfFrameToGrayFrame(frame, scanConversionParams);
            LogUtils.LOGI("render", "do sc cost:" + (System.currentTimeMillis() - sc_start));
            Message message = MainViewPresenter.this.frameProcessHandler.obtainMessage();
            message.obj = grayFrame;
            message.what = msg.what;
            message.arg1 = msg.arg1;
            MainViewPresenter.this.frameProcessHandler.sendMessage(message);
            return true;
        }
    }

    class C10293 implements Callback {
        C10293() {
        }

        public boolean handleMessage(Message msg) {
            final GrayFrame grayFrame = msg.obj;
            MainViewPresenter.this.updateDisplayBitmapSize(grayFrame.getWidth(), grayFrame.getHeight());
            if (MainViewPresenter.this.frameDataInteractor.frameSequenceSize() > 0) {
                float correlationEnhanceFactor;
                float contrastEnhanceFactor;
                LogUtils.LOGI("enhance", "do frame enhance.");
                long start = System.currentTimeMillis();
                int mode = msg.arg1;
                if (mode == 1) {
                    correlationEnhanceFactor = MainViewPresenter.PT_CORRELATION_ENHANCE_FACTOR;
                    contrastEnhanceFactor = MainViewPresenter.PT_CONTRAST_ENHANCE_FACTOR;
                } else if (mode == 2) {
                    correlationEnhanceFactor = MainViewPresenter.BF_CORRELATION_ENHANCE_FACTOR;
                    contrastEnhanceFactor = MainViewPresenter.BF_CONTRAST_ENHANCE_FACTOR;
                } else {
                    throw new IllegalStateException("unexpected ptbfMode value.");
                }
                FrameProcessUtil.frameCorrelationEnhance(MainViewPresenter.this.lastCorrelationEnhancedFrame, grayFrame, correlationEnhanceFactor);
                MainViewPresenter.this.lastCorrelationEnhancedFrame.release();
                MainViewPresenter.this.lastCorrelationEnhancedFrame = grayFrame.copy();
                FrameProcessUtil.frameContrastEnhance(grayFrame, contrastEnhanceFactor);
                LogUtils.LOGI("enhance", "do frame enhance cost:" + (System.currentTimeMillis() - start));
            } else {
                if (MainViewPresenter.this.lastCorrelationEnhancedFrame != null) {
                    MainViewPresenter.this.lastCorrelationEnhancedFrame.release();
                }
                MainViewPresenter.this.lastCorrelationEnhancedFrame = grayFrame.copy();
            }
            MainViewPresenter.this.frameDataInteractor.addFrame(grayFrame);
            grayFrame.getPixels(MainViewPresenter.this.reusedDisplayFrameBitmap);
            MainViewPresenter.this.mainHandler.post(new Runnable() {
                public void run() {
                    MainViewPresenter.this.currentDisplayFrame = grayFrame;
                    MainViewPresenter.this.view.showFrame(MainViewPresenter.this.reusedDisplayFrameBitmap);
                }
            });
            LogUtils.LOGI("render", "frame rendered.");
            return true;
        }
    }

    class C10304 implements Runnable {
        C10304() {
        }

        public void run() {
            if (MainViewPresenter.this.isReplaying) {
                MainViewPresenter.this.loadFrameAtProgress(MainViewPresenter.this.replayProgress);
                MainViewPresenter.access$1004(MainViewPresenter.this);
                if (MainViewPresenter.this.replayProgress >= MainViewPresenter.this.frameDataInteractor.frameSequenceSize()) {
                    MainViewPresenter.this.replayProgress = 0;
                }
                MainViewPresenter.this.mainHandler.postDelayed(this, 50);
            }
        }
    }

    class C10315 implements Observer<Integer> {
        C10315() {
        }

        public void onCompleted() {
            MainViewPresenter.this.view.showToast(C0796R.string.toast_video_saved);
        }

        public void onError(Throwable e) {
            MainViewPresenter.this.view.showToast(C0796R.string.toast_save_video_failed);
        }

        public void onNext(Integer integer) {
            MainViewPresenter.this.view.updateSavingVideoDialogProgress(integer.intValue());
        }
    }

    class C10326 implements Action0 {
        C10326() {
        }

        public void call() {
            MainViewPresenter.this.view.dismissSavingVideoDialog();
        }
    }

    class C10337 implements Action0 {
        C10337() {
        }

        public void call() {
            MainViewPresenter.this.view.showSavingVideoDialog(MainViewPresenter.this.frameDataInteractor.frameSequenceSize());
        }
    }

    static /* synthetic */ int access$1004(MainViewPresenter x0) {
        int i = x0.replayProgress + 1;
        x0.replayProgress = i;
        return i;
    }

    public MainViewPresenter(final IMainView view, ProbeService probeService, final WifiHelper wifiHelper) {
        this.wifiHelper = wifiHelper;
        this.view = view;
        this.probeService = probeService;
        this.probeService.setProbeStateListener(this);
        this.saveRFHelper = new SaveRFHelper(probeService.getApplicationContext());
        this.frameDataInteractor = new FrameDataInteractor(probeService.getApplicationContext());
        this.appContext = probeService.getApplicationContext();
        initView();
        this.frameScanConversionThread.start();
        this.frameScanConversionHandler = new Handler(this.frameScanConversionThread.getLooper(), this.frameScanConversionHandlerCallback);
        this.frameProcessThread.start();
        this.frameProcessHandler = new Handler(this.frameProcessThread.getLooper(), this.frameProcessHandlerCallback);
        this.wifiStateSubscription = RxBus.observe(WifiState.class).subscribe(new Action1<WifiState>() {
            public void call(WifiState wifiState) {
                LogUtils.LOGI("wifistate", wifiState.name());
                if (wifiState.equals(WifiState.CONNECTED)) {
                    view.dismissNoProbeFoundDialog();
                    LogUtils.LOGI("wifistate", "connected " + wifiHelper.getConnectionInfo().getSSID().replace("\"", ""));
                } else if (!wifiState.equals(WifiState.DISCONNECTED)) {
                    if (wifiState.equals(WifiState.ENABLED)) {
                        MainViewPresenter.this.scanWifi();
                    } else if (!wifiState.equals(WifiState.DISABLED)) {
                        if (wifiState.equals(WifiState.SCAN_FINISHED)) {
                            List<ScanResult> scanResults = wifiHelper.getScanResults();
                            LogUtils.LOGI("wifistate", "scanresults:" + scanResults.size());
                            MainViewPresenter.this.apNames.clear();
                            for (ScanResult scanResult : scanResults) {
                                LogUtils.LOGI("wifistate", "scanresult(ssid):" + scanResult.SSID);
                                if (scanResult.SSID.contains(MainViewPresenter.PROBE_AP_NAME_PREFIX)) {
                                    MainViewPresenter.this.apNames.add(scanResult.SSID);
                                } else if (scanResult.SSID.contains("AscannerDAP")) {
                                    MainViewPresenter.this.apNames.add(scanResult.SSID);
                                }
                            }
                            if (!MainViewPresenter.this.apNames.contains(wifiHelper.getActiveApName())) {
                                if (MainViewPresenter.this.apNames.size() > 1) {
                                    view.showWifiListDialog((String[]) MainViewPresenter.this.apNames.toArray(new String[MainViewPresenter.this.apNames.size()]));
                                } else if (MainViewPresenter.this.apNames.size() < 1) {
                                    view.showNoProbeFoundDialog(C0796R.string.dialog_title_hint, C0796R.string.dialog_mesasge_no_probe_wifi);
                                } else {
                                    LogUtils.LOGI("wifistate", "enableNetwork  ap:" + ((String) MainViewPresenter.this.apNames.get(0)) + "  pwd:" + ((String) MainViewPresenter.this.apNames.get(0)));
                                    MainViewPresenter.this.enableNetwork((String) MainViewPresenter.this.apNames.get(0), (String) MainViewPresenter.this.apNames.get(0));
                                }
                            }
                        } else if (!wifiState.equals(WifiState.WRONG_PWD)) {
                        }
                    }
                }
            }
        });
        updateStateByPtBfMode();
    }

    private boolean isTargetAPActive() {
        String activeApName = this.wifiHelper.getActiveApName();
        return activeApName.startsWith(PROBE_AP_NAME_PREFIX) || activeApName.equals("AscannerDAP");
    }

    private void initView() {
        ProbeStatus probeStatus = this.probeService.getProbeStatus();
        ScanConversionParams scanConversionParams = probeStatus.getScanConversionParams();
        this.view.showFrame(Bitmap.createBitmap(scanConversionParams.getImageWidth(), scanConversionParams.getImageHeight(), Config.ARGB_8888));
        this.view.updateTgcDisplay(probeStatus.getTgc());
        this.view.updateFreq(probeStatus.getFrequencyType().getValue());
        this.view.showPtBfMode(this.ptbfMode);
        updateViewDepth(probeStatus.getDepthType());
    }

    public void onStart() {
        if (this.wifiHelper.isLocationServiceEnabled()) {
            if (!this.wifiHelper.isWifiEnabled()) {
                this.wifiHelper.openWifi();
            } else if (!isTargetAPActive()) {
                scanWifi();
            }
        } else if (VERSION.SDK_INT >= 21) {
            this.view.showNeedLocationService();
        }
    }

    public void onStop() {
        stopScan();
    }

    public void onDestroy() {
        this.wifiStateSubscription.unsubscribe();
        this.frameScanConversionThread.quit();
        this.frameProcessThread.quit();
        this.probeService.setProbeStateListener(null);
        this.view = null;
        if (this.lastCorrelationEnhancedFrame != null) {
            this.lastCorrelationEnhancedFrame.release();
        }
    }

    public void enableNetwork(String apName, String pwd) {
        LogUtils.LOGI(TAG, "enableNetwork  apName:" + apName + "  pwd:" + pwd);
        if (pwd.equals("AscannerDAP")) {
            pwd = "12345678";
            LogUtils.LOGI(TAG, "enableNetwork is default AP, use default pwd:" + pwd);
        }
        this.wifiHelper.enableNetwork(apName, pwd);
    }

    public void setProbeWifi(String newApName, String newPwd) {
        this.probeService.setProbeWifi(newApName, newPwd);
    }

    public void scanWifi() {
        LogUtils.LOGI(TAG, "start wifi scan");
        Preconditions.checkState(this.wifiHelper.startScan());
    }

    public void increaseDepth() {
        if (this.probeService.isScanning()) {
            changeDepth(this.probeService.getProbeStatus().getDepthType().getNext());
        }
    }

    public void decreaseDepth() {
        if (this.probeService.isScanning()) {
            changeDepth(this.probeService.getProbeStatus().getDepthType().getPrev());
        }
    }

    private void changeDepth(DepthType depthType) {
        this.probeService.changeDepth(depthType);
        updateViewDepth(depthType);
    }

    public void setTgc(short tgc) {
        if (this.probeService.isScanning()) {
            short new_tgc;
            if (tgc > (short) 0) {
                new_tgc = tgc;
            }
            new_tgc = tgc < TgcCurve.MAX_TGC_VALUE ? tgc : TgcCurve.MAX_TGC_VALUE;
            this.probeService.changeTgc(new_tgc);
            this.view.updateTgcDisplay(new_tgc);
        }
    }

    public void changeTgc(short tgcDiff) {
        short tgc = (short) (this.probeService.getProbeStatus().getTgc() + tgcDiff);
        if (tgc <= (short) 0) {
            tgc = (short) 0;
        }
        if (tgc >= TgcCurve.MAX_TGC_VALUE) {
            tgc = TgcCurve.MAX_TGC_VALUE;
        }
        this.probeService.changeTgc(tgc);
        this.view.updateTgcDisplay(tgc);
    }

    public short getTgc() {
        return this.probeService.getProbeStatus().getTgc();
    }

    public void toggleFreq() {
        FrequencyType newFreq = this.probeService.getProbeStatus().getFrequencyType().equals(FrequencyType.F_3_5MHz) ? FrequencyType.F_5_0MHz : FrequencyType.F_3_5MHz;
        this.probeService.changeFreq(newFreq);
        this.view.updateFreq(newFreq.getValue());
    }

    public void startScan() {
        if (this.isReplaying) {
            toggleReplay();
        }
        this.currentDisplayFrame = null;
        this.frameDataInteractor.clearFrames();
        this.view.showFreeze(false);
        this.view.setEnableButtonsByScanningState(true);
        this.view.updateReplayProgress(0);
        this.view.setReplayMax(this.frameDataInteractor.frameSequenceSize() - 1);
        this.view.setScanningState(true);
        this.probeService.startScanning();
    }

    public void stopScan() {
        this.probeService.stopScanning();
        this.frameScanConversionHandler.removeMessages(1);
        this.frameProcessHandler.removeMessages(1);
        this.replayProgress = 0;
        this.view.setReplayMax(this.frameDataInteractor.frameSequenceSize() - 1);
        this.view.setEnableButtonsByScanningState(false);
        this.view.setScanningState(false);
        this.view.showFreeze(true);
    }

    public void nextFrame() {
        if (!this.isReplaying && !this.probeService.isScanning()) {
            this.replayProgress++;
            this.replayProgress = this.replayProgress > this.frameDataInteractor.frameSequenceSize() + -1 ? 0 : this.replayProgress;
            loadFrameAtProgress(this.replayProgress);
        }
    }

    public void prevFrame() {
        if (!this.isReplaying && !this.probeService.isScanning()) {
            this.replayProgress--;
            this.replayProgress = this.replayProgress < 0 ? this.frameDataInteractor.frameSequenceSize() - 1 : this.replayProgress;
            loadFrameAtProgress(this.replayProgress);
        }
    }

    private void updateDisplayBitmapSize(int width, int height) {
        if (this.reusedDisplayFrameBitmap == null) {
            this.reusedDisplayFrameBitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        } else if (this.reusedDisplayFrameBitmap.getWidth() != width || this.reusedDisplayFrameBitmap.getHeight() != height) {
            this.reusedDisplayFrameBitmap.recycle();
            this.reusedDisplayFrameBitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        }
    }

    public void loadFrameAtProgress(int progress) {
        this.replayProgress = progress;
        GrayFrame grayFrame = this.frameDataInteractor.getFrame(progress);
        if (grayFrame != null) {
            updateDisplayBitmapSize(grayFrame.getWidth(), grayFrame.getHeight());
            grayFrame.getPixels(this.reusedDisplayFrameBitmap);
            this.currentDisplayFrame = grayFrame;
            this.view.showFrame(this.reusedDisplayFrameBitmap);
            this.view.updateReplayProgress(progress);
        }
    }

    public void loadFrameFromFile(String filename) {
        try {
            Bitmap frameImage = FrameDataInteractor.loadFrameImageFromFile(filename);
            Preconditions.checkArgument(!frameImage.isRecycled());
            this.currentDisplayFrame = null;
            this.view.showLoadedScreenShot(frameImage);
        } catch (IOException e) {
            this.view.showToast(C0796R.string.load_screen_shot_failed);
        }
    }

    public void updateFrameViewSize(int width, int height) {
        this.probeService.setScanConversionImageSize(width, height);
    }

    public void updatePrefParams() {
        this.probeService.sendSettingsParams();
    }

    public void openFileDialog() {
        stopReplay();
        this.view.showOpenFileDialog();
    }

    public void openSettings() {
        stopReplay();
        this.view.showUserSettingsDialog();
    }

    public void onPregMeasure(PointF startPoint, PointF endPoint) {
        this.view.showPregDays((int) (((double) PointF.length(startPoint.x - endPoint.x, startPoint.y - endPoint.y)) * this.probeService.getProbeStatus().getScanConversionParams().getCmPerPixel()));
    }

    private void drawContourOnBitmap(Bitmap bitmap, Point[] contour) {
        int i = 0;
        if (!bitmap.isMutable()) {
            throw new IllegalArgumentException("bitmap must be mutable!");
        } else if (contour != null) {
            Paint contourPaint = new Paint();
            contourPaint.setStrokeWidth(10.0f);
            contourPaint.setColor(-16776961);
            contourPaint.setStyle(Style.FILL_AND_STROKE);
            Path contourPath = new Path();
            contourPath.moveTo((float) contour[0].f203x, (float) contour[0].f204y);
            int length = contour.length;
            while (i < length) {
                Point p = contour[i];
                contourPath.lineTo((float) p.f203x, (float) p.f204y);
                i++;
            }
            contourPath.close();
            new Canvas(bitmap).drawPath(contourPath, contourPaint);
        }
    }

    public void detectFat() {
        if (this.currentDisplayFrame != null && !this.probeService.isScanning()) {
            Rect roi = getFatRoiRect(this.currentDisplayFrame);
            Optional<PointF> fatPosition = FatDetectUtil2.detectFat(this.currentDisplayFrame, roi);
            if (fatPosition.isPresent()) {
                PointF midDeadZonePoint = new PointF(0.5f * ((float) this.currentDisplayFrame.getWidth()), getProbeMidDeadZoneDepthInGrayFrame(this.currentDisplayFrame));
                PointF position = (PointF) fatPosition.get();
                LogUtils.LOGI("fat", "roi left:" + roi.left + "  top:" + roi.top);
                LogUtils.LOGI("fat", "fatPosition   x:" + position.x + "  y:" + position.y);
                LogUtils.LOGI("fat", "frame  w:" + this.currentDisplayFrame.getWidth() + "  h:" + this.currentDisplayFrame.getHeight());
                this.view.showBackfatInfo(frameYToFatDepthInMM(position.y));
                this.view.showFatGuideLine(position, midDeadZonePoint);
                this.view.showToast(C0796R.string.toast_fat_detected);
                return;
            }
            this.view.showToast(C0796R.string.toast_no_fat_detected);
        }
    }

    private void updateStateByPtBfMode() {
        if (this.ptbfMode == 1) {
            setTgc((short) 100);
            changeDepth(DepthType.D_16CM);
        } else if (this.ptbfMode == 2) {
            setTgc((short) 0);
            changeDepth(DepthType.D_10CM);
        } else {
            throw new IllegalStateException("unexpected ptbfMode value.");
        }
    }

    public void togglePtBfMode() {
        int i = 2;
        if (this.probeService.isScanning()) {
            if (this.ptbfMode == 2) {
                i = 1;
            }
            this.ptbfMode = i;
            this.view.showPtBfMode(this.ptbfMode);
            updateStateByPtBfMode();
        }
    }

    public void onScanId() {
        if (!this.probeService.isScanning()) {
            this.view.goToScanQrcode();
        }
    }

    private void scaledRoiToOriginCoordinate(Point p, float scaleX, float scaleY, int offsetX, int offsetY) {
        p.f203x /= (double) scaleX;
        p.f204y /= (double) scaleY;
        p.f203x += (double) offsetX;
        p.f204y += (double) offsetY;
    }

    private double frameYToFatDepthInMM(float y) {
        ScanConversionParams scanConversionParams = this.probeService.getProbeStatus().getScanConversionParams();
        int imageWidth = scanConversionParams.getImageWidth();
        int imageHeight = scanConversionParams.getImageHeight();
        LogUtils.LOGI("fat", "scanConversionParam  image w:" + imageWidth + " h:" + imageHeight);
        return (double) (((10.0f * y) / ((float) imageHeight)) * scanConversionParams.getDepthInCM());
    }

    private Rect getFatRoiRect(GrayFrame frame) {
        ScanConversionParams scanConversionParams = this.probeService.getProbeStatus().getScanConversionParams();
        float imageWidthInCM = (float) scanConversionParams.getImageWidthInCM();
        float pixelsPerCM = ((float) frame.getWidth()) / imageWidthInCM;
        int roiX = (int) ((0.5f * (imageWidthInCM - PdfWriter.SPACE_CHAR_RATIO_DEFAULT)) * pixelsPerCM);
        int roiY = (int) ((BaseField.BORDER_WIDTH_THIN + ((float) scanConversionParams.getProbeImageDeadZoneInCM())) * pixelsPerCM);
        return new Rect(roiX, roiY, roiX + ((int) (PdfWriter.SPACE_CHAR_RATIO_DEFAULT * pixelsPerCM)), roiY + ((int) (5.0f * pixelsPerCM)));
    }

    private float getProbeMidDeadZoneDepthInGrayFrame(GrayFrame frame) {
        ScanConversionParams scanConversionParams = this.probeService.getProbeStatus().getScanConversionParams();
        return ((float) scanConversionParams.getProbeImageDeadZoneInCM()) * (((float) frame.getWidth()) / ((float) scanConversionParams.getImageWidthInCM()));
    }

    private Bitmap cropAndResizeFatRoi(Bitmap bitmap, Rect roiRect, int scaledWidth, int scaledHeight) {
        return Bitmap.createScaledBitmap(Bitmap.createBitmap(bitmap, roiRect.left, roiRect.top, roiRect.width(), roiRect.height()), scaledWidth, scaledHeight, true);
    }

    public void onBattery(short batteryData) {
        LogUtils.LOGI("battery", "batteryData:" + batteryData);
        int batteryPercentage = batteryDataToPercentage(batteryData);
        LogUtils.LOGI("battery", "batteryPercentage:" + batteryPercentage);
        this.batteryFilterBuffer.append((float) batteryPercentage);
        this.view.updateBattery((int) this.batteryFilterBuffer.getFilteredValue(), false);
    }

    private int batteryDataToPercentage(short batteryData) {
        return (int) (((((((float) batteryData) * 0.01618f) * 100.0f) - 350.0f) / 50.0f) * 100.0f);
    }

    public void onConnected() {
        this.isConnected = true;
        this.view.showConnected(true);
        this.view.setEnableStatePanel(true);
        if (this.isReplaying) {
            toggleReplay();
        }
        LogUtils.LOGI("CONNECTION", "show connected.");
    }

    public void onDisconnected() {
        this.isConnected = false;
        this.view.showConnected(false);
        this.view.setEnableStatePanel(false);
        this.view.setEnableButtonsByScanningState(false);
        this.probeService.stopScanning();
    }

    public void onProbeButtonClicked() {
        if (this.probeService.isScanning()) {
            stopScan();
        } else {
            startScan();
        }
    }

    public void onUpdateTgc(short tgc) {
        this.view.updateTgcDisplay(tgc);
    }

    public void onUpdateFreq(FrequencyType frequencyType) {
        this.view.updateFreq(frequencyType.getValue());
    }

    private void updateViewDepth(DepthType depthType) {
        this.view.updateDepth((float) this.probeService.getProbeStatus().getScanConversionParams().getProbeImageDeadZoneInCM(), depthType.getValueInCM());
    }

    public void onUpdateDepth(DepthType depthType) {
        updateViewDepth(depthType);
    }

    public void onGetFrame(RFFrame frame, ScanConversionParams scanConversionParams) {
        this.prevRF = frame;
        LogUtils.LOGI("render", "send frame to render.");
        Message message = this.frameScanConversionHandler.obtainMessage();
        message.obj = new Object[]{frame, scanConversionParams};
        message.what = 1;
        message.arg1 = this.ptbfMode;
        this.frameScanConversionHandler.removeMessages(1);
        this.frameScanConversionHandler.sendMessage(message);
    }

    public void onRfid(String rfid) {
        this.view.showID(rfid);
    }

    public void onReset() {
        initView();
    }

    public boolean toggleReplay() {
        this.isReplaying = !this.isReplaying;
        this.view.updateReplayState(this.isReplaying);
        this.mainHandler.post(this.replayFrameRunnable);
        return this.isReplaying;
    }

    public void stopReplay() {
        if (this.isReplaying) {
            toggleReplay();
        }
    }

    public File saveCurrentFrame() {
        stopReplay();
        if (this.frameDataInteractor.frameSequenceSize() < 1) {
            this.view.showToast(C0796R.string.toast_no_frame_to_save);
            return null;
        }
        try {
            return this.frameDataInteractor.saveFrame(this.view.captureCurrentFrame());
        } catch (IOException e) {
            this.view.showToast(C0796R.string.toast_save_frame_failed);
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void saveCurrentFramesToVideoFile() {
        stopReplay();
        if (this.frameDataInteractor.frameSequenceSize() == 0) {
            this.view.showToast(C0796R.string.toast_no_frame_to_save);
        } else {
            this.frameDataInteractor.saveFrameAsVideoFile().observeOn(AndroidSchedulers.mainThread()).doOnSubscribe(new C10337()).doOnUnsubscribe(new C10326()).subscribe(new C10315());
        }
    }

    public boolean savePDFReportAndExportRecord(double backfat, String time, String id, boolean isPregnant, String remarks, File imageFile) {
        stopReplay();
        try {
            File file = ReportHelper.generateReportFile(this.appContext, imageFile, time, id, isPregnant, remarks);
            ReportHelper.exportExcelRecord(this.appContext, time, id, backfat, isPregnant, remarks);
            return file != null;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (DocumentException e2) {
            e2.printStackTrace();
            return false;
        }
    }
}
