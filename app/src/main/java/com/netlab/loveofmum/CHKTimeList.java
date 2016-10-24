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
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
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
import com.netlab.loveofmum.myadapter.ChktimelistAdapter;
import com.netlab.loveofmum.utils.IOUtils;
import com.netlab.loveofmum.utils.SystemStatusManager;
import com.netlab.loveofmum.widget.CircularImage;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;

public class CHKTimeList extends BaseActivity
{

	private TextView txtHead,tvchao;
	private ImageView imgBack;
	
	private CircularImage imgphoto;

	private TextView txt001;
//	private TextView txt002;

	private List<Map<String, Object>> arrayList = new ArrayList<Map<String, Object>>();
	private String returnvalue001;
	private ListView listview;
	
	private User user;
	String hosId,hosName;
	
	//private ImageLoader imageLoader;

	// 定义Web Service相关的字符串
	private final String SOAP_NAMESPACE = MMloveConstants.URL001;
	private final String SOAP_ACTION = MMloveConstants.URL001
			+ MMloveConstants.CHKTimeInquiry;
	private final String SOAP_METHODNAME = MMloveConstants.CHKTimeInquiry;
	private final String SOAP_URL = MMloveConstants.URL002;
	
	private final String SOAP_ACTION2 = MMloveConstants.URL001
			+ MMloveConstants.UserInquiry;
	private final String SOAP_METHODNAME2 = MMloveConstants.UserInquiry;
	// 请求医院列表
	private final String SOAP_ACTION4 = MMloveConstants.URL001
			+ MMloveConstants.HospitalInquiry;
	private final String SOAP_METHODNAME4 = MMloveConstants.HospitalInquiry;
	private Boolean isHosiptal = false;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setTranslucentStatus() ;
		
