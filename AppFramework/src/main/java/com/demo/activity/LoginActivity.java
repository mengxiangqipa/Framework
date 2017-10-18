package com.demo.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.demo.configs.ConstantsME;
import com.demo.configs.EventBusTag;
import com.demo.configs.IntentCode;
import com.demo.demo.R;
import com.demo.wchatutil.JsonUtils;
import com.demo.wxapi.WXEntryActivity;
import com.framework.utils.KeyBoardUtil;
import com.framework.utils.PreferencesHelper;
import com.framework.utils.RegularUtil;
import com.framework.utils.ScreenUtils;
import com.framework.utils.ToastUtil;
import com.framework.utils.Y;
import com.framework.customviews.OverScrollView;
import com.framework.security.RSAmethodInRaw;
import com.framework2.utils.CustomLoadingDialogUtils;
import com.framework2.utils.PicToastUtil;
import com.framework2.baseEvent.BaseOnClickListener;
import com.tencent.connect.UserInfo;
import com.tencent.connect.common.Constants;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 登录注册
 *
 * @author Yangjie
 *         className LoginActivity
 *         created at  2017/3/13  16:24
 */
public class LoginActivity extends BaseActivity {
    @BindView(R.id.etLoginPhone)
    EditText etLoginPhone;
    @BindView(R.id.etLoginPassword)
    EditText etLoginPassword;
    @BindView(R.id.tv_forget_pwd)
    TextView tvForgetPwd;
    @BindView(R.id.loginLayout)
    LinearLayout loginLayout;
    @BindView(R.id.tv_login)
    TextView tvLogin;
    @BindView(R.id.tv_register)
    TextView tvRegister;
    @BindView(R.id.overScrollView)
    OverScrollView overScrollView;
    @BindView(R.id.wchatLogin)
    LinearLayout wchatLogin;
    @BindView(R.id.qqLogin)
    LinearLayout qqLogin;
    //************************三方状态
    private UserInfo qqUserInfo;
    private boolean qqAuthSuccess;
    private String qqOpenId;
    private String qqImgUrl;
    private String qqNick;

    private boolean wchatAuthSuccess;
    private String wchatOpenId;
    private String wChatImgUrl;
    private String wChatNick;
    private boolean checkQQBindSuccess;
    private boolean checkWchatBindSuccess;

    private String phoneTemp;
    private String pwdTemp;
    private String captchaTemp;
    private int thirdType = 2;//2qq  3wchat

