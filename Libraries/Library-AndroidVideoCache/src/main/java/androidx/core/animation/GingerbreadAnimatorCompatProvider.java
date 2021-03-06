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

import android.annotation.TargetApi;
import androidx.annotation.RequiresApi;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

@RequiresApi(9)
@TargetApi(9)
class GingerbreadAnimatorCompatProvider implements AnimatorProvider {
    GingerbreadAnimatorCompatProvider() {
    }

    @Override
    public ValueAnimatorCompat emptyValueAnimator() {
        return new GingerbreadFloatValueAnimator();
    }

    @Override
    public void clearInterpolator(View view) {
    }

    private static class GingerbreadFloatValueAnimator implements ValueAnimatorCompat {
        List<AnimatorListenerCompat> mListeners = new ArrayList();
        List<AnimatorUpdateListenerCompat> mUpdateListeners = new ArrayList();
        View mTarget;
        private long mStartTime;
        private long mDuration = 200L;
        private float mFraction = 0.0F;
        private boolean mStarted = false;
        private boolean mEnded = false;
        private Runnable mLoopRunnable = new Runnable() {
            @Override
            public void run() {
                long dt =
                        GingerbreadFloatValueAnimator.this.getTime() - GingerbreadFloatValueAnimator.this.mStartTime;
                float fraction =
                        (float) dt * 1.0F / (float) GingerbreadFloatValueAnimator.this.mDuration;
                if (fraction > 1.0F || GingerbreadFloatValueAnimator.this.mTarget.getParent() == null) {
                    fraction = 1.0F;
                }

                GingerbreadFloatValueAnimator.this.mFraction = fraction;
                GingerbreadFloatValueAnimator.this.notifyUpdateListeners();
                if (GingerbreadFloatValueAnimator.this.mFraction >= 1.0F) {
                    GingerbreadFloatValueAnimator.this.dispatchEnd();
                } else {
                    GingerbreadFloatValueAnimator.this.mTarget.postDelayed(GingerbreadFloatValueAnimator.this.mLoopRunnable, 16L);
                }
            }
        };

        public GingerbreadFloatValueAnimator() {
        }

        private void notifyUpdateListeners() {
            for (int i = this.mUpdateListeners.size() - 1; i >= 0; --i) {
                ((AnimatorUpdateListenerCompat) this.mUpdateListeners.get(i)).onAnimationUpdate(this);
            }
        }

        @Override
        public void setTarget(View view) {
            this.mTarget = view;
        }

        @Override
        public void addListener(AnimatorListenerCompat listener) {
            this.mListeners.add(listener);
        }

        @Override
        public void setDuration(long duration) {
            if (!this.mStarted) {
                this.mDuration = duration;
            }
        }

        @Override
        public void start() {
            if (!this.mStarted) {
                this.mStarted = true;
                this.dispatchStart();
                this.mFraction = 0.0F;
                this.mStartTime = this.getTime();
                this.mTarget.postDelayed(this.mLoopRunnable, 16L);
            }
        }

        private long getTime() {
            return this.mTarget.getDrawingTime();
        }

        private void dispatchStart() {
            for (int i = this.mListeners.size() - 1; i >= 0; --i) {
                ((AnimatorListenerCompat) this.mListeners.get(i)).onAnimationStart(this);
            }
        }

        private void dispatchEnd() {
            for (int i = this.mListeners.size() - 1; i >= 0; --i) {
                ((AnimatorListenerCompat) this.mListeners.get(i)).onAnimationEnd(this);
            }
        }

        private void dispatchCancel() {
            for (int i = this.mListeners.size() - 1; i >= 0; --i) {
                ((AnimatorListenerCompat) this.mListeners.get(i)).onAnimationCancel(this);
            }
        }

        @Override
        public void cancel() {
            if (!this.mEnded) {
                this.mEnded = true;
                if (this.mStarted) {
                    this.dispatchCancel();
                }

                this.dispatchEnd();
            }
        }

        @Override
        public void addUpdateListener(AnimatorUpdateListenerCompat animatorUpdateListener) {
            this.mUpdateListeners.add(animatorUpdateListener);
        }

        @Override
        public float getAnimatedFraction() {
            return this.mFraction;
        }
    }
}
