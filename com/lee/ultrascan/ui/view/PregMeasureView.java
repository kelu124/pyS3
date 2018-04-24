package com.lee.ultrascan.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import com.itextpdf.text.pdf.BaseField;

public class PregMeasureView extends View {
    private final int CROSS_COLOR;
    private final int LINE_COLOR;
    private final int POINT_RADIUS_SIZE;
    private Rect dirtyRect;
    private RectF dirtyRectF;
    private RectF endPointRect;
    private onDragListener onDragListener;
    private final Paint paint;
    private float prevX;
    private float prevY;
    private RectF startPointRect;
    private State state;

    public interface onDragListener {
        void onDragEnd(PointF pointF, PointF pointF2, boolean z);
    }

    private enum State {
        IDLE,
        DRAGGING_START_POINT,
        DRAGGING_END_POINT
    }

    public PregMeasureView(Context context) {
        this(context, null);
    }

    public PregMeasureView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PregMeasureView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.POINT_RADIUS_SIZE = 20;
        this.LINE_COLOR = -16776961;
        this.CROSS_COLOR = -16711936;
        this.paint = new Paint();
        this.state = State.IDLE;
        this.dirtyRectF = new RectF();
        this.dirtyRect = new Rect();
    }

    public void setOnDragListener(onDragListener dragListener) {
        this.onDragListener = dragListener;
    }

    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (visibility == 0) {
            resetView();
        }
    }

    protected void onSizeChanged(int w, int h, int t, int b) {
        super.onSizeChanged(w, h, t, b);
        resetView();
    }

    public void resetView() {
        int width = getWidth();
        float xOffset = 0.33f * ((float) width);
        float yOffset = 0.5f * ((float) getHeight());
        this.startPointRect = new RectF(xOffset - 20.0f, yOffset - 20.0f, xOffset + 20.0f, yOffset + 20.0f);
        xOffset = 0.66f * ((float) width);
        this.endPointRect = new RectF(xOffset - 20.0f, yOffset - 20.0f, xOffset + 20.0f, yOffset + 20.0f);
        this.paint.setStrokeWidth(BaseField.BORDER_WIDTH_THICK);
        this.paint.setColor(-16776961);
        this.paint.setAntiAlias(true);
    }

    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.paint.setColor(-16776961);
        canvas.drawLine(this.startPointRect.centerX(), this.startPointRect.centerY(), this.endPointRect.centerX(), this.endPointRect.centerY(), this.paint);
        this.paint.setColor(-16711936);
        drawPointCross(canvas, this.startPointRect, this.paint);
        drawPointCross(canvas, this.endPointRect, this.paint);
    }

    private static void drawPointCross(Canvas canvas, RectF pointRect, Paint paint) {
        canvas.drawLine(pointRect.left, pointRect.top, pointRect.right, pointRect.bottom, paint);
        canvas.drawLine(pointRect.left, pointRect.bottom, pointRect.right, pointRect.top, paint);
    }

    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case 0:
                this.prevX = x;
                this.prevY = y;
                if (!this.startPointRect.contains(x, y)) {
                    if (!this.endPointRect.contains(x, y)) {
                        this.state = State.IDLE;
                        break;
                    }
                    this.state = State.DRAGGING_END_POINT;
                    break;
                }
                this.state = State.DRAGGING_START_POINT;
                break;
            case 1:
                if (!(this.state == State.IDLE || this.onDragListener == null)) {
                    this.onDragListener.onDragEnd(new PointF(this.startPointRect.centerX(), this.startPointRect.centerY()), new PointF(this.endPointRect.centerX(), this.endPointRect.centerY()), false);
                }
                this.state = State.IDLE;
                break;
            case 2:
                float diffX = x - this.prevX;
                float diffY = y - this.prevY;
                this.prevX = x;
                this.prevY = y;
                if (this.state != State.DRAGGING_START_POINT) {
                    if (this.state == State.DRAGGING_END_POINT) {
                        this.endPointRect.offset(diffX, diffY);
                        break;
                    }
                }
                this.startPointRect.offset(diffX, diffY);
                break;
                break;
            case 3:
                if (this.onDragListener != null) {
                    this.onDragListener.onDragEnd(new PointF(this.startPointRect.centerX(), this.startPointRect.centerY()), new PointF(this.endPointRect.centerX(), this.endPointRect.centerY()), true);
                }
                this.state = State.IDLE;
                break;
        }
        this.dirtyRectF.union(this.startPointRect);
        this.dirtyRectF.union(this.endPointRect);
        this.dirtyRectF.round(this.dirtyRect);
        invalidate(this.dirtyRect);
        return true;
    }
}
