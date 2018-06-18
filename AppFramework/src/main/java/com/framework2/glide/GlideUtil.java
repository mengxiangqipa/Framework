package com.framework2.glide;
/**
 *     @author YobertJomi
 *     className GlideUtil
 *     created at  2017/2/22  15:29
 */
public class GlideUtil {

    private static volatile GlideUtil singleton;

    private GlideUtil() {
    }

    public static GlideUtil getInstance() {
        if (singleton == null) {
            synchronized (GlideUtil.class) {
                if (singleton == null) {
                    singleton = new GlideUtil();
                }
            }
        }
        return singleton;
    }
}