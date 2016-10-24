package com.netlab.loveofmum.widget;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.netlab.loveofmum.Index;
import com.netlab.loveofmum.R;
import com.netlab.loveofmum.api.JsonAsyncTaskOnComplete;
import com.netlab.loveofmum.api.JsonAsyncTask_Info;
import com.netlab.loveofmum.api.LocalAccessor;
import com.netlab.loveofmum.api.MMloveConstants;
import com.netlab.loveofmum.model.User;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class SignView extends Activity {
	private User user;
	private ImageView ivjien;
	private final String SOAP_NAMESPACE = MMloveConstants.URL001;
	private final String SOAP_URL = MMloveConstants.URL002;
	//请求积分口

	
	     @Override
	     protected void onCreate(Bundle savedInstanceState) {
	         super.onCreate(savedInstanceState);
	         setContentView(R.layout.dialog_sign_layout);
	         ivjien= (ImageView) findViewById(R.id.ivjifen);
	         ivjien.bringToFront();
	         user=LocalAccessor.getInstance(SignView.this).getUser();
	         
	     }
	   


}
