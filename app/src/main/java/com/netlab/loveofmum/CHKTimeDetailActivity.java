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
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.Html.ImageGetter;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.netlab.loveofmum.activity.LoginActivity;
import com.netlab.loveofmum.activity.MustKnownActivity;
import com.netlab.loveofmum.api.BaseActivity;
import com.netlab.loveofmum.api.MMloveConstants;
import com.netlab.loveofmum.api.JsonAsyncTaskOnComplete;
import com.netlab.loveofmum.api.JsonAsyncTask_Info;
import com.netlab.loveofmum.api.ListItemClickHelp;
import com.netlab.loveofmum.api.LocalAccessor;
import com.netlab.loveofmum.api.MyApplication;
import com.netlab.loveofmum.model.Order;
import com.netlab.loveofmum.model.User;
import com.netlab.loveofmum.model.Yuchan;
import com.netlab.loveofmum.myadapter.CHKTimeDetail_ItemListAdapter;
import com.netlab.loveofmum.myadapter.CHKTimeDetail_TopicAdapter;
import com.netlab.loveofmum.utils.DialogUtils;
import com.netlab.loveofmum.utils.IOUtils;
import com.netlab.loveofmum.utils.SystemStatusManager;
import com.netlab.loveofmum.widget.CustomerSpinner;
import com.netlab.loveofmum.widget.DialogEnsureEdit;
import com.netlab.loveofmum.widget.ListViewForScrollView;
import com.netlab.loveofmum.widget.XScrollView.PullToRefreshLayout;
import com.netlab.loveofmum.widget.XScrollView.RefreshLayout;
import com.umeng.analytics.MobclickAgent;

public class CHKTimeDetailActivity extends BaseActivity implements ListItemClickHelp,View.OnClickListener,PullToRefreshLayout.OnRefreshListener
{

	private String returnvalue001;
	private CustomerSpinner spinner;        //医院选择
	private CornerListView listviewChekItem;		//检查的项目
	private TextView talknum;			//讨论的条数

	private ListViewForScrollView listview;			//讨论列表
	CHKTimeDetail_TopicAdapter talkadapter;	//评论adapter
	private ImageView ivSend;    // 发送评论按钮
	private EditText value;     	//输入的评论内容

	private User user;
	private int HospitalID;
	// 根据传过来的参数ID
	private String CHKTypeID;
	private String CHKTypeName2;
	private String CHKItemValue;
	private Double CHKFEE = 0.0;
	private List<Map<String, Object>> arrayList = new ArrayList<Map<String, Object>>();
	private List<Map<String, Object>> arrayList2= new ArrayList<Map<String, Object>>();
	private List<Map<String, Object>> arrayList3= new ArrayList<Map<String, Object>>();
	public static ArrayList<String> list = new ArrayList<String>();
	public ArrayAdapter<String> adapter;
	private int selectedIndex = 0;

	// 定义Web Service相关的字符串
	private final String SOAP_NAMESPACE = MMloveConstants.URL001;
	private final String SOAP_ACTION = MMloveConstants.URL001
			+ MMloveConstants.CHKTypeItemsInquiry;
	private final String SOAP_METHODNAME = MMloveConstants.CHKTypeItemsInquiry;
	private final String SOAP_URL = MMloveConstants.URL002;

	private final String SOAP_ACTION2 = MMloveConstants.URL001
			+ MMloveConstants.NewsTopicInquiry;
	private final String SOAP_METHODNAME2 = MMloveConstants.NewsTopicInquiry;

	private final String SOAP_ACTION3 = MMloveConstants.URL001
			+ MMloveConstants.NewsTopicAdd;
	private final String SOAP_METHODNAME3 = MMloveConstants.NewsTopicAdd;
	
