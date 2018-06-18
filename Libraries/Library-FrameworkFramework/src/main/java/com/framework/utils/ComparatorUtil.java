package com.framework.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * 对象排序工具类
 *
 * @author YobertJomi
 * className ComparatorUtil
 * created at  2017/4/26  10:06
 */
public class ComparatorUtil {
    private static volatile ComparatorUtil singleton;

    private ComparatorUtil() {
    }

    public static ComparatorUtil getInstance() {
        if (singleton == null) {
            synchronized (ComparatorUtil.class) {
                if (singleton == null) {
                    singleton = new ComparatorUtil();
                }
            }
        }
        return singleton;
    }

    /**
     * 字符串属性排序方法
     *
     * @param list     集合
     * @param attrName 属性名
     * @return ArrayList<Object>
     */
    public ArrayList<Object> sortByStringAttr(ArrayList<Object> list, String attrName, SortType sortType) {
        Collections.sort(list, new ComparatorString(attrName, sortType));
        return list;
    }

    public ArrayList<Object> sortByStringAttr(ArrayList<Object> list, String attrName) {

        return sortByStringAttr(list, attrName, SortType.DESC);
    }

    public ArrayList<Object> sortByIntAttr(ArrayList<Object> list, String attrName) {
        return sortByIntAttr(list, attrName, SortType.DESC);
    }

    /**
     * 整型属性排序方法
     *
     * @param list     集合
     * @param attrName 属性名
     * @return ArrayList
     */
    public ArrayList<Object> sortByIntAttr(ArrayList<Object> list, String attrName, SortType sortType) {
        Collections.sort(list, new ComparatorInt(attrName, sortType));
        return list;
    }

    /**
     * double属性排序方法
     *
     * @param list     集合
     * @param attrName 属性名
     * @return ArrayList
     */
    public ArrayList<Object> sortByDoubleAttr(ArrayList<Object> list, String attrName, SortType sortType) {
        Collections.sort(list, new ComparatorDouble(attrName, sortType));
        return list;
    }

    public ArrayList<Object> sortByDoubleAttr(ArrayList<Object> list, String attrName) {
        return sortByDoubleAttr(list, attrName, SortType.DESC);
    }

    public enum SortType {
        DESC, ASC
    }

    /**
     * 字符串属性比较类
     */
    private class ComparatorString implements Comparator<Object> {

        private String attr;
        private SortType sortType;

        private ComparatorString(String attr, SortType sortType) {
            this.attr = attr;
            this.sortType = sortType;
        }

        @Override
        public int compare(Object o1, Object o2) {
            String attr1;
            String attr2;
            if (null != o1 && null != o2) {
                try {
                    if (o1 instanceof String && o2 instanceof String) {
                        attr1 = (String) o1;
                        attr2 = (String) o2;
                        if (sortType == SortType.DESC) {
                            //按倒序排序，从大到小
                            attr1 = (String) o2;
                            attr2 = (String) o1;
                        }
                    } else {
                        Field f1 = o1.getClass().getDeclaredField(attr);
                        Field f2 = o2.getClass().getDeclaredField(attr);
                        f1.setAccessible(true);
                        f2.setAccessible(true);
                        attr1 = (String) f1.get(o1);
                        attr2 = (String) f1.get(o2);
                        if (sortType == SortType.DESC) {
                            //按倒序排序，从大到小
                            attr1 = (String) f2.get(o2);
                            attr2 = (String) f1.get(o1);
                        }
                    }
                    if (attr1.compareTo(attr2) > 0) {
                        return 1;
                    } else if (attr1.compareTo(attr2) == 0) {
                        return 0;
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                }
            }
            return -1;
        }
    }

    /**
     * 整型属性比较类
     */
    private class ComparatorInt implements Comparator<Object> {
        private String attr;
        private SortType sortType;

        private ComparatorInt(String attr, SortType sortType) {
            this.attr = attr;
            this.sortType = sortType;
        }

        @Override
        public int compare(Object o1, Object o2) {
            if (null != o1 && null != o2) {
                int attr1;
                int attr2;
                try {
                    if (o1 instanceof Integer && o2 instanceof Integer) {
                        attr1 = (int) o1;
                        attr2 = (int) o2;
                        if (sortType == SortType.DESC) {
                            //按倒序排序，从大到小
                            attr1 = (int) o2;
                            attr2 = (int) o1;
                        }
                    } else {
                        Field f1 = o1.getClass().getDeclaredField(attr);
                        Field f2 = o2.getClass().getDeclaredField(attr);
                        f1.setAccessible(true);
                        f2.setAccessible(true);
                        attr1 = f1.getInt(o1);
                        attr2 = f2.getInt(o2);
                        if (sortType == SortType.DESC) {
                            //按倒序排序，从大到小
                            attr1 = f2.getInt(o2);
                            attr2 = f1.getInt(o1);
                        }
                    }
                    if (attr1 > attr2) {
                        return 1;
                    } else if (attr1 == attr2) {
                        return 0;
                    }
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            return -1;
        }
    }

    /**
     * 整型属性比较类
     */
    private class ComparatorDouble implements Comparator<Object> {
        private String attr;
        private SortType sortType;

        private ComparatorDouble(String attr, SortType sortType) {
            this.attr = attr;
            this.sortType = sortType;
        }

        @Override
        public int compare(Object o1, Object o2) {
            if (null != o1 && null != o2) {
                double attr1;
                double attr2;
                try {
                    if (o1 instanceof Double && o2 instanceof Double) {
                        attr1 = (double) o1;
                        attr2 = (double) o2;
                        if (sortType == SortType.DESC) {
                            //按倒序排序，从大到小
                            attr1 = (double) o2;
                            attr2 = (double) o1;
                        }
                    } else {
                        Field f1 = o1.getClass().getDeclaredField(attr);
                        Field f2 = o2.getClass().getDeclaredField(attr);
                        f1.setAccessible(true);
                        f2.setAccessible(true);
                        attr1 = f1.getDouble(o1);
                        attr2 = f2.getDouble(o2);
                        if (sortType == SortType.DESC) {
                            //按倒序排序，从大到小
                            attr1 = f2.getDouble(o2);
                            attr2 = f1.getDouble(o1);
                        }
                    }
                    if (attr1 > attr2) {
                        return 1;
                    } else if (attr1 == attr2) {
                        return 0;
                    }
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            return -1;
        }
    }
}
