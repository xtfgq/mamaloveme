package com.netlab.loveofmum;

import java.text.DateFormat;
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
import com.netlab.loveofmum.api.MyApplication;
import com.netlab.loveofmum.model.Yuchan;
import com.netlab.loveofmum.myadapter.CHKTimeDetail_TopicAdapter;
import com.netlab.loveofmum.myadapter.CHK_DoctorList_Adapter;
import com.netlab.loveofmum.myadapter.CHK_PostList_Adapter;
import com.netlab.loveofmum.utils.IOUtils;
import com.netlab.loveofmum.utils.SystemStatusManager;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.umeng.analytics.MobclickAgent;

public class PostList extends BaseActivity
{

	private TextView txtHead;
	private ImageView imgBack;

	private List<Map<String, Object>> arrayList = new ArrayList<Map<String, Object>>();
	private String returnvalue001;
	private String TopicID;
	private String LeftID;
	
	private String TopicType;
	private ListView listview;

	private SimpleAdapter adapter;

	// 定义Web Service相关的字符串
	private final String SOAP_NAMESPACE = MMloveConstants.URL001;
	private final String SOAP_ACTION = MMloveConstants.URL001
			+ MMloveConstants.NewsTopicInquiry;
	private final String SOAP_METHODNAME = MMloveConstants.NewsTopicInquiry;
	private final String SOAP_URL = MMloveConstants.URL002;
	
	//private ImageLoader imageLoader;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setTranslucentStatus() ;
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.layout_postlist);
		MyApplication.getInstance().addActivity(this);
		iniView();
		setListeners();
		// iniCHKInfo();
		if(hasInternetConnected())
		{
			//imageLoader = ImageLoader.getInstance();
			//imageLoader.init(ImageLoaderConfiguration.createDefault(PostList.this));
			searchPostList();
		}
		else
		{
			Toast.makeText(PostList.this, R.string.msgUninternet,
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

		txtHead.setText("回复列表");

		TopicID = this.getIntent().getExtras().getString("TopicID").toString();
		LeftID = this.getIntent().getExtras().getString("LeftID").toString();
		listview = (ListView) findViewById(R.id.listview_postlist);
		TopicType = this.getIntent().getExtras().getString("TopicType").toString();
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
	private void searchPostList()
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
									.getJSONArray("NewsTopicInquiry");
							for (int i = 0; i < array.length(); i++)
							{

								map = new HashMap<String, Object>();
								map.put("ID",
										array.getJSONObject(i).getString("ID"));

								map.put("TopicTitle", array.getJSONObject(i)
										.getString("TopicTitle"));
								map.put("ParentID", array.getJSONObject(i)
										.getString("ParentID"));
								map.put("GoodCount", array.getJSONObject(i)
										.getString("GoodCount"));
								map.put("UserID", array.getJSONObject(i)
										.getString("UserID"));
								map.put("NickName", array.getJSONObject(i)
										.getString("NickName"));
								map.put("TopicType", array.getJSONObject(i)
										.getString("TopicType"));
								map.put("PictureURL", array.getJSONObject(i)
										.getString("PictureURL"));
								map.put("CreatedDate", array.getJSONObject(i)
										.getString("CreatedDate"));
								
								String CreatedDate = array.getJSONObject(i)
										.getString("CreatedDate");
								CreatedDate = CreatedDate.split(" ")[0]
										.replaceAll("/", "-");
								String YuBirthTime = array.getJSONObject(i)
										.getString("YuBirthTime");
								YuBirthTime = YuBirthTime.split(" ")[0]
										.replaceAll("/", "-");
								
								
								DateFormat fmt = new SimpleDateFormat(
										"yyyy-MM-dd");
								Date date;
								
								Date date2;
								date = fmt.parse(YuBirthTime);
								
								date2 = fmt.parse(CreatedDate);
								Yuchan yu = new Yuchan();
							
								yu = IOUtils.WeekInfo2(date,date2);
								int weekitem=Integer.valueOf(yu.Week+"");
								if(weekitem<0){
									date = fmt.parse(fmt.format((new Date()).getTime()));
									yu = IOUtils.WeekInfo2(date,date2);
								}
								if( yu.Week>=40){
									map.put("YuBirthTime", "怀孕40周");
								}else{
								map.put("YuBirthTime", "怀孕" + yu.Week + "周");
								}
							
								map.put("PostsCount", array.getJSONObject(i)
										.getString("PostsCount"));
								arrayList.add(map);
							}
//							adapter = new SimpleAdapter(
//									PostList.this,
//									arrayList,
//									R.layout.item_postlist,
//									new Object[]
//									{ "NickName", "YuBirthTime", "TopicTitle","CreatedDate" },
//									new int[]
//									{ R.id.item_postlist_txt001,
//											R.id.item_postlist_txt002,
//											R.id.item_postlist_txt003,R.id.item_chkdetail_list_txt007 });

							CHK_PostList_Adapter adapter = new CHK_PostList_Adapter(
									PostList.this, arrayList);	
							listview.setAdapter(adapter);
							listview.setEnabled(true);
							// 添加listView点击事件

						}
						catch (Exception ex)
						{
							ex.printStackTrace();
						}
					}
				}
			};

			JsonAsyncTask_Info task = new JsonAsyncTask_Info(PostList.this,
					true, doProcess);
			Map<String, Object> paramMap = new HashMap<String, Object>();
			String str = "<Request><TopicType>%s</TopicType><LeftID>%s</LeftID><ParentID>%s</ParentID><PageIndex>%s</PageIndex><PageSize>%s</PageSize></Request>";

			str = String.format(str, new Object[]
			{TopicType, LeftID, TopicID, "1", "100" });
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
	public void onDestroy()
	{
		// TODO Auto-generated method stub
		super.onDestroy();
	}
}
