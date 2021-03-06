package com.framework2.baseEvent;

import android.view.View;

/**
 * 单次点击，间隔200ms
 *
 * @author Yangjie
 * className BaseOnClickListener
 * created at  2017/3/20  14:53
 */

public abstract class BaseOnClickListener implements View.OnClickListener {
    private long timeMillisInterval = 200;//时间间隔
    private long lastTimeMillis;

    public BaseOnClickListener() {
        this(200);
    }

    public BaseOnClickListener(long timeMillisInterval) {
        this.timeMillisInterval = timeMillisInterval;
    }

    protected abstract void onBaseClick(View v);

    public void setTimeMillisInterval(long timeMillisInterval) {
        this.timeMillisInterval = timeMillisInterval;
    }

    @Override
    public void onClick(View v) {
        if (System.currentTimeMillis() - lastTimeMillis < timeMillisInterval) {

        } else {
            lastTimeMillis = System.currentTimeMillis();
            onBaseClick(v);
        }
    }
}
