/*
 * Copyright 2016 bingoogolapple
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.library.swipefinish;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.view.View;

import java.util.List;

import androidx.annotation.DrawableRes;
import androidx.annotation.FloatRange;

/**
 * @author YobertJomi
 * className SwipeFinishHelper
 * created at  2020/10/23  17:36
 */
public class SwipeFinishHelper {
    private Activity mActivity;
    private Delegate mDelegate;
    private SwipeFinishLayout mSwipeFinishLayout;

    /**
     * 必须在 Application 的 onCreate 方法中调用
     *
     * @param application          应用程序上下文
     * @param problemViewClassList 如果发现滑动返回后立即触摸界面时应用崩溃，
     *                             请把该界面里比较特殊的 View 的 class 添加到该集合中，
     *                             目前在库中已经添加了 WebView 和 SurfaceView
     */
    public static void init(Application application,
                            List<Class<? extends View>> problemViewClassList) {
        SwipeFinishManager.getInstance().init(application, problemViewClassList);
    }

    /**
     * @param activity
     * @param delegate
     */
    public SwipeFinishHelper(Activity activity, Delegate delegate) {
        mActivity = activity;
        mDelegate = delegate;

        initSwipeFinishFinish();
    }

    /**
     * 初始化滑动返回
     */
    private void initSwipeFinishFinish() {
        if (mDelegate.isSupportSwipeFinish()) {
            mSwipeFinishLayout = new SwipeFinishLayout(mActivity);
            mSwipeFinishLayout.attachToActivity(mActivity);
            mSwipeFinishLayout.setPanelSlideListener(new SwipeFinishLayout.PanelSlideListener() {
                @Override
                public void onPanelSlide(View panel, float slideOffset) {
                    // 开始滑动返回时关闭软键盘
                    if (slideOffset < 0.03) {
                        SwipFinishKeyboardUtil.closeKeyboard(mActivity);
                    }

                    mDelegate.onSwipeFinishLayoutSlide(slideOffset);
                }

                @Override
                public void onPanelOpened(View panel) {
                    mDelegate.onSwipeFinishLayoutExecuted();
                }

                @Override
                public void onPanelClosed(View panel) {
                    mDelegate.onSwipeFinishLayoutCancel();
                }
            });
        }
    }

    /**
     * 设置滑动返回是否可用。默认值为 true
     *
     * @param swipeFinishEnable
     * @return
     */
    public SwipeFinishHelper setSwipeFinishEnable(boolean swipeFinishEnable) {
        if (mSwipeFinishLayout != null) {
            mSwipeFinishLayout.setSwipeFinishEnable(swipeFinishEnable);
        }
        return this;
    }

    /**
     * 设置是否仅仅跟踪左侧边缘的滑动返回。默认值为 true
     *
     * @param isOnlyTrackingLeftEdge
     * @return
     */
    public SwipeFinishHelper setIsOnlyTrackingLeftEdge(boolean isOnlyTrackingLeftEdge) {
        if (mSwipeFinishLayout != null) {
            mSwipeFinishLayout.setIsOnlyTrackingLeftEdge(isOnlyTrackingLeftEdge);
        }
        return this;
    }

    /**
     * 设置是否是微信滑动返回样式。默认值为 true
     *
     * @param isWeChatStyle
     * @return
     */
    public SwipeFinishHelper setIsWeChatStyle(boolean isWeChatStyle) {
        if (mSwipeFinishLayout != null) {
            mSwipeFinishLayout.setIsWeChatStyle(isWeChatStyle);
        }
        return this;
    }

    /**
     * 设置阴影资源 id。默认值为 R.drawable.bga_sbl_shadow
     *
     * @param shadowResId
     * @return
     */
    public SwipeFinishHelper setShadowResId(@DrawableRes int shadowResId) {
        if (mSwipeFinishLayout != null) {
            mSwipeFinishLayout.setShadowResId(shadowResId);
        }
        return this;
    }

    /**
     * 设置是否显示滑动返回的阴影效果。默认值为 true
     *
     * @param isNeedShowShadow
     * @return
     */
    public SwipeFinishHelper setIsNeedShowShadow(boolean isNeedShowShadow) {
        if (mSwipeFinishLayout != null) {
            mSwipeFinishLayout.setIsNeedShowShadow(isNeedShowShadow);
        }
        return this;
    }

    /**
     * 设置阴影区域的透明度是否根据滑动的距离渐变。默认值为 true
     *
     * @param isShadowAlphaGradient
     * @return
     */
    public SwipeFinishHelper setIsShadowAlphaGradient(boolean isShadowAlphaGradient) {
        if (mSwipeFinishLayout != null) {
            mSwipeFinishLayout.setIsShadowAlphaGradient(isShadowAlphaGradient);
        }
        return this;
    }

    /**
     * 设置触发释放后自动滑动返回的阈值，默认值为 0.3f
     *
     * @param threshold
     */
    public SwipeFinishHelper setSwipeFinishThreshold(@FloatRange(from = 0.0f, to = 1.0f) float threshold) {
        if (mSwipeFinishLayout != null) {
            mSwipeFinishLayout.setSwipeFinishThreshold(threshold);
        }
        return this;
    }

