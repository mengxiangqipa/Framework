/*
 *  Copyright (c) 2020 YobertJomi
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package androidx.core.animation;

import android.view.View;

import androidx.annotation.RestrictTo;
import androidx.annotation.RestrictTo.Scope;

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
