package com.library.camerafilter.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.library.camerafilter.model.ImageInfo;
import com.libray.camerafilter.R;
import com.muzhi.camerasdk.library.utils.ViewHolder;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 特效页顶部小图片
 *
 * @author jazzy
 */

public class SmallThumbAdapter extends CommonListAdapter<ImageInfo> {

    public SmallThumbAdapter(Context context, List<String> sData) {
        super(context);
        this.mContext = context;
        this.mLayoutId = R.layout.camerasdk_list_item_image_thumb;
        this.mList = new ArrayList<ImageInfo>();
        for (String path : sData) {
            ImageInfo info = new ImageInfo();
            info.path = path;
            this.mList.add(info);
        }
    }

    public void setSelected(int position) {
        ImageInfo item = mList.get(position);
        boolean flag = false;
        for (ImageInfo info : mList) {
            flag = info.path.equals(item.path);
            info.selected = flag;
        }
        notifyDataSetChanged();
    }

    @Override
    public void getCommonView(ViewHolder helper, ImageInfo info) {
        // TODO Auto-generated method stub

        ImageView img = (ImageView) helper.getView(R.id.iv_image);
        ImageView img_mask = (ImageView) helper.getView(R.id.iv_mask);

        String path = info.path;
        int mItemSize = 90;
        File imageFile = new File(path);
        Picasso.with(mContext)
                .load(imageFile)
                .error(R.drawable.camerasdk_pic_loading)
                .resize(mItemSize, mItemSize)
                .centerCrop()
                .into(img);

        if (info.selected) {
            img_mask.setVisibility(View.VISIBLE);
        } else {
            img_mask.setVisibility(View.GONE);
        }
    }
}



