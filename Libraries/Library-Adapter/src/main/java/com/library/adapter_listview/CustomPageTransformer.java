package com.library.adapter_listview;

import android.annotation.SuppressLint;
import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * 自定义3D轮播的viewPager--PageTransformer
 *
 * @author Yangjie
 *         className CustomPageTransformer
 *         created at  2017/3/26  11:31
 */

public class CustomPageTransformer implements ViewPager.PageTransformer {
    public static float MIN_SCALE = 0.75f;
    private float MIN_ALPHA = 0.5f;
    private float MARGIN_LENGTH = 300f;

    private float lastPositon;

    public CustomPageTransformer(float MARGIN_LENGTH) {
        this.MARGIN_LENGTH = MARGIN_LENGTH;
    }

    @SuppressLint("NewApi")
    @Override
    public void transformPage(View view, float position) {
        int pageWidth = view.getWidth();
        if (position < -2) { // [-Infinity,-2)
            // This page is way off-screen to the left.
            view.setAlpha(0);
            view.setZ(1);
        } else if (position <= -1) { // [-2,-1)
            view.setZ(1);
            view.setAlpha(MIN_ALPHA * (2 + position));
            view.setTranslationX((pageWidth - MARGIN_LENGTH) * (-position));
            view.setScaleX(MIN_SCALE);
            view.setScaleY(MIN_SCALE);
        } else if (position <= 0) { // [-1,0]
            if (position == 0 || position > 0.5) {
                view.setZ(5);
//                view.bringToFront();
            } else if (lastPositon <= 0 && position - lastPositon > 0) {
                //右滑
                view.setZ(4);
            } else {
                view.setZ(3);
            }
            lastPositon = position;
            float scaleFactor = MIN_SCALE + (1 - MIN_SCALE)
                    * (1 - Math.abs(position));
            view.setAlpha(MIN_ALPHA + (scaleFactor - MIN_SCALE)
                    / (1 - MIN_SCALE) * (1 - MIN_ALPHA));
            view.setTranslationX((pageWidth - MARGIN_LENGTH) * -position);
            // Scale the page down (between MIN_SCALE and 1)
            view.setScaleX(scaleFactor);
            view.setScaleY(scaleFactor);
        } else if (position <= 1) { // (0,1]
            view.setZ(4);
            // Fade the page out.
            float scaleFactor = MIN_SCALE + (1 - MIN_SCALE)
                    * (1 - Math.abs(position));
            view.setAlpha(MIN_ALPHA + (scaleFactor - MIN_SCALE)
                    / (1 - MIN_SCALE) * (1 - MIN_ALPHA));
            view.setTranslationX((pageWidth - MARGIN_LENGTH) * -position);
            // Scale the page down (between MIN_SCALE and 1)
            view.setScaleX(scaleFactor);
            view.setScaleY(scaleFactor);
        } else if (position <= 2) { // (1,2]
            view.setZ(2);
            view.setAlpha(MIN_ALPHA * (2 - position));
            view.setTranslationX((pageWidth - MARGIN_LENGTH) * -position);
            view.setScaleX(MIN_SCALE);
            view.setScaleY(MIN_SCALE);
        } else { // (2,+Infinity]
            // This page is way off-screen to the right.
            view.setAlpha(0);
            view.setZ(2);
        }
    }
}
