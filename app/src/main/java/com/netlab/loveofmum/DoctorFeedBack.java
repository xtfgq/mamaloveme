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
//import com.netlab.loveofmum.widget.xlistview.ScrollXlistView;
//import com.netlab.loveofmum.widget.xlistview.ScrollXlistView.IXSrollListViewListener;
import com.netlab.loveofmum.widget.xlistview.XListView;
import com.netlab.loveofmum.widget.xlistview.XListView.IXListViewListener;


public class DoctorFeedBack extends BaseActivity{
	private ListViewForScrollView mListView;
	private DoctorAadpter mAdapter;
//	public static final int FRIST_GET_DATE = 111;
//	public static final int REFRESH_GET_DATE = 112;
//	public static final int LOADMORE_GET_DATE = 113;
	private int page=1;
	private List<Map<String,Object>> listDoc=new ArrayList<Map<String,Object>>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_post);
		 iniView();
		 mAdapter = new DoctorAadpter(DoctorFeedBack.this);
		
		 mListView.setAdapter(mAdapter);
		 setLvHeight(mListView);
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
		   int height=Util.getScreenHeight(DoctorFeedBack.this);
		   
		    ViewGroup.LayoutParams layoutParams = list.getLayoutParams();
		    layoutParams.height = totalHeight+600*height/1812+
		        + (list.getDividerHeight() * (adapter.getCount() - 1));// 总行高+每行的间距
		    list.setLayoutParams(layoutParams);
		  }

	@Override
	protected void iniView() {
		// TODO Auto-generated method stub
		mListView=(ListViewForScrollView) findViewById(R.id.listview_post);
	}
	private void geneItems() {
//	if(ACTION==FRIST_GET_DATE){//第一次加载
		Map<String, Object> map;
	    for(int i=0;i<10;i++){
	    	 map = new HashMap<String, Object>();
	    map.put("Content", "zhiyi"+i);
	   map.put("Name","赵铁柱"+i);
	   map.put("PicUrl", MMloveConstants.JE_BASE_URL2+"~\\UpLoadFile\\Doctor\\2015-08-06/20150806114200.JPG".replace("~", "").replace("\\", "/"));
	    listDoc.add(map);
	    }
	
		mAdapter.setListdat(listDoc);
	
		
//	}else if(ACTION==REFRESH_GET_DATE){//刷新数据
//		Map<String, Object> map;
//	    for(int i=0;i<10;i++){
//	    	 map = new HashMap<String, Object>();
//	    map.put("Content", "zhiyi"+i);
//	   map.put("Name","赵铁柱"+i);
//	   map.put("PicUrl", MMloveConstants.JE_BASE_URL2+"~\\UpLoadFile\\Doctor\\2015-08-06/20150806114200.JPG".replace("~", "").replace("\\", "/"));
//	    listDoc.add(map);
//	    }
//		onLoad();
//		page = 1;
//		
//	}else if(ACTION==LOADMORE_GET_DATE){//加载更多
//		Map<String, Object> map;
//	    for(int i=0;i<10;i++){
//	    	 map = new HashMap<String, Object>();
//	    map.put("Content", "zhiyi"+i);
//	   map.put("Name","赵铁柱"+i);
//	   map.put("PicUrl", MMloveConstants.JE_BASE_URL2+"~\\UpLoadFile\\Doctor\\2015-08-06/20150806114200.JPG".replace("~", "").replace("\\", "/"));
//	    listDoc.add(map);
//	    }
//		onLoad();
//		page++;
//	}
	mAdapter.notifyDataSetChanged();
//	setListViewHeightBasedOnChildren(mListView);
//	mListView.reSize(mListView);
	}
//	@Override
//	public void onRefresh() {
//				geneItems(REFRESH_GET_DATE);
//	}
//    
//	
//	private void onLoad() {
//
//		mListView.stopRefresh();
//		mListView.stopLoadMore();
//		SimpleDateFormat  formatter = new SimpleDateFormat("yyyy年MM月dd日   HH:mm:ss");     
//		Date  curDate  =  new Date(System.currentTimeMillis());
//		String   str   =   formatter.format(curDate);     
//		mListView.setRefreshTime(str);
////		setListViewHeightBasedOnChildren(mListView);
//	}
//	
//	@Override
//	public void onLoadMore() {
//		geneItems(LOADMORE_GET_DATE);
//	}
}
