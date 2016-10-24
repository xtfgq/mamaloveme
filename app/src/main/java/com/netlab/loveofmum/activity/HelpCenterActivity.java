package com.netlab.loveofmum.activity;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.netlab.loveofmum.CHK_HandleBook;
import com.netlab.loveofmum.DownloadServiceForAPK;
import com.netlab.loveofmum.FeedbackAct;
import com.netlab.loveofmum.R;
import com.netlab.loveofmum.api.BaseActivity;
import com.netlab.loveofmum.api.MMloveConstants;
import com.netlab.loveofmum.api.JsonAsyncTaskOnComplete;
import com.netlab.loveofmum.api.JsonAsyncTask_Info;
import com.netlab.loveofmum.api.LocalAccessor;
import com.netlab.loveofmum.api.MyApplication;
import com.netlab.loveofmum.model.User;
import com.netlab.loveofmum.utils.DialogUtils;
import com.netlab.loveofmum.utils.SystemStatusManager;
import com.netlab.loveofmum.widget.DialogEnsureCancelView;
import com.umeng.analytics.MobclickAgent;

/**
 * 帮助中心
 */
public class HelpCenterActivity extends BaseActivity implements OnClickListener{
	private View llcheckVersion,llfeedback,llphone,llfunction,sqrule,sqhandlebook;
	
	protected String returnvalue001;
	private TextView versioncode,versintxt,tel;
	private int newCode;
	String appVersion;
	private int oldCode;
	private String versionlog;
	private User user;

	
	private final String SOAP_NAMESPACE = MMloveConstants.URL001;
	private final String SOAP_URL = MMloveConstants.URL002;
	
