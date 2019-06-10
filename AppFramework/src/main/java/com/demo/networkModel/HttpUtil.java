package com.demo.networkModel;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.demo.configs.InterfaceConfig;
import com.framework.util.JSONParseUtil;
import com.framework.util.Y;
import com.framework2.okhttp3.HandlerCallback;
import com.framework2.okhttp3.Ok3Util;
import com.framework2.okhttp3.StringRequest;

import org.json.JSONArray;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.OkHttpClient;

/**
 * HttpUtil
 *
 * @author Yangjie
 * className GohomePopupWindowUtil
 * created at  2017/3/17  13:01
 */
public class HttpUtil {
    private static volatile HttpUtil singleton;
    private DecimalFormat decimalFormat = (DecimalFormat) DecimalFormat.getInstance();

    private HttpUtil() {
    }

    public static HttpUtil getInstance() {
        if (singleton == null) {
            synchronized (HttpUtil.class) {
                if (singleton == null) {
                    singleton = new HttpUtil();
                }
            }
        }
        return singleton;
    }

    /**
     * 手机登录
     */
    public void requestLogin(String _interface, @NonNull String phone, @NonNull String pwd, final
    OnRequestResult<String> resultListener) {
        HashMap<String, String> map = new HashMap<>();
        map.put("mobile", phone);
        map.put("password", pwd);
        StringRequest jsonRequest = StringRequest.getBuilder(false).url(InterfaceConfig.BASE_SERVER_URL + _interface)
                .post(map)
                .build(new HandlerCallback() {
                    @Override
                    public void onResponseMainThread(Call call, String result) throws IOException {
                        Y.y("JSONRequestonResponse:" + result);
                        if (null != resultListener) {
                            boolean success = ParseResponseUtil.getInstance().isSuccess(result);
                            String token = ParseResponseUtil.getInstance().parseReturnString(result, "userKey");
                            String nickName = ParseResponseUtil.getInstance().parseReturnString(result, "nickName");
                            String headPicUrl = ParseResponseUtil.getInstance().parseReturnString(result, "headPicUrl");
                            String errorMsg = ParseResponseUtil.getInstance().parseReturnStringError(result);
                            if (success) {
                                if (!TextUtils.isEmpty(token)) {
                                    resultListener.onSuccess(token, nickName, headPicUrl);
                                } else {
                                    resultListener.onFail(2, "未获取有效token");
                                }
                            } else {
                                resultListener.onFail(1, errorMsg);
                            }
                        }
                    }

                    @Override
                    public void onFailureMainThread(Call call, IOException e) {
                        if (null != resultListener) {
                            resultListener.onFail(0, e.getMessage());
                        }
                        Y.y("JSONRequestonFailure:" + e.getMessage());
                    }
                });
        Ok3Util.getInstance().setBuilder(new OkHttpClient.Builder().connectTimeout(8000, TimeUnit.MILLISECONDS)
                .retryOnConnectionFailure(false)).addToRequestQueueAsynchoronous(false, jsonRequest);
    }

    /**
     * 手机登出/退出
     */
    public void requestLogout(String _interface, final OnRequestResult<String> resultListener) {

        StringRequest jsonRequest = StringRequest.getBuilder(false).url(InterfaceConfig.BASE_SERVER_URL + _interface)
                .post(null)
                .build(new HandlerCallback() {
                    @Override
                    public void onResponseMainThread(Call call, String result) throws IOException {
                        if (null != resultListener) {
                            boolean success = ParseResponseUtil.getInstance().isSuccess(result);
                            String errorMsg = ParseResponseUtil.getInstance().parseReturnStringError(result);
                            if (success) {
                                resultListener.onSuccess("");
                            } else {
                                resultListener.onFail(1, errorMsg);
                            }
                        }
                    }

                    @Override
                    public void onFailureMainThread(Call call, IOException e) {
                        if (null != resultListener) {
                            resultListener.onFail(0, e.getMessage());
                        }
                    }
                });
        Ok3Util.getInstance().setBuilder(new OkHttpClient.Builder().connectTimeout(8000, TimeUnit.MILLISECONDS)
                .retryOnConnectionFailure(false)).addToRequestQueueAsynchoronous(false, jsonRequest);
    }

