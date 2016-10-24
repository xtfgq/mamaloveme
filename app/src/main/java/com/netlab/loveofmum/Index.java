package com.netlab.loveofmum;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.igexin.sdk.PushManager;
import com.netlab.loveofmum.R.string;
import com.netlab.loveofmum.activity.LoginActivity;
import com.netlab.loveofmum.api.BaseActivity;
import com.netlab.loveofmum.api.CustomHttpClient;
import com.netlab.loveofmum.api.JsonAsyncTaskOnComplete;
import com.netlab.loveofmum.api.JsonAsyncTask_Info;
import com.netlab.loveofmum.api.LocalAccessor;
import com.netlab.loveofmum.api.MMloveConstants;
import com.netlab.loveofmum.api.MyApplication;
import com.netlab.loveofmum.model.MyApp;
import com.netlab.loveofmum.model.User;
import com.netlab.loveofmum.model.Yuchan;
import com.netlab.loveofmum.model.YunWeekAll;
import com.netlab.loveofmum.myadapter.FeedLastAdapter;
import com.netlab.loveofmum.myadapter.Index003Adapter;
import com.netlab.loveofmum.testwebview.TestWebView;
import com.netlab.loveofmum.timepicker.Util;
import com.netlab.loveofmum.utils.DetialGallery;
import com.netlab.loveofmum.utils.DialogUtils;
import com.netlab.loveofmum.utils.IOUtils;
import com.netlab.loveofmum.utils.PermissionsChecker;
import com.netlab.loveofmum.utils.ScreenUtils;
import com.netlab.loveofmum.utils.SystemStatusManager;
import com.netlab.loveofmum.utils.UiUtils;
import com.netlab.loveofmum.widget.DialogEnsureCancelView;
import com.netlab.loveofmum.widget.ImageCycleView;
import com.netlab.loveofmum.widget.ImageCycleView.ImageCycleViewListener;
import com.netlab.loveofmum.widget.ListViewForScrollView;
import com.netlab.loveofmum.widget.MyViewPageAdapter;
import com.netlab.loveofmum.widget.MyViewPager;
import com.netlab.loveofmum.widget.RoundProgressBar;
import com.netlab.loveofmum.widget.UpScrollViewExtend;
import com.netlab.loveofmum.widget.XScrollView.RefreshLayout;
import com.netlab.loveofmum.widget.XScrollView.RefreshLayout.OnRefreshListener;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import cn.sharesdk.framework.ShareSDK;
//import cn.sharesdk.framework.ShareSDK;


public class Index extends BaseActivity implements OnRefreshListener, onSelectListener {


	private final String mPageName = "AnalyticsHome";

	// private ListView list001;
	private ListViewForScrollView list002;

	/**
	 * 社区数据列表
	 */
	private ListViewForScrollView listsq;


	private TextView txtHead,tvyuyue;
	private TextView txt001;

	//	private TextView txt_index_002;
	private ImageView imgBack;
	private LinearLayout imgMore;
	private LinearLayout imgYsq;
	private TextView txtmore;

	private TextView txt0001;
	private TextView txt0002;

	private TableRow tab003;
	private List<Map<String, Object>> arrayList = new ArrayList<Map<String, Object>>();

	private List<Map<String, Object>> arrayList2 = new ArrayList<Map<String, Object>>();
	private List<Map<String, Object>> hosList = new ArrayList<Map<String, Object>>();
	private List<Map<String, Object>> listFeeds = new ArrayList<Map<String, Object>>();
	private String returnvalue002;

	private LayoutInflater inflater;

	private LinearLayout linear001, chktimell, llhos;
	int scrollX = 0;
	int scrollY = 0;
	private int newCode;


	private int oldCode;

	private int per = 0;

	private String versionlog;

	private MyApp app;

	private int week = 0;

	private User user;
	//宝宝当前情况的大概描述
	private TextView txtcontent, tvday, mTextLenth, mTextWeight;
	//宝宝情况下边广告
	private ImageCycleView mAdView;

	//暂时隐藏
	private ImageView imgcontent;

	private UpScrollViewExtend sv;

	private long mExitTime;


	private String CHKTypeName2;


	private ImageCycleView mYsQView;
	//产品预约中的ViewPager
	private MyViewPager mHosView;

	private ImageView ivleft, ivright;
	private LayoutInflater layoutInflater;

	RefreshLayout refreshview;
	private RelativeLayout rlcontnet, rlchktime;
	private DetialGallery mViewPager;
	private LinearLayout rltop;
	private final String SOAP_TIPSACTION = MMloveConstants.URL001
			+ MMloveConstants.TipsInquiry;
	private final String SOAP_TIPSMETHODNAME = MMloveConstants.TipsInquiry;

	// 首页广告位置
	ArrayList<String> mImageUrl = new ArrayList<String>();
	ArrayList<Map<String, Object>> mObject = new ArrayList<Map<String, Object>>();
	// 首页广告位置2
	ArrayList<String> mImageUrl2 = new ArrayList<String>();
	ArrayList<Map<String, Object>> mObject2 = new ArrayList<Map<String, Object>>();
	// 定义Web Service相关的字符串
	private final String SOAP_NAMESPACE = MMloveConstants.URL001;
	private final String SOAP_ACTION = MMloveConstants.URL001
			+ MMloveConstants.NewsIndexInquiry;
	private final String NewsIndexInquiry = MMloveConstants.URL001
			+ MMloveConstants.NewsIndexInquiry;
	private final String SOAP_METHODNAME = MMloveConstants.NewsIndexInquiry;
	private final String SOAP_URL = MMloveConstants.URL002;


	private final String SOAP_ACTION2 = MMloveConstants.URL001
			+ MMloveConstants.OrderInquiryForIndex;
	private final String SOAP_METHODNAME2 = MMloveConstants.OrderInquiryForIndex;

	private final String SOAP_ACTION3 = MMloveConstants.URL001
			+ MMloveConstants.VersionInquiry;
	private final String SOAP_METHODNAME3 = MMloveConstants.VersionInquiry;

	// 请求医院列表
	private final String SOAP_ACTION4 = MMloveConstants.URL001
			+ MMloveConstants.HospitalInquiry;
	private final String SOAP_METHODNAME4 = MMloveConstants.HospitalInquiry;

	// 请求广告接口
	private final String SOAP_ACTIONADs = MMloveConstants.URL001
			+ MMloveConstants.Ads;
	private final String SOAP_METHODNAMEADs = MMloveConstants.Ads;
	// private final String SOAP_NEWSINDEXINQUIRYAC = MMloveConstants.URL001
	// + MMloveConstants.Ads;
	// private final String SOAP_NewsIndexInquiryMETHODNAME=
	// MMloveConstants.Ads;

	private final String SOAP_CONTENT = MMloveConstants.URL001
			+ MMloveConstants.Ads;
	private final String SOAP_CONTENTTWO = MMloveConstants.Ads;

	// 查询积分接口
	private final String SOAP_LOGMETHODNAME = MMloveConstants.UserPointLog;
	private final String SOAP_LOGACTION = MMloveConstants.URL001
			+ MMloveConstants.UserPointLog;

	// 用户总积分
	private final String SOAP_INQUIRYMETHODNAME = MMloveConstants.UserInquiry;
	private final String SOAP_INQUIRYACTION = MMloveConstants.URL001
			+ MMloveConstants.UserInquiry;

	private String returnAds;
	private List<View> views = new ArrayList<View>();
	private List<View> childViews = new ArrayList<View>();

	private MyViewPageAdapter vpAdapter;
	private MyViewPageAdapter vpChildAdapter;
	private int hosiSize;
	private LinearLayout ll_index;
	// 底部小点图片
	private ImageView[] dots;
	private LinearLayout ivsearch,lltime;
	private RelativeLayout rlchktiem;



	// 记录当前选中位置
	private int currentIndex;
	protected String returnvalue001;
	int width, height;
	private TextView tv_viewhos;
	// private FeedAdapter feedAadpter;
	Yuchan yu;
	private Context mContext;
	private Date dNow, dYun;
	private ImageView tvgotaday;
	private LinearLayout llchildleft, llchildright;
	// 得到系统时间
	private final String SOAP_SERVERTIMEMETHODNAME = MMloveConstants.GetServerTime;
	private final String SOAP_SERVERTIMEACTION = MMloveConstants.URL001
			+ MMloveConstants.GetServerTime;
	private ImageView tvsign;
	private Animation animation1 = null;

	private TextView tvchildprogress, tvnext, tvcenter, tvyesterday;
	private Boolean isFisrt=false;
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case 0:
					initViewAds();

					break;
				case 1:
					FeedLastAdapter adapter = new FeedLastAdapter(Index.this,
							listFeeds, ClientID);
					listsq.setAdapter(adapter);
					setListViewHeightBasedOnChildren(listsq);
					listsq.setEnabled(true);

					break;
				case 1010:
					if (isPost) {

						TipsInquiry();
					}
					break;

