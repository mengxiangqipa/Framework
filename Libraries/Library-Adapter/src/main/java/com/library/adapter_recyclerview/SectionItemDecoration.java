package com.library.adapter_recyclerview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import androidx.annotation.ColorRes;
import androidx.annotation.FloatRange;
import androidx.annotation.IntDef;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.View;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 顶部悬停的ItemDecoration
 * 对瀑布流似乎无效StaggeredGridLayoutManager
 *
 * @author YobertJomi
 * className SectionDecoration
 * created at  2017/1/17  12:10
 */
@SuppressWarnings("unused")
public class SectionItemDecoration extends RecyclerView.ItemDecoration {
    private SectionItemDecoration.SectionCallBack callback;
    private TextPaint textPaint;
    private Paint backgroundPaint;
    private Paint dividerPaint;
    /**
     * 浮条的高度
     */
    private int sectionHeight = 100;
    //文字左边距
    private int textLeftMargin;
    private Paint.FontMetrics fontMetrics;

    public static final int NONE = 0;
    public static final int TOP = 1;
    public static final int BOTTOM = 2;
    public static final int BOTH = 3;

    /**
     * 悬停栏的模式
     */
    private int sectionDividerMode = NONE;
    /**
     * 悬停栏的的 顶部分割线高度
     */
    private int sectionTopDividerHeight = 0;
    /**
     * 悬停栏的的 顶部分割线 左边边距
     */
    private int sectionTopDividerLeftMargin = 0;
    /**
     * 悬停栏的的 顶部分割线 右边边距
     */
    private int sectionTopDividerRightMargin = 0;
    /**
     * 悬停栏的的 底部部分割线高度
     */
    private int sectionBottomDividerHeight = 0;
    /**
     * 悬停栏的的 底部分割线 左边边距
     */
    private int sectionBottomDividerLeftMargin = 0;
    /**
     * 悬停栏的的 底部分割线 右边边距
     */
    private int sectionBottomDividerRightMargin = 0;

    @IntDef(value = {NONE, TOP, BOTTOM, BOTH})
    @Retention(RetentionPolicy.SOURCE)
    @interface SectionDividerLineMode {

    }

    public SectionItemDecoration(@NonNull SectionItemDecoration.SectionCallBack callback) {
        this.callback = callback;
        //设置悬浮栏的画笔---backgroundPaint
        backgroundPaint = new Paint();
        //设置悬浮栏的分割线画笔---dividerPaint
        dividerPaint = new Paint();
        //设置悬浮栏中文本的画笔
        textPaint = new TextPaint();
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(40);
        textPaint.setColor(Color.DKGRAY);
        textPaint.setTextAlign(Paint.Align.LEFT);

        fontMetrics = textPaint.getFontMetrics();
    }

    /**
     * 设置背景颜色
     *
     * @param context context
     * @param color   color
     */
    @SuppressWarnings("deprecation")
    public SectionItemDecoration setBackgroudColor(@NonNull Context context, @ColorRes int color) {
        if (null != backgroundPaint) {
            backgroundPaint.setColor(context.getResources().getColor(color));
        }
        return this;
    }

    /**
     * 设置分割线颜色
     *
     * @param context context
     * @param color   color
     */
    @SuppressWarnings("deprecation")
    public SectionItemDecoration setTopDividerColor(@NonNull Context context, @ColorRes int color) {
        if (null != dividerPaint) {
            dividerPaint.setColor(context.getResources().getColor(color));
        }
        return this;
    }

    @SuppressWarnings("deprecation")
    public SectionItemDecoration setBottomDividerColor(@NonNull Context context, @ColorRes int color) {
        if (null != dividerPaint) {
            dividerPaint.setColor(context.getResources().getColor(color));
        }
        return this;
    }

    /**
     * 设置字体颜色
     *
     * @param context context
     * @param color   color
     */
    @SuppressWarnings("deprecation")
    public SectionItemDecoration setTextColor(@NonNull Context context, @ColorRes int color) {
        if (null != textPaint) {
            textPaint.setColor(context.getResources().getColor(color));
        }
        return this;
    }

    /**
     * 设置字体大小px
     *
     * @param textSize textSize
     */
    public SectionItemDecoration setTextSize(@FloatRange(from = 2, to = 100) float textSize) {
        if (null != textPaint) {
            textPaint.setTextSize(textSize);
            fontMetrics = textPaint.getFontMetrics();
        }
        return this;
    }

    /**
     * 设置浮条高度px
     */
    public SectionItemDecoration setSectionHeight(@IntRange(from = 1) int sectionHeight) {
        this.sectionHeight = sectionHeight;
        return this;
    }

    /**
     * 设置悬停兰的分割线样式
     */
    public SectionItemDecoration setSectionDividerMode(@SectionDividerLineMode int sectionDividerMode) {
        this.sectionDividerMode = sectionDividerMode;
        return this;
    }

    /**
     * 设置文字的左边距px
     *
     * @param textLeftMargin textLeftMargin
     */
    public SectionItemDecoration setTextLeftMargin(@IntRange(from = 0) int textLeftMargin) {
        this.textLeftMargin = textLeftMargin;
        return this;
    }

    public SectionItemDecoration setSectionTopDividerHeight(int sectionTopDividerHeight) {
        this.sectionTopDividerHeight = sectionTopDividerHeight;
        return this;
    }

