package com.demo.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.demo.demo.R;
import com.library.instance.BaseVideoFragment;

public class VideoTestActivity extends FragmentActivity {
    private BaseVideoFragment baseVideoFragment;

    @Override
    protected void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.video_activity_video_test);
        baseVideoFragment = BaseVideoFragment.build
                ("http://112.253.22.157/17/z/z/y/u/zzyuasjwufnqerzvyxgkuigrkcatxr/hc.yinyuetai" +
                        ".com/D046015255134077DDB3ACA0D7E68D45.flv","我是title",400);
        if (state == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.containerView, baseVideoFragment)
                    .commit();
        }
    }

    @Override
    public void onBackPressed() {
        baseVideoFragment.onBackPressed_();
    }
}
