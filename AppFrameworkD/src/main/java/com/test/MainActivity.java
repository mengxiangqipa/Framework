package com.test;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Toast;

import com.demo.activity.BaseSlideFinishActivity;
import com.demo.demo.R;
import com.library.pulltorefresh.BaseAbstractPullToRefreshLayout;
import com.library.pulltorefresh.pullableview.PullableListView;


import java.util.ArrayList;
import java.util.List;

import custom.org.greenrobot.eventbus.Subscribe;
import custom.org.greenrobot.eventbus.ThreadMode;

/**
 * 更多详解见博客http://blog.csdn.net/zhongkejingwang/article/details/38868463
 */
public class MainActivity extends BaseSlideFinishActivity {
    private PullableListView listView;

    //eventBus通知新消息
    @Subscribe(threadMode = ThreadMode.MAIN, tag = "newMessage")
    public void receivedNewMessage(String info) {
    }

    @Override
    public void _onCreate() {
        setContentView(R.layout.test_activity_main);
        BaseAbstractPullToRefreshLayout pull = ((BaseAbstractPullToRefreshLayout) findViewById(R.id.refresh_view));
        pull.setOnRefreshListener(new MyListener2());
        //		pull.setPullDownContentText("欲速则不达");
        //		pull.setPullUpContentText("宁静以致远");
        //		pull.setPullDownStateText("刷新的的的--下拉");
        //		int color = getResources().getColor(R.color.black);
        //		pull.setHeadViewBackgroundColor(color);
        //		// pull.setHeadViewBackgroundResource(R.drawable.allview_refreshing);
        //		pull.setPullDownContentTextSize(16);
        //		pull.setPullDownStateTextSize(12);
        listView = (PullableListView) findViewById(R.id.content_view);
        listView.setCanPullUp(true);
        initListView();
    }

    /**
     * ListView初始化方法
     */
    private void initListView() {
        List<String> items = new ArrayList<>();
        items.add("可下拉刷新上拉加载的ListView默认");
        items.add("可下拉刷新上拉加载的ListView_classical");
        items.add("可下拉刷新上拉加载的ListView_store_house");
        items.add("可下拉刷新上拉加载的ListView_material");
        items.add("可下拉刷新上拉加载的ListView_rentalsSun");
        items.add("可下拉刷新上拉加载的GridView");
        items.add("可下拉刷新上拉加载的ExpandableListView");
        items.add("可下拉刷新上拉加载的SrcollView");
        items.add("可下拉刷新上拉加载的WebView");
        items.add("可下拉刷新上拉加载的ImageView");
        items.add("可下拉刷新上拉加载的TextView");
        items.add("可下拉刷新上拉加载的RecyclerView");
        items.add("可下拉刷新上拉加载的RecyclerView（SOFT）");
        items.add("可下拉刷新上拉加载的RecyclerView（AutoLoad）");
        items.add("可下拉刷新上拉加载的RecyclerView（AutoLoad&Swipe）");
        items.add("可下拉刷新上拉加载的RecyclerView（拖拽）");
        items.add("可下拉刷新上拉加载的RecyclerView（侧滑）SwipeItemView");
        items.add("可下拉刷新上拉加载的RecyclerView（侧滑2）ItemTouchHelper");
        items.add("可下拉刷新上拉加载的RecyclerView CustomPullToRefreshlayout");
        MyAdapter adapter = new MyAdapter(this, items);
        // TextView text = new TextView(getApplicationContext());
        // text.setText("qqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq");
        // listView.addHeaderView(text);
        listView.setAdapter(adapter);
        listView.setOnItemLongClickListener(new OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, " LongClick on " + parent.getAdapter().getItemId(position), Toast
                        .LENGTH_SHORT).show();
                return true;
            }
        });
        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent it = new Intent();
                switch (position) {
                    case 0:
                        it.setClass(MainActivity.this, PullableListViewActivity.class);
                        break;
                    case 1:
                        it.setClass(MainActivity.this, TestListviewClassicalActivity.class);
                        break;
                    case 2:
                        it.setClass(MainActivity.this, TestListviewStoreHouseActivity.class);
                        break;
                    case 3:
                        it.setClass(MainActivity.this, TestListviewMaterialActivity.class);
                        break;
                    case 4:
                        it.setClass(MainActivity.this, TestListviewRentalsSunActivity.class);
                        break;
                    case 5:
                        it.setClass(MainActivity.this, PullableGridViewActivity.class);
                        break;
                    case 6:
                        it.setClass(MainActivity.this, PullableExpandableListViewActivity.class);
                        break;
                    case 7:
                        it.setClass(MainActivity.this, PullableScrollViewActivity.class);
                        break;
                    case 8:
                        it.setClass(MainActivity.this, PullableWebViewActivity.class);
                        break;
                    case 9:
                        it.setClass(MainActivity.this, PullableImageViewActivity.class);
                        break;
                    case 10:
                        it.setClass(MainActivity.this, PullableTextViewActivity.class);
                        break;
                    case 11:
                        it.setClass(MainActivity.this, TestRecyclerViewClassicalActivity.class);
                        break;
                    case 12:
                        it.setClass(MainActivity.this, TestRecyclerViewSoftClassicalActivity.class);
                        break;
                    case 13:
                        it.setClass(MainActivity.this, TestRecyclerViewAutoLoadClassicalActivity.class);
                        break;
                    case 14:
                        it.setClass(MainActivity.this, TestRecyclerViewAutoLoadClassicalSwipeActivity.class);
                        break;
                    case 15:
                        it.setClass(MainActivity.this, TestRecyclerViewDragActivity.class);
                        break;
                    case 16:
                        it.setClass(MainActivity.this, TestRecyclerViewSwipeActivity.class);
                        break;
                    case 17:
                        it.setClass(MainActivity.this, TestRecyclerViewSwipe2Activity.class);
                        break;
                    case 18:
                        it.setClass(MainActivity.this, TestRecyclerViewCustomActivity.class);
                        break;
                    default:
                        break;
                }
                startActivity(it);
            }
        });
    }
}
