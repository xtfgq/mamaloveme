package com.netlab.loveofmum;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.kobjects.base64.Base64;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Bitmap.Config;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.netlab.loveofmum.api.BaseActivity;
import com.netlab.loveofmum.api.MMloveConstants;
import com.netlab.loveofmum.api.ImageTools;
import com.netlab.loveofmum.api.JsonAsyncTaskOnComplete;
import com.netlab.loveofmum.api.JsonAsyncTask_Info;
import com.netlab.loveofmum.api.LocalAccessor;
import com.netlab.loveofmum.api.MyApplication;
import com.netlab.loveofmum.huanxin.MainChatActivity;
import com.netlab.loveofmum.model.Order;
import com.netlab.loveofmum.model.User;
import com.netlab.loveofmum.myadapter.CHKTimeDetail_ItemAdapter;
import com.netlab.loveofmum.myadapter.MyCHK_OrderAdd_Adapter;
import com.netlab.loveofmum.myadapter.MyCHK_Timeline_Adapter;
import com.netlab.loveofmum.timepicker.Util;
import com.netlab.loveofmum.utils.DialogUtils;
import com.netlab.loveofmum.utils.IOUtils;
import com.netlab.loveofmum.utils.SystemStatusManager;
import com.netlab.loveofmum.widget.DialogCancle;
import com.netlab.loveofmum.widget.DialogEnsureCancelView;
import com.netlab.loveofmum.widget.GridViewForScrollView;
import com.netlab.loveofmum.widget.ListViewForScrollView;

import com.nostra13.universalimageloader.cache.memory.impl.LRULimitedMemoryCache;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.umeng.analytics.MobclickAgent;

public class MyCHK_OrderDetail extends BaseActivity
{

	private TextView txtHead;
	private ImageView imgBack;

	private List<Map<String, Object>> arrayList = new ArrayList<Map<String, Object>>();
	private List<Map<String, Object>> arrayList2;
	
	private List<Map<String, Object>> arrayList3 = new ArrayList<Map<String, Object>>();
	private List<Map<String, Object>> arrayList4 = new ArrayList<Map<String, Object>>();
	
	private String returnvalue001;
	
	private TextView txt001;
	private TextView txt002;
	private TextView txt003;
	private TextView txt004;
	
	private TextView txt005;
	private TextView txt006;
	private TextView tvnotice;
	
	private LinearLayout linear001;
	private LinearLayout linear002;
	private LinearLayout linear003;
	private LinearLayout linear004;
	
	private LinearLayout linear005;
	
	
	private GridViewForScrollView gridview;
	
	private ListViewForScrollView gridview2;
	
	private Order order;	
	
	private String status;
	
	private String ID;
	private String OrderID;
	private User user;
	
	private String CHKTypeItems = "";
	
	private ScrollView sv;
	
	private String chktypeid="";
	
	private LinearLayout tab001;
	
	private String HospitalID;
	private LinearLayout ll_mmtalk;
	
	//private ImageLoader imageLoader;
	
	// 定义Web Service相关的字符串
	private final String SOAP_NAMESPACE = MMloveConstants.URL001;
	private final String SOAP_ACTION = MMloveConstants.URL001
			+ MMloveConstants.OrderInquiry;
	private final String SOAP_METHODNAME = MMloveConstants.OrderInquiry;
	private final String SOAP_URL = MMloveConstants.URL002;
	
	private final String SOAP_ACTION2 = MMloveConstants.URL001
			+ MMloveConstants.OrderItemInquiry;
	private final String SOAP_METHODNAME2 = MMloveConstants.OrderItemInquiry;
	
	private final String SOAP_ACTION3 = MMloveConstants.URL001
			+ MMloveConstants.UserPicInquiry;
	private final String SOAP_METHODNAME3 = MMloveConstants.UserPicInquiry;
	
	private final String SOAP_ACTION4 = MMloveConstants.URL001
			+ MMloveConstants.UploadUserPic;
	private final String SOAP_METHODNAME4 = MMloveConstants.UploadUserPic;
	
	private final String SOAP_ACTION5 = MMloveConstants.URL001
			+ MMloveConstants.CHKTypeItemsInquiry;
	private final String SOAP_METHODNAME5 = MMloveConstants.CHKTypeItemsInquiry;
	
	private final String SOAP_ACTIONCANCLE = MMloveConstants.URL001
			+ MMloveConstants.OrderCancel;
	private final String SOAP_METHODNAMECANCLE = MMloveConstants.OrderCancel;
	
