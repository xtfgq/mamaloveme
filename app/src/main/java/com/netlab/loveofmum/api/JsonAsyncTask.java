package com.netlab.loveofmum.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;

import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

public class JsonAsyncTask extends AsyncTask<Object, Integer, JSONObject>
{
	private JsonAsyncTaskOnComplete	mySATCM					= null;
	private boolean					ShowProgressDialog		= false;
	private Context					myContext				= null;
	private ProgressDialog			myProgressDialog		= null;

	public JsonAsyncTask(Context context, boolean show,
			JsonAsyncTaskOnComplete method)
	{
		myContext = context;
		ShowProgressDialog = show;
		mySATCM = method;
	}
	
	@Override
	protected void onPreExecute()
	{
		// TODO Auto-generated method stub
		/*if (ShowProgressDialog && myContext != null)
		{
			myProgressDialog = new ProgressDialog(myContext, 0);
			myProgressDialog.setIcon(android.R.drawable.ic_dialog_info);
			//myProgressDialog.setMessage(ProgressDialogMessage);
			//myProgressDialog.setTitle(ProgressDialogTitle);
			myProgressDialog.setCancelable(false);
			myProgressDialog.show();
		}*/
		super.onPreExecute();
	}
	
	@Override
	protected void onPostExecute(JSONObject result)
	{
		// TODO Auto-generated method stub
		if (myProgressDialog != null)
			myProgressDialog.dismiss();
		if (mySATCM != null)
		{
			mySATCM.processJsonObject(result);
		}
		super.onPostExecute(result);
	}
	
	@Override
	protected JSONObject doInBackground(Object... params)
	{
		String NAMESPACE = params[0].toString();
		String ACTION = params[1].toString();
		String METHODNAME = params[2].toString();
		String URL = params[3].toString()+"/"+METHODNAME;
		String para = "";
		Map<String, Object> propertyMap = (Map<String, Object>) params[4];

		JSONObject response = null;
		
		try
		{
			HostnameVerifier hostnameVerifier = SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;
			KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
	        trustStore.load(null, null);
			SchemeRegistry registry = new SchemeRegistry();   
			SSLSocketFactory socketFactory = new MySSLSocketFactory(trustStore); 
			socketFactory.setHostnameVerifier((X509HostnameVerifier) hostnameVerifier);   
			registry.register(new Scheme("https", socketFactory, 443)); 
			registry.register(new Scheme("http",PlainSocketFactory.getSocketFactory (), 80)); 
			
			DefaultHttpClient client = new DefaultHttpClient();
	        SingleClientConnManager mgr = new SingleClientConnManager(client.getParams(), registry);       
	        DefaultHttpClient localDefaultHttpClient = new DefaultHttpClient(mgr, client.getParams()); 
			//------- Set verifier    
			HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier); 
			
			HttpPost localHttpPost = new HttpPost(URL);
			ArrayList<BasicNameValuePair> localArrayList = new ArrayList<BasicNameValuePair>();
			Iterator<String> localIterator = null;
			InputStreamReader localInputStreamReader = null;
			StringEntity reqEntity = new StringEntity("userid=10&pointx=8.8&pointy=8.9");
			reqEntity.setContentType("application/x-www-form-urlencoded"); 
			reqEntity.setContentEncoding("utf-8");
			if (propertyMap != null)
			{
				localIterator = propertyMap.keySet().iterator();
				while (localIterator.hasNext())
				{
					String str = (String) localIterator.next();
					localArrayList.add(new BasicNameValuePair(str,
							(String) propertyMap.get(str)));
				}
				try
				{
					localHttpPost.setEntity(new UrlEncodedFormEntity(localArrayList, "utf-8"));
					localInputStreamReader = new InputStreamReader(
							localDefaultHttpClient.execute(localHttpPost)
									.getEntity().getContent(), "utf-8");
					String responseStr = convertInputStreamReaderToString(localInputStreamReader);
					response = new JSONObject(responseStr);
				}
				catch (IllegalStateException localIllegalStateException)
				{
					localInputStreamReader = null;
					throw localIllegalStateException;
					
				}
				catch (IOException localIOException)
				{
					localInputStreamReader = null;
					throw localIOException;
				}
			}
			
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		return response;
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