		setContentView(R.layout.layout_chklist);
		MyApplication.getInstance().addActivity(this);
		iniView();
		setListeners();
		iniCHKInfo();
		if(this.getIntent()!=null&&this.getIntent().getStringExtra("HospitalID")!=null&&this.getIntent().getStringExtra("name")!=null){
			hosId=this.getIntent().getStringExtra("HospitalID");
			hosName=this.getIntent().getStringExtra("name");
			user.HospitalID=Integer.parseInt(hosId);
			user.HospitalName=hosName;
			try {
				LocalAccessor.getInstance(CHKTimeList.this).updateUser(user);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//imageLoader = ImageLoader.getInstance();
		//imageLoader.init(ImageLoaderConfiguration.createDefault(CHKTimeList.this));

		
	}
	@Override
	protected void onResume()
	{
		// TODO Auto-generated method stub
		
		iniView();
		searchHospital();
		setListeners();
		user = LocalAccessor.getInstance(CHKTimeList.this).getUser();
		if(hasInternetConnected())
		{
			searchCHKTime();

			UserInquiry();
		}
		else
		{
			Toast.makeText(CHKTimeList.this, R.string.msgUninternet,
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
	private void searchHospital() {

		try {
			user=LocalAccessor.getInstance(CHKTimeList.this).getUser();
			JsonAsyncTaskOnComplete doProcess = new JsonAsyncTaskOnComplete() {
				public void processJsonObject(Object result) {
					returnvalue001 = result.toString();
					if (returnvalue001 == null) {

					} else {
						Map<String, Object> map;

						try {
							JSONObject mySO = (JSONObject) result;
							JSONArray array = mySO
									.getJSONArray("HospitalInquiry");

							// list.clear();

							for (int i = 0; i < array.length(); i++) {
								map = new HashMap<String, Object>();
								map.put("HospitalName", array.getJSONObject(i)
										.getString("HospitalName"));
								map.put("SoundFlag", array.getJSONObject(i)
										.getString("SoundFlag"));

								map.put("HospitalID", array.getJSONObject(i)
										.getString("HospitalID"));

								map.put("picUrl",
										(MMloveConstants.JE_BASE_URL + array
												.getJSONObject(i).getString(
														"ImageURL")).toString()
												.replace("~", "")
												.replace("\\", "/"));
								if (user.UserID != 0) {
									if (user.HospitalID == Integer.valueOf(array.getJSONObject(i)
											.getString("HospitalID"))) {
										if (Integer.valueOf(array.getJSONObject(i)
												.getString("SoundFlag")) == 1) {
											isHosiptal = true;
										} else {
											isHosiptal = false;
										}
									}
								}



								if (isHosiptal) {
									tvchao.setVisibility(View.VISIBLE);
								} else {
									tvchao.setVisibility(View.GONE);
								}

								// list.add(array.getJSONObject(i)
								// .getString("HospitalName"));
								//
								// if(HospitalID==Integer.valueOf(array.getJSONObject(i)
								// .getString("HospitalID")))
								// {
								// isDefault = i;
								// }

							}



						} catch (Exception ex) {
							ex.printStackTrace();
						}
					}
				}

			};

			JsonAsyncTask_Info task = new JsonAsyncTask_Info(CHKTimeList.this, true,
					doProcess);
			Map<String, Object> paramMap = new HashMap<String, Object>();
			String str = "<Request><HospitalID>0</HospitalID></Request>";
			paramMap.put("str", str);
			// 必须是这5个参数，而且得按照顺序
			task.execute(SOAP_NAMESPACE, SOAP_ACTION4, SOAP_METHODNAME4,
					SOAP_URL, paramMap);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
		txtHead.setText("产检时间表");

		txt001 = (TextView) findViewById(R.id.txt001);
//		txt002 = (TextView) findViewById(R.id.txt002);
		
		imgphoto = (CircularImage)findViewById(R.id.cover_user_photo);

		listview = (ListView) findViewById(R.id.listview_chk);
		tvchao=(TextView)findViewById(R.id.tvchaoshen);
	}

	private void iniCHKInfo()
	{
		user = LocalAccessor.getInstance(CHKTimeList.this).getUser();
		DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
		Date date;
		try
		{
			date = fmt.parse(user.YuBirthDate);
			Yuchan yu = new Yuchan();
			int days = IOUtils.DaysCount(date);

			yu = IOUtils.WeekInfo(date);

			if(days<0)
			{
				txt001.setText("恭喜成为新妈妈！"+"，祝愿宝宝健康成长！");
			}
			else
			{
				txt001.setText("您已怀孕" + yu.Week + "周零" + yu.Days + "天，"+"距离预产期还有" + days + "天");
			}
//			if(days<0)
//			{
//				txt002.setText("祝愿宝宝健康成长！");
//			}
//			else
//			{
//				txt002.setText("距离预产期还有" + days + "天");
//			}
		}
		catch (ParseException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
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
//				overridePendingTransition(R.anim.out_of_right,R.anim.in_from_left);
			}
		});
	}

	/*
	 * 查询listview
	 */
	private void searchCHKTime()
	{
		try
		{
			user=LocalAccessor.getInstance(CHKTimeList.this).getUser();
			JsonAsyncTaskOnComplete doProcess = new JsonAsyncTaskOnComplete()
			{
				public void processJsonObject(Object result)
				{
//					{
//						"CHKTimeInquiry": [
//						{
//							"CHKType": "第一次产检",
//								"ID": "33",
//								"WeekStart": "11",
//								"WeekEnd": "13",
//								"Type": "NT预约"
//						},
//						{
//							"CHKType": "第二次产检",
//								"ID": "34",
//								"WeekStart": "14",
//								"WeekEnd": "16",
//								"Type": ""
//						},
//						{
//							"CHKType": "第三次产检",
//								"ID": "35",
//								"WeekStart": "17",
//								"WeekEnd": "20",
//								"Type": ""
//						},
//						{
//							"CHKType": "第四次产检",
//								"ID": "36",
//								"WeekStart": "21",
//								"WeekEnd": "24",
//								"Type": ""
//						},
//						{
//							"CHKType": "第五次产检",
//								"ID": "37",
//								"WeekStart": "25",
//								"WeekEnd": "28",
//								"Type": ""
//						},
//						{
//							"CHKType": "第六次产检",
//								"ID": "38",
//								"WeekStart": "29",
//								"WeekEnd": "30",
//								"Type": ""
//						},
//						{
//							"CHKType": "第七次产检",
//								"ID": "39",
//								"WeekStart": "31",
//								"WeekEnd": "32",
//								"Type": ""
//						},
//						{
//							"CHKType": "第八次产检",
//								"ID": "47",
//								"WeekStart": "33",
//								"WeekEnd": "34",
//								"Type": ""
//						},
//						{
//							"CHKType": "第九次产检",
//								"ID": "40",
//								"WeekStart": "35",
//								"WeekEnd": "36",
//								"Type": ""
//						},
//						{
//							"CHKType": "第十次产检",
//								"ID": "41",
//								"WeekStart": "37",
//								"WeekEnd": "37",
//								"Type": ""
//						},
//						{
//							"CHKType": "第十一次产检",
//								"ID": "42",
//								"WeekStart": "38",
//								"WeekEnd": "38",
//								"Type": ""
//						},
//						{
//							"CHKType": "第十二次产检",
//								"ID": "43",
//								"WeekStart": "39",
//								"WeekEnd": "39",
//								"Type": ""
//						},
//						{
//							"CHKType": "第十三次产检",
//								"ID": "44",
//								"WeekStart": "40",
//								"WeekEnd": "40",
//								"Type": ""
//						}
//						]
//					}
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
							if(arrayList!=null&&arrayList.size()>0){
								arrayList.clear();
							}
							JSONObject mySO = (JSONObject) result;
							JSONArray array = mySO
									.getJSONArray("CHKTimeInquiry");
							for (int i = 0; i < array.length(); i++)
							{

								map = new HashMap<String, Object>();
								int sweek = Integer.parseInt(array
										.getJSONObject(i)
										.getString("WeekStart"));
								int eweek = Integer.parseInt(array
										.getJSONObject(i).getString("WeekEnd"));

								if (eweek > sweek)
								{
									map.put("CHKType",
											array.getJSONObject(i).getString(
													"CHKType")
													+ "("
													+ array.getJSONObject(i)
															.getString(
																	"WeekStart")
													+ "-"
													+ array.getJSONObject(i)
															.getString(
																	"WeekEnd")
													+ "周)");
									map.put("CHKTypeName",
													"第("
													+ array.getJSONObject(i)
															.getString(
																	"WeekStart")
													+ "-"
													+ array.getJSONObject(i)
															.getString(
																	"WeekEnd")
													+ "周)产检");

								}
								else if (eweek == sweek)
								{
									map.put("CHKType",
											array.getJSONObject(i).getString(
													"CHKType")
													+ "("
													+ array.getJSONObject(i)
															.getString(
																	"WeekStart")
													+ "周)");
									map.put("CHKTypeName",
													"第"
													+ array.getJSONObject(i)
															.getString(
																	"WeekStart")
													+ "周产检");
								}

								map.put("ID",
										array.getJSONObject(i).getString("ID"));

								map.put("Type",
										array.getJSONObject(i).getString("Type"));



								// map.put("WeekStart", array.getJSONObject(i)
								// .getString("WeekStart"));
								// map.put("WeekEnd", array.getJSONObject(i)
								// .getString("WeekEnd"));
								// map.put("icon_right",
								// R.drawable.icon_arrow_right);
								// map.put("icon_left",
								// R.drawable.new_icon_right_arrow);
								arrayList.add(map);

							}



//							SimpleAdapter adapter = new SimpleAdapter(
//									CHKTimeList.this, arrayList,
//									R.layout.item_chklist, new Object[]
//									{ "CHKType" }, new int[]
//									{ R.id.chktime_name });
//							listview.setAdapter(adapter);
							ChktimelistAdapter adapter=new ChktimelistAdapter(CHKTimeList.this, arrayList);
							listview.setAdapter(adapter);
							listview.setEnabled(true);
							// 添加listView点击事件
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
									String CHKType = mapDetail.get("CHKTypeName")
											.toString();
									Intent i = new Intent(CHKTimeList.this,
											CHKTimeDetailActivity.class);
								    i.putExtra("ID", ID);
//									i.putExtra("ID", hosId);
									i.putExtra("CHKType", CHKType);
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

			JsonAsyncTask_Info task = new JsonAsyncTask_Info(CHKTimeList.this,
					true, doProcess);
			Map<String, Object> paramMap = new HashMap<String, Object>();
			String str;
			hosId=user.HospitalID+"";
			if(hosId!=null&&!"".equals(hosId)){
				str= "<Request><HospitalID>"+hosId+"</HospitalID><Week>0</Week></Request>";
			}else{
			str = "<Request><HospitalID>1</HospitalID><Week>0</Week></Request>";
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
	
	
	private void UserInquiry()
	{
		try
		{
			JsonAsyncTaskOnComplete doProcess = new JsonAsyncTaskOnComplete()
			{
				public void processJsonObject(Object result)
				{
					String UserClientBind = result.toString();
					if (UserClientBind == null)
					{

					}
					else
					{
						// 下边是具体的处理
						try
						{
							JSONObject mySO = (JSONObject) result;
							JSONArray array = mySO
									.getJSONArray("UserInquiry");

							if (array.getJSONObject(0).has("MessageCode"))
							{
								
							}
							else
							{
								
								for (int i = 0; i < array.length(); i++)
								{
									
									if(array.getJSONObject(i)
											.getString("PictureURL").toString().contains("vine.gif"))
									{
										imgphoto.setImageResource(R.drawable.icon_user_normal);
									}
									else
									{
										ImageLoader.getInstance().displayImage(MMloveConstants.JE_BASE_URL3 + array.getJSONObject(i)
													.getString("PictureURL").toString().replace("~", "").replace("\\", "/"), imgphoto);
									}
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

			JsonAsyncTask_Info task = new JsonAsyncTask_Info(CHKTimeList.this, true,
					doProcess);
			Map<String, Object> paramMap = new HashMap<String, Object>();
			String str = "<Request><UserID>%s</UserID></Request>";
			str = String.format(str, new Object[]
			{ String.valueOf(user.UserID)});
			paramMap.put("str", str);

			// 必须是这5个参数，而且得按照顺序
			task.execute(SOAP_NAMESPACE, SOAP_ACTION2, SOAP_METHODNAME2,
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
