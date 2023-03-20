package com.example.myapp.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.example.myapp.BackupActivity;
import com.example.myapp.HistoryActivity;
import com.example.myapp.MonthChartActivity;
import com.example.myapp.R;
import com.example.myapp.SettingActivity;

/**
* @Auther: SanXiaoXing
* @Date: on 2023-03
* @Description:
*/
public class MoreDialog extends Dialog implements View.OnClickListener {
    Button backBtn,settingBtn,historyBtn,infoBtn;
    ImageView errorIv;

    public MoreDialog(@NonNull Context context) {
        super(context);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_more);
        backBtn = findViewById(R.id.dialog_more_btn_back);
        settingBtn = findViewById(R.id.dialog_more_btn_setting);
        historyBtn = findViewById(R.id.dialog_more_btn_history);
        infoBtn = findViewById(R.id.dialog_more_btn_info);
        errorIv = findViewById(R.id.dialog_more_iv);

        backBtn.setOnClickListener(this);
        settingBtn.setOnClickListener(this);
        historyBtn.setOnClickListener(this);
        infoBtn.setOnClickListener(this);
        errorIv.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.dialog_more_btn_back:
                intent.setClass(getContext(), BackupActivity.class);
                getContext().startActivity(intent);
                break;
            case R.id.dialog_more_btn_setting:
                intent.setClass(getContext(), SettingActivity.class);
                getContext().startActivity(intent);
                break;
            case R.id.dialog_more_btn_history:
                intent.setClass(getContext(), HistoryActivity.class);
                getContext().startActivity(intent);
                break;
            case R.id.dialog_more_btn_info:
                intent.setClass(getContext(), MonthChartActivity.class);
                getContext().startActivity(intent);
                break;
            case R.id.dialog_more_iv:
                break;
        }
        cancel();
    }


    /* 设置Dialog的尺寸和屏幕尺寸一致*/
    public void setDialogSize(){
        //获取当前窗口对象
        Window window = getWindow();
        //获取窗口对象的参数
        WindowManager.LayoutParams wlp = window.getAttributes();
        // 获取屏幕宽度
        Display d = window.getWindowManager().getDefaultDisplay();
        wlp.width = (int)(d.getWidth());  //对话框窗口为屏幕窗口
        wlp.gravity = Gravity.BOTTOM;
        window.setBackgroundDrawableResource(android.R.color.transparent);
        window.setAttributes(wlp);
    }
}
