package com.netlab.loveofmum;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;


import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.igexin.sdk.PushManager;
import com.netlab.loveofmum.api.BaseActivity;
import com.netlab.loveofmum.api.JsonAsyncTaskOnComplete;
import com.netlab.loveofmum.api.JsonAsyncTask_Info;
import com.netlab.loveofmum.api.LocalAccessor;
import com.netlab.loveofmum.api.MMloveConstants;
import com.netlab.loveofmum.api.MyApplication;
import com.netlab.loveofmum.model.User;
import com.netlab.loveofmum.testwebview.TestWebView;

import com.netlab.loveofmum.utils.Des;
import com.netlab.loveofmum.utils.IOUtils;
import com.netlab.loveofmum.utils.SystemStatusManager;
import com.netlab.loveofmum.utils.ThreeDes;
import com.umeng.analytics.MobclickAgent;

import cn.sharesdk.framework.ShareSDK;
import im.delight.android.webview.AdvancedWebView;

//import cn.sharesdk.framework.ShareSDK;

public class BBSWebView extends Activity implements AdvancedWebView.Listener{
	private AdvancedWebView mWebView=null;

	private View vTop;
	private User user;
	private long mExitTime;
	private MyApplication myApplication;
	String ClientID;
	// 定义Web Service相关的字符串
	private final String SOAP_NAMESPACE = MMloveConstants.URL001;
	private final String SOAP_ACTION = MMloveConstants.URL001 + MMloveConstants.UserLogin;
	private final String SOAP_METHODNAME = MMloveConstants.UserLogin;
	private final String SOAP_URL = MMloveConstants.URL002;
	private BroadcastReceiver mRefreshBroadcastReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if (intent.getAction().equals("action.login")) {
				ClientID = PushManager.getInstance().getClientid(BBSWebView.this);
				user=LocalAccessor.getInstance(BBSWebView.this).getUser();
				String url =  MMloveConstants.URLWEB+"/molms/index.jhtml?redirectUrl=forum"+ "&UserID=" + user.UserID
						+ "&YuBirthTime=" + user.YuBirthDate + "&ClientID="
						+ ClientID;
//				String url =  MMloveConstants.URLWEB+"redirectUrl=forum"+ "&UserID=" + user.UserID
//						+ "&BithdayTime=" + user.YuBirthDate + "&ClientID="
//					+ ClientID;
				mWebView.loadUrl(url);

			}
		}

	};
	//	private BroadcastReceiver mReceiver = new BroadcastReceiver() {
//
//		@Override
//		public void onReceive(Context context, Intent intent) {
//			// TODO Auto-generated method stub
//			if (intent.getAction().equals("action.url")) {
//				String urls=intent.getStringExtra("url");
//				mWebView.loadUrl(urls);
//				mWebView.reload();
//			}
//		}
//
//	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setTranslucentStatus();
		setContentView(R.layout.bbs_webview);
		mWebView = (AdvancedWebView) findViewById(R.id.webview);
		vTop = findViewById(R.id.v_top);
		WebSettings webSettings = mWebView.getSettings();

		mWebView.setWebViewClient(new MyWebViewClient());

		webSettings.setSaveFormData(false);
		webSettings.setJavaScriptEnabled(true);
		webSettings.setSupportZoom(false);

		myApplication = MyApplication.getInstance();
		user=LocalAccessor.getInstance(BBSWebView.this).getUser();
		PushManager.getInstance().initialize(this.getApplicationContext());
		ClientID = PushManager.getInstance().getClientid(BBSWebView.this);
		String url;
		if(TextUtils.isEmpty(myApplication.urls)) {
			url= MMloveConstants.URLWEB + "/molms/index.jhtml?redirectUrl=forum" + "&UserID=" + user.UserID
					+ "&YuBirthTime=" + user.YuBirthDate + "&ClientID="
					+ ClientID;
		}else{
			url=myApplication.urls;
		}
		mWebView.loadUrl(url);
		myApplication.addActivity(this);


