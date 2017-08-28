package com.library.adapter_listview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

public abstract class CommonAdapter<T> extends BaseAdapter {
    protected final int mItemLayoutId;
    public List<T> mDatas;
    protected LayoutInflater mInflater;
    protected Context mContext;

    public CommonAdapter(Context context, List<T> mDatas,
                         int itemLayoutId) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(mContext);
        this.mDatas = mDatas;
        this.mItemLayoutId = itemLayoutId;
    }


    @Override
    public int getCount() {
        return null == mDatas ? 0 : mDatas.size();
    }

    /**
     * @param viewHolder
     * @param view
     * @param item
     * @param position
     * @param parent
     * @return 返回为null则getview用默认layoutid，or使用返回的view
     */
    public abstract View getListItemview(ViewHolder viewHolder, View view,
                                         T item, int position, ViewGroup parent);

    @Override
    public T getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder = getViewHolder(position, convertView,
                parent);
        View view = getListItemview(viewHolder, viewHolder.getConvertView(),
                getItem(position), position, parent);
        if (view == null) {
            return viewHolder.getConvertView();
        } else {
            return view;
        }
    }

    public ViewHolder getViewHolder(int position, View convertView,
                                    ViewGroup parent) {
        return ViewHolder.get(mContext, convertView, parent, mItemLayoutId,
                position);
    }

    public void addDatas(List list) {// 数据追加
        if (mDatas != null)
            mDatas.addAll(list);
        this.notifyDataSetChanged();
    }

    public void add(int position, T t) {
        mDatas.add(position, t);
    }
}
