package com.library.instance;

import android.content.res.Configuration;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.library.videoview.CustomVideoViewWithUI;
import com.library.videoview.utils.ScreenUtils;

/**
 * 更新了第一版进入后台再打开页面的时候报错的问题，保存播放进度。
 * qiangyu on 1/26/16 15:33
 * csdn博客:http://blog.csdn.net/yissan
 */
public class VideoViewPlayingActivity extends AppCompatActivity {

    CustomVideoViewWithUI customVideoViewWithUI;
    TextView titleBar;
    View titleTemp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//		setContentView(R.layout.content_main);
//
//		titleBar = (TextView) findViewById(R.id.titleBar);
//		titleTemp =findViewById(R.id.titleTemp);
//		customVideoViewWithUI = (CustomVideoViewWithUI) findViewById(R.id.common_videoView);
//		customVideoViewWithUI.start("http://7ximq1.com1.z0.glb.clouddn.com/66257_20160912164112");
//		//		customVideoViewWithUI.start("http://112.253.22.157/17/z/z/y/u/zzyuasjwufnqerzvyxgkuigrkcatxr/hc
// .yinyuetai.com/D046015255134077DDB3ACA0D7E68D45.flv");
//		customVideoViewWithUI.setTitle("我是视频的title我是视频的title我是视频的title我是视频的title");
//		//        videoView.start("你在服务器上的视频地址，videoView仅支持mp4格式");
    }

    @Override
    protected void onPause() {
        super.onPause();
        customVideoViewWithUI.resetVolumAndBrightness();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        customVideoViewWithUI.resetVolumAndBrightness();
    }

    @Override
    protected void onResume() {
        super.onResume();
        customVideoViewWithUI.resumeVolumAndBrightness();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            titleBar.setVisibility(View.GONE);
            titleTemp.setVisibility(View.GONE);
            customVideoViewWithUI.setFullScreen();
        } else {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams
                    .MATCH_PARENT, ScreenUtils.getInstance().getStatusBarHeightPx(VideoViewPlayingActivity.this));
            titleTemp.setLayoutParams(layoutParams);
            titleTemp.setVisibility(View.VISIBLE);
            titleBar.setVisibility(View.VISIBLE);
            customVideoViewWithUI.setNormalScreen();
        }
    }

    @Override
    public void onBackPressed() {
        customVideoViewWithUI.onBackClicked();
    }
}
