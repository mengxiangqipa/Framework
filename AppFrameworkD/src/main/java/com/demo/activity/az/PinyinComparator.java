package com.demo.activity.az;

import java.util.Comparator;

/**
 * 用来对ListView中的数据根据A-Z进行排序，前面两个if判断主要是将不是以汉字开头的数据放在后面
 */
public class PinyinComparator implements Comparator<ContactSortModel> {

    public int compare(ContactSortModel o1, ContactSortModel o2) {
        //这里主要是用来对ListView里面的数据根据ABCDEFG...来排序,修改#显示在上面
        try {
            if (o1.getSortLetters().equals("#")
                    || o2.getSortLetters().equals("@")) {
                return -1;
            } else if (o1.getSortLetters().equals("@")
                    || o2.getSortLetters().equals("#")) {
                return 1;
            } else {
                return o1.getSortLetters().compareTo(o2.getSortLetters());
            }
        } catch (Exception e) {
            return -1;
        }
    }
}
