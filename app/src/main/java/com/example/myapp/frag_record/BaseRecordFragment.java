package com.example.myapp.frag_record;

import android.inputmethodservice.KeyboardView;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapp.R;
import com.example.myapp.db.AccountBean;
import com.example.myapp.db.TypeBean;
import com.example.myapp.utils.KeyBoardUtils;
import com.example.myapp.utils.RemarksDialog;
import com.example.myapp.utils.SelectTimeDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 *
 * 记录页面的支出模块
 */
public abstract class BaseRecordFragment extends Fragment implements View.OnClickListener {

    //初始化
    KeyboardView keyboardView;
    EditText moneyEt;
    ImageView typeIv;
    TextView typeTv,beizhuTv,timeTv;
    GridView typeGv;
    List<TypeBean>typeList;
    TypeBaseAdapter adapter;
    AccountBean accountBean;    //将需要插入的记账数据保存成对象的形式

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        accountBean = new AccountBean();    //创建对象
        accountBean.setTypename("其他");
        accountBean.setsImageId(R.mipmap.more);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_outcome, container, false);
        initView(view);
        setInitTime();
        //给GridView填充数据的方法
        loadDateToGV();
        setGVListerner();//设置GridView每一项的点击事件
        return view;
    }
    //获取当前时间
    private void setInitTime() {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日  HH:mm");
        String time = simpleDateFormat.format(date);
        timeTv.setText(time);
        accountBean.setTime(time);

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH)+1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        accountBean.setYear(year);
        accountBean.setMonth(month);
        accountBean.setDay(day);
    }

    //设置GridView每一项的点击事件
    private void setGVListerner() {
        typeGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.selectPos= position;
                adapter.notifyDataSetChanged();//提示绘制发生变化
                TypeBean typeBean = typeList.get(position);
                String typename = typeBean.getTypename();
                typeTv.setText(typename);
                accountBean.setTypename(typename);
                int simageId = typeBean.getSimageId();
                typeIv.setImageResource(simageId);
                accountBean.setsImageId(simageId);
            }
        });
    }

    //给GridView填充数据的方法
    public void loadDateToGV() {
        typeList = new ArrayList<>();
        adapter = new TypeBaseAdapter(getContext(), typeList);
        typeGv.setAdapter(adapter);

    }

    private void initView(View view) {
        keyboardView = view.findViewById(R.id.frag_record_keyboard);
        moneyEt = view.findViewById(R.id.frag_record_et_money);
        typeIv = view.findViewById(R.id.frag_record_iv);
        typeGv = view.findViewById(R.id.frag_record_gv);
        typeTv = view.findViewById(R.id.frag_record_tv_type);
        beizhuTv = view.findViewById(R.id.frag_record_tv_beizhu);
        timeTv = view.findViewById(R.id.frag_record_tv_time);
        beizhuTv.setOnClickListener(this);
        timeTv.setOnClickListener(this);
        //显示键盘
        KeyBoardUtils boardUtils = new KeyBoardUtils(keyboardView, moneyEt);
        boardUtils.showKeyboard();
        //设置接口，监听确定按钮
        boardUtils.setOnEnsureListener(new KeyBoardUtils.OnEnsureListener() {
            @Override
            public void onEnsure() {
                //获取输入钱数
                String moneyStr = moneyEt.getText().toString();
                if (TextUtils.isEmpty(moneyStr)||moneyStr.equals("0")) {
                    getActivity().finish();
                    return;
                }
                float money = Float.parseFloat(moneyStr);
                accountBean.setMoney(money);
                //确定点击按钮
                //获取记录信息，保存数据
                saveAccountToDB();
                
                //返回上一级
                getActivity().finish();
            }
        });
    }

    //让子类一定重写此方法
    public abstract void saveAccountToDB();

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.frag_record_tv_time:
                showTimeDialog();
                break;
            case R.id.frag_record_tv_beizhu:
                showRemarksDialog();
                break;
        }
    }

    //弹出显示时间的对话框
    private void showTimeDialog() {
        SelectTimeDialog dialog = new SelectTimeDialog(getContext());
        dialog.show();
        //确定按钮被点击监听器
        dialog.setOnEnsureListener(new SelectTimeDialog.OnEnsureListener() {
            @Override
            public void onEnsure(String time, int year, int month, int day) {
                timeTv.setText(time);
                accountBean.setTime(time);
                accountBean.setYear(year);
                accountBean.setMonth(month);
                accountBean.setDay(day);
            }
        });
    }

    //弹出备注对话框
    public void showRemarksDialog() {
        RemarksDialog dialog = new RemarksDialog(getContext());
        dialog.show();
        dialog.setDialogSize();

        dialog.setOnEnsureListener(new RemarksDialog.OnEnsureListener() {
            @Override
            public void onEnsure() {
                String msg = dialog.getEditText();
                if (!TextUtils.isEmpty(msg)) {
                    beizhuTv.setText(msg);
                    accountBean.setRemarks(msg);
                }
                dialog.cancel();
            }

        });
    }

}