	private final String SOAP_ACTIONUPATE = MMloveConstants.URL001
			+ MMloveConstants.OrderUpdate;
	private final String SOAP_METHODNAMEUPDATE=MMloveConstants.OrderUpdate;

	private static final int TAKE_PICTURE = 0;
	private static final int CHOOSE_PICTURE = 1;
	private static final int CROP = 2;
	private static final int CROP_PICTURE = 3;

	private static final int SCALE = 5;// 照片缩小比例
	private ImageView iv_image = null;
	
	private ImageView img001;
	private ImageView ivhead;
	private ImageView ivarrow;
	private RelativeLayout rlmore;
	private int num=1;

	private byte[] mContent;
//	private EditText mMMsay;
	
	public static DisplayImageOptions mNormalImageOptions;
	private Button btnCancle,sendmm;
	private Handler handler = new Handler() {
	@Override
	public void handleMessage(Message msg) {
		super.handleMessage(msg);
	
		switch (msg.what) {
		case 0:
			btnCancle.setEnabled(false);
		
			break;
		case 1:
			tvnotice.setText("");
			btnCancle.setVisibility(View.GONE);
			break;
	
		
		default:
			break;
		}
		
	}
};
	
	public static final String SDCARD_PATH = Environment.getExternalStorageDirectory().toString();
	
	public static final String IMAGES_FOLDER = SDCARD_PATH + File.separator + "mmlove" + File.separator + "images" + File.separator;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setTranslucentStatus() ;
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.layout_my_orderdetail);
		
//		initImageLoader(this);
		MyApplication.getInstance().addActivity(this);
		iniView();
		setListeners();
		if(hasInternetConnected())
		{
			searchOrderDetail();
			
			//imageLoader = ImageLoader.getInstance();
			//imageLoader.init(ImageLoaderConfiguration.createDefault(MyCHK_OrderDetail.this));
		}
		else
		{
			Toast.makeText(MyCHK_OrderDetail.this, R.string.msgUninternet,
					Toast.LENGTH_SHORT).show();
		}
		ViewGroup.LayoutParams para;
		para = ivhead.getLayoutParams();
		para.width = Util.getScreenWidth(MyCHK_OrderDetail.this);
		para.height = para.width * 163/ 720;
		ivhead.setLayoutParams(para);
