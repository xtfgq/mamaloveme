package com.netlab.loveofmum.community;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.netlab.loveofmum.R;
import com.netlab.loveofmum.SmallTalkAct;
import com.netlab.loveofmum.api.JsonAsyncTaskOnComplete;
import com.netlab.loveofmum.api.JsonAsyncTask_Info;
import com.netlab.loveofmum.api.MMloveConstants;
import com.netlab.loveofmum.myadapter.MyListViewAdapter;
import com.netlab.loveofmum.widget.xlistview.XListView;
import com.netlab.loveofmum.widget.xlistview.XListView.IXListViewListener;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class TopicFragment extends Fragment implements IXListViewListener{
	
	 private XListView listView;
	 private Context mContext;
    private MyListViewAdapter adapter;
    private List<Map<String,Object>> microTopicList=new ArrayList<Map<String,Object>>();
    private int page=1;
    private  String doctorid;

	private RelativeLayout rlbacktop;
	public static final int FRIST_GET_DATE = 111;
	public static final int REFRESH_GET_DATE = 112;
	public static final int LOADMORE_GET_DATE = 113;
	//获取微专题列表
	private final String SOAP_NAMESPACE = MMloveConstants.URL001;
	private final String SOAP_ACTION = MMloveConstants.URL001
			+ MMloveConstants.MicroTopicInquiry;
	private final String SOAP_METHODNAME = MMloveConstants.MicroTopicInquiry;
	private final String SOAP_URL = MMloveConstants.URL002;

	public static TopicFragment newInstance(String id) {
		TopicFragment fragment = new TopicFragment();
		fragment.doctorid=id;
		return fragment;
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view= inflater.inflate(R.layout.channel_news_view_pager_item,container,false);
		listView=(XListView) view.findViewById(R.id.listview_topic);
		rlbacktop=(RelativeLayout) view.findViewById(R.id.rlbacktop);
		
		adapter = new MyListViewAdapter(getActivity());
		listView.setAdapter(adapter);
		rlbacktop.setVisibility(View.GONE);
		listView.setPullLoadEnable(true);
		listView.setXListViewListener(this);
		listView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Map<String, Object> mapDetail = microTopicList.get(position-1);
				String ID = mapDetail.get("ID").toString();
				Intent i = new Intent(getActivity(),
						SmallTalkAct.class);
			i.putExtra("ID", ID);
//				i.putExtra("ID", hosId);
			
				startActivity(i);
			}});
		rlbacktop.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				listView.smoothScrollToPosition(0);
				
			}
		});
		
		getTopic(FRIST_GET_DATE);
		return view;
	}
	
	protected void getTopic(final int ACTION ){
		if(ACTION==FRIST_GET_DATE){//第一次加载
			if(microTopicList.size()>0){
				microTopicList.clear();
			}
     getMicroTopicInquiry(doctorid);
	
			}else if(ACTION==REFRESH_GET_DATE){//刷新数据
				if(microTopicList.size()>0){
				microTopicList.clear();
				}
				onLoad();
				page = 1;
				  getMicroTopicInquiry(doctorid);
			}else if(ACTION==LOADMORE_GET_DATE){
				page++;
				  getMicroTopicInquiry(doctorid);
			}
		adapter.notifyDataSetChanged();
		if(page>1){
			rlbacktop.setVisibility(View.VISIBLE);
			
		}else{
			rlbacktop.setVisibility(View.GONE);
			
		}
		}
		private void getMicroTopicInquiry(String doctorid) {
			JsonAsyncTaskOnComplete doProcess=new JsonAsyncTaskOnComplete(){

				@Override
				public void processJsonObject(Object result) {
					// TODO Auto-generated method stub
					String returnvalue= result.toString();
					Map<String, Object> map;
						try {
							JSONObject mySO = (JSONObject) result;
							JSONArray array = mySO
									.getJSONArray("MicroTopicInquiry");
//							{
//							    "MicroTopicInquiry": [
//							        {
//							            "ImageURL": "",
//							            "likecount": "0",
//							            "Fromto": "0",
//							            "Replys": "0",
//							            "Type": "3",
//							            "DeleteFlag": "0",
//							            "Likes": "0",
//							            "Title": "",
//							            "Status": "1",
//							            "CreatedDate": "2016/1/26 11:46:50",
//							            "ID": "15",
//							            "DoctorID": "28",
//							            "EndTime": "2016/1/28 10:21:50",
//							            "StartTime": "2016/1/27 10:21:50",
//							            "ReplysCount": "0",
//							            "rownumber": "1",
//							            "Content": "adfadfadfdfaf"
//							        }
//							    ]
//							}
//							{"MicroTopicInquiry":[{"MessageContent":"数据为空！","MessageCode":"2"}]}
//							for (int i = 0; i < array.length(); i++) {
//						    map = new HashMap<String, Object>();
//							map.put("DoctorName", array.getJSONObject(i)
//									.getString("DoctorName"));
//							map.put("DoctorID", array.getJSONObject(i)
//									.getString("DoctorID"));
//							microTopicList.add(map);
//							}
							if(array.getJSONObject(0).has("MessageCode")){
								Toast.makeText(mContext, "本医生暂时没有过往微专题", 1).show();
							}	else{
							
								for (int i = 0; i < array.length(); i++) {

									map = new HashMap<String, Object>();
									map.put("Title",
											array.getJSONObject(i).getString("Title"));
									map.put("ID",
											array.getJSONObject(i).getString("ID"));
									microTopicList.add(map);

								}
								adapter.setList(microTopicList);
								adapter.notifyDataSetChanged();
							}	
							
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						onLoad();
				}};
			JsonAsyncTask_Info task = new JsonAsyncTask_Info(
					mContext, true, doProcess);
			Map<String, Object> paramMap = new HashMap<String, Object>();
		
			String str = "<Request><DoctorID>%s</DoctorID><PageIndex>%s</PageIndex><PageSize>%s</PageSize><Type>%s</Type><Flag>%s</Flag></Request>";
			
			str = String.format(str, new Object[]
			{ doctorid,page+"",10+"","3","0"});
		
			paramMap.put("str", str);

			
			task.execute(SOAP_NAMESPACE, SOAP_ACTION, SOAP_METHODNAME,
					SOAP_URL, paramMap);
		
	}
		// TODO Auto-generated method stub
//		Map<String, Object> map;
//	    map = new HashMap<String, Object>();
//	    if(ACTION==REFRESH_GET_DATE||ACTION==LOADMORE_GET_DATE){
//	    	list.clear();
//		}else{
//			page++;
//		}
//	  
//		map.put("Topic", "xxxxxxx我怀孕13周，请问该如何保养！偶尔疼痛22222222");
//		list.add(map);
//		map.put("Topic", "xx333xxx我怀孕15周，请问该如何保养！偶尔疼痛22222222");
//		list.add(map);
//		map.put("Topic", "xx333xxx我怀孕15周，请问该如何保养！偶尔疼痛22222222");
//		list.add(map);
//		adapter.setList(list);
//		if(pagesize>1){
//			rlbacktop.setVisibility(View.VISIBLE);
//			
//		}else{
//			rlbacktop.setVisibility(View.GONE);
//			
//		}
//		adapter.notifyDataSetChanged();
//		onLoad();
	
	
		private void onLoad() {
			listView.stopRefresh();
			listView.stopLoadMore();
			SimpleDateFormat  formatter = new SimpleDateFormat("yyyy年MM月dd日   HH:mm:ss");     
			Date  curDate  =  new Date(System.currentTimeMillis());
			String   str   =   formatter.format(curDate);     
			listView.setRefreshTime(str);
		}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		

	}
	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		page=1;
		getTopic(REFRESH_GET_DATE);
	}
	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		
		getTopic(LOADMORE_GET_DATE);
	}



	

}
