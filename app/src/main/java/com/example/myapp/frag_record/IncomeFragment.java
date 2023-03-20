package com.example.myapp.frag_record;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.myapp.R;
import com.example.myapp.db.DBManager;
import com.example.myapp.db.TypeBean;

import java.util.List;

public class IncomeFragment extends BaseRecordFragment{
    @Override
    public void loadDateToGV() {
        super.loadDateToGV();
        //获取数据库的数据源
        List<TypeBean> inlist = DBManager.getTypeList(1);
        typeList.addAll(inlist);
        adapter.notifyDataSetChanged();
        typeTv.setText("其他");
        typeIv.setImageResource(R.mipmap.qita1);
    }

    @Override
    public void saveAccountToDB() {
        accountBean.setKind(1);
        DBManager.insertItemToAccounttb(accountBean);

    }

}