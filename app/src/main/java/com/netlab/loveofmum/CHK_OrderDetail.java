package com.netlab.loveofmum;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.opengl.Visibility;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.netlab.loveofmum.api.BaseActivity;
import com.netlab.loveofmum.api.MMloveConstants;
import com.netlab.loveofmum.api.JsonAsyncTaskOnComplete;
import com.netlab.loveofmum.api.JsonAsyncTask_Info;
import com.netlab.loveofmum.api.LocalAccessor;
import com.netlab.loveofmum.api.MyApplication;
import com.netlab.loveofmum.model.Order;
import com.netlab.loveofmum.model.User;
import com.netlab.loveofmum.model.Yuchan;
import com.netlab.loveofmum.utils.IOUtils;
import com.netlab.loveofmum.utils.SystemStatusManager;
import com.netlab.loveofmum.widget.ListViewForScrollView;
import com.umeng.analytics.MobclickAgent;



public class CHK_OrderDetail extends BaseActivity
{

	private TextView txtHead;
	private ImageView imgBack;

	private List<Map<String, Object>> arrayList = new ArrayList<Map<String, Object>>();
	private String returnvalue001;
	private ListViewForScrollView listview;
	
	private TextView txt001;
	private TextView txt002;
	private TextView txt003;
	private TextView txt004;
	
	private TextView txt005;
	private TextView txt006;
	
	private TableRow tab001;
	
	private TextView txt007;
	
	private LinearLayout linear001;
	
	private String YunWeek;

	private User user;
	
	private Order order;
	
	private ScrollView sv;
	Yuchan yu;
	
	private final String SOAP_NAMESPACE = MMloveConstants.URL001;
	private final String SOAP_ACTION = MMloveConstants.URL001
			+ MMloveConstants.NewsInquiry;
	private final String SOAP_METHODNAME = MMloveConstants.NewsInquiry;
	private final String SOAP_URL = MMloveConstants.URL002;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setTranslucentStatus();
		setContentView(R.layout.layout_order_details);
		MyApplication.getInstance().addActivity(this);

