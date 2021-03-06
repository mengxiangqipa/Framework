package com.test;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Toast;

import com.demo.demo.R;
import com.library.pulltorefresh.BaseAbstractPullToRefreshLayout;
import com.library.pulltorefresh.IndicatorDelegate;
import com.library.pulltorefresh.pullableview.PullableListView;
import com.library.pulltorefresh.pullableview.PullableListView2;
import com.library.pulltorefresh.pullableview.PullableListView2.OnLoadListener;

import java.util.ArrayList;
import java.util.List;

public class TestListviewMaterialActivity extends AppCompatActivity implements OnLoadListener {
    PullableListView listView;
    int len = 0;
    BaseAbstractPullToRefreshLayout layout;
    boolean first = true;
    List<String> items;
    MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_activity_test_material);//测试更改布局就好了
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
        //		((StoreHousePullToRefreshLayout) findViewById(R.id.refresh_view))
        //				.setOnRefreshListener(new MyCustomListener());
        listView = (PullableListView) findViewById(R.id.content_view);
        //		listView.setOnLoadListener(this);
        //		listView.setLoadMoreBackgroundColor(getResources().getColor(
        //				R.color.light_blue));
        //		listView.setLoadMoreTextNoMore("你妹的，没有了!");
        //		listView.setLoadMoreText("点点看");
        //		listView.setLoadMoreTextSize(18);
        listView.setCanPullUp(true);
//				listView.setAutoLoad(true);
        //		listView.setHasMoreData(false);//Git测试

        initListView();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (first && hasFocus) {
            first = false;
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            Dialog dialog = builder.create();
            builder.setSingleChoiceItems(new String[]{"FixedNothing", "FixedHeader",
                    "FixedContent"}, 0, new
                    DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case 0:
                                    layout.getIndicatorDelegate().setFixedMode(IndicatorDelegate.FixedMode
                                            .FixedNothing);
                                    dialog.dismiss();
                                    break;
                                case 1:
                                    layout.getIndicatorDelegate().setFixedMode(IndicatorDelegate.FixedMode.FixedHeader);
                                    dialog.dismiss();
                                    break;
                                case 2:
                                    layout.getIndicatorDelegate().setFixedMode(IndicatorDelegate.FixedMode
                                            .FixedContent);
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
//		layout.postDelayed(new Runnable() {
//			@Override
//			public void run() {
//				layout.autoRefresh(5000);
//				Log.e("yy","onResume");
//			}
//		},4000);
    }

    /**
     * ListView初始化方法
     */
    private void initListView() {
        items = new ArrayList<String>();
        for (int i = 0; i < 50; i++) {
            items.add("这里是item " + i);
        }
        adapter = new MyAdapter(this, items);
        listView.setAdapter(adapter);
        listView.setOnItemLongClickListener(new OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position,
                                           long id) {
                Toast.makeText(TestListviewMaterialActivity.this,
                        "LongClick on " + parent.getAdapter().getItemId
                        (position), Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(TestListviewMaterialActivity.this,
                        " Click on " + parent.getAdapter().getItemId
                        (position), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onLoad(PullableListView2 pullableListView2) {
        // TODO Auto-generated method stub
        new Handler() {
            @Override
            public void handleMessage(Message msg) {
                //				for (int i = 100; i < 130; i++) {
                //					items.add("这里是自动加载进来的item" + i);
                //				}
                //				listView.finishLoading();
                //				adapter.notifyDataSetChanged();
                //				if (listView.getCount() > 50) {
                //					listView.setHasMoreData(false);
                //				}
            }
        }.sendEmptyMessageDelayed(0, 2000);
    }
}
