package android.support.percent;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.v4.view.MarginLayoutParamsCompat;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;

public class PercentLayoutHelper {
    private static final boolean DEBUG = false;
    private static final String TAG = "PercentLayout";
    private static final boolean VERBOSE = false;
    private final ViewGroup mHost;

    public interface PercentLayoutParams {
        PercentLayoutInfo getPercentLayoutInfo();
    }

    public static class PercentLayoutInfo {
        public float aspectRatio;
        public float bottomMarginPercent = -1.0f;
        public float endMarginPercent = -1.0f;
        public float heightPercent = -1.0f;
        public float leftMarginPercent = -1.0f;
        final PercentMarginLayoutParams mPreservedParams = new PercentMarginLayoutParams(0, 0);
        public float rightMarginPercent = -1.0f;
        public float startMarginPercent = -1.0f;
        public float topMarginPercent = -1.0f;
        public float widthPercent = -1.0f;

        public void fillLayoutParams(LayoutParams params, int widthHint, int heightHint) {
            boolean widthNotSet;
            boolean heightNotSet;
            this.mPreservedParams.width = params.width;
            this.mPreservedParams.height = params.height;
            if ((this.mPreservedParams.mIsWidthComputedFromAspectRatio || this.mPreservedParams.width == 0) && this.widthPercent < 0.0f) {
                widthNotSet = true;
            } else {
                widthNotSet = false;
            }
            if ((this.mPreservedParams.mIsHeightComputedFromAspectRatio || this.mPreservedParams.height == 0) && this.heightPercent < 0.0f) {
                heightNotSet = true;
            } else {
                heightNotSet = false;
            }
            if (this.widthPercent >= 0.0f) {
                params.width = Math.round(((float) widthHint) * this.widthPercent);
            }
            if (this.heightPercent >= 0.0f) {
                params.height = Math.round(((float) heightHint) * this.heightPercent);
            }
            if (this.aspectRatio >= 0.0f) {
                if (widthNotSet) {
                    params.width = Math.round(((float) params.height) * this.aspectRatio);
                    this.mPreservedParams.mIsWidthComputedFromAspectRatio = true;
                }
                if (heightNotSet) {
                    params.height = Math.round(((float) params.width) / this.aspectRatio);
                    this.mPreservedParams.mIsHeightComputedFromAspectRatio = true;
                }
            }
        }

        @Deprecated
        public void fillMarginLayoutParams(MarginLayoutParams params, int widthHint, int heightHint) {
            fillMarginLayoutParams(null, params, widthHint, heightHint);
        }

        public void fillMarginLayoutParams(View view, MarginLayoutParams params, int widthHint, int heightHint) {
            fillLayoutParams(params, widthHint, heightHint);
            this.mPreservedParams.leftMargin = params.leftMargin;
            this.mPreservedParams.topMargin = params.topMargin;
            this.mPreservedParams.rightMargin = params.rightMargin;
            this.mPreservedParams.bottomMargin = params.bottomMargin;
            MarginLayoutParamsCompat.setMarginStart(this.mPreservedParams, MarginLayoutParamsCompat.getMarginStart(params));
            MarginLayoutParamsCompat.setMarginEnd(this.mPreservedParams, MarginLayoutParamsCompat.getMarginEnd(params));
            if (this.leftMarginPercent >= 0.0f) {
                params.leftMargin = Math.round(((float) widthHint) * this.leftMarginPercent);
            }
            if (this.topMarginPercent >= 0.0f) {
                params.topMargin = Math.round(((float) heightHint) * this.topMarginPercent);
            }
            if (this.rightMarginPercent >= 0.0f) {
                params.rightMargin = Math.round(((float) widthHint) * this.rightMarginPercent);
            }
            if (this.bottomMarginPercent >= 0.0f) {
                params.bottomMargin = Math.round(((float) heightHint) * this.bottomMarginPercent);
            }
            boolean shouldResolveLayoutDirection = false;
            if (this.startMarginPercent >= 0.0f) {
                MarginLayoutParamsCompat.setMarginStart(params, Math.round(((float) widthHint) * this.startMarginPercent));
                shouldResolveLayoutDirection = true;
            }
            if (this.endMarginPercent >= 0.0f) {
                MarginLayoutParamsCompat.setMarginEnd(params, Math.round(((float) widthHint) * this.endMarginPercent));
                shouldResolveLayoutDirection = true;
            }
            if (shouldResolveLayoutDirection && view != null) {
                MarginLayoutParamsCompat.resolveLayoutDirection(params, ViewCompat.getLayoutDirection(view));
            }
        }