//		para.height=para.width * 183/ 720;
//		tab001.setLayoutParams(para);
		
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
	private void initImageLoader(Context context) {
//		int memoryCacheSize = (int) (Runtime.getRuntime().maxMemory() / 5);
//		MemoryCacheAware<String, Bitmap> memoryCache;
//		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
//			memoryCache = new LruMemoryCache(memoryCacheSize);
//		} else {
//			memoryCache = new LRULimitedMemoryCache(memoryCacheSize);
//		}

//		mNormalImageOptions = new DisplayImageOptions.Builder().bitmapConfig(Config.RGB_565).cacheInMemory(true).cacheOnDisc(true)
//				.resetViewBeforeLoading(true).build();
//
//		// This
//		// This configuration tuning is custom. You can tune every option, you
//		// may tune some of them,
//		// or you can create default configuration by
//		// ImageLoaderConfiguration.createDefault(this);
//		// method.
//		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context).defaultDisplayImageOptions(mNormalImageOptions)
//				.denyCacheImageMultipleSizesInMemory().discCache(new UnlimitedDiscCache(new File(IMAGES_FOLDER)))
//				// .discCacheFileNameGenerator(new Md5FileNameGenerator())
//				.memoryCache(memoryCache)
//				// .memoryCacheSize(memoryCacheSize)
//				.tasksProcessingOrder(QueueProcessingType.LIFO).threadPriority(Thread.NORM_PRIORITY - 2).threadPoolSize(3).build();
//
//		// Initialize ImageLoader with configuration.
//		ImageLoader.getInstance().init(config);
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
		
		txtHead.setText("订单详情");
		
		txt001 = (TextView)findViewById(R.id.weekly_001);
		txt002 = (TextView)findViewById(R.id.weekly_002);
		txt003 = (TextView)findViewById(R.id.weekly_time);
		txt004 = (TextView)findViewById(R.id.txt006_my_orderdetail);
		
		txt005 = (TextView)findViewById(R.id.txt001_my_orderdetail);
		
		txt006 = (TextView)findViewById(R.id.txt007_my_orderdetail);
		
		user =LocalAccessor.getInstance(MyCHK_OrderDetail.this).getUser();
		ID = this.getIntent().getExtras().getString("ID").toString();
		
		linear001 = (LinearLayout)findViewById(R.id.linear02_myorderdetail);
		linear002 = (LinearLayout)findViewById(R.id.linear002_layout_my_orderdetail);		
		
		linear003 = (LinearLayout)findViewById(R.id.linear003_layout_my_orderdetail);
		
		linear004 = (LinearLayout)findViewById(R.id.linear01_myorderdetail);
		
		linear005 = (LinearLayout)findViewById(R.id.linear05_myorderdetail);
		
		gridview = (GridViewForScrollView)findViewById(R.id.grid_myorderdetail);
		
		gridview2 = (ListViewForScrollView)findViewById(R.id.grd002_myorderdetail);
		
		tab001 = (LinearLayout)findViewById(R.id.tab001_order_detail);
		
		img001 = (ImageView)findViewById(R.id.weekly_004);
		ll_mmtalk=(LinearLayout) findViewById(R.id.ll_mmtalk);
		sv = (ScrollView) findViewById(R.id.sv_orderdetail);
//		mMMsay=(EditText) findViewById(R.id.weekly_in_spection06);
//		sendmm=(Button) findViewById(R.id.sendmm);
		btnCancle=(Button) findViewById(R.id
				.btn_cancle);
		ivarrow=(ImageView)findViewById(R.id.iv_arrow);
		ivhead=(ImageView)findViewById(R.id.img001_orderdetails);
		rlmore=(RelativeLayout)findViewById(R.id.rlmore);
		tvnotice=(TextView)findViewById(R.id.tv_notice);
		sv.smoothScrollTo(0, 0);
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
		
		img001.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				if(hasInternetConnected())
				{
					if(status.equals("03")||status.equals("07"))
					{
						Toast.makeText(MyCHK_OrderDetail.this, "该预约产检已经取消，不能上传检查单！",
								Toast.LENGTH_SHORT).show();
					}else if(status.equals("06")){
						Toast.makeText(MyCHK_OrderDetail.this, "该预约已经违约，不能上传检查单！",
								Toast.LENGTH_SHORT).show();
						}else if(status.equals("05")){
							Toast.makeText(MyCHK_OrderDetail.this, "该预约已经停诊，不能上传检查单！",
									Toast.LENGTH_SHORT).show();
						}
					else
					{
						showPicturePicker(MyCHK_OrderDetail.this, false);
					}
				}
				else
				{
					Toast.makeText(MyCHK_OrderDetail.this, R.string.msgUninternet,
							Toast.LENGTH_SHORT).show();
				}
				
			}
		});
		
		tab001.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				Intent i = new Intent(MyCHK_OrderDetail.this,CHK_Zhuyi.class);
				i.putExtra("ID", chktypeid);
				i.putExtra("HospitalID", HospitalID);
				startActivity(i);
			}
		});
		btnCancle.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				DialogCancle dialogCancelView = new DialogCancle(
						MyCHK_OrderDetail.this).setDialogMsg("友情提示",
								"亲，一个月内若取消订单两次，则该月不能再预约了哦！", "确定").setOnClickListenerEnsure(
						new OnClickListener() {

							@Override
							public void onClick(View v) {
								
								cancleOrder(OrderID,HospitalID);
								
							}
						});
				DialogUtils.showSelfDialog(MyCHK_OrderDetail.this, dialogCancelView);
				
			}
		});
		rlmore.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(num%2==0){
					CHKTimeDetail_ItemAdapter adapter = new CHKTimeDetail_ItemAdapter(
							MyCHK_OrderDetail.this, arrayList3);
					ivarrow.setBackgroundResource(R.drawable.icom_xsjt);
					gridview2.setAdapter(adapter);
					gridview2.setEnabled(true);
				}else{

					CHKTimeDetail_ItemAdapter adapter = new CHKTimeDetail_ItemAdapter(
							MyCHK_OrderDetail.this,arrayList4);
					ivarrow.setBackgroundResource(R.drawable.icon_arrow_des);

					gridview2.setAdapter(adapter);
					gridview2.setEnabled(true);

				}
				num++;

			}
		});
