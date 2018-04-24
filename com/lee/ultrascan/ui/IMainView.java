package com.lee.ultrascan.ui;

import android.graphics.Bitmap;
import android.graphics.PointF;

interface IMainView {
    Bitmap captureCurrentFrame();

    void dismissNoProbeFoundDialog();

    void dismissSavingVideoDialog();

    void dismissWaitingDetectFatDialog();

    void goToScanQrcode();

    Bitmap loadTestBitmap();

    void openSettingsView();

    void setEnableButtonsByScanningState(boolean z);

    void setEnableStatePanel(boolean z);

    void setReplayMax(int i);

    void setScanningState(boolean z);

    void showBackfatInfo(double d);

    void showConfirmSaveVideoDialog();

    void showConnected(boolean z);

    void showFatGuideLine(PointF pointF, PointF pointF2);

    void showFrame(Bitmap bitmap);

    void showFreeze(boolean z);

    void showID(String str);

    void showLoadedScreenShot(Bitmap bitmap);

    void showNeedLocationService();

    void showNoProbeFoundDialog(int i, int i2);

    void showOpenFileDialog();

    void showPregDays(int i);

    void showPregMeasureView(boolean z);

    void showPtBfMode(int i);

    void showSavingVideoDialog(int i);

    void showToast(int i);

    void showUserSettingsDialog();

    void showWaitingDetectFatDialog();

    void showWifiListDialog(String[] strArr);

    void updateBattery(int i, boolean z);

    void updateColor(int i);

    void updateDepth(float f, float f2);

    void updateFreq(float f);

    void updateReplayProgress(int i);

    void updateReplayState(boolean z);

    void updateSavingVideoDialogProgress(int i);

    void updateTgcDisplay(short s);
}