				default:
					break;
			}
		}

	};
	String[] lengths;
	String[] weights;
	private TextView tvScore;
	private String ClientID = "";
	private Boolean isHosiptal = false;

	private TextView tvkey, tvchao;
	private static final int FLING_MIN_DISTANCE = 50;
	private static final int FLING_MIN_VELOCITY = 0;
	int days, childday;
	public Boolean isPost = false;
	public int selectNum = 0;//全局变量，保存被选中的item
	public GalleryAdapter adapterGallery = null;
	final List<YunWeekAll> yunlist = new ArrayList<YunWeekAll>();
	private Boolean isAlready=false;
	private static final String PACKAGE_URL_SCHEME = "package:";
	private PermissionsChecker mPermissionsChecker; // 权限检测器
	// 所需的全部权限
	static final String[] PERMISSIONS = new String[]{

			android.Manifest.permission.WRITE_EXTERNAL_STORAGE
	};
	private final int MY_PERMISSIONS_REQUEST=15;
	private int[] weekIcons = new int[]{R.drawable.week1, R.drawable.week2, R.drawable.week3, R.drawable.week4, R.drawable.week5, R.drawable.week6, R.drawable.week7, R.drawable.week8,
			R.drawable.week9, R.drawable.week10, R.drawable.week11, R.drawable.week12, R.drawable.week13, R.drawable.week14, R.drawable.week15, R.drawable.week16, R.drawable.week17, R.drawable.week18,
			R.drawable.week19, R.drawable.week20, R.drawable.week21, R.drawable.week22, R.drawable.week23, R.drawable.week24, R.drawable.week25, R.drawable.week26, R.drawable.week27, R.drawable.week28,
			R.drawable.week29, R.drawable.week30, R.drawable.week31, R.drawable.week32, R.drawable.week33, R.drawable.week34, R.drawable.week35, R.drawable.week36, R.drawable.week37, R.drawable.week38, R.drawable.week39, R.drawable.week40};


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setTranslucentStatus();
		mPermissionsChecker = new PermissionsChecker(this);
		width = Util.getScreenWidth(Index.this);
		setContentView(R.layout.layout_index);


		//初始化首页顶部Gallery的Adapter
		for (int i = 1; i <= 281; i++) {
			YunWeekAll map = new YunWeekAll();
			if (i / 7 == 0) {
				map.setDay(i % 7 + "天");
				map.setImage(getResources().getDrawable(weekIcons[0]));
			} else if (i % 7 == 0) {
				map.setDay(i / 7 + "周");
				map.setImage(getResources().getDrawable(weekIcons[i / 7 - 1]));
			} else if (i == 281) {
				map.setDay("宝宝已经出生了");
				map.setImage(getResources().getDrawable(R.drawable.img_bb));
			} else {
				map.setDay(i / 7 + "周+" + i % 7 + "天");
				map.setImage(getResources().getDrawable(weekIcons[i / 7 - 1]));
			}
			yunlist.add(map);
		}
		adapterGallery = new GalleryAdapter(this, yunlist);


		try {
			dNow = string2Date(MyApplication.getInstance().nowtime, "yyyy-MM-dd");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Finally stick the string into the text view.

		//版本更新
		if (hasInternetConnected()) {
			versioninquiry();
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			String date = df.format(new Date());// new Date()为获取当前系统时间

			searchAds(date);
			searchYsq(date);
		} else {
			Toast.makeText(Index.this, string.msgUninternet,
					Toast.LENGTH_SHORT).show();
		}
		MyApplication.getInstance().addActivity(this);
		// if(myApplication.arrayList3!=null&&myApplication.arrayList3.size()>0){
		// hosiSize=myApplication.arrayList3.size();
		//
		// }

		iniView();


		String text = null;
		try {
			InputStream is = getAssets().open("eat.txt");
			int size = is.available();

			// Read the entire asset into a local byte buffer.
			byte[] buffer = new byte[size];
			is.read(buffer);
			is.close();

			// Convert the buffer into a string.
			text = new String(buffer, "UTF-8");
			String[] keys = text.split("\\,");
			Random ra = new Random();

			int key = ra.nextInt(keys.length) + 0;
			tvkey.setText("能不能吃:" + keys[key]);
			InputStream isH = getAssets().open("height.txt");
			int sizeH = isH.available();

			// Read the entire asset into a local byte buffer.
			byte[] bufferH = new byte[sizeH];
			isH.read(bufferH);
			isH.close();

			// Convert the buffer into a string.
			text = new String(bufferH, "UTF-8");
			lengths = text.split("\\,");
			InputStream isW = getAssets().open("weight.txt");
			int sizeW = isW.available();

			// Read the entire asset into a local byte buffer.
			byte[] bufferW = new byte[sizeW];
			isW.read(bufferW);
			isW.close();

			// Convert the buffer into a string.
			text = new String(bufferW, "UTF-8");
			weights = text.split("\\,");

		} catch (IOException e) {
			e.printStackTrace();
		}



		UiUtils.resetLL(Index.this, tvkey);
		setListeners();

		PushManager.getInstance().initialize(Index.this);
		ClientID = PushManager.getInstance().getClientid(Index.this);

		initViewAds();
		rlchktime.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (week > 10) {

					Intent i = new Intent(Index.this, CHKTimeList.class);
					startActivity(i);
				}
			}
		});
		animation1 = AnimationUtils.loadAnimation(Index.this, R.anim.anim3);
		mViewPager.setAdapter(adapterGallery);
		mViewPager.setSelection(childday - 1);
		mViewPager.setOnItemSelectedListener(new Gallery.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
									   int arg2, long arg3) {
				System.out.println(arg2);
				selectNum = arg2;//
				adapterGallery.notifyDataSetChanged();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				System.out.println("not");
			}
		});
		sv.smoothScrollTo(0,0);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			if (mPermissionsChecker.lacksPermissions(PERMISSIONS)) {
				showMissingPermissionDialog(Index.this);
				return;
			}
		}
	}
	public void showMissingPermissionDialog(final Context context){
		DialogEnsureCancelView dialogEnsureCancelView = new DialogEnsureCancelView(
				Index.this).setDialogMsg("温馨提示", "权限->存储", "去打开存储权限")
				.setOnClickListenerEnsure(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
						intent.setData(Uri.parse(PACKAGE_URL_SCHEME + getPackageName()));
						startActivity(intent);

					}
				});
		DialogUtils.showSelfDialog(Index.this, dialogEnsureCancelView);


	}

	/**
	 * 获取孕社区数据列表
	 */
	private void getYsq() {
		// TODO Auto-generated method stub
		new Thread(
				new Runnable() {

					@Override
					public void run() {
						String result = "";


						try {
							result = CustomHttpClient.getFromWebByHttpClient(Index.this, MMloveConstants.URLWEB + "/molms/webservice/getTopic.jspx",
									new BasicNameValuePair("topiccount", "3"));
							Map<String, Object> map;

							com.alibaba.fastjson.JSONObject obj = JSON.parseObject(result);

							com.alibaba.fastjson.JSONArray jsonArray = obj.getJSONArray("topicData");
							for (int i = 0; i < jsonArray.size(); i++) {
								map = new HashMap<String, Object>();
								com.alibaba.fastjson.JSONObject jo = jsonArray.getJSONObject(i);
								map.put("title", jo.getString("title"));
								map.put("Name", jo.getString("bbsForumName"));
								map.put("username", jo.getString("username"));
								map.put("content", jo.getString("content"));
								map.put("avatar", jo.getString("avatar"));
								map.put("replyCount", jo.getString("replyCount"));
								map.put("viewCount", jo.getString("viewCount"));
								map.put("createTime", jo.getString("createTime"));
								map.put("YuBirthTime", jo.getString("YuBirthTime"));
								map.put("url", MMloveConstants.URLWEB + jo.getString("url"));
								listFeeds.add(map);
							}
							Message message = Message.obtain();
							message.what = 1;
							mHandler.sendMessage(message);

						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}


					}
				}
		).start();
	}

	/**
	 * 跳转到签到或者登录页面
     */
	public void goSingn(View v) {
		if (!hasInternetConnected()) {
			Toast.makeText(Index.this, "网络数据获取失败，请检查网络设置", 1).show();
			return;
		}
		// SignDialog(this);
		if (user.UserID != 0) {
			startActivity(new Intent(this, SignAct.class));
		} else {
			startActivity(new Intent(this, LoginActivity.class));
		}
	}
	@Override
	public void onRequestPermissionsResult(int requestCode,
										   String permissions[], int[] grantResults) {
		switch (requestCode) {
			case MY_PERMISSIONS_REQUEST: {
				// If request is cancelled, the result arrays are empty.
				if (grantResults.length > 0
						&& grantResults[0] == PackageManager.PERMISSION_GRANTED) {

					// permission was granted, yay! Do the
					// contacts-related task you need to do.

				} else {

					// permission denied, boo! Disable the
					// functionality that depends on this permission.
				}
				return;
			}
		}
	}

	private void getTime() {
		// TODO Auto-generated method stub
		JsonAsyncTaskOnComplete doProcess = new JsonAsyncTaskOnComplete() {

			@Override
			public void processJsonObject(Object result) {
				// {"UserPointAdd":[{"MessageContent":"成功！","MessageCode":"0"}]}
				// TODO Auto-generated method stub

				String value = result.toString();
				// {"UserInquiry":[{"UserSex":"01","UserAge":"33","PictureURL":"~\/UploadFile\/image\/2016030109454450694.jpg","NickName":"宝爸20153028230623hhhh","UserNO":"410102198201251510","UserID":"50694","YuBirthTime":"2016\/10\/25
				// 0:00:00","RealName":"郭芮","Point":"60","ClientID":"0c1726ae905edaa84b2b26e794910ab1","City":"郑州"}]}
				JSONObject mySO = (JSONObject) result;
				try {
					JSONArray array = mySO.getJSONArray("GetServerTime");
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					myApplication.nowtime = sdf.format(IOUtils.string2Date(
							array.getJSONObject(0).getString("ServerTime"),
							"yyyy-MM-dd").getTime());
					dNow = string2Date(MyApplication.getInstance().nowtime, "yyyy-MM-dd");
					Date dStart = new Date(dNow.getTime() - 60 * 60 * 1000 * 24);

					String endTime = sdf.format(dNow.getTime() + 60 * 60 * 1000
							* 24);
					String startTime = sdf.format(dStart);

					getUserInqury();

					getPonit(startTime, endTime);

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		};
		JsonAsyncTask_Info task = new JsonAsyncTask_Info(this, true, doProcess);
		Map<String, Object> paramMap = new HashMap<String, Object>();
		String str = "<Request></Request>";
		str = String.format(str, new Object[]{"",});

		paramMap.put("str", str);
		task.execute(SOAP_NAMESPACE, SOAP_SERVERTIMEACTION,
				SOAP_SERVERTIMEMETHODNAME, SOAP_URL, paramMap);
	}
//	protected void useCustomLogin() {
//		// 管理器
//		LoginSDKManager.getInstance().addAndUse(new CustomLoginImpl());
//	}

	private void searchAds(String date) {

		JsonAsyncTaskOnComplete doProcess = new JsonAsyncTaskOnComplete() {

			@Override
			public void processJsonObject(Object result) {

				returnAds = result.toString();
				if (returnAds != null) {
					JSONObject mySO = (JSONObject) result;
					Map<String, Object> map;
					try {
						JSONArray array = mySO.getJSONArray("ADInquiry");
						if (array.getJSONObject(0).has("MessageCode")) {
							// Toast.makeText(Index.this, "数据为空", 1).show();
							mAdView.setVisibility(View.GONE);


						} else {
							if (mImageUrl != null && mImageUrl.size() > 0) {
								mImageUrl.clear();
							}


							if (mObject != null && mObject.size() > 0) {
								mObject.clear();
							}

							for (int i = 0; i < array.length(); i++) {

								map = new HashMap<String, Object>();
								map.put("ID",
										array.getJSONObject(i).getString("LinkID"));
								map.put("Position",
										array.getJSONObject(i).getString("Position"));
								map.put("ImageUr",
										(MMloveConstants.JE_BASE_URL + array
												.getJSONObject(i).getString(
														"ImageUrl")).toString()
												.replace("~", "")
												.replace("\\", "/"));


								mImageUrl
										.add((MMloveConstants.JE_BASE_URL + array
												.getJSONObject(i).getString(
														"ImageUrl")).toString()
												.replace("~", "")
												.replace("\\", "/"));

								mObject.add(map);

							}

							showADs();
						}

					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

			}
		};
		// TODO Auto-generated method stub
		JsonAsyncTask_Info task = new JsonAsyncTask_Info(Index.this, true,
				doProcess);
		Map<String, Object> paramMap = new HashMap<String, Object>();
		String str = "<Request><Date>%s</Date><ShowPosition>%s</ShowPosition></Request>";
		str = String.format(str, new Object[]{date, "01"});

		paramMap.put("str", str);
		task.execute(SOAP_NAMESPACE, SOAP_ACTIONADs, SOAP_METHODNAMEADs,
				SOAP_URL, paramMap);
	}

	private void searchYsq(String date) {
		JsonAsyncTaskOnComplete doProcess = new JsonAsyncTaskOnComplete() {

			@Override
			public void processJsonObject(Object result) {

				returnAds = result.toString();
				if (returnAds != null) {
					JSONObject mySO = (JSONObject) result;
					Map<String, Object> map;
					try {
						JSONArray array = mySO.getJSONArray("ADInquiry");
						if (array.getJSONObject(0).has("MessageCode")) {
							// Toast.makeText(Index.this, "数据为空", 1).show();

							//模拟怀孕社区
							mYsQView.setVisibility(View.GONE);
						} else {

							if (mImageUrl2 != null && mImageUrl2.size() > 0) {
								mImageUrl2.clear();
							}

							if (mObject2 != null && mObject2.size() > 0) {
								mObject2.clear();
							}

							for (int i = 0; i < array.length(); i++) {

								map = new HashMap<String, Object>();
								map.put("ID",
										array.getJSONObject(i).getString("LinkID"));
								map.put("Position",
										array.getJSONObject(i).getString("Position"));
								map.put("ImageUr",
										(MMloveConstants.JE_BASE_URL + array
												.getJSONObject(i).getString(
														"ImageUrl")).toString()
												.replace("~", "")
												.replace("\\", "/"));


								mImageUrl2.add((MMloveConstants.JE_BASE_URL + array
										.getJSONObject(i).getString(
												"ImageUrl")).toString()
										.replace("~", "")
										.replace("\\", "/"));
								mObject2.add(map);

							}

							showYsq();
						}

					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

			}
		};
		// TODO Auto-generated method stub
		JsonAsyncTask_Info task = new JsonAsyncTask_Info(Index.this, true,
				doProcess);
		Map<String, Object> paramMap = new HashMap<String, Object>();
		String str = "<Request><Date>%s</Date><ShowPosition>%s</ShowPosition></Request>";
		str = String.format(str, new Object[]{date, "03"});

		paramMap.put("str", str);
		task.execute(SOAP_NAMESPACE, SOAP_ACTIONADs, SOAP_METHODNAMEADs,
				SOAP_URL, paramMap);

	}

	private void searchReAds(String date) {

		JsonAsyncTaskOnComplete doProcess = new JsonAsyncTaskOnComplete() {

			@Override
			public void processJsonObject(Object result) {

				returnAds = result.toString();

				if (returnAds != null) {
					JSONObject mySO = (JSONObject) result;
					Map<String, Object> map;
					try {
						JSONArray array = mySO.getJSONArray("ADInquiry");
						if (array.getJSONObject(0).has("MessageCode")) {
							// Toast.makeText(Index.this, "数据为空", 1).show();
							mAdView.setVisibility(View.GONE);

						} else {
							if (mImageUrl != null && mImageUrl.size() > 0) {
								mImageUrl.clear();
							}
							if (mObject != null && mObject.size() > 0) {
								mObject.clear();
							}

							for (int i = 0; i < array.length(); i++) {

								map = new HashMap<String, Object>();
								map.put("ID",
										array.getJSONObject(i).getString("LinkID"));
								map.put("Position",
										array.getJSONObject(i).getString("Position"));
								map.put("ImageUr",
										(MMloveConstants.JE_BASE_URL + array
												.getJSONObject(i).getString(
														"ImageUrl")).toString()
												.replace("~", "")
												.replace("\\", "/"));
								Log.i("vvvvvvvvvvvvvv", (MMloveConstants.JE_BASE_URL + array
										.getJSONObject(i).getString(
												"ImageUrl")).toString()
										.replace("~", "")
										.replace("\\", "/"));


								mImageUrl
										.add((MMloveConstants.JE_BASE_URL + array
												.getJSONObject(i).getString(
														"ImageUrl")).toString()
												.replace("~", "")
												.replace("\\", "/"));
								mObject.add(map);

							}


							showADs();
						}
						new Handler() {
							@Override
							public void handleMessage(Message msg) {
								// 千万别忘了告诉控件刷新完毕了哦！

								refreshview.refreshFinish(RefreshLayout.SUCCEED);
							}
						}.sendEmptyMessageDelayed(0, 500);

					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						new Handler() {
							@Override
							public void handleMessage(Message msg) {
								// 千万别忘了告诉控件刷新完毕了哦！

								refreshview.refreshFinish(RefreshLayout.FAIL);
							}
						}.sendEmptyMessageDelayed(0, 500);

					}

				}

			}
		};
		// TODO Auto-generated method stub
		JsonAsyncTask_Info task = new JsonAsyncTask_Info(Index.this, true,
				doProcess);
		Map<String, Object> paramMap = new HashMap<String, Object>();
		String str = "<Request><Date>%s</Date><ShowPosition>%s</ShowPosition></Request>";
		str = String.format(str, new Object[]{date, "01"});

		paramMap.put("str", str);
		task.execute(SOAP_NAMESPACE, SOAP_ACTIONADs, SOAP_METHODNAMEADs,
				SOAP_URL, paramMap);
	}

	/**
	 * 设置状态栏背景状态
	 */
	private void setTranslucentStatus() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			Window win = getWindow();
			WindowManager.LayoutParams winParams = win.getAttributes();
			final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
			winParams.flags |= bits;
			win.setAttributes(winParams);
		}
		SystemStatusManager tintManager = new SystemStatusManager(this);
		tintManager.setStatusBarTintEnabled(true);
		tintManager.setStatusBarTintResource(R.color.home);// 状态栏无背景
	}

	@Override
	public boolean hasInternetConnected() {
		// TODO Auto-generated method stub
		return super.hasInternetConnected();
	}

	public static Date string2Date(String strTime, String formatType)
			throws ParseException {
		SimpleDateFormat formatter = new SimpleDateFormat(formatType);
		Date date = null;
		try {
			date = formatter.parse(strTime);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return date;
	}

	@SuppressLint("NewApi")
	@Override
	protected void onResume() {
		// sv = (ScrollViewExtend) findViewById(R.id.sv_index);
		super.onResume();

		MobclickAgent.onPageStart(mPageName);
		MobclickAgent.onResume(this);
		initUser();
		iniCHKInfo();

		if (hasInternetConnected()) {
			if (listFeeds.size() > 0) {
				listFeeds.clear();
			}
			getYsq();

			searchlist001();
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//			String date = df.format(new Date());// new Date()为获取当前系统时间
//			searchAds(date);
			searchHospital();
			TipsInquiry();

			if (user.UserID != 0) {
				getTime();


			} else {
//				tvScore.setText("0积分");
//				tvsign.setText("签到");


				tvsign.setBackgroundResource(R.drawable.bg_icon_sign);
//				tvsign.setTextColor(Color.parseColor("#ff799c"));

			}
			// mRoundProgressBar.setSweepAngle(per);
		} else {
			Toast.makeText(Index.this, string.msgUninternet,
					Toast.LENGTH_SHORT).show();
		}

		// TODO Auto-generated method stub

		sv.post(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				sv.scrollTo(scrollX, scrollY);

			}
		});
	}

	private void getPonit(String startTime, String endTime) {
		JsonAsyncTaskOnComplete doProcess = new JsonAsyncTaskOnComplete() {

			@Override
			public void processJsonObject(Object result) {
				// TODO Auto-generated method stub
				String value = result.toString();

				JSONObject mySO = (JSONObject) result;
				try {
					JSONArray array = mySO.getJSONArray("UserPointLogInquiry");
					if (array.getJSONObject(0).has("MessageCode")) {
//						tvsign.setText("签到");
//						tvScore.setVisibility(View.VISIBLE);

						tvsign.setBackgroundResource(R.drawable.bg_icon_sign);
//						tvsign.setTextColor(Color.parseColor("#ff799c"));
					} else {
						String time = array.getJSONObject(0).get("CreatedDate")
								.toString();
						SimpleDateFormat formatter = new SimpleDateFormat(
								"yyyy-MM-dd");
						time = time.split(" ")[0].replace("/", "-");
						Date dNow = new Date();
						String nowTime = formatter.format(dNow.getTime());
						time = stringToDate2(time, "yyyy-MM-dd");
						if (time.equals(MyApplication.getInstance().nowtime)) {
//							tvsign.setText("已签到");


							tvsign.setBackgroundResource(R.drawable.bg_icon_no_sign);
//							tvsign.setTextColor(Color.parseColor("#ffffff"));

						} else {
//							tvsign.setText("签到");


							tvsign.setBackgroundResource(R.drawable.bg_icon_sign);
//							tvsign.setTextColor(Color.parseColor("#ff799c"));

						}
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					tvsign.setBackgroundResource(R.drawable.bg_icon_sign);
//					tvsign.setTextColor(Color.parseColor("#ff799c"));
				}
			}

		};
		JsonAsyncTask_Info task = new JsonAsyncTask_Info(Index.this, true,
				doProcess);
		Map<String, Object> paramMap = new HashMap<String, Object>();
		String str = "<Request><UserID>%s</UserID><StartTime>%s</StartTime><EndTime>%s</EndTime><Remark>%s</Remark></Request>";
		str = String.format(str, new Object[]{user.UserID + "", startTime,
				endTime, "签到送积分"});

		paramMap.put("str", str);
		task.execute(SOAP_NAMESPACE, SOAP_LOGACTION, SOAP_LOGMETHODNAME,
				SOAP_URL, paramMap);

	}

	// 返回string类型时间
	public static String stringToDate2(String strTime, String formatType)
			throws ParseException {
		SimpleDateFormat formatter = new SimpleDateFormat(formatType);
		Date date = null;
		try {
			date = formatter.parse(strTime);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return formatter.format(date);
	}

	private void getUserInqury() {
		// TODO Auto-generated method stub
		JsonAsyncTaskOnComplete doProcess = new JsonAsyncTaskOnComplete() {

			@Override
			public void processJsonObject(Object result) {
				// {"UserPointAdd":[{"MessageContent":"成功！","MessageCode":"0"}]}
				// TODO Auto-generated method stub
				// {"UserPointLogInquiry":[{"Value":"5","UpdatedDate":"2016\/1\/18
				// 14:16:40","DeleteFlag":"0","CreatedDate":"2016\/1\/18
				// 14:16:40","CreatedBy":"","ID":"3","UserID":"50694","UpdatedBy":"","Remark":"签到送积分"}]}
				String value = result.toString();
				// {"UserInquiry":[{"UserSex":"01","UserAge":"33","PictureURL":"~\/UploadFile\/image\/2016030109454450694.jpg","NickName":"宝爸20153028230623hhhh","UserNO":"410102198201251510","UserID":"50694","YuBirthTime":"2016\/10\/25
				// 0:00:00","RealName":"郭芮","Point":"60","ClientID":"0c1726ae905edaa84b2b26e794910ab1","City":"郑州"}]}
				JSONObject mySO = (JSONObject) result;
				try {
					JSONArray array = mySO.getJSONArray("UserInquiry");
//					tvScore.setText(array.getJSONObject(0).getString("Point")
//							.toString()+"积分"
//							);
					if(!TextUtils.isEmpty(array.getJSONObject(0)
							.getString("IsTwins").toString()))
						user.isTwins=Integer.valueOf(array.getJSONObject(0)
								.getString("IsTwins").toString());
					LocalAccessor.getInstance(Index.this).updateUser(user);

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		};
		JsonAsyncTask_Info task = new JsonAsyncTask_Info(Index.this, true,
				doProcess);
		Map<String, Object> paramMap = new HashMap<String, Object>();
		String str = "<Request><UserID>%s</UserID></Request>";
		str = String.format(str, new Object[]{user.UserID + "",});

		paramMap.put("str", str);
		task.execute(SOAP_NAMESPACE, SOAP_INQUIRYACTION,
				SOAP_INQUIRYMETHODNAME, SOAP_URL, paramMap);
	}

	private void initUser() {
		user = LocalAccessor.getInstance(Index.this).getUser();
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(mPageName);
		MobclickAgent.onPause(this);

	}

	@TargetApi(Build.VERSION_CODES.M)
	@Override
	protected void iniView() {
		// TODO Auto-generated method stub
		refreshview = (RefreshLayout) findViewById(R.id.refreshindex);
		sv = (UpScrollViewExtend) findViewById(R.id.sv_index);


		txtcontent = (TextView) findViewById(R.id.txt_index_content);

		//imgcontent 隐藏
		imgcontent = (ImageView) findViewById(R.id.img_index_content);
		imgcontent.bringToFront();

		inflater = LayoutInflater.from(this);
		list002 = (ListViewForScrollView) findViewById(R.id.listview_index002);
		tvyesterday = (TextView) findViewById(R.id.tvyesterday);
		tvnext = (TextView) findViewById(R.id.tvnext);
		tvcenter = (TextView) findViewById(R.id.tvcenter);
		tvchildprogress = (TextView) findViewById(R.id.tvchildprogress);

		txtmore = (TextView) findViewById(R.id.txtmore_index);
		imgMore = (LinearLayout) findViewById(R.id.imgmore_index);
		listsq = (ListViewForScrollView) findViewById(R.id.listview_indexsq);

		mAdView = (ImageCycleView) findViewById(R.id.ad_view);

		linear001 = (LinearLayout) findViewById(R.id.linear001_index);
		mHosView = (MyViewPager) findViewById(R.id.ad_hospital);
		llhos = (LinearLayout) findViewById(R.id.llhos);
		mYsQView = (ImageCycleView) findViewById(R.id.ad_ysq);

		ivleft = (ImageView) findViewById(R.id.ivleft);
		ivright = (ImageView) findViewById(R.id.ivright);
		tv_viewhos = (TextView) findViewById(R.id.tv_viewhos);
		chktimell = (LinearLayout) findViewById(R.id.chk_time);
		tvScore = (TextView) findViewById(R.id.tvscore);
		tvsign = (ImageView) findViewById(R.id.tvsign);
		imgYsq = (LinearLayout) findViewById(R.id.imgmore_more);
//		ivprogress=(ImageView)findViewById(R.id.ivprogress);
		rltop = (LinearLayout) findViewById(R.id.rl_top);
		rlchktime = (RelativeLayout) findViewById(R.id.rlchktime);
		ivsearch = (LinearLayout) findViewById(R.id.iv_search);
		tvkey = (TextView) findViewById(R.id.tvkey);
		rlchktiem = (RelativeLayout) findViewById(R.id.rlchktiem);


		llchildleft = (LinearLayout) findViewById(R.id.llchildleft);
		llchildright = (LinearLayout) findViewById(R.id.llchildright);
		tvgotaday = (ImageView) findViewById(R.id.tvgotaday);
		tvchao = (TextView) findViewById(R.id.tvchao);


//		chilidViewPager=new ChilidViewPager(Index.this,txt001);
		refreshview.setOnRefreshListener(this);
		mViewPager = (DetialGallery) findViewById(R.id.lltopview);
		mViewPager.setSpacing(50 * ScreenUtils.getScreenHeight(Index.this) / 1080);
//		mViewPager.setSpacing(10 * ScreenUtils.getScreenHeight(Index.this) / 1080);
		tvday = (TextView) findViewById(R.id.tv_day);
		mTextLenth = (TextView) findViewById(R.id.tvlength);
		mTextWeight = (TextView) findViewById(R.id.tvweight);
		tvyuyue=(TextView) findViewById(R.id.tvyuyue);
		lltime=(LinearLayout)findViewById(R.id.lltime);
//		LayoutParams para;
//		para =  mViewPager.getLayoutParams();
//		para.width = Util.getScreenWidth(Index.this);
//		para.height = Util.getScreenWidth(Index.this)*498/720;
//
//		mViewPager.setLayoutParams(para);

//		UiUtils.resetRL(Index.this,txt001);

//		chilidViewPager=(ChilidViewPager)findViewById(R.id.childview);

	}


	private void initViewAds() {
		// layoutAds = (LinearLayout) inflater
		// .inflate(
		// R.layout.index_ads,
		// null,
		// true);
		// mHosView=(MyViewPager)findViewById(R.id.ad_hospital);

		for (int i = 0; i < hosList.size(); i++) {
			View view = inflater.inflate(R.layout.hospital_viewpager_layout,
					null);

			ImageView iv_pic1 = (ImageView) view.findViewById(R.id.iv_pic1);
			ImageLoader.getInstance().displayImage(
					hosList.get(i).get("picUrl") + "", iv_pic1);
			TextView tv_hosname1 = (TextView) view
					.findViewById(R.id.tv_hosname1);
			final String hosName1 = hosList.get(i).get("HospitalName") + "";
			tv_hosname1.setText(hosName1);
			final String hosID1 = hosList.get(i).get("HospitalID").toString();
			iv_pic1.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					Intent intent = new Intent(Index.this, CHKTimeList.class);
					intent.putExtra("HospitalID", hosID1);
					intent.putExtra("name", hosName1);
					startActivity(intent);
				}
			});

			i++;

			ImageView iv_pic2 = (ImageView) view.findViewById(R.id.iv_pic2);
			TextView tv_hosname2 = (TextView) view
					.findViewById(R.id.tv_hosname2);
			if (i < hosList.size()) {
				ImageLoader.getInstance().displayImage(
						hosList.get(i).get("picUrl") + "", iv_pic2);

				final String hosName2 = hosList.get(i).get("HospitalName") + "";
				tv_hosname2.setText(hosName2);
				final String hosID2 = hosList.get(i).get("HospitalID")
						.toString();

				iv_pic2.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						Intent intent = new Intent(Index.this,
								CHKTimeList.class);
						intent.putExtra("HospitalID", hosID2);
						intent.putExtra("name", hosName2);
						startActivity(intent);
					}
				});
			} else {
				iv_pic2.setVisibility(View.INVISIBLE);
				tv_hosname2.setVisibility(View.INVISIBLE);

			}
			views.add(view);

		}
		vpAdapter = new MyViewPageAdapter(views);
		mHosView.setAdapter(vpAdapter);


	}

	// void initHeadImage() {
	// layoutInflater = (LayoutInflater)
	// getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	// View headview = layoutInflater.inflate(R.layout.index_ads, null);
	//
	// mHosView=(ViewPager) headview.findViewById(R.id.ad_hospital);
	// for(int i=0;i<hosiSize;i++){
	// View view
	// =inflater.inflate(R.layout.hospital_viewpager_layout, null);
	// ImageView iv_pic=(ImageView) view.findViewById(R.id.iv_pic);
	// ImageLoader.getInstance().displayImage(myApplication.arrayList3.get(i).get("picUrl")+"",
	// iv_pic);
	// TextView tv_hosname=(TextView) view.findViewById(R.id.tv_hosname);
	// tv_hosname.setText(myApplication.arrayList3.get(i).get("HospitalName")+"");
	// views.add(view);
	//
	// }
	// vpAdapter = new MyViewPageAdapter(views);
	// mHosView.setAdapter(vpAdapter);
	//
	// }

	private void showADs() {

		mAdView.setVisibility(View.VISIBLE);


		mAdView.setImageResources(mImageUrl, mObject,
				new ImageCycleViewListener() {

					@Override
					public void displayImage(String imageURL,
											 ImageView imageView) {
						try {
							ImageLoader.getInstance().displayImage(imageURL,
									imageView);
							LayoutParams para;
							para = imageView.getLayoutParams();
							para.width = Util.getScreenWidth(Index.this);
							para.height = para.width * 2 / 5;
							imageView.setLayoutParams(para);
						} catch (OutOfMemoryError e) {
							// TODO Auto-generated catch block

						}

					}

					@Override
					public void onImageClick(int position, View imageView,
											 ArrayList<Map<String, Object>> mObject) {
						// TODO Auto-generated method stub
						// Toast.makeText(getApplicationContext(),
						// "type"+mType.get(position), 1).show();
						String p = mObject.get(position).get("Position")
								.toString();
						// P0005 小贴士
						// P0006 热点
						// P0007 个人中心
						// P0008 妈妈足迹
						// P0009 微专题
						// P0010 医生首页
						// P0011 医生文章
						// P0012 孕健康
						// P0013 话题
						// P0014 商城
						// P0015 商品购买
						// P0016 选择医生
						// smartPush第三方回执调用接口，actionid范围为90000-90999，可根据业务场景执行
						if (p.equals("P0005")) {
							Intent intent = new Intent(Index.this,
									TestWebView.class);
							intent.putExtra("YunWeek", yu.Week + 1 + "");
							startActivity(intent);


						} else if (p.equals("P0003") || p.equals("P0006") || p.equals("P0004")) {
							String ID = mObject.get(position).get("ID")
									.toString();
							int linkid = Integer.valueOf(ID);
							if (linkid != 0) {
								Intent i = new Intent(Index.this,
										News_Detail.class);
								i.putExtra("ID", ID);
								startActivity(i);
							}
						} else if (p.equals("P0007")) {

							Intent i = new Intent(Index.this, MainTabActivity.class);
							i.putExtra("TabIndex", "D_TAB");

							startActivity(i);

						} else if (p.equals("P0008")) {
							if (user.UserID == 0) {

								startActivity(new Intent(Index.this, LoginActivity.class));
							} else {
								Intent i = new Intent(Index.this,
										MyCHK_Timeline.class);
								i.putExtra("Page", "User_Foot");
								startActivity(i);
							}
						} else if (p.equals("P0009")) {


							String ID = mObject.get(position).get("ID")
									.toString();
							int linkid = Integer.valueOf(ID);
							if (linkid != 0) {
								Intent i = new Intent(Index.this, SmallTalkAct.class);
								i.putExtra("ID", ID);
								startActivity(i);
							}


						} else if (p.equals("P0010")) {


							String ID = mObject.get(position).get("ID")
									.toString();
							int linkid = Integer.valueOf(ID);
							if (linkid != 0) {
								Intent i = new Intent(Index.this,
										DoctorHomeAct.class);
								i.putExtra("DoctorID", ID);
								startActivity(i);
							}


						} else if (p.equals("P0016")) {
							String ID = mObject.get(position).get("ID")
									.toString();
							int linkid = Integer.valueOf(ID);
							if (linkid != 0) {
								Intent i = new Intent(Index.this,
										CHK_DoctorList.class);
								i.putExtra("HostiptalID", ID);
								i.putExtra("selectdoc", "select");
								startActivity(i);
							}

						} else if (p.equals("P0011")) {

							String ID = mObject.get(position).get("ID")
									.toString();
							int linkid = Integer.valueOf(ID);
							if (linkid != 0) {
								Intent i = new Intent(Index.this,
										Activity_ReplyPage.class);
								i.putExtra("ID", ID);

								startActivity(i);
							}


						} else if (p.equals("P0017")) {
//							https://lovestaging.topmd.cn/html/1.html
							String ID = mObject.get(position).get("ID")
									.toString();
							Intent i = new Intent(Index.this,
									StaticWebView.class);
							i.putExtra("ID", ID);

							startActivity(i);


						}

					}

				});
		mAdView.startImageCycle();

		sv.smoothScrollTo(0, 0);
	}
	//显示孕社区的数据
	private void showYsq() {
		mYsQView.setVisibility(View.VISIBLE);
		mYsQView.setImageResources(mImageUrl2, mObject2,
				new ImageCycleViewListener() {

					@Override
					public void displayImage(String imageURL,
											 ImageView imageView) {
						try {
							ImageLoader.getInstance().displayImage(imageURL,
									imageView);
							LayoutParams para;
							para = imageView.getLayoutParams();
							para.width = Util.getScreenWidth(Index.this);
							para.height = para.width * 2 / 5;
							imageView.setLayoutParams(para);
						} catch (OutOfMemoryError e) {
							// TODO Auto-generated catch block

						}

					}

					@Override
					public void onImageClick(int position, View imageView,
											 ArrayList<Map<String, Object>> mObject) {
						// TODO Auto-generated method stub
						// Toast.makeText(getApplicationContext(),
						// "type"+mType.get(position), 1).show();
						String p = mObject.get(position).get("Position")
								.toString();
						// P0005 小贴士
						// P0006 热点
						// P0007 个人中心
						// P0008 妈妈足迹
						// P0009 微专题
						// P0010 医生首页
						// P0011 医生文章
						// P0012 孕健康
						// P0013 话题
						// P0014 商城
						// P0015 商品购买
						// P0016 选择医生
						// smartPush第三方回执调用接口，actionid范围为90000-90999，可根据业务场景执行
						if (p.equals("P0005")) {
							Intent intent = new Intent(Index.this,
									TestWebView.class);
							intent.putExtra("YunWeek", yu.Week + 1 + "");
							startActivity(intent);


						} else if (p.equals("P0003") || p.equals("P0006") || p.equals("P0004")) {
							String ID = mObject.get(position).get("ID")
									.toString();
							int linkid = Integer.valueOf(ID);
							if (linkid != 0) {
								Intent i = new Intent(Index.this,
										News_Detail.class);
								i.putExtra("ID", ID);
								startActivity(i);
							}
						} else if (p.equals("P0007")) {

							Intent i = new Intent(Index.this, MainTabActivity.class);
							i.putExtra("TabIndex", "D_TAB");

							startActivity(i);

						} else if (p.equals("P0008")) {
							if (user.UserID == 0) {

								startActivity(new Intent(Index.this, LoginActivity.class));
							} else {
								Intent i = new Intent(Index.this,
										MyCHK_Timeline.class);
								i.putExtra("Page", "User_Foot");
								startActivity(i);
							}
						} else if (p.equals("P0009")) {


							String ID = mObject.get(position).get("ID")
									.toString();
							int linkid = Integer.valueOf(ID);
							if (linkid != 0) {
								Intent i = new Intent(Index.this, SmallTalkAct.class);
								i.putExtra("ID", ID);
								startActivity(i);
							}


						} else if (p.equals("P0010")) {


							String ID = mObject.get(position).get("ID")
									.toString();
							int linkid = Integer.valueOf(ID);
							if (linkid != 0) {
								Intent i = new Intent(Index.this,
										DoctorHomeAct.class);
								i.putExtra("DoctorID", ID);
								startActivity(i);
							}


						} else if (p.equals("P0016")) {
							String ID = mObject.get(position).get("ID")
									.toString();
							int linkid = Integer.valueOf(ID);
							if (linkid != 0) {
								Intent i = new Intent(Index.this,
										CHK_DoctorList.class);
								i.putExtra("HostiptalID", ID);
								i.putExtra("selectdoc", "select");
								startActivity(i);
							}

						} else if (p.equals("P0011")) {

							String ID = mObject.get(position).get("ID")
									.toString();
							int linkid = Integer.valueOf(ID);
							if (linkid != 0) {
								Intent i = new Intent(Index.this,
										Activity_ReplyPage.class);
								i.putExtra("ID", ID);

								startActivity(i);
							}


						} else if (p.equals("P0017")) {

							String ID = mObject.get(position).get("ID")
									.toString();
							Intent i = new Intent(Index.this,
									StaticWebView.class);
							i.putExtra("ID", ID);

							startActivity(i);


						}

					}

				});
		mYsQView.startImageCycle();

	}


	private void iniCHKInfo() {
		DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
		DateFormat fmt3 = new SimpleDateFormat("yyyy年MM月dd");
		DateFormat fmt2 = new SimpleDateFormat("M月d日");
		Date date;
		try {
			String yubirth = user.YuBirthDate;
			date = fmt.parse(user.YuBirthDate);

			dYun = fmt.parse(fmt.format(new Date()));

			yu = new Yuchan();
			days = IOUtils.DaysCount(date);

			yu = IOUtils.WeekInfo(date);
			week = yu.Week;
			String time = yu.Week + "周+" + yu.Days;
//			String d = fmt2.format(new Date());

			if (week >= 40) {
				txtmore.setText("40周提醒");
				week = 39;
			} else {
				txtmore.setText((yu.Week)+1 + "周提醒");
//				txtmore.setText("一周提醒");
			}
			per = (int) ((280 - days) / 2.8);

			if (100 < per) {
				per = 100;
			}
			if (per <= 0) {
				per = 1;
			}

			if (days > 0) {
				childday = 280 - days;


//				String span = "距离宝宝出生还有" + days + "天";
//				SpannableString style = new SpannableString(span);
//				int bstart = span.indexOf(days + "天");
//				int bend = bstart + (days + "天").length()-1;
//
//				style.setSpan(
//						new TextAppearanceSpan(this, R.style.styleindex), bstart, bend, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//				txt001.setText(style);
				// txt001.setText("距离宝宝出生还有" + days + "天");
//				if(yu.Week==0){
//					String spantxtyun = "您已经怀孕"  + yu.Days + "天";
//					SpannableString styleyun = new SpannableString (
//							spantxtyun);
//					int bstartyun = spantxtyun.indexOf( yu.Days
//							+ "天");
//					int bendyun = bstartyun
//							+ ( yu.Days + "天").length();
//
//
//					styleyun.setSpan(new TextAppearanceSpan(this, R.style.styleindex), bstartyun, bendyun, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//					txt_index_002.setText(yu.Days + "天");

//				}else if(yu.Days==0){
//					String spantxtyun = "您已经怀孕" + yu.Week + "周" ;
//					SpannableString styleyun = new SpannableString(
//							spantxtyun);
//					int bstartyun = spantxtyun.indexOf(yu.Week + "周" );
//					int bendyun = bstartyun
//							+ (yu.Week + "周" ).length();
//
//					styleyun.setSpan(new TextAppearanceSpan(this, R.style.styleindex), bstartyun, bendyun, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//					txt_index_002.setText(yu.Week + "周");

//				}else{
//				String spantxtyun = "您已经怀孕" + yu.Week + "周" + yu.Days + "天";
//					SpannableString styleyun = new SpannableString(
//						spantxtyun);
//				int bstartyun = spantxtyun.indexOf(yu.Week + "周" + yu.Days
//						+ "天");
//				int bendyun = bstartyun
//						+ (yu.Week + "周" + yu.Days + "天").length();
//
//				styleyun.setSpan(new TextAppearanceSpan(this, R.style.styleindex), bstartyun, bendyun, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//					txt_index_002.setText(yu.Week + "周" + yu.Days + "天");
//				}
				mViewPager.setSelection(childday - 1);

				llchildright.setVisibility(View.VISIBLE);
				llchildleft.setVisibility(View.VISIBLE);
				setTitle(dYun);

			} else if (days == 0) {
				childday = 280;

//				txt001.setText("恭喜您，您的宝宝马上就要出生了！");

//				txt_index_002.setText("40周");

				mViewPager.setSelection(279);
				llchildright.setVisibility(View.INVISIBLE);
				llchildleft.setVisibility(View.VISIBLE);
//				dYun=fmt.parse(user.YuBirthDate);
				setTitle(dYun);

			} else {
//				txt001.setText("恭喜您，您的宝宝已经出生了！");
//				txt_index_002.setText("40周");

				childday = 280;
				mViewPager.setSelection(280);
				llchildright.setVisibility(View.INVISIBLE);
				llchildleft.setVisibility(View.VISIBLE);
				isAlready=true;
//				dYun=fmt.parse(user.YuBirthDate);
				setTitle(dYun);
			}


//			if (per == 100) {
			// mRoundProgressBar.setText("40周");
//				mRoundProgressBar.setText2(per + "%");
//				tvchildprogress.setText("成长进度"+per+"%");
//			} else {
			// mRoundProgressBar.setText(time);
//				mRoundProgressBar.setText2(per + "%");
//				tvchildprogress.setText("成长进度"+per+"%");
//			}
//			mRoundProgressBar.setText("");


			// txt0001.setText(fmt3.format(IOUtils.getNextDay(new Date(), -1)));
			// txt0002.setText(fmt3.format(IOUtils.getNextDay(new Date(), 1)));


//			changeChildProgress(ivprogress,yu.Week);
			if (yu.Week < 40) {
				searchIndexOrder();
			} else if (yu.Week >= 40) {
				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
						LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
				rlchktime.setVisibility(View.GONE);
				mHosView.setVisibility(View.VISIBLE);
				tv_viewhos.setVisibility(View.VISIBLE);
				llhos.setVisibility(View.VISIBLE);
				ivleft.setVisibility(View.VISIBLE);
				ivright.setVisibility(View.VISIBLE);
				tv_viewhos.setText("");
				chktimell.setVisibility(View.GONE);
			}
			tvgotaday.setVisibility(View.INVISIBLE);

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void searchIndexOrder() {
		if (user.UserID != 0) {
			// Toast.makeText(this, "uuudingdanuu"+user.UserID,
			// 1).show();
			searchOrder();
		} else {
			// Toast.makeText(this,
			// "wwwwwwdingdan"+user.UserID+":user.HospitalName"+user.HospitalName,
			// 1).show();
			// initViewAds();
			rlchktime.setVisibility(View.GONE);
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);


			mHosView.setVisibility(View.VISIBLE);
			tv_viewhos.setVisibility(View.VISIBLE);
			llhos.setVisibility(View.VISIBLE);
			ivleft.setVisibility(View.VISIBLE);
			ivright.setVisibility(View.VISIBLE);
			tv_viewhos.setText("");
			chktimell.setVisibility(View.GONE);
		}
	}

	//	private void changeChildProgress(ImageView ivprogress, int week) {
//		if(week>0) {
//			if(week<40) {
//				ivprogress.setBackgroundResource(weekIcons[week - 1]);
//			}else{
//				ivprogress.setBackgroundResource(weekIcons[39]);
//			}
//
//		}else{
//			ivprogress.setBackgroundResource(weekIcons[0]);
//		}
//
//
//	}
	private void setTitle(Date d) {
		DateFormat fmt2 = new SimpleDateFormat("M月d日");

			tvyesterday.setText(fmt2.format(d.getTime() - 24 * 60 * 60 * 1000));

			tvcenter.setText(fmt2.format(d.getTime()));
			tvnext.setText(fmt2.format(d.getTime() + 24 * 60 * 60 * 1000));


//				tvyesterday.setText("10月15日");
//		tvcenter.setText("10月15日");
//		tvnext.setText("10月15日");
	}

	private void setListeners() {
		// tab003.setOnClickListener(new View.OnClickListener()
		// {
		//
		// @Override
		// public void onClick(View v)
		// {
		// // TODO Auto-generated method stub
		// Intent i = new Intent(Index.this, CHKTimeList.class);
		// startActivity(i);
		// //
		// // Index.this.overridePendingTransition(R.anim.in_from_left,
		// // R.anim.out_of_right);
		// }
		// });
		ImgIndexContent();
		llchildleft.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				left();
			}
		});
		llchildright.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				right();
			}
		});
		tvgotaday.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				iniCHKInfo();
			}
		});
		ivsearch.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(Index.this, SearchWebView.class));
			}
		});
		rlchktiem.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(Index.this, CHKTimeList.class);
				startActivity(i);
			}
		});

		imgMore.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(Index.this, News_List.class);
				// i.putExtra("Week", week);
				startActivity(i);
				//
				// Index.this.overridePendingTransition(R.anim.in_from_left,
				// R.anim.out_of_right);
			}
		});
		imgYsq.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(Index.this, MainTabActivity.class);
				i.putExtra("TabIndex", "B_TAB");

				startActivity(i);
			}
		});

