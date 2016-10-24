package com.netlab.loveofmum;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;


import com.netlab.loveofmum.api.BaseActivity;

import com.netlab.loveofmum.api.JsonAsyncTaskOnComplete;
import com.netlab.loveofmum.api.JsonAsyncTask_Info;
import com.netlab.loveofmum.api.LocalAccessor;
import com.netlab.loveofmum.api.MMloveConstants;
import com.netlab.loveofmum.model.User;
import com.netlab.loveofmum.widget.ChartView;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class CHK_DeteWeight extends BaseActivity {
	private EditText editText01;
	private EditText editText02;
	private String returnvalue001;
	private User user;
    public static String bankId="";
	private final String SOAP_NAMESPACE = MMloveConstants.URL001;
	private final String SOAP_ACTION = MMloveConstants.URL001 + MMloveConstants.WeightInsert;
	private final String SOAP_METHODNAME = MMloveConstants.WeightInsert;
	private final String SOAP_URL = MMloveConstants.URL002;
	private EditText degreeID1;
	private ImageView needleView; // 指针图片
	private Timer timer; // 时间
	private EditText degreeText;
	private TextView showText;
	private Button degreeBtn;
	private float maxDegree = 0.0f;
	private float degree = 0.0f; // 记录指针旋转
	private RotateAnimation animation;
	private boolean flag = true;
	private boolean isExit = false;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_pregnancy_weight_statistics);
		setView();
		LayoutInflater inflater = getLayoutInflater();
		user = LocalAccessor.getInstance(this).getUser();
		View layout = inflater.inflate(R.layout.item_weight, null);
		editText01 = (EditText) layout.findViewById(R.id.edittext01);
		editText02 = (EditText) layout.findViewById(R.id.edittext02);
		AlertDialog.Builder builder = new AlertDialog.Builder(CHK_DeteWeight.this);
		
		builder.setTitle("自定义布局").setView(layout).setPositiveButton("确定", new OnClickListener() {

			@Override
			
			public void onClick(DialogInterface arg0, int arg1) {
				searchCHK();
				
				bankId=editText01.getText().toString().trim();
				
//				Log.i("bankId",bankId);        //debug的日志 
				degreeText.setText(bankId);
				setPreferenceData(bankId);

			}
		}).setNegativeButton("取消", null).create().show();
	

	}


	private void setView() {
		needleView = (ImageView) findViewById(R.id.needle);
		degreeID1 = (EditText) findViewById(R.id.degreeID2);
		degreeText = (EditText) findViewById(R.id.degreeID2);
		degreeBtn = (Button) findViewById(R.id.degreeButton);
		showText=(TextView)findViewById(R.id.degreeID);
		final ChartView view = (ChartView) findViewById(R.id.myview);
				degreeBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
//				maxDegree = Float.parseFloat(degreeText.getText().toString()
//						.trim());
//				// 开始转动
//				timer = new Timer();
//				// 设置每100毫秒转动一下
//				timer.schedule(new NeedleTask(), 0, 10);
//				showText.setText(degreeText.getText().toString()
//						.trim());
//				flag = true;
				String tizhong  = getPreferenceData();
				String []tizhongDatas = tizhong.split(",");
				view.SetInfo(
				        new String[]{"7-11","7-12","7-13","7-14","7-15","7-16","7-17"},   //X轴刻度
				        new String[]{"","50","100","150","200","250"},   //Y轴刻度
				        tizhongDatas,  //数据
				        "图标的标题"
				);

				if(isExit){
					view.setAnimation(AnimationUtils.loadAnimation(CHK_DeteWeight.this,
							R.anim.pop_exit));
					view.setVisibility(View.GONE);
					isExit = !isExit;
				}else{
				view.setAnimation(AnimationUtils.loadAnimation(CHK_DeteWeight.this,
						R.anim.pop_enter));
				view.setVisibility(View.VISIBLE);
				     isExit = !isExit;
				}
				
			}
		});
		degreeID1.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				if(!degreeText.getText().toString().trim().equals("")){
				maxDegree = Float.parseFloat(degreeText.getText().toString()
						.trim());
				// 开始转动
				timer = new Timer();
				// 设置每100毫秒转动一下
				timer.schedule(new NeedleTask(), 0, 10);
				showText.setText(degreeText.getText().toString()
						.trim());
				flag = true;
				}
			}
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
			}
			@Override
			public void afterTextChanged(Editable arg0) {
				
			}
		});
	}
	/*
	 * 查询gridview
	 */

	private void searchCHK() {
		try {
			JsonAsyncTaskOnComplete doProcess = new JsonAsyncTaskOnComplete() {
				public void processJsonObject(Object result) {
					returnvalue001 = result.toString();
					if (returnvalue001 == null) {

					} else {
						System.out.println(returnvalue001);
					}
				}
			};

			JsonAsyncTask_Info task = new JsonAsyncTask_Info(CHK_DeteWeight.this, true, doProcess);
			Map<String, Object> paramMap = new HashMap<String, Object>();
			String str = "<Request><UserID>%s</UserID><Weight>%s</Weight><Height>%s</Height></Request>";
			str = String.format(str, new Object[] { String.valueOf(user.UserID), editText01.getText().toString().trim(), editText02.getText().toString().trim() });
			paramMap.put("str", str);

			// 必须是这5个参数，而且得按照顺序
			task.execute(SOAP_NAMESPACE, SOAP_ACTION, SOAP_METHODNAME, SOAP_URL, paramMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void iniView() {
		editText01 = (EditText) findViewById(R.id.edittext01);
		editText02 = (EditText) findViewById(R.id.edittext02);
	}
	private class NeedleTask extends TimerTask {
		@Override
		public void run() {
			if (degree <= maxDegree * (271 / 100.0f)) {
				handler.sendEmptyMessage(0);
			}
			if (degree > maxDegree * (271 / 100.0f) && flag == true) {
				handler2.sendEmptyMessage(0);
			}
		}
	} 
   
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// 设置仪表盘指针转动动画
			// 仪表盘最大是271度
			if (degree >= maxDegree * (271 / 100.0f)) {
				timer.cancel();
			}else{
			degree += 5.0f;
			animation = new RotateAnimation(degree, maxDegree * (271 / 100.0f),
					Animation.RELATIVE_TO_SELF, 0.5f,
					Animation.RELATIVE_TO_SELF, 0.5f);
			}
			// 设置动画时间1秒
			animation.setDuration(10);
			animation.setFillAfter(true);
			needleView.startAnimation(animation);
			flag = false;
		}
	};

	private Handler handler2 = new Handler() {
		@Override
		public void handleMessage(Message msg) { // 设置仪表盘指针转动动画
			// 仪表盘最大是172度，这个是自己测出来的
			if (degree <= maxDegree * (271 / 100.0f)) {
				timer.cancel();
			}else{
			degree += -5.0f;
			animation = new RotateAnimation(degree, maxDegree * (271 / 100.0f),
					Animation.RELATIVE_TO_SELF, 0.5f,
					Animation.RELATIVE_TO_SELF, 0.5f);
			}
			// 设置动画时间5毫秒
			animation.setDuration(10);
			animation.setFillAfter(true);
			needleView.startAnimation(animation);
			flag = true;
		}
	};
	
	/**
	 * 关闭计时器对象
	 */
	public void onDestroy() {
		timer.cancel();
		timer = null;
		super.onDestroy();

	};
	
	private void setPreferenceData(String data){
		SharedPreferences preferences = getSharedPreferences("TIZHONG", 
				Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		String tizhong = null;
		String str = getPreferenceData();
		if(str.equals("")){
			tizhong = data;
		}else{
			tizhong = str+","+data;
		}
		editor.putString("tizhong", tizhong);
		editor.commit();
	}
    private String getPreferenceData(){
    	SharedPreferences preferences = getSharedPreferences("TIZHONG", 
				Context.MODE_PRIVATE);
    	String data = preferences.getString("tizhong", "");
		return data;
    }
}
