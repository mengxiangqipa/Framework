package com.framework.utils;

import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Yangjie
 *         className JsonParseUtils
 *         created at  2016/9/3  12:00
 *         json解析类
 */
public class JSONParseUtil {
    private static volatile JSONParseUtil instance;

    public JSONParseUtil() {
    }

    public static JSONParseUtil getInstance() {
        if (null == instance) {
            synchronized (JSONParseUtil.class) {
                if (null == instance) {
                    instance = new JSONParseUtil();
                }
            }
        }
        return instance;
    }

    public String optString(String response, String key) {
        return optString(response, key, "");
    }

    public String optString(String response, String key, String defaultValue) {
        try {
            JSONObject object = new JSONObject(response);
            return object.optString(key, defaultValue);
        } catch (JSONException e) {
            e.printStackTrace();
            return defaultValue;
        }
    }

    public String optString(JSONObject jsonObject, String key) {
        return optString(jsonObject, key, "");
    }

    public String optString(JSONObject jsonObject, String key, String defaultValue) {
        try {
            String optString = jsonObject.optString(key, defaultValue);
            if (TextUtils.isEmpty(optString)) {
                return defaultValue;
            }
            return optString;
        } catch (Exception e) {
            e.printStackTrace();
            return defaultValue;
        }
    }

    public boolean optBoolean(String response, String key) {
        try {
            JSONObject object = new JSONObject(response);
            return object.optBoolean(key, false);
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean optBoolean(JSONObject jsonObject, String key) {
        try {
            return jsonObject.optBoolean(key, false);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public int optInt(String response, String key) {
        try {
            JSONObject object = new JSONObject(response);
            return object.optInt(key, 0);
        } catch (Exception e) {
            e.printStackTrace();
            return Integer.MAX_VALUE;
        }
    }

    public int optInt(JSONObject jsonObject, String key) {
        try {
            return jsonObject.optInt(key, 0);
        } catch (Exception e) {
            e.printStackTrace();
            return Integer.MAX_VALUE;
        }
    }

    public long optLong(String response, String key) {
        try {
            JSONObject object = new JSONObject(response);
            return object.optLong(key, 0);
        } catch (Exception e) {
            e.printStackTrace();
            return Long.MAX_VALUE;
        }
    }

    public long optLong(JSONObject jsonObject, String key) {
        try {
            return jsonObject.optLong(key, 0);
        } catch (Exception e) {
            e.printStackTrace();
            return Long.MAX_VALUE;
        }
    }

    public float optFloat(String response, String key) {
        try {
            JSONObject object = new JSONObject(response);
            String optString = object.optString(key, "");
            return Float.parseFloat(optString);
        } catch (Exception e) {
            e.printStackTrace();
            return Float.MAX_VALUE;
        }
    }

    public float optFloat(JSONObject jsonObject, String key) {
        try {
            String optString = jsonObject.optString(key, "");
            return Float.parseFloat(optString);
        } catch (Exception e) {
            e.printStackTrace();
            return Float.MAX_VALUE;
        }
    }

    public double optDouble(String response, String key) {
        try {
            JSONObject object = new JSONObject(response);
            String optString = object.optString(key, "");
            return Double.parseDouble(optString);
        } catch (Exception e) {
            e.printStackTrace();
            return Double.MAX_VALUE;
        }
    }

    public double optDouble(JSONObject jsonObject, String key) {
        try {
            String optString = jsonObject.optString(key, "");
            return Double.parseDouble(optString);
        } catch (Exception e) {
            e.printStackTrace();
            return Double.MAX_VALUE;
        }
    }

    public JSONObject optJSONObject(String response, String key) {
        try {
            JSONObject object = new JSONObject(response);
            return object.optJSONObject(key);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public JSONObject optJSONObject(JSONObject jsonObject, String key) {
        try {
            return jsonObject.optJSONObject(key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public JSONArray optJSONArray(String response, String key) {
        try {
            JSONObject object = new JSONObject(response);
            return object.optJSONArray(key);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public JSONArray optJSONArray(JSONObject jsonObject, String key) {
        try {
            return jsonObject.optJSONArray(key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
