package com.netlab.loveofmum;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.netlab.loveofmum.DialogEnsure;
import com.netlab.loveofmum.R;
import com.netlab.loveofmum.api.BaseActivity;



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
import org.json.JSONObject;
import org.kobjects.base64.Base64;

import android.*;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Editable;
import android.text.Html;
import android.text.InputFilter;
import android.text.Selection;
import android.text.Spanned;
import android.text.format.DateFormat;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.netlab.loveofmum.api.BaseActivity;
import com.netlab.loveofmum.api.ImageTools;
import com.netlab.loveofmum.api.JsonAsyncTaskOnComplete;
import com.netlab.loveofmum.api.JsonAsyncTask_Info;
import com.netlab.loveofmum.api.LocalAccessor;
import com.netlab.loveofmum.api.MMloveConstants;
import com.netlab.loveofmum.api.MyApplication;
import com.netlab.loveofmum.huanxin.MainChatActivity;
import com.netlab.loveofmum.model.PictureItem;
import com.netlab.loveofmum.model.User;
import com.netlab.loveofmum.model.Yuchan;
import com.netlab.loveofmum.myadapter.FeedLastAdapter;
import com.netlab.loveofmum.myadapter.MyCHK_OrderAdd_Adapter;
import com.netlab.loveofmum.utils.IOUtils;
import com.netlab.loveofmum.utils.ImageUtil;
import com.netlab.loveofmum.utils.PermissionsChecker;
import com.netlab.loveofmum.utils.SystemStatusManager;
import com.netlab.loveofmum.widget.CustomProgressDialog;
import com.netlab.loveofmum.widget.GridViewForScrollView;
import com.umeng.analytics.MobclickAgent;
import com.netlab.loveofmum.myadapter.EditPhotoUploadAdapter;


public class MyCHK_OrderAdd extends BaseActivity implements OnClickListener
{

	private TextView txtHead;
	private ImageView imgBack;

	private List<PictureItem> arrayList=new ArrayList<PictureItem>();
	private List<Map<String, Object>> arrayList2;
	
	
	private TextView txt003;
	private TextView tvyear;
//	private TextView imgbtn;
	private TextView tvyun;
	private TextView tv_year1;
	private TextView tv_day1;
	private TextView tvyun1;

	private EditText edt001;
	private Button btn001;
	
	private GridViewForScrollView gridview;

	private TextView txt005;

	private String CHK_Time;
	private String CHK_Talk;
	private String CHK_Hospital;
	private String CHK_Doctor;
	private String CHKTypeItems = "";
	private String OrderID;
   private String Add="";
	private String YunWeek;
	private User user;
	private String mmsay="";
	private Boolean isUpload = false;
	
	private ScrollView sv;
	private CustomProgressDialog progressDialog = null;
	//private ImageLoader imageLoader;

	// 定义Web Service相关的字符串
	private final String SOAP_NAMESPACE = MMloveConstants.URL001;
	private final String SOAP_ACTION = MMloveConstants.URL001 + MMloveConstants.OrderInsert;
	private final String SOAP_METHODNAME = MMloveConstants.OrderInsert;
	private final String SOAP_URL = MMloveConstants.URL002;

	private final String SOAP_ACTION2 = MMloveConstants.URL001
			+ MMloveConstants.UploadUserPic;
	private final String SOAP_METHODNAME2 = MMloveConstants.UploadUserPic;
	
	private final String SOAP_ACTION3 = MMloveConstants.URL001
			+ MMloveConstants.UserPicInquiry;
	private final String SOAP_METHODNAME3 = MMloveConstants.UserPicInquiry;

	private static final int TAKE_PICTURE = 0;
	private static final int CHOOSE_PICTURE = 1;
	private static final int CROP = 2;
	private static final int CROP_PICTURE = 3;

	private static final int SCALE = 5;// 照片缩小比例
	private ImageView iv_image = null;

	private byte[] mContent;
	
