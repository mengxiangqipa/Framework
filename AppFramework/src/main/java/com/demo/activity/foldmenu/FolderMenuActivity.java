package com.demo.activity.foldmenu;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.demo.demo.R;
import com.framework.customview.foldermenu.FoldingLayout;
import com.framework.customview.foldermenu.OnFoldListener;
import com.framework.util.Y;

public class FolderMenuActivity extends Activity {

    private final int FOLD_ANIMATION_DURATION = 1000;
    private String TAG_ARROW = "service_arrow";
    private String TAG_ITEM = "service_item";
    private View mBottomView;
    private LinearLayout mTrafficLayout, mLifeLayout, mMedicalLayout, mLiveLayout, mPublicLayout;
    private RelativeLayout mTrafficBarLayout, mLifeBarLayout, mMedicalBarLayout, mLiveBarLayout, mPublicBarLayout;
    private FoldingLayout mTrafficFoldingLayout, mLifeFoldingLayout, mMedicalFoldingLayout, mLiveFoldingLayout,
            mPublicFoldingLayout;
    private int mNumberOfFolds = 3;

    public final static int dp2px(Context context, float dpValue) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * density + 0.5f);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_foldermenu_layout);

        findViews();

        initViews();
    }

    private void findViews() {

        mTrafficLayout = (LinearLayout) findViewById(R.id.traffic_layout);
        mLifeLayout = (LinearLayout) findViewById(R.id.life_layout);
        mMedicalLayout = (LinearLayout) findViewById(R.id.medical_layout);
        mLiveLayout = (LinearLayout) findViewById(R.id.live_layout);
        mPublicLayout = (LinearLayout) findViewById(R.id.public_layout);

        mTrafficBarLayout = (RelativeLayout) findViewById(R.id.traffic_bar_layout);
        mLifeBarLayout = (RelativeLayout) findViewById(R.id.life_bar_layout);
        mMedicalBarLayout = (RelativeLayout) findViewById(R.id.medical_bar_layout);
        mLiveBarLayout = (RelativeLayout) findViewById(R.id.live_bar_layout);
        mPublicBarLayout = (RelativeLayout) findViewById(R.id.public_bar_layout);

        mTrafficFoldingLayout = ((FoldingLayout) mTrafficLayout.findViewWithTag(TAG_ITEM));
        mLifeFoldingLayout = ((FoldingLayout) mLifeLayout.findViewWithTag(TAG_ITEM));
        mMedicalFoldingLayout = ((FoldingLayout) mMedicalLayout.findViewWithTag(TAG_ITEM));
        mLiveFoldingLayout = ((FoldingLayout) mLiveLayout.findViewWithTag(TAG_ITEM));
        mPublicFoldingLayout = ((FoldingLayout) mPublicLayout.findViewWithTag(TAG_ITEM));

        mBottomView = findViewById(R.id.bottom_view);
    }

    private void initViews() {

        mTrafficBarLayout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                handleAnimation(v, mTrafficFoldingLayout, mTrafficLayout, mLifeLayout);
            }
        });

        mLifeBarLayout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                handleAnimation(v, mLifeFoldingLayout, mLifeLayout, mMedicalLayout);
            }
        });

        mMedicalBarLayout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                handleAnimation(v, mMedicalFoldingLayout, mMedicalLayout, mLiveLayout);
            }
        });

        mLiveBarLayout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                handleAnimation(v, mLiveFoldingLayout, mLiveLayout, mPublicLayout);
            }
        });

        mPublicBarLayout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                handleAnimation(v, mPublicFoldingLayout, mPublicLayout, mBottomView);
            }
        });

        mTrafficFoldingLayout.setNumberOfFolds(mNumberOfFolds);
        mLifeFoldingLayout.setNumberOfFolds(mNumberOfFolds);
        mMedicalFoldingLayout.setNumberOfFolds(mNumberOfFolds);
        mLiveFoldingLayout.setNumberOfFolds(mNumberOfFolds);
        mPublicFoldingLayout.setNumberOfFolds(mNumberOfFolds);

        animateFold(mTrafficFoldingLayout, 350);
        setMarginToTop(1, mLifeLayout);

        animateFold(mLifeFoldingLayout, 350);
        setMarginToTop(1, mMedicalLayout);

        animateFold(mMedicalFoldingLayout, 350);
        setMarginToTop(1, mLiveLayout);

        animateFold(mLiveFoldingLayout, 350);
        setMarginToTop(1, mPublicLayout);

        animateFold(mPublicFoldingLayout, 350);
        setMarginToTop(1, mBottomView);

//      mTrafficFoldingLayout.setFoldFactor(1.0f);
//        mLifeFoldingLayout.setFoldFactor(1.0f);
//        mMedicalFoldingLayout.setFoldFactor(1.0f);
//        mLiveFoldingLayout.setFoldFactor(1.0f);
//        mPublicFoldingLayout.setFoldFactor(1.0f);
    }

    private void handleAnimation(final View bar, final FoldingLayout foldingLayout, View parent, final View
            nextParent) {

        final ImageView arrow = (ImageView) parent.findViewWithTag(TAG_ARROW);

        foldingLayout.setFoldListener(new OnFoldListener() {

            @Override
            public void onStartFold(float foldFactor) {
                Y.y("handleAnimation:-onStartFold-:" + foldFactor);
                bar.setClickable(true);
                arrow.setBackgroundResource(R.drawable.foldermenu_service_arrow_up);
                resetMarginToTop(foldingLayout, foldFactor, nextParent);
            }

            @Override
            public void onFoldingState(float foldFactor, float foldDrawHeight) {
                Y.y("handleAnimation:-onFoldingState-:" + foldFactor + "    " + foldDrawHeight);
                bar.setClickable(false);
                resetMarginToTop(foldingLayout, foldFactor, nextParent);
            }

            @Override
            public void onEndFold(float foldFactor) {
                Y.y("handleAnimation:-onEndFold-:" + foldFactor);
                bar.setClickable(true);
                arrow.setBackgroundResource(R.drawable.foldermenu_service_arrow_down);
                resetMarginToTop(foldingLayout, foldFactor, nextParent);
            }
        });

//      foldingLayout.setNumberOfFolds(mNumberOfFolds);
        animateFold(foldingLayout, FOLD_ANIMATION_DURATION);
    }

    private void resetMarginToTop(View view, float foldFactor, View nextParent) {

        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) nextParent.getLayoutParams();
        lp.topMargin = (int) (-view.getMeasuredHeight() * foldFactor) + dp2px(FolderMenuActivity.this, 10);
        nextParent.setLayoutParams(lp);
    }

    private void setMarginToTop(float foldFactor, View nextParent) {

        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) nextParent.getLayoutParams();
        lp.topMargin = (int) (-dp2px(FolderMenuActivity.this, 135) * foldFactor) + dp2px(FolderMenuActivity.this, 10);
        nextParent.setLayoutParams(lp);
    }

    @SuppressLint("NewApi")
    public void animateFold(FoldingLayout foldLayout, int duration) {
        float foldFactor = foldLayout.getFoldFactor();

        ObjectAnimator animator = ObjectAnimator.ofFloat(foldLayout,
                "foldFactor", foldFactor, foldFactor > 0 ? 0 : 1);
        animator.setRepeatMode(ValueAnimator.REVERSE);
        animator.setRepeatCount(0);
        animator.setDuration(duration);
        animator.setInterpolator(new AccelerateInterpolator());
        animator.start();
    }
}
