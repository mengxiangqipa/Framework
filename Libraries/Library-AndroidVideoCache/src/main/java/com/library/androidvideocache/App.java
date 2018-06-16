//package com.library.androidvideocache;
//
//import android.app.Application;
//import android.content.Context;
//
//import com.danikula.videocache.HttpProxyCacheServer;
//
///**
// * @author Alexey Danilov (danikula@gmail.com).
// */
//public class App extends Application {
//
//    private HttpProxyCacheServer proxy;
//
//    public static HttpProxyCacheServer getProxy(Context context) {
//        App app = (App) context.getApplicationContext();
//        return app.proxy == null ? (app.proxy = app.newProxy()) : app.proxy;
//    }
//
//    private HttpProxyCacheServer newProxy() {
//        return new HttpProxyCacheServer.Builder(this)
//                .cacheDirectory(Utils.getVideoCacheDir(this))
//                .build();
//    }
//    //VideoFragment.build("http://112.253.22.157/17/z/z/y/u/zzyuasjwufnqerzvyxgkuigrkcatxr/hc.yinyuetai
// .com/D046015255134077DDB3ACA0D7E68D45.flv")
//}
