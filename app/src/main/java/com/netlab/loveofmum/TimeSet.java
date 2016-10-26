package com.netlab.loveofmum;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import com.netlab.loveofmum.api.BaseActivity;
import com.netlab.loveofmum.api.MMloveConstants;
import com.netlab.loveofmum.api.JsonAsyncTaskOnComplete;
import com.netlab.loveofmum.api.JsonAsyncTask_Info;
import com.netlab.loveofmum.api.LocalAccessor;
import com.netlab.loveofmum.api.MyApplication;
import com.netlab.loveofmum.model.User;
import com.netlab.loveofmum.timepicker.DateDrumPicker;
import com.netlab.loveofmum.utils.IOUtils;
import com.netlab.loveofmum.utils.SystemStatusManager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.DatePicker.OnDateChangedListener;

public class TimeSet extends BaseActivity
{
	private final static String TAG = TimeSet.class.getSimpleName();

	// private Button btnOK;
	//@ViewInject(id = R.id.btnOK, click = "btnOKClick")
	//Button btnOK;

	private int years = 0;
	private int month = 0;
	private int day = 0;
	private String DATE_FORMAT = "";

	private User user;
	
	private Button btnOK;
	
	private String clientID;

	// 定义Web Service相关的字符串
	private final String SOAP_NAMESPACE = MMloveConstants.URL001;
	private final String SOAP_ACTION = MMloveConstants.URL001
			+ MMloveConstants.UserTimeChange;
	private final String SOAP_METHODNAME = MMloveConstants.UserTimeChange;
	private final String SOAP_URL = MMloveConstants.URL002;

	private String returnvalue001;

	private DatePicker dp;


	private Calendar ca;
	private RadioGroup group;
	private RadioButton rbleft, rbright;
	private int  index=1;
	private TextView tvyuchan;

