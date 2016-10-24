package com.netlab.loveofmum;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Nickname;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.NumberKeyListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.igexin.sdk.PushManager;
import com.netlab.loveofmum.api.BaseActivity;
import com.netlab.loveofmum.api.MMloveConstants;
import com.netlab.loveofmum.api.JsonAsyncTaskOnComplete;
import com.netlab.loveofmum.api.JsonAsyncTask_Info;
import com.netlab.loveofmum.api.LocalAccessor;
import com.netlab.loveofmum.api.MyApplication;
import com.netlab.loveofmum.huanxin.MainChatActivity;
import com.netlab.loveofmum.model.User;
import com.netlab.loveofmum.utils.DialogUtils;
import com.netlab.loveofmum.utils.IOUtils;
import com.netlab.loveofmum.utils.SystemStatusManager;
import com.netlab.loveofmum.widget.DialogEnsureCancelView;
import com.netlab.loveofmum.widget.DialogEnsureView;
import com.umeng.analytics.MobclickAgent;


public class User_Register extends BaseActivity
{
	private TextView txtHead;
	private ImageView imgBack;
	
	private EditText edt001;
	private EditText edt002;
	private EditText edt003;
	private EditText edt004;
	private EditText edt005;
	private EditText edpasscode;
	
	private EditText edt006;
	
	private String returnvalue001;
	
	private RadioGroup group;
	
	private Button btnOK;
	
	private String UserMobile;
	private String Password;
	private String RealName;
	private String UserNO;
	private String UserSex;
	private String FromTo = "5";
	
	private String UserAge;
	
	private User user;
	
	private String ClientID;
	
