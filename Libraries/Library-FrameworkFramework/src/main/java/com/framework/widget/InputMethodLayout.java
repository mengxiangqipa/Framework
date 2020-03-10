package com.framework.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.framework.util.AndroidAdjustResizeBugFix;

/**
 * 一个继承自RelativeLayout的输入法监听布局
 * 使用：inputMethodLayout.setEditText(et).setOnKeyboardStateChangeListener(listener);
 * <p>
 * <activity
 * android:name="com.asiainfo.andcampus.activity.ThemeDetailActivity"
 * android:configChanges="orientation|keyboardHidden|screenSize"
 * android:exported="true"
 * android:launchMode="singleTask"
 * android:screenOrientation="portrait"
 * android:theme="@style/AppTheme.NoActionBar"
 * android:windowSoftInputMode="adjustUnspecified|stateHidden"/>
 * <p/>
 */
public class InputMethodLayout extends RelativeLayout {
    /**
     * 隐藏状态
     **/
    public static final byte KEYBOARD_STATE_HIDE = -2;
    /**
     * 打开状态
     **/
    public static final byte KEYBOARD_STATE_SHOW = -3;
    private OnKeyboardStateChangeListener onKeyboardStateChangeListener;// 键盘状态监听
    private EditText editText;
    private ViewTreeObserver.OnGlobalLayoutListener mOnGlobalLayoutListener = new ViewTreeObserver
            .OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            Rect r = new Rect();
            //获取当前界面可视部分
            ((Activity) getContext()).getWindow().getDecorView().getWindowVisibleDisplayFrame(r);
            //获取屏幕的高度
            int screenHeight = ((Activity) getContext()).getWindow().getDecorView().getRootView().getHeight();
            //此处就是用来获取键盘的高度的， 在键盘没有弹出的时候 此高度为0 键盘弹出的时候为一个正数
            int heightDifference = screenHeight - r.bottom;
            keyboardSateChange(heightDifference > 0 ? KEYBOARD_STATE_SHOW : KEYBOARD_STATE_HIDE);
        }
    };

    public InputMethodLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public InputMethodLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public InputMethodLayout(Context context) {
        super(context);
    }

    private void initAndroidAdjustResizeBugFix() {
        //解决全屏时获取软键盘是否弹出问题
        AndroidAdjustResizeBugFix.assistActivity((Activity) getContext());
    }

    @Override
    public void onWindowSystemUiVisibilityChanged(int visible) {
        super.onWindowSystemUiVisibilityChanged(visible);
        if (visible > 0) {
            initAndroidAdjustResizeBugFix();
        }
    }

    /**
     * 设置软键盘状态监听
     *
     * @param onKeyboardStateChangeListener onKeyboardStateChangeListener
     */
    public void setOnKeyboardStateChangeListener(OnKeyboardStateChangeListener onKeyboardStateChangeListener) {
        this.onKeyboardStateChangeListener = onKeyboardStateChangeListener;
    }

    public InputMethodLayout setEditText(EditText editText) {
        if (null != editText) {
            this.editText = editText;
            this.editText.getViewTreeObserver().addOnGlobalLayoutListener(mOnGlobalLayoutListener);
        }
        return this;
    }

    @Override
    protected void onDetachedFromWindow() {
        if (null != editText)
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN) {
                editText.getViewTreeObserver().removeGlobalOnLayoutListener(mOnGlobalLayoutListener);
            } else {
                editText.getViewTreeObserver().removeOnGlobalLayoutListener(mOnGlobalLayoutListener);
            }
        super.onDetachedFromWindow();
    }

    /**
     * 切换软键盘状态
     *
     * @param state // 状态
     */
    public InputMethodLayout keyboardSateChange(int state) {
        if (onKeyboardStateChangeListener != null) {
            onKeyboardStateChangeListener.onKeyBoardStateChange(state);
        }
        return this;
    }

    /**
     * 软键盘状态切换监听
     */
    public interface OnKeyboardStateChangeListener {
        void onKeyBoardStateChange(int state);
    }
}