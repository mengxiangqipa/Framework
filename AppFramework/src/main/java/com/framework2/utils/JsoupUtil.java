package com.framework2.utils;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.framework.util.CollectionUtil;
import com.framework.util.ThreadPoolUtil;
import com.framework.util.ToastUtil;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.List;

import okhttp3.Cookie;

/**
 * jsoup网页html抓包 app：compile 'org.jsoup:jsoup:1.10.3'
 * http://www.jianshu.com/p/a620a2664f58
 *
 * @author YobertJomi
 * className JsoupUtil
 * created at  2017/6/19  17:51
 */
public class JsoupUtil {
    private static volatile JsoupUtil singleton;

    private JsoupUtil() {
    }

    public static JsoupUtil getInstance() {
        if (singleton == null) {
            synchronized (JsoupUtil.class) {
                if (singleton == null) {
                    singleton = new JsoupUtil();
                }
            }
        }
        return singleton;
    }

    public void connectGet(@NonNull final String sourceUrl, final List<Cookie> cookieList) {
        ThreadPoolUtil.getInstance().getInstanceLimitedTaskExecutor(2).submit(new Runnable() {
            @Override
            public void run() {
                try {
                    //还是一样先从一个URL加载一个Document对象。
                    Document doc;
                    Connection connection;
                    if (!TextUtils.isEmpty(sourceUrl)) {
                        connection = Jsoup.connect(sourceUrl);
                    } else {
                        connection = Jsoup.connect("http://home.meishichina.com/show-top-type-recipe.html");
                    }
                    if (!CollectionUtil.getInstance().isEmpty(cookieList)) {
                        for (int i = 0; i < cookieList.size(); i++) {
                            Cookie cookie = cookieList.get(i);
                            if (null != cookie) {
                                connection.cookie(cookie.name(), cookie.value());
                            }
                        }
                    }
                    doc = connection.get();
                    if (null != doc) {

                        //“椒麻鸡”和它对应的图片都在<div class="pic">中
                        Elements titleAndPic = doc.select("div.pic");
                        //使用Element.select(String selector)查找元素，使用Node.attr(String key)方法取得一个属性的值
                        Log.e("yy", "title:" + titleAndPic.get(1).select("a").attr("title") + "pic:" + titleAndPic
                                .get(1).select("a").select("img").attr("data-src"));
                        ToastUtil.getInstance().showToast("title:" + titleAndPic.get(1).select("a").attr("title") +
                                "pic:" + titleAndPic.get(1).select("a").select("img").attr("data-src"));
                        //所需链接在<div class="detail">中的<a>标签里面
                        Elements url = doc.select("div.detail").select("a");
                        Log.e("yy", "url:" + url.get(1).attr("href"));

                        //原料在<p class="subcontent">中
                        Elements burden = doc.select("p.subcontent");
                        //对于一个元素中的文本，可以使用Element.text()方法
                        Log.e("yy", "burden:" + burden.get(1).text());
                        ToastUtil.getInstance().showToast("burden:" + burden.get(1).text());
                    }
                } catch (Exception e) {
                    ToastUtil.getInstance().showToast("Exception:" + e.toString());
                    Log.e("yy", e.toString());
                }
            }
        });
    }
}
