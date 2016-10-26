package com.netlab.loveofmum.activity;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.igexin.sdk.PushManager;
import com.netlab.loveofmum.R;
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


public class RegistActivity extends BaseActivity implements  View.OnClickListener
{

	
	private EditText phone;
	private EditText pwd;
	private EditText input_code;
	private EditText name;
	private EditText identID;
	private EditText year;
	private EditText edpasscode;		//邀请码
	

	
	private String returnvalue001;
	
	private RadioGroup group;

	
	private String UserMobile;
	private String Password;
	private String RealName;
	private String UserNO;
	private String UserSex;
	private String FromTo = "5";
	
	private String UserAge;
	
	private User user;
	
	private String ClientID;
	
	private Button getcode;
	private String chkcode;
	
	private TimerTask task;
	
	private int time = 120;
	private Timer timer = new Timer();
	
	private String mobile;
	
	
	// 定义Web Service相关的字符串
	private final String SOAP_NAMESPACE = MMloveConstants.URL001;
	private final String SOAP_ACTION = MMloveConstants.URL001
			+ MMloveConstants.UserRegister;
	private final String SOAP_METHODNAME = MMloveConstants.UserRegister;
	private final String SOAP_URL = MMloveConstants.URL002;
	
	private final String SOAP_ACTION2 = MMloveConstants.URL001
			+ MMloveConstants.SendAuthCode;
	private final String SOAP_METHODNAME2 = MMloveConstants.SendAuthCode;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setTranslucentStatus() ;
		setContentView(R.layout.layout_register);
		MyApplication.getInstance().addActivity(this);
		iniView();
		ClientID = PushManager.getInstance().getClientid(RegistActivity.this);
	}
	@Override
	protected void iniView()
	{
		// TODO Auto-generated method stub
		findViewById(R.id.img_left).setOnClickListener(this);
		((TextView) findViewById(R.id.txtHead)).setText("新用户注册");
		phone = (EditText)findViewById(R.id.phone_register7);
		pwd = (EditText)findViewById(R.id.pass_register8);
		input_code = (EditText)findViewById(R.id.edt001_layout_findpass);
		name = (EditText)findViewById(R.id.name_title_bar10);
		identID = (EditText)findViewById(R.id.id_title_bar11);
		year = (EditText)findViewById(R.id.txt01_userinfochange);

		getcode = (Button)findViewById(R.id.getcode);
		getcode.setOnClickListener(this);
		group = (RadioGroup)findViewById(R.id.radioGroup);

		user = LocalAccessor.getInstance(RegistActivity.this).getUser();
		edpasscode=(EditText) findViewById(R.id.pass_code);
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

	public  void submit(View v){
		if(hasInternetConnected())
		{
			// TODO Auto-generated method stub
			if(phone.getText().toString().trim().equals(""))
			{
				Toast.makeText(RegistActivity.this, R.string.str001_register,
						Toast.LENGTH_SHORT).show();
				return;
			}
			else if(!IOUtils.isMobileNO(phone.getText().toString().trim()))
			{
				Toast.makeText(RegistActivity.this, R.string.str001_findpass,
						Toast.LENGTH_SHORT).show();
				return;
			}
			else
			{
				UserMobile = phone.getText().toString().trim();
			}
			if(pwd.getText().toString().trim().equals(""))
			{
				Toast.makeText(RegistActivity.this, R.string.str002_register,
						Toast.LENGTH_SHORT).show();
				return;
			}
			else
			{
				if(pwd.getText().toString().length()<6||pwd.getText().toString().length()>20)
				{

					Toast.makeText(RegistActivity.this, R.string.str003_register,
							Toast.LENGTH_SHORT).show();
					return;
				}
				else
				{
					if(pwd.getText().toString().contains(" ")){
						Toast.makeText(RegistActivity.this, "输入内容含有非法字符",
								Toast.LENGTH_SHORT).show();
						return;
					}
					else{
						if(IOUtils.isPass(pwd.getText().toString())){
							Password = pwd.getText().toString();
						}else{
							Toast.makeText(RegistActivity.this, "输入内容含有特殊字符",
									Toast.LENGTH_SHORT).show();
							return;
						}
					}
				}
			}

			// TODO Auto-generated method stub
			if(input_code.getText().toString().trim().equals(chkcode)&&phone.getText().toString().trim().equals(mobile))
			{
			}
			else
			{
				Toast.makeText(RegistActivity.this, R.string.str002_findpass,
						Toast.LENGTH_SHORT).show();
				return;
			}
			if(name.getText().toString().trim().equals(""))
			{
				RealName = name.getText().toString().trim();
			}
			else
			{
				if(!IOUtils.isName(name.getText().toString().trim()))
				{
					Toast.makeText(RegistActivity.this, R.string.str007_userinfochange,
							Toast.LENGTH_SHORT).show();
					return;
				}
				else
				{
					RealName = name.getText().toString().trim();
				}
			}
			if(!identID.getText().toString().trim().equals(""))
			{
				if(!IOUtils.isUserNO(identID.getText().toString().trim()))
				{
					Toast.makeText(RegistActivity.this, R.string.str007_register,
							Toast.LENGTH_SHORT).show();
					return;
				}
			}
			UserNO = identID.getText().toString().trim();
			UserSex = "02";
			int radioButtonId = group.getCheckedRadioButtonId();
			RadioButton rb = (RadioButton)RegistActivity.this.findViewById(radioButtonId);

			if(rb.getText().toString().trim().equals("男"))
			{
				UserSex = "01";
			}
			else
			{
				UserSex = "02";
			}
			if(!year.getText().toString().trim().equals(""))
			{
				if(!IOUtils.isBeyondAge(Integer.parseInt(year.getText().toString().trim())))
				{
					Toast.makeText(RegistActivity.this, R.string.str008_register,
							Toast.LENGTH_SHORT).show();
					return;
				}
				else
				{
					UserAge = year.getText().toString().trim();
				}
			}
			else
			{
				UserAge = year.getText().toString().trim();
			}

			UserRegister();
		}
		else
		{
			Toast.makeText(RegistActivity.this, R.string.msgUninternet,
					Toast.LENGTH_SHORT).show();
		}
	}
	private void UserRegister()
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
							final JSONArray array = mySO.getJSONArray("UserRegister");

							if (array.getJSONObject(0).has("UserID"))
							{



								user = LocalAccessor.getInstance(RegistActivity.this).getUser();

								try
								{
									user.UserID = Integer.valueOf(array.getJSONObject(0).getString("UserID").toString());
									user.NickName=array.getJSONObject(0).getString("NickName").toString();
									user.UserMobile = UserMobile;
									user.RealName = RealName;
									user.UserNO = UserNO;
									user.PayId="";

									LocalAccessor.getInstance(RegistActivity.this).updateUser(user);
									Intent intent = new Intent();
									intent.setAction("action.login");
									sendBroadcast(intent);

									if(array.getJSONObject(0).has("PointAddCode")) {
										if(array.getJSONObject(0).getString("PointAddCode").equals("0")) {
											LayoutInflater inflater = LayoutInflater.from(RegistActivity.this);
											View layout=inflater.inflate(R.layout.dialog_default_ensure,null);

											AlertDialog.Builder builder =new AlertDialog.Builder(RegistActivity.this);
											builder.setView(layout);
											builder.setCancelable(false);
											builder.create().show();
											TextView tvok=(TextView) layout.findViewById(R.id.dialog_default_click_ensure);
											TextView tvcontent=(TextView) layout.findViewById(R.id.dialog_default_click_text_msg);
											tvcontent.setText(array.getJSONObject(0).getString("PointAddContent") + "+" + array.getJSONObject(0).getString("PointValue"));
											tvok.setOnClickListener(new OnClickListener(){
												public void onClick(View v) {
													setResult(1);
													finish();

												}
											});

//																	Intent i = new Intent(User_Register.this, DialogEnsure.class);
//																	i.putExtra("content", array.getJSONObject(0).getString("PointAddContent") + "+" + array.getJSONObject(0).getString("PointValue"));
//																	startActivity(i);
										}
									}



								}
								catch (NumberFormatException e)
								{
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								catch (JSONException e)
								{
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								catch (Exception e)
								{
									// TODO Auto-generated catch block
									e.printStackTrace();
								}






							}
//								DialogUtils.showSelfDialog(User_Register.this, dialogEnsureView);
//								new AlertDialog.Builder(User_Register.this)
//								.setTitle(R.string.str005_register)
//								.setMessage(R.string.str006_register)
//								.setPositiveButton(R.string.btnOK,
//										new DialogInterface.OnClickListener()
//										{
//											public void onClick(
//													DialogInterface dialog,
//													int which)
//											{
//												// TODO Auto-generated method
//												// stub
////												Intent i = new Intent(User_Register.this, User_Set.class);
////												startActivity(i);
//
//												user = LocalAccessor.getInstance(User_Register.this).getUser();
//
//												try
//												{
//													user.UserID = Integer.valueOf(array.getJSONObject(0).getString("UserID").toString());
//													user.NickName=array.getJSONObject(0).getString("NickName").toString();
//													user.UserMobile = UserMobile;
//													user.RealName = RealName;
//													user.UserNO = UserNO;
//
//
//													LocalAccessor.getInstance(User_Register.this).updateUser(user);
//													mockLoginData(user);
//												}
//												catch (NumberFormatException e)
//												{
//													// TODO Auto-generated catch block
//													e.printStackTrace();
//												}
//												catch (JSONException e)
//												{
//													// TODO Auto-generated catch block
//													e.printStackTrace();
//												}
//												catch (Exception e)
//												{
//													// TODO Auto-generated catch block
//													e.printStackTrace();
//												}
//
//												setResult(1);
//												finish();
//											}
//
//										}).show();
//							}
							else
							{
								Toast.makeText(RegistActivity.this, array.getJSONObject(0).getString("MessageContent"),
										Toast.LENGTH_SHORT).show();
							}

						}
						catch (Exception ex)
						{
							ex.printStackTrace();
						}
					}
				}
			};
			String pass=edpasscode.getText().toString().trim();

			JsonAsyncTask_Info task = new JsonAsyncTask_Info(
					RegistActivity.this, true, doProcess);
			Map<String, Object> paramMap = new HashMap<String, Object>();
			String str = "<Request><UserMobile>%s</UserMobile><Password>%s</Password><UserNO>%s</UserNO><RealName>%s</RealName><UserSex>%s</UserSex><ClientID>%s</ClientID><FromTo>%s</FromTo><YuBirthTime>%s</YuBirthTime><PhoneType>%s</PhoneType><DeviceToken>%s</DeviceToken><UserAge>%s</UserAge><BYQM>%s</BYQM></Request>";
			str = String.format(str, new Object[]
					{UserMobile,Password,UserNO,RealName,UserSex,ClientID,FromTo,user.YuBirthDate,"0","",UserAge,pass});


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
			case R.id.getcode:
				if(hasInternetConnected())
				{
					if(phone.getText().toString().trim().equals(""))
					{
						Toast.makeText(RegistActivity.this, R.string.str001_register,
								Toast.LENGTH_SHORT).show();
					}
					else
					{
						if(IOUtils.isMobileNO(phone.getText().toString().trim()))
						{
							mobile = phone.getText().toString().trim();

							getcode.setEnabled(false);
							task = new TimerTask() {
								@Override
								public void run() {

									runOnUiThread(new Runnable() { // UI thread
										@Override
										public void run() {
											if (time <= 0) {
												getcode.setEnabled(true);
												getcode.setText("获取验证码");
												task.cancel();
											} else {
												getcode.setText("" + time);
											}
											time--;
										}
									});
								}
							};
							time = 120;
							timer.schedule(task, 0, 1000);

							CHKCodeSend();
						}
						else
						{
							Toast.makeText(RegistActivity.this, R.string.str001_findpass,
									Toast.LENGTH_SHORT).show();
						}
					}
				}
				else
				{
					Toast.makeText(RegistActivity.this, R.string.msgUninternet,
							Toast.LENGTH_SHORT).show();
				}
				break;
		}
	}
	/*
	 * 发送验证码
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
//					Toast.makeText(User_Register.this,result.toString(),1).show();
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
							for (int i = 0; i < array.length(); i++)
							{
								chkcode = array.getJSONObject(i)
										.getString("MessageContent");
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
					RegistActivity.this, true, doProcess);
			Map<String, Object> paramMap = new HashMap<String, Object>();
			String str = "<Request><UserMobile>%s</UserMobile><Flag>%s</Flag></Request>";
			str = String.format(str, new Object[]
					{phone.getText().toString(),"001" });
			paramMap.put("str", str);

			// 必须是这5个参数，而且得按照顺序
			task.execute(SOAP_NAMESPACE, SOAP_ACTION2, SOAP_METHODNAME2,
					SOAP_URL, paramMap);
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
