package com.uuzuche.lib_zxing.view;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import com.google.zxing.ResultPoint;
import com.itextpdf.text.pdf.BaseField;
import com.uuzuche.lib_zxing.C1038R;
import com.uuzuche.lib_zxing.DisplayUtil;
import com.uuzuche.lib_zxing.camera.CameraManager;
import java.util.Collection;
import java.util.HashSet;

public final class ViewfinderView extends View {
    private static final long ANIMATION_DELAY = 100;
    private static final int OPAQUE = 255;
    private int SCAN_VELOCITY;
    private int innercornercolor;
    private int innercornerlength;
    private int innercornerwidth;
    private boolean isCircle;
    private Collection<ResultPoint> lastPossibleResultPoints;
    private final int maskColor;
    private final Paint paint = new Paint();
    private Collection<ResultPoint> possibleResultPoints;
    private Bitmap resultBitmap;
    private final int resultColor;
    private final int resultPointColor;
    private Bitmap scanLight;
    private int scanLineTop;

    public ViewfinderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Resources resources = getResources();
        this.maskColor = resources.getColor(C1038R.color.viewfinder_mask);
        this.resultColor = resources.getColor(C1038R.color.result_view);
        this.resultPointColor = resources.getColor(C1038R.color.possible_result_points);
        this.possibleResultPoints = new HashSet(5);
        this.scanLight = BitmapFactory.decodeResource(resources, C1038R.drawable.scan_light);
        initInnerRect(context, attrs);
    }

    private void initInnerRect(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, C1038R.styleable.innerrect);
        float innerMarginTop = ta.getDimension(C1038R.styleable.innerrect_inner_margintop, -1.0f);
        if (innerMarginTop != -1.0f) {
            CameraManager.FRAME_MARGINTOP = (int) innerMarginTop;
        }
        CameraManager.FRAME_WIDTH = (int) ta.getDimension(C1038R.styleable.innerrect_inner_width, (float) (DisplayUtil.screenWidthPx / 2));
        CameraManager.FRAME_HEIGHT = (int) ta.getDimension(C1038R.styleable.innerrect_inner_height, (float) (DisplayUtil.screenWidthPx / 2));
        this.innercornercolor = ta.getColor(C1038R.styleable.innerrect_inner_corner_color, Color.parseColor("#45DDDD"));
        this.innercornerlength = (int) ta.getDimension(C1038R.styleable.innerrect_inner_corner_length, 65.0f);
        this.innercornerwidth = (int) ta.getDimension(C1038R.styleable.innerrect_inner_corner_width, 15.0f);
        if (ta.getDrawable(C1038R.styleable.innerrect_inner_scan_bitmap) != null) {
            this.scanLight = BitmapFactory.decodeResource(getResources(), ta.getResourceId(C1038R.styleable.innerrect_inner_scan_bitmap, C1038R.drawable.scan_light));
            this.SCAN_VELOCITY = ta.getInt(C1038R.styleable.innerrect_inner_scan_speed, 5);
            this.isCircle = ta.getBoolean(C1038R.styleable.innerrect_inner_scan_iscircle, true);
            ta.recycle();
        } else {
            this.scanLight = BitmapFactory.decodeResource(getResources(), ta.getResourceId(C1038R.styleable.innerrect_inner_scan_bitmap, C1038R.drawable.scan_light));
            this.SCAN_VELOCITY = ta.getInt(C1038R.styleable.innerrect_inner_scan_speed, 5);
            this.isCircle = ta.getBoolean(C1038R.styleable.innerrect_inner_scan_iscircle, true);
            ta.recycle();
        }
    }

    public void onDraw(Canvas canvas) {
        Rect frame = CameraManager.get().getFramingRect();
        if (frame != null) {
            int width = canvas.getWidth();
            int height = canvas.getHeight();
            this.paint.setColor(this.resultBitmap != null ? this.resultColor : this.maskColor);
            canvas.drawRect(0.0f, 0.0f, (float) width, (float) frame.top, this.paint);
            canvas.drawRect(0.0f, (float) frame.top, (float) frame.left, (float) (frame.bottom + 1), this.paint);
            canvas.drawRect((float) (frame.right + 1), (float) frame.top, (float) width, (float) (frame.bottom + 1), this.paint);
            canvas.drawRect(0.0f, (float) (frame.bottom + 1), (float) width, (float) height, this.paint);
            if (this.resultBitmap != null) {
                this.paint.setAlpha(255);
                canvas.drawBitmap(this.resultBitmap, (float) frame.left, (float) frame.top, this.paint);
                return;
            }
            drawFrameBounds(canvas, frame);
            drawScanLight(canvas, frame);
            Collection<ResultPoint> currentPossible = this.possibleResultPoints;
            Collection<ResultPoint> currentLast = this.lastPossibleResultPoints;
            if (currentPossible.isEmpty()) {
                this.lastPossibleResultPoints = null;
            } else {
                this.possibleResultPoints = new HashSet(5);
                this.lastPossibleResultPoints = currentPossible;
                this.paint.setAlpha(255);
                this.paint.setColor(this.resultPointColor);
                if (this.isCircle) {
                    for (ResultPoint point : currentPossible) {
                        canvas.drawCircle(((float) frame.left) + point.getX(), ((float) frame.top) + point.getY(), 6.0f, this.paint);
                    }
                }
            }
            if (currentLast != null) {
                this.paint.setAlpha(127);
                this.paint.setColor(this.resultPointColor);
                if (this.isCircle) {
                    for (ResultPoint point2 : currentLast) {
                        canvas.drawCircle(((float) frame.left) + point2.getX(), ((float) frame.top) + point2.getY(), BaseField.BORDER_WIDTH_THICK, this.paint);
                    }
                }
            }
            postInvalidateDelayed(ANIMATION_DELAY, frame.left, frame.top, frame.right, frame.bottom);
        }
    }

    private void drawScanLight(Canvas canvas, Rect frame) {
        if (this.scanLineTop == 0) {
            this.scanLineTop = frame.top;
        }
        if (this.scanLineTop >= frame.bottom - 30) {
            this.scanLineTop = frame.top;
        } else {
            this.scanLineTop += this.SCAN_VELOCITY;
        }
        canvas.drawBitmap(this.scanLight, null, new Rect(frame.left, this.scanLineTop, frame.right, this.scanLineTop + 30), this.paint);
    }

    private void drawFrameBounds(Canvas canvas, Rect frame) {
        this.paint.setColor(this.innercornercolor);
        this.paint.setStyle(Style.FILL);
        int corWidth = this.innercornerwidth;
        int corLength = this.innercornerlength;
        canvas.drawRect((float) frame.left, (float) frame.top, (float) (frame.left + corWidth), (float) (frame.top + corLength), this.paint);
        canvas.drawRect((float) frame.left, (float) frame.top, (float) (frame.left + corLength), (float) (frame.top + corWidth), this.paint);
        canvas.drawRect((float) (frame.right - corWidth), (float) frame.top, (float) frame.right, (float) (frame.top + corLength), this.paint);
        canvas.drawRect((float) (frame.right - corLength), (float) frame.top, (float) frame.right, (float) (frame.top + corWidth), this.paint);
        canvas.drawRect((float) frame.left, (float) (frame.bottom - corLength), (float) (frame.left + corWidth), (float) frame.bottom, this.paint);
        canvas.drawRect((float) frame.left, (float) (frame.bottom - corWidth), (float) (frame.left + corLength), (float) frame.bottom, this.paint);
        canvas.drawRect((float) (frame.right - corWidth), (float) (frame.bottom - corLength), (float) frame.right, (float) frame.bottom, this.paint);
        canvas.drawRect((float) (frame.right - corLength), (float) (frame.bottom - corWidth), (float) frame.right, (float) frame.bottom, this.paint);
    }

    public void drawViewfinder() {
        this.resultBitmap = null;
        invalidate();
    }

    public void addPossibleResultPoint(ResultPoint point) {
        this.possibleResultPoints.add(point);
    }

    public static int dip2px(Context context, float dpValue) {
        return (int) ((dpValue * context.getResources().getDisplayMetrics().density) + 0.5f);
    }
}
