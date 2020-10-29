package com.demo.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.demo.activity.SplashActivity;
import com.demo.callbacks.CallBack_enter;
import com.demo.configs.ConstantsME;
import com.demo.demo.R;
import com.framework.util.PreferencesHelper;

import org.jetbrains.annotations.Nullable;

import androidx.core.view.GestureDetectorCompat;
import butterknife.BindView;

/**
 * 引导页3
 *
 * @author YobertJomi
 * className GuideFragment3
 * created at  2020/10/29  13:52
 */
public class GuideFragment3 extends BaseFragment {

    @BindView(R.id.tv_go)
    TextView tvGo;

    private GestureDetectorCompat gestureDetectorCompat;

    public GuideFragment3() {
    }

    public static GuideFragment3 newInstance(Bundle args) {
        GuideFragment3 fragment = new GuideFragment3();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gestureDetectorCompat = new GestureDetectorCompat(getWeakReference().get(),
                onGestureListener);
    }

    @Override
    public View initContentView(LayoutInflater inflater, ViewGroup container,
                                Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_guide_3, container, false);
    }

    @Override
    protected void initUi(@Nullable Bundle savedInstanceState) {

    }

    @Override
    public void initListener() {
        tvGo.setOnClickListener(v -> {
            if (getActivity() instanceof CallBack_enter) {
                ((CallBack_enter) getActivity()).clickEnterMe();
            }
        });
    }

    GestureDetector.OnGestureListener onGestureListener = new GestureDetector.OnGestureListener() {
        @Override //此方法必须重写且返回真，否则onFling不起效
        public boolean onDown(MotionEvent e) {
            //        PreferencesHelper.putBoolean(ConstantsME.hasGuide, true);
            //        startActivity(new Intent(mContext, SplashActivity.class));
            //        ((Activity) mContext).finish();
            return true;
        }

        @Override
        public void onShowPress(MotionEvent motionEvent) {

        }

        @Override
        public boolean onSingleTapUp(MotionEvent motionEvent) {
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v,
                                float v1) {
            if ((motionEvent.getX() - motionEvent1.getX() > 5)) {
                PreferencesHelper.getInstance().putInfo(ConstantsME.NOTFIRSTIN, true);
                startActivity(new Intent(getActivityM(), SplashActivity.class));
                getActivityM().finish();
                return true;
            } else if ((motionEvent.getX() - motionEvent1.getX() > 120) && Math.abs(v1) > 200) {
                return true;
            }
            return false;
        }

        @Override
        public void onLongPress(MotionEvent motionEvent) {

        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return false;
        }
    };
}
