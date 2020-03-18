package com.demo.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.demo.activity.az.AzActivity;
import com.demo.activity.calendar.CalendarActivity;
import com.demo.activity.cameraFilter.CameraFilterActivity;
import com.demo.activity.cloudtags.CloudTagsActivity;
import com.demo.activity.crop.CropSampleActivity;
import com.demo.activity.foldmenu.FolderMenuActivity;
import com.demo.activity.netty_test.NettyTestActivity;
import com.demo.activity.slidingDrawer.SlidingDrawerActivity;
import com.demo.activity.tasksCompleted.TasksCompletedActivity;
import com.demo.adapter.ADPagerAdapter;
import com.demo.commonWebview.DetachedProcessCommonFullWebViewActivity;
import com.demo.configs.ConstantsME;
import com.demo.configs.EventBusTag;
import com.demo.demo.R;
import com.demo.entity.UpdateInfo;
import com.demo.service.CheckUpdateService;
import com.framework.application.ProxyApplication;
import com.framework.security.AesUtil;
import com.framework.security.Base64Coder;
import com.framework.security.RSAutil;
import com.framework.security.SecurityManagerUtil;
import com.framework.util.DownLoadManagerUtils;
import com.framework.util.DownLoadObserver;
import com.framework.util.FileUtil;
import com.framework.util.PackageManagerUtil;
import com.framework.util.PreferencesHelper;
import com.framework.util.RequestPermissionsUtil;
import com.framework.util.ScreenUtil;
import com.framework.util.ToastUtil;
import com.framework.util.Y;
import com.framework.util.multyprocessprovider.provider.PreferencesUtil;
import com.framework.widget.WholeNotification;
import com.framework2.dialog.UpdateDialog;
import com.framework2.okhttp3.Ok3Util;
import com.framework2.okhttp3.StringRequest;
import com.framework2.okhttp3.upload_dowload.DownloadBrokenUtil;
import com.framework2.okhttp3.upload_dowload.ProgressRequestBody;
import com.framework2.okhttp3.upload_dowload.ProgressResponseBody;
import com.framework2.tinker.app.TinkerMainActivity;
import com.framework2.utils.PicToastUtil;
import com.library.adapter_recyclerview.HorizontalDividerItemDecoration2;
import com.library.adapter_recyclerview.UniversalAdapter;
import com.library.androidvideocache.Utils;
import com.test.MainActivity;
import com.testactivity.ScanActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import custom.org.greenrobot.eventbus.EventBus;
import custom.org.greenrobot.eventbus.Subscribe;
import custom.org.greenrobot.eventbus.ThreadMode;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * @author Yobert Jomi
 * className HomePageActivity
 * created at  2016/10/15  16:40
 */
