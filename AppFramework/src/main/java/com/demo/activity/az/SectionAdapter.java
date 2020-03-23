package com.demo.activity.az;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.demo.demo.R;
import com.library.adapter_recyclerview.UniversalAdapter;

import java.util.List;

/**
 * @author YobertJomi
 * className PinAdapter
 * created at  2017/7/12  16:35
 */

public class SectionAdapter extends UniversalAdapter<ContactSortModel> {

    public SectionAdapter(@NonNull Context context, @LayoutRes int layoutId, @Nullable List list) {
        super(context, layoutId, list);
    }

    @Override
    protected void getItemView(UniversalViewHolder viewHolder, ContactSortModel item,
                               int position) {
        try {
            viewHolder.setText(R.id.tvTitle, item.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