        public String toString() {
            return String.format("PercentLayoutInformation width: %f height %f, margins (%f, %f,  %f, %f, %f, %f)", new Object[]{Float.valueOf(this.widthPercent), Float.valueOf(this.heightPercent), Float.valueOf(this.leftMarginPercent), Float.valueOf(this.topMarginPercent), Float.valueOf(this.rightMarginPercent), Float.valueOf(this.bottomMarginPercent), Float.valueOf(this.startMarginPercent), Float.valueOf(this.endMarginPercent)});
        }

        public void restoreMarginLayoutParams(MarginLayoutParams params) {
            restoreLayoutParams(params);
            params.leftMargin = this.mPreservedParams.leftMargin;
            params.topMargin = this.mPreservedParams.topMargin;
            params.rightMargin = this.mPreservedParams.rightMargin;
            params.bottomMargin = this.mPreservedParams.bottomMargin;
            MarginLayoutParamsCompat.setMarginStart(params, MarginLayoutParamsCompat.getMarginStart(this.mPreservedParams));
            MarginLayoutParamsCompat.setMarginEnd(params, MarginLayoutParamsCompat.getMarginEnd(this.mPreservedParams));
        }

        public void restoreLayoutParams(LayoutParams params) {
            if (!this.mPreservedParams.mIsWidthComputedFromAspectRatio) {
                params.width = this.mPreservedParams.width;
            }
            if (!this.mPreservedParams.mIsHeightComputedFromAspectRatio) {
                params.height = this.mPreservedParams.height;
            }
            this.mPreservedParams.mIsWidthComputedFromAspectRatio = false;
            this.mPreservedParams.mIsHeightComputedFromAspectRatio = false;
        }
    }

    static class PercentMarginLayoutParams extends MarginLayoutParams {
        private boolean mIsHeightComputedFromAspectRatio;
        private boolean mIsWidthComputedFromAspectRatio;

        public PercentMarginLayoutParams(int width, int height) {
            super(width, height);
        }
    }

    public PercentLayoutHelper(@NonNull ViewGroup host) {
        if (host == null) {
            throw new IllegalArgumentException("host must be non-null");
        }
        this.mHost = host;
    }

    public static void fetchWidthAndHeight(LayoutParams params, TypedArray array, int widthAttr, int heightAttr) {
        params.width = array.getLayoutDimension(widthAttr, 0);
        params.height = array.getLayoutDimension(heightAttr, 0);
    }

    public void adjustChildren(int widthMeasureSpec, int heightMeasureSpec) {
        int widthHint = (MeasureSpec.getSize(widthMeasureSpec) - this.mHost.getPaddingLeft()) - this.mHost.getPaddingRight();
        int heightHint = (MeasureSpec.getSize(heightMeasureSpec) - this.mHost.getPaddingTop()) - this.mHost.getPaddingBottom();
        int N = this.mHost.getChildCount();
        for (int i = 0; i < N; i++) {
            View view = this.mHost.getChildAt(i);
            LayoutParams params = view.getLayoutParams();
            if (params instanceof PercentLayoutParams) {
                PercentLayoutInfo info = ((PercentLayoutParams) params).getPercentLayoutInfo();
                if (info != null) {
                    if (params instanceof MarginLayoutParams) {
                        info.fillMarginLayoutParams(view, (MarginLayoutParams) params, widthHint, heightHint);
                    } else {
                        info.fillLayoutParams(params, widthHint, heightHint);
                    }
                }
            }
        }
    }

