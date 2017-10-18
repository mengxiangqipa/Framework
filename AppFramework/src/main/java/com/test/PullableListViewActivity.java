package com.test;

import java.util.ArrayList;
import java.util.List;

import com.demo.demo.R;
import com.library.pulltorefresh.BaseAbstractPullToRefreshLayout;
import com.library.pulltorefresh.pullableview.PullableListView2;
import com.library.pulltorefresh.pullableview.PullableListView2.OnLoadListener;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Toast;

public class PullableListViewActivity extends AppCompatActivity implements OnLoadListener
{
	PullableListView2 listView;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_activity_listview);
		((BaseAbstractPullToRefreshLayout) findViewById(R.id.refresh_view)).setOnRefreshListener(new MyListener2());
		listView = (PullableListView2) findViewById(R.id.content_view);
		listView.setOnLoadListener(this);
		listView.setLoadMoreBackgroundColor(getResources().getColor(R.color.blue));
		listView.setLoadMoreTextNoMore("你妹的，没有了!");
		listView.setLoadMoreText("点点看");
		listView.setLoadMoreTextSize(18);
		listView.setCanPullUp(true);
		listView.setAutoLoad(true);
		listView.setHasMoreData(false);
		initListView();
	}

	List<String> items;
	MyAdapter adapter;

	/**
	 * ListView初始化方法
	 */
	private void initListView()
	{
		items = new ArrayList<>();
		for (int i = 0; i < 50; i++)
		{
			items.add("这里是item " + i);
		}
		adapter = new MyAdapter(this, items);
		listView.setAdapter(adapter);
		listView.setOnItemLongClickListener(new OnItemLongClickListener()
		{

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
			{
				Toast.makeText(PullableListViewActivity.this, "LongClick on " + parent.getAdapter().getItemId(position), Toast.LENGTH_SHORT).show();
				return true;
			}
		});
		listView.setOnItemClickListener(new OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				Toast.makeText(PullableListViewActivity.this, " Click on " + parent.getAdapter().getItemId(position), Toast.LENGTH_SHORT).show();
			}
		});
	}

	@Override
	public void onLoad(PullableListView2 pullableListView2)
	{
		// TODO Auto-generated method stub
		new Handler()
		{
			@Override
			public void handleMessage(Message msg)
			{
				for (int i = 100; i < 130; i++)
				{
					items.add("这里是自动加载进来的item" + i);
				}
				listView.finishLoading();
				adapter.notifyDataSetChanged();
				if (listView.getCount() > 50)
				{
					listView.setHasMoreData(false);
				}
			}
		}.sendEmptyMessageDelayed(0, 2000);
	}

}
