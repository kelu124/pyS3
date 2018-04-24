package com.uuzuche.lib_zxing.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.uuzuche.lib_zxing.C1038R;
import com.uuzuche.lib_zxing.activity.CodeUtils.AnalyzeCallback;

public class CaptureActivity extends AppCompatActivity {
    AnalyzeCallback analyzeCallback = new C10391();

    class C10391 implements AnalyzeCallback {
        C10391() {
        }

        public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
            Intent resultIntent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putInt(CodeUtils.RESULT_TYPE, 1);
            bundle.putString(CodeUtils.RESULT_STRING, result);
            resultIntent.putExtras(bundle);
            CaptureActivity.this.setResult(-1, resultIntent);
            CaptureActivity.this.finish();
        }

        public void onAnalyzeFailed() {
            Intent resultIntent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putInt(CodeUtils.RESULT_TYPE, 2);
            bundle.putString(CodeUtils.RESULT_STRING, "");
            resultIntent.putExtras(bundle);
            CaptureActivity.this.setResult(-1, resultIntent);
            CaptureActivity.this.finish();
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C1038R.layout.camera);
        CaptureFragment captureFragment = new CaptureFragment();
        captureFragment.setAnalyzeCallback(this.analyzeCallback);
        getSupportFragmentManager().beginTransaction().replace(C1038R.id.fl_zxing_container, captureFragment).commit();
    }
}
