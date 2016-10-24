package com.netlab.loveofmum.wxapi;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.netlab.loveofmum.CHK_network_anomaly;
import com.netlab.loveofmum.CHK_network_anomalythree;
import com.netlab.loveofmum.CHK_network_anomalytwo;
import com.netlab.loveofmum.DoctorDes;
import com.netlab.loveofmum.R;
import com.netlab.loveofmum.alipay.PayActivity;
import com.netlab.loveofmum.api.BaseActivity;
import com.netlab.loveofmum.api.JsonAsyncTaskOnComplete;
import com.netlab.loveofmum.api.JsonAsyncTask_Info;
import com.netlab.loveofmum.api.LocalAccessor;
import com.netlab.loveofmum.api.MMloveConstants;
import com.netlab.loveofmum.api.MyApplication;
import com.netlab.loveofmum.huanxin.MainChatActivity;
import com.netlab.loveofmum.model.User;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class WXPayEntryActivity extends BaseActivity implements IWXAPIEventHandler{
	
	private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";
    private IWXAPI api;
 // 定义Web Service相关的字符串
 	private final String SOAP_NAMESPACE = MMloveConstants.URL001;
 	private final String SOAP_ACTION = MMloveConstants.URL001
 			+ MMloveConstants.AskOrderUpdate;
 	private final String SOAP_METHODNAME = MMloveConstants.AskOrderUpdate;
 	private final String SOAP_URL = MMloveConstants.URL002;
	private final String SOAP_ACTIONORDERINQUIRY= MMloveConstants.URL001
			+ MMloveConstants.AskOrderInquiry;
	private final String SOAP_METHODNAMEORDERINQUIRY= MMloveConstants.AskOrderInquiry;
 	private User user;
 	private MyApplication myApp;
	private String AskOrderID;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_result);
