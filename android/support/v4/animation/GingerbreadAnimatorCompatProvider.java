package android.support.v4.animation;

import android.annotation.TargetApi;
import android.support.annotation.RequiresApi;
import android.view.View;
import com.itextpdf.text.pdf.BaseField;
import java.util.ArrayList;
import java.util.List;

@TargetApi(9)
@RequiresApi(9)
class GingerbreadAnimatorCompatProvider implements AnimatorProvider {

    private static class GingerbreadFloatValueAnimator implements ValueAnimatorCompat {
        private long mDuration = 200;
        private boolean mEnded = false;
        private float mFraction = 0.0f;
        List<AnimatorListenerCompat> mListeners = new ArrayList();
        private Runnable mLoopRunnable = new C00941();
        private long mStartTime;
        private boolean mStarted = false;
        View mTarget;
        List<AnimatorUpdateListenerCompat> mUpdateListeners = new ArrayList();

        class C00941 implements Runnable {
            C00941() {
            }

            public void run() {
                float fraction = (((float) (GingerbreadFloatValueAnimator.this.getTime() - GingerbreadFloatValueAnimator.this.mStartTime)) * BaseField.BORDER_WIDTH_THIN) / ((float) GingerbreadFloatValueAnimator.this.mDuration);
                if (fraction > BaseField.BORDER_WIDTH_THIN || GingerbreadFloatValueAnimator.this.mTarget.getParent() == null) {
                    fraction = BaseField.BORDER_WIDTH_THIN;
                }
                GingerbreadFloatValueAnimator.this.mFraction = fraction;
                GingerbreadFloatValueAnimator.this.notifyUpdateListeners();
                if (GingerbreadFloatValueAnimator.this.mFraction >= BaseField.BORDER_WIDTH_THIN) {
                    GingerbreadFloatValueAnimator.this.dispatchEnd();
                } else {
                    GingerbreadFloatValueAnimator.this.mTarget.postDelayed(GingerbreadFloatValueAnimator.this.mLoopRunnable, 16);
                }
            }
        }

        private void notifyUpdateListeners() {
            for (int i = this.mUpdateListeners.size() - 1; i >= 0; i--) {
                ((AnimatorUpdateListenerCompat) this.mUpdateListeners.get(i)).onAnimationUpdate(this);
            }
        }

        public void setTarget(View view) {
            this.mTarget = view;
        }

        public void addListener(AnimatorListenerCompat listener) {
            this.mListeners.add(listener);
        }

        public void setDuration(long duration) {
            if (!this.mStarted) {
                this.mDuration = duration;
            }
        }

        public void start() {
            if (!this.mStarted) {
                this.mStarted = true;
                dispatchStart();
                this.mFraction = 0.0f;
                this.mStartTime = getTime();
                this.mTarget.postDelayed(this.mLoopRunnable, 16);
            }
        }

        private long getTime() {
            return this.mTarget.getDrawingTime();
        }

        private void dispatchStart() {
            for (int i = this.mListeners.size() - 1; i >= 0; i--) {
                ((AnimatorListenerCompat) this.mListeners.get(i)).onAnimationStart(this);
            }
        }

        private void dispatchEnd() {
            for (int i = this.mListeners.size() - 1; i >= 0; i--) {
                ((AnimatorListenerCompat) this.mListeners.get(i)).onAnimationEnd(this);
            }
        }

        private void dispatchCancel() {
            for (int i = this.mListeners.size() - 1; i >= 0; i--) {
                ((AnimatorListenerCompat) this.mListeners.get(i)).onAnimationCancel(this);
            }
        }

        public void cancel() {
            if (!this.mEnded) {
                this.mEnded = true;
                if (this.mStarted) {
                    dispatchCancel();
                }
                dispatchEnd();
            }
        }

        public void addUpdateListener(AnimatorUpdateListenerCompat animatorUpdateListener) {
            this.mUpdateListeners.add(animatorUpdateListener);
        }

        public float getAnimatedFraction() {
            return this.mFraction;
        }
    }

    GingerbreadAnimatorCompatProvider() {
    }

    public ValueAnimatorCompat emptyValueAnimator() {
        return new GingerbreadFloatValueAnimator();
    }

    public void clearInterpolator(View view) {
    }
}