	private final String SOAP_ACTION3 = MMloveConstants.URL001
			+ MMloveConstants.VersionInquiry;
	private final String SOAP_METHODNAME3 = MMloveConstants.VersionInquiry;
	private Handler handler = new Handler() {
	@Override
	public void handleMessage(Message msg) {
		super.handleMessage(msg);
	
		switch (msg.what) {
		case 0:
			versioncode.setText("v"+appVersion);
		
			break;
		case 1:
			versioncode.setText("更新");
			versioncode.setTextColor(getResources().getColor(R.color.white));
			versioncode.setBackgroundResource(R.drawable.gengxin);
			break;
	
		
		default:
			break;
		}
		
	}
};
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		user = LocalAccessor.getInstance(HelpCenterActivity.this).getUser();
		MobclickAgent.onResume(this);
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	    requestWindowFeature(Window.FEATURE_NO_TITLE);
		setTranslucentStatus() ;
		setContentView(R.layout.activity_help);
		iniView();
		MyApplication.getInstance().addActivity(this);
		PackageManager manager = HelpCenterActivity.this.getPackageManager();
		PackageInfo info;
		try {
			info = manager.getPackageInfo(HelpCenterActivity.this.getPackageName(), 0);
			appVersion = info.versionName; // 版本名
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		versioninquiry();
		
		
		versintxt.setText("V"+appVersion);
	}
	/*
	 * 查询版本信息
	 */
	private void versioninquiry()
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
							JSONArray array = mySO.getJSONArray("VersionInquiry");
							if (array.getJSONObject(0).has("MessageCode"))
							{
								
							}
							else
							{
								for (int i = 0; i < array.length(); i++)
								{
									newCode = Integer.valueOf(array.getJSONObject(i)
											.getString("VersionID"));
									
									PackageManager manager = HelpCenterActivity.this.getPackageManager();
									PackageInfo info = manager.getPackageInfo(HelpCenterActivity.this.getPackageName(), 0);
									appVersion = info.versionName; // 版本名
									oldCode = info.versionCode; // 版本号
									
									if(oldCode<newCode)
									{
//										SharedPreferences prefs = Index.this.getSharedPreferences("version", Context.MODE_PRIVATE);
//										
//										if(prefs.getInt("Version", 0) == newCode)
//										{
//											 versionlog
//										}
//										else
//										{
											versionlog = array.getJSONObject(i)
													.getString("VersionLog").replace("@", "\n");
//											versioncode.setText("更新");
//											versioncode.setTextColor(getResources().getColor(R.color.white));
//											versioncode.setBackgroundResource(R.drawable.switch_btn_bg_green);
											Message msg = new Message();
											msg.what = 1;
											handler.sendMessage(msg);
//										}
									}else{
										Message msg = new Message();
										msg.what = 0;
										handler.sendMessage(msg);
										
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

			JsonAsyncTask_Info task = new JsonAsyncTask_Info(HelpCenterActivity.this, true,
					doProcess);
			Map<String, Object> paramMap = new HashMap<String, Object>();
			String str = "<Request></Request>";

			paramMap.put("str", str);

			// 必须是这5个参数，而且得按照顺序
			task.execute(SOAP_NAMESPACE, SOAP_ACTION3, SOAP_METHODNAME3,
					SOAP_URL, paramMap);
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	protected void showUpdateDialog() {
		// TODO Auto-generated method stub
//		AlertDialog.Builder builder = new AlertDialog.Builder(this);
//		builder.setTitle("检测到新版本");
//		builder.setMessage(versionlog);
//		builder.setPositiveButton("下载", new DialogInterface.OnClickListener() {
//
//			public void onClick(DialogInterface dialog, int which) {
//			
//				
//				final Intent it = new Intent(HelpAct.this, DownloadServiceForAPK.class);
//				startService(it);
//				
//			}
//		}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
//
//			public void onClick(DialogInterface dialog, int which) {
//
//			}
//		});
//		builder.show();
		DialogEnsureCancelView dialogEnsureCancelView = new DialogEnsureCancelView(
				HelpCenterActivity.this).setDialogMsg("检测到新版本",
						"versionlog", "下载").setOnClickListenerEnsure(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						final Intent it = new Intent(HelpCenterActivity.this, DownloadServiceForAPK.class);
						startService(it);
						
						
					}
				});
		DialogUtils.showSelfDialog(HelpCenterActivity.this, dialogEnsureCancelView);
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
		versintxt=(TextView) findViewById(R.id.txtversion);
		llcheckVersion= findViewById(R.id.llcheckVersion);
		versioncode=(TextView) findViewById(R.id.versioncode);
		tel=(TextView) findViewById(R.id.tel);
		llfunction= findViewById(R.id.llfunction);
		llfeedback= findViewById(R.id.llfeedback);
		llphone= findViewById(R.id.llphoe);

		sqrule=findViewById(R.id.sqrule);
		sqhandlebook= findViewById(R.id.sqhandlebook);
		llcheckVersion.setOnClickListener(this);

		llfunction.setOnClickListener(this);
		findViewById(R.id.llinfo).setOnClickListener(this);
		llfeedback.setOnClickListener(this);
		llphone.setOnClickListener(this);
		sqrule.setOnClickListener(this);
		sqhandlebook.setOnClickListener(this);
	}
	
	public void back(View v){
		finish();
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.llcheckVersion:
			if(oldCode<newCode){
				showUpdateDialog();
			}else{
				Toast.makeText(HelpCenterActivity.this, "已经是最新版本", 1).show();
			}
			break;
	case R.id.llfunction:
		Intent i2= new Intent(HelpCenterActivity.this, AboutUsActivity.class);
		startActivity(i2);
			break;
	case R.id.llinfo:
		Intent i = new Intent(HelpCenterActivity.this, MustKnownActivity.class);
		startActivity(i);
		break;
	case R.id.llfeedback:
		if(user.UserID == 0)
		{
			Intent i1 = new Intent(HelpCenterActivity.this, LoginActivity.class);
			i1.putExtra("Page", "Feedback");
			startActivity(i1);
		
		}else{
			
			startActivity(new Intent(HelpCenterActivity.this, FeedbackAct.class));
		}
		
		break;
	case R.id.sqhandlebook:
		
		
		
		startActivity(new Intent(HelpCenterActivity.this, CHK_HandleBook.class));
	
	break;
	case R.id.sqrule:
			startActivity(new Intent(HelpCenterActivity.this, CommunityRuleActivity.class));
		
		break;

	case R.id.llphoe:
		Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+tel.getText().toString()));
        startActivity(intent);
		break;

		default:
			break;
		}
		
	}

}
