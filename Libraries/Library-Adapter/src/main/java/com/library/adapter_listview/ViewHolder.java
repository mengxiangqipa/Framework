package com.library.adapter_listview;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

public class ViewHolder {
    private final SparseArray<View> mViews;
    private int mPosition;
    private View mConvertView;

    private ViewHolder(Context context, ViewGroup parent, int layoutId,
                       int position) {
        this.mPosition = position;
        this.mViews = new SparseArray<View>();
        mConvertView = LayoutInflater.from(context).inflate(layoutId, parent,
                false);
        // setTag
        mConvertView.setTag(this);
    }

    /**
     * 拿到一个ViewHolder对象
     *
     * @param context
     * @param convertView
     * @param parent
     * @param layoutId
     * @param position
     * @return
     */
    public static ViewHolder get(Context context, View convertView,
                                 ViewGroup parent, int layoutId, int position) {
        // MLog.i("ViewHolder:position:"+position);
        // MLog.i("ViewHolder:layoutId:"+layoutId);
        // MLog.i("ViewHolder:(parent==null):"+(parent==null));
        // MLog.i("ViewHolder:(convertView==null):"+(convertView==null));
        if (convertView == null) {
            return new ViewHolder(context, parent, layoutId, position);
        }
        // //
        // MLog.i("ViewHolder:(convertView.getTag()==null)"+(convertView.getTag()==null));
        return (null == (ViewHolder) convertView.getTag()) ? new ViewHolder(
                context, parent, layoutId, position) : (ViewHolder) convertView
                .getTag();
    }

    /**
     * 在position位置添加一个view
     *
     * @param position
     */
    public void addView(int position, View view) {
        mViews.append(position, view);
    }

    public View getConvertView() {
        return mConvertView;
    }

    /**
     * 通过控件的Id获取对于的控件，如果没有则加入views
     *
     * @param viewId
     * @return
     */
    public <T extends View> T getView(int viewId) {
        try {

            View view = mViews.get(viewId);
            if (view == null) {
                view = mConvertView.findViewById(viewId);
                mViews.put(viewId, view);
            }
            return (T) view;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 为TextView设置字符串
     *
     * @param viewId
     * @param text
     * @return
     */
    public ViewHolder setText(int viewId, CharSequence text) {
        TextView view = getView(viewId);
        view.setText(text);
        return this;
    }

    /**
     * @param viewId
     * @return
     */
    public ViewHolder setScaleType(int viewId, ScaleType ScaleType) {
        ImageView view = getView(viewId);
        view.setScaleType(ScaleType);
        return this;
    }

    /**
     * 为ImageView设置图片
     *
     * @param viewId
     * @param drawableId
     * @return
     */
    public ViewHolder setImageResource(int viewId, int drawableId) {
        ImageView view = getView(viewId);
        view.setImageResource(drawableId);
        return this;
    }

    /**
     * 为ImageView设置图片
     *
     * @param viewId
     * @return
     */
    public ViewHolder setImageBitmap(int viewId, Bitmap bm) {
        ImageView view = getView(viewId);
        view.setImageBitmap(bm);
        return this;
    }

    /**
     * 为ImageView设置图片
     *
     * @return
     */
    // public ViewHolder setImageByUrl(int viewId, String url, boolean
    // isFromNet,
    // int defaultResId) {
    // try {
    // ImageLoader.getInstance().loadImage(url,
    // (ImageView) getView(viewId), isFromNet, defaultResId);
    // } catch (Exception e) {
    // return null;
    // }
    // return this;
    // }
    public int getPosition() {
        return mPosition;
    }
}
