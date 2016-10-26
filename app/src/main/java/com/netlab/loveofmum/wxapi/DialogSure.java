package com.netlab.loveofmum.wxapi;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.netlab.loveofmum.CHK_OrderDetail;
import com.netlab.loveofmum.CHK_OrderSure;
import com.netlab.loveofmum.OrderActivity;
import com.netlab.loveofmum.R;
import com.netlab.loveofmum.api.BaseActivity;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by Dell on 2016/3/16.
 */
public class DialogSure extends BaseActivity {
    private TextView tvcontent;
    private TextView tvok;
    private String order;
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
        order=this.getIntent().getExtras().getString("Order").toString();

    }

    private void setListeners()
    {
        tvok.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i=new Intent(DialogSure.this,CHK_OrderDetail.class);
                i.putExtra("Order", order);

                startActivity(i);
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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            Intent i=new Intent(DialogSure.this,CHK_OrderDetail.class);
            i.putExtra("Order", order);
            startActivity(i);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