	private final String SOAP_ACTION4 = MMloveConstants.URL001
			+ MMloveConstants.HospitalInquiry;
	private final String SOAP_METHODNAME4 = MMloveConstants.HospitalInquiry;
	private int pageindex=1;
	PullToRefreshLayout refreshLayout;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setTranslucentStatus() ;
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		setContentView(R.layout.layout_chkdetail);
		iniView();
		MyApplication.getInstance().addActivity(this);
		if(!hasInternetConnected())
		{
			Toast.makeText(CHKTimeDetailActivity.this, R.string.msgUninternet,
					Toast.LENGTH_SHORT).show();
		}else{
			setListeners();
		}
	}

	@Override
	protected void onResume()
	{
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onResume(this);
		
//		sv.smoothScrollTo(0, 0);
		InitUser();
		searchHospital();
		searchCHKTimeItems();
		searchTopic();
		
	}


	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPause(this);
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
		tintManager.setStatusBarTintResource(R.color.home);//状态栏无背景
	}

	@Override
	protected void iniView()
	{
		// TODO Auto-generated method stub
		TextView txtHead = (TextView) findViewById(R.id.txtHead);
		txtHead.setText(this.getIntent().getExtras().getString("CHKType")
				.toString());
		findViewById(R.id.img_left).setOnClickListener(this);
		findViewById(R.id.mustkown).setOnClickListener(this);
		TextView label = (TextView)findViewById(R.id.label);
		label.setText(Html.fromHtml("本次产检可以直接预约医生门诊，"+"<img src='"+R.drawable.iconwdddkyysmall+"'/>"+"表示该超声检查项目可以网上直接预约。",imageGetter,null));
		//txtHos = (TextView)findViewById(R.id.txtHos);
		listview = (ListViewForScrollView) findViewById(R.id.talklist);
		listviewChekItem = (CornerListView) findViewById(R.id.checkitem);

		CHKTypeID = this.getIntent().getExtras().getString("ID").toString();
		CHKTypeName2 = this.getIntent().getExtras().getString("CHKType")
				.toString();
		String CHKTypeName = this.getIntent().getExtras().getString("CHKType")
				.toString().split("\\(")[0].toString();
//		txt002.setText("关于" + CHKTypeName2 + "大家说什么?");

		talknum = (TextView) findViewById(R.id.talknum);

		findViewById(R.id.submit).setOnClickListener(this);


		value=(EditText) findViewById(R.id.value);
	    ivSend= (ImageView) findViewById(R.id.send);
		spinner = (CustomerSpinner)findViewById(R.id.spinner);




		refreshLayout =(PullToRefreshLayout)findViewById(R.id.refreshlayout);
		refreshLayout.setOnRefreshListener(this);
	}
	public void goSend(View v) {
		if (hasInternetConnected())
		{
		if (user.UserID == 0)
		{
			Intent i = new Intent(CHKTimeDetailActivity.this,
					LoginActivity.class);
			startActivityForResult(i, 1);
		}
		else
		{
//			inputTitleDialog("0");
			String str=value.getText().toString().trim();
			if(str.equals(""))
			{
				Toast.makeText(CHKTimeDetailActivity.this, R.string.topic_msg,
						Toast.LENGTH_SHORT).show();
				return;
			}
			String inputName = Html.toHtml(value.getText());
			if(inputName.equals(""))
			{
				Toast.makeText(CHKTimeDetailActivity.this, R.string.topic_msg,
						Toast.LENGTH_SHORT).show();
			}
			else
			{
				if(inputName.contains("\"ltr\""))
				{
					
				}
				else
				{
					inputName = inputName.replace("ltr", "\"ltr\"");
				}
				value.setText("");
				TopicAdd(inputName, "0",value);
			}
		}
		}else{
			Toast.makeText(CHKTimeDetailActivity.this, R.string.msgUninternet,
					Toast.LENGTH_SHORT).show();
		}
	}
	 ImageGetter imageGetter = new ImageGetter() { 
		 
	        @Override 
	        public Drawable getDrawable(String source) { 
	            int id = Integer.parseInt(source); 
	            Drawable drawable = getResources().getDrawable(id); 
	            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),drawable.getIntrinsicHeight()); 
	            return drawable; 
	        } 
	    }; 
	
	private void InitUser()
	{
		user = LocalAccessor.getInstance(CHKTimeDetailActivity.this).getUser();
		HospitalID = user.HospitalID;
	
	}

	private void setListeners()
	{
		spinner.setOnItemSelectedListener(new OnItemSelectedListener()
		{
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id)
			{
				// TODO Auto-generated method stub
				Map<String, Object> mapDetail = new HashMap<String, Object>();
				mapDetail = arrayList3.get(position);
				int hosID = Integer.valueOf(mapDetail.get("HospitalID").toString());
				
				if(hosID == HospitalID)
				{
					
				}
				else
				{
					user.HospitalID = hosID;
					user.HospitalName = mapDetail.get("HospitalName").toString();
					HospitalID = hosID;
					try
					{
						LocalAccessor.getInstance(CHKTimeDetailActivity.this).updateUser(user);
						
						searchCHKTimeItems();
					}
					catch (Exception e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent)
			{
				// TODO Auto-generated method stub
				
			}
		});
		value.addTextChangedListener(new TextWatcher(){
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {}
			@Override
			public void afterTextChanged(Editable s) {
				if(value.getText().toString().length()>0){
					ivSend.setBackgroundResource(R.drawable.send_post);
					}else{
					ivSend.setBackgroundResource(R.drawable.send_post_un);
				}
			}
		} );
	}

	/*
	 * 查询gridview
	 */
	private void searchCHKTimeItems()
	{
		try
		{
			JsonAsyncTaskOnComplete doProcess = new JsonAsyncTaskOnComplete()
			{
				public void processJsonObject(Object result)
				{
					returnvalue001 = result.toString();
					CHKItemValue = result.toString();
					arrayList.clear();
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
							JSONArray array = mySO
									.getJSONArray("CHKTypeItemsInquiry");
							
							if(array.getJSONObject(0).has("MessageCode"))
							{
								if (array.getJSONObject(0).getString("MessageCode")
										.equals("0"))
								{
									
								}
							}
							else
							{
								for (int i = 0; i < array.length(); i++)
								{
	
									map = new HashMap<String, Object>();
									map.put("ID",
											array.getJSONObject(i).getString("ID"));
	
									map.put("ItemName", array.getJSONObject(i)
											.getString("ItemName"));
									map.put("ItemPrice", array.getJSONObject(i)
											.getString("ItemPrice"));
									map.put("ItemStatus", array.getJSONObject(i)
											.getString("Value"));
									map.put("ItemContent", array.getJSONObject(i)
											.getString("ItemContent"));
									arrayList.add(map);
	
									if ("可预约".equals(array.getJSONObject(i)
											.getString("Value")))
									{
										if(!array.getJSONObject(i).getString("ItemPrice").equals(""))
										{
											CHKFEE += Double.valueOf(array
													.getJSONObject(i).getString(
															"ItemPrice"));
										}
									}
								}
							}
							// SimpleAdapter adapter = new SimpleAdapter(
							// CHKTimeDetail.this, arrayList,
							// R.layout.item_chkdetail,
							// new Object[] { "ItemName" },
							// new int[] { R.id.txt_item_chkdetail });

							CHKTimeDetail_ItemListAdapter adapter = new CHKTimeDetail_ItemListAdapter(
									CHKTimeDetailActivity.this, arrayList);
							// listview.setAdapter(adapter);

							listviewChekItem.setAdapter(adapter);
							listviewChekItem.setEnabled(true);
							
//							btnYuyue.setVisibility(View.VISIBLE);
							// 添加listView点击事件

						}
						catch (Exception ex)
						{
							ex.printStackTrace();
						}
					}
				}
			};
//			btnYuyue.setVisibility(View.INVISIBLE);
			JsonAsyncTask_Info task = new JsonAsyncTask_Info(
					CHKTimeDetailActivity.this, true, doProcess);
			Map<String, Object> paramMap = new HashMap<String, Object>();
			String str = "<Request><CHKTypeID>%s</CHKTypeID><HospitalID>%s</HospitalID></Request>";

			str = String.format(str, new Object[]
			{ CHKTypeID, String.valueOf(HospitalID) });
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

	/*
	 * 查询listview
	 */
	private void searchTopic()

	{

		spinner.setEnabled(false);
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
						if(pageindex==1){
							arrayList2.clear();
						}
						// 下边是具体的处理
						try
						{
							JSONObject mySO = (JSONObject) result;
							JSONArray array = mySO
									.getJSONArray("NewsTopicInquiry");

							spinner.setEnabled(true);
							if(array.getJSONObject(0).has("MessageCode"))
							{
								if (array.getJSONObject(0).getString("MessageCode")
										.equals("0"))
								{
								}
							}
							else
							{
								for (int i = 0; i < array.length(); i++)
								{
	
									map = new HashMap<String, Object>();
									map.put("ID",
											array.getJSONObject(i).getString("ID"));
									map.put("LeftID", array.getJSONObject(i)
											.getString("LeftID"));
									map.put("TopicTitle", array.getJSONObject(i)
											.getString("TopicTitle"));
									map.put("ParentID", array.getJSONObject(i)
											.getString("ParentID"));
									map.put("GoodCount", array.getJSONObject(i)
											.getString("GoodCount"));
									map.put("UserID", array.getJSONObject(i)
											.getString("UserID"));
									map.put("NickName", array.getJSONObject(i)
											.getString("NickName"));
									map.put("TopicType", array.getJSONObject(i)
											.getString("TopicType"));
									map.put("PictureURL", array.getJSONObject(i)
											.getString("PictureURL"));
									map.put("CreatedDate", array.getJSONObject(i)
											.getString("CreatedDate"));
									String CreatedDate = array.getJSONObject(i)
											.getString("CreatedDate");
									CreatedDate = CreatedDate.split(" ")[0]
											.replaceAll("/", "-");
									String YuBirthTime = array.getJSONObject(i)
											.getString("YuBirthTime");
									YuBirthTime = YuBirthTime.split(" ")[0]
											.replaceAll("/", "-");
									
									
									DateFormat fmt = new SimpleDateFormat(
											"yyyy-MM-dd");
									Date date;
									
									Date date2;
									date = fmt.parse(YuBirthTime);
									
									date2 = fmt.parse(CreatedDate);
									Yuchan yu = new Yuchan();
									yu = IOUtils.WeekInfo2(date,date2);
									int weekitem=Integer.valueOf(yu.Week+"");
									if(weekitem<0){
										date = fmt.parse(fmt.format((new Date()).getTime()));
										yu = IOUtils.WeekInfo2(date,date2);
									}
									if( yu.Week>=40){
										map.put("YuBirthTime", "怀孕40周");
									}else{
									map.put("YuBirthTime", "怀孕" + yu.Week + "周");
									}
									
									map.put("PostsCount", array.getJSONObject(i)
											.getString("PostsCount"));
									arrayList2.add(map);
	
								}
							}
							// SimpleAdapter adapter = new SimpleAdapter(
							// CHKTimeDetail.this, arrayList2,
							// R.layout.item_chkdetail_list,
							// new Object[] {
							// "NickName","YuBirthTime","TopicTitle","GoodCount","PostsCount"
							// },
							// new int[] {
							// R.id.item_chkdetail_list_txt001,R.id.item_chkdetail_list_txt002,R.id.item_chkdetail_list_txt003,R.id.item_chkdetail_list_txt004,R.id.item_chkdetail_list_txt005
							// });
							if(talkadapter==null){
								talkadapter = new CHKTimeDetail_TopicAdapter(
										CHKTimeDetailActivity.this, arrayList2,
										CHKTimeDetailActivity.this);
								listview.setAdapter(talkadapter);
							}else {
								talkadapter.notifyDataSetChanged();
							}

							talknum.setText("评论:"+arrayList2.size());
							// listview.setEnabled(true);
							// 添加listView点击事件
							
						}
						catch (Exception ex)
						{
							ex.printStackTrace();
						}
						if(pageindex!=1){
							refreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
						}
					}
				}
			};

			if (IOUtils.hasInternetConnected(CHKTimeDetailActivity.this))
			{
				JsonAsyncTask_Info task = new JsonAsyncTask_Info(
						CHKTimeDetailActivity.this, true, doProcess);
				Map<String, Object> paramMap = new HashMap<String, Object>();
				String str = "<Request><TopicType>01</TopicType><LeftID>%s</LeftID><ParentID>%s</ParentID><PageIndex>%s</PageIndex><PageSize>%s</PageSize></Request>";
	
				str = String.format(str, new Object[]
				{ CHKTypeID, "0", pageindex+"", "16" });
				paramMap.put("str", str);
	
				// 必须是这5个参数，而且得按照顺序
				task.execute(SOAP_NAMESPACE, SOAP_ACTION2, SOAP_METHODNAME2,
						SOAP_URL, paramMap);
			}
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*
	 * 添加评论或回复
	 */
	private void TopicAdd(String topicTitle, String parentID,final EditText edit)
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
							JSONArray array = mySO.getJSONArray("NewsTopicAdd");

							if (array.getJSONObject(0).getString("MessageCode")
									.equals("0"))
							{
								 InputMethodManager imm = (InputMethodManager)getSystemService(CHKTimeDetailActivity.this.INPUT_METHOD_SERVICE);
						           imm.hideSoftInputFromWindow(edit.getWindowToken(),0);
								if(array.getJSONObject(0).has("PointAddCode")) {
									if(array.getJSONObject(0).getString("PointAddCode").equals("0")) {

										Intent i = new Intent(CHKTimeDetailActivity.this, DialogEnsure.class);
										i.putExtra("content", array.getJSONObject(0).getString("PointAddContent") + "+" + array.getJSONObject(0).getString("PointValue"));
										startActivity(i);
									}
								}
								searchTopic();
							}

						}
						catch (Exception ex)
						{
							ex.printStackTrace();
						}
					}
				}
			};

			if (hasInternetConnected())
			{
				JsonAsyncTask_Info task = new JsonAsyncTask_Info(
						CHKTimeDetailActivity.this, true, doProcess);
				Map<String, Object> paramMap = new HashMap<String, Object>();
				String str = "<Request><TopicType>01</TopicType><LeftID>%s</LeftID><ParentID>%s</ParentID><TopicTitle>%s</TopicTitle><UserID>%s</UserID></Request>";
				
				str = String.format(
						str,
						new Object[]
						{ CHKTypeID, parentID, topicTitle.replaceAll("\\&[a-zA-Z]{0,9};", "").replaceAll("<[^>]*>", "\n\t"),
								String.valueOf(user.UserID) });
				paramMap.put("str", str);

				// 必须是这5个参数，而且得按照顺序
				task.execute(SOAP_NAMESPACE, SOAP_ACTION3, SOAP_METHODNAME3,
						SOAP_URL, paramMap);
			}
			else
			{
				Toast.makeText(CHKTimeDetailActivity.this, R.string.msgUninternet,
						Toast.LENGTH_SHORT).show();
			}
			
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void onClick(View item, View widget, int position, int which)
	{
		switch (which)
		{
			case R.id.item_chkdetail_list_txt004:
				searchTopic();
				// notify();
				break;
			case R.id.img002_item_chkdetail_list:

				break;
			case R.id.item_chkdetail_list_txt006:
				// EditText disInput =
				// (EditText)findViewById(R.id.group_discuss);
				// disInput.requestFocus();

				if (user.UserID != 0)
				{
					inputTitleDialog(arrayList2.get(position).get("ID")
							.toString());
				}
				else
				{
					Intent i = new Intent(CHKTimeDetailActivity.this,
							LoginActivity.class);
					startActivityForResult(i, 1);
				}
				break;
			default:
				break;
		}
	}

	private void inputTitleDialog(final String parentID)
	{
//		if(!TextUtils.isEmpty()) {
//			Toast.makeText(context, "回复内容不能为空", 1).show();
//			return;
//		}
		DialogEnsureEdit dialogEdit=new DialogEnsureEdit(CHKTimeDetailActivity.this);
		final EditText edit=dialogEdit.getmEditText();
		
		dialogEdit.setDialogMsg("请填写评论内容","确定").
				setOnClickListenerEnsure(new OnClickListener() {
							
							@Override
							public void onClick(View v) {
								if(TextUtils.isEmpty(edit.toString().trim())){
									Toast.makeText(CHKTimeDetailActivity.this, R.string.topic_msg,
											Toast.LENGTH_SHORT).show();
									return;
								}else{
									String inputName = Html.toHtml(edit.getText());
									if(inputName.equals(""))
									{
										Toast.makeText(CHKTimeDetailActivity.this, R.string.topic_msg,
												Toast.LENGTH_SHORT).show();
									}
									else
									{
										if(inputName.contains("\"ltr\""))
										{
											
										}
										else
										{
											inputName = inputName.replace("ltr", "\"ltr\"");
										}
										TopicAdd(inputName, parentID,edit);
									}
								}
							}
						});
		DialogUtils.showSelfDialog(this, dialogEdit);

	}
	
	
	
	/*
	 * 查询医院信息
	 */
	private void searchHospital()
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
							JSONArray array = mySO
									.getJSONArray("HospitalInquiry");
							list.clear();
							int isDefault=0;
							for (int i = 0; i < array.length(); i++)
							{
								map = new HashMap<String, Object>();
								map.put("HospitalName", array.getJSONObject(i)
										.getString("HospitalName"));
								map.put("HospitalID", array.getJSONObject(i)
										.getString("HospitalID"));
								arrayList3.add(map);
								list.add(array.getJSONObject(i)
										.getString("HospitalName"));
								
								if(HospitalID==Integer.valueOf(array.getJSONObject(i)
										.getString("HospitalID")))
								{
									isDefault = i;
								}
							}
							
							
							spinner.setList(list);
							adapter = new ArrayAdapter<String>(CHKTimeDetailActivity.this, android.R.layout.simple_spinner_item, list);
					        spinner.setAdapter(adapter);
					        spinner.setPosition(isDefault);
					        spinner.setSelection(isDefault,true);
						}
						catch (Exception ex)
						{
							ex.printStackTrace();
						}
					}
				}
			};

			JsonAsyncTask_Info task = new JsonAsyncTask_Info(
					CHKTimeDetailActivity.this, true, doProcess);
			Map<String, Object> paramMap = new HashMap<String, Object>();
			String str = "<Request><HospitalID>0</HospitalID></Request>";

			paramMap.put("str", str);

			// 必须是这5个参数，而且得按照顺序
			task.execute(SOAP_NAMESPACE, SOAP_ACTION4, SOAP_METHODNAME4,
					SOAP_URL, paramMap);
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		switch (resultCode)
		{ // resultCode为回传的标记，我在B中回传的是RESULT_OK
			case 1:
				InitUser();
				break;
			default:
				break;
		}
	}
	
	@Override
	public void onDestroy()
	{
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	
	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	if(keyCode == KeyEvent.KEYCODE_BACK){
    		list.clear();
    	}
    	return super.onKeyDown(keyCode, event);
    }

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.img_left:
				finish();
				break;
			case R.id.mustkown:
				Intent i = new Intent(CHKTimeDetailActivity.this, MustKnownActivity.class);
				startActivity(i);
				break;
			case R.id.submit:
				if (hasInternetConnected())
				{
					LocalAccessor local = new LocalAccessor(CHKTimeDetailActivity.this,
							MMloveConstants.ORDERINFO);

					Order order = new Order();

					order.CHKTypeID = Integer.valueOf(CHKTypeID);
					order.CHKItemValue = CHKItemValue;
					order.CHKFee = String.valueOf(CHKFEE);
					order.CHKTypeName = CHKTypeName2;

					try
					{
						local.updateOrder(order);
					}
					catch (Exception e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					Intent intent = new Intent(CHKTimeDetailActivity.this, CHK_DoctorList.class);
					intent.putExtra("ID", CHKTypeID);
					intent.putExtra("CHKType", CHKTypeName2);
					startActivity(intent);
				}
				else
				{
					Toast.makeText(CHKTimeDetailActivity.this, R.string.msgUninternet,
							Toast.LENGTH_SHORT).show();
				}
				break;
		}
	}
	@Override
	public void onRefresh(final PullToRefreshLayout pullToRefreshLayout)
	{
		// 下拉刷新操作
		pageindex=1;
		refreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);

	}

	@Override
	public void onLoadMore(final PullToRefreshLayout pullToRefreshLayout)
	{
		// 加载操作
		pageindex++;
		searchTopic();

	}
}
