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
import com.netlab.loveofmum.model.Yuchan;
import com.netlab.loveofmum.myadapter.CHKTimeDetail_TopicAdapter;
import com.netlab.loveofmum.myadapter.CHKTimeInfo_Adapter;
import com.netlab.loveofmum.utils.IOUtils;
import com.netlab.loveofmum.utils.SystemStatusManager;
import com.umeng.analytics.MobclickAgent;

public class CHKTimeInfo extends BaseActivity
{

	private TextView txtHead;
	private ImageView imgBack;

	private TextView txt001;

	private String CHKTypeID;

	private List<Map<String, Object>> arrayList = new ArrayList<Map<String, Object>>();
	private String returnvalue001;
	private ListView listview;

	private CHKTimeInfo_Adapter adapter;

	// 定义Web Service相关的字符串
	private final String SOAP_NAMESPACE = MMloveConstants.URL001;
	private final String SOAP_ACTION = MMloveConstants.URL001
			+ MMloveConstants.CHKTypeItemsInquiry;
	private final String SOAP_METHODNAME = MMloveConstants.CHKTypeItemsInquiry;
	private final String SOAP_URL = MMloveConstants.URL002;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setTranslucentStatus() ;
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.layout_chktimeinfo);
		if(hasInternetConnected())
		{
			setListeners();
			// iniCHKInfo();
			searchCHKTimeItems();
		}
		else
		{
			Toast.makeText(CHKTimeInfo.this, R.string.msgUninternet,
					Toast.LENGTH_SHORT).show();
		}
		MyApplication.getInstance().addActivity(this);
		iniView();

	}
	@Override
	protected void onResume()
	{
		// TODO Auto-generated method stub
		
		iniView();
		setListeners();
		super.onResume();

		MobclickAgent.onResume(this);
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
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
		tintManager.setStatusBarTintResource(R.drawable.bg_header);//状态栏无背景
	}

	@Override
	protected void iniView()
	{
		// TODO Auto-generated method stub
		txtHead = (TextView) findViewById(R.id.txtHead);
		imgBack = (ImageView) findViewById(R.id.img_left);

		txtHead.setText("第"+this.getIntent().getExtras().getString("CHKType")
				.toString()+"产检");

		txt001 = (TextView) findViewById(R.id.txt_chkdetail_001);

		txt001.setText("产检项目");
		listview = (ListView) findViewById(R.id.listview_chk);

		CHKTypeID = this.getIntent().getExtras().getString("ID").toString();
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

	/*
	 * 查询gridview
	 */
	private void searchCHKTimeItems()
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
									.getJSONArray("CHKTypeItemsInquiry");
							for (int i = 0; i < array.length(); i++)
							{

								map = new HashMap<String, Object>();
								map.put("ID",
										array.getJSONObject(i).getString("ID"));

								map.put("ItemName", array.getJSONObject(i)
										.getString("ItemName"));
								map.put("ItemPrice", array.getJSONObject(i)
										.getString("ItemPrice"));
								map.put("ItemContent", array.getJSONObject(i)
										.getString("ItemContent"));
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
							adapter = new CHKTimeInfo_Adapter(CHKTimeInfo.this,
									arrayList);
							listview.setAdapter(adapter);
							// 添加listView点击事件

							listview.setOnItemClickListener(new OnItemClickListener()
							{

								public void onItemClick(AdapterView<?> arg0,
										View arg1, int arg2, long arg3)
								{
									// 点击的时候设置选中的编号，在自定义adapter中设置属性selectItem
									adapter.setSelectItem(arg2);
									// 刷新listView
									adapter.notifyDataSetChanged();

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

			JsonAsyncTask_Info task = new JsonAsyncTask_Info(CHKTimeInfo.this,
					true, doProcess);
			Map<String, Object> paramMap = new HashMap<String, Object>();
			String str = "<Request><CHKTypeID>%s</CHKTypeID><HospitalID>1</HospitalID></Request>";

			str = String.format(str, new Object[]
			{ CHKTypeID });
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
}
