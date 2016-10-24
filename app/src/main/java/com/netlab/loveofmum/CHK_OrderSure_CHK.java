package com.netlab.loveofmum;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.netlab.loveofmum.api.BaseActivity;
import com.netlab.loveofmum.api.MMloveConstants;
import com.netlab.loveofmum.api.JsonAsyncTaskOnComplete;
import com.netlab.loveofmum.api.JsonAsyncTask_Info;
import com.netlab.loveofmum.api.LocalAccessor;
import com.netlab.loveofmum.api.MyApplication;
import com.netlab.loveofmum.huanxin.MainChatActivity;
import com.netlab.loveofmum.model.Order;
import com.netlab.loveofmum.model.User;
import com.netlab.loveofmum.model.Yuchan;
import com.netlab.loveofmum.utils.DialogUtils;
import com.netlab.loveofmum.utils.IOUtils;
import com.netlab.loveofmum.utils.SystemStatusManager;
import com.netlab.loveofmum.widget.DialogEnsureCancelView;
import com.umeng.analytics.MobclickAgent;
import com.netlab.loveofmum.wxapi.DialogSure;

public class CHK_OrderSure_CHK extends BaseActivity
{

	private TextView txtHead;
	private ImageView imgBack;

	private List<Map<String, Object>> arrayList = new ArrayList<Map<String, Object>>();
	private String returnvalue001;

//	private TextView txt001;
	private TextView txt002;
	private TextView txt003;

	private TextView txt004;

	private Order order;

	private String YunWeek;

	private User user;

	private String OrderID;

	private String items = "<Item><ItemID>%s</ItemID><ItemName>%s</ItemName><Price>%s</Price></Item>";
	private String item = "";
	private String keyueitem="";

	private String txtitem = "";

	private Boolean isSuccess = false;
	private Button btnSure;

	private Button btnCancle;
	private List<Map<String, Object>> yuyueList=new  ArrayList<Map<String, Object>>();

	private Boolean isHasCHK = false;

	private ProgressBar bar;
	private FrameLayout layout_progress;
	
	private ListView tvyuyue_content;
	private UISwitchButton isTwins;
	private Boolean isTwinsBtn;

	// 定义Web Service相关的字符串
	private final String SOAP_NAMESPACE = MMloveConstants.URL001;
	private final String SOAP_ACTION = MMloveConstants.URL001 + MMloveConstants.OrderInsert;
	private final String SOAP_METHODNAME = MMloveConstants.OrderInsert;
	private final String SOAP_URL = MMloveConstants.URL002;

	private final String SOAP_ACTION2 = MMloveConstants.URL001
			+ MMloveConstants.UserInfoChange;
	private final String SOAP_METHODNAME2 = MMloveConstants.UserInfoChange;
	private String cardNo="";

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setTranslucentStatus();
		setContentView(R.layout.layout_ordersure_chk);
		MyApplication.getInstance().addActivity(this);
		iniView();
		isTwins.setChecked(false);
		
		setListeners();
		InitData();

