package com.framework.utils;

/**
 * 距离
 *     @author YobertJomi
 *     className DistanceUtil
 *     created at  2017/8/4  11:29
 */
public class DistanceUtil {
    private static volatile DistanceUtil singleton;

    private DistanceUtil() {
    }

    public static DistanceUtil getInstance() {
        if (singleton == null) {
            synchronized (DistanceUtil.class) {
                if (singleton == null) {
                    singleton = new DistanceUtil();
                }
            }
        }
        return singleton;
    }
    private static final double EARTH_RADIUS = 6378137.0;
    /**
     * 计算距离
     * <p>
     * 网 返回单位米；
     */
    public  double distanceByLngLat(double lng1,double lat1, double lng2, double lat2) {
        double radLat1 = lat1 * Math.PI / 180;
        double radLat2 = lat2 * Math.PI / 180;
        double a = radLat1 - radLat2;
        double b = lng1 * Math.PI / 180 - lng2 * Math.PI / 180;
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
                + Math.cos(radLat1) * Math.cos(radLat2)
                * Math.pow(Math.sin(b / 2), 2)));
        s = s * 6378137.0;
        s = Math.round(s * 10000) / 10000;
        return s;
    }

    // 两个方法差不多；

    /**
     * 计算距离 直接传入自己的经纬度和另外一个点的经纬度
     *
     * @return 返回单位米；
     */
    public  double getDistance(double longitude1,double latitude1, double longitude2, double latitude2) {
        try {
            if (null == String.valueOf(longitude1)
                    || "".equals(String.valueOf(longitude1)))
                return 0.00;
        } catch (Exception e) {
            return 0.00;
        }
        double Lat1 = rad(latitude1);
        double Lat2 = rad(latitude2);
        double a = Lat1 - Lat2;
        double b = rad(longitude1) - rad(longitude2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
                + Math.cos(Lat1) * Math.cos(Lat2)
                * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000) / 10000;
        return s;
    }

    public double rad(double d) {
        return d * Math.PI / 180.0;
    }
}
