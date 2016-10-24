package com.netlab.loveofmum;

import com.netlab.loveofmum.api.LocalAccessor;
import com.netlab.loveofmum.model.User;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class GuidePictureActivity extends Activity{
	private WebView mWebView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_guide_picture);
		initView();
	}
	@SuppressLint("SetJavaScriptEnabled")
	private void initView() {
		mWebView = (WebView)findViewById(R.id.activity_guide_picture_webview);
		mWebView.setWebViewClient(new MyWebViewClient());
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
		mWebView.loadUrl("file:///android_asset/index.html");
		
	}

//	private void goLogin(){
//		PreferencesGuide.getInstance(this).setGuide(true);
////		Intent mIntent = new Intent(this,LoginActivity.class);
//		Intent mIntent = new Intent(this,NewLogAct.class);
//		startActivity(mIntent);
//		finish();
//	}
//	private void goRegister(){
//		PreferencesGuide.getInstance(this).setGuide(true);
//		Intent intent = new Intent(this,RegisterOneActivity.class);
//		startActivity(intent);
//		finish();
//	}
	class MyWebViewClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			if(url.contains("http://startapp")){
				goHome();
			}else{
			view.loadUrl(url);
			}
			return true;
		}
	}
	private void goHome()
	{

	
		
			Intent intent = new Intent(GuidePictureActivity.this,
					MainTabActivity.class);
			intent.putExtra("TabIndex", "A_TAB");
			startActivity(intent);
			finish();
		
		
		
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		mWebView.destroy();
	}
}