//		sendmm.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				if("".equals(mMMsay.getText().toString().trim())){
//					Toast.makeText(MyCHK_OrderDetail.this, "请填写说话内容", 1).show();
//					
//				}else{
//					updateOrder(OrderID,mMMsay.getText().toString().trim());
//				}
//			}
//		});
	}

	protected void cancleOrder(String OrderID,String HospitalID) {
		// TODO Auto-generated method stub
		
		
		
		
			JsonAsyncTaskOnComplete	doProcess = new  JsonAsyncTaskOnComplete(){

				@Override
				public void processJsonObject(Object result) {
					// TODO Auto-generated method stub
//					{"OrderInsert":[{"MessageContent":"成功！","MessageCode":"0"}]}
					try {
						JSONObject mySO = (JSONObject) result;
						JSONArray array = mySO
								.getJSONArray("OrderInsert");
						if("0".equals(array.getJSONObject(0).getString("MessageCode"))){
							if(array.getJSONObject(0).has("PointAddCode")) {
								if(array.getJSONObject(0).getString("PointAddCode").equals("0")) {

									Intent i = new Intent(MyCHK_OrderDetail.this, ReducePointsActivity.class);
									i.putExtra("content", array.getJSONObject(0).getString("PointAddContent") + "-" + array.getJSONObject(0).getString("PointValue"));
									startActivity(i);
								}
							}
						}
						Message msg1 = new Message();
						msg1.what = 1;
						handler.sendMessage(msg1);
						
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}};
		
		JsonAsyncTask_Info task = new JsonAsyncTask_Info(
				MyCHK_OrderDetail.this, true, doProcess);
		Map<String, Object> paramMap = new HashMap<String, Object>();
		String str = "<Request><OrderID>%s</OrderID><HospitalID>%s</HospitalID></Request>";

		str = String.format(str, new Object[]
		{ OrderID,HospitalID });
		paramMap.put("str", str);

		// 必须是这5个参数，而且得按照顺序
		task.execute(SOAP_NAMESPACE, SOAP_ACTIONCANCLE, SOAP_METHODNAMECANCLE,
				SOAP_URL, paramMap);
	}
	protected void updateOrder(String OrderID,String MomSay) {
		// TODO Auto-generated method stub
		
			JsonAsyncTaskOnComplete	doProcess = new  JsonAsyncTaskOnComplete(){

				@Override
				public void processJsonObject(Object result) {
					// TODO Auto-generated method stub
//				{"OrderUpdate":[{"MessageContent":"成功！","MessageCode":"0"}]}
					try {
						String res=result.toString();
						JSONObject mySO = (JSONObject) result;
//						Log.i("resssss",res);
//						JSONArray array = mySO
//								.getJSONArray("OrderInsert");
//						if("0".equals(array.getJSONObject(0).getString("MessageCode"))){
//							Toast.makeText(MyCHK_OrderDetail.this, "取消订单成功！", 1).show();
//						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}};
		
		JsonAsyncTask_Info task = new JsonAsyncTask_Info(
				MyCHK_OrderDetail.this, true, doProcess);
		Map<String, Object> paramMap = new HashMap<String, Object>();
		String str = "<Request><OrderID>%s</OrderID><MomSay>%s</MomSay></Request>";

		str = String.format(str, new Object[]
		{ OrderID,MomSay });
		paramMap.put("str", str);

		// 必须是这5个参数，而且得按照顺序
		task.execute(SOAP_NAMESPACE, SOAP_ACTIONUPATE, SOAP_METHODNAMEUPDATE,
				SOAP_URL, paramMap);
	}

	/*
	 * 查询gridview
	 */
	private void searchOrderItems()
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
									.getJSONArray("OrderItemInquiry");
							
							if(array.getJSONObject(0).has("MessageCode"))
							{
								linear001.setVisibility(View.GONE);
							}
							else
							{
								linear001.setVisibility(View.VISIBLE);
								for (int i = 0; i < array.length(); i++)
								{
									CHKTypeItems += array.getJSONObject(i)
											.getString("ItemName")+";";
								}
								txt004.setText(CHKTypeItems);
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
					MyCHK_OrderDetail.this, true, doProcess);
			Map<String, Object> paramMap = new HashMap<String, Object>();
			String str = "<Request><OrderID>%s</OrderID><HospitalID>%s</HospitalID></Request>";

			str = String.format(str, new Object[]
			{ OrderID,HospitalID });
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
	
	/*
	 * 查询gridview
	 */
	private void searchPictureList()
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
									.getJSONArray("UserPicInquiry");
							arrayList2 = new ArrayList<Map<String, Object>>();
							arrayList2.clear();
							
							if(array.getJSONObject(0).has("MessageCode"))
							{
								
							}
							else
							{
								for (int i = 0; i < array.length(); i++)
								{
	
									map = new HashMap<String, Object>();
									map.put("PicURL",
											array.getJSONObject(i).getString("PicURL"));
	
									arrayList2.add(map);
	
								}
								
							}
							// 添加listView点击事件
							MyCHK_OrderAdd_Adapter adapter = new MyCHK_OrderAdd_Adapter(MyCHK_OrderDetail.this, arrayList2);
							
							gridview.setAdapter(adapter);
						}
						catch (Exception ex)
						{
							ex.printStackTrace();
						}
					}
				}
			};

			JsonAsyncTask_Info task = new JsonAsyncTask_Info(MyCHK_OrderDetail.this,
					true, doProcess);
			Map<String, Object> paramMap = new HashMap<String, Object>();
			String str = "<Request><UserID>%s</UserID><OrderID>%s</OrderID></Request>";

			str = String.format(str, new Object[]
			{String.valueOf(user.UserID),OrderID });
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
	 * 查询
	 */
	private void searchOrderDetail()
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
									.getJSONArray("OrderInquiry");
							
							if(array.getJSONObject(0).has("MessageCode"))
							{
								
							}
							else
							{
								for(int i = 0; i < array.length(); i++)
								{
									OrderID = array.getJSONObject(i).getString("OrderID").toString();
									
									HospitalID = array.getJSONObject(i).getString("HospitalID").toString();
									
									if(array.getJSONObject(i).getString("OrderStatus").toString().equals("04"))
									{
										linear002.setVisibility(View.VISIBLE);
										linear003.setVisibility(View.VISIBLE);
										linear001.setVisibility(View.GONE);
										
										linear004.setVisibility(View.GONE);
										
										linear005.setVisibility(View.VISIBLE);
										showFootView();
									}
//									else if(array.getJSONObject(i).getString("OrderStatus").toString().equals("05")){
//										Message msg1 = new Message();
//										msg1.what = 1;
//										handler.sendMessage(msg1);
//										
////										1.停诊订单、用户取消订单，未到产检预约时间，订单详情界面，取消订单按钮隐藏
////										2.停诊订单、用户取消订单，已过产检预约时间，订单详情界面，不可记录妈妈想说，不可上传检查单
//									}
									
									else
									{
										chktypeid = array.getJSONObject(i).getString("CHKID").toString();
										linear002.setVisibility(View.GONE);
										linear003.setVisibility(View.GONE);
										
										String time = array.getJSONObject(i)
												.getString("Gotime").toString();
										
										SimpleDateFormat formatter = new SimpleDateFormat(
												"yyyy-MM-dd");
										
										time = time.split(" ")[0].replace("/", "-");
										
										time = formatter.format(formatter
												.parse(time));
										Date d = formatter.parse(time);
										
										Date dnow = formatter.parse(formatter.format(new Date()));
										
										if(d.compareTo(dnow)<0)
										{
											linear004.setVisibility(View.GONE);
											linear005.setVisibility(View.VISIBLE);
										
										}
										else
										{
											linear004.setVisibility(View.VISIBLE);
											linear005.setVisibility(View.GONE);
											
											searchCHKTimeItems();
										}
											hideFootView();
										searchOrderItems();
									}
									txt001.setText(array.getJSONObject(i)
											.getString("HospitalName").toString());
									txt002.setText(array.getJSONObject(i)
											.getString("DoctorName").toString());
									
									
									
									String time = array.getJSONObject(i)
											.getString("Gotime").toString();
									
									SimpleDateFormat formatter = new SimpleDateFormat(
											"yyyy-MM-dd");
									time = time.split(" ")[0].replace("/", "-");
									String[] str=time.split("\\-");
									int day=Integer.valueOf(str[2]) - 1;
									int month=Integer.valueOf(str[1]);

									if(day==0){
										if((month-1==1)||(month-1==3)||(month-1==5)||(month-1==7)||(month-1==8)||(month-1==10)||(month-1==12)){
											day=31;
										}else if((month-1==2)){
											if(IOUtils.checkYear(Integer.valueOf(str[0]))){
												day=29;
											}else{
												day=28;
											}


										}else{
											day=30;
										}
										tvnotice.setText("温馨提示：请在" + (month-1) + "月" + day + "日" + "12：00之前取消预约哦！");



									}else {
										tvnotice.setText("温馨提示：请在" +  str[1] + "月" + (Integer.valueOf(str[2]) - 1) + "日" + "12：00之前取消预约哦！");

									}
									btnCancle.setVisibility(View.VISIBLE);
									if(!array.getJSONObject(i).getString("OrderStatus").toString().equals("04")){
									String chkTime=time+" 00:00:00";
									
								
									
									SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
									
									String end=" "+array.getJSONObject(i).getString("EndTime").toString();
									String endtime;
									
									endtime=time+end+":00";
								
									Date endTingzhen=sf.parse(endtime);
									Date d1 = sf.parse(chkTime);
									long timeBefore=d1.getTime()-12 * 60 * 60 * 1000;
									long endTime=d1.getTime()+24*60 * 60 * 1000;
									Date dNow = new Date();
									if(array.getJSONObject(i).getString("OrderStatus").toString().equals("05")){
										 if(dNow.getTime()>endTingzhen.getTime()){
											hideFootView();
											linear005.setVisibility(View.GONE);
										}
										Message msg = new Message();
										msg.what = 1;
										handler.sendMessage(msg);
										
									}else if(array.getJSONObject(i).getString("OrderStatus").toString().equals("07")||array.getJSONObject(i).getString("OrderStatus").toString().equals("03")){
										Message msg = new Message();
										msg.what = 1;
										handler.sendMessage(msg);
									}
									else{
											if(dNow.getTime()>timeBefore&&dNow.getTime()<endTime){
												Message msg = new Message();
												msg.what = 0;
												handler.sendMessage(msg);
											}else if(dNow.getTime()>endTime){
												Message msg1 = new Message();
												msg1.what = 1;
												handler.sendMessage(msg1);
											}
									}
									}
									txt005.setText(array.getJSONObject(i)
											.getString("Reason").toString());
									
									status =  array.getJSONObject(i)
											.getString("OrderStatus").toString();
									
									try
									{
										time = formatter.format(formatter
												.parse(time));
//										

										time=time.replace("-","/");
									}
									catch (ParseException e)
									{
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									
									if(array.getJSONObject(i).getString("OrderStatus").toString().equals("04"))
									{
										txt003.setText(time);
									}
									else
									{
										txt003.setText(time+" "+array.getJSONObject(i)
												.getString("BeginTime").toString()+"-"+array.getJSONObject(i)
												.getString("EndTime").toString());
									}
									
									txt006.setText(time);
								}
								
								searchPictureList();
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
					MyCHK_OrderDetail.this, true, doProcess);
			Map<String, Object> paramMap = new HashMap<String, Object>();
			String str = "<Request><UserID>%s</UserID><UserMobile>%s</UserMobile><CHKID>%s</CHKID><ID>%s</ID><HospitalID>%s</HospitalID><Flag>%s</Flag></Request>";
			str = String.format(str, new Object[]
			{String.valueOf(user.UserID),"","",ID,HospitalID,"0"});
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
	
	
	private void showFootView() {
		// TODO Auto-generated method stub
  ll_mmtalk.setVisibility(View.VISIBLE);
		btnCancle.setVisibility(View.GONE);
		
	}
	private void  hideFootView(){
		ll_mmtalk.setVisibility(View.GONE);
		btnCancle.setVisibility(View.GONE);
	}

	private void UploadPicture(String uploadBuffer)
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
									.getJSONArray("UploadUserPic");

							if (array.getJSONObject(0).getString("MessageCode")
									.equals("0"))
							{
//								Toast.makeText(MyCHK_OrderAdd.this, "上传成功",
//										Toast.LENGTH_SHORT).show();
								searchPictureList();
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
					MyCHK_OrderDetail.this, true, doProcess);
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("UserID", String.valueOf(user.UserID));
			paramMap.put("fileName", getPhotoFileName());
			paramMap.put("img", uploadBuffer);
			paramMap.put("OrderID", OrderID);

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
								
								map.put("ItemStatus", array.getJSONObject(i)
										.getString("Value"));
								map.put("ItemContent", array.getJSONObject(i)
										.getString("ItemContent"));
							
									arrayList3.add(map);
								
							
							}

							// SimpleAdapter adapter = new SimpleAdapter(
							// CHKTimeDetail.this, arrayList,
							// R.layout.item_chkdetail,
							// new Object[] { "ItemName" },
							// new int[] { R.id.txt_item_chkdetail });


							// listview.setAdapter(adapter);
							if(arrayList3.size()>1){
								rlmore.setVisibility(View.VISIBLE);
							}else{
								rlmore.setVisibility(View.GONE);
							}
							arrayList4.add(arrayList3.get(0));
							CHKTimeDetail_ItemAdapter adapter = new CHKTimeDetail_ItemAdapter(
									MyCHK_OrderDetail.this, arrayList4);
							gridview2.setAdapter(adapter);
							gridview2.setEnabled(true);
							// 添加listView点击事件

						}
						catch (Exception ex)
						{
							ex.printStackTrace();
						}
					}
				}
			};

			JsonAsyncTask_Info task = new JsonAsyncTask_Info(
					MyCHK_OrderDetail.this, true, doProcess);
			Map<String, Object> paramMap = new HashMap<String, Object>();
			String str = "<Request><CHKTypeID>%s</CHKTypeID><HospitalID>%s</HospitalID></Request>";

			str = String.format(str, new Object[]
			{ chktypeid,HospitalID });
			paramMap.put("str", str);

			// 必须是这5个参数，而且得按照顺序
			task.execute(SOAP_NAMESPACE, SOAP_ACTION5, SOAP_METHODNAME5,
					SOAP_URL, paramMap);
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 用当前时间给取得的图片命名
	 * 
	 */
	private String getPhotoFileName()
	{
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyyMMddHHmmss"+user.UserID);
		return dateFormat.format(date) + ".jpg";
	}
	
	/**
	 * 用当前时间给取得的图片命名
	 * 
	 */
	private String getOrderID()
	{
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyyMMddHHmmss");
		return dateFormat.format(date) + user.UserID;
	}
	

	public static Bitmap rotateImage(Bitmap bmp, int degrees)
	{
		if (degrees != 0)
		{
			Matrix matrix = new Matrix();
			matrix.postRotate(degrees);
			return Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(),
					bmp.getHeight(), matrix, true);
		}
		return bmp;
	}

	protected String getAbsoluteImagePath(Uri uri)
	{
		// can post image
		String[] proj =
		{ MediaStore.Images.Media.DATA };
		Cursor cursor = managedQuery(uri, proj, // Which columns to return
				null, // WHERE clause; which rows to return (all rows)
				null, // WHERE clause selection arguments (none)
				null); // Order-by clause (ascending by name)

		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();

		return cursor.getString(column_index);
	}

	private int getDegree(ExifInterface exif)
	{
		int degree = 0;

		if (exif != null)
		{

			// 读取图片中相机方向信息

			int ori = exif.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,

					ExifInterface.ORIENTATION_UNDEFINED);

			// 计算旋转角度

			switch (ori)
			{

				case ExifInterface.ORIENTATION_ROTATE_90:

					degree = 90;

					break;

				case ExifInterface.ORIENTATION_ROTATE_180:

					degree = 180;

					break;

				case ExifInterface.ORIENTATION_ROTATE_270:

					degree = 270;

					break;

				default:

					degree = 0;

					break;

			}
		}
		return degree;
	}
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK)
		{
			switch (requestCode)
			{
				case TAKE_PICTURE:
					// 将保存在本地的图片取出并缩小后显示在界面上
					Bitmap bitmap = BitmapFactory.decodeFile(Environment
							.getExternalStorageDirectory() + "/image.jpg");
					Bitmap newBitmap = ImageTools.zoomBitmap(bitmap,
							bitmap.getWidth() / SCALE, bitmap.getHeight()
									/ SCALE);
					// 由于Bitmap内存占用较大，这里需要回收内存，否则会报out of memory异常
					bitmap.recycle();

					String url = Environment.getExternalStorageDirectory() + "/image.jpg";
					
					ExifInterface exif2 = null;
					try
					{
						exif2 = new ExifInterface(url);
					}
					catch (FileNotFoundException e)
					{
						e.printStackTrace();
					}
					catch (IOException e)
					{
						e.printStackTrace();
					}
					int degree2 = getDegree(exif2);
					if(degree2!=0)
					{
						newBitmap = rotateImage(newBitmap,degree2);
					}
					
					// 将处理过的图片显示在界面上，并保存到本地
					//iv_image.setImageBitmap(newBitmap);
					ImageTools.savePhotoToSDCard(newBitmap, Environment
							.getExternalStorageDirectory().getAbsolutePath(),
							String.valueOf(System.currentTimeMillis()));

					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					newBitmap.compress(Bitmap.CompressFormat.JPEG,
							100, baos);
					mContent = baos.toByteArray();

					String uploadBuffer = new String(
							Base64.encode(mContent));

					UploadPicture(uploadBuffer);
					System.gc();
					break;

				case CHOOSE_PICTURE:
					ContentResolver resolver = getContentResolver();
					// 照片的原始资源地址
					Uri originalUri = data.getData();
					try
					{
						// 使用ContentProvider通过URI获取原始图片
						Bitmap photo = MediaStore.Images.Media.getBitmap(
								resolver, originalUri);

						ExifInterface exif = null;
						if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.KITKAT)
						{
							exif = new ExifInterface(IOUtils.getPath(MyCHK_OrderDetail.this, originalUri));
						}
						else
						{
							exif = new ExifInterface(
									getAbsoluteImagePath(originalUri));
						}
						
						
						int degree = getDegree(exif);
						
						if (photo != null)
						{
							// 为防止原始图片过大导致内存溢出，这里先缩小原图显示，然后释放原始Bitmap占用的内存
							Bitmap smallBitmap = ImageTools.zoomBitmap(photo,
									photo.getWidth() / SCALE, photo.getHeight()
											/ SCALE);
							// 释放原始图片占用的内存，防止out of memory异常发生
							photo.recycle();
							if(degree!=0)
							{
								smallBitmap = rotateImage(smallBitmap,degree);
							}
							//iv_image.setImageBitmap(smallBitmap);

							ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
							smallBitmap.compress(Bitmap.CompressFormat.JPEG,
									100, baos2);
							mContent = baos2.toByteArray();

							String uploadBuffer2 = new String(
									Base64.encode(mContent));

							UploadPicture(uploadBuffer2);
							System.gc();
						}
					}
					catch (FileNotFoundException e)
					{
						e.printStackTrace();
					}
					catch (IOException e)
					{
						e.printStackTrace();
					}
					break;

				case CROP:
					Uri uri = null;
					if (data != null)
					{
						uri = data.getData();
						System.out.println("Data");
					}
					else
					{
						System.out.println("File");
						String fileName = getSharedPreferences("temp",
								Context.MODE_WORLD_WRITEABLE).getString(
								"tempName", "");
						uri = Uri.fromFile(new File(Environment
								.getExternalStorageDirectory(), fileName));
					}
					cropImage(uri, 500, 500, CROP_PICTURE);
					break;

				case CROP_PICTURE:
					Bitmap photo = null;
					Uri photoUri = data.getData();
					if (photoUri != null)
					{
						photo = BitmapFactory.decodeFile(photoUri.getPath());
					}
					if (photo == null)
					{
						Bundle extra = data.getExtras();
						if (extra != null)
						{
							photo = (Bitmap) extra.get("data");
							ByteArrayOutputStream stream = new ByteArrayOutputStream();
							photo.compress(Bitmap.CompressFormat.JPEG, 100,
									stream);
						}
					}
					iv_image.setImageBitmap(photo);
					break;
				default:
					break;
			}
		}
	}

	public void showPicturePicker(Context context, boolean isCrop)
	{
		final boolean crop = isCrop;
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("图片来源");
		builder.setNegativeButton("取消", null);
		builder.setItems(new String[]
		{ "拍照", "相册" }, new DialogInterface.OnClickListener()
		{
			// 类型码
			int REQUEST_CODE;

			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				switch (which)
				{
					case TAKE_PICTURE:
						Uri imageUri = null;
						String fileName = null;
						Intent openCameraIntent = new Intent(
								MediaStore.ACTION_IMAGE_CAPTURE);
						if (crop)
						{
							REQUEST_CODE = CROP;
							// 删除上一次截图的临时文件
							SharedPreferences sharedPreferences = getSharedPreferences(
									"temp", Context.MODE_WORLD_WRITEABLE);
							ImageTools.deletePhotoAtPathAndName(Environment
									.getExternalStorageDirectory()
									.getAbsolutePath(), sharedPreferences
									.getString("tempName", ""));

							// 保存本次截图临时文件名字
							fileName = String.valueOf(System
									.currentTimeMillis()) + ".jpg";
							Editor editor = sharedPreferences.edit();
							editor.putString("tempName", fileName);
							editor.commit();
						}
						else
						{
							REQUEST_CODE = TAKE_PICTURE;
							fileName = "image.jpg";
						}
						imageUri = Uri.fromFile(new File(Environment
								.getExternalStorageDirectory(), fileName));
						// 指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
						openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,
								imageUri);
						startActivityForResult(openCameraIntent, REQUEST_CODE);
						break;

					case CHOOSE_PICTURE:
						Intent openAlbumIntent = new Intent(
								Intent.ACTION_GET_CONTENT);
						if (crop)
						{
							REQUEST_CODE = CROP;
						}
						else
						{
							REQUEST_CODE = CHOOSE_PICTURE;
						}
						openAlbumIntent.setDataAndType(
								MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
								"image/*");
						startActivityForResult(openAlbumIntent, REQUEST_CODE);
						break;

					default:
						break;
				}
			}
		});
		builder.create().show();
	}

	// 截取图片
	public void cropImage(Uri uri, int outputX, int outputY, int requestCode)
	{
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", outputX);
		intent.putExtra("outputY", outputY);
		intent.putExtra("outputFormat", "JPEG");
		intent.putExtra("noFaceDetection", true);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, requestCode);
	}
	
	@Override
	public void onDestroy()
	{
		// TODO Auto-generated method stub
		super.onDestroy();
	}
}
