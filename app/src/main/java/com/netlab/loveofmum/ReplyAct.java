package com.netlab.loveofmum;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.netlab.loveofmum.api.BaseActivity;
import com.netlab.loveofmum.api.JsonAsyncTaskOnComplete;
import com.netlab.loveofmum.api.JsonAsyncTask_Info;
import com.netlab.loveofmum.api.MMloveConstants;
import com.netlab.loveofmum.api.MyApplication;
import com.netlab.loveofmum.model.Yuchan;
import com.netlab.loveofmum.myadapter.CHK_PostList_Adapter;
import com.netlab.loveofmum.myadapter.ReplyItemAdapter;
import com.netlab.loveofmum.utils.IOUtils;
import com.netlab.loveofmum.utils.SystemStatusManager;

public class ReplyAct extends BaseActivity {
	private TextView txtHead;
	private ImageView imgBack;
	private List<Map<String, Object>> arrayList = new ArrayList<Map<String, Object>>();
	// 定义Web Service相关的字符串
	private final String SOAP_NAMESPACE = MMloveConstants.URL001;
	private final String SOAP_ACTION = MMloveConstants.URL001
			+ MMloveConstants.ArticleTopicInquiry;
	private final String SOAP_METHODNAME = MMloveConstants.ArticleTopicInquiry;
	private final String SOAP_URL = MMloveConstants.URL002;
	String ArticleID;
	private Intent mIntent;
	private ListView listview;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setTranslucentStatus();
		setContentView(R.layout.layout_postlist);
		MyApplication.getInstance().addActivity(this);
		iniView();
		setListeners();
		mIntent = this.getIntent();
		if (mIntent.getStringExtra("ArticleID") == null) {
			return;
		} else {
			ArticleID = mIntent.getStringExtra("ArticleID");
		}
		// iniCHKInfo();
		if (hasInternetConnected()) {
			// imageLoader = ImageLoader.getInstance();
			// imageLoader.init(ImageLoaderConfiguration.createDefault(PostList.this));
			searchPostList();
		} else {
			Toast.makeText(ReplyAct.this, R.string.msgUninternet,
					Toast.LENGTH_SHORT).show();
		}

	}

	/**
	 * 设置状态栏背景状态
	 */
	private void setTranslucentStatus() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
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

	private void setListeners() {
		// TODO Auto-generated method stub
		imgBack.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}

	@Override
	protected void iniView() {
		// TODO Auto-generated method stub
		txtHead = (TextView) findViewById(R.id.txtHead);
		imgBack = (ImageView) findViewById(R.id.img_left);

		txtHead.setText("回复列表");
		listview = (ListView) findViewById(R.id.listview_postlist);
	}

	private void searchPostList() {
		try {
			JsonAsyncTaskOnComplete doProcess = new JsonAsyncTaskOnComplete() {
				public void processJsonObject(Object result) {
					String returnvalue001 = result.toString();
					if (returnvalue001 == null) {

					} else {
						Map<String, Object> map;
						// 下边是具体的处理
						try {
							JSONObject mySO = (JSONObject) result;
							JSONArray array = mySO
									.getJSONArray("ArticleTopicInquiry");
							for (int i = 0; i < array.length(); i++) {

								map = new HashMap<String, Object>();
								
								String time = array.getJSONObject(i)
										.get("CreatedDate").toString()
										.replace("/", "-");
								time = IOUtils.stringToDate2(time,
										"yyyy-MM-dd HH:mm:ss");
								String times = IOUtils.getMistimingTimes(time)
										.toString();
								
								String str = array.getJSONObject(i).getString(
										"AuthorNickPic");
								map.put("AuthorType",array.getJSONObject(i)
										.getString("AuthorType").toString());
								map.put("CreatedDate", times);
								map.put("ParentID",array.getJSONObject(i).getString(
										"ParentID"));
								map.put("Content", array.getJSONObject(i)
										.getString("Content"));
								if("".equals(str)){
									map.put("Nick", "宝妈");
									map.put("PicUrl", "");
								}else{
									String[] strs = str.split("\\,");
									map.put("Nick", strs[0]);
									map.put("PicUrl", strs[1]);

								}

								String str1 = array.getJSONObject(i).getString(
										"ReplyNickPic");
								
								if("".equals(str1)){
									map.put("ReplyNick", "宝妈");
									
								}else{
									String[] strs1 = str1.split("\\,");
									map.put("ReplyNick", strs1[0]);
									
								}



								arrayList.add(map);
							}
							ReplyItemAdapter adapter = new ReplyItemAdapter(
									ReplyAct.this, arrayList);
							listview.setAdapter(adapter);

						} catch (Exception ex) {
							ex.printStackTrace();
						}
					}
				}
			};

			JsonAsyncTask_Info task = new JsonAsyncTask_Info(ReplyAct.this,
					true, doProcess);
			Map<String, Object> paramMap = new HashMap<String, Object>();
			String str = "<Request><ArticleID>%s</ArticleID></Request>";

			str = String.format(str, new Object[] { ArticleID });
			paramMap.put("str", str);

			// 必须是这5个参数，而且得按照顺序
			task.execute(SOAP_NAMESPACE, SOAP_ACTION, SOAP_METHODNAME,
					SOAP_URL, paramMap);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
