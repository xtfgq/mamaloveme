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
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
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
import com.netlab.loveofmum.myadapter.News_Type_Adapter;
import com.netlab.loveofmum.testwebview.TestWebView;
import com.netlab.loveofmum.utils.IOUtils;
import com.netlab.loveofmum.utils.SystemStatusManager;
import com.umeng.analytics.MobclickAgent;

public class News_List extends BaseActivity
{

	private TextView txtHead;
	private ImageView imgBack;
	
	private ImageView btnLeft;
	private ImageView btnRight;

	private List<Map<String, Object>> arrayList;
	private String returnvalue001;
	private ListView listview;

	private int Week;
	private TextView tvtisps;
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
		setTranslucentStatus() ;
		setContentView(R.layout.layout_newslist);
		MyApplication.getInstance().addActivity(this);
		iniView();
		if(hasInternetConnected())
		{
			iniCHKInfo();
			if(this.getIntent().getExtras()!=null&&this.getIntent().getExtras().getString("Week").toString()!=null)
			{
				Week = Integer.valueOf(this.getIntent().getExtras().getString("Week").toString());
				txtHead.setText(Week+"周");
			}
			setBtn();
			setListeners();
			
			// iniCHKInfo();
			searchNewsList();
		}
		else
		{
			Toast.makeText(News_List.this, R.string.msgUninternet,
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
		txtHead = (TextView) findViewById(R.id.txtHead);
		imgBack = (ImageView) findViewById(R.id.img_left);
//		txtHead.setText("孕期知识");
		listview = (ListView) findViewById(R.id.listview_newslist);
		
		btnLeft = (ImageView)findViewById(R.id.btn_left);
		btnRight = (ImageView)findViewById(R.id.btn_right);
		tvtisps=(TextView) findViewById(R.id.txt_tips);
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
	
	private void iniCHKInfo()
	{
		User user = LocalAccessor.getInstance(News_List.this).getUser();
		DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
		Date date;
		try
		{
			date = fmt.parse(user.YuBirthDate);
			Yuchan yu = new Yuchan();
			yu = IOUtils.WeekInfo(date);
			
			if(yu.Week>=40)
			{
				Week = 40;
			}
			else
			{
				Week = yu.Week+1;
			}
			txtHead.setText(Week+"周");
		}
		catch (ParseException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void setBtn()
	{
		btnLeft.setVisibility(View.VISIBLE);
		btnRight.setVisibility(View.VISIBLE);
		if(Week == 1)
		{
			btnLeft.setVisibility(View.GONE);
		}
		else if(Week == 40)
		{
			btnRight.setVisibility(View.GONE);
		}
	}

	private void setListeners()
	{
		imgBack.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		btnLeft.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				if(Week>1)
				{
					Week = Week-1;
					
				}
				else
				{
					
				}
				
				txtHead.setText(Week+"周");
				setBtn();
				searchNewsList();
			}
		});
		
		btnRight.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				if(Week<40)
				{
					Week = Week+1;
				}
				else
				{
					
				}
				
				txtHead.setText(Week+"周");
				setBtn();
				searchNewsList();
			}
		});
		tvtisps.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(News_List.this,TestWebView.class);
				intent.putExtra("YunWeek", Week+"");
			   startActivity(intent);

			}
		});
	}

	/*
	 * 查询gridview
	 */
	private void searchNewsList()
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
//								TextView emptyView = new TextView(News_List.this);  
//		                        emptyView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));  
//		                        emptyView.setText("还没有发送过短信，看不到短信记录！");  
//		                        emptyView.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);
//		                        emptyView.setVisibility(View.GONE);  
//		                        ((ViewGroup)listview.getParent()).addView(emptyView);  
//		                        listview.setEmptyView(emptyView);
								listview.setAdapter(null);
							}
							else
							{
								for (int i = 0; i < array.length(); i++)
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
										News_List.this, arrayList,
										R.layout.item_newslist, new String[]
										{ "Title","Zhaiyao" }, new int[]
										{ R.id.txt001_item_newslist,R.id.txt002_item_newslist });
								
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
									Intent i = new Intent(News_List.this, News_Detail.class);
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
					News_List.this, true, doProcess);
			Map<String, Object> paramMap = new HashMap<String, Object>();
			String str = "<Request><Top>%s</Top><Week>%s</Week><Type>%s</Type><PageIndex>%s</PageIndex><PageSize>%s</PageSize></Request>";
			str = String.format(str, new Object[]
			{ "20", String.valueOf(Week), "99999", "1", "20" });
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

