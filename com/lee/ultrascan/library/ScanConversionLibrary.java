package com.lee.ultrascan.library;

import com.lee.ultrascan.model.RFFrame;

public class ScanConversionLibrary {
    protected static native void fillRFCoordinateTables(ScanConversionParams scanConversionParams);

    public static native String getVersionName();

    protected static native void nativeScanConversion(byte[] bArr, ScanConversionParams scanConversionParams, long j);

    static {
        System.loadLibrary("ScanConversionLibrary");
    }

    public static ScanConversionParams initializeRFCoordinateTable(ScanConversionParams scanConversionParams) {
        fillRFCoordinateTables(scanConversionParams);
        return scanConversionParams;
    }

    public static GrayFrame rfFrameToGrayFrame(RFFrame frame, ScanConversionParams scanConversionParams) {
        if (scanConversionParams.isRFCoordinateTableInitialized()) {
            GrayFrame grayFrame = new GrayFrame(scanConversionParams.getImageWidth(), scanConversionParams.getImageHeight());
            nativeScanConversion(frame.getEnvelopData(), scanConversionParams, grayFrame.getNativeHandler());
            return grayFrame;
        }
        throw new IllegalArgumentException("ScanConversionParams must call fillRFCoordinate before use.");
    }
}
