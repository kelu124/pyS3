package com.lee.ultrascan.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.View.MeasureSpec;
import com.google.common.base.Preconditions;
import com.itextpdf.text.pdf.BaseField;
import com.lee.ultrascan.R$styleable;
import com.lee.ultrascan.utils.LogUtils;

public class ScaleplateView extends View {
    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;
    private int bigIntervalSteps;
    private int color;
    private int defaultLength;
    private int defaultWidth;
    private float endPixelPos;
    private float endValue;
    private boolean isFlip;
    private boolean isHorizontal;
    private Paint paint;
    private float smallValueInterval;
    private float startPixelPos;
    private float startValue;
    private Rect textBound;
    private float totalValue;
    private String unit;

    public ScaleplateView(Context context) {
        this(context, null);
    }

    public ScaleplateView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScaleplateView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.startPixelPos = 0.0f;
        this.endPixelPos = 0.0f;
        this.defaultWidth = 10;
        this.defaultLength = 80;
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R$styleable.ScaleplateView, defStyleAttr, 0);
        this.isHorizontal = typedArray.getInt(0, 1) == 0;
        this.startValue = typedArray.getFloat(2, 0.0f);
        this.endValue = typedArray.getFloat(3, 10.0f);
        this.totalValue = typedArray.getFloat(4, 10.0f);
        this.smallValueInterval = typedArray.getFloat(5, BaseField.BORDER_WIDTH_THIN);
        this.bigIntervalSteps = typedArray.getInt(6, 5);
        this.color = typedArray.getColor(7, -1);
        this.unit = typedArray.getString(1);
        this.unit = this.unit == null ? "" : this.unit;
        this.isFlip = typedArray.getBoolean(8, false);
        typedArray.recycle();
        this.paint = new Paint();
        this.textBound = new Rect();
        this.paint.setColor(this.color);
        this.paint.setStrokeWidth(4.0f);
        this.paint.getTextBounds(this.endValue + "(" + this.unit + ")", 0, this.unit.length(), this.textBound);
        LogUtils.LOGE("scaleplate", "bound w:" + this.textBound.width());
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
            width = ((int) TypedValue.applyDimension(1, (float) this.defaultLength, getResources().getDisplayMetrics())) + this.textBound.width();
            height = (int) TypedValue.applyDimension(1, (float) this.defaultWidth, getResources().getDisplayMetrics());
        } else {
            width = (int) TypedValue.applyDimension(1, (float) this.defaultWidth, getResources().getDisplayMetrics());
            height = ((int) TypedValue.applyDimension(1, (float) this.defaultLength, getResources().getDisplayMetrics())) + this.textBound.height();
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

    protected void onDraw(Canvas canvas) {
        float totalPixels;
        int drawWidth;
        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();
        if (this.isHorizontal) {
            totalPixels = (float) width;
        } else {
            totalPixels = (float) height;
        }
        totalPixels = this.endPixelPos - this.startPixelPos;
        float start = ((this.startValue / this.totalValue) * totalPixels) + this.startPixelPos;
        float end = ((this.endValue / this.totalValue) * totalPixels) + this.startPixelPos;
        if (this.isHorizontal) {
            drawWidth = getHeight() / 2;
            if (this.isFlip) {
                canvas.drawLine(start, (float) drawWidth, end, (float) drawWidth, this.paint);
            } else {
                canvas.drawLine(start, 0.0f, end, 0.0f, this.paint);
            }
        } else {
            drawWidth = getWidth() / 2;
            if (this.isFlip) {
                canvas.drawLine((float) width, start, (float) width, end, this.paint);
            } else {
                canvas.drawLine(0.0f, start, 0.0f, end, this.paint);
            }
        }
        float value = this.startValue;
        int steps = 0;
        int intervalPixels = (int) (((end - start) / Math.abs(this.endValue - this.startValue)) * this.smallValueInterval);
        while (value <= this.endValue) {
            value += this.smallValueInterval;
            float pos = start + ((float) (intervalPixels * steps));
            float widthScale = steps % this.bigIntervalSteps == 0 ? BaseField.BORDER_WIDTH_MEDIUM : BaseField.BORDER_WIDTH_THIN;
            if (this.isHorizontal) {
                canvas.drawLine(pos, ((float) drawWidth) * widthScale, pos, 0.0f, this.paint);
            } else if (this.isFlip) {
                canvas.drawLine((float) width, pos, ((float) width) - (((float) drawWidth) * widthScale), pos, this.paint);
            } else {
                canvas.drawLine(0.0f, pos, ((float) drawWidth) * widthScale, pos, this.paint);
            }
            steps++;
        }
    }

    public void setColor(int color) {
        this.color = color;
        invalidate();
    }

    public void setDirection(int direction) {
        this.isHorizontal = direction == 0;
        invalidate();
    }

    public void setTotalValue(float value) {
        Preconditions.checkArgument(value >= 0.0f);
        this.totalValue = value;
        invalidate();
    }

    public void setEndValue(float value) {
        Preconditions.checkArgument(value >= 0.0f);
        this.endValue = value;
        invalidate();
    }

    public void setStartValue(float value) {
        Preconditions.checkArgument(value >= 0.0f);
        this.startValue = value;
        invalidate();
    }

    public void setStartPixelPos(float startPixelPos) {
        this.startPixelPos = startPixelPos;
        invalidate();
    }

    public void setEndPixelPos(float endPixelPos) {
        this.endPixelPos = endPixelPos;
        invalidate();
    }
}