    /**
     * 获取验证码（各种）
     */
    public void requestMobileVerify(String _interface, @NonNull int codeType, @NonNull String phone, final
    OnRequestResult<String> resultListener) {
        HashMap<String, String> map = new HashMap<>();
        map.put("codeType", codeType + "");
        map.put("mobile", phone);
        StringRequest jsonRequest = StringRequest.getBuilder(false).url(InterfaceConfig.BASE_SERVER_URL + _interface)
                .post(map)
                .build(new HandlerCallback() {
                    @Override
                    public void onResponseMainThread(Call call, String result) throws IOException {
                        Y.y("JSONRequestonResponse:" + result);
                        String code = ParseResponseUtil.getInstance().parseVerifyCode(result);
                        String errorMsg = ParseResponseUtil.getInstance().parseReturnStringError(result);
                        if (null != resultListener && !TextUtils.isEmpty(code)) {
                            resultListener.onSuccess(code);
                        } else if (null != resultListener) {
                            resultListener.onFail(1, TextUtils.isEmpty(errorMsg) ? "获取验证码失败" : errorMsg);
                        }
                    }

                    @Override
                    public void onFailureMainThread(Call call, IOException e) {
                        if (null != resultListener) {
                            resultListener.onFail(0, "获取验证码失败");
                        }
                        Y.y("JSONRequestonFailure:" + e.getMessage());
                    }
                });
        Ok3Util.getInstance().addToRequestQueueAsynchoronous(false, jsonRequest);
    }

    /**
     * 手机号注册
     */
    public void requestRegister(String _interface, @NonNull String phone, @NonNull String pwd, @NonNull String
            captchaKey, @NonNull String captcha, final OnRequestResult<String> resultListener) {
        HashMap<String, String> map = new HashMap<>();
        map.put("userName", phone);
        map.put("mobile", phone);
        map.put("password", pwd);
        map.put("mobileValidVoucher", captchaKey);
        map.put("mobileVerifyCode", captcha);
        StringRequest jsonRequest = StringRequest.getBuilder(false).url(InterfaceConfig.BASE_SERVER_URL + _interface)
                .post(map)
                .build(new HandlerCallback() {
                    @Override
                    public void onResponseMainThread(Call call, String result) throws IOException {
                        Y.y("JSONRequestonResponse:" + result);
                        if (null != resultListener) {
                            boolean success = ParseResponseUtil.getInstance().isSuccess(result);
                            String errorMsg = ParseResponseUtil.getInstance().parseReturnStringError(result);
                            if (success) {
                                resultListener.onSuccess("");
                            } else {
                                resultListener.onFail(1, errorMsg);
                            }
                        }
                    }

                    @Override
                    public void onFailureMainThread(Call call, IOException e) {
                        if (null != resultListener) {
                            resultListener.onFail(0, e.getMessage());
                        }
                        Y.y("JSONRequestonFailure:" + e.getMessage());
                    }
                });
        Ok3Util.getInstance().addToRequestQueueAsynchoronous(false, jsonRequest);
    }

    /**
     * 修改密码
     */
    public void requestModifyPwd(String _interface, @NonNull String pwd_pre, @NonNull String pwd_new, @NonNull String
            pwd_new_again, final OnRequestResult<String> resultListener) {
        HashMap<String, String> map = new HashMap<>();
        map.put("oldPassword", pwd_pre);
        map.put("newPassword", pwd_new);
        map.put("reNewPassword", pwd_new_again);
        StringRequest jsonRequest = StringRequest.getBuilder(false).url(InterfaceConfig.BASE_SERVER_URL + _interface)
                .post(map)
                .build(new HandlerCallback() {
                    @Override
                    public void onResponseMainThread(Call call, String result) throws IOException {
                        if (null != resultListener) {
                            boolean success = ParseResponseUtil.getInstance().isSuccess(result);
                            String errorMsg = ParseResponseUtil.getInstance().parseReturnStringError(result);
                            if (success) {
                                resultListener.onSuccess("");
                            } else {
                                resultListener.onFail(1, errorMsg);
                            }
                        }
                    }

                    @Override
                    public void onFailureMainThread(Call call, IOException e) {
                        if (null != resultListener) {
                            resultListener.onFail(0, e.getMessage());
                        }
                    }
                });
        Ok3Util.getInstance().addToRequestQueueAsynchoronous(false, jsonRequest);
    }

