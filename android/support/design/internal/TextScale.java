package android.support.design.internal;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.annotation.TargetApi;
import android.support.annotation.RequiresApi;
import android.support.transition.Transition;
import android.support.transition.TransitionValues;
import android.view.ViewGroup;
import android.widget.TextView;
import com.itextpdf.text.pdf.BaseField;
import java.util.Map;

@TargetApi(14)
@RequiresApi(14)
public class TextScale extends Transition {
    private static final String PROPNAME_SCALE = "android:textscale:scale";

    public void captureStartValues(TransitionValues transitionValues) {
        captureValues(transitionValues);
    }

    public void captureEndValues(TransitionValues transitionValues) {
        captureValues(transitionValues);
    }

    private void captureValues(TransitionValues transitionValues) {
        if (transitionValues.view instanceof TextView) {
            transitionValues.values.put(PROPNAME_SCALE, Float.valueOf(transitionValues.view.getScaleX()));
        }
    }

    public Animator createAnimator(ViewGroup sceneRoot, TransitionValues startValues, TransitionValues endValues) {
        if (startValues == null || endValues == null || !(startValues.view instanceof TextView) || !(endValues.view instanceof TextView)) {
            return null;
        }
        final TextView view = endValues.view;
        Map<String, Object> startVals = startValues.values;
        Map<String, Object> endVals = endValues.values;
        if ((startVals.get(PROPNAME_SCALE) != null ? ((Float) startVals.get(PROPNAME_SCALE)).floatValue() : BaseField.BORDER_WIDTH_THIN) == (endVals.get(PROPNAME_SCALE) != null ? ((Float) endVals.get(PROPNAME_SCALE)).floatValue() : BaseField.BORDER_WIDTH_THIN)) {
            return null;
        }
        Animator animator = ValueAnimator.ofFloat(new float[]{startVals.get(PROPNAME_SCALE) != null ? ((Float) startVals.get(PROPNAME_SCALE)).floatValue() : BaseField.BORDER_WIDTH_THIN, endVals.get(PROPNAME_SCALE) != null ? ((Float) endVals.get(PROPNAME_SCALE)).floatValue() : BaseField.BORDER_WIDTH_THIN});
        animator.addUpdateListener(new AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float animatedValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
                view.setScaleX(animatedValue);
                view.setScaleY(animatedValue);
            }
        });
        return animator;
    }
}
