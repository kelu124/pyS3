package com.vi.swipenumberpicker;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Build.VERSION;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.widget.LinearLayout.LayoutParams;
import android.widget.NumberPicker;
import android.widget.TextView;
import com.itextpdf.text.pdf.BaseField;

public class SwipeNumberPicker extends TextView {
    private static final float GESTURE_STEP_DP = 5.0f;
    private static final String TAG = "SwpNumPicker";
    private int mArrowColor;
    private int mBackgroundColor;
    private float mCornerRadius;
    private String mDialogTitle = "";
    private int mGestureStepPx;
    private boolean mIntermediate = false;
    private float mIntermediateValue;
    private float mIntermediateX;
    private boolean mIsShowNumberPickerDialog = true;
    private int mMaxValue;
    private int mMinValue;
    private int mNumColor;
    private OnValueChangeListener mOnValueChangeListener;
    private int mPrimaryValue;
    private float mStartX;
    private float mStrokeWidth;
    private float mTextWidth;
    private AlertDialog numberPickerDialog;

    public SwipeNumberPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public SwipeNumberPicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attributeSet) {
        initAttributes(context, attributeSet);
        this.mGestureStepPx = (int) ((GESTURE_STEP_DP * getResources().getDisplayMetrics().density) + 0.5f);
        customize();
    }

    private void initAttributes(Context context, AttributeSet attributeSet) {
        TypedArray attrs = context.obtainStyledAttributes(attributeSet, C1042R.styleable.SwipeNumberPicker, 0, 0);
        if (attrs != null) {
            try {
                this.mPrimaryValue = attrs.getInteger(C1042R.styleable.SwipeNumberPicker_snp_value, 0);
                this.mMinValue = attrs.getInteger(C1042R.styleable.SwipeNumberPicker_snp_min, -9999);
                this.mMaxValue = attrs.getInteger(C1042R.styleable.SwipeNumberPicker_snp_max, 9999);
                this.mArrowColor = attrs.getColor(C1042R.styleable.SwipeNumberPicker_snp_arrowColor, context.getResources().getColor(C1042R.color.arrows));
                this.mBackgroundColor = attrs.getColor(C1042R.styleable.SwipeNumberPicker_snp_backgroundColor, context.getResources().getColor(C1042R.color.background));
                this.mNumColor = attrs.getColor(C1042R.styleable.SwipeNumberPicker_snp_numberColor, context.getResources().getColor(C1042R.color.text));
                this.mIntermediate = attrs.getBoolean(C1042R.styleable.SwipeNumberPicker_snp_intermediate, false);
            } finally {
                attrs.recycle();
            }
        }
        this.mCornerRadius = context.getResources().getDimension(C1042R.dimen.radius);
        this.mStrokeWidth = context.getResources().getDimension(C1042R.dimen.stroke_width);
        Paint textPaint = new Paint();
        textPaint.setTextSize(getTextSize());
        this.mTextWidth = textPaint.measureText(Integer.toString(this.mMaxValue));
    }

    private void customize() {
        setCompoundDrawablesWithIntrinsicBounds(getDrawableCompat(C1042R.drawable.ic_arrow_left), null, getDrawableCompat(C1042R.drawable.ic_arrow_right), null);
        setBackgroundCompat(createBackgroundStateList());
        setGravity(17);
        setSingleLine(true);
        setTextColor(this.mNumColor);
        setNormalBackground();
        this.mIntermediateValue = (float) this.mPrimaryValue;
        changeValue(this.mPrimaryValue);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Drawable arrow = getCompoundDrawables()[0];
        setMinWidth((int) TypedValue.applyDimension(0, this.mTextWidth + ((float) (arrow.getBounds().width() * 2)), getContext().getResources().getDisplayMetrics()));
        setMinHeight((int) TypedValue.applyDimension(0, ((float) arrow.getBounds().height()) * 1.5f, getContext().getResources().getDisplayMetrics()));
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (!isEnabled()) {
            return false;
        }
        float currentX = event.getX();
        switch (event.getAction()) {
            case 0:
                this.mStartX = event.getX();
                this.mIntermediateX = this.mStartX;
                highlightBackground();
                return true;
            case 1:
            case 3:
                setNormalBackground();
                if (Math.abs(currentX - this.mStartX) <= ((float) this.mGestureStepPx)) {
                    processClick();
                    return false;
                }
                notifyListener((int) this.mIntermediateValue);
                return false;
            case 2:
                if (Math.abs(currentX - this.mStartX) > ((float) this.mGestureStepPx)) {
                    float distance = currentX - this.mIntermediateX;
                    highlightArrows(distance);
                    double swipedDistance = (double) TypedValue.applyDimension(0, Math.abs(distance), getContext().getResources().getDisplayMetrics());
                    if (swipedDistance < 25.0d) {
                        return true;
                    }
                    float threshold;
                    if (swipedDistance < 50.0d) {
                        threshold = BaseField.BORDER_WIDTH_THIN;
                    } else if (swipedDistance < 150.0d) {
                        threshold = BaseField.BORDER_WIDTH_MEDIUM;
                    } else if (swipedDistance < 300.0d) {
                        threshold = BaseField.BORDER_WIDTH_THICK;
                    } else if (swipedDistance < 450.0d) {
                        threshold = 4.0f;
                    } else {
                        threshold = GESTURE_STEP_DP;
                    }
                    float f = this.mIntermediateValue;
                    if (distance <= 0.0f) {
                        threshold = -threshold;
                    }
                    this.mIntermediateValue = f + threshold;
                    changeValue((int) this.mIntermediateValue);
                    if (this.mIntermediate) {
                        notifyListener((int) this.mIntermediateValue);
                    }
                }
                this.mIntermediateX = currentX;
                return true;
            default:
                setNormalBackground();
                return false;
        }
    }

    private void notifyListener(int newValue) {
        if (this.mOnValueChangeListener == null || !this.mOnValueChangeListener.onValueChange(this, this.mPrimaryValue, newValue)) {
            changeValue(this.mPrimaryValue);
        } else {
            this.mPrimaryValue = newValue;
        }
        this.mIntermediateValue = (float) this.mPrimaryValue;
    }

    private void changeValue(int value) {
        if (value < this.mMinValue || value > this.mMaxValue) {
            value = value < this.mMinValue ? this.mMinValue : this.mMaxValue;
            this.mIntermediateValue = (float) value;
        }
        setText(String.valueOf(value));
    }

    private void processClick() {
        if (this.mIsShowNumberPickerDialog) {
            showNumberPickerDialog();
        } else {
            performClick();
        }
    }

    public void showNumberPickerDialog() {
        if (this.numberPickerDialog == null || !this.numberPickerDialog.isShowing()) {
            this.numberPickerDialog = getNumberPickerDialog();
            this.numberPickerDialog.show();
        }
    }

    private AlertDialog getNumberPickerDialog() {
        int i;
        final NumberPicker numberPicker = new NumberPicker(getContext());
        numberPicker.setLayoutParams(new LayoutParams(-2, -2));
        numberPicker.setMaxValue(this.mMaxValue);
        if (this.mMinValue > 0) {
            i = this.mMinValue;
        } else {
            i = 0;
        }
        numberPicker.setMinValue(i);
        numberPicker.setValue(this.mPrimaryValue);
        numberPicker.setWrapSelectorWheel(false);
        Builder builder = new Builder(getContext());
        if (!this.mDialogTitle.equals("")) {
            builder.setTitle(this.mDialogTitle);
        }
        builder.setView(numberPicker).setPositiveButton(17039370, new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                numberPicker.clearFocus();
                int newValue = numberPicker.getValue();
                SwipeNumberPicker.this.changeValue(newValue);
                SwipeNumberPicker.this.notifyListener(newValue);
            }
        });
        return builder.create();
    }

    private void setNormalBackground() {
        setPressed(false);
        customizeArrows(this.mArrowColor);
    }

    private StateListDrawable createBackgroundStateList() {
        StateListDrawable drawable = new StateListDrawable();
        drawable.addState(new int[]{-16842910}, createBackgroundDrawable(lightenColor(this.mBackgroundColor)));
        drawable.addState(new int[]{16842919}, createBackgroundDrawable(darkenColor(this.mBackgroundColor)));
        drawable.addState(new int[0], createBackgroundDrawable(this.mBackgroundColor));
        return drawable;
    }

    private Drawable createBackgroundDrawable(int color) {
        Paint paint = new ShapeDrawable(new RoundRectShape(new float[]{this.mCornerRadius, this.mCornerRadius, this.mCornerRadius, this.mCornerRadius, this.mCornerRadius, this.mCornerRadius, this.mCornerRadius, this.mCornerRadius}, null, null)).getPaint();
        paint.setAntiAlias(true);
        paint.setStyle(Style.FILL_AND_STROKE);
        paint.setColor(color);
        paint.setStrokeWidth(this.mStrokeWidth);
        LayerDrawable drawable = new LayerDrawable(new Drawable[]{backgroundDrawable});
        int halfStrokeWidth = (int) (this.mStrokeWidth / BaseField.BORDER_WIDTH_MEDIUM);
        drawable.setLayerInset(0, halfStrokeWidth, halfStrokeWidth, halfStrokeWidth, halfStrokeWidth);
        return drawable;
    }

    private void customizeArrows(int color) {
        setColorFilter(getCompoundDrawables()[0], color);
        setColorFilter(getCompoundDrawables()[2], color);
    }

    private void highlightBackground() {
        setPressed(true);
    }

    private void highlightArrows(float distance) {
        setPressed(true);
        if (distance < 0.0f) {
            setColorFilter(getCompoundDrawables()[0], darkenColor(this.mArrowColor));
            setColorFilter(getCompoundDrawables()[2], this.mArrowColor);
            return;
        }
        setColorFilter(getCompoundDrawables()[0], this.mArrowColor);
        setColorFilter(getCompoundDrawables()[2], darkenColor(this.mArrowColor));
    }

    private void setColorFilter(Drawable drawable, int color) {
        if (drawable != null) {
            drawable.mutate().setColorFilter(color, Mode.SRC_IN);
        }
    }

    private int darkenColor(int color) {
        return Color.argb(Color.alpha(color), Math.max((int) (((float) Color.red(color)) * 0.9f), 0), Math.max((int) (((float) Color.green(color)) * 0.9f), 0), Math.max((int) (((float) Color.blue(color)) * 0.9f), 0));
    }

    private int lightenColor(int color) {
        return Color.argb(Color.alpha(color), (int) ((((((float) Color.red(color)) * (BaseField.BORDER_WIDTH_THIN - 0.3f)) / 255.0f) + 0.3f) * 255.0f), (int) ((((((float) Color.green(color)) * (BaseField.BORDER_WIDTH_THIN - 0.3f)) / 255.0f) + 0.3f) * 255.0f), (int) ((((((float) Color.blue(color)) * (BaseField.BORDER_WIDTH_THIN - 0.3f)) / 255.0f) + 0.3f) * 255.0f));
    }

    public void setBackgroundColor(int color) {
        this.mBackgroundColor = color;
        setBackgroundCompat(createBackgroundStateList());
    }

    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        int arrowColor = this.mArrowColor;
        int numColor = this.mNumColor;
        if (!enabled) {
            arrowColor = lightenColor(this.mArrowColor);
            numColor = lightenColor(this.mNumColor);
        }
        customizeArrows(arrowColor);
        setTextColor(numColor);
    }

    @TargetApi(21)
    private Drawable getDrawableCompat(int resource) {
        if (VERSION.SDK_INT >= 21) {
            return getContext().getResources().getDrawable(resource, null);
        }
        return getContext().getResources().getDrawable(resource);
    }

    private void setBackgroundCompat(Drawable drawable) {
        if (VERSION.SDK_INT >= 16) {
            setBackground(drawable);
        } else {
            setBackgroundDrawable(drawable);
        }
    }

    private void setValue(int value) {
        this.mPrimaryValue = value;
        this.mIntermediateValue = (float) value;
    }

    public void setValue(int value, boolean isNotifyListener) {
        setValue(value);
        changeValue(value);
        if (isNotifyListener) {
            notifyListener(value);
        }
    }

    public void setArrowColor(int mArrowColor) {
        this.mArrowColor = mArrowColor;
        customizeArrows(mArrowColor);
    }

    public void setOnValueChangeListener(OnValueChangeListener valueChangeListener) {
        this.mOnValueChangeListener = valueChangeListener;
    }

    public int getValue() {
        return this.mPrimaryValue;
    }

    public void setNumberPickerDialogTitle(String title) {
        this.mDialogTitle = title;
    }

    public void setShowNumberPickerDialog(boolean isShowNumberPickerDialog) {
        this.mIsShowNumberPickerDialog = isShowNumberPickerDialog;
    }

    public int getMinValue() {
        return this.mMinValue;
    }

    public void setMinValue(int minValue) {
        this.mMinValue = minValue;
    }

    public int getMaxValue() {
        return this.mMaxValue;
    }

    public void setMaxValue(int maxValue) {
        this.mMaxValue = maxValue;
    }

    public boolean isIntermediate() {
        return this.mIntermediate;
    }

    public void setIntermediate(boolean intermediate) {
        this.mIntermediate = intermediate;
    }
}
