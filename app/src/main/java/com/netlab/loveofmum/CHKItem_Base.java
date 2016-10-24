package com.netlab.loveofmum;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.netlab.loveofmum.api.BaseActivity;
import com.netlab.loveofmum.api.MyApplication;
import com.netlab.loveofmum.utils.SystemStatusManager;
import com.umeng.analytics.MobclickAgent;

public class CHKItem_Base extends BaseActivity
{
	private TextView textview1;
	private TextView textview2;
	private Button btnok;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//setTranslucentStatus() ;
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.layout_chkitembase);
		MyApplication.getInstance().addActivity(this);
		iniView();
		setListeners();
	}
	
	/**
	 * 设置状态栏背景状态
	 */
	private void setTranslucentStatus() 
	{
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
		{
			Window win = getWindow();
			WindowManager.LayoutParams winParams = win.getAttributes();
			final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
			winParams.flags |= bits;
			win.setAttributes(winParams);
		}
		SystemStatusManager tintManager = new SystemStatusManager(this);
		tintManager.setStatusBarTintEnabled(true);
		tintManager.setStatusBarTintResource(R.drawable.bg_header);//状态栏无背景
	}

	@Override
	protected void iniView()
	{
		textview1=(TextView)findViewById(R.id.chkitembase_name);
        textview2=(TextView)findViewById(R.id.txtdecription);
        textview1.setText(this.getIntent().getExtras().getString("ItemName").toString());
        textview2.setText(this.getIntent().getExtras().getString("ItemContent").toString());
        btnok=(Button)findViewById(R.id.btnok);
	}

	private void setListeners()
	{
		btnok.setOnClickListener(new View.OnClickListener()
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
