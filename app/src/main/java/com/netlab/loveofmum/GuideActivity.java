package com.netlab.loveofmum;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.netlab.loveofmum.api.LocalAccessor;
import com.netlab.loveofmum.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 *
 *
 * class desc: 引导界面
 *
 * <p>
 * Copyright: Copyright(c) 2013
 * </p>
 *
 * @Version 1.0
 * @Author <a href="mailto:gaolei_xj@163.com">Leo</a>
 *
 *
 */
public class GuideActivity extends Activity implements OnPageChangeListener
{

	private ViewPager vp;
	private ViewPageAdapter vpAdapter;
	private List<View> views;

	private RadioGroup dotLayout;


	// 记录当前选中位置
	private int currentIndex;

	private static final String SHAREDPREFERENCES_NAME = "first_pref";

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.guide);

		// 初始化页面
		initViews();

		// 初始化底部小点
//		initDots();
	}

	private void initViews()
	{
		LayoutInflater inflater = LayoutInflater.from(this);

		views = new ArrayList<View>();
		// 初始化引导图片列表
		views.add(inflater.inflate(R.layout.what_new_one, null));
		views.add(inflater.inflate(R.layout.what_new_one1, null));
		views.add(inflater.inflate(R.layout.what_new_two, null));
		views.add(inflater.inflate(R.layout.what_new_three, null));
		views.add(inflater.inflate(R.layout.what_new_four, null));
		dotLayout = (RadioGroup) findViewById(R.id.advertise_point_group);
		// 初始化Adapter
		vpAdapter = new ViewPageAdapter(views, this);

		vp = (ViewPager) findViewById(R.id.viewpager);
		vp.setCurrentItem(0);
		vp.setAdapter(vpAdapter);
		// 绑定回调
		vp.setOnPageChangeListener(this);
	}

//	private void initDots()
//	{
//		LinearLayout ll = (LinearLayout) findViewById(R.id.ll);
//
//		dots = new ImageView[views.size()-1];
//
//		// 循环取得小点图片
//		for (int i = 0; i < views.size()-1; i++)
//		{
//			dots[i] = (ImageView) ll.getChildAt(i);
////			dots[i].setEnabled(true);// 都设为灰色
//		}
//
//		currentIndex = 0;
////		dots[currentIndex].setEnabled(false);// 设置为白色，即选中状态
//	}

//	private void setCurrentDot(int position)
//	{
//		if (position < 0 || position > views.size() - 1
//				|| currentIndex == position)
//		{
//			return;
//		}
//
//		dots[position].setEnabled(false);
//		dots[currentIndex].setEnabled(true);
//
//		currentIndex = position;
//
////		if (position ==2)
////		{
////			TimerTask task = new TimerTask()
////			{
////
////				public void run()
////				{
////
////					// execute the task
////					setGuided();
////					goHome();
////				}
////
////			};
////
////			Timer timer = new Timer();
////
////			timer.schedule(task, 2000);
////
////		}
//	}

	// 当滑动状态改变时调用
	@Override
	public void onPageScrollStateChanged(int arg0)
	{

	}

	// 当当前页面被滑动时调用
	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2)

	{
		if(arg0!=4) {

		((RadioButton) dotLayout.getChildAt(arg0)).setChecked(true);
			dotLayout.setVisibility(View.VISIBLE);
	}else{
			dotLayout.setVisibility(View.GONE);
		}

//		float ar01 = arg1;
//		int ar02 = arg2;
//		if(arg0 ==2)
//		{
//			setGuided();
//			goHome();
//		}
	}

	// 当新的页面被选中时调用
	@Override
	public void onPageSelected(int arg0)
	{
		// 设置底部小点选中状态
//		setCurrentDot(arg0);
		if(arg0==4)dotLayout.setVisibility(View.GONE);
		else dotLayout.setVisibility(View.VISIBLE);
		currentIndex = arg0;
	}

	private void goHome()
	{

		User user = LocalAccessor.getInstance(GuideActivity.this).getUser();
		if (user.YuBirthDate == null)
		{
			Intent intent = new Intent(GuideActivity.this, Hos_Select.class);
			startActivity(intent);
			finish();
		}
		else
		{
			int hosid = user.HospitalID;
			if (hosid == 0)
			{
				user.HospitalID = 1;
				user.HospitalName = "郑州大学第三附属医院";

				try
				{
					LocalAccessor.getInstance(GuideActivity.this).updateUser(
							user);
				}
				catch (Exception e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			Intent intent = new Intent(GuideActivity.this,
					MainTabActivity.class);
			intent.putExtra("TabIndex", "A_TAB");
			startActivity(intent);
			finish();
		}
	}

	private void setGuided()
	{
		SharedPreferences preferences = GuideActivity.this
				.getSharedPreferences(SHAREDPREFERENCES_NAME,
						Context.MODE_PRIVATE);

		PackageManager manager = GuideActivity.this.getPackageManager();
		PackageInfo info;
		try
		{
			info = manager.getPackageInfo(GuideActivity.this.getPackageName(),
					0);

			int newCode = info.versionCode; // 版本号

			Editor editor = preferences.edit();

			editor.putInt("VersionCode", newCode);

			editor.commit();
		}
		catch (NameNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
