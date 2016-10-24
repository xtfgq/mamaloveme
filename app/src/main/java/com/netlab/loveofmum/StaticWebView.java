package com.netlab.loveofmum;

import java.net.MalformedURLException;
import java.net.URL;

import com.netlab.loveofmum.api.MMloveConstants;
import com.netlab.loveofmum.testwebview.TestWebView;

import com.netlab.loveofmum.utils.SystemStatusManager;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import im.delight.android.webview.AdvancedWebView;

public class StaticWebView extends Activity implements AdvancedWebView.Listener{
	private AdvancedWebView mWebView = null;
private TextView txtHead;
private ImageView imgBack;
private Intent mIntent;
String ID= "";

@Override
protected void onCreate(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	super.onCreate(savedInstanceState);
	requestWindowFeature(Window.FEATURE_NO_TITLE);
	setTranslucentStatus();
	setContentView(R.layout.testwebview_layout);
	mWebView = (AdvancedWebView) findViewById(R.id.webview);
	WebSettings webSettings = mWebView.getSettings();
	mWebView.setWebViewClient(new MyWebViewClient());
	webSettings.setSaveFormData(false);
	webSettings.setJavaScriptEnabled(true);
	webSettings.setSupportZoom(false);
	mIntent = this.getIntent();
	if (mIntent != null && mIntent.getStringExtra("ID") != null) {
		ID= mIntent.getStringExtra("ID");
	}
//	https://lovestaging.topmd.cn/html/1.html
	String url = MMloveConstants.JE_BASE_URL2
			+ "/html/" + ID+".html";
	mWebView.loadUrl(url);
	txtHead = (TextView) findViewById(R.id.txtHead);
 
    mWebView.setWebChromeClient(new WebChromeClient(){
    	@Override
    	public void onReceivedTitle(WebView view, String title) {
    		// TODO Auto-generated method stub
    		super.onReceivedTitle(view, title);
    		txtHead.setText(title); 
    	}
    });  
 
	

	imgBack = (ImageView) findViewById(R.id.img_left);
	imgBack.setOnClickListener(new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			// 返回键退回
			if (mWebView.canGoBack()) {
				mWebView.goBack();
			} else {
				finish();
			}
		}
	});
	txtHead.setText("");
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
		
		
						view.loadUrl(url);
		
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
		if (mWebView.canGoBack()) {
			mWebView.goBack();
		} else {
			finish();
		}
		return true;
	}

	return super.onKeyDown(keyCode, event);
}
	@Override
	public void onBackPressed() {
		if (!mWebView.onBackPressed()) { return; }

		super.onBackPressed();
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
