package com.example.myapp.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

/**
* @Auther: SanXiaoXing
* @Date: on 2023-03
* @Description:
*/
public class ChartVPAdapter extends FragmentPagerAdapter{
    List<Fragment> fragmentList;
    public ChartVPAdapter(@NonNull FragmentManager fm, List<Fragment>fragmentList) {
        super(fm);
        this.fragmentList = fragmentList;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }
}
