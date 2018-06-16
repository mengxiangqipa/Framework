package com.library.emoji.fragment;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.library.adapter_recyclerview.GridDividerItemDecoration2;
import com.library.adapter_recyclerview.UniversalAdapter;
import com.library.emoji.R;
import com.library.emoji.R2;
import com.library.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author YobertJomi
 * className BaseAbstractFaceFragment
 * created at  2017/8/23  16:03
 */
public abstract class BaseAbstractFaceFragment<T> extends Fragment {

    @BindView(R2.id.recyclerView)
    RecyclerView recyclerView;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 10086:
                    requestData();
                    break;
            }
        }
    };
    private Context mContext;
    private View view;
    private Unbinder unbinder;
    private List<T> list = new ArrayList<>();
    private UniversalAdapter adapter;
    private boolean hasInit;

    public BaseAbstractFaceFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }

    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        if (view == null)
            view = inflater.inflate(R.layout.fragment_emoji_recyclerview, container, false);
        unbinder = ButterKnife.bind(this, view);
        if (!hasInit) {
            hasInit = true;
            initRecyclerView();
            handler.sendEmptyMessage(10086);
        }
        initEvent();
        return view;
    }

    private void initEvent() {

    }

    @SuppressWarnings("unchecked")
    private void initRecyclerView() {
        //第一种，简单的颜色，高度,水平分割线
//        HorizontalDividerItemDecoration horizontalDividerItemDecoration = new HorizontalDividerItemDecoration
// (mContext);
//        horizontalDividerItemDecoration.setColor(mContext, R.color.library_emoji_white);
//        horizontalDividerItemDecoration.setDividerHeightPx(ScreenUtils.getInstance().dip2px(mContext, 10));
        GridDividerItemDecoration2 gridDividerItemDecoration2 = new GridDividerItemDecoration2(mContext);
        gridDividerItemDecoration2.setCanDraw(false);
        ColorDrawable colorDrawable = new ColorDrawable();
        colorDrawable.setColor(mContext.getResources().getColor(R.color.red_adapter));
//        colorDrawable.setBounds(new Rect(0, 0, ScreenUtils.getInstance().dp2px(getContext(), 3), ScreenUtils
// .getInstance().dp2px(getContext(), 3)));
        colorDrawable.setBounds(0, 0, ScreenUtils.getInstance().dp2px(getContext(), 10), ScreenUtils.getInstance()
                .dp2px(getContext(), 10));
//        gridDividerItemDecoration2.setDrawable(colorDrawable);
//        gridDividerItemDecoration2.setDrawable(getResources().getDrawable(R.drawable.gradient_griditem_emoji));
//        FrameLayout.LayoutParams params= (FrameLayout.LayoutParams) recyclerView.getLayoutParams();
//        int _50=ScreenUtils.getInstance().dip2px(getContext(),50);
//        params.setMargins(_50,_50,_50,_50);
//        recyclerView.setLayoutParams(params);
        recyclerView.setPadding(ScreenUtils.getInstance().dp2px(getContext(), 10), ScreenUtils.getInstance().dp2px
                        (getContext(), 10),
                ScreenUtils.getInstance().dp2px(getContext(), 10), 0);
        recyclerView.addItemDecoration(gridDividerItemDecoration2);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 7));
        adapter = initAdapter();
        recyclerView.setAdapter(adapter);
    }

    public abstract void requestData();

    public abstract UniversalAdapter<T> initAdapter();

    public List<T> getList() {
        return list;
    }

    public UniversalAdapter getAdapter() {
        return adapter;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
