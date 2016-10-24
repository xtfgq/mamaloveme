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
import com.netlab.loveofmum.myadapter.CHK_DoctorList_Adapter;
import com.netlab.loveofmum.myadapter.News_Type_Adapter;
import com.netlab.loveofmum.utils.IOUtils;
import com.netlab.loveofmum.utils.SystemStatusManager;
import com.umeng.analytics.MobclickAgent;

public class News_Type extends BaseActivity
{

	private TextView txtHead;
	private ImageView imgBack;

	private List<Map<String, Object>> arrayList = new ArrayList<Map<String, Object>>();
	private String returnvalue001;
	private ListView listview;

	private News_Type_Adapter adapter;

	private int Week;
	// 定义Web Service相关的字符串
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
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setTranslucentStatus() ;
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.layout_newstype);
		MyApplication.getInstance().addActivity(this);
		if(hasInternetConnected())
		{
			iniView();
			setListeners();
			//iniCHKInfo();
			// iniCHKInfo();
			searchNewsType();
		}
		else
		{
			Toast.makeText(News_Type.this, R.string.msgUninternet,
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
	protected void iniView()
	{
		// TODO Auto-generated method stub
		txtHead = (TextView) findViewById(R.id.txtHead);
		imgBack = (ImageView) findViewById(R.id.img_left);
		txtHead.setText("孕期知识");
		listview = (ListView) findViewById(R.id.listview_newstype);

	}
	
	private void iniCHKInfo()
	{
		User user = LocalAccessor.getInstance(News_Type.this).getUser();
		DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
		Date date;
		try
		{
			date = fmt.parse(user.YuBirthDate);
			Yuchan yu = new Yuchan();
			yu = IOUtils.WeekInfo(date);
			Week = yu.Week;
			//txtHead.setText(yu.Week+"周");
		}
		catch (ParseException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
	private void searchNewsType()
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
									.getJSONArray("NewsInquiry");
							for (int i = 0; i < array.length(); i++)
							{

								map = new HashMap<String, Object>();
								map.put("TypeName", array.getJSONObject(i)
										.getString("Value"));

//								map.put("Code", array.getJSONObject(i)
//										.getString("Code"));
								map.put("Description", array.getJSONObject(i)
										.getString("Description"));
								arrayList.add(map);

							}

							//
							adapter = new News_Type_Adapter(
									News_Type.this, arrayList);
							
//							SimpleAdapter adapter = new SimpleAdapter(
//									News_Type.this, arrayList,
//									R.layout.item_newstype, new Object[]
//									{ "Value" }, new int[]
//									{ R.id.txt001_item_newstype });
							
							listview.setAdapter(adapter);
							// 添加listView点击事件
							listview.setEnabled(true);
							listview.setOnItemClickListener(new OnItemClickListener()
							{

								public void onItemClick(AdapterView<?> arg0,
										View arg1, int arg2, long arg3)
								{
									
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
					News_Type.this, true, doProcess);
			Map<String, Object> paramMap = new HashMap<String, Object>();
			String str = "<Request><Top>%s</Top><Week>%s</Week><Type>%s</Type><PageIndex>%s</PageIndex><PageSize>%s</PageSize></Request>";
			str = String.format(str, new Object[]
			{ "3", String.valueOf(Week), "0", "1", "20" });
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
