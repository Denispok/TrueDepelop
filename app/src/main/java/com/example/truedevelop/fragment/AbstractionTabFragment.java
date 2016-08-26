package com.example.truedevelop.fragment;

import android.content.Context;
import android.view.View;
import android.support.v4.app.Fragment;

public class AbstractionTabFragment extends Fragment {

    private String title;

    protected View view;
    protected Context context;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;

    }
}
