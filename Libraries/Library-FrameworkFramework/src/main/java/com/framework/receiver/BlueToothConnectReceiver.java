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

package com.framework.receiver;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class BlueToothConnectReceiver extends BroadcastReceiver {

    private OnBleConnectListener onBleConnectListener;

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
        if (null == action) {
            return;
        }
        switch (action) {
//            case BluetoothA2dp.ACTION_CONNECTION_STATE_CHANGED:
//                switch (intent.getIntExtra(BluetoothA2dp.EXTRA_STATE, -1)) {
//                    case BluetoothA2dp.STATE_CONNECTING:
//                        device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
//                        Y.y("device: " + device.getName() +" connecting");
//                        break;
//                    case BluetoothA2dp.STATE_CONNECTED:
//                        device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
//                        LogUtil.i("device: " + device.getName() +" connected");
//                        mOnBluetoothListener.deviceConnected(device);
//                        break;
//                    case BluetoothA2dp.STATE_DISCONNECTING:
//                        device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
//                        Y.y("device: " + device.getName() +" disconnecting");
//                        break;
//                    case BluetoothA2dp.STATE_DISCONNECTED:
//                        device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
//                        Y.y("device: " + device.getName() +" disconnected");
//                        break;
//                    default:
//                        break;
//                }
//                break;
            case BluetoothAdapter.ACTION_STATE_CHANGED:
                int blueState = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, 0);
                switch (blueState) {
                    case BluetoothAdapter.STATE_TURNING_ON:
                        //蓝牙正在打开
                        break;
                    case BluetoothAdapter.STATE_ON:
                        //蓝牙已经打开
                        if (onBleConnectListener != null) {
                            onBleConnectListener.onOpen(device);
                        }
                        Toast.makeText(context, "蓝牙已经打开", Toast.LENGTH_SHORT).show();
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        //蓝牙正在关闭
                        break;
                    case BluetoothAdapter.STATE_OFF:
                        //蓝牙已经关闭
                        if (onBleConnectListener != null) {
                            onBleConnectListener.onDisConnect(device);
                        }
                        break;
                }
                break;
            case BluetoothDevice.ACTION_ACL_CONNECTED:
                //蓝牙设备已连接
                if (onBleConnectListener != null) {
                    onBleConnectListener.onConnect(device);
                }
                break;
            case BluetoothDevice.ACTION_ACL_DISCONNECTED:
                //蓝牙设备已断开
                //当直接关闭蓝牙时此处不会被触发，只有当蓝牙未关闭并且断开蓝牙时才会触发
                if (onBleConnectListener != null) {
                    onBleConnectListener.onDisConnect(device);
                }
                break;
        }
    }

    public interface OnBleConnectListener {

        void onOpen(BluetoothDevice device);

        void onConnect(BluetoothDevice device);

        void onDisConnect(BluetoothDevice device);
    }

    public void setOnBleConnectListener(OnBleConnectListener onBleConnectListener) {
        this.onBleConnectListener = onBleConnectListener;
    }
}
