package com.netlab.loveofmum;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.netlab.loveofmum.activity.HelpCenterActivity;
import com.netlab.loveofmum.activity.LoginActivity;
import com.netlab.loveofmum.activity.UserAskActivity;
import com.netlab.loveofmum.activity.User_InfoEditActivity;
import com.netlab.loveofmum.api.BaseActivity;
import com.netlab.loveofmum.api.JsonAsyncTaskOnComplete;
import com.netlab.loveofmum.api.JsonAsyncTask_Info;
import com.netlab.loveofmum.api.LocalAccessor;
import com.netlab.loveofmum.api.MMloveConstants;
import com.netlab.loveofmum.api.MyApplication;
import com.netlab.loveofmum.model.ShareModel;
import com.netlab.loveofmum.model.User;
import com.netlab.loveofmum.model.Yuchan;
import com.netlab.loveofmum.utils.IOUtils;
import com.netlab.loveofmum.utils.ImageOptions;
import com.netlab.loveofmum.utils.ImageUtil;
import com.netlab.loveofmum.utils.SystemStatusManager;
import com.netlab.loveofmum.widget.CircularImage;

import com.netlab.loveofmum.widget.SharePopupWindow;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

public class User_Set extends BaseActivity implements  View.OnClickListener
{

	private final String mPageName = "AnalyticsMe";
	private TextView nickname,credit,status;
	private TextView brithtime,birthinfo;
	private CircularImage user_unlogin_avatar;
	private CircularImage user_avatar;
	private User user;
	Yuchan yu;
	//private ImageLoader imageLoader;

	private int UserID;
	public static ArrayList<String> list = new ArrayList<String>();
	//private String ClientID;
	public ArrayAdapter<String> adapter;
	private long mExitTime;
//	private List<Map<String, Object>> arrayList3= new ArrayList<Map<String, Object>>();

	// 定义Web Service相关的字符串
	private final String SOAP_NAMESPACE = MMloveConstants.URL001;
	private final String SOAP_ACTION = MMloveConstants.URL001
			+ MMloveConstants.UserClientIDChange;
	private final String SOAP_METHODNAME = MMloveConstants.UserClientIDChange;
	private final String SOAP_URL = MMloveConstants.URL002;

