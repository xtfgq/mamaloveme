package com.netlab.loveofmum;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import com.netlab.loveofmum.api.BaseActivity;
import com.netlab.loveofmum.api.MyApplication;
import com.umeng.analytics.MobclickAgent;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Dell on 2016/3/16.
 */
public class DialogEnsure extends BaseActivity {
    private TextView tvcontent;
    private TextView tvok;
    private Timer mTimer;
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {

                case 2:
                   finish();

                    break;
            }
            super.handleMessage(msg);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_default_ensure);
        iniView();
        mTimer = new Timer();
        setListeners();
        setTimerTask();
    }

    private void setTimerTask() {
        // TODO Auto-generated method stub
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                Message message = new Message();
                message.what = 2;
                mHandler.sendMessage(message);
            }
        }, 3000, 3000);
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
        mTimer.cancel();
    }
}
