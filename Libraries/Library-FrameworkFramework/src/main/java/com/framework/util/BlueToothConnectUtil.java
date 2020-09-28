/*
 *  Copyright (c) 2019 YobertJomi
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package com.framework.util;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothProfile;
import android.content.IntentFilter;
import android.os.Build;
import androidx.annotation.RequiresApi;
import android.text.TextUtils;

import com.framework.receiver.BlueToothConnectReceiver;

import java.util.Iterator;
import java.util.Set;

/**
 * 蓝牙连接变化util
 */
public class BlueToothConnectUtil {

    private BlueToothConnectReceiver blueToothConnectReceiver;

    private static volatile BlueToothConnectUtil singleton;

    private BlueToothConnectUtil() {
    }

    public static BlueToothConnectUtil getInstance() {
        if (singleton == null) {
            synchronized (BlueToothConnectUtil.class) {
                if (singleton == null) {
                    singleton = new BlueToothConnectUtil();
                }
            }
        }
        return singleton;
    }

    //注册广播接收器，用于监听蓝牙状态变化
    public void registerBlueToothStateReceiver(Activity activity,
                                               final BlueToothConnectReceiver.OnBleConnectListener onBleConnectListener) {
        if (null == activity) {
            return;
        }
        blueToothConnectReceiver = new BlueToothConnectReceiver();
        IntentFilter intentFilter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        // 监视蓝牙关闭和打开的状态
        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);

        // 监视蓝牙设备与APP连接的状态
        intentFilter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        intentFilter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);

        activity.registerReceiver(blueToothConnectReceiver, intentFilter);
        if (null != onBleConnectListener) {
            blueToothConnectReceiver.setOnBleConnectListener(new BlueToothConnectReceiver.OnBleConnectListener() {
                @Override
                public void onOpen(BluetoothDevice device) {
                    onBleConnectListener.onOpen(device);
                }

                @Override
                public void onConnect(BluetoothDevice device) {
                    onBleConnectListener.onConnect(device);
                }

                @Override
                public void onDisConnect(BluetoothDevice device) {
                    onBleConnectListener.onDisConnect(device);
                }
            });
        }
    }

    public void unregisterBlueToothConnectReceiver(Activity activity) {
        if (activity == null || blueToothConnectReceiver == null) {
            return;
        }
        activity.unregisterReceiver(blueToothConnectReceiver);
    }

    /**
     * 检查蓝牙是否连接到特定的蓝牙前缀
     */
    @RequiresApi(api = Build.VERSION_CODES.ECLAIR)
    public boolean isConnectToSpecialBt(String prefix) {
        //获取蓝牙适配器
        BluetoothAdapter bluetoothAdapter = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.ECLAIR) {
            bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        }
        if (bluetoothAdapter == null) {
            return false;
        }
        //判断本机蓝牙是否打开
        if (!bluetoothAdapter.isEnabled()) {
            //如果没打开，则打开蓝牙
//                boolean enable = bluetoothAdapter.enable();
            return false;
        } else {
            int a2dp = -1; //
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                a2dp = bluetoothAdapter.getProfileConnectionState(BluetoothProfile.A2DP);
            }
            // 可操控蓝牙设备，如带播放暂停功能的蓝牙耳机
            int headset = -1;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                headset = bluetoothAdapter.getProfileConnectionState(BluetoothProfile.HEADSET);
            }
            // 蓝牙头戴式耳机，支持语音输入输出
            int health = -1; //
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                health = bluetoothAdapter.getProfileConnectionState(BluetoothProfile.HEALTH);
            }
            // 蓝牙穿戴式设备
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                int GATT = bluetoothAdapter.getProfileConnectionState(BluetoothProfile.GATT);
            }
            // 查看是否蓝牙是否连接到三种设备的一种，以此来判断是否处于连接状态还是打开并没有连接的状态
            int flag = -1;
            if (a2dp == BluetoothProfile.STATE_CONNECTED) {
                flag = a2dp;
            } else if (headset == BluetoothProfile.STATE_CONNECTED) {
                flag = headset;
            } else if (health == BluetoothProfile.STATE_CONNECTED) {
                flag = health;
            }
            if (flag != -1) {
                if (TextUtils.isEmpty(prefix)) {
                    return true;
                }
                //获取已绑定的设备列表
                Set<BluetoothDevice> deviceList = bluetoothAdapter.getBondedDevices();
                if (null != deviceList && deviceList.size() > 0) {
                    Iterator<BluetoothDevice> iterator = deviceList.iterator();
                    if (iterator != null && iterator.hasNext()) {
                        BluetoothDevice device = deviceList.iterator().next();
                        if (null != device) {
                            Y.y("device.getBondState:" + device.getBondState());
                            if (device.getBondState() == BluetoothDevice.BOND_BONDED || device.getBondState() == BluetoothDevice.BOND_BONDING) {
                                Y.y("device.getName:" + device.getName());
                                if (!TextUtils.isEmpty(device.getName()) && device.getName().startsWith(prefix)) {
                                    return true;
                                }
                            }
                        }
                    }
                }
            } else {
                return false;
            }
            return false;
        }
    }
}
