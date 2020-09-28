//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package androidx.core.animation;

import androidx.annotation.RestrictTo;
import androidx.annotation.RestrictTo.Scope;

@RestrictTo({Scope.LIBRARY_GROUP})
public interface AnimatorListenerCompat {
    void onAnimationStart(ValueAnimatorCompat var1);

    void onAnimationEnd(ValueAnimatorCompat var1);

    void onAnimationCancel(ValueAnimatorCompat var1);

    void onAnimationRepeat(ValueAnimatorCompat var1);
}
