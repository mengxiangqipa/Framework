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
        Bundle bundle=new Bundle();
        bundle.putString(BaseVideoFragment.URL,"http://112.253.22.157/17/z/z/y/u/zzyuasjwufnqerzvyxgkuigrkcatxr/hc.yinyuetai" +
                ".com/D046015255134077DDB3ACA0D7E68D45.flv");
        bundle.putString(BaseVideoFragment.TITLE,"我是title");
        bundle.putInt(BaseVideoFragment.HEIGHT,400);
        bundle.putBoolean(BaseVideoFragment.WITH_CACHE,true);
        bundle.putBoolean(BaseVideoFragment.SHOW_CLOCK,false);
        bundle.putBoolean(BaseVideoFragment.SHOW_FULL_SCREENB,false);
        bundle.putBoolean(BaseVideoFragment.SHOW_BOTTOM,true);
        bundle.putBoolean(BaseVideoFragment.SHOW_TOP,false);
        bundle.putBoolean(BaseVideoFragment.AUTO_PLAY,false);
        baseVideoFragment = BaseVideoFragment.build(bundle);
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
