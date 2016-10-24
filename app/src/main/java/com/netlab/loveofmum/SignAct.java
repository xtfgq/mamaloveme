package com.netlab.loveofmum;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Color;
import android.net.ParseException;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;


import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.netlab.loveofmum.api.BaseActivity;
import com.netlab.loveofmum.api.JsonAsyncTaskOnComplete;
import com.netlab.loveofmum.api.JsonAsyncTask_Info;
import com.netlab.loveofmum.api.LocalAccessor;
import com.netlab.loveofmum.api.MMloveConstants;
import com.netlab.loveofmum.api.MyApplication;
import com.netlab.loveofmum.model.User;
import com.netlab.loveofmum.timepicker.Util;
import com.netlab.loveofmum.utils.IOUtils;
import com.netlab.loveofmum.utils.SystemStatusManager;
import com.netlab.loveofmum.utils.UiUtils;
import com.nostra13.universalimageloader.utils.IoUtils;
import com.umeng.analytics.MobclickAgent;
import com.netlab.loveofmum.RotateAnimation;



public class SignAct extends BaseActivity implements com.netlab.loveofmum.RotateAnimation.InterpolatedTimeListener {
	private User user;
	private int Score;
	private TextView[] mTabViews;
	private TextView[] mTextViews;
	private TextView tvqdao;
	private ImageView imgBack;
	private TextView tvscore, txtHead;
	private ImageView ivjf;
	private LinearLayout lltop,llone,lltwo;
	private ImageView ivbottom;
	private RelativeLayout rlthree;
	private int[] dayIcons = new int[] {R.drawable.img_1,R.drawable.img_2,R.drawable.img_3,R.drawable.img_4,R.drawable.img_5,R.drawable.img_6,R.drawable.img_7
			};

	// 添加积分接口
	private final String SOAP_NAMESPACE = MMloveConstants.URL001;
	private final String SOAP_ADDACTION = MMloveConstants.URL001
			+ MMloveConstants.UserPointAdd;
	private final String SOAP_METHODNAMEADD = MMloveConstants.UserPointAdd;
	private final String SOAP_URL = MMloveConstants.URL002;
	// 查询积分接口
	private final String SOAP_LOGMETHODNAME = MMloveConstants.UserPointLog;
	private final String SOAP_LOGACTION = MMloveConstants.URL001
			+ MMloveConstants.UserPointLog;
	// 增加次数接口
	private final String SOAP_USERSIGINUPDATEMETHODNAME = MMloveConstants.UserSiginUpdate;
	private final String SOAP_USERSIGINUPDATEACTION = MMloveConstants.URL001
			+ MMloveConstants.UserSiginUpdate;
	// 清除次数接口
	private final String SOAP_CLEARMETHODNAME = MMloveConstants.UserSiginClear;
	private final String SOAP_CLEARACTION = MMloveConstants.URL001
			+ MMloveConstants.UserSiginClear;
	// 查询次数接口

	private final String SOAP_SEARCHMETHODNAME = MMloveConstants.UserSiginSearch;
	private final String SOAP_SEARCHACTION = MMloveConstants.URL001
			+ MMloveConstants.UserSiginSearch;

	// 用户总积分
	private final String SOAP_INQUIRYMETHODNAME = MMloveConstants.UserInquiry;
	private final String SOAP_INQUIRYACTION = MMloveConstants.URL001
			+ MMloveConstants.UserInquiry;

	// 得到系统时间
	private final String SOAP_SERVERTIMEMETHODNAME = MMloveConstants.GetServerTime;
	private final String SOAP_SERVERTIMEACTION = MMloveConstants.URL001
			+ MMloveConstants.GetServerTime;
	private ImageView ivdaynum;
	private RelativeLayout rlhearder;
	private boolean enableRefresh;
	private int index=-1;

