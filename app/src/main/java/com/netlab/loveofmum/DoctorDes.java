package com.netlab.loveofmum;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.netlab.loveofmum.activity.LoginActivity;
import com.netlab.loveofmum.alipay.PayActivity;
import com.netlab.loveofmum.api.BaseActivity;
import com.netlab.loveofmum.api.JsonAsyncTaskOnComplete;
import com.netlab.loveofmum.api.JsonAsyncTask_Info;
import com.netlab.loveofmum.api.LocalAccessor;
import com.netlab.loveofmum.api.MMloveConstants;
import com.netlab.loveofmum.community.CHK_medical_community;
import com.netlab.loveofmum.huanxin.FaceConversionUtil;
import com.netlab.loveofmum.huanxin.MainChatActivity;
import com.netlab.loveofmum.model.User;
import com.netlab.loveofmum.utils.ImageOptions;
import com.netlab.loveofmum.utils.SystemStatusManager;
import com.netlab.loveofmum.widget.CircleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;

public class DoctorDes extends BaseActivity implements OnClickListener {
	private static final int VIDEO_CONTENT_DESC_MAX_LINE = 3;// 默认展示最大行数3行
	private static final int SHOW_CONTENT_NONE_STATE = 0;// 扩充
	private static final int SHRINK_UP_STATE = 1;// 收起状态
	private static final int SPREAD_STATE = 2;// 展开状态
	private static int mState = SHRINK_UP_STATE;// 默认收起状态
	private Intent mIntnet;
	private static long lastClickTime;
	private TextView mContentText;// 展示文本内容
	private RelativeLayout mShowMore;// 展示更多
	private ImageView mImageSpread;// 展开
	private ImageView mImageShrinkUp;// 收起
	private CircleImageView cover_user_photo;// 收起
	private ImageView imgBack;
	private TextView txtHead, btn2,textfans;
	private TextView text1, text2, text3, text5, text6;
	String HospitalID;
	String DoctorID, AskCount, UserCount, AskByOne;
	private User user;
	private Boolean isFav = false;
	int status;
	private ImageLoader mImageLoader;
//	private ImageView isLine;
	private TextView btnguanzhu;
	private Boolean isChat = false;
	// 咨询订单编号
	private String AskOrderID;
	private String DoctorName, HospitalName;
	// 定义Web Service相关的字符串
	private final String SOAP_NAMESPACE = MMloveConstants.URL001;
	private final String SOAP_ACTION = MMloveConstants.URL001
			+ MMloveConstants.DoctorInquiry;
	private final String SOAP_METHODNAME = MMloveConstants.DoctorInquiry;
	private final String SOAP_URL = MMloveConstants.URL002;

	private final String SOAP_ACTION2 = MMloveConstants.URL001
			+ MMloveConstants.FavorDoctorInsert;
	private final String SOAP_METHODNAME2 = MMloveConstants.FavorDoctorInsert;

	private final String SOAP_ACTION3 = MMloveConstants.URL001
			+ MMloveConstants.FavorDoctorDelete;
	private final String SOAP_METHODNAME3 = MMloveConstants.FavorDoctorDelete;
	private final String SOAP_ACTIONADDORDER = MMloveConstants.URL001
			+ MMloveConstants.AskOrderInsert;
	private final String SOAP_METHODNAMEADDORDER = MMloveConstants.AskOrderInsert;

	private final String SOAP_ACTIONORDERINQUIRY = MMloveConstants.URL001
			+ MMloveConstants.AskOrderInquiry;
	private final String SOAP_METHODNAMEORDERINQUIRY = MMloveConstants.AskOrderInquiry;

	private List<Map<String, Object>> arrayList = new ArrayList<Map<String, Object>>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setTranslucentStatus();
		setContentView(R.layout.activity_docdes);

		iniView();
		setListeners();
		mImageLoader = ImageLoader.getInstance();

