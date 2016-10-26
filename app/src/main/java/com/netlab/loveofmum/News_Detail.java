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

import android.R.integer;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.igexin.sdk.PushManager;
import com.netlab.loveofmum.activity.LoginActivity;
import com.netlab.loveofmum.api.BaseActivity;
import com.netlab.loveofmum.api.MMloveConstants;
import com.netlab.loveofmum.api.JsonAsyncTaskOnComplete;
import com.netlab.loveofmum.api.JsonAsyncTask_Info;
import com.netlab.loveofmum.api.ListItemClickHelp;
import com.netlab.loveofmum.api.LocalAccessor;
import com.netlab.loveofmum.api.MyApplication;
import com.netlab.loveofmum.model.ShareModel;
import com.netlab.loveofmum.model.User;
import com.netlab.loveofmum.model.Yuchan;
import com.netlab.loveofmum.myadapter.CHKTiemDet_newsAdapter;
import com.netlab.loveofmum.myadapter.CHKTimeDetail_TopicAdapter;
import com.netlab.loveofmum.utils.DialogUtils;
import com.netlab.loveofmum.utils.IOUtils;
import com.netlab.loveofmum.utils.ImageUtil;
import com.netlab.loveofmum.utils.LoadPictrue;
import com.netlab.loveofmum.utils.SystemStatusManager;
import com.netlab.loveofmum.widget.CustomProgressDialog;
import com.netlab.loveofmum.widget.DialogEnsureEdit;
import com.netlab.loveofmum.widget.ListViewForScrollView;
import com.netlab.loveofmum.widget.SharePopupWindow;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.umeng.analytics.MobclickAgent;
import android.text.TextWatcher;
public class News_Detail extends BaseActivity implements ListItemClickHelp
{

	private TextView txtHead;
	private ImageView imgBack;
	private ImageView imgMore;

	private List<Map<String, Object>> arrayList = new ArrayList<Map<String, Object>>();
	
	private List<Map<String, Object>> arrayList2;
	
	private User user;
	
	
	private String returnvalue001;
	private ListViewForScrollView listview;
	
	private LinearLayout liner_newdetail;
	
	private TextView txt001;
	private TextView txt002;
	
	private ImageView img001;
	private EditText edmessage;
	private CustomProgressDialog progressDialog = null;
	private ImageView ivsend;
	
//	private ImageButton imgTopicAdd;
	
	private ScrollView sv;
	
	private String PicURL;
	
	private WebView web;
	
//	private FinalBitmap fb;
	
	private String ID;

	private int Week;
	
	private String YunWeek;
	private TextView tvzannum;
	private LinearLayout llzan;
	private ImageView ivzan;
	private TextView tvnum;
	private ShareWebWindow share;
	private RelativeLayout rlshare,rlpinlun;
	private String url,title;
	
	//private ImageLoader imageLoader;
	// 定义Web Service相关的字符串
	private final String SOAP_NAMESPACE = MMloveConstants.URL001;
	private final String SOAP_ACTION = MMloveConstants.URL001
			+ MMloveConstants.NewsDetailInquiry;
	private final String SOAP_METHODNAME = MMloveConstants.NewsDetailInquiry;
	private final String SOAP_URL = MMloveConstants.URL002;
	
	private final String SOAP_ACTION2 = MMloveConstants.URL001
			+ MMloveConstants.NewsTopicInquiry;
	private final String SOAP_METHODNAME2 = MMloveConstants.NewsTopicInquiry;

	private final String SOAP_ACTION3 = MMloveConstants.URL001
			+ MMloveConstants.NewsTopicAdd;
	private final String SOAP_METHODNAME3 = MMloveConstants.NewsTopicAdd;
	private final String SOAP_ZANACTION = MMloveConstants.URL001
			+ MMloveConstants.NewsLikesInsert;
	private final String SOAP_ZANMETHODNAMEA = MMloveConstants.NewsLikesInsert;
	private BroadcastReceiver mRefreshBroadcastReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if (intent.getAction().equals("action.login")) {
				user = LocalAccessor.getInstance(News_Detail.this).getUser();
				searchRefrushNewsDetail();
				searchTopic();

			}
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setTranslucentStatus() ;
		setContentView(R.layout.layout_newsdetail);
		MyApplication.getInstance().addActivity(this);
		iniView();
		
