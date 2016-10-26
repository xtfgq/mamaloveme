package com.netlab.loveofmum;

import com.igexin.sdk.PushManager;
import com.netlab.loveofmum.ShopWebView.MyWebViewClient;
import com.netlab.loveofmum.api.LocalAccessor;
import com.netlab.loveofmum.api.MMloveConstants;
import com.netlab.loveofmum.model.User;
import com.netlab.loveofmum.utils.SystemStatusManager;
import com.umeng.analytics.MobclickAgent;

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

import im.delight.android.webview.AdvancedWebView;

public class MyTiptocWebView extends Activity implements AdvancedWebView.Listener{
	private AdvancedWebView mWebView = null;
	private User user;
	String ClientID;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setTranslucentStatus();
		setContentView(R.layout.shop_webview);
		user=LocalAccessor.getInstance(MyTiptocWebView.this).getUser();
		mWebView = (AdvancedWebView) findViewById(R.id.webview);
		WebSettings webSettings = mWebView.getSettings();
		mWebView.setWebViewClient(new MyWebViewClient());
		webSettings.setSaveFormData(false);
		webSettings.setJavaScriptEnabled(true);
		webSettings.setSupportZoom(false);
		PushManager.getInstance().initialize(this.getApplicationContext());
		ClientID = PushManager.getInstance().getClientid(MyTiptocWebView.this);

		mWebView.loadUrl(MMloveConstants.URLWEB
				+ "/molms/member/mytopic.jhtml"+"?returnflag=mytopic" + "&UserID=" + user.UserID
				+ "&YuBirthTime=" + user.YuBirthDate + "&ClientID="
				+ ClientID);
		String url=MMloveConstants.URLWEB
				+ "/molms/member/mytopic.jhtml"+"?returnflag=mytopic" + "&UserID=" + user.UserID
				+ "&YuBirthTime=" + user.YuBirthDate + "&ClientID="
				+ ClientID;
//		Log.i("jjjjj",url);
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

				
					if (url.contains("http://momloveme/?returnflag=mytopic")) {
						finish();
					}else{
					
				view.loadUrl(url);
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
