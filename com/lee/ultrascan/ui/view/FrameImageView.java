package com.lee.ultrascan.ui.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

public class FrameImageView extends AppCompatImageView {
    private final DashPathEffect mDashPathEffect;
    private final int mFatGuideLineColor;
    private final float mFatGuideLineWidth;
    private PointF mFatLineEndPoint;
    private PointF mFatLineStartPoint;
    private final Paint mPaint;
    private final Path mPath;

    public FrameImageView(Context context) {
        this(context, null);
    }

    public FrameImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FrameImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mFatGuideLineWidth = 8.0f;
        this.mFatGuideLineColor = -16711936;
        this.mPaint = new Paint();
        this.mPath = new Path();
        this.mDashPathEffect = new DashPathEffect(new float[]{10.0f, 10.0f}, 0.0f);
        initPaint();
    }

    private void initPaint() {
        this.mPaint.setPathEffect(this.mDashPathEffect);
        this.mPaint.setColor(-16711936);
        this.mPaint.setAntiAlias(true);
        this.mPaint.setStyle(Style.STROKE);
        this.mPaint.setStrokeWidth(8.0f);
    }

    public RectF getImageBounds() {
        RectF bounds = new RectF();
        Drawable drawable = getDrawable();
        if (drawable != null) {
            getImageMatrix().mapRect(bounds, new RectF(drawable.getBounds()));
        }
        return bounds;
    }

    public void setImageBitmap(Bitmap bitmap) {
        this.mFatLineEndPoint = null;
        super.setImageBitmap(bitmap);
    }

    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        if (this.mFatLineEndPoint != null) {
            canvas.concat(getImageMatrix());
            this.mPaint.setColor(-16711936);
            this.mPath.rewind();
            this.mPath.moveTo(this.mFatLineStartPoint.x, this.mFatLineStartPoint.y);
            this.mPath.lineTo(this.mFatLineEndPoint.x, this.mFatLineEndPoint.y);
            canvas.drawPath(this.mPath, this.mPaint);
        }
        canvas.restore();
    }

    public void setFatGuideLine(PointF fatLineStartPoint, PointF fatLineEndPoint) {
        this.mFatLineStartPoint = fatLineStartPoint;
        this.mFatLineEndPoint = fatLineEndPoint;
        invalidate();
    }
}
