package com.library.adapter_recyclerview;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * 广告PagerAdapter
 *
 * @author YobertJomi
 * className ADPagerAdapter
 * created at  2017/1/18  12:16
 */
public class ADPagerAdapter extends PagerAdapter {
    private ArrayList<View> list;

    public ADPagerAdapter(@NonNull ArrayList<View> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size() <= 1 ? list.size() : Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        try {
            container.addView(list.get(position % list.size()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list.get(position % list.size());
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
    }
}
