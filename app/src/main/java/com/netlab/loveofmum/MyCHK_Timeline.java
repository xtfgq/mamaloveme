package com.netlab.loveofmum;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ParseException;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.netlab.loveofmum.api.BaseActivity;
import com.netlab.loveofmum.api.MMloveConstants;
import com.netlab.loveofmum.api.JsonAsyncTaskOnComplete;
import com.netlab.loveofmum.api.JsonAsyncTask_Info;
import com.netlab.loveofmum.api.LocalAccessor;
import com.netlab.loveofmum.api.MyApplication;
import com.netlab.loveofmum.model.User;
import com.netlab.loveofmum.myadapter.MyCHK_Timeline_Adapter;
import com.netlab.loveofmum.myadapter.MyCHK_Timeline_Adapter2;
import com.netlab.loveofmum.myadapter.MyNoOrderAdapter;
import com.netlab.loveofmum.myadapter.MyOrderlist;
import com.netlab.loveofmum.utils.SystemStatusManager;
import com.umeng.analytics.MobclickAgent;


public class MyCHK_Timeline extends BaseActivity implements  OnClickListener
{

	private TextView txtHead,tv_right;

	private List<Map<String, Object>> arrayList;
	private String returnvalue001;
	private ListView listview;
	List<Map<String,Object>> templist;
	private List<Map<String, Object>> orderarrayList=new ArrayList<Map<String, Object>>();
	private ImageView imgAdd;
	
	private User user;
    String page;

