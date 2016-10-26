package com.netlab.loveofmum;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.igexin.sdk.PushManager;
import com.netlab.loveofmum.api.JsonAsyncTaskOnComplete;
import com.netlab.loveofmum.api.JsonAsyncTask_Info;
import com.netlab.loveofmum.api.LocalAccessor;
import com.netlab.loveofmum.api.MMloveConstants;
import com.netlab.loveofmum.model.User;
import com.netlab.loveofmum.utils.Des;
import com.netlab.loveofmum.utils.IOUtils;
import com.netlab.loveofmum.utils.SystemStatusManager;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import im.delight.android.webview.AdvancedWebView;

public class ShopWebView extends Activity implements AdvancedWebView.Listener{
	private AdvancedWebView mWebView = null;
	// 定义Web Service相关的字符串
	private final String SOAP_NAMESPACE = MMloveConstants.URL001;
	private final String SOAP_ACTION = MMloveConstants.URL001 + MMloveConstants.UserLogin;
	private final String SOAP_METHODNAME = MMloveConstants.UserLogin;
	private final String SOAP_URL = MMloveConstants.URL002;
	private User user;
	String ClientID;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setTranslucentStatus();
		setContentView(R.layout.shop_webview);
		mWebView = (AdvancedWebView) findViewById(R.id.webview);
		WebSettings webSettings = mWebView.getSettings();
		mWebView.setWebViewClient(new MyWebViewClient());
		webSettings.setSaveFormData(false);
		webSettings.setJavaScriptEnabled(true);
		webSettings.setSupportZoom(false);
		user = LocalAccessor.getInstance(ShopWebView.this).getUser();
		PushManager.getInstance().initialize(this.getApplicationContext());
		ClientID = PushManager.getInstance().getClientid(ShopWebView.this);
		String url= MMloveConstants.URLWEB
				+ "/molms/index.jhtml?redirectUrl=commodity"
				+ "&returnflag=jifenshangcheng" + "&UserID=" + user.UserID
				+ "&YuBirthTime=" + user.YuBirthDate + "&ClientID=" + ClientID;
		Log.i("ffff",url);
//		webSettings.setDomStorageEnabled(true);

		mWebView.loadUrl(MMloveConstants.URLWEB
				+ "/molms/index.jhtml?redirectUrl=commodity"
				+ "&returnflag=jifenshangcheng" + "&UserID=" + user.UserID
				+ "&YuBithdayTime=" + user.YuBirthDate + "&ClientID=" + ClientID);
		mWebView.setListener(this, this);


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
		tintManager.setStatusBarTintResource(R.color.home);// 状态栏无背景
	}

	class MyWebViewClient extends WebViewClient {
		// 重写shouldOverrideUrlLoading方法，使点击链接后不使用其他的浏览器打开。
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			if (url.startsWith("tel:")) {
				String tels=url.substring(url.indexOf("tel:")+4, url.length());
				startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+tels)));

			} else {

				if(url.contains("http://momloveme/?returnflag=appindex")||url.contains("http://momloveme/?returnflag=commall")||url.contains("http://momloveme/?returnflag=shop")||url.contains("http://momloveme/?returnflag=mytopic")||url.contains("http://momloveme/?returnflag=appindex")||url.contains("/molms/forum.jhtml")){

					if(view.canGoBack()){
						view.goBack();
					}else{
						finish();
					}
				}
				else {
					if(url.contains("http://momloveme/shop?returnflag=commall")){
						finish();

					}else {
						String ss = url.toString();
						try {
							ss = new String(URLDecoder.decode(ss, "iso8859-1").getBytes("iso8859-1"), "utf-8");
						} catch (UnsupportedEncodingException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						String urls = "";
						try {

							if (ss.contains("momSuccess=true")) {
								int beginIndex = 0;
								int endIndex = 0;
								beginIndex = ss.indexOf("username=");
								endIndex = ss.indexOf("&") - 1;

								String username = ss.substring(beginIndex+9, beginIndex+9+11);

								String pass = ss.substring(ss.indexOf("password=") + 9, ss.length());
								if(pass.contains("&")){
									pass = pass.substring(0, pass.indexOf("&"));
								}
								pass = Des.decode(pass);
								UserLogin(username, pass);
								view.loadUrl(url);
							} else {
								view.loadUrl(url);
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}


				}


			}

			// 如果不需要其他对点击链接事件的处理返回true，否则返回false
			return true;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mWebView.destroy();
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
								Toast.makeText(ShopWebView.this,array.getJSONObject(0).getString("MessageContent").toString(),
										Toast.LENGTH_SHORT).show();
							}
							else
							{
								String UserID = array.getJSONObject(0).getString("UserID").toString();

								User user = LocalAccessor.getInstance(ShopWebView.this).getUser();
								user.UserID = Integer.valueOf(UserID);
								user.NickName = array.getJSONObject(0).getString("NickName").toString();
								if(!array.getJSONObject(0).getString("YuBirthTime").toString().equals(""))
								{
									user.YuBirthDate = IOUtils.formatStrDate(array.getJSONObject(0).getString("YuBirthTime").toString());

//								user.YuBirthDate = IOUtils.formatStrDate("2016-06-13");
								}



								String url=array.getJSONObject(0).getString("PictureURL").toString().replace("~", "").replace("\\", "/");
								if(url==null){
									url= MMloveConstants.JE_BASE_URL3+"/img/icon_user_normal.png";

								}else if(url.contains("vine.gif")){
									int end=url.lastIndexOf("/");
									url=url.substring(0, end);
									url= MMloveConstants.JE_BASE_URL3+url+"/icon_user_normal.png";
								}else{
									url= MMloveConstants.JE_BASE_URL3+url;
								}
								user.PicURL =url;
								user.RealName = array.getJSONObject(0).getString("RealName").toString();
								user.UserMobile = array.getJSONObject(0).getString("Mobile").toString();
								user.UserNO = array.getJSONObject(0).getString("UserNO").toString();

								LocalAccessor.getInstance(ShopWebView.this).updateUser(user);
								Intent intent = new Intent();
								intent.setAction("action.login");
								sendBroadcast(intent);

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

			JsonAsyncTask_Info task = new JsonAsyncTask_Info(ShopWebView.this, true,
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

	@Override
	protected void onStop() {
		super.onStop();

	}

	@Override
	protected void onPause() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			mWebView.onPause(); // 暂停网页中正在播放的视频
		}
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		mWebView.onResume();
		MobclickAgent.onResume(this);

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
			// 返回键退回
			mWebView.goBack();
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}
	@Override
	public void onPageStarted(String url, Bitmap favicon) { }

	@Override
	public void onPageFinished(String url) { }

	@Override
	public void onPageError(int errorCode, String description, String failingUrl) { }

	@Override
	public void onDownloadRequested(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) { }

	@Override
	public void onExternalPageRequest(String url) { }

}
