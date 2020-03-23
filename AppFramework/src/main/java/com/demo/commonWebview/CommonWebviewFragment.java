package com.demo.commonWebview;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.demo.configs.ConstantsME;
import com.demo.configs.InterfaceConfig;
import com.demo.configs.RealInterfaceConfig;
import com.demo.demo.R;
import com.demo.util.ReloginUtil;
import com.framework.util.ToastUtil;
import com.framework.util.Y;
import com.framework2.utils.CookieManagerUtil;
import com.library.loadingview.LoadingIndicatorView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class CommonWebviewFragment extends Fragment {

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

    private Context mContext;
    private View view;// infalte的布局
    private LinearLayout containerLayout;// 新建容器
    private Unbinder unbinder;
    private boolean isShowingError;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }

    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        if (null == view) {
            containerLayout = new LinearLayout(mContext);
            view = inflater.inflate(R.layout.fragment_common_webview, null);
            view.setMinimumHeight(((WindowManager) getActivity()
                    .getSystemService(Context.WINDOW_SERVICE))
                    .getDefaultDisplay().getHeight());
            view.setMinimumWidth(((WindowManager) getActivity()
                    .getSystemService(Context.WINDOW_SERVICE))
                    .getDefaultDisplay().getWidth());
            containerLayout.addView(view);
            unbinder = ButterKnife.bind(this, containerLayout);
            Y.y("onCreateView1:" + getArguments().getString(ConstantsME.url));
//            CookieManagerUtil.getProxyApplication().synCookies(getActivity(), null ==
//            getArguments() ? RealInterfaceConfig
// .getRealBaseServerUrl() : getArguments().getString(ConstantsME.url));
            CookieManagerUtil.getInstance().synCookies(getActivity(),
                    RealInterfaceConfig.getRealBaseServerUrl());
            Y.y("onCreateView2:" + getArguments().getString(ConstantsME.url));
            CookieManager cookieManager = CookieManager.getInstance();
            String cookie =
                    cookieManager.getCookie(RealInterfaceConfig.getRealBaseServerUrl() + InterfaceConfig
                    .jsCLickLogin);
            Y.y("jsClick--cookie：" + cookie);
            Y.y("主页cookie：" + cookieManager.getCookie(getArguments().getString(ConstantsME.url)));

            initWebViewSetting();
            loadUrl(null == getArguments() ? null : getArguments().getString(ConstantsME.url));
        } else {
            containerLayout.removeAllViews();
            containerLayout = new LinearLayout(getActivity());
            containerLayout.addView(view);
        }
