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
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
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
import com.netlab.loveofmum.myadapter.CHKTimeDetail_ItemListAdapter;
import com.netlab.loveofmum.myadapter.CHKTimeInfo_Adapter;
import com.netlab.loveofmum.myadapter.CHK_DoctorList_Adapter;
import com.netlab.loveofmum.utils.IOUtils;
import com.netlab.loveofmum.utils.SystemStatusManager;
import com.netlab.loveofmum.widget.XScrollView.PullToRefreshLayout;


public class CHK_DoctorList extends BaseActivity
{

	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		mIntent=this.getIntent();
		if(mIntent!=null&&mIntent.getStringExtra("HostiptalID")!=null&&!("").equals(mIntent.getStringExtra("HostiptalID"))){
			HospitalID = Integer.valueOf(mIntent.getStringExtra("HostiptalID"));
			getWeek();
		}else{
		HospitalID = user.HospitalID;
		}
	}

	private TextView txtHead;
	private ImageView imgBack;
	private Order order;
	private RelativeLayout loadmore;
	private TextView loadstate_tv;

	private List<Map<String, Object>> arrayList = new ArrayList<Map<String, Object>>();

	private String returnvalue001;
	private ListView listview;

	private CHK_DoctorList_Adapter adapter;
	
	private int HospitalID;
	private Intent mIntent;

	// 定义Web Service相关的字符串
	private final String SOAP_NAMESPACE = MMloveConstants.URL001;
	private final String SOAP_ACTION = MMloveConstants.URL001
			+ MMloveConstants.DoctorInquiry;
	private final String SOAP_METHODNAME = MMloveConstants.DoctorInquiry;
	private final String SOAP_URL = MMloveConstants.URL002;
	// 得到孕周
	private final String SOAP_YUNWEEKMETHOD= MMloveConstants.CHKInquiryForWeek;
	private final String SOAP_YUNWEEKACTION = MMloveConstants.URL001
			+ MMloveConstants.CHKInquiryForWeek;
	private final String SOAP_ACTION3 = MMloveConstants.URL001
			+ MMloveConstants.CHKTypeItemsInquiry;
	private final String SOAP_METHODNAME3 = MMloveConstants.CHKTypeItemsInquiry;
	// 根据传过来的参数ID
	private String CHKTypeID;
	private String CHKTypeName2;

	private String CHKItemValue;
	private Double CHKFEE = 0.0;
	Yuchan yu;
	private User user;
	LocalAccessor local;
	int page=1;
	private PullToRefreshLayout refreshLayout;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setTranslucentStatus() ;
		setContentView(R.layout.layout_chk_doctorlist);
		local = new LocalAccessor(CHK_DoctorList.this,
				MMloveConstants.ORDERINFO);

		MyApplication.getInstance().addActivity(this);
		mIntent=this.getIntent();
		if(hasInternetConnected())
		{
			
			iniView();
			setListeners();
			// iniCHKInfo();
			searchCHK_Doctors();
		}
		else
		{
			Toast.makeText(CHK_DoctorList.this, R.string.msgUninternet,
					Toast.LENGTH_SHORT).show();
		}
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
		txtHead = (TextView) findViewById(R.id.txtHead);
		imgBack = (ImageView) findViewById(R.id.img_left);

		txtHead.setText("选择医生");

		listview = (ListView) findViewById(R.id.listview_chk_doctorlist);
		adapter = new CHK_DoctorList_Adapter(
				CHK_DoctorList.this);
		listview.setAdapter(adapter);
		refreshLayout=(PullToRefreshLayout)findViewById(R.id.refresh_view);
		refreshLayout.setOnRefreshListener(new MyListener());
		loadmore = (RelativeLayout) findViewById(R.id.loadmore);
		loadstate_tv = (TextView) loadmore.findViewById(R.id.loadstate_tv);
		
	user = LocalAccessor.getInstance(CHK_DoctorList.this).getUser();

		if(mIntent!=null&&mIntent.getStringExtra("HostiptalID")!=null&&!("").equals(mIntent.getStringExtra("HostiptalID"))){
			HospitalID = Integer.valueOf(mIntent.getStringExtra("HostiptalID"));
			order=local.getOrder();
			Integer chktypeid = new Integer(order.CHKTypeID);
			if(chktypeid==null||chktypeid==0){
				getWeek();
			}
			if(mIntent.getStringExtra("selectdoc")!=null&&("select").equals(mIntent.getStringExtra("selectdoc"))){
				getWeek();
			}

//Toast.makeText(CHK_DoctorList.this,"wwwww",1).show();
		}else{
		HospitalID = user.HospitalID;
		}
	}

	private void setListeners()
	{
		imgBack.setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				finish();
			}
		});
	}
