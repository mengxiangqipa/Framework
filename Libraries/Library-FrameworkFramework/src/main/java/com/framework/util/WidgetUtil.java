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

import android.os.SystemClock;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.MotionEvent;

/**
 * 简单视图工具类
 *     @author YobertJomi
 *     className WidgetUtil
 *     created at  2019/10/20  11:31
 */
public class WidgetUtil {
    private static volatile WidgetUtil singleton;

    private WidgetUtil() {
    }

    public static WidgetUtil getInstance() {
        if (singleton == null) {
            synchronized (WidgetUtil.class) {
                if (singleton == null) {
                    singleton = new WidgetUtil();
                }
            }
        }
        return singleton;
    }

    public static void forceStopRecyclerViewScroll(RecyclerView recyclerView) {
        if (recyclerView != null) {
            recyclerView.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(),
                    SystemClock.uptimeMillis(), MotionEvent.ACTION_CANCEL, 0, 0, 0));
        }
    }
}
