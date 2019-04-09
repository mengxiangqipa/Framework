package com.framework.utils;

import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author YobertJomi
 * className JsonParseUtils
 * created at  2016/9/3  12:00
 * json解析类
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
            return defaultValue;
        }
    }

    public boolean optBoolean(String response, String key) {
        return optBoolean(response, key, false);
    }

    public boolean optBoolean(String response, String key, boolean defaultValue) {
        try {
            JSONObject object = new JSONObject(response);
            return object.optBoolean(key, defaultValue);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public boolean optBoolean(JSONObject jsonObject, String key) {
        return optBoolean(jsonObject, key, false);
    }

    public boolean optBoolean(JSONObject jsonObject, String key, boolean defaultValue) {
        try {
            return jsonObject.optBoolean(key, defaultValue);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public int optInt(String response, String key) {
        return optInt(response, key, 0);
    }

    public int optInt(String response, String key, int defaultValue) {
        try {
            JSONObject object = new JSONObject(response);
            return object.optInt(key, defaultValue);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public int optInt(JSONObject jsonObject, String key) {
        return optInt(jsonObject, key, 0);
    }

    public int optInt(JSONObject jsonObject, String key, int defaultValue) {
        try {
            return jsonObject.optInt(key, defaultValue);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public long optLong(String response, String key) {
        return optLong(response, key, 0L);
    }

    public long optLong(String response, String key, long defaultValue) {
        try {
            JSONObject object = new JSONObject(response);
            return object.optLong(key, defaultValue);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public long optLong(JSONObject jsonObject, String key) {
        return optLong(jsonObject, key, 0L);
    }

    public long optLong(JSONObject jsonObject, String key, long defaultValue) {
        try {
            return jsonObject.optLong(key, defaultValue);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public float optFloat(String response, String key) {
        return optFloat(response, key, 0F);
    }

    public float optFloat(String response, String key, float defaultValue) {
        try {
            JSONObject object = new JSONObject(response);
            String optString = object.optString(key, "");
            return Float.parseFloat(optString);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public float optFloat(JSONObject jsonObject, String key) {
        return optFloat(jsonObject, key, 0F);
    }

    public float optFloat(JSONObject jsonObject, String key, float defaultValue) {
        try {
            String optString = jsonObject.optString(key, "");
            return Float.parseFloat(optString);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public double optDouble(String response, String key) {
        return optDouble(response, key, 0D);
    }

    public double optDouble(String response, String key, double defaultValue) {
        try {
            JSONObject object = new JSONObject(response);
            String optString = object.optString(key, "");
            return Double.parseDouble(optString);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public double optDouble(JSONObject jsonObject, String key) {
        return optDouble(jsonObject, key, 0D);
    }

    public double optDouble(JSONObject jsonObject, String key, double defaultValue) {
        try {
            String optString = jsonObject.optString(key, "");
            return Double.parseDouble(optString);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public JSONObject optJSONObject(String response, String key) {
        try {
            JSONObject object = new JSONObject(response);
            return object.optJSONObject(key);
        } catch (JSONException e) {
            return null;
        }
    }

    public JSONObject optJSONObject(JSONObject jsonObject, String key) {
        try {
            return jsonObject.optJSONObject(key);
        } catch (Exception e) {
            return null;
        }
    }

    public JSONArray optJSONArray(String response, String key) {
        try {
            JSONObject object = new JSONObject(response);
            return object.optJSONArray(key);
        } catch (JSONException e) {
            return null;
        }
    }

    public JSONArray optJSONArray(JSONObject jsonObject, String key) {
        try {
            return jsonObject.optJSONArray(key);
        } catch (Exception e) {
            return null;
        }
    }
}