	private TextView tvlastyuejing;
	private Handler handler = new Handler() {
	@Override
	public void handleMessage(Message msg) {
		super.handleMessage(msg);
	
		switch (msg.what) {
		case 0:
			tvlastyuejing.setVisibility(View.VISIBLE);
			tvlastyuejing.setText("输入末次月经开始时间");
			index=2;
			String date = years + "-"
					+ (month+1)+ "-"
					+ day;
			String dateNum=IOUtils.ComputeYuchan(date);
			String[] time=dateNum.split("\\-");
			dp.updateDate(years, month, day);
			tvyuchan.setText("您的预产期是:"+time[0]+"年"+time[1]+"月"+time[2]+"日");

			break;
		case 1:
			tvlastyuejing.setVisibility(View.INVISIBLE);
			tvlastyuejing.setText("");
			dp.updateDate(years, month, day);
			index=1;
			tvyuchan.setText("您的预产期是:"+years+"年"+(month+1)+"月"+day+"日");
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
		setTranslucentStatus();
		DATE_FORMAT = "%04d" + getString(R.string.year) + "%02d"
				+ getString(R.string.month) + "%02d" + getString(R.string.day);

		Calendar calendar = Calendar.getInstance();

		setContentView(R.layout.layout_timeset);
	
		iniView();

		user = LocalAccessor.getInstance(TimeSet.this).getUser();

		dp = (DatePicker) findViewById(R.id.datepicker);
	
		ca = null;
		if (user.YuBirthDate != null)
		{

			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			Date daystart;
			try
			{
				daystart = df.parse(user.YuBirthDate);
				ca = new GregorianCalendar();
				ca.setTime(daystart);
			}
			catch (ParseException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		else
		{
			ca = Calendar.getInstance();
		}
		years = ca.get(Calendar.YEAR);
		month = ca.get(Calendar.MONTH);
		day = ca.get(Calendar.DAY_OF_MONTH);
		dp.updateDate(ca.YEAR, ca.MONTH, ca.DAY_OF_MONTH);
		dp.init(ca.get(Calendar.YEAR), ca.get(Calendar.MONTH),
				ca.get(Calendar.DAY_OF_MONTH),
				new OnDateChangedListener()
				{
					// 监听选择的出生日期
					@Override
					public void onDateChanged(DatePicker view, int year,
							int monthOfYear, int dayOfMonth)
					{
						// TODO Auto-generated method stub
						ca.set(year, monthOfYear, dayOfMonth);// 改变日期
						years=year;
						month= monthOfYear;
						day=dayOfMonth;
						String date = years + "-"
								+ (month+1)+"-"
								+ day;
						if(index==1){
							tvyuchan.setText("您的预产期是:"+year+"年"+(month+1)+"月"+dayOfMonth+"日");
						}else if(index==2) {
							String dateNum=IOUtils.ComputeYuchan(date);
							String[] time=dateNum.split("\\-");
							tvyuchan.setText("您的预产期是:"+time[0]+"年"+time[1]+"月"+time[2]+"日");

						}

						// 取得值
					}
				});
//		resizeTimerPicker(dp);
		
//		setDatePickerDividerColor1(dp);
		setDatePickerDividerColor(dp);
		//PushManager.getInstance().initialize(this.getApplicationContext());
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
		tintManager.setStatusBarTintResource(R.color.home);// 状态栏无背景
	}
	
	
	/**
     * 
     * 设置时间选择器的分割线颜色
     * @param datePicker
     */
    private void setDatePickerDividerColor(DatePicker datePicker){
        // Divider changing:
         
        // 获取 mSpinners
        LinearLayout llFirst       = (LinearLayout) datePicker.getChildAt(0);
        
        // 获取 NumberPicker 
        LinearLayout mSpinners      = (LinearLayout) llFirst.getChildAt(0);
        for (int i = 0; i < mSpinners.getChildCount(); i++) {
            NumberPicker picker = (NumberPicker) mSpinners.getChildAt(i); 
             
            Field[] pickerFields = NumberPicker.class.getDeclaredFields();
            for (Field pf : pickerFields) {
                if (pf.getName().equals("mSelectionDivider")) {
                    pf.setAccessible(true);
                    try {
                        pf.set(picker, new ColorDrawable(getResources().getColor(R.color.timeset_color2)));
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (NotFoundException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }
        }
//        resizePikcer(datePicker);//调整datepicker大小
        
    }
    /**
	 * 调整FrameLayout大小
	 * @param tp
	 */
	private void resizePikcer(FrameLayout tp){
		List<NumberPicker> npList = findNumberPicker(tp);
		for(NumberPicker np:npList){
			resizeNumberPicker(np);
		}
	}
	/**
	 * 得到viewGroup里面的numberpicker组件
	 * @param viewGroup
	 * @return
	 */
	private List<NumberPicker> findNumberPicker(ViewGroup viewGroup){
		List<NumberPicker> npList = new ArrayList<NumberPicker>();
		View child = null;
		if(null != viewGroup){
			for(int i = 0;i<viewGroup.getChildCount();i++){
				child = viewGroup.getChildAt(i);
				if(child instanceof NumberPicker){
					npList.add((NumberPicker)child);
				}
				else if(child instanceof LinearLayout){
					List<NumberPicker> result = findNumberPicker((ViewGroup)child);
					if(result.size()>0){
						return result;
					}
				}
			}
		}
		return npList;
	}
	/*
	 * 调整numberpicker大小
	 */
	private void resizeNumberPicker(NumberPicker np){
		LayoutParams params = new LayoutParams(100, LayoutParams.WRAP_CONTENT);
		params.setMargins(50, 0, 50, 0);
		np.setLayoutParams(params);
	}
	

	protected void iniView()
	{
		// TODO Auto-generated method stub
		mContext = this;
		
		btnOK = (Button)findViewById(R.id.btnOK);
		tvlastyuejing=(TextView)findViewById(R.id.tv_lastyuejing);
		// validateInternet();
		// PushNotification(R.drawable.ic_launcher, "测试", "内容测试",
		// TimeSet.class,"嘻嘻");
		group = (RadioGroup) findViewById(R.id.radioGroup);
		rbleft = (RadioButton) findViewById(R.id.date_set);
		rbright = (RadioButton) findViewById(R.id.date_yuejing);
		tvyuchan=(TextView) findViewById(R.id.tv_yuchan);
		group.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				switch (checkedId) {
				case R.id.date_set:
					index=1;
					rbleft.setChecked(true);
					rbleft.setTextColor(getResources().getColor(R.color.white));
					rbright.setChecked(false);
					rbright.setTextColor(getResources().getColor(R.color.timeset_color));
					Message msg = new Message();
					msg.what = 1;
					handler.sendMessage(msg);
//					tvlastyuejing.setText("");
//			tvlastyuejing.setVisibility(View.INVISIBLE);
					break;
				case R.id.date_yuejing:
				    index=2;
					rbleft.setChecked(false);
					rbright.setChecked(true);
					rbleft.setTextColor(getResources().getColor(R.color.timeset_color));
					rbright.setTextColor(getResources().getColor(R.color.white));
					Message msg1 = new Message();
					msg1.what = 0;
					handler.sendMessage(msg1);
				
					break;

				default:
					break;
				}
			}
		});
		btnOK.setOnClickListener(new View.OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				try
				{
					
					if (hasInternetConnected())
					{
						String date = ca.get(Calendar.YEAR) + "-"
								+ (ca.get(Calendar.MONTH) + 1) + "-"
								+ ca.get(Calendar.DAY_OF_MONTH);
						if(index==1){
						
						
						DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");

						if (IOUtils.IsBeyondTimeSet(fmt.parse(date)))
						{
							Toast.makeText(TimeSet.this, "请设置正确的预产时间！", Toast.LENGTH_SHORT)
									.show();
						}
						else
						{
							user = LocalAccessor.getInstance(TimeSet.this).getUser();

							user.YuBirthDate=date;
							LocalAccessor.getInstance(TimeSet.this).updateUser(user);
//							if(user.HospitalID==0)
//							{
//								Intent i = new Intent(TimeSet.this, Hos_Select.class);
//
//								startActivity(i);
//								return;
//							}

							if (user.UserID == 0)
							{
								setGuided();
								Intent i = new Intent(TimeSet.this, MainTabActivity.class);
								i.putExtra("TabIndex", "A_TAB");

								startActivity(i);
								finish();
							}
							else
							{
								UserTimeSet();
							}
						}
					}
						else if(index==2){
							DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");

//							if (!IOUtils.IsYuejingTimeSet(fmt.parse(date)))
//							{
//								Toast.makeText(TimeSet.this, "请设置正确的末次月经时间！", Toast.LENGTH_SHORT)
//										.show();
//							}
//							else
//							{
								user = LocalAccessor.getInstance(TimeSet.this).getUser();
								
								String birth=IOUtils.ComputeYuchan(date);
							
								if (IOUtils.IsBeyondTimeSet(fmt.parse(birth)))
								{
									Toast.makeText(TimeSet.this, "请设置正确的末次月经时间！", Toast.LENGTH_SHORT)
											.show();
									return;
								}
//							   if(dBirth.compareTo(dEnd)>=0){
//								   Toast.makeText(TimeSet.this, "请设置正确的末次月经时间！", Toast.LENGTH_SHORT)
//									.show();
//								   return;
//							   }
								user.YuBirthDate = IOUtils.ComputeYuchan(date);

							LocalAccessor.getInstance(TimeSet.this).updateUser(user);
//								if(user.HospitalID==0)
//								{
//									Intent i = new Intent(TimeSet.this, Hos_Select.class);
	//
//									startActivity(i);
//									return;
//								}

								if (user.UserID == 0)
								{
									Intent i = new Intent(TimeSet.this, MainTabActivity.class);
									i.putExtra("TabIndex", "A_TAB");

									startActivity(i);
									finish();
								}
								else
								{
									UserTimeSet();
								}
//							}
							
						}
					}
					else
					{
						Toast.makeText(TimeSet.this, R.string.msgUninternet,
								Toast.LENGTH_SHORT).show();
					}
				}
				catch (Exception e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		});
	}
	private void setGuided() {
		SharedPreferences preferences = getSharedPreferences(
				"first_pref", Context.MODE_PRIVATE);

		PackageManager manager = getPackageManager();
		PackageInfo info;
		try
		{
			info = manager.getPackageInfo(getPackageName(), 0);
			int newCode = info.versionCode; // 版本号
			SharedPreferences.Editor editor = preferences.edit();
			editor.putInt("VersionCode", newCode);
			editor.commit();
		}
		catch (PackageManager.NameNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	private void UserTimeSet()
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
							final JSONArray array = mySO
									.getJSONArray("UserTimeChange");

							if (array.getJSONObject(0).has("MessageCode"))
							{
								if (array.getJSONObject(0).get("MessageCode")
										.toString().equals("0"))
								{
//									Intent i = new Intent(TimeSet.this,
//											MainTabActivity.class);
//									i.putExtra("TabIndex", "A_TAB");
//
//									startActivity(i);

									finish();
								}
							}
							else
							{

							}

						}
						catch (Exception ex)
						{
							ex.printStackTrace();
						}
					}
				}
			};

			JsonAsyncTask_Info task = new JsonAsyncTask_Info(TimeSet.this,
					true, doProcess);
			Map<String, Object> paramMap = new HashMap<String, Object>();
			String str = "<Request><UserID>%s</UserID><ClientID>%s</ClientID><YuBirthTime>%s</YuBirthTime></Request>";
			str = String.format(str, new Object[]
			{ String.valueOf(user.UserID), "", user.YuBirthDate });

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
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		ca = null;
		dp=null;
	}

}