//		社区入口：http://125.46.31.185:8080/molms/index.jhtml?redirectUrl=forum
//			特卖：http://125.46.31.185:8080/molms/index.jhtml?redirectUrl=shop
//			积分商城入口：http://125.46.31.185:8080/molms/index.jhtml?redirectUrl=commodity

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			int h = getStatusHeight(this);
			LayoutParams params = vTop.getLayoutParams();
			params.height = h;
			params.width = LayoutParams.FILL_PARENT;
			vTop.setLayoutParams(params);
		} else {
			vTop.setVisibility(View.GONE);
		}
		mWebView.setListener(this, this);
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("action.login");
		registerReceiver(mRefreshBroadcastReceiver, intentFilter);
//		IntentFilter intentFilter2 = new IntentFilter();
//		intentFilter2.addAction("action.url");
//		registerReceiver(mReceiver, intentFilter2);
	}



	public static int getStatusHeight(Context context) {

		int statusHeight = -1;
		try {
			Class<?> clazz = Class.forName("com.android.internal.R$dimen");
			Object object = clazz.newInstance();
			int height = Integer.parseInt(clazz.getField("status_bar_height")
					.get(object).toString());
			statusHeight = context.getResources().getDimensionPixelSize(height);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return statusHeight;
	}

	/**
	 * 设置状态栏背景状态
	 */
	private void setTranslucentStatus() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			Window win = getWindow();
			WindowManager.LayoutParams winParams = win.getAttributes();
			final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
			winParams.flags |= bits;
			win.setAttributes(winParams);
		}
		SystemStatusManager tintManager = new SystemStatusManager(this);
		tintManager.setStatusBarTintEnabled(true);
		tintManager.setStatusBarTintResource(R.drawable.bg_header);// 状态栏无背景
	}

	class MyWebViewClient extends WebViewClient {
		// 重写shouldOverrideUrlLoading方法，使点击链接后不使用其他的浏览器打开。



		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {



//					http://125.46.31.185:8080/molms/loginSuccess.jhtml?momSuccess=true&res=molms/r/cms/momloveme/mobile&location=http://125.46.31.185:8080/molms/login.jspx?username=15637147870&password=123abc&locale=zh_CN&_start_time=1456541982820&resSys=/molms/r/cms
//			if (url.contains("http://momloveme/?returnflag=commall")||url.contains("http://momloveme/?returnflag=shop")||url.contains("http://momloveme/?returnflag=mytopic")||url.contains("http://momloveme/?returnflag=appindex")) {
//				if(view.canGoBack()){
//					view.goBack();
//				}
//
//			}else {
//				if(url.contains("https://lovestaging.topmd.cn/WebServices/NewsShare.aspx?")){
//					Intent i=new Intent(BBSWebView.this,News_Detail.class);
//					int beginIndex = url.indexOf("ID=");
//					String ID=url.substring(beginIndex+3,url.length());
//					i.putExtra("ID",ID);
//					startActivity(i);
//
//				}else {
//					String ss = url.toString();
//					try {
//						ss = new String(URLDecoder.decode(ss, "iso8859-1").getBytes("iso8859-1"), "utf-8");
//					} catch (UnsupportedEncodingException e1) {
//						// TODO Auto-generated catch block
//						e1.printStackTrace();
//					}
//					String urls = "";
//					try {
//
//						if (ss.contains("momSuccess=true")) {
//							int beginIndex = 0;
//							int endIndex = 0;
//							beginIndex = ss.indexOf("username");
//							endIndex = ss.indexOf("locale") - 1;
//							urls = ss.substring(beginIndex, endIndex);
//
//							urls = urls.replace("&", "=").trim();
//							String[] strs = urls.split("\\=");
//							String username = strs[1];
//							String pass = ss.substring(ss.indexOf("password=") + 9, ss.indexOf("locale") - 1);
////						String decryptString= Des.encode("123456");
////						Log.i("vvvv",decryptString);
//
//							pass = Des.decode(pass);
//							UserLogin(username, pass);
//						}
//						if(!url.contains("/molms/forum.jhtml")) {
//							Intent i = new Intent(BBSWebView.this,SimpleWebView.class);
//							i.putExtra("url", url);
//							startActivity(i);
//						}
//					} catch (Exception e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
			if(!url.contains("/molms/forum.jhtml")) {
				if(url.contains("https://lovestaging.topmd.cn/WebServices/NewsShare.aspx?")) {
					Intent i=new Intent(BBSWebView.this,News_Detail.class);
					int beginIndex = url.indexOf("ID=");
					String ID=url.substring(beginIndex+3,url.length());
					i.putExtra("ID",ID);
					startActivity(i);
				}else if(url.contains("/molms/index.jhtml")){
					view.loadUrl(url);
				}
				else {
					Intent i = new Intent(BBSWebView.this, SimpleWebView.class);
					i.putExtra("url", url);
					startActivity(i);
				}
			}
			else{
				view.loadUrl(url);
			}

//					view.loadUrl(url);
//				}


//			}
			return true;

		}
		@Override
		public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
//			handler.cancel();
			handler.proceed();

		}
	}


	@Override
	protected void onDestroy() {

		mWebView.onDestroy();
		try {
			unregisterReceiver(mRefreshBroadcastReceiver);

			mRefreshBroadcastReceiver = null;
		} catch (Exception e) {
		}
//		try {
//			unregisterReceiver(mReceiver);
//
//			mReceiver = null;
//		} catch (Exception e) {
//		}
		// ...
		super.onDestroy();
	}

	private void UserLogin(String uername, String pass)
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
						// 下边是具体的处理
						try
						{
							JSONObject mySO = (JSONObject) result;
							org.json.JSONArray array = mySO
									.getJSONArray("UserLogin");

							if (array.getJSONObject(0).has("MessageCode"))
							{
								Toast.makeText(BBSWebView.this,array.getJSONObject(0).getString("MessageContent").toString(),
										Toast.LENGTH_SHORT).show();
							}
							else
							{
								String UserID = array.getJSONObject(0).getString("UserID").toString();

								User user = LocalAccessor.getInstance(BBSWebView.this).getUser();
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

								LocalAccessor.getInstance(BBSWebView.this).updateUser(user);

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

			JsonAsyncTask_Info task = new JsonAsyncTask_Info(BBSWebView.this, true,
					doProcess);
			Map<String, Object> paramMap = new HashMap<String, Object>();
			String str = "<Request><UserMobile>%s</UserMobile><Password>%s</Password><ClientID>%s</ClientID><YuBirthTime>%s</YuBirthTime><PhoneType>%s</PhoneType><DeviceToken>%s</DeviceToken></Request>";
			str = String.format(str, new Object[]
					{ uername, pass,ClientID,user.YuBirthDate,"0",""});
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
	@SuppressLint("NewApi")

	@Override
	protected void onPause()  {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			mWebView.onPause(); // 暂停网页中正在播放的视频
		}
		super.onPause();
		mWebView.onPause();
		MobclickAgent.onPause(this);
	}
	@SuppressLint("NewApi")
	@Override
	protected void onResume() {


		super.onResume();
		mWebView.onResume();
		MobclickAgent.onResume(this);


	}

	@Override
	protected void onStop() {
		super.onStop();
	}


	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			// 返回键退回
			if (mWebView.canGoBack()) {
				mWebView.goBack();
			} else if ((System.currentTimeMillis() - mExitTime) > 2000)
			{
				Toast.makeText(getApplicationContext(), "再按一次退出程序",
						Toast.LENGTH_SHORT).show();
				mExitTime = System.currentTimeMillis();
			} else {
				MobclickAgent.onKillProcess(this);
				ShareSDK.stopSDK(this);
				myApplication.exit();


			}
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		mWebView.onActivityResult(requestCode, resultCode, intent);

	}

	@Override
	public void onBackPressed() {
		if (!mWebView.onBackPressed()) { return; }

		super.onBackPressed();
	}

	@Override
	public void onPageStarted(String url, Bitmap favicon) { }

	@Override
	public void onPageFinished(String url) {

	}

	@Override
	public void onPageError(int errorCode, String description, String failingUrl) { }

	@Override
	public void onDownloadRequested(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) { }

	@Override
	public void onExternalPageRequest(String url) { }


}
