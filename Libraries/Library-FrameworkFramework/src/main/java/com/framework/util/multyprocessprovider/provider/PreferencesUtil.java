package com.framework.util.multyprocessprovider.provider;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.framework.application.ProxyApplication;
import com.framework.util.multyprocessprovider.provider.exception.WrongTypeException;

import java.util.List;

public class PreferencesUtil {
    private static volatile PreferencesUtil preferencesUtil;
    private static volatile String moduleName = "PreferencesUtil";
    private PreferencesHelper preferencesHelper;

    private PreferencesUtil() {
    }

    private PreferencesUtil(Context context, String moduleName) {
        PreferencesUtil.moduleName = moduleName;
        preferencesHelper = new PreferencesHelper(context);
    }

    public static PreferencesUtil getInstance() {
        return getInstance(moduleName);
    }

    public static PreferencesUtil getInstance(String moduleName) {
        if (preferencesUtil == null) {
            synchronized (PreferencesHelper.class) {
                if (preferencesUtil == null) {
                    preferencesUtil = new PreferencesUtil(ProxyApplication.getProxyApplication().getApplicationContext(),
                            moduleName);
                }
            }
        }
        return preferencesUtil;
    }

    public void putString(@NonNull final String key, final String value) {
        try {
            preferencesHelper.insert(moduleName, key, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void putInt(@NonNull final String key, final int value) {
        try {
            preferencesHelper.insert(moduleName, key, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void putFloat(@NonNull final String key, final float value) {
        try {
            preferencesHelper.insert(moduleName, key, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void putLong(@NonNull final String key, final long value) {
        try {
            preferencesHelper.insert(moduleName, key, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void putBoolen(@NonNull final String key, final boolean value) {
        try {
            preferencesHelper.insert(moduleName, key, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean getBoolean(@NonNull final String key, final boolean defaultValue) {
        try {
            return getBoolean(key);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public boolean getBoolean(@NonNull final String key) {
        try {
            final String value = getString(key);
            return Boolean.parseBoolean(value);
        } catch (Exception e) {
            return false;
        }
    }

    public float getFloat(@NonNull final String key, final float defaultValue) {
        try {
            return getFloat(key);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public float getFloat(@NonNull final String key) {
        final String value = getString(key);
//        throwForNullValue(value, Float.class, key);
        try {
            return Float.parseFloat(value);
        } catch (NumberFormatException e) {
//            throw new WrongTypeException(e);
            return 0f;
        }
    }

    public int getInt(@NonNull final String key, final int defaultValue) {
        try {
            return getInt(key);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public int getInt(@NonNull final String key) {
        final String value = getString(key);
//        throwForNullValue(value, Integer.class, key);
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
//            throw new WrongTypeException(e);
            return 0;
        }
    }

    public long getLong(@NonNull final String key, final long defaultValue) {
        try {
            return getLong(key);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public long getLong(@NonNull final String key) {
        final String value = getString(key);
//        throwForNullValue(value, Long.class, key);
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
//            throw new WrongTypeException(e);
            return 0;
        }
    }

    public String getString(@NonNull final String key) {
        try {
            String value = preferencesHelper.query(moduleName, key);
//            if (value == null)
//            {
//                throw new ItemNotFoundException(String.format("Value for Key <%s> not found", key));
//            }
            return value;
        } catch (Exception e) {
            return null;
        }
    }

    @Nullable
    public String getString(@NonNull final String key, final String defaultValue) {
        try {
            return getString(key);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public int remove(@NonNull final String key) {
        return preferencesHelper.remove(moduleName, key);
    }

    public int clear() {
        return preferencesHelper.clear(moduleName);
    }

    public List<PreferenceItem> getAll() {
        return preferencesHelper.getAll(moduleName);
    }

    /**
     * logs a warning that warns that the given value for the given key is null and null is only
     * supported when reading it as a String and not other java primitives
     */
    private void throwForNullValue(@Nullable final String value,
                                   final Class<?> clazz, final @NonNull String key) throws WrongTypeException {
        if (value == null) {
            throw new WrongTypeException("The value for key <" + key + "> is null. "
                    + "You obviously saved this value as String and try to access it with type "
                    + clazz.getSimpleName() + " which cannot be null. "
                    + " Always use getString(key, defaultValue) when accessing data you saved with put(String).");
        }
    }
}