    /**
     * 忘记密码/重置密码
     */
    public void requestForgetPwd(String _interface, @NonNull String phone, @NonNull String pwd, @NonNull String
            captchaKey, @NonNull String captcha, final OnRequestResult<String> resultListener) {
        HashMap<String, String> map = new HashMap<>();
        map.put("mobile", phone);
        map.put("newPassword", pwd);
        map.put("reNewPassword", pwd);
        map.put("smsValidVoucher", captchaKey);
        map.put("codeContent", captcha);
        StringRequest jsonRequest = StringRequest.getBuilder(false).url(InterfaceConfig.BASE_SERVER_URL + _interface)
                .post(map)
                .build(new HandlerCallback() {
                    @Override
                    public void onResponseMainThread(Call call, String result) throws IOException {
                        if (null != resultListener) {
                            boolean success = ParseResponseUtil.getInstance().isSuccess(result);
                            String errorMsg = ParseResponseUtil.getInstance().parseReturnStringError(result);
                            if (success) {
                                resultListener.onSuccess("");
                            } else {
                                resultListener.onFail(1, errorMsg);
                            }
                        }
                    }

                    @Override
                    public void onFailureMainThread(Call call, IOException e) {
                        if (null != resultListener) {
                            resultListener.onFail(0, e.getMessage());
                        }
                    }
                });
        Ok3Util.getInstance().addToRequestQueueAsynchoronous(false, jsonRequest);
    }

    /**
     * 修改昵称
     */
    public void requestModifyNick(String _interface, @NonNull String nick, final OnRequestResult<String>
            resultListener) {
        HashMap<String, String> map = new HashMap<>();
        map.put("nickName", nick);
        StringRequest jsonRequest = StringRequest.getBuilder(false).url(InterfaceConfig.BASE_SERVER_URL + _interface)
                .post(map)
                .build(new HandlerCallback() {
                    @Override
                    public void onResponseMainThread(Call call, String result) throws IOException {
                        if (null != resultListener) {
                            boolean success = ParseResponseUtil.getInstance().isSuccess(result);
                            String errorMsg = ParseResponseUtil.getInstance().parseReturnStringError(result);
                            if (success) {
                                resultListener.onSuccess("");
                            } else {
                                resultListener.onFail(1, errorMsg);
                            }
                        }
                    }

                    @Override
                    public void onFailureMainThread(Call call, IOException e) {
                        if (null != resultListener) {
                            resultListener.onFail(0, e.getMessage());
                        }
                    }
                });
        Ok3Util.getInstance().addToRequestQueueAsynchoronous(false, jsonRequest);
    }

    /**
     * 三方登录检查是否绑定手机号
     */
    public void requestThirdLoginCheckBinding(String _interface, int type, @NonNull String openId, final String nick,
                                              final String imgUrl, final OnRequestResult<String> resultListener) {
        HashMap<String, String> map = new HashMap<>();
        map.put("loginResouce", type + "");
        map.put("openId", openId);
        map.put("nickname", nick);
        map.put("headPicUrl", imgUrl);
        StringRequest jsonRequest = StringRequest.getBuilder(true).url(InterfaceConfig.BASE_SERVER_URL + _interface)
                .post(map)
                .build(new HandlerCallback() {
                    @Override
                    public void onResponseMainThread(Call call, String result) throws IOException {
                        if (null != resultListener) {
                            boolean success = ParseResponseUtil.getInstance().isSuccess(result);
                            String errorMsg = ParseResponseUtil.getInstance().parseReturnStringError(result);
                            String mobile = ParseResponseUtil.getInstance().parseReturnString(result, "mobile");
                            String userKey = ParseResponseUtil.getInstance().parseReturnString(result, "userKey");
                            String nickName = ParseResponseUtil.getInstance().parseReturnString(result, "nickName");
                            String headPicUrl = ParseResponseUtil.getInstance().parseReturnString(result, "headPicUrl");
                            if (success) {
                                resultListener.onSuccess(mobile, userKey, nickName, headPicUrl);
                            } else {
                                resultListener.onFail(1, errorMsg);
                            }
                        }
                    }

                    @Override
                    public void onFailureMainThread(Call call, IOException e) {
                        if (null != resultListener) {
                            resultListener.onFail(0, e.getMessage());
                        }
                    }
                });
        Ok3Util.getInstance().addToRequestQueueAsynchoronous(false, jsonRequest);
    }