//		rlcontnet.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				Intent intent = new Intent(Index.this, TestWebView.class);
//				if(childday/7>39){
//					intent.putExtra("YunWeek", 40+ "");
//				}else{
//					intent.putExtra("YunWeek", (childday/7 + 1) + "");
//				}
//				startActivity(intent);
//			}
//		});

		sv.setOnTouchListener(new OnTouchListener() {
			private int lastY = 0;
			private int touchEventId = -9983761;
			Handler handler = new Handler() {
				@Override
				public void handleMessage(Message msg) {
					super.handleMessage(msg);
					View scroller = (View) msg.obj;

					if (msg.what == touchEventId) {
						if (lastY == scroller.getScrollY()) {
							//停止了，此处你的操作业务
							scrollX = sv.getScrollX();
							scrollY = sv.getScrollY();
						} else {
							handler.sendMessageDelayed(handler.obtainMessage(touchEventId, scroller), 1);
							lastY = scroller.getScrollY();
						}
					}
				}
			};


			@Override
			public boolean onTouch(View v, MotionEvent event) {

				int eventAction = event.getAction();
				int y = (int) event.getRawY();
				switch (eventAction) {
					case MotionEvent.ACTION_UP:

						handler.sendMessageDelayed(handler.obtainMessage(touchEventId, v), 5);

						break;
					default:
						break;
				}
				return false;
			}


		});

	}

	/*
	 * 查询订单信息
	 */
	private void searchOrder() {
		try {
			JsonAsyncTaskOnComplete doProcess = new JsonAsyncTaskOnComplete() {
				public void processJsonObject(Object result) {
					returnvalue001 = result.toString();
					if (returnvalue001 == null) {
						rlchktime.setVisibility(View.GONE);
						LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
								LayoutParams.MATCH_PARENT,
								LayoutParams.WRAP_CONTENT);


						mHosView.setVisibility(View.VISIBLE);
						llhos.setVisibility(View.VISIBLE);
						tv_viewhos.setVisibility(View.VISIBLE);
						ivleft.setVisibility(View.VISIBLE);
						ivright.setVisibility(View.VISIBLE);
						tv_viewhos.setText("");
						chktimell.setVisibility(View.GONE);
					} else {
						Map<String, Object> map;

						// 下边是具体的处理
						try {
							JSONObject mySO = (JSONObject) result;
							JSONArray array = mySO
									.getJSONArray("OrderInquiryForIndex");

							if (array.getJSONObject(0).has("MessageCode")) {
								// list002.setAdapter(null);
								if (yu.Week < 11) {
									mHosView.setVisibility(View.GONE);
									tv_viewhos.setVisibility(View.GONE);

									llhos.setVisibility(View.GONE);
									ivleft.setVisibility(View.GONE);
									ivright.setVisibility(View.GONE);
									rlchktime.setVisibility(View.VISIBLE);

									TextView txt_index_003 = (TextView)Index.this.findViewById(R.id.yuyuelabel);
									TextView tv_yuyue= (TextView)Index.this.findViewById(R.id.yuyuebtn);
									tv_yuyue.setBackgroundResource(R.color.yuyuebtncolor2);
									tv_yuyue.setText("待检查");
									linear001.setClickable(false);

									txt_index_003
											.setText("妈妈正处于孕早期，如果没有特别的身体不适，此阶段不需要做频繁的孕期检查！");
//									ImageView iv_right = (ImageView) layout
//											.findViewById(R.id.in_right);
//									iv_right.setVisibility(View.INVISIBLE);
									chktimell.setVisibility(View.VISIBLE);

								} else {
									rlchktime.setVisibility(View.GONE);
									mHosView.setVisibility(View.VISIBLE);
									tv_viewhos.setVisibility(View.VISIBLE);
									llhos.setVisibility(View.VISIBLE);
									ivleft.setVisibility(View.VISIBLE);
									ivright.setVisibility(View.VISIBLE);
									tv_viewhos.setText("");
									chktimell.setVisibility(View.GONE);
								}
							} else {

								// 如果已经做过检查，订单ID非空
								if (array.getJSONObject(0).has("OrderID")) {

									for (int i = 0; i < array.length(); i++) {

										if (i == 0) {
											String time = array
													.getJSONObject(i)
													.getString("Gotime");
											int days = IOUtils.DaysCount2(time);
											if (0 <= days) {

												tv_viewhos
														.setText(("还有"
																+ days
																+ "天就要进行"
																+ array.getJSONObject(
																i)
																.getString(
																		"CHKType") + "了"));

												//bezhiceshihahahahahhfouqgweofgqouwegfouqwyegfouqgweouyfgoqwdbcljhasbdkcjhufqowegfouqwegou
//												LinearLayout layout = (LinearLayout) inflater
//														.inflate(
//																R.layout.item_index_04,
//																null, true);
//
//												TextView txt_index_003 = (TextView) layout
//														.findViewById(R.id.txt_index_003);
//												TextView txt_index_004 = (TextView) layout
//														.findViewById(R.id.txt_index_004);
//												TextView txt_index_005 = (TextView) layout
//														.findViewById(R.id.txt_index_005);
//
//												txt_index_003
//														.setText(array
//																.getJSONObject(
//																		i)
//																.getString(
//																		"HospitalName"));
//
//												txt_index_004
//														.setText(array
//																.getJSONObject(
//																		i)
//																.getString(
//																		"DoctorName"));
//gqerwgwqeuf8qweugfwefoi ufhpqiweugfopi	ugu7
//												txt_index_005
//														.setText(IOUtils.formatStrDate(array.getJSONObject(i).getString("Gotime"))
//																+ " "
//																+ array.getJSONObject(
//																i)
//																.getString(
//																		"BeginTime")
//																+ "-"
//																+ array.getJSONObject(
//																i)
//																.getString(
//																		"EndTime"));
//
//												TableRow row001 = (TableRow) layout
//														.findViewById(R.id.tab_index_view001);
//

//												row001.setOnClickListener(new OnClickListener() {
//													@Override
//													public void onClick(View v) {
//														// TODO Auto-generated
//														// method stub
//														Intent i = new Intent(
//																Index.this,
//																MyCHK_OrderDetail.class);
//														i.putExtra("ID", ID);
//														startActivity(i);
//
//													}
//												});
//
//												LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
//														LayoutParams.FILL_PARENT,
//														LayoutParams.WRAP_CONTENT);
//												linear001.removeAllViews();
//												linear001.addView(layout, lp);
												String label = array.getJSONObject(i).getString("HospitalName")+"		"+array.getJSONObject(i).getString("DoctorName")+
														"		"+IOUtils.formatStrDate(array.getJSONObject(i).getString("Gotime"))+"	"+array.getJSONObject(i).getString("BeginTime")+"-"+array.getJSONObject(i).getString("EndTime");
												TextView txt_index_003 = (TextView)Index.this.findViewById(R.id.yuyuelabel);
												TextView tv_yuyue= (TextView)Index.this.findViewById(R.id.yuyuebtn);
												tv_yuyue.setText("待产检");
												tv_yuyue.setBackgroundResource(R.color.yuyuebtncolor1);
												txt_index_003
														.setText(label);
												linear001.setClickable(true);
												final String ID = array
														.getJSONObject(i)
														.getString("ID");
												linear001.setOnClickListener(new OnClickListener() {
													@Override
													public void onClick(View v) {
														// TODO Auto-generated
														// method stub
														Intent i = new Intent(
																Index.this,
																MyCHK_OrderDetail.class);
														i.putExtra("ID", ID);
														startActivity(i);

													}
												});


											}
										}
										break;

									}
								}
								// 如果没做过预约，ORderID为空
								else {

									for (int i = 0; i < array.length(); i++) {

										TextView txt_index_003 = (TextView)Index.this.findViewById(R.id.yuyuelabel);
										TextView tv_yuyue= (TextView)Index.this.findViewById(R.id.yuyuebtn);
										tv_yuyue.setBackgroundResource(R.color.yuyuebtncolor3);
										tv_yuyue.setText("快速预约");
										final String CHKTypeName;
										if (array
												.getJSONObject(i)
												.getString("WeekStart")
												.equals(array.getJSONObject(i)
														.getString("WeekEnd"))) {
											// CHKTypeName = array
											// .getJSONObject(i).getString(
											// "CHKType")
											// + "("
											// + array.getJSONObject(i)
											// .getString("WeekStart")
											// + "周)";
											CHKTypeName = "第"
													+ array.getJSONObject(i)
													.getString(
															"WeekStart")
													+ "周产检";
										} else {
											// CHKTypeName = array
											// .getJSONObject(i).getString(
											// "CHKType")
											// + "("
											// + array.getJSONObject(i)
											// .getString("WeekStart")
											// + "-"
											// + array.getJSONObject(i)
											// .getString("WeekEnd")
											// + "周)";
											CHKTypeName = "第("
													+ array.getJSONObject(i)
													.getString(
															"WeekStart")
													+ "-"
													+ array.getJSONObject(i)
													.getString(
															"WeekEnd")
													+ "周)产检";
										}

										String str001 = "您马上就该进行"
												+ CHKTypeName
												+ "了，点这里快速预约。";
										int bstart = str001
												.indexOf(CHKTypeName);
										int bend = bstart
												+ CHKTypeName.length();
										SpannableString style = new SpannableString(
												str001);
										style.setSpan(
												new TextAppearanceSpan(Index.this, R.style.stylecolor),
												bstart,
												bend,
												Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
										txt_index_003.setText(style);
//										TableRow row001 = (TableRow) layout
//												.findViewById(R.id.tab_index_view001);
//
//										final String ID = array
//												.getJSONObject(i).getString(
//														"ID");
//										row001.setOnClickListener(new OnClickListener() {
//											@Override
//											public void onClick(View v) {
//												// TODO Auto-generated method
//												// stub
//												Intent i = new Intent(
//														Index.this,
//														CHKTimeDetailActivity.class);
//												i.putExtra("ID", ID);
//												i.putExtra("CHKType",
//														CHKTypeName);
//												startActivity(i);
//											}
//										});
										linear001.setClickable(true);
										final String ID = array
												.getJSONObject(i)
												.getString("ID");
										linear001.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												// TODO Auto-generated
												// method stub
												Intent i = new Intent(
														Index.this,
														CHKTimeDetailActivity.class);
												i.putExtra("ID", ID);
												i.putExtra("CHKType",
														CHKTypeName);
												startActivity(i);

											}
										});


									}

								}
								mHosView.setVisibility(View.GONE);
								tv_viewhos.setVisibility(View.GONE);
								llhos.setVisibility(View.GONE);
								ivleft.setVisibility(View.GONE);
								ivright.setVisibility(View.GONE);
								rlchktime.setVisibility(View.VISIBLE);
								linear001.setVisibility(View.VISIBLE);
								chktimell.setVisibility(View.VISIBLE);
							}

							// Message msg = new Message();
							// msg.what = 1;
							// handler.sendMessage(msg);

						} catch (Exception ex) {
							ex.printStackTrace();
							mHosView.setVisibility(View.VISIBLE);
							llhos.setVisibility(View.GONE);
							ivleft.setVisibility(View.VISIBLE);
							ivright.setVisibility(View.VISIBLE);
							rlchktime.setVisibility(View.GONE);
							chktimell.setVisibility(View.GONE);
						}
					}
				}
			};

			JsonAsyncTask_Info task = new JsonAsyncTask_Info(Index.this, true,
					doProcess);
			Map<String, Object> paramMap = new HashMap<String, Object>();
			String str = "<Request><UserID>%s</UserID><Week>%s</Week><HospitalID>%s</HospitalID></Request>";
			initUser();
			str = String.format(
					str,
					new Object[]{String.valueOf(user.UserID),
							String.valueOf(week + 1), "1"});
			paramMap.put("str", str);

			// 必须是这5个参数，而且得按照顺序
			task.execute(SOAP_NAMESPACE, SOAP_ACTION2, SOAP_METHODNAME2,
					SOAP_URL, paramMap);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*
	 * 查询listview
	 */
	private void ImgIndexContent() {
		imgcontent.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Index.this, TestWebView.class);
				if (yu.Week > 39) {
					intent.putExtra("YunWeek", 40 + "");

				} else {

					intent.putExtra("YunWeek", yu.Week + 1 + "");
				}
				startActivity(intent);
			}
		});

	}

	private void searchlist001() {
		try {
			JsonAsyncTaskOnComplete doProcess = new JsonAsyncTaskOnComplete() {
				public void processJsonObject(Object result) {
					returnvalue001 = result.toString();
					if (returnvalue001 == null) {

					} else {
						Map<String, Object> map;
						// 下边是具体的处理
						try {
							JSONObject mySO = (JSONObject) result;

							JSONArray array = mySO
									.getJSONArray("NewsInquiryIndex");

							arrayList.clear();
							if (array.getJSONObject(0).has("MessageCode")) {
								list002.setAdapter(null);
							} else {
								for (int i = 0; i < array.length(); i++) {

									map = new HashMap<String, Object>();

									map.put("ID", array.getJSONObject(i)
											.getString("ID"));
									map.put("Title", array.getJSONObject(i)
											.getString("Title"));
									map.put("PicURL", array.getJSONObject(i)
											.getString("PicURL"));
									map.put("SubContent", array.getJSONObject(i)
											.getString("SubContent"));
									arrayList.add(map);

								}
								arrayList.size();
								Index003Adapter adapter = new Index003Adapter(
										Index.this, arrayList);

								list002.setAdapter(adapter);
								setListViewHeightBasedOnChildren(list002);
								list002.setEnabled(true);

							}

							// 添加listView点击事件
							list002.setOnItemClickListener(new OnItemClickListener() {
								public void onItemClick(AdapterView<?> arg0,
														View arg1, int arg2, long arg3) {
									Map<String, Object> mapDetail = new HashMap<String, Object>();
									if (arrayList.isEmpty()) {

									} else {
										mapDetail = arrayList.get(arg2);
									}
									String ID = mapDetail.get("ID").toString();

									Intent i = new Intent(Index.this,
											News_Detail.class);
									i.putExtra("ID", ID);
									startActivity(i);
								}
							});

						} catch (Exception ex) {
							ex.printStackTrace();
						}
					}
				}
			};

			JsonAsyncTask_Info task = new JsonAsyncTask_Info(Index.this, true,
					doProcess);
			Map<String, Object> paramMap = new HashMap<String, Object>();
			String str = "<Request><Week>%s</Week></Request>";

			str = String
					.format(str, new Object[]{String.valueOf(week + 1),});

			paramMap.put("str", str);

			// 必须是这5个参数，而且得按照顺序
			task.execute(SOAP_NAMESPACE, SOAP_ACTION, SOAP_METHODNAME,
					SOAP_URL, paramMap);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*
	 * 查询版本信息
	 */
	private void versioninquiry() {
		try {
			JsonAsyncTaskOnComplete doProcess = new JsonAsyncTaskOnComplete() {
				public void processJsonObject(Object result) {
					returnvalue001 = result.toString();
					if (returnvalue001 == null) {

					} else {
						Map<String, Object> map;
						// 下边是具体的处理
						try {
							JSONObject mySO = (JSONObject) result;
							JSONArray array = mySO
									.getJSONArray("VersionInquiry");

							if (array.getJSONObject(0).has("MessageCode")) {

							} else {
								for (int i = 0; i < array.length(); i++) {
									newCode = Integer.valueOf(array
											.getJSONObject(i).getString(
													"VersionID"));
									int VersionFlag = Integer.valueOf(array
											.getJSONObject(i).getString(
													"VersionFlag"));

									PackageManager manager = Index.this
											.getPackageManager();
									PackageInfo info = manager.getPackageInfo(
											Index.this.getPackageName(), 0);
									String appVersion = info.versionName; // 版本名
									oldCode = info.versionCode; // 版本号

									if (oldCode < newCode) {
										// SharedPreferences prefs =
										// Index.this.getSharedPreferences("version",
										// Context.MODE_PRIVATE);
										//
										// if(prefs.getInt("Version", 0) ==
										// newCode)
										// {
										// versionlog
										// }
										// else
										// {
										versionlog = array.getJSONObject(i)
												.getString("VersionLog")
												.replace("@", "\n");
										if (VersionFlag == 1) {

											showForceUpdate();
										} else {
											showUpdateDialog();
										}
										// }
									}
								}
							}
						} catch (Exception ex) {
							ex.printStackTrace();
						}
					}
				}
			};

			JsonAsyncTask_Info task = new JsonAsyncTask_Info(Index.this, true,
					doProcess);
			Map<String, Object> paramMap = new HashMap<String, Object>();
			String str = "<Request></Request>";

			paramMap.put("str", str);

			// 必须是这5个参数，而且得按照顺序
			task.execute(SOAP_NAMESPACE, SOAP_ACTION3, SOAP_METHODNAME3,
					SOAP_URL, paramMap);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void showForceUpdate() {
		LayoutInflater inflater = LayoutInflater.from(this);
		View layout = inflater.inflate(R.layout.dialog_default_ensure, null);

		AlertDialog.Builder builder = new AlertDialog.Builder(Index.this);
		builder.setView(layout);
		builder.setCancelable(false);
		builder.create().show();
		TextView tvok = (TextView) layout.findViewById(R.id.dialog_default_click_ensure);
		TextView tvtitel = (TextView) layout.findViewById(R.id.dialog_default_click_text_title);
		tvtitel.setText("检测到新版本");
		TextView tvcontent = (TextView) layout.findViewById(R.id.dialog_default_click_text_msg);
		tvcontent.setText(versionlog);
		tvok.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
					if (mPermissionsChecker.lacksPermissions(PERMISSIONS)) {
						showMissingPermissionDialog(Index.this);
						return;
					}
				}

				final Intent it = new Intent(Index.this,
						DownloadServiceForAPK.class);
				startService(it);

			}
		});
	}

	private void showUpdateDialog() {
		DialogEnsureCancelView dialogEnsureCancelView = new DialogEnsureCancelView(
				Index.this).setDialogMsg("检测到新版本", versionlog, "下载")
				.setOnClickListenerEnsure(new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
							if (mPermissionsChecker.lacksPermissions(PERMISSIONS)) {
								showMissingPermissionDialog(Index.this);
								return;
							}
						}
						final Intent it = new Intent(Index.this,
								DownloadServiceForAPK.class);
						startService(it);

					}
				});
		DialogUtils.showSelfDialog(Index.this, dialogEnsureCancelView);
		// AlertDialog.Builder builder = new AlertDialog.Builder(this);
		// builder.setTitle("检测到新版本");
		// builder.setMessage(versionlog);
		// builder.setPositiveButton("下载", new DialogInterface.OnClickListener()
		// {

	}
	// public void onClick(DialogInterface dialog, int which) {
	// // TODO Auto-generated method stub
	// //Intent it = new Intent(Index.this,
	// NotificationUpdateActivity.class);
	// //startActivity(it);
	// // MapApp.isDownload = true;
	// //app.setDownload(true);
	//
	// final Intent it = new Intent(Index.this,
	// DownloadServiceForAPK.class);
	// startService(it);
	// //bindService(it, conn, Context.BIND_AUTO_CREATE);
	// }
	// }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
	//
	// public void onClick(DialogInterface dialog, int which) {
	// // TODO Auto-generated method stub
	// // LocalAccessor lacal = new LocalAccessor(Index.this,"version");
	// //
	// // SharedPreferences prefs =
	// Index.this.getSharedPreferences("version", Context.MODE_PRIVATE);
	// // SharedPreferences.Editor editor = prefs.edit();
	// //
	// // editor.putInt("Version", newCode);
	// //
	// // editor.commit();
	// }
	// });
	// builder.show();


	public void setListViewHeightBasedOnChildren(ListView listView) {
		// 获取ListView对应的Adapter
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}
		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) { // listAdapter.getCount()返回数据项的数目
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0); // 计算子项View 的宽高
			totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
		}
		LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		// listView.getDividerHeight()获取子项间分隔符占用的高度
		// params.height最后得到整个ListView完整显示需要的高度
		listView.setLayoutParams(params);
	}

	private void searchHospital() {
		try {
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
							if (hosList.size() > 0) {
								hosList.clear();
							}
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


								hosList.add(map);
								if (isHosiptal) {
									tvyuyue.setVisibility(View.GONE);
									lltime.setVisibility(View.VISIBLE);
									tvchao.setText("进入");

								} else {
									tvyuyue.setVisibility(View.VISIBLE);
									lltime.setVisibility(View.GONE);
									tvchao.setText("查询");
								}



							}

							Message message = Message.obtain();
							message.what = 0;
							mHandler.sendMessage(message);

						} catch (Exception ex) {
							ex.printStackTrace();
						}
					}
				}

			};

			JsonAsyncTask_Info task = new JsonAsyncTask_Info(Index.this, true,
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


	@Override
	public void onRefresh(RefreshLayout pullToRefreshLayout) {
//		new Handler() {
//			@Override
//			public void handleMessage(Message msg) {
//				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//				String date = df.format(new Date());// new Date()为获取当前系统时间
//				Toast.makeText(Index.this,"yyyyyyyyyyyyyyyyyyyyyy",1).show();
//				searchReAds(date);
//			}
//			}.sendEmptyMessageDelayed(0, 500);
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String date = df.format(new Date());// new Date()为获取当前系统时间
//		Toast.makeText(Index.this,"yyyyyyyyyyyyyyyyyyyyyy",1).show();
		searchReAds(date);
		searchYsq(date);

	}


	private void left() {
		isPost = false;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if(isAlready) {
			mViewPager.setSelection(279);
			isAlready=false;
		}else {
			if (childday != 0) {

				if (childday == 280) {
					childday--;
					try {
						Date date1 = sdf.parse(user.YuBirthDate);
						String curr = sdf.format(IOUtils.getDayBefore(date1, "yyyy-MM-dd", (280 - childday)));
						setTitle(sdf.parse(curr));
						dYun = sdf.parse(curr);
						mViewPager.setSelection(278);
					} catch (ParseException e) {
						e.printStackTrace();
					}


				} else {
					childday--;
					try {

						if (childday == 0) {
							childday = 1;
							setTitle(dYun);
							llchildleft.setVisibility(View.INVISIBLE);
							llchildright.setVisibility(View.VISIBLE);
							Date date1 = sdf.parse(user.YuBirthDate);
							dYun = IOUtils.getDayBefore(date1, "yyyy-MM-dd", 280);
						} else {
							llchildleft.setVisibility(View.VISIBLE);
							llchildright.setVisibility(View.VISIBLE);
							dYun = sdf.parse(sdf.format(dYun.getTime() - 24 * 60 * 60 * 1000));
//					mViewPager.setCurrentItem(childday - 1);
							mViewPager.setSelection(childday - 1);
							mTextLenth.setText(lengths[childday - 1] + "mm");
							mTextWeight.setText(weights[childday - 1] + "g");
							tvday.setText((280 - childday) + "天");
							setTitle(dYun);
						}


					} catch (ParseException e) {
						e.printStackTrace();
					}
				}
			} else {
//			txt001.setText("距离宝宝出生还有" +  "280天");

			}
			if (childday == (280 - days)) {
				tvgotaday.setVisibility(View.INVISIBLE);
			} else {

				tvgotaday.setVisibility(View.VISIBLE);
				tvgotaday.startAnimation(animation1);
			}
		}
		isPost = true;
		mHandler.sendEmptyMessageDelayed(1010, 50);
	}

	private void right() {
		isPost = false;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		if (childday != 280) {
			isAlready=false;
			childday++;
//			txt001.setText("距离宝宝出生还有" + (280-childday)+ "天");

			mViewPager.setSelection(childday - 1);
			mTextLenth.setText(lengths[childday - 1] + "mm");
			mTextWeight.setText(weights[childday - 1] + "g");
			tvday.setText((280 - childday) + "天");
			try {
				dYun = sdf.parse(sdf.format(dYun.getTime() + 24 * 60 * 60 * 1000));
				llchildleft.setVisibility(View.VISIBLE);
				llchildright.setVisibility(View.VISIBLE);
				setTitle(dYun);

			} catch (ParseException e) {
				e.printStackTrace();
			}


			if (childday / 7 == 0) {
//				txt_index_002.setText(childday%7 + "天");

			} else if (childday % 7 == 0) {
//				txt_index_002.setText(childday/7 + "周");
			} else {
//				txt_index_002.setText(childday/7 + "周" + childday%7+ "天");
			}
		} else if (days == 0) {
			isAlready=false;

			llchildleft.setVisibility(View.VISIBLE);
			llchildright.setVisibility(View.INVISIBLE);

//			txt001.setText("恭喜您，您的宝宝马上就要出生了！");

//			txt_index_002.setText("40周");
			mViewPager.setSelection(279);
		} else {
			isAlready=true;

			llchildleft.setVisibility(View.VISIBLE);
			llchildright.setVisibility(View.INVISIBLE);
//			txt001.setText("恭喜您，您的宝宝已经出生了！");
//			txt_index_002.setText("40周");
			mViewPager.setSelection(280);
		}
		if (childday == (280 - days)) {
			tvgotaday.setVisibility(View.INVISIBLE);
		} else {
			tvgotaday.setVisibility(View.VISIBLE);
			tvgotaday.startAnimation(animation1);
		}

		isPost = true;
		mHandler.sendEmptyMessageDelayed(1010, 50);

	}

	@Override
	public void OnSelcet(int day) {
		DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
		childday = day;
		isPost = false;
		try {
			Date date1 = fmt.parse(user.YuBirthDate);
			String curr = fmt.format(IOUtils.getDayBefore(date1, "yyyy-MM-dd", (280 - childday)));
			setTitle(fmt.parse(curr));
			dYun = fmt.parse(curr);
//			mViewPager.setCurrentItem(childday - 1);
			mTextLenth.setText(lengths[childday - 1] + "mm");
			mTextWeight.setText(weights[childday - 1] + "g");
			tvday.setText((280 - childday) + "天");
			if (childday - 1 == 0) {
				llchildleft.setVisibility(View.INVISIBLE);
				llchildright.setVisibility(View.VISIBLE);
			} else if (childday - 1 == 279) {
				llchildleft.setVisibility(View.VISIBLE);
				llchildright.setVisibility(View.INVISIBLE);

			} else {
				llchildleft.setVisibility(View.VISIBLE);
				llchildright.setVisibility(View.VISIBLE);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}


//		if(childday!=280){
//
//			txt001.setText("距离宝宝出生还有" + (280-childday)+ "天");
//
//			if(childday/7==0){
////				txt_index_002.setText(childday%7 + "天");
//
//			}else if(childday%7==0){
////				txt_index_002.setText(childday/7 + "周");
//			}else{
////				txt_index_002.setText(childday/7 + "周" + childday%7+ "天");
//			}
//		}else if (days == 0) {
//
//			txt001.setText("恭喜您，您的宝宝马上就要出生了！");
//
////			txt_index_002.setText("40周");
//
//		} else {
//			txt001.setText("恭喜您，您的宝宝已经出生了！");
////			txt_index_002.setText("40周");
//
//		}
		if (childday == (280 - days)) {
			tvgotaday.setVisibility(View.INVISIBLE);
		} else {
			tvgotaday.setVisibility(View.VISIBLE);
			tvgotaday.startAnimation(animation1);
		}
		isPost = true;
		mHandler.sendEmptyMessageDelayed(1010, 50);
	}

	private void TipsInquiry() {
		final Map<String, Object> map;
		try {

			JsonAsyncTaskOnComplete doProcess = new JsonAsyncTaskOnComplete() {
				public void processJsonObject(Object result) {
					String resultObj = result.toString();
					// {"TipsInquiry":[{"IsPic":"0","Sort":"99","Flag":"03","YunWeek":"4","Type":"","DeleteFlag":"0","LinkID":"","IsTop":"0","LinkTitle":"","Zhaiyao":"你好么","Title":"胎儿发育情况","Hits":"0","EndDate":"","StartDate":"","UpdatedDate":"2016\/1\/6
					// 15:39:36","PicURL":"","CreatedDate":"2016\/1\/6
					// 15:39:36","CreatedBy":"1","ID":"287","UpdatedBy":"1","Content":""}]}
					if (resultObj == null) {
						txtcontent.setText("");
					} else {

						try {
							JSONObject mySO = (JSONObject) result;
							JSONArray array = mySO.getJSONArray("DevelopmentInquiry");

							if (array.getJSONObject(0).has("MessageCode")) {
								txtcontent.setText("");
							} else {
								String content = array.getJSONObject(0).getString(
										"Content");
								txtcontent.setText(content);

							}
							if(!isFisrt) {
								isFisrt = true;
								sv.smoothScrollTo(0, 0);
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				}
			};

			JsonAsyncTask_Info task = new JsonAsyncTask_Info(Index.this, true,
					doProcess);
			Map<String, Object> paramMap = new HashMap<String, Object>();
			String str = "<Request><Day>%s</Day></Request>";
			if(isAlready){
				str = String.format(str, new Object[]{"281"
				});

			}else {

				str = String.format(str, new Object[]{String.valueOf(childday)
				});
			}

			paramMap.put("str", str);


			// 必须是这5个参数，而且得按照顺序
			task.execute(SOAP_NAMESPACE, SOAP_TIPSACTION, SOAP_TIPSMETHODNAME,
					SOAP_URL, paramMap);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void onStart() {
		super.onStart();



	}

	@Override
	public void onStop() {
		super.onStop();



	}

	public class GalleryAdapter extends BaseAdapter {
		public List<YunWeekAll> list = null;
		public Context ctx = null;

		public GalleryAdapter(Context ctx, List<YunWeekAll> list) {
			this.list = list;
			this.ctx = ctx;
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final ViewHolder holder;
			DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = View.inflate(ctx, R.layout.image_item, null);
				holder.image = (ImageView) convertView.findViewById(R.id.image);
				holder.imageRel = (RelativeLayout) convertView.findViewById(R.id.image_rel);
				holder.rlimg = (RelativeLayout) convertView.findViewById(R.id.rlimg);
				holder.tvcontent = (TextView) convertView.findViewById(R.id.tvcontent);
				holder.mRoundProgressBar = (RoundProgressBar) convertView.findViewById(R.id.roundProgressBarright);
				holder.mRoundProgressBar.setText("");
				holder.mRoundProgressBar.setText2("");
				holder.mRoundProgressBar.getTextPaint1().setTextSize(28 * ScreenUtils.getScreenHeight(Index.this) / 720);
				holder.mRoundProgressBar.getTextPaint2().setTextSize(58 * ScreenUtils.getScreenHeight(Index.this) / 720);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.image.setImageDrawable(list.get(position).getImage());
			holder.tvcontent.setText(list.get(position).getDay());

			if (selectNum == position) {
				isPost = false;

//				int width = 220 * ScreenUtils.getScreenHeight(Index.this) / 1080;
//				int width =110 *ScreenUtils.getScreenHeight(Index.this) / 1080;
//				ScaleAnimation animation = new ScaleAnimation(1, 1.5f, 1, 1.5f,
//						                       Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
//				holder.rlimg.setLayoutParams(new RelativeLayout.LayoutParams(width, width));
//				               //从1倍到1.5倍需要1秒钟
//				                 animation.setDuration(1000);
//				                 //开始执行动画
//				holder.rlimg.startAnimation(animation);

				width =210 *ScreenUtils.getScreenHeight(Index.this) / 1080;
				holder.rlimg.setLayoutParams(new RelativeLayout.LayoutParams(width, width));
//				holder.rlimg.setLayoutParams(new RelativeLayout.LayoutParams(width, width));//如果被选择则放大显示
//				holder.mRoundProgressBar.setLayoutParams(new RelativeLayout.LayoutParams(375, 375));
//				holder.tvcontent.setLayoutParams(new TextView());
				if (position + 1 == 281) {
					childday = 280;
					isAlready=true;
				} else {
					childday = position + 1;
					isAlready=false;
				}

				holder.tvcontent.setTextSize(18);
				setText(holder.tvcontent, list.get(position).getDay(), 2);
				holder.image.setAlpha(250);
				holder.mRoundProgressBar.setAlpha(1.0f);
				try {
					if(isAlready&&position + 1 == 281){
						Date date1 = fmt.parse(user.YuBirthDate);
						if((new Date().getTime())>(date1.getTime()+24*60*60*1000)){
							dYun = new Date();

						}else{

							dYun=fmt.parse(fmt.format(date1.getTime()+24*60*60*1000));
						}
						setTitle(dYun);
					}else{
						Date date1 = fmt.parse(user.YuBirthDate);
						String curr = fmt.format(IOUtils.getDayBefore(date1, "yyyy-MM-dd", (280 - childday)));
						setTitle(fmt.parse(curr));
						dYun = fmt.parse(curr);
					}


					mTextLenth.setText(lengths[childday - 1] + "mm");
					mTextWeight.setText(weights[childday - 1] + "g");
					tvday.setText((280 - childday) + "天");
					if (childday - 1 == 0) {
						llchildleft.setVisibility(View.INVISIBLE);
						llchildright.setVisibility(View.VISIBLE);
					} else if (position + 1 == 281) {
						llchildleft.setVisibility(View.VISIBLE);
						llchildright.setVisibility(View.INVISIBLE);

					} else {
						llchildleft.setVisibility(View.VISIBLE);
						llchildright.setVisibility(View.VISIBLE);
					}
					if (position != 280) {
						if (childday == (280 - days)) {
							tvgotaday.setVisibility(View.INVISIBLE);
						} else {
							tvgotaday.setVisibility(View.VISIBLE);
							tvgotaday.startAnimation(animation1);
						}
					} else {
						tvgotaday.setVisibility(View.INVISIBLE);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				isPost = true;
				mHandler.sendEmptyMessageDelayed(1010, 50);

			} else {
				int width2 = 110 *ScreenUtils.getScreenHeight(Index.this) / 1080;
				holder.rlimg.setLayoutParams(new RelativeLayout.LayoutParams(width2, width2));//否则正常
//				holder.mRoundProgressBar.setLayoutParams(new RelativeLayout.LayoutParams(175, 175));
				holder.tvcontent.setTextSize(12);
				setText(holder.tvcontent, list.get(position).getDay(), 1);

				holder.image.setAlpha(100);
				if(position==279){
					if(isAlready&&selectNum==280){
						holder.rlimg.setVisibility(View.INVISIBLE);
						holder.tvcontent.setVisibility(View.INVISIBLE);
					}else{
						holder.rlimg.setVisibility(View.VISIBLE);
						holder.tvcontent.setVisibility(View.VISIBLE);
					}
				}
				holder.mRoundProgressBar.setAlpha(0.5f);
			}
			int per = (int) (childday / 2.8);

			if (100 < per) {
				per = 100;
			}
			if (per <= 0) {
				per = 1;
			}
			holder.mRoundProgressBar.setSweepAngle((int) (per * 3.6));
			holder.mRoundProgressBar.startCustomAnimation();
			return convertView;
		}

		private void setText(TextView tv, String content, int type) {
			if (content.indexOf("+") >= 0) {
				if (type == 1) {
					String spantxtyun = content;
					SpannableString styleyun = new SpannableString(
							spantxtyun);

					styleyun.setSpan(new TextAppearanceSpan(Index.this, R.style.styleIndexSmallColor), content.length() - 3, content.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

					tv.setText(styleyun);
				} else {
					String spantxtyun = content;
					SpannableString styleyun = new SpannableString(
							spantxtyun);
					styleyun.setSpan(new TextAppearanceSpan(Index.this, R.style.styleBigColor), content.length() - 3, content.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
					tv.setText(styleyun);
				}

			}

		}


	}

	class ViewHolder {
		ImageView image;
		RelativeLayout imageRel;
		RelativeLayout rlimg;
		RoundProgressBar mRoundProgressBar;
		TextView tvcontent;
	}
}
