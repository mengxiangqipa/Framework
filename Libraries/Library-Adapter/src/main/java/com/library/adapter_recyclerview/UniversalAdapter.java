package com.library.adapter_recyclerview;

import android.content.Context;
import android.graphics.Bitmap;
import androidx.annotation.CallSuper;
import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.IdRes;
import androidx.annotation.IntDef;
import androidx.annotation.IntRange;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.collection.SparseArrayCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.library.adapter.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

/**
 * recyclerView的adapter
 *
 * @author YobertJomi
 * className UniversalRecyclerViewAdapter
 * created at  2016/12/26  12:38
 */
@SuppressWarnings("unused")
public abstract class UniversalAdapter<D> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final Context context;
    private final int layoutId;
    /**
     * 普通类型
     */
    private final int VIEW_TYPE_NORMAL = 10086;
    /**
     * header类型
     */
    private final int VIEW_TYPE_HEADER = 1500000;
    /**
     * footer类型
     */
    private final int VIEW_TYPE_FOODER = 2000000;
    private List<D> list;
    private SparseArrayCompat<View> headersSparseArray = new SparseArrayCompat<>();
    private SparseArrayCompat<View> footerSparseArray = new SparseArrayCompat<>();
    private int initHeaderKey = 0;
    private int initFooterKey = 0;

    public UniversalAdapter(@NonNull Context context, @LayoutRes int layoutId,
                            @Nullable List<D> list) {
        this.context = context;
        this.layoutId = layoutId;
        this.list = list;
    }

    public void preDealItemView(View itemView) {

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        if (viewType >= VIEW_TYPE_HEADER && viewType < VIEW_TYPE_FOODER) {
            itemView = headersSparseArray.get(viewType % VIEW_TYPE_HEADER);
        } else if (viewType >= VIEW_TYPE_FOODER) {
            itemView = footerSparseArray.get(viewType % VIEW_TYPE_FOODER);
        } else {
            itemView = initItemView(viewType);
            if (null == itemView)
                itemView = LayoutInflater.from(context).inflate(initLayoutId(viewType), parent,
                        false);
            preDealItemView(itemView);
        }
        return createUniversalViewHolder(itemView);
    }

    @SuppressWarnings("unchecked")
    @CallSuper
    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        if (position >= headersSparseArray.size() && position < headersSparseArray.size() + list.size()) {
            position = position - headersSparseArray.size();
            if (null != mOnItemClickListener) {
                ((UniversalViewHolder) holder).setOnItemClickListenerWithTag(onClickListener,
                        position);
            }
            if (null != mOnItemLongClickListener) {
                ((UniversalViewHolder) holder).setOnItemLongClickListenerWithTag(onLongClickListener, position);
            }
            getItemView((UniversalViewHolder) holder, list.get(position), position);
        }
    }

    @Override
    public int getItemCount() {
        return headersSparseArray.size() + footerSparseArray.size() + (list == null ? 0 :
                list.size());
    }

    @Override
    public int getItemViewType(int position) {
        return position < headersSparseArray.size() ?
                VIEW_TYPE_HEADER + headersSparseArray.keyAt(position)
                : (position >= headersSparseArray.size() + list.size()
                ?
                VIEW_TYPE_FOODER + footerSparseArray.keyAt(position - headersSparseArray.size() - list.size())
                : initItemViewType(position - headersSparseArray.size()));
    }

    /**
     * 获取item项
     */
    protected abstract void getItemView(UniversalViewHolder viewHolder, D item, int position);

    public View initItemView(int viewType) {
        return null;
    }

    /**
     * 初始化layoutId
     *
     * @param viewType viewType
     * @return @LayoutRes int
     */
    public
    @LayoutRes
    int initLayoutId(int viewType) {
        return layoutId;
    }

    /**
     * 初始化getItemViewType
     * 多布局支持调用的方法(MultiUniversalAdapter)
     *
     * @param position position
     * @return 默认VIEW_TYPE_NORMAL
     */
    public int initItemViewType(int position) {
        return VIEW_TYPE_NORMAL;
    }

    /**
     * createUniversalViewHolder
     *
     * @param itemView viewType
     * @return RecyclerView.ViewHolder//修改
     */
    public RecyclerView.ViewHolder createUniversalViewHolder(View itemView) {
        return new UniversalViewHolder(itemView);
    }

    /**
     * 更新数据
     *
     * @param list list
     */
    public void updateData(@Nullable List<D> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    /**
     * 获取context
     *
     * @return context
     */
    public Context getContext() {
        return context;
    }

    /**
     * 获取数据源list
     */
    public List<D> getDataList() {
        return list;
    }

    public void insertData(D d) {
        insertData(list == null ? 0 : list.size(), d);
    }

    public void insertData(int index, D d) {
        insertData(index, null == list ? 1 : Math.min(15, Math.max(1, list.size() - index + 1)), d);
    }

    /**
     * 添加一条数据
     *
     * @param index     插入数据的位置
     * @param itemCount notifyItemRangeChanged刷新的数据个数,一屏的最大item个数
     *                  index+itemCount不能大于list.size()
     */
    public void insertData(int index, int itemCount, D d) {
        if (list != null) {
            if (index >= 0 && index <= list.size()) {
                if (list != null) {
                    list.add(index, d);
                    notifyItemInserted(index + headersSparseArray.size());
                }
            }
        } else {
            list = new ArrayList<>();
            list.add(d);
            notifyItemInserted(headersSparseArray.size());
        }
        if (itemCount > 0 && itemCount <= list.size() - index + 1)
            notifyItemRangeChanged(index + headersSparseArray.size(), itemCount);
    }

    public void deleteData(int index) {
        deleteData(index, null == list ? 0 : Math.min(15, Math.max(0, list.size() - index - 1)));
    }

    /**
     * 删除一条数据
     * * @param index 删除数据的位置
     *
     * @param itemCount notifyItemRangeChanged刷新的数据个数,一屏的最大item个数
     */
    public void deleteData(int index, int itemCount) {
        if (list != null) {
            if (index >= 0 && index < list.size()) {
                list.remove(index);
                notifyItemRemoved(index + headersSparseArray.size());
            }
            if (itemCount > 0 && list.size() - index > 0)//==0时表示删除最后一项，无需刷新数据
                notifyItemRangeChanged(index + headersSparseArray.size(), Math.min(itemCount,
                        list.size() - index));
        }
    }

    /**
     * 添加header,考虑添加后再移除的情况，put的position做了处理
     *
     * @param header header
     */
    public void addHeaderView(@NonNull View header) {
        headersSparseArray.put(initHeaderKey++, header);
    }

    /**
     * setAdapter之后添加header就调用这个方法
     *
     * @param header header
     */
    public void addHeaderViewAfterSetAdapter(@NonNull View header) {
        addHeaderView(header);
        notifyItemInserted(headersSparseArray.size() <= 0 ? 0 : headersSparseArray.size() - 1);
    }

    /**
     * 添加footer,考虑添加后再移除的情况，put的position做了处理
     *
     * @param footer footer
     */
    public void addFooterView(@NonNull View footer) {
        footerSparseArray.put(initFooterKey++, footer);
    }

    /**
     * setAdapter之后添加footer就调用这个方法
     *
     * @param footer footer
     */
    public void addFooterViewAfterSetAdapter(@NonNull View footer) {
        addFooterView(footer);
        notifyItemInserted((footerSparseArray.size() <= 0 ? 0 : footerSparseArray.size() - 1) + (list == null ? 0 :
                list.size()) + headersSparseArray.size());
    }

    /**
     * 移除header
     *
     * @param header header
     */
    public void removeHeaderView(@NonNull View header) {
        int index = headersSparseArray.indexOfValue(header);
        if (index >= 0) {
            headersSparseArray.removeAt(index);
            notifyItemRemoved(index);
            //notifyItemRangeChanged刷新会触发onCreateViewHolder，headerView，footerView是用sparseArray
            // 存储，在onCreateViewHolder
            // 只能取出一次，
            // 而LayoutInflater.from(context).inflate(initLayoutId(viewType), parent, false)
            // 每次都是新view，所以不能调用刷新header，footer
            //况且header,footer不需要刷新
        }
    }

    /**
     * 移除HeaderView
     *
     * @param position position
     */
    public void removeHeaderViewAtPosition(@IntRange(from = 0) int position) {
        if (headersSparseArray.size() > 0 && headersSparseArray.size() > position) {
            View header = headersSparseArray.get(headersSparseArray.keyAt(position));
            if (header != null) {
                int index = headersSparseArray.indexOfValue(header);
                if (index >= 0) {
                    headersSparseArray.removeAt(index);
                    notifyItemRemoved(index);
                }
            }
        }
    }

    /**
     * 移除所有header
     */
    public void removeAllHeaderView() {
        if (headersSparseArray.size() > 0) {
            int lenth = headersSparseArray.size();
            for (int i = 0; i < lenth; i++) {
                View header = headersSparseArray.get(headersSparseArray.keyAt(0));
                if (header != null) {
                    headersSparseArray.removeAt(0);
                    notifyItemRemoved(0);
                }
            }
        }
    }

    /**
     * 移除footer
     *
     * @param footer footer
     */
    public void removeFooterView(@NonNull View footer) {
        int index = footerSparseArray.indexOfValue(footer);
        if (index >= 0) {
            footerSparseArray.removeAt(index);
            notifyItemRemoved(headersSparseArray.size() + list.size() + index);
        }
    }

    /**
     * 移除footer
     *
     * @param position position
     */
    public void removeFooterViewAtPosition(@IntRange(from = 0) int position) {
        if (footerSparseArray.size() > 0 && footerSparseArray.size() > position) {
            View footer = footerSparseArray.get(footerSparseArray.keyAt(position));
            if (footer != null) {
                int index = footerSparseArray.indexOfValue(footer);
                if (index >= 0) {
                    footerSparseArray.removeAt(index);
                    notifyItemRemoved(headersSparseArray.size() + list.size() + index);
                }
            }
        }
    }

    /**
     * 移除所有footer
     */
    public void removeAllFooterView() {
        if (footerSparseArray.size() > 0) {
            int lenth = footerSparseArray.size();
            for (int i = 0; i < lenth; i++) {
                View footer = footerSparseArray.get(footerSparseArray.keyAt(0));
                if (footer != null) {
                    footerSparseArray.removeAt(0);
                    notifyItemRemoved(headersSparseArray.size() + list.size());
                }
            }
        }
    }

    /**
     * 获取header数量
     *
     * @return getHeaderCount
     */
    public int getHeaderCount() {
        return headersSparseArray.size();
    }

    /**
     * 获取footer数量
     *
     * @return getFooterCount
     */
    public int getFooterCount() {
        return footerSparseArray.size();
    }

    /**
     * 获取footer数量
     *
     * @return getFooterCount
     */
    public int getDataItemCount() {
        return null == list ? 0 : list.size();
    }

    /**
     * 获取header的位置
     *
     * @param header header
     * @return 当前header的位置，<0表示不存在
     */
    public int getHeaderPosition(@NonNull View header) {
        return headersSparseArray.indexOfValue(header);
    }

    /**
     * 判断是否是header
     *
     * @param position position
     * @return 判断是否是header
     */
    public boolean isHeader(@IntRange(from = 0) int position) {
        return position < getHeaderCount();
    }

    /**
     * 判断是否是footer
     *
     * @param position position
     * @return 判断是否是footer
     */
    public boolean isFooter(@IntRange(from = 0) int position) {
        return position >= getHeaderCount() + getDataItemCount();
    }

    /**
     * 获取footer的位置
     *
     * @param footer footer
     * @return 当前footer的位置，<0表示不存在
     */
    public int getFooterPosition(@NonNull View footer) {
        return footerSparseArray.indexOfValue(footer);
    }

    /**
     * 获取headers
     */
    public SparseArrayCompat<View> getHeaderArray() {
        return headersSparseArray;
    }

    /**
     * 获取footers
     */
    public SparseArrayCompat<View> getFooterArray() {
        return footerSparseArray;
    }

    /**
     * 处理GridLayoutManager--上下拉的header，footer
     *
     * @param recyclerView recyclerView
     */
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        final RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if (getItemViewType(position) >= VIEW_TYPE_HEADER) {
                        return gridLayoutManager.getSpanCount();
                    } else {
                        return resetSpanSizeLookup(position, gridLayoutManager.getSpanCount(),
                                getItemViewType
                                (position));
                    }
                }
            });
            gridLayoutManager.setSpanCount(gridLayoutManager.getSpanCount());
        }
    }

    public int resetSpanSizeLookup(int realPosition, int spanCount, int getItemViewType) {
        return 1;
    }

    /**
     * 处理StaggeredGridLayoutManager--上下拉的header，footer
     *
     * @param holder holder
     */
    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        int position = holder.getLayoutPosition();
        if (isHeader(position) || isFooter(position)) {
            ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
            if (lp != null && lp instanceof StaggeredGridLayoutManager.LayoutParams) {
                StaggeredGridLayoutManager.LayoutParams p =
                        (StaggeredGridLayoutManager.LayoutParams) lp;
                p.setFullSpan(true);
            }
        }
    }

    @IntDef({View.VISIBLE, View.INVISIBLE, View.GONE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Visibility {
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getTag(R.id.UniversalAdapterTagView) != null
                    && v.getTag(R.id.UniversalAdapterTagPosition) != null) {
                mOnItemClickListener.onItemClick((View) v.getTag(R.id.UniversalAdapterTagView),
                        (int) v.getTag(R.id.UniversalAdapterTagPosition));
            }
        }
    };

    private View.OnLongClickListener onLongClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            if (v.getTag(R.id.UniversalAdapterTagView) != null
                    && v.getTag(R.id.UniversalAdapterTagPosition) != null) {
                mOnItemLongClickListener.onItemLongClick((View) v.getTag(R.id.UniversalAdapterTagView),
                        (int) v.getTag(R.id.UniversalAdapterTagPosition));
            }
            return true;
        }
    };

    private OnItemClickListener<D> mOnItemClickListener;

    private OnItemLongClickListener<D> mOnItemLongClickListener;

    public interface OnItemClickListener<D> {
        void onItemClick(View view, int realPosition);
    }

    public interface OnItemLongClickListener<D> {
        void onItemLongClick(View view, int realPosition);
    }

    public void setOnItemClickListener(OnItemClickListener<D> mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener<D> mOnItemLongClickListener) {
        this.mOnItemLongClickListener = mOnItemLongClickListener;
    }

    public class UniversalViewHolder extends RecyclerView.ViewHolder {
        private final SparseArray<View> sparseArray;
        private View convertView;

        public UniversalViewHolder(View itemView) {
            super(itemView);
            this.convertView = itemView;
            sparseArray = new SparseArray<>();
        }

        /**
         * 通过控件的Id获取对于的控件，如果没有则加入views
         *
         * @param viewId viewId
         * @return T
         */
        @SuppressWarnings("unchecked")
        public <T extends View> T getView(@IdRes int viewId) {
            try {
                View view = sparseArray.get(viewId);
                if (view == null) {
                    view = convertView.findViewById(viewId);
                    sparseArray.put(viewId, view);
                }
                return (T) view;
            } catch (Exception e) {
                return null;
            }
        }

        public UniversalViewHolder setVisibl1e(@IdRes int viewId, @Visibility int visible) {
            View view = getView(viewId);
            if (view != null)
                view.setVisibility(visible);
            return UniversalViewHolder.this;
        }

        /**
         * 为TextView设置字符串
         *
         * @param viewId viewId
         * @param text   CharSequence
         * @return ViewHolder
         */
        public UniversalViewHolder setText(@IdRes int viewId, @Nullable CharSequence text) {
            TextView view = getView(viewId);
            if (view != null)
                view.setText(text);
            return UniversalViewHolder.this;
        }

        public UniversalViewHolder setTextColor(@IdRes int viewId, @ColorInt int color) {
            TextView view = getView(viewId);
            if (view != null)
                view.setTextColor(color);
            return UniversalViewHolder.this;
        }

        /**
         * @param viewId    viewId
         * @param scaleType ScaleType
         * @return ViewHolder
         */
        public UniversalViewHolder setScaleType(@IdRes int viewId,
                                                @NonNull ImageView.ScaleType scaleType) {
            ImageView view = getView(viewId);
            if (view != null)
                view.setScaleType(scaleType);
            return this;
        }

        /**
         * 为ImageView设置图片
         *
         * @param viewId     viewId
         * @param drawableId drawableId
         * @return ViewHolder
         */
        public UniversalViewHolder setImageResource(@IdRes int viewId,
                                                    @DrawableRes int drawableId) {
            ImageView view = getView(viewId);
            if (null != view)
                view.setImageResource(drawableId);
            return this;
        }

        /**
         * 为ImageView设置图片
         *
         * @param viewId viewId
         * @param bm     Bitmap
         * @return ViewHolder
         */
        public UniversalViewHolder setImageBitmap(@IdRes int viewId, @NonNull Bitmap bm) {
            ImageView view = getView(viewId);
            if (null != view)
                view.setImageBitmap(bm);
            return this;
        }

        /**
         * 为view设置setBackgroundColor
         *
         * @param viewId viewId
         * @param color  @ColorInt int color
         * @return UniversalViewHolder
         */
        public UniversalViewHolder setBackGroundColor(@IdRes int viewId, @ColorInt int color) {
            View view = getView(viewId);
            if (null != view)
                view.setBackgroundColor(color);
            return this;
        }

        public UniversalViewHolder setOnClickListener(@IdRes int viewId,
                                                      @NonNull View.OnClickListener
                onClickListener) {
            View view = getView(viewId);
            if (null != view)
                view.setOnClickListener(onClickListener);
            return this;
        }

        public UniversalViewHolder setOnItemClickListener(@NonNull View.OnClickListener onClickListener) {
            itemView.setOnClickListener(onClickListener);
            return this;
        }

        public UniversalViewHolder setOnItemClickListenerWithTag(@NonNull View.OnClickListener onClickListener,
                                                                 int position) {
            itemView.setTag(R.id.UniversalAdapterTagPosition, position);
            itemView.setTag(R.id.UniversalAdapterTagView, itemView);
            itemView.setOnClickListener(onClickListener);
            return this;
        }

        public UniversalViewHolder setOnItemLongClickListener(@NonNull View.OnLongClickListener onLongClickListener) {
            itemView.setOnLongClickListener(onLongClickListener);
            return this;
        }

        public UniversalViewHolder setOnItemLongClickListenerWithTag(@NonNull View.OnLongClickListener onLongClickListener, int position) {
            itemView.setTag(R.id.UniversalAdapterTagPosition, position);
            itemView.setTag(R.id.UniversalAdapterTagView, itemView);
            itemView.setOnLongClickListener(onLongClickListener);
            return this;
        }

        public UniversalViewHolder setOnTouchListener(@IdRes int viewId,
                                                      @NonNull View.OnTouchListener
                onTouchListener) {
            View view = getView(viewId);
            if (null != view)
                view.setOnTouchListener(onTouchListener);
            return this;
        }
        //在这里添加其他方法
    }
}
