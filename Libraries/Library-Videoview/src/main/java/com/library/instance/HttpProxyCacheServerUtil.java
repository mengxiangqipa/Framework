package com.library.instance;

import android.content.Context;

import com.danikula.videocache.HttpProxyCacheServer;
import com.library.androidvideocache.Utils;

/**
 * @author YobertJomi
 * className HttpProxyCacheServerUtil
 * created at  2017/9/5  11:58
 */
public class HttpProxyCacheServerUtil {
    private static volatile HttpProxyCacheServerUtil singleton;
    private HttpProxyCacheServer proxy;

    private HttpProxyCacheServerUtil() {
    }

    public static HttpProxyCacheServerUtil getInstance() {
        if (singleton == null) {
            synchronized (HttpProxyCacheServerUtil.class) {
                if (singleton == null) {
                    singleton = new HttpProxyCacheServerUtil();
                }
            }
        }
        return singleton;
    }

    public HttpProxyCacheServer getProxy(Context context) {
//        App app = (App) context.getApplicationContext();
        return proxy == null ? newProxy(context) : proxy;
    }

    private HttpProxyCacheServer newProxy(Context context) {
        return new HttpProxyCacheServer.Builder(context.getApplicationContext())
                .cacheDirectory(Utils.getVideoCacheDir(context.getApplicationContext()))
                .build();
    }
}
