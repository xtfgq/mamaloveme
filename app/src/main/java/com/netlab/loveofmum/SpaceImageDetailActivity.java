package com.netlab.loveofmum;

import java.util.ArrayList;

import com.netlab.loveofmum.utils.SystemStatusManager;
import com.netlab.loveofmum.widget.SmoothImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView.ScaleType;
import android.widget.Toast;


public class SpaceImageDetailActivity extends Activity {

	private ArrayList<String> mDatas;
	private int mPosition;
	private int mLocationX;
	private int mLocationY;
	private int mWidth;
	private int mHeight;
	SmoothImageView imageView = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mDatas = (ArrayList<String>) getIntent().getSerializableExtra("images");
		mPosition = getIntent().getIntExtra("position", 0);
		mLocationX = getIntent().getIntExtra("locationX", 0);
		mLocationY = getIntent().getIntExtra("locationY", 0);
		mWidth = getIntent().getIntExtra("width", 0);
		mHeight = getIntent().getIntExtra("height", 0);

		imageView = new SmoothImageView(this);
		imageView.setOriginalInfo(mWidth, mHeight, mLocationX, mLocationY);
		imageView.transformIn();
		imageView.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
		imageView.setScaleType(ScaleType.FIT_CENTER);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setTranslucentStatus() ;
		
		setContentView(imageView);
	
//		imageView.setImageResource(R.drawable.temp);
		// ScaleAnimation scaleAnimation = new ScaleAnimation(0.5f, 1.0f, 0.5f,
		// 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
		// 0.5f);
		// scaleAnimation.setDuration(300);
		// scaleAnimation.setInterpolator(new AccelerateInterpolator());
		// imageView.startAnimation(scaleAnimation);

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
	public void onBackPressed() {
		if(!hasInternetConnected()){
		finish();
		}
		if(Build.MANUFACTURER.equalsIgnoreCase("Xiaomi")){
			finish();
		}else{
		imageView.setOnTransformListener(new SmoothImageView.TransformListener() {
			@Override
			public void onTransformComplete(int mode) {
				if (mode == 2) {
					finish();
				}
			}
		});
		imageView.transformOut();
		}
	}
	/**
	 * 判断是否有网络连接,没有返回false
	 */
	
	public boolean hasInternetConnected() {
		ConnectivityManager manager = (ConnectivityManager)this
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (manager != null) {
			NetworkInfo network = manager.getActiveNetworkInfo();
			if (network != null && network.isConnectedOrConnecting()) {
				return true;
			}
		}
		return false;
	}
	@Override
	protected void onPause() {
		super.onPause();
		if (isFinishing()) {
			overridePendingTransition(0, 0);
		}
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if(!hasInternetConnected()){
			Toast.makeText(SpaceImageDetailActivity.this, "数据获取失败，请检查网络", 1).show();
			return;
		}
			ImageLoader.getInstance().displayImage(mDatas.get(mPosition), imageView);
	}

}
