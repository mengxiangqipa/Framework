package com.demo.commonWebview;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.demo.activity.BaseActivity;
import com.demo.configs.ConstantsME;
import com.demo.configs.EventBusTag;
import com.demo.configs.InterfaceConfig;
import com.demo.configs.RealInterfaceConfig;
import com.demo.demo.R;
import com.demo.networkModel.HttpUtil;
import com.demo.util.ReloginUtil;
import com.framework.utils.KeyBoardUtil;
import com.framework.utils.ScreenUtils;
import com.framework.utils.ToastUtil;
import com.framework.utils.Y;
import com.framework2.customviews.TitleView;
import com.framework2.utils.CustomLoadingDialogUtils;
import com.framework2.utils.TBSCookieManagerUtil;
import com.library.loadingview.LoadingIndicatorView;
import com.tencent.smtt.export.external.interfaces.SslError;
import com.tencent.smtt.export.external.interfaces.SslErrorHandler;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.export.external.interfaces.WebResourceResponse;
import com.tencent.smtt.sdk.CookieManager;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import custom.org.greenrobot.eventbus.Subscribe;
import custom.org.greenrobot.eventbus.ThreadMode;

/**
 * 通用加载webview的activity
 *
 * @author YobertJomi
 * className CommonFullTBSWebViewActivity
 * created at  2017/6/13  15:54
 */
public class CommonFullTBSWebViewActivity extends BaseActivity {

