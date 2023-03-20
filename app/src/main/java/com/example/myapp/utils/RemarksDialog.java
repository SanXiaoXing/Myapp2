package com.example.myapp.utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myapp.R;

public class RemarksDialog extends Dialog implements View.OnClickListener{

    EditText editText;
    Button cancelBtn,ensureBtn;
    OnEnsureListener onEnsureListener;
    //回调接口方法
    public void setOnEnsureListener(OnEnsureListener onEnsureListener) {
        this.onEnsureListener = onEnsureListener;
    }

    public RemarksDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_remarks);    //设置对话框显示布局
        editText = findViewById(R.id.dialog_remarks_et);
        cancelBtn = findViewById(R.id.dialog_remarks_btn_cancel);
        ensureBtn = findViewById(R.id.dialog_remarks_btn_ensure);
        cancelBtn.setOnClickListener(this);
        ensureBtn.setOnClickListener(this);
    }

    public interface OnEnsureListener{
        public void onEnsure();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_remarks_btn_cancel:
                cancel();
                break;
            case R.id.dialog_remarks_btn_ensure:
                if (onEnsureListener!=null) {
                    onEnsureListener.onEnsure();
                }
                break;
        }
    }
    //获取输入数据的方法
    public String getEditText() {
        return editText.getText().toString().trim();
    }

    //设置dialog尺寸和屏幕一致
    public void setDialogSize(){
        //获取窗口对象
        Window window = getWindow();
        //获取窗口对象参数
        WindowManager.LayoutParams wlp = window.getAttributes();
        //获取屏幕宽度
        Display display = window.getWindowManager().getDefaultDisplay();
        wlp.width = (int)(display.getWidth());//对话窗口为屏幕窗口
        wlp.gravity = Gravity.BOTTOM;
        window.setBackgroundDrawableResource(android.R.color.transparent);
        window.setAttributes(wlp);
    }
}