public class HomePageActivity extends BaseAbsSlideFinishActivity implements ActivityCompat
        .OnRequestPermissionsResultCallback {
    private final ProgressHandler progressHandler = new ProgressHandler(this);
    private final int CHOOSE_VIDEO_RESULT = 10086;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.ivGlide)
    ImageView ivGlide;
    @BindView(R.id.ivGif5)
    ImageView ivGif5;
    @BindView(R.id.ivGif4)
    ImageView ivGif4;
    @BindView(R.id.ivGif3)
    ImageView ivGif3;
    @BindView(R.id.ivGif2)
    ImageView ivGif2;
    @BindView(R.id.ivGif)
    ImageView ivGif;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    int countTemp = 0;
    private Intent serviceIntent;
    private String updateContent;
    private String downLoadUrl;
    private String version;
    private WholeNotification wholeNotification;
    private long exitTime;

    //eventBus通知新消息
    @Subscribe(threadMode = ThreadMode.MAIN, tag = EventBusTag.updateDialog)
    public void eventBusUpdate(UpdateInfo info) {
        if (info != null) {
            updateContent = info.getUpdateContent();
            downLoadUrl = info.getDownLoadUrl();
            version = info.getVersion();
            new UpdateDialog(HomePageActivity.this).setUpdate(true).setContent(updateContent).setDownLoadUrl
                    (downLoadUrl).setVersion(version).showDialog(progressHandler);
            EventBus.getDefault().unregister(this);
            if (null != serviceIntent) {
                stopService(serviceIntent);
                serviceIntent = null;
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void _onCreate() {
        setContentView(R.layout.activity_homepage_setcontentview);
        ButterKnife.bind(this);
        //        		ActivityHomepageBinding viewDataBinding = DataBindingUtil.inflate
        //        		(LayoutInflater.from(this), R
        // .layout.activity_homepage, null, true);
//        ActivityHomepageBinding viewDataBinding = DataBindingUtil.setContentView(this, R.layout
//        .activity_homepage);
//        DataBindingItem item = new DataBindingItem();
//        item.title.set("我来自dataBinding:\n" + getResources().getText(R.string.content));
//        viewDataBinding.setDataBindingItem(item);
//        viewDataBinding.setMyHandler(new MyHandlers(this));
        String ddd=Base64Coder.encodeString("我是encodeString");
        Log.e("HomePageActivity enc:", ddd);
        Log.e("HomePageActivity dec:", Base64Coder.decodeString(ddd));
        Log.e("HomePageActivity Aes:", AesUtil.getInstance().generateKey());
        PreferencesUtil.getInstance().putString("test", "testvalue");
        Log.e("HomePageActivity我是跨进程:", PreferencesUtil.getInstance().getString("test"));
        ToastUtil.getInstance().showToast("我是跨进程数据操作：" + PreferencesUtil.getInstance().getString(
                "test"));
        try {
            SecurityManagerUtil.getInstance().put(ProxyApplication.getProxyApplication(), "sec",
                    "我是加密");
            Log.e("HomePageActivity我是加密:",
                    SecurityManagerUtil.getInstance().get(ProxyApplication.getProxyApplication(),
                            "sec"));
            Log.e("HomePageActivity", "encrypt-AA:" + RSAutil.getInstance().encryptData(
                    "18725618900"));
            Log.e("HomePageActivity",
                    "decrypt-AA:" + RSAutil.getInstance().decryptData(RSAutil.getInstance().encryptData("18725618900")));
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("HomePageActivity我是加密e:", e.getMessage());
        }
        ToastUtil.getInstance().showToast("我是跨进程数据操作--加密：" + SecurityManagerUtil.getInstance().get(this, "sec"));
        serviceIntent = new Intent(this, CheckUpdateService.class);
        startService(serviceIntent);
        //		UpdateUtil.getInstanse().requestUpdateVersion(HomePageActivity.this,
        //		progressHandler, false);//检查版本更新
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("is_printed", "1");
            jsonObject.put("_time", "1480408429");
            jsonObject.put("send_place", "前台");
            jsonObject.put("foods",
                    "[{\"goods_id\":\"c27ef2ab-5dd3-4310-a87d-f04200ea3ab3\",\"type\":\"goods\"," +
                            "\"quantity\":1," +
                            "\"is_present\":0,\"order_time\":\"2016-11-29 16:33:49\"," +
                            "\"goods\":[]}]");
            jsonObject.put("_sign", "08f920da319c3d888577d62a5fe60c46");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        StringRequest jsonRequest =
                new StringRequest.Builder().url("http://www.baidu.com").postString_json
                        (jsonObject.toString())
                        .build(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e1) {
                                    e1.printStackTrace();
                                }
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {

                            }
                        });
//        for (int i = 0; i < 500; i++)
        Ok3Util.getInstance().setBuilder(new OkHttpClient.Builder()
//                .cookieJar(new CookiesManager(context))
//                .cookieJar(new AllCookieJar(this))
                .connectTimeout(15000, TimeUnit.MILLISECONDS).retryOnConnectionFailure(false))
                .addToRequestQueueAsynchoronous(false, jsonRequest);
        viewPager();
        Glide.with(this).setDefaultRequestOptions(RequestOptions.priorityOf(Priority.HIGH)).load(
                "http://img.my.csdn" +
                        ".net/uploads/201508/05/1438760757_3588.jpg")
                .into(ivGlide);
        Glide.with(this).setDefaultRequestOptions(RequestOptions.priorityOf(Priority.HIGH).diskCacheStrategy
                (DiskCacheStrategy.ALL)).load(R.drawable.gif).into(ivGif);
        Glide.with(this).setDefaultRequestOptions(RequestOptions.priorityOf(Priority.HIGH).diskCacheStrategy
                (DiskCacheStrategy.ALL)).load(R.drawable.abcd).into(ivGif2);
        Glide.with(this).setDefaultRequestOptions(RequestOptions.priorityOf(Priority.HIGH).diskCacheStrategy
                (DiskCacheStrategy.ALL)).load(R.mipmap.fun).into(ivGif3);
        Glide.with(this).setDefaultRequestOptions(RequestOptions.priorityOf(Priority.HIGH).diskCacheStrategy
                (DiskCacheStrategy.ALL)).load(R.mipmap.dog).into(ivGif4);

        Glide.with(this).setDefaultRequestOptions(RequestOptions.priorityOf(Priority.HIGH)
                .placeholder(R.mipmap.icon_qq)
                .error(R.mipmap.icon_qq)
                .diskCacheStrategy(DiskCacheStrategy.ALL))
                .load(R.mipmap.beauty)
                .into(ivGif5);
        initRecyclerView();
    }

    @Override
    public void onSlideClose() {
        finishActivity();
    }

    @Override
    public int[] initPrimeryColor() {
//        return new int[]{R.color.colorPrimary,R.color.colorPrimaryDark};
        return null;
    }

    private void initRecyclerView() {
        List<String> items = new ArrayList<>();
        items.add("刷新测试");
        items.add("Tinker测试");
        items.add("退出登录");
        items.add("清除引导标识");
        items.add("聊骚表情");
        items.add("图片裁剪");
        items.add("视频测试");
        items.add("视频测试--清除缓存");
        items.add("图像滤镜");
        items.add("Netty测试");
        items.add("Calendar");
        items.add("展开书签");
        items.add("TasksCompletedProgress");
        items.add("SlidingDrawer");
        items.add("跳转设置");
        items.add("云标签");
        items.add("A--Z");
        items.add("LocalHost");
        items.add("文件上传");
        items.add("文件下载");
        items.add("独立进程webview");
        items.add("zxing二维码(优化)");
        items.add("权限页面");
        items.add("权限页面(APIGuide)");
        HorizontalDividerItemDecoration2 itemDecoration =
                new HorizontalDividerItemDecoration2(this);
        itemDecoration.setColor(this, R.color.share_texttoast_color);
        itemDecoration.setDividerHeightPx(1);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));//（与scrollview嵌套）
        recyclerView.setAdapter(new UniversalAdapter<String>(this, R.layout.test_list_item_layout
                , items) {
            @Override
            protected void getItemView(UniversalViewHolder universalViewHolder, final String str,
                                       final int i) {
                universalViewHolder.setText(R.id.tv, TextUtils.isEmpty(str) ? "null" : str);
                universalViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (str) {
                            case "刷新测试":
                                EventBus.getDefault().post("我的", "test2");
                                startActivity(MainActivity.class);
                                break;
                            case "Tinker测试":
                                startActivity(TinkerMainActivity.class);
                                break;
                            case "退出登录":
                                Intent intent = new Intent(HomePageActivity.this,
                                        LoginActivity.class).setFlags
                                        (Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                break;
                            case "清除引导标识":
                                PreferencesHelper.getInstance().putInfo(ConstantsME.NOTFIRSTIN,
                                        false);
                                PicToastUtil.getInstance().showPicToast(HomePageActivity.this,
                                        "下次启动可见Guide");
                                break;
                            case "聊骚表情":
                                startActivity(EmojiDetailActivity.class);
                                break;
                            case "图片裁剪":
                                startActivity(CropSampleActivity.class);
                                break;
                            case "视频测试":
                                startActivity(VideoTestActivity.class);
                                break;
                            case "视频测试--清除缓存":
                                try {
                                    Utils.cleanVideoCacheDir(HomePageActivity.this);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                break;
                            case "图像滤镜":
                                PicToastUtil.getInstance().showPicToast(HomePageActivity.this,
                                        "先申请权限");
                                startActivity(CameraFilterActivity.class);
                                break;
                            case "Netty测试"://netty测试
                                startActivity(NettyTestActivity.class);
                                break;
                            case "Calendar"://calendar
                                startActivity(CalendarActivity.class);
                                break;
                            case "展开书签"://展开书签
                                startActivity(FolderMenuActivity.class);
                                break;
                            case "TasksCompletedProgress"://TasksCompletedProgress
                                startActivity(TasksCompletedActivity.class);
                                break;
                            case "SlidingDrawer"://SlidingDrawer
                                startActivity(SlidingDrawerActivity.class);
                                break;
                            case "跳转设置"://设置
                                RequestPermissionsUtil.getInstance().showCurrentAppDetailSettingIntent
                                        (HomePageActivity.this);
                                break;
                            case "云标签":
                                startActivity(CloudTagsActivity.class);
                                break;
                            case "A--Z":
                                startActivity(AzActivity.class);
                                break;
                            case "LocalHost":
                                JSONObject jsonObject = new JSONObject();
                                try {
                                    jsonObject.put("userName", "root");
                                    jsonObject.put("passWord", "123456");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                StringRequest jsonRequest = new StringRequest.Builder().url
                                        ("http://192.168.1.105:8080/postUserList").postString_json(jsonObject
                                        .toString())
                                        .build(new Callback() {
                                            @Override
                                            public void onFailure(Call call, IOException e) {
                                                Y.y("JSONRequestonFailure:" + e.getMessage());
                                            }

                                            @Override
                                            public void onResponse(Call call, Response response) throws IOException {
                                                String aa = response.body().string();
                                                Y.y("JSONRequestonResponse:" + aa);
                                                ToastUtil.getInstance().showToast("返回:" + aa);
                                            }
                                        });
                                Ok3Util.getInstance().setBuilder(new OkHttpClient.Builder()
//                .cookieJar(new CookiesManager(context))
//                .cookieJar(new AllCookieJar(this))
                                        .connectTimeout(8000, TimeUnit.MILLISECONDS).retryOnConnectionFailure(false))
                                        .addToRequestQueueAsynchoronous(false, jsonRequest);
                                break;
                            case "文件上传":
                                if (!RequestPermissionsUtil.getInstance().checkPermissionsThenRequest
                                        (HomePageActivity.this, new String[]{Manifest.permission
                                                        .WRITE_EXTERNAL_STORAGE, Manifest.permission
                                                        .READ_EXTERNAL_STORAGE},
                                                RequestPermissionsUtil.PERMISSION_WRITE_READ_EXTERNAL_STORAGE)) {
                                    chooseVideo();
                                }
                                break;
                            case "文件下载":
                                File file = new File(getPackageResourcePath());
                                file = new File(Environment.getExternalStoragePublicDirectory(Environment
                                        .DIRECTORY_DOWNLOADS).getPath() + "/yayya.apk");
                                new DownloadBrokenUtil("http://assets.geilicdn" +
                                        ".com/channelapk/1000n_shurufa_1.9.6.apk", file,
                                        new ProgressResponseBody.ProgressListener() {
                                            @Override
                                            public void onPreExecute(long contentLength) {
                                                Y.y("准备下载：" + contentLength);
                                            }

                                            @Override
                                            public void update(long totalBytes, boolean done) {
                                                Y.y("更新进度：" + totalBytes + "              " + done);
                                            }
                                        }).download(0);
                                break;
                            case "独立进程webview":
                                Bundle b = new Bundle();
                                b.putString(ConstantsME.title, "独立进程WEBVIEW--TITLE");
                                b.putString(ConstantsME.url, "http://www.baidu.com");
                                startActivity(DetachedProcessCommonFullWebViewActivity.class, b);
                                break;
                            case "zxing二维码(优化)":
                                startActivityForResult(ScanActivity.class, null, 10086);
                                break;
                            case "权限页面":
                                startActivity(PermissionActivity.class);
                                break;
                            case "权限页面(APIGuide)":
                                startActivity(PermissionApiGuideActivity.class);
                                break;
                            default:
                                break;
                        }
                    }
                });
            }
        });
    }

    private void chooseVideo() {
//        Intent intent = new Intent();
//        intent.setType("video/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        intent.addCategory(Intent.CATEGORY_OPENABLE);
//        startActivityForResult(intent, CHOOSE_VIDEO_RESULT);
        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, CHOOSE_VIDEO_RESULT);
    }

    @SuppressWarnings("unchecked")
    private void viewPager() {
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        ArrayList list = new ArrayList();
        for (int i = 0; i < 3; i++) {
            View view = LayoutInflater.from(this).inflate(R.layout.allview_auto_data, null);
            ((TextView) view.findViewById(R.id.tv_loadstate)).setText("我的位置:" + i);
            list.add(view);
        }
        ADPagerAdapter adapter = new ADPagerAdapter(list);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(Integer.MAX_VALUE / 2);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        progressHandler.removeCallbacksAndMessages(null);
    }

    private void upLoadVideo(String path) {
        String currentApkPath = TextUtils.isEmpty(path) ?
                getApplicationContext().getPackageResourcePath() : path;
        File apkFile = new File(currentApkPath);
//        String url = "http://www.baidu.com";
        String url = "http://124.207.3.100:8084/openApi/sdkApi/upload/";
        MultipartBody.Builder bodyBuilder = new MultipartBody.Builder();
        bodyBuilder.setType(MultipartBody.FORM);
        ToastUtil.getInstance().showToast("文件名：" + apkFile.getName());
//        bodyBuilder.addFormDataPart("file", apkFile.getName(), RequestBody.create(MediaType.parse
// ("application/octet-stream"), apkFile));
        bodyBuilder.addFormDataPart("type", "1");
        bodyBuilder.addFormDataPart("file", apkFile.getName(),
                RequestBody.create(MediaType.parse("video/mpeg"),
                        apkFile));
        ProgressRequestBody progressRequestBody = new ProgressRequestBody(bodyBuilder.build(), new
                ProgressRequestBody.ProgressListener() {
                    @Override
                    public void update(long totalWritedBytes, long len, boolean done) {
                        Y.y("上传文件：" + totalWritedBytes + "       " + len + "    " + done);
                    }
                });
        StringRequest uploadRequet = new StringRequest.Builder().url(url)
                .postRequestBody(progressRequestBody)
//                .postRequestBody(multipartBody)
                .build(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Y.y("JSONRequestonFailure:" + e.getMessage());
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String aa = response.body().string();
                        Y.y("JSONRequestonResponse:" + aa);
                        ToastUtil.getInstance().showToast("返回:" + aa);
                    }
                });
