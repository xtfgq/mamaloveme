package com.netlab.loveofmum;

import android.app.Activity;
import android.os.Bundle;
import com.igexin.sdk.PushManager;

public class GeTui extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		PushManager.getInstance().initialize(this.getApplicationContext());
	}
}
