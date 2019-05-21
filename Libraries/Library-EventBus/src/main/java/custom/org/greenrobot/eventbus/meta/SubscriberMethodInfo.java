/*
 * Copyright (C) 2012-2016 Markus Junginger, greenrobot (http://greenrobot.org)
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
package custom.org.greenrobot.eventbus.meta;

import custom.org.greenrobot.eventbus.ThreadMode;

public class SubscriberMethodInfo {
    final String methodName;
    final ThreadMode threadMode;
    final Class<?> eventType;
    final int priority;
    final boolean sticky;
    final String tag;//我修改

    public SubscriberMethodInfo(String methodName, Class<?> eventType, ThreadMode threadMode,
                                int priority, boolean sticky, String tag) {//我修改
        this.methodName = methodName;
        this.threadMode = threadMode;
        this.eventType = eventType;
        this.priority = priority;
        this.sticky = sticky;
        this.tag = tag;//我修改
    }

    public SubscriberMethodInfo(String methodName, Class<?> eventType) {
        this(methodName, eventType, ThreadMode.POSTING, 0, false, "EventBus");//我修改
    }

    public SubscriberMethodInfo(String methodName, Class<?> eventType, ThreadMode threadMode) {
        this(methodName, eventType, threadMode, 0, false, "EventBus");//我修改
    }
}