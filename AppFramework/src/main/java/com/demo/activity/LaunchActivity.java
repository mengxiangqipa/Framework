package com.demo.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.demo.callbacks.CallBack_enter;
import com.demo.configs.ConstantsME;
import com.demo.demo.R;
import com.demo.fragment.Fragment_guide_1;
import com.demo.fragment.Fragment_guide_2;
import com.demo.fragment.Fragment_guide_3;
import com.framework.security.RSAmethodInRaw;
import com.framework.util.ActivityTaskUtil;
import com.framework.util.PreferencesHelper;
import com.framework.util.ScreenUtil;
import com.library.adapter_recyclerview.GuideFragmentPagerAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import custom.org.greenrobot.eventbus.Subscribe;
import custom.org.greenrobot.eventbus.ThreadMode;

public class LaunchActivity extends BaseActivity implements OnClickListener, CallBack_enter {
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.tv_dot_dynamic)
    TextView tvDotDynamic;
    @BindView(R.id.dotsLayout)
    RelativeLayout dotsLayout;
    @BindView(R.id.linear_bottom_bar)
    LinearLayout linearBottomBar;
    @BindView(R.id.relative_show_btn)
    RelativeLayout relativeShowBtn;

    private Animation animation_btn;
    private boolean isShowingDialog = false;
    private int count = 0;
    private int currentPosition = 0;
    private int oldPosition = 0;

    //eventBus通知新消息
    @Subscribe(threadMode = ThreadMode.MAIN, tag = "newMessage")
    public void receivedNewMessage(String info) {
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        PreferencesHelper.getInstance().putInfo(ConstantsME.fromLauncher, true);
        ActivityTaskUtil.getInstance().addActivity(this);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager
//        .LayoutParams.FLAG_FULLSCREEN);

        if (PreferencesHelper.getInstance().getBooleanData(ConstantsME.NOTFIRSTIN)) {
            if (!TextUtils.isEmpty(RSAmethodInRaw.getInstance().rsaDecrypt(this,
                    PreferencesHelper.getInstance().getStringData
                    (ConstantsME.PHONE)))
                    && !TextUtils.isEmpty(PreferencesHelper.getInstance().getStringData(ConstantsME.token))
                    && PreferencesHelper.getInstance().getBooleanData(ConstantsME.LOGINED)
                    && !TextUtils.isEmpty(PreferencesHelper.getInstance().getStringData(ConstantsME.currentShopId))) {
                startActivity(new Intent(LaunchActivity.this, SplashActivity.class));
                finish();
            } else {
//                startActivity(new Intent(LaunchActivity.this, LoginActivity.class));

                startActivity(new Intent(LaunchActivity.this, SplashActivity.class));//test
                finish();
            }
        } else {
            setContentView(R.layout.activity_guide);
            ButterKnife.bind(this);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                ScreenUtil.getInstance().setSystemUiColorDark(this, false);//设置状态栏字体颜色浅
            }
            initData();
        }
    }

    private void initData() {
        animation_btn = AnimationUtils.loadAnimation(this, R.anim.myset_showbtn);
        if (PreferencesHelper.getInstance().getBooleanData(ConstantsME.NOTFIRSTIN)) {
            viewPager.setVisibility(View.GONE);
            dotsLayout.setVisibility(View.INVISIBLE);
            relativeShowBtn.setVisibility(View.VISIBLE);
            linearBottomBar.setAnimation(animation_btn);
        } else {
            PreferencesHelper.getInstance().putInfo(ConstantsME.NOTFIRSTIN, true);
            dotsLayout.setVisibility(View.VISIBLE);
            relativeShowBtn.setVisibility(View.INVISIBLE);
            initViewPager();
        }
    }

    private void initViewPager() {
        ArrayList<Fragment> listFragments = new ArrayList<>();
        Fragment_guide_1 fragment_guide_1 = new Fragment_guide_1();
        Fragment_guide_2 fragment_guide_2 = new Fragment_guide_2();
        Fragment_guide_3 fragment_guide_3 = new Fragment_guide_3();
        listFragments.add(fragment_guide_1);
        listFragments.add(fragment_guide_2);
        listFragments.add(fragment_guide_3);
        GuideFragmentPagerAdapter adapter =
                new GuideFragmentPagerAdapter(getSupportFragmentManager(), listFragments);
        //viewPager.setPageTransformer(true, new MyPageTransformer());
        viewPager.setOffscreenPageLimit(1);
        viewPager.setAdapter(adapter);
        final int screenWidth = ScreenUtil.getInstance().getScreenWidthPx(this);
        final int _21dp = ScreenUtil.getInstance().dip2px(this, 21);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {
                //                Y.y("onPageScrolled:" + position + "   " + positionOffsetPixels
                //                + "    " +
                // positionOffset + "          " + ViewHelper.getTranslationX(tv_dot_dynamic));
                if (currentPosition == position && oldPosition == position) {
                    //                    ViewHelper.setTranslationX(tv_dot_dynamic,
                    //                    currentPosition * _21dp +
                    // positionOffsetPixels * _21dp / screenWidth);
                    tvDotDynamic.setTranslationX(currentPosition * _21dp + positionOffsetPixels * _21dp / screenWidth);
                }
                oldPosition = currentPosition;//最后一次位置
                currentPosition = position;
                if (currentPosition == 2 && positionOffsetPixels == 0) {
                    count++;
                } else {
                    count = 0;
                }
                //                if (count >= 3) {
                //                    PreferenceHelper.setInfo(ConstantsME.NOTFIRSTIN, true);
                //                    startActivity(new Intent(LaunchActivity.this,
                //                    SplashActivity.class));
                //                    finish();
                //                }
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    @Override
    public void clickEnterMe() {
        viewPager.setVisibility(View.GONE);
        dotsLayout.setVisibility(View.INVISIBLE);
        relativeShowBtn.setVisibility(View.VISIBLE);
        linearBottomBar.setAnimation(animation_btn);
    }

    @Override
    public void onBackPressed() {
        if (!isShowingDialog) {
            super.onBackPressed();
            finish();
            overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
        }
    }

    @OnClick(R.id.tvGo)
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.tvGo:
                intent = new Intent(LaunchActivity.this, LoginActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
                finish();
                break;
//            case R.id.tv_register:
//                intent = new Intent(LaunchActivity.this, SplashActivity.class);
//                startActivity(intent);
//                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
//                break;
            default:
                break;
        }
    }
}
