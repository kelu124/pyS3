package com.lee.ultrascan.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import com.itextpdf.text.pdf.BaseField;
import com.lee.ultrascan.C0796R;

public class ColorPickerDialog extends AlertDialog {
    private static final int BORDER_COLOR = -16777216;
    private static final int BORDER_DP = 1;
    private static final int CONTROL_SPACING_DP = 20;
    private static final int PADDING_DP = 20;
    private static final int SELECTED_COLOR_HEIGHT_DP = 50;
    private OnClickListener clickListener = new C10153();
    private HSVColorWheel colorWheel;
    private final OnColorSelectedListener listener;
    private int selectedColor;
    private View selectedColorView;
    private HSVValueSlider valueSlider;

    public interface OnColorSelectedListener {
        void colorSelected(Integer num);
    }

    class C10131 implements OnColorSelectedListener {
        C10131() {
        }

        public void colorSelected(Integer color) {
            ColorPickerDialog.this.valueSlider.setColor(color.intValue(), true);
        }
    }

    class C10142 implements OnColorSelectedListener {
        C10142() {
        }

        public void colorSelected(Integer color) {
            ColorPickerDialog.this.selectedColor = color.intValue();
            ColorPickerDialog.this.selectedColorView.setBackgroundColor(color.intValue());
        }
    }

