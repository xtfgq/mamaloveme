package com.netlab.loveofmum;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.netlab.loveofmum.api.BaseActivity;
import com.netlab.loveofmum.api.MyApplication;

public class RegisterAct extends BaseActivity {
private TextView tvok;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//setTranslucentStatus() ;
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_reg);
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
				Intent i = new Intent(RegisterAct.this,
						MainTabActivity.class);
				i.putExtra("TabIndex", "D_TAB");

				startActivity(i);
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
