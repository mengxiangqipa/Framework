package com.framework2.greendao;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;


import com.demo.demo.R;

import java.util.ArrayList;
import java.util.List;

public class GreenDaoTestActivity extends AppCompatActivity {
    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        GreenDaoManager dbManager = GreenDaoManager.getInstance();
        List<User> ll = new ArrayList<>();
        for (int i = 0; i < 100000; i++) {
            User user = new User();
            user.setId(i);
            user.setAge(i * 3);
            user.setName("第" + i + "人[]");
            ll.add(user);
        }
        long a = System.currentTimeMillis();
        dbManager.insertEntityList(GreenDaoTestActivity.this, User.class, ll);
        Log.e("yy", "添加耗时-->" + (System.currentTimeMillis() - a));
        a = System.currentTimeMillis();
        List<User> userList = dbManager.queryEntityList(GreenDaoTestActivity.this, User.class);
        Log.e("yy", "查询耗时-->" + (System.currentTimeMillis() - a));
        for (User user : userList) {
            user.setAge(10);
        }
        userList = dbManager.queryEntityListWithCondition(GreenDaoTestActivity.this, User.class, UserDao.Properties.Age.between(200, 300));
        for (User user : userList) {
            Log.e("yy", "queryUserList--条件查询--->" + user.getId() + "---" + user.getName() + "--" + user.getAge());
        }
        a = System.currentTimeMillis();
        dbManager.updateEntityList(GreenDaoTestActivity.this, User.class, userList);
        Log.e("yy", "更新耗时-->" + (System.currentTimeMillis() - a));
        a = System.currentTimeMillis();
        userList = dbManager.queryEntityList(GreenDaoTestActivity.this, User.class);
        Log.e("yy", "查询耗时-->" + (userList.size() + "--") + (System.currentTimeMillis() - a));
        //				a = System.currentTimeMillis();
        //				dbManager.deleteEntityList(MainActivity.this, User.class, userList );
        //				Log.e("yy", "删除耗时-->" + (System.currentTimeMillis() - a));

        a = System.currentTimeMillis();
        dbManager.deleteAll(GreenDaoTestActivity.this, User.class);
        Log.e("yy", "删除耗时2-->" + (System.currentTimeMillis() - a));
        //		a = System.currentTimeMillis();
        userList = dbManager.queryEntityListWithCondition(GreenDaoTestActivity.this, User.class, UserDao.Properties.Age.eq(11));
        for (User user : userList) {
            Log.e("yy", "queryUserList--after--->" + user.getId() + "---" + user.getName() + "--" + user.getAge());
        }
        //		Log.e("yy", "log耗时-->" + (System.currentTimeMillis() - a));
        //		dbManager.insertEntityList(MainActivity.this,User.class,userList);
    }
}