		if(hasInternetConnected())
		{
			setListeners();
			iniCHKInfo();
			// iniCHKInfo();
			searchNewsDetail();

			searchTopic();
		}
		else
		{
			Toast.makeText(News_Detail.this, R.string.msgUninternet,
					Toast.LENGTH_SHORT).show();
		}
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("action.login");
		registerReceiver(mRefreshBroadcastReceiver, intentFilter);
		
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
	
	@Override
	protected void onResume()
	{
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onResume(this);
		InitUser();
		if (share != null) {
			share.dismiss();
		}
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
		tintManager.setStatusBarTintResource(R.color.home);//状态栏无背景
	}

	@Override
	protected void iniView()
	{
		// TODO Auto-generated method stub
		txtHead = (TextView) findViewById(R.id.txtHead);
		imgBack = (ImageView) findViewById(R.id.img_left);
		imgMore = (ImageView) findViewById(R.id.img_right);
		imgMore.setVisibility(View.VISIBLE);
		imgMore.setImageResource(R.drawable.icon_more);
		txtHead.setText(R.string.str_news_detail001);

		ID = this.getIntent().getExtras().getString("ID").toString();
		
		txt001 = (TextView)findViewById(R.id.txt001_newsdetail);
		//txt002 = (TextView)findViewById(R.id.txt002_newsdetail);
		
		web = (WebView)findViewById(R.id.txt002_newsdetail);
		WebSettings webSettings = web.getSettings();

		webSettings.setSaveFormData(false);
		webSettings.setJavaScriptEnabled(true);
		webSettings.setSupportZoom(false);
		webSettings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		
		//fb = FinalBitmap.create(this);//初始化FinalBitmap模块

		img001 = (ImageView)findViewById(R.id.img001_newsdetail);
		
		//liner_newdetail = (LinearLayout)findViewById(R.id.liner_newdetail);
		
//		imgTopicAdd = (ImageButton) findViewById(R.id.img001_newsdetail);
		
		listview = (ListViewForScrollView) findViewById(R.id.listview_news_detail);
		tvzannum=(TextView)findViewById(R.id.tvzannum);
		edmessage=(EditText)findViewById(R.id.et_sendmessage);
		ivsend=(ImageView)findViewById(R.id.iv_send);
		sv = (ScrollView) findViewById(R.id.sv_news_detail);
		llzan=(LinearLayout) findViewById(R.id.llzan);
		ivzan=(ImageView)findViewById(R.id.ivzan);
		tvnum=(TextView) findViewById(R.id.tvplunnum);
		rlshare=(RelativeLayout)findViewById(R.id.rlshare);
		rlpinlun=(RelativeLayout) findViewById(R.id.rlpinlun);
		sv.smoothScrollTo(0, 0);
//		fb.configLoadingImage(R.drawable.downloading);
	}
	
	private void iniCHKInfo()
	{
		InitUser();
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
	
	private void InitUser()
	{
		user = LocalAccessor.getInstance(News_Detail.this).getUser();
	}

	private void setListeners()

	{
		llzan.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(user.UserID==0){
					startActivity(new Intent(News_Detail.this,LoginActivity.class));
				}else{
					postZan();
				}

			}
		});
		rlpinlun.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				edmessage.setFocusable(true);
				edmessage.setFocusableInTouchMode(true);
				edmessage.requestFocus();
				edmessage.findFocus();
				InputMethodManager imm = (InputMethodManager) edmessage.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);

			}
		});

		rlshare.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(!TextUtils.isEmpty(url)) {

					share = new ShareWebWindow(News_Detail.this);


					ShareModel model = new ShareModel();

					model.setImgPath(ImageUtil.saveResTolocal(News_Detail.this.getResources(), R.drawable.icon, "mmlove_logo"));
					if(!TextUtils.isEmpty(title)){
						model.setText(title);
					}

					model.setTitle("妈妈爱我");
					model.setUrl(url);

					share.initShareParams(model);
//				backgroundAlpha(0.3f);
					share.showShareWindow();
//				share.setOnDismissListener(new poponDismissListener());

					// 显示窗口 (设置layout在PopupWindow中显示的位置)
					share.showAtLocation(News_Detail.this.findViewById(R.id.detail),
							Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
				}
			}
		});
		edmessage.addTextChangedListener(new TextWatcher(){
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				if(edmessage.getText().toString().length()>0){
					ivsend.setBackgroundResource(R.drawable.send_post);

				}else{
					ivsend.setBackgroundResource(R.drawable.send_post_un);
				}

			}
		} );
		imgBack.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				finish();
			}
		});
		imgMore.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				Intent i = new Intent(News_Detail.this, News_List.class);
				if(Week>39){
					i.putExtra("Week", 40+"");
				}else {
					i.putExtra("Week", Week + 1 + "");
				}
				startActivity(i);
			}
		});

		
