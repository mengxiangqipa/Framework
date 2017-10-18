package com.framework.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * List 倒序输出
 *
 * @author Yangjie
 *         className ListReverseOrderUtil
 *         created at  2017/3/17  13:01
 */
public class ListReverseOrderUtil {
    private static volatile ListReverseOrderUtil singleton;

    private ListReverseOrderUtil() {
    }

    public static ListReverseOrderUtil getInstance() {
        if (singleton == null) {
            synchronized (ListReverseOrderUtil.class) {
                if (singleton == null) {
                    singleton = new ListReverseOrderUtil();
                }
            }
        }
        return singleton;
    }

    @SuppressWarnings("unchecked")
    public List reverseOrder(List list) {
        if (list == null || list.size() <= 0) {
            return null;
        } else {
            List list1 = new ArrayList();
            int lenth = list.size();
            for (int i = 0; i < lenth; i++) {
                list1.add(list.get(lenth - 1-i));
            }
            return list1;
        }
    }
}