    @BindView(R.id.titleView)
    TitleView titleView;
    @BindView(R.id.progress)
    ProgressBar progress;
    @BindView(R.id.tvEmpty)
    TextView tvEmpty;
    @BindView(R.id.emptyLayout)
    LinearLayout emptyLayout;
    @BindView(R.id.webView)
    WebView webView;
    @BindView(R.id.onLoading)
    LoadingIndicatorView onLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_tbs_webview);
        ButterKnife.bind(this);
        ScreenUtils.getInstance().setTranslucentStatus(this, true);
        ScreenUtils.getInstance().setStatusBarTintColor(this,
                getResources().getColor(R.color.white));
        initView();
        TBSCookieManagerUtil.getInstance().synCookies(this, RealInterfaceConfig.getRealBaseServerUrl());
        initWebViewSetting();
        loadUrl();
    }

    //eventBus通知新消息
    @Subscribe(threadMode = ThreadMode.MAIN, tag = EventBusTag.newMessage)
    public void receivedNewMessage(String info) {
    }

    private void initView() {
        try {
//            titleView.setRightVisible(false).setLeftOnClickListener(new BaseOnClickListener() {
//                @Override
//                protected void onBaseClick(View v) {
//                    goBack();
//                }
//            });
            titleView.setTitle(getIntent().getStringExtra(ConstantsME.title));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initWebViewSetting() {

        WebSettings webSettings = webView.getSettings();
        //支持缩放，默认为true。
        webSettings.setSupportZoom(false);
        //调整图片至适合webview的大小
        webSettings.setUseWideViewPort(true);
        // 缩放至屏幕的大小
        webSettings.setLoadWithOverviewMode(true);
        //设置默认编码
        webSettings.setDefaultTextEncodingName("utf-8");
        ////设置自动加载图片
        ////设置自动加载图片--Build.VERSION.SDK_INT>Build.VERSION_CODES.LOLLIPOP
        webSettings.setLoadsImagesAutomatically(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP);
        webSettings.setGeolocationEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setAppCacheEnabled(true);

//下面3句添加后webview滑动不顺畅
//        webSettings.setDatabaseEnabled(true);
//        String dir = this.getApplicationContext().getDir("database", Context.MODE_PRIVATE).getPath();
//        webSettings.setDatabasePath(dir);

        //多窗口
        webSettings.supportMultipleWindows();
        //获取触摸焦点
        webView.requestFocusFromTouch();
        //允许访问文件
        webSettings.setAllowFileAccess(true);
        //开启javascript
        webSettings.setBuiltInZoomControls(false);
        webSettings.setJavaScriptEnabled(true);
        //支持通过JS打开新窗口
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        //提高渲染的优先级
        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        //支持内容重新布局
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
//        //优先webview中使用缓存
//        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        //设置 缓存模式
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        // 开启 DOM storage API 功能
        webSettings.setDomStorageEnabled(true);
        webSettings.setBlockNetworkImage(false);//阻断网络图片
        webSettings.setBlockNetworkLoads(false);//阻断网络下载
        webSettings.setSaveFormData(true);//设置WebView是否保存表单数据，默认true，保存数据。

//        //        //对离线应用的支持
//        webSettings.setDomStorageEnabled(true);
//        webSettings.setAppCacheMaxSize(1024 * 1024 * 10);//设置缓冲大小，10M
//        String appCacheDir =getDir("cache", Context.MODE_PRIVATE).getPath();
//        webSettings.setAppCachePath(appCacheDir);
    }

    private void loadUrl() {
        Y.y("loadUrl:--" + ";currentThread:" + Thread.currentThread().getId() + "   getName:" + Thread.currentThread
                ().getName());
        try {
            onLoading.setVisibility(View.VISIBLE);
            progress.setVisibility(View.VISIBLE);
            emptyLayout.setVisibility(View.GONE);
            webView.loadUrl(getIntent().getStringExtra(ConstantsME.url));
            //如果不设置WebViewClient，请求会跳转系统浏览器
            webView.setWebViewClient(new WebViewClient() {

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    //该方法在Build.VERSION_CODES.LOLLIPOP以前有效，从Build.VERSION_CODES
                    // .LOLLIPOP起，建议使用shouldOverrideUrlLoading(WebView, WebResourceRequest)} instead
                    //返回false，意味着请求过程里，不管有多少次的跳转请求（即新的请求地址），均交给webView自己处理，这也是此方法的默认处理
                    //返回true，说明你自己想根据url，做新的跳转，比如在判断url符合条件的情况下，我想让webView加载http://ask.csdn.net/questions/178242
                    Y.y("CommonFullTBSWebViewActivity--shouldOverrideUrlLoading111:" + url);
                    view.loadUrl(url);
                    CookieManager cookieManager = CookieManager.getInstance();
                    String cookie = cookieManager.getCookie(RealInterfaceConfig.getRealBaseServerUrl() +
                            InterfaceConfig.jsCLickLogin);
                    if (!TextUtils.isEmpty(cookie) && cookie.contains(";")) {
                        Y.y("CommonFullTBSWebViewActivity-shouldOverrideUrlLoading成功后保存的cookie：" + cookie);
                        cookieManager.removeAllCookie();
                        TBSCookieManagerUtil.getInstance().saveCookie(CommonFullTBSWebViewActivity.this, cookie);
                    }

                    if (TextUtils.equals(url, RealInterfaceConfig.getRealBaseServerUrl() + InterfaceConfig.webMain)
                            || TextUtils.equals(url, RealInterfaceConfig.getRealBaseServerUrl() + InterfaceConfig
                            .webKPIdetail)) {
                        TBSCookieManagerUtil.getInstance().synCookies(CommonFullTBSWebViewActivity.this,
                                RealInterfaceConfig.getRealBaseServerUrl());
                    }

                    if (!TextUtils.isEmpty(url) && TextUtils.equals(url, RealInterfaceConfig.getRealBaseServerUrl())) {
                        ReloginUtil.getInstance().gotoLogin(CommonFullTBSWebViewActivity.this);
                        return true;
                    }
                    return true;
                }

                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                    //返回false，意味着请求过程里，不管有多少次的跳转请求（即新的请求地址），均交给webView自己处理，这也是此方法的默认处理
                    //返回true，说明你自己想根据url，做新的跳转，比如在判断url符合条件的情况下，我想让webView加载http://ask.csdn.net/questions/178242
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                    }
                    String url = request.getUrl().toString();
                    Y.y("shouldOverrideUrlLoading222:" + url);
                    CookieManager cookieManager = CookieManager.getInstance();
                    String cookie = cookieManager.getCookie(RealInterfaceConfig.getRealBaseServerUrl() +
                            InterfaceConfig.jsCLickLogin);
                    Y.y("full -shouldOverrideUrlLoading成功后保存的cookie：" + cookie);
                    if (!TextUtils.isEmpty(cookie) && cookie.contains(";")) {
                        cookieManager.removeAllCookie();
                        TBSCookieManagerUtil.getInstance().saveCookie(CommonFullTBSWebViewActivity.this, cookie);
                    }

                    if (TextUtils.equals(url, RealInterfaceConfig.getRealBaseServerUrl() + InterfaceConfig.webMain)
                            || TextUtils.equals(url, RealInterfaceConfig.getRealBaseServerUrl() + InterfaceConfig
                            .webKPIdetail)) {
                        ToastUtil.getInstance().showToast("start:" + cookie);
                        TBSCookieManagerUtil.getInstance().synCookies(CommonFullTBSWebViewActivity.this,
                                RealInterfaceConfig.getRealBaseServerUrl());
                        Y.y("end:" + cookieManager.getCookie(RealInterfaceConfig.getRealBaseServerUrl()));
                        Y.y("end2:" + cookieManager.getCookie(RealInterfaceConfig.getRealBaseServerUrl() +
                                InterfaceConfig.webMain));
                    }
                    if (!TextUtils.isEmpty(url) && TextUtils.equals(url, RealInterfaceConfig.getRealBaseServerUrl())) {
                        ReloginUtil.getInstance().gotoLogin(CommonFullTBSWebViewActivity.this);
                        return true;
                    }
                    view.loadUrl(url);
                    return true;
                }

                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    super.onPageStarted(view, url, favicon);
                    Y.y("onPageStarted:--" + url);
                    if (!TextUtils.isEmpty(url) && TextUtils.equals(url, RealInterfaceConfig.getRealBaseServerUrl())) {
                        ReloginUtil.getInstance().gotoLogin(CommonFullTBSWebViewActivity.this);
                    }
                }

                @Override
                public void onLoadResource(final WebView view, final String url) {
//                    Y.y("加载资源:--"+url);
                    Y.y("加载资源:--" + ";currentThread:" + Thread.currentThread().getId() + "   getName:" + Thread
                            .currentThread().getName());
                    super.onLoadResource(view, url);
                }

                @Override
                public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
                    Y.y("shouldInterceptRequest1:--" + url);
                    return super.shouldInterceptRequest(view, url);
                }

//                @Override
//                public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
//                    Y.y("shouldInterceptRequest:--" + ";currentThread:" + Thread.currentThread().getId() + "
// getName:" + Thread.currentThread().getName());
////                    WebResourceResponse response = null;
////                    if(url.contains("avatar.php?")){
////                        try {
////                            final PipedOutputStream out = new PipedOutputStream();
////                            PipedInputStream in = new PipedInputStream(out);
//////                            ImageLoader.getInstance().loadImage(url, new ImageLoadingListener() {
//////                                @Override
//////                                public void onLoadingStarted(String s, View view) {}
//////                                @Override
//////                                public void onLoadingFailed(String s, View view, FailReason failReason) {}
//////                                @Override
//////                                public void onLoadingCancelled(String s, View view) {}
//////
//////                                @Override
//////                                public void onLoadingComplete(String s, View view, Bitmap bitmap) {
//////                                    if (bitmap != null) {
//////                                        try {
//////                                            out.write(IOUtils.Bitmap2Bytes(bitmap));
//////                                            out.close();
//////                                        }catch (Exception e){
//////                                            e.printStackTrace();
//////                                        }
//////                                    }
//////                                }
//////                            });
////                            response = new WebResourceResponse("image/png", "UTF-8", in);
////                        }catch (Exception e){
////                            e.printStackTrace();
////                        }
////                    }
////                    return response;
//
//
////                    WebResourceResponse response = new WebResourceResponse("image/png", "UTF-8", in);
//
//                    return super.shouldInterceptRequest(view, request);
//                }

                @Override
                public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                    super.onReceivedSslError(view, handler, error);
                    handler.proceed(); // 接受所有证书
                }

                //                @Override