	private LinearLayout ll_mmtalk,llchecktime;
	private String reason,gotime,page,ID,HospitalID,HospitalName,DoctorName,time;
	private LinearLayout linear02_myorderdetail,llguahao;
	private Intent mIntent;
	private TextView tvhosname,doctorname,yuyuetime,yuyueitem,yuyuegotime;
	private final String SOAP_ACTIONSEARCH = MMloveConstants.URL001
			+ MMloveConstants.OrderInquiry;
	private final String SOAP_METHODNAMESEARCH = MMloveConstants.OrderInquiry;
	private final String SOAP_ACTIONITEM = MMloveConstants.URL001
			+ MMloveConstants.OrderItemInquiry;
	private final String SOAP_METHODNAMEITEM = MMloveConstants.OrderItemInquiry;
	private final String SOAP_ACTIONUPATE = MMloveConstants.URL001
			+ MMloveConstants.OrderUpdate;
	private final String SOAP_METHODNAMEUPDATE=MMloveConstants.OrderUpdate;
	private TextView tvok;
	private TextView tvphoto;
	private TextView tvalbum;
	private EditPhotoUploadAdapter mAdapter1;
	int REQUEST_CODE;
	private AlertDialog.Builder builder;
	AlertDialog dialog;
	private static final int ADAPTER1 = 1;
	private static final String PACKAGE_URL_SCHEME = "package:";
	private PermissionsChecker mPermissionsChecker; // 权限检测器
	// 所需的全部权限
	static final String[] PERMISSIONS = new String[]{

			android.Manifest.permission.WRITE_EXTERNAL_STORAGE
	};
	 boolean crop;
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {

				case 0:
//					MyCHK_OrderAdd_Adapter adapter = new MyCHK_OrderAdd_Adapter(MyCHK_OrderAdd.this, arrayList);
					mAdapter1.setList(arrayList);
					mAdapter1.notifyDataSetChanged();
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
		setContentView(R.layout.layout_my_orderadd);

		MyApplication.getInstance().addActivity(this);
		mPermissionsChecker = new PermissionsChecker(this);
		iniView();
		gridview.setAdapter(mAdapter1);
//		gridview.setOnItemClickListener(new MyOnItemClickListener(ADAPTER1));

		//iniYunWeek();
		
		mIntent=this.getIntent();
		tv_year1.setVisibility(View.GONE);
		tv_day1.setVisibility(View.GONE);
		tvyun1.setVisibility(View.GONE);


		if(mIntent!=null&&mIntent.getStringExtra("Reason")!=null){
			
			reason=mIntent.getStringExtra("Reason");
			if(!"".equals(reason)){
			gotime=mIntent.getStringExtra("Gotime");
			llguahao.setVisibility(View.GONE);
			linear02_myorderdetail.setVisibility(View.GONE);
			txtHead.setText("我的足迹");
//				btn001.setBackgroundResource(R.drawable.home_btn_green);
			edt001.setText(reason);
				String timeYear="";

					timeYear=IOUtils.StringPattern(gotime,"yyyy-MM-dd","yyyy.MM.dd");


				tvyear.setText(timeYear.substring(0,4));

				txt003.setText(timeYear.substring(5,timeYear.length()));
				tvyun.setText(mIntent.getStringExtra("TitleName"));
			OrderID=mIntent.getStringExtra("Orderid");
			btn001.setVisibility(View.VISIBLE);
//			edt001.setKeyListener(null);
			Editable etext = edt001.getText();
			edt001.setMovementMethod(ScrollingMovementMethod.getInstance());
			edt001.setBackgroundDrawable(null);
				Selection.setSelection(etext, etext.length());
//			edt001.setFilters(new InputFilter[] { new InputFilter() {  
//			  
//
//				@Override
//				public CharSequence filter(CharSequence source, int start,
//						int end, Spanned dest, int dstart, int dend) {
//					return source.length() < 1 ? dest.subSequence(dstart, dend) : "";  
//				}  
//			}   
//			});
			if(OrderID.equals("null")){
				OrderID = getOrderID();

			}else{
			searchPictureList();
			}
			}
//			 searchOrderDetail();
		}
		else if(mIntent!=null&&mIntent.getStringExtra("page")!=null&&mIntent.getStringExtra("ID")!=null){
			page=mIntent.getStringExtra("page");
//			if(page.equals("User_Foot")){
//				txtHead.setText("我的足迹");
//				llguahao.setVisibility(View.VISIBLE);
//				linear02_myorderdetail.setVisibility(View.VISIBLE);
//				HospitalID=mIntent.getStringExtra("HospitalID");
//				HospitalName=mIntent.getStringExtra("HospitalName");
//				DoctorName=mIntent.getStringExtra("DoctorName");
//				time=mIntent.getStringExtra("Gotime");
//				ID = this.getIntent().getExtras().getString("ID").toString();
//				tvhosname.setText(HospitalName);
//				doctorname.setText(DoctorName);
//				yuyuetime.setText(time);
//				yuyuegotime.setText(time);
//				 searchOrderDetail();
////				 OrderID = getOrderID();
//			}
			 if(mIntent!=null&&mIntent.getStringExtra("page")!=null&&mIntent.getStringExtra("MomSay")!=null){
				page=mIntent.getStringExtra("page");
				if(page.equals("User_Foot")){
					txtHead.setText("添加足迹");
					llguahao.setVisibility(View.VISIBLE);
					llchecktime.setVisibility(View.GONE);
					tv_year1.setVisibility(View.VISIBLE);
					tv_day1.setVisibility(View.VISIBLE);
					tvyun1.setVisibility(View.VISIBLE);
					linear02_myorderdetail.setVisibility(View.VISIBLE);
					HospitalID=mIntent.getStringExtra("HospitalID");
					HospitalName=mIntent.getStringExtra("HospitalName");
					DoctorName=mIntent.getStringExtra("DoctorName");
					time=mIntent.getStringExtra("Gotime");

					ID = this.getIntent().getExtras().getString("ID").toString();
					//btn001.setBackgroundResource(R.drawable.home_btn_green);
					tvhosname.setText(HospitalName);
					doctorname.setText(DoctorName);
					yuyuetime.setText(time);
					yuyuegotime.setText(time);

					String timeYear=mIntent.getStringExtra("Yeartime").split(" ")[0].replace("/", "-");

					timeYear=IOUtils.StringPattern(timeYear,"yyyy-MM-dd","yyyy.MM.dd");


					tv_year1.setText(timeYear.substring(0,4));

					tv_day1.setText(timeYear.substring(4,timeYear.length()));
					tvyun1.setText(mIntent.getStringExtra("TitleName"));

					mmsay=mIntent.getStringExtra("MomSay");
					 searchOrderDetail();
					 if(!mmsay.equals("")){
							edt001.setText(mmsay);
							edt001.setKeyListener(null);
						
							Editable etext = edt001.getText();
							edt001.setMovementMethod(ScrollingMovementMethod.getInstance());
							edt001.setBackgroundDrawable(null);
						Selection.setSelection(etext, 0);
						
						}

					 
				}
				else{
					llchecktime.setVisibility(View.GONE
							);
					txtHead.setText("写心情");
					llguahao.setVisibility(View.VISIBLE);
					linear02_myorderdetail.setVisibility(View.VISIBLE);
					HospitalID=mIntent.getStringExtra("HospitalID");
					HospitalName=mIntent.getStringExtra("HospitalName");
					DoctorName=mIntent.getStringExtra("DoctorName");
					time=mIntent.getStringExtra("Gotime");

					btn001.setBackgroundResource(R.drawable.home_btn_bg4);
					ID = this.getIntent().getExtras().getString("ID").toString();
					mmsay=mIntent.getStringExtra("MomSay");
					if(!mmsay.equals("")){
						edt001.setText(mmsay);
						edt001.setKeyListener(null);
						Editable etext = edt001.getText();
						Selection.setSelection(etext, 0);

						edt001.setBackgroundDrawable(null);
					}
				
					tvhosname.setText(HospitalName);
					doctorname.setText(DoctorName);
					yuyuetime.setText(time);
					yuyuegotime.setText(time);
				
					 searchOrderDetail();
					 
				}
			}
//			else if(page.equals("User_Order")){
////				txtHead.setText("我的订单");
////				HospitalID=mIntent.getStringExtra("HospitalID");
////				HospitalName=mIntent.getStringExtra("HospitalName");
////				DoctorName=mIntent.getStringExtra("DoctorName");
////				time=mIntent.getStringExtra("Gotime");
////				ID = this.getIntent().getExtras().getString("ID").toString();
////				tvhosname.setText(HospitalName);
////				doctorname.setText(DoctorName);
////				yuyuetime.setText(time);
////				yuyuegotime.setText(time);
////				 searchOrderDetail();
////				 searchOrderItems();
//				llchecktime.setVisibility(View.GONE);
//				txtHead.setText("写心情");
//				llguahao.setVisibility(View.VISIBLE);
//				linear02_myorderdetail.setVisibility(View.VISIBLE);
//				HospitalID=mIntent.getStringExtra("HospitalID");
//				HospitalName=mIntent.getStringExtra("HospitalName");
//				DoctorName=mIntent.getStringExtra("DoctorName");
//				time=mIntent.getStringExtra("Gotime");
//				ID = this.getIntent().getExtras().getString("ID").toString();
//				mmsay=mIntent.getStringExtra("MomSay");
//				if(!mmsay.equals("")){
//					edt001.setText(mmsay);
//					Editable etext = edt001.getText();
//					Selection.setSelection(etext, etext.length());
//				}
//			
//				tvhosname.setText(HospitalName);
//				doctorname.setText(DoctorName);
//				yuyuetime.setText(time);
//				yuyuegotime.setText(time);
//				 searchOrderDetail();
//				 
//			}
		}else if(mIntent!=null&&mIntent.getStringExtra("Add_food")!=null){
			Add=mIntent.getStringExtra("Add_food");
			llguahao.setVisibility(View.GONE);
			OrderID=getOrderID();
			PictureItem mItem = new PictureItem();
			mItem.setAdd(true);
			arrayList.add(mItem);
			iniYunWeekAdd();

			Message message = Message.obtain();
			message.what = 0;
			mHandler.sendMessage(message);
			linear02_myorderdetail.setVisibility(View.GONE);
		}else{
			llguahao.setVisibility(View.GONE);
			linear02_myorderdetail.setVisibility(View.GONE);
			OrderID=getOrderID();
		}
		
		setListeners();
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onRestart();
		MobclickAgent.onResume(this);
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPause(this);
	}
	private void startProgressDialog(){
		if (progressDialog == null){
			progressDialog = CustomProgressDialog.createDialog(this);
			progressDialog.setMessage("正在加载中...");
		}

		progressDialog.show();
	}