    class C10153 implements OnClickListener {
        C10153() {
        }

        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case -3:
                    dialog.dismiss();
                    ColorPickerDialog.this.listener.colorSelected(Integer.valueOf(-1));
                    return;
                case -2:
                    dialog.dismiss();
                    return;
                case -1:
                    ColorPickerDialog.this.listener.colorSelected(Integer.valueOf(ColorPickerDialog.this.selectedColor));
                    return;
                default:
                    return;
            }
        }
    }

    private static class HSVColorWheel extends View {
        private static final float FADE_OUT_FRACTION = 0.03f;
        private static final int POINTER_LENGTH_DP = 10;
        private static final int POINTER_LINE_WIDTH_DP = 2;
        private static final float SCALE = 2.0f;
        private Bitmap bitmap;
        float[] colorHsv = new float[]{0.0f, 0.0f, BaseField.BORDER_WIDTH_THIN};
        private final Context context;
        private float fullCircleRadius;
        private float innerCircleRadius;
        private int innerPadding;
        private OnColorSelectedListener listener;
        private int[] pixels;
        private int pointerLength;
        private Paint pointerPaint = new Paint();
        private Rect rect;
        private int scale;
        private float scaledFadeOutSize;
        private float scaledFullCircleRadius;
        private int scaledHeight;
        private float scaledInnerCircleRadius;
        private int[] scaledPixels;
        private int scaledWidth;
        private Point selectedPoint = new Point();

        public HSVColorWheel(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
            this.context = context;
            init();
        }

        public HSVColorWheel(Context context, AttributeSet attrs) {
            super(context, attrs);
            this.context = context;
            init();
        }

        public HSVColorWheel(Context context) {
            super(context);
            this.context = context;
            init();
        }

        private void init() {
            float density = this.context.getResources().getDisplayMetrics().density;
            this.scale = (int) (density * 2.0f);
            this.pointerLength = (int) (10.0f * density);
            this.pointerPaint.setStrokeWidth((float) ((int) (2.0f * density)));
            this.innerPadding = this.pointerLength / 2;
        }

        public void setListener(OnColorSelectedListener listener) {
            this.listener = listener;
        }

        public void setColor(int color) {
            Color.colorToHSV(color, this.colorHsv);
            invalidate();
        }

        protected void onDraw(Canvas canvas) {
            if (this.bitmap != null) {
                canvas.drawBitmap(this.bitmap, null, this.rect, null);
                float hueInPiInterval = (this.colorHsv[0] / 180.0f) * 3.1415927f;
                this.selectedPoint.x = this.rect.left + ((int) ((((-Math.cos((double) hueInPiInterval)) * ((double) this.colorHsv[1])) * ((double) this.innerCircleRadius)) + ((double) this.fullCircleRadius)));
                this.selectedPoint.y = this.rect.top + ((int) ((((-Math.sin((double) hueInPiInterval)) * ((double) this.colorHsv[1])) * ((double) this.innerCircleRadius)) + ((double) this.fullCircleRadius)));
                canvas.drawLine((float) (this.selectedPoint.x - this.pointerLength), (float) this.selectedPoint.y, (float) (this.selectedPoint.x + this.pointerLength), (float) this.selectedPoint.y, this.pointerPaint);
                canvas.drawLine((float) this.selectedPoint.x, (float) (this.selectedPoint.y - this.pointerLength), (float) this.selectedPoint.x, (float) (this.selectedPoint.y + this.pointerLength), this.pointerPaint);
            }
        }

        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);
            this.rect = new Rect(this.innerPadding, this.innerPadding, w - this.innerPadding, h - this.innerPadding);
            this.bitmap = Bitmap.createBitmap(w - (this.innerPadding * 2), h - (this.innerPadding * 2), Config.ARGB_8888);
            this.fullCircleRadius = (float) (Math.min(this.rect.width(), this.rect.height()) / 2);
            this.innerCircleRadius = this.fullCircleRadius * 0.97f;
            this.scaledWidth = this.rect.width() / this.scale;
            this.scaledHeight = this.rect.height() / this.scale;
            this.scaledFullCircleRadius = (float) (Math.min(this.scaledWidth, this.scaledHeight) / 2);
            this.scaledInnerCircleRadius = this.scaledFullCircleRadius * 0.97f;
            this.scaledFadeOutSize = this.scaledFullCircleRadius - this.scaledInnerCircleRadius;
            this.scaledPixels = new int[(this.scaledWidth * this.scaledHeight)];
            this.pixels = new int[(this.rect.width() * this.rect.height())];
            createBitmap();
        }

        private void createBitmap() {
            int w = this.rect.width();
            int h = this.rect.height();
            float[] hsv = new float[]{0.0f, 0.0f, BaseField.BORDER_WIDTH_THIN};
            int x = (int) (-this.scaledFullCircleRadius);
            int y = (int) (-this.scaledFullCircleRadius);
            for (int i = 0; i < this.scaledPixels.length; i++) {
                if (i % this.scaledWidth == 0) {
                    x = (int) (-this.scaledFullCircleRadius);
                    y++;
                } else {
                    x++;
                }
                double centerDist = Math.sqrt((double) ((x * x) + (y * y)));
                if (centerDist <= ((double) this.scaledFullCircleRadius)) {
                    int alpha;
                    hsv[0] = ((float) ((Math.atan2((double) y, (double) x) / 3.141592653589793d) * 180.0d)) + 180.0f;
                    hsv[1] = (float) (centerDist / ((double) this.scaledInnerCircleRadius));
                    if (centerDist <= ((double) this.scaledInnerCircleRadius)) {
                        alpha = 255;
                    } else {
                        alpha = 255 - ((int) (((centerDist - ((double) this.scaledInnerCircleRadius)) / ((double) this.scaledFadeOutSize)) * 255.0d));
                    }
                    this.scaledPixels[i] = Color.HSVToColor(alpha, hsv);
                } else {
                    this.scaledPixels[i] = 0;
                }
            }
            for (x = 0; x < w; x++) {
                int scaledX = x / this.scale;
                if (scaledX >= this.scaledWidth) {
                    scaledX = this.scaledWidth - 1;
                }
                for (y = 0; y < h; y++) {
                    int scaledY = y / this.scale;
                    if (scaledY >= this.scaledHeight) {
                        scaledY = this.scaledHeight - 1;
                    }
                    this.pixels[(x * h) + y] = this.scaledPixels[(this.scaledHeight * scaledX) + scaledY];
                }
            }
            this.bitmap.setPixels(this.pixels, 0, w, 0, 0, w, h);
            invalidate();
        }

        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            int height = Math.min(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec));
            setMeasuredDimension(height, height);
        }

        public int getColorForPoint(int x, int y, float[] hsv) {
            x = (int) (((float) x) - this.fullCircleRadius);
            y = (int) (((float) y) - this.fullCircleRadius);
            double centerDist = Math.sqrt((double) ((x * x) + (y * y)));
            hsv[0] = ((float) ((Math.atan2((double) y, (double) x) / 3.141592653589793d) * 180.0d)) + 180.0f;
            hsv[1] = Math.max(0.0f, Math.min(BaseField.BORDER_WIDTH_THIN, (float) (centerDist / ((double) this.innerCircleRadius))));
            return Color.HSVToColor(hsv);
        }

        public boolean onTouchEvent(MotionEvent event) {
            switch (event.getActionMasked()) {
                case 0:
                case 2:
                    if (this.listener != null) {
                        this.listener.colorSelected(Integer.valueOf(getColorForPoint((int) event.getX(), (int) event.getY(), this.colorHsv)));
                    }
                    invalidate();
                    return true;
                default:
                    return super.onTouchEvent(event);
            }
        }
    }

    private static class HSVValueSlider extends View {
        private Bitmap bitmap;
        float[] colorHsv = new float[]{0.0f, 0.0f, BaseField.BORDER_WIDTH_THIN};
        private Rect dstRect;
        private OnColorSelectedListener listener;
        private int[] pixels;
        private Rect srcRect;

        public HSVValueSlider(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
        }

        public HSVValueSlider(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public HSVValueSlider(Context context) {
            super(context);
        }

        public void setListener(OnColorSelectedListener listener) {
            this.listener = listener;
        }

        public void setColor(int color, boolean keepValue) {
            float oldValue = this.colorHsv[2];
            Color.colorToHSV(color, this.colorHsv);
            if (keepValue) {
                this.colorHsv[2] = oldValue;
            }
            if (this.listener != null) {
                this.listener.colorSelected(Integer.valueOf(Color.HSVToColor(this.colorHsv)));
            }
            createBitmap();
        }

        protected void onDraw(Canvas canvas) {
            if (this.bitmap != null) {
                canvas.drawBitmap(this.bitmap, this.srcRect, this.dstRect, null);
            }
        }

        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);
            this.srcRect = new Rect(0, 0, w, 1);
            this.dstRect = new Rect(0, 0, w, h);
            this.bitmap = Bitmap.createBitmap(w, 1, Config.ARGB_8888);
            this.pixels = new int[w];
            createBitmap();
        }

        private void createBitmap() {
            if (this.bitmap != null) {
                int w = getWidth();
                float[] hsv = new float[]{this.colorHsv[0], this.colorHsv[1], BaseField.BORDER_WIDTH_THIN};
                int selectedX = (int) (this.colorHsv[2] * ((float) w));
                float value = 0.0f;
                float valueStep = BaseField.BORDER_WIDTH_THIN / ((float) w);
                int x = 0;
                while (x < w) {
                    value += valueStep;
                    if (x < selectedX - 1 || x > selectedX + 1) {
                        hsv[2] = value;
                        this.pixels[x] = Color.HSVToColor(hsv);
                    } else {
                        this.pixels[x] = (65793 * (255 - ((int) (255.0f * value)))) - 16777216;
                    }
                    x++;
                }
                this.bitmap.setPixels(this.pixels, 0, w, 0, 0, w, 1);
                invalidate();
            }
        }

        public boolean onTouchEvent(MotionEvent event) {
            switch (event.getActionMasked()) {
                case 0:
                case 2:
                    float value = ((float) Math.max(0, Math.min(this.bitmap.getWidth() - 1, (int) event.getX()))) / ((float) this.bitmap.getWidth());
                    if (this.colorHsv[2] != value) {
                        this.colorHsv[2] = value;
                        if (this.listener != null) {
                            this.listener.colorSelected(Integer.valueOf(Color.HSVToColor(this.colorHsv)));
                        }
                        createBitmap();
                        invalidate();
                    }
                    return true;
                default:
                    return super.onTouchEvent(event);
            }
        }
    }

    public ColorPickerDialog(Context context, int initialColor, OnColorSelectedListener listener) {
        super(context);
        this.selectedColor = initialColor;
        this.listener = listener;
        this.colorWheel = new HSVColorWheel(context);
        this.valueSlider = new HSVValueSlider(context);
        int padding = (int) (context.getResources().getDisplayMetrics().density * 20.0f);
        int borderSize = (int) (context.getResources().getDisplayMetrics().density * BaseField.BORDER_WIDTH_THIN);
        RelativeLayout layout = new RelativeLayout(context);
        LayoutParams lp = new LayoutParams(-1, -1);
        lp.addRule(14, -1);
        lp.bottomMargin = (int) (context.getResources().getDisplayMetrics().density * 20.0f);
        this.colorWheel.setListener(new C10131());
        this.colorWheel.setColor(initialColor);
        this.colorWheel.setId(C0796R.id.colorpicker_colorwheel_Id);
        layout.addView(this.colorWheel, lp);
        int selectedColorHeight = (int) (context.getResources().getDisplayMetrics().density * 50.0f);
        FrameLayout valueSliderBorder = new FrameLayout(context);
        valueSliderBorder.setBackgroundColor(-16777216);
        valueSliderBorder.setPadding(borderSize, borderSize, borderSize, borderSize);
        valueSliderBorder.setId(C0796R.id.colorpicker_siderborder_Id);
        lp = new LayoutParams(-1, (borderSize * 2) + selectedColorHeight);
        lp.bottomMargin = (int) (context.getResources().getDisplayMetrics().density * 20.0f);
        lp.addRule(3, C0796R.id.colorpicker_colorwheel_Id);
        layout.addView(valueSliderBorder, lp);
        this.valueSlider.setColor(initialColor, false);
        this.valueSlider.setListener(new C10142());
        valueSliderBorder.addView(this.valueSlider);
        FrameLayout selectedColorborder = new FrameLayout(context);
        selectedColorborder.setBackgroundColor(-16777216);
        lp = new LayoutParams(-1, (borderSize * 2) + selectedColorHeight);
        selectedColorborder.setPadding(borderSize, borderSize, borderSize, borderSize);
        lp.addRule(3, C0796R.id.colorpicker_siderborder_Id);
        layout.addView(selectedColorborder, lp);
        this.selectedColorView = new View(context);
        this.selectedColorView.setBackgroundColor(this.selectedColor);
        selectedColorborder.addView(this.selectedColorView);
        setButton(-2, context.getString(17039360), this.clickListener);
        setButton(-1, context.getString(17039370), this.clickListener);
        setButton(-3, context.getString(C0796R.string.dialog_colorpicker_default), this.clickListener);
        setView(layout, padding, padding, padding, padding);
    }

    public void setNoColorButton(int res) {
        setButton(-3, getContext().getString(res), this.clickListener);
    }
}
