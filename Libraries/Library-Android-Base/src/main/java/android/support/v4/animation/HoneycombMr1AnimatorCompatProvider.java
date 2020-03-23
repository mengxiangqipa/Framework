//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package android.support.v4.animation;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.annotation.TargetApi;
import android.support.annotation.RequiresApi;
import android.view.View;

@RequiresApi(12)
@TargetApi(12)
class HoneycombMr1AnimatorCompatProvider implements AnimatorProvider {
    private TimeInterpolator mDefaultInterpolator;

    HoneycombMr1AnimatorCompatProvider() {
    }

    public ValueAnimatorCompat emptyValueAnimator() {
        return new HoneycombValueAnimatorCompat(ValueAnimator.ofFloat(new float[]{0.0F, 1.0F}));
    }

    public void clearInterpolator(View view) {
        if (this.mDefaultInterpolator == null) {
            this.mDefaultInterpolator = (new ValueAnimator()).getInterpolator();
        }

        view.animate().setInterpolator(this.mDefaultInterpolator);
    }

    static class AnimatorListenerCompatWrapper implements AnimatorListener {
        final AnimatorListenerCompat mWrapped;
        final ValueAnimatorCompat mValueAnimatorCompat;

        public AnimatorListenerCompatWrapper(AnimatorListenerCompat wrapped,
                                             ValueAnimatorCompat valueAnimatorCompat) {
            this.mWrapped = wrapped;
            this.mValueAnimatorCompat = valueAnimatorCompat;
        }

        public void onAnimationStart(Animator animation) {
            this.mWrapped.onAnimationStart(this.mValueAnimatorCompat);
        }

        public void onAnimationEnd(Animator animation) {
            this.mWrapped.onAnimationEnd(this.mValueAnimatorCompat);
        }

        public void onAnimationCancel(Animator animation) {
            this.mWrapped.onAnimationCancel(this.mValueAnimatorCompat);
        }

        public void onAnimationRepeat(Animator animation) {
            this.mWrapped.onAnimationRepeat(this.mValueAnimatorCompat);
        }
    }

    static class HoneycombValueAnimatorCompat implements ValueAnimatorCompat {
        final Animator mWrapped;

        public HoneycombValueAnimatorCompat(Animator wrapped) {
            this.mWrapped = wrapped;
        }

        public void setTarget(View view) {
            this.mWrapped.setTarget(view);
        }

        public void addListener(AnimatorListenerCompat listener) {
            this.mWrapped.addListener(new AnimatorListenerCompatWrapper(listener, this));
        }

        public void setDuration(long duration) {
            this.mWrapped.setDuration(duration);
        }

        public void start() {
            this.mWrapped.start();
        }

        public void cancel() {
            this.mWrapped.cancel();
        }

        public void addUpdateListener(final AnimatorUpdateListenerCompat animatorUpdateListener) {
            if (this.mWrapped instanceof ValueAnimator) {
                ((ValueAnimator) this.mWrapped).addUpdateListener(new AnimatorUpdateListener() {
                    public void onAnimationUpdate(ValueAnimator animation) {
                        animatorUpdateListener.onAnimationUpdate(HoneycombValueAnimatorCompat.this);
                    }
                });
            }
        }

        public float getAnimatedFraction() {
            return ((ValueAnimator) this.mWrapped).getAnimatedFraction();
        }
    }
}
