package com.netlab.loveofmum;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.netlab.loveofmum.api.BaseActivity;
import com.netlab.loveofmum.api.MMloveConstants;
import com.netlab.loveofmum.myadapter.DoctorAadpter;
import com.netlab.loveofmum.timepicker.Util;
import com.netlab.loveofmum.widget.ListViewForScrollView;
import com.netlab.loveofmum.widget.xlistview.XListView;
import com.netlab.loveofmum.widget.xlistview.XListView.IXListViewListener;
import com.umeng.analytics.MobclickAgent;

public class DoctorCommAct extends BaseActivity {
	private ListViewForScrollView mListView;
	private DoctorAadpter mAdapter;
//	public static final int FRIST_GET_DATE = 111;
//	public static final int REFRESH_GET_DATE = 112;
//	public static final int LOADMORE_GET_DATE = 113;
//	private int page=1;
	private List<Map<String,Object>> listDoc=new ArrayList<Map<String,Object>>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_post);
		 iniView();
//		 mListView.setPullLoadEnable(true);
//		
//		 mListView.setXListViewListener(this);
		 mAdapter = new DoctorAadpter(DoctorCommAct.this);
			mListView.setAdapter(mAdapter);
//			setLvHeight(mListView);
		 geneItems();
	}
	 public void setLvHeight(ListView list) {
		    ListAdapter adapter = list.getAdapter();
		    if (adapter == null) {
		      return;
		    }
		    int totalHeight = 0;
		    for (int i = 0; i < adapter.getCount(); i++) {
		      View itemView = adapter.getView(i, null, list);
		      itemView.measure(0, 0);
		      totalHeight += itemView.getMeasuredHeight();
		    }
		    int height=Util.getScreenHeight(DoctorCommAct.this);
		    ViewGroup.LayoutParams layoutParams = list.getLayoutParams();
		    layoutParams.height = totalHeight+600*height/1812
		        + (list.getDividerHeight() * (adapter.getCount() - 1));// 总行高+每行的间距
		    list.setLayoutParams(layoutParams);
		  }

	@Override
	protected void iniView() {
		// TODO Auto-generated method stub
		mListView=(ListViewForScrollView) findViewById(R.id.listview_post);
	}
	@Override
	protected void onResume()
	{
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

	private void geneItems() {
//	if(ACTION==FRIST_GET_DATE){//第一次加载
		Map<String, Object> map;
	    for(int i=0;i<10;i++){
	    	 map = new HashMap<String, Object>();
	    map.put("Content", "我需要提问应该注意一下事项"+i);
	   map.put("Name","赵铁柱"+i);
	   map.put("PicUrl", MMloveConstants.JE_BASE_URL2+"~\\UpLoadFile\\Doctor\\2015-08-06/20150806114200.JPG".replace("~", "").replace("\\", "/"));
	    listDoc.add(map);
	    }
	
		mAdapter.setListdat(listDoc);
		mAdapter.notifyDataSetChanged();
//	}else if(ACTION==REFRESH_GET_DATE){//刷新数据
//		listDoc.clear();
//		Map<String, Object> map;
//	    for(int i=0;i<10;i++){
//	    	 map = new HashMap<String, Object>();
//	    map.put("Content", "我想知道孕期应该注意什么"+i);
//	   map.put("Name","赵铁柱"+i);
//	   map.put("PicUrl", MMloveConstants.JE_BASE_URL2+"~\\UpLoadFile\\Doctor\\2015-08-06/20150806114200.JPG".replace("~", "").replace("\\", "/"));
//	    listDoc.add(map);
//	    }
//		mAdapter.notifyDataSetChanged();
//		onLoad();
//		page = 1;
//	}else if(ACTION==LOADMORE_GET_DATE){//加载更多
//		Map<String, Object> map;
//	    for(int i=0;i<10;i++){
//	    map = new HashMap<String, Object>();
//	    map.put("Content", "我想知道提问什么时候能回答"+i);
//	   map.put("Name","赵铁柱"+i);
//	   map.put("PicUrl", MMloveConstants.JE_BASE_URL2+"~\\UpLoadFile\\Doctor\\2015-08-06/20150806114200.JPG".replace("~", "").replace("\\", "/"));
//	    listDoc.add(map);
//	    }
//		mAdapter.notifyDataSetChanged();
//		onLoad();
//		page++;
	}
//	setListViewHeightBasedOnChildren(mListView);
	
	}
//	@Override
//	public void onRefresh() {
//				geneItems(REFRESH_GET_DATE);
//	}
    
	
//	private void onLoad() {
//
//		mListView.stopRefresh();
//		mListView.stopLoadMore();
//		SimpleDateFormat  formatter = new SimpleDateFormat("yyyy年MM月dd日   HH:mm:ss");     
//		Date  curDate  =  new Date(System.currentTimeMillis());
//		String   str   =   formatter.format(curDate);     
//		mListView.setRefreshTime(str);
//	}
	
//	@Override
//	public void onLoadMore() {
//		geneItems(LOADMORE_GET_DATE);
//	}
//	public void setListViewHeightBasedOnChildren(ListView listView)
//	{
//		// 获取ListView对应的Adapter
//		ListAdapter listAdapter = listView.getAdapter();
//		if (listAdapter == null)
//		{
//			return;
//		}
//		int totalHeight = 0;
//		for (int i = 0; i < listAdapter.getCount(); i++)
//		{ // listAdapter.getCount()返回数据项的数目
//			View listItem = listAdapter.getView(i, null, listView);
//			listItem.measure(0, 0); // 计算子项View 的宽高
//			totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
//		}
//		ViewGroup.LayoutParams params = listView.getLayoutParams();
//		params.height = totalHeight
//				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
//		// listView.getDividerHeight()获取子项间分隔符占用的高度
//		// params.height最后得到整个ListView完整显示需要的高度
//		listView.setLayoutParams(params);
//	}

