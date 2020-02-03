package com.framework.customview.clipview;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

import com.framework.R;
import com.framework.util.BitmapUtil;
import com.framework.util.ScreenUtil;
import com.framework.util.Y;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class TestClipCircleIconActivity extends AppCompatActivity {
    private ClipImageLayout mClipImageLayout;

//前一个页面
//    Intent intent = new Intent(mContext,ClipCircleIconActivity.class);
////    intent.putExtra("bitmap", datas);
//     intent.putExtra("filePath",file.getAbsolutePath());
//    startActivityForResult(intent, 6000);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        Y.y("剪切头像：0");
        super.onCreate(savedInstanceState);
        Y.y("剪切头像：1");
        setContentView(R.layout.clipcircleicon);
        Y.y("剪切头像：2");
//        ((TextView) findViewById(R.id.tv_title)).setText("拖动选头像");
        Y.y("剪切头像：3");
//        ((TextView) findViewById(R.id.tv_edit)).setText("完成");
        Y.y("剪切头像：4");
//        findViewById(R.id.tv_edit).setVisibility(View.VISIBLE);
        Y.y("剪切头像：5");
//        Utils.setTranslucentStatus(this, true);
//        Utils.setStatusBarTintColor(this,
//                getResources().getColor(R.color.gooddream_black));
        Y.y("剪切头像：6");
        byte[] b = getIntent().getByteArrayExtra("bitmap");
        if (b == null) {
            String filePath = getIntent().getStringExtra("filePath");
            File file = new File(filePath);
            if (file.exists()) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                Y.y("跳转:" + "2");
                Bitmap bmap_sdcard = BitmapUtil.getInstance().getBitmapFromFile(file,
                        ScreenUtil.getInstance().getScreenWidthPx(this),
                        ScreenUtil.getInstance().getScreenWidthPx(this));
                bmap_sdcard.compress(Bitmap.CompressFormat.JPEG,
                        100, baos);
                Y.y("跳转:" + "3");
                b = baos.toByteArray();
            }
        }

        Y.y("剪切头像：7");
        Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
        Y.y("剪切头像：8");
        mClipImageLayout = (ClipImageLayout) findViewById(R.id.clipImageLayout);
        Y.y("剪切头像：9");
        mClipImageLayout.setClipLayoutCircle(this);
        Y.y("剪切头像：10");
        mClipImageLayout.setClipZoomImageViewBitmap(bitmap);
        Y.y("剪切头像：11");
        mClipImageLayout.setClipLayoutWidth(200, this);
        Y.y("剪切头像：12");
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case 1:
                Bitmap bitmap = mClipImageLayout.clip();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] datas = baos.toByteArray();
                Intent intent = getIntent();
                intent.putExtra("bitmap", datas);
                setResult(RESULT_OK, intent);
                finish();
                break;
            default:
                break;
        }
    }
}
