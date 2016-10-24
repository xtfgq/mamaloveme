package com.netlab.loveofmum;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.netlab.loveofmum.api.BaseActivity;
import com.netlab.loveofmum.api.JsonAsyncTaskOnComplete;
import com.netlab.loveofmum.api.JsonAsyncTask_Info;
import com.netlab.loveofmum.api.LocalAccessor;
import com.netlab.loveofmum.api.MMloveConstants;
import com.netlab.loveofmum.api.MyApplication;

import com.netlab.loveofmum.model.User;
import com.netlab.loveofmum.myadapter.FeedAadapter;
import com.netlab.loveofmum.myadapter.PrivateDortorAdapter;
import com.netlab.loveofmum.utils.ImageOptions;
import com.netlab.loveofmum.utils.SystemStatusManager;
import com.umeng.analytics.MobclickAgent;


public class PrivateDoctor extends BaseActivity {
	private User user;
	private TextView txtHead;
	private ImageView imgBack;
	private ListView listview;
	
	private final String SOAP_ACTIONCONTENTINQUIRY= MMloveConstants.URL001
			+ MMloveConstants.AskOrderInquiry;
	private final String SOAP_METHODNAMCONTENTINQUIRY= MMloveConstants.AskOrderInquiry;
	private final String SOAP_NAMESPACE = MMloveConstants.URL001;

