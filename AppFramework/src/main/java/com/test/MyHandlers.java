package com.test;

import android.content.Intent;
import android.view.View;

import com.demo.activity.HomePageActivity;

/**
 * dataBinding的事件监听
 *     @author YobertJomi
 *     className MyHandlers
 *     created at  2016/12/28  14:29
 */

public class MyHandlers
{
   private HomePageActivity activity;

    public MyHandlers(HomePageActivity activity) {
        this.activity = activity;
    }

    public void onClickRefresh(View view){
        activity.startActivity(new Intent(activity,MainActivity.class));
    }
}
