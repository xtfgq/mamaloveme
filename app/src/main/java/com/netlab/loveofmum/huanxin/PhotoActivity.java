package com.netlab.loveofmum.huanxin;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import com.netlab.loveofmum.R;
import com.netlab.loveofmum.api.MMloveConstants;
import com.netlab.loveofmum.utils.ImageOptions;
import com.netlab.loveofmum.utils.SystemStatusManager;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class PhotoActivity extends Activity
{
	private Intent mIntent;
	private ImageView ivpro;
	private ImageLoader iMageLoader;
	private TextView txtHead;
	private ImageView imgBack;
		public void onCreate(Bundle savedInstanceState)
		{
			super.onCreate(savedInstanceState);
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			setTranslucentStatus() ;
			setContentView(R.layout.activity_photo);
		
			ivpro=(ImageView) findViewById(R.id.pic_pro);
			txtHead = (TextView) findViewById(R.id.txtHead);
			imgBack = (ImageView) findViewById(R.id.img_left);
			txtHead.setText("预览");
			imgBack.setOnClickListener(new View.OnClickListener()
			{
				
				@Override
				public void onClick(View v)
				{
					// TODO Auto-generated method stub
					finish();
				}
			});
			mIntent=this.getIntent();
			iMageLoader=iMageLoader.getInstance();
			
			String path = "";
			if(mIntent!=null){
				path=mIntent.getStringExtra("Url");
			}
			iMageLoader.displayImage(path, ivpro);
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


}

