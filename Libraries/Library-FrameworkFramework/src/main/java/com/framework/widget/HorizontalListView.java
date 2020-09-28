package com.framework.widget;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.Scroller;

import java.util.LinkedList;
import java.util.Queue;

/**
 * 水平listview
 *
 * @author YobertJomi
 * className HorizontalListView
 * created at  2017/9/12  9:46
 */

public class HorizontalListView extends AdapterView<ListAdapter> {
    public boolean mAlwaysOverrideTouch = true;
    protected ListAdapter mAdapter;
    protected int mCurrentX;
    protected int mNextX;
    protected Scroller mScroller;
    private int mLeftViewIndex = -1;
    private int mRightViewIndex = 0;
    private int mMaxX = Integer.MAX_VALUE;
    private int mDisplayOffset = 0;
    private GestureDetector mGesture;
    private Queue<View> mRemovedViewQueue = new LinkedList();
    private OnItemSelectedListener mOnItemSelected;
    private OnItemClickListener mOnItemClicked;
    private OnItemLongClickListener mOnItemLongClicked;
    private boolean mDataChanged = false;
    private GestureDetector.OnGestureListener mOnGesture =
            new GestureDetector.SimpleOnGestureListener() {
        public boolean onDown(MotionEvent e) {
            return HorizontalListView.this.onDown(e);
        }

        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return HorizontalListView.this.onFling(e1, e2, velocityX, velocityY);
        }

        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            HorizontalListView var5 = HorizontalListView.this;
            synchronized (HorizontalListView.this) {
                HorizontalListView.this.mNextX += (int) distanceX;
            }

