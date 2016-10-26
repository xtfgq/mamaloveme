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

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		setContentView(R.layout.guide);

		// 初始化页面
		initViews();
	}

	private void initViews()
	{
		LayoutInflater inflater = LayoutInflater.from(this);

		views = new ArrayList<View>();
		// 初始化引导图片列表
		views.add(inflater.inflate(R.layout.what_new_four, null));
		dotLayout = (RadioGroup) findViewById(R.id.advertise_point_group);
		// 初始化Adapter
		vpAdapter = new ViewPageAdapter(views, this);
		vp = (ViewPager) findViewById(R.id.viewpager);
		vp.setAdapter(vpAdapter);
		// 绑定回调
		vp.setOnPageChangeListener(this);
	}


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
	}

	// 当新的页面被选中时调用
	@Override
	public void onPageSelected(int arg0)
	{
		if(arg0==4)dotLayout.setVisibility(View.GONE);
		else dotLayout.setVisibility(View.VISIBLE);

	}



}
