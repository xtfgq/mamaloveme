package com.netlab.loveofmum.activity;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
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

public class ForgetPwd2Activity extends BaseActivity implements OnClickListener
{

	
	private EditText edt001;
	private EditText edt002;
	
	private String mobile;

	private String returnvalue001;
	
	// 定义Web Service相关的字符串
	private final String SOAP_NAMESPACE = MMloveConstants.URL001;
	private final String SOAP_ACTION = MMloveConstants.URL001
			+ MMloveConstants.UserFindPass;
	private final String SOAP_METHODNAME = MMloveConstants.UserFindPass;
	private final String SOAP_URL = MMloveConstants.URL002;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setTranslucentStatus() ;
		setContentView(R.layout.layout_findpass2);
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

		edt001 = (EditText)findViewById(R.id.newpass_titke_bar18);
		edt002 = (EditText)findViewById(R.id.edt_itle_bar19);

		mobile = this.getIntent().getExtras().getString("UserMobile")
				.toString();
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

	public void submit(View v){
		if(hasInternetConnected())
		{
			if(edt001.getText().toString().trim().equals(""))
			{
				Toast.makeText(ForgetPwd2Activity.this, R.string.str001_findpass2,
						Toast.LENGTH_SHORT).show();
				return;
			}
			else if(edt002.getText().toString().trim().equals(""))
			{
				Toast.makeText(ForgetPwd2Activity.this, R.string.str002_findpass2,
						Toast.LENGTH_SHORT).show();
				return;
			}
			else if(edt001.getText().toString().length()<6||edt002.getText().toString().length()>20)
			{


				Toast.makeText(ForgetPwd2Activity.this, R.string.str004_findpass2,
						Toast.LENGTH_SHORT).show();
				return;


			}else if(	edt001.getText().toString().contains(" ")){
				Toast.makeText(ForgetPwd2Activity.this, "输入内容含有非法字符",
						Toast.LENGTH_SHORT).show();
				return;
			}else if(	edt002.getText().toString().contains(" ")){
				Toast.makeText(ForgetPwd2Activity.this, "输入内容含有非法字符",
						Toast.LENGTH_SHORT).show();
				return;
			}
			else if(!IOUtils.isPass(edt002.getText().toString().trim())){
				Toast.makeText(ForgetPwd2Activity.this, "输入内容含有特殊字符",
						Toast.LENGTH_SHORT).show();
				return;
			}
			else if(!edt002.getText().toString().trim().equals(edt001.getText().toString()))
			{
				Toast.makeText(ForgetPwd2Activity.this, R.string.str003_findpass2,
						Toast.LENGTH_SHORT).show();
				return;
			}
			else
			{
				UserPassChange();
			}
		}
		else
		{
			Toast.makeText(ForgetPwd2Activity.this, R.string.msgUninternet,
					Toast.LENGTH_SHORT).show();
		}
	}
	/*
	 * 
	 */
	private void UserPassChange()
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
									.getJSONArray("UserFindPass");
							if(array.getJSONObject(0).has("MessageCode"))
							{
								for (int i = 0; i < array.length(); i++)
								{
									if(array.getJSONObject(i)
											.getString("MessageCode").equals("0"))
									{
									
										SuccussDialog(ForgetPwd2Activity.this);

//										DialogEnsureView dialogEnsureView = new DialogEnsureView(
//												User_FindPass2.this).setDialogMsg("信息提示！",
//														"密码修改成功，请重新登录！", "确定").setOnClickListenerEnsure(
//												new OnClickListener() {
//
//													@Override
//													public void onClick(View v) {
//														Intent i = new Intent(User_FindPass2.this, LoginActivity.class);
//														startActivity(i);
//														finish();
//
//
//													}
//												});
//										DialogUtils.showSelfDialog(User_FindPass2.this, dialogEnsureView);
//										new AlertDialog.Builder(User_FindPass2.this)
//										.setTitle(R.string.str005_findpass2)
//										.setMessage(R.string.str006_findpass2)
//										.setPositiveButton(R.string.btnOK,
//												new DialogInterface.OnClickListener()
//												{
//													public void onClick(
//															DialogInterface dialog,
//															int which)
//													{
//														// TODO Auto-generated method
//														// stub
//														Intent i = new Intent(User_FindPass2.this, LoginActivity.class);
//														startActivity(i);
//														finish();
//													}
//
//												}).show();
									}
									else
									{
										Toast.makeText(ForgetPwd2Activity.this, array.getJSONObject(0)
														.getString("MessageContent"),
												Toast.LENGTH_SHORT).show();
									}
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
					ForgetPwd2Activity.this, true, doProcess);
			Map<String, Object> paramMap = new HashMap<String, Object>();
			String str = "<Request><UserMobile>%s</UserMobile><Password>%s</Password></Request>";
			str = String.format(str, new Object[]
			{mobile,edt001.getText().toString() });
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
	public void SuccussDialog(final Context context){
		LayoutInflater inflater = LayoutInflater.from(this);
		View layout=inflater.inflate(R.layout.dialog_default_ensure,null);
		    
		AlertDialog.Builder builder =new AlertDialog.Builder(ForgetPwd2Activity.this);
		builder.setView(layout);
		builder.setCancelable(false);
		builder.create().show();
		TextView tvok=(TextView) layout.findViewById(R.id.dialog_default_click_ensure);
		TextView tvcontent=(TextView) layout.findViewById(R.id.dialog_default_click_text_msg);
		tvcontent.setText("找回密码成功");
		tvok.setOnClickListener(new OnClickListener(){
		  public void onClick(View v) {
			  Intent i = new Intent(ForgetPwd2Activity.this, LoginActivity.class);
				startActivity(i);
				finish();
		    }
		  });
	
		 }
	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.img_left:
				finish();
				break;
		}
	}

}
