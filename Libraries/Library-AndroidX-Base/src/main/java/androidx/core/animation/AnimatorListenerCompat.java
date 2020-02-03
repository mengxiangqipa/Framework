//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package androidx.core.animation;

import androidx.annotation.RestrictTo;
import androidx.annotation.RestrictTo.Scope;

@RestrictTo({Scope.LIBRARY_GROUP})
public interface AnimatorListenerCompat {
    void onAnimationStart(androidx.core.animation.ValueAnimatorCompat var1);

    void onAnimationEnd(androidx.core.animation.ValueAnimatorCompat var1);

    void onAnimationCancel(androidx.core.animation.ValueAnimatorCompat var1);

    void onAnimationRepeat(androidx.core.animation.ValueAnimatorCompat var1);
}
