package com.netlab.loveofmum;

import java.util.List;

import com.netlab.loveofmum.api.LocalAccessor;
import com.netlab.loveofmum.model.User;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;


public class ViewPageAdapter extends PagerAdapter {

	
	private List<View> views;
	private Activity activity;

	private static final String SHAREDPREFERENCES_NAME = "first_pref";

	public ViewPageAdapter(List<View> views, Activity activity) {
		this.views = views;
		this.activity = activity;
	}

	
	@Override
	public void destroyItem(View arg0, int arg1, Object arg2) {
		((ViewPager) arg0).removeView(views.get(arg1));
	}

	@Override
	public void finishUpdate(View arg0) {
	}

	
	@Override
	public int getCount() {
		if (views != null) {
			return views.size();
		}
		return 0;
	}

	
	@Override
	public Object instantiateItem(View arg0, int arg1) {
		((ViewPager) arg0).addView(views.get(arg1), 0);
		if (arg1 == views.size() - 1) {
			ImageView mStartWeiboImageButton = (ImageView) arg0
					.findViewById(R.id.iv_start_weibo);
			mStartWeiboImageButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
				
					setGuided();
					goHome();

				}

			});
//			
//			setGuided();
//			goHome();
		}
		return views.get(arg1);
	}

	private void goHome() {
	
		User user = LocalAccessor.getInstance(activity).getUser();
		if(user.YuBirthDate == null)
		{
		//二期跳到预产设置页面
			Intent intent = new Intent(activity,TimeSet.class);
			activity.startActivity(intent);
			activity.finish();
		}
		else
		{
			int hosid = user.HospitalID;
			if(hosid==0)
			{
				user.HospitalID=1;
				user.HospitalName="郑州大学第三附属医院";
				
				try
				{
					LocalAccessor.getInstance(activity).updateUser(user);
				}
				catch (Exception e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			Intent intent = new Intent(activity,MainTabActivity.class);
			intent.putExtra("TabIndex", "A_TAB");
			activity.startActivity(intent);
			activity.finish();
		}
	}

	
	private void setGuided() {
		SharedPreferences preferences = activity.getSharedPreferences(
				SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
		
		PackageManager manager = activity.getPackageManager();
		PackageInfo info;
		try
		{
			info = manager.getPackageInfo(activity.getPackageName(), 0);

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

	
	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return (arg0 == arg1);
	}

	@Override
	public void restoreState(Parcelable arg0, ClassLoader arg1) {
	}

	@Override
	public Parcelable saveState() {
		return null;
	}

	@Override
	public void startUpdate(View arg0) {
	}

}
