package com.netlab.loveofmum.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.kxml2.kdom.Element;
import org.kxml2.kdom.Node;

import com.netlab.loveofmum.CHK_network_anomalythree;
import com.netlab.loveofmum.MyCHK_OrderDetail;
import com.netlab.loveofmum.R;
import com.netlab.loveofmum.model.User;
import com.netlab.loveofmum.wxapi.WXPayEntryActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

public class JsonAsyncTask_Info extends AsyncTask<Object, Integer, JSONObject>
{
	private JsonAsyncTaskOnComplete	mySATCM					= null;
	private boolean					ShowProgressDialog		= false;
	private Context					myContext				= null;
	private ProgressDialog			myProgressDialog		= null;
	private String					ProgressDialogTitle		= "";
	private String					ProgressDialogMessage	= "努力加载中，请稍候...."; 
	private MyApplication myApp;
	private User user;
	private String AskOrderID;
	public JsonAsyncTask_Info(Context context, boolean show,
			JsonAsyncTaskOnComplete method)
	{
		user = LocalAccessor.getInstance(context).getUser();
		 AskOrderID=user.PayId;
		  myApp=MyApplication.getInstance();
		myContext = context;
		ShowProgressDialog = show;
		mySATCM = method;
	}
	
	@Override
	protected void onPreExecute()
	{
		// TODO Auto-generated method stub
		 if (ShowProgressDialog && myContext != null)
		{
//			myProgressDialog = new ProgressDialog(myContext, 0);
//			myProgressDialog.setIcon(android.R.drawable.ic_dialog_info);
//			myProgressDialog.setMessage(ProgressDialogMessage);
//			myProgressDialog.setTitle(ProgressDialogTitle);
//			myProgressDialog.setCancelable(false);
//			myProgressDialog.show();
		}
		 
		super.onPreExecute();
	}
	
	@Override
	protected void onPostExecute(JSONObject result)
	{
		// TODO Auto-generated method stub
		if (myProgressDialog != null)
			myProgressDialog.dismiss();
		if (mySATCM != null&&result!=null)
		{
			mySATCM.processJsonObject(result);
		}
		super.onPostExecute(result);
	}
	
	@Override
	protected JSONObject doInBackground(Object... params)
	{
		String SOAP_NAMESPACE = params[0].toString();
		String SOAP_ACTION = params[1].toString();
		String SOAP_METHODNAME = params[2].toString();
		String SOAP_URL = params[3].toString();
		
		Map<String, Object> propertyMap = (Map<String, Object>) params[4];
		
		String response = null;
		JSONObject rsJson = null;
		try
		{			
			SoapObject request = new SoapObject(SOAP_NAMESPACE,SOAP_METHODNAME);
			
			Element[] header = new Element[1];
			header[0] = new Element().createElement(SOAP_NAMESPACE, "Identify");

			Element username = new Element().createElement(SOAP_NAMESPACE,
					"UserName");
			username.addChild(Node.TEXT, MMloveConstants.UserName);
			header[0].addChild(Node.ELEMENT, username);
			Element pass = new Element().createElement(SOAP_NAMESPACE,
					"PassWord");
			pass.addChild(Node.TEXT,MMloveConstants.Identify);
			header[0].addChild(Node.ELEMENT, pass);
			   
			
			if(propertyMap!=null && !propertyMap.isEmpty())
			{
				for(String s:propertyMap.keySet())
				{
					request.addProperty(s,propertyMap.get(s));
				}
			}
					
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.headerOut=header;
			envelope.bodyOut=request;
			envelope.dotNet = true;
			
			envelope.setOutputSoapObject(request);
			
//			HttpTransportSE httpTransport = new HttpTransportSE(SOAP_URL);
//			httpTransport.call(SOAP_ACTION,envelope);
			
			HttpTransportSE httpTransport = new HttpTransportSE(SOAP_URL,120000);
			
			httpTransport.call(SOAP_ACTION,envelope);
			
			SoapPrimitive result = (SoapPrimitive) envelope.getResponse(); 
			response  =result.toString();
			
			rsJson = new JSONObject(response);  
		}
		catch(IOException io)
		{
			try {
				if(SOAP_METHODNAME.contains("AskOrderUpdate")){
					Intent i = new Intent(myContext,
							CHK_network_anomalythree.class);
					i.putExtra("AskOrderID", AskOrderID);
					i.putExtra("DoctorId", myApp.DoctorId);
					i.putExtra("HospitalID", myApp.Hosid);
					i.putExtra("DoctorImageUrl", myApp.DoctorUrl);
					i.putExtra("DoctorName", myApp.DoctorName);
					i.putExtra("Hosname", myApp.HosName);
					i.putExtra("Title", myApp.Title);
					myContext.startActivity(i);
					
				}else{
				Toast.makeText(myContext, "一附院访问人数过多，数据加载失败，请稍后重试！",
						Toast.LENGTH_SHORT).show();
				io.printStackTrace();
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		catch (Exception ex)
		{
			Toast.makeText(myContext, R.string.msgUninternet,
					Toast.LENGTH_SHORT).show();
			ex.printStackTrace();
		}
		
		return rsJson;
	}
	
	public static String convertStreamToString(java.io.InputStream is)
			throws java.io.UnsupportedEncodingException
	{
		BufferedReader reader = new BufferedReader(new InputStreamReader(is,
				"utf-8"));
		
		StringBuilder sb = new StringBuilder();
		String line = null;
		
		try
		{
			while ((line = reader.readLine()) != null)
			{
				sb.append(line);// line+"/n"
			}
			is.close();
			
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				is.close();
			}
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return sb.toString();
	}
	
	public static String convertInputStreamReaderToString(InputStreamReader is)
			throws java.io.UnsupportedEncodingException
	{
		BufferedReader reader = new BufferedReader(is);
		
		StringBuilder sb = new StringBuilder();
		String line = null;
		
		try
		{
			while ((line = reader.readLine()) != null)
			{
				sb.append(line);// line+"/n"
			}
			is.close();
			
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				is.close();
			}
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return sb.toString();
	}
}
