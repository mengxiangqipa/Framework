//package com.framework2.okhttp3_bak;
//
//import android.content.Context;
//import android.text.TextUtils;
//
//import com.demo.configs.ConstantsME;
//import com.framework.utils.PreferencesHelper;
//import com.framework2.utils.CookieManagerUtil;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//
//import okhttp3.Cookie;
//import okhttp3.CookieJar;
//import okhttp3.HttpUrl;
//
///**
// * CookieJar
// *
// * @author YobertJomi
// *         className CustomCookieJar
// *         created at  2017/6/19  12:03
// */
//
//public class AllCookieJar implements CookieJar {
//    private final HashMap<HttpUrl, List<Cookie>> cookieStore = new HashMap<>();
//    private Context context;
//
//    public AllCookieJar(Context context) {
//        this.context = context;
//    }
//
//    @Override
//    public void saveFromResponse(HttpUrl httpUrl, List<Cookie> list) {
//        if (list != null) {
//            cookieStore.put(httpUrl, list);
//        }
//    }
//
//    @Override
//    public List<Cookie> loadForRequest(HttpUrl httpUrl) {
//        String decodeCookie = CookieManagerUtil.getInstance().decodeCookie(context, PreferencesHelper.getInstance().getStringData(ConstantsME.cookies));
//        if (!TextUtils.isEmpty(decodeCookie)) {
//            List<Cookie> cookies = new ArrayList<>();
//            String[] split = decodeCookie.split(";");
//
//            for (int i = 0; i < split.length; i++) {
//                String currentCookie = split[i];
//                int dex = currentCookie.indexOf("=");
//                if (dex > 0) {
//                    Cookie.Builder builder = new Cookie.Builder();
//                    builder.name(currentCookie.substring(0, dex));
//                    builder.value(currentCookie.substring(dex + 1, currentCookie.length()));
//                    builder.domain(httpUrl.host());
//                    Cookie build = builder.build();
//                    cookies.add(build);
//                }
//            }
//            return cookies;
//        } else
//            return new ArrayList<>();
//    }
//}
