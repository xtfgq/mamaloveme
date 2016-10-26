package com.netlab.loveofmum.community;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ArrayAdapter;

import com.igexin.sdk.PushManager;
import com.netlab.loveofmum.BBSWebView;
import com.netlab.loveofmum.R;
import com.netlab.loveofmum.api.BaseActivity;
import com.netlab.loveofmum.api.JsonAsyncTaskOnComplete;
import com.netlab.loveofmum.api.JsonAsyncTask_Info;
import com.netlab.loveofmum.api.LocalAccessor;
import com.netlab.loveofmum.api.MMloveConstants;
import com.netlab.loveofmum.api.MyApplication;
import com.netlab.loveofmum.model.User;
import com.netlab.loveofmum.myadapter.CHK_Medical_Adapter;
import com.netlab.loveofmum.utils.SystemStatusManager;
import com.netlab.loveofmum.widget.CustomProgressDialog;
import com.netlab.loveofmum.widget.CustomerSpinner;
import com.netlab.loveofmum.widget.XScrollView.PullToRefreshLayout;
import com.netlab.loveofmum.widget.XScrollView.PullToRefreshLayout.OnRefreshListener;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.sharesdk.framework.ShareSDK;

//import cn.sharesdk.framework.ShareSDK;

public class CHK_medical_community extends BaseActivity
{

	private List<Map<String, Object>> arrayList = new ArrayList<Map<String, Object>>();
	private String returnvalue001;
	private ListView listview;

