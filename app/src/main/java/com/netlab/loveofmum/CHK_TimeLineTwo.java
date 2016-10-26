package com.netlab.loveofmum;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
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
import com.netlab.loveofmum.utils.SystemStatusManager;


public class CHK_TimeLineTwo extends BaseActivity
{

	private TextView txtHead;
	private ImageView imgBack;
	
	private ImageView imgRight;

	private List<Map<String, Object>> arrayList;
	private String returnvalue001;
	private ListView listview;
	
	private ImageView imgAdd;
	
	private User user;
    private Intent mIntent;
    String page;

	private int Week;
	// 定义Web Service相关的字符串
	private final String SOAP_NAMESPACE = MMloveConstants.URL001;
	private final String SOAP_ACTION = MMloveConstants.URL001
			+ MMloveConstants.OrderInquiry;
	private final String SOAP_METHODNAME = MMloveConstants.OrderInquiry;
	private final String SOAP_URL = MMloveConstants.URL002;
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
		
			switch (msg.what) {
			case 0:
				MyCHK_Timeline_Adapter adapter = new MyCHK_Timeline_Adapter(CHK_TimeLineTwo.this, arrayList);
				listview.setAdapter(adapter);
			
				break;
		
		
			
			default: 
				break;
			}
			
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setTranslucentStatus() ;
		setContentView(R.layout.layout_chk_timeline);
		mIntent=this.getIntent();
		if(mIntent!=null){
			page= mIntent.getStringExtra("Page");
			
		}
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
		tintManager.setStatusBarTintResource(R.drawable.medical_quxiao);//状态栏无背景
	}

	@Override
	protected void iniView()
	{
		// TODO Auto-generated method stub
		txtHead = (TextView) findViewById(R.id.txtHead);
		imgBack = (ImageView) findViewById(R.id.img_left);
		
	
		if(page.equals("User_Foot")){
			txtHead.setText("妈妈足迹");
			
		}else if(page.equals("User_Order")){
			txtHead.setText("我的订单");
		}
		imgAdd = (ImageView)findViewById(R.id.img_right);
		imgAdd.setVisibility(View.VISIBLE);
		imgAdd.setImageResource(R.drawable.btn_pluswhite);
		listview = (ListView) findViewById(R.id.listview_timeline);
		
		user =LocalAccessor.getInstance(CHK_TimeLineTwo.this).getUser();
		
	}

	private void setListeners()
	{
		imgBack.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				Intent i = new Intent(CHK_TimeLineTwo.this,
						MainTabActivity.class);
				i.putExtra("TabIndex", "D_TAB");

				startActivity(i);
				finish();
			}
		});
		
		imgAdd.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
			
				Intent i = new Intent(CHK_TimeLineTwo.this,MyCHK_OrderAdd.class);
				startActivity(i);
		
			}
		});
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
					returnvalue001 = result.toString();
					if (returnvalue001 == null)
					{

					}
					else
					{
						Map<String, Object> map;
						arrayList = new ArrayList<Map<String, Object>>();
						List<Map<String,Object>> templist= new ArrayList<Map<String, Object>>();
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
								
							}
							else
							{
								length = array.length();
							}
							
							if(length>0)
							{
								for (int i = 0; i < array.length(); i++)
								{
									map = new HashMap<String, Object>();
	
									map.put("OrderID",
											array.getJSONObject(i).getString("OrderID"));
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
									map.put("MomSay", array.getJSONObject(i)
											.getString("MomSay"));
									map.put("BeginTime", array.getJSONObject(i)
											.getString("BeginTime"));
									map.put("EndTime", array.getJSONObject(i)
											.getString("EndTime"));
									map.put("TitleName", "第"+array.getJSONObject(i)
											.getString("YunWeek")+"周产检");
									map.put("Week", array.getJSONObject(i)
											.getString("YunWeek"));
									listWithOrder.add(Integer.valueOf(array.getJSONObject(i)
											.getString("YunWeek")));
									templist.add(map);
								}
								MyCHK_Timeline_Adapter adapter;
								if(page.equals("User_Foot")){
									listWithOrder=new ArrayList<Integer>(new HashSet<Integer>(listWithOrder));
			                          Collections.sort(listWithOrder, new Comparator<Integer>() { 
										@Override
										public int compare(Integer lhs, Integer rhs) {
											// TODO Auto-generated method stub
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
											adapter = new MyCHK_Timeline_Adapter(CHK_TimeLineTwo.this, arrayList);
											listview.setAdapter(adapter);
								}else if(page.equals("User_Order")){
									adapter = new MyCHK_Timeline_Adapter(CHK_TimeLineTwo.this, templist);
									listview.setAdapter(adapter);
								}
							
//								Message msg = new Message();
//								msg.what = 0;
//								handler.sendMessage(msg);

//								for(int i=0;i<)
								
//								if(flag==0){
//				
//								MyCHK_Timeline_Adapter adapter = new MyCHK_Timeline_Adapter(MyCHK_Timeline.this, arrayList);
//								
//								listview.setAdapter(adapter);
//								}else if(flag==1){
//									MyCHK_Timeline_Adapter adapter = new MyCHK_Timeline_Adapter(MyCHK_Timeline.this, arrayList);
//									
//									listview.setAdapter(adapter);
//								}
								
							
								listview.setOnItemClickListener(new OnItemClickListener()
								{
	
									public void onItemClick(AdapterView<?> arg0,
											View arg1, int arg2, long arg3)
									{
										Map<String, Object> mapDetail = new HashMap<String, Object>();
										if (arrayList.isEmpty())
										{
	
										}
										else
										{
											mapDetail = arrayList.get(arg2);
										}
										String ID = mapDetail.get("ID").toString();
										Intent i = new Intent(CHK_TimeLineTwo.this, MyCHK_OrderDetail.class);
										i.putExtra("ID", ID);
										startActivity(i);
									}
								});
							}
							else
							{
								map = new HashMap<String, Object>();
								arrayList.add(map);
								MyCHK_Timeline_Adapter2 adapter = new MyCHK_Timeline_Adapter2(CHK_TimeLineTwo.this, arrayList);
								listview.setAdapter(adapter);
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
					CHK_TimeLineTwo.this, true, doProcess);
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

	@Override
	protected void onResume()
	{
		// TODO Auto-generated method stub
		iniView();
		setListeners();
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
			Toast.makeText(CHK_TimeLineTwo.this, R.string.msgUninternet,
					Toast.LENGTH_SHORT).show();
		}
		super.onResume();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		// 在欢迎界面屏蔽BACK键
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			Intent i = new Intent(CHK_TimeLineTwo.this, MainTabActivity.class);
			i.putExtra("TabIndex", "D_TAB");

			startActivity(i);
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
