package com.library.camerafilter;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.library.camerafilter.model.Constants;
import com.libray.camerafilter.R;
import com.muzhi.camerasdk.library.utils.MResource;
import com.muzhi.camerasdk.library.utils.PhotoEnhance;


public class PhotoEnhanceActivity extends BaseActivity implements OnSeekBarChangeListener
{

    private ImageView mCropView;
    private TextView btn_done;

    private SeekBar seekbar_brightness, seekbar_contrast, seekbar_saturation; //饱和,色相,亮度

    private RadioButton button_brightness, button_contrast, button_saturation;
    private int button_brightnessId, button_contrastId, button_saturationId;
    private RadioGroup layout_tab;

    private Bitmap sourceMap;

    private PhotoEnhance mPhotoEnhance = null;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.camerasdk_activity_enhance);

        showLeftIcon();
        setActionBarTitle("调整");

        mCropView = (ImageView) findViewById(R.id.cropImageView);
        btn_done = (TextView) findViewById(R.id.camerasdk_title_txv_right_text);
        btn_done.setVisibility(View.VISIBLE);
        btn_done.setText("确定");


        seekbar_brightness = (SeekBar) findViewById(R.id.seekBar1);
        seekbar_contrast = (SeekBar) findViewById(R.id.seekBar2);
        seekbar_saturation = (SeekBar) findViewById(R.id.seekBar3);

        button_brightnessId = MResource.getIdRes(mContext, "button_brightness");
        button_contrastId = MResource.getIdRes(mContext, "button_contrast");
        button_saturationId = MResource.getIdRes(mContext, "button_saturation");

        button_brightness = (RadioButton) findViewById(button_brightnessId);
        button_contrast = (RadioButton) findViewById(button_contrastId);
        button_saturation = (RadioButton) findViewById(button_saturationId);

        layout_tab = (RadioGroup) findViewById(MResource.getIdRes(mContext, "layout_tab"));

        sourceMap = Constants.bitmap;
        mCropView.setImageBitmap(sourceMap);
        initEvent();
    }


    private void initEvent()
    {
        seekbar_brightness.setOnSeekBarChangeListener(this);
        seekbar_contrast.setOnSeekBarChangeListener(this);
        seekbar_saturation.setOnSeekBarChangeListener(this);

        mPhotoEnhance = new PhotoEnhance(sourceMap);

        layout_tab.setOnCheckedChangeListener(new OnCheckedChangeListener()
        {

            @Override
            public void onCheckedChanged(RadioGroup arg0, int arg1)
            {
                if (arg1 == button_brightnessId)
                {
                    seekbar_brightness.setVisibility(View.VISIBLE);
                    seekbar_contrast.setVisibility(View.GONE);
                    seekbar_saturation.setVisibility(View.GONE);
                } else if (arg1 == button_contrastId)
                {
                    seekbar_brightness.setVisibility(View.GONE);
                    seekbar_contrast.setVisibility(View.VISIBLE);
                    seekbar_saturation.setVisibility(View.GONE);
                } else if (arg1 == button_saturationId)
                {
                    seekbar_brightness.setVisibility(View.GONE);
                    seekbar_contrast.setVisibility(View.GONE);
                    seekbar_saturation.setVisibility(View.VISIBLE);
                }
            }
        });

        btn_done.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                done();
            }
        });
    }

    //拖动值监听
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromTouch)
    {
        int type = 0;
        int vid = seekBar.getId();

        if (vid == R.id.seekBar1)
        {
            //亮度
            mPhotoEnhance.setBrightness(progress);
            type = mPhotoEnhance.Enhance_Brightness;
        } else if (vid == R.id.seekBar2)
        {
            //对比度
            mPhotoEnhance.setContrast(progress);
            type = mPhotoEnhance.Enhance_Contrast;
        } else if (vid == R.id.seekBar3)
        {
            //饱和度
            mPhotoEnhance.setContrast(progress);
            type = mPhotoEnhance.Enhance_Contrast;
        }

        sourceMap = mPhotoEnhance.handleImage(type);
        mCropView.setImageBitmap(sourceMap);

    }

    public void onStartTrackingTouch(SeekBar seekBar)
    {
    }

    //SeekBar 停止拖动
    public void onStopTrackingTouch(SeekBar seekBar)
    {
    }

    private void done()
    {

        Constants.bitmap = sourceMap;
        setResult(Constants.RequestCode_Croper);
        finish();
    }
}
