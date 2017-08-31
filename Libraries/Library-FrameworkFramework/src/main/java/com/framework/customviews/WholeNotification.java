package com.framework.customviews;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.IntEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.framework.R;

/**
 * 全局通知
 *
 * @author YobertJomi
 *         className WholeNotification
 *         created at  2017/8/30  17:15
 */
public class WholeNotification implements View.OnTouchListener
{

    private static final int DIRECTION_LEFT = -1;
    private static final int DIRECTION_NONE = 0;
    private static final int DIRECTION_RIGHT = 1;

    private static final int DISMISS_INTERVAL = 3000;

    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mWindowParams;
    private View mContentView;
    private Context mContext;
    private int mScreenWidth = 0;
    private int mStatusBarHeight = 0;

    private boolean isShowing = false;
    private ValueAnimator restoreAnimator = null;
    private ValueAnimator dismissAnimator = null;


    public WholeNotification(Builder builder)
    {
        mContext = builder.getContext();

        mStatusBarHeight = getStatusBarHeight();
        mScreenWidth = mContext.getResources().getDisplayMetrics().widthPixels;

        mWindowManager = (WindowManager)
                mContext.getSystemService(Context.WINDOW_SERVICE);
        mWindowParams = new WindowManager.LayoutParams();
        mWindowParams.type = WindowManager.LayoutParams.TYPE_TOAST;// 系统提示window
        mWindowParams.gravity = Gravity.LEFT | Gravity.TOP;
        mWindowParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        mWindowParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mWindowParams.flags =
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
                        WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS;
        //设置进入和退出动画
        mWindowParams.windowAnimations = R.style.WholeNotificationAnim;
        mWindowParams.x = 0;
        mWindowParams.y = -mStatusBarHeight;

        setView(builder);
    }


    private static final int HIDE_WINDOW = 0;

    private Handler mHandler = new Handler(new Handler.Callback()
    {
        @Override
        public boolean handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case HIDE_WINDOW:
                    dismiss();
                    break;
            }
            return false;
        }
    });

    /***
     * 设置内容视图
     *
     */
    private void setView(Builder builder)
    {
        mContentView = builder.getView();
        if (builder.isMonitorTouch())
            mContentView.setOnTouchListener(this);
    }


    private int downX = 0;
    private int direction = DIRECTION_NONE;

    @Override
    public boolean onTouch(View v, MotionEvent event)
    {
        if (isAnimatorRunning())
        {
            return false;
        }
        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                downX = (int) event.getRawX();
                break;
            case MotionEvent.ACTION_MOVE:
                //处于滑动状态就取消自动消失
                mHandler.removeMessages(HIDE_WINDOW);
                int moveX = (int) event.getRawX() - downX;
                //判断滑动方向
                if (moveX > 0)
                {
                    direction = DIRECTION_RIGHT;
                } else
                {
                    direction = DIRECTION_LEFT;
                }
                updateWindowLocation(moveX, mWindowParams.y);
                break;
            case MotionEvent.ACTION_UP:
                if (Math.abs(mWindowParams.x) > mScreenWidth / 2)
                {
                    startDismissAnimator(direction);
                } else
                {
                    startRestoreAnimator();
                }
                break;
        }
        return true;
    }

    private void startRestoreAnimator()
    {
        restoreAnimator = new ValueAnimator().ofInt(mWindowParams.x, 0);
        restoreAnimator.setDuration(300);
        restoreAnimator.setEvaluator(new IntEvaluator());

        restoreAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
        {
            @Override
            public void onAnimationUpdate(ValueAnimator animation)
            {
                updateWindowLocation((Integer) animation.getAnimatedValue(), -mStatusBarHeight);
            }
        });
        restoreAnimator.addListener(new AnimatorListenerAdapter()
        {
            @Override
            public void onAnimationEnd(Animator animation)
            {
                restoreAnimator = null;
                autoDismiss();
            }
        });
        restoreAnimator.start();
    }

    private void startDismissAnimator(int direction)
    {
        if (direction == DIRECTION_LEFT)
            dismissAnimator = new ValueAnimator().ofInt(mWindowParams.x, -mScreenWidth);
        else
        {
            dismissAnimator = new ValueAnimator().ofInt(mWindowParams.x, mScreenWidth);
        }
        dismissAnimator.setDuration(300);
        dismissAnimator.setEvaluator(new IntEvaluator());

        dismissAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
        {
            @Override
            public void onAnimationUpdate(ValueAnimator animation)
            {
                updateWindowLocation((Integer) animation.getAnimatedValue(), -mStatusBarHeight);
            }
        });
        dismissAnimator.addListener(new AnimatorListenerAdapter()
        {
            @Override
            public void onAnimationEnd(Animator animation)
            {
                restoreAnimator = null;
                dismiss();
            }
        });
        dismissAnimator.start();
    }

    private boolean isAnimatorRunning()
    {
        return (restoreAnimator != null && restoreAnimator.isRunning()) || (dismissAnimator != null && dismissAnimator.isRunning());
    }

    public void updateWindowLocation(int x, int y)
    {
        if (isShowing)
        {
            mWindowParams.x = x;
            mWindowParams.y = y;
            mWindowManager.updateViewLayout(mContentView, mWindowParams);
        }
    }

    public void show()
    {
        if (!isShowing)
        {
            isShowing = true;
            mWindowManager.addView(mContentView, mWindowParams);
            autoDismiss();
        }
    }

    public void dismiss()
    {
        if (isShowing)
        {
            resetState();
            mWindowManager.removeView(mContentView);
        }
    }

    /**
     * 重置状态
     */
    private void resetState()
    {
        isShowing = false;
        mWindowParams.x = 0;
        mWindowParams.y = -mStatusBarHeight;
    }

    /**
     * 自动隐藏通知
     */
    private void autoDismiss()
    {
        mHandler.removeMessages(HIDE_WINDOW);
        mHandler.sendEmptyMessageDelayed(HIDE_WINDOW, DISMISS_INTERVAL);
    }

    /**
     * 获取状态栏的高度
     */
    public int getStatusBarHeight()
    {
        int height = 0;
        int resId = mContext.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resId > 0)
        {
            height = mContext.getResources().getDimensionPixelSize(resId);
        }
        return height;
    }


    public static class Builder
    {
        private Context context;
        private boolean monitorTouch = true;
        private View view;

        public Context getContext()
        {
            return context;
        }

        public boolean isMonitorTouch()
        {
            return monitorTouch;
        }

        public Builder setMonitorTouch(boolean monitorTouch)
        {
            this.monitorTouch = monitorTouch;
            return this;
        }

        public View getView()
        {
            return view;
        }

        public Builder setContext(Context context)
        {
            this.context = context;
            return this;
        }

        public Builder setView(View view)
        {
            this.view = view;
            return this;
        }


        public WholeNotification build()
        {
            if (null == context)
                throw new IllegalArgumentException("the context is required.");

            return new WholeNotification(this);
        }

    }

}
