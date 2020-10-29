package com.demo.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.demo.demo.R;

import org.jetbrains.annotations.Nullable;

/**
 * 引导页1
 *
 * @author YobertJomi
 * className Fragment_guide_1
 * created at  2020/10/29  13:47
 */
public class GuideFragment1 extends BaseFragment {

    public GuideFragment1() {
    }

    public static GuideFragment1 newInstance(Bundle args) {
        GuideFragment1 fragment = new GuideFragment1();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View initContentView(LayoutInflater inflater, ViewGroup container,
                                Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_guide_1, container, false);
    }

    @Override
    protected void initUi(@Nullable Bundle savedInstanceState) {

    }
}
