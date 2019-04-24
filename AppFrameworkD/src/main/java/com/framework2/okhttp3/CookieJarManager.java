package com.framework2.okhttp3;

import android.content.Context;

import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

/**
 * 自动管理Cookies
 *
 * @author YobertJomi
 * className CookieJarManager
 * created at  2017/6/19  13:47
 */
public class CookieJarManager implements CookieJar {
    private Context context;
    private final PersistentCookieStore cookieStore = new PersistentCookieStore(context);

    public CookieJarManager(Context context) {
        this.context = context;
    }

    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        if (cookies != null && cookies.size() > 0) {
            for (Cookie item : cookies) {
                cookieStore.add(url, item);
            }
        }
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {
        List<Cookie> cookies = cookieStore.get(url);
        return cookies;
    }
}
