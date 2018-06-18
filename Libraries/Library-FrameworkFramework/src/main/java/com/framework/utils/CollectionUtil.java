package com.framework.utils;

import java.util.Collection;

/**
 * 判断List是否为空
 *
 * @author YobertJomi
 * className CollectionUtil
 * created at  2017/3/17  13:01
 */
public class CollectionUtil {
    private static volatile CollectionUtil singleton;

    private CollectionUtil() {
    }

    public static CollectionUtil getInstance() {
        if (singleton == null) {
            synchronized (CollectionUtil.class) {
                if (singleton == null) {
                    singleton = new CollectionUtil();
                }
            }
        }
        return singleton;
    }

    public boolean isEmpty(Collection<?> list) {
        return list == null || list.size() <= 0;
    }
}
