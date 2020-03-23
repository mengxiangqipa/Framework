package com.demo.activity.az;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.demo.demo.R;
import com.library.utils.ScreenUtils;

/**
 * 自定义带有删除功能的EditText
 *
 * @author YobertJomi
 * className EditTextWithDel
 * created at  2017/7/12  16:22
 */
public class EditTextWithDel extends android.support.v7.widget.AppCompatEditText {
    private Drawable imgLeft, imgRight;

    public EditTextWithDel(Context context) {
        super(context);
        init(context);
    }

    public EditTextWithDel(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public EditTextWithDel(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        imgLeft = context.getResources().getDrawable(
                R.drawable.az_icon_search);
        imgRight = context.getResources().getDrawable(
                R.drawable.az_icon_delete);
        addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                setDrawable();
            }
        });
        setDrawable();
    }

    // 设置删除图片
    private void setDrawable() {
        if (length() < 1) {
            setCompoundDrawablesWithIntrinsicBounds(imgLeft, null, null, null);
        } else {
            int marginRight = ScreenUtils.getInstance().dip2px(getContext(), 10);
            imgRight.setBounds(-marginRight, 0, ScreenUtils.getInstance().dip2px(getContext(),
                    20) - marginRight,
                    ScreenUtils.getInstance().dip2px(getContext(), 20));
            setCompoundDrawables(imgLeft, null, imgRight, null);
//            setCompoundDrawablesWithIntrinsicBounds(imgLeft, null, imgRight, null);
        }
    }

    // 处理删除事件
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (imgRight != null && event.getAction() == MotionEvent.ACTION_UP) {
            int eventX = (int) event.getRawX();
            int eventY = (int) event.getRawY();
            Rect rect = new Rect();
            getGlobalVisibleRect(rect);
            rect.left = rect.right - 50;
            if (rect.contains(eventX, eventY))
                setText("");
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }
}