		mIntnet = this.getIntent();
		if (mIntnet != null) {
			HospitalID = mIntnet.getStringExtra("HospitalID");
			DoctorID = mIntnet.getStringExtra("DoctorID");
			AskCount = mIntnet.getStringExtra("AskCount");
			UserCount = mIntnet.getStringExtra("UserCount");
			AskByOne = mIntnet.getStringExtra("AskByOne");

			text5.setText(UserCount);
			text6.setText(AskCount);
			btn2.setText( AskByOne + "元/次");
		}
		new Thread(new Runnable() {
			@Override
			public void run() {
				FaceConversionUtil.getInstace().getFileText(getApplication());
			}
		}).start();
		 initData();
	}

	public void goChat(View v) {
		if (!hasInternetConnected()) {
			Toast.makeText(DoctorDes.this, "网络数据获取失败，请检查网络设置", 1).show();
			return;
		}
		if (isFastDoubleClick()) {
			return;
		} else {
			if (!isChat) {
				if (user.UserID != 0) {
					isChat = true;
					AskOrderInquiry();

					// AskOrderInsert();
					// startActivity(new Intent(DoctorDes.this,
					// PayActivity.class));
				} else {

					startActivity(new Intent(DoctorDes.this, LoginActivity.class));
				}
			}
		}

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPause(this);
	}

	private void AskOrderInquiry() {
		// TODO Auto-generated method stub

		// 必须是这5个参数，而且得按照顺序
		JsonAsyncTaskOnComplete doProcess = new JsonAsyncTaskOnComplete() {

			@Override
			public void processJsonObject(Object result) {
				// TODO Auto-generated method stub
				String returnvalue = result.toString();
				try {
					// {"AskOrderInquiry":[{"MessageContent":"数据为空！","MessageCode":"2"}]}

					JSONObject mySO = (JSONObject) result;
					JSONArray array = mySO.getJSONArray("AskOrderInquiry");
					if (array.getJSONObject(0).has("MessageCode")) {
						// if()
						if (Integer.valueOf(AskByOne) == 0) {
							AskOrderInsert("02");
						} else {
							AskOrderInsert("01");
						}

					} else {
						isChat = false;
						status = Integer.valueOf(array.getJSONObject(0)
								.getString("OrderStatus").toString());
						AskOrderID = array.getJSONObject(0)
								.getString("AskOrderID").toString();
						if (status == 2 || status == 3) {

							Intent i = new Intent(DoctorDes.this,
									MainChatActivity.class);
							i.putExtra("AskOrderID", AskOrderID);
							i.putExtra("DoctorId", DoctorID);
							i.putExtra("AskOrderID", AskOrderID);
							i.putExtra("DoctorName", DoctorName);
							i.putExtra("DoctorImageUrl",
									arrayList.get(0).get("ImageURL").toString());
							i.putExtra("HospitalID", HospitalID);
							i.putExtra("Hosname",
									arrayList.get(0).get("HospitalName")
											.toString());
							i.putExtra("Status", status);
							i.putExtra("Title", arrayList.get(0).get("Title")
									.toString());
							startActivity(i);
						} else if (status == 1) {
							if (Integer.valueOf(AskByOne) == 0) {
								Intent i = new Intent(DoctorDes.this,
										MainChatActivity.class);
								i.putExtra("AskOrderID", AskOrderID);
								i.putExtra("DoctorId", DoctorID);
								i.putExtra("AskOrderID", AskOrderID);
								i.putExtra("DoctorName", DoctorName);
								i.putExtra("DoctorImageUrl", arrayList.get(0)
										.get("ImageURL").toString());
								i.putExtra("HospitalID", HospitalID);
								i.putExtra("Hosname",
										arrayList.get(0).get("HospitalName")
												.toString());
								i.putExtra("Status", status);
								i.putExtra("Title",
										arrayList.get(0).get("Title")
												.toString());
								startActivity(i);
							} else {
								Intent i = new Intent(DoctorDes.this,
										PayActivity.class);
								i.putExtra("AskOrderID", AskOrderID);
								i.putExtra("DoctorId", DoctorID);
								i.putExtra("HospitalID", HospitalID);
								i.putExtra("DoctorName", DoctorName);
								i.putExtra("AskByOne", AskByOne);
								i.putExtra("DoctorImageUrl", arrayList.get(0)
										.get("ImageURL").toString());
								i.putExtra("Hosname",
										arrayList.get(0).get("HospitalName")
												.toString());
								i.putExtra("Title",
										arrayList.get(0).get("Title")
												.toString());
								startActivity(i);
							}

						} else if (status == 4) {
							if (Integer.valueOf(AskByOne) == 0) {
								AskOrderInsert("02");
							} else {
								AskOrderInsert("01");
							}
						}
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();

				}

			}
		};

		JsonAsyncTask_Info task = new JsonAsyncTask_Info(DoctorDes.this, true,
				doProcess);
		Map<String, Object> paramMap = new HashMap<String, Object>();
		String str = "<Request><UserID>%s</UserID><Reason>%s</Reason><PayType>%s</PayType><PayStatus>%s</PayStatus><CustomerIP>%s</CustomerIP><HospitalID>%s</HospitalID><HospitalName>%s</HospitalName><DeptRowID>%s</DeptRowID><DeptDesc>%s</DeptDesc>"
				+ "<DoctorID>%s</DoctorID><DoctorName>%s</DoctorName><RealName>%s</RealName><UserNO>%s</UserNO><UserMobile>%s</UserMobile><AskOrderID>%s</AskOrderID></Request>";

		str = String.format(str, new Object[] { user.UserID + "", "", "", "",
				"", HospitalID,
				arrayList.get(0).get("HospitalName").toString(), "", "",
				DoctorID, arrayList.get(0).get("DoctorName").toString(),
				user.RealName, user.UserNO, user.UserMobile, "" });

		paramMap.put("str", str);
		task.execute(SOAP_NAMESPACE, SOAP_ACTIONORDERINQUIRY,
				SOAP_METHODNAMEORDERINQUIRY, SOAP_URL, paramMap);
	}
	/**
	 * 
	 * @return
	 */

	public static boolean isFastDoubleClick() {
		long time = System.currentTimeMillis();
		long timeD = time - lastClickTime;
		if (0 < timeD && timeD < 1000) {
			return true;
		}
		lastClickTime = time;
		return false;
	}

	private void AskOrderInsert(String OrderStatus) {
		// TODO Auto-generated method stub
		JsonAsyncTaskOnComplete doProcess = new JsonAsyncTaskOnComplete() {

			@Override
			public void processJsonObject(Object result) {
				// TODO Auto-generated method stub
				String returnvalue = result.toString();
				// {"AskOrderInsert":[{"OrderID":"20151212103433"}]}
				try {
					JSONObject mySO = (JSONObject) result;
					JSONArray array = mySO.getJSONArray("AskOrderInsert");
					AskOrderID = array.getJSONObject(0).getString("AskOrderID");
					isChat = false;
					if (AskByOne == null || "".equals(AskByOne)
							|| "0".equals(AskByOne)) {

						Intent i = new Intent(DoctorDes.this,
								MainChatActivity.class);
						i.putExtra("AskOrderID", AskOrderID);
						i.putExtra("DoctorId", DoctorID);
						i.putExtra("HospitalID", HospitalID);
						i.putExtra("DoctorName", DoctorName);
						i.putExtra("DoctorImageUrl",
								arrayList.get(0).get("ImageURL").toString());
						i.putExtra("AskByOne", AskByOne);
						i.putExtra("Hosname",
								arrayList.get(0).get("HospitalName").toString());
						i.putExtra("Status", 2);
						i.putExtra("Title", arrayList.get(0).get("Title")
								.toString());
						startActivity(i);

					} else {
						Intent i = new Intent(DoctorDes.this, PayActivity.class);
						i.putExtra("AskOrderID", AskOrderID);
						i.putExtra("DoctorId", DoctorID);
						i.putExtra("HospitalID", HospitalID);
						i.putExtra("DoctorName", DoctorName);
						i.putExtra("AskByOne", AskByOne);
						i.putExtra("Hosname",
								arrayList.get(0).get("HospitalName").toString());
						i.putExtra("DoctorImageUrl",
								arrayList.get(0).get("ImageURL").toString());
						i.putExtra("Title", arrayList.get(0).get("Title")
								.toString());
						startActivity(i);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		};
		JsonAsyncTask_Info task = new JsonAsyncTask_Info(DoctorDes.this, true,
				doProcess);
		Map<String, Object> paramMap = new HashMap<String, Object>();

		String str = "<Request><UserID>%s</UserID><Reason>%s</Reason><PayType>%s</PayType><PayStatus>%s</PayStatus><CustomerIP>%s</CustomerIP><HospitalID>%s</HospitalID><HospitalName>%s</HospitalName><DeptRowID>%s</DeptRowID><DeptDesc>%s</DeptDesc>"
				+ "<DoctorID>%s</DoctorID><DoctorName>%s</DoctorName><RealName>%s</RealName><UserNO>%s</UserNO><UserMobile>%s</UserMobile><Fromto>%s</Fromto><OrderStatus>%s</OrderStatus></Request>";

		str = String
				.format(str, new Object[] { user.UserID + "", "", "", "", "",
						HospitalID,
						arrayList.get(0).get("HospitalName").toString(), "",
						"", DoctorID,
						arrayList.get(0).get("DoctorName").toString(),
						user.RealName, user.UserNO, user.UserMobile, "5",
						OrderStatus });

		paramMap.put("str", str);

		// 必须是这5个参数，而且得按照顺序
		task.execute(SOAP_NAMESPACE, SOAP_ACTIONADDORDER,
				SOAP_METHODNAMEADDORDER, SOAP_URL, paramMap);
	}

	private void setListeners() {
		// TODO Auto-generated method stub
		imgBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
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
		tintManager.setStatusBarTintResource(R.drawable.bg_header);// 状态栏无背景
	}

	@Override
	protected void iniView() {
		// TODO Auto-generated method stub
		txtHead = (TextView) findViewById(R.id.txtHead);
		imgBack = (ImageView) findViewById(R.id.img_left);
		txtHead.setText("私人医生");
		mContentText = (TextView) findViewById(R.id.text_content);
		text1 = (TextView) findViewById(R.id.text1);
		text2 = (TextView) findViewById(R.id.text2);
		text5 = (TextView) findViewById(R.id.text5);
		text6 = (TextView) findViewById(R.id.text6);
		// btnguanzhu=(TextView) findViewById(R.id.btnguanzhu);
		// btnguanzhu.setOnClickListener(this);
		text3 = (TextView) findViewById(R.id.texthname);
//		isLine = (ImageView) findViewById(R.id.iv_isonline);
		mShowMore = (RelativeLayout) findViewById(R.id.show_more);
		mImageSpread = (ImageView) findViewById(R.id.spread);
		mImageShrinkUp = (ImageView) findViewById(R.id.shrink_up);
		cover_user_photo = (CircleImageView) findViewById(R.id.cover_user_photo);
		btn2 = (TextView) findViewById(R.id.tvchat);
		textfans=(TextView)findViewById(R.id.textfans);

		mShowMore.setOnClickListener(this);
	}

	private void initData() {
		// mContentText.setText(R.string.txt_info);

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onResume(this);
		user = LocalAccessor.getInstance(DoctorDes.this).getUser();
		if (!hasInternetConnected()) {
			Toast.makeText(DoctorDes.this, "网络数据获取失败，请检查网络设置", 1).show();
			return;
		}
		searchDocDes();

	}

	/**
	 * 查询医生详情
	 */
	private void searchDocDes() {
		JsonAsyncTaskOnComplete doProcess = new JsonAsyncTaskOnComplete() {

			@Override
			public void processJsonObject(Object result) {
				// TODO Auto-generated method stub

				String returnvalue = result.toString();
				if (arrayList.size() > 0) {
					arrayList.clear();
				}
				if (returnvalue != null) {
					Map<String, Object> map;
					try {
						JSONObject mySO = (JSONObject) result;

						JSONArray array = mySO.getJSONArray("DoctorInquiry");

						map = new HashMap<String, Object>();
						map.put("Description", array.getJSONObject(0)
								.getString("Description"));
						map.put("AskCount",
								array.getJSONObject(0).getString("AskCount"));
						map.put("UserCount",
								array.getJSONObject(0).getString("UserCount"));

						map.put("IsLine",
								array.getJSONObject(0).getString("IsLine"));

						map.put("DoctorType",
								array.getJSONObject(0).getString("DoctorType"));

						map.put("DoctorName",
								array.getJSONObject(0).getString("DoctorName"));
						map.put("Title",
								array.getJSONObject(0).getString("Title"));

						map.put("HospitalName", array.getJSONObject(0)
								.getString("HospitalName"));
						map.put("DepartNO",
								array.getJSONObject(0).getString("DepartNO"));
						map.put("AskPrice",
								array.getJSONObject(0).getString("AskPrice"));
						map.put("IsFavored",
								array.getJSONObject(0).getString("IsFavored"));
						map.put("HospitalID",
								array.getJSONObject(0).getString("HospitalID"));
						map.put("Price",
								array.getJSONObject(0).getString("Price"));
						map.put("AskPriceByOne", array.getJSONObject(0)
								.getString("AskPriceByOne"));
						map.put("DoctorID",
								array.getJSONObject(0).getString("DoctorID"));
						map.put("DoctorNO",
								array.getJSONObject(0).getString("DoctorNO"));
						map.put("Title",
								array.getJSONObject(0).getString("Title"));
						map.put("DoctorFavors",
								array.getJSONObject(0).getString("DoctorFavors"));


						map.put("ImageURL", MMloveConstants.JE_BASE_URL2
								+ array.getJSONObject(0).getString("ImageURL")
										.replace("~", "").replace("\\", "/"));
						AskByOne = array.getJSONObject(0).getString(
								"AskPriceByOne");
						DoctorName = array.getJSONObject(0).getString(
								"DoctorName");
						AskCount = array.getJSONObject(0).getString("AskCount");
						UserCount = array.getJSONObject(0).getString(
								"UserCount");
						arrayList.add(map);

						text1.setText(array.getJSONObject(0).getString(
								"DoctorName"));
						text2.setText(array.getJSONObject(0).getString("Title"));
						text3.setText(array.getJSONObject(0).getString(
								"HospitalName"));
						textfans.setText(array.getJSONObject(0).getString("DoctorFavors"));
//						if (Integer.valueOf(arrayList.get(0).get("IsLine")
//								.toString()) != 1) {
//							.setImageResource(R.drawable.icon_online);
//						} else {
//							isLine.setImageResource(R.drawable.icon_offline);
//						}
						text5.setText(UserCount);
						text6.setText(AskCount);
						btn2.setText( AskByOne + "元/次");
						// if(Integer.valueOf(arrayList.get(0).get("IsFavored").toString())>0){
						// btnguanzhu.setText("取消关注");
						//
						// }else{
						// btnguanzhu.setText("关注");
						// }
//						if (mState == SHRINK_UP_STATE
//								&& arrayList.get(0).get("Description")
//										.toString().length() > 20) {
//
//							mContentText.setText(("简介:"+arrayList.get(0)
//									.get("Description").toString()
//									.substring(0, 25) + "..."));
//						} else {
//							mContentText.setText("简介:"+arrayList.get(0)
//									.get("Description").toString());
//						}
						mContentText.setText("简介:"+arrayList.get(0)
									.get("Description").toString());
						mContentText.post(new Runnable() {
							@Override
							public void run() {
								if(mContentText.getLineCount()>3){
									mContentText.setMaxLines(3);
									mImageShrinkUp.setVisibility(View.GONE);
									mImageSpread.setVisibility(View.VISIBLE);
								}else{
									mImageShrinkUp.setVisibility(View.GONE);
									mImageSpread.setVisibility(View.GONE);
								}
							}
						});
						mImageLoader
								.displayImage(arrayList.get(0).get("ImageURL")
										.toString(), cover_user_photo,
										ImageOptions.getFaceOptions());
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			}
		};
		// TODO Auto-generated method stub
		JsonAsyncTask_Info task = new JsonAsyncTask_Info(DoctorDes.this, true,
				doProcess);
		Map<String, Object> paramMap = new HashMap<String, Object>();

		String str = "<Request><DoctorID>%s</DoctorID><UserID>%s</UserID><HospitalID>%s</HospitalID><IsPMD>%s</IsPMD></Request>";
		if (user.UserID != 0) {
			str = String.format(str, new Object[] { DoctorID, user.UserID + "",
					HospitalID, "1" });
		} else {
			str = String.format(str, new Object[] { DoctorID, "", HospitalID,
					"1" });
		}

		paramMap.put("str", str);

		// 必须是这5个参数，而且得按照顺序
		task.execute(SOAP_NAMESPACE, SOAP_ACTION, SOAP_METHODNAME, SOAP_URL,
				paramMap);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.show_more: {
			if (mState == SPREAD_STATE) {
				mContentText.setText("简介:"+arrayList.get(0).get("Description")
							.toString());
				mContentText.setMaxLines(VIDEO_CONTENT_DESC_MAX_LINE);
				mContentText.post(new Runnable() {
					@Override
					public void run() {
						if(mContentText.getLineCount()>3){
							mContentText.setMaxLines(3);
						}
					}
				});
//				if (arrayList.get(0).get("Description").toString().length() > 20) {
//
//					mContentText.setText((arrayList.get(0).get("Description")
//							.toString().substring(0, 25) + "..."));
//				} else {
//					mContentText.setText(arrayList.get(0).get("Description")
//							.toString());
//				}

				mContentText.requestLayout();
				mImageShrinkUp.setVisibility(View.GONE);
				mImageSpread.setVisibility(View.VISIBLE);

				mState = SHRINK_UP_STATE;
			} else if (mState == SHRINK_UP_STATE) {

				mContentText.setMaxLines(Integer.MAX_VALUE);
				mContentText.requestLayout();
				mContentText.setText("简介:"+arrayList.get(0).get("Description")
						.toString());
				mContentText.setMaxLines(100);
				mImageShrinkUp.setVisibility(View.VISIBLE);
				mImageSpread.setVisibility(View.GONE);

				mState = SPREAD_STATE;
			}
			break;
		}
		// case R.id.btnguanzhu:
		// if(user.UserID==0){
		// startActivity(new Intent(DoctorDes.this, User_Login.class));
		// }else if(btnguanzhu.getText().toString().trim().equals("取消关注")){
		// if (!hasInternetConnected()){
		// Toast.makeText(DoctorDes.this, "网络数据获取失败，请检查网络设置", 1).show();
		// return;
		// }
		// if(!isFav){
		//
		// FavorDel();
		// }else{
		// Toast.makeText(DoctorDes.this, "请不要重复关注", 1).show();
		// }
		// }else if(btnguanzhu.getText().toString().trim().equals("关注")){
		// if (!hasInternetConnected()){
		// Toast.makeText(DoctorDes.this, "网络数据获取失败，请检查网络设置", 1).show();
		// return;
		// }
		// if(!isFav){
		// FavorDoctor();
		// }else{
		// Toast.makeText(DoctorDes.this, "请不要重复关注", 1).show();
		// }
		// }
		// break;
		default:
			break;

		}

	}

	private void FavorDel() {
		// TODO Auto-generated method stub
		JsonAsyncTaskOnComplete doProcess = new JsonAsyncTaskOnComplete() {

			@Override
			public void processJsonObject(Object result) {
				// TODO Auto-generated method stub
				String returnvalue = result.toString();
				JSONObject mySO = (JSONObject) result;
				try {
					JSONArray array = mySO.getJSONArray("FavorDoctorDelete");
					if ("0".equals(array.getJSONObject(0).getString(
							"MessageCode"))) {
						Toast.makeText(DoctorDes.this, "取消关注成功！", 1).show();
						btnguanzhu.setText("关注");
					} else if ("1".equals(array.getJSONObject(0).getString(
							"MessageCode"))) {
						Toast.makeText(
								DoctorDes.this,
								array.getJSONObject(0).getString(
										"MessageContent"), 1).show();
					}
					isFav = false;

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					isFav = false;
				}
				// {"FavorDoctorInsert":[{"MessageContent":"成功！","MessageCode":"0"}]}

			}
		};
		JsonAsyncTask_Info task = new JsonAsyncTask_Info(DoctorDes.this, true,
				doProcess);
		isFav = true;
		String userId = user.UserID + "";
		Map<String, Object> paramMap = new HashMap<String, Object>();
		String str = "<Request><UserID>%s</UserID><DoctorID>%s</DoctorID></Request>";

		str = String.format(str, new Object[] { userId, DoctorID });

		paramMap.put("str", str);

		// 必须是这5个参数，而且得按照顺序
		task.execute(SOAP_NAMESPACE, SOAP_ACTION3, SOAP_METHODNAME3, SOAP_URL,
				paramMap);
	}

	private void FavorDoctor() {
		// TODO Auto-generated method stub
		JsonAsyncTaskOnComplete doProcess = new JsonAsyncTaskOnComplete() {

			@Override
			public void processJsonObject(Object result) {
				// TODO Auto-generated method stub
				String returnvalue = result.toString();
				JSONObject mySO = (JSONObject) result;
				try {
					JSONArray array = mySO.getJSONArray("FavorDoctorInsert");
					if ("0".equals(array.getJSONObject(0).getString(
							"MessageCode"))) {
						Toast.makeText(DoctorDes.this, "关注成功！", 1).show();
						btnguanzhu.setText("取消关注");
					} else if ("1".equals(array.getJSONObject(0).getString(
							"MessageCode"))) {
						Toast.makeText(
								DoctorDes.this,
								array.getJSONObject(0).getString(
										"MessageContent"), 1).show();
					}

					isFav = false;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					isFav = false;
				}
				// {"FavorDoctorInsert":[{"MessageContent":"成功！","MessageCode":"0"}]}

			}
		};
		JsonAsyncTask_Info task = new JsonAsyncTask_Info(DoctorDes.this, true,
				doProcess);
		String userId = user.UserID + "";
		Map<String, Object> paramMap = new HashMap<String, Object>();
		String str = "<Request><UserID>%s</UserID><DoctorID>%s</DoctorID></Request>";
		isFav = true;
		str = String.format(str, new Object[] { userId, DoctorID });

		paramMap.put("str", str);

		// 必须是这5个参数，而且得按照顺序
		task.execute(SOAP_NAMESPACE, SOAP_ACTION2, SOAP_METHODNAME2, SOAP_URL,
				paramMap);
	}

}
