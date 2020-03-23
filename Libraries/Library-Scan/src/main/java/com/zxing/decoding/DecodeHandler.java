/*
 * Copyright (C) 2010 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.zxing.decoding;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.ReaderException;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.multi.GenericMultipleBarcodeReader;
import com.library.scan.R;
import com.testactivity.ScanActivity;
import com.zxing.camera.CameraManager;
import com.zxing.camera.PlanarYUVLuminanceSource;

import java.util.Hashtable;

final class DecodeHandler extends Handler {

    private static final String TAG = DecodeHandler.class.getSimpleName();

    private final ScanActivity activity;
    private final MultiFormatReader multiFormatReader;

    DecodeHandler(ScanActivity activity, Hashtable<DecodeHintType, Object> hints) {
        multiFormatReader = new MultiFormatReader();
        multiFormatReader.setHints(hints);
        this.activity = activity;
    }

    @Override
    public void handleMessage(Message message) {
        if (message.what == R.id.decode) {//Log.d(TAG, "Got decode message");
            if (!CameraManager.isDecodeMultiple()) {
                decode((byte[]) message.obj, message.arg1, message.arg2);
            } else {
                decodeMultiple((byte[]) message.obj, message.arg1, message.arg2);
            }
        } else if (message.what == R.id.quit) {
            Looper.myLooper().quit();
        }
    }

    /**
     * Decode the data within the viewfinder rectangle, and time how long it took. For efficiency,
     * reuse the same reader objects from one decode to the next.
     *
     * @param data   The YUV preview frame.
     * @param width  The width of the preview frame.
     * @param height The height of the preview frame.
     */
    private void decode(byte[] data, int width, int height) {
        long start = System.currentTimeMillis();
        Result rawResult = null;
        //我修改
        byte[] rotatedData = null;
        if (CameraManager.isOrientationPotrait()) {
            rotatedData = new byte[data.length];
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    rotatedData[x * height + height - y - 1] = data[x + y * width];
                }
            }
        } else {
            // TODO: 2017/4/29 待完成
            rotatedData = new byte[data.length];
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++)
                    rotatedData[x * height + height - y - 1] = data[x + y * width];
            }
        }
//        Log.e("yy", "rotatedData (" + rotatedData.length);
//        Log.e("yy", "decode (" + (System.currentTimeMillis() - start) + " ms):\n");
        int tmp = width; // Here we are swapping, that's the difference to #11
        width = height;
        height = tmp;
        // 构造基于平面的YUV亮度源，即包含二维码区域的数据源
        PlanarYUVLuminanceSource source = CameraManager.get().buildLuminanceSource(rotatedData,
                width, height);
        // 构造二值图像比特流，使用HybridBinarizer算法解析数据源
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        try {
            // 采用MultiFormatReader解析图像，可以解析多种数据格式
            rawResult = multiFormatReader.decodeWithState(bitmap);
        } catch (ReaderException re) {
            // continue
        } catch (Exception e) {
            // continue
            Log.e("yy", "decode异常：" + e.getMessage());
        } finally {
            multiFormatReader.reset();
        }
//        Log.e("yy", "decodedddd (" + (System.currentTimeMillis() - start1) + " ms):\n");
        if (rawResult != null) {
            long end = System.currentTimeMillis();
            Log.e("yy", "Found barcode (" + (end - start) + " ms):\n" + rawResult.toString());
            Message message = Message.obtain(activity.getCaptureActivityHandler(),
                    R.id.decode_succeeded,
                    new Result[]{rawResult});
            Bundle bundle = new Bundle();
            bundle.putParcelable(DecodeThread.BARCODE_BITMAP,
                    source.renderCroppedGreyscaleBitmap());
            message.setData(bundle);
            //Log.d(TAG, "Sending decode succeeded message...");
            message.sendToTarget();
        } else {
            Message message = Message.obtain(activity.getCaptureActivityHandler(),
                    R.id.decode_failed);
            message.sendToTarget();
        }
    }

    /**
     * Decode the data within the viewfinder rectangle, and time how long it took. For efficiency,
     * reuse the same reader objects from one decode to the next.
     *
     * @param data   The YUV preview frame.
     * @param width  The width of the preview frame.
     * @param height The height of the preview frame.
     */
    private void decodeMultiple(byte[] data, int width, int height) {
        long start = System.currentTimeMillis();
        //我修改
        byte[] rotatedData = null;
        if (CameraManager.isOrientationPotrait()) {
            rotatedData = new byte[data.length];
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    rotatedData[x * height + height - y - 1] = data[x + y * width];
                }
            }
        } else {
            // TODO: 2017/4/29 待完成
            rotatedData = new byte[data.length];
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++)
                    rotatedData[x * height + height - y - 1] = data[x + y * width];
            }
        }
//        Log.e("yy", "rotatedData (" + rotatedData.length);
//        Log.e("yy", "decode (" + (System.currentTimeMillis() - start) + " ms):\n");
        int tmp = width; // Here we are swapping, that's the difference to #11
        width = height;
        height = tmp;
        // 构造基于平面的YUV亮度源，即包含二维码区域的数据源
        PlanarYUVLuminanceSource multipleSource =
                CameraManager.get().buildLuminanceSource(rotatedData, width, height);
        // 构造二值图像比特流，使用HybridBinarizer算法解析数据源
        BinaryBitmap multipleBinaryBitmap = new BinaryBitmap(new HybridBinarizer(multipleSource));

        Result[] results = null;
        try {
            GenericMultipleBarcodeReader genericMultipleBarcodeReader =
                    new GenericMultipleBarcodeReader(multiFormatReader);
            results = genericMultipleBarcodeReader.decodeMultiple(multipleBinaryBitmap);
            if (results != null) {
                Log.e("yy",
                        "Found barcode_s(" + (System.currentTimeMillis() - start) + " ms):\n" + results.toString());
                Log.e("yy", "Found barcode_s_length:(" + results.length);
                for (int i = 0; i < results.length; i++) {
                    Result result = results[i];
                    Log.e("yy",
                            "Found barcode (" + (System.currentTimeMillis() - start) + " ms):\n" + result.getText());
                }
            }
        } catch (ReaderException e) {
            // continue
        } catch (Exception e) {
            // continue
//            Log.e("yy", "decode异常：" + e.getMessage());
        } finally {
            multiFormatReader.reset();
        }
        if (results != null) {
            long end = System.currentTimeMillis();
//            Log.e("yy", "results_Found barcode (" + (end - start) + " ms):\n" + results
//            .toString());
            Message message = Message.obtain(activity.getCaptureActivityHandler(),
                    R.id.decode_succeeded, results);
            Bundle bundle = new Bundle();
            bundle.putParcelable(DecodeThread.BARCODE_BITMAP,
                    multipleSource.renderCroppedGreyscaleBitmap());
            message.setData(bundle);
            message.sendToTarget();
        } else {
            Message message = Message.obtain(activity.getCaptureActivityHandler(),
                    R.id.decode_failed);
            message.sendToTarget();
        }
    }
}