	private CHK_Medical_Adapter adapter;
	private CustomProgressDialog progressDialog = null;
	private int HospitalID=-1;
	private RelativeLayout loadmore;
	private TextView loadstate_tv;
	private User user;
	private long mExitTime;
	// 定义Web Service相关的字符串
	private final String SOAP_NAMESPACE = MMloveConstants.URL001;
	private final String SOAP_ACTION = MMloveConstants.URL001
			+ MMloveConstants.DoctorInquiry;
	private final String SOAP_METHODNAME = MMloveConstants.DoctorInquiry;
	private final String SOAP_URL = MMloveConstants.URL002;

//	private final String SOAP_ACTION2 = MMloveConstants.URL001
//			+ MMloveConstants.UserInquiry;
//	private final String SOAP_METHODNAME2 = MMloveConstants.UserInquiry;
	int page=1;
	private PullToRefreshLayout refreshLayout;
	private CustomerSpinner spinner;
	public static ArrayList<String> list = new ArrayList<String>();
	private List<Map<String, Object>> arrayList3= new ArrayList<Map<String, Object>>();
	private final String SOAP_ACTION4 = MMloveConstants.URL001
			+ MMloveConstants.HospitalInquiry;
	private final String SOAP_METHODNAME4 = MMloveConstants.HospitalInquiry;
	public ArrayAdapter<String> mHadapter;
	View nodoctor;
	private BroadcastReceiver mRefreshBroadcastReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if (intent.getAction().equals("action.community")) {

				HospitalID=-1;
				spinner.setSelection(0);
				adapter.setSelectItem(-1);
			searchCHK_Doctors();
			}
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setTranslucentStatus() ;
		setContentView(R.layout.layout_cmm);
		nodoctor = findViewById(R.id.nodoctor);
		MyApplication.getInstance().addActivity(this);
		iniView();
		 View vTop=findViewById(R.id.v_top);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
		    int h=getStatusHeight(this);
			LayoutParams params=vTop.getLayoutParams();
			params.height=h;
			params.width=LayoutParams.FILL_PARENT;
			vTop.setLayoutParams(params);
			}else{
				vTop.setVisibility(View.GONE);
			}
		if(hasInternetConnected())
		{
			setListeners();
			user = LocalAccessor.getInstance(CHK_medical_community.this).getUser();
			searchHospital();
		}
		else
		{
			Toast.makeText(CHK_medical_community.this, R.string.msgUninternet,
					Toast.LENGTH_SHORT).show();
		}
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("action.community");
		registerReceiver(mRefreshBroadcastReceiver, intentFilter);
		MyApplication.getInstance().addActivity(this);
	}
	public static int getStatusHeight(Context context) {
		 
	    int statusHeight = -1;
	    try {
	        Class<?> clazz = Class.forName("com.android.internal.R$dimen");
	        Object object = clazz.newInstance();
	        int height = Integer.parseInt(clazz.getField("status_bar_height")
	                .get(object).toString());
	        statusHeight = context.getResources().getDimensionPixelSize(height);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return statusHeight;
	}
	private void startProgressDialog(){
		if (progressDialog == null){
			progressDialog = CustomProgressDialog.createDialog(this);
			progressDialog.setMessage("正在加载");
		}

		progressDialog.show();
	}

	private void stopProgressDialog(){
		if (progressDialog != null){
			progressDialog.dismiss();
			progressDialog = null;
		}
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if(hasInternetConnected()){
		}
		else
		{
			Toast.makeText(CHK_medical_community.this, R.string.msgUninternet,
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
		tintManager.setStatusBarTintResource(R.color.home);//状态栏无背景
	}
	

	@Override
	protected void iniView()
	{
		// TODO Auto-generated method stub
		findViewById(R.id.img_left).setVisibility(View.GONE);
		((TextView) findViewById(R.id.txtHead)).setText("私人医生");
		listview = (ListView) findViewById(R.id.listview_chk_doctorlist);
		adapter = new CHK_Medical_Adapter(
			CHK_medical_community.this);
		listview.setAdapter(adapter);
		refreshLayout=(PullToRefreshLayout)findViewById(R.id.refresh_view);
		refreshLayout.setOnRefreshListener(new MyListener());
		loadmore = (RelativeLayout) findViewById(R.id.loadmore);
		loadstate_tv = (TextView) loadmore.findViewById(R.id.loadstate_tv);
		spinner=(CustomerSpinner)findViewById(R.id.spinner);
	}
	/*
	 * 查询医院信息
	 */
	private void searchHospital()
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
						Map<String, Object> map1;
						// 下边是具体的处理
						try
						{
							JSONObject mySO = (JSONObject) result;
							JSONArray array = mySO
									.getJSONArray("HospitalInquiry");
							list.clear();
							list.add("全部医院");
							int isDefault=-1;
							map1 = new HashMap<String, Object>();
							map1.put("HospitalName", "全部医院");
							map1.put("HospitalID", "-1");
							arrayList3.add(map1);
							for (int i = 0; i < array.length(); i++)
							{
								map = new HashMap<String, Object>();
								map.put("HospitalName", array.getJSONObject(i)
										.getString("HospitalName"));
								map.put("HospitalID", array.getJSONObject(i)
										.getString("HospitalID"));
								arrayList3.add(map);
								list.add(array.getJSONObject(i)
										.getString("HospitalName"));

							}


							spinner.setList(list);
							mHadapter=new ArrayAdapter<String>(CHK_medical_community.this, R.layout.simple_textview, list);
//							adapter = new ArrayAdapter<String>(CHK_medical_community.this, android.R.layout.simple_spinner_item, list);
							spinner.setAdapter(mHadapter);
							if(isDefault!=-1) {
								spinner.setPosition(isDefault);
								spinner.setSelection(isDefault,true);
							}else{
								spinner.setText("请选择");
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
					CHK_medical_community.this, true, doProcess);
			Map<String, Object> paramMap = new HashMap<String, Object>();
			String str = "<Request><HospitalID>0</HospitalID></Request>";

			paramMap.put("str", str);

			// 必须是这5个参数，而且得按照顺序
			task.execute(SOAP_NAMESPACE, SOAP_ACTION4, SOAP_METHODNAME4,
					SOAP_URL, paramMap);
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void setListeners()
	{

		spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
		{
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
									   int position, long id)
			{
				// TODO Auto-generated method stub
				Map<String, Object> mapDetail = new HashMap<String, Object>();
				mapDetail = arrayList3.get(position);
				int hosID = Integer.valueOf(mapDetail.get("HospitalID").toString());

				if(hosID == HospitalID)
				{

				}
				else
				{
					user = LocalAccessor.getInstance(CHK_medical_community.this).getUser();
					user.HospitalID = hosID;
					user.HospitalName = mapDetail.get("HospitalName").toString();
					HospitalID = hosID;

					try
					{
						LocalAccessor.getInstance(CHK_medical_community.this).updateUser(user);
						page=1;
						searchCHK_Doctors();
					}
					catch (Exception e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}


			}

			@Override
			public void onNothingSelected(AdapterView<?> parent)
			{
				// TODO Auto-generated method stub

			}
		});

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
							if(page==1&&arrayList.size()>0){
								arrayList.clear();
							}

							JSONObject mySO = (JSONObject) result;
							JSONArray array = mySO
									.getJSONArray("DoctorInquiry");
							stopProgressDialog();
							if(array.getJSONObject(0).has("MessageCode")){
								if (page == 1) {
									loadmore.setVisibility(View.GONE);
									adapter.setListData(arrayList);
									adapter.notifyDataSetChanged();
								} else {
									loadmore.setVisibility(View.VISIBLE);
									loadstate_tv.setText("没有更多数据");

									listview.setSelection(listview.getBottom());
								}
								new Handler() {
									@Override
									public void handleMessage(Message msg) {
										// 千万别忘了告诉控件加载完毕了哦！
										refreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
									}
								}.sendEmptyMessageDelayed(0, 500);

							}else {

								for (int i = 0; i < array.length(); i++) {

									map = new HashMap<String, Object>();
									map.put("DoctorID", array.getJSONObject(i)
											.getString("DoctorID"));

									map.put("DoctorNO", array.getJSONObject(i)
											.getString("DoctorNO"));

									map.put("DepartNO", array.getJSONObject(i)
											.getString("DepartNO"));
									map.put("IsLine", array.getJSONObject(i)
											.getString("IsLine"));
									map.put("AskCount", array.getJSONObject(i)
											.getString("AskCount"));
									map.put("UserCount", array.getJSONObject(i)
											.getString("UserCount"));
									map.put("DoctorName", array.getJSONObject(i)
											.getString("DoctorName"));
									map.put("HospitalID", array.getJSONObject(i)
											.getString("HospitalID"));
									map.put("HospitalName", array.getJSONObject(i)
											.getString("HospitalName"));
									map.put("AskPriceByOne", array.getJSONObject(i)
											.getString("AskPriceByOne"));

									map.put("Title", array.getJSONObject(i)
											.getString("Title"));
									map.put("Description", array.getJSONObject(i)
											.getString("Description"));
									map.put("Price", array.getJSONObject(i)
											.getString("Price"));
									map.put("IsFavored", array.getJSONObject(i)
											.getString("IsFavored"));


									map.put("ImageURL", array.getJSONObject(i)
											.getString("ImageURL"));

									arrayList.add(map);

								}


								adapter.setListData(arrayList);
								adapter.notifyDataSetChanged();

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

									public void onItemClick(AdapterView<?> arg0,
															View arg1, int arg2, long arg3) {
										// 点击的时候设置选中的编号，在自定义adapter中设置属性selectItem
										adapter.setSelectItem(arg2);
										adapter.notifyDataSetChanged();
										// 刷新listView


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
					if(arrayList.size()==0){
						nodoctor.setVisibility(View.VISIBLE);
					}else{
						nodoctor.setVisibility(View.GONE);
					}
				}

			};

			JsonAsyncTask_Info task = new JsonAsyncTask_Info(
					CHK_medical_community.this, true, doProcess);
			Map<String, Object> paramMap = new HashMap<String, Object>();
			String str = "<Request><UserID>%s</UserID><DoctorID>%s</DoctorID><HospitalID>%s</HospitalID><IsPMD>%s</IsPMD><PageIndex>%s</PageIndex><PageSize>%s</PageSize></Request>";
           if(user.UserID!=0){
			   if(HospitalID!=-1) {
				   str = String.format(str, new Object[]
						   {user.UserID + "", "", HospitalID + "", "1", page + "", 10 + ""});
			   }else{
				   str = String.format(str, new Object[]
						   {user.UserID + "", "", "", "1", page + "", 10 + ""});
			   }
           }else{
			   if(HospitalID!=-1) {
				   str = String.format(str, new Object[]
						   {"", "",HospitalID+"","1" ,page+"",10+"" });
			   }else{
				   str = String.format(str, new Object[]
						   {"", "","","1" ,page+"",10+"" });
			   }

           }
			paramMap.put("str", str);

			// 必须是这5个参数，而且得按照顺序
			task.execute(SOAP_NAMESPACE, SOAP_ACTION, SOAP_METHODNAME,
					SOAP_URL, paramMap);
			startProgressDialog();
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {

			if ((System.currentTimeMillis() - mExitTime) > 2000) // System.currentTimeMillis()无论何时调用，肯定大于2000
			{
				Toast.makeText(getApplicationContext(), "再按一次退出程序",
						Toast.LENGTH_SHORT).show();
				mExitTime = System.currentTimeMillis();
			} else {
				// finish();
				// System.exit(0);
				stopService();
				 MobclickAgent.onKillProcess(this);
				ShareSDK.stopSDK(this);
				myApplication.exit();

				
			}

			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	public class MyListener implements OnRefreshListener
	{

		@Override
		public void onRefresh(final PullToRefreshLayout pullToRefreshLayout)
		{
			// 下拉刷新操作
			page=1;
			user = LocalAccessor.getInstance(CHK_medical_community.this).getUser();
			adapter.setSelectItem(-1);
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

	@Override
	public void onDestroy() {
		super.onDestroy();
		try {
			unregisterReceiver(mRefreshBroadcastReceiver);

			mRefreshBroadcastReceiver = null;
		} catch (Exception e) {
		}
	}


	

}
