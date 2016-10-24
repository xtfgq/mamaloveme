package com.netlab.loveofmum;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.netlab.loveofmum.api.BaseActivity;
import com.netlab.loveofmum.api.MMloveConstants;
import com.netlab.loveofmum.myadapter.CommAdapter;
import com.netlab.loveofmum.myadapter.ZanAdapter;
import com.netlab.loveofmum.utils.SystemStatusManager;
import com.netlab.loveofmum.widget.ListViewForScrollView;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;

public class ReplyPageTwoListAct extends BaseActivity {
private List<Map<String, Object>> arrayList = new ArrayList<Map<String, Object>>();
	
	private ListViewForScrollView listview;

	private CommAdapter adapter;

	// 定义Web Service相关的字符串
		private final String SOAP_NAMESPACE = MMloveConstants.URL001;
		private final String SOAP_ACTION = MMloveConstants.URL001
				+ MMloveConstants.DoctorInquiry;
		private final String SOAP_METHODNAME = MMloveConstants.DoctorInquiry;
		private final String SOAP_URL = MMloveConstants.URL002;
	@Override
	protected void iniView() {
		// TODO Auto-generated method stub
		listview = (ListViewForScrollView) findViewById(R.id.listviewcontent);

		adapter=new CommAdapter(this);
		listview.setAdapter(adapter);
		
	}
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	
		setContentView(R.layout.layout_replypage_one);

		iniView();
		
	
		if(hasInternetConnected())
		{
			getReolayList();
		}
		else
		{
			Toast.makeText(ReplyPageTwoListAct.this, R.string.msgUninternet,
					Toast.LENGTH_SHORT).show();
		}
	}
	private void getReolayList() {
		// TODO Auto-generated method stub
		Map<String, Object> map;
	    for(int i=0;i<10;i++){
	   	 map = new HashMap<String, Object>();
		   map.put("Content","好好了");
		   map.put("Name","赵铁柱在回复"+i);
		   map.put("Time",i+1+"分钟前");
		   map.put("PicUrl", MMloveConstants.JE_BASE_URL2+"~\\UpLoadFile\\Doctor\\2015-08-06/20150806114200.JPG".replace("~", "").replace("\\", "/"));
		   arrayList.add(map);
	    }
	    adapter.setListdat(arrayList);
	    adapter.notifyDataSetChanged();
	}



}