	private final String SOAP_URL = MMloveConstants.URL002;
	private List<Map<String,Object>> arrayList=new ArrayList<Map<String,Object>>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setTranslucentStatus() ;
		setContentView(R.layout.layout_chk_doctorlist);
		user = LocalAccessor.getInstance(PrivateDoctor.this).getUser();
		if(hasInternetConnected())
		{
			
			iniView();
			setListeners();
			 AskOrderInquiry();
		}
		else
		{
			Toast.makeText(PrivateDoctor.this, R.string.msgUninternet,
					Toast.LENGTH_SHORT).show();
		}
		MyApplication.getInstance().addActivity(this);
	}

	@Override
	protected void iniView() {
		// TODO Auto-generated method stub
		txtHead = (TextView) findViewById(R.id.txtHead);
		imgBack = (ImageView) findViewById(R.id.img_left);
		txtHead.setText("我的咨询");
		listview=(ListView) findViewById(R.id.listview_chk_doctorlist);
		
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
	private void AskOrderInquiry() {
		// TODO Auto-generated method stub
//		AskOrderInquiry  列表
//		string UserID = "";
//		                string Reason = "";
//		                string PayType = "";
//		                string PayStatus = "";
//		                string CustomerIP = "";
//		                string HospitalID = "";
//		                string HospitalName = "";
//		                string DeptRowID = "";
//
//		                string DeptDesc = "";
//		                string DoctorID = "";
//		                string DoctorName = "";
//		                string RealName = "";
//		                string UserNO = "";
//		                string UserMobile = "";
//		                string OrderID = "";
//		AskOrderInquiry": [
//        {
//            "OrderStatus": "02",
//            "DeptRowID": "",
//            "DeleteFlag": "0",
//            "UserNO": "410102198201251510",
//            "DeptDesc": "",
//            "CustomerIP": "",
//            "DoctorName": "赵先兰",
//            "UserID": "50694",
//            "RealName": "郭强",
//            "HospitalName": "郑州大学第一附属医院",
//            "AskOrderID": "2015121511042950694",
//            "UserMobile": "15637147870",
//            "PayStatus": "",
//            "UpdatedDate": "2015\/12\/15 11:04:29",
//            "HospitalID": "46",
//            "CreatedDate": "2015\/12\/15 11:04:29",
//            "CreatedBy": "",
//            "ID": "460",
//            "DoctorID": "53",
//            "UpdatedBy": "",
//            "PayType": "0",
//            "Reason": ""
//        },

		// 必须是这5个参数，而且得按照顺序
		JsonAsyncTaskOnComplete doProcess=new JsonAsyncTaskOnComplete(){

			@Override
			public void processJsonObject(Object result) {
				// TODO Auto-generated method stub
				String returnvalue= result.toString();
				Map<String, Object> map;
				try {
				JSONObject mySO = (JSONObject) result;
					JSONArray array = mySO
							.getJSONArray("AskOrderInquiry");
//					  "OrderStatus": "01",
//			            "DeptRowID": "",
//			            "DocPicURL": "~\\UpLoadFile\\Doctor\\2015-07-16/20150716093757.JPG",
//			            "DeleteFlag": "0",
//			            "UserNO": "",
//			            "DeptDesc": "",
//			            "CustomerIP": "",
//			            "DoctorName": "韩宁",
//			            "UserID": "50729",
//			            "RealName": "",
//			            "ConsultationContent": "",
//			            "HospitalName": "郑州大学第三附属医院",
//			            "AskOrderID": "2015122215373050729",
//			            "UserMobile": "18037829251",
//			            "PayStatus": "",
//			            "UpdatedDate": "2015/12/22 15:37:30",
//			            "HospitalID": "1",
//			            "CreatedDate": "2015/12/22 15:37:30",
//			            "CreatedBy": "",
//			            "ID": "895",
//			            "DoctorID": "40",
//			            "UpdatedBy": "",
//			            "PayType": "0",
//			            "Reason": ""

//					"AskOrderInquiry": [
//					                    {
//					                        "OrderStatus": "04",
//					                        "DeptRowID": "",
//					                        "DocPicURL": "~\\UpLoadFile\\Doctor\\2015-08-06\/20150806114200.JPG",
//					                        "DeleteFlag": "0",
//					                        "UserNO": "410102198201251510",
//					                        "DeptDesc": "",
//					                        "CustomerIP": "",
//					                        "DoctorName": "赵先兰",
//					                        "UserID": "50694",
//					                        "RealName": "郭芮",
//					                        "ConsultationContent": "",
//					                        "HospitalName": "郑州大学第一附属医院",
//					                        "AskOrderID": "2015122113583750694",
//					                        "UserMobile": "15637147870",
//					                        "PayStatus": "",
//					                        "UpdatedDate": "2015\/12\/21 14:15:39",
//					                        "HospitalID": "46",
//					                        "CreatedDate": "2015\/12\/21 13:58:37",
//					                        "CreatedBy": "",
//					                        "ID": "785",
//					                        "DoctorID": "53",
//					                        "UpdatedBy": "",
//					                        "PayType": "0",
//					                        "Reason": ""
//					                    },

					if(array.getJSONObject(0).has("MessageCode")){
						
					}else{
						for(int i=0;i<array.length();i++){
							map = new HashMap<String, Object>();
							map.put("OrderStatus",array.getJSONObject(i).get("OrderStatus").toString());
							map.put("DoctorID",array.getJSONObject(i).get("DoctorID").toString());
							map.put("HospitalID",array.getJSONObject(i).get("HospitalID").toString());
							map.put("DocPicURL"
									,MMloveConstants.JE_BASE_URL2+array.getJSONObject(i).get("DocPicURL").toString().replace("~", "").replace("\\", "/"));
							map.put("DoctorName",array.getJSONObject(i).get("DoctorName").toString());
							map.put("UpdatedDate",array.getJSONObject(i).get("UpdatedDate").toString());
							map.put("DoctorName",array.getJSONObject(i).get("DoctorName").toString());
							map.put("AskOrderID",array.getJSONObject(i).get("AskOrderID").toString());
							
							map.put("DocPicURL",MMloveConstants.JE_BASE_URL2 + array.getJSONObject(i).get("DocPicURL").toString().replace("~", "").replace("\\", "/")
									);
							map.put("ConsultationContent",array.getJSONObject(i).get("ConsultationContent").toString());
							if(!"".equals(array.getJSONObject(i).get("ConsultationContent").toString()))
							arrayList.add(map);
						}
						PrivateDortorAdapter adapter = new PrivateDortorAdapter(
								PrivateDoctor.this, arrayList);
//						Toast.makeText(getApplicationContext(), myApplication.arrayListyoumeng0.size()+"", 1).show();
						adapter.notifyDataSetChanged();
		                listview.setAdapter(adapter);
						
					}
					
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					
					
				}
				
			}};
		
		JsonAsyncTask_Info task = new JsonAsyncTask_Info(
				PrivateDoctor.this, true, doProcess);
		Map<String, Object> paramMap = new HashMap<String, Object>();
		String str = "<Request><UserID>%s</UserID><Reason>%s</Reason><PayType>%s</PayType><PayStatus>%s</PayStatus><CustomerIP>%s</CustomerIP><HospitalID>%s</HospitalID><HospitalName>%s</HospitalName><DeptRowID>%s</DeptRowID><DeptDesc>%s</DeptDesc>" +
				"<DoctorID>%s</DoctorID><DoctorName>%s</DoctorName><RealName>%s</RealName><UserNO>%s</UserNO><UserMobile>%s</UserMobile><AskOrderID>%s</AskOrderID></Request>";
		str = String.format(str, new Object[]
				{ user.UserID+"","","","","","","","","","","",user.RealName,user.UserNO,user.UserMobile,""});
	
	
         
		paramMap.put("str", str);
		task.execute(SOAP_NAMESPACE, SOAP_ACTIONCONTENTINQUIRY, SOAP_METHODNAMCONTENTINQUIRY,
				SOAP_URL, paramMap);
	}
}
