package com.netlab.loveofmum.activity;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.igexin.sdk.PushManager;
import com.netlab.loveofmum.FeedbackAct;
import com.netlab.loveofmum.R;
import com.netlab.loveofmum.User_FindPass;
import com.netlab.loveofmum.User_Register;
import com.netlab.loveofmum.api.BaseActivity;
import com.netlab.loveofmum.api.MMloveConstants;
import com.netlab.loveofmum.api.JsonAsyncTaskOnComplete;
import com.netlab.loveofmum.api.JsonAsyncTask_Info;
import com.netlab.loveofmum.api.LocalAccessor;
import com.netlab.loveofmum.api.MyApplication;
import com.netlab.loveofmum.model.User;
import com.netlab.loveofmum.utils.IOUtils;
import com.netlab.loveofmum.utils.SystemStatusManager;
import com.umeng.analytics.MobclickAgent;


public class LoginActivity extends BaseActivity implements View.OnClickListener
{

	private EditText phone;
	private EditText pwd;

	private String ClientID;
	
	private User user;
	private Intent mIntent;
	String page="";
	// 定义Web Service相关的字符串
	private final String SOAP_NAMESPACE = MMloveConstants.URL001;
	private final String SOAP_ACTION = MMloveConstants.URL001 + MMloveConstants.UserLogin;
	private final String SOAP_METHODNAME = MMloveConstants.UserLogin;
	private final String SOAP_URL = MMloveConstants.URL002;
//	//友盟微社区
//	  public static LoginListener sLoginListener;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setTranslucentStatus() ;
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.layout_login);
		MyApplication.getInstance().addActivity(this);
		mIntent=this.getIntent();
		if(mIntent!=null&&mIntent.getStringExtra("Page")!=null){
			page=mIntent.getStringExtra("Page");
		}
		iniView();
		ClientID = PushManager.getInstance().getClientid(LoginActivity.this);
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
		findViewById(R.id.img_left).setOnClickListener(this);
		((TextView) findViewById(R.id.txtHead)).setText("用户登录");
		// TODO Auto-generated method stub
		phone = (EditText) findViewById(R.id.phone_login);
		pwd = (EditText) findViewById(R.id.pass_login);
		findViewById(R.id.forget_pwd).setOnClickListener(this);
	findViewById(R.id.regist).setOnClickListener(this);

		user = LocalAccessor.getInstance(LoginActivity.this).getUser();
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

	private void UserLogin(String username,String pwd)
	{
		try
		{
			JsonAsyncTaskOnComplete doProcess = new JsonAsyncTaskOnComplete()
			{
				public void processJsonObject(Object result)
				{
					String Phone_Login = result.toString();

					if (Phone_Login == null)
					{

					}
					else
					{
						// 下边是具体的处理u
						try
						{
							JSONObject mySO = (JSONObject) result;
							org.json.JSONArray array = mySO
									.getJSONArray("UserLogin");

							if (array.getJSONObject(0).has("MessageCode"))
							{
								Toast.makeText(LoginActivity.this,array.getJSONObject(0).getString("MessageContent").toString(),
										Toast.LENGTH_SHORT).show();
							}
							else
							{
								String UserID = array.getJSONObject(0).getString("UserID").toString();
								
								User user = LocalAccessor.getInstance(LoginActivity.this).getUser();
								user.UserID = Integer.valueOf(UserID);
								user.NickName = array.getJSONObject(0).getString("NickName").toString();
								if(!array.getJSONObject(0).getString("YuBirthTime").toString().equals(""))
								{
							     user.YuBirthDate = IOUtils.formatStrDate(array.getJSONObject(0).getString("YuBirthTime").toString());
									
//								user.YuBirthDate = IOUtils.formatStrDate("2016-06-13");
								}
								
								
							
							String url=array.getJSONObject(0).getString("PictureURL").toString().replace("~", "").replace("\\", "/");
							if(url==null){
								url=MMloveConstants.JE_BASE_URL3+"/img/icon_user_normal.png";

							}else if(url.contains("vine.gif")){
								int end=url.lastIndexOf("/"); 
								url=url.substring(0, end);
								url=MMloveConstants.JE_BASE_URL3+url+"/icon_user_normal.png";
							}else{
								url=MMloveConstants.JE_BASE_URL3+url;
							}
							user.PicURL =url;
							user.RealName = array.getJSONObject(0).getString("RealName").toString();
							user.UserMobile = array.getJSONObject(0).getString("Mobile").toString();
							user.UserNO = array.getJSONObject(0).getString("UserNO").toString();
							
							LocalAccessor.getInstance(LoginActivity.this).updateUser(user);
								Intent intent = new Intent();
								intent.setAction("action.login");
								sendBroadcast(intent);
//							mockLoginData(user);
								
								if(page.equals("Feedback")){
									startActivity(new Intent(LoginActivity.this,FeedbackAct.class));
									
								}else{
								
								setResult(1);
								}
								
								finish();
							}

						}
						catch (Exception ex)
						{
							ex.printStackTrace();
							finish();
						}
					}
				}
			};

			JsonAsyncTask_Info task = new JsonAsyncTask_Info(LoginActivity.this, true,
					doProcess);
			Map<String, Object> paramMap = new HashMap<String, Object>();
			String str = "<Request><UserMobile>%s</UserMobile><Password>%s</Password><ClientID>%s</ClientID><YuBirthTime>%s</YuBirthTime><PhoneType>%s</PhoneType><DeviceToken>%s</DeviceToken></Request>";
			str = String.format(str, new Object[]
			{ username, pwd,ClientID,user.YuBirthDate,"0",""});
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

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.img_left:
				finish();
				break;
			case R.id.forget_pwd:
				Intent intent = new Intent();
				intent.setClass(LoginActivity.this, User_FindPass.class);
				startActivity(intent);
				finish();
				break;
			case R.id.regist:
				// TODO Auto-generated method stub
				Intent intenttwo = new Intent();
				intenttwo.setClass(LoginActivity.this, User_Register.class);
				startActivity(intenttwo);
				finish();
				break;
		}
	}
	public void submit(View v){
		if(hasInternetConnected())
		{
			String username="";
			String pwdstr = "";
			// TODO Auto-generated method stub
			if (phone.getText().toString().trim().equals(""))
			{
				Toast.makeText(LoginActivity.this, "请输入您的账号", Toast.LENGTH_SHORT)
						.show();
				return;
			}
			else
			{
				if (IOUtils.isMobileNO(phone.getText().toString().trim()))
				{
					username = phone.getText().toString().trim();
				}
				else
				{
					Toast.makeText(LoginActivity.this, "请输入正确的手机号！",
							Toast.LENGTH_SHORT).show();
					return;
				}
			}
			if (pwd.getText().toString().trim().equals(""))
			{
				Toast.makeText(LoginActivity.this, "请输入您的密码",
						Toast.LENGTH_SHORT).show();
				return;
			}
			else
			{
				pwdstr = pwd.getText().toString().trim();
			}
			UserLogin(username,pwdstr);
		}
		else
		{
			Toast.makeText(LoginActivity.this, R.string.msgUninternet,
					Toast.LENGTH_SHORT).show();
		}
	}
}

