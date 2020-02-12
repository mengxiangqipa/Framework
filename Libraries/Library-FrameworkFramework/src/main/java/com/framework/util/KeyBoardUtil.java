package com.framework.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * 键盘
 *
 * @author YobertJomi
 * className KeyBoardUtil
 * created at  2017/8/4  11:29
 */
public class KeyBoardUtil {
    private static volatile KeyBoardUtil singleton;

    private KeyBoardUtil() {
    }

    public static KeyBoardUtil getInstance() {
        if (singleton == null) {
            synchronized (KeyBoardUtil.class) {
                if (singleton == null) {
                    singleton = new KeyBoardUtil();
                }
            }
        }
        return singleton;
    }

    /**
     * <pre>
     * 传入ture表示收起软键盘，false改变软键盘当前显示状态
     */
    public void isCloseSoftInputMethod(Context context, EditText et,
                                       boolean bool) {
        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (bool) {
            if (null != et) {
                // 设置软键盘隐藏
                imm.hideSoftInputFromWindow(et.getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
                // imm.hideSoftInputFromWindow(getCurrentFocus()
                // .getApplicationWindowToken(),
                // InputMethodManager.HIDE_NOT_ALWAYS);
            } else {
                // 关闭activity的SoftInputMethod
                try {
                    imm.hideSoftInputFromWindow(((Activity) context)
                                    .getCurrentFocus().getApplicationWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            // 改变软键盘当前显示状态
            imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT,
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * <p>
     * 显示软键盘
     * </p>
     */
    public void showSoftInputMethod(@NonNull Context context, EditText et) {
        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (null != et) {
            // 设置软键盘展开
            imm.showSoftInput(et, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    /**
     * <p>
     * 隐藏软键盘
     * </p>
     */
    public void hideSoftInputMethod(@NonNull Context context, EditText et) {
        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (null != et) {
            // 设置软键盘展开
            imm.hideSoftInputFromWindow(et.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 判断keyBoard是否显示
     *
     * @param rootView view.getRootView();
     * @return boolean
     */
    public boolean isKeyboardShown(final @NonNull View rootView, final @IntRange(from = 0, to = 1920 * 2) int
            softKeyboardHeight) {
        Rect r = new Rect();
        rootView.getWindowVisibleDisplayFrame(r);
        DisplayMetrics dm = rootView.getResources().getDisplayMetrics();
        //此处就是用来获取键盘的高度的， 在键盘没有弹出的时候 此高度为0 键盘弹出的时候为一个正数
        int heightDiff = rootView.getBottom() - r.bottom;
        return heightDiff > softKeyboardHeight * dm.density;
    }

    /**
     * 判断keyBoard是否显示
     *
     * @return boolean
     */
    public boolean isKeyboardShown(final @NonNull Context context) {
        return isKeyboardShown((((Activity) context).getWindow().getDecorView()), 0);
    }

    /**
     * 判断keyBoard是否显示
     *
     * @return boolean
     */
    public boolean isKeyboardShown(final @NonNull Context context, final @IntRange(from = 0, to = 1920 * 2) int
            softKeyboardHeight) {
        return isKeyboardShown((((Activity) context).getWindow().getDecorView()), softKeyboardHeight);
    }

    private View rootView;//activity的根视图
    private int rootViewVisibleHeight;//纪录根视图的显示高度
    private OnSoftKeyBoardChangeListener onSoftKeyBoardChangeListener;

    private KeyBoardUtil(Activity activity, final @IntRange(from = 0, to = 1920 * 2) int
            halfSoftKeyboardHeight) {
        //获取activity的根视图
        rootView = activity.getWindow().getDecorView();

        //监听视图树中全局布局发生改变或者视图树中的某个视图的可视状态发生改变
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //获取当前根视图在屏幕上显示的大小
                Rect r = new Rect();
                rootView.getWindowVisibleDisplayFrame(r);

                int visibleHeight = r.height();
//                System.out.println("" + visibleHeight);
                if (rootViewVisibleHeight == 0) {
                    rootViewVisibleHeight = visibleHeight;
                    return;
                }

                //根视图显示高度没有变化，可以看作软键盘显示／隐藏状态没有改变
                if (rootViewVisibleHeight == visibleHeight) {
                    return;
                }

                //根视图显示高度变小超过200，可以看作软键盘显示了
                if (rootViewVisibleHeight - visibleHeight > halfSoftKeyboardHeight) {
                    if (onSoftKeyBoardChangeListener != null) {
                        onSoftKeyBoardChangeListener.keyBoardShow(rootViewVisibleHeight - visibleHeight);
                    }
                    rootViewVisibleHeight = visibleHeight;
                    return;
                }

                //根视图显示高度变大超过200，可以看作软键盘隐藏了
                if (visibleHeight - rootViewVisibleHeight > 200) {
                    if (onSoftKeyBoardChangeListener != null) {
                        onSoftKeyBoardChangeListener.keyBoardHide(visibleHeight - rootViewVisibleHeight);
                    }
                    rootViewVisibleHeight = visibleHeight;
                    return;
                }
            }
        });
    }

    private void setOnSoftKeyBoardChangeListener(OnSoftKeyBoardChangeListener onSoftKeyBoardChangeListener) {
        this.onSoftKeyBoardChangeListener = onSoftKeyBoardChangeListener;
    }

    public interface OnSoftKeyBoardChangeListener {
        void keyBoardShow(int height);

        void keyBoardHide(int height);
    }

    /**
     * 监听键盘弹出
     *
     * @param activity                     Activity
     * @param onSoftKeyBoardChangeListener OnSoftKeyBoardChangeListener
     */
    public void setOnSoftKeyBoardChangeListener(Activity activity,
                                                OnSoftKeyBoardChangeListener onSoftKeyBoardChangeListener,
                                                final @IntRange(from = 0, to = 1920 * 2) int halfSoftKeyboardHeight) {
        KeyBoardUtil softKeyBoardListener = new KeyBoardUtil(activity, halfSoftKeyboardHeight);
        softKeyBoardListener.setOnSoftKeyBoardChangeListener(onSoftKeyBoardChangeListener);
    }
}
