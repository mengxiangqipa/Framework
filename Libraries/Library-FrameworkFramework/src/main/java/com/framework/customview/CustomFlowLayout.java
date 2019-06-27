package com.framework.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import java.util.ArrayList;

/**
 * 流式布局
 *
 * @author YobertJomi
 * className CustomFlowLayout
 * created at  2017/9/12  9:22
 */
public class CustomFlowLayout extends LinearLayout {
    OnCheckedChangeListener onCheckedChangeListener;
    OnSingleCheckedChangeListener onSingleCheckedChangeListener;
    OnItemClickListener onItemClickListener;
    private boolean checkable;
    /**
     * 存储所有的子View
     */
    private ArrayList<ArrayList<View>> listAllChildViews;
    /**
     * destination:容器 --装载LinearLayout
     */
    private ArrayList<View> listLineViews;
    /**
     * 存储每一行的高度
     */
    private ArrayList<Integer> listLineHeight;
    private ArrayList<CompoundButton> listCompoundButton = new ArrayList<CompoundButton>();//存储CompoundButton
    CustomOnCheckedChangeListener mOnCheckedChangeListener = new CustomOnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            super.onCheckedChanged(compoundButton, b);
        }
    };

    public CustomFlowLayout(Context context) {
        this(context, null);
    }

    public CustomFlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomFlowLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * <pre>
     *     @param views 数组
     *     @param  checkable 布局为checkbox时，设置为true可响应oncheckchangedlistener
     *     lastModified 2016/7/20  17:09
     *     </pre>
     */
    public void addData(final View[] views, boolean checkable) {
        try {
            this.checkable = checkable;
            if (null != views && views.length > 0) {
                for (View view : views) {
                    if (null != view)
                        addView(view);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addData(final View[] views) {
        addData(views, false);
    }

    public void clearViews() {
        try {
            this.checkable = false;
            removeAllViews();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        listAllChildViews = new ArrayList();
        listLineViews = new ArrayList();
        listLineHeight = new ArrayList<Integer>();
        //         获取当前ViewGroup的宽度
        int width = getWidth();
        int lineWidth = 0;
        int lineHeight = 0;
        //         记录当前行的view
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            //			Log.e("yy","自定义view lineWidth: " + lineWidth+ "    lineHeight:" + lineHeight);
            View child = getChildAt(i);
            if ((child instanceof CheckBox || child instanceof RadioButton) && checkable) {
                child.setId(10000 + i);
                ((CompoundButton) child).setOnCheckedChangeListener(mOnCheckedChangeListener);
                listCompoundButton.add((CompoundButton) child);
            } else if (child instanceof CheckBox && !checkable) {
                child.setEnabled(false);
            }
            LayoutParams lp = (LayoutParams) child.getLayoutParams();
            int childWidth = child.getMeasuredWidth();
            int childHeight = child.getMeasuredHeight();
            // 如果需要换行
            if (childWidth + lineWidth + lp.leftMargin + lp.rightMargin + getPaddingLeft() + getPaddingRight() >
                    width) {//2016/11/18添加paddingLeft与paddingRight
                // 记录LineHeight
                listLineHeight.add(lineHeight);
                // 记录当前行的Views
                listAllChildViews.add(listLineViews);
                // 重置行的宽高
                lineWidth = 0;
                lineHeight = childHeight + lp.topMargin + lp.bottomMargin;
                //				Log.e("yy","自定义view lp.topMargin: " + lp.topMargin+ "    lp.bottomMargin:" + lp
                // .bottomMargin);
                // 重置view的集合
                listLineViews = new ArrayList<View>();
            }
            lineWidth += childWidth + lp.leftMargin + lp.rightMargin;
            lineHeight = Math.max(lineHeight, childHeight + lp.topMargin + lp.bottomMargin);
            listLineViews.add(child);
        }
        // 处理最后一行
        listLineHeight.add(lineHeight);
        listAllChildViews.add(listLineViews);

        MarginLayoutParams params = (MarginLayoutParams) this.getLayoutParams();
        // 设置子View的位置
        int left = getPaddingLeft();//2016/11/18初始化左边时获取paddingLeft。。aaaaaaaaaa
        // 添加marginTop
        int top = 0;//2016/11/18(水平布局时)父类topMargin,bottomMargin不应该影响子类 int top = params.topMargin
        // 获取行数
        int lineCount = listAllChildViews.size();
        for (int i = 0; i < lineCount; i++) {
            // 当前行的views和高度
            listLineViews = listAllChildViews.get(i);
            lineHeight = listLineHeight.get(i);
            for (int j = 0; j < listLineViews.size(); j++) {
                // 为每一列设置marginLeft
                View child = listLineViews.get(j);
                // 判断是否显示
                if (child.getVisibility() == View.GONE) {
                    continue;
                }
                MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
                int cLeft = left + lp.leftMargin;
                int cTop = top + lp.topMargin;
//                Log.e("yy", "自定义view: " + (i + 1) + " 行" + (j + 1) + "列_  cTop:" + cTop + "     top:" + top);
                int cRight = cLeft + child.getMeasuredWidth();
                int cBottom = cTop + child.getMeasuredHeight();
                // 进行子View进行布局
                child.layout(cLeft, cTop, cRight, cBottom);
                left += child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
                //				Log.e("yy","自定义view: " + (i + 1) + " 行" + (j + 1) + "列_left:" +left);
            }
            left = getPaddingLeft();//2016/11/18初始化左边时获取paddingLeft。。aaaaaaaaaa
            top += lineHeight;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 父控件传进来的宽度和高度以及对应的测量模式
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        //        Logger.e("流失布局11:" + sizeWidth);
        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);
        // 如果当前ViewGroup的宽高为wrap_content的情况
        int width = 0;// 自己测量的 宽度
        int height = 0;// 自己测量的高度
        // 记录每一行的宽度和高度
        int lineWidth = 0;
        int lineHeight = 0;
        // 获取子view的个数
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            // 测量子View的宽和高
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            // 得到LayoutParams
            MarginLayoutParams params = (MarginLayoutParams) child.getLayoutParams();
            // 子View占据的宽度
            int childWidth = child.getMeasuredWidth() + params.leftMargin + params.rightMargin;
            // 子View占据的高度
            int childHeight = child.getMeasuredHeight() + params.bottomMargin + params.topMargin;
            // 换行时候
            if (lineWidth + childWidth > sizeWidth) {
                // 对比得到最大的宽度
                width = Math.max(width, lineWidth);
                // 重置lineWidth
                lineWidth = childWidth;
                // 记录行高
                height += lineHeight;
                lineHeight = childHeight;
            } else {
                // 不换行情况   // 叠加行宽
                lineWidth += childWidth;
                // 得到最大行高
                lineHeight = Math.max(lineHeight, childHeight);
            } // 处理最后一个子View的情况
            if (i == childCount - 1) {
                width = Math.max(width, lineWidth);
                height += lineHeight;
            }
        }
//        Log.e("yy", "流失布局22:" + width);
        setMeasuredDimension(modeWidth == MeasureSpec.EXACTLY ? sizeWidth : width, modeHeight == MeasureSpec.EXACTLY
                ? sizeHeight : height);
    }

    /**
     * 与当前ViewGroup对应的LayoutParams
     */
    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    /**
     * 设置多选监听
     *
     * @param onCheckedChangeListener 设置多选监听
     */
    public void setOnCheckedChangeListener(OnCheckedChangeListener onCheckedChangeListener) {
        this.onCheckedChangeListener = onCheckedChangeListener;
    }

    /**
     * 设置单选监听
     *
     * @param onSingleCheckedChangeListener 设置单选监听
     */
    public void setOnSingleCheckedChangeListener(OnSingleCheckedChangeListener onSingleCheckedChangeListener) {
        this.onSingleCheckedChangeListener = onSingleCheckedChangeListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnCheckedChangeListener {
        void onCheckedChanged(int position, boolean b, CompoundButton compoundButton);
    }

    public interface OnSingleCheckedChangeListener {
        void onSingleCheckedChanged(int position, boolean b, CompoundButton compoundButton);
    }

    public interface OnItemClickListener {
        void onItemClicked(int position);
    }

    class CustomOnCheckedChangeListener implements CompoundButton.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            if (compoundButton.getId() >= 10000 && compoundButton.getId() < 40000) {
                int position = compoundButton.getId() % 10000;
                if ((getContext() instanceof OnCheckedChangeListener || onCheckedChangeListener != null) &&
                        compoundButton instanceof CheckBox) {
                    if (getContext() instanceof OnCheckedChangeListener) {
                        ((OnCheckedChangeListener) getContext()).onCheckedChanged(position, b, compoundButton);
                    } else {
                        onCheckedChangeListener.onCheckedChanged(position, b, compoundButton);
                    }
                } else if (b && (getContext() instanceof OnSingleCheckedChangeListener ||
                        onSingleCheckedChangeListener != null)
                        && compoundButton instanceof RadioButton) {
                    if (listCompoundButton.size() > 0) {
                        for (CompoundButton bt : listCompoundButton) {
                            if (bt == compoundButton) {
                                bt.setChecked(true);
                            } else {
                                bt.setChecked(false);
                            }
                        }
                    }
                    if (getContext() instanceof OnSingleCheckedChangeListener) {

                        ((OnSingleCheckedChangeListener) getContext()).onSingleCheckedChanged(position, true,
                                compoundButton);
                    } else {
                        onSingleCheckedChangeListener.onSingleCheckedChanged(position, true, compoundButton);
                    }
                } else if ((getContext() instanceof OnItemClickListener || onItemClickListener != null)) {
                    if (getContext() instanceof OnItemClickListener) {
                        ((OnItemClickListener) getContext()).onItemClicked(position);
                    } else {
                        onItemClickListener.onItemClicked(position);
                    }
                }
            }
        }
    }
}
