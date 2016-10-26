package com.netlab.loveofmum.activity;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.netlab.loveofmum.R;
import com.netlab.loveofmum.api.BaseActivity;
import com.netlab.loveofmum.api.MMloveConstants;
import com.netlab.loveofmum.api.JsonAsyncTaskOnComplete;
import com.netlab.loveofmum.api.JsonAsyncTask_Info;
import com.netlab.loveofmum.api.MyApplication;
import com.netlab.loveofmum.utils.IOUtils;
import com.netlab.loveofmum.utils.SystemStatusManager;
import com.umeng.analytics.MobclickAgent;

public class ForgetPwdActivity extends BaseActivity implements OnClickListener
{
	private EditText edt001;
	private EditText edt002;
	private Button btnchkcode;
	private String returnvalue001;
	
	private String chkcode;
	
	private TimerTask task;
	
	private int time = 120;
	private Timer timer = new Timer();
	
	private String mobile;
	
	// 定义Web Service相关的字符串
	private final String SOAP_NAMESPACE = MMloveConstants.URL001;
	private final String SOAP_ACTION = MMloveConstants.URL001
			+ MMloveConstants.SendAuthCode;
	private final String SOAP_METHODNAME = MMloveConstants.SendAuthCode;
	private final String SOAP_URL = MMloveConstants.URL002;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setTranslucentStatus() ;
		setContentView(R.layout.layout_findpass);
		MyApplication.getInstance().addActivity(this);
		iniView();
	}

	@Override
	protected void iniView()
	{
		// TODO Auto-generated method stub
		findViewById(R.id.img_left).setOnClickListener(this);
		((TextView) findViewById(R.id.txtHead)).setText("找回密码");
		//imgBack.setVisibility(View.INVISIBLE);
		edt001 = (EditText)findViewById(R.id.phone_register7);
		edt002 = (EditText)findViewById(R.id.edt001_layout_findpass);
		btnchkcode = (Button)findViewById(R.id.getcode);
		btnchkcode.setOnClickListener(this);
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

	public  void submit(View v)
	{
		if(hasInternetConnected())
		{
			if(edt001.getText().toString().trim().equals(""))
			{
				Toast.makeText(ForgetPwdActivity.this, R.string.str000_findpass,
						Toast.LENGTH_SHORT).show();
			}
			else if(edt002.getText().toString().trim().equals(""))
			{
				Toast.makeText(ForgetPwdActivity.this, R.string.str003_findpass,
						Toast.LENGTH_SHORT).show();
			}
			else
			{
				if(IOUtils.isMobileNO(edt001.getText().toString().trim()))
				{

				}
				else
				{
					Toast.makeText(ForgetPwdActivity.this, R.string.str001_findpass,
							Toast.LENGTH_SHORT).show();
					return;
				}
				// TODO Auto-generated method stub
				if(edt002.getText().toString().trim().equals(chkcode)&&edt001.getText().toString().trim().equals(mobile))
				{
					Intent i = new Intent(ForgetPwdActivity.this, ForgetPwd2Activity.class);
					i.putExtra("UserMobile", edt001.getText().toString());
					startActivity(i);
					finish();
				}
				else
				{
					Toast.makeText(ForgetPwdActivity.this, R.string.str002_findpass,
							Toast.LENGTH_SHORT).show();
				}
			}
		}
		else
		{
			Toast.makeText(ForgetPwdActivity.this, R.string.msgUninternet,
					Toast.LENGTH_SHORT).show();
		}
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.img_left:
				finish();
				break;
			case R.id.getcode:
				if(hasInternetConnected())
				{
					if(edt001.getText().toString().trim().equals(""))
					{
						Toast.makeText(ForgetPwdActivity.this, R.string.str000_findpass,
								Toast.LENGTH_SHORT).show();
					}
					else
					{
						if(IOUtils.isMobileNO(edt001.getText().toString().trim()))
						{
							mobile = edt001.getText().toString().trim();

							btnchkcode.setEnabled(false);


							CHKCodeSend();
						}
						else
						{
							Toast.makeText(ForgetPwdActivity.this, R.string.str001_findpass,
									Toast.LENGTH_SHORT).show();
						}
					}
				}
				else
				{
					Toast.makeText(ForgetPwdActivity.this, R.string.msgUninternet,
							Toast.LENGTH_SHORT).show();
				}
				break;
		}
	}

	/*
	 * 查询UserInquiry
	 */
	private void CHKCodeSend()
	{
		try
		{
			JsonAsyncTaskOnComplete doProcess = new JsonAsyncTaskOnComplete()
			{
				public void processJsonObject(Object result)
				{
					returnvalue001 = result.toString();
					if (returnvalue001 == null)
					{

					}
					else
					{
						Map<String, Object> map;
						// 下边是具体的处理
						try
						{
							JSONObject mySO = (JSONObject) result;
							JSONArray array = mySO
									.getJSONArray("SendAuthCode");
							if(array.getJSONObject(0).has("MessageCode")){
								if(array.getJSONObject(0).getString("MessageCode").equals("0")){
									for (int i = 0; i < array.length(); i++) {
										chkcode = array.getJSONObject(i)
												.getString("MessageContent");
									}
									task = new TimerTask() {
										@Override
										public void run() {

											runOnUiThread(new Runnable() { // UI thread
												@Override
												public void run() {
													if (time <= 0) {
														btnchkcode.setEnabled(true);
														btnchkcode.setText("获取验证码");
														task.cancel();
													} else {
														btnchkcode.setText("" + time);
													}
													time--;
												}
											});
										}
									};
									time = 120;
									timer.schedule(task, 0, 1000);
								}
								else{
									Toast.makeText(ForgetPwdActivity.this,array.getJSONObject(0).getString("MessageContent"),1).show();
								}
							}

						}
						catch (Exception ex)
						{
							ex.printStackTrace();
						}
					}
				}
			};

			JsonAsyncTask_Info task = new JsonAsyncTask_Info(
					ForgetPwdActivity.this, true, doProcess);
			Map<String, Object> paramMap = new HashMap<String, Object>();
			String str = "<Request><UserMobile>%s</UserMobile><Flag>%s</Flag></Request>";
			str = String.format(str, new Object[]
					{edt001.getText().toString(),"002" });
			paramMap.put("str", str);

			// 必须是这5个参数，而且得按照顺序
			task.execute(SOAP_NAMESPACE, SOAP_ACTION, SOAP_METHODNAME,
					SOAP_URL, paramMap);
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
