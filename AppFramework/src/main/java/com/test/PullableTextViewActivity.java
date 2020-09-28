package com.test;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.demo.demo.R;
import com.library.pulltorefresh.classical.ClassicalPullToRefreshLayout;

public class PullableTextViewActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_activity_textview);
        ((ClassicalPullToRefreshLayout) findViewById(R.id.refresh_view)).setOnRefreshListener(new MyListener2());
    }
}