	//	final RotateAnimation animation =new RotateAnimation(0f,360f, Animation.RELATIVE_TO_SELF,
//			0.5f,Animation.RELATIVE_TO_SELF,0.5f);
   Date dNow;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setTranslucentStatus();
		setContentView(R.layout.activity_sign_new);
		user = LocalAccessor.getInstance(SignAct.this).getUser();
		iniView();
		/** 设置旋转动画 */

//		animation.setDuration(3000);
		ViewGroup.LayoutParams para;
		para =  rlhearder.getLayoutParams();
		para.width = Util.getScreenWidth(SignAct.this);
		para.height = para.width*5/14;
		rlhearder.setLayoutParams(para);
		ViewGroup.LayoutParams para2;
		para2 =  ivbottom.getLayoutParams();
		para2.width = Util.getScreenWidth(SignAct.this);
		para2.height = para2.width*2/5;
		ivbottom.setLayoutParams(para2);
		try {
			if(MyApplication.getInstance().nowtime!=null)
			dNow = IOUtils.string2Date(MyApplication.getInstance().nowtime, "yyyy-MM-dd");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		UiUtils.resetRL(SignAct.this,lltop);
		UiUtils.resetRL(SignAct.this,llone);

		UiUtils.resetLL(SignAct.this,lltwo);

//		UiUtils.imageRLViewReset(ivbottom,720,264,true,SignAct.this);
		UiUtils.resetLL(SignAct.this,rlthree);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String endTime = sdf.format(dNow.getTime() + 60 * 60 * 1000
				* 24);
		Date dStart = new Date(dNow.getTime() - 60 * 60 * 1000 * 24
				* 7);
		String startTime = sdf.format(dStart);
//		addNum();
		getPonit(startTime, endTime);


		setListner();

	}
	@Override
	public void interpolatedTime(float interpolatedTime) {
		// 监听到翻转进度过半时，更新txtNumber显示内容。
		if (enableRefresh && interpolatedTime > 0.5f) {

			String spantxtyun = "第"  + (index+1) + "天";
			SpannableString styleyun = new SpannableString (
					spantxtyun);
			mTabViews[index].setBackgroundResource(R.drawable.img_yqd);
			mTabViews[index].setTextColor(Color.parseColor("#c5500a"));
			mTextViews[index].setTextColor(Color.parseColor("#c5500a"));
			styleyun.setSpan(new TextAppearanceSpan(this, R.style.styleSignRedColor), 1, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			mTextViews[index].setText(styleyun);
			enableRefresh = false;
		}
	}



	public static Date string2Date(String strTime, String formatType)
			throws ParseException {
		SimpleDateFormat formatter = new SimpleDateFormat(formatType);
		Date date = null;
		try {
			date = formatter.parse(strTime);
		} catch (java.text.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return date;
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
					tvscore.setText(array.getJSONObject(0).getString("Point")
							.toString()+"积分"
							);
					getNum();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		};
		JsonAsyncTask_Info task = new JsonAsyncTask_Info(SignAct.this, true,
				doProcess);
		Map<String, Object> paramMap = new HashMap<String, Object>();
		String str = "<Request><UserID>%s</UserID></Request>";
		str = String.format(str, new Object[] { user.UserID + "", });

		paramMap.put("str", str);
		task.execute(SOAP_NAMESPACE, SOAP_INQUIRYACTION,
				SOAP_INQUIRYMETHODNAME, SOAP_URL, paramMap);
	}

	private void getNum() {
		// TODO Auto-generated method stub
		JsonAsyncTaskOnComplete doProcess = new JsonAsyncTaskOnComplete() {

			@Override
			public void processJsonObject(Object result) {
				String value = result.toString();
				JSONObject mySO = (JSONObject) result;
				// {"UserSiginSearch":[{"UpdatedDate":"2016\/1\/20
				// 11:15:10","DeleteFlag":"0","CreatedDate":"2016\/1\/20
				// 11:07:42","CreatedBy":"","ID":"1","SigninCount":"0","UserID":"50694","UpdatedBy":""}]}
				try {
					JSONArray array = mySO.getJSONArray("UserSiginSearch");
					int count = Integer.valueOf(array.getJSONObject(0)
							.getString("SigninCount").toString());


//					tvqdao.setText("已经连续签到" + count + "天");
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		};
		JsonAsyncTask_Info task = new JsonAsyncTask_Info(SignAct.this, true,
				doProcess);
		Map<String, Object> paramMap = new HashMap<String, Object>();
		String str = "<Request><UserID>%s</UserID></Request>";
		str = String.format(str, new Object[] { user.UserID + "", });

		paramMap.put("str", str);
		task.execute(SOAP_NAMESPACE, SOAP_SEARCHACTION, SOAP_SEARCHMETHODNAME,
				SOAP_URL, paramMap);
	}

	private void setListner() {
		imgBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	private void getPonit(String startTime, String endTime) {
		// TODO Auto-generated method stub
		JsonAsyncTaskOnComplete doProcess = new JsonAsyncTaskOnComplete() {

			@Override
			public void processJsonObject(Object result) {
				
				String value = result.toString();

				JSONObject mySO = (JSONObject) result;
				try {
					JSONArray array = mySO.getJSONArray("UserPointLogInquiry");
					if (array.getJSONObject(0).has("MessageCode")) {
						clearNum();
				
//						addNum();
					} else {
						Score = Integer.valueOf(array.getJSONObject(0)
								.get("Value").toString());
						String time = array.getJSONObject(0).get("CreatedDate")
								.toString();
						SimpleDateFormat formatter = new SimpleDateFormat(
								"yyyy-MM-dd");
						time = time.split(" ")[0].replace("/", "-");

						time = stringToDate2(time, "yyyy-MM-dd");
						if (time.equals(MyApplication.getInstance().nowtime)) {
							setAlayText(Score);

						
							getUserInqury();
						} else {
							Date forwardtime = stringToDate(time, "yyyy-MM-dd");
							int day = DaysCount2(forwardtime, dNow);
							if (day > 1) {

								clearNum();
							} else {
								if (Score==35) {
									setAnim(0);
									setText(5);
									setUserPoint(5);

								} else {
									setAnim(Integer.valueOf(Score + 5)/5-1);
									setText(Integer.valueOf(Score + 5));
									setUserPoint(Integer.valueOf(Score + 5));

								}
							

							}
						}
					}

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		};
		JsonAsyncTask_Info task = new JsonAsyncTask_Info(SignAct.this, true,
				doProcess);
		Map<String, Object> paramMap = new HashMap<String, Object>();
		String str = "<Request><UserID>%s</UserID><StartTime>%s</StartTime><EndTime>%s</EndTime><Remark>%s</Remark></Request>";
		str = String.format(str, new Object[] { user.UserID + "", startTime,
				endTime, "签到送积分" });

		paramMap.put("str", str);
		task.execute(SOAP_NAMESPACE, SOAP_LOGACTION, SOAP_LOGMETHODNAME,
				SOAP_URL, paramMap);
	}

	public static int DaysCount2(Date d, Date dn) {

		long intermilli = dn.getTime() - d.getTime();
		int days = (int) (intermilli / (24 * 60 * 60 * 1000));

		// if(days < 0)
		// {
		// days = 0;
		// }
		return days;
	}

	// 返回string类型时间
	public static String stringToDate2(String strTime, String formatType)
			throws ParseException {
		SimpleDateFormat formatter = new SimpleDateFormat(formatType);
		Date date = null;
		try {
			date = formatter.parse(strTime);
		} catch (java.text.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return formatter.format(date);
	}

	protected void addNum() {
		// TODO Auto-generated method stub
		JsonAsyncTaskOnComplete doProcess = new JsonAsyncTaskOnComplete() {

			@Override
			public void processJsonObject(Object result) {
				String value = result.toString();

				getNum();
			}

		};
		JsonAsyncTask_Info task = new JsonAsyncTask_Info(SignAct.this, true,
				doProcess);
		Map<String, Object> paramMap = new HashMap<String, Object>();
		String str = "<Request><UserID>%s</UserID></Request>";
		str = String.format(str, new Object[] { user.UserID + "", });

		paramMap.put("str", str);
		task.execute(SOAP_NAMESPACE, SOAP_USERSIGINUPDATEACTION,
				SOAP_USERSIGINUPDATEMETHODNAME, SOAP_URL, paramMap);
	}

	private void clearNum() {
		// TODO Auto-generated method stub
		setText(5);
		setAnim(0);
		JsonAsyncTaskOnComplete doProcess = new JsonAsyncTaskOnComplete() {

			@Override
			public void processJsonObject(Object result) {
				String value = result.toString();

				setUserPoint(5);

			}

		};
		JsonAsyncTask_Info task = new JsonAsyncTask_Info(SignAct.this, true,
				doProcess);
		Map<String, Object> paramMap = new HashMap<String, Object>();
		String str = "<Request><UserID>%s</UserID></Request>";
		str = String.format(str, new Object[] { user.UserID + "", });

		paramMap.put("str", str);
		task.execute(SOAP_NAMESPACE, SOAP_CLEARACTION, SOAP_CLEARMETHODNAME,
				SOAP_URL, paramMap);
	}

	public static Date stringToDate(String strTime, String formatType)
			throws ParseException {
		SimpleDateFormat formatter = new SimpleDateFormat(formatType);
		Date date = null;
		try {
			date = formatter.parse(strTime);
		} catch (java.text.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return date;
	}

	private void setAlayText(int dayNum) {
	int m = dayNum / 5;
		ivdaynum.setBackgroundResource(dayIcons[m-1]);
		for(int n=0;n<7;n++){
			String spantxtyun = "第"  + (n+1) + "天";
			SpannableString styleyun = new SpannableString (
					spantxtyun);
			mTabViews[n].setBackgroundResource(R.drawable.img_wqd);
			mTabViews[n].setTextColor(Color.parseColor("#a9a9a9"));
			mTextViews[n].setTextColor(Color.parseColor("#a9a9a9"));
			styleyun.setSpan(new TextAppearanceSpan(this, R.style.styleSignColor), 1, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			mTextViews[n].setText(styleyun);
		}

		for (int n = 0; n < m; n++) {
			String spantxtyun = "第"  + (n+1) + "天";
			SpannableString styleyun = new SpannableString (
					spantxtyun);
			mTabViews[n].setBackgroundResource(R.drawable.img_yqd);
			mTabViews[n].setTextColor(Color.parseColor("#c5500a"));
			mTextViews[n].setTextColor(Color.parseColor("#c5500a"));
			styleyun.setSpan(new TextAppearanceSpan(this, R.style.styleSignRedColor), 1, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			mTextViews[n].setText(styleyun);
		}

	}
	private void setText(int dayNum) {
		int m = dayNum / 5;
		for(int n=0;n<7;n++){
			String spantxtyun = "第"  + (n+1) + "天";
			SpannableString styleyun = new SpannableString (
					spantxtyun);
			mTabViews[n].setBackgroundResource(R.drawable.img_wqd);
			mTabViews[n].setTextColor(Color.parseColor("#a9a9a9"));
			mTextViews[n].setTextColor(Color.parseColor("#a9a9a9"));
			styleyun.setSpan(new TextAppearanceSpan(this, R.style.styleSignColor), 1, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			mTextViews[n].setText(styleyun);
		}
		for (int n = 0; n < m-1; n++) {
			String spantxtyun = "第"  + (n+1) + "天";
			SpannableString styleyun = new SpannableString (
					spantxtyun);
			mTabViews[n].setBackgroundResource(R.drawable.img_yqd);
			mTabViews[n].setTextColor(Color.parseColor("#c5500a"));
			mTextViews[n].setTextColor(Color.parseColor("#c5500a"));
			styleyun.setSpan(new TextAppearanceSpan(this, R.style.styleSignRedColor), 1, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			mTextViews[n].setText(styleyun);
		}

	}
	private void setAnim(int postion){
		ivdaynum.setBackgroundResource(dayIcons[postion]);
		enableRefresh = true;
		RotateAnimation rotateAnim = null;


		float cX = Util.getScreenWidth(mContext)/12.0f;
		float cY = Util.getScreenWidth(mContext)/12.0f;
		rotateAnim = new RotateAnimation(cX, cY, RotateAnimation.ROTATE_DECREASE);
		rotateAnim.setDuration(1000);
		if (rotateAnim != null) {
			rotateAnim.setInterpolatedTimeListener(this);
			rotateAnim.setFillAfter(true);
			mTabViews[postion].startAnimation(rotateAnim);
			index=postion;


		}
	}

	private void setUserPoint(final int point) {
		// TODO Auto-generated method stub

		JsonAsyncTaskOnComplete doProcess = new JsonAsyncTaskOnComplete() {

			@Override
			public void processJsonObject(Object result) {
				// {"UserPointAdd":[{"MessageContent":"成功！","MessageCode":"0"}]}
				// TODO Auto-generated method stub
				String value = result.toString();
				JSONObject mySO = (JSONObject) result;
				try {
					JSONArray array = mySO.getJSONArray("UserPointAdd");
					if ("0".equals(array.getJSONObject(0).getString(
							"MessageCode"))) {
						// Toast.makeText(
						// SignAct.this,
						// array.getJSONObject(0).getString(
						// "MessageContent"), 1).show();


						addNum();
						getUserInqury();

					}

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		JsonAsyncTask_Info task = new JsonAsyncTask_Info(SignAct.this, true,
				doProcess);
		Map<String, Object> paramMap = new HashMap<String, Object>();
		String str = "<Request><UserID>%s</UserID><PointValue>%s</PointValue><Remark>%s</Remark></Request>";
		str = String.format(str, new Object[] { user.UserID + "", point + "",
				"签到送积分" });

		paramMap.put("str", str);
		task.execute(SOAP_NAMESPACE, SOAP_ADDACTION, SOAP_METHODNAMEADD,
				SOAP_URL, paramMap);
	}

	@Override
	protected void iniView() {
		// TODO Auto-generated method stub
		mTabViews = new TextView[7];
		mTextViews=new TextView[7];
		mTabViews[0] = (TextView) findViewById(R.id.tv_add1);
		mTabViews[1] = (TextView) findViewById(R.id.tv_add2);
		mTabViews[2] = (TextView) findViewById(R.id.tv_add3);
		mTabViews[3] = (TextView) findViewById(R.id.tv_add4);
		mTabViews[4] = (TextView) findViewById(R.id.tv_add5);
		mTabViews[5] = (TextView) findViewById(R.id.tv_add6);
		mTabViews[6] = (TextView) findViewById(R.id.tv_add7);
		mTextViews[0]=(TextView)findViewById(R.id.tv_day1);
		mTextViews[1]=(TextView)findViewById(R.id.tv_day2);
		mTextViews[2]=(TextView)findViewById(R.id.tv_day3);
		mTextViews[3]=(TextView)findViewById(R.id.tv_day4);
		mTextViews[4]=(TextView)findViewById(R.id.tv_day5);
		mTextViews[5]=(TextView)findViewById(R.id.tv_day6);
		mTextViews[6]=(TextView)findViewById(R.id.tv_day7);

//		tvqdao = (TextView) findViewById(R.id.tvsignday);
		imgBack = (ImageView) findViewById(R.id.img_left);

		tvscore = (TextView) findViewById(R.id.tvscore);
		ivjf=(ImageView) findViewById(R.id.im_jf);
		ivdaynum=(ImageView)findViewById(R.id.ivdaynum);
		rlhearder=(RelativeLayout)findViewById(R.id.rlhearder);
		lltop=(LinearLayout)findViewById(R.id.lltop);
		llone=(LinearLayout)findViewById(R.id.llone);
		lltwo=(LinearLayout)findViewById(R.id.lltwo);
		rlthree=(RelativeLayout)findViewById(R.id.rlthree);
		ivbottom=(ImageView)findViewById(R.id.ivbottom);

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
		tintManager.setStatusBarTintResource(R.drawable.bg_header);// 状态栏无背景
	}

}