	private final String SOAP_ACTION2 = MMloveConstants.URL001
			+ MMloveConstants.UserInquiry;
	private final String SOAP_METHODNAME2 = MMloveConstants.UserInquiry;
	// 用户总积分
	private final String SOAP_INQUIRYMETHODNAME = MMloveConstants.UserInquiry;
	private final String SOAP_INQUIRYACTION = MMloveConstants.URL001
			+ MMloveConstants.UserInquiry;
	private SharePopupWindow share;
//	private View vTop;


	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setTranslucentStatus() ;
		setContentView(R.layout.layout_userset);
		MyApplication.getInstance().addActivity(this);
		iniView();

	}
	private void getUserInqury() {
		// TODO Auto-generated method stub
		JsonAsyncTaskOnComplete doProcess = new JsonAsyncTaskOnComplete() {

			@Override
			public void processJsonObject(Object result) {

				String value = result.toString();

				JSONObject mySO = (JSONObject) result;
				try {
					JSONArray array = mySO.getJSONArray("UserInquiry");
					credit.setText(array.getJSONObject(0).getString("Point")
							.toString()+"积分"
					);

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		};
		JsonAsyncTask_Info task = new JsonAsyncTask_Info(User_Set.this, true,
				doProcess);
		Map<String, Object> paramMap = new HashMap<String, Object>();
		String str = "<Request><UserID>%s</UserID></Request>";
		str = String.format(str, new Object[] { user.UserID + "", });

		paramMap.put("str", str);
		task.execute(SOAP_NAMESPACE, SOAP_INQUIRYACTION,
				SOAP_INQUIRYMETHODNAME, SOAP_URL, paramMap);
	}

	@Override
	protected void iniView()
	{

		findViewById(R.id.credit_container).setOnClickListener(this);
		status = (TextView)findViewById(R.id.status);
		findViewById(R.id.status).setOnClickListener(this);
		findViewById(R.id.user_footmark).setOnClickListener(this);
		findViewById(R.id.user_ask).setOnClickListener(this);
		findViewById(R.id.user_helpcenter).setOnClickListener(this);
		user_unlogin_avatar=(CircularImage) findViewById(R.id.user_unlogin_avatar);
		findViewById(R.id.toeditinfo).setOnClickListener(this);
		findViewById(R.id.user_shop).setOnClickListener(this);
		findViewById(R.id.user_order).setOnClickListener(this);
		findViewById(R.id.user_bars).setOnClickListener(this);
		user_avatar = (CircularImage)findViewById(R.id.user_avatar);
		findViewById(R.id.user_avatar).setOnClickListener(this);
		brithtime=(TextView) findViewById(R.id.brithtime);
		findViewById(R.id.userinfo_container).setOnClickListener(this);
		findViewById(R.id.user_sales).setOnClickListener(this);
		nickname = (TextView)findViewById(R.id.user_nickname);
		birthinfo=(TextView)findViewById(R.id.birthinfo);
		credit=(TextView) findViewById(R.id.credit);
		findViewById(R.id.user_share).setOnClickListener(this);
		ScrollView sv = (ScrollView)findViewById(R.id.sv_index);
		sv.setClipToPadding(false);
		setInsets(User_Set.this,sv);
	}
	public static void setInsets(Activity context, View view)
	{
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT)
			return;
		SystemStatusManager tintManager = new SystemStatusManager(context);
		SystemStatusManager.SystemBarConfig config = tintManager.getConfig();
		view.setPadding(0, config.getPixelInsetTop(false),
				config.getPixelInsetRight(), config.getPixelInsetBottom());
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
	protected void onResume()
	{
		InitData();
		super.onResume();
		if(status.equals("注销")) {
			Intent intent = new Intent();
			intent.setAction("action.login");
			sendBroadcast(intent);
		}
		MobclickAgent.onPageStart(mPageName);
		MobclickAgent.onResume(this);
		if (share != null) {
			share.dismiss();
		}
	}
	private void InitData()
	{
		user = LocalAccessor.getInstance(User_Set.this).getUser();
		if(user!=null&&user.UserID!=0&&user.YuBirthDate!=null){
			String str[] = user.YuBirthDate.split("\\-");
			Date date;
			user_avatar.setVisibility(View.VISIBLE);
			user_unlogin_avatar.setVisibility(View.GONE);
			DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
			String yubirth = user.YuBirthDate;
			getUserInqury();
			try {
				date = fmt.parse(user.YuBirthDate);
				yu = new Yuchan();
				int days = IOUtils.DaysCount(date);

				yu = IOUtils.WeekInfo(date);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(yu.Week>=40){
				birthinfo.setText("你已经怀孕40周");
				brithtime.setText("");
			}else{
				brithtime.setText("预产期："+str[0]+"年"+str[1]+"月"+str[2]+"日");
				birthinfo.setText("你已经怀孕"+yu.Week + "周" + (yu.Days+1)+"天");
			}

		}else{
			brithtime.setText("");
			birthinfo.setText("");
			credit.setText("积分");
			nickname.setText("");
			user_avatar.setVisibility(View.INVISIBLE);
			user_unlogin_avatar.setVisibility(View.VISIBLE);
		}

		if(user.UserID == 0)
		{
			user_avatar.setImageResource(R.drawable.icon_user_normal);

			status.setText("登录/注册");

//			logOut();
			nickname.setText("");
		}
		else
		{
			status.setText("注销");
			credit.setText("积分");
			UserInquiry();
		}
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPageEnd(mPageName);
		MobclickAgent.onPause(this);
	}
	private void showShare() {
		ShareSDK.initSDK(this);
		OnekeyShare oks = new OnekeyShare();
		//关闭sso授权
		oks.disableSSOWhenAuthorize();

		// title标题：微信、QQ（新浪微博不需要标题）
		oks.setTitle("妈妈爱我");  //最多30个字符

		// text是分享文本：所有平台都需要这个字段
		oks.setText("我这有免费咨询的私人医生，产检预约还能优先就诊，帮你省钱省时间，还不来约嘛？~https://lovestaging.topmd.cn/Share/index.html");  //最多40个字符

		// imagePath是图片的本地路径：除Linked-In以外的平台都支持此参数
//		oks.setImagePath(Environment.getExternalStorageDirectory() + "/meinv.jpg");//确保SDcard下面存在此张图片

		//网络图片的url：所有平台
		oks.setImagePath(ImageUtil.saveResTolocal(User_Set.this.getResources(),R.drawable.icon,"mmlove_logo"));//网络图片rul
//		oks.setImageUrl(MMloveConstants.JE_BASE_URL+"img/icon.jpg");//网络图片rul

		// url：仅在微信（包括好友和朋友圈）中使用
		oks.setUrl(MMloveConstants.JE_BASE_URL+"Share/index.html");   //网友点进链接后，可以看到分享的详情

		// Url：仅在QQ空间使用
		oks.setTitleUrl(MMloveConstants.JE_BASE_URL+"Share/index.html");  //网友点进链接后，可以看到分享的详情

		// 启动分享GUI
		oks.show(this);
	}



	private void UserClientBind()
	{
		try
		{
			JsonAsyncTaskOnComplete doProcess = new JsonAsyncTaskOnComplete()
			{
				public void processJsonObject(Object result)
				{
					String UserClientBind = result.toString();
					if (UserClientBind == null)
					{

					}
					else
					{
						// 下边是具体的处理
						try
						{
							JSONObject mySO = (JSONObject) result;
							JSONArray array = mySO
									.getJSONArray("UserClientIDChange");
							Intent intent = new Intent();
							intent.setAction("action.login");
							sendBroadcast(intent);

							if (array.getJSONObject(0).has("MessageCode"))
							{

							}
							else
							{

							}

						}
						catch (Exception ex)
						{
							ex.printStackTrace();
						}
					}
				}
			};

			JsonAsyncTask_Info task = new JsonAsyncTask_Info(User_Set.this, true,
					doProcess);
			Map<String, Object> paramMap = new HashMap<String, Object>();
			String str = "<Request><UserID>%s</UserID></Request>";
			str = String.format(str, new Object[]
					{ String.valueOf(UserID)});
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


	private void UserInquiry()
	{
		try
		{
			JsonAsyncTaskOnComplete doProcess = new JsonAsyncTaskOnComplete()
			{
				public void processJsonObject(Object result)
				{
					String UserClientBind = result.toString();
					if (UserClientBind == null)
					{

					}
					else
					{
						// 下边是具体的处理
						try
						{
							JSONObject mySO = (JSONObject) result;
							JSONArray array = mySO
									.getJSONArray("UserInquiry");


							if (array.getJSONObject(0).has("MessageCode"))
							{

							}
							else
							{

								for (int i = 0; i < array.length(); i++)

								{
									String nick=array.getJSONObject(i)
											.getString("NickName").toString();
									if(!"".equals(nick)) {
										if(nick.length()>14) {
											nickname.setText(array.getJSONObject(i)
													.getString("NickName").toString().substring(0, 14) + "...");
										}else{
											nickname.setText(nick);
										}
									}
									if(array.getJSONObject(i)
											.getString("PictureURL").toString().contains("vine.gif"))
									{
										user_avatar.setImageResource(R.drawable.icon_user_normal);
									}
									else
									{

										user.PicURL=MMloveConstants.JE_BASE_URL3 + array.getJSONObject(i)
												.getString("PictureURL").toString().replace("~", "").replace("\\", "/");

										ImageLoader.getInstance().displayImage(MMloveConstants.JE_BASE_URL3 + array.getJSONObject(i)
												.getString("PictureURL").toString().replace("~", "").replace("\\", "/"), user_avatar,ImageOptions.getHeaderOptions());
									}
									if(!TextUtils.isEmpty(array.getJSONObject(i)
											.getString("IsTwins").toString()))
										user.isTwins=Integer.valueOf(array.getJSONObject(i)
												.getString("IsTwins").toString());
									LocalAccessor.getInstance(User_Set.this).updateUser(user);
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

			if (hasInternetConnected())
			{
				JsonAsyncTask_Info task = new JsonAsyncTask_Info(User_Set.this, true,
						doProcess);
				Map<String, Object> paramMap = new HashMap<String, Object>();
				String str = "<Request><UserID>%s</UserID></Request>";
				str = String.format(str, new Object[]
						{ String.valueOf(user.UserID)});
				paramMap.put("str", str);

				// 必须是这5个参数，而且得按照顺序
				task.execute(SOAP_NAMESPACE, SOAP_ACTION2, SOAP_METHODNAME2,
						SOAP_URL, paramMap);
			}
			else
			{
				Toast.makeText(User_Set.this, R.string.msgUninternet,
						Toast.LENGTH_SHORT).show();
			}
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN)
		{
			if ((System.currentTimeMillis() - mExitTime) > 2000) // System.currentTimeMillis()无论何时调用，肯定大于2000
			{
				Toast.makeText(getApplicationContext(), "再按一次退出程序",
						Toast.LENGTH_SHORT).show();
				mExitTime = System.currentTimeMillis();
			} else {
				stopService();
				MobclickAgent.onKillProcess(this);
				ShareSDK.stopSDK(this);
				myApplication.exit();

			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onDestroy()
	{
		// TODO Auto-generated method stub
		super.onDestroy();
		ShareSDK.stopSDK(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){

			case R.id.user_helpcenter:
				if (!hasInternetConnected()){
					Toast.makeText(User_Set.this, "网络数据获取失败，请检查网络设置", 1).show();
					return;
				}
				// TODO Auto-generated method stub
				startActivity(new Intent(User_Set.this,HelpCenterActivity.class));
				break;
			case R.id.credit_container:               //积分点击
				if(user.UserID == 0)
				{
					Intent i = new Intent(User_Set.this, LoginActivity.class);
					startActivityForResult(i, 1);
				}
				break;
			case R.id.user_ask:
				if(user.UserID == 0)
				{
					Intent i = new Intent(User_Set.this, LoginActivity.class);
					i.putExtra("Page", "User_Set");
					startActivityForResult(i, 1);
				}
				else
				{
					Intent i = new Intent(User_Set.this, UserAskActivity.class);
					startActivity(i);
				}
				break;
			case R.id.status:
				// TODO Auto-generated method stub
				if (!hasInternetConnected()){
					Toast.makeText(User_Set.this, "网络数据获取失败，请检查网络设置", 1).show();
					return;
				}
				if(user.UserID == 0)
				{
					Intent i = new Intent(User_Set.this, LoginActivity.class);
					startActivityForResult(i, 1);
				}
				else
				{
					UserID = user.UserID;
					user.UserID = 0;
					user.PayId="";

					try {
						UserClientBind();
						LocalAccessor.getInstance(User_Set.this).updateUser(user);
						Intent intent = new Intent();
						intent.setAction("action.login");
						sendBroadcast(intent);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					if(user.UserID==0){
						InitData();
					}
				}
				break;
			case R.id.user_bars:
				if (!hasInternetConnected()){
					Toast.makeText(User_Set.this, "网络数据获取失败，请检查网络设置", 1).show();
					return;
				}
				if(user.UserID == 0)
				{
					Intent i = new Intent(User_Set.this, LoginActivity.class);
					startActivityForResult(i, 1);
				}else{

					startActivity(new Intent(User_Set.this, MyTiptocWebView.class));
				}
				break;
			case R.id.user_sales:
				startActivity(new Intent(User_Set.this,SpecialShopWebView.class));
				break;
			case R.id.user_shop:
				startActivity(new Intent(User_Set.this,ShopWebView.class));
				break;
			case R.id.user_order:
				if(user.UserID == 0)
				{
					Intent i = new Intent(User_Set.this, LoginActivity.class);
					i.putExtra("Page", "User_Set");
					startActivityForResult(i, 1);
				}
				else
				{
					Intent i = new Intent(User_Set.this, MyCHK_Timeline.class);
					i.putExtra("Page", "User_Order");
					startActivity(i);
				}
				break;
			case R.id.user_share:
				if(share==null) {
					share = new SharePopupWindow(User_Set.this);
					ShareModel model = new ShareModel();
					model.setImgPath(ImageUtil.saveResTolocal(User_Set.this.getResources(), R.drawable.icon, "mmlove_logo"));
					model.setText("我这有免费咨询的私人医生，产检预约还能优先就诊，帮你省钱省时间，还不来约嘛？");
					model.setTitle("妈妈爱我");
					model.setUrl(MMloveConstants.JE_BASE_URL + "Share/index.html");

					share.initShareParams(model);
				}
				share.showShareWindow();
				// 显示窗口 (设置layout在PopupWindow中显示的位置)
				share.showAtLocation(User_Set.this.findViewById(R.id.set),
						Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
				break;
			case R.id.user_footmark:
				if(user.UserID == 0)
				{
					Intent i = new Intent(User_Set.this, LoginActivity.class);
					i.putExtra("Page", "User_Set");
					startActivityForResult(i, 1);
				}
				else
				{
					Intent i = new Intent(User_Set.this, MyCHK_Timeline.class);
					i.putExtra("Page", "User_Foot");
					startActivity(i);
				}
				break;
			case R.id.userinfo_container:
				if(user.UserID == 0)
				{
					Intent i = new Intent(User_Set.this, LoginActivity.class);
					i.putExtra("Page", "User_Set");
					startActivityForResult(i, 1);
				}
				else
				{
					Intent i = new Intent(User_Set.this, User_InfoEditActivity.class);
					startActivity(i);
				}
				break;
			case R.id.toeditinfo:
			case R.id.user_avatar:
				if(user.UserID == 0)
				{
					Intent i = new Intent(User_Set.this, LoginActivity.class);
					i.putExtra("Page", "User_Set");
					startActivityForResult(i, 1);
				}
				else
				{
					Intent i = new Intent(User_Set.this, User_InfoEditActivity.class);
					startActivity(i);
				}
				break;
		}
	}
}
