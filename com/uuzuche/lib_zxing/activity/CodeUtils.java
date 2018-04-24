package com.uuzuche.lib_zxing.activity;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.text.TextUtils;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.itextpdf.text.pdf.BaseField;
import com.uuzuche.lib_zxing.camera.BitmapLuminanceSource;
import com.uuzuche.lib_zxing.camera.CameraManager;
import com.uuzuche.lib_zxing.decoding.DecodeFormatManager;
import java.util.Hashtable;
import java.util.Vector;

public class CodeUtils {
    public static final String LAYOUT_ID = "layout_id";
    public static final int RESULT_FAILED = 2;
    public static final String RESULT_STRING = "result_string";
    public static final int RESULT_SUCCESS = 1;
    public static final String RESULT_TYPE = "result_type";

    public interface AnalyzeCallback {
        void onAnalyzeFailed();

        void onAnalyzeSuccess(Bitmap bitmap, String str);
    }

    public static void analyzeBitmap(String path, AnalyzeCallback analyzeCallback) {
        Options options = new Options();
        options.inJustDecodeBounds = true;
        Bitmap mBitmap = BitmapFactory.decodeFile(path, options);
        options.inJustDecodeBounds = false;
        int sampleSize = (int) (((float) options.outHeight) / 400.0f);
        if (sampleSize <= 0) {
            sampleSize = 1;
        }
        options.inSampleSize = sampleSize;
        mBitmap = BitmapFactory.decodeFile(path, options);
        MultiFormatReader multiFormatReader = new MultiFormatReader();
        Hashtable<DecodeHintType, Object> hints = new Hashtable(2);
        Vector<BarcodeFormat> decodeFormats = new Vector();
        if (decodeFormats == null || decodeFormats.isEmpty()) {
            decodeFormats = new Vector();
            decodeFormats.addAll(DecodeFormatManager.ONE_D_FORMATS);
            decodeFormats.addAll(DecodeFormatManager.QR_CODE_FORMATS);
            decodeFormats.addAll(DecodeFormatManager.DATA_MATRIX_FORMATS);
        }
        hints.put(DecodeHintType.POSSIBLE_FORMATS, decodeFormats);
        multiFormatReader.setHints(hints);
        Result rawResult = null;
        try {
            rawResult = multiFormatReader.decodeWithState(new BinaryBitmap(new HybridBinarizer(new BitmapLuminanceSource(mBitmap))));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (rawResult != null) {
            if (analyzeCallback != null) {
                analyzeCallback.onAnalyzeSuccess(mBitmap, rawResult.getText());
            }
        } else if (analyzeCallback != null) {
            analyzeCallback.onAnalyzeFailed();
        }
    }

    public static Bitmap createImage(String text, int w, int h, Bitmap logo) {
        if (TextUtils.isEmpty(text)) {
            return null;
        }
        try {
            Bitmap scaleLogo = getScaleLogo(logo, w, h);
            int offsetX = w / 2;
            int offsetY = h / 2;
            int scaleWidth = 0;
            int scaleHeight = 0;
            if (scaleLogo != null) {
                scaleWidth = scaleLogo.getWidth();
                scaleHeight = scaleLogo.getHeight();
                offsetX = (w - scaleWidth) / 2;
                offsetY = (h - scaleHeight) / 2;
            }
            Hashtable<EncodeHintType, Object> hints = new Hashtable();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            hints.put(EncodeHintType.MARGIN, Integer.valueOf(0));
            BitMatrix bitMatrix = new QRCodeWriter().encode(text, BarcodeFormat.QR_CODE, w, h, hints);
            int[] pixels = new int[(w * h)];
            int y = 0;
            while (y < h) {
                int x = 0;
                while (x < w) {
                    if (x >= offsetX && x < offsetX + scaleWidth && y >= offsetY && y < offsetY + scaleHeight) {
                        int pixel = scaleLogo.getPixel(x - offsetX, y - offsetY);
                        if (pixel == 0) {
                            if (bitMatrix.get(x, y)) {
                                pixel = ViewCompat.MEASURED_STATE_MASK;
                            } else {
                                pixel = -1;
                            }
                        }
                        pixels[(y * w) + x] = pixel;
                    } else if (bitMatrix.get(x, y)) {
                        pixels[(y * w) + x] = ViewCompat.MEASURED_STATE_MASK;
                    } else {
                        pixels[(y * w) + x] = -1;
                    }
                    x++;
                }
                y++;
            }
            Bitmap bitmap = Bitmap.createBitmap(w, h, Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, w, 0, 0, w, h);
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static Bitmap getScaleLogo(Bitmap logo, int w, int h) {
        if (logo == null) {
            return null;
        }
        Matrix matrix = new Matrix();
        float scaleFactor = Math.min(((((float) w) * BaseField.BORDER_WIDTH_THIN) / 5.0f) / ((float) logo.getWidth()), ((((float) h) * BaseField.BORDER_WIDTH_THIN) / 5.0f) / ((float) logo.getHeight()));
        matrix.postScale(scaleFactor, scaleFactor);
        return Bitmap.createBitmap(logo, 0, 0, logo.getWidth(), logo.getHeight(), matrix, true);
    }

    public static void setFragmentArgs(CaptureFragment captureFragment, int layoutId) {
        if (captureFragment != null && layoutId != -1) {
            Bundle bundle = new Bundle();
            bundle.putInt(LAYOUT_ID, layoutId);
            captureFragment.setArguments(bundle);
        }
    }

    public static void isLightEnable(boolean isEnable) {
        Camera camera;
        if (isEnable) {
            camera = CameraManager.get().getCamera();
            if (camera != null) {
                Parameters parameter = camera.getParameters();
                parameter.setFlashMode("torch");
                camera.setParameters(parameter);
                return;
            }
            return;
        }
        camera = CameraManager.get().getCamera();
        if (camera != null) {
            parameter = camera.getParameters();
            parameter.setFlashMode("off");
            camera.setParameters(parameter);
        }
    }
}