		iniView();
		setListeners();
		if(hasInternetConnected())
		{
			iniYunWeek();
			InitData();
			InitNewsList();
		}
		else
		{
			Toast.makeText(CHK_OrderDetail.this, R.string.msgUninternet,
					Toast.LENGTH_SHORT).show();
		}
	}
	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	
	}
	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
		
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
		imgBack = (ImageView) findViewById(R.id.img_left);

		imgBack.setImageResource(R.drawable.icon_home2);
		
		txtHead.setText("订单详情");
		
		linear001 = (LinearLayout)findViewById(R.id.linear001_order_details);

		listview = (ListViewForScrollView) findViewById(R.id.listview_newslist);
		
		//tab001 = (TableRow)findViewById(R.id.tab001_order_detail);
		
		LocalAccessor local = new LocalAccessor(CHK_OrderDetail.this,
				MMloveConstants.ORDERINFO);
		order = local.getOrder();
		
		user = LocalAccessor.getInstance(CHK_OrderDetail.this).getUser();
		
		//txt001 = (TextView)findViewById(R.id.txt003_orderdetails);
		txt002 = (TextView)findViewById(R.id.txt004_orderdetails);
		txt003 = (TextView)findViewById(R.id.txt005_orderdetails);
		txt004 = (TextView)findViewById(R.id.txt006_orderdetails);
		
		txt005 = (TextView)findViewById(R.id.txt0001_orderdetails);
		
		txt006 = (TextView)findViewById(R.id.txt0002_orderdetails);
		
		txt007 = (TextView)findViewById(R.id.txt0003_order_details);
		txt007.setText("热门资讯");
		
		sv = (ScrollView) findViewById(R.id.sv_order_detail);
		sv.smoothScrollTo(0, 0);
	}

	private void setListeners()
	{
		imgBack.setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				
				Intent i = new Intent(CHK_OrderDetail.this, MainTabActivity.class);
				i.putExtra("TabIndex", "A_TAB");

				startActivity(i);
				finish();
			}
		});
	}

	/*
	 * 查询gridview
	 */
	private void InitData()
	{
		
			//txt001.setText(order.CHKTypeName);
			txt002.setText(order.HospitalName);
			txt003.setText(order.DoctorName);
			txt004.setText(order.DoctorTime+" "+order.TimeStart+"-"+order.TimeEnd);
			txt006.setText(order.DoctorTime);
			//txt005.setText(order.GuaFee);
			
//			returnvalue001 = order.CHKItemValue.toString();
//			if (returnvalue001 == "")
//			{
//				linear001.setVisibility(View.GONE);
//			}
//			else
//			{
//				linear001.setVisibility(View.VISIBLE);
//				Map<String, Object> map;
//				// 下边是具体的处理
//				JSONObject mySO = new JSONObject(returnvalue001);
//				JSONArray array = mySO.getJSONArray("CHKTypeItemsInquiry");
//				
//				String items = "";
//				for (int i = 0; i < array.length(); i++)
//				{
//					if(array.getJSONObject(i)
//							.getString("Value").equals("可预约"))
//					{
//						map = new HashMap<String, Object>();
//						map.put("ID",
//								array.getJSONObject(i).getString("ID"));
//	
//						map.put("ItemName", array.getJSONObject(i)
//								.getString("ItemName"));
//						map.put("ItemPrice", array.getJSONObject(i)
//								.getString("ItemPrice"));
//						
//						map.put("ItemShow", "以医院为准");
//						map.put("ItemStatus", array.getJSONObject(i)
//								.getString("Value"));
//						arrayList.add(map);
//						
//						items = array.getJSONObject(i)
//								.getString("ItemName")+";"+items;
//					}
//				}
				
			if(this.getIntent().getStringExtra("Order")!=null&&!"".equals(this.getIntent().getStringExtra("Order").toString().trim())){
				txt005.setText(this.getIntent().getStringExtra("Order").toString());
			}else{
				linear001.setVisibility(View.GONE);
			}
//				SimpleAdapter adapter = new SimpleAdapter(
//						CHK_OrderDetail.this, arrayList,
//						R.layout.item_orderdetails, new String[]
//						{ "ItemName","ItemPrice" }, new int[]
//						{ R.id.txt001_item_orderdetail,R.id.txt002_item_orderdetail });
//				SimpleAdapter adapter = new SimpleAdapter(
//						CHK_OrderDetail.this, arrayList,
//						R.layout.item_orderdetails, new String[]
//						{ "ItemName","ItemShow" }, new int[]
//						{ R.id.txt001_item_orderdetail,R.id.txt002_item_orderdetail});
//
//				listview.setAdapter(adapter);

//			}
//		}
//		catch (Exception e)
//		{
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
	
    @Override  
    public boolean onKeyDown(int keyCode, KeyEvent event) {   
        //在欢迎界面屏蔽BACK键   
        if(keyCode==KeyEvent.KEYCODE_BACK)
        {   
        	Intent i = new Intent(CHK_OrderDetail.this, MainTabActivity.class);
			i.putExtra("TabIndex", "A_TAB");

			startActivity(i);
        	finish();
        	return true;
        }   
        return super.onKeyDown(keyCode, event);  
    } 
    
    
    /*
	 * 查询gridview
	 */
	private void InitNewsList()
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
						// 下边是具体的处理
						try
						{
							JSONObject mySO = (JSONObject) result;
							JSONArray array = mySO
									.getJSONArray("NewsInquiry");
							
							if(array.getJSONObject(0).has("MessageCode"))
							{
								listview.setAdapter(null);
							}
							else
							{
								for (int i = 0; i <2; i++)
								{

									map = new HashMap<String, Object>();

									map.put("ID",
											array.getJSONObject(i).getString("ID"));
									map.put("Title", array.getJSONObject(i)
											.getString("Title"));
									map.put("Zhaiyao", array.getJSONObject(i)
											.getString("Zhaiyao"));
									arrayList.add(map);

								}
								
								SimpleAdapter adapter = new SimpleAdapter(
										CHK_OrderDetail.this, arrayList,
										R.layout.item_newslist2, new String[]
										{ "Title"}, new int[]
										{ R.id.txt001_item_newslist });
								
								listview.setAdapter(adapter);
								// 添加listView点击事件
								listview.setEnabled(true);
							}	
						    
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
									String newsID = mapDetail.get("ID").toString();
									Intent i = new Intent(CHK_OrderDetail.this, News_Detail.class);
									i.putExtra("ID", newsID);
									startActivity(i);
								}
							});

						}
						catch (Exception ex)
						{
							ex.printStackTrace();
						}
					}
				}
			};

			JsonAsyncTask_Info task = new JsonAsyncTask_Info(
					CHK_OrderDetail.this, true, doProcess);
			Map<String, Object> paramMap = new HashMap<String, Object>();
			String str = "<Request><Top>%s</Top><Week>%s</Week><Type>%s</Type><PageIndex>%s</PageIndex><PageSize>%s</PageSize></Request>";
			if(yu.Week>39){
				str = String.format(str, new Object[]
						{"20", 40+"", "99999", "1", "20"});

			}else {
				str = String.format(str, new Object[]
						{"20", String.valueOf(YunWeek), "99999", "1", "20"});
			}
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
	
	private void iniYunWeek()
	{
		DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
		DateFormat fmt2 = new SimpleDateFormat("MM月dd日");
		Date date;
		try
		{
			date = fmt.parse(user.YuBirthDate);

			yu = IOUtils.WeekInfo(date);
			YunWeek = String.valueOf(yu.Week+1);
			//txt007.setText(YunWeek+"周提醒");
		}
		catch (ParseException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
}
