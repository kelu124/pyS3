package com.lee.ultrascan.ui;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;
import com.lee.ultrascan.C0796R;
import com.lee.ultrascan.ui.view.ColorRampView;
import com.lee.ultrascan.ui.view.FrameImageView;
import com.lee.ultrascan.ui.view.PregMeasureView;
import com.lee.ultrascan.ui.view.ScaleplateView;

public class MainActivity$$ViewBinder<T extends MainActivity> implements ViewBinder<T> {
    public void bind(Finder finder, T target, Object source) {
        target.batteryImageView = (ImageView) finder.castView((View) finder.findRequiredView(source, C0796R.id.battery_iv, "field 'batteryImageView'"), C0796R.id.battery_iv, "field 'batteryImageView'");
        target.timeTextView = (TextView) finder.castView((View) finder.findRequiredView(source, C0796R.id.time_tv, "field 'timeTextView'"), C0796R.id.time_tv, "field 'timeTextView'");
        target.ptbfStateTextView = (TextView) finder.castView((View) finder.findRequiredView(source, C0796R.id.pt_bf_tv, "field 'ptbfStateTextView'"), C0796R.id.pt_bf_tv, "field 'ptbfStateTextView'");
        target.backfatButton = (Button) finder.castView((View) finder.findRequiredView(source, C0796R.id.layout_state_panel_backfat_btn, "field 'backfatButton'"), C0796R.id.layout_state_panel_backfat_btn, "field 'backfatButton'");
        target.pregButton = (Button) finder.castView((View) finder.findRequiredView(source, C0796R.id.layout_state_panel_preg_btn, "field 'pregButton'"), C0796R.id.layout_state_panel_preg_btn, "field 'pregButton'");
        target.depthUpButton = (Button) finder.castView((View) finder.findRequiredView(source, C0796R.id.layout_state_panel_depth_up_btn, "field 'depthUpButton'"), C0796R.id.layout_state_panel_depth_up_btn, "field 'depthUpButton'");
        target.depthDownButton = (Button) finder.castView((View) finder.findRequiredView(source, C0796R.id.layout_state_panel_depth_down_btn, "field 'depthDownButton'"), C0796R.id.layout_state_panel_depth_down_btn, "field 'depthDownButton'");
        target.tgcSeekBar = (SeekBar) finder.castView((View) finder.findRequiredView(source, C0796R.id.layout_state_panel_tgc_seekbar, "field 'tgcSeekBar'"), C0796R.id.layout_state_panel_tgc_seekbar, "field 'tgcSeekBar'");
        target.scanFreezeToggleButton = (ToggleButton) finder.castView((View) finder.findRequiredView(source, C0796R.id.layout_state_panel_scan_freeze_togglebtn, "field 'scanFreezeToggleButton'"), C0796R.id.layout_state_panel_scan_freeze_togglebtn, "field 'scanFreezeToggleButton'");
        target.ptbfButton = (Button) finder.castView((View) finder.findRequiredView(source, C0796R.id.layout_state_panel_pt_bf_btn, "field 'ptbfButton'"), C0796R.id.layout_state_panel_pt_bf_btn, "field 'ptbfButton'");
        target.tgcTextView = (TextView) finder.castView((View) finder.findRequiredView(source, C0796R.id.layout_state_panel_info_tgc_tv, "field 'tgcTextView'"), C0796R.id.layout_state_panel_info_tgc_tv, "field 'tgcTextView'");
        target.depthTextView = (TextView) finder.castView((View) finder.findRequiredView(source, C0796R.id.layout_state_panel_info_depth_tv, "field 'depthTextView'"), C0796R.id.layout_state_panel_info_depth_tv, "field 'depthTextView'");
        target.freqTextView = (TextView) finder.castView((View) finder.findRequiredView(source, C0796R.id.layout_state_panel_info_freq_tv, "field 'freqTextView'"), C0796R.id.layout_state_panel_info_freq_tv, "field 'freqTextView'");
        target.idTextView = (TextView) finder.castView((View) finder.findRequiredView(source, C0796R.id.id_tv, "field 'idTextView'"), C0796R.id.id_tv, "field 'idTextView'");
        target.backfatTextView = (TextView) finder.castView((View) finder.findRequiredView(source, C0796R.id.backfat_tv, "field 'backfatTextView'"), C0796R.id.backfat_tv, "field 'backfatTextView'");
        target.pregDaysTextView = (TextView) finder.castView((View) finder.findRequiredView(source, C0796R.id.preg_days_tv, "field 'pregDaysTextView'"), C0796R.id.preg_days_tv, "field 'pregDaysTextView'");
        target.freezeHintTextView = (TextView) finder.castView((View) finder.findRequiredView(source, C0796R.id.freeze_hint_tv, "field 'freezeHintTextView'"), C0796R.id.freeze_hint_tv, "field 'freezeHintTextView'");
        target.scanIdImageView = (ImageView) finder.castView((View) finder.findRequiredView(source, C0796R.id.scan_id_iv, "field 'scanIdImageView'"), C0796R.id.scan_id_iv, "field 'scanIdImageView'");
        target.settingButton = (Button) finder.castView((View) finder.findRequiredView(source, C0796R.id.layout_player_panel_setting_button, "field 'settingButton'"), C0796R.id.layout_player_panel_setting_button, "field 'settingButton'");
        target.replayButton = (ImageView) finder.castView((View) finder.findRequiredView(source, C0796R.id.layout_player_panel_play_button, "field 'replayButton'"), C0796R.id.layout_player_panel_play_button, "field 'replayButton'");
        target.saveImageButton = (Button) finder.castView((View) finder.findRequiredView(source, C0796R.id.layout_player_panel_save_image_button, "field 'saveImageButton'"), C0796R.id.layout_player_panel_save_image_button, "field 'saveImageButton'");
        target.saveVideoButton = (Button) finder.castView((View) finder.findRequiredView(source, C0796R.id.layout_player_panel_save_video_button, "field 'saveVideoButton'"), C0796R.id.layout_player_panel_save_video_button, "field 'saveVideoButton'");
        target.openFileButton = (Button) finder.castView((View) finder.findRequiredView(source, C0796R.id.layout_player_panel_openfile_button, "field 'openFileButton'"), C0796R.id.layout_player_panel_openfile_button, "field 'openFileButton'");
        target.frameTextView = (TextView) finder.castView((View) finder.findRequiredView(source, C0796R.id.layout_player_panel_frame_tv, "field 'frameTextView'"), C0796R.id.layout_player_panel_frame_tv, "field 'frameTextView'");
        target.playerSeekBar = (SeekBar) finder.castView((View) finder.findRequiredView(source, C0796R.id.layout_player_panel_seekbar, "field 'playerSeekBar'"), C0796R.id.layout_player_panel_seekbar, "field 'playerSeekBar'");
        target.scaleplateView = (ScaleplateView) finder.castView((View) finder.findRequiredView(source, C0796R.id.layout_scale_plate_panel_scaleplate, "field 'scaleplateView'"), C0796R.id.layout_scale_plate_panel_scaleplate, "field 'scaleplateView'");
        target.colorRampView = (ColorRampView) finder.castView((View) finder.findRequiredView(source, C0796R.id.layout_scale_plate_panel_colorramp, "field 'colorRampView'"), C0796R.id.layout_scale_plate_panel_colorramp, "field 'colorRampView'");
        target.statePanelLayout = (ViewGroup) finder.castView((View) finder.findRequiredView(source, C0796R.id.activity_main_state_panel, "field 'statePanelLayout'"), C0796R.id.activity_main_state_panel, "field 'statePanelLayout'");
        target.playerPanelLayout = (ViewGroup) finder.castView((View) finder.findRequiredView(source, C0796R.id.activity_main_player_panel, "field 'playerPanelLayout'"), C0796R.id.activity_main_player_panel, "field 'playerPanelLayout'");
        target.framePanelLayout = (ViewGroup) finder.castView((View) finder.findRequiredView(source, C0796R.id.activity_main_frame_panel, "field 'framePanelLayout'"), C0796R.id.activity_main_frame_panel, "field 'framePanelLayout'");
        target.frameImageView = (FrameImageView) finder.castView((View) finder.findRequiredView(source, C0796R.id.activity_main_frame_fiv, "field 'frameImageView'"), C0796R.id.activity_main_frame_fiv, "field 'frameImageView'");
        target.roiTouchView = (View) finder.findRequiredView(source, C0796R.id.activity_main_frame_roi_touch_view, "field 'roiTouchView'");
        target.pregMeasureView = (PregMeasureView) finder.castView((View) finder.findRequiredView(source, C0796R.id.activity_main_preg_measure_view, "field 'pregMeasureView'"), C0796R.id.activity_main_preg_measure_view, "field 'pregMeasureView'");
        target.loadedScreenshotImageView = (ImageView) finder.castView((View) finder.findRequiredView(source, C0796R.id.activity_main_loaded_screenshot_iv, "field 'loadedScreenshotImageView'"), C0796R.id.activity_main_loaded_screenshot_iv, "field 'loadedScreenshotImageView'");
    }

    public void unbind(T target) {
        target.batteryImageView = null;
        target.timeTextView = null;
        target.ptbfStateTextView = null;
        target.backfatButton = null;
        target.pregButton = null;
        target.depthUpButton = null;
        target.depthDownButton = null;
        target.tgcSeekBar = null;
        target.scanFreezeToggleButton = null;
        target.ptbfButton = null;
        target.tgcTextView = null;
        target.depthTextView = null;
        target.freqTextView = null;
        target.idTextView = null;
        target.backfatTextView = null;
        target.pregDaysTextView = null;
        target.freezeHintTextView = null;
        target.scanIdImageView = null;
        target.settingButton = null;
        target.replayButton = null;
        target.saveImageButton = null;
        target.saveVideoButton = null;
        target.openFileButton = null;
        target.frameTextView = null;
        target.playerSeekBar = null;
        target.scaleplateView = null;
        target.colorRampView = null;
        target.statePanelLayout = null;
        target.playerPanelLayout = null;
        target.framePanelLayout = null;
        target.frameImageView = null;
        target.roiTouchView = null;
        target.pregMeasureView = null;
        target.loadedScreenshotImageView = null;
    }
}
