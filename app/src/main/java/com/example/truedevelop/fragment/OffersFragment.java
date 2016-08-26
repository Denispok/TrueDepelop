package com.example.truedevelop.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.truedevelop.R;

public class OffersFragment extends AbstractionTabFragment {

    private static final int LAYOUT = R.layout.fragment_example;

    public static OffersFragment getInstance(Context context){

        Bundle args = new Bundle();
        OffersFragment fragment = new OffersFragment();
        fragment.setArguments(args);
        fragment.setContext(context);
        fragment.setTitle(context.getString(R.string.tab_item_offers));

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(LAYOUT, container, false);
        return view;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}