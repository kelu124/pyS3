package com.lee.ultrascan.ui;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.PointF;
import android.graphics.PorterDuff.Mode;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AlertDialog.Builder;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.itextpdf.text.pdf.BaseField;
import com.lee.ultrascan.C0796R;
import com.lee.ultrascan.interactor.FrameDataInteractor;
import com.lee.ultrascan.library.ProbeParams;
import com.lee.ultrascan.model.TgcCurve;
import com.lee.ultrascan.service.ProbeService;
import com.lee.ultrascan.service.ProbeService.ProbeServiceBinder;
import com.lee.ultrascan.service.WifiStateBroadcastReceiver;
import com.lee.ultrascan.service.net.WifiHelper;
import com.lee.ultrascan.ui.ChooseFileDialogFragment.ChooseFileDialogListener;
import com.lee.ultrascan.ui.ColorPickerDialog.OnColorSelectedListener;
import com.lee.ultrascan.ui.view.ColorRampView;
import com.lee.ultrascan.ui.view.FrameImageView;
import com.lee.ultrascan.ui.view.PregMeasureView;
import com.lee.ultrascan.ui.view.PregMeasureView.onDragListener;
import com.lee.ultrascan.ui.view.ScaleplateView;
import com.lee.ultrascan.utils.DialogFactory;
import com.lee.ultrascan.utils.LogUtils;
import com.lee.ultrascan.utils.PreferenceUtils;
import com.lee.ultrascan.utils.ScreenUtils;
import com.lee.ultrascan.utils.ToastUtils;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.vi.swipenumberpicker.SwipeNumberPicker;
import java.io.File;
import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements IMainView, ChooseFileDialogListener {
    public static final String KEY_USE_LEFT_HAND_LAYOUT = "KEY_USE_LEFT_HANDL_LAYOUT";
    private static final int REQUEST_SCAN_ID = 1;
    public static final int SHOW_BF_MODE = 2;
    public static final int SHOW_PT_MODE = 1;
    private static final int SMALL_DIALOG_WIDTH_DP = 400;
    private static final SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.getDefault());
    @Bind({2131689688})
    Button backfatButton;
    @Bind({2131689666})
    TextView backfatTextView;
    @Bind({2131689685})
    ImageView batteryImageView;
    @Bind({2131689686})
    ColorRampView colorRampView;
    private double currentBackfat = 0.0d;
    private String currentId = "";
    @Bind({2131689691})
    Button depthDownButton;
    @Bind({2131689662})
    TextView depthTextView;
    @Bind({2131689690})
    Button depthUpButton;
    @Bind({2131689624})
    FrameImageView frameImageView;
    @Bind({2131689622})
    ViewGroup framePanelLayout;
    @Bind({2131689683})
    TextView frameTextView;
    @Bind({2131689667})
    TextView freezeHintTextView;
    @Bind({2131689660})
    TextView freqTextView;
    private Handler handler = new Handler(Looper.getMainLooper());
    @Bind({2131689658})
    TextView idTextView;
    private boolean isServiceBounded = false;
    @Bind({2131689627})
    ImageView loadedScreenshotImageView;
    private IMainViewPresenter mainViewPresenter;
    @Bind({2131689679})
    Button openFileButton;
    @Bind({2131689619})
    ViewGroup playerPanelLayout;
    private View[] playerPanelViews;
    @Bind({2131689677})
    SeekBar playerSeekBar;
    @Bind({2131689689})
    Button pregButton;
    @Bind({2131689665})
    TextView pregDaysTextView;
    @Bind({2131689626})
    PregMeasureView pregMeasureView;
    @Bind({2131689693})
    Button ptbfButton;
    @Bind({2131689656})
    TextView ptbfStateTextView;
    @Bind({2131689682})
    ImageView replayButton;
    @Bind({2131689625})
    View roiTouchView;
    @Bind({2131689680})
    Button saveImageButton;
    @Bind({2131689681})
    Button saveVideoButton;
    private ProgressDialog savingVideoProgressDialog;
    @Bind({2131689684})
    ScaleplateView scaleplateView;
    @Bind({2131689692})
    ToggleButton scanFreezeToggleButton;
    @Bind({2131689657})
    ImageView scanIdImageView;
    private ServiceConnection serviceConnection = new C10171();
    @Bind({2131689678})
    Button settingButton;
    private Dialog simpleErrorDialog;
    @Bind({2131689620})
    ViewGroup statePanelLayout;
    private View[] statePanelViews;
    @Bind({2131689694})
    SeekBar tgcSeekBar;
    @Bind({2131689664})
    TextView tgcTextView;
    @Bind({2131689655})
    TextView timeTextView;
    private Runnable updateTimeTextView = new Runnable() {
        public void run() {
            MainActivity.this.timeTextView.setText(MainActivity.timeFormat.format(Calendar.getInstance().getTime()));
            MainActivity.this.handler.postDelayed(MainActivity.this.updateTimeTextView, 1000);
        }
    };
    private ProgressDialog waitingDetectFatDialog;
    private Dialog wifiListDialog;
    private WifiStateBroadcastReceiver wifiStateBroadcastReceiver = new WifiStateBroadcastReceiver();

    class C10171 implements ServiceConnection {
        private ProbeService probeService;

        C10171() {
        }

        public void onServiceConnected(ComponentName name, IBinder service) {
            LogUtils.LOGI("probe", "onServiceConnected");
            this.probeService = ((ProbeServiceBinder) service).getService();
            MainActivity.this.mainViewPresenter = new MainViewPresenter(MainActivity.this, this.probeService, new WifiHelper(MainActivity.this.getApplicationContext()));
            MainActivity.this.mainViewPresenter.onStart();
            MainActivity.this.initStatus();
            MainActivity.this.isServiceBounded = true;
        }

        public void onServiceDisconnected(ComponentName name) {
            MainActivity.this.isServiceBounded = false;
            this.probeService = null;
        }

        public ProbeService getProbeService() {
            return this.probeService;
        }
    }

    class C10182 implements OnClickListener {
        C10182() {
        }

        public void onClick(View v) {
            MainActivity.this.mainViewPresenter.openSettings();
        }
    }

    class C10193 implements OnClickListener {
        C10193() {
        }

        public void onClick(View v) {
            MainActivity.this.mainViewPresenter.detectFat();
        }
    }

    class C10204 implements OnClickListener {
        C10204() {
        }

        public void onClick(View v) {
            MainActivity.this.showPregDays(0);
            MainActivity.this.pregDaysTextView.setVisibility(0);
            MainActivity.this.pregMeasureView.setVisibility(0);
        }
    }

    class C10215 implements OnClickListener {
        C10215() {
        }

        public void onClick(View v) {
            MainActivity.this.mainViewPresenter.increaseDepth();
        }
    }

    class C10226 implements OnClickListener {
        C10226() {
        }

        public void onClick(View v) {
            MainActivity.this.mainViewPresenter.decreaseDepth();
        }
    }

    class C10237 implements OnClickListener {
        C10237() {
        }

        public void onClick(View v) {
            if (MainActivity.this.scanFreezeToggleButton.isChecked()) {
                MainActivity.this.mainViewPresenter.startScan();
            } else {
                MainActivity.this.mainViewPresenter.stopScan();
            }
        }
    }

    class C10248 implements OnClickListener {
        C10248() {
        }

        public void onClick(View v) {
            MainActivity.this.mainViewPresenter.togglePtBfMode();
        }
    }

    class C10259 implements OnSeekBarChangeListener {
        C10259() {
        }

        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            MainActivity.this.updateTgcDisplay((short) progress);
        }

        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        public void onStopTrackingTouch(SeekBar seekBar) {
            MainActivity.this.mainViewPresenter.setTgc((short) seekBar.getProgress());
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (PreferenceUtils.getBoolean(this, KEY_USE_LEFT_HAND_LAYOUT, true)) {
            setContentView(C0796R.layout.activity_main);
        } else {
            setContentView(C0796R.layout.activity_main_right_hand);
        }
        ButterKnife.bind(this);
        setTitle(C0796R.string.empty);
        getWindow().setFlags(1024, 1024);
        ScreenUtils.changeWindowBrightness(this, BaseField.BORDER_WIDTH_THIN);
        bindService(new Intent(this, ProbeService.class), this.serviceConnection, 1);
        initViews();
        this.savingVideoProgressDialog = DialogFactory.createProgressDialog((Context) this, (int) C0796R.string.dialog_title_waiting, (int) C0796R.string.dialog_message_saving);
        this.savingVideoProgressDialog.setProgressStyle(1);
        this.waitingDetectFatDialog = DialogFactory.createProgressDialog((Context) this, (int) C0796R.string.dialog_title_waiting, (int) C0796R.string.dialog_message_detecting);
        registerReceiver(this.wifiStateBroadcastReceiver, this.wifiStateBroadcastReceiver.getIntentFilter());
    }

    protected void initStatus() {
        this.mainViewPresenter.setTgc((short) 100);
    }

    protected void initViews() {
        this.frameImageView.setScaleType(ScaleType.FIT_CENTER);
        this.statePanelViews = new View[]{this.scanFreezeToggleButton, this.depthUpButton, this.depthDownButton, this.backfatButton, this.pregButton};
        this.playerPanelViews = new View[]{this.settingButton, this.replayButton, this.saveImageButton, this.saveVideoButton, this.openFileButton, this.playerSeekBar};
        setEnableButtonsByScanningState(false);
        setEnableStatePanel(false);
        this.scaleplateView.setStartValue(0.0f);
        this.scaleplateView.setEndValue(ProbeParams.DEFAULT_DEPTH_TYPE.getValueInCM());
        this.settingButton.setOnClickListener(new C10182());
        this.backfatButton.setOnClickListener(new C10193());
        this.pregButton.setOnClickListener(new C10204());
        this.depthUpButton.setOnClickListener(new C10215());
        this.depthDownButton.setOnClickListener(new C10226());
        this.scanFreezeToggleButton.setOnClickListener(new C10237());
        this.ptbfButton.setOnClickListener(new C10248());
        this.tgcSeekBar.setMax(300);
        this.tgcSeekBar.setOnSeekBarChangeListener(new C10259());
        this.frameImageView.setOnTouchListener(new OnTouchListener() {
            private boolean isChangingDepth = true;
            private float startX = 0.0f;
            private float startY = 0.0f;

            public boolean onTouch(View v, MotionEvent event) {
                float x = event.getX();
                float y = event.getY();
                boolean isScanning = MainActivity.this.scanFreezeToggleButton.isChecked();
                float diffX;
                float diffY;
                switch (event.getAction()) {
                    case 0:
                        this.startX = x;
                        this.startY = y;
                        this.isChangingDepth = isScanning;
                        break;
                    case 1:
                        diffX = x - this.startX;
                        diffY = y - this.startY;
                        if (!this.isChangingDepth) {
                            if (Math.abs(diffX) > 10.0f) {
                                if (diffX <= 0.0f) {
                                    MainActivity.this.mainViewPresenter.prevFrame();
                                    break;
                                }
                                MainActivity.this.mainViewPresenter.nextFrame();
                                break;
                            }
                        } else if (Math.abs(diffY) > 10.0f) {
                            if (diffY <= 0.0f) {
                                MainActivity.this.mainViewPresenter.increaseDepth();
                                break;
                            }
                            MainActivity.this.mainViewPresenter.decreaseDepth();
                            break;
                        }
                        break;
                    case 2:
                        diffX = x - this.startX;
                        diffY = y - this.startY;
                        break;
                }
                return true;
            }
        });
        this.roiTouchView.setOnLongClickListener(new OnLongClickListener() {
            public boolean onLongClick(View v) {
                MainActivity.this.mainViewPresenter.detectFat();
                return true;
            }
        });
        setReplayMax(0);
        this.playerSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    MainActivity.this.mainViewPresenter.loadFrameAtProgress(progress);
                }
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        this.saveImageButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                File imageFile = MainActivity.this.mainViewPresenter.saveCurrentFrame();
                if (imageFile != null) {
                    MainActivity.this.showSaveReportDialog(imageFile);
                }
            }
        });
        this.saveVideoButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                MainActivity.this.showConfirmSaveVideoDialog();
            }
        });
        this.openFileButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                MainActivity.this.mainViewPresenter.openFileDialog();
            }
        });
        this.replayButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                MainActivity.this.mainViewPresenter.toggleReplay();
            }
        });
        this.colorRampView.setOnClickListener(new OnClickListener() {

            class C10161 implements OnColorSelectedListener {
                C10161() {
                }

                public void colorSelected(Integer color) {
                    MainActivity.this.updateColor(color.intValue());
                }
            }

            public void onClick(View v) {
                new ColorPickerDialog(MainActivity.this, MainActivity.this.colorRampView.getStartColor(), new C10161()).show();
            }
        });
        this.pregMeasureView.setOnDragListener(new onDragListener() {
            public void onDragEnd(PointF startPoint, PointF endPoint, boolean isCanceled) {
                if (!isCanceled) {
                    MainActivity.this.mainViewPresenter.onPregMeasure(startPoint, endPoint);
                }
            }
        });
        hidePregDayViewsAndBackFatTextView();
        showID(getString(C0796R.string.empty));
        this.scanIdImageView.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                MainActivity.this.mainViewPresenter.onScanId();
            }
        });
        this.handler.post(this.updateTimeTextView);
    }

    public void onStart() {
        super.onStart();
        if (this.mainViewPresenter != null) {
            this.mainViewPresenter.onStart();
        }
    }

    public void onStop() {
        super.onStop();
        if (this.mainViewPresenter != null) {
            this.mainViewPresenter.onStop();
        }
        this.scanFreezeToggleButton.setChecked(false);
        if (isFinishing()) {
            unregisterReceiver(this.wifiStateBroadcastReceiver);
            if (this.mainViewPresenter != null) {
                this.mainViewPresenter.onDestroy();
            }
            unbindService(this.serviceConnection);
        }
    }

    public void onBackPressed() {
        finish();
    }

    public void onDestroy() {
        super.onDestroy();
        ScreenUtils.fixInputMethodManagerLeak(this);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(C0796R.menu.main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    public void showConnected(boolean isConnected) {
        String toast;
        this.scanFreezeToggleButton.setChecked(false);
        if (isConnected) {
            toast = getString(C0796R.string.toast_connected);
            this.batteryImageView.setVisibility(0);
        } else {
            toast = getString(C0796R.string.toast_disconnected);
            this.batteryImageView.setVisibility(4);
            showFreeze(false);
        }
        ToastUtils.showLong((Context) this, toast);
    }

    public void updateBattery(int percentage, boolean isCharging) {
        if (percentage > 80) {
            this.batteryImageView.setImageResource(isCharging ? C0796R.drawable.ic_battery_charging_full : C0796R.drawable.ic_battery_full);
        } else if (percentage > 60) {
            this.batteryImageView.setImageResource(isCharging ? C0796R.drawable.ic_battery_charging_80 : C0796R.drawable.ic_battery_80);
        } else if (percentage > 40) {
            this.batteryImageView.setImageResource(isCharging ? C0796R.drawable.ic_battery_charging_50 : C0796R.drawable.ic_battery_50);
        } else if (percentage > 20) {
            this.batteryImageView.setImageResource(isCharging ? C0796R.drawable.ic_battery_charging_30 : C0796R.drawable.ic_battery_30);
        } else {
            this.batteryImageView.setImageResource(isCharging ? C0796R.drawable.ic_battery_charging_empty : C0796R.drawable.ic_battery_empty);
        }
    }

    public void updateTgcDisplay(short tgc) {
        if (tgc <= (short) 0) {
            tgc = (short) 0;
        }
        if (tgc >= TgcCurve.MAX_TGC_VALUE) {
            tgc = TgcCurve.MAX_TGC_VALUE;
        }
        LogUtils.LOGI("TGC", "updateTgcDisplay:" + tgc);
        this.tgcSeekBar.setProgress(tgc);
        this.tgcTextView.setText(String.format(getString(C0796R.string.activity_main_tgc_content), new Object[]{Integer.valueOf((tgc / 10) + 90)}));
    }

    public void updateDepth(float frameDeadZoneInCM, float depthInCM) {
        int depthInMMRounded = (int) ((10.0f * depthInCM) + 0.5f);
        this.depthTextView.setText(String.format(getString(C0796R.string.activity_main_depth_cotent), new Object[]{Integer.valueOf(depthInMMRounded)}));
        hidePregDayViewsAndBackFatTextView();
        RectF imageBounds = this.frameImageView.getImageBounds();
        this.scaleplateView.setStartPixelPos(imageBounds.top);
        this.scaleplateView.setEndPixelPos(imageBounds.bottom);
        float endValue = depthInCM + frameDeadZoneInCM;
        this.scaleplateView.setStartValue(frameDeadZoneInCM);
        this.scaleplateView.setEndValue(endValue);
        this.scaleplateView.setTotalValue(endValue);
    }

    public void updateFreq(float freq) {
        this.freqTextView.setText(String.format(getString(C0796R.string.activity_main_frequency_content), new Object[]{Float.valueOf(freq)}));
    }

    public void setEnableStatePanel(boolean isEnable) {
        for (View view : this.statePanelViews) {
            view.setEnabled(isEnable);
        }
        if (isEnable) {
            this.tgcSeekBar.setOnTouchListener(new OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    return false;
                }
            });
        } else {
            this.tgcSeekBar.setOnTouchListener(new OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    return true;
                }
            });
        }
    }

    public void setEnableButtonsByScanningState(boolean isScanning) {
        boolean z;
        this.ptbfButton.setEnabled(isScanning);
        Button button = this.backfatButton;
        if (isScanning) {
            z = false;
        } else {
            z = true;
        }
        button.setEnabled(z);
        button = this.pregButton;
        if (isScanning) {
            z = false;
        } else {
            z = true;
        }
        button.setEnabled(z);
        for (View view : this.playerPanelViews) {
            if (isScanning) {
                z = false;
            } else {
                z = true;
            }
            view.setEnabled(z);
        }
    }

    public void setReplayMax(int replayMax) {
        this.playerSeekBar.setMax(replayMax);
        this.playerSeekBar.setProgress(0);
        this.frameTextView.setText(this.playerSeekBar.getProgress() + "/" + this.playerSeekBar.getMax());
    }

    public void updateReplayProgress(int progress) {
        this.playerSeekBar.setProgress(progress);
        this.frameTextView.setText(this.playerSeekBar.getProgress() + "/" + this.playerSeekBar.getMax());
    }

    public void updateReplayState(boolean isStartReplay) {
        if (isStartReplay) {
            hidePregDayViewsAndBackFatTextView();
            this.replayButton.setImageResource(C0796R.drawable.pause_button);
            return;
        }
        this.replayButton.setImageResource(C0796R.drawable.play_button);
    }

    public void updateColor(int color) {
        this.colorRampView.setStartColor(color);
        this.frameImageView.setColorFilter(color, Mode.MULTIPLY);
    }

    public void showFrame(Bitmap bitmap) {
        if (this.loadedScreenshotImageView.isShown()) {
            this.loadedScreenshotImageView.setVisibility(8);
        }
        this.frameImageView.setImageBitmap(bitmap);
    }

    public void showLoadedScreenShot(Bitmap bitmap) {
        this.loadedScreenshotImageView.setImageBitmap(bitmap);
        this.loadedScreenshotImageView.setVisibility(0);
    }

    public Bitmap captureCurrentFrame() {
        this.framePanelLayout.setDrawingCacheEnabled(false);
        this.framePanelLayout.setDrawingCacheEnabled(true);
        return this.framePanelLayout.getDrawingCache();
    }

    public void showOpenFileDialog() {
        try {
            ChooseFileDialogFragment.newInstance(FrameDataInteractor.getFrameImagesFileDir().toString(), FrameDataInteractor.frameImageExtension).show(getSupportFragmentManager(), ChooseFileDialogFragment.TAG);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void openSettingsView() {
        startActivityForResult(new Intent(this, ParamSettingsActivity.class), 1);
    }

    public void showFreeze(boolean isShow) {
        this.freezeHintTextView.setVisibility(isShow ? 0 : 8);
    }

    public void showPregDays(int days) {
        this.pregDaysTextView.setText(getString(C0796R.string.activity_main_info_preg_days, new Object[]{Integer.valueOf(days)}));
        this.pregDaysTextView.setVisibility(0);
    }

    public void showID(String id) {
        this.currentId = id.substring(0, Math.min(16, id.length()));
        this.idTextView.setText(getString(C0796R.string.activity_main_info_id, new Object[]{this.currentId}));
    }

    public void showBackfatInfo(double backFatInMM) {
        this.currentBackfat = backFatInMM;
        new DecimalFormat("###.#").setRoundingMode(RoundingMode.HALF_UP);
        this.backfatTextView.setText(getString(C0796R.string.activity_main_info_backfat, new Object[]{fatDecimalFormat.format(backFatInMM)}));
        this.backfatTextView.setVisibility(0);
    }

    public void showSavingVideoDialog(int maxProgress) {
        this.savingVideoProgressDialog.setMax(maxProgress);
        this.savingVideoProgressDialog.show();
    }

    public void updateSavingVideoDialogProgress(int progress) {
        this.savingVideoProgressDialog.setProgress(progress);
    }

    public void dismissSavingVideoDialog() {
        this.savingVideoProgressDialog.dismiss();
    }

    public void showWaitingDetectFatDialog() {
        this.waitingDetectFatDialog.show();
    }

    public void dismissWaitingDetectFatDialog() {
        this.waitingDetectFatDialog.dismiss();
    }

    public void showFatGuideLine(PointF fatLineStartPoint, PointF fatLineEndPoint) {
        this.frameImageView.setFatGuideLine(fatLineStartPoint, fatLineEndPoint);
    }

    public void showPregMeasureView(boolean isShow) {
        this.pregMeasureView.setVisibility(isShow ? 0 : 8);
    }

    @SuppressLint({"SetTextI18n"})
    public void showPtBfMode(int mode) {
        if (mode == 2) {
            this.ptbfStateTextView.setText("BF");
        } else if (mode == 1) {
            this.ptbfStateTextView.setText("PT");
        } else {
            throw new IllegalArgumentException("unexpected mode value for showPtBfMode.");
        }
    }

    public void showNeedLocationService() {
        Builder builder = new Builder(this);
        builder.setTitle(C0796R.string.need_location_service_dialog_title);
        builder.setMessage(C0796R.string.need_location_service_dialog_message);
        builder.setPositiveButton(C0796R.string.dialog_action_ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                MainActivity.this.startActivity(new Intent("android.settings.LOCATION_SOURCE_SETTINGS"));
            }
        });
        builder.setCancelable(false);
        Dialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

    public void goToScanQrcode() {
        startActivityForResult(new Intent(this, CaptureActivity.class), 1);
    }

    public Bitmap loadTestBitmap() {
        return BitmapFactory.decodeResource(getResources(), C0796R.drawable.frame_20161103_152413_);
    }

    public void showWifiListDialog(final String[] apNames) {
        if (this.wifiListDialog == null || !this.wifiListDialog.isShowing()) {
            this.wifiListDialog = DialogFactory.createItemsDialog(this, apNames, getString(C0796R.string.dialog_pick_ap_title), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    String apName = apNames[which];
                    MainActivity.this.mainViewPresenter.enableNetwork(apName, apName);
                }
            }, getString(C0796R.string.dialog_action_cancel), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    MainActivity.this.finish();
                }
            });
            this.wifiListDialog.show();
        }
    }

    public void showNoProbeFoundDialog(int titleId, int messageId) {
        if (this.simpleErrorDialog == null || !this.simpleErrorDialog.isShowing()) {
            this.simpleErrorDialog = DialogFactory.createSimpleErrorDialog(this, getString(titleId), getString(messageId), getString(C0796R.string.dialog_action_button_exit), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    MainActivity.this.finish();
                }
            }, getString(C0796R.string.dialog_action_button_retry), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    MainActivity.this.mainViewPresenter.scanWifi();
                }
            });
            this.simpleErrorDialog.show();
        }
    }

    public void dismissNoProbeFoundDialog() {
        if (this.simpleErrorDialog != null && this.simpleErrorDialog.isShowing()) {
            this.simpleErrorDialog.dismiss();
        }
    }

    public void showConfirmSaveVideoDialog() {
        Builder builder = new Builder(this);
        builder.setTitle(C0796R.string.dialog_title_confirm);
        builder.setMessage(C0796R.string.dialog_message_save_video);
        builder.setPositiveButton(C0796R.string.dialog_action_ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                MainActivity.this.mainViewPresenter.saveCurrentFramesToVideoFile();
            }
        });
        builder.setNegativeButton(C0796R.string.dialog_action_cancel, null);
        builder.create().show();
    }

    public void showSaveReportDialog(File imageFile) {
        this.mainViewPresenter.stopReplay();
        Builder builder = new Builder(this);
        View view = LayoutInflater.from(this).inflate(C0796R.layout.dialog_save_report, null);
        Options options = new Options();
        options.inSampleSize = 2;
        final Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath(), options);
        ((ImageView) view.findViewById(C0796R.id.dialog_save_report_iv)).setImageBitmap(bitmap);
        TextView timeTextView = (TextView) view.findViewById(C0796R.id.dialog_save_report_time_tv);
        final EditText remarksEditText = (EditText) view.findViewById(C0796R.id.dialog_save_report_remarks_et);
        final EditText idEditText = (EditText) view.findViewById(C0796R.id.dialog_save_report_id_et);
        final CheckBox isPregnantCheckbox = (CheckBox) view.findViewById(C0796R.id.dialog_save_report_preg_checkbox);
        final String time = new SimpleDateFormat("yyyy.MM.dd HH:mm", Locale.getDefault()).format(Calendar.getInstance().getTime());
        timeTextView.setText(time);
        builder.setView(view);
        final File file = imageFile;
        builder.setPositiveButton(C0796R.string.dialog_action_ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                String id = idEditText.getText().toString();
                String remarks = remarksEditText.getText().toString();
                if (MainActivity.this.mainViewPresenter.savePDFReportAndExportRecord(MainActivity.this.currentBackfat, time, id, isPregnantCheckbox.isChecked(), remarks, file)) {
                    ToastUtils.showLong(MainActivity.this, (int) C0796R.string.toast_report_saved);
                } else {
                    ToastUtils.showLong(MainActivity.this, (int) C0796R.string.toast_save_report_failed);
                }
            }
        });
        builder.setNegativeButton(C0796R.string.dialog_action_cancel, null);
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setSoftInputMode(2);
        dialog.show();
        dialog.setOnDismissListener(new OnDismissListener() {
            public void onDismiss(DialogInterface dialog) {
                bitmap.recycle();
            }
        });
        idEditText.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                idEditText.setError(TextUtils.isEmpty(s.toString().trim()) ? MainActivity.this.getString(C0796R.string.id_is_empty) : null);
            }

            public void afterTextChanged(Editable s) {
            }
        });
        idEditText.setText(this.currentId);
    }

    public void showUserSettingsDialog() {
        boolean isLeftHandLayout = PreferenceUtils.getBoolean(this, KEY_USE_LEFT_HAND_LAYOUT, true);
        int sleepTimeMinute = PreferenceUtils.getInt(this, ProbeParams.POWER_OFF_MINUTES_KEY, 8);
        LogUtils.LOGI("sleeptime", "getInt:" + sleepTimeMinute);
        Builder builder = new Builder(this);
        View view = LayoutInflater.from(this).inflate(C0796R.layout.dialog_user_settings, null);
        ToggleButton layoutToggleButton = (ToggleButton) view.findViewById(C0796R.id.dialog_user_settings_layout_toggle);
        final SwipeNumberPicker sleepTimeNumberPicker = (SwipeNumberPicker) view.findViewById(C0796R.id.dialog_user_settings_sleep_time_numberpicker);
        sleepTimeNumberPicker.setShowNumberPickerDialog(false);
        sleepTimeNumberPicker.setMaxValue(20);
        sleepTimeNumberPicker.setMinValue(1);
        sleepTimeNumberPicker.setValue(sleepTimeMinute, true);
        sleepTimeNumberPicker.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                if (action == 1) {
                    float x = event.getX();
                    int value = sleepTimeNumberPicker.getValue();
                    if (x > ((float) (v.getWidth() / 2))) {
                        if (value >= sleepTimeNumberPicker.getMaxValue()) {
                            return true;
                        }
                        sleepTimeNumberPicker.setValue(value + 1, false);
                        return true;
                    } else if (value <= sleepTimeNumberPicker.getMinValue()) {
                        return true;
                    } else {
                        sleepTimeNumberPicker.setValue(value - 1, false);
                        return true;
                    }
                } else if (action != 2) {
                    return false;
                } else {
                    return true;
                }
            }
        });
        layoutToggleButton.setChecked(isLeftHandLayout);
        layoutToggleButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                PreferenceUtils.putBoolean(MainActivity.this, MainActivity.KEY_USE_LEFT_HAND_LAYOUT, isChecked);
                ToastUtils.showShort(MainActivity.this, (int) C0796R.string.toast_layout_take_effect);
            }
        });
        builder.setTitle(C0796R.string.dialog_user_settings_label);
        builder.setView(view);
        builder.setPositiveButton(C0796R.string.dialog_action_ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                PreferenceUtils.putInt(MainActivity.this, ProbeParams.POWER_OFF_MINUTES_KEY, sleepTimeNumberPicker.getValue());
                MainActivity.this.mainViewPresenter.updatePrefParams();
            }
        });
        builder.setNegativeButton(C0796R.string.dialog_action_cancel, null);
        Dialog dialog = builder.create();
        dialog.getWindow().setLayout(ScreenUtils.dpToPxSize(this, 400), dialog.getWindow().getAttributes().height);
        dialog.show();
    }

    public void showToast(int messageId) {
        ToastUtils.showLong((Context) this, messageId);
    }

    public void setScanningState(boolean isScanning) {
        if (isScanning) {
            hidePregDayViewsAndBackFatTextView();
        }
        LogUtils.LOGI("UI", "update freezeButton state:" + isScanning);
        this.scanFreezeToggleButton.setChecked(isScanning);
    }

    private void hidePregDayViewsAndBackFatTextView() {
        this.pregMeasureView.setVisibility(4);
        this.pregDaysTextView.setVisibility(4);
        this.backfatTextView.setVisibility(4);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != 1) {
            this.mainViewPresenter.updatePrefParams();
        } else if (data != null && data.getExtras() != null) {
            Bundle bundle = data.getExtras();
            int resultType = bundle.getInt(CodeUtils.RESULT_TYPE);
            if (resultType == 1) {
                String id = bundle.getString(CodeUtils.RESULT_STRING, "");
                Toast.makeText(this, id, 1).show();
                showID(id);
            } else if (resultType == 2) {
                showToast(C0796R.string.scan_id_failed_hint);
            }
        }
    }

    public void onSelectFile(String filename) {
        this.mainViewPresenter.loadFrameFromFile(filename);
    }
}
