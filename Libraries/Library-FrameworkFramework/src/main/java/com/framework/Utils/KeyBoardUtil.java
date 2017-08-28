package com.framework.Utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * 键盘
 *
 * @author YobertJomi
 *         className KeyBoardUtil
 *         created at  2017/8/4  11:29
 */
public class KeyBoardUtil
{
    private static volatile KeyBoardUtil singleton;

    private KeyBoardUtil()
    {
    }

    public static KeyBoardUtil getInstance()
    {
        if (singleton == null)
        {
            synchronized (KeyBoardUtil.class)
            {
                if (singleton == null)
                {
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
                                       boolean bool)
    {
        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (bool)
        {
            if (null != et)
            {
                // 设置软键盘隐藏
                imm.hideSoftInputFromWindow(et.getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
                // imm.hideSoftInputFromWindow(getCurrentFocus()
                // .getApplicationWindowToken(),
                // InputMethodManager.HIDE_NOT_ALWAYS);
            } else
            {
                // 关闭activity的SoftInputMethod
                try
                {
                    imm.hideSoftInputFromWindow(((Activity) context)
                                    .getCurrentFocus().getApplicationWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                } catch (Exception e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        } else
        {
            // 改变软键盘当前显示状态
            imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT,
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 判断keyBoard是否显示
     *
     * @param rootView view.getRootView();
     * @return boolean
     */
    public boolean isKeyboardShown(final @NonNull View rootView, final @IntRange(from = 0, to = 1920 * 2) int softKeyboardHeight)
    {
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
    public boolean isKeyboardShown(final @NonNull Context context)
    {
        return isKeyboardShown((((Activity) context).getWindow().getDecorView()), 0);
    }
}