    private MyCountDownTimer myCountDownTimer;
    private String verifyKey;//验证码key

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        ScreenUtils.getInstance().setTranslucentStatus(this, true);
        ScreenUtils.getInstance().setStatusBarTintColor(this,
                getResources().getColor(R.color.white));
        initData();
        initEvent();
        receiver = new WchatLoginBroadcastReceiver();
        IntentFilter filter = new IntentFilter("com.yaxin.yyt.wchatlogin");
        registerReceiver(receiver, filter);
    }

    private void initData() {
        overScrollView
                .setOverScrollTinyListener(new OverScrollView.OverScrollTinyListener() {
                    @Override
                    public void scrollLoosen() {

                    }

                    @Override
                    public void scrollDistance(int tinyDistance,
                                               int totalDistance) {
                        KeyBoardUtil.getInstance().isCloseSoftInputMethod(LoginActivity.this,
                                etLoginPhone, true);
                    }
                });
        String phone = RSAmethodInRaw.rsaDecrypt(this,
                PreferencesHelper.getInstance().getStringData(ConstantsME.PHONE));
        if (!TextUtils.isEmpty(phone)) {
            etLoginPhone.setText(phone);
            etLoginPhone.setSelection(phone.length());
            etLoginPhone.requestFocus();
            KeyBoardUtil.getInstance().isCloseSoftInputMethod(LoginActivity.this, etLoginPhone, false);
        }
        if (!TextUtils.isEmpty(phone)) {
            etLoginPhone.setText(phone);
            etLoginPhone.setSelection(phone.length());
            etLoginPassword.requestFocus();
            KeyBoardUtil.getInstance().isCloseSoftInputMethod(LoginActivity.this,
                    etLoginPhone, true);
        }
    }

    private void initEvent() {
        etLoginPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tvForgetPwd.setVisibility((s != null && !TextUtils.isEmpty(s)) ? View.GONE : View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        qqLogin.setOnClickListener(onClickListener);
        wchatLogin.setOnClickListener(onClickListener);
    }

    private boolean isLegal(EditText et_phone, EditText et_password, EditText et_captcha) {
        if (null == et_phone || null == et_password)
            return false;
        if (TextUtils.isEmpty(et_phone.getText())) {
            PicToastUtil.getInstance().showPicToast(LoginActivity.this, "请输入你的手机号码!");
            et_phone.requestFocus();
            KeyBoardUtil.getInstance().isCloseSoftInputMethod(LoginActivity.this, et_phone, false);
            return false;
        } else if (!RegularUtil.getInstance().isMobileNO(et_phone.getText().toString())) {
            PicToastUtil.getInstance().showPicToast(LoginActivity.this, "请输入正确的手机号码!");
            et_phone.requestFocus();
            KeyBoardUtil.getInstance().isCloseSoftInputMethod(LoginActivity.this, et_phone, false);
            return false;
        } else if (TextUtils.isEmpty(et_password.getText())) {
            PicToastUtil.getInstance().showPicToast(LoginActivity.this, "请输入密码!");
            et_password.requestFocus();
            KeyBoardUtil.getInstance().isCloseSoftInputMethod(LoginActivity.this, et_phone, false);
            return false;
        } else if (null != et_captcha && !TextUtils.isEmpty(et_captcha.getText())) {
            PicToastUtil.getInstance().showPicToast(LoginActivity.this, "请输入验证码!");
            et_captcha.requestFocus();
            KeyBoardUtil.getInstance().isCloseSoftInputMethod(LoginActivity.this, et_phone, false);
            return false;
        }
        return true;
    }

    //eventBus通知 getShopIdSuccess
    @Subscribe(threadMode = ThreadMode.MAIN, tag = EventBusTag.getShopIdSuccess)
    public void getShopIdSuccess(String info) {
        if (!TextUtils.isEmpty(PreferencesHelper.getInstance().getStringData(ConstantsME.currentShopId))) {
            startActivity(HomePageActivity.class);
            finish();
        } else {
            ToastUtil.getInstance().showToast("请先登录电脑端创建店铺!");
        }
    }

    //eventBus通知 getShopIdFail
    @Subscribe(threadMode = ThreadMode.MAIN, tag = EventBusTag.noCompanyId)
    public void noCompanyId(String noCompanyId) {
        ToastUtil.getInstance().showToast("请先登录电脑端创建店铺!");
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_forget_pwd:
                KeyBoardUtil.getInstance().isCloseSoftInputMethod(LoginActivity.this, etLoginPhone, true);
                Bundle forgetBundle = new Bundle();
                if (!TextUtils.isEmpty(etLoginPhone.getText()) && RegularUtil.getInstance().isMobileNO(etLoginPhone.getText().toString()))
                    forgetBundle.putString(ConstantsME.PHONE, etLoginPhone.getText().toString());
                startActivityForResult(ForgetPasswordActivity.class, forgetBundle, IntentCode.login);
                break;
            case R.id.tv_login:
                startActivity(HomePageActivity.class);
                finish();

//                Utils.isCloseSoftInputMethod(LoginActivity.this, etLoginPhone, true);
//                if (isLegal(etLoginPhone, etLoginPassword, null)) {
//                    CustomLoadingDialogUtils.getInstance().showDialog(LoginActivity.this, "正在登录");
//                    requestLogin();
//                }
                break;
            case R.id.tv_register:
                KeyBoardUtil.getInstance().isCloseSoftInputMethod(LoginActivity.this, etLoginPhone, true);
                Bundle bundle = new Bundle();
                if (!TextUtils.isEmpty(etLoginPhone.getText()) && RegularUtil.getInstance().isMobileNO(etLoginPhone.getText().toString()))
                    bundle.putString(ConstantsME.PHONE, etLoginPhone.getText().toString());
                startActivityForResult(RegisterActivity.class, bundle, IntentCode.login);
                break;
            default:
                break;
        }
    }

    BaseOnClickListener onClickListener = new BaseOnClickListener() {
        @Override
        protected void onBaseClick(View v) {
            switch (v.getId()) {
                case R.id.wchatLogin:
                    thirdType = 3;
                    if (wchatAuthSuccess && checkWchatBindSuccess && !TextUtils.isEmpty(wchatOpenId)) {
//                        showBindingPop(thirdType, phoneTemp, qqOpenId, qqImgUrl, qqNick);
                        showBindingPop(thirdType, phoneTemp, qqOpenId, pwdTemp, captchaTemp);
                    } else if (wchatAuthSuccess && !checkWchatBindSuccess) {
                        CustomLoadingDialogUtils.getInstance().showDialog(LoginActivity.this, "检查用户信息");
                        requestThirdLoginCheckBinding(thirdType, wchatOpenId, wChatImgUrl, wChatNick);
                    } else {
                        CustomLoadingDialogUtils.getInstance().showDialog(LoginActivity.this, "跳转到微信");
                        wchatLogin();
                    }
                    break;
                case R.id.qqLogin:
                    thirdType = 2;
                    if (qqAuthSuccess && checkQQBindSuccess && !TextUtils.isEmpty(qqOpenId)) {
//                        showBindingPop(thirdType, phoneTemp, wchatOpenId, wChatImgUrl, wChatNick);
                        showBindingPop(thirdType, phoneTemp, qqOpenId, pwdTemp, captchaTemp);
                    } else if (qqAuthSuccess && !checkQQBindSuccess) {
                        CustomLoadingDialogUtils.getInstance().showDialog(LoginActivity.this, "检查用户信息");
                        requestThirdLoginCheckBinding(thirdType, qqOpenId, qqImgUrl, qqNick);
                    } else {
                        CustomLoadingDialogUtils.getInstance().showDialog(LoginActivity.this, "跳转到QQ");
                        qqLogin();
                    }
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (receiver != null) {
            unregisterReceiver(receiver);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        try {
            Y.y("onNewIntent");
            setIntent(intent);
        } catch (Exception e) {
        }
    }

    //*************************************************************************************//chat_Login
    ////////////////////////微信登录
    private IWXAPI api;
    public static final String WCHAT_APP_ID = "wxf159411fd8b11d79";// 微信开放平台申请到的app_id
    public static final String WCHAT_APP_SECRET = "ac9a8bbaa1684cf81952f190f6b26463";// 微信开放平台申请到的app_id对应的app_secret
    private static final int RETURN_OPENID_ACCESSTOKEN = 0;// 返回openid，accessToken消息码
    private static final int RETURN_NICKNAME_UID = 1; // 返回昵称，uid消息码
    private static final String WEIXIN_SCOPE = "snsapi_userinfo";// 用于请求用户信息的作用域
    private static final String WEIXIN_STATE = "android_wchat_login_state"; // 自定义

    //////////////////////微信登录
    private void wchatLogin() {
        if (null == api) {
            api = WXAPIFactory.createWXAPI(this, WCHAT_APP_ID, true);
            api.registerApp(WCHAT_APP_ID);
        }
        try {
            api.handleIntent(getIntent(), new WXEntryActivity());
        } catch (Exception e) {
            e.printStackTrace();
        }
        final SendAuth.Req req = new SendAuth.Req();
        req.scope = WEIXIN_SCOPE;
        req.state = WEIXIN_STATE;
        api.sendReq(req);
    }

    /////////////////微信登录
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Y.y("微信登录：" + "handler:" + "丫丫");
            switch (msg.what) {
                case RETURN_OPENID_ACCESSTOKEN:
                    Y.y("微信登录：" + "handler:" + "RETURN_OPENID_ACCESSTOKEN");
                    Bundle bundle1 = (Bundle) msg.obj;
                    String accessToken = bundle1.getString("access_token");
                    String openId = bundle1.getString("openid");
                    Y.y("微信登录：" + "accessToken:" + accessToken);
                    Y.y("微信登录：" + "openId:" + openId);
                    getUID(openId, accessToken);
                    break;

                case RETURN_NICKNAME_UID:
                    Y.y("微信登录：" + "handler:" + "RETURN_NICKNAME_UID");
                    Bundle bundle2 = (Bundle) msg.obj;
                    String nickname = bundle2.getString("nickname");
                    String headimgurl = bundle2.getString("headimgurl");
                    String unionid = bundle2.getString("unionid");
                    String openId2 = bundle2.getString("openId");
                    Y.y("微信登录：" + "unionid:" + unionid);
                    CustomLoadingDialogUtils.getInstance().showDialog(LoginActivity.this, "检查用户信息");
                    wchatOpenId = openId2;
                    wChatImgUrl = headimgurl;
                    wChatNick = nickname;
                    wchatAuthSuccess = true;
                    requestThirdLoginCheckBinding(thirdType, wchatOpenId, wChatImgUrl, wChatNick);
                    break;

                default:
                    break;
            }
        }

    };

    /**
     * 获取openid accessToken值用于后期操作
     *
     * @param code 请求码
     */
    private void getResult(final String code) {
        CustomLoadingDialogUtils.getInstance().dismissDialog();
        Y.y("微信登录：" + "getResult:" + code);
        new Thread() {// 开启工作线程进行网络请求
            public void run() {
                String path = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="
                        + WCHAT_APP_ID
                        + "&secret="
                        + WCHAT_APP_SECRET
                        + "&code="
                        + code
                        + "&grant_type=authorization_code";
                try {
                    JSONObject jsonObject = JsonUtils
                            .initSSLWithHttpClinet(path);// 请求https连接并得到json结果
                    if (null != jsonObject) {
                        String openid = jsonObject.getString("openid").toString().trim();
                        String access_token = jsonObject.getString("access_token").toString().trim();
                        Y.y("微信openid = " + openid);
                        Y.y("微信access_token = " + access_token);

                        Message msg = handler.obtainMessage();
                        msg.what = RETURN_OPENID_ACCESSTOKEN;
                        Bundle bundle = new Bundle();
                        bundle.putString("openid", openid);
                        bundle.putString("access_token", access_token);
                        msg.obj = bundle;
                        handler.sendMessage(msg);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return;
            }
        }.start();
    }

    /**
     * 获取用户唯一标识
     *
     * @param openId
     * @param accessToken
     */
    private void getUID(final String openId, final String accessToken) {
        Y.y("微信登录：" + "getUID:" + openId + "      " + accessToken);
        new Thread() {
            @Override
            public void run() {
                String path = "https://api.weixin.qq.com/sns/userinfo?access_token="
                        + accessToken + "&openid=" + openId;
                JSONObject jsonObject = null;
                try {
                    jsonObject = JsonUtils.initSSLWithHttpClinet(path);
                    String nickname = jsonObject.getString("nickname");
                    String unionid = jsonObject.getString("unionid");
                    String headimgurl = jsonObject.getString("headimgurl");
                    Y.y("微信jsonObject = " + jsonObject);
                    Y.y("微信nickname = " + nickname);
                    Y.y("微信unionid = " + unionid);
                    Y.y("headimgurl = " + headimgurl);

                    Message msg = handler.obtainMessage();
                    msg.what = RETURN_NICKNAME_UID;
                    Bundle bundle = new Bundle();
                    bundle.putString("nickname", nickname);
                    bundle.putString("headimgurl", headimgurl);
                    bundle.putString("unionid", unionid);
                    bundle.putString("openId", openId);
                    msg.obj = bundle;
                    handler.sendMessage(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    WchatLoginBroadcastReceiver receiver;

    class WchatLoginBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Y.y("微信WchatLoginBroadcastReceiver ");
            try {
                if (intent.getAction().equals("com.yaxin.yyt.wchatlogin")) {
                    ToastUtil.getInstance().showToast("微信授权登录成功");
                    String code = intent.getStringExtra("code");
                    getResult(code);
                }
            } catch (Exception e) {
                e.printStackTrace();
                ToastUtil.getInstance().showToast("微信登录异常");
            }
        }

    }
    /////////////////微信登录
    // *************************************************************************************//wchat_Login

    //*************************************************************************************QQ_Login
    private Tencent mTencent;
    private String mAppid = "1106106460";

    private void qqLogin() {
        if (mTencent == null) {
            mTencent = Tencent.createInstance(mAppid, this);
        }
        if (mTencent != null) {
//        if (!mTencent.isSessionValid()) {
            mTencent.login(this, "all", loginListener);
//        }
        }
    }

    private IUiListener loginListener = new BaseUiListener() {
        @Override
        protected void doComplete(JSONObject values) {
            initOpenidAndToken(values);
            CustomLoadingDialogUtils.getInstance().dismissDialog();
            CustomLoadingDialogUtils.getInstance().showDialog(LoginActivity.this, "检查用户信息");

            qqUserInfo = new UserInfo(LoginActivity.this, mTencent.getQQToken());
            qqUserInfo.getUserInfo(new BaseUiListener() {
                @Override
                protected void doComplete(JSONObject values) {
                    super.doComplete(values);
                    if (null == values) {
                        CustomLoadingDialogUtils.getInstance().dismissDialog();
                        return;
                    }
                    Y.y("json=" + String.valueOf(values));
                    String nickName = values.optString("nickname");
                    qqNick = nickName;
                    qqImgUrl = values.optString("figureurl_qq_2");
                    requestThirdLoginCheckBinding(thirdType, qqOpenId, qqImgUrl, nickName);
                }
            });
        }
    };

    private void initOpenidAndToken(JSONObject jsonObject) {
        try {
            Y.y("initOpenidAndToken：" + jsonObject.toString());
            String token = jsonObject.getString(Constants.PARAM_ACCESS_TOKEN);
            String expires = jsonObject.getString(Constants.PARAM_EXPIRES_IN);
            String openId = jsonObject.getString(Constants.PARAM_OPEN_ID);
            if (!TextUtils.isEmpty(token) && !TextUtils.isEmpty(expires)
                    && !TextUtils.isEmpty(openId)) {
                qqAuthSuccess = true;
                qqOpenId = openId;
                mTencent.setAccessToken(token, expires);
                mTencent.setOpenId(openId);
            }
        } catch (Exception e) {
        }
    }

    private class BaseUiListener implements IUiListener {

        @Override
        public void onComplete(Object response) {
            if (null == response) {
                ToastUtil.getInstance().showToast("返回为空" + "登录失败");
                return;
            }
            JSONObject jsonResponse = (JSONObject) response;
            if (null != jsonResponse && jsonResponse.length() == 0) {
                ToastUtil.getInstance().showToast("返回为空" + "登录失败");
                return;
            }
            doComplete((JSONObject) response);
        }

        protected void doComplete(JSONObject values) {

        }

        @Override
        public void onError(UiError e) {
            CustomLoadingDialogUtils.getInstance().dismissDialog();
            ToastUtil.getInstance().showToast("QQ授权错误: " + e.errorDetail);
        }

        @Override
        public void onCancel() {
            CustomLoadingDialogUtils.getInstance().dismissDialog();
            ToastUtil.getInstance().showToast("已取消QQ授权");
        }
    }
    //*************************************************************************************QQ_Login

    /**
     * login
     *
     */
    private void requestLogin() {
//        HttpUtil.getInstance().requestLogin(InterfaceConfig.login, etLoginPhone.getText().toString(), etLoginPassword.getText().toString(), new HttpUtil.OnRequestResult<String>() {
//            @Override
//            public void onSuccess(String... msg) {
////                CustomProgressDialogUtils.dismissProcessDialog();
//                PreferencesHelper.getInstance().putInfo(ConstantsME.PHONE, RSAmethod.rsaEncrypt(LoginActivity.this, etLoginPhone.getText().toString()));
//                PreferencesHelper.getInstance().putInfo(ConstantsME.token, msg != null ? msg[0] : "");
//                PreferencesHelper.getInstance().putInfo(ConstantsME.nick, msg != null ? msg[1] : "");
//                PreferencesHelper.getInstance().putInfo(ConstantsME.imgUrl, msg != null ? msg[2] : "");
//                PreferencesHelper.getInstance().putInfo(ConstantsME.LOGINED, true);
//
//                CustomLoadingDialogUtils.getInstance().showDialog(LoginActivity.this, "检查店铺");
//                ConstantRequestUtil.getInstance().requestJustShopIds(LoginActivity.this);
//            }
//
//            @Override
//            public void onFail(int code, String msg) {
//                CustomProgressDialogUtils.dismissProcessDialog();
//                ToastUtil.getInstance().showToast(TextUtils.isEmpty(msg) ? "登录失败" : msg);
//            }
//        });
    }

    private class MyCountDownTimer extends CountDownTimer {
        private TextView tv_captcha;

        public MyCountDownTimer(TextView tv_captcha, long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
            this.tv_captcha = tv_captcha;
        }

        @Override
        public void onFinish() {// 计时完毕时触发
            tv_captcha.setText("重新获取");
            tv_captcha.setTextColor(getResources().getColor(R.color._blue));
            tv_captcha.setClickable(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {// 计时过程显示
            try {
                tv_captcha.setClickable(false);
                tv_captcha.setTextColor(getResources().getColor(
                        R.color.trans));
                tv_captcha.setText(millisUntilFinished / 1000 + "秒");
            } catch (Resources.NotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void showBindingPop(final int type, String phone, final String openId, final String pwd, final String captcha) {
//        CustomProgressDialogUtils.dismissProcessDialog();
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_UNSPECIFIED | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
//        final BindingPhonePopupwindow pop = new BindingPhonePopupwindow(LoginActivity.this);
//
////        pop.getEditText().setOnEditorActionListener(new TextView.OnEditorActionListener() {
////            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
////                if (actionId == EditorInfo.IME_ACTION_SEND) {
////                    if (!TextUtils.isEmpty(v.getText())) {
////                        Utils.isCloseSoftInputMethod(LoginActivity.this, null, true);
////                        CustomLoadingDialogUtils.getInstance().showDialog(LoginActivity.this, "正在绑定手机号");
////                        requestThirdLoginBindPhone(type, v.getText().toString(), openId, imgUrl, nick);
////                        pop.dismiss();
////                    } else {
////                        pop.getEditText().requestFocus();
////                        PicToastUtil.showPicToast(LoginActivity.this, "请输入电话号码");
////                    }
////                    return true;
////                }
////                return false;
////            }
////        });
//        long last = PreferencesHelper.getInstance().getLongData(ConstantsME.captcha_last_clicked);
//        long currentTimeMillis = System.currentTimeMillis();
//        long difference = currentTimeMillis - last;
//        int diff = (int) ((60 * 1000 - difference) / 1000);
//        Y.y("last:" + last);
//        Y.y("currentTimeMillis:" + currentTimeMillis);
//        Y.y("difference:" + difference);
//        if (difference < 60 * 1000 && diff >= 1) {
//            new MyCountDownTimer(pop.getCaptcaTextView(), diff * 1000, 1000).start();
//        }
//        pop.setContent(phone).setPassword(pwd).setCaptcha(captcha).setOnGetCaptchaListener(new BindingPhonePopupwindow.OnGetCaptchaListener() {
//            @Override
//            public void onGetCaptchaListener(String phoneNum) {
//                myCountDownTimer = new MyCountDownTimer(pop.getCaptcaTextView(), 60000, 1000);
//                myCountDownTimer.start();
//                PreferencesHelper.getInstance().putInfo(ConstantsME.captcha_last_clicked, System.currentTimeMillis());
//                CustomLoadingDialogUtils.getInstance().showDialog(LoginActivity.this, "获取验证码");
//                requestCaptcha(pop.getCaptcaTextView(), phoneNum);
//            }
//        });
//        pop.setOnDismissListener(new PopupWindow.OnDismissListener() {
//            @Override
//            public void onDismiss() {
//                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
//            }
//        });
//        pop.setOnSureClickListener(new BindingPhonePopupwindow.OnSureClickListener() {
//            @Override
//            public void onSureClickListener(String phone, String pwd, String captcha) {
//                phoneTemp = phone;
//                pwdTemp = pwd;
//                captchaTemp = captcha;
//                if (TextUtils.isEmpty(verifyKey)) {
//                    ToastUtil.getInstance().showToast("请先获取验证码");
//                } else {
//                    CustomLoadingDialogUtils.getInstance().showDialog(LoginActivity.this, "正在绑定手机号");
//                    Utils.isCloseSoftInputMethod(LoginActivity.this, pop.getEditText(), true);
//                    requestThirdLoginBindPhone(type, openId, phone, verifyKey, captcha, pwd);
//                    pop.dismiss();
//                }
//            }
//        }).showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0, 0);
    }

    /**
     * 请求获取验证码
     */
    private void requestCaptcha(final TextView tv_captcha, String phone) {
//        HttpUtil.getInstance().requestMobileVerify(InterfaceConfig.captcha, 4, phone,
//                new HttpUtil.OnRequestResult<String>() {
//                    @Override
//                    public void onSuccess(String... s) {
//                        CustomProgressDialogUtils.dismissProcessDialog();
//                        ToastUtil.getInstance().showToast("获取验证码成功");
//                        if (null != s && !TextUtils.isEmpty(s[0])) {
//                            verifyKey = s[0];
//                        }
//                    }
//
//                    @Override
//                    public void onFail(int code, String msg) {
//                        CustomProgressDialogUtils.dismissProcessDialog();
//                        ToastUtil.getInstance().showToast(TextUtils.isEmpty(msg) ? "获取验证码失败" : msg);
//                        PreferencesHelper.getInstance().putInfo(ConstantsME.captcha_last_clicked, 0L);
//                        if (null != myCountDownTimer)
//                            myCountDownTimer.cancel();
//                        tv_captcha.setText("重新获取");
//                        tv_captcha.setTextColor(getResources().getColor(R.color._blue));
//                        tv_captcha.setClickable(true);
//                    }
//                });
    }

    /**
     * 请求 检测三方登录是否绑定phone
     */
    private void requestThirdLoginCheckBinding(final int type, final String openId, final String imgUrl, final String nick) {
//        HttpUtil.getInstance().requestThirdLoginCheckBinding(InterfaceConfig.thirdLoginCheckBinding, type, openId, nick, imgUrl,
//                new HttpUtil.OnRequestResult<String>() {
//                    @Override
//                    public void onSuccess(String... s) {
////                        CustomProgressDialogUtils.dismissProcessDialog();
//                        if (null != s) {
//                            if (type == 2) {
//                                checkQQBindSuccess = true;
//                            } else if (type == 3) {
//                                checkWchatBindSuccess = true;
//                            }
//                            if (!TextUtils.isEmpty(s[0])) {
//                                PreferencesHelper.getInstance().putInfo(ConstantsME.PHONE, RSAmethod.rsaEncrypt(LoginActivity.this, s[0]));
//                                PreferencesHelper.getInstance().putInfo(ConstantsME.token, s.length > 1 ? s[1] : "");
//                                PreferencesHelper.getInstance().putInfo(ConstantsME.nick, s.length > 2 ? s[2] : "");
//                                PreferencesHelper.getInstance().putInfo(ConstantsME.imgUrl, s.length > 3 ? s[3] : "");
//                                PreferencesHelper.getInstance().putInfo(ConstantsME.LOGINED, true);
//                                if (type == 2) {
//                                    PreferencesHelper.getInstance().putInfo(ConstantsME.qqBind, true);
//                                } else if (type == 3) {
//                                    PreferencesHelper.getInstance().putInfo(ConstantsME.wChatBind, true);
//                                }
//                                CustomLoadingDialogUtils.getInstance().showDialog(LoginActivity.this, "检查店铺");
//                                ConstantRequestUtil.getInstance().requestJustShopIds(LoginActivity.this);
////                                startActivity(HomepageActivity.class);
////                                finish();
//                            } else {
//                                PreferencesHelper.getInstance().putInfo(ConstantsME.PHONE, "");
//                                PreferencesHelper.getInstance().putInfo(ConstantsME.token, s.length > 1 ? s[1] : "");
//                                PreferencesHelper.getInstance().putInfo(ConstantsME.nick, s.length > 2 ? s[2] : "");
//                                PreferencesHelper.getInstance().putInfo(ConstantsME.imgUrl, s.length > 3 ? s[3] : "");
//                                PreferencesHelper.getInstance().putInfo(ConstantsME.LOGINED, false);
//                                showBindingPop(type, "", openId, "", "");
//                            }
//                        } else {
//                            showBindingPop(type, "", openId, "", "");
//                        }
//                    }
//
//                    @Override
//                    public void onFail(int code, String msg) {
//                        CustomProgressDialogUtils.dismissProcessDialog();
//                        ToastUtil.getInstance().showToast(TextUtils.isEmpty(msg) ? "未检测是否绑定手机" : msg);
////                        showBindingPop(type, "", openId, "", "");
//                        if (type == 2) {
//                            checkQQBindSuccess = false;
//                        } else if (type == 3) {
//                            checkWchatBindSuccess = false;
//                        }
//                    }
//                });
    }

    /**
     * 请求 三方登录绑定phone
     */
    private void requestThirdLoginBindPhone(final int type, final String openId, final String phone, final String mobileValidVoucher, final String moblieVerifyCode, final String password) {
//        HttpUtil.getInstance().requestThirdLoginBindPhone(InterfaceConfig.thirdLoginBindPhone, type, openId, phone, mobileValidVoucher, moblieVerifyCode, password,
//                new HttpUtil.OnRequestResult<String>() {
//                    @Override
//                    public void onSuccess(String... s) {
//                        if (null != s) {
//                            if (!TextUtils.isEmpty(s[0]) && TextUtils.equals("true", s[0])) {
//                                PreferencesHelper.getInstance().putInfo(ConstantsME.PHONE, RSAmethod.rsaEncrypt(LoginActivity.this, phone));
//                                if (s.length > 1 && !TextUtils.isEmpty(s[1]))
//                                    PreferencesHelper.getInstance().putInfo(ConstantsME.token, s[1]);
//                                if (s.length > 2 && !TextUtils.isEmpty(s[2]))
//                                    PreferencesHelper.getInstance().putInfo(ConstantsME.nick, s[2]);
//                                if (s.length > 3 && !TextUtils.isEmpty(s[3]))
//                                    PreferencesHelper.getInstance().putInfo(ConstantsME.imgUrl, s[3]);
//                                PreferencesHelper.getInstance().putInfo(ConstantsME.LOGINED, true);
//                                if (type == 2) {
//                                    PreferencesHelper.getInstance().putInfo(ConstantsME.qqBind, true);
//                                } else if (type == 3) {
//                                    PreferencesHelper.getInstance().putInfo(ConstantsME.wChatBind, true);
//                                }
//                                CustomLoadingDialogUtils.getInstance().showDialog(LoginActivity.this, "检查店铺");
//                                ConstantRequestUtil.getInstance().requestJustShopIds(LoginActivity.this);
////                                startActivity(HomepageActivity.class);
////                                finish();
//                            } else {
//                                onFail(1, "");
//                            }
//                        } else {
//                            onFail(1, "");
//                        }
//                    }
//
//                    @Override
//                    public void onFail(int code, String msg) {
//                        ToastUtil.getInstance().showToast(TextUtils.isEmpty(msg) ? "未成功绑定手机" : msg);
//                        CustomProgressDialogUtils.dismissProcessDialog();
//                        showBindingPop(type, phone, openId, password, moblieVerifyCode);
//                    }
//                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && resultCode == Activity.RESULT_OK && requestCode == IntentCode.login) {
            String phone = data.getStringExtra(ConstantsME.PHONE);
            if (!TextUtils.isEmpty(phone)) {
                etLoginPhone.setText(phone);
                etLoginPassword.requestFocus();
            }
        } else if (requestCode == Constants.REQUEST_LOGIN ||
                requestCode == Constants.REQUEST_APPBAR) {
            Y.y("REQUEST_LOGIN:");
            Tencent.onActivityResultData(requestCode, resultCode, data, loginListener);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        Y.y("onWindowFocusChanged" + hasFocus);
    }
}
