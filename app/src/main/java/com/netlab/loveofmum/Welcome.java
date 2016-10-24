package com.netlab.loveofmum;

import java.io.IOException;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.baidu.mobads.SplashAd;
import com.baidu.mobads.SplashAdListener;
import com.netlab.loveofmum.api.BaseActivity;
import com.netlab.loveofmum.api.MMloveConstants;
import com.netlab.loveofmum.api.CrashApplication;
import com.netlab.loveofmum.api.JsonAsyncTaskOnComplete;
import com.netlab.loveofmum.api.JsonAsyncTask_Info;
import com.netlab.loveofmum.api.LocalAccessor;
import com.netlab.loveofmum.api.MyApplication;
import com.netlab.loveofmum.huanxin.MainChatActivity;
import com.netlab.loveofmum.model.User;
import com.netlab.loveofmum.myadapter.FeedAadapter;
import com.netlab.loveofmum.testwebview.TestWebView;
import com.netlab.loveofmum.utils.IOUtils;
import com.netlab.loveofmum.utils.ListUtils;
import com.umeng.analytics.MobclickAgent;
import com.umeng.analytics.MobclickAgentJSInterface;

import cn.sharesdk.framework.ShareSDK;

//import cn.sharesdk.framework.ShareSDK;


public class Welcome extends BaseActivity {
	boolean isFirstIn = true;
	private static final int GO_HOME = 1000;
	private static final int GO_GUIDE = 1001;
	// 延迟3秒
	private static final long SPLASH_DELAY_MILLIS = 3000;
	private String returnvalue001;
	private static final String SHAREDPREFERENCES_NAME = "first_pref";
	// 定义Web Service相关的字符串
	private final String SOAP_NAMESPACE = MMloveConstants.URL001;
	private final String SOAP_ACTION = MMloveConstants.URL001
			+ MMloveConstants.NewsInquiry;
	private final String SOAP_METHODNAME = MMloveConstants.NewsInquiry;
	private final String SOAP_URL = MMloveConstants.URL002;

	protected MyApplication myApplication;
	private User user;
	String nowtime;
	// 得到系统时间
	private final String SOAP_SERVERTIMEMETHODNAME = MMloveConstants.GetServerTime;
	private final String SOAP_SERVERTIMEACTION = MMloveConstants.URL001
			+ MMloveConstants.GetServerTime;
	/**
	 * Handler:跳转到不同界面
	 */

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case GO_HOME:
				goHome();
				break;
			case GO_GUIDE:
				goGuide();
				break;
			case 2:
				showApp();
				break;
				case 3:
					break;
			}
			super.handleMessage(msg);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_welcome);
		setTimerTask();
		myApplication = MyApplication.getInstance();
		getTime();
		ShareSDK.initSDK(this);
	}
	public boolean canJumpImmediately = false;
	private void jumpWhenCanClick() {
		Log.d("test", "this.hasWindowFocus():" + this.hasWindowFocus());
		if (canJumpImmediately) {
			goGuide();
		} else {
			canJumpImmediately = true;
		}

	}
	/**
	 * 不可点击的开屏，使用该jump方法，而不是用jumpWhenCanClick
	 */
	private void jump() {
		goGuide();
	}
	private void setTimerTask() {
		// TODO Auto-generated method stub
			mHandler.sendEmptyMessageDelayed(2, 1000);

	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		isFirstIn=false;
		MobclickAgent.onPause(this);
	}

	public void showApp() {
		if (!hasInternetConnected()) {
			setTimerTask();
				Toast.makeText(Welcome.this, "网络数据获取失败，请检查网络设置", 0).show();
			return;
		}
		// searchHospital();
		user = LocalAccessor.getInstance(Welcome.this).getUser();
		init();
		myApplication = MyApplication.getInstance();
	}



	private void init() {
		// 读取SharedPreferences中需要的数据
		// 使用SharedPreferences来记录程序的使用次数
		SharedPreferences preferences = getSharedPreferences(
				SHAREDPREFERENCES_NAME, MODE_PRIVATE);

		PackageManager manager = Welcome.this.getPackageManager();
		PackageInfo info;
		try {
			info = manager.getPackageInfo(Welcome.this.getPackageName(), 0);

			int newCode = info.versionCode; // 版本号

			int oldCode = preferences.getInt("VersionCode", 0);

			// 取得相应的值，如果没有该值，说明还未写入，用true作为默认值
			// isFirstIn = preferences.getBoolean("isFirstIn", true);

			// 判断程序与第几次运行，如果是第一次运行则跳转到引导界面，否则跳转到主界面
			if (newCode == oldCode && user.YuBirthDate != null) {
				// 使用Handler的postDelayed方法，3秒后执行跳转到MainActivity
				mHandler.sendEmptyMessageDelayed(GO_HOME, SPLASH_DELAY_MILLIS);
			} else {
				mHandler.sendEmptyMessageDelayed(GO_GUIDE, SPLASH_DELAY_MILLIS);
			}
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void goHome() {


		User user = LocalAccessor.getInstance(Welcome.this).getUser();

		Intent intent = new Intent(Welcome.this, MainTabActivity.class);
		intent.putExtra("TabIndex", "A_TAB");
		Welcome.this.startActivity(intent);
		Welcome.this.finish();
		// }

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

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		};
		JsonAsyncTask_Info task = new JsonAsyncTask_Info(this, true, doProcess);
		Map<String, Object> paramMap = new HashMap<String, Object>();
		String str = "<Request></Request>";
		str = String.format(str, new Object[] { "", });

		paramMap.put("str", str);
		task.execute(SOAP_NAMESPACE, SOAP_SERVERTIMEACTION,
				SOAP_SERVERTIMEMETHODNAME, SOAP_URL, paramMap);
	}

	private void goGuide() {
		Intent intent = new Intent(Welcome.this, GuideActivity.class);
		Welcome.this.startActivity(intent);
		Welcome.this.finish();
	}

	
	@Override
	protected void iniView() {
		// TODO Auto-generated method stub

	}
}
