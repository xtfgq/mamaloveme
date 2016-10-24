package com.netlab.loveofmum;

import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TabHost;
import android.widget.TextView;

import com.netlab.loveofmum.R;
import com.netlab.loveofmum.activity.LoginActivity;
import com.netlab.loveofmum.api.LocalAccessor;
import com.netlab.loveofmum.api.MyApplication;
import com.netlab.loveofmum.community.CHK_medical_community;

import com.netlab.loveofmum.huanxin.MainChatActivity;
import com.netlab.loveofmum.model.User;
import com.netlab.loveofmum.model.Yuchan;
import com.netlab.loveofmum.testwebview.TestWebView;
import com.netlab.loveofmum.utils.IOUtils;
import com.netlab.loveofmum.utils.SystemStatusManager;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class MainTabActivity extends TabActivity implements
		OnClickListener
{

	private TabHost mTabHost;
	private Intent mAIntent;
	private Intent mBIntent;
    private Intent mCIntent;
	private Intent mDIntent;
	private TextView[] mTabViews;
	private RadioButton[] mButtons;
	private LinearLayout homelayout,yunsq,jk,melayout;

	Yuchan yu;
	private User user;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setTranslucentStatus();
		setContentView(R.layout.maintabs);
		MyApplication.getInstance().addActivity(this);
		this.mAIntent = new Intent(this, Index.class);

    MobclickAgent.openActivityDurationTrack(false);
		
//		this.mBIntent = new Intent(this, Doctor_Talk.class);
		this.mBIntent = new Intent(this, BBSWebView.class);
//	this.mBIntent=new Intent(this,ExampleActivity.class);
//		CommunitySDK mCommSDK = CommunityFactory.getCommSDK(Welcome.this);
////		// 打开微社区的接口, 参数1为Context类型
//		mCommSDK.openCommunity(Welcome.this);
	
		this.mCIntent = new Intent(this, CHK_medical_community.class);
		this.mDIntent = new Intent(this, User_Set.class);
		mTabViews = new TextView[4];
		mButtons = new RadioButton[4];
		mTabViews[0] = (TextView) findViewById(R.id.activity_main_home);
		mTabViews[1] = (TextView) findViewById(R.id.tv_ysq);
		mTabViews[2] = (TextView) findViewById(R.id.activity_main_jk);
		mTabViews[3] = (TextView) findViewById(R.id.activity_tv_zx);
		mButtons[0] = (RadioButton) findViewById(R.id.radio_button0);
		mButtons[1] = (RadioButton) findViewById(R.id.radio_button1);
		mButtons[2] = (RadioButton) findViewById(R.id.radio_button2);
		mButtons[3] = (RadioButton) findViewById(R.id.radio_button3);
		homelayout=(LinearLayout)findViewById(R.id.activity_main_tab_home_layout);
		yunsq=(LinearLayout)findViewById(R.id.activity_main_sq);
	    jk=(LinearLayout)findViewById(R.id.activity_main_tab_jk);
	    melayout=(LinearLayout)findViewById(R.id.activity_main_zx);
	    homelayout.setOnClickListener(this);
	    yunsq.setOnClickListener(this);
	    jk.setOnClickListener(this);
	    melayout.setOnClickListener(this);

//		mButtons[0].setOnCheckedChangeListener(this);
//		mButtons[1] .setOnCheckedChangeListener(this);
//		mButtons[2] 
//				.setOnCheckedChangeListener(this);
//		mButtons[3] 
//		.setOnCheckedChangeListener(this);
		setupIntent();

	    if (this.getIntent().getExtras().getString("TabIndex").toString()
				.equals("A_TAB"))
		{
			this.mTabHost.setCurrentTabByTag("A_TAB");
			setButton(0);


			if(!TextUtils.isEmpty(this.getIntent().getExtras().getString("Push"))){
				Log.i("999999","9999999999===============");

				pushIntent(this.getIntent().getExtras().getString("MessageCode").toString(),this.getIntent().getExtras().getString("MessageContent").toString());
			}
		}else
		if (this.getIntent().getExtras().getString("TabIndex").toString()
				.equals("B_TAB"))
		{
			this.mTabHost.setCurrentTabByTag("B_TAB");
			setButton(1);
		}else
		if (this.getIntent().getExtras().getString("TabIndex").toString()
				.equals("C_TAB"))
		{
			this.mTabHost.setCurrentTabByTag("C_TAB");
			setButton(2);

		}
		else 
		if (this.getIntent().getExtras().getString("TabIndex").toString()
				.equals("D_TAB"))
		{
			this.mTabHost.setCurrentTabByTag("D_TAB");
			setButton(3);
		}else{
			Log.i("vvvvvvvvbbbb","xxxxxxxxxxxx");
			this.mTabHost.setCurrentTabByTag("A_TAB");
			setButton(0);
		}
	
		
	

	}
	private void setButton(int postion){
		for(int i=0;i<mButtons.length;i++){
			mButtons[i].setChecked(false);
		}
		mButtons[postion].setChecked(true);
		for(int m=0;m<mTabViews.length;m++){
			mTabViews[m].setTextColor(getResources().getColor(R.color.tabscolor));
		}
		mTabViews[postion].setTextColor(getResources().getColor(R.color.pink));
		if(postion==2){
			Intent intent = new Intent();
			intent.setAction("action.community");
			sendBroadcast(intent);
		}
	}
	@Override
	protected void onNewIntent(Intent intent) {
		if (this.getIntent().getExtras().getString("TabIndex").toString()
				.equals("A_TAB"))
		{
			this.mTabHost.setCurrentTabByTag("A_TAB");
			setButton(0);
			Log.i("88888====",intent.getStringExtra("Push")+"");
			if(!TextUtils.isEmpty(intent.getStringExtra("Push"))){

				Log.i("88888","888888888888888888===============");
				pushIntent(intent.getStringExtra("MessageCode"),intent.getStringExtra("MessageContent"));
			}
		}else
		if (intent.getExtras().getString("TabIndex").toString()
				.equals("B_TAB"))
		{
			this.mTabHost.setCurrentTabByTag("B_TAB");
			setButton(1);
		}else
		if (intent.getExtras().getString("TabIndex").toString()
				.equals("C_TAB"))
		{
			this.mTabHost.setCurrentTabByTag("C_TAB");
			setButton(2);
		}
		else
		if (intent.getExtras().getString("TabIndex").toString()
				.equals("D_TAB"))
		{
			this.mTabHost.setCurrentTabByTag("D_TAB");
			setButton(3);
		}else{
			this.mTabHost.setCurrentTabByTag("A_TAB");
			setButton(0);
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
	private void pushIntent(String MessageCode,String MessageContent){

Log.i("MessageCode==========",MessageCode+";;;;;"+MessageContent);

			if(MessageCode.equals("P0003")||MessageCode.equals("P0004")||MessageCode.equals("P0006"))
				{
					Intent it;
					Log.i("==========",">>>>>>>><<<<<<<");

					it = new Intent(this,News_Detail.class);

					it.putExtra(
							"ID",
							MessageContent);

//					it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(it);
					Log.i("==========",">>>>>>>>iiiiiiiiii<<<<<<<");
				}
				else if(MessageCode.equals("P0005")){
					Intent i = new Intent(this,
							TestWebView.class);
					if(yu.Week>39){
						i.putExtra("YunWeek", 40+"");
					}else{
						i.putExtra("YunWeek", yu.Week + 1 + "");
					}
					i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(i);


				}else if(MessageCode.equals("P0006")){
					Intent i = new Intent(this,
							News_Detail.class);
					i.putExtra("ID", MessageContent);
					i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(i);
				}

				else if(MessageCode.equals("P0008")){
					if(user.UserID==0){
						Intent i = new Intent(this, LoginActivity.class);
						i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						startActivity(i);

					}else{
						Intent i = new Intent(this,
								MyCHK_Timeline.class);
						i.putExtra("Page", "User_Foot");
						i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

						startActivity(i);
					}
				}
				else if(MessageCode.equals("P0009")){

					Intent i = new Intent(this, SmallTalkAct.class);
					i.putExtra("ID", MessageContent);
					i.putExtra("huifu", "huifu");
					i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(i);
//									Intent int1 = new Intent();
//									intent.setAction("action.push");
//									context.sendBroadcast(int1);

				}	else if(MessageCode.equals("P0010")){

					Intent i = new Intent(this, DoctorHomeAct.class);
					i.putExtra("DoctorID", MessageContent);
					i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(i);

				}else if(MessageCode.equals("P0011")){

					Intent i = new Intent(this, Activity_ReplyPage.class);
					i.putExtra("ID", MessageContent);
					i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(i);

				}else if(MessageCode.equals("P0016")){

					Intent i = new Intent(this, CHK_DoctorList.class);
					i.putExtra("HostiptalID", MessageContent);
					i.putExtra("selectdoc","select");
					i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(i);

				}else if (MessageCode.equals("P0017")) {
//								https://lovestaging.topmd.cn/html/1.html

					String ID = MessageContent;
					Intent i = new Intent(this,
							StaticWebView.class);
					i.putExtra("ID", ID);
					i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(i);

				}else if(MessageCode.equals("P0018")){
					String ID = MessageContent;
					Intent i = new Intent(this,
							MainChatActivity.class);
					i.putExtra("ID", ID);
					i.putExtra("doctorpush", "push");
					i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(i);

				}

			}




//	@Override
//	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
//	{
//		if (isChecked)
//		{
//			switch (buttonView.getId())
//			{
//				case R.id.radio_button0:
//					this.mTabHost.setCurrentTabByTag("A_TAB");
//					setButton(0);
//					break;
//				case R.id.radio_button1:
//					this.mTabHost.setCurrentTabByTag("B_TAB");
//					setButton(1);
//					break;
//				case R.id.radio_button2:
//					this.mTabHost.setCurrentTabByTag("C_TAB");
//					setButton(2);
//					break;
//				case R.id.radio_button3:
//					this.mTabHost.setCurrentTabByTag("D_TAB");
//					setButton(3);
//					break;
//			}
//		}
//
//	}

	private void setupIntent()
	{
		this.mTabHost = getTabHost();
		TabHost localTabHost = this.mTabHost;

		localTabHost.addTab(buildTabSpec("A_TAB", R.string.strnull,
				R.drawable.tab_icon1, this.mAIntent));

		localTabHost.addTab(buildTabSpec("B_TAB", R.string.strnull,
				R.drawable.tab_icon2, this.mBIntent));

		localTabHost.addTab(buildTabSpec("C_TAB", R.string.strnull,
				R.drawable.tab_icon3, this.mCIntent));
		localTabHost.addTab(buildTabSpec("D_TAB", R.string.strnull,
				R.drawable.tab_icon3, this.mDIntent));
		

	}

	private TabHost.TabSpec buildTabSpec(String tag, int resLabel, int resIcon,
			final Intent content)
	{
		return this.mTabHost
				.newTabSpec(tag)
				.setIndicator(getString(resLabel),
						getResources().getDrawable(resIcon))
				.setContent(content);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.activity_main_tab_home_layout:
			this.mTabHost.setCurrentTabByTag("A_TAB");
			setButton(0);
			MobclickAgent.onEvent(this, "tab_index");
			break;
		case R.id.activity_main_sq:
			MyApplication.getInstance().urls="";
			this.mTabHost.setCurrentTabByTag("B_TAB");
			setButton(1);
			MobclickAgent.onEvent(this, "tab_community");
			break;
		case R.id.activity_main_tab_jk:
			this.mTabHost.setCurrentTabByTag("C_TAB");
			setButton(2);
			MobclickAgent.onEvent(this, "tab_privatelist");
			break;
		case R.id.activity_main_zx:
			this.mTabHost.setCurrentTabByTag("D_TAB");
			setButton(3);
			MobclickAgent.onEvent(this, "tab_me");
			break;

		default:
			break;
		}
		
	}
}