    public static PercentLayoutInfo getPercentLayoutInfo(Context context, AttributeSet attrs) {
        PercentLayoutInfo info = null;
        TypedArray array = context.obtainStyledAttributes(attrs, C0080R.styleable.PercentLayout_Layout);
        float value = array.getFraction(C0080R.styleable.PercentLayout_Layout_layout_widthPercent, 1, 1, -1.0f);
        if (value != -1.0f) {
            if (null == null) {
                info = new PercentLayoutInfo();
            }
            info.widthPercent = value;
        }
        value = array.getFraction(C0080R.styleable.PercentLayout_Layout_layout_heightPercent, 1, 1, -1.0f);
        if (value != -1.0f) {
            if (info == null) {
                info = new PercentLayoutInfo();
            }
            info.heightPercent = value;
        }
        value = array.getFraction(C0080R.styleable.PercentLayout_Layout_layout_marginPercent, 1, 1, -1.0f);
        if (value != -1.0f) {
            if (info == null) {
                info = new PercentLayoutInfo();
            }
            info.leftMarginPercent = value;
            info.topMarginPercent = value;
            info.rightMarginPercent = value;
            info.bottomMarginPercent = value;
        }
        value = array.getFraction(C0080R.styleable.PercentLayout_Layout_layout_marginLeftPercent, 1, 1, -1.0f);
        if (value != -1.0f) {
            if (info == null) {
                info = new PercentLayoutInfo();
            }
            info.leftMarginPercent = value;
        }
        value = array.getFraction(C0080R.styleable.PercentLayout_Layout_layout_marginTopPercent, 1, 1, -1.0f);
        if (value != -1.0f) {
            if (info == null) {
                info = new PercentLayoutInfo();
            }
            info.topMarginPercent = value;
        }
        value = array.getFraction(C0080R.styleable.PercentLayout_Layout_layout_marginRightPercent, 1, 1, -1.0f);
        if (value != -1.0f) {
            if (info == null) {
                info = new PercentLayoutInfo();
            }
            info.rightMarginPercent = value;
        }
        value = array.getFraction(C0080R.styleable.PercentLayout_Layout_layout_marginBottomPercent, 1, 1, -1.0f);
        if (value != -1.0f) {
            if (info == null) {
                info = new PercentLayoutInfo();
            }
            info.bottomMarginPercent = value;
        }
        value = array.getFraction(C0080R.styleable.PercentLayout_Layout_layout_marginStartPercent, 1, 1, -1.0f);
        if (value != -1.0f) {
            if (info == null) {
                info = new PercentLayoutInfo();
            }
            info.startMarginPercent = value;
        }
        value = array.getFraction(C0080R.styleable.PercentLayout_Layout_layout_marginEndPercent, 1, 1, -1.0f);
        if (value != -1.0f) {
            if (info == null) {
                info = new PercentLayoutInfo();
            }
            info.endMarginPercent = value;
        }
        value = array.getFraction(C0080R.styleable.PercentLayout_Layout_layout_aspectRatio, 1, 1, -1.0f);
        if (value != -1.0f) {
            if (info == null) {
                info = new PercentLayoutInfo();
            }
            info.aspectRatio = value;
        }
        array.recycle();
        return info;
    }

    public void restoreOriginalParams() {
        int N = this.mHost.getChildCount();
        for (int i = 0; i < N; i++) {
            LayoutParams params = this.mHost.getChildAt(i).getLayoutParams();
            if (params instanceof PercentLayoutParams) {
                PercentLayoutInfo info = ((PercentLayoutParams) params).getPercentLayoutInfo();
                if (info != null) {
                    if (params instanceof MarginLayoutParams) {
                        info.restoreMarginLayoutParams((MarginLayoutParams) params);
                    } else {
                        info.restoreLayoutParams(params);
                    }
                }
            }
        }
    }

    public boolean handleMeasuredStateTooSmall() {
        boolean needsSecondMeasure = false;
        int N = this.mHost.getChildCount();
        for (int i = 0; i < N; i++) {
            View view = this.mHost.getChildAt(i);
            LayoutParams params = view.getLayoutParams();
            if (params instanceof PercentLayoutParams) {
                PercentLayoutInfo info = ((PercentLayoutParams) params).getPercentLayoutInfo();
                if (info != null) {
                    if (shouldHandleMeasuredWidthTooSmall(view, info)) {
                        needsSecondMeasure = true;
                        params.width = -2;
                    }
                    if (shouldHandleMeasuredHeightTooSmall(view, info)) {
                        needsSecondMeasure = true;
                        params.height = -2;
                    }
                }
            }
        }
        return needsSecondMeasure;
    }

    private static boolean shouldHandleMeasuredWidthTooSmall(View view, PercentLayoutInfo info) {
        return (ViewCompat.getMeasuredWidthAndState(view) & ViewCompat.MEASURED_STATE_MASK) == 16777216 && info.widthPercent >= 0.0f && info.mPreservedParams.width == -2;
    }

    private static boolean shouldHandleMeasuredHeightTooSmall(View view, PercentLayoutInfo info) {
        return (ViewCompat.getMeasuredHeightAndState(view) & ViewCompat.MEASURED_STATE_MASK) == 16777216 && info.heightPercent >= 0.0f && info.mPreservedParams.height == -2;
    }
}
