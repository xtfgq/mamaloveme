package com.netlab.loveofmum;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.netlab.loveofmum.api.BaseActivity;
import com.netlab.loveofmum.api.MyApplication;

public class PingLunAct extends BaseActivity {
	
	private TextView tvok;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//setTranslucentStatus() ;
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_add10_jifen);
		MyApplication.getInstance().addActivity(this);
		iniView();
		setListeners();
	}
	
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	protected void iniView()
	{
	
		tvok=(TextView)findViewById(R.id.dialog_default_click_ensure);
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
		
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		
	}

}