//        String co =
// "aaabbbcccaaabbbcccaaabbbcccaaabbbcccaaabbbcccaaabbbcccaaabbbcccaaabbbcccaaabbbcccaaabbbcccaaabbbcccaaabbbcccaaabbbcccaaabbbcccaaabbbcccaaabbbcccaaabbbcccaaabbbcccaaabbbcccaaabbbccc";
//        CookieManagerUtil.getProxyApplication().saveCookie(getContext(), co);
//        CookieManagerUtil.getProxyApplication().synCookies(getContext(), RealInterfaceConfig
//        .getRealBaseServerUrl());

        return containerLayout;
    }

    private void initWebViewSetting() {

        WebSettings webSettings = webView.getSettings();
        //支持缩放，默认为true。
        webSettings.setSupportZoom(true);
        //调整图片至适合webview的大小
        webSettings.setUseWideViewPort(true);
        // 缩放至屏幕的大小
        webSettings.setLoadWithOverviewMode(true);
        //设置默认编码
        webSettings.setDefaultTextEncodingName("utf-8");
        ////设置自动加载图片--Build.VERSION.SDK_INT>Build.VERSION_CODES.LOLLIPOP
        webSettings.setLoadsImagesAutomatically(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP);
        webSettings.setGeolocationEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setAppCacheEnabled(true);
//下面3句添加后webview滑动不顺畅
//        webSettings.setDatabaseEnabled(true);
//        String dir = getActivity().getApplicationContext().getDir("database", Context
//        .MODE_PRIVATE).getPath();
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

//        //对离线应用的支持
        webSettings.setDomStorageEnabled(true);
        webSettings.setAppCacheMaxSize(1024 * 1024 * 10);//设置缓冲大小，10M
        String appCacheDir = this.getActivity().getDir("cache", Context.MODE_PRIVATE).getPath();
        webSettings.setAppCachePath(appCacheDir);

        webView.setVerticalScrollBarEnabled(false);
        webView.setHorizontalScrollBarEnabled(false);
    }

    private void loadUrl(String url) {
        try {
            isShowingError = false;
            webView.setVisibility(View.VISIBLE);
            onLoading.setVisibility(View.VISIBLE);
            progress.setVisibility(View.VISIBLE);
            emptyLayout.setVisibility(View.GONE);
            webView.loadUrl(!TextUtils.isEmpty(url) ? url : "http://www.baidu.com");//http://www
            // .baidu.com
            //如果不设置WebViewClient，请求会跳转系统浏览器
            webView.setWebViewClient(new WebViewClient() {

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    //该方法在Build.VERSION_CODES.LOLLIPOP以前有效，从Build.VERSION_CODES
                    // .LOLLIPOP起，建议使用shouldOverrideUrlLoading(WebView, WebResourceRequest)} instead
                    //返回false，意味着请求过程里，不管有多少次的跳转请求（即新的请求地址），均交给webView自己处理，这也是此方法的默认处理
                    //返回true，说明你自己想根据url，做新的跳转，比如在判断url符合条件的情况下，我想让webView加载http://ask.csdn
                    // .net/questions/178242
                    Y.y("shouldOverrideUrlLoading111:" + url);
                    CookieManager cookieManager = CookieManager.getInstance();
                    String cookie =
                            cookieManager.getCookie(RealInterfaceConfig.getRealBaseServerUrl() +
                            InterfaceConfig.jsCLickLogin);
                    Y.y("当前网页的cookie：" + cookieManager.getCookie(url));
//                    if (!TextUtils.isEmpty(cookie) && cookie.contains(";")) {
//                        Y.y("fragment111-shouldOverrideUrlLoading成功后保存的cookie：" + cookie);
//                        cookieManager.removeAllCookie();
//                        CookieManagerUtil.getProxyApplication().saveCookie(getActivity(), cookie);
//                    }

                    if (TextUtils.equals(url,
                            RealInterfaceConfig.getRealBaseServerUrl() + InterfaceConfig.webMain)
                            || TextUtils.equals(url,
                            RealInterfaceConfig.getRealBaseServerUrl() + InterfaceConfig
                            .webKPIdetail)) {
                        Y.y("同步base：" + cookieManager.getCookie(RealInterfaceConfig.getRealBaseServerUrl() +
                                InterfaceConfig.webMain));
                        CookieManagerUtil.getInstance().synCookies(getActivity(),
                                RealInterfaceConfig
                                .getRealBaseServerUrl());
                        Y.y("同步base2：" + cookieManager.getCookie(RealInterfaceConfig.getRealBaseServerUrl() +
                                InterfaceConfig.webMain));
                    }
                    if (!TextUtils.isEmpty(url) && TextUtils.equals(url,
                            RealInterfaceConfig.getRealBaseServerUrl())) {
                        ReloginUtil.getInstance().gotoLogin(mContext);
                        return true;
                    }
                    view.loadUrl(url);
                    return true;
                }

                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                    //返回false，意味着请求过程里，不管有多少次的跳转请求（即新的请求地址），均交给webView自己处理，这也是此方法的默认处理
                    //返回true，说明你自己想根据url，做新的跳转，比如在判断url符合条件的情况下，我想让webView加载http://ask.csdn
                    // .net/questions/178242
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                    }
                    String url = request.getUrl().toString();
                    Y.y("shouldOverrideUrlLoading222:" + url);
                    CookieManager cookieManager = CookieManager.getInstance();
                    String cookie =
                            cookieManager.getCookie(RealInterfaceConfig.getRealBaseServerUrl() +
                            InterfaceConfig.jsCLickLogin);
                    Y.y("fragment222-shouldOverrideUrlLoading成功后保存的cookie：" + cookie);
                    if (!TextUtils.isEmpty(cookie) && cookie.contains(";")) {
                        cookieManager.removeAllCookie();
                        CookieManagerUtil.getInstance().saveCookie(getActivity(), cookie);
                    }

                    if (TextUtils.equals(url,
                            RealInterfaceConfig.getRealBaseServerUrl() + InterfaceConfig.webMain)
                            || TextUtils.equals(url,
                            RealInterfaceConfig.getRealBaseServerUrl() + InterfaceConfig
                            .webKPIdetail)) {
                        ToastUtil.getInstance().showToast("start:" + cookie);
                        CookieManagerUtil.getInstance().synCookies(getActivity(),
                                RealInterfaceConfig
                                .getRealBaseServerUrl());
                        Y.y("end:" + cookieManager.getCookie(RealInterfaceConfig.getRealBaseServerUrl()));
                        Y.y("end2:" + cookieManager.getCookie(RealInterfaceConfig.getRealBaseServerUrl() +
                                InterfaceConfig.webMain));
                    }
                    if (!TextUtils.isEmpty(url) && TextUtils.equals(url,
                            RealInterfaceConfig.getRealBaseServerUrl())) {
                        ReloginUtil.getInstance().gotoLogin(mContext);
                        return true;
                    }
                    view.loadUrl(url);
                    return true;
                }

                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    super.onPageStarted(view, url, favicon);
                    if (!TextUtils.isEmpty(url) && TextUtils.equals(url,
                            RealInterfaceConfig.getRealBaseServerUrl())) {
                        ReloginUtil.getInstance().gotoLogin(mContext);
                    }
                }

