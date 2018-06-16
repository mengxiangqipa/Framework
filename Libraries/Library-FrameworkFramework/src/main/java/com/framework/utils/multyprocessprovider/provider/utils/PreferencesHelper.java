package com.framework.utils.multyprocessprovider.provider.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.framework.utils.multyprocessprovider.provider.PreferenceItem;
import com.framework.utils.multyprocessprovider.provider.preferences.PreferencesContentValues;
import com.framework.utils.multyprocessprovider.provider.preferences.PreferencesCursor;
import com.framework.utils.multyprocessprovider.provider.preferences.PreferencesSelection;

import java.util.ArrayList;
import java.util.List;

public class PreferencesHelper {

    private static final String TAG = PreferencesHelper.class.getSimpleName();

    private final Context context;
    private final ContentResolver contentResolver;

    public PreferencesHelper(Context context) {
        this.context = context.getApplicationContext();
        this.contentResolver = this.context.getContentResolver();
    }

    private static void closeQuietly(Cursor cursor) {
        if (cursor != null) {
            cursor.close();
        }
    }

    public void insert(@NonNull final String moduleName, @NonNull final String key, @Nullable Object value) {
        insert(moduleName, key, String.valueOf(value));
    }

    public void insert(@NonNull final String moduleName, @NonNull final String key, @Nullable String value) {
        LogUtils.i(TAG, "insert: module " + moduleName + ", " + key + " = " + value);
        PreferencesContentValues contentValues = new PreferencesContentValues();
        contentValues.putModule(moduleName);
        contentValues.putKey(key);
        contentValues.putValue(value);
        contentValues.insert(contentResolver);
    }

    public String query(@NonNull final String moduleName, @NonNull final String key) {
        LogUtils.i(TAG, "query start: module " + moduleName + ", " + key);
        String value = null;
        PreferencesCursor cursor = new PreferencesSelection().module(moduleName).and().key(key).query(contentResolver);
        if (cursor != null && cursor.moveToFirst()) {
            value = cursor.getValue();
        }
        closeQuietly(cursor);
        LogUtils.i(TAG, "query end: module " + moduleName + ", " + key + " = " + value);
        return value;
    }

    public int remove(@NonNull final String moduleName, @NonNull final String key) {
        return new PreferencesSelection().module(moduleName).and().key(key).delete(contentResolver);
    }

    public int clear(@NonNull final String moduleName) {
        return new PreferencesSelection().module(moduleName).delete(contentResolver);
    }

    public List<PreferenceItem> getAll(@NonNull final String moduleName) {
        PreferencesCursor cursor = new PreferencesSelection().module(moduleName).query(contentResolver);
        final ArrayList<PreferenceItem> list = new ArrayList<>();
        if (cursor != null) {
            for (boolean hasItem = cursor.moveToFirst(); hasItem; hasItem = cursor.moveToNext()) {
                list.add(new PreferenceItem(cursor));
            }
        }
        closeQuietly(cursor);
        return list;
    }
    //以下自己添加

    /**
     * 查询PreferencesColumns.KEY  小于 value
     *
     * @param moduleName
     * @return
     */
    public List<PreferenceItem> queryLessThan(@NonNull final String moduleName, Object value) {
        PreferencesCursor cursor = new PreferencesSelection().module(moduleName).lessThan(value).query(contentResolver);
        final ArrayList<PreferenceItem> list = new ArrayList<>();
        if (cursor != null) {
            for (boolean hasItem = cursor.moveToFirst(); hasItem; hasItem = cursor.moveToNext()) {
                list.add(new PreferenceItem(cursor));
            }
        }
        closeQuietly(cursor);
        return list;
    }

    /**
     * 查询PreferencesColumns.KEY  大于 value
     *
     * @param moduleName
     * @return
     */
    public List<PreferenceItem> queryGreaterThan(@NonNull final String moduleName, Object value) {
        PreferencesCursor cursor = new PreferencesSelection().module(moduleName).greaterThan(value).query
                (contentResolver);
        final ArrayList<PreferenceItem> list = new ArrayList<>();
        if (cursor != null) {
            for (boolean hasItem = cursor.moveToFirst(); hasItem; hasItem = cursor.moveToNext()) {
                list.add(new PreferenceItem(cursor));
            }
        }
        closeQuietly(cursor);
        return list;
    }

    /**
     * 查询PreferencesColumns.KEY  大于等于 value
     *
     * @param moduleName
     * @return
     */
    public List<PreferenceItem> queryGreaterThanOrEquals(@NonNull final String moduleName, Object value) {
        PreferencesCursor cursor = new PreferencesSelection().module(moduleName).greaterThanOrEquals(value).query
                (contentResolver);
        final ArrayList<PreferenceItem> list = new ArrayList<>();
        if (cursor != null) {
            for (boolean hasItem = cursor.moveToFirst(); hasItem; hasItem = cursor.moveToNext()) {
                list.add(new PreferenceItem(cursor));
            }
        }
        closeQuietly(cursor);
        return list;
    }

    /**
     * 查询PreferencesColumns.KEY  小于等于 value
     *
     * @param moduleName
     * @return
     */
    public List<PreferenceItem> queryLessThanOrEquals(@NonNull final String moduleName, Object value) {
        PreferencesCursor cursor = new PreferencesSelection().module(moduleName).lessThanOrEquals(value).query
                (contentResolver);
        final ArrayList<PreferenceItem> list = new ArrayList<>();
        if (cursor != null) {
            for (boolean hasItem = cursor.moveToFirst(); hasItem; hasItem = cursor.moveToNext()) {
                list.add(new PreferenceItem(cursor));
            }
        }
        closeQuietly(cursor);
        return list;
    }
}
