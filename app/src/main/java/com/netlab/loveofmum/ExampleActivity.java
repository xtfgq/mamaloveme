package com.netlab.loveofmum;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import com.netlab.loveofmum.utils.BackAES;

import com.netlab.loveofmum.utils.Des;
import com.netlab.loveofmum.utils.SystemStatusManager;

public class ExampleActivity extends Activity {
    private View vTop;
private TextView tvjiami;
    String content="12346469";
    String skey="admin";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setTranslucentStatus();
        setContentView(R.layout.activity_example);
        iniViews();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            int h = getStatusHeight(this);
            ViewGroup.LayoutParams params = vTop.getLayoutParams();
            params.height = h;
            params.width = ViewGroup.LayoutParams.FILL_PARENT;
            vTop.setLayoutParams(params);
        } else {
            vTop.setVisibility(View.GONE);
        }
    }
    public static int getStatusHeight(Context context) {

        int statusHeight = -1;
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height")
                    .get(object).toString());
            statusHeight = context.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusHeight;
    }
    private void iniViews() {
        vTop=findViewById(R.id.v_top);
        tvjiami=(TextView) findViewById(R.id.tv_jiami);

        try {



            String decryptString= Des.encode(content);

           tvjiami.setText(decryptString+"解密数据"+Des.decode("U4UwyX30n0E="));
//            tvjiami.setText(decryptString);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 设置状态栏背景状态
     */
    private void setTranslucentStatus() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window win = getWindow();
            WindowManager.LayoutParams winParams = win.getAttributes();
            final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            winParams.flags |= bits;
            win.setAttributes(winParams);
        }
        SystemStatusManager tintManager = new SystemStatusManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(R.drawable.bg_header);// 状态栏无背景
    }
}
