package com.h6ah4i.android.widget.verticalseekbar;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;

public class VerticalSeekBarWrapper extends FrameLayout {
    public VerticalSeekBarWrapper(Context context) {
        this(context, null, 0);
    }

    public VerticalSeekBarWrapper(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VerticalSeekBarWrapper(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        if (useViewRotation()) {
            onSizeChangedUseViewRotation(w, h, oldw, oldh);
        } else {
            onSizeChangedTraditionalRotation(w, h, oldw, oldh);
        }
    }

    private void onSizeChangedTraditionalRotation(int w, int h, int oldw, int oldh) {
        VerticalSeekBar seekBar = getChildSeekBar();
        if (seekBar != null) {
            LayoutParams lp = (LayoutParams) seekBar.getLayoutParams();
            lp.width = -2;
            lp.height = h;
            seekBar.setLayoutParams(lp);
            seekBar.measure(0, 0);
            int seekBarWidth = seekBar.getMeasuredWidth();
            seekBar.measure(MeasureSpec.makeMeasureSpec(w, Integer.MIN_VALUE), MeasureSpec.makeMeasureSpec(h, 1073741824));
            lp.gravity = 51;
            lp.leftMargin = (w - seekBarWidth) / 2;
            seekBar.setLayoutParams(lp);
        }
        super.onSizeChanged(w, h, oldw, oldh);
    }

    private void onSizeChangedUseViewRotation(int w, int h, int oldw, int oldh) {
        VerticalSeekBar seekBar = getChildSeekBar();
        if (seekBar != null) {
            seekBar.measure(MeasureSpec.makeMeasureSpec(h, 1073741824), MeasureSpec.makeMeasureSpec(w, Integer.MIN_VALUE));
        }
        applyViewRotation(w, h);
        super.onSizeChanged(w, h, oldw, oldh);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        VerticalSeekBar seekBar = getChildSeekBar();
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        if (seekBar == null || widthMode == 1073741824) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }
        int seekBarWidth;
        int seekBarHeight;
        if (useViewRotation()) {
            seekBar.measure(heightMeasureSpec, widthMeasureSpec);
            seekBarWidth = seekBar.getMeasuredHeight();
            seekBarHeight = seekBar.getMeasuredWidth();
        } else {
            seekBar.measure(widthMeasureSpec, heightMeasureSpec);
            seekBarWidth = seekBar.getMeasuredWidth();
            seekBarHeight = seekBar.getMeasuredHeight();
        }
        setMeasuredDimension(ViewCompat.resolveSizeAndState(seekBarWidth, widthMeasureSpec, 0), ViewCompat.resolveSizeAndState(seekBarHeight, heightMeasureSpec, 0));
    }

    void applyViewRotation() {
        applyViewRotation(getWidth(), getHeight());
    }

    private void applyViewRotation(int w, int h) {
        VerticalSeekBar seekBar = getChildSeekBar();
        if (seekBar != null) {
            int rotationAngle = seekBar.getRotationAngle();
            ViewGroup.LayoutParams lp = seekBar.getLayoutParams();
            lp.width = h;
            lp.height = -2;
            seekBar.setLayoutParams(lp);
            if (rotationAngle == 90) {
                int paddingEnd = ViewCompat.getPaddingEnd(seekBar);
                ViewCompat.setRotation(seekBar, 90.0f);
                ViewCompat.setTranslationX(seekBar, (float) ((-(h - w)) / 2));
                ViewCompat.setTranslationY(seekBar, (float) ((h / 2) - paddingEnd));
            } else if (rotationAngle == 270) {
                int paddingStart = ViewCompat.getPaddingStart(seekBar);
                ViewCompat.setRotation(seekBar, -90.0f);
                ViewCompat.setTranslationX(seekBar, (float) ((-(h - w)) / 2));
                ViewCompat.setTranslationY(seekBar, (float) ((h / 2) - paddingStart));
            }
        }
    }

    private VerticalSeekBar getChildSeekBar() {
        View child;
        if (getChildCount() > 0) {
            child = getChildAt(0);
        } else {
            child = null;
        }
        if (child instanceof VerticalSeekBar) {
            return (VerticalSeekBar) child;
        }
        return null;
    }

    private boolean useViewRotation() {
        VerticalSeekBar seekBar = getChildSeekBar();
        if (seekBar != null) {
            return seekBar.useViewRotation();
        }
        return false;
    }
}
