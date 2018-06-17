//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package android.support.v4.animation;

import android.support.annotation.RestrictTo;
import android.support.annotation.RestrictTo.Scope;

@RestrictTo({Scope.LIBRARY_GROUP})
public interface AnimatorListenerCompat {
    void onAnimationStart(ValueAnimatorCompat var1);

    void onAnimationEnd(ValueAnimatorCompat var1);

    void onAnimationCancel(ValueAnimatorCompat var1);

    void onAnimationRepeat(ValueAnimatorCompat var1);
}