	private void stopProgressDialog(){
		if (progressDialog != null){
			progressDialog.dismiss();
			progressDialog = null;
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
					if (result == null)
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
								linear02_myorderdetail.setVisibility(View.GONE);
							}
							else
							{
								linear02_myorderdetail.setVisibility(View.VISIBLE);
								for (int i = 0; i < array.length(); i++)
								{
									CHKTypeItems += array.getJSONObject(i)
											.getString("ItemName")+";";
								}
								yuyueitem.setText(CHKTypeItems);
							}
							if(hasInternetConnected())
							{
								//imageLoader = ImageLoader.getInstance();
								//imageLoader.init(ImageLoaderConfiguration.createDefault(MyCHK_OrderAdd.this));
								searchPictureList();
							}
							else
							{
								Toast.makeText(MyCHK_OrderAdd.this, R.string.msgUninternet,
										Toast.LENGTH_SHORT).show();
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
					MyCHK_OrderAdd.this, true, doProcess);
			Map<String, Object> paramMap = new HashMap<String, Object>();
			String str = "<Request><OrderID>%s</OrderID><HospitalID>%s</HospitalID></Request>";

			str = String.format(str, new Object[]
			{ OrderID,HospitalID });
			paramMap.put("str", str);

			// 必须是这5个参数，而且得按照顺序
			task.execute(SOAP_NAMESPACE, SOAP_ACTIONITEM, SOAP_METHODNAMEITEM,
					SOAP_URL, paramMap);
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void searchOrderDetail()
	{
		try
		{
			JsonAsyncTaskOnComplete doProcess = new JsonAsyncTaskOnComplete()
			{
				public void processJsonObject(Object result)
				{
//					{"OrderInquiry":[{"MomSay":"","OrderStatus":"02","YunWeek":"20","OrderID":"20151222034806-50728","UserID":"50728","CHKType":"第(14-16周)产检","RegisterMoney":"5","ID":"1767","CHKID":"34","PayType":"01","Reason":"成功","OrderStatusName":"待检查","UpdateDBy":"","DeptDesc":"","DoctorName":"周仲元","PayStatusName":"未付款","IsUpload":"1","HospitalName":"郑州大学第三附属医院","PayStatus":"01","BeginTime":"17:00","UpdatedDate":"2015\/12\/24 13:56:09","HospitalID":"1","CreatedDate":"2015\/12\/22 15:48:06","CreatedBy":"","EndTime":"18:00","Gotime":"2015\/12\/25 0:00:00"}]}
					if (result == null)
					{

					}
					else
					{
						Map<String, Object> map;
						arrayList2 = new ArrayList<Map<String, Object>>();
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
									
								}
								searchOrderItems();
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
					MyCHK_OrderAdd.this, true, doProcess);
			Map<String, Object> paramMap = new HashMap<String, Object>();
			String str = "<Request><UserID>%s</UserID><UserMobile>%s</UserMobile><CHKID>%s</CHKID><ID>%s</ID><HospitalID>%s</HospitalID><Flag>%s</Flag></Request>";
			str = String.format(str, new Object[]
			{String.valueOf(user.UserID),"","",ID,HospitalID,"0"});
			paramMap.put("str", str);

			// 必须是这5个参数，而且得按照顺序
			task.execute(SOAP_NAMESPACE, SOAP_ACTIONSEARCH, SOAP_METHODNAMESEARCH,
					SOAP_URL, paramMap);
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	protected void updateOrder(String OrderID,String MomSay) {
		// TODO Auto-generated method stub
		
			JsonAsyncTaskOnComplete	doProcess = new  JsonAsyncTaskOnComplete(){

				@Override
				public void processJsonObject(Object result) {
					// TODO Auto-generated method stub
//				{"OrderUpdate":[{"MessageContent":"成功！","MessageCode":"0"}]}
					try {
						btn001.setEnabled(true);
						String res=result.toString();
						JSONObject mySO = (JSONObject) result;
//						Log.i("resssss",res);
						JSONArray array = mySO
								.getJSONArray("OrderUpdate");
						if(array.getJSONObject(0).has("MessageCode")) {

							if (array.getJSONObject(0).getString("MessageCode")
									.equals("0")) {
								if(array.getJSONObject(0).has("PointAddCode")) {
									if(array.getJSONObject(0).getString("PointAddCode").equals("0")) {

										Intent i = new Intent(MyCHK_OrderAdd.this, DialogEnsure.class);
										i.putExtra("content", array.getJSONObject(0).getString("PointAddContent") + "+" + array.getJSONObject(0).getString("PointValue"));
										startActivity(i);

									}
								}else{
									Toast.makeText(MyCHK_OrderAdd.this,array.getJSONObject(0).getString("MessageContent"),1).show();
								}
								finish();
							}
						}
//						if("0".equals(array.getJSONObject(0).getString("MessageCode"))){
//							Toast.makeText(MyCHK_OrderDetail.this, "取消订单成功！", 1).show();
//						}


					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						btn001.setEnabled(true);
					}
					
				}};
		
		JsonAsyncTask_Info task = new JsonAsyncTask_Info(
				MyCHK_OrderAdd.this, true, doProcess);
		Map<String, Object> paramMap = new HashMap<String, Object>();
		String str = "<Request><OrderID>%s</OrderID><Reason>%s</Reason><MomSay>%s</MomSay></Request>";

		str = String.format(str, new Object[]
		{ OrderID,MomSay,MomSay});
		paramMap.put("str", str);

		// 必须是这5个参数，而且得按照顺序
		task.execute(SOAP_NAMESPACE, SOAP_ACTIONUPATE, SOAP_METHODNAMEUPDATE,
				SOAP_URL, paramMap);
	}
	@Override
	protected void iniView()
	{
		// TODO Auto-generated method stub
		txtHead = (TextView) findViewById(R.id.txtHead);
		imgBack = (ImageView) findViewById(R.id.img_left);
		txtHead.setText("添加足迹");

		
		txt003 = (TextView) findViewById(R.id.weekly_in_spection03);
		// txt004 = (TextView)findViewById(R.id.weekly_003);
//		imgbtn = (TextView) findViewById(R.id.weekly_in_spection04);
		tvyear=(TextView)findViewById(R.id.tv_year);
		user = LocalAccessor.getInstance(MyCHK_OrderAdd.this).getUser();
		tvyun=(TextView)findViewById(R.id.tv_yun);
		edt001 = (EditText) findViewById(R.id.weekly_in_spection06);
		llchecktime=(LinearLayout)findViewById(R.id.llchecktime);
		btn001 = (Button) findViewById(R.id.weekly_in_spection05);
		tvhosname=(TextView) findViewById(R.id.weekly_001);
		doctorname = (TextView)findViewById(R.id.weekly_002);
		yuyuetime=(TextView) findViewById(R.id.weekly_time);
		yuyueitem=(TextView) findViewById(R.id.txt006_my_orderdetail);
		yuyuegotime=(TextView)findViewById(R.id.txt007_my_orderdetail);
		linear02_myorderdetail=(LinearLayout) findViewById(R.id.linear02_myorderdetail);
		tvyun=(TextView)findViewById(R.id.tv_yun);
		//iv_image = (ImageView) findViewById(R.id.weekly_in_spection08);
		tv_year1=(TextView)findViewById(R.id.tv_year1);
		tv_day1=(TextView)findViewById(R.id.tv_day1);
		tvyun1=(TextView)findViewById(R.id.tv_yun1);
		gridview = (GridViewForScrollView)findViewById(R.id.grid_myorderadd);
		ll_mmtalk=(LinearLayout) findViewById(R.id.ll_mmtalk);
		llguahao=(LinearLayout) findViewById(R.id.llguahao);
		
		sv = (ScrollView) findViewById(R.id.sv_orderadd);
		mAdapter1 = new EditPhotoUploadAdapter(this);
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
		

//		imgbtn.setOnClickListener(new OnClickListener()
//		{
//
//			@Override
//			public void onClick(View v)
//			{
//				// TODO Auto-generated method stub
//				if(hasInternetConnected())
//				{
//					showPicturePicker(MyCHK_OrderAdd.this, false);
//				}
//				else
//				{
//					Toast.makeText(MyCHK_OrderAdd.this, R.string.msgUninternet,
//							Toast.LENGTH_SHORT).show();
//				}
//			}
//		});

		btn001.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
//				if(txtHead.equals("写心情")){
//					String mmsay=edt001.getText().toString().trim();
//					if(mmsay.length()>0){
//					updateOrder(OrderID, mmsay);
//					}else{
//						Toast.makeText(MyCHK_OrderAdd.this, "请填写妈妈想说",
//								Toast.LENGTH_SHORT).show();
//					}
//				}else{

				
				if(!txtHead.equals("写心情")&&mIntent.getStringExtra("Add_food")!=null&&Add.equals("Add")){
				
					if (txt003.getText().toString().equals(""))
					{
//						Toast.makeText(MyCHK_OrderAdd.this,
//								R.string.str001_mychk_orderadd, Toast.LENGTH_SHORT)
//								.show();
						Toast.makeText(MyCHK_OrderAdd.this,
								"请选择产检时间", Toast.LENGTH_SHORT)
								.show();
						return;
					}
					else
					{
						CHK_Time = tvyear.getText().toString()+"-"+txt003.getText().toString().replace(".","-");
					}
					if(edt001.getText().toString().trim().equals(""))
					{
						Toast.makeText(MyCHK_OrderAdd.this,
								"请填写妈妈想说", Toast.LENGTH_SHORT)
								.show();
						return;
					}
				}
			
				String inputName = Html.toHtml(edt001.getText());
				if(inputName.equals("")&&arrayList.size()==0)
				{
					Toast.makeText(MyCHK_OrderAdd.this,
							"请填写妈妈想说或上传检查单", Toast.LENGTH_SHORT)
							.show();
					return;
				}
				else
				{
					
					if(inputName.contains("\"ltr\""))
					{
						
					}
					else
					{
						inputName = inputName.replace("ltr", "\"ltr\"");
					}
				
					CHK_Talk = inputName.replaceAll("\\&[a-zA-Z]{0,9};", "").replaceAll("<[^>]*>", "\n\t");
				}
//				if(!isUpload)
//				{
//					Toast.makeText(MyCHK_OrderAdd.this,
//							R.string.str002_mychk_orderadd, Toast.LENGTH_SHORT)
//							.show();
//					return;
//				}
				
				
				


				
				
				if(hasInternetConnected())
				{
					btn001.setEnabled(false);

				    if(txtHead.getText().toString().equals("写心情")){

							updateOrder(OrderID, CHK_Talk);

				    }else if(txtHead.getText().toString().equals("我的足迹")){

							updateOrder(OrderID, CHK_Talk);

				    }else{

						InsertOrder();

				    
				    }
				}
				else
				{
					Toast.makeText(MyCHK_OrderAdd.this, R.string.msgUninternet,
							Toast.LENGTH_SHORT).show();
				}
			}
			/*Intent i = new Intent(MyCHK_OrderAdd.this,
					MyCHK_Tim.class);
			

			startActivity();
			finish();*/
		});

		txt003.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				final Calendar c = Calendar.getInstance();
				
				DatePickerDialog dialog = new DatePickerDialog(
						MyCHK_OrderAdd.this,
						new DatePickerDialog.OnDateSetListener()
						{
							@Override
							public void onDateSet(DatePicker view, int year,
									int monthOfYear, int dayOfMonth)
							{
								// TODO Auto-generated method stub
								c.set(year, monthOfYear, dayOfMonth);
								
								SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
								try
								{
									if(IOUtils.IsBeyondTimeSet2(c.getTime(),IOUtils.YunStart(fmt.parse(user.YuBirthDate)),new Date()))
									{
										Toast.makeText(MyCHK_OrderAdd.this, "请设置正确的产检时间！",
												Toast.LENGTH_SHORT).show();
									}
									else
									{
										txt003.setText(DateFormat.format("MM.dd",c));
										tvyear.setText(year+"");
										String day=year+"-"+(monthOfYear+1)+"-"+dayOfMonth;
										Date date = fmt.parse(user.YuBirthDate);
										Yuchan yu = new Yuchan();
										yu = IOUtils.WeekInfo2(date,fmt.parse(day));
										if(yu.Week>39){
											tvyun.setText("孕"+40+"周");
										}else
										tvyun.setText("孕"+yu.Week+"周");

									}
								}
								catch (ParseException e)
								{
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c
								.get(Calendar.DAY_OF_MONTH));
				dialog.show();
			}
		});
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
					if (result == null)
					{

					}
					else
					{
						Map<String, Object> map;
						// 下边是具体的处理
						try
						{
							if(arrayList.size()>0) {
								arrayList.clear();

							}

							JSONObject mySO = (JSONObject) result;
							JSONArray array = mySO
									.getJSONArray("UserPicInquiry");
							if(array.getJSONObject(0).has("MessageCode")){
								PictureItem mItem = new PictureItem();
								mItem.setAdd(true);
								arrayList.add(mItem);
								Message message = Message.obtain();
								message.what = 0;
								mHandler.sendMessage(message);
							}
							else{
							for (int i = 0; i < array.length(); i++)
							{

//								map = new HashMap<String, Object>();
//								map.put("PicURL",
//										array.getJSONObject(i).getString("PicURL"));
								PictureItem item=new PictureItem();
								item.setPath(MMloveConstants.JE_BASE_URL2 + array.getJSONObject(i).getString("PicURL").toString().replace("~", "").replace("\\", "/"));
								item.setAdd(false);
								arrayList.add(item);


							}
								PictureItem mItem = new PictureItem();
								mItem.setAdd(true);
								arrayList.add(mItem);

//							if (arrayList.size()>0) {
//							imgbtn.setText("继续添加图片");
//
//							} else {
//
//							}
								Message message = Message.obtain();
								message.what = 0;
								mHandler.sendMessage(message);


						
							// 添加listView点击事件
							
						}
						}
						catch (Exception ex)
						{
							ex.printStackTrace();
						}
					}
				}
			};

