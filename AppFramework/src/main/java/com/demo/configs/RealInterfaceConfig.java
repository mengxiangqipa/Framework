package com.demo.configs;

/**
 * @author YobertJomi
 * className RealInterfaceConfig
 * created at  2017/7/4  14:14
 */

public class RealInterfaceConfig {
    public static String BASE_SERVER_URL = InterfaceConfig.BASE_SERVER_URL;

    public static String getRealBaseServerUrl() {
        return BASE_SERVER_URL;
    }

    public static void setBaseServerUrl(String baseServerUrl) {
        BASE_SERVER_URL = baseServerUrl;
    }
}
