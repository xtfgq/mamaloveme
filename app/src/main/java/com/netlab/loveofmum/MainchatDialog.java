package com.netlab.loveofmum;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.netlab.loveofmum.api.BaseActivity;
import com.umeng.analytics.MobclickAgent;

import java.util.Timer;

/**
 * Created by Administrator on 2016/5/21.
 */
public class MainchatDialog extends BaseActivity {
    private TextView tvcontent;
    private TextView tvok;
    private Timer mTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_default_ensure);
        iniView();
        setListeners();

    }



    @Override
    protected void iniView()
    {
        tvcontent=(TextView)findViewById(R.id.dialog_default_click_text_msg);
        tvok=(TextView)findViewById(R.id.dialog_default_click_ensure);
        tvcontent.setText(this.getIntent().getExtras().getString("content").toString());

    }

    private void setListeners()
    {
        tvok.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });
    }
    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        MobclickAgent.onResume(this);
    }
    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}