            HorizontalListView.this.requestLayout();
            return true;
        }

        public boolean onSingleTapConfirmed(MotionEvent e) {
            for (int i = 0; i < HorizontalListView.this.getChildCount(); ++i) {
                View child = HorizontalListView.this.getChildAt(i);
                if (this.isEventWithinView(e, child)) {
                    if (HorizontalListView.this.mOnItemClicked != null) {
                        HorizontalListView.this.mOnItemClicked.onItemClick(HorizontalListView.this, child,
                                HorizontalListView.this.mLeftViewIndex + 1 + i,
                                HorizontalListView.this.mAdapter
                                        .getItemId(HorizontalListView.this.mLeftViewIndex + 1 + i));
                    }

                    if (HorizontalListView.this.mOnItemSelected != null) {
                        HorizontalListView.this.mOnItemSelected.onItemSelected(HorizontalListView.this, child,
                                HorizontalListView.this.mLeftViewIndex + 1 + i,
                                HorizontalListView.this.mAdapter
                                        .getItemId(HorizontalListView.this.mLeftViewIndex + 1 + i));
                    }
                    break;
                }
            }

            return true;
        }

        public void onLongPress(MotionEvent e) {
            int childCount = HorizontalListView.this.getChildCount();

            for (int i = 0; i < childCount; ++i) {
                View child = HorizontalListView.this.getChildAt(i);
                if (this.isEventWithinView(e, child)) {
                    if (HorizontalListView.this.mOnItemLongClicked != null) {
                        HorizontalListView.this.mOnItemLongClicked.onItemLongClick(HorizontalListView.this, child,
                                HorizontalListView.this.mLeftViewIndex + 1 + i,
                                HorizontalListView.this.mAdapter
                                        .getItemId(HorizontalListView.this.mLeftViewIndex + 1 + i));
                    }
                    break;
                }
            }
        }

        private boolean isEventWithinView(MotionEvent e, View child) {
            Rect viewRect = new Rect();
            int[] childPosition = new int[2];
            child.getLocationOnScreen(childPosition);
            int left = childPosition[0];
            int right = left + child.getWidth();
            int top = childPosition[1];
            int bottom = top + child.getHeight();
            viewRect.set(left, top, right, bottom);
            return viewRect.contains((int) e.getRawX(), (int) e.getRawY());
        }
    };
    private DataSetObserver mDataObserver = new DataSetObserver() {
        public void onChanged() {
            HorizontalListView var1 = HorizontalListView.this;
            synchronized (HorizontalListView.this) {
                HorizontalListView.this.mDataChanged = true;
            }

            HorizontalListView.this.invalidate();
            HorizontalListView.this.requestLayout();
        }

        public void onInvalidated() {
            HorizontalListView.this.reset();
            HorizontalListView.this.invalidate();
            HorizontalListView.this.requestLayout();
        }
    };

    public HorizontalListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.initView();
    }

    private synchronized void initView() {
        this.mLeftViewIndex = -1;
        this.mRightViewIndex = 0;
        this.mDisplayOffset = 0;
        this.mCurrentX = 0;
        this.mNextX = 0;
        this.mMaxX = 2147483647;
        this.mScroller = new Scroller(this.getContext());
        this.mGesture = new GestureDetector(this.getContext(), this.mOnGesture);
    }

    public void setOnItemSelectedListener(OnItemSelectedListener listener) {
        this.mOnItemSelected = listener;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClicked = listener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.mOnItemLongClicked = listener;
    }

    public ListAdapter getAdapter() {
        return this.mAdapter;
    }

    public void setAdapter(ListAdapter adapter) {
        if (this.mAdapter != null) {
            this.mAdapter.unregisterDataSetObserver(this.mDataObserver);
        }

        this.mAdapter = adapter;
        this.mAdapter.registerDataSetObserver(this.mDataObserver);
        this.reset();
    }

    public View getSelectedView() {
        return null;
    }

    private synchronized void reset() {
        this.initView();
        this.removeAllViewsInLayout();
        this.requestLayout();
    }

    private void addAndMeasureChild(View child, int viewPos) {
        LayoutParams params = child.getLayoutParams();
        if (params == null) {
            params = new LayoutParams(-1, -1);
        }

        this.addViewInLayout(child, viewPos, params, true);
        child.measure(MeasureSpec.makeMeasureSpec(this.getWidth(), MeasureSpec.AT_MOST),
                MeasureSpec.makeMeasureSpec
                (this.getHeight(), MeasureSpec.AT_MOST));
    }

    protected synchronized void onLayout(boolean changed, int left, int top, int right,
                                         int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (this.mAdapter != null) {
            int dx;
            if (this.mDataChanged) {
                dx = this.mCurrentX;
                this.initView();
                this.removeAllViewsInLayout();
                this.mNextX = dx;
                this.mDataChanged = false;
            }

            if (this.mScroller.computeScrollOffset()) {
                dx = this.mScroller.getCurrX();
                this.mNextX = dx;
            }

            if (this.mNextX <= 0) {
                this.mNextX = 0;
                this.mScroller.forceFinished(true);
            }

            if (this.mNextX >= this.mMaxX) {
                this.mNextX = this.mMaxX;
                this.mScroller.forceFinished(true);
            }

            dx = this.mCurrentX - this.mNextX;
            this.removeNonVisibleItems(dx);
            this.fillList(dx);
            this.positionItems(dx);
            this.mCurrentX = this.mNextX;
            if (!this.mScroller.isFinished()) {
                this.post(new Runnable() {
                    public void run() {
                        HorizontalListView.this.requestLayout();
                    }
                });
            }
        }
    }

    private void fillList(int dx) {
        int edge = 0;
        View child = this.getChildAt(this.getChildCount() - 1);
        if (child != null) {
            edge = child.getRight();
        }

        this.fillListRight(edge, dx);
        edge = 0;
        child = this.getChildAt(0);
        if (child != null) {
            edge = child.getLeft();
        }

        this.fillListLeft(edge, dx);
    }

    private void fillListRight(int rightEdge, int dx) {
        for (; rightEdge + dx < this.getWidth() && this.mRightViewIndex < this.mAdapter.getCount(); ++this
                .mRightViewIndex) {
            View child = this.mAdapter.getView(this.mRightViewIndex,
                    (View) this.mRemovedViewQueue.poll(), this);
            this.addAndMeasureChild(child, -1);
            rightEdge += child.getMeasuredWidth();
            if (this.mRightViewIndex == this.mAdapter.getCount() - 1) {
                this.mMaxX = this.mCurrentX + rightEdge - this.getWidth();
            }

            if (this.mMaxX < 0) {
                this.mMaxX = 0;
            }
        }
    }

    private void fillListLeft(int leftEdge, int dx) {
        while (leftEdge + dx > 0 && this.mLeftViewIndex >= 0) {
            View child = this.mAdapter.getView(this.mLeftViewIndex,
                    (View) this.mRemovedViewQueue.poll(), this);
            this.addAndMeasureChild(child, 0);
            leftEdge -= child.getMeasuredWidth();
            --this.mLeftViewIndex;
            this.mDisplayOffset -= child.getMeasuredWidth();
        }
    }

    private void removeNonVisibleItems(int dx) {
        View child;
        for (child = this.getChildAt(0); child != null && child.getRight() + dx <= 0; child =
                this.getChildAt(0)) {
            this.mDisplayOffset += child.getMeasuredWidth();
            this.mRemovedViewQueue.offer(child);
            this.removeViewInLayout(child);
            ++this.mLeftViewIndex;
        }

        for (child = this.getChildAt(this.getChildCount() - 1); child != null && child.getLeft() + dx >= this
                .getWidth(); child = this.getChildAt(this.getChildCount() - 1)) {
            this.mRemovedViewQueue.offer(child);
            this.removeViewInLayout(child);
            --this.mRightViewIndex;
        }
    }

    private void positionItems(int dx) {
        if (this.getChildCount() > 0) {
            this.mDisplayOffset += dx;
            int left = this.mDisplayOffset;

            for (int i = 0; i < this.getChildCount(); ++i) {
                View child = this.getChildAt(i);
                int childWidth = child.getMeasuredWidth();
                child.layout(left, 0, left + childWidth, child.getMeasuredHeight());
                left += childWidth + child.getPaddingRight();
            }
        }
    }

    public synchronized void scrollTo(int x) {
        this.mScroller.startScroll(this.mNextX, 0, x - this.mNextX, 0);
        this.requestLayout();
    }

    public boolean dispatchTouchEvent(MotionEvent ev) {
        boolean handled = super.dispatchTouchEvent(ev);
        handled |= this.mGesture.onTouchEvent(ev);
        return handled;
    }

    protected boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        synchronized (this) {
            this.mScroller.fling(this.mNextX, 0, (int) (-velocityX), 0, 0, this.mMaxX, 0, 0);
        }

        this.requestLayout();
        return true;
    }

    protected boolean onDown(MotionEvent e) {
        this.mScroller.forceFinished(true);
        return true;
    }

    public void setSelection(int position) {
    }
}

