package com.netlab.loveofmum.testwebview;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.acl.LastOwnerException;

import com.netlab.loveofmum.Index;
import com.netlab.loveofmum.News_Detail;
import com.netlab.loveofmum.News_List;
import com.netlab.loveofmum.R;
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
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import im.delight.android.webview.AdvancedWebView;

public class TestWebView extends Activity implements AdvancedWebView.Listener{
	private AdvancedWebView mWebView = null;
	private TextView txtHead;
	private ImageView imgBack;
	private Intent mIntent;
	String week = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setTranslucentStatus();
		setContentView(R.layout.testwebview_layout);
		mWebView = (AdvancedWebView) findViewById(R.id.webview);
		WebSettings webSettings = mWebView.getSettings();
		mWebView.setWebViewClient(new MyWebViewClient());
		webSettings.setSaveFormData(false);
		webSettings.setJavaScriptEnabled(true);
		webSettings.setSupportZoom(false);
		mIntent = this.getIntent();
		if (mIntent != null && mIntent.getStringExtra("YunWeek") != null) {
			week = mIntent.getStringExtra("YunWeek");
		}
		String url = MMloveConstants.JE_BASE_URL2
				+ "/tieshi/tieshi.aspx?YunWeek=" + week;
		mWebView.loadUrl(url);

		txtHead = (TextView) findViewById(R.id.txtHead);

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
		txtHead.setText("小贴士");
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
				startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
			} else {
				// https://lovestaging.topmd.cn/tieshi/list.aspx?ID=174
				try {
					URL redirectedURL = new URL(url);

					// https://lovestaging.topmd.cn/tieshi/details.aspx
					if (redirectedURL.getPath().contains("list")) {
						Intent i = new Intent(TestWebView.this, News_List.class);
						startActivity(i);
						finish();
					} else if (redirectedURL.getPath().contains("detail")) {
						
						String newsID = url.substring(url.indexOf("=") + 1,
								url.lastIndexOf("="));
						
						Intent i = new Intent(TestWebView.this,
								News_Detail.class);
						i.putExtra("ID", newsID);
						startActivity(i);
					} else {
						if (url.contains("http://yunweek/")) {
							Intent i = new Intent(TestWebView.this,
									News_List.class);
							startActivity(i);
							finish();
						} else {
							view.loadUrl(url);
						}
					}
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
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
	public void onResume() {
		super.onResume();
		mWebView.onResume();
		MobclickAgent.onResume(this);

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

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
			// 返回键退回
			mWebView.goBack();
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}
}