    /**
     * 设置底部导航条是否悬浮在内容上
     *
     * @param overlap
     */
    public SwipeFinishHelper setIsNavigationBarOverlap(boolean overlap) {
        if (mSwipeFinishLayout != null) {
            mSwipeFinishLayout.setIsNavigationBarOverlap(overlap);
        }
        return this;
    }

    /**
     * 是否正在滑动
     *
     * @return
     */
    public boolean isSliding() {
        if (mSwipeFinishLayout != null) {
            return mSwipeFinishLayout.isSliding();
        }
        return false;
    }

    /**
     * 执行跳转到下一个 Activity 的动画
     */
    public void executeForwardAnim() {
        executeForwardAnim(mActivity);
    }

    /**
     * 执行回到到上一个 Activity 的动画
     */
    public void executeBackwardAnim() {
        executeBackwardAnim(mActivity);
    }

    /**
     * 执行滑动返回到到上一个 Activity 的动画
     */
    public void executeSwipeFinishAnim() {
        executeSwipeFinishAnim(mActivity);
    }

    /**
     * 执行跳转到下一个 Activity 的动画。这里弄成静态方法，方便在 Fragment 中调用
     */
    public static void executeForwardAnim(Activity activity) {
        activity.overridePendingTransition(R.anim.sfl_activity_forward_enter,
                R.anim.sfl_activity_forward_exit);
    }

    /**
     * 执行回到到上一个 Activity 的动画。这里弄成静态方法，方便在 Fragment 中调用
     */
    public static void executeBackwardAnim(Activity activity) {
        activity.overridePendingTransition(R.anim.sfl_activity_backward_enter,
                R.anim.sfl_activity_backward_exit);
    }

    /**
     * 执行滑动返回到到上一个 Activity 的动画。这里弄成静态方法，方便在 Fragment 中调用
     */
    public static void executeSwipeFinishAnim(Activity activity) {
        activity.overridePendingTransition(R.anim.sfl_activity_swipefinish_enter,
                R.anim.sfl_activity_swipefinish_exit);
    }

    /**
     * 跳转到下一个 Activity，并且销毁当前 Activity
     *
     * @param cls 下一个 Activity 的 Class
     */
    public void forwardAndFinish(Class<?> cls) {
        forward(cls);
        mActivity.finish();
    }

    /**
     * 跳转到下一个 Activity，不销毁当前 Activity
     *
     * @param cls 下一个 Activity 的 Class
     */
    public void forward(Class<?> cls) {
        SwipFinishKeyboardUtil.closeKeyboard(mActivity);
        mActivity.startActivity(new Intent(mActivity, cls));
        executeForwardAnim();
    }

    /**
     * 跳转到下一个 Activity，不销毁当前 Activity
     *
     * @param cls         下一个 Activity 的 Class
     * @param requestCode 请求码
     */
    public void forward(Class<?> cls, int requestCode) {
        forward(new Intent(mActivity, cls), requestCode);
    }

    /**
     * 跳转到下一个 Activity，销毁当前 Activity
     *
     * @param intent 下一个 Activity 的意图对象
     */
    public void forwardAndFinish(Intent intent) {
        forward(intent);
        mActivity.finish();
    }

    /**
     * 跳转到下一个 Activity,不销毁当前 Activity
     *
     * @param intent 下一个 Activity 的意图对象
     */
    public void forward(Intent intent) {
        SwipFinishKeyboardUtil.closeKeyboard(mActivity);
        mActivity.startActivity(intent);
        executeForwardAnim();
    }

    /**
     * 跳转到下一个 Activity,不销毁当前 Activity
     *
     * @param intent      下一个 Activity 的意图对象
     * @param requestCode 请求码
     */
    public void forward(Intent intent, int requestCode) {
        SwipFinishKeyboardUtil.closeKeyboard(mActivity);
        mActivity.startActivityForResult(intent, requestCode);
        executeForwardAnim();
    }

    /**
     * 回到上一个 Activity，并销毁当前 Activity
     */
    public void backward() {
        SwipFinishKeyboardUtil.closeKeyboard(mActivity);
        mActivity.finish();
        executeBackwardAnim();
    }

    /**
     * 滑动返回上一个 Activity，并销毁当前 Activity
     */
    public void swipeFinishward() {
        SwipFinishKeyboardUtil.closeKeyboard(mActivity);
        mActivity.finish();
        executeSwipeFinishAnim();
    }

    /**
     * 回到上一个 Activity，并销毁当前 Activity（应用场景：欢迎、登录、注册这三个界面）
     *
     * @param cls 上一个 Activity 的 Class
     */
    public void backwardAndFinish(Class<?> cls) {
        SwipFinishKeyboardUtil.closeKeyboard(mActivity);
        mActivity.startActivity(new Intent(mActivity, cls));
        mActivity.finish();
        executeBackwardAnim();
    }

    public interface Delegate {
        /**
         * 是否支持滑动返回
         *
         * @return
         */
        boolean isSupportSwipeFinish();

        /**
         * 正在滑动返回
         *
         * @param slideOffset 从 0 到 1
         */
        void onSwipeFinishLayoutSlide(float slideOffset);

        /**
         * 没达到滑动返回的阈值，取消滑动返回动作，回到默认状态
         */
        void onSwipeFinishLayoutCancel();

        /**
         * 滑动返回执行完毕，销毁当前 Activity
         */
        void onSwipeFinishLayoutExecuted();
    }
}
