package com.netlab.loveofmum.activity;

import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.netlab.loveofmum.R;
import com.netlab.loveofmum.utils.SystemStatusManager;

/**
 * @author 蔺子岭
 * 功能说明：基础Activity
 */
public abstract class BaseActivity extends FragmentActivity implements View.OnClickListener {
    /**
     * 设置状态栏背景状态
     */
    protected void setTranslucentStatus() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window win = getWindow();
            WindowManager.LayoutParams winParams = win.getAttributes();
            final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            winParams.flags |= bits;
            win.setAttributes(winParams);
        }
        SystemStatusManager tintManager = new SystemStatusManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(R.color.home);// 状态栏无背景
    }
}
