package com.lee.ultrascan.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader.TileMode;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.View.MeasureSpec;
import com.lee.ultrascan.R$styleable;

public class ColorRampView extends View {
    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;
    private int defaultLength;
    private int defaultWidth;
    private int endColor;
    private boolean isHorizontal;
    private LinearGradient linearGradient;
    private Paint paint;
    private int startColor;

    public ColorRampView(Context context) {
        this(context, null);
    }

    public ColorRampView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ColorRampView(Context context, AttributeSet attrs, int defStyleAttr) {
        boolean z = false;
        super(context, attrs, defStyleAttr);
        this.defaultWidth = 10;
        this.defaultLength = 80;
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R$styleable.ColorRampView, defStyleAttr, 0);
        this.startColor = typedArray.getColor(1, -1);
        this.endColor = typedArray.getColor(2, ViewCompat.MEASURED_STATE_MASK);
        if (typedArray.getInt(0, 1) == 0) {
            z = true;
        }
        this.isHorizontal = z;
        typedArray.recycle();
        this.paint = new Paint();
        setLayerType(1, null);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width;
        int height;
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        if (this.isHorizontal) {
            width = (int) TypedValue.applyDimension(1, (float) this.defaultLength, getResources().getDisplayMetrics());
            height = (int) TypedValue.applyDimension(1, (float) this.defaultWidth, getResources().getDisplayMetrics());
        } else {
            width = (int) TypedValue.applyDimension(1, (float) this.defaultWidth, getResources().getDisplayMetrics());
            height = (int) TypedValue.applyDimension(1, (float) this.defaultLength, getResources().getDisplayMetrics());
        }
        if (widthMode == 1073741824) {
            width = widthSize;
        } else {
            width = (getPaddingLeft() + width) + getPaddingRight();
        }
        if (heightMode == 1073741824) {
            height = heightSize;
        } else {
            height = (getPaddingTop() + height) + getPaddingBottom();
        }
        setMeasuredDimension(width, height);
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        resetShader();
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRect((float) getPaddingLeft(), (float) getPaddingTop(), (float) (getWidth() - getPaddingRight()), (float) (getHeight() - getPaddingBottom()), this.paint);
    }

    private void resetShader() {
        if (this.isHorizontal) {
            this.linearGradient = new LinearGradient((float) getPaddingLeft(), 0.0f, (float) (getWidth() - getPaddingRight()), 0.0f, this.startColor, this.endColor, TileMode.CLAMP);
        } else {
            this.linearGradient = new LinearGradient(0.0f, (float) getPaddingTop(), 0.0f, (float) (getHeight() - getPaddingBottom()), this.startColor, this.endColor, TileMode.CLAMP);
        }
        this.paint.setShader(this.linearGradient);
    }

    public void setStartColor(int color) {
        this.startColor = color;
        resetShader();
        invalidate();
    }

    public void setEndColor(int color) {
        this.endColor = color;
        resetShader();
        invalidate();
    }

    public int getStartColor() {
        return this.startColor;
    }

    public int getEndColor() {
        return this.endColor;
    }

    public void setDirection(int direction) {
        this.isHorizontal = direction == 0;
        resetShader();
    }
}
