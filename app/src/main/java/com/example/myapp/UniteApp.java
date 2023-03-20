package com.example.myapp;

import android.app.Application;

import com.example.myapp.db.DBManager;

public class UniteApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //初始化数据库
        DBManager.initDB(getApplicationContext());
    }
}
