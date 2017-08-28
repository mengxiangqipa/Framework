package com.demo.commonWebview;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.demo.configs.ConstantsME;
import com.demo.configs.InterfaceConfig;
import com.demo.configs.RealInterfaceConfig;
import com.framework.Utils.KeyBoardUtil;
import com.framework.Utils.PreferencesHelper;
import com.framework.Utils.ToastUtil;

/**
 * js调用android
 *
 * @author YobertJomi
 *         className JsCallAndroid
 *         created at  2017/7/14  9:43
 */
public class JsCallAndroid
{
    private Context context;
    private WebView webView;

    public JsCallAndroid(Context context, WebView webView)
    {
        this.context = context;
        this.webView = webView;
    }

    @JavascriptInterface
    public void clickOnAndroid()
    {
        ((Activity) context).runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                ToastUtil.getInstance().showToast("js 调用android--");
                webView.loadUrl("javascript:displayDate()");
            }
        });
    }

    @JavascriptInterface
    public void getLatLng()
    {
        ((Activity) context).runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                ToastUtil.getInstance().showToast("js 调用android--clickOnAndroidGetLatLng");
                webView.loadUrl("javascript:getLatLng('" + PreferencesHelper.getInstance().getStringData(ConstantsME.LATITUDE) + "','" + PreferencesHelper.getInstance().getStringData(ConstantsME.LONGTITUDE) + "')");
            }
        });
    }

    @JavascriptInterface
    public void nativeLogin()
    {
        ((Activity) context).runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                webView.loadUrl("javascript:dispalyDate()");
            }
        });
    }

    @JavascriptInterface
    public void nativeGoBack()
    {
        ((Activity) context).runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                goBack();
            }
        });
    }

    private void goBack()
    {
        if (null != webView && webView.canGoBack() && !TextUtils.equals(webView.getUrl(), RealInterfaceConfig.getRealBaseServerUrl() + InterfaceConfig.webMain))
        {
            webView.goBack();
        } else
        {
            KeyBoardUtil.getInstance().isCloseSoftInputMethod(context, null, true);
            ((Activity) context).finish();
        }
    }
}
