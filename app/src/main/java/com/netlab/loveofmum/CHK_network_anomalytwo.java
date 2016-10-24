package com.netlab.loveofmum;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.netlab.loveofmum.api.BaseActivity;
import com.netlab.loveofmum.api.LocalAccessor;
import com.netlab.loveofmum.huanxin.MainChatActivity;
import com.netlab.loveofmum.utils.SystemStatusManager;

public class CHK_network_anomalytwo extends BaseActivity{
	private TextView zixun;
	private Intent mIntet;
	private String AskOrderID,DoctorId,Hosid,DoctorUrl,HospitalName,DoctorName,Title;
	private TextView txtHead;
	private ImageView imgBack;
	 public static Activity pay;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	    requestWindowFeature(Window.FEATURE_NO_TITLE);
			setTranslucentStatus() ;
		setContentView(R.layout.layout_network_anomalytwo);
		iniView();
		pay=this;
		mIntet=this.getIntent();
		if(mIntet!=null){
			AskOrderID=mIntet.getStringExtra("AskOrderID");
			DoctorId=mIntet.getStringExtra("DoctorId");
			DoctorUrl=mIntet.getStringExtra("DoctorImageUrl");
			Hosid=mIntet.getStringExtra("HospitalID");
			HospitalName=mIntet.getStringExtra("Hosname");
			DoctorName=mIntet.getStringExtra("DoctorName");
			Title=mIntet.getStringExtra("Title");
		}
	
		
	
		zixun.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				Intent intent =new Intent();
//				
//				startActivity(intent);
				if(hasInternetConnected()){
					finish();
					
					Intent i = new Intent(CHK_network_anomalytwo.this,
							MainChatActivity.class);
					i.putExtra("AskOrderID", AskOrderID);
					i.putExtra("DoctorId", DoctorId);
					i.putExtra("HospitalID", Hosid);
					i.putExtra("DoctorImageUrl", DoctorUrl);
					i.putExtra("DoctorName", DoctorName);
					i.putExtra("Hosname", HospitalName);
					i.putExtra("Title", Title);
					i.putExtra("Status",2);
					startActivity(i);
					
				}else{
					finish();
					Toast.makeText(CHK_network_anomalytwo.this, "网络数据获取失败，请检查网络设置", 1).show();
				}
			}
		});
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
	protected void iniView() {
		// TODO Auto-generated method stub
		zixun=(TextView) findViewById(R.id.kaishizixun);
		txtHead = (TextView) findViewById(R.id.txtHead);
		txtHead.setText("支付完成");
		imgBack = (ImageView) findViewById(R.id.img_left);
		imgBack.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				finish();
			}
		});
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN)
		{

		   finish();

			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}	
	


 
