package com.netlab.loveofmum.activity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.widget.ImageView;
import android.widget.TextView;

import com.netlab.loveofmum.R;
import com.netlab.loveofmum.api.BaseActivity;
import com.netlab.loveofmum.api.MMloveConstants;
import com.netlab.loveofmum.api.MyApplication;
import com.netlab.loveofmum.utils.SystemStatusManager;
import com.umeng.analytics.MobclickAgent;

public class AboutUsActivity extends BaseActivity {

	
		private TextView txtHead;
		private ImageView imgBack;
		
		private WebView web;


		@Override
		protected void onCreate(Bundle savedInstanceState)
		{
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			setTranslucentStatus() ;
//			requestWindowFeature(Window.FEATURE_NO_TITLE);
			setContentView(R.layout.layout_xuzhi);
			MyApplication.getInstance().addActivity(this);
			iniView();
			setListeners();
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
		protected void iniView()
		{
			// TODO Auto-generated method stub
			txtHead = (TextView) findViewById(R.id.txtHead);
			imgBack = (ImageView) findViewById(R.id.img_left);
			
			txtHead.setText("关于我们");
			
			web = (WebView)findViewById(R.id.web001_layout_xuzhi);
			
			web.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);

			web.loadUrl(MMloveConstants.JE_BASE_URL+"help/about.html");
		}

		private void setListeners()
		{
			imgBack.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					// TODO Auto-generated method stub
					finish();
				}
			});
		}
		
		@Override
		public void onDestroy()
		{
			// TODO Auto-generated method stub
			super.onDestroy();
			
			web.removeAllViews();
			web.destroy();
		}


}
