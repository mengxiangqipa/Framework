package com.test;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.demo.adapter.ADPagerAdapter;
import com.demo.demo.R;
import com.framework.utils.ToastUtil;
import com.framework.utils.Y;
import com.framework2.customLoading.LoadingFooter;
import com.framework2.customviews.CustomViewPager;
import com.library.adapter_recyclerview.DragAdapter;
import com.library.adapter_recyclerview.DragItemTouchHelperCallback;
import com.library.adapter_recyclerview.GridDividerItemDecoration;
import com.library.adapter_recyclerview.HorizontalDividerItemDecoration;
import com.library.adapter_recyclerview.SectionItemDecoration;
import com.library.adapter_recyclerview.UniversalAdapter;
import com.library.loadingview.indicators.BallBeatIndicator;
import com.library.loadingview.indicators.BallClipRotateIndicator;
import com.library.loadingview.indicators.BallClipRotateMultipleIndicator;
import com.library.loadingview.indicators.BallClipRotatePulseIndicator;
import com.library.loadingview.indicators.BallGridBeatIndicator;
import com.library.loadingview.indicators.BallGridPulseIndicator;
import com.library.loadingview.indicators.BallPulseIndicator;
import com.library.loadingview.indicators.BallPulseRiseIndicator;
import com.library.loadingview.indicators.BallPulseSyncIndicator;
import com.library.loadingview.indicators.BallRotateIndicator;
import com.library.loadingview.indicators.BallScaleIndicator;
import com.library.loadingview.indicators.BallScaleMultipleIndicator;
import com.library.loadingview.indicators.BallScaleRippleIndicator;
import com.library.loadingview.indicators.BallScaleRippleMultipleIndicator;
import com.library.loadingview.indicators.BallSpinFadeLoaderIndicator;
import com.library.loadingview.indicators.BallTrianglePathIndicator;
import com.library.loadingview.indicators.BallZigZagDeflectIndicator;
import com.library.loadingview.indicators.BallZigZagIndicator;
import com.library.loadingview.indicators.CubeTransitionIndicator;
import com.library.loadingview.indicators.LineScaleIndicator;
import com.library.loadingview.indicators.LineScalePartyIndicator;
import com.library.loadingview.indicators.LineScalePulseOutIndicator;
import com.library.loadingview.indicators.LineScalePulseOutRapidIndicator;
import com.library.loadingview.indicators.LineSpinFadeLoaderIndicator;
import com.library.loadingview.indicators.PacmanIndicator;
import com.library.loadingview.indicators.SemiCircleSpinIndicator;
import com.library.loadingview.indicators.SquareSpinIndicator;
import com.library.loadingview.indicators.TriangleSkewSpinIndicator;
import com.library.pulltorefresh.BaseAbstractPullToRefreshLayout;
import com.library.pulltorefresh.IndicatorDelegate;
import com.library.pulltorefresh.pullableview.PullableRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class TestRecyclerViewAutoLoadClassicalSwipeActivity extends AppCompatActivity
        implements PullableRecyclerView.OnAutoLoadListener, DragAdapter.OnStartDragListener {
    PullableRecyclerView recyclerView;
    int len = 0;
    BaseAbstractPullToRefreshLayout layout;
    LoadingFooter footerView;
    ItemTouchHelper mItemTouchHelper;
    boolean first = true;
    List<String> items;
    UniversalAdapter<String> adapter1;
    Class[] clss = new Class[]{BallBeatIndicator.class, BallClipRotateIndicator.class,
            BallClipRotateMultipleIndicator.class,
            BallClipRotatePulseIndicator.class, BallGridBeatIndicator.class, BallGridPulseIndicator.class,
            BallPulseIndicator.class,
            BallPulseRiseIndicator.class, BallPulseSyncIndicator.class, BallRotateIndicator.class, BallScaleIndicator
            .class, BallScaleMultipleIndicator.class,
            BallScaleRippleIndicator.class, BallScaleRippleMultipleIndicator.class, BallSpinFadeLoaderIndicator
            .class, BallTrianglePathIndicator.class,
            BallZigZagDeflectIndicator.class, BallZigZagIndicator.class, CubeTransitionIndicator.class,
            LineScaleIndicator.class, LineScalePartyIndicator.class,
            LineScalePulseOutIndicator.class, LineScalePulseOutRapidIndicator.class, LineSpinFadeLoaderIndicator
            .class, PacmanIndicator.class,
            SemiCircleSpinIndicator.class, SquareSpinIndicator.class, TriangleSkewSpinIndicator.class,};
    List<String> dataList;

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_activity_test_soft_classical_recyclerview);//测试更改布局就好了
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);//toolBar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ((BaseAbstractPullToRefreshLayout) findViewById(R.id.refresh_view)).setOnRefreshListener(new BaseAbstractPullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(final BaseAbstractPullToRefreshLayout pullToRefreshLayout) {
                // 下拉刷新操作
                new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        if (len % 2 == 0) {
                            // 千万别忘了告诉控件刷新完毕了哦！
                            pullToRefreshLayout.refreshFinish(BaseAbstractPullToRefreshLayout.FAIL);
                        } else {
                            pullToRefreshLayout.refreshFinish(BaseAbstractPullToRefreshLayout.SUCCEED);
                        }
                        len++;
                    }
                }.sendEmptyMessageDelayed(0, 1500);
            }

            @Override
            public void onLoadMore(final BaseAbstractPullToRefreshLayout pullToRefreshLayout) {
                // 加载操作
                new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        // 千万别忘了告诉控件加载完毕了哦！
                        pullToRefreshLayout.loadmoreFinish(BaseAbstractPullToRefreshLayout.SUCCEED);
                    }
                }.sendEmptyMessageDelayed(0, 1000);
            }
        });
        layout = (BaseAbstractPullToRefreshLayout) findViewById(R.id.refresh_view);
        recyclerView = (PullableRecyclerView) findViewById(R.id.content_view);
        //RecyclerView 的Item宽或者高不会变。每一个Item添加或者删除都不会变。
        // 如果你没有设置setHasFixedSized没有设置的代价将会是非常昂贵的。因为RecyclerView会需要而外计算每个item的size，
        recyclerView.setHasFixedSize(true);
        recyclerView.setCanPullUp(true);
        //第一种，简单的颜色，高度,水平分割线
        HorizontalDividerItemDecoration horizontalDividerItemDecoration = new HorizontalDividerItemDecoration(
                TestRecyclerViewAutoLoadClassicalSwipeActivity.this);
        horizontalDividerItemDecoration.setColor(this, R.color.share_texttoast_color);
        horizontalDividerItemDecoration.setDividerHeightPx(1);
        //recyclerView.addItemDecoration(horizontalDividerItemDecoration);
        //第二种，这个是系统提供的，可以设置图片,水平分割线
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration
                (TestRecyclerViewAutoLoadClassicalSwipeActivity.this,
                        DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.shape_horizontal_divider));
        //recyclerView.addItemDecoration(dividerItemDecoration);
        //第三种，用于网格式
        GridDividerItemDecoration gridDividerItemDecoration = new GridDividerItemDecoration(this);
        recyclerView.addItemDecoration(gridDividerItemDecoration);

        initRecyclerView();
        //以下这个是做拖动的
        DragItemTouchHelperCallback callback = new DragItemTouchHelperCallback
                (TestRecyclerViewAutoLoadClassicalSwipeActivity.this, (DragAdapter) adapter1);
        callback.setDragFlag(DragItemTouchHelperCallback.DragFlag.UP_DOWN);
        callback.setSwipeFlag(ItemTouchHelper.RIGHT);//设置滑动方向
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (first && hasFocus) {
            first = false;
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            Dialog dialog = builder.create();
            builder.setSingleChoiceItems(new String[]{"FixedNothing", "FixedHeader", "FixedContent"}, 0, new
                    DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case 0:
                                    layout.getIndicatorDelegate().setFixedMode(IndicatorDelegate.FixedMode.FixedNothing);
                                    dialog.dismiss();
                                    break;
                                case 1:
                                    layout.getIndicatorDelegate().setFixedMode(IndicatorDelegate.FixedMode.FixedHeader);
                                    dialog.dismiss();
                                    break;
                                case 2:
                                    layout.getIndicatorDelegate().setFixedMode(IndicatorDelegate.FixedMode.FixedContent);
                                    dialog.dismiss();
                                    break;
                            }
                        }
                    });
            builder.show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        layout.postDelayed(new Runnable() {
            @Override
            public void run() {
                layout.autoRefresh(2000);
                Y.y("onResume");
            }
        }, 4000);
    }

    private void initFooterView(Class cls, String state) {
        footerView = new LoadingFooter(this);
        footerView.setLoadingHint(TextUtils.isEmpty(state) ? "加载更多" : state);
        footerView.setNoMoreHint("已经全部为你呈现了");
        footerView.setNoNetWorkHint("网络不给力啊，点击再试一次吧");
        footerView.setState(LoadingFooter.State.Loading);
        footerView.setProgressStyle(cls);
    }

    private View initViewPager() {
        View inflate = LayoutInflater.from(this).inflate(R.layout.test_inflater_viewpager, null);
        CustomViewPager viewPager = (CustomViewPager) inflate.findViewById(R.id.viewPager);
        viewPager.setLayout(layout);
        ArrayList list = new ArrayList();
        for (int i = 0; i < 3; i++) {
            View view = LayoutInflater.from(this).inflate(R.layout.allview_auto_data, null);
            ((TextView) view.findViewById(R.id.tv_loadstate)).setText("我的位置:" + i);
            list.add(view);
        }
        ADPagerAdapter adapter = new ADPagerAdapter(list);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(Integer.MAX_VALUE / 2);
        return inflate;
    }

    /**
     * ListView初始化方法
     */
    private void initRecyclerView() {
        items = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            items.add("我是recyclerView： " + i);
        }

        recyclerView.setCanPullDown(false);//设置不能下拉时拖拽正常
        recyclerView.setCanPullUp(false);
        //				recyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager
        // .VERTICAL));
        //				recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasMoreData(true);
        recyclerView.setOnAutoLoadListener(this);

        dataList = new ArrayList<>();
        for (int i = 0; i < items.size(); i++) {
            dataList.add(items.get(i));
        }
        recyclerView.addItemDecoration(new SectionItemDecoration(new SectionItemDecoration.SectionCallBack() {
            //返回标记id (即每一项对应的标志性的字符串)
            @Override
            public String getSectionId(int position) {
                try {
                    if (position >= 3 && position <= 8)
                        return dataList.get(3);
                    return items.get(position);
                } catch (Exception e) {
                    return "";
                }
            }

            //获取同组中的第一个内容
            @Override
            public String getSectionTitle(int position) {
                try {
                    if (position >= 0 && position <= 8)
                        return "我是测试悬浮";
                    return items.get(position);
                } catch (Exception e) {
                    return "";
                }
            }
        }).setTextSize(40f).setBackgroudColor(TestRecyclerViewAutoLoadClassicalSwipeActivity.this, R.color
                .color_ic_laucher).setTextLeftMargin(40)
                .setSectionHeight(100).setTextColor(TestRecyclerViewAutoLoadClassicalSwipeActivity.this, R.color
                        .white));

        LoadingFooter header = new LoadingFooter(this);
        header.setLoadingHint("头部测试");
        header.setNoMoreHint("已经全部为你呈现了");
        header.setNoNetWorkHint("网络不给力啊，点击再试一次吧");
        header.setState(LoadingFooter.State.Loading);
        header.setProgressStyle(BallClipRotateIndicator.class);
        LoadingFooter header2 = new LoadingFooter(this);
        header2.setLoadingHint("头部测试2");
        header2.setNoMoreHint("已经全部为你呈现了");
        header2.setNoNetWorkHint("网络不给力啊，点击再试一次吧");
        header2.setState(LoadingFooter.State.Loading);
        header2.setProgressStyle(BallClipRotateIndicator.class);
        LoadingFooter footer = new LoadingFooter(this);
        footer.setLoadingHint("我是footer");
        footer.setNoMoreHint("已是全部");
        footer.setNoNetWorkHint("网络不给力啊，点击再试一次吧");
        footer.setState(LoadingFooter.State.Loading);
        footer.setProgressStyle(BallClipRotateIndicator.class);
        //				adapter1 = new UniversalAdapter<String>(TestRecyclerViewAutoLoadClassicalSwipeActivity.this, R
        // .layout.test_list_item_layout, items)
        //				{
        //					@Override
        //					protected void getItemView(UniversalViewHolder viewHolder, String item, final int position)
        //					{
        //						if (position < clss.length)
        //						{
        //							viewHolder.setText(R.id.tv, position + "   " + clss[position].getSimpleName());
        //						} else
        //						{
        //							viewHolder.setText(R.id.tv, item);
        //						}
        //						viewHolder.getView(R.id.tv).setOnClickListener(new View.OnClickListener()
        //						{
        //							@Override
        //							public void onClick(View v)
        //							{
        //								try
        //								{
        //									initFooterView(clss[position], "我是动态");
        //									adapter1.removeAllFooterView();
        //									adapter1.addFooterViewAfterSetAdapter(footerView);
        //									ToastUtil.getInstance().showToast("recyclerView的点击:" + clss[position]);
        //									Y.y("items.size:" + items.size());
        //									if (items.size() > 50)
        //									{
        //										int len = items.size();
        //										for (int i = 50; i < len; i++)
        //										{
        //											items.remove(50);
        //										}
        //									}
        //									Y.y("items.size:" + items.size());
        //									notifyDataSetChanged();
        //								} catch (Exception e)
        //								{
        //									Y.y("异常:" + e.getMessage());
        //								}
        //							}
        //						});
        //					}
        //				};
        adapter1 = new DragAdapter<String>(this, R.layout.test_list_item_layout, items, new DragAdapter
                .OnStartDragListener() {
            @Override
            public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
                mItemTouchHelper.startDrag(viewHolder);
            }
        }) {

            @Override
            public void onItemSelected_(Context context, SwipeViewHolder viewHolder, int realPosition) {
                viewHolder.setBackGroundColor(R.id.tv, ContextCompat.getColor(context, R.color.colorAccent));
                viewHolder.setTextColor(R.id.tv, ContextCompat.getColor(context, R.color.white));
                Y.y("onItemSelected_:" + realPosition + "   :");
            }

            @Override
            public void onItemClear_(Context context, SwipeViewHolder viewHolder, int realPosition) {
                viewHolder.setBackGroundColor(R.id.tv, ContextCompat.getColor(context, R.color.white));
                viewHolder.setTextColor(R.id.tv, ContextCompat.getColor(context, R.color.black));
                Y.y("onItemClear_:" + realPosition + "   :");
            }

            @Override
            public void onItemDismiss_(Context context, int position) {

            }

            @Override
            public void onItemMoveSuccess_(Context context, int fromPosition, int toPosition) {
                Y.y("22onItemMoveSuccess_：" + fromPosition + "    " + toPosition);
            }

            @Override
            protected void getItemView(final UniversalViewHolder viewHolder, String item, final int position) {
                if (position < clss.length) {
                    viewHolder.setText(R.id.tv, position + "   " + clss[position].getSimpleName());
                } else {
                    viewHolder.setText(R.id.tv, item);
                }
                viewHolder.getView(R.id.iv_delete).setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                            ((DragAdapter) adapter1).startDrag(((DragAdapter.SwipeViewHolder) viewHolder));
                        }
                        return false;
                    }
                });
                viewHolder.getView(R.id.tv).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            initFooterView(clss[position], "我是动态");
                            adapter1.removeAllFooterView();
                            adapter1.addFooterViewAfterSetAdapter(footerView);
                            ToastUtil.getInstance().showToast("recyclerView的点击:" + clss[position]);
                            Y.y("items.size:" + items.size());
                            if (items.size() > 50) {
                                int len = items.size();
                                for (int i = 50; i < len; i++) {
                                    items.remove(50);
                                }
                            }
                            Y.y("items.size:" + items.size());
                            notifyDataSetChanged();
                        } catch (Exception e) {
                            Y.y("异常:" + e.getMessage());
                        }
                    }
                });
            }
        };
        adapter1.addHeaderView(initViewPager());
        adapter1.addHeaderView(header);
        adapter1.addHeaderView(header2);
        adapter1.addFooterView(footer);
        recyclerView.setAdapter(adapter1);
        initFooterView(BallPulseIndicator.class, "");
        adapter1.addFooterView(footerView);
    }

    @Override
    public void onAutoLoad(final PullableRecyclerView pullableRecyclerView) {
        pullableRecyclerView.setOnLoading(true);
        new Handler() {
            @Override
            public void handleMessage(Message msg) {
                for (int i = 0; i < 5; i++) {
                    items.add("这里是自动加载进来的item:" + adapter1.getDataItemCount());
                }
                adapter1.notifyDataSetChanged();
                if (adapter1.getDataItemCount() > 90) {
                    footerView.setState(LoadingFooter.State.NetWorkError);
                    pullableRecyclerView.setHasMoreData(false);
                }
                pullableRecyclerView.setOnLoading(false);
            }
        }.sendEmptyMessageDelayed(0, 2000);
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }
}