private void getWeek(){
	JsonAsyncTaskOnComplete doProcess = new JsonAsyncTaskOnComplete(){

		@Override
		public void processJsonObject(Object result) {
			// TODO Auto-generated method stub
			String rs=result.toString();
//			Toast.makeText(CHK_DoctorList.this,"vvvwekkkkkk",1).show();
//			LocalAccessor local = new LocalAccessor(CHK_DoctorList.this,
//					MMloveConstants.ORDERINFO);

			Order order = new Order();

			JSONObject mySO = (JSONObject) result;
			JSONArray array;
			try {
				array = mySO
						.getJSONArray("CHKInquiryForWeek");
				CHKTypeID= array.getJSONObject(0)
						.getString("ID");
				
				int sweek = Integer.parseInt(array
						.getJSONObject(0)
						.getString("WeekStart"));
				int eweek = Integer.parseInt(array
						.getJSONObject(0).getString("WeekEnd"));

				if (eweek > sweek)
				{
					CHKTypeName2=
									"第("
									+ array.getJSONObject(0)
											.getString(
													"WeekStart")
									+ "-"
									+ array.getJSONObject(0)
											.getString(
													"WeekEnd")
									+ "周)产检";

				}
				else if (eweek == sweek)
				{
					
					CHKTypeName2=
									"第"
									+ array.getJSONObject(0)
											.getString(
													"WeekStart")
									+ "周产检";
				}
			
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			searchCHKTimeItems();
	
		}};
		JsonAsyncTask_Info task = new JsonAsyncTask_Info(this, true, doProcess);
		Map<String, Object> paramMap = new HashMap<String, Object>();
		yu = new Yuchan();
		Date date;
		DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
			
			try {
				String yubirth = user.YuBirthDate;
				date = fmt.parse(user.YuBirthDate);
				yu = IOUtils.WeekInfo(date);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		String str = "<Request><Week>%s</Week></Request>";
		if(yu.Week>39){
			str = String.format(str, new Object[] { "40", });
		}else{
		str = String.format(str, new Object[] { yu.Week+1+"", });
		}
		paramMap.put("str", str);
		task.execute(SOAP_NAMESPACE, SOAP_YUNWEEKACTION,
				SOAP_YUNWEEKMETHOD, SOAP_URL, paramMap);
	}
private void searchCHKTimeItems()
{
	try
	{
		JsonAsyncTaskOnComplete doProcess = new JsonAsyncTaskOnComplete()
		{
			public void processJsonObject(Object result)
			{

				CHKItemValue = result.toString();
			
				if (returnvalue001 == null)
				{

				}
				else
				{
					Map<String, Object> map;
					// 下边是具体的处理
					try
					{
						JSONObject mySO = (JSONObject) result;
						JSONArray array = mySO
								.getJSONArray("CHKTypeItemsInquiry");
						
						if(array.getJSONObject(0).has("MessageCode"))
						{
							if (array.getJSONObject(0).getString("MessageCode")
									.equals("0"))
							{
								
							}
						}
						else
						{
							for (int i = 0; i < array.length(); i++)
							{

							

								if ("可预约".equals(array.getJSONObject(i)
										.getString("Value")))
								{
									if(!array.getJSONObject(i).getString("ItemPrice").equals(""))
									{
										CHKFEE += Double.valueOf(array
												.getJSONObject(i).getString(
														"ItemPrice"));
									}
								}
							}
//							LocalAccessor local = new LocalAccessor(CHK_DoctorList.this,
//									MMloveConstants.ORDERINFO);
							order.CHKTypeID = Integer.valueOf(CHKTypeID);
							order.CHKItemValue = CHKItemValue;
							order.CHKFee = String.valueOf(CHKFEE);
							order.CHKTypeName = CHKTypeName2;
							try
							{
								local.updateOrder(order);
							}
							catch (Exception e)
							{
								// TODO Auto-generated catch block
								e.printStackTrace();
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
				CHK_DoctorList.this, true, doProcess);
		Map<String, Object> paramMap = new HashMap<String, Object>();
		String str = "<Request><CHKTypeID>%s</CHKTypeID><HospitalID>%s</HospitalID></Request>";

		str = String.format(str, new Object[]
		{ CHKTypeID, String.valueOf(HospitalID) });
		paramMap.put("str", str);

		// 必须是这5个参数，而且得按照顺序
		task.execute(SOAP_NAMESPACE, SOAP_ACTION3, SOAP_METHODNAME3,
				SOAP_URL, paramMap);
	}
	catch (Exception e)
	{
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}

	/*
	 * 查询gridview
	 */
	private void searchCHK_Doctors()
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
						// 下边是具体的处理
						try
						{
							JSONObject mySO = (JSONObject) result;
							JSONArray array = mySO
									.getJSONArray("DoctorInquiry");
							if(page==1&&arrayList.size()>0){
								arrayList.clear();
							}

							if(array.getJSONObject(0).has("MessageCode")){
								if (page == 1) {
									loadmore.setVisibility(View.GONE);
								} else {
									loadmore.setVisibility(View.VISIBLE);
									loadstate_tv.setText("没有更多数据");
									new Handler() {
										@Override
										public void handleMessage(Message msg) {
											// 千万别忘了告诉控件加载完毕了哦！
											refreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
										}
									}.sendEmptyMessageDelayed(0, 500);
									listview.setSelection(listview.getBottom());
								}

							}else {
								for (int i = 0; i < array.length(); i++) {

									map = new HashMap<String, Object>();
									map.put("DoctorID", array.getJSONObject(i)
											.getString("DoctorID"));

									map.put("DoctorNO", array.getJSONObject(i)
											.getString("DoctorNO"));

									map.put("DepartNO", array.getJSONObject(i)
											.getString("DepartNO"));

									map.put("DoctorName", array.getJSONObject(i)
											.getString("DoctorName"));
									map.put("HospitalID", array.getJSONObject(i)
											.getString("HospitalID"));
									map.put("HospitalName", array.getJSONObject(i)
											.getString("HospitalName"));
									map.put("Title", array.getJSONObject(i)
											.getString("Title"));
									map.put("Description", array.getJSONObject(i)
											.getString("Description"));
									map.put("Price", array.getJSONObject(i)
											.getString("Price"));

									map.put("ImageURL", array.getJSONObject(i)
											.getString("ImageURL"));
									arrayList.add(map);

								}

								// SimpleAdapter adapter = new SimpleAdapter(
								// CHKTimeInfo.this, arrayList,
								// R.layout.item_chktimeinfo,
								// new Object[] { "ItemName","ItemContent" },
								// new int[] {
								// R.id.chktime_name,R.id.chktime_description });
								// listview.setAdapter(adapter);
								// listview.setEnabled(true);
								//

								adapter.setListData(arrayList);
								adapter.notifyDataSetChanged();
								// 添加listView点击事件
								if (page > 1) {
									new Handler() {
										@Override
										public void handleMessage(Message msg) {
											// 千万别忘了告诉控件加载完毕了哦！
											refreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
										}
									}.sendEmptyMessageDelayed(0, 500);
									listview.setSelection(listview.getBottom());
								} else {
									new Handler() {
										@Override
										public void handleMessage(Message msg) {
											// 千万别忘了告诉控件刷新完毕了哦！
											refreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
										}
									}.sendEmptyMessageDelayed(0, 500);

								}
								listview.setOnItemClickListener(new OnItemClickListener() {

									public void onItemClick(AdapterView<?> parent,
															View view, int postion, long id) {
										// 点击的时候设置选中的编号，在自定义adapter中设置属性selectItem
										adapter.setSelectItem(postion);
										// 刷新listView
										adapter.notifyDataSetChanged();
//									Intent i = new Intent(CHK_DoctorList.this,
//											CHKItem_Base.class);
//									i.putExtra("ItemName","简介");
//									i.putExtra("ItemContent",arrayList.get(postion).get("Description").toString());
//									startActivity(i); 


									}
								});
							}

						}
						catch (Exception ex)
						{

							ex.printStackTrace();
							if(page>1){
								new Handler()
								{
									@Override
									public void handleMessage(Message msg)
									{
										// 千万别忘了告诉控件加载完毕了哦！

										refreshLayout.loadmoreFinish(PullToRefreshLayout.FAIL);
									}
								}.sendEmptyMessageDelayed(0, 500);
							}else {
								new Handler()
								{
									@Override
									public void handleMessage(Message msg)
									{
										// 千万别忘了告诉控件刷新完毕了哦！
										refreshLayout.refreshFinish(PullToRefreshLayout.FAIL);
									}
								}.sendEmptyMessageDelayed(0, 500);

							}
						}
					}
				}
			};

			JsonAsyncTask_Info task = new JsonAsyncTask_Info(
					CHK_DoctorList.this, true, doProcess);
			Map<String, Object> paramMap = new HashMap<String, Object>();
			String str = "<Request><DoctorID>%s</DoctorID><HospitalID>%s</HospitalID><Enabled>%s</Enabled><PageIndex>%s</PageIndex><PageSize>%s</PageSize></Request>";

			str = String.format(str, new Object[]
			{ "",String.valueOf(HospitalID),0+"",page+"",10+""  });
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
	public class MyListener implements PullToRefreshLayout.OnRefreshListener
	{

		@Override
		public void onRefresh(final PullToRefreshLayout pullToRefreshLayout)
		{
			// 下拉刷新操作
			page=1;
			searchCHK_Doctors();

		}

		@Override
		public void onLoadMore(final PullToRefreshLayout pullToRefreshLayout)
		{
			// 加载操作
			page++;
			searchCHK_Doctors();

		}

	}
	
}
