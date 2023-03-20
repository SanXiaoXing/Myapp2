package com.example.myapp.utils;

import android.content.Context;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.text.Editable;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;

import com.example.myapp.R;

public class KeyBoardUtils {
    private final Keyboard keyboard;
    private KeyboardView keyboardView;
    private EditText editText;

    public interface OnEnsureListener{
        public void onEnsure();
    }
    OnEnsureListener onEnsureListener;

    public void setOnEnsureListener(OnEnsureListener onEnsureListener){
        this.onEnsureListener = onEnsureListener;
    }

    public KeyBoardUtils(KeyboardView keyboardView, EditText editText){
        // 获取EditText对象
        EditText myEditText = editText.findViewById(R.id.frag_record_et_money);
        // 获取EditText的上下文（即当前Activity的上下文）
        Context context = myEditText.getContext();

        this.keyboardView=keyboardView;
        this.editText=editText;
        this.editText.setInputType(InputType.TYPE_NULL); //取消系统弹出键盘
        // 创建新的Keyboard对象
        keyboard= new Keyboard(context, R.xml.key);

        this.keyboardView.setKeyboard(keyboard); //设置要显示的键盘样式
        this.keyboardView.setEnabled(true);
        this.keyboardView.setPreviewEnabled(false);
        this.keyboardView.setOnKeyboardActionListener(listener); // 设置键盘俺就被点击了监听
    }
    KeyboardView.OnKeyboardActionListener listener = new KeyboardView.OnKeyboardActionListener() {
        @Override
        public void onPress(int primaryCode) {

        }

        @Override
        public void onRelease(int primaryCode) {

        }

        @Override
        public void onKey(int primaryCode, int[] keyCodes) {
            Editable editable = editText.getText();
            int start = editText.getSelectionStart();
            switch (primaryCode){
                case Keyboard.KEYCODE_DELETE:             // 删除
                    if (editable!=null && editable.length()>0){
                        if (start>0){
                            editable.delete(start-1,start);
                        }
                    }
                    break;
                case Keyboard.KEYCODE_CANCEL:            // 清零
                    editable.clear();
                    break;
                case Keyboard.KEYCODE_DONE:             // 完成
                    onEnsureListener.onEnsure();        //使用接口调用方法
                    break;
                default:
                    editable.insert(start,Character.toString((char)primaryCode));
                    break;
            }

        }

        @Override
        public void onText(CharSequence text) {

        }

        @Override
        public void swipeLeft() {

        }

        @Override
        public void swipeRight() {

        }

        @Override
        public void swipeDown() {

        }

        @Override
        public void swipeUp() {

        }
    };

    //显示键盘
    public void showKeyboard(){
        int visibility = keyboardView.getVisibility();
        if (visibility == View.INVISIBLE || visibility == View.GONE){
            keyboardView.setVisibility(View.VISIBLE);
        }
    }
    //隐藏键盘
    public void hideKeyboard(){
        int visibility = keyboardView.getVisibility();
        if (visibility == View.VISIBLE || visibility==View.INVISIBLE){
            keyboardView.setVisibility(View.GONE);
        }
    }
}
