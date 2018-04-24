package com.lee.ultrascan.ui;

import android.graphics.PointF;
import java.io.File;

interface IMainViewPresenter {
    void changeTgc(short s);

    void decreaseDepth();

    void detectFat();

    void enableNetwork(String str, String str2);

    short getTgc();

    void increaseDepth();

    void loadFrameAtProgress(int i);

    void loadFrameFromFile(String str);

    void nextFrame();

    void onDestroy();

    void onPregMeasure(PointF pointF, PointF pointF2);

    void onScanId();

    void onStart();

    void onStop();

    void openFileDialog();

    void openSettings();

    void prevFrame();

    File saveCurrentFrame();

    void saveCurrentFramesToVideoFile();

    boolean savePDFReportAndExportRecord(double d, String str, String str2, boolean z, String str3, File file);

    void scanWifi();

    void setProbeWifi(String str, String str2);

    void setTgc(short s);

    void startScan();

    void stopReplay();

    void stopScan();

    void toggleFreq();

    void togglePtBfMode();

    boolean toggleReplay();

    void updateFrameViewSize(int i, int i2);

    void updatePrefParams();
}
