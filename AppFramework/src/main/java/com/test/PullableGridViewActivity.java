package com.test;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Toast;

import com.demo.demo.R;
import com.library.pulltorefresh.BaseAbstractPullToRefreshLayout;
import com.library.pulltorefresh.pullableview.PullableGridView;

import java.util.ArrayList;
import java.util.List;

public class PullableGridViewActivity extends AppCompatActivity {
    PullableGridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_activity_gridview);
        ((BaseAbstractPullToRefreshLayout) findViewById(R.id.refresh_view)).setOnRefreshListener(new BaseAbstractPullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(BaseAbstractPullToRefreshLayout pullToRefreshLayout) {
                // 千万别忘了告诉控件刷新完毕了哦！
                pullToRefreshLayout.refreshFinish(BaseAbstractPullToRefreshLayout.SUCCEED);
            }

            @Override
            public void onLoadMore(BaseAbstractPullToRefreshLayout pullToRefreshLayout) {
                // 千万别忘了告诉控件加载完毕了哦！
                pullToRefreshLayout.loadmoreFinish(BaseAbstractPullToRefreshLayout.SUCCEED);
            }
        });
        gridView = (PullableGridView) findViewById(R.id.content_view);
        gridView.setCanPullUp(true);
        initGridView();
    }

    /**
     * GridView初始化方法
     */
    private void initGridView() {
        List<String> items = new ArrayList<String>();
        for (int i = 0; i < 50; i++) {
            items.add("这里是item " + i);
        }
        MyAdapter adapter = new MyAdapter(this, items);
        gridView.setAdapter(adapter);
        gridView.setOnItemLongClickListener(new OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position,
                                           long id) {
                Toast.makeText(PullableGridViewActivity.this,
                        "LongClick on " + parent.getAdapter().getItemId
                        (position), Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        gridView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(PullableGridViewActivity.this,
                        " Click on " + parent.getAdapter().getItemId(position),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}
