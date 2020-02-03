//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package androidx.core.animation;

import androidx.annotation.RestrictTo;
import androidx.annotation.RestrictTo.Scope;
import android.view.View;

@RestrictTo({Scope.LIBRARY_GROUP})
public interface ValueAnimatorCompat {
    void setTarget(View var1);

    void addListener(AnimatorListenerCompat var1);

    void setDuration(long var1);

    void start();

    void cancel();

    void addUpdateListener(AnimatorUpdateListenerCompat var1);

    float getAnimatedFraction();
}
