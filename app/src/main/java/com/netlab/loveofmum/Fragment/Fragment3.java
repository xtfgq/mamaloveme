package com.netlab.loveofmum.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.netlab.loveofmum.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment3 extends BaseFragment {


    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_3, container, false);
        return v;
    }

    @Override
    public void initData() {

    }

    public Fragment3() {
        // Required empty public constructor
    }


    @Override
    public void onClick(View v) {

    }
}
