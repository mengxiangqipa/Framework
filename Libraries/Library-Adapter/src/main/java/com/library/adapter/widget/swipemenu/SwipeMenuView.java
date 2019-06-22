package com.library.adapter.widget.swipemenu;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.library.adapter.Controller;
import com.library.adapter.listener.OnItemMenuClickListener;

import java.util.List;

/**
 * @author YobertJomi
 * className SwipeMenuView
 * created at  2019/6/22  23:59
 */
public class SwipeMenuView extends LinearLayout implements View.OnClickListener {

    private RecyclerView.ViewHolder mViewHolder;

    private OnItemMenuClickListener mItemClickListener;

    public SwipeMenuView(Context context) {
        this(context, null);
    }

    public SwipeMenuView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwipeMenuView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setGravity(Gravity.CENTER_VERTICAL);
    }

    public void createMenu(RecyclerView.ViewHolder viewHolder, SwipeMenu swipeMenu, Controller controller,
                           int direction, OnItemMenuClickListener itemClickListener) {
        removeAllViews();

        this.mViewHolder = viewHolder;
        this.mItemClickListener = itemClickListener;

        List<SwipeMenuItem> items = swipeMenu.getMenuItems();
        for (int i = 0; i < items.size(); i++) {
            SwipeMenuItem item = items.get(i);

            LayoutParams params = new LayoutParams(item.getWidth(), item.getHeight());
            params.weight = item.getWeight();
            LinearLayout parent = new LinearLayout(getContext());
            parent.setId(i);
            parent.setGravity(Gravity.CENTER);
            parent.setOrientation(VERTICAL);
            parent.setLayoutParams(params);
            ViewCompat.setBackground(parent, item.getBackground());
            parent.setOnClickListener(this);
            addView(parent);

            SwipeMenuBridge menuBridge = new SwipeMenuBridge(controller, direction, i);
            parent.setTag(menuBridge);

            if (item.getImage() != null) {
                ImageView iv = createIcon(item);
                parent.addView(iv);
            }

            if (!TextUtils.isEmpty(item.getText())) {
                TextView tv = createTitle(item);
                parent.addView(tv);
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (mItemClickListener != null) {
            mItemClickListener.onItemClick((SwipeMenuBridge) v.getTag(), mViewHolder.getAdapterPosition());
        }
    }

    private ImageView createIcon(SwipeMenuItem item) {
        ImageView imageView = new ImageView(getContext());
        imageView.setImageDrawable(item.getImage());
        return imageView;
    }

    private TextView createTitle(SwipeMenuItem item) {
        TextView textView = new TextView(getContext());
        textView.setText(item.getText());
        textView.setGravity(Gravity.CENTER);
        int textSize = item.getTextSize();
        if (textSize > 0) textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
        ColorStateList textColor = item.getTitleColor();
        if (textColor != null) textView.setTextColor(textColor);
        int textAppearance = item.getTextAppearance();
        if (textAppearance != 0) TextViewCompat.setTextAppearance(textView, textAppearance);
        Typeface typeface = item.getTextTypeface();
        if (typeface != null) textView.setTypeface(typeface);
        return textView;
    }
}