//        for (int i = 0; i < 500; i++)
        Ok3Util.getInstance().setBuilder(new OkHttpClient.Builder()
//                .cookieJar(new CookiesManager(context))
//                .cookieJar(new AllCookieJar(this))
                .writeTimeout(38000, TimeUnit.MILLISECONDS)
                .readTimeout(38000, TimeUnit.MILLISECONDS)
                .connectTimeout(38000, TimeUnit.MILLISECONDS).retryOnConnectionFailure(false))
                .addToRequestQueueAsynchoronous(true, uploadRequet);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && requestCode == CHOOSE_VIDEO_RESULT && resultCode == RESULT_OK) {
            Log.e("yy", "onActivityResult:" + data.getDataString());
            Uri uri = data.getData();
            if (Build.VERSION.SDK_INT >= 19) {
                String path = FileUtil.getInstance().getRealFilePath(this, data.getData());
                Log.e("yy", "pathpath=" + path);
            }
            Cursor cursor = getContentResolver().query(uri, null, null,
                    null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                String imgNo = cursor.getString(0); // 编号
                String v_path = cursor.getString(1); // 文件路径
                String v_name = cursor.getString(2); // 文件名
                String v_size = cursor.getString(3); // 大小
                String v_name4 = cursor.getString(4); // 文件名
                String v_name5 = cursor.getString(5); // 文件名
                cursor.close();
                Log.e("yy", "No=" + imgNo);
                Log.e("yy", "v_path=" + v_path);
                Log.e("yy", "v_size=" + v_size);
                Log.e("yy", "v_name=" + v_name);
                Log.e("yy", "v_name4=" + v_name4);
                Log.e("yy", "v_name5=" + v_name5);
//                upLoadVideo(((countTemp++) % 2 == 0) ? v_path : null);
                upLoadVideo(v_path);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[]
                                                   grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Y.y("Homepage--onRequestPermissionsResult:" + requestCode);
        if (requestCode == RequestPermissionsUtil.PERMISSION_WRITE_READ_EXTERNAL_STORAGE) {
            if ((grantResults.length > 1) && (grantResults[0] == PackageManager.PERMISSION_GRANTED) &&
                    (grantResults[1] == PackageManager.PERMISSION_GRANTED)) {
                if (0 == DownLoadManagerUtils.getInstance().requestDownLoad(HomePageActivity.this
                        , Environment
                                .DIRECTORY_DOWNLOADS,
                        downLoadUrl,
                        getResources().getString(R.string.app_name)
                                + version + ".apk",
                        getResources().getString(R.string.app_name) + "新版本"
                                + version, getResources().getString(R.string.app_name) + "新版本"
                                + version)) {
                    if (null != progressHandler) {
                        //监听下载进度条
                        getContentResolver().registerContentObserver(Uri.parse("content" +
                                "://downloads/"), true, new
                                DownLoadObserver(progressHandler, this,
                                PreferencesHelper.getInstance().getLongData(PreferencesHelper.getInstance()
                                        .getStringData(DownLoadManagerUtils.DownLoad_FileName))));
                    }
                    PicToastUtil.getInstance().showPicToast(HomePageActivity.this, "正在下载安装包，请稍后");
                }
            } else {
                ToastUtil.getInstance().showToast("存储权限未开启，请手动开启");
//                requestPermissionMannual = true;
                RequestPermissionsUtil.getInstance().showCurrentAppDetailSettingIntent(HomePageActivity.this);
            }
        } else if (requestCode == RequestPermissionsUtil.PERMISSION_LOCATION) {
            if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                Y.y("定位权限：initLocation_gaode");
                if (TextUtils.isEmpty(PreferencesHelper.getInstance().getStringData(ConstantsME.LATITUDE))) {
                    Y.y("这里要获取定位");
                } else {
                    Y.y("定位权限：initLocation_gaode:不为空");
                }
            } else {
                ToastUtil.getInstance().showToast("未能获取定位权限，将会导致无法定位!");
            }
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Y.y("再按一次:" + System.currentTimeMillis());
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (System.currentTimeMillis() - this.exitTime > 2000L) {
                this.exitTime = System.currentTimeMillis();
                View view = LayoutInflater.from(this).inflate(R.layout.layout_whole_notification,
                        null);
                View v_state_bar = view.findViewById(R.id.v_state_bar);
                ViewGroup.LayoutParams layoutParameter = v_state_bar.getLayoutParams();
                layoutParameter.height = ScreenUtil.getInstance().getStatusBarHeightPx(this);
                v_state_bar.setLayoutParams(layoutParameter);
                ((TextView) view.findViewById(R.id.tv_content)).setText(this.getResources().getString(R.string.exist)
                        + this.getResources().getString(R.string.app_name));
                wholeNotification =
                        new WholeNotification.Builder().setContext(HomePageActivity.this)
                                .setView(view)
                                .setMonitorTouch(true)
                                .build();
                wholeNotification.show();
                return false;
            } else {
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
                int pid = Process.myPid();
                Process.killProcess(pid);
            }
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    /**
     * 防止内存泄露handler,destroy需要置空消息
     */
    private static class ProgressHandler extends Handler {
        private final WeakReference<HomePageActivity> weakReference;

        ProgressHandler(HomePageActivity activity) {
            weakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == DownLoadObserver.MESSAGE_PROGRESS) {
                int progress = msg.arg1;
                Y.y("下载中：" + progress);
                if (progress >= 100) {
                    String path = (String) msg.obj;
                    if (!TextUtils.isEmpty(path)) {
                        PackageManagerUtil.getInstance().installApk(weakReference.get(), path, "");
                    }
                }
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, tag = "test2")
    public void eventTest2(String test2) {
        ToastUtil.getInstance().showToast("我是测试222222");
    }

    @Subscribe(threadMode = ThreadMode.MAIN, tag = "test1")
    public void eventTest1(String test) {
        ToastUtil.getInstance().showToast("我是测试1");
    }
}
