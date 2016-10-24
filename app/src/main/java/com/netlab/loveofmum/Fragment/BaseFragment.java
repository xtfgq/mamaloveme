package com.netlab.loveofmum.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.netlab.loveofmum.R;
import com.netlab.loveofmum.utils.PermissionsChecker;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class BaseFragment extends Fragment implements View.OnClickListener {
    public View rootView;
    public abstract View  initView(LayoutInflater inflater, ViewGroup container,
                                   Bundle savedInstanceState);
    public abstract void  initData();
    protected PermissionsChecker mPermissionsChecker; // 权限检测器

    public BaseFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mPermissionsChecker = new PermissionsChecker(getActivity());
        rootView =  initView(inflater,container,savedInstanceState);
        initData();
        return rootView;
    }

}
