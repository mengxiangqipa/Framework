package com.framework.util;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.format.Formatter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.TimeZone;
import java.util.regex.Pattern;

/**
 * 系统工具，内存（已用，总体），硬盘（已用，总体），手机号等，版本号等
 *
 * @author YobertJomi
 * className OSutil
 * created at  2018/1/24  16:44
 */
public class OSutil {
    private static volatile OSutil singleton;
    private String platform = "Android";

    public static OSutil getInstance() {
        if (singleton == null) {
            synchronized (OSutil.class) {
                if (singleton == null) {
                    singleton = new OSutil();
                }
            }
        }
        return singleton;
    }

    public JSONObject getAllOSparams(Context context) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("PLATFORM", getPlatform());
            jsonObject.put("IMEI", getIMEI(context));
            jsonObject.put("UUID", getUUID(context));
            jsonObject.put("SIMNUMBER", getSimSerialNumber(context));
            jsonObject.put("IMSI", getIMSI(context));
            jsonObject.put("OSVERSION", getOSVersion());
            jsonObject.put("SDKVERSION", getSDKVersion());
            jsonObject.put("TIMEZONEID", getTimeZoneID());
            jsonObject.put("MODEL", getModel());
            jsonObject.put("MANUFACTURER", getManufacturer());
            jsonObject.put("MAC", getMac());
            jsonObject.put("IP", getIP(context));
            jsonObject.put("BRAND", getBrand());
            jsonObject.put("PRODUCTNAME", getProductName());
            jsonObject.put("NETWORKTYPE", getNetworkType(context));
        } catch (JSONException e) {
        }
        return jsonObject;
    }

    public String getIMSI(Context context) {
        if (null == context)
            return "NoContext";
        TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager
                .PERMISSION_GRANTED) {
            return "NoPermission";
        }
        return manager.getSubscriberId();
    }

    public String getPlatform() {
        return platform;
    }

    public String getUUID(Context context) {
        if (null == context)
            return "NoContext";
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public String getTelphoneNumber(Context context) {
        if (null == context)
            return "NoContext";
        TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_SMS) != PackageManager
                .PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            return "NoPermission";
        }
        return manager.getLine1Number();
    }

    public String getIMEI(Context context) {
        if (null == context)
            return "NoContext";
        TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager
                .PERMISSION_GRANTED) {
            return "NoPermission";
        }
        return manager.getDeviceId();
    }

    public String getSimSerialNumber(Context context) {
        if (null == context)
            return "NoContext";
        TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager
                .PERMISSION_GRANTED) {
            return "NoPermission";
        }
        return manager.getSimSerialNumber();
    }

    public String getOSVersion() {
        return android.os.Build.VERSION.RELEASE;
    }

    public String getSDKVersion() {
        return android.os.Build.VERSION.SDK;
    }

    public String getTimeZoneID() {
        TimeZone tz = TimeZone.getDefault();
        return (tz.getID());
    }

    public String getManufacturer() {
        return android.os.Build.MANUFACTURER;
    }

    public String getBrand() {
        return android.os.Build.BRAND;
    }

    public String getModel() {
        return android.os.Build.MODEL;
    }

    public String getProductName() {
        return android.os.Build.PRODUCT;
    }

    /**
     * 获取MAC地址
     */
    public String getMac() {
        String macAddress = "";
        try {
            StringBuffer buf = new StringBuffer();
            NetworkInterface networkInterface = null;
            networkInterface = NetworkInterface.getByName("eth1");
            if (networkInterface == null) {
                networkInterface = NetworkInterface.getByName("wlan0");
            }
            if (networkInterface == null) {
                return "02:00:00:00:00:02";
            }
            byte[] addr = networkInterface.getHardwareAddress();
            for (byte b : addr) {
                buf.append(String.format("%02X:", b));
            }
            if (buf.length() > 0) {
                buf.deleteCharAt(buf.length() - 1);
            }
            macAddress = buf.toString();
        } catch (Exception e) {
        }
        return macAddress;
    }

    public String getIP(Context context) {
        return getIPV4(context);
    }

    /**
     * 获取IPV4地址
     */
    public String getIPV4(Context context) {
        if (null == context)
            return "NoContext";
        NetworkInfo info = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE))
                .getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            if (info.getType() == ConnectivityManager.TYPE_MOBILE) {//当前使用2G/3G/4G网络
                try {
                    for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en
                            .hasMoreElements(); ) {
                        NetworkInterface intf = en.nextElement();
                        for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr
                                .hasMoreElements(); ) {
                            InetAddress inetAddress = enumIpAddr.nextElement();
                            if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                                return inetAddress.getHostAddress();
                            }
                        }
                    }
                } catch (Exception e) {
                }
            } else if (info.getType() == ConnectivityManager.TYPE_WIFI) {//当前使用无线网络
                WifiManager wifiManager = (WifiManager) (context.getApplicationContext()).getSystemService(Context
                        .WIFI_SERVICE);
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                String ipAddress = intIP2StringIP(wifiInfo.getIpAddress());//得到IPV4地址
                return ipAddress;
            }
        }
        return "";
    }

    /**
     * 将得到的int类型的IP转换为String类型
     */
    public String intIP2StringIP(int ip) {
        return (ip & 0xFF) + "." +
                ((ip >> 8) & 0xFF) + "." +
                ((ip >> 16) & 0xFF) + "." +
                (ip >> 24 & 0xFF);
    }

    /**
     * 获取网络类型
     */
    public String getNetworkType(Context context) {
        if (null == context) {
            return "no_context";
        }
        String strNetworkType = "";
        ConnectivityManager connectivityManager = (ConnectivityManager) (context).getSystemService(Context
                .CONNECTIVITY_SERVICE);
        //如果当前没有网络
        if (null == connectivityManager) {
            strNetworkType = "NETWORN_NONE";
        }
        //获取当前网络类型，如果为空，返回无网络
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isAvailable()) {
            strNetworkType = "NETWORN_NONE";
        }
        if (networkInfo != null && networkInfo.isConnected()) {
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                strNetworkType = "WIFI";
            } else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                String _strSubTypeName = networkInfo.getSubtypeName();
                int networkType = networkInfo.getSubtype();
                switch (networkType) {
                    case TelephonyManager.NETWORK_TYPE_GPRS:
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                    case TelephonyManager.NETWORK_TYPE_1xRTT:
                    case TelephonyManager.NETWORK_TYPE_IDEN: //api<8 : replace by 11
                        strNetworkType = "2G";
                        break;
                    case TelephonyManager.NETWORK_TYPE_UMTS:
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    case TelephonyManager.NETWORK_TYPE_HSDPA:
                    case TelephonyManager.NETWORK_TYPE_HSUPA:
                    case TelephonyManager.NETWORK_TYPE_HSPA:
                    case TelephonyManager.NETWORK_TYPE_EVDO_B: //api<9 : replace by 14
                    case TelephonyManager.NETWORK_TYPE_EHRPD:  //api<11 : replace by 12
                    case TelephonyManager.NETWORK_TYPE_HSPAP:  //api<13 : replace by 15
                        strNetworkType = "3G";
                        break;
                    case TelephonyManager.NETWORK_TYPE_LTE:    //api<11 : replace by 13
                        strNetworkType = "4G";
                        break;
                    default:
                        // http://baike.baidu.com/item/TD-SCDMA 中国移动 联通 电信 三种3G制式
                        if (_strSubTypeName.equalsIgnoreCase("TD-SCDMA")
                                || _strSubTypeName.equalsIgnoreCase("WCDMA")
                                || _strSubTypeName.equalsIgnoreCase("CDMA2000")) {
                            strNetworkType = "3G";
                        } else {
                            strNetworkType = _strSubTypeName;
                        }
                        break;
                }
            } else {
                strNetworkType = "NETWORN_NONE";
            }
        } else {
            strNetworkType = "NETWORN_NONE";
        }
        return strNetworkType;
    }

    /**
     * 获得sd卡可用大小
     */
    public String getRomUsableSpace(Context context) {
        if (null == context)
            return "NoContext";
        File sdcard_filedir = Environment.getExternalStorageDirectory();//得到sdcard的目录作为一个文件对象
        long usableSpace = sdcard_filedir.getUsableSpace();//获取文件目录对象剩余空间
        String usableSpace_str = Formatter.formatFileSize(context, usableSpace);
        return usableSpace_str;
    }

    /**
     * 获得sd卡总大小
     */
    public String getRomTotalSpace(Context context) {
        if (null == context)
            return "NoContext";
        File sdcard_filedir = Environment.getExternalStorageDirectory();//得到sdcard的目录作为一个文件对象
        long totalSpace = sdcard_filedir.getTotalSpace();
        String totalSpace_str = Formatter.formatFileSize(context, totalSpace);
        return totalSpace_str;
    }

    /**
     * 获得内存大小
     */
    public String getRamTotalSpace(Context context) {//GB
        if (null == context)
            return "NoContext";
        String path = "/proc/meminfo";
        String firstLine = null;
        try {
            FileReader fileReader = new FileReader(path);
            BufferedReader br = new BufferedReader(fileReader, 8192);
            firstLine = br.readLine().split("\\s+")[1];
            br.close();
            if (firstLine != null) {
                return Formatter.formatFileSize(context, Long.parseLong(firstLine) * 1024);
                //返回1GB/2GB/3GB/4GB
            }
        } catch (Exception e) {
        }
        return "0 GB";
    }

    /**
     * 获得可用的内存
     */
    public String getRamUsableSpace(Context context) {
        if (null == context)
            return "NoContext";
        long MEM_UNUSED;
        // 得到ActivityManager
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        // 创建ActivityManager.MemoryInfo对象  
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(mi);
        // 取得剩余的内存空间 
        return Formatter.formatFileSize(context, mi.availMem);
    }

    // 获取CPU最大频率（单位GHZ）
    public Float getMaxCpuFreq() {
        String result = "";
        ProcessBuilder cmd;
        try {
            String[] args = {"/system/bin/cat",
                    "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq"};
            cmd = new ProcessBuilder(args);
            Process process = cmd.start();
            InputStream in = process.getInputStream();
            byte[] re = new byte[24];
            while (in.read(re) != -1) {
                result = result + new String(re);
            }
            in.close();
            double div = 1000 * 1000;
            return BigDecimal.valueOf(Double.parseDouble(result) / div).setScale(1, BigDecimal.ROUND_HALF_UP)
                    .floatValue();
        } catch (Exception ex) {
            return 0f;
        }
    }

    // 获取CPU最小频率（单位GHZ）
    public float getMinCpuFreq() {
        String result = "";
        ProcessBuilder cmd;
        try {
            String[] args = {"/system/bin/cat",
                    "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_min_freq"};
            cmd = new ProcessBuilder(args);
            Process process = cmd.start();
            InputStream in = process.getInputStream();
            byte[] re = new byte[24];
            while (in.read(re) != -1) {
                result = result + new String(re);
            }
            in.close();
            double div = 1000 * 1000;
            return BigDecimal.valueOf(Double.parseDouble(result) / div).setScale(1, BigDecimal.ROUND_HALF_UP)
                    .floatValue();
        } catch (Exception ex) {
            return 0f;
        }
    }

    // 实时获取CPU当前频率（单位GHZ）
    public float getCurCpuFreq() {
        String result = "N/A";
        try {
            FileReader fr = new FileReader(
                    "/sys/devices/system/cpu/cpu0/cpufreq/scaling_cur_freq");
            BufferedReader br = new BufferedReader(fr);
            String text = br.readLine();
            result = text.trim();
            double div = 1000 * 1000;
            return BigDecimal.valueOf(Double.parseDouble(result) / div).setScale(1, BigDecimal.ROUND_HALF_UP)
                    .floatValue();
        } catch (Exception e) {
            return 0f;
        }
    }

    public String getCpuName() {
        try {
            FileReader fr = new FileReader("/proc/cpuinfo");
            BufferedReader br = new BufferedReader(fr);
            String text = br.readLine();
            String[] array = text.split(":\\s+", 2);
            for (int i = 0; i < array.length; i++) {
            }
            return array[1];
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int getCpuCores() {
        //Private Class to display only CPU devices in the directory listing
        class CpuFilter implements FileFilter {
            @Override
            public boolean accept(File pathname) {
                //Check if filename is "cpu", followed by a single digit number
                if (Pattern.matches("cpu[0-9]", pathname.getName())) {
                    return true;
                }
                return false;
            }
        }

        try {
            //Get directory containing CPU info
            File dir = new File("/sys/devices/system/cpu/");
            //Filter to only list the devices we care about
            File[] files = dir.listFiles(new CpuFilter());
            Y.y("CPU Count: " + files.length);
            //Return the number of cores (virtual CPU devices)
            return files.length;
        } catch (Exception e) {
            //Print exception
            Y.y("CPU Count: Failed.");
            e.printStackTrace();
            //Default to return 1 core
            return 1;
        }
    }
}
