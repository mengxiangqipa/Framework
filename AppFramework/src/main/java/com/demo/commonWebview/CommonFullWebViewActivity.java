package com.demo.commonWebview;

import android.Manifest;
import android.app.Activity;
import android.content.ClipData;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.webkit.GeolocationPermissions;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.demo.demo.R;
import com.framework.util.FileUtil;
import com.framework.util.ScreenUtil;
import com.framework2.baseEvent.BaseOnClickListener;
import com.framework2.customviews.TitleView;
import com.library.loadingview.LoadingIndicatorView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 通用加载webview的activity
 *
 * @author YobertJomi
 * className CommonFullWebViewActivity
 * created at  2017/6/13  15:54
 */
public class CommonFullWebViewActivity extends AppCompatActivity {

    public static String TITLE = "TITLE";
    public static String URL = "URL";
    public static String SHOWRIGHT = "SHOWRIGHT";
    @BindView(R.id.titleView)
    TitleView titleView;
    @BindView(R.id.progress)
    ProgressBar progress;
    @BindView(R.id.webView)
    WebView webView;
    @BindView(R.id.tvEmpty)
    TextView tvEmpty;
    @BindView(R.id.emptyLayout)
    LinearLayout emptyLayout;
    @BindView(R.id.onLoading)
    LoadingIndicatorView onLoading;

