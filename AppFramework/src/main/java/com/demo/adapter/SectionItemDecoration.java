package com.demo.adapter;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.ColorRes;
import android.support.annotation.FloatRange;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.View;

/**
 * 顶部悬停的ItemDecoration
 * 对瀑布流似乎无效StaggeredGridLayoutManager
 *     @author Yangjie
 *     className SectionDecoration
 *     created at  2017/1/17  12:10
 */
@SuppressWarnings("unused")
public class SectionItemDecoration extends RecyclerView.ItemDecoration
{
	private SectionCallBack callback;
	private TextPaint textPaint;
	private Paint backgroundPaint;
	/**
	 * 浮条的高度
	 */
	private int sectionHeight = 100;
	//文字左边距
	private int textLeftMargin;
	private Paint.FontMetrics fontMetrics;

	public SectionItemDecoration(@NonNull SectionCallBack callback)
	{
		this.callback = callback;
		//设置悬浮栏的画笔---backgroundPaint
		backgroundPaint = new Paint();
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
	 * @param context context
	 * @param color color
	 */
	@SuppressWarnings("deprecation")
	public SectionItemDecoration setBackgroudColor(@NonNull Context context, @ColorRes int color)
	{
		if (null != backgroundPaint)
		{
			backgroundPaint.setColor(context.getResources().getColor(color));
		}
		return this;
	}

	/**
	 * 设置字体颜色
	 * @param context context
	 * @param color color
	 */
	@SuppressWarnings("deprecation")
	public SectionItemDecoration setTextColor(@NonNull Context context, @ColorRes int color)
	{
		if (null != textPaint)
		{
			textPaint.setColor(context.getResources().getColor(color));
		}
		return this;
	}

	/**
	 * 设置字体大小px
	 * @param textSize textSize
	 */
	public SectionItemDecoration setTextSize(@FloatRange(from = 2, to = 100) float textSize)
	{
		if (null != textPaint)
		{
			textPaint.setTextSize(textSize);
			fontMetrics = textPaint.getFontMetrics();
		}
		return this;
	}

	/**
	 * 设置浮条高度px
	 */
	public SectionItemDecoration setSectionHeight(@IntRange(from = 1) int sectionHeight)
	{
		this.sectionHeight = sectionHeight;
		return this;
	}

	/**
	 *设置文字的左边距px
	 * @param textLeftMargin textLeftMargin
	 */
	public SectionItemDecoration setTextLeftMargin(@IntRange(from = 0) int textLeftMargin)
	{
		this.textLeftMargin = textLeftMargin;
		return this;
	}

	private int getSpanCount(RecyclerView parent)
	{
		// 列数
		int spanCount = 1;
		RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
		if (layoutManager instanceof GridLayoutManager)
		{

			spanCount = ((GridLayoutManager) layoutManager).getSpanCount();
		} else if (layoutManager instanceof StaggeredGridLayoutManager)
		{
			spanCount = ((StaggeredGridLayoutManager) layoutManager).getSpanCount();
		}
		return spanCount;
	}

	@Override
	public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state)
	{
		UniversalAdapter adapter = (UniversalAdapter) parent.getAdapter();
		int position = parent.getChildAdapterPosition(view);
		String sectionId = callback.getSectionId(position - adapter.getHeaderCount());
		if (TextUtils.isEmpty(sectionId))
			return;
		//只有是同一组的第一个才显示悬浮栏
		int dex = getSpanCount(parent);
		if (isFirstInSection(position - adapter.getHeaderCount()) || (dex>1&&isFirstInSection((position - adapter.getHeaderCount()) / dex * dex)))
		{//   ||或判断主要是针对gridLayoutManager,左边第一个是新Section，该行都留outRect.top = sectionHeight
			outRect.top = sectionHeight;
			if (TextUtils.isEmpty(callback.getSectionTitle(position - adapter.getHeaderCount())))
			{
				outRect.top = 0;
			}
		} else
		{
			outRect.top = 0;
		}
	}

	@Override
	public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state)
	{
		int itemCount = state.getItemCount();
		int childCount = parent.getChildCount();
		int left = parent.getPaddingLeft();
		int right = parent.getWidth() - parent.getPaddingRight();

		String preSectionId;//前一个id
		String currentSectionId = null;//当前sectionId
		UniversalAdapter adapter = (UniversalAdapter) parent.getAdapter();
		for (int i = 0; i < childCount; i++)
		{
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
			if (position + next < itemCount)
			{
				String nextSectionId = callback.getSectionId(position + next);//下一个sectionId
				//组内最后一个view进入了header
				if (!TextUtils.equals(nextSectionId, currentSectionId) && viewBottom < textY)
				{
					textY = viewBottom;
				}
			}
			//textY - sectionHeight决定了悬浮栏绘制的高度和位置
			c.drawRect(left, textY - sectionHeight, right, textY, backgroundPaint);
			c.drawText(title, left + textLeftMargin, (2 * textY - sectionHeight - fontMetrics.top - fontMetrics.bottom) / 2, textPaint);
			//(targetRect.bottom + targetRect.top - fontMetrics.bottom - fontMetrics.top) / 2    字体基准线
		}
	}

	/**
	 * 判断是不是组中的第一个位置
	 *
	 * @param position position - adapter.getHeaderCount()  出去headers的真正position
	 * @return isFirstInGroup
	 */
	private boolean isFirstInSection(int position)
	{
		if (position < 0)
		{
			return false;
		}
		if (position == 0)
		{
			return true;
		} else
		{
			String prevSectionId = callback.getSectionId(position - 1);
			String curentSectionId = callback.getSectionId(position);
			return !TextUtils.equals(prevSectionId, curentSectionId);
		}
	}

	public interface SectionCallBack
	{//如果有header，position是从负数开始
		String getSectionId(int position);

		String getSectionTitle(int position);
	}
}
