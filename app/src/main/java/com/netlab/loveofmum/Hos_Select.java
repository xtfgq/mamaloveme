package com.netlab.loveofmum;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.netlab.loveofmum.api.BaseActivity;
import com.netlab.loveofmum.api.MMloveConstants;
import com.netlab.loveofmum.api.LocalAccessor;
import com.netlab.loveofmum.api.MyApplication;
import com.netlab.loveofmum.model.User;
import com.netlab.loveofmum.utils.SystemStatusManager;
import com.umeng.analytics.MobclickAgent;

public class Hos_Select extends BaseActivity
{
	private TextView txtHead;
	private ImageView imgBack;
	
	private Button btn001;
	
	private Button btn002;
	
	private User user;


	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setTranslucentStatus() ;
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.layout_hoslist);
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
	protected void iniView()
	{
		// TODO Auto-generated method stub
		//txtHead = (TextView) findViewById(R.id.txtHead);
		//imgBack = (ImageView) findViewById(R.id.img_left);
		
		//txtHead.setText("请选择产检医院");
		
		btn001 = (Button) findViewById(R.id.btn001_hoslist);
		btn002 = (Button) findViewById(R.id.btn002_hoslist);
		user = LocalAccessor.getInstance(Hos_Select.this).getUser();
	}

	private void setListeners()
	{
//		imgBack.setOnClickListener(new View.OnClickListener()
//		{
//			@Override
//			public void onClick(View v)
//			{
//				// TODO Auto-generated method stub
//				finish();
//			}
//		});
		
		btn001.setOnClickListener(new View.OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				int hosID = user.HospitalID;
				user.HospitalID = 1;
				user.HospitalName = "郑州大学第三附属医院";
				
				try
				{
					LocalAccessor.getInstance(Hos_Select.this).updateUser(user);
					
					if (hosID == 0)
					{
						//LocalAccessor.getInstance(Hos_Select.this).updateUser(user);
						Intent i = new Intent(Hos_Select.this, TimeSet.class);
						//i.putExtra("TabIndex", "A_TAB");

						startActivity(i);
					}
					else
					{
						finish();
					}
				}
				catch (Exception e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		btn002.setOnClickListener(new View.OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				int hosID = user.HospitalID;
				user.HospitalID = 46;
				user.HospitalName = "郑州大学第一附属医院";
				try
				{
					LocalAccessor.getInstance(Hos_Select.this).updateUser(user);
					
					if (hosID == 0)
					{
						//LocalAccessor.getInstance(Hos_Select.this).updateUser(user);
						Intent i = new Intent(Hos_Select.this, TimeSet.class);
						//i.putExtra("TabIndex", "A_TAB");

						startActivity(i);
					}
					else
					{
						finish();
					}
				}
				catch (Exception e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}
	
	@Override
	public void onDestroy()
	{
		// TODO Auto-generated method stub
		super.onDestroy();
	}
}
