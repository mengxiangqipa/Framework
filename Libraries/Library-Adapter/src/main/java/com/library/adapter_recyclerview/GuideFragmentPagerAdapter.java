package com.library.adapter_recyclerview;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class GuideFragmentPagerAdapter extends FragmentPagerAdapter
{
	private ArrayList<Fragment> listFragments;

	public GuideFragmentPagerAdapter(FragmentManager fm, ArrayList<Fragment> listFragments)
	{
		super(fm);
		this.listFragments = listFragments;
	}

	@Override
	public Fragment getItem(int position)
	{
		return null == listFragments ? null : listFragments.get(position);
	}

	@Override
	public int getCount()
	{
		return null == listFragments ? 0 : listFragments.size();
	}
}