    public SectionItemDecoration setSectionTopDividerLeftMargin(int sectionTopDividerLeftMargin) {
        this.sectionTopDividerLeftMargin = sectionTopDividerLeftMargin;
        return this;
    }

    public SectionItemDecoration setSectionTopDividerRightMargin(int sectionTopDividerRightMargin) {
        this.sectionTopDividerRightMargin = sectionTopDividerRightMargin;
        return this;
    }

    public SectionItemDecoration setSectionBottomDividerHeight(int sectionBottomDividerHeight) {
        this.sectionBottomDividerHeight = sectionBottomDividerHeight;
        return this;
    }

    public SectionItemDecoration setSectionBottomDividerLeftMargin(int sectionBottomDividerLeftMargin) {
        this.sectionBottomDividerLeftMargin = sectionBottomDividerLeftMargin;
        return this;
    }

    public SectionItemDecoration setSectionBottomDividerRightMargin(int sectionBottomDividerRightMargin) {
        this.sectionBottomDividerRightMargin = sectionBottomDividerRightMargin;
        return this;
    }

    public TextPaint getTextPaint() {
        return textPaint;
    }

    private int getSpanCount(RecyclerView parent) {
        // 列数
        int spanCount = 1;
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            spanCount = ((GridLayoutManager) layoutManager).getSpanCount();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            spanCount = ((StaggeredGridLayoutManager) layoutManager).getSpanCount();
        }
        return spanCount;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        UniversalAdapter adapter = (UniversalAdapter) parent.getAdapter();
        int position = parent.getChildAdapterPosition(view);
        String sectionId = callback.getSectionId(position - adapter.getHeaderCount());
        if (TextUtils.isEmpty(sectionId))
            return;
        //只有是同一组的第一个才显示悬浮栏
        int dex = getSpanCount(parent);
        if (isFirstInSection(position - adapter.getHeaderCount())
                || (dex > 1 && parent.getLayoutManager() instanceof GridLayoutManager
                && isFirstInSection((position - adapter.getHeaderCount()) - ((GridLayoutManager) parent
                .getLayoutManager()).
                getSpanSizeLookup().getSpanIndex(position, dex)))) {// ||或判断主要是针对gridLayoutManager,
            // 左边第一个是新Section，该行都留outRect.top =sectionHeight
            outRect.top = sectionHeight;
            if (TextUtils.isEmpty(callback.getSectionTitle(position - adapter.getHeaderCount()))) {
                outRect.top = 0;
            }
        } else {
            outRect.top = 0;
        }
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int itemCount = state.getItemCount();
        int childCount = parent.getChildCount();
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();

        String preSectionId;//前一个id
        String currentSectionId = null;//当前sectionId
        UniversalAdapter adapter = (UniversalAdapter) parent.getAdapter();
        for (int i = 0; i < childCount; i++) {
            View view = parent.getChildAt(i);
            int position = parent.getChildAdapterPosition(view);
            position = position - adapter.getHeaderCount();
            preSectionId = currentSectionId;
            currentSectionId = callback.getSectionId(position);
            if (TextUtils.isEmpty(currentSectionId) || TextUtils.equals(currentSectionId, preSectionId))
                continue;
            String title = callback.getSectionTitle(position);
            if (TextUtils.isEmpty(title))
                continue;
            int viewBottom = view.getBottom();
            float textY = Math.max(sectionHeight, view.getTop());
            int next = getSpanCount(parent);
            //下一个和当前不一样移动当前
            if (position + next < itemCount) {
                String nextSectionId = callback.getSectionId(position + next);//下一个sectionId
                //组内最后一个view进入了header
                if (!TextUtils.equals(nextSectionId, currentSectionId) && viewBottom < textY) {
                    textY = viewBottom;
                }
            }
            //textY - sectionHeight决定了悬浮栏绘制的高度和位置
            c.drawRect(left, textY - sectionHeight, right, textY, backgroundPaint);
            c.drawText(title, left + textLeftMargin, (2 * textY - sectionHeight - fontMetrics.top - fontMetrics
                    .bottom) / 2, textPaint);
            if (sectionDividerMode == TOP || sectionDividerMode == BOTH) {
                c.drawLine(left+sectionTopDividerLeftMargin,textY - sectionHeight,right-sectionTopDividerRightMargin,textY - sectionHeight,dividerPaint);
            }
            if (sectionDividerMode == BOTTOM || sectionDividerMode == BOTH) {
                c.drawLine(left+sectionBottomDividerLeftMargin,textY,right-sectionBottomDividerRightMargin,textY,dividerPaint);
            }
            //(targetRect.bottom + targetRect.top - fontMetrics.bottom - fontMetrics.top) / 2    字体基准线
        }
    }

    /**
     * 判断是不是组中的第一个位置
     *
     * @param position position - adapter.getHeaderCount()  出去headers的真正position
     * @return isFirstInGroup
     */
    private boolean isFirstInSection(int position) {
        if (position < 0) {
            return false;
        }
        if (position == 0) {
            return true;
        } else {
            String prevSectionId = callback.getSectionId(position - 1);
            String curentSectionId = callback.getSectionId(position);
            return !TextUtils.equals(prevSectionId, curentSectionId);
        }
    }

    public SectionItemDecoration.SectionCallBack getSectionCallBack() {
        return callback;
    }

    public interface SectionCallBack {//如果有header，position是从负数开始

        String getSectionId(int position);

        String getSectionTitle(int position);
    }
}