	Boolean isfirst=false;
	// 定义Web Service相关的字符串
	private final String SOAP_NAMESPACE = MMloveConstants.URL001;
	private final String SOAP_ACTION = MMloveConstants.URL001
			+ MMloveConstants.OrderInquiry;
	private final String SOAP_METHODNAME = MMloveConstants.OrderInquiry;
	private final String SOAP_URL = MMloveConstants.URL002;
	private MyOrderlist orderlist;
	private LinearLayout llcheck;
	private Button btnCheck;
	private BroadcastReceiver mRefreshBroadcastReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if (intent.getAction().equals("action.foot")) {
				searchCHKList(0);
			}
		}

	};
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				MyCHK_Timeline_Adapter adapter = new MyCHK_Timeline_Adapter(MyCHK_Timeline.this, arrayList);
				listview.setAdapter(adapter);
				break;
			default:
				break;
			}
			
		}
	};

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setTranslucentStatus() ;
		setContentView(R.layout.layout_chk_timeline);
		Intent	mIntent=this.getIntent();
		if(mIntent!=null){
			page= mIntent.getStringExtra("Page");
		}
		iniView();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("action.foot");
		registerReceiver(mRefreshBroadcastReceiver, intentFilter);
		MyApplication.getInstance().addActivity(this);
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

	@Override
	protected void iniView()
	{
		// TODO Auto-generated method stub
		txtHead = (TextView) findViewById(R.id.txtHead);
		findViewById(R.id.img_left).setOnClickListener(this);
		imgAdd = (ImageView)findViewById(R.id.img_right);
		imgAdd.setImageResource(R.drawable.icon_add_foot);
		imgAdd.setOnClickListener(this);
		listview = (ListView) findViewById(R.id.listview_timeline);
		tv_right=(TextView) findViewById(R.id.tv_right);
		tv_right.setOnClickListener(this);
		llcheck=(LinearLayout)findViewById(R.id.llcheck);
		btnCheck=(Button) llcheck.findViewById(R.id.btn_my_check);
		btnCheck.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(MyCHK_Timeline.this, MyCHK_OrderAdd.class);
				i.putExtra("Add_food", "Add");
				startActivity(i);
			}
		});
		user =LocalAccessor.getInstance(MyCHK_Timeline.this).getUser();
	
		if(page.equals("User_Foot")){
			txtHead.setText("妈妈足迹");
			
			imgAdd.setVisibility(View.VISIBLE);
		}else if(page.equals("User_Order")){
			txtHead.setText("产检预约订单");
			tv_right.setVisibility(View.VISIBLE);
		}

		imgAdd.setOnClickListener(this);
		
	
		
	}
	public void refesh(){

	}
	/*
	 * 查询gridview
	 */
	private void searchCHKList(final int flag)
	{
		try
		{
			JsonAsyncTaskOnComplete doProcess = new JsonAsyncTaskOnComplete()
			{
				public void processJsonObject(Object result)
				{
//					{"OrderInquiry":[{"ID":"1701","OrderID":"2015121810255950401","UserID":"50401","OrderStatus":"04","PayType":"01","PayStatus":"02","IsUpload":"1","HospitalID":"1","HospitalName":"null","DeptDesc":"","DoctorName":"null","CHKID":"","CHKType":"","Gotime":"2015\/12\/18 0:00:00","YunWeek":"2","RegisterMoney":"0","BeginTime":"","EndTime":"","Reason":"拒绝快来了","MomSay":"","CreatedBy":"","CreatedDate":"2015\/12\/18 10:28:10","UpdateDBy":"","UpdatedDate":"2015\/12\/18 10:28:10","OrderStatusName":"用户上传","PayStatusName":"已付款"},{"ID":"1700","OrderID":"2015121810163150401","UserID":"50401","OrderStatus":"04","PayType":"01","PayStatus":"02","IsUpload":"1","HospitalID":"1","HospitalName":"null","DeptDesc":"","DoctorName":"null","CHKID":"","CHKType":"","Gotime":"2015\/12\/18 0:00:00","YunWeek":"2","RegisterMoney":"0","BeginTime":"","EndTime":"","Reason":"古古惑惑","MomSay":"","CreatedBy":"","CreatedDate":"2015\/12\/18 10:16:31","UpdateDBy":"","UpdatedDate":"2015\/12\/18 10:16:31","OrderStatusName":"用户上传","PayStatusName":"已付款"},{"ID":"1631","OrderID":"20151209034613-50401","UserID":"50401","OrderStatus":"01","PayType":"01","PayStatus":"01","IsUpload":"0","HospitalID":"1","HospitalName":"郑州大学第三附属医院","DeptDesc":"","DoctorName":"一附院test","CHKID":"33","CHKType":"第(11-13周)产检","Gotime":"2015\/12\/17 0:00:00","YunWeek":"1","RegisterMoney":"352.68","BeginTime":"14:00","EndTime":"14:15","Reason":"","MomSay":"","CreatedBy":"","CreatedDate":"2015\/12\/9 15:46:13","UpdateDBy":"","UpdatedDate":"2015\/12\/9 15:46:13","OrderStatusName":"待确认","PayStatusName":"未付款"},{"ID":"1676","OrderID":"2015121611012750401","UserID":"50401","OrderStatus":"04","PayType":"01","PayStatus":"02","IsUpload":"1","HospitalID":"1","HospitalName":"null","DeptDesc":"","DoctorName":"null","CHKID":"","CHKType":"","Gotime":"2015\/12\/16 0:00:00","YunWeek":"1","RegisterMoney":"0","BeginTime":"","EndTime":"","Reason":"发挥好分","MomSay":"","CreatedBy":"","CreatedDate":"2015\/12\/16 11:02:07","UpdateDBy":"","UpdatedDate":"2015\/12\/16 11:02:07","OrderStatusName":"用户上传","PayStatusName":"已付款"},{"ID":"1675","OrderID":"2015121610441450401","UserID":"50401","OrderStatus":"04","PayType":"01","PayStatus":"02","IsUpload":"1","HospitalID":"1","HospitalName":"null","DeptDesc":"","DoctorName":"null","CHKID":"","CHKType":"","Gotime":"2015\/12\/16 0:00:00","YunWeek":"1","RegisterMoney":"0","BeginTime":"","EndTime":"","Reason":"呵呵呵","MomSay":"","CreatedBy":"","CreatedDate":"2015\/12\/16 10:44:52","UpdateDBy":"","UpdatedDate":"2015\/12\/16 10:44:52","OrderStatusName":"用户上传","PayStatusName":"已付款"},{"ID":"1674","OrderID":"2015121609533950401","UserID":"50401","OrderStatus":"04","PayType":"01","PayStatus":"02","IsUpload":"1","HospitalID":"1","HospitalName":"null","DeptDesc":"","DoctorName":"null","CHKID":"","CHKType":"","Gotime":"2015\/12\/16 0:00:00","YunWeek":"1","RegisterMoney":"0","BeginTime":"","EndTime":"","Reason":"富贵花好","MomSay":"","CreatedBy":"","CreatedDate":"2015\/12\/16 9:53:46","UpdateDBy":"","UpdatedDate":"2015\/12\/16 9:53:46","OrderStatusName":"用户上传","PayStatusName":"已付款"},{"ID":"1672","OrderID":"2015121609391950401","UserID":"50401","OrderStatus":"04","PayType":"01","PayStatus":"02","IsUpload":"1","HospitalID":"1","HospitalName":"null","DeptDesc":"","DoctorName":"null","CHKID":"","CHKType":"","Gotime":"2015\/12\/16 0:00:00","YunWeek":"1","RegisterMoney":"0","BeginTime":"","EndTime":"","Reason":"默默","MomSay":"啦啦啦","CreatedBy":"","CreatedDate":"2015\/12\/16 9:39:31","UpdateDBy":"","UpdatedDate":"2015\/12\/17 22:02:10","OrderStatusName":"用户上传","PayStatusName":"已付款"},{"ID":"1671","OrderID":"2015121609375650401","UserID":"50401","OrderStatus":"04","PayType":"01","PayStatus":"02","IsUpload":"1","HospitalID":"1","HospitalName":"null","DeptDesc":"","DoctorName":"null","CHKID":"","CHKType":"","Gotime":"2015\/12\/16 0:00:00","YunWeek":"1","RegisterMoney":"0","BeginTime":"","EndTime":"","Reason":"啦啦啦","MomSay":"","CreatedBy":"","CreatedDate":"2015\/12\/16 9:38:55","UpdateDBy":"","UpdatedDate":"2015\/12\/16 9:38:55","OrderStatusName":"用户上传","PayStatusName":"已付款"},{"ID":"1634","OrderID":"20151209051046-50401","UserID":"50401","OrderStatus":"01","PayType":"01","PayStatus":"01","IsUpload":"1","HospitalID":"1","HospitalName":"郑州大学第三附属医院","DeptDesc":"","DoctorName":"胡孟彩","CHKID":"33","CHKType":"第(11-13周)产检","Gotime":"2015\/12\/12 0:00:00","YunWeek":"1","RegisterMoney":"352.68","BeginTime":"15:00","EndTime":"16:00","Reason":"","MomSay":"","CreatedBy":"","CreatedDate":"2015\/12\/9 17:10:46","UpdateDBy":"","UpdatedDate":"2015\/12\/16 10:48:08","OrderStatusName":"待确认","PayStatusName":"未付款"},{"ID":"1618","OrderID":"20151208045036-50401","UserID":"50401","OrderStatus":"07","PayType":"01","PayStatus":"01","IsUpload":"1","HospitalID":"46","HospitalName":"郑州大学第一附属医院","DeptDesc":"","DoctorName":"张梅","CHKID":"33","CHKType":"第(11-13周)产检","Gotime":"2015\/12\/11 0:00:00","YunWeek":"1","RegisterMoney":"0","BeginTime":"8:00","EndTime":"12:00","Reason":"","MomSay":"","CreatedBy":"","CreatedDate":"2015\/12\/8 16:50:38","UpdateDBy":"","UpdatedDate":"2015\/12\/12 15:55:22","OrderStatusName":"用户取消","PayStatusName":"未付款"},{"ID":"1615","OrderID":"20151208110846-50401","UserID":"50401","OrderStatus":"07","PayType":"01","PayStatus":"01","IsUpload":"0","HospitalID":"46","HospitalName":"郑州大学第一附属医院","DeptDesc":"","DoctorName":"赵先兰","CHKID":"33","CHKType":"第(11-13周)产检","Gotime":"2015\/12\/10 0:00:00","YunWeek":"1","RegisterMoney":"0","BeginTime":"8:00","EndTime":"12:00","Reason":"","MomSay":"","CreatedBy":"","CreatedDate":"2015\/12\/8 11:08:47","UpdateDBy":"","UpdatedDate":"2015\/12\/8 11:13:39","OrderStatusName":"用户取消","PayStatusName":"未付款"}]}
					returnvalue001 = result.toString();
					if (returnvalue001 == null)
					{

					}
					else
					{
						Map<String, Object> map;
						arrayList = new ArrayList<Map<String, Object>>();
						templist= new ArrayList<Map<String, Object>>();
						// 下边是具体的处理
						 List<Integer> listWithOrder = new ArrayList<Integer>();
						 
						try
						{
							int length=0;
							JSONObject mySO = (JSONObject) result;
							JSONArray array = mySO
									.getJSONArray("OrderInquiry");
							
							if(array.getJSONObject(0).has("MessageCode"))
							{
								if(page.equals("User_Order")){
								tv_right.setVisibility(View.GONE);
								}
							}
							else
							{
								length = array.length();
							}
							
							if(length>0)
							{
								if (orderarrayList.size()>0) {
									orderarrayList.clear();
									
								} 
								if(templist.size()>0){
									templist.clear();
								}
//								listview.removeAllViews();
								for (int i = 0; i < array.length(); i++)
								{
									map = new HashMap<String, Object>();
	
									map.put("OrderID",
											array.getJSONObject(i).getString("OrderID"));
									map.put("MomSay",
											array.getJSONObject(i).getString("MomSay"));
									map.put("ID",
											array.getJSONObject(i).getString("ID"));
									map.put("OrderStatusName",
											array.getJSONObject(i).getString("OrderStatusName"));
									map.put("OrderStatus",
											array.getJSONObject(i).getString("OrderStatus"));
									map.put("PayStatusName", array.getJSONObject(i)
											.getString("PayStatusName"));
									map.put("IsUpload", array.getJSONObject(i)
											.getString("IsUpload"));
									map.put("HospitalName", array.getJSONObject(i)
											.getString("HospitalName"));
									map.put("DoctorName", array.getJSONObject(i)
											.getString("DoctorName"));
									map.put("Gotime", array.getJSONObject(i)
											.getString("Gotime"));
									map.put("CreatedDate", array.getJSONObject(i)
											.getString("CreatedDate"));
									map.put("HospitalID", array.getJSONObject(i)
											.getString("HospitalID"));
									
									map.put("Reason", array.getJSONObject(i)
											.getString("Reason"));
									map.put("BeginTime", array.getJSONObject(i)
											.getString("BeginTime"));
									map.put("EndTime", array.getJSONObject(i)
											.getString("EndTime"));
									map.put("TitleName", "孕"+array.getJSONObject(i)
											.getString("YunWeek")+"周");
									map.put("Week", array.getJSONObject(i)
											.getString("YunWeek"));
									listWithOrder.add(Integer.valueOf(array.getJSONObject(i)
											.getString("YunWeek")));
									
									if(page.equals("User_Foot")){
									
									
									  int status=Integer.valueOf(array.getJSONObject(i).getString("OrderStatus").toString());	
									  if(status==2||status==3||status==1){
											String time = array.getJSONObject(i)
													.getString("Gotime").toString();
											
										SimpleDateFormat formatter = new SimpleDateFormat(
												"yyyy-MM-dd");
										time = time.split(" ")[0].replace("/", "-");
										
										
										Date dGotime=stringToDate(time,"yyyy-MM-dd");
										long timelengeth = 24 * 60 * 60 * 1000;
										Date dEnd = new Date(dGotime.getTime() + timelengeth);
										Date dnow=new Date();
										long intermilli = dnow.getTime() - dEnd.getTime();
										  if(intermilli>=0) 
											  templist.add(map);
											}else if(status==4){
												  templist.add(map);
											}
									  
							 
							  } else if(page.equals("User_Order")){
								  orderarrayList.add(map);
							  }
									
								}
								MyCHK_Timeline_Adapter adapter;
								if(page.equals("User_Foot")){
								
									listWithOrder=new ArrayList<Integer>(new HashSet<Integer>(listWithOrder));
			                          Collections.sort(listWithOrder, new Comparator<Integer>() { 
										@Override
										public int compare(Integer lhs, Integer rhs) {
											return rhs.compareTo(lhs);
										} 
			              			              });

										
											for(int i=0;i<listWithOrder.size();i++){
												int m=listWithOrder.get(i);
												for(int n=0;n<templist.size();n++){
													if(m==Integer.valueOf(templist.get(n).get("Week").toString())){
														arrayList.add(templist.get(n));
													}
												}
												
											}
										if(arrayList.size()>0){
										if(!isfirst){
											isfirst=true;
										}
										
											
											adapter = new MyCHK_Timeline_Adapter(MyCHK_Timeline.this,arrayList);
											listview.setAdapter(adapter);
											listview.setVisibility(View.VISIBLE);
											llcheck.setVisibility(View.GONE);
										}else{
											listview.setVisibility(View.GONE);
											llcheck.setVisibility(View.VISIBLE);

										}
									 
								}else if(page.equals("User_Order")){
									
									orderlist = new MyOrderlist(MyCHK_Timeline.this, orderarrayList);
									listview.setAdapter(orderlist);
									listview.setVisibility(View.VISIBLE);
									llcheck.setVisibility(View.GONE);
									listview.setOnItemClickListener(new OnItemClickListener()
									{
		
										public void onItemClick(AdapterView<?> arg0,
												View arg1, int arg2, long arg3)
										{
											Map<String, Object> mapDetail = new HashMap<String, Object>();
											if (orderarrayList.isEmpty())
											{
		
											}
											else
											{
												mapDetail = orderarrayList.get(arg2);
												try {
													String time = mapDetail.get("Gotime").toString();
													
													SimpleDateFormat formatter = new SimpleDateFormat(
															"yyyy-MM-dd");
													time = time.split(" ")[0].replace("/", "-");
													time = formatter.format(formatter
															.parse(time));
													Date d = formatter.parse(time);
													
													Date dnow = formatter.parse(formatter.format(new Date()));
													String OrderStatus= mapDetail.get("OrderStatus").toString();
													String ordername=mapDetail.get("OrderStatusName").toString();
													
													if(ordername.equals("待确认")||ordername.equals("待检查")){
															if(d.compareTo(dnow)<0){
																Intent i = new Intent(MyCHK_Timeline.this, MyCHK_OrderAdd.class);
																String ID = mapDetail.get("ID").toString();
																String MomSay=mapDetail.get("MomSay").toString();
																i.putExtra("page", "User_Order");
																i.putExtra("HospitalName",  mapDetail.get("HospitalName").toString());
																i.putExtra("DoctorName", mapDetail.get("DoctorName").toString());
																i.putExtra("Gotime", time+mapDetail
																		.get("BeginTime").toString()+"-"+mapDetail
																		.get("EndTime").toString());
																i.putExtra("HospitalID", mapDetail.get("HospitalID").toString());
																i.putExtra("ID", ID);
																i.putExtra("MomSay", MomSay);
																startActivity(i);
															}
															else{
																String ID = mapDetail.get("ID").toString();
																Intent i = new Intent(MyCHK_Timeline.this, MyCHK_OrderDetail.class);
																i.putExtra("ID", ID);
																startActivity(i);
																
															}
													}else{

													String ID = mapDetail.get("ID").toString();
													Intent i = new Intent(MyCHK_Timeline.this, MyCHK_OrderDetail.class);
													i.putExtra("ID", ID);
													startActivity(i);
													}
												} catch (Exception e) {
													// TODO Auto-generated catch block
													e.printStackTrace();
												}
											}
										
										}
									});
								}

								
							
							
							}
							else
							{
								if("User_Order".equals(page)){
									listview.setVisibility(View.VISIBLE);
									llcheck.setVisibility(View.GONE);
									map = new HashMap<String, Object>();
									orderarrayList.add(map);
									tv_right.setVisibility(View.VISIBLE);
								MyNoOrderAdapter adapter = new MyNoOrderAdapter(MyCHK_Timeline.this, orderarrayList);
									listview.setAdapter(adapter);

									}else if("User_Foot".equals(page)){
										map = new HashMap<String, Object>();
										templist.add(map);
									listview.setVisibility(View.GONE);
									llcheck.setVisibility(View.VISIBLE);
									}
							}
						}
						catch (Exception ex)
						{
							ex.printStackTrace();
						}
					}
				}
			};
             
			JsonAsyncTask_Info task = new JsonAsyncTask_Info(
					MyCHK_Timeline.this, true, doProcess);
			Map<String, Object> paramMap = new HashMap<String, Object>();
			String str = "<Request><UserID>%s</UserID><UserMobile>%s</UserMobile><CHKID>%s</CHKID><ID>%s</ID><Flag>%s</Flag></Request>";
			str = String.format(str, new Object[]
			{String.valueOf(user.UserID),"","","",flag+""});
			paramMap.put("str", str);
            
			// 必须是这5个参数，而且得按照顺序
			task.execute(SOAP_NAMESPACE, SOAP_ACTION, SOAP_METHODNAME,
					SOAP_URL, paramMap);
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static Date stringToDate(String strTime, String formatType)
 			throws ParseException {
 		SimpleDateFormat formatter = new SimpleDateFormat(formatType);
 		Date date = null;
 		try {
			date = formatter.parse(strTime);
		} catch (java.text.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 		return date;
 	}

	@Override
	protected void onResume()
	{
		if(hasInternetConnected())
		{
			if("User_Order".equals(page)){
			searchCHKList(1);
			}else if("User_Foot".equals(page)){
				searchCHKList(0);
			}
		}
		else
		{
			Toast.makeText(MyCHK_Timeline.this, R.string.msgUninternet,
					Toast.LENGTH_SHORT).show();
		}
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
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		try {
			unregisterReceiver(mRefreshBroadcastReceiver);
			mRefreshBroadcastReceiver = null;
		} catch (Exception e) {
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.img_left:
				finish();
				break;
			case R.id.img_right:
				if(page.equals("User_Foot")){
					Intent i = new Intent(MyCHK_Timeline.this,MyCHK_OrderAdd.class);
					i.putExtra("page", "User_Foot");
					i.putExtra("Add_food", "Add");
					startActivity(i);
				}
				break;
			case R.id.tv_right:
				Intent i = new Intent(MyCHK_Timeline.this,
						MainTabActivity.class);
				i.putExtra("TabIndex", "A_TAB");
				startActivity(i);
				break;
		}
	}
}