//    	requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0f;
        getWindow().setAttributes(lp);
    	api = WXAPIFactory.createWXAPI(this, "wx2a644a43882160a9");
    	  myApp=MyApplication.getInstance();
    	
    	user = LocalAccessor.getInstance(WXPayEntryActivity.this).getUser();
    	  AskOrderID=user.PayId;
        api.handleIntent(getIntent(), this);
      
        
    }

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
        api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
	}

	@Override
	public void onResp(BaseResp resp) {
		Log.d(TAG, "onPayFinish, errCode = " + resp.errCode);
       //微信支付状态
		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX&&resp.errCode==0) {
		
//			AlertDialog.Builder builder = new AlertDialog.Builder(this);
//			builder.setTitle(R.string.app_tip);
//			builder.setMessage(getString(R.string.pay_result_callback_msg, String.valueOf(resp.errCode)));
//			builder.show();
			if(hasInternetConnected())
				AskOrderInquiry();
			else{
//				Toast.makeText(WXPayEntryActivity.this, "网络数据获取失败，请检查网络设置", 1).show();
				Intent i = new Intent(WXPayEntryActivity.this,
						CHK_network_anomalythree.class);
				i.putExtra("AskOrderID", AskOrderID);
				i.putExtra("DoctorId", myApp.DoctorId);
				i.putExtra("HospitalID", myApp.Hosid);
				i.putExtra("DoctorImageUrl", myApp.DoctorUrl);
				i.putExtra("DoctorName", myApp.DoctorName);
				i.putExtra("Hosname", myApp.HosName);
				i.putExtra("Title", myApp.Title);
				startActivity(i);
			}
		}else{
			finish();
		}
	}
	private void AskOrderInquiry() {


		// 必须是这5个参数，而且得按照顺序
		JsonAsyncTaskOnComplete doProcess=new JsonAsyncTaskOnComplete(){

			@Override
			public void processJsonObject(Object result) {
				// TODO Auto-generated method stub
				String returnvalue= result.toString();
				try {


					JSONObject mySO = (JSONObject) result;
					JSONArray array = mySO
							.getJSONArray("AskOrderInquiry");
					int status=Integer.valueOf(array.getJSONObject(0)
							.getString("OrderStatus").toString());
					if(status==4){

						Toast.makeText(WXPayEntryActivity.this,"该订单已经结束",1).show();

					}else{

						AskOrderUpdate(2);
					}



				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();


				}

			}};

		JsonAsyncTask_Info task = new JsonAsyncTask_Info(
				WXPayEntryActivity.this, true, doProcess);
		Map<String, Object> paramMap = new HashMap<String, Object>();
		String str = "<Request><UserID>%s</UserID><Reason>%s</Reason><PayType>%s</PayType><PayStatus>%s</PayStatus><CustomerIP>%s</CustomerIP><HospitalID>%s</HospitalID><HospitalName>%s</HospitalName><DeptRowID>%s</DeptRowID><DeptDesc>%s</DeptDesc>" +
				"<DoctorID>%s</DoctorID><DoctorName>%s</DoctorName><RealName>%s</RealName><UserNO>%s</UserNO><UserMobile>%s</UserMobile><AskOrderID>%s</AskOrderID></Request>";

		str = String.format(str, new Object[]
				{ user.UserID+"","","","","","","","","","","",user.RealName,user.UserNO,user.UserMobile,AskOrderID});

		paramMap.put("str", str);
		task.execute(SOAP_NAMESPACE, SOAP_ACTIONORDERINQUIRY, SOAP_METHODNAMEORDERINQUIRY,
				SOAP_URL, paramMap);
	}
	protected void AskOrderUpdate(int paytype) {
		// TODO Auto-generated method stub
		JsonAsyncTaskOnComplete doProcess=new JsonAsyncTaskOnComplete(){

			@Override
			public void processJsonObject(Object result) {
				// TODO Auto-generated method stub
				String returnvalue= result.toString();
//				{"AskOrderUpdate":[{"MessageContent":"成功！","MessageCode":"0"}]}
				try {
                JSONObject mySO = (JSONObject) result;
					JSONArray array = mySO
							.getJSONArray("AskOrderUpdate");
					if("0".equals(array.getJSONObject(0).getString("MessageCode"))){
						
					
						AskOrderID=user.PayId;
						user.PayId="";

						LocalAccessor.getInstance(WXPayEntryActivity.this).updateUser(user);
				
						Intent i = new Intent(WXPayEntryActivity.this,
								CHK_network_anomalytwo.class);
//						Intent i = new Intent(WXPayEntryActivity.this,
//								CHK_network_anomaly.class);
						
						i.putExtra("AskOrderID", AskOrderID);
						i.putExtra("DoctorId", myApp.DoctorId);
						i.putExtra("HospitalID", myApp.Hosid);
						i.putExtra("DoctorImageUrl", myApp.DoctorUrl);
						i.putExtra("DoctorName", myApp.DoctorName);
						i.putExtra("Hosname", myApp.HosName);
						i.putExtra("Title", myApp.Title);
						i.putExtra("Status",2);
						startActivity(i);
						finish();
						
					}
//					Intent 
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Intent i = new Intent(WXPayEntryActivity.this,
							CHK_network_anomalythree.class);
					i.putExtra("AskOrderID", AskOrderID);
					i.putExtra("DoctorId", myApp.DoctorId);
					i.putExtra("HospitalID", myApp.Hosid);
					i.putExtra("DoctorImageUrl", myApp.DoctorUrl);
					i.putExtra("DoctorName", myApp.DoctorName);
					i.putExtra("Hosname", myApp.HosName);
					i.putExtra("Title", myApp.Title);
					startActivity(i);
					finish();
				}
//					
//					JSONArray array = mySO
//							.getJSONArray("DoctorInquiry");
//					OrderID=array.getJSONObject(0)
//					.getString("OrderID");
//					Intent i = new Intent(DoctorDes.this,
//							PayActivity.class);
//					i.putExtra("OrderID", OrderID);
//				    startActivity(i);
				
			}};
		JsonAsyncTask_Info task = new JsonAsyncTask_Info(
				WXPayEntryActivity.this, true, doProcess);
		Map<String, Object> paramMap = new HashMap<String, Object>();
//		string OrderID = "";
//        string OrderStatus = "";
//        string Reason = "";
//        string PayType = "";
//        string PayStatus = "";

		String str = "<Request><AskOrderID>%s</AskOrderID><OrderStatus>%s</OrderStatus><Reason>%s</Reason><PayType>%s</PayType><PayStatus>%s</PayStatus></Request>";
		 AskOrderID=user.PayId;
		str = String.format(str, new Object[]
		{ AskOrderID,"02","",paytype+"",""});
         
		paramMap.put("str", str);

		// 必须是这5个参数，而且得按照顺序
		task.execute(SOAP_NAMESPACE, SOAP_ACTION, SOAP_METHODNAME,
				SOAP_URL, paramMap);
	}

	@Override
	protected void iniView() {
		// TODO Auto-generated method stub
		
	}
}
