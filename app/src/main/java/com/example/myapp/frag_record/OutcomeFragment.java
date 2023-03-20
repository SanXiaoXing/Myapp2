package com.example.myapp.frag_record;

import com.example.myapp.R;
import com.example.myapp.db.DBManager;
import com.example.myapp.db.TypeBean;

import java.util.List;

public class OutcomeFragment extends BaseRecordFragment{
    @Override
    public void loadDateToGV() {
        super.loadDateToGV();
        //获取数据库的数据源
        List<TypeBean> outlist = DBManager.getTypeList(0);
        typeList.addAll(outlist);
        adapter.notifyDataSetChanged();
        typeTv.setText("其他");
        typeIv.setImageResource(R.mipmap.more);
    }

    @Override
    public void saveAccountToDB() {
        accountBean.setKind(0);
        DBManager.insertItemToAccounttb(accountBean);

    }
}
