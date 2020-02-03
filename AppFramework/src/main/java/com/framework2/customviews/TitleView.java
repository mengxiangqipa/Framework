package com.framework2.customviews;

import android.app.Activity;
import android.content.Context;
import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.demo.activity.BaseActivity;
import com.demo.demo.R;
import com.framework2.baseEvent.BaseOnClickListener;

/**
 * 自定义头部view
 *
 * @author Yangjie
 * className TitleView
 * created at  2017/3/15  14:29
 */
public class TitleView extends FrameLayout {
    private View view;
    private ImageView iv_back;
    private TextView tv_title;
    private RelativeLayout rightLayout;//右侧部分
    private ImageView iv_right;
    private ImageView iv_right_red;
    private TextView tv_right;

    public TitleView(Context context) {
        super(context);
        init();
    }

    public TitleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TitleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        view = LayoutInflater.from(getContext()).inflate(
                R.layout.include_title_bar_common, null);
        tv_title = (TextView) view.findViewById(R.id.tv_title);
        iv_back = (ImageView) view.findViewById(R.id.iv_back);
        iv_right = (ImageView) view.findViewById(R.id.iv_right);
        iv_right_red = (ImageView) view.findViewById(R.id.iv_right_red);
        tv_right = (TextView) view.findViewById(R.id.tv_right);
        rightLayout = (RelativeLayout) view.findViewById(R.id.rightLayout);

        iv_back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getContext() instanceof BaseActivity) {
                    ((BaseActivity) getContext()).finishActivity();
                } else {
                    ((Activity) getContext()).finish();
                }
            }
        });
        rightLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tv_right.getVisibility() != VISIBLE) {
                    showRightMarkIcon(false);
//                    GohomePopupWindowUtil.getProxyApplication().showGohomePopupWindow(getContext(), iv_right.getVisibility
// () == VISIBLE && PreferencesHelper.getProxyApplication().getBooleanData(ConstantsME.hasNewMessage), TitleView.this);
                }
            }
        });
        addView(view);
    }

    /**
     * @param leftVisible 左边可见
     */
    public TitleView setLeftVisible(boolean leftVisible) {
        iv_back.setVisibility(leftVisible ? VISIBLE : INVISIBLE);
        return this;
    }

    /**
     * @param onClickListener onclickListener
     */
    public TitleView setLeftOnClickListener(BaseOnClickListener onClickListener) {
        if (iv_back != null && onClickListener != null) {
            iv_back.setOnClickListener(onClickListener);
        }
        return this;
    }

    /**
     * @param rightVisible 右边可见
     */
    public TitleView setRightVisible(boolean rightVisible) {
        rightLayout.setVisibility(rightVisible ? VISIBLE : INVISIBLE);
        tv_right.setVisibility(rightVisible ? VISIBLE : INVISIBLE);
        return this;
    }

    /**
     * @param showRightMarkIcon 右边小红点可见
     */
    public TitleView showRightMarkIcon(boolean showRightMarkIcon) {
        rightLayout.setVisibility(VISIBLE);
        iv_right_red.setVisibility(showRightMarkIcon ? VISIBLE : INVISIBLE);
        iv_right.setVisibility(VISIBLE);
        tv_right.setVisibility(INVISIBLE);
        return this;
    }

    /**
     * @param showRightTxt 右边textview
     */
    public TitleView showRightTxt(boolean showRightTxt) {
        rightLayout.setVisibility(VISIBLE);
        iv_right_red.setVisibility(!showRightTxt ? VISIBLE : INVISIBLE);
        iv_right.setVisibility(!showRightTxt ? VISIBLE : INVISIBLE);
        tv_right.setVisibility(!showRightTxt ? INVISIBLE : VISIBLE);
        return this;
    }

    /**
     * 设置右侧点击事件
     *
     * @param clickListner clickListner
     */
    public TitleView setRightClickListner(@Nullable OnClickListener clickListner) {
        rightLayout.setOnClickListener(clickListner);
        return this;
    }

    /**
     * 设置title
     *
     * @param charSequence charSequence
     */
    public TitleView setTitle(CharSequence charSequence) {
        if (null != charSequence)
            tv_title.setText(charSequence);
        return this;
    }

    /**
     * 设置title
     *
     * @param res res
     */
    public TitleView setTitle(@StringRes int res) {
        tv_title.setText(res);
        return this;
    }

    /**
     * 设置右边 textview
     *
     * @param charSequence charSequence
     */
    public TitleView setRightTxt(CharSequence charSequence) {
        showRightTxt(true);
        if (null != charSequence)
            tv_right.setText(charSequence);
        return this;
    }

    /**
     * 设置右边 textview
     *
     * @param res res
     */
    public TitleView setRightTxt(@StringRes int res) {
        showRightTxt(true);
        tv_right.setText(res);
        return this;
    }

    /**
     * 设置右边 textview  颜色
     *
     * @param res res
     */
    public TitleView setRightTxtColor(int res) {
        showRightTxt(true);
        tv_right.setTextColor(res);
        return this;
    }

    /**
     * 设置title
     *
     * @param resId resId
     */
    public TitleView setImageDrawableRes(@DrawableRes int resId) {
        if (resId > 0)
            iv_right.setImageResource(resId);
        return this;
    }

    public RelativeLayout getRightLayout() {
        return rightLayout;
    }
}