//                @Override
//                public void onReceivedError(WebView view, WebResourceRequest request,
//                WebResourceError error) {
//                    super.onReceivedError(view, request, error);
////                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
////                        request.getUrl();
////                    }
//                    webViewTemp=view;
//                    isShowingError = true;
//                    webView.setVisibility(View.GONE);
//                    onLoading.setVisibility(View.GONE);
//                    progress.setVisibility(View.GONE);
//                    emptyLayout.setVisibility(View.VISIBLE);
//                }

                @Override
                public void onReceivedSslError(WebView view, SslErrorHandler handler,
                                               SslError error) {
                    super.onReceivedSslError(view, handler, error);
                    handler.proceed(); // 接受所有证书
                }

                @Override
                public void onPageFinished(WebView view, String url) {
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
//                    Y.y("newProgress:" + newProgress);
                    if (!isShowingError)
                        if (newProgress >= 100) {
                            webView.setVisibility(View.VISIBLE);
                            progress.setProgress(100);
                            onLoading.setVisibility(View.GONE);
                            progress.setVisibility(View.GONE);
                            emptyLayout.setVisibility(View.GONE);
                        } else {
                            webView.setVisibility(View.VISIBLE);
                            progress.setProgress(newProgress);
                            onLoading.setVisibility(View.GONE);// TODO: 2017/6/13 可以修改为visible
                            progress.setVisibility(View.VISIBLE);
                            emptyLayout.setVisibility(View.GONE);
                        }
                }
            });
        } catch (Exception e) {
            Y.y("loadUrl-Exception:" + e.getMessage());
            webView.setVisibility(View.GONE);
            onLoading.setVisibility(View.GONE);
            progress.setVisibility(View.GONE);
            emptyLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDestroyView() {
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
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.emptyLayout)
    public void onViewClicked() {
//        try {
//            isShowingError = false;
//            webView.setVisibility(View.VISIBLE);
//            onLoading.setVisibility(View.VISIBLE);
//            progress.setVisibility(View.VISIBLE);
//            emptyLayout.setVisibility(View.GONE);
//
//            webViewTemp.loadUrl(webViewTemp.getUrl());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }
}
