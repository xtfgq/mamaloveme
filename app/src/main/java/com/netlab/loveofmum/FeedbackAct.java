package com.netlab.loveofmum;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.netlab.loveofmum.api.BaseActivity;
import com.netlab.loveofmum.api.MMloveConstants;
import com.netlab.loveofmum.api.JsonAsyncTaskOnComplete;
import com.netlab.loveofmum.api.JsonAsyncTask_Info;
import com.netlab.loveofmum.api.LocalAccessor;
import com.netlab.loveofmum.model.User;
import com.netlab.loveofmum.utils.IOUtils;
import com.netlab.loveofmum.utils.SystemStatusManager;
import com.umeng.analytics.MobclickAgent;

public class FeedbackAct  extends BaseActivity implements OnClickListener{
	private EditText edFeedback;
	private Button btnOk;
	private ImageView imgBack;
	private TextView txtHead;
	// 定义Web Service相关的字符串
	private final String SOAP_NAMESPACE = MMloveConstants.URL001;
	private final String SOAP_ACTIONBACK = MMloveConstants.URL001 + MMloveConstants.FeedBackInsert;
	private final String SOAP_METHODNAMEBACK = MMloveConstants.FeedBackInsert;
	private final String SOAP_URL = MMloveConstants.URL002;
	
	private User user;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		  requestWindowFeature(Window.FEATURE_NO_TITLE);
			setTranslucentStatus();
		
		setContentView(R.layout.activity_feedback);
	
		iniView();
		txtHead.setText("反馈");
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

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		   MobclickAgent.onResume(this);
		user = LocalAccessor.getInstance(FeedbackAct.this).getUser();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.ed_feedback:
			
			break;
	case R.id.btnOK:
		if("".equals(edFeedback.getText().toString().trim())){
			Toast.makeText(FeedbackAct.this, "请填写反馈内容", 1).show();
			return;
		}
		if(edFeedback.getText().toString().length()>200){
			Toast.makeText(FeedbackAct.this, "输入内容过多！", 1).show();
			return;
		}
			postFeedBack();
			break;

		default:
			break;
		}
	}

	private void postFeedBack() {
		// TODO Auto-generated method stub
		try
		{
			JsonAsyncTaskOnComplete doProcess = new JsonAsyncTaskOnComplete()
			{
				//{"FeedBackInsert":[{"MessageContent":"成功！","MessageCode":"0"}]}
				public void processJsonObject(Object result)
				{
					String resultObj = result.toString();
					if (resultObj == null)
					{

					}
					else
					{
						// 下边是具体的处理
						try
						{
							JSONObject mySO = (JSONObject) result;
							org.json.JSONArray array = mySO
									.getJSONArray("FeedBackInsert");
//
							if (array.getJSONObject(0).has("MessageCode"))
							{
								Toast.makeText(FeedbackAct.this,array.getJSONObject(0).getString("MessageContent").toString(),
										Toast.LENGTH_SHORT).show();
								finish();
							}


						}
						catch (Exception ex)
						{
							ex.printStackTrace();
							finish();
						}
					}
				}
			};
			String inputName = Html.toHtml(edFeedback.getText());
			JsonAsyncTask_Info task = new JsonAsyncTask_Info(FeedbackAct.this, true,
					doProcess);
			Map<String, Object> paramMap = new HashMap<String, Object>();
			String str = "<Request><UserID>%s</UserID><Value>%s</Value></Request>";
			str = String.format(str, new Object[]
			{ user.UserID+"", inputName.replaceAll("\\&[a-zA-Z]{0,9};", "").replaceAll("<[^>]*>", "\n\t")});
			paramMap.put("str", str);

			// 必须是这5个参数，而且得按照顺序
			task.execute(SOAP_NAMESPACE, SOAP_ACTIONBACK, SOAP_METHODNAMEBACK,
					SOAP_URL, paramMap);
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override
	protected void iniView() {
		// TODO Auto-generated method stub
		edFeedback=(EditText) findViewById(R.id.ed_feedback);
		btnOk=(Button) findViewById(R.id.btnOK);
		btnOk.setOnClickListener(this);
		txtHead = (TextView) findViewById(R.id.txtHead);
		imgBack = (ImageView) findViewById(R.id.img_left);
	}

}
