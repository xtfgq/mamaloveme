package com.netlab.loveofmum.myadapter;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

public class TopicNewsAdapter extends FragmentPagerAdapter {
private ArrayList<Fragment> list;
	
	public TopicNewsAdapter(FragmentManager fm) {
		super(fm);
	}
	
	

	public TopicNewsAdapter(FragmentManager fm,
			ArrayList<Fragment> list) {
		super(fm);
		this.list = list;
	}



	@Override
	public Fragment getItem(int arg0) {
		// TODO Auto-generated method stub
		return list.get(arg0);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}


}
