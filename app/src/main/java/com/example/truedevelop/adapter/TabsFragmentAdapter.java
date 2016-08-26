package com.example.truedevelop.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.truedevelop.Constants;
import com.example.truedevelop.dto.RemindDTO;
import com.example.truedevelop.fragment.AbstractionTabFragment;
import com.example.truedevelop.fragment.NewsFragment;
import com.example.truedevelop.fragment.OffersFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TabsFragmentAdapter extends FragmentPagerAdapter {

    private Map <Integer, AbstractionTabFragment> tabs;
    private Context context;

    private NewsFragment newsFragment;

    private List<RemindDTO> data;

    public TabsFragmentAdapter(Context context, FragmentManager fm) {
        super(fm);
        this.context = context;
        this.data = new ArrayList<>();
        initTabsMap(context);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabs.get(position).getTitle();
    }

    @Override
    public Fragment getItem(int position) {
        return tabs.get(position);
    }

    @Override
    public int getCount() {
        return tabs.size();
    }

    private void initTabsMap(Context context) {
        newsFragment = NewsFragment.getInstance(context, data);

        tabs = new HashMap<>();
        tabs.put(Constants.TAB_ONE_N, newsFragment);
        tabs.put(Constants.TAB_TWO_O, OffersFragment.getInstance(context));
    }

    public void setData(List<RemindDTO> data) {
        this.data = data;
        newsFragment.refreshData(data);
    }
}
