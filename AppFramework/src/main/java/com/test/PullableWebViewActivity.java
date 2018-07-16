package com.test;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.demo.demo.R;
import com.library.pulltorefresh.classical.ClassicalPullToRefreshLayout;
import com.library.pulltorefresh.pullableview.PullableWebView;

public class PullableWebViewActivity extends AppCompatActivity {
    PullableWebView webView;

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_activity_webview);
        ((ClassicalPullToRefreshLayout) findViewById(R.id.refresh_view)).setOnRefreshListener(new MyListener2());
        webView = (PullableWebView) findViewById(R.id.content_view);
        webView.loadUrl("http://blog.csdn.net/zhongkejingwang?viewmode=list");
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return false;
            }

            @Override
            public void onScaleChanged(WebView view, float oldScale, float newScale) {
                super.onScaleChanged(view, oldScale, newScale);
            }
        });
    }
}
