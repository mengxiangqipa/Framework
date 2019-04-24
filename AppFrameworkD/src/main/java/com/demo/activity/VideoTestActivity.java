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
//        baseVideoFragment = BaseVideoFragment.build
//                ("http://112.253.22.157/17/z/z/y/u/zzyuasjwufnqerzvyxgkuigrkcatxr/hc.yinyuetai" +
//                        ".com/D046015255134077DDB3ACA0D7E68D45.flv","我是title",500);
        Bundle bundle=new Bundle();
        bundle.putString(BaseVideoFragment.URL,"http://dmp-media.eos-beijing-1.cmecloud.cn/201811/15/0F04C0981D8D4DFFA053CB8966A2985F.3gp");
        bundle.putString(BaseVideoFragment.TITLE,"我是title");
        bundle.putInt(BaseVideoFragment.HEIGHT,500);
        baseVideoFragment = BaseVideoFragment.build(bundle);
//        urlSource="http://dmp-media.eos-beijing-1.cmecloud.cn/201811/15/0F04C0981D8D4DFFA053CB8966A2985F.3gp";
////        urlSource="http://dmp-media.eos-beijing-1.cmecloud.cn/201809/18/799461150373445D8E4EF696B92A68EC.3gp";
//        Y.y("视频地址："+urlSource);
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
