package com.library.adapter_listview;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * @author YobertJomi
 *         className CommonPagerAdapter
 *         created at  2017/7/17  16:46
 */

public class CommonPagerAdapter extends FragmentPagerAdapter {
    private String[] titles;
    private List<Fragment> fragmentList;
    private Context context;

    public CommonPagerAdapter(Context context, FragmentManager fm, List<Fragment> fragmentList, String[] titles) {
        super(fm);
        this.context = context;
        this.fragmentList = fragmentList;
        this.titles = titles;
    }

    @Override
    public Fragment getItem(int position) {
        return null!=fragmentList&&fragmentList.size()>position?fragmentList.get(position):null;
    }

    @Override
    public int getCount() {
        return null!=fragmentList&&fragmentList.size()>0?fragmentList.size():0;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