	private Button btnchkcode;
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
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setTranslucentStatus() ;
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.layout_register);
		MyApplication.getInstance().addActivity(this);
		iniView();
		
		setListeners();
		ClientID = PushManager.getInstance().getClientid(User_Register.this);
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
		txtHead = (TextView) findViewById(R.id.txtHead);
		imgBack = (ImageView) findViewById(R.id.img_left);

		txtHead.setText("新用户注册");
		
		btnOK = (Button)findViewById(R.id.btnOK);
		
		edt001 = (EditText)findViewById(R.id.phone_register7);
		edt002 = (EditText)findViewById(R.id.pass_register8);
		edt003 = (EditText)findViewById(R.id.edt001_layout_findpass);
		edt004 = (EditText)findViewById(R.id.name_title_bar10);
		edt005 = (EditText)findViewById(R.id.id_title_bar11);
		
		edt006 = (EditText)findViewById(R.id.txt01_userinfochange);

		btnchkcode = (Button)findViewById(R.id.verification_titlt_bar9);
		
		group = (RadioGroup)findViewById(R.id.radioGroup);
		
		user = LocalAccessor.getInstance(User_Register.this).getUser();
		edpasscode=(EditText) findViewById(R.id.pass_code);
	}

	private void setListeners()
	
	{
//		edt002.addTextChangedListener(new TextWatcher() {
//	        @Override
//	        public void onTextChanged(CharSequence s, int start, int before, int count) {
//	            // TODO Auto-generated method stub
//	             String location_name=s.toString();       
//	             if (location_name.matches("^[A-Za-z0-9 _-]+$")){
//	                Toast.makeText(getApplicationContext(),"请输入字母或者数字!",Toast.LENGTH_SHORT).show();
//	             }else{
//	                Toast.makeText(getApplicationContext(),"类型不符!",Toast.LENGTH_SHORT).show();                  
//	               
//	             }
//	            }
//
//			@Override
//			public void beforeTextChanged(CharSequence s, int start, int count,
//					int after) {
//				// TODO Auto-generated method stub
//				
//			}
//
//			@Override
//			public void afterTextChanged(Editable s) {
//				// TODO Auto-generated method stub
//				
//			}
//		});
		imgBack.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		btnchkcode.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				if(hasInternetConnected())
				{
					if(edt001.getText().toString().trim().equals(""))
					{
						Toast.makeText(User_Register.this, R.string.str001_register,
								Toast.LENGTH_SHORT).show();
					}
					else
					{
						if(IOUtils.isMobileNO(edt001.getText().toString().trim()))
						{
							mobile = edt001.getText().toString().trim();
							
							btnchkcode.setEnabled(false);
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
							
							CHKCodeSend();
						}
						else
						{
							Toast.makeText(User_Register.this, R.string.str001_findpass,
									Toast.LENGTH_SHORT).show();
						}
					}
				}
				else
				{
					Toast.makeText(User_Register.this, R.string.msgUninternet,
							Toast.LENGTH_SHORT).show();
				}
				
			}
		});

		btnOK.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				if(hasInternetConnected())
				{
					// TODO Auto-generated method stub
					if(edt001.getText().toString().trim().equals(""))
					{
						Toast.makeText(User_Register.this, R.string.str001_register,
								Toast.LENGTH_SHORT).show();
						return;
					}
					else if(!IOUtils.isMobileNO(edt001.getText().toString().trim()))
					{
						Toast.makeText(User_Register.this, R.string.str001_findpass,
								Toast.LENGTH_SHORT).show();
						return;
					}
					else
					{
						UserMobile = edt001.getText().toString().trim();
					}
					if(edt002.getText().toString().trim().equals(""))
					{
						Toast.makeText(User_Register.this, R.string.str002_register,
								Toast.LENGTH_SHORT).show();
						return;
					}
					else
					{
						if(edt002.getText().toString().length()<6||edt002.getText().toString().length()>20)
						{
							
							Toast.makeText(User_Register.this, R.string.str003_register,
									Toast.LENGTH_SHORT).show();
							return;
						}
						else
						{ 
							if(edt002.getText().toString().contains(" ")){
								Toast.makeText(User_Register.this, "输入内容含有非法字符",
										Toast.LENGTH_SHORT).show();
								return;
							}
							else{
								if(IOUtils.isPass(edt002.getText().toString())){
									Password = edt002.getText().toString();
								}else{
									Toast.makeText(User_Register.this, "输入内容含有特殊字符",
											Toast.LENGTH_SHORT).show();
									return;
								}
							}
						}
					}
					
					// TODO Auto-generated method stub
					if(edt003.getText().toString().trim().equals(chkcode)&&edt001.getText().toString().trim().equals(mobile))
					{
						
					}
					else
					{
						Toast.makeText(User_Register.this, R.string.str002_findpass,
								Toast.LENGTH_SHORT).show();
						return;
					}
					
					if(edt004.getText().toString().trim().equals(""))
					{
						RealName = edt004.getText().toString().trim();
					}
					else
					{
						if(!IOUtils.isName(edt004.getText().toString().trim()))
						{
							Toast.makeText(User_Register.this, R.string.str007_userinfochange,
									Toast.LENGTH_SHORT).show();
							return;
						}
						else
						{
							RealName = edt004.getText().toString().trim();
						}
					}
					
				
					if(!edt005.getText().toString().trim().equals(""))
					{
						if(!IOUtils.isUserNO(edt005.getText().toString().trim()))
						{
							Toast.makeText(User_Register.this, R.string.str007_register,
									Toast.LENGTH_SHORT).show();
							return;
						}
					}
					UserNO = edt005.getText().toString().trim();
					
					
					UserSex = "02";
					
					int radioButtonId = group.getCheckedRadioButtonId();
					RadioButton rb = (RadioButton)User_Register.this.findViewById(radioButtonId);
					
					if(rb.getText().toString().trim().equals("男"))
					{
						UserSex = "01";
					}
					else
					{
						UserSex = "02";
					}
					
					if(!edt006.getText().toString().trim().equals(""))
					{
						if(!IOUtils.isBeyondAge(Integer.parseInt(edt006.getText().toString().trim())))
						{
							Toast.makeText(User_Register.this, R.string.str008_register,
									Toast.LENGTH_SHORT).show();
							return;
						}
						else
						{
							UserAge = edt006.getText().toString().trim();
						}
					}
					else
					{
						UserAge = edt006.getText().toString().trim();
					}
					
					UserRegister();
				}
				else
				{
					Toast.makeText(User_Register.this, R.string.msgUninternet,
							Toast.LENGTH_SHORT).show();
				}
			}
		});
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
								

												
												user = LocalAccessor.getInstance(User_Register.this).getUser();
												
												try
												{
													user.UserID = Integer.valueOf(array.getJSONObject(0).getString("UserID").toString());
													user.NickName=array.getJSONObject(0).getString("NickName").toString();
													user.UserMobile = UserMobile;
													user.RealName = RealName;
													user.UserNO = UserNO;
													user.PayId="";
													
													LocalAccessor.getInstance(User_Register.this).updateUser(user);
													Intent intent = new Intent();
													intent.setAction("action.login");
													sendBroadcast(intent);

															if(array.getJSONObject(0).has("PointAddCode")) {
																if(array.getJSONObject(0).getString("PointAddCode").equals("0")) {
																	LayoutInflater inflater = LayoutInflater.from(User_Register.this);
																	View layout=inflater.inflate(R.layout.dialog_default_ensure,null);

																	AlertDialog.Builder builder =new AlertDialog.Builder(User_Register.this);
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
								Toast.makeText(User_Register.this, array.getJSONObject(0).getString("MessageContent"),
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
					User_Register.this, true, doProcess);
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
					User_Register.this, true, doProcess);
			Map<String, Object> paramMap = new HashMap<String, Object>();
			String str = "<Request><UserMobile>%s</UserMobile><Flag>%s</Flag></Request>";
			str = String.format(str, new Object[]
			{edt001.getText().toString(),"001" });
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
