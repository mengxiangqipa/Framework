package com.demo.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.demo.configs.ConstantsME;
import com.demo.demo.R;
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
import com.framework2.customviews.TitleView;

/**
 * 注册
 *
 * @author Yangjie
 *         className RegisterActivity
 *         created at  2017/3/15  11:30
 */
public class RegisterActivity extends BaseActivity {
    private EditText et_register_phone, et_register_password, et_register_captcha;
    private TextView tv_captcha, tv_protocol;
    private TextView tv_exist;//倒计时返回
    private MyCountDownTimer myCountDownTimer;
    private ExistCountDownTimer existCountDownTimer;
    private boolean registerSuccess;
    private String verifyKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ScreenUtils.getInstance().setTranslucentStatus(this, true);
        ScreenUtils.getInstance().setStatusBarTintColor(this,
                getResources().getColor(R.color.white));
        initView();
        myCountDownTimer = new MyCountDownTimer(60000, 1000);
        initData();
    }

    private void initData() {
        ((OverScrollView) findViewById(R.id.overScrollView))
                .setOverScrollTinyListener(new OverScrollView.OverScrollTinyListener() {

                    @Override
                    public void scrollLoosen() {

                    }

                    @Override
                    public void scrollDistance(int tinyDistance,
                                               int totalDistance) {
                        KeyBoardUtil.getInstance().isCloseSoftInputMethod(RegisterActivity.this,
                                et_register_phone, true);
                    }
                });
        String phoneTemp = RSAmethodInRaw.rsaDecrypt(this,
                PreferencesHelper.getInstance().getStringData(ConstantsME.PHONE_TEMP));
        long last = PreferencesHelper.getInstance().getLongData(ConstantsME.captcha_last_clicked);
        long currentTimeMillis = System.currentTimeMillis();
        long difference = currentTimeMillis - last;
        int diff = (int) ((60 * 1000 - difference) / 1000);
        if (difference < 60 * 1000 && diff >= 1) {
            if (!TextUtils.isEmpty(phoneTemp)) {
                et_register_phone.setText(phoneTemp);
                et_register_phone.setSelection(phoneTemp.length());
            }
            new MyCountDownTimer(diff * 1000, 1000).start();
        } else {
            String phone = RSAmethodInRaw.rsaDecrypt(this,
                    PreferencesHelper.getInstance().getStringData(ConstantsME.PHONE));
            if (!TextUtils.isEmpty(phone)) {
                et_register_phone.setText(phone);
                et_register_phone.setSelection(phone.length());
                et_register_phone.requestFocus();
                KeyBoardUtil.getInstance().isCloseSoftInputMethod(RegisterActivity.this, et_register_phone, false);
            } else if (!TextUtils.isEmpty(getIntent().getStringExtra(ConstantsME.PHONE))) {
                et_register_phone.setText(getIntent().getStringExtra(ConstantsME.PHONE));
                et_register_phone.setSelection(getIntent().getStringExtra(ConstantsME.PHONE).length());
                et_register_phone.requestFocus();
                KeyBoardUtil.getInstance().isCloseSoftInputMethod(RegisterActivity.this, et_register_phone, false);
            }
        }
    }

    private void initView() {
        TitleView titleView = (TitleView) findViewById(R.id.titleView);
        titleView.setTitle(R.string.register).setRightVisible(false).setLeftOnClickListener(new BaseOnClickListener() {
            @Override
            protected void onBaseClick(View v) {
                if (registerSuccess) {
                    back();
                } else {
                    finishActivity();
                }
            }
        });

        et_register_phone = (EditText) findViewById(R.id.et_register_phone);
        et_register_password = (EditText) findViewById(R.id.et_register_password);
        et_register_captcha = (EditText) findViewById(R.id.et_register_captcha);

        tv_captcha = (TextView) findViewById(R.id.tv_captcha);
        tv_exist = (TextView) findViewById(R.id.tv_exist);

        tv_protocol = (TextView) findViewById(R.id.tv_protocol);
        SpannableString spannableString = new SpannableString(getString(R.string.protocol));
        spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color._blue)), 14, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_protocol.setText(spannableString);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_captcha:
                if (null == et_register_phone.getText()
                        || "".equals(et_register_phone.getText().toString())) {
                    PicToastUtil.getInstance().showPicToast(RegisterActivity.this, "请输入你的手机号码!");
                    et_register_phone.requestFocus();
                    KeyBoardUtil.getInstance().isCloseSoftInputMethod(RegisterActivity.this, et_register_phone, false);
                } else if (!RegularUtil.getInstance().isMobileNO(et_register_phone.getText().toString())) {
                    PicToastUtil.getInstance().showPicToast(RegisterActivity.this, "请输入正确的手机号码!");
                    et_register_phone.requestFocus();
                    KeyBoardUtil.getInstance().isCloseSoftInputMethod(RegisterActivity.this, et_register_phone, false);
                } else {
                    PreferencesHelper.getInstance().putInfo(ConstantsME.PHONE_TEMP, RSAmethodInRaw.rsaEncrypt(this, et_register_phone.getText().toString()));
                    PreferencesHelper.getInstance().putInfo(ConstantsME.captcha_last_clicked, System.currentTimeMillis());
                    myCountDownTimer.start();
                    CustomLoadingDialogUtils.getInstance().showDialog(RegisterActivity.this, "获取验证码");
                    requestCaptcha();
                }
                break;
            case R.id.tv_sure:
