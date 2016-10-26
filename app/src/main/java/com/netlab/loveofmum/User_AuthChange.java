package com.netlab.loveofmum;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

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

public class User_AuthChange extends BaseActivity
{
	private TextView txtHead;
	private ImageView imgBack;
	
	private EditText edt001;
	private EditText edt002;
	private EditText edt003;
	
	private String returnvalue001;
	
	private String Password;
	
	private Button btnChange;
	
	private String OldPass;
	private String NewPass;
	private String NewPass2;
	
	private User user;
	
	// 定义Web Service相关的字符串
	private final String SOAP_NAMESPACE = MMloveConstants.URL001;
	private final String SOAP_ACTION = MMloveConstants.URL001
			+ MMloveConstants.UserAuthChange;
	private final String SOAP_METHODNAME = MMloveConstants.UserAuthChange;
	private final String SOAP_URL = MMloveConstants.URL002;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setTranslucentStatus() ;
		setContentView(R.layout.layout_userauthchange);
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
		tintManager.setStatusBarTintResource(R.color.home);//状态栏无背景
	}

	@Override
	protected void iniView()
	{
		// TODO Auto-generated method stub
		txtHead = (TextView) findViewById(R.id.txtHead);
		imgBack = (ImageView) findViewById(R.id.img_left);

		txtHead.setText("密码修改");
		
		btnChange = (Button)findViewById(R.id.btn01_userauthchange);
		
		edt001 = (EditText)findViewById(R.id.edt_title_bar17);
		edt002 = (EditText)findViewById(R.id.newpass_titke_bar18);
		edt003 = (EditText)findViewById(R.id.edt_itle_bar19);
		
		user = LocalAccessor.getInstance(User_AuthChange.this).getUser();
	}

	private void setListeners()
	{
		imgBack.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				finish();
			}
		});

		btnChange.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				if(edt001.getText().toString().trim().equals(""))
				{
					Toast.makeText(User_AuthChange.this, R.string.str001_authchange,
							Toast.LENGTH_SHORT).show();
					return;
				}
				else
				{
					OldPass = edt001.getText().toString().trim();
				}
				if(edt002.getText().toString().trim().equals(""))
				{
					Toast.makeText(User_AuthChange.this, R.string.str002_authchange,
							Toast.LENGTH_SHORT).show();
					return;
				}
				else
				{
					NewPass = edt002.getText().toString().trim();
				}
				
				if(!edt003.getText().toString().trim().equals(edt002.getText().toString().trim()))
				{
					Toast.makeText(User_AuthChange.this, R.string.str003_authchange,
							Toast.LENGTH_SHORT).show();
					return;
				}
				if(edt001.getText().toString().trim().equals(edt002.getText().toString().trim()))
				{
					Toast.makeText(User_AuthChange.this, R.string.str008_authchange,
							Toast.LENGTH_SHORT).show();
					return;
				}
				if(edt002.getText().toString().trim().length()<6||edt002.getText().toString().trim().length()>20)
				{
					Toast.makeText(User_AuthChange.this, R.string.str004_authchange,
							Toast.LENGTH_SHORT).show();
					return;
				}
				if(edt002.getText().toString().contains(" ")){
					Toast.makeText(User_AuthChange.this, "输入内容含有非法字符",
							Toast.LENGTH_SHORT).show();
					return;
				}
				if(edt003.getText().toString().contains(" ")){
					Toast.makeText(User_AuthChange.this, "输入内容含有非法字符",
							Toast.LENGTH_SHORT).show();
					return;
				}
			
				if(!IOUtils.isPass(edt002.getText().toString().trim())){
					Toast.makeText(User_AuthChange.this, "密码必须是数字和字母的组合",
							Toast.LENGTH_SHORT).show();
					return;
				}
				if(!IOUtils.isPass(edt003.getText().toString().trim())){
					Toast.makeText(User_AuthChange.this, "密码必须是数字和字母的组合",
							Toast.LENGTH_SHORT).show();
					return;
				}
				
				
				if(hasInternetConnected())
				{
					UserAuthChange();
				}
				else
				{
					Toast.makeText(User_AuthChange.this, R.string.msgUninternet,
							Toast.LENGTH_SHORT).show();
				}
				
			}
		});
	}
	
	private void UserAuthChange()
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
							JSONArray array = mySO.getJSONArray("UserAuthChange");

							if (array.getJSONObject(0).getString("MessageCode")
									.equals("0"))
							{
								showSuccuss();
//								DialogEnsureView dialogEnsureView = new DialogEnsureView(
//										User_AuthChange.this).setDialogMsg("信息提示!",
//												"恭喜你，密码修改成功！", "确定").setOnClickListenerEnsure(
//										new OnClickListener() {
//
//											@Override
//											public void onClick(View v) {
//												Intent i = new Intent(User_AuthChange.this, MainTabActivity.class);
//												i.putExtra("TabIndex", "D_TAB");
//												startActivity(i);
//												
//												
//											}
//										});
//								DialogUtils.showSelfDialog(User_AuthChange.this, dialogEnsureView);
//								new AlertDialog.Builder(User_AuthChange.this)
//								.setTitle(R.string.str005_authchange)
//								.setMessage(R.string.str006_authchange)
//								.setPositiveButton(R.string.btnOK,
//										new DialogInterface.OnClickListener()
//										{
//											public void onClick(
//													DialogInterface dialog,
//													int which)
//											{
//												// TODO Auto-generated method
//												// stub
//												Intent i = new Intent(User_AuthChange.this, MainTabActivity.class);
//												i.putExtra("TabIndex", "D_TAB");
//												startActivity(i);
//											}
//											
//										}).show();
							}
							else
							{
								Toast.makeText(User_AuthChange.this, array.getJSONObject(0).getString("MessageContent"),
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

			JsonAsyncTask_Info task = new JsonAsyncTask_Info(
					User_AuthChange.this, true, doProcess);
			Map<String, Object> paramMap = new HashMap<String, Object>();
			String str = "<Request><UserID>%s</UserID><UserMobile>%s</UserMobile><Password>%s</Password><NewPassword>%s</NewPassword></Request>";
			str = String.format(str, new Object[]
			{String.valueOf(user.UserID),user.UserMobile,OldPass,NewPass});
			
			
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

	protected void showSuccuss() {
		// TODO Auto-generated method stub
		LayoutInflater inflater = LayoutInflater.from(this);
		View layout=inflater.inflate(R.layout.dialog_default_ensure,null);
		    
		AlertDialog.Builder builder =new AlertDialog.Builder(User_AuthChange.this);
		builder.setView(layout);
		builder.setCancelable(false);
		builder.create().show();
		TextView tvok=(TextView) layout.findViewById(R.id.dialog_default_click_ensure);
		TextView tvcontent=(TextView) layout.findViewById(R.id.dialog_default_click_text_msg);
		tvcontent.setText("恭喜你，密码修改成功！");
		tvok.setOnClickListener(new OnClickListener(){
		  public void onClick(View v) {
				Intent i = new Intent(User_AuthChange.this, MainTabActivity.class);
				i.putExtra("TabIndex", "D_TAB");
				startActivity(i);
				finish();
		    }
		  });
	}
	
}
