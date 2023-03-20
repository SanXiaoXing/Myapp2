package com.example.myapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;

import com.example.myapp.adapter.RecordPagerAdapter;
import com.example.myapp.frag_record.IncomeFragment;
import com.example.myapp.frag_record.BaseRecordFragment;
import com.example.myapp.frag_record.OutcomeFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class RecordActivity extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        //查找控件
        tabLayout = findViewById(R.id.record_tabs);
        viewPager = findViewById(R.id.record_vp);
        //设置ViewPaper加载页面
        initPaper();
    }

    private void initPaper() {
        //初始化
        List<Fragment>fragmentList = new ArrayList<>();
        //创建收入和支出页面，放在Fragment
        OutcomeFragment outcomeFragment = new OutcomeFragment();//支出
        IncomeFragment incomeFragment = new IncomeFragment(); // 收入
        fragmentList.add(outcomeFragment);
        fragmentList.add(incomeFragment);

        //创建适配器adapter
        RecordPagerAdapter pagerAdapter = new RecordPagerAdapter(getSupportFragmentManager(),fragmentList);
        //设置适配器
        viewPager.setAdapter(pagerAdapter);
        //将TabLayout和ViewPager进行关联
        tabLayout.setupWithViewPager(viewPager);


    }


    //点击
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.record_iv_back:
                finish();
                break;
        }
    }
}