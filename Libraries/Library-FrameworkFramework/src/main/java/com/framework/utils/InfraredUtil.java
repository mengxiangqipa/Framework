package com.framework.utils;

import android.content.Context;
import android.hardware.ConsumerIrManager;
import android.os.Build;
import android.support.annotation.NonNull;

/**
 * 红外控制类
 *
 * @author YobertJomi
 * className InfraredUtil
 * created at  2018/11/19  15:33
        <!-- 调用红外设备权限声明 -->
        <uses-permission android:name="android.permission.TRANSMIT_IR" />
        <!-- Android Market会根据uses-feature过滤所有你设备不支持的应用,即无红外功能的设备看不到此应用 -->
        <uses-feature android:name="android.hardware.ConsumerIrManager" />
 */

public class InfraredUtil {

    private static volatile InfraredUtil mInstance;
    private ConsumerIrManager consumerIrManager = null;

    /**
     * 获取单例引用
     */
    public static InfraredUtil getInstance() {
        if (mInstance == null) {
            synchronized (InfraredUtil.class) {
                if (mInstance == null) {
                    mInstance = new InfraredUtil();
                }
            }
        }
        return mInstance;
    }

    public boolean hasIrEmitter(@NonNull Context context) {
        //如果sdk版本大于4.4才进行是否有红外的功能
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //获取红外管理器,调用系统API  CONSUMER_IR_SERVICE红外的API
            consumerIrManager = (ConsumerIrManager) context.getSystemService(Context
                    .CONSUMER_IR_SERVICE);
            //判断是否有红外
            return consumerIrManager.hasIrEmitter();
        } else {
            return false;
        }
    }

    public void transmit(@NonNull Context context, int carrierFrequency, int[] pattern) {
        //如果sdk版本大于4.4才进行是否有红外的功能
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && null == consumerIrManager) {
            //获取红外管理器,调用系统API  CONSUMER_IR_SERVICE红外的API
            consumerIrManager = (ConsumerIrManager) context.getSystemService(Context
                    .CONSUMER_IR_SERVICE);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && null != consumerIrManager) {
            consumerIrManager.transmit(carrierFrequency, pattern);
        }
    }
}