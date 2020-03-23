package com.framework.util;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * DateAndTimeUtil
 *
 * @author YobertJomi
 * className DateAndTimeUtil
 * created at  2017/8/4  11:29
 */
public class DateAndTimeUtil {
    private static volatile DateAndTimeUtil singleton;

    private DateAndTimeUtil() {
    }

    public static DateAndTimeUtil getInstance() {
        if (singleton == null) {
            synchronized (DateAndTimeUtil.class) {
                if (singleton == null) {
                    singleton = new DateAndTimeUtil();
                }
            }
        }
        return singleton;
    }

    /**
     * @param timeMillis 毫秒数
     * @return 30:32
     */
    public String getTimeFilmFormat(String timeMillis) {
        try {
            Y.y("String_timeMillis:" + timeMillis);
            float timeMillis2 = Float.parseFloat(timeMillis);
            Y.y("timeMillis2:" + timeMillis2);
            if (timeMillis2 <= 0) {
                return "0:00";
            }
            int seconds = (int) ((timeMillis2 / 1000) / 60);
            int minute = (int) ((timeMillis2 / 1000) % 60);
            String secondsT = (seconds >= 10) ? String.valueOf(minute) : "0"
                    + String.valueOf(seconds);
            String minuteT = (minute >= 10) ? String.valueOf(minute) : "0"
                    + String.valueOf(minute);
            return secondsT + ":" + minuteT;
        } catch (Exception e) {
            return "0:00";
        }
    }

    /**
     * 将时间戳转为代表"距现在多久之前"的字符串
     *
     * @param timeStr 时间戳 传入日期格式"yyyy-MM-dd HH:mm:ss"
     */
    public String getDateDistance(String timeStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        long timemillis;
        try {
            Date date = sdf.parse(timeStr);
            timemillis = date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return "时间未知";
        }
        return getDateDistance(timemillis);
    }

    /**
     * 将时间戳转为代表"距现在多久之前"的字符串
     */
    public String getDateDistance(long timemillis) {
        StringBuilder sb = new StringBuilder();
        if (0 == timemillis) {
            return "";
        } else {
            long time = System.currentTimeMillis() - (timemillis);
            // 秒前
            long mill = (long) Math.floor(time / 1000);
            // 分钟前
            long minute = (long) Math.floor(time / 60 / 1000.0f);
            // 小时
            long hour = (long) Math.floor(time / 60 / 60 / 1000.0f);
            // 天前
            long day = (long) Math.floor(time / 24 / 60 / 60 / 1000.0f);
            if (day > 0) {
                sb.append(day).append("天");
            } else if (hour > 0) {
                if (hour >= 24) {
                    sb.append("1天");
                } else {
                    sb.append(hour).append("小时");
                }
            } else if (minute > 0) {
                if (minute == 60) {
                    sb.append("1小时");
                } else {
                    sb.append(minute).append("分钟");
                }
            } else if (mill > 0) {
                if (mill == 60) {
                    sb.append("1分钟");
                } else {
                    sb.append(mill).append("秒");
                }
            } else {
                sb.append("刚刚");
            }
            if (!"刚刚".equals(sb.toString())) {
                sb.append("前");
            }
            return sb.toString();
        }
    }

    /**
     * @param timeMillis 毫秒数
     * @return 30:32
     */
    public String getTimeFilmFormat(float timeMillis) {
        if (timeMillis <= 0) {
            return "0:00";
        }
        int seconds = (int) ((timeMillis / 1000) / 60);
        int minute = (int) ((timeMillis / 1000) % 60);
        String secondsT = (seconds >= 10) ? String.valueOf(minute) : "0"
                + String.valueOf(seconds);
        String minuteT = (minute >= 10) ? String.valueOf(minute) : "0"
                + String.valueOf(minute);
        return secondsT + ":" + minuteT;
    }

    public Date stringToDate(String time, String formatType) {
        SimpleDateFormat sdf = new SimpleDateFormat(formatType, Locale.getDefault());
        Date date = null;
        if (!TextUtils.isEmpty(time)) {
            try {
                date = sdf.parse(time);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return date;
    }

    public String dateToString(Date date, String formatType) {
        String time = null;
        if (null != date) {
            time = new SimpleDateFormat(formatType, Locale.getDefault()).format(date);
        }
        return time;
    }

    public long dateToLong(Date date) {
        if (null != date) {
            return date.getTime();
        }
        return 0;
    }

    public Date longToDate(long time) {
        return new Date(time);
    }

    public String longToString(long time, String formatType) {
        Date date = new Date(time);
        return dateToString(date, formatType);
    }

    public long stringTolong(String timeStr, String formatType) {
        if (!TextUtils.isEmpty(timeStr)) {
            Date date = stringToDate(timeStr, formatType);
            return date.getTime();
        }
        return 0;
    }

    public String longToString(long time) {
        return longToString(time, "yyyy-MM-dd HH:mm:ss");
    }

    public String longToStringWithfff(long time) {
        return longToString(time, "yyyy-MM-dd HH:mm:ss.fff");
    }
}
