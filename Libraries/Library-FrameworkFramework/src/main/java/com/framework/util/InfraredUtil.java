package com.framework.util;

import android.content.Context;
import android.hardware.ConsumerIrManager;
import android.os.Build;
import androidx.annotation.NonNull;

/**
 * 红外控制类
 *
 * @author YobertJomi
 * className InfraredUtil
 * created at  2018/11/19  15:33
 * <!-- 调用红外设备权限声明 -->
 * <uses-permission android:name="android.permission.TRANSMIT_IR" />
 * <!-- Android Market会根据uses-feature过滤所有你设备不支持的应用,即无红外功能的设备看不到此应用 -->
 * <uses-feature android:name="android.hardware.ConsumerIrManager" />
 * final int carrierFrequency=38*1000;
 * /*
 * 一种交替的载波序列模式，用于发射红外, pattern要和所用的红外码对应
 * 下标偶数：红外开
 * 下标奇数：红外关
 * 单位：微秒
 * 如：打开1000微秒再关闭500微秒再打开1000微秒关闭500微秒。
 * 注：1.开对应的是示波器上的低电平，关对应的高电平
 * 2.整个数组的时间之和不能超过两秒,且不能太短，否则无法读取用户码数据码
 * final  int[] pattern = {
 * 1000,500,1000,500,
 * 1000,500,1000,500,
 * 1000,500,1000,500,
 * 1000,500,1000,500,
 * 1000,500,1000,500 };
 * transmit(int carrierFrequency, int[] pattern)
 * 参数1：代表红外传输的频率，一般是38KHz，参数2：pattern就是指以微妙为单位的红外开和关的交替时间。
 * 通过38400赫兹的载波频率发射红外
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

    /**
     * @param context Context
     * @return 是否有红外功能
     */
    public boolean hasIrEmitter(@NonNull Context context) {
        //如果sdk版本大于4.4才进行是否有红外的功能
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //获取红外管理器,调用系统API  CONSUMER_IR_SERVICE红外的API
            consumerIrManager = (ConsumerIrManager) context.getSystemService(Context
                    .CONSUMER_IR_SERVICE);
            //判断是否有红外
            return null != consumerIrManager && consumerIrManager.hasIrEmitter();
        } else {
            return false;
        }
    }

    /**
     * 发射红外信号
     *
     * @param context Context
     */
    public void transmit(final @NonNull Context context, final int carrierFrequency, final int[] pattern) {
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

    /**
     * @param context Context
     * @return 最大红外频率
     */
    public int getMaxFrequency(final @NonNull Context context) {
        //如果sdk版本大于4.4才进行是否有红外的功能
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && null == consumerIrManager) {
            //获取红外管理器,调用系统API  CONSUMER_IR_SERVICE红外的API
            consumerIrManager = (ConsumerIrManager) context.getSystemService(Context
                    .CONSUMER_IR_SERVICE);
        }
        int maxFrequency = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && null != consumerIrManager) {
            ConsumerIrManager.CarrierFrequencyRange[] carrierFrequencyRange = consumerIrManager.getCarrierFrequencies();
            if (null != carrierFrequencyRange) {
                for (ConsumerIrManager.CarrierFrequencyRange range : carrierFrequencyRange) {
                    if (null != range)
                        maxFrequency = range.getMaxFrequency();
                }
            }
        }
        return maxFrequency;
    }

    /**
     * @param context Context
     * @return 最小红外频率
     */
    public int getMinFrequency(final @NonNull Context context) {
        //如果sdk版本大于4.4才进行是否有红外的功能
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && null == consumerIrManager) {
            //获取红外管理器,调用系统API  CONSUMER_IR_SERVICE红外的API
            consumerIrManager = (ConsumerIrManager) context.getSystemService(Context
                    .CONSUMER_IR_SERVICE);
        }
        int minFrequency = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && null != consumerIrManager) {
            ConsumerIrManager.CarrierFrequencyRange[] carrierFrequencyRange = consumerIrManager.getCarrierFrequencies();
            if (null != carrierFrequencyRange) {
                for (ConsumerIrManager.CarrierFrequencyRange range : carrierFrequencyRange) {
                    if (null != range)
                        minFrequency = range.getMinFrequency();
                }
            }
        }
        return minFrequency;
    }
}