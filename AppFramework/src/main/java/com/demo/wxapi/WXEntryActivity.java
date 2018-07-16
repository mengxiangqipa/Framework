package com.demo.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.demo.configs.EventBusTag;
import com.demo.entity.WchatAuthResult;
import com.framework.utils.Y;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;

/**
 * @author Yangjie
 * className WXEntryActivity
 * created at  2017/4/13  17:15
 */
/*
 * 微信登录，分享应用中必须有这个名字叫WXEntryActivity，并且必须在wxapi包名下，腾讯官方文档中有要求
 */
public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
    private static final String WCHAT_APP_ID = "wxf159411fd8b11d79";// 微信开放平台申请到的app_id
    private IWXAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.wxentry_activity);
        api = WXAPIFactory.createWXAPI(this, WCHAT_APP_ID, false);
        api.registerApp(WCHAT_APP_ID);
        try {
            api.handleIntent(getIntent(), this);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        try {
            super.onNewIntent(intent);
            setIntent(intent);
            api.handleIntent(intent, this);
        } catch (Exception e) {
        }
    }

    @Override
    public void onReq(BaseReq baseReq) {
        try {
            Y.y("微信登录：" + "请求响应回调接口:" + "onReq");
            this.finish();
        } catch (Exception e) {
        }
    }

    @Override
    public void onResp(BaseResp baseResp) {
        Y.y("微信登录：" + "请求响应回调接口:" + "baseResp");
        if (baseResp instanceof SendAuth.Resp) {
            try {
                SendAuth.Resp sendAuthResp = (SendAuth.Resp) baseResp;// 用于分享时不要有这个，不能强转
                String code = sendAuthResp.code;
                Y.y("微信登录：" + "请求响应回调接口:" + code);
                Y.y("微信登录：" + "请求响应回调接口:" + baseResp.errCode);
                WchatAuthResult result = new WchatAuthResult();
                result.setAuthCode(code);
                if (baseResp.errCode == BaseResp.ErrCode.ERR_OK) {
                    result.setSuccess(true);
                    EventBus.getDefault().post(result, EventBusTag.wchatAuthSuccess);
                    Intent intent = new Intent();
                    intent.setAction("com.yaxin.yyt.wchatlogin");
                    intent.putExtra("code", code);
                    sendBroadcast(intent);
                } else {
                    result.setSuccess(false);
                    EventBus.getDefault().post(result, EventBusTag.wchatAuthSuccess);
                }
                finish();
            } catch (Exception e) {
                this.finish();
            }
        }
    }
}
