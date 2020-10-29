package com.demo.fragment;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.demo.demo.R;

import org.jetbrains.annotations.Nullable;

/**
 * 引导页2
 *
 * @author YobertJomi
 * className Fragment_guide_2
 * created at  2020/10/29  13:48
 */
public class GuideFragment2 extends BaseFragment {

    public GuideFragment2() {
    }

    public static GuideFragment2 newInstance(Bundle args) {
        GuideFragment2 fragment = new GuideFragment2();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View initContentView(LayoutInflater inflater, ViewGroup container,
                                Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_guide_2, container, false);
    }

    @Override
    protected void initUi(@Nullable Bundle savedInstanceState) {

    }
}
