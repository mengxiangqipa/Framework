package com.demo.activity;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import androidx.fragment.app.FragmentActivity;

import com.demo.demo.R;
import com.library.instance.BaseVideoFragment;

import java.util.HashMap;

public class VideoTestActivity extends FragmentActivity {
    private BaseVideoFragment baseVideoFragment;

    /**
     * 给出url，获取视频的第一帧
     *
     * @param url 视频地址
     * @return
     */
    public static Bitmap getVideoThumbnail(String url) {
        Bitmap bitmap = null;
        //MediaMetadataRetriever 是android中定义好的一个类，提供了统一
        //的接口，用于从输入的媒体文件中取得帧和元数据；
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            //根据文件路径获取缩略图
            retriever.setDataSource(url, new HashMap());
            //获得第一帧图片
            bitmap = retriever.getFrameAtTime();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } finally {
            retriever.release();
        }
        return bitmap;
    }

    @Override
    protected void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.video_activity_video_test);
        Bundle bundle = new Bundle();
        bundle.putString(BaseVideoFragment.URL, "http://112.253.22" +
                ".157/17/z/z/y/u/zzyuasjwufnqerzvyxgkuigrkcatxr/hc" +
                ".yinyuetai" +
                ".com/D046015255134077DDB3ACA0D7E68D45.flv");
        bundle.putString(BaseVideoFragment.TITLE, "我是title");
        bundle.putInt(BaseVideoFragment.HEIGHT, 400);
        bundle.putBoolean(BaseVideoFragment.WITH_CACHE, true);
        bundle.putBoolean(BaseVideoFragment.SHOW_CLOCK, false);
        bundle.putBoolean(BaseVideoFragment.SHOW_FULL_SCREENB, false);
        bundle.putBoolean(BaseVideoFragment.SHOW_BOTTOM, true);
        bundle.putBoolean(BaseVideoFragment.SHOW_TOP, false);
        bundle.putBoolean(BaseVideoFragment.AUTO_PLAY, false);
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
