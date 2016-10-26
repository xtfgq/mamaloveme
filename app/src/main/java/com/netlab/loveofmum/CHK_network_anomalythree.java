package com.netlab.loveofmum;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.netlab.loveofmum.api.BaseActivity;
import com.netlab.loveofmum.api.JsonAsyncTaskOnComplete;
import com.netlab.loveofmum.api.JsonAsyncTask_Info;
import com.netlab.loveofmum.api.LocalAccessor;
import com.netlab.loveofmum.api.MMloveConstants;
import com.netlab.loveofmum.huanxin.MainChatActivity;
import com.netlab.loveofmum.model.User;
import com.netlab.loveofmum.utils.SystemStatusManager;

public class CHK_network_anomalythree extends BaseActivity{
	protected static final int DIALOG_EXIT = 0;
	private String AskOrderID,DoctorId,Hosid,DoctorUrl,DoctorName,HosName,Title;
private int i;
private int num_tv;
private Intent mIntent;
private User user;
private TextView ZiXun;
private Boolean isPost=false;
private TextView txtHead;
private ImageView imgBack;
// 定义Web Service相关的字符串
private final String SOAP_NAMESPACE = MMloveConstants.URL001;
private final String SOAP_ACTION = MMloveConstants.URL001
		+ MMloveConstants.AskOrderUpdate;
private final String SOAP_METHODNAME = MMloveConstants.AskOrderUpdate;
private final String SOAP_URL = MMloveConstants.URL002;
	@Override
	
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setTranslucentStatus() ;
		setContentView(R.layout.layout_network_anomalythree);
		iniView();
		num_tv=1;
		mIntent=this.getIntent();
		user = LocalAccessor.getInstance(CHK_network_anomalythree.this).getUser();
		if(mIntent!=null){
			AskOrderID=mIntent.getStringExtra("AskOrderID");
			DoctorId=mIntent.getStringExtra("DoctorId");
			DoctorUrl=mIntent.getStringExtra("DoctorImageUrl");
			Hosid=mIntent.getStringExtra("HospitalID");
			DoctorName=mIntent.getStringExtra("DoctorImageUrl");
			HosName=mIntent.getStringExtra("Hosname");
			Title=mIntent.getStringExtra("Title");
		}
		ZiXun.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!isPost&&num_tv<6){
				AskOrderUpdate(1);
				}else if(num_tv>5){
					Intent intent =new Intent();
					intent.setClass(CHK_network_anomalythree.this,CHK_network_anomaly.class);
					startActivity(intent);
					finish();
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
		tintManager.setStatusBarTintResource(R.color.home);//状态栏无背景
	}
	protected void AskOrderUpdate(int paytype) {
		// TODO Auto-generated method stub
		 if (!hasInternetConnected()){
			 isPost=false;
				num_tv++;
				Toast.makeText(CHK_network_anomalythree.this, "网络数据获取失败，请检查网络设置", 1).show();
				return;
				}
			if ( num_tv>5) {
				Intent intent =new Intent();
				intent.setClass(CHK_network_anomalythree.this,CHK_network_anomaly.class);
				startActivity(intent);
				finish();
			}else{
		JsonAsyncTaskOnComplete doProcess=new JsonAsyncTaskOnComplete(){

			@Override
			public void processJsonObject(Object result) {
				// TODO Auto-generated method stub
				String returnvalue= result.toString();
//				{"AskOrderUpdate":[{"MessageContent":"成功！","MessageCode":"0"}]}
				try {
                JSONObject mySO = (JSONObject) result;
					JSONArray array = mySO
							.getJSONArray("AskOrderUpdate");
					if("0".equals(array.getJSONObject(0).getString("MessageCode"))){
						user.PayId="";
						LocalAccessor.getInstance(CHK_network_anomalythree.this).updateUser(user);
						Intent i = new Intent(CHK_network_anomalythree.this,
								CHK_network_anomalytwo.class);
						i.putExtra("AskOrderID", AskOrderID);
						i.putExtra("DoctorId", DoctorId);
						i.putExtra("HospitalID", Hosid);
						i.putExtra("DoctorImageUrl", DoctorUrl);
						i.putExtra("DoctorName", DoctorName);
						i.putExtra("Hosname", HosName);
						i.putExtra("Title", Title);
						i.putExtra("Status",2);
						startActivity(i);
						finish();
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					isPost=false;
				}

				
			}};
			isPost=true;
		JsonAsyncTask_Info task = new JsonAsyncTask_Info(
				CHK_network_anomalythree.this, true, doProcess);
		Map<String, Object> paramMap = new HashMap<String, Object>();
		num_tv++;

		String str = "<Request><AskOrderID>%s</AskOrderID><OrderStatus>%s</OrderStatus><Reason>%s</Reason><PayType>%s</PayType><PayStatus>%s</PayStatus></Request>";
     
		str = String.format(str, new Object[]
		{ AskOrderID,"02","",paytype+"",""});
		
		paramMap.put("str", str);
		
		// 必须是这5个参数，而且得按照顺序
		task.execute(SOAP_NAMESPACE, SOAP_ACTION, SOAP_METHODNAME,
				SOAP_URL, paramMap);
		Toast.makeText(CHK_network_anomalythree.this, "正在进行请求", 1).show();
			}
	}
	@Override
	protected void iniView() {
		// TODO Auto-generated method stub
		ZiXun=(TextView) findViewById(R.id.zixun);
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
 
