package com.demo.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.demo.adapter.ADPagerAdapter;
import com.demo.configs.ConstantsME;
import com.demo.configs.EventBusTag;
import com.demo.demo.R;
import com.demo.entity.UpdateInfo;
import com.demo.service.CheckUpdateService;
import com.framework.Utils.DownLoadManagerUtils;
import com.framework.Utils.DownLoadObserver;
import com.framework.Utils.PackageManagerUtil;
import com.framework.Utils.PreferencesHelper;
import com.framework.Utils.RequestPermissionsUtil;
import com.framework.Utils.ScreenUtils;
import com.framework.Utils.ToastUtil;
import com.framework.Utils.Y;
import com.framework.customviews.WholeNotification;
import com.framework2.utils.PicToastUtil;
import com.framework2.dialog.UpdateDialog;
import com.framework2.okhttp3.Ok3Util;
import com.framework2.okhttp3.StringRequest;
import com.framework2.tinker.app.TinkerMainActivity;
import com.test.MainActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Response;

/**
 * @author Yobert Jomi
 *         className HomePageActivity
 *         created at  2016/10/15  16:40
 */
public class HomePageActivity extends BaseSlideFinishActivity implements ActivityCompat.OnRequestPermissionsResultCallback
{
    private final ProgressHandler progressHandler = new ProgressHandler(this);
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.ivGlide)
    ImageView ivGlide;
    @BindView(R.id.btnRefreshTest)
    Button btnRefreshTest;
    @BindView(R.id.btnTinker)
    Button btnTinker;
    @BindView(R.id.btnLogout)
    Button btnLogout;
    @BindView(R.id.btnGuide)
    Button btnGuide;

    private Intent serviceIntent;
    private String updateContent;
    private String downLoadUrl;
    private String version;

    //eventBus通知新消息
    @Subscribe(threadMode = ThreadMode.MAIN, tag = EventBusTag.updateDialog)
    public void eventBusUpdate(UpdateInfo info)
    {
        if (info != null)
        {
            updateContent = info.getUpdateContent();
            downLoadUrl = info.getDownLoadUrl();
            version = info.getVersion();
            new UpdateDialog(HomePageActivity.this).setUpdate(true).setContent(updateContent).setDownLoadUrl(downLoadUrl).setVersion(version).showDialog(progressHandler);
            EventBus.getDefault().unregister(this);
            if (null != serviceIntent)
            {
                stopService(serviceIntent);
                serviceIntent = null;
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void _onCreate()
    {
        setContentView(R.layout.activity_homepage_setcontentview);
        ButterKnife.bind(this);
        //        		ActivityHomepageBinding viewDataBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.activity_homepage, null, true);
//        ActivityHomepageBinding viewDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_homepage);
//        DataBindingItem item = new DataBindingItem();
//        item.title.set("我来自dataBinding:\n" + getResources().getText(R.string.content));
//        viewDataBinding.setDataBindingItem(item);
//        viewDataBinding.setMyHandler(new MyHandlers(this));

        serviceIntent = new Intent(this, CheckUpdateService.class);
        startService(serviceIntent);
        //		UpdateUtil.getInstanse().requestUpdateVersion(HomePageActivity.this, progressHandler, false);//检查版本更新
        JSONObject jsonObject = new JSONObject();
        try
        {
            jsonObject.put("is_printed", "1");
            jsonObject.put("_time", "1480408429");
            jsonObject.put("send_place", "前台");
            jsonObject.put("foods",
                    "[{\"goods_id\":\"c27ef2ab-5dd3-4310-a87d-f04200ea3ab3\",\"type\":\"goods\",\"quantity\":1,\"is_present\":0,\"order_time\":\"2016-11-29 16:33:49\",\"goods\":[]}]");
            jsonObject.put("_sign", "08f920da319c3d888577d62a5fe60c46");
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
        StringRequest jsonRequest = new StringRequest.Builder().url("http://test.branch.k.haochang.tv/api/outer/goods").postString_json(jsonObject.toString())
                .build(new Callback()
                {
                    @Override
                    public void onFailure(Call call, IOException e)
                    {
                        Y.y("JSONRequestonFailure:" + e.getMessage());
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException
                    {
                        Y.y("JSONRequestonResponse:" + response.body().string());
                    }
                });
        Ok3Util.getInstance().setBuilder(new OkHttpClient.Builder()
//                .cookieJar(new CookiesManager(context))
//                .cookieJar(new AllCookieJar(this))
                .connectTimeout(15000, TimeUnit.MILLISECONDS).retryOnConnectionFailure(false)).addToRequestQueueAsynchoronous(jsonRequest);
        viewPager();
        ImageView ivGlide = (ImageView) findViewById(R.id.ivGlide);
        Glide.with(this).setDefaultRequestOptions(RequestOptions.priorityOf(Priority.HIGH)).load("http://img.my.csdn.net/uploads/201508/05/1438760757_3588.jpg")
               .into(ivGlide);
    }

    @SuppressWarnings("unchecked")
    private void viewPager()
    {
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        ArrayList list = new ArrayList();
        for (int i = 0; i < 3; i++)
        {
            View view = LayoutInflater.from(this).inflate(R.layout.allview_auto_data, null);
            ((TextView) view.findViewById(R.id.tv_loadstate)).setText("我的位置:" + i);
            list.add(view);
        }
        ADPagerAdapter adapter = new ADPagerAdapter(list);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(Integer.MAX_VALUE / 2);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        progressHandler.removeCallbacksAndMessages(null);
    }


    @OnClick({R.id.btnRefreshTest, R.id.btnTinker, R.id.btnLogout, R.id.btnGuide, R.id.btnEmoji, R.id.btnUcrop})
    public void onViewClicked(View view)
    {
        switch (view.getId())
        {
            case R.id.btnRefreshTest:
                startActivity(MainActivity.class);
                break;
            case R.id.btnTinker:
                startActivity(TinkerMainActivity.class);
                break;
            case R.id.btnLogout:
                Intent intent = new Intent(this, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
            case R.id.btnGuide:
                PreferencesHelper.getInstance().putInfo(ConstantsME.NOTFIRSTIN, false);
                PicToastUtil.getInstance().showPicToast(this, "下次启动可见Guide");
                break;
            case R.id.btnEmoji:
                startActivity(EmojiDetailActivity.class);
                break;
            case R.id.btnUcrop:
                startActivity(EmojiDetailActivity.class);
                break;
        }
    }


    /**
     * 防止内存泄露handler,destroy需要置空消息
     */
    private static class ProgressHandler extends Handler
    {
        private final WeakReference<HomePageActivity> weakReference;

        ProgressHandler(HomePageActivity activity)
        {
            weakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            if (msg.what == DownLoadObserver.MESSAGE_PROGRESS)
            {
                int progress = msg.arg1;
                Y.y("下载中：" + progress);
                if (progress >= 100)
                {
                    String path = (String) msg.obj;
                    if (!TextUtils.isEmpty(path))
                    {
                        PackageManagerUtil.getInstance().installApk(weakReference.get(), path);
                    }
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Y.y("Homepage--onRequestPermissionsResult:" + requestCode);
        if (requestCode == RequestPermissionsUtil.PERMISSION_WRITE_READ_EXTERNAL_STORAGE)
        {
            if ((grantResults.length > 1) && (grantResults[0] == PackageManager.PERMISSION_GRANTED) && (grantResults[1] == PackageManager.PERMISSION_GRANTED))
            {
                if (0 == DownLoadManagerUtils.getInstance().requestDownLoad(HomePageActivity.this, Environment.DIRECTORY_DOWNLOADS,
                        downLoadUrl,
                        getResources().getString(R.string.app_name)
                                + version + ".apk",
                        getResources().getString(R.string.app_name) + "新版本"
                                + version, getResources().getString(R.string.app_name) + "新版本"
                                + version))
                {
                    if (null != progressHandler)
                    {
                        //监听下载进度条
                        getContentResolver().registerContentObserver(Uri.parse("content://downloads/"), true, new DownLoadObserver(progressHandler, this,
                                PreferencesHelper.getInstance().getLongData(PreferencesHelper.getInstance().getStringData(DownLoadManagerUtils.DownLoad_FileName))));
                    }
                    PicToastUtil.getInstance().showPicToast(HomePageActivity.this, "正在下载安装包，请稍后");
                }
            } else
            {
                ToastUtil.getInstance().showToast("存储权限未开启，请手动开启");
//                requestPermissionMannual = true;
                RequestPermissionsUtil.getInstance().showCurrentAppDetailSettingIntent(HomePageActivity.this);
            }
        } else if (requestCode == RequestPermissionsUtil.PERMISSION_LOCATION)
        {
            if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED))
            {
                Y.y("定位权限：initLocation_gaode");
                if (TextUtils.isEmpty(PreferencesHelper.getInstance().getStringData(ConstantsME.LATITUDE)))
                {
                    Y.y("这里要获取定位");
                } else
                {
                    Y.y("定位权限：initLocation_gaode:不为空");
                }
            } else
            {
                ToastUtil.getInstance().showToast("未能获取定位权限，将会导致无法定位!");
            }
        }
    }

    private WholeNotification wholeNotification;
    private long exitTime;

    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        Y.y("再按一次:");
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN)
        {
            if (System.currentTimeMillis() - this.exitTime > 2000L)
            {
                this.exitTime = System.currentTimeMillis();
                View view = LayoutInflater.from(this).inflate(R.layout.layout_whole_notification, null);
                View v_state_bar = view.findViewById(R.id.v_state_bar);
                ViewGroup.LayoutParams layoutParameter = v_state_bar.getLayoutParams();
                layoutParameter.height = ScreenUtils.getInstance().getStatusBarHeightPx(this);
                v_state_bar.setLayoutParams(layoutParameter);
                ((TextView) view.findViewById(R.id.tv_content)).setText(this.getResources().getString(R.string.exist) + this.getResources().getString(R.string.app_name));
                wholeNotification = new WholeNotification.Builder().setContext(HomePageActivity.this)
                        .setView(view)
                        .setMonitorTouch(false)
                        .build();
                wholeNotification.show();
                return false;
            } else
            {
                wholeNotification.dismiss();
                // 返回主界面
                /**
                 * 退出登录,清空数据
                 */
                Intent intent_home = new Intent(Intent.ACTION_MAIN);
                intent_home.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent_home.addCategory(Intent.CATEGORY_HOME);
                startActivity(intent_home);
//                finish();
//                System.exit(0);
                ////////////////////
                int pid = android.os.Process.myPid();
                android.os.Process.killProcess(pid);
            }
            return true;
        } else
        {
            return super.onKeyDown(keyCode, event);
        }
    }
}
