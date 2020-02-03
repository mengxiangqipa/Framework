package com.demo.fragment;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.demo.demo.R;

public class Fragment_guide_2 extends Fragment {
    private Context mContext;
    private View view;// infalte的布局
    private LinearLayout myContainer;// 新建容器

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (null == view) {
            myContainer = new LinearLayout(mContext);
            view = inflater.inflate(R.layout.fragment_guide_2, container, false);
            DisplayMetrics displayMetrics = new DisplayMetrics();
            ((WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics
                    (displayMetrics);
            view.setMinimumHeight(displayMetrics.heightPixels);
            view.setMinimumWidth(displayMetrics.widthPixels);
            myContainer.addView(view);
        } else {
            myContainer.removeAllViews();
            myContainer = new LinearLayout(getActivity());
            myContainer.addView(view);
        }
        return myContainer;
    }
}
