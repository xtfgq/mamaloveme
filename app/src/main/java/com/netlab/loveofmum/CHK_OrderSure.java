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

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.netlab.loveofmum.activity.LoginActivity;
import com.netlab.loveofmum.activity.User_InfoEditActivity;
import com.netlab.loveofmum.api.BaseActivity;
import com.netlab.loveofmum.api.MMloveConstants;
import com.netlab.loveofmum.api.JsonAsyncTaskOnComplete;
import com.netlab.loveofmum.api.JsonAsyncTask_Info;
import com.netlab.loveofmum.api.LocalAccessor;
import com.netlab.loveofmum.api.MyApplication;
import com.netlab.loveofmum.model.Order;
import com.netlab.loveofmum.model.User;
import com.netlab.loveofmum.model.Yuchan;
import com.netlab.loveofmum.utils.DialogUtils;
import com.netlab.loveofmum.utils.IOUtils;
import com.netlab.loveofmum.utils.SystemStatusManager;
import com.netlab.loveofmum.widget.DialogEnsureCancelView;
import com.netlab.loveofmum.wxapi.DialogSure;

public class CHK_OrderSure extends BaseActivity
{

	private TextView txtHead;
	private ImageView imgBack;

	private List<Map<String, Object>> arrayList = new ArrayList<Map<String, Object>>();
	private String returnvalue001;
	private GridView gridview;

	private TextView txt001;
	private TextView txt002;
	private TextView txt003;
	private TextView txt004;
	private TextView txt005;

	private TableRow tab001;
	private TableRow tab002;
	private TableRow tab003;
	private Order order;

	private String YunWeek;

	private User user;

	private Boolean isHasItems=false;

	private String OrderID;

	private String items = "<Item><ItemID>%s</ItemID><ItemName>%s</ItemName><Price>%s</Price></Item>";
	private String item = "";

	private Boolean isSuccess = false;
	private Button btnSure;

	private ProgressBar bar;
	private FrameLayout layout_progress;
	private String cardNo="";

	// 定义Web Service相关的字符串
	private final String SOAP_NAMESPACE = MMloveConstants.URL001;
	private final String SOAP_ACTION = MMloveConstants.URL001 + MMloveConstants.OrderInsert;
	private final String SOAP_METHODNAME = MMloveConstants.OrderInsert;
	private final String SOAP_URL = MMloveConstants.URL002;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setTranslucentStatus();
		setContentView(R.layout.layout_pia);
		MyApplication.getInstance().addActivity(this);
		iniView();

		if (hasInternetConnected())
		{

		}
		else
		{
			Toast.makeText(CHK_OrderSure.this, R.string.msgUninternet,
					Toast.LENGTH_SHORT).show();
		}

