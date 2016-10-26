package com.netlab.loveofmum;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.netlab.loveofmum.api.BaseActivity;
import com.netlab.loveofmum.api.JsonAsyncTaskOnComplete;
import com.netlab.loveofmum.api.JsonAsyncTask_Info;
import com.netlab.loveofmum.api.LocalAccessor;
import com.netlab.loveofmum.api.MMloveConstants;
import com.netlab.loveofmum.api.MyApplication;
import com.netlab.loveofmum.huanxin.ChatMsgEntity;
import com.netlab.loveofmum.huanxin.ChatMsgViewAdapter;
import com.netlab.loveofmum.huanxin.MainChatActivity;
import com.netlab.loveofmum.model.User;
import com.netlab.loveofmum.utils.SystemStatusManager;
import com.umeng.analytics.MobclickAgent;


public class HistoryList extends BaseActivity {
	private List<ChatMsgEntity> mDateArrays = new ArrayList<ChatMsgEntity>();
	private Intent mIntent;
	private User user;
	private ImageView mBtnBack;
	String DoctorId,AskOrderID,OrderStatus,DoctorName;
	private TextView tv_nick,tv_nodata;
	private ListView mListView;
	private ChatMsgViewAdapter mAdapter;
	private final String SOAP_NAMESPACE = MMloveConstants.URL001;
	private final String SOAP_ACTION = MMloveConstants.URL001
			+ MMloveConstants.ConsultationInquiry;
	private final String SOAP_METHODNAME = MMloveConstants.ConsultationInquiry;
	private final String SOAP_URL = MMloveConstants.URL002;
	private Handler mHandler= new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				mListView.setVisibility(View.GONE);
				tv_nodata.setVisibility(View.VISIBLE);
				
				break;

			default:
				break;
			}
		}
		
	};
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setTranslucentStatus() ;
		setContentView(R.layout.activity_history);
		mIntent=this.getIntent();
		iniView();
		tv_nodata.setVisibility(View.GONE);
		mBtnBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		if(mIntent!=null){
			AskOrderID=mIntent.getStringExtra("AskOrderID");
			DoctorId=mIntent.getStringExtra("DoctorID");
			OrderStatus=mIntent.getStringExtra("OrderStatus");
			DoctorName=mIntent.getStringExtra("DoctorName");
		}
		user = LocalAccessor.getInstance(HistoryList.this).getUser();
		tv_nick.setText(DoctorName);
		initData();
//		tv_nodata.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				Intent i = new Intent(HistoryList.this,MainChatActivity.class);
//				i.putExtra("DoctorID", DoctorId);
//				
//				i.putExtra("AskOrderID", AskOrderID);
//				startActivity(i);
//			}
//		});
//		yu.Week + "周+" + yu.Days;

	}
	
	@Override
	protected void onResume() {
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

	@Override
	protected void iniView() {
		// TODO Auto-generated method stub
		mListView = (ListView)findViewById(R.id.listview);
		mBtnBack = (ImageView)findViewById(R.id.btn_back);
		tv_nick=(TextView) findViewById(R.id.tv_nick);
		tv_nodata=(TextView) findViewById(R.id.tv_nodata);
		
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
		tintManager.setStatusBarTintResource(R.color.home);//状态栏无背景
	}
	public void initData(){
		JsonAsyncTaskOnComplete doProcess=new JsonAsyncTaskOnComplete(){
			@Override
			public void processJsonObject(Object result) {
				// TODO Auto-generated method stub
				String returnvalue= result.toString();
				//{"ConsultationInquiry":[{"MessageContent":"数据为空！","MessageCode":"2"}]}
				   JSONObject mySO = (JSONObject) result;
				   
				   try { 
					JSONArray array = mySO.getJSONArray("ConsultationInquiry");
					
					if(mDateArrays.size()>0){
						mDateArrays.clear();
						
					}
					for(int i=0;i<array.length();i++){
					ChatMsgEntity entity = new ChatMsgEntity();
					String time = array.getJSONObject(i).getString("CreatedDate").toString().replace("/", "-");
					entity.setDate(time);
					entity.setName(array.getJSONObject(i).getString("AuthorName").toString());
					if("0".equals(array.getJSONObject(i).getString("UserType").toString())){
						entity.setMsgType(false);
						entity.setHeadurl(user.PicURL);
					}else{

						entity.setHeadurl( MMloveConstants.JE_BASE_URL2+array.getJSONObject(i).getString("DocPicURL").toString().replace("~", "").replace("\\", "/"));
						entity.setMsgType(true);
					}
					if("0".equals(array.getJSONObject(i).getString("MsgType").toString())){
						entity.setMsgtype(0);
						entity.setText(array.getJSONObject(i).getString("ConsultationContent").toString());
						entity.setContent("");
					}else{
						entity.setMsgtype(1);
						entity.setContent( MMloveConstants.JE_BASE_URL2+array.getJSONObject(i).getString("PicURL").toString().replace("~", "").replace("\\", "/"));
						entity.setText("");
					}
				
					  mDateArrays.add(entity); 
					}
					if(mAdapter==null){
					mAdapter = new ChatMsgViewAdapter(HistoryList.this, mDateArrays);
					mListView.setAdapter(mAdapter);
					}else{
					mAdapter.notifyDataSetChanged();
					}
					
					
					} catch (Exception e) {
						// TODO Auto-generated catch block
//						if(!"04".equals(OrderStatus)){
						  Message message = Message.obtain();
							message.what = 0; 
							mHandler.sendMessage(message);
//						}
						e.printStackTrace();
					}
				 
			}}; 
			
		JsonAsyncTask_Info task = new JsonAsyncTask_Info(
				HistoryList.this, true, doProcess);
		Map<String, Object> paramMap = new HashMap<String, Object>();
		String str = "<Request><DoctorID>%s</DoctorID><AuthorID>%s</AuthorID><AskOrderID>%s</AskOrderID><UserType>%s</UserType></Request>";
		str = String.format(str, new Object[]
				{ DoctorId,user.UserID+"",AskOrderID,"01"});
				paramMap.put("str", str);
				task.execute(SOAP_NAMESPACE, SOAP_ACTION, SOAP_METHODNAME,
						SOAP_URL, paramMap);
				
	
	}
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
}

