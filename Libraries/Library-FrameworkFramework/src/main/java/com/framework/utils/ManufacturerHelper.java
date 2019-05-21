package com.framework2.util;

import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 系统辅助类
 */
public final class ManufacturerHelper {

    private static volatile ManufacturerHelper singleton;

    private ManufacturerHelper() {
    }

    public static ManufacturerHelper getInstance() {
        if (singleton == null) {
            synchronized (ManufacturerHelper.class) {
                if (singleton == null) {
                    singleton = new ManufacturerHelper();
                }
            }
        }
        return singleton;
    }

    public static final String ROM_MIUI = "MIUI";
    public static final String ROM_EMUI = "EMUI";
    public static final String ROM_FLYME = "FLYME";
    public static final String ROM_OPPO = "OPPO";
    public static final String ROM_SMARTISAN = "SMARTISAN";
    public static final String ROM_VIVO = "VIVO";
    public static final String ROM_QIKU = "QIKU";

    private static final String KEY_VERSION_MIUI = "ro.miui.ui.version.name";
    private static final String KEY_VERSION_EMUI = "ro.build.version.emui";
    private static final String KEY_VERSION_OPPO = "ro.build.version.opporom";
    private static final String KEY_VERSION_SMARTISAN = "ro.smartisan.version";
    private static final String KEY_VERSION_VIVO = "ro.vivo.os.version";

    private static String sName;
    private static String sVersion;

    /**
     * 是否是华为系统
     */
    public boolean isEMUI() {
        return check(ROM_EMUI);
    }

    /**
     * 是否是小米系统
     */
    public boolean isMIUI() {
        return check(ROM_MIUI);
    }

    /**
     * 是否是VIVO系统
     */
    public boolean isVIVO() {
        return check(ROM_VIVO);
    }

    /**
     * 是否是OPPO系统
     */
    public boolean isOPPO() {
        return check(ROM_OPPO);
    }

    /**
     * 是否是魅族系统
     */
    public boolean isFlyme() {
        return check(ROM_FLYME);
    }

    public boolean is360() {
        return check(ROM_QIKU) || check("360");
    }

    /**
     * 是否是锤子系统
     */
    public boolean isSmartisan() {
        return check(ROM_SMARTISAN);
    }

    public String getName() {
        if (sName == null) {
            check("");
        }
        return sName;
    }

    public String getVersion() {
        if (sVersion == null) {
            check("");
        }
        return sVersion;
    }

    public static boolean check(String rom) {
        if (sName != null) {
            return sName.equals(rom);
        }

        if (!TextUtils.isEmpty(sVersion = getProp(KEY_VERSION_MIUI))) {
            sName = ROM_MIUI;
        } else if (!TextUtils.isEmpty(sVersion = getProp(KEY_VERSION_EMUI))) {
            sName = ROM_EMUI;
        } else if (!TextUtils.isEmpty(sVersion = getProp(KEY_VERSION_OPPO))) {
            sName = ROM_OPPO;
        } else if (!TextUtils.isEmpty(sVersion = getProp(KEY_VERSION_VIVO))) {
            sName = ROM_VIVO;
        } else if (!TextUtils.isEmpty(sVersion = getProp(KEY_VERSION_SMARTISAN))) {
            sName = ROM_SMARTISAN;
        } else {
            sVersion = Build.DISPLAY;
            if (sVersion.toUpperCase().contains(ROM_FLYME)) {
                sName = ROM_FLYME;
            } else {
                sVersion = Build.UNKNOWN;
                sName = Build.MANUFACTURER.toUpperCase();
            }
        }
        return sName.equals(rom);
    }

    public static String getProp(String name) {
        String line = null;
        BufferedReader input = null;
        try {
            Process p = Runtime.getRuntime().exec("getprop " + name);
            input = new BufferedReader(new InputStreamReader(p.getInputStream()), 1024);
            line = input.readLine();
            input.close();
        } catch (IOException ex) {
            Log.e("ManufacturerHelper", "Unable to read prop " + name, ex);
            return null;
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return line;
    }
}