//		imgTopicAdd.setOnClickListener(new OnClickListener()
//		{
//
//			@Override
//			public void onClick(View v)
//			{
//				// TODO Auto-generated method stub
//
//				if (hasInternetConnected())
//				{
//					if (user.UserID != 0)
//					{
//						inputTitleDialog("0");
//					}
//					else
//					{
//						Intent i = new Intent(News_Detail.this,
//								LoginActivity.class);
//						startActivityForResult(i, 1);
//					}
//				}
//				else
//				{
//					Toast.makeText(News_Detail.this, R.string.msgUninternet,
//							Toast.LENGTH_SHORT).show();
//				}
//
//			}
//		});
	}

	public void goSend(View v) {

		if (hasInternetConnected())
		{
			if (user.UserID != 0)
			{
//				inputTitleDialog("0");
				if(TextUtils.isEmpty(edmessage.toString().trim())) {
					Toast.makeText(News_Detail.this, R.string.topic_msg,
							Toast.LENGTH_SHORT).show();
					return;
				}else {

					String inputName = Html.toHtml(edmessage.getText());
					if(inputName.equals(""))
					{
						Toast.makeText(News_Detail.this, R.string.topic_msg,
								Toast.LENGTH_SHORT).show();
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
						TopicAdd(inputName, "0",edmessage);
					}
				}
			}
			else
			{
				Intent i = new Intent(News_Detail.this,
						LoginActivity.class);
				startActivityForResult(i, 1);
			}
		}
		else
		{
			Toast.makeText(News_Detail.this, R.string.msgUninternet,
					Toast.LENGTH_SHORT).show();
		}
	}
	/*
	 * 查询gridview
	 */
	private void searchNewsDetail()
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
									.getJSONArray("NewsDetailInquiry");
							for (int i = 0; i < array.length(); i++)
							{
								YunWeek = array.getJSONObject(i)
										.getString("YunWeek");
								txt001.setText(array.getJSONObject(i)
										.getString("Title"));
								title=array.getJSONObject(i)
										.getString("Title");

								tvzannum.setText(array.getJSONObject(i).getString("likecount"));
//								likecount
//										IsLiked
								url=MMloveConstants.URLIMAGE2+ array.getJSONObject(i)
										.getString("ID");

								web.loadUrl(MMloveConstants.URLIMAGE + array.getJSONObject(i)
										.getString("ID"));
//								web.loadUrl("http://5104f21e.nat123.net:24256");
								int status=Integer.valueOf(array.getJSONObject(i).getString("IsLiked"));
								if(status>0){
									ivzan.setBackgroundResource(R.drawable.btn_zanyx);
								}else{
									ivzan.setBackgroundResource(R.drawable.btn_zanwx);
								}
								
								//txt002.setText(array.getJSONObject(i)
//										.getString("Content"));
								
								//fb.display(liner_newdetail,Constants.JE_BASE_URL2 + array.getJSONObject(i).getString("PicURL"));
								
//								Bitmap bitmap = IOUtils.getHttpBitmap(Constants.JE_BASE_URL2 + array.getJSONObject(i).getString("PicURL"));
//								Drawable drawable =new BitmapDrawable(bitmap);
//								liner_newdetail.setBackgroundDrawable(drawable);
								
								//PicURL = Constants.JE_BASE_URL2 + array.getJSONObject(i).getString("PicURL");
							}
							
							//LoadImage();
						}
						catch (Exception ex)
						{
							ex.printStackTrace();
						}
					}
				}
			};

			JsonAsyncTask_Info task = new JsonAsyncTask_Info(
					News_Detail.this, true, doProcess);
			Map<String, Object> paramMap = new HashMap<String, Object>();
			String str = "<Request><ID>%s</ID><UserID>%s</UserID></Request>";
			if(user.UserID==0){
				str = String.format(str, new Object[]
						{ ID,"" });
			}else {
				str = String.format(str, new Object[]
						{ID, user.UserID + ""});
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
	/*
 * 查询gridview
 */
	private void searchRefrushNewsDetail()
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
									.getJSONArray("NewsDetailInquiry");
							for (int i = 0; i < array.length(); i++)
							{
								YunWeek = array.getJSONObject(i)
										.getString("YunWeek");
								txt001.setText(array.getJSONObject(i)
										.getString("Title"));
								title=array.getJSONObject(i)
										.getString("Title");

								tvzannum.setText(array.getJSONObject(i).getString("likecount"));
//								likecount
//										IsLiked

								int status=Integer.valueOf(array.getJSONObject(i).getString("IsLiked"));
								if(status>0){
									ivzan.setBackgroundResource(R.drawable.btn_zanyx);
								}else{
									ivzan.setBackgroundResource(R.drawable.btn_zanwx);
								}


							}

							//LoadImage();
						}
						catch (Exception ex)
						{
							ex.printStackTrace();
						}
					}
				}
			};

			JsonAsyncTask_Info task = new JsonAsyncTask_Info(
					News_Detail.this, true, doProcess);
			Map<String, Object> paramMap = new HashMap<String, Object>();
			String str = "<Request><ID>%s</ID><UserID>%s</UserID></Request>";
			if(user.UserID==0){
				str = String.format(str, new Object[]
						{ ID,"" });
			}else {
				str = String.format(str, new Object[]
						{ID, user.UserID + ""});
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
	protected void postZan() {
		// TODO Auto-generated method stub
		JsonAsyncTaskOnComplete doProcess = new JsonAsyncTaskOnComplete() {

			@Override
			public void processJsonObject(Object result) {
				// TODO Auto-generated method stub
				String value = result.toString();
				// {"ArticleLikesInsert":[{"MessageContent":"成功！","MessageCode":"0"}]}
				JSONObject mySO = (JSONObject) result;
				JSONArray array;
				try {
					array = mySO.getJSONArray("NewsLikesInsert");
					if ("0".equals(array.getJSONObject(0).getString(
							"MessageCode"))) {
						ivzan.setBackgroundResource(R.drawable.btn_zanyx);
						searchRefrushNewsDetail();

					} else {
						Toast.makeText(
								News_Detail.this,
								array.getJSONObject(0).getString(
										"MessageContent"), 1).show();
					}

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		};
		if (hasInternetConnected()) {
			JsonAsyncTask_Info task = new JsonAsyncTask_Info(
					News_Detail.this, true, doProcess);
			Map<String, Object> paramMap = new HashMap<String, Object>();
			String str = "<Request><UserID>%s</UserID><NewsID>%s</NewsID></Request>";
			str = String.format(str, new Object[] { user.UserID + "", ID });

			paramMap.put("str", str);
			task.execute(SOAP_NAMESPACE, SOAP_ZANACTION, SOAP_ZANMETHODNAMEA,
					SOAP_URL, paramMap);
		} else {
			Toast.makeText(News_Detail.this, R.string.msgUninternet,
					Toast.LENGTH_SHORT).show();
		}
		// ArticleLikesInsert
	}
	
	private void LoadImage()
	{
		new LoadPictrue().getPicture(PicURL, liner_newdetail);
	}
	
	/*
	 * 查询listview
	 */
	private void searchTopic()
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
						arrayList2 = new ArrayList<Map<String, Object>>();
						// 下边是具体的处理
						try
						{
							JSONObject mySO = (JSONObject) result;
							JSONArray array = mySO
									.getJSONArray("NewsTopicInquiry");
							if(array.getJSONObject(0).has("MessageCode"))
							{
								if (array.getJSONObject(0).getString("MessageCode")
										.equals("0"))
								{
									
								}
							}
							else
							{
								if(arrayList2!=null&&arrayList2.size()>0)
									arrayList2.clear();
								for (int i = 0; i < array.length(); i++)
								{
	
									map = new HashMap<String, Object>();
									map.put("ID",
											array.getJSONObject(i).getString("ID"));
									map.put("LeftID", array.getJSONObject(i)
											.getString("LeftID"));
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
									
									
//									DateFormat fmt = new SimpleDateFormat(
//											"yyyy-MM-dd");
//									Date date;
//
//									Date date2;
//									date = fmt.parse(YuBirthTime);
//
//									date2 = fmt.parse(CreatedDate);
//									Yuchan yu = new Yuchan();
//									yu = IOUtils.WeekInfo2(date,date2);
//
//									int weekitem=Integer.valueOf(yu.Week+"");
//									if(weekitem<0){
//										//如果为负值，就用当前时间减去预产期
//										date = fmt.parse(fmt.format((new Date()).getTime()));
//										yu = IOUtils.WeekInfo2(date,date2);
//									}
//									if( yu.Week>=40){
//										map.put("YuBirthTime", "怀孕40周");
//									}else{
//									map.put("YuBirthTime", "怀孕" + yu.Week + "周");
//									}
								
									map.put("PostsCount", array.getJSONObject(i)
											.getString("PostsCount"));
									arrayList2.add(map);
	
								}
								tvnum.setText(arrayList2.size()+"");
							}
							// SimpleAdapter adapter = new SimpleAdapter(
							// CHKTimeDetail.this, arrayList2,
							// R.layout.item_chkdetail_list,
							// new Object[] {
							// "NickName","YuBirthTime","TopicTitle","GoodCount","PostsCount"
							// },
							// new int[] {
							// R.id.item_chkdetail_list_txt001,R.id.item_chkdetail_list_txt002,R.id.item_chkdetail_list_txt003,R.id.item_chkdetail_list_txt004,R.id.item_chkdetail_list_txt005
							// });
							CHKTiemDet_newsAdapter adapter = new CHKTiemDet_newsAdapter(
									News_Detail.this, arrayList2,listview,
									News_Detail.this);
							listview.setAdapter(adapter);
							stopProgressDialog();
							// listview.setEnabled(true);
							// 添加listView点击事件


						}
						catch (Exception ex)
						{
							ex.printStackTrace();
						}
					}
				}
			};

			if (hasInternetConnected())
			{
				JsonAsyncTask_Info task = new JsonAsyncTask_Info(
						News_Detail.this, true, doProcess);
				Map<String, Object> paramMap = new HashMap<String, Object>();
				String str = "<Request><TopicType>02</TopicType><LeftID>%s</LeftID><ParentID>%s</ParentID><PageIndex>%s</PageIndex><PageSize>%s</PageSize></Request>";
	
				str = String.format(str, new Object[]
				{ ID, "0", "1", "999" });
				paramMap.put("str", str);
	
				// 必须是这5个参数，而且得按照顺序
				task.execute(SOAP_NAMESPACE, SOAP_ACTION2, SOAP_METHODNAME2,
						SOAP_URL, paramMap);
			}
			else
			{
				Toast.makeText(News_Detail.this, R.string.msgUninternet,
						Toast.LENGTH_SHORT).show();
			}
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*
	 * 添加评论或回复
	 */
	private void TopicAdd(String topicTitle, String parentID,final EditText edit)
	{
		try
		{
			JsonAsyncTaskOnComplete doProcess = new JsonAsyncTaskOnComplete()
			{
				public void processJsonObject(Object result)
				{
					returnvalue001 = result.toString();
					edit.setText("");
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
							JSONArray array = mySO.getJSONArray("NewsTopicAdd");

							if (array.getJSONObject(0).getString("MessageCode")
									.equals("0"))
							{
								 InputMethodManager imm = (InputMethodManager)getSystemService(News_Detail.this.INPUT_METHOD_SERVICE); 
						           imm.hideSoftInputFromWindow(edit.getWindowToken(),0);
								if(array.getJSONObject(0).has("PointAddCode")) {
									if (array.getJSONObject(0).getString("PointAddCode").equals("0")) {

										Intent i = new Intent(News_Detail.this, DialogEnsure.class);
										i.putExtra("content", array.getJSONObject(0).getString("PointAddContent") + "+" + array.getJSONObject(0).getString("PointValue"));
										startActivity(i);
										searchTopic();
									}
								}else {
									searchTopic();
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

			if (hasInternetConnected())
			{
				JsonAsyncTask_Info task = new JsonAsyncTask_Info(
						News_Detail.this, true, doProcess);
				Map<String, Object> paramMap = new HashMap<String, Object>();
				String str = "<Request><TopicType>02</TopicType><LeftID>%s</LeftID><ParentID>%s</ParentID><TopicTitle>%s</TopicTitle><UserID>%s</UserID></Request>";

				str = String.format(str, new Object[]
				{ ID, parentID, topicTitle, String.valueOf(user.UserID) });
				paramMap.put("str", str);
				startProgressDialog();

				// 必须是这5个参数，而且得按照顺序
				task.execute(SOAP_NAMESPACE, SOAP_ACTION3, SOAP_METHODNAME3,
						SOAP_URL, paramMap);
			}
			else
			{
				Toast.makeText(News_Detail.this, R.string.msgUninternet,
						Toast.LENGTH_SHORT).show();
			}
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void onClick(View item, View widget, int position, int which)
	{
		switch (which)
		{
			case R.id.item_chkdetail_list_txt004:
				searchTopic();
				// notify();
				break;
			case R.id.img002_item_chkdetail_list:

				break;
			case R.id.item_chkdetail_list_txt006:
				// EditText disInput =
				// (EditText)findViewById(R.id.group_discuss);
				// disInput.requestFocus();

				if (user.UserID != 0)
				{
					inputTitleDialog(arrayList2.get(position).get("ID")
							.toString());
				}
				else
				{
					Intent i = new Intent(News_Detail.this,
							LoginActivity.class);
					startActivityForResult(i, 1);
				}
				break;
			default:
				break;
		}
	}

	private void inputTitleDialog(final String parentID)
	{

		DialogEnsureEdit dialogEdit=new DialogEnsureEdit(News_Detail.this);
		final EditText edit=dialogEdit.getmEditText();
		
		dialogEdit.setDialogMsg("请填写评论内容","确定").
				setOnClickListenerEnsure(new OnClickListener() {
							
							@Override
							public void onClick(View v) {
								if(TextUtils.isEmpty(edit.toString().trim())){
									Toast.makeText(News_Detail.this, R.string.topic_msg,
											Toast.LENGTH_SHORT).show();
									return;
								}else{
									String inputName = Html.toHtml(edit.getText());
									if(inputName.equals(""))
									{
										Toast.makeText(News_Detail.this, R.string.topic_msg,
												Toast.LENGTH_SHORT).show();
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
										TopicAdd(inputName, parentID,edit);
									}
								}
							}
						});
		DialogUtils.showSelfDialog(this, dialogEdit);
//		final EditText inputServer = new EditText(News_Detail.this);
//		inputServer.setGravity(Gravity.TOP);
//		inputServer.setMinLines(5);
//		inputServer.setFocusable(true);
//		final String parent = parentID;
//		AlertDialog.Builder builder = new AlertDialog.Builder(
//				News_Detail.this);
//		builder.setTitle(R.string.topictitle)
//				.setView(inputServer)
//				.setNegativeButton(R.string.btnCancel, null);
//		builder.setPositiveButton(R.string.btnOK,
//				new DialogInterface.OnClickListener()
//				{
//
//					public void onClick(DialogInterface dialog, int which)
//					{
//						String str = inputServer.getText().toString().trim();
//						if(str.equals(""))
//						{
//							Toast.makeText(News_Detail.this, R.string.topic_msg,
//									Toast.LENGTH_SHORT).show();
//							return;
//						}
//						
//						String inputName = Html.toHtml(inputServer.getText());
//
//						if(inputName.equals(""))
//						{
//							Toast.makeText(News_Detail.this, R.string.topic_msg,
//									Toast.LENGTH_SHORT).show();
//						}
//						else
//						{
//							if(inputName.contains("\"ltr\""))
//							{
//								
//							}
//							else
//							{
//								inputName = inputName.replace("ltr", "\"ltr\"");
//							}
//							TopicAdd(inputName, parent,edit);
//						}
//					}
//				});
//		builder.show();
	}
	
	
	@Override
	public void onDestroy()
	{
		// TODO Auto-generated method stub
		super.onDestroy();
		
		web.removeAllViews();
		web.destroy();
		try {
			unregisterReceiver(mRefreshBroadcastReceiver);

			mRefreshBroadcastReceiver = null;
		} catch (Exception e) {
		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		switch (resultCode)
		{ // resultCode为回传的标记，我在B中回传的是RESULT_OK
			case 0:
				
				break;
			case 1:
				InitUser();
				break;
			case 2:
	
				break;
			case 3:
				
				break;
			case 4:
				
				break;
			case 5:
				
				break;
			case 6:
				
				break;
			case -1:
				break;
			default:
				break;
		}
	}
}
