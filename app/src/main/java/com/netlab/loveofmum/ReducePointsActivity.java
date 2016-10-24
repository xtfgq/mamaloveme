package com.netlab.loveofmum;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.netlab.loveofmum.api.BaseActivity;
import com.umeng.analytics.MobclickAgent;

public class ReducePointsActivity extends BaseActivity {
    private TextView tvcontent;
    private TextView tvok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_reduce_points);
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
}
