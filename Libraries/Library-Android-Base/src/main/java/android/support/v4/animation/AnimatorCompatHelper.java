//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package androidx.core.animation;

import android.os.Build.VERSION;
import androidx.annotation.RestrictTo;
import androidx.annotation.RestrictTo.Scope;
import android.view.View;

@RestrictTo({Scope.LIBRARY_GROUP})
public final class AnimatorCompatHelper {
    private static final AnimatorProvider IMPL;

    public static ValueAnimatorCompat emptyValueAnimator() {
        return IMPL.emptyValueAnimator();
    }

    private AnimatorCompatHelper() {
    }

    public static void clearInterpolator(View view) {
        IMPL.clearInterpolator(view);
    }

    static {
        if (VERSION.SDK_INT >= 12) {
            IMPL = new HoneycombMr1AnimatorCompatProvider();
        } else {
            IMPL = new GingerbreadAnimatorCompatProvider();
        }
    }
}