    private int PERMISSION_CALL_PHONE = 10086;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_webview_with_titlebar);
        ButterKnife.bind(this);
        ScreenUtil.getInstance().setTranslucentStatus(this, true);
        ScreenUtil.getInstance().setStatusBarTintColor(this, getResources().getColor(R.color.white));
        ScreenUtil.getInstance().setSystemUiColorDark(this, true);
        initView();
        initWebViewSetting();
        loadUrl();
    }

    private void initView() {
        try {
            titleView.setTitle(getIntent().getStringExtra(TITLE));
            titleView.setRightVisible((getIntent().getBooleanExtra(SHOWRIGHT, false)));
            titleView.setLeftOnClickListener(new BaseOnClickListener() {
                @Override
                protected void onBaseClick(View v) {
                    goBack();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        emptyLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onViewClicked();
            }
        });
    }

    private void initWebViewSetting() {

        WebSettings webSettings = webView.getSettings();
        //加载https 在Android5.0中，WebView方面做了些修改，如果你的系统target api为21以上:
        //系统默认禁止了mixed content和第三方cookie。可以使用setMixedContentMode()
        // 和 setAcceptThirdPartyCookies()以分别启用。
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
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
    }

    private void loadUrl() {
        try {
            onLoading.setVisibility(View.VISIBLE);
            progress.setVisibility(View.VISIBLE);
            emptyLayout.setVisibility(View.GONE);
            webView.loadUrl(getIntent().getStringExtra(URL));
            //如果不设置WebViewClient，请求会跳转系统浏览器
            webView.setWebViewClient(new WebViewClient() {

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    //该方法在Build.VERSION_CODES.LOLLIPOP以前有效，从Build.VERSION_CODES
                    // .LOLLIPOP起，建议使用shouldOverrideUrlLoading(WebView, WebResourceRequest)} instead
                    //返回false，意味着请求过程里，不管有多少次的跳转请求（即新的请求地址），均交给webView自己处理，这也是此方法的默认处理
                    //返回true，说明你自己想根据url，做新的跳转，比如在判断url符合条件的情况下，我想让webView加载http://ask.csdn.net/questions/178242
//                    view.loadUrl(url);
//                    return true;
                    String tag = "tel:";
                    if (!TextUtils.isEmpty(url) && url.contains(tag)) {
                        int result = callPhone(url, tag);
                        if (result == -1) {
                            return super.shouldOverrideUrlLoading(view, url);
                        } else if (result == 0) {
                            return super.shouldOverrideUrlLoading(view, url);
                        } else if (result == 1) {
                            return true;
                        }
                    }
                    return super.shouldOverrideUrlLoading(view, url);
                }

                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                    //返回false，意味着请求过程里，不管有多少次的跳转请求（即新的请求地址），均交给webView自己处理，这也是此方法的默认处理
                    //返回true，说明你自己想根据url，做新的跳转，比如在判断url符合条件的情况下，我想让webView加载http://ask.csdn.net/questions/178242
                    String url = request.getUrl().toString();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        String tag = "tel:";
                        if (!TextUtils.isEmpty(url) && url.contains(tag)) {
                            int result = callPhone(url, tag);
                            if (result == -1) {
                                return super.shouldOverrideUrlLoading(view, url);
                            } else if (result == 0) {
                                return super.shouldOverrideUrlLoading(view, url);
                            } else if (result == 1) {
                                return true;
                            }
                        }
                    }
//                    view.loadUrl(url);
                    return false;
                }

                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    super.onPageStarted(view, url, favicon);
                }

                @Override
                public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
//                    super.onReceivedSslError(view, handler, error);
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
//                }
                public void onPageFinished(WebView view, String url) {
//                    CookieManager cookieManager = CookieManager.getProxyApplication();
//                    String cookie = cookieManager.getCookie(RealInterfaceConfig.getRealBaseServerUrl() +
// InterfaceConfig.jsCLickLogin);
//                    if (!TextUtils.isEmpty(cookie)) {
//                        cookieManager.removeAllCookie();
//                        CookieManagerUtil.getProxyApplication().saveCookie(CommonWebViewActivity.this, cookie);
//                    }
                    super.onPageFinished(view, url);
                    if (!webView.getSettings().getLoadsImagesAutomatically()) {
                        webView.getSettings().setLoadsImagesAutomatically(true);
                    }
                    //兼容低版本设置HTML的title
                    String title = view.getTitle();
                    if (!TextUtils.isEmpty(title) && null != titleView) {
                        titleView.setTitle(title);
                    }
                }
            });
            webView.setWebChromeClient(new WebChromeClient() {

                @Override
                public void onReceivedTitle(WebView view, String title) {
                    super.onReceivedTitle(view, title);
                    if (!TextUtils.isEmpty(title) && null != titleView) {
                        titleView.setTitle(title);
                    }
                }

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

                @Override
                public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback
                        callback) {
                    callback.invoke(origin, true, true);
                    super.onGeolocationPermissionsShowPrompt(origin, callback);
                }

                @Override
                public boolean onShowFileChooser(WebView webView,
                                                 ValueCallback<Uri[]> filePathCallback,
                                                 FileChooserParams fileChooserParams) {
                    mUploadCallbackAboveL = filePathCallback;
                    takePic();
                    return true;
                }

                public void openFileChooser(ValueCallback<Uri> uploadMsg) {
                    mUploadMessage = uploadMsg;
                    takePic();
                }

                public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
                    mUploadMessage = uploadMsg;
                    takePic();
                }

                public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
                    mUploadMessage = uploadMsg;
                    takePic();
                }
            });
        } catch (Exception e) {
            onLoading.setVisibility(View.GONE);
            progress.setVisibility(View.GONE);
            emptyLayout.setVisibility(View.VISIBLE);
        }
    }

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
        if (null != webView && webView.canGoBack()) {
            webView.goBack();
        }else{
            finish();
        }
    }

    private String tempMobileNo;

    private int callPhone(String url, String tag) {
        if (TextUtils.isEmpty(url) || TextUtils.isEmpty(tag))
            return 0;
        if (url.contains(tag)) {
            final String mobile = url.substring(url.lastIndexOf("/") + 1);
            tempMobileNo = mobile;
            boolean startWithNum = false;
            if (!TextUtils.isEmpty(tempMobileNo)) {
                Pattern pattern = Pattern.compile("[0-9]*");
                Matcher isNum = pattern.matcher(tempMobileNo.charAt(0) + "");
                startWithNum = isNum.matches();
            }
            if (TextUtils.isDigitsOnly(tempMobileNo) || startWithNum || mobile.startsWith("tel:")) {
//                checkPermission(new CheckPermListener() {
//                    @SuppressLint("MissingPermission")
//                    @Override
//                    public void superPermission() {
//                        Intent mIntent = new Intent(Intent.ACTION_CALL);
//                        Uri data = Uri.parse(mobile);
//                        mIntent.setData(data);
//                        startActivity(mIntent);
//                    }
//                }, R.string.require_permission_call_phone, Manifest.permission.CALL_PHONE);
                //Android6.0以后的动态获取打电话权限
                if (ActivityCompat.checkSelfPermission(CommonFullWebViewActivity.this,
                        Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                    try {
                        Intent mIntent = new Intent(Intent.ACTION_CALL);
                        Uri data;
                        if (mobile.startsWith("tel")) {
                            data = Uri.parse(mobile);
                        } else {
                            data = Uri.parse("tel:" + mobile);
                        }
                        mIntent.setData(data);
                        startActivity(mIntent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //这个超连接,java已经处理了，webview不要处理
                    return 1;
                } else {
                    //申请权限
                    ActivityCompat.requestPermissions(CommonFullWebViewActivity.this, new
                            String[]{Manifest.permission.CALL_PHONE}, 1);
                    return 1;
                }
            } else {
                return 0;
            }
        } else {
            return 0;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[]
            grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults != null && grantResults.length >= 1) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent mIntent = new Intent(Intent.ACTION_CALL);
                    Uri data;
                    if (tempMobileNo.startsWith("tel")) {
                        data = Uri.parse(tempMobileNo);
                    } else {
                        data = Uri.parse("tel:" + tempMobileNo);
                    }
                    mIntent.setData(data);
                    startActivity(mIntent);
                }
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            goBack();
//            return super.onKeyDown(keyCode, event);
            return true;//不继承BaseActivity时使用
        }
        return super.onKeyDown(keyCode, event);
    }

    //===>>===>>===>>===>>===>>===>>===>>===>>===>>===>>===>>===>>===>>===>>===>>===>>===>>===>>===>>===>>===>>===>>===>>===>>===>>
    //===>>===>>===>>===>>===>>===>>===>>===>>===>>===>>===>>===>>===>>===>>===>>===>>===>>===>>===>>===>>===>>===>>===>>===>>===>>
    private ValueCallback<Uri> mUploadMessage;// 表单的数据信息
    private ValueCallback<Uri[]> mUploadCallbackAboveL;
    private final static int FILECHOOSER_RESULTCODE = 1;// 表单的结果回调</span>
    private Uri imageUri;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FILECHOOSER_RESULTCODE) {
            if (null == mUploadMessage && null == mUploadCallbackAboveL) return;
            Uri result = data == null || resultCode != RESULT_OK ? null : data.getData();
            if (mUploadCallbackAboveL != null) {
                onActivityResultAboveL(requestCode, resultCode, data);
            } else if (mUploadMessage != null) {
                if (result != null) {
                    String path = FileUtil.getInstance().getRealFilePath(getApplicationContext(), result);
                    Uri uri = Uri.fromFile(new File(path));
                    mUploadMessage
                            .onReceiveValue(uri);
                } else {
                    mUploadMessage.onReceiveValue(imageUri);
                }
                mUploadMessage = null;
            }
        }
    }

    @SuppressWarnings("null")
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void onActivityResultAboveL(int requestCode, int resultCode, Intent data) {
        if (requestCode != FILECHOOSER_RESULTCODE
                || mUploadCallbackAboveL == null) {
            return;
        }
        Uri[] results = null;
        if (resultCode == Activity.RESULT_OK) {
            if (data == null) {
                results = new Uri[]{imageUri};
            } else {
                String dataString = data.getDataString();
                ClipData clipData = data.getClipData();
                if (clipData != null) {
                    results = new Uri[clipData.getItemCount()];
                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        ClipData.Item item = clipData.getItemAt(i);
                        results[i] = item.getUri();
                    }
                }
                if (dataString != null) {
                    results = new Uri[]{Uri.parse(dataString)};
                }
            }
        }
        if (results != null) {
            mUploadCallbackAboveL.onReceiveValue(results);
            mUploadCallbackAboveL = null;
        } else {
            results = new Uri[]{imageUri};
            mUploadCallbackAboveL.onReceiveValue(results);
            mUploadCallbackAboveL = null;
        }
        return;
    }

    private void takePic() {
        File imageStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                , "IOV");
        // Create the storage directory if it does not exist
        if (!imageStorageDir.exists()) {
            imageStorageDir.mkdirs();
        }
        File file = new File(imageStorageDir + File.separator + "IMG_" + String.valueOf(System.currentTimeMillis()) +
                ".jpg");
        imageUri = Uri.fromFile(file);

        final List<Intent> cameraIntents = new ArrayList<Intent>();
        final Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        final PackageManager packageManager = getPackageManager();
        final List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for (ResolveInfo res : listCam) {
            final String packageName = res.activityInfo.packageName;
            final Intent i = new Intent(captureIntent);
            i.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            i.setPackage(packageName);
            i.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            cameraIntents.add(i);
        }
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("image/*");
        Intent chooserIntent = Intent.createChooser(i, "Image Chooser");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, cameraIntents.toArray(new Parcelable[]{}));
        startActivityForResult(chooserIntent, FILECHOOSER_RESULTCODE);
    }
}
