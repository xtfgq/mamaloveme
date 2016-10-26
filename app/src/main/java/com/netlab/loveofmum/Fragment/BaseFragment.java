package com.netlab.loveofmum.fragment;


import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.netlab.loveofmum.R;
import com.netlab.loveofmum.api.LocalAccessor;
import com.netlab.loveofmum.model.User;
import com.netlab.loveofmum.utils.DialogUtils;
import com.netlab.loveofmum.utils.PermissionsChecker;
import com.netlab.loveofmum.widget.DialogEnsureCancelView;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class BaseFragment extends Fragment implements View.OnClickListener {
    public View rootView;
    User user;
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
        user = LocalAccessor.getInstance(getActivity()).getUser();
        initData();
        return rootView;
    }
    public void showMissingPermissionDialog(final Context context){
        DialogEnsureCancelView dialogEnsureCancelView = new DialogEnsureCancelView(
                getActivity()).setDialogMsg("温馨提示", "权限->存储", "去打开存储权限")
                .setOnClickListenerEnsure(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        intent.setData(Uri.parse("package:" + getActivity().getPackageName()));
                        startActivity(intent);

                    }
                });
        DialogUtils.showSelfDialog(getActivity(), dialogEnsureCancelView);
    }
    public boolean hasInternetConnected() {
        ConnectivityManager manager = (ConnectivityManager) getActivity()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager != null) {
            NetworkInfo network = manager.getActiveNetworkInfo();
            if (network != null && network.isConnectedOrConnecting()) {
                return true;
            }
        }
        return false;
    }
}
