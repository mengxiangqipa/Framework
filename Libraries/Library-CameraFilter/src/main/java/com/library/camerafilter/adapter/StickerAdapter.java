package com.library.camerafilter.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.library.camerafilter.model.Filter_Sticker_Info;
import com.libray.camerafilter.R;
import com.muzhi.camerasdk.library.utils.ViewHolder;
import com.squareup.picasso.Picasso;


/**
 * 贴纸库Adapter
 */
public class StickerAdapter extends CommonListAdapter<Filter_Sticker_Info>
{

    public StickerAdapter(Context context)
    {
        super(context);
        this.mContext = mContext;
        this.mLayoutId = R.layout.camerasdk_list_item_sticker;
    }

    @Override
    public void getCommonView(ViewHolder helper, Filter_Sticker_Info item)
    {
        // TODO Auto-generated method stub

        ImageView image = (ImageView) helper.getView(R.id.iv_sticker);

        Picasso.with(mContext)
                .load(item.getImage())
                .placeholder(R.drawable.camerasdk_pic_loading)
                .error(R.drawable.camerasdk_pic_loading)
                .resize(120, 120)
                .centerCrop()
                .into(image);

    }
}


