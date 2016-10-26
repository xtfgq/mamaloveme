package com.netlab.loveofmum;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;

import com.netlab.loveofmum.api.BaseActivity;
import com.netlab.loveofmum.api.MMloveConstants;
import com.netlab.loveofmum.api.JsonAsyncTaskOnComplete;
import com.netlab.loveofmum.api.JsonAsyncTask_Info;
import com.netlab.loveofmum.api.LocalAccessor;
import com.netlab.loveofmum.api.MyApplication;
import com.netlab.loveofmum.huanxin.ReplyPageListAdapter;

import com.netlab.loveofmum.myadapter.ZanAdapter;
import com.netlab.loveofmum.utils.SystemStatusManager;
import com.netlab.loveofmum.widget.ListViewForScrollView;
import com.netlab.loveofmum.widget.ScrollViewForListView;


public class ReplyPageOneList extends BaseActivity
{
	private List<Map<String, Object>> arrayList = new ArrayList<Map<String, Object>>();
	
	private ListViewForScrollView listview;
    private RelativeLayout rl_title;
	private ZanAdapter adapter;
	
	private int HospitalID;

	// 定义Web Service相关的字符串
	private final String SOAP_NAMESPACE = MMloveConstants.URL001;
	private final String SOAP_ACTION = MMloveConstants.URL001
			+ MMloveConstants.DoctorInquiry;
	private final String SOAP_METHODNAME = MMloveConstants.DoctorInquiry;
	private final String SOAP_URL = MMloveConstants.URL002;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_replypage_one);

		iniView();
		
		getZanList();
		if(hasInternetConnected())
		{
			getZanList();
		}
		else
		{
			Toast.makeText(ReplyPageOneList.this, R.string.msgUninternet,
					Toast.LENGTH_SHORT).show();
		}
	}
	
	
	
	@Override
	public boolean hasInternetConnected()
	{
		// TODO Auto-generated method stub
		return super.hasInternetConnected();
	}
	

	@Override
	protected void iniView()
	{ 
		// TODO Auto-generated method stub
		listview = (ListViewForScrollView) findViewById(R.id.listviewcontent);
		rl_title=(RelativeLayout) findViewById(R.id.rl_title);
		
		adapter=new ZanAdapter(this);
		listview.setAdapter(adapter);

	}
	private void getZanList() {
		// TODO Auto-generated method stub
		Map<String, Object> map;
	    for(int i=0;i<10;i++){
	   	 map = new HashMap<String, Object>();
		   
		   map.put("Name","赵铁柱"+i);
		   map.put("PicUrl", MMloveConstants.JE_BASE_URL2+"~\\UpLoadFile\\Doctor\\2015-08-06/20150806114200.JPG".replace("~", "").replace("\\", "/"));
		   arrayList.add(map);
	    }
	    adapter.setListdat(arrayList);
	    adapter.notifyDataSetChanged();
	}
	

	

	
}