    /**
     * 三方登录请求绑定手机号
     */
    public void requestThirdLoginBindPhone(String _interface, int type, final String openId, @NonNull String phone,
                                           final String mobileValidVoucher, final String moblieVerifyCode, final
                                           String password, final OnRequestResult<String> resultListener) {
        HashMap<String, String> map = new HashMap<>();
        map.put("loginResouce", type + "");
        map.put("mobile", phone);
        map.put("mobileValidVoucher", mobileValidVoucher);
        map.put("moblieVerifyCode", moblieVerifyCode);
        map.put("password", password);
        map.put("openId", openId);
        StringRequest jsonRequest = StringRequest.getBuilder(true).url(InterfaceConfig.BASE_SERVER_URL + _interface)
                .post(map)
                .build(new HandlerCallback() {
                    @Override
                    public void onResponseMainThread(Call call, String result) throws IOException {
                        if (null != resultListener) {
                            boolean success = ParseResponseUtil.getInstance().isSuccess(result);
                            String errorMsg = ParseResponseUtil.getInstance().parseReturnStringError(result);
                            String mobile = ParseResponseUtil.getInstance().parseReturnString(result, "mobile");
                            String token = ParseResponseUtil.getInstance().parseReturnString(result, "userKey");
                            String nickName = ParseResponseUtil.getInstance().parseReturnString(result, "nickName");
                            String headPicUrl = ParseResponseUtil.getInstance().parseReturnString(result, "headPicUrl");
                            if (success && !TextUtils.isEmpty(mobile)) {
                                resultListener.onSuccess("true", token, nickName, headPicUrl);
                            } else {
                                resultListener.onFail(1, errorMsg);
                            }
                        }
                    }

                    @Override
                    public void onFailureMainThread(Call call, IOException e) {
                        if (null != resultListener) {
                            resultListener.onFail(0, e.getMessage());
                        }
                    }
                });
        Ok3Util.getInstance().addToRequestQueueAsynchoronous(false, jsonRequest);
    }

    /**
     * app--版本更新检测
     */
    public void requestVersionUpdate(String checkUpdateUrl, final OnRequestResult<String> resultListener) {
        try {
            final StringRequest jsonRequest = StringRequest.getBuilder(false).url(checkUpdateUrl).get()
                    .build(new HandlerCallback() {
                        @Override
                        public void onResponseMainThread(Call call, String result) throws IOException {
                            if (null != resultListener) {
                                if (!TextUtils.isEmpty(result)) {
                                    Y.y("更新：" + result);
//                                    result= URLEncoder.encode(result,"gb2312");

//                                    buffer.append(new String(bytes,0,n,"UTF-8"));
                                    Y.y("更新：" + result.getBytes("UTF-8"));
                                    Y.y("更新：" + result.getBytes("GBK"));
                                    Y.y("更新：" + result.getBytes("ISO8859-1"));
//                                    boolean success = ParseResponseUtil.getProxyApplication().isSuccess(baseActivity, result);
                                    boolean success = JSONParseUtil.getInstance().optBoolean(result, "success");
                                    String errorMsg = ParseResponseUtil.getInstance().parseReturnStringError(result);
                                    int versionCode = ParseResponseUtil.getInstance().parseReturnInt(result,
                                            "versionCode");
                                    String versionName = ParseResponseUtil.getInstance().parseReturnString(result,
                                            "versionName");
                                    String downLoadUrl = ParseResponseUtil.getInstance().parseReturnString(result,
                                            "downLoadUrl");
                                    JSONArray updateMsgs = JSONParseUtil.getInstance().optJSONArray(JSONParseUtil
                                            .getInstance().optJSONObject(result, "data"), "updateMsgs");
                                    StringBuilder stringBuilder = new StringBuilder();
                                    if (updateMsgs != null && updateMsgs.length() > 0) {
                                        for (int i = 0; i < updateMsgs.length(); i++) {
                                            String item = updateMsgs.optString(i);
//                                            item="测试";
                                            stringBuilder.append((i + 1)).append(".").append(item.trim()).append("\n");
                                        }
                                    }
                                    if (success) {
                                        resultListener.onSuccess(versionCode + "", versionName, stringBuilder
                                                .toString(), downLoadUrl);
                                    } else {
                                        resultListener.onFail(1, errorMsg);
                                    }
                                } else {
                                    resultListener.onFail(1, "");
                                }
                            }
                        }

                        @Override
                        public void onFailureMainThread(Call call, IOException e) {
                            if (null != resultListener) {
                                resultListener.onFail(0, e.getMessage());
                            }
                        }
                    });
            Ok3Util.getInstance().addToRequestQueueAsynchoronous(false, jsonRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 一般的返回
     */
    public interface OnRequestResult<T> {
        void onSuccess(T... t);

        void onFail(int code, String msg);
    }

    /**
     * 返回列表
     *
     * @param <T>
     */
    public interface OnRequestListResult<T> {
        void onSuccess(List<T> list, String... msg);

        void onFail(int code, String msg);
    }
}
