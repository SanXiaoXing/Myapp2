package com.example.myapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.myapp.adapter.AccountAdapter;
import com.example.myapp.db.AccountBean;
import com.example.myapp.db.DBManager;
import com.example.myapp.utils.BudgetDialog;
import com.example.myapp.utils.MoreDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    ListView todayLv;  //展示今日收入情况的ListView
    ImageView searchIv;
    ImageButton editBtn,moreBtn;

    //数据源
    List<AccountBean>mDatas;
    AccountAdapter adapter;
    int year,month,day;
    //头布局相关控件
    View headerView;
    TextView topOutTv,topInTv,topbudgetTv,topConTv;
    ImageView topShowIv;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initTime();
        initView();
        preferences = getSharedPreferences("budget", Context.MODE_PRIVATE);
        //添加ListView的头布局
        addLVHeaderView();

        mDatas = new ArrayList<>();
        //设置适配器，加载每一行数据到列表中
        adapter = new AccountAdapter(this, mDatas);
        todayLv.setAdapter(adapter);
    }

    /**
    * @Auther: SanXiaoXing
    * @Date: on 2023-03
    * @Description: 初始化自带的View方法
    */
    private void initView() {
        todayLv = findViewById(R.id.main_lv);
        editBtn = findViewById(R.id.main_btn_add);
        moreBtn = findViewById(R.id.main_btn_more);
        searchIv = findViewById(R.id.main_iv_search);
        editBtn.setOnClickListener(this);
        moreBtn.setOnClickListener(this);
        searchIv.setOnClickListener(this);
        setLVLongClickListener();
    }

    /**
    * @Auther: SanXiaoXing
    * @Date: on 2023-03
    * @Description: 设置ListView的长按事件
    */
    private void setLVLongClickListener() {
        todayLv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {  //点击了头布局
                    return false;
                }
                int pos = position-1;
                AccountBean clickBean = mDatas.get(pos);  //获取正在被点击的这条信息

                //弹出提示用户是否删除的对话框
                showDeleteItemDialog(clickBean);
                return false;
            }
        });
    }
    /* 弹出是否删除某一条记录的对话框*/
    private void showDeleteItemDialog(final  AccountBean clickBean) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示")
                .setMessage("确定没有此消费吗？")
                .setNegativeButton("取消", null)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int click_id = clickBean.getId();
                DBManager.deleteItemFromAccounttbById(click_id);
                mDatas.remove(clickBean);   //实时刷新，移除集合当中的对象
                adapter.notifyDataSetChanged();   //提示适配器更新数据
                setTopTvShow();   //改变头布局TextView显示的内容
            }
        });
        builder.create().show();
    }

    /** 给ListView添加头布局的方法*/
    private void addLVHeaderView() {
        //将布局转换成View对象
        headerView = getLayoutInflater().inflate(R.layout.item_mainlv_top, null);
        todayLv.addHeaderView(headerView);
        //查找头布局可用控件
        topOutTv = headerView.findViewById(R.id.item_mainlv_top_tv_out);
        topInTv = headerView.findViewById(R.id.item_mainlv_top_tv_in);
        topbudgetTv = headerView.findViewById(R.id.item_mainlv_top_tv_budget);
        topConTv = headerView.findViewById(R.id.item_main_top_tv_day);
        topShowIv = headerView.findViewById(R.id.item_mainlv_top_iv_hide);

        topbudgetTv.setOnClickListener(this);
        headerView.setOnClickListener(this);
        topShowIv.setOnClickListener(this);

    }

    /* 获取今日的具体时间*/
    private void initTime() {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH)+1;
        day = calendar.get(Calendar.DAY_OF_MONTH);
    }

    // 当activity获取焦点时，会调用的方法
    @Override
    protected void onResume() {
        super.onResume();
        loadDBData();
        setTopTvShow();
    }

    /**
    * @Auther: SanXiaoXing
    * @Date: on 2023-03
    * @Description: 设置头布局当中文本内容的显示
    */
    private void setTopTvShow() {
        /**
        * @Auther: SanXiaoXing
        * @Date: on 2023-03
        * @Description:  获取今日支出和收入总金额，显示在view当中
        */
        float outcomeOneDay = DBManager.getSumMoneyOneDay(year, month, day, 0);
        float incomeOneDay = DBManager.getSumMoneyOneDay(year, month, day, 1);

        String infoOneDay = "今日支出 ￥"+ outcomeOneDay + "  收入 ￥" + incomeOneDay;
        topConTv.setText(infoOneDay);

        /**
        * @Auther: SanXiaoXing
        * @Date: on 2023-03
        * @Description: 获取本月收入和支出总金额
        */
        float outcomeOneMonth = DBManager.getSumMoneyOneMonth(year, month, 0);
        float incomeOneMonth = DBManager.getSumMoneyOneMonth(year, month, 1);

        topInTv.setText("￥" + incomeOneMonth);
        topOutTv.setText("￥" + outcomeOneMonth);

        /**
        * @Auther: SanXiaoXing
        * @Date: on 2023-03
        * @Description: 设置显示运算剩余
        */
        float bmoney = preferences.getFloat("bmoney", 0);//预算
        if (bmoney == 0) {
            topbudgetTv.setText("￥ 0");
        }else{
            float syMoney = bmoney-outcomeOneMonth;
            topbudgetTv.setText("￥"+syMoney);
        }
    }


    // 加载数据库数据
    private void loadDBData() {
        List<AccountBean>list = DBManager.getAccountListOneDayFromAccounttb(year,month,day);
        mDatas.clear();
        mDatas.addAll(list);
        adapter.notifyDataSetChanged();
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.main_iv_search:
                Intent it = new Intent(this, SearchActivity.class);  //跳转界面
                startActivity(it);
                break;
            case R.id.main_btn_add:
                Intent it1 = new Intent(this, RecordActivity.class);  //跳转界面
                startActivity(it1);
                break;
            case R.id.main_btn_more:
                MoreDialog moreDialog = new MoreDialog(this);
                moreDialog.show();
                moreDialog.setDialogSize();
                break;
            case R.id.item_mainlv_top_tv_budget:
                showBudgetDialog();
                break;
            case R.id.item_mainlv_top_iv_hide:

                /**
                * @Auther: SanXiaoXing
                * @Date: on 2023-03
                * @Description: 显示文字和隐藏
                */
                toggleShow();
                break;
        }
        /**
        * @Auther: SanXiaoXing
        * @Date: on 2023-03
        * @Description: 头部被点击
        */
        if (v == headerView) {
            Intent intent = new Intent();
            intent.setClass(this, MonthChartActivity.class);
            startActivity(intent);
        }

    }

    /**
    * @Auther: SanXiaoXing
    * @Date: on 2023-03
    * @Description: 显示预算
    */
    private void showBudgetDialog() {
        BudgetDialog dialog = new BudgetDialog(this);
        dialog.show();
        dialog.setDialogSize();
        dialog.setOnEnsureListener(new BudgetDialog.OnEnsureListener() {
            @Override
            public void onEnsure(float money) {
                //将预算金额写入到共享参数当中，进行存储
                SharedPreferences.Editor editor = preferences.edit();
                editor.putFloat("bmoney",money);
                editor.commit();
                //计算剩余金额
                float outcomeOneMonth = DBManager.getSumMoneyOneMonth(year, month, 0);
                float syMoney = money-outcomeOneMonth;  //预算剩余 = 预算-支出
                topbudgetTv.setText("￥"+syMoney);
            }
        });
    }

    boolean isShow = true;
    /**
    * @Auther: SanXiaoXing
    * @Date: on 2023-03
    * @Description: 点击眼睛显示和隐藏文字
    */
    private void toggleShow() {
        if (isShow) {
            PasswordTransformationMethod passwordMethod = PasswordTransformationMethod.getInstance();
            topInTv.setTransformationMethod(passwordMethod);   //设置隐藏
            topOutTv.setTransformationMethod(passwordMethod);   //设置隐藏
            topbudgetTv.setTransformationMethod(passwordMethod);   //设置隐藏
            topShowIv.setImageResource(R.drawable.baseline_visibility_off_24);
            isShow = false;   //设置标志位为隐藏状态
        } else {
            HideReturnsTransformationMethod hideMethod = HideReturnsTransformationMethod.getInstance();
            topInTv.setTransformationMethod(hideMethod);   //设置隐藏
            topOutTv.setTransformationMethod(hideMethod);   //设置隐藏
            topbudgetTv.setTransformationMethod(hideMethod);   //设置隐藏
            topShowIv.setImageResource(R.drawable.baseline_remove_red_eye_24);
            isShow = true;   //设置标志位为隐藏状态
        }
    }
}