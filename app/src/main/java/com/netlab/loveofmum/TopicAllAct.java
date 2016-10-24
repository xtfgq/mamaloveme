package com.netlab.loveofmum;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.LayoutParams;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.netlab.loveofmum.api.JsonAsyncTaskOnComplete;
import com.netlab.loveofmum.api.JsonAsyncTask_Info;
import com.netlab.loveofmum.api.LocalAccessor;
import com.netlab.loveofmum.api.MMloveConstants;

import com.netlab.loveofmum.community.TopicFragment;
import com.netlab.loveofmum.model.User;
import com.netlab.loveofmum.myadapter.TopicNewsAdapter;
import com.netlab.loveofmum.utils.SystemStatusManager;
import com.netlab.loveofmum.widget.TitleTextView;

public class TopicAllAct  extends FragmentActivity implements OnClickListener,
    OnPageChangeListener {
	private ViewPager mViewPager;
	private ViewGroup mViewGroup;
	private TextView txtHead;
	private ImageView imgBack;
	private TopicNewsAdapter mAdapter;
	private User user;
//	private int postion;
	
	private int mPreSelectItem;
	private Intent mIntent;
	String DoctorId;
	private List<Map<String, Object>> doctorList = new ArrayList<Map<String, Object>>();
	private List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
	//获取微专题医生列表
	private final String SOAP_NAMESPACE = MMloveConstants.URL001;
	private final String SOAP_ACTION = MMloveConstants.URL001
			+ MMloveConstants.DoctorInquiry;
	private final String SOAP_METHODNAME = MMloveConstants.DoctorInquiry;
	private final String SOAP_URL = MMloveConstants.URL002;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setTranslucentStatus();
		setContentView(R.layout.topic_all);
		iniView();
		txtHead.setText("专题汇总");
		user=LocalAccessor.getInstance(TopicAllAct.this).getUser();
		setListeners();
		mIntent=this.getIntent();
		if(mIntent!=null){
			DoctorId=mIntent.getStringExtra("DoctorID");
		}
		searchDocLists();
		
		
	}
	private void searchDocLists() {
		// TODO Auto-generated method stub
		JsonAsyncTaskOnComplete doProcess=new JsonAsyncTaskOnComplete(){

			@Override
			public void processJsonObject(Object result) {
				// TODO Auto-generated method stub
				String returnvalue= result.toString();
//				Toast.makeText(TopicAllAct.this,returnvalue,1).show();
			
				Map<String, Object> map;
					try {
						JSONObject mySO = (JSONObject) result;
						JSONArray array = mySO
								.getJSONArray("DoctorInquiry");
						
						for (int i = 0; i < array.length(); i++) {
					    map = new HashMap<String, Object>();
						map.put("DoctorName", array.getJSONObject(i)
								.getString("DoctorName"));
						map.put("DoctorID", array.getJSONObject(i)
								.getString("DoctorID"));
						if(DoctorId.equals(array.getJSONObject(i)
								.getString("DoctorID").toString())){
							if(i==0){
								doctorList.add(map);
							}else {
								doctorList.set(0, map);
							}
						}else{
						doctorList.add(map);
						}
						}
						addViewPagerView();
						
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				
			}};
		JsonAsyncTask_Info task = new JsonAsyncTask_Info(
				TopicAllAct.this, true, doProcess);
		Map<String, Object> paramMap = new HashMap<String, Object>();
	
		String str = "<Request><UserID>%s</UserID><TopicOpen>%s</TopicOpen></Request>";
		
		str = String.format(str, new Object[]
		{ user.UserID+"","1"});
	
			
         
		paramMap.put("str", str);

		// 必须是这5个参数，而且得按照顺序
		task.execute(SOAP_NAMESPACE, SOAP_ACTION, SOAP_METHODNAME,
				SOAP_URL, paramMap);
		
	}
	private void setListeners()
	{
		imgBack.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				finish();
			}
		});
	

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

	@SuppressLint("NewApi")
	private void addViewPagerView() {
		LayoutParams params = new LayoutParams();
		params.width = LayoutParams.WRAP_CONTENT;
		params.height = LayoutParams.WRAP_CONTENT;

		LayoutInflater inflater = this.getLayoutInflater();
		ArrayList<Fragment> newview = new ArrayList<Fragment>();
		for (int i = 0; i < doctorList.size(); i++) {

			String label = doctorList.get(i).get("DoctorName").toString();
			newview.add(TopicFragment.newInstance(doctorList.get(i).get("DoctorID").toString()));
//			onLoad(listTopic);
			TitleTextView tv = new TitleTextView(this);
			int itemWidth = (int) tv.getPaint().measureText(label);
			tv.setLayoutParams(new LinearLayout.LayoutParams((itemWidth * 2),
					-1));
			tv.setTextSize(14);
			tv.setText(label);
			tv.setGravity(Gravity.CENTER);
			tv.setOnClickListener(this);
			if (i == 0) {
				tv.setTextColor(getResources().getColor(R.color.pink));
				tv.setIsHorizontaline(true);
			} else {
				tv.setTextColor(getResources().getColor(R.color.hometext));
				tv.setIsHorizontaline(false);
			}
			mViewGroup.addView(tv);
		}
		

		mViewPager.setAdapter(new TopicNewsAdapter(getSupportFragmentManager(),newview));
		mViewPager.setCurrentItem(0);
		mViewPager.setOnPageChangeListener(this);

	}

	@Override
	public void onClick(View v) {
		// 点击tabbar
		for (int i = 0; i < mViewGroup.getChildCount(); i++) {
			TitleTextView child = (TitleTextView) mViewGroup
					.getChildAt(i);
			if (child == v) {
				mViewPager.setCurrentItem(i);
				break;
			}
		}

	}

	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	@Override
	public void onPageSelected(int selectPosition) {
		moveTitleLabel(selectPosition);
	}

	/*
	 * 点击新闻分类的tabbar，使点击的bar居中显示到屏幕中间
	 */
	@SuppressLint("NewApi")
	private void moveTitleLabel(int position) {

		// 点击当前按钮所有左边按钮的总宽度
		int visiableWidth = 0;
		// HorizontalScrollView的宽度
		int scrollViewWidth = 0;

		mViewGroup.measure(mViewGroup.getMeasuredWidth(), -1);
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
				mViewGroup.getMeasuredWidth(), -1);
		params.gravity = Gravity.CENTER_VERTICAL;
		mViewGroup.setLayoutParams(params);
		for (int i = 0; i < mViewGroup.getChildCount(); i++) {
			TitleTextView itemView = (TitleTextView) mViewGroup
					.getChildAt(i);
			int width = itemView.getMeasuredWidth();
			if (i < position) {
				visiableWidth += width;
			}
			scrollViewWidth += width;

//			if (i == mViewGroup.getChildCount() - 1) {
//				break;
//			}
			if (position != i) {
				itemView.setTextColor(getResources().getColor(R.color.hometext));
				itemView.setIsHorizontaline(false);
			} else {
				itemView.setTextColor(getResources().getColor(R.color.pink));
				itemView.setIsHorizontaline(true);
			}
			
		}
		// 当前点击按钮的宽度
		int titleWidth = mViewGroup.getChildAt(position).getMeasuredWidth();
		int nextTitleWidth = 0;
		if (position > 0) {
			// 当前点击按钮相邻右边按钮的宽度
			nextTitleWidth = position == mViewGroup.getChildCount() - 1 ? 0
					: mViewGroup.getChildAt(position - 1).getMeasuredWidth();
		}
		int screenWidth = getWindowManager().getDefaultDisplay().getWidth();
		final int move = visiableWidth - (screenWidth - titleWidth) / 2;
		if (mPreSelectItem < position) {// 向屏幕右边移动
			if ((visiableWidth + titleWidth + nextTitleWidth) >= (screenWidth / 2)) {
			
				((HorizontalScrollView) mViewGroup.getParent())
						.setScrollX(move);
		

			}
		} else {// 向屏幕左边移动
			if ((scrollViewWidth - visiableWidth) >= (screenWidth / 2)) {
				((HorizontalScrollView) mViewGroup.getParent())
						.setScrollX(move);
			}
		}
		mPreSelectItem = position;
	}


	protected void iniView() {
		// TODO Auto-generated method stub
		mViewPager = (ViewPager) findViewById(R.id.viewpager);
		mViewGroup = (ViewGroup) findViewById(R.id.viewgroup);

		txtHead = (TextView) findViewById(R.id.txtHead);
		imgBack = (ImageView) findViewById(R.id.img_left);
	}
	
	


}
