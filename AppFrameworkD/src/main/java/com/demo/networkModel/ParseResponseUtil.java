package com.demo.networkModel;

import android.text.TextUtils;

import com.framework.utils.JSONParseUtil;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * 返回的response---json解析类
 *
 * @author Yangjie
 * className ParseJsonUtil
 * created at  2017/4/1  9:01
 */
public class ParseResponseUtil {
    private static volatile ParseResponseUtil singleton;

    private ParseResponseUtil() {
    }

    public static ParseResponseUtil getInstance() {
        if (singleton == null) {
            synchronized (ParseResponseUtil.class) {
                if (singleton == null) {
                    singleton = new ParseResponseUtil();
                }
            }
        }
        return singleton;
    }

    public boolean isSuccess(String response) {
        return JSONParseUtil.getInstance().optBoolean(response, "success");
    }

//    public boolean isSuccess(final BaseActivity baseActivity, String response) {
//        if (JSONParseUtil.getInstance().optBoolean(response, "success")) {
//            return true;
//        } else if (response.contains("获取用户信息失败") || response.contains("重新登录") || response.contains("未登录") ||
// response.contains("未登陆") || response.contains("登录已失效")) {
//            if (null != baseActivity) {
//                CustomProgressDialogUtils.dismissProcessDialog();
//                final ReloginDialog dialog = new ReloginDialog(baseActivity);
//                dialog.setOnSureClickListener(new ReloginDialog.OnSureClickListener() {
//                    @Override
//                    public void onSureClickListener(String info) {
//                        dialog.dismiss();
//                        baseActivity.reLogin();
//                    }
//                }).showDialog();
//            }
//            return false;
//        }
//        return false;
//    }

    /**
     * 解析返回一个JSONObject
     */
    public JSONObject parseReturnJSONObject(String result) {
        if (!TextUtils.isEmpty(result)) {
            return JSONParseUtil.getInstance().optJSONObject(result, "data");
        }
        return null;
    }

    /**
     * 解析返回一个JSONArray
     */
    public JSONArray parseReturnJSONArray(String result) {
        if (!TextUtils.isEmpty(result)) {
            return JSONParseUtil.getInstance().optJSONArray(result, "data");
        }
        return null;
    }

    /**
     * 解析data返回一个string
     */
    public String parseReturnString(String result, String key) {
        if (!TextUtils.isEmpty(result)) {
            return JSONParseUtil.getInstance().optString(JSONParseUtil.getInstance().optJSONObject(result, "data"),
                    key);
        }
        return null;
    }

    /**
     * 解析data返回一个int
     */
    public int parseReturnInt(String result, String key) {
        if (!TextUtils.isEmpty(result)) {
            return JSONParseUtil.getInstance().optInt(JSONParseUtil.getInstance().optJSONObject(result, "data"), key);
        }
        return 0;
    }

    public String parseReturnStringError(String result) {
        if (!TextUtils.isEmpty(result)) {
            return JSONParseUtil.getInstance().optString(result, "data");
        }
        return null;
    }

    /**
     * 解析验证码
     */
    public String parseVerifyCode(String result) {
        if (!TextUtils.isEmpty(result)) {
            if (isSuccess(result)) {
                return JSONParseUtil.getInstance().optString(JSONParseUtil.getInstance().optJSONObject(result,
                        "data"), "key");
            }
        }
        return null;
    }
}