		if (hasInternetConnected())
		{

		}
		else
		{
			Toast.makeText(CHK_OrderSure_CHK.this, R.string.msgUninternet,
					Toast.LENGTH_SHORT).show();
		}
//		DialogEnsureCancelView dialogEnsureCancelView = new DialogEnsureCancelView(
//				getActivity()).setDialogMsg("提示",
//						"是否要删除"+userDelCons.getNick()+"的会话？", "确定").setOnClickListenerEnsure(
//				new OnClickListener() {
//
//					@Override
//					public void onClick(View v) {
//						
//						
//						EMChatManager.getInstance().deleteConversation(tobeDeleteCons.getUserName(), tobeDeleteCons.isGroup());
//
//						InviteMessgeDao inviteMessgeDao = new InviteMessgeDao(getActivity());
//						inviteMessgeDao.deleteMessage(tobeDeleteCons.getUserName());
//						adapter.remove(tobeDeleteCons);
//						adapter.notifyDataSetChanged();
//						((MainActivity) getActivity()).updateUnreadLabel();
//					}
//				});
//		DialogUtils.showSelfDialog(getActivity(),
//				dialogEnsureCancelView);
		// iniCHKInfo();
		// searchCHK_Doctors();
	}

	@Override
	protected void onResume()
	{
		if (hasInternetConnected())
		{
			user = LocalAccessor.getInstance(this).getUser();
			if(user.isTwins==0){

				isTwins.setChecked(false);
				isTwinsBtn=false;
			}else{
				isTwins.setChecked(true);
				isTwinsBtn=true;

			}
			iniYunWeek();
//			iniYunWeek();
//			setListeners();
//			InitData();
		}
		else
		{
			Toast.makeText(CHK_OrderSure_CHK.this, R.string.msgUninternet,
					Toast.LENGTH_SHORT).show();
		}

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
		tintManager.setStatusBarTintResource(R.drawable.bg_header);// 状态栏无背景
	}

	@Override
	protected void iniView()
	{
		// TODO Auto-generated method stub
		txtHead = (TextView) findViewById(R.id.txtHead);
		imgBack = (ImageView) findViewById(R.id.img_left);

		txtHead.setText("产检项目预约");

		LocalAccessor local = new LocalAccessor(CHK_OrderSure_CHK.this,
				MMloveConstants.ORDERINFO);
		order = local.getOrder();

//		txt001 = (TextView) findViewById(R.id.txtitems_order_sure);
		txt002 = (TextView) findViewById(R.id.txttime_order_sure);
		txt003 = (TextView) findViewById(R.id.txtitemfee_order_sure);

		txt004 = (TextView) findViewById(R.id.txt002_orderdetails);

		btnSure = (Button) findViewById(R.id.btn_sure);

//		btnCancle = (Button) findViewById(R.id.btn_sure2);

		layout_progress = (FrameLayout) findViewById(R.id.layout_progress);
		bar = (ProgressBar) findViewById(R.id.progressBar1);
		tvyuyue_content=(ListView) findViewById(R.id.tvyuyue_content);
		isTwins=(UISwitchButton)findViewById(R.id.yuyue_isTwins);
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
		isTwins.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked)	{
					isTwinsBtn=true;

				}else{
					isTwinsBtn=false;
				}

			}
		});

		btnSure.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{

				if(isTwinsBtn){
					UserInfoChange(1);
				}else {
					UserInfoChange(0);
				}

					if (user.RealName.equals("") || user.UserNO.equals(""))
					{
						DialogEnsureCancelView dialogEnsureCancelView = new DialogEnsureCancelView(
								CHK_OrderSure_CHK.this).setDialogMsg("友情提示",
										"预约产检请完善姓名和身份证号信息！", "确定").setOnClickListenerEnsure(
								new OnClickListener() {

									@Override
									public void onClick(View v) {
										
										Intent i = new Intent(
												CHK_OrderSure_CHK.this,
												User_InfoChange.class);
										i.putExtra("tag_info", "info");
										startActivityForResult(i, 1);
										
									}
								});
						DialogUtils.showSelfDialog(CHK_OrderSure_CHK.this, dialogEnsureCancelView);
//						new AlertDialog.Builder(CHK_OrderSure_CHK.this)
//								.setTitle("友情提示")
//								// 设置对话框标题
//
//								.setMessage("预约产检请完善姓名和身份证号信息！")
//								// 设置显示的内容
//
//								.setPositiveButton("确定",
//										new DialogInterface.OnClickListener()
//										{// 添加确定按钮
//
//											@Override
//											public void onClick(
//													DialogInterface dialog,
//													int which)
//											{// 确定按钮的响应事件
//
//												// TODO Auto-generated method
//												// stub
//
//												Intent i = new Intent(
//														CHK_OrderSure_CHK.this,
//														User_InfoChange.class);
//												startActivityForResult(i, 1);
//
//											}
//
//										}).show();// 在按键响应事件中显示此对话框
					}
					else
					{
						isHasCHK = true;
						keyueitem="";
						for(int i=0;i<yuyueList.size();i++){
							if("1".equals(yuyueList.get(i).get("isSelected").toString())){
								String id=yuyueList.get(i).get("ID").toString();
								String name=yuyueList.get(i).get("ItemName").toString();
								keyueitem=name+";"+keyueitem;
							}
							}
						if(keyueitem.length()>2){
						DialogEnsureCancelView dialogEnsureCancelView = new DialogEnsureCancelView(
								CHK_OrderSure_CHK.this).setDialogMsg("温馨提示",
										"本次产检您预约了"+keyueitem, "确定").setOnClickListenerEnsure(
								new OnClickListener() {

									@Override
									public void onClick(View v) {
										
										InsertOrder();
										
									}
								});
						DialogUtils.showSelfDialog(CHK_OrderSure_CHK.this, dialogEnsureCancelView);
						}else{
							DialogEnsureCancelView dialogEnsureCancelView = new DialogEnsureCancelView(
									CHK_OrderSure_CHK.this).setDialogMsg("温馨提示",
											"本次产检您只预约门诊挂号，无其他产检项目", "确定").setOnClickListenerEnsure(
									new OnClickListener() {

										@Override
										public void onClick(View v) {
											
											InsertOrder();
											
										}
									});
							DialogUtils.showSelfDialog(CHK_OrderSure_CHK.this, dialogEnsureCancelView);
							
						}
						
					}

				// TODO Auto-generated method stub
			}
		});