		// iniCHKInfo();
		// searchCHK_Doctors();
	}

	@Override
	protected void onResume()
	{
		if (hasInternetConnected())
		{
			user = LocalAccessor.getInstance(this).getUser();
			iniYunWeek();
			setListeners();
			InitData();
		}
		else
		{
			Toast.makeText(CHK_OrderSure.this, R.string.msgUninternet,
					Toast.LENGTH_SHORT).show();
		}

		// TODO Auto-generated method stub
		super.onResume();
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
		tintManager.setStatusBarTintResource(R.color.home);// 状态栏无背景
	}

	@Override
	protected void iniView()
	{
		// TODO Auto-generated method stub
		txtHead = (TextView) findViewById(R.id.txtHead);
		imgBack = (ImageView) findViewById(R.id.img_left);

		txtHead.setText("预约挂号");

		// gridview = (GridView) findViewById(R.id.grid_order_sure);

		// tab001 = (TableRow)findViewById(R.id.tab_pia_view001);
		tab002 = (TableRow) findViewById(R.id.tab_pia_view002);
		tab003 = (TableRow) findViewById(R.id.tab_pia_view003);
		LocalAccessor local = new LocalAccessor(CHK_OrderSure.this,
				MMloveConstants.ORDERINFO);
		order = local.getOrder();

		txt001 = (TextView) findViewById(R.id.txthosname_order_sure);
		txt002 = (TextView) findViewById(R.id.txtdocname_order_sure);
		txt003 = (TextView) findViewById(R.id.txttime_order_sure);
		// txt004 = (TextView) findViewById(R.id.txtitemfee_order_sure);
		txt005 = (TextView) findViewById(R.id.txtguafee_order_sure);

		btnSure = (Button) findViewById(R.id.btn_sure);

		layout_progress = (FrameLayout) findViewById(R.id.layout_progress);
		bar = (ProgressBar) findViewById(R.id.progressBar1);

		// items =
		// "<Item><ItemID>%s</ItemID><Price>%s</Price><ItemName>%s</ItemName></Item>";
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

		// tab001.setOnClickListener(new View.OnClickListener()
		// {
		//
		// @Override
		// public void onClick(View v)
		// {
		// // TODO Auto-generated method stub
		//
		// }
		// });
		tab002.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				Intent i = new Intent(CHK_OrderSure.this, CHK_DoctorList.class);
				i.putExtra("ID", String.valueOf(order.CHKTypeID));
				i.putExtra("CHKType", order.CHKTypeName);
				i.putExtra("HostiptalID", order.HospitalID);
				startActivity(i);
			}
		});
		tab003.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				Intent i = new Intent(CHK_OrderSure.this, CHK_TimeSelect.class);
				i.putExtra("DoctorID", order.DoctorID);
				i.putExtra("DoctorName", order.DoctorName);
				i.putExtra("HospitalName", order.HospitalName);
				i.putExtra("DoctorTitle", order.DoctorTitle);
				i.putExtra("Price", order.Price);
				i.putExtra("ImageURL", order.ImageURL);
				i.putExtra("HospitalID", order.HospitalID);
				i.putExtra("DepartNO", order.DepartNO);
				i.putExtra("DoctorNO", order.DoctorNO);
				startActivity(i);
			}
		});

		btnSure.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				if (user.UserID == 0)
				{
					Intent i = new Intent(CHK_OrderSure.this, LoginActivity.class);
					startActivityForResult(i, 1);
				}
				else
				{
					if (user.RealName.equals("") || user.UserNO.equals(""))
					{
						DialogEnsureCancelView dialogEnsureCancelView = new DialogEnsureCancelView(
								CHK_OrderSure.this).setDialogMsg("友情提示",
										"预约产检请完善姓名和身份证号信息", "确定").setOnClickListenerEnsure(
								new OnClickListener() {

									@Override
									public void onClick(View v) {
										Intent i = new Intent(
												CHK_OrderSure.this,
												User_InfoEditActivity.class);
										i.putExtra("tag_info", "info");
										startActivityForResult(i, 1);
										
										
									}
								});
						DialogUtils.showSelfDialog(CHK_OrderSure.this, dialogEnsureCancelView);
//						new AlertDialog.Builder(CHK_OrderSure.this)
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
//														CHK_OrderSure.this,
//														User_InfoChange.class);
//												startActivityForResult(i, 1);
//
//											}
//
//										}).show();// 在按键响应事件中显示此对话框
					}
					else
					{
						if (!isHasItems)
						{
							InsertOrder();
						}
						else
						{
							LocalAccessor local = new LocalAccessor(
									CHK_OrderSure.this, MMloveConstants.ORDERINFO);

							try
							{
								local.updateOrder(order);
							}
							catch (Exception e)
							{
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							Intent i = new Intent(CHK_OrderSure.this,
									CHK_OrderSure_CHK.class);
							startActivity(i);

						}
					}
				}
				// TODO Auto-generated method stub
			}
		});
	}

	/*
	 * 查询gridview
	 */
	private void InitData()
	{
		try
		{
			txt001.setText(order.HospitalName);
			txt002.setText(order.DoctorName);
			if(Integer.valueOf(order.HospitalID)==1){
				txt003.setText(order.DoctorTime + " " + order.TimeStart
						);
			}else {
				txt003.setText(order.DoctorTime + " " + order.TimeStart + "-"
						+ order.TimeEnd);
			}
			// txt004.setText(order.CHKFee);
			// txt005.setText(order.GuaFee);
			// txt004.setText("以医院为准");
			txt005.setText("以医院为准");
			// item = "";
			returnvalue001 = order.CHKItemValue.toString();

			if (returnvalue001 == null)
			{
				isHasItems = false;
			}
			else
			{
				Map<String, Object> map;
				arrayList.clear();
				// 下边是具体的处理
				JSONObject mySO = new JSONObject(returnvalue001);
				JSONArray array = mySO.getJSONArray("CHKTypeItemsInquiry");
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
						isHasItems = true;
					}
				}

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
//					Toast.makeText(CHK_OrderSure.this, result.toString(),1).show();
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
//							Toast.makeText(CHK_OrderSure.this,array.toString(),1).show();

							if (array.getJSONObject(0).getString("MessageCode")
									.equals("0"))
							{
								LocalAccessor local = new LocalAccessor(
										CHK_OrderSure.this, MMloveConstants.ORDERINFO);
								order.CHKItemValue = "";
								local.updateOrder(order);
								user.HospitalID= Integer.valueOf(order.HospitalID);

								LocalAccessor.getInstance(CHK_OrderSure.this).updateUser(user);

									if(array.getJSONObject(0).has("PointAddCode")) {
										if(array.getJSONObject(0).getString("PointAddCode").equals("0")) {

											Intent i = new Intent(CHK_OrderSure.this, DialogSure.class);
											i.putExtra("content", array.getJSONObject(0).getString("PointAddContent") + "+" + array.getJSONObject(0).getString("PointValue"));
											i.putExtra("Order", "");
											startActivity(i);
										}
										else{
											Intent i1=new Intent(CHK_OrderSure.this,CHK_OrderDetail.class);
											i1.putExtra("Order", "");
											startActivity(i1);
										}
									}else{
										Intent i1=new Intent(CHK_OrderSure.this,CHK_OrderDetail.class);
										i1.putExtra("Order", "");
										startActivity(i1);
									}



							
							}
							else
							{
								Toast.makeText(
										CHK_OrderSure.this,
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
					CHK_OrderSure.this, true, doProcess);
			Map<String, Object> paramMap = new HashMap<String, Object>();
			String str = "<Request><UserID>%s</UserID><OrderStatus>%s</OrderStatus><PayType>%s</PayType><PayStatus>%s</PayStatus><HospitalID>%s</HospitalID><HospitalName>%s</HospitalName><DeptRowID>%s</DeptRowID><DeptDesc>%s</DeptDesc><DoctorID>%s</DoctorID><DoctorName>%s</DoctorName><Gotime>%s</Gotime><BeginTime>%s</BeginTime><EndTime>%s</EndTime><RegisterMoney>%s</RegisterMoney><CHKID>%s</CHKID><CHKType>%s</CHKType><YunWeek>%s</YunWeek><UserMobile>%s</UserMobile><RealName>%s</RealName><UserNO>%s</UserNO><DoctorNO>%s</DoctorNO><DeptCode>%s</DeptCode><DeptName>%s</DeptName><SchemaID>%s</SchemaID><Items>%s</Items><PhoneType>%s</PhoneType><MedicalCard>%s</MedicalCard></Request>";

			Double gf = 0.0;
			Double chkf = 0.0;
			if (!order.GuaFee.equals(""))
			{
				gf = Double.valueOf(order.GuaFee);
			}
			if (!order.CHKFee.equals(""))
			{
				chkf = Double.valueOf(order.CHKFee);
			}
			
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
			String fee = String.valueOf(gf + chkf);
			str = String.format(str, new Object[]
			{ String.valueOf(user.UserID), orderStatus, "01", "01", order.HospitalID,
					order.HospitalName, "", "", order.DoctorID,
					order.DoctorName, order.DoctorTime, order.TimeStart,
					order.TimeEnd, fee, String.valueOf(order.CHKTypeID),
					order.CHKTypeName, YunWeek, user.UserMobile, user.RealName,
					user.UserNO, order.DoctorNO, order.DeptCode,
					order.DeptName, order.SchemaID, item,"0" ,cardNo});

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
}
