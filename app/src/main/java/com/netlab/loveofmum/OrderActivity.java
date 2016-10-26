package com.netlab.loveofmum;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.netlab.loveofmum.api.BaseActivity;
import com.netlab.loveofmum.api.MyApplication;

public class OrderActivity extends BaseActivity {
private TextView tvok;
	private Intent mIntent;
	String keyitem;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//setTranslucentStatus() ;
		setContentView(R.layout.order_act);
		mIntent=this.getIntent();
		if(mIntent!=null){
			keyitem=mIntent.getStringExtra("Order");
		}
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
				if("".equals(keyitem)){
					startActivity( new Intent(OrderActivity.this,
						CHK_OrderDetail.class));
				}else{
				Intent i = new Intent(OrderActivity.this,
						CHK_OrderDetail.class);
				i.putExtra("Order", keyitem);
				startActivity(i);
				}
			
				
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