//		btnCancle.setOnClickListener(new View.OnClickListener()
//		{
//
//			@Override
//			public void onClick(View v)
//			{
//				// TODO Auto-generated method stub
//				isHasCHK = false;
//				item = "";
//				InsertOrder();
//			}
//		});
	}
	private void UserInfoChange(final int isTwinsInt)
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
							JSONArray array = mySO.getJSONArray("UserInfoChange");

							if (array.getJSONObject(0).getString("MessageCode")
									.equals("0"))
							{


								User user = LocalAccessor.getInstance(CHK_OrderSure_CHK.this).getUser();

								user.isTwins = isTwinsInt;



								LocalAccessor.getInstance(CHK_OrderSure_CHK.this).updateUser(user);


							}
							else
							{
								Toast.makeText(CHK_OrderSure_CHK.this, R.string.str002_userinfochange,
										Toast.LENGTH_SHORT).show();
							}

						}
						catch (Exception ex)
						{
							ex.printStackTrace();
						}
					}
				}
			};

			JsonAsyncTask_Info task = new JsonAsyncTask_Info(
					CHK_OrderSure_CHK.this, true, doProcess);
			Map<String, Object> paramMap = new HashMap<String, Object>();
			String str = "<Request><UserID>%s</UserID><IsTwins>%s</IsTwins></Request>";

				str = String.format(str, new Object[]
						{String.valueOf(user.UserID),isTwinsInt+""});




			paramMap.put("str", str);

			// 必须是这5个参数，而且得按照顺序
			task.execute(SOAP_NAMESPACE, SOAP_ACTION2, SOAP_METHODNAME2,
					SOAP_URL, paramMap);
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*
	 * 查询gridview
	 */
	private void InitData()
	{
		try
		{
			txt002.setText(order.DoctorTime);
			txt003.setText("以医院为准");
			txtitem = "";

			returnvalue001 = order.CHKItemValue.toString();
			if (returnvalue001 == null)
			{

			}
			else
			{
				Map<String, Object> map;
				Map<String, Object> mapKeyue;
				arrayList.clear();
				
				// 下边是具体的处理
				JSONObject mySO = new JSONObject(returnvalue001);
				JSONArray array = mySO.getJSONArray("CHKTypeItemsInquiry");
				if(yuyueList!=null&&yuyueList.size()>0){
					yuyueList.clear();
				}
				for (int i = 0; i < array.length(); i++)
				{
					map = new HashMap<String, Object>();
					map.put("ID", array.getJSONObject(i).getString("ID"));

					map.put("ItemName",
							array.getJSONObject(i).getString("ItemName"));
					map.put("ItemPrice",
							array.getJSONObject(i).getString("ItemPrice"));
					map.put("ItemStatus",
							array.getJSONObject(i).getString("Value"));
					map.put("ItemContent",
							array.getJSONObject(i).getString("ItemContent"));
					arrayList.add(map);

					if (array.getJSONObject(i).getString("Value").equals("可预约"))
					{
						
						mapKeyue=new HashMap<String, Object>();
						mapKeyue.put("ID",array.getJSONObject(i)
								.getString("ID"));
						mapKeyue.put("ItemName",array.getJSONObject(i)
								.getString("ItemName"));
						mapKeyue.put("ItemPrice",array.getJSONObject(i)
								.getString("ItemPrice"));
						mapKeyue.put("ItemContent",array.getJSONObject(i)
								.getString("ItemContent"));
						mapKeyue.put("isSelected",1);
						yuyueList.add(mapKeyue);
//						String itemvalue = String
//								.format(items,
//										new Object[]
//										{
//												array.getJSONObject(i)
//														.getString("ID"),
//												array.getJSONObject(i)
//														.getString("ItemName"),
//												array.getJSONObject(i)
//														.getString("ItemPrice") });
//						item += itemvalue;
						txtitem += array.getJSONObject(i).getString("ItemName")
								+ ";";
					}
				}
				
				if(yuyueList.size()>0){
					YuyueAdapter adapter = new YuyueAdapter(CHK_OrderSure_CHK.this, yuyueList);  
					tvyuyue_content.setAdapter(adapter);  
					
				}

//				txt001.setText(txtitem);
			


				txt004.setText("本次产检包含\"" + txtitem + "\"的预约，您是否需要预约这些产检项？");
			}
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void iniYunWeek()
	{
		DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
		DateFormat fmt2 = new SimpleDateFormat("MM月dd日");
		Date date;
		try
		{
			date = fmt.parse(user.YuBirthDate);
			Yuchan yu = new Yuchan();
			yu = IOUtils.WeekInfo(date);
			YunWeek = String.valueOf(yu.Week + 1);
		}
		catch (ParseException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private Boolean InsertOrder()
	{
		try
		{
			JsonAsyncTaskOnComplete doProcess = new JsonAsyncTaskOnComplete()
			{
				public void processJsonObject(Object result)
				{
					returnvalue001 = result.toString();
					layout_progress.setVisibility(View.GONE);
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
							JSONArray array = mySO.getJSONArray("OrderInsert");


							if (array.getJSONObject(0).getString("MessageCode")
									.equals("0"))
							{
								LocalAccessor local = new LocalAccessor(
										CHK_OrderSure_CHK.this,
										MMloveConstants.ORDERINFO);
								if (isHasCHK)
								{

								}
								else
								{
									order.CHKItemValue = "";
								}
								local.updateOrder(order);
								user.HospitalID= Integer.valueOf(order.HospitalID);
								LocalAccessor.getInstance(CHK_OrderSure_CHK.this).updateUser(user);
								if(array.getJSONObject(0).has("PointAddCode")) {
									if(array.getJSONObject(0).getString("PointAddCode").equals("0")) {

										Intent i = new Intent(CHK_OrderSure_CHK.this, DialogSure.class);
										i.putExtra("content", array.getJSONObject(0).getString("PointAddContent") + "+" + array.getJSONObject(0).getString("PointValue"));
										i.putExtra("Order", keyueitem);
										startActivity(i);
									}
									else{
										Intent i=new Intent(CHK_OrderSure_CHK.this,CHK_OrderDetail.class);
										i.putExtra("Order", keyueitem);
										startActivity(i);
									}
								}else{
									Intent i=new Intent(CHK_OrderSure_CHK.this,CHK_OrderDetail.class);
									i.putExtra("Order", keyueitem);
									startActivity(i);
								}

							}
							else
							{
								Toast.makeText(
										CHK_OrderSure_CHK.this,
										array.getJSONObject(0).getString(
												"MessageContent"),
										Toast.LENGTH_SHORT).show();
								return;
							}

						}
						catch (Exception ex)
						{
							ex.printStackTrace();
						}
					}
				}
			};
			cardNo="111600075978";

			JsonAsyncTask_Info task = new JsonAsyncTask_Info(
					CHK_OrderSure_CHK.this, true, doProcess);
			Map<String, Object> paramMap = new HashMap<String, Object>();
			String str = "<Request><UserID>%s</UserID><OrderStatus>%s</OrderStatus><PayType>%s</PayType><PayStatus>%s</PayStatus><HospitalID>%s</HospitalID><HospitalName>%s</HospitalName><DeptRowID>%s</DeptRowID><DeptDesc>%s</DeptDesc><DoctorID>%s</DoctorID><DoctorName>%s</DoctorName><Gotime>%s</Gotime><BeginTime>%s</BeginTime><EndTime>%s</EndTime><RegisterMoney>%s</RegisterMoney><CHKID>%s</CHKID><CHKType>%s</CHKType><YunWeek>%s</YunWeek><UserMobile>%s</UserMobile><RealName>%s</RealName><UserNO>%s</UserNO><DoctorNO>%s</DoctorNO><DeptCode>%s</DeptCode><DeptName>%s</DeptName><SchemaID>%s</SchemaID><Items>%s</Items><PhoneType>%s</PhoneType><MedicalCard>%s</MedicalCard></Request>";

			Double gf = 0.0;
			Double chkf = 0.0;
			if (!order.GuaFee.equals(""))
			{
				gf = Double.valueOf(order.GuaFee);
			}
//			if (!order.CHKFee.equals(""))
//			{
//				chkf = Double.valueOf(order.CHKFee);
//			}
			
			String orderStatus = "01";
		
			if(order.HospitalID.equals("46"))
			{
				orderStatus = "02";
			}
			if(order.HospitalID.equals("47"))
			{
				orderStatus = "02";
			}
			if(order.HospitalID.equals("48"))
			{
				orderStatus = "02";
			}
			item="";
			keyueitem="";
			Double price=0d;
			for(int i=0;i<yuyueList.size();i++){
			if("1".equals(yuyueList.get(i).get("isSelected").toString())){
				String id=yuyueList.get(i).get("ID").toString();
				String name=yuyueList.get(i).get("ItemName").toString();
				keyueitem=name+";"+keyueitem;

				String priceStr=yuyueList.get(i).get("ItemPrice").toString();
				price+=Double.valueOf(priceStr);
					String itemvalue = String
							.format(items,
									new Object[]
									{id,name,priceStr
									});
					item += itemvalue;
			}
			}
			chkf = price;
			String fee = String.valueOf(gf + chkf);
			str = String.format(str, new Object[]
			{ String.valueOf(user.UserID), orderStatus, "01", "01", order.HospitalID,
					order.HospitalName, "", "", order.DoctorID,
					order.DoctorName, order.DoctorTime, order.TimeStart,
					order.TimeEnd, fee, String.valueOf(order.CHKTypeID),
					order.CHKTypeName, YunWeek, user.UserMobile, user.RealName,
					user.UserNO, order.DoctorNO, order.DeptCode,
					order.DeptName, order.SchemaID, item,"0",cardNo});

			paramMap.put("str", str);
			layout_progress.setVisibility(View.VISIBLE);
			// 必须是这5个参数，而且得按照顺序
			task.execute(SOAP_NAMESPACE, SOAP_ACTION, SOAP_METHODNAME,
					SOAP_URL, paramMap);
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return isSuccess;
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		switch (resultCode)
		{ // resultCode为回传的标记，我在B中回传的是RESULT_OK
			case 0:

				break;
			case 1:
				// iniView();
				// InitData();
				break;
			case 2:

				break;
			case 3:

				break;
			case 4:

				break;
			case 5:

				break;
			case 6:

				break;
			case -1:
				break;
			default:
				break;
		}
	}
	public class YuyueAdapter extends BaseAdapter {  
		   
	    private Context context;  
	    private List<Map<String, Object>> list;  
	   
	    // 用来控制CheckBox的选中状况  
	    private HashMap<Integer, Boolean> isSelected;  
//	   
	    class ViewHolder {  
	   

	        TextView tvCountnet;
	        UISwitchButton cb; 
	        ImageView ivright;
	    }  
	   
	    public YuyueAdapter(Context context, List<Map<String, Object>> list) {  
	        // TODO Auto-generated constructor stub  
	        this.list = list;  
	        this.context = context;  
	    }  
	   
	  
	   
	    @Override 
	    public int getCount() {  
	        // TODO Auto-generated method stub  
	        return list.size();  
	    }  
	   
	    @Override 
	    public Object getItem(int position) {  
	        // TODO Auto-generated method stub  
	        return list.get(position);  
	    }  
	   
	    @Override 
	    public long getItemId(int position) {  
	        // TODO Auto-generated method stub  
	        return position;  
	    }  
	   
	    @Override 
	    public View getView(final int position, View convertView, ViewGroup parent) {  
	        // TODO Auto-generated method stub  
	        // 页面  
	        ViewHolder holder;  
	     
	        LayoutInflater inflater = LayoutInflater.from(context);  
	        if (convertView == null) {  
	            convertView = inflater.inflate(  
	                    R.layout.yuyue_item, null);  
	            holder = new ViewHolder();  

	            holder.tvCountnet=(TextView) convertView  
	                    .findViewById(R.id.tv_content);
	            holder.cb = (UISwitchButton) convertView.findViewById(R.id.yuyue_item_switch_slideswitch);  
//	        holder.ivright=(ImageView) convertView.findViewById(R.id.ivright);
	            convertView.setTag(holder);  
	        } else {  
	            
	            holder = (ViewHolder) convertView.getTag();  
	        }  

	       holder.tvCountnet.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(context,
						CHKItem_Base.class);
				i.putExtra("ItemName",list.get(position).get("ItemName").toString());
				i.putExtra("ItemContent",list.get(position).get("ItemContent").toString());
				startActivity(i); 
			}
		});
	        // 监听checkBox并根据原来的状态来设置新的状态  
	        holder.cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked)	{
                    	list.get(position).put("isSelected", 1);
                       
                    }else{
                    	list.get(position).put("isSelected", 0);
                    }	
                    notifyDataSetChanged();
				}
			});
	       
	       holder.tvCountnet.setText(list.get(position).get("ItemName").toString()); 
	        // 根据isSelected来设置checkbox的选中状况  
	      if("1".equals(list.get(position).get("isSelected").toString())){
	    	  holder.cb.setChecked(true);
	      }else{
	    	  holder.cb.setChecked(false); 
	      }
	        return convertView;  
	    }  

	    
	}

		

	
}