			JsonAsyncTask_Info task = new JsonAsyncTask_Info(MyCHK_OrderAdd.this,
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


	private void iniYunWeekAdd()
	{

		SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
		Date date;
		try

		{
			String timeYearAndDay=fmt.format(new Date());
			tvyear.setText(timeYearAndDay.split("-")[0]);
			txt003.setText(timeYearAndDay.split("-")[1]+"."+timeYearAndDay.split("-")[2]);

			CHK_Time=timeYearAndDay;
			user=LocalAccessor.getInstance(MyCHK_OrderAdd.this).getUser();
			date = fmt.parse(user.YuBirthDate);
			Yuchan yu = new Yuchan();
			yu = IOUtils.WeekInfo2(date,fmt.parse(CHK_Time));
			if(yu.Week>40){
				YunWeek = 40+"";
			}else{
				YunWeek = String.valueOf(yu.Week+1);
			}

			tvyun.setText("孕"+YunWeek+"周");



		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void InsertOrder()
	{
		try
		{
			JsonAsyncTaskOnComplete doProcess = new JsonAsyncTaskOnComplete()
			{
				public void processJsonObject(Object result)
				{
					if (result == null)
					{

					}
					else
					{
						Map<String, Object> map;
						// 下边是具体的处理
						try
						{
							JSONObject mySO = (JSONObject) result;
							btn001.setEnabled(true);
							JSONArray array = mySO.getJSONArray("OrderInsert");
							if(array.getJSONObject(0).has("MessageCode")) {

								if (array.getJSONObject(0).getString("MessageCode")
										.equals("0")) {
									if(array.getJSONObject(0).has("PointAddCode")) {
										if(array.getJSONObject(0).getString("PointAddCode").equals("0")) {

											Intent i = new Intent(MyCHK_OrderAdd.this, DialogEnsure.class);
											i.putExtra("content", array.getJSONObject(0).getString("PointAddContent") + "+" + array.getJSONObject(0).getString("PointValue"));
											startActivity(i);
										}
									}
									finish();
								}
							}else{
								Toast.makeText(MyCHK_OrderAdd.this,array.getJSONObject(0).getString("MessageContent"),Toast.LENGTH_SHORT).show();
							}

						}
						catch (Exception ex)
						{
							ex.printStackTrace();
							btn001.setEnabled(true);
						}
					}
				}
			};

			JsonAsyncTask_Info task = new JsonAsyncTask_Info(
					MyCHK_OrderAdd.this, true, doProcess);
			Map<String, Object> paramMap = new HashMap<String, Object>();
			String str = "<Request><UserID>%s</UserID><OrderStatus>%s</OrderStatus><PayType>%s</PayType><PayStatus>%s</PayStatus><HospitalID>%s</HospitalID><HospitalName>%s</HospitalName><DeptRowID>%s</DeptRowID><DeptDesc>%s</DeptDesc><DoctorID>%s</DoctorID><DoctorName>%s</DoctorName><Gotime>%s</Gotime><BeginTime>%s</BeginTime><EndTime>%s</EndTime><RegisterMoney>%s</RegisterMoney><CHKID>%s</CHKID><CHKType>%s</CHKType><YunWeek>%s</YunWeek><Reason>%s</Reason><MomSay>%s</MomSay><OrderID>%s</OrderID><IsUpload>%s</IsUpload><Items>%s</Items><PhoneType>%s</PhoneType><SchemaID>%s</SchemaID></Request>";

			String fee = "0";
			
			str = String.format(str, new Object[]
					{ String.valueOf(user.UserID), "04", "01", "02", "1", CHK_Hospital,
							"", "", "", CHK_Doctor, CHK_Time, "", "", fee, "", "",
							YunWeek, CHK_Talk,CHK_Talk,OrderID,"1", "","0","" });
		

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
//	public void ExitChatDialog(final Context context){
//		LayoutInflater inflater = LayoutInflater.from(this);
//		View layout=inflater.inflate(R.layout.dialog_default_ensure,null);
//
//		AlertDialog.Builder builder =new AlertDialog.Builder(MyCHK_OrderAdd.this);
//		builder.setView(layout);
//		builder.setCancelable(false);
//		builder.create().show();
//		TextView tvok=(TextView) layout.findViewById(R.id.dialog_default_click_ensure);
//		TextView tvcontent=(TextView) layout.findViewById(R.id.dialog_default_click_text_msg);
//		tvcontent.setText("恭喜您添加妈妈足迹成功，奖励10积分！");
//		tvok.setOnClickListener(new OnClickListener(){
//		  public void onClick(View v) {
//			  finish();
//		    }
//		  });
//
//		 }
	private void uploadBitmap(final Bitmap newBitmap){
		startProgressDialog();
		new Thread(){
			@Override
			public void run() {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			newBitmap.compress(Bitmap.CompressFormat.JPEG,100, baos);
			mContent = baos.toByteArray();
			final String uploadBuffer = new String(Base64.encode(mContent));
			MyCHK_OrderAdd.this.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					UploadPicture(uploadBuffer);
				}
			});
			}
		}.start();
	}
	private void UploadPicture(String uploadBuffer)
	{
		try
		{

			JsonAsyncTaskOnComplete doProcess = new JsonAsyncTaskOnComplete()
			{
				public void processJsonObject(Object result)
				{
					System.gc();
					if (result == null)
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
//							Toast.makeText(MyCHK_OrderAdd.this,returnvalue001,1).show();
							stopProgressDialog();

							if (array.getJSONObject(0).getString("MessageCode")
									.equals("0"))
							{
//								Toast.makeText(MyCHK_OrderAdd.this, "上传成功",
//										Toast.LENGTH_SHORT).show();
								isUpload = true;
								searchPictureList();
										if(array.getJSONObject(0).has("PointAddCode")) {
											if(array.getJSONObject(0).getString("PointAddCode").equals("0")) {
												Intent i = new Intent(MyCHK_OrderAdd.this, DialogEnsure.class);
												i.putExtra("content", array.getJSONObject(0).getString("PointAddContent") + "+" + array.getJSONObject(0).getString("PointValue"));
												startActivity(i);
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

			JsonAsyncTask_Info task = new JsonAsyncTask_Info(
					MyCHK_OrderAdd.this, true, doProcess);
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("UserID", String.valueOf(user.UserID));
			paramMap.put("fileName", getPhotoFileName());
			paramMap.put("img", uploadBuffer);
			paramMap.put("OrderID", OrderID);

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
					Bitmap bitmap,newBitmap;
					 if (Build.MANUFACTURER.equalsIgnoreCase("Xiaomi")) {
						 newBitmap = BitmapFactory.decodeFile(ImageUtil.doPicture(Environment
									.getExternalStorageDirectory() + "/image.jpg"));
					 }else{
						 bitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + "/image.jpg");
						 newBitmap = ImageTools.zoomBitmap(bitmap,
								 bitmap.getWidth() / SCALE, bitmap.getHeight()
										 / SCALE);
						 // 由于Bitmap内存占用较大，这里需要回收内存，否则会报out of memory异常
						 bitmap.recycle();
					 }
			
						
//			        } else{
//			        	bitmap = BitmapFactory.decodeFile(Environment
//								.getExternalStorageDirectory() + "/image.jpg");
//			        	Toast.makeText(this, Build.MANUFACTURER, 1).show();
//			        }


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
					uploadBitmap(newBitmap);
					System.gc();
					break;

				case CHOOSE_PICTURE:
					ContentResolver resolver = getContentResolver();
					// 照片的原始资源地址
					Uri originalUri = data.getData();
					
					try
					{
						  long mid =ImageUtil.getAutoFileOrFilesSize(ImageUtil.getImageAbsolutePath(MyCHK_OrderAdd.this,originalUri));
			                 if(mid>6*1024*1024){
			                	 Toast.makeText(MyCHK_OrderAdd.this, "图片尺寸过大", 1).show();
			                	 return;
			                 }
						// 使用ContentProvider通过URI获取原始图片

//						Bitmap photo = MediaStore.Images.Media.getBitmap(
//								resolver, originalUri);
						Bitmap photo  = BitmapFactory.decodeFile(ImageUtil.doPicture(ImageUtil.getImageAbsolutePath(MyCHK_OrderAdd.this, originalUri)));
						ExifInterface exif = null;
						
//						File fileHandle = new File(ImageUtil.getUriString(originalUri, resolver));     
//				        long length = fileHandle.length();
//		                 ByteArrayOutputStream baosChoosepic = new ByteArrayOutputStream();
//		                 photo.compress(Bitmap.CompressFormat.JPEG, 100, baosChoosepic);
//		                 byte[] b = baosChoosepic.toByteArray();
//		                 //将字节换成mb
		               
		                
		              
						if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.KITKAT)
						{
							exif = new ExifInterface(IOUtils.getPath(MyCHK_OrderAdd.this, originalUri));
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
//							Bitmap smallBitmap = ImageTools.zoomBitmap(photo,
//									photo.getWidth() / SCALE, photo.getHeight()
//											/ SCALE);
//							 释放原始图片占用的内存，防止out of memory异常发生
//							photo.recycle();
							if(degree!=0)
							{
								photo = rotateImage(photo,degree);
							}
							//iv_image.setImageBitmap(smallBitmap);

//							ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
//							photo.compress(Bitmap.CompressFormat.JPEG,
//									100, baos2);
//							mContent = baos2.toByteArray();
//
//							String uploadBuffer2 = new String(
//									Base64.encode(mContent));
//
//							UploadPicture(uploadBuffer2);
							uploadBitmap(photo);

						}
					}
					catch (FileNotFoundException e)
					{
						e.printStackTrace();
					}
					catch (Exception e)
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

	public Bitmap readBitmap(Uri selectedImage) {
		Bitmap bm = null;
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 5;
		AssetFileDescriptor fileDescriptor =null;
		try {
			fileDescriptor = this.getContentResolver().openAssetFileDescriptor(selectedImage,"r");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		finally{
			try {
				bm = BitmapFactory.decodeFileDescriptor(fileDescriptor.getFileDescriptor(), null, options);
				fileDescriptor.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return bm;
	}

	public void showPicturePicker(final Context context,boolean isCrop){
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			if (mPermissionsChecker.lacksPermissions(PERMISSIONS)) {
				showMissingPermissionDialog(context);
				return;
			}
		}

		crop = isCrop;
		LayoutInflater inflater = LayoutInflater.from(this);
		View layout=inflater.inflate(R.layout.dialog_photo,null);
		    
		builder =new AlertDialog.Builder(MyCHK_OrderAdd.this);
		builder.setView(layout);
		builder.setCancelable(false);
		dialog=builder.show();
		tvok=(TextView) layout.findViewById(R.id.dialog_default_click_ensure);
		tvphoto=(TextView) layout.findViewById(R.id.dialog_phtoto_tv);
		tvalbum=(TextView) layout.findViewById(R.id.dialog_album);
		tvok.setOnClickListener(this);
		tvphoto.setOnClickListener(this);
		tvalbum.setOnClickListener(this);
		 }
	public void showMissingPermissionDialog(final Context context){
		LayoutInflater inflater = LayoutInflater.from(this);
		View layout=inflater.inflate(R.layout.dialog_default_ensure,null);

		AlertDialog.Builder builder =new AlertDialog.Builder(context);
		builder.setView(layout);
		builder.setCancelable(false);
		builder.create().show();
		TextView tvok=(TextView) layout.findViewById(R.id.dialog_default_click_ensure);
		TextView tvcontent=(TextView) layout.findViewById(R.id.dialog_default_click_text_msg);
		tvcontent.setText(" 请点击设置，打开所需存储权限");
		tvok.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
				intent.setData(Uri.parse(PACKAGE_URL_SCHEME + getPackageName()));
				startActivity(intent);
				finish();
			}
		});

	}
//	public void showPicturePicker(Context context, boolean isCrop)
//	{
//		final boolean crop = isCrop;
////		AlertDialog.Builder builder = new AlertDialog.Builder(context);
////		builder.setTitle("图片来源");
////		builder.setNegativeButton("取消", null);
////		builder.setItems(new Object[]
////		{ "拍照", "相册" }, new DialogInterface.OnClickListener()
////		{
////			// 类型码
//			int REQUEST_CODE;
////
////			@Override
////			public void onClick(DialogInterface dialog, int which)
////			{
////				switch (which)
////				{
////					case TAKE_PICTURE:
////						Uri imageUri = null;
////						String fileName = null;
////						Intent openCameraIntent = new Intent(
////								MediaStore.ACTION_IMAGE_CAPTURE);
////						if (crop)
////						{
////							REQUEST_CODE = CROP;
////							// 删除上一次截图的临时文件
////							SharedPreferences sharedPreferences = getSharedPreferences(
////									"temp", Context.MODE_WORLD_WRITEABLE);
////							ImageTools.deletePhotoAtPathAndName(Environment
////									.getExternalStorageDirectory()
////									.getAbsolutePath(), sharedPreferences
////									.getString("tempName", ""));
////
////							// 保存本次截图临时文件名字
////							fileName = String.valueOf(System
////									.currentTimeMillis()) + ".jpg";
////							Editor editor = sharedPreferences.edit();
////							editor.putString("tempName", fileName);
////							editor.commit();
////						}
////						else
////						{
////							REQUEST_CODE = TAKE_PICTURE;
////							fileName = "image.jpg";
////						}
////						imageUri = Uri.fromFile(new File(Environment
////								.getExternalStorageDirectory(), fileName));
////						// 指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
////						openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,
////								imageUri);
////						startActivityForResult(openCameraIntent, REQUEST_CODE);
////						break;
////
////					case CHOOSE_PICTURE:
////						Intent openAlbumIntent = new Intent(
////								Intent.ACTION_GET_CONTENT);
////						if (crop)
////						{
////							REQUEST_CODE = CROP;
////						}
////						else
////						{
////							REQUEST_CODE = CHOOSE_PICTURE;
////						}
////						openAlbumIntent.setDataAndType(
////								MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
////								"image/*");
////						startActivityForResult(openAlbumIntent, REQUEST_CODE);
////						break;
////
////					default:
////						break;
////				}
////			}
////		});
////		builder.create().show();
//	}

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
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.dialog_default_click_ensure:
			if(dialog!=null){
				dialog.dismiss();
			}
		
			break;
		case R.id.dialog_phtoto_tv:
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
			if(dialog!=null){
				dialog.dismiss();
			}
			break;
		case R.id.dialog_album:
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
			if(dialog!=null){
				dialog.dismiss();
			}
			break;

		default:
			break;
		}
	}
}
