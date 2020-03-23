package com.demo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import com.demo.demo.R;
import com.library.permission.Utils;
import com.library.permission.UtilsWithPermission;

public class PermissionActivity extends BaseActivity {
    private static final int REQUEST_CODE_CONTACT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);
        findViewById(R.id.bt_call).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UtilsWithPermission.makeCall(PermissionActivity.this, "10086");
//                makeCall();
            }
        });
        findViewById(R.id.bt_choose_contact).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseContact();
            }
        });
    }

//    public void makeCall() {
//        Permission.getProxyApplication()
//                .checkAndRequestPermission(Manifest.permission.CALL_PHONE, new
//                CheckRequestPermissionListener() {
//                    @Override
//                    public void onPermissionGranted(Permission permission) {
//                        Utils.makeCall(AfterActivity.this, "10086");
//                    }
//
//                    @Override
//                    public void onPermissionDenied(Permission permission) {
//                        //绿色框中的流程
//                        //用户第一次拒绝了权限，没有勾选"不再提示。"这个值为true，此时告诉用户为什么需要这个权限。
//                        if (permission.shouldRationale) {
//                            new AlertDialog.Builder(AfterActivity.this)
//                                    .setTitle("提示")
//                                    .setMessage("如果你拒绝了权限，你将无法拨打电话，请点击授予权限")
//                                    .setPositiveButton("授予", new DialogInterface
//                                    .OnClickListener() {
//                                        @Override
//                                        public void onClick(DialogInterface dialogInterface,
//                                        int i) {
//                                            //用户确定以后，重新执行请求原始流程
//                                            makeCall();
//                                        }
//                                    }).create().show();
//                        } else {
//                            Toast.makeText(AfterActivity.this, "本次拨打电话授权失败,
//                            请手动去设置页打开权限，或者重试授权权限", Toast
//                            .LENGTH_SHORT).show();
//                        }
//                    }
//                });
//    }

    public void chooseContact() {
        UtilsWithPermission.chooseContact(PermissionActivity.this, REQUEST_CODE_CONTACT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_CONTACT:
                    Utils.onGetChooseContactData(PermissionActivity.this, data,
                            new Utils.ReadContactListener() {
                        @Override
                        public void onSuccess(Utils.ContactInfo contactInfo) {
                            Toast.makeText(PermissionActivity.this, contactInfo.toString(),
                                    Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailed() {

                        }
                    });
                    break;
                default:
                    break;
            }
        }
    }
}
