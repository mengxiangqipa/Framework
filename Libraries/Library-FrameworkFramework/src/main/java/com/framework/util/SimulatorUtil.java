package com.framework.util;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Build;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

/**
 * 验证当前设备是否为模拟器
 *
 * @author YobertJomi
 */
public class SimulatorUtil {
    public static boolean isSimulator = false;

    /**
     * 模拟器验证结果
     */
    @RequiresApi(api = Build.VERSION_CODES.ECLAIR)
    public static boolean isSimulator(@NonNull Context context) {
        if (withoutBlueTooth()) {
            isSimulator = true;
        } else if (withoutLightSensorManager(context)) {
            isSimulator = true;
        } else if (ifFeatures()) {
            isSimulator = true;
        } else if (checkIsNotRealPhone()) {
            isSimulator = true;
        }
        return isSimulator;
    }

    /**
     * 判断蓝牙是否有效
     */
    @RequiresApi(api = Build.VERSION_CODES.ECLAIR)
    private static boolean withoutBlueTooth() {
        try {
            BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (bluetoothAdapter == null) {
                return true;
            } else {
                // 如果蓝牙不一定有效的。获取蓝牙名称，若为 null 则默认为模拟器
                String name = bluetoothAdapter.getName();
                return TextUtils.isEmpty(name);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
    }

    /**
     * 依据是否存在 光传感器 来判断是否为模拟器
     *
     * @param context Context
     * @return 是否没有感应器
     */
    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    private static boolean withoutLightSensorManager(Context context) {
        try {
            SensorManager sensorManager =
                    (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
            Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
            return sensor == null;
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
    }

    /**
     * 根据部分特征参数设备信息来判断是否为模拟器
     *
     * @return 是否是模拟器
     */
    @RequiresApi(api = Build.VERSION_CODES.DONUT)
    private static boolean ifFeatures() {
        return Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.toLowerCase().contains("vbox")
                || Build.FINGERPRINT.toLowerCase().contains("test-keys")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.MANUFACTURER.contains("Genymotion")
                || (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
                || "google_sdk".equals(Build.PRODUCT);
    }

    /**
     * 根据CPU是否为电脑来判断是否为模拟器
     * 返回:true 为模拟器
     */
    private static boolean checkIsNotRealPhone() {
        try {
            String cpuInfo = readCpuInfo();
            return cpuInfo.contains("intel") || cpuInfo.contains("amd");
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
    }

    /**
     * 根据 CPU 是否为电脑来判断是否为模拟器
     *
     * @return cup型号
     */
    private static String readCpuInfo() {
        String result = "";
        try {
            String[] args = {"/system/bin/cat", "/proc/cpuinfo"};
            ProcessBuilder processBuilder = new ProcessBuilder(args);
            Process process = processBuilder.start();
            StringBuilder stringBuffer = new StringBuilder();
            String readLine;
            BufferedReader responseReader;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                responseReader = new BufferedReader(new InputStreamReader(process.getInputStream(),
                        StandardCharsets.UTF_8));
            } else {
                responseReader = new BufferedReader(new InputStreamReader(process.getInputStream(),
                        "utf-8"));
            }
            while ((readLine = responseReader.readLine()) != null) {
                stringBuffer.append(readLine);
            }
            responseReader.close();
            result = stringBuffer.toString().toLowerCase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
