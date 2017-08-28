package com.framework.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.framework.Application.FrameApplication;
import com.framework.Configs.Configs;

/**
 * @author Yobert Jomi
 *         className PreferenceHelper
 *         created at  2016/10/15  16:34
 */
public class PreferencesHelper {
    private static volatile SharedPreferences mSharedPreferences;
    private static volatile PreferencesHelper mPreferencesHelper;
    private static volatile int mode = Context.MODE_PRIVATE;

    private PreferencesHelper() {

    }

    public static PreferencesHelper getInstance() {
        if (mPreferencesHelper == null) {
            synchronized (PreferencesHelper.class) {
                if (mPreferencesHelper == null) {
                    mPreferencesHelper = new PreferencesHelper();
                    mSharedPreferences = FrameApplication.frameApplication.getSharedPreferences(Configs.SHAREPREFERENCES_NAME, mode);
                }
            }
        }
        return mPreferencesHelper;
    }

    public void putInfo(String name, String data) {
        if (mPreferencesHelper == null) {
            mPreferencesHelper = new PreferencesHelper();
            mSharedPreferences = FrameApplication.frameApplication.getSharedPreferences(Configs.SHAREPREFERENCES_NAME, mode);
        }
        SharedPreferences.Editor e = mSharedPreferences.edit().putString(name, data);
        e.apply();
    }

    public void putInfo(String name, int data) {
        if (mPreferencesHelper == null) {
            mPreferencesHelper = new PreferencesHelper();
            mSharedPreferences = FrameApplication.frameApplication.getSharedPreferences(Configs.SHAREPREFERENCES_NAME, mode);
        }
        SharedPreferences.Editor e = mSharedPreferences.edit().putInt(name, data);
        e.apply();
    }

    public void putInfo(String name, boolean data) {
        if (mPreferencesHelper == null) {
            mPreferencesHelper = new PreferencesHelper();
            mSharedPreferences = FrameApplication.frameApplication.getSharedPreferences(Configs.SHAREPREFERENCES_NAME, mode);
        }
        SharedPreferences.Editor e = mSharedPreferences.edit().putBoolean(name, data);
        e.apply();
    }

    public int getIntData(String name) {
        if (mPreferencesHelper == null) {
            mPreferencesHelper = new PreferencesHelper();
            mSharedPreferences = FrameApplication.frameApplication.getSharedPreferences(Configs.SHAREPREFERENCES_NAME, mode);
        }
        return mSharedPreferences.getInt(name, 0);
    }

    public String getStringData(String name) {
        if (mPreferencesHelper == null) {
            mPreferencesHelper = new PreferencesHelper();
            mSharedPreferences = FrameApplication.frameApplication.getSharedPreferences(Configs.SHAREPREFERENCES_NAME, mode);
        }
        return mSharedPreferences.getString(name, "");
    }

    public boolean getBooleanData(String name) {
        if (mPreferencesHelper == null) {
            mPreferencesHelper = new PreferencesHelper();
            mSharedPreferences = FrameApplication.frameApplication.getSharedPreferences(Configs.SHAREPREFERENCES_NAME, mode);
        }
        return mSharedPreferences.getBoolean(name, false);
    }

    public void putInfo(String name, long data) {
        if (mPreferencesHelper == null) {
            mPreferencesHelper = new PreferencesHelper();
            mSharedPreferences = FrameApplication.frameApplication.getSharedPreferences(Configs.SHAREPREFERENCES_NAME, mode);
        }
        SharedPreferences.Editor e = mSharedPreferences.edit().putLong(name, data);
        e.apply();
    }

    public long getLongData(String name) {
        if (mPreferencesHelper == null) {
            mPreferencesHelper = new PreferencesHelper();
            mSharedPreferences = FrameApplication.frameApplication.getSharedPreferences(Configs.SHAREPREFERENCES_NAME, mode);
        }
        return mSharedPreferences.getLong(name, 0);
    }

    public void remove(String key) {
        if (mPreferencesHelper == null) {
            mPreferencesHelper = new PreferencesHelper();
            mSharedPreferences = FrameApplication.frameApplication.getSharedPreferences(Configs.SHAREPREFERENCES_NAME, mode);
        }
        try {
            mSharedPreferences.edit().remove(key).apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