//                public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
//                    super.onReceivedError(view, request, error);
////                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
////                        request.getUrl();
////                    }
////                    webViewTemp=view;
////                    isShowingError = true;
////                    webView.setVisibility(View.GONE);
////                    onLoading.setVisibility(View.GONE);
////                    progress.setVisibility(View.GONE);
////                    emptyLayout.setVisibility(View.VISIBLE);
//                    Y.y("onReceivedError:"+error.getDescription());
//                }
                public void onPageFinished(WebView view, String url) {
                    Y.y("onPageFinished:--" + url);
                    super.onPageFinished(view, url);
                    if (!webView.getSettings().getLoadsImagesAutomatically()) {
                        webView.getSettings().setLoadsImagesAutomatically(true);
                    }
                }
            });
            webView.setWebChromeClient(new WebChromeClient() {
                @Override
                public void onProgressChanged(WebView view, int newProgress) {
                    super.onProgressChanged(view, newProgress);
                    if (newProgress >= 100) {
                        progress.setProgress(100);
                        onLoading.setVisibility(View.GONE);
                        progress.setVisibility(View.GONE);
                        emptyLayout.setVisibility(View.GONE);
                    } else {
                        progress.setProgress(newProgress);
                        onLoading.setVisibility(View.GONE);// TODO: 2017/6/13 可以修改为visible
                        progress.setVisibility(View.VISIBLE);
                        emptyLayout.setVisibility(View.GONE);
                    }
                }
            });
        } catch (Exception e) {
            onLoading.setVisibility(View.GONE);
            progress.setVisibility(View.GONE);
            emptyLayout.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 请求更改昵称
     */
    private void requestModifyNick(@NonNull String nick) {
        HttpUtil.getInstance().requestModifyNick(InterfaceConfig.modifyNick, nick,
                new HttpUtil.OnRequestResult<String>() {
                    @Override
                    public void onSuccess(String... s) {
                        CustomLoadingDialogUtils.getInstance().dismissDialog();
                        finishActivity();
                    }

                    @Override
                    public void onFail(int code, String msg) {
                        CustomLoadingDialogUtils.getInstance().dismissDialog();
                        ToastUtil.getInstance().showToast(TextUtils.isEmpty(msg) ? "修改昵称失败" : msg);
                    }
                });
    }

    @OnClick(R.id.emptyLayout)
    public void onViewClicked() {
        loadUrl();
    }

    @Override
    protected void onDestroy() {
//        try {
//            if (webView != null) {
//                webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
//                webView.clearHistory();
//                ((ViewGroup) webView.getParent()).removeView(webView);
//                webView.destroy();
//                webView = null;
//            }
//        } catch (Exception e) {
//        }

        if (webView != null) {
            try {
                // 如果先调用destroy()方法，则会命中if (isDestroyed()) return;这一行代码，需要先onDetachedFromWindow()，再
                // destory()
                ViewParent parent = webView.getParent();
                if (parent != null) {
                    ((ViewGroup) parent).removeView(webView);
                }
                webView.stopLoading();
                // 退出时调用此方法，移除绑定的服务，否则某些特定系统会报错
                webView.getSettings().setJavaScriptEnabled(false);
                webView.clearHistory();
                webView.clearView();
                webView.removeAllViews();
                webView.destroy();
            } catch (Exception ex) {

            }
        }
        super.onDestroy();
    }

    private void goBack() {
        if (null != webView && webView.canGoBack() && !TextUtils.equals(webView.getUrl(), RealInterfaceConfig
                .getRealBaseServerUrl() + InterfaceConfig.webMain)) {
            webView.goBack();
        } else {
            KeyBoardUtil.getInstance().isCloseSoftInputMethod(this, null, true);
            beforeFinishActivity();
            finishActivity();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
