package com.demo.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.CountDownTimer;
import androidx.annotation.NonNull;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.demo.configs.ConstantsME;
import com.demo.demo.R;
import com.framework.security.RSAmethodInRaw;
import com.framework.util.KeyBoardUtil;
import com.framework.util.PreferencesHelper;
import com.framework.util.RegularUtil;
import com.framework.util.ScreenUtil;
import com.framework.util.ToastUtil;
import com.framework.util.Y;
import com.framework.widget.OverScrollView;
import com.framework2.baseEvent.BaseOnClickListener;
import com.framework2.customviews.TitleView;
import com.framework2.utils.CustomLoadingDialogUtils;
import com.framework2.utils.PicToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 忘记密码
 *
 * @author Yangjie
 * className ForgetPasswordActivity
 * created at  2017/4/5  11:10
 */
public class ForgetPasswordActivity extends BaseActivity {
    @BindView(R.id.titleView)
    TitleView titleView;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.et_captcha)
    EditText etCaptcha;
    @BindView(R.id.tv_captcha)
    TextView tvCaptcha;
    @BindView(R.id.forgetLayout)
    LinearLayout forgetLayout;
    @BindView(R.id.tv_sure)
    TextView tvSure;
    @BindView(R.id.overScrollView)
    OverScrollView overScrollView;
    @BindView(R.id.tv_back)
    TextView tvBack;
    @BindView(R.id.tv_exist)
    TextView tvExist;//倒计时返回
    @BindView(R.id.successLayout)
    LinearLayout successLayout;
    private MyCountDownTimer myCountDownTimer;
    private ExistCountDownTimer existCountDownTimer;
    private boolean resetPwdSuccess;
    private String verifyKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pwd);
        ButterKnife.bind(this);
        ScreenUtil.getInstance().setTranslucentStatus(this, true);
        ScreenUtil.getInstance().setStatusBarTintColor(this,
                getResources().getColor(R.color.white));
        initView();
        myCountDownTimer = new MyCountDownTimer(60000, 1000);
        initData();
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
                        KeyBoardUtil.getInstance().isCloseSoftInputMethod(ForgetPasswordActivity.this,
                                etPhone, true);
                    }
                });
        String phoneTemp = RSAmethodInRaw.getInstance().rsaDecrypt(this,
                PreferencesHelper.getInstance().getStringData(ConstantsME.PHONE_TEMP));
        long last = PreferencesHelper.getInstance().getLongData(ConstantsME.captcha_last_clicked);
        long currentTimeMillis = System.currentTimeMillis();
        long difference = currentTimeMillis - last;
        int diff = (int) ((60 * 1000 - difference) / 1000);
        if (difference < 60 * 1000 && diff >= 1) {
            if (!TextUtils.isEmpty(phoneTemp)) {
                etPhone.setText(phoneTemp);
                etPhone.setSelection(phoneTemp.length());
            }
            new MyCountDownTimer(diff * 1000, 1000).start();
        } else {
            String phone = RSAmethodInRaw.getInstance().rsaDecrypt(this,
                    PreferencesHelper.getInstance().getStringData(ConstantsME.PHONE));
            if (!TextUtils.isEmpty(phone)) {
                etPhone.setText(phone);
                etPhone.setSelection(phone.length());
                etPhone.requestFocus();
                KeyBoardUtil.getInstance().isCloseSoftInputMethod(ForgetPasswordActivity.this,
                        etPhone, false);
            } else if (!TextUtils.isEmpty(getIntent().getStringExtra(ConstantsME.PHONE))) {
                etPhone.setText(getIntent().getStringExtra(ConstantsME.PHONE));
                etPhone.setSelection(getIntent().getStringExtra(ConstantsME.PHONE).length());
                etPhone.requestFocus();
                KeyBoardUtil.getInstance().isCloseSoftInputMethod(ForgetPasswordActivity.this,
                        etPhone, false);
            }
        }
    }

    private void initView() {
        titleView.setTitle("忘记密码").setRightVisible(false).setLeftOnClickListener(new BaseOnClickListener() {
            @Override
            protected void onBaseClick(View v) {
                if (resetPwdSuccess) {
                    back();
                } else {
                    finishActivity();
                }
            }
        });
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_captcha:
                if (null == etPhone.getText()
                        || "".equals(etPhone.getText().toString())) {
                    PicToastUtil.getInstance().showPicToast(ForgetPasswordActivity.this,
                            "请输入你的手机号码!");
                    etPhone.requestFocus();
                    KeyBoardUtil.getInstance().isCloseSoftInputMethod(ForgetPasswordActivity.this
                            , etPhone, false);
                } else if (!RegularUtil.getInstance().isMobileNO(etPhone.getText().toString())) {
                    PicToastUtil.getInstance().showPicToast(ForgetPasswordActivity.this,
                            "请输入正确的手机号码!");
                    etPhone.requestFocus();
                    KeyBoardUtil.getInstance().isCloseSoftInputMethod(ForgetPasswordActivity.this
                            , etPhone, false);
                } else {
                    PreferencesHelper.getInstance().putInfo(ConstantsME.PHONE_TEMP,
                            RSAmethodInRaw.getInstance().rsaEncrypt(this,
                            etPhone.getText().toString()));
                    PreferencesHelper.getInstance().putInfo(ConstantsME.captcha_last_clicked, System
                            .currentTimeMillis());
                    myCountDownTimer.start();
                    CustomLoadingDialogUtils.getInstance().showDialog(ForgetPasswordActivity.this
                            , "正在获取验证码");
                    requestCaptcha();
                }
                break;
            case R.id.tv_sure:
                KeyBoardUtil.getInstance().isCloseSoftInputMethod(ForgetPasswordActivity.this,
                        etPhone, true);
                if (isLegal(etPhone, etPassword, etCaptcha)) {
                    if (TextUtils.isEmpty(verifyKey)) {
                        ToastUtil.getInstance().showToast("请先获取验证码");
                    } else {
                        CustomLoadingDialogUtils.getInstance().showDialog(ForgetPasswordActivity.this, "正在重置密码");
                        requestResetPwd(verifyKey);
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
//        HttpUtil.getProxyApplication().requestMobileVerify(InterfaceConfig.captcha, 2, etPhone
//        .getText().toString(),
//                new HttpUtil.OnRequestResult<String>() {
//                    @Override
//                    public void onSuccess(String... s) {
//                        CustomProgressDialogUtils.dismissProcessDialog();
//                        ToastUtil.getProxyApplication().showToast("获取验证码成功");
//                        if (null != s && !TextUtils.isEmpty(s[0])) {
//                            verifyKey = s[0];
//                        }
//                    }
//
//                    @Override
//                    public void onFail(int code, String msg) {
//                        CustomProgressDialogUtils.dismissProcessDialog();
//                        ToastUtil.getProxyApplication().showToast(TextUtils.isEmpty(msg) ?
//                        "获取验证码失败" : msg);
//                        PreferencesHelper.getProxyApplication().putInfo(ConstantsME
//                        .captcha_last_clicked, 0L);
//                        if (null != myCountDownTimer)
//                            myCountDownTimer.cancel();
//                        tvCaptcha.setText("重新获取");
//                        tvCaptcha.setTextColor(getResources().getColor(R.color._blue));
//                        tvCaptcha.setClickable(true);
//                    }
//                });
    }

    /**
     * 请求重置密码
     */
    private void requestResetPwd(@NonNull String verifyKey) {
//        HttpUtil.getProxyApplication().requestForgetPwd(InterfaceConfig.forgetPwd, etPhone
//        .getText().toString(), etPassword
// .getText().toString(), verifyKey, etCaptcha.getText().toString(),
//                new HttpUtil.OnRequestResult<String>() {
//                    @Override
//                    public void onSuccess(String... s) {
//                        CustomProgressDialogUtils.dismissProcessDialog();
//                        ToastUtil.getProxyApplication().showToast("密码重置成功");
//                        resetPwdSuccess();
//                    }
//
//                    @Override
//                    public void onFail(int code, String msg) {
//                        CustomProgressDialogUtils.dismissProcessDialog();
//                        ToastUtil.getProxyApplication().showToast(TextUtils.isEmpty(msg) ?
//                        "找回密码失败" : msg);
//                    }
//                });
    }

    /**
     * 重置密码成功
     */
    private void resetPwdSuccess() {
        KeyBoardUtil.getInstance().isCloseSoftInputMethod(this, etPhone, true);
        resetPwdSuccess = true;
        successLayout.setVisibility(View.VISIBLE);
        existCountDownTimer = new ExistCountDownTimer(5000, 1000);
        existCountDownTimer.start();
    }

    /**
     * 返回
     */
    private void back() {
        Intent intent = getIntent();
        intent.putExtra(ConstantsME.PHONE, etPhone.getText().toString());
        setResult(RESULT_OK, intent);
        finishActivity();
    }

    private boolean isLegal(EditText etPhone, EditText et_password, EditText et_captcha) {
        if (null == etPhone || null == et_password)
            return false;
        if (TextUtils.isEmpty(etPhone.getText())) {
            PicToastUtil.getInstance().showPicToast(ForgetPasswordActivity.this, "请输入你的手机号码!");
            etPhone.requestFocus();
            KeyBoardUtil.getInstance().isCloseSoftInputMethod(ForgetPasswordActivity.this,
                    etPhone, false);
            return false;
        } else if (!RegularUtil.getInstance().isMobileNO(etPhone.getText().toString())) {
            PicToastUtil.getInstance().showPicToast(ForgetPasswordActivity.this, "请输入正确的手机号码!");
            etPhone.requestFocus();
            KeyBoardUtil.getInstance().isCloseSoftInputMethod(ForgetPasswordActivity.this,
                    etPhone, false);
            return false;
        } else if (TextUtils.isEmpty(et_password.getText())) {
            PicToastUtil.getInstance().showPicToast(ForgetPasswordActivity.this, "请输入密码!");
            et_password.requestFocus();
            KeyBoardUtil.getInstance().isCloseSoftInputMethod(ForgetPasswordActivity.this,
                    etPhone, false);
            return false;
        } else if (TextUtils.isEmpty(et_captcha.getText())) {
            PicToastUtil.getInstance().showPicToast(ForgetPasswordActivity.this, "请输入验证码!");
            et_captcha.requestFocus();
            KeyBoardUtil.getInstance().isCloseSoftInputMethod(ForgetPasswordActivity.this,
                    etPhone, false);
            return false;
        }
        return true;
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
            if (resetPwdSuccess) {
                back();
            } else {
                finishActivity();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private class MyCountDownTimer extends CountDownTimer {
        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {// 计时完毕时触发
            tvCaptcha.setText("重新获取");
//			tv_get_captcha.setBackgroundDrawable(getResources().getDrawable(
//					R.drawable.selector_bg_captcha_clickable));
            tvCaptcha.setTextColor(getResources().getColor(R.color._blue));
            tvCaptcha.setClickable(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {// 计时过程显示
            try {
                tvCaptcha.setClickable(false);
                tvCaptcha.setTextColor(getResources().getColor(
                        R.color.trans));
                tvCaptcha.setText(millisUntilFinished / 1000 + "秒");
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
                tvExist.setText(0 + "S" + "后返回登录页面");
            } catch (Exception e) {
                e.printStackTrace();
            }
            back();
        }

        @Override
        public void onTick(long millisUntilFinished) {// 计时过程显示
            Y.y("onTick:" + millisUntilFinished);
            try {
                tvExist.setText(millisUntilFinished / 1000 + "S" + "后返回登录页面");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