//                verifyKey="test";
                KeyBoardUtil.getInstance().isCloseSoftInputMethod(RegisterActivity.this, et_register_phone, true);
                if (isLegal(et_register_phone, et_register_password, et_register_captcha)) {
                    if (TextUtils.isEmpty(verifyKey)) {
                        ToastUtil.getInstance().showToast("请先获取验证码");
                    } else {
                        CustomLoadingDialogUtils.getInstance().showDialog(RegisterActivity.this, "正在注册");
                        requestRegister(verifyKey);
                    }
                }
                break;
            case R.id.tv_back:
                back();
                break;
            default:
                break;
        }
    }

    /**
     * 请求获取验证码
     */
    private void requestCaptcha() {
//        HttpUtil.getInstance().requestMobileVerify(InterfaceConfig.captcha, 1, et_register_phone.getText().toString(),
//                new HttpUtil.OnRequestResult<String>() {
//                    @Override
//                    public void onSuccess(String... s) {
//                        CustomProgressDialogUtils.dismissProcessDialog();
//                        ToastUtil.getInstance().showToast("获取验证码成功");
//                        if (null!=s&&!TextUtils.isEmpty(s[0])) {
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
     * 请求注册
     */

    private void requestRegister(@NonNull String verifyKey) {
//        HttpUtil.getInstance().requestRegister(InterfaceConfig.register, et_register_phone.getText().toString(), et_register_password.getText().toString(),verifyKey, et_register_captcha.getText().toString(),
//                new HttpUtil.OnRequestResult<String>() {
//                    @Override
//                    public void onSuccess(String... s) {
//                        CustomProgressDialogUtils.dismissProcessDialog();
//                        ToastUtil.getInstance().showToast("注册成功");
//                        registerSuccess();
//                    }
//
//                    @Override
//                    public void onFail(int code, String msg) {
//                        CustomProgressDialogUtils.dismissProcessDialog();
//                        ToastUtil.getInstance().showToast(TextUtils.isEmpty(msg) ? "注册失败" : msg);
//                    }
//                });
    }

    /**
     * 注册成功
     */
    private void registerSuccess() {
        KeyBoardUtil.getInstance().isCloseSoftInputMethod(this, et_register_phone, true);
        registerSuccess = true;
        findViewById(R.id.successLayout).setVisibility(View.VISIBLE);
        existCountDownTimer = new ExistCountDownTimer(5000, 1000);
        existCountDownTimer.start();
    }

    /**
     * 返回
     */
    private void back() {
        Intent intent = getIntent();
        intent.putExtra(ConstantsME.PHONE, et_register_phone.getText().toString());
        setResult(RESULT_OK, intent);
        finishActivity();
    }

    private boolean isLegal(EditText et_phone, EditText et_password, EditText et_captcha) {
        if (null == et_phone || null == et_password)
            return false;
        if (TextUtils.isEmpty(et_phone.getText())) {
            PicToastUtil.getInstance().showPicToast(RegisterActivity.this, "请输入你的手机号码!");
            et_phone.requestFocus();
            KeyBoardUtil.getInstance().isCloseSoftInputMethod(RegisterActivity.this, et_phone, false);
            return false;
        } else if (!RegularUtil.getInstance().isMobileNO(et_phone.getText().toString())) {
            PicToastUtil.getInstance().showPicToast(RegisterActivity.this, "请输入正确的手机号码!");
            et_phone.requestFocus();
            KeyBoardUtil.getInstance().isCloseSoftInputMethod(RegisterActivity.this, et_phone, false);
            return false;
        } else if (TextUtils.isEmpty(et_password.getText())) {
            PicToastUtil.getInstance().showPicToast(RegisterActivity.this, "请输入密码!");
            et_password.requestFocus();
            KeyBoardUtil.getInstance().isCloseSoftInputMethod(RegisterActivity.this, et_phone, false);
            return false;
        } else if (TextUtils.isEmpty(et_captcha.getText())) {
            PicToastUtil.getInstance().showPicToast(RegisterActivity.this, "请输入验证码!");
            et_captcha.requestFocus();
            KeyBoardUtil.getInstance().isCloseSoftInputMethod(RegisterActivity.this, et_phone, false);
            return false;
        }
        return true;
    }

    private class MyCountDownTimer extends CountDownTimer {
        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
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

    private class ExistCountDownTimer extends CountDownTimer {
        public ExistCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {// 计时完毕时触发
            try {
                tv_exist.setText(0 + "S" + "后返回登录页面");
            } catch (Exception e) {
                e.printStackTrace();
            }
            back();
        }

        @Override
        public void onTick(long millisUntilFinished) {// 计时过程显示
            Y.y("onTick:" + millisUntilFinished);
            try {
                tv_exist.setText(millisUntilFinished / 1000 + "S" + "后返回登录页面");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (myCountDownTimer != null) {
            myCountDownTimer.cancel();
            myCountDownTimer = null;
        }
        if (existCountDownTimer != null) {
            existCountDownTimer.cancel();
            existCountDownTimer = null;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (registerSuccess) {
                back();
            } else {
                finishActivity